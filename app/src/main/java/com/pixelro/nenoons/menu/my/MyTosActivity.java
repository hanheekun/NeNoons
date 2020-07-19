package com.pixelro.nenoons.menu.my;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.TestProfile;

public class MyTosActivity extends AppCompatActivity{


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tos);

        findViewById(R.id.button_arrow_back_background).setOnClickListener(v -> finish());

    }

}
