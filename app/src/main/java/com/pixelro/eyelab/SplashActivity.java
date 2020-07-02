package com.pixelro.eyelab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.pixelro.eyelab.account.AccountActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity{

    private String masterKeyAlias;
    private SharedPreferences sharedPreferences;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    private Date currentTime;

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
        version.setText("ver"+versionName);

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
        sharedPreferences = getSharedPreferences(EYELAB.APPDATA.NAME_LOGIN,MODE_PRIVATE);
        if (sharedPreferences.getBoolean(EYELAB.APPDATA.LOGIN.FIRST_LOGIN,true)){
            // 권한 받기
            Intent mainIntent = new Intent(SplashActivity.this, FirstActivity.class);
            startActivityForResult(mainIntent,0);
        }
        else {

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    // 자동 로그인 가능 한지 확인 한다
                    if (sharedPreferences.getBoolean(EYELAB.APPDATA.LOGIN.LOGINNING,false)){ // 자동 로그인 가능하면 메뉴로 바로 이동
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                    }
                    else { // 자동 로그인 아닐 경우 로그인, 회원 가입 페이지 이동
                        Intent mainIntent = new Intent(SplashActivity.this, AccountActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                    }
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


        currentTime = Calendar.getInstance().getTime();

        int a = 0;

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
