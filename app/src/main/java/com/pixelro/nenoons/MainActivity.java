package com.pixelro.nenoons;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.account.AccountIDFragment;
import com.pixelro.nenoons.menu.exercise.ExerciseFragment;
import com.pixelro.nenoons.menu.home.HomeFragment;
import com.pixelro.nenoons.menu.my.MyFragment;
import com.pixelro.nenoons.server.HttpTask;
import com.pixelro.nenoons.test.TestActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.zip.CheckedInputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private long mBackKeyPressedTime = 0;
    private SharedPreferences sharedPreferences;

    private FragmentManager fragmentManager;
    private Fragment fa, fb, fc;

    public HomeWebLoadingDialog mHomeWebLoadingDialog;

    // gps
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                //R.id.navigation_home, R.id.navigation_care, R.id.navigation_my)
//                R.id.navigation_home, R.id.navigation_my)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_test_fragment);
//        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // 처음 메인 페이지 오면 노안 측정 dialog 출력
        sharedPreferences = getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(EYELAB.APPDATA.ACCOUNT.FIRST_LOGIN, true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(EYELAB.APPDATA.ACCOUNT.FIRST_LOGIN, false);
            editor.commit();

            FirstDialog dlg = new FirstDialog(this);
            dlg.setOnResultEventListener(new FirstDialog.OnResultEventListener() {
                @Override
                public void ResultEvent(boolean result) {
                    if (result) {
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                    }
                }
            });
            dlg.showDialog();
        }

        fa = new HomeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_test_fragment, fa).commit();

        if (!MainActivity.this.checkForPermission()) {
            Log.i("MainActivity", "The user may not allow the access to apps usage. ");
            //Toast.makeText((Context)SplashActivity.this, (CharSequence)"Failed to retrieve app usage statistics. You may need to enable access for this app through Settings > Security > Apps with usage access", 1).show();
            MainActivity.this.startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
        }

        //loading 동안 progress
        mHomeWebLoadingDialog = new HomeWebLoadingDialog(this);
        mHomeWebLoadingDialog.showDialog();

        //loading 동안 progress 1초뒤 종료
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mHomeWebLoadingDialog.dismiss();

            }
        }, 4000);


        /////////////////////////////////////////////////////
        // FCM 설정
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // 서버연결 20200720

                        // FCM 토큰 가지고 옴
                        // Get new Instance ID token
                        String fcmToken = task.getResult().getToken();
                        String token = getToken(MainActivity.this);
                        String fcmTokenLocal = getFcmToken(MainActivity.this);
                        //FCM 토큰 전송
                        if (fcmToken!=null && !fcmToken.equals("")  && token!=null && token.equals("")) {
                            if (fcmTokenLocal == "" || !fcmToken.equals(fcmTokenLocal)) {
                                // 신규 fcmToken 서버에 저장
                                HashMap<String, String> param = new HashMap<String, String>();
                                // 파라메터는 넣기 예
                                param.put("token", token);    //PARAM
                                param.put("fcmToken", fcmToken);    // 서버연결 20200716 이름 추가 필요
                                Handler handler = new Handler(message -> {
                                    Bundle bundle = message.getData();
                                    String result = bundle.getString("result");
                                    System.out.println(result);
                                    try {
                                        JSONObject j = new JSONObject(result);
                                        String error = j.getString("error");
                                        String msg = j.getString("msg");
                                        System.out.println(error);
                                        System.out.println(msg);
                                        if (error != "null") {
                                            //Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                            System.out.println("저장 실패");
                                        }
                                        else if (msg != "null") {
                                            //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                            System.out.println("저장 성공");
                                            setFcmToken(MainActivity.this,fcmToken);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        // 실패
                                        //Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                });
                                // API 주소와 위 핸들러 전달 후 실행.
//                                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_fcm", handler).execute(param);
                new HttpTask("http://192.168.1.162:4002/android/update_user_fcm", handler).execute(param);

                            }
                        }


                        // Log and toast

                        Log.d(TAG, ">>>> fcm token : " + fcmToken);
//                        Toast.makeText(MainActivity.this, tokenFCM, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();

        // TODO 노티피케이션으로부터 들어온 경우 향후 동작 체크 , 아직 작동 안함.
        // 서버에서 title, message, link, action, value 항목으로 노티가 날아옴
        // 아래 내용에 따라 띄우는 화면 분기할 예정

        Intent intent = getIntent();
        if (intent.hasExtra("link")) {
            String link = intent.getStringExtra("link");
            String action = intent.getStringExtra("action");
            String value = intent.getStringExtra("value");
            System.out.println(" link=" + link + ",action=" + action + " , value="+ value);
            Toast.makeText(this, "노티에서 호출 : " + link + " " + action + " " + value,Toast.LENGTH_LONG).show();
        }

        // gps
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

    }

    boolean checkForPermission() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        // 두번 눌러 종료 추가
        if (System.currentTimeMillis() > mBackKeyPressedTime + 2000) {
            mBackKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        (new Handler()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finishAffinity();
                    }
                }
                , 200);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                if (fa == null) {
                    fa = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fa).commit();
                }

                if (fa != null) fragmentManager.beginTransaction().show(fa).commit();
                if (fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                if (fc != null) fragmentManager.beginTransaction().hide(fc).commit();

                ((HomeFragment) fa).refrashBoard();

                return true;
            case R.id.navigation_exercise:
                if (fb == null) {
                    fb = new ExerciseFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fb).commit();
                }

                if (fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                if (fb != null) fragmentManager.beginTransaction().show(fb).commit();
                if (fc != null) fragmentManager.beginTransaction().hide(fc).commit();
                return true;
            case R.id.navigation_my:
                if (fc == null) {
                    fc = new MyFragment();
                    fragmentManager.beginTransaction().add(R.id.nav_test_fragment, fc).commit();
                }

                if (fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                if (fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                if (fc != null) fragmentManager.beginTransaction().show(fc).commit();
                return true;
        }

        return false;
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN, "");
    }
    public String getFcmToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.FCM_TOKEN, "");
    }
    public void setFcmToken(Context context, String fcmToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.FCM_TOKEN, fcmToken);
        editor.commit();
    }
}
