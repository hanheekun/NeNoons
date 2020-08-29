package com.pixelro.nenoons.menu.my;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.R;

public class MyTosActivity extends AppCompatActivity{


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tos);

        findViewById(R.id.button_arrow_close_background).setOnClickListener(v -> finish());

    }

}
