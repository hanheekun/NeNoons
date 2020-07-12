package com.pixelro.nenoons;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.JWT;
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.account.AccountLoginFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity{

    private final static String TAG = SplashActivity.class.getSimpleName();

    private String masterKeyAlias;
    private SharedPreferences sharedPreferences;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = pi.versionName;

        TextView version = (TextView) findViewById(R.id.textView_splash_ver);
        version.setText("ver "+versionName);

//        // 로그인 정보 저장
//        try {
//            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            sharedPreferences = EncryptedSharedPreferences.create(
//                    "secret_shared_prefs",
//                    masterKeyAlias,
//                    this,
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            );
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        sharedPreferences = getSharedPreferences(EYELAB.APPDATA.NAME_LOGIN, MODE_PRIVATE);

        // 처음 엡을 실행 하였을 경우 권한 받기 실행
        sharedPreferences = getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT,MODE_PRIVATE);
        if (sharedPreferences.getBoolean(EYELAB.APPDATA.ACCOUNT.FIRST_RUN,true)){
            // 권한 받기
            Intent mainIntent = new Intent(SplashActivity.this, FirstActivity.class);
            startActivityForResult(mainIntent,0);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(EYELAB.APPDATA.ACCOUNT.FIRST_RUN, false);
            editor.commit();

        }
        else {

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    // token 존재, 종료 남은 시간 따라 자동 로그인 결정
                    String token = sharedPreferences.getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");

                    Log.i(TAG, "token = " + token);

                    if (token != ""){

                        JWT jwt = new JWT(token);

                        // token 을 확인해서 1시간 남지 않았으면 로그인 정보를 지우고 재로그인 한다
                        if (jwt.isExpired(3600)){ // jwt.isExpired 확인 필요, return false

                            // token 정보 삭재
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(EYELAB.APPDATA.ACCOUNT.FIRST_LOGIN);
                            editor.remove(EYELAB.APPDATA.ACCOUNT.TOKEN);
                            editor.commit();

                            // 로그인, 회원가입 으로 이동
                            Intent mainIntent = new Intent(SplashActivity.this, AccountActivity.class);
                            SplashActivity.this.startActivity(mainIntent);

                        }
                        else {
                            // 자동 로그인 메인으로 바로 이동
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                        }
                    }
                    else {
                        // 로그인, 회원가입 으로 이동
                        Intent mainIntent = new Intent(SplashActivity.this, AccountActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                    }

                    SplashActivity.this.finish();

                }
            }, AUTO_HIDE_DELAY_MILLIS);
        }

        if (!SplashActivity.this.checkForPermission()) {
            Log.i("MainActivity", "The user may not allow the access to apps usage. ");
            //Toast.makeText((Context)SplashActivity.this, (CharSequence)"Failed to retrieve app usage statistics. You may need to enable access for this app through Settings > Security > Apps with usage access", 1).show();
            SplashActivity.this.startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
        }

    }

    boolean checkForPermission(){
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finishDelay();
    }

    void finishDelay(){
        /* SPLASH_DISPLAY_LENGTH 뒤에 메뉴 액티비티를 실행시키고 종료한다.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this, AccountActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                //SplashActivity.this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                SplashActivity.this.finish();

//                //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
//                SharedPreferences sf = getSharedPreferences(LOGIN,MODE_PRIVATE);
//                //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
//                String isLogined = sf.getString(LOGIN_ING,LOGIN_ING_NO);
//
//                if (isLogined.equals(LOGIN_ING_YES)){
//                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                    SplashActivity.this.startActivity(mainIntent);
//                    SplashActivity.this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                    SplashActivity.this.finish();
//                }
//                else {
//                    Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
//                    SplashActivity.this.startActivity(mainIntent);
//                    SplashActivity.this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                    SplashActivity.this.finish();
//                }

            }
        }, AUTO_HIDE_DELAY_MILLIS);
    }


}
