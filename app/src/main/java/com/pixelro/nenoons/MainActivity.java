package com.pixelro.nenoons;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import com.pixelro.nenoons.test.TestActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private long mBackKeyPressedTime = 0;
    private SharedPreferences sharedPreferences;

    private FragmentManager fragmentManager;
    private Fragment fa, fb, fc;

    public ProgressDialog mLoginHomeLoadingProgressDialog;

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
        mLoginHomeLoadingProgressDialog = ProgressDialog.show(this, "", "불러오는 중...", true, true);

        //loading 동안 progress 1초뒤 종료
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mLoginHomeLoadingProgressDialog.dismiss();

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
                        String tokenFCM = task.getResult().getToken();
                        String token = getToken(MainActivity.this);
                        //FCM 토큰 전송


                        // Log and toast

                        Log.d(TAG, ">>>> fcm token : " + tokenFCM);
                        Toast.makeText(MainActivity.this, tokenFCM, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();



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

    public String getToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN, "");
    }
}
