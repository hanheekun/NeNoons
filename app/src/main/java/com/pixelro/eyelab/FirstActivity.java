package com.pixelro.eyelab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.pixelro.eyelab.account.AccountActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FirstActivity extends BaseActivity{

    private static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.btn_first_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 카메라 사용 권한 얻기
                int rc = ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.CAMERA);
                if (rc != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FirstActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

                }
                finish();
            }
        });

    }

}
