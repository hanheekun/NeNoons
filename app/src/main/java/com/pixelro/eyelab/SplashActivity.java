package com.pixelro.eyelab;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.pixelro.eyelab.account.AccountActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity{

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
        version.setText("ver"+versionName);

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

//        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesDefine.LOGIN,MODE_PRIVATE);
//        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
//        SharedPreferences.Editor editor = sharedPreferences.edit();

    }

}
