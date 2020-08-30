package com.pixelro.nenoons.menu.my;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.test.Test04ColorFragment;
import com.pixelro.nenoons.test.TestActivity;

public class MyColorActivity extends AppCompatActivity   implements View.OnClickListener{

    private SharedPreferencesManager mSm;

    private ConstraintLayout ClColor1;
    private ConstraintLayout ClColor2;
    private ConstraintLayout ClColor3;
    private ConstraintLayout ClColor4;
    private ConstraintLayout ClColor5;
    private ConstraintLayout ClColor6;

    private FrameLayout FlColor1;
    private FrameLayout FlColor2;
    private FrameLayout FlColor3;
    private FrameLayout FlColor4;
    private FrameLayout FlColor5;
    private FrameLayout FlColor6;

    private FrameLayout FlColorDark1;
    private FrameLayout FlColorDark2;
    private FrameLayout FlColorDark3;
    private FrameLayout FlColorDark4;
    private FrameLayout FlColorDark5;
    private FrameLayout FlColorDark6;

    private FrameLayout FlColorLight1;
    private FrameLayout FlColorLight2;
    private FrameLayout FlColorLight3;
    private FrameLayout FlColorLight4;
    private FrameLayout FlColorLight5;
    private FrameLayout FlColorLight6;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_color);

        mSm = new SharedPreferencesManager(this);

        findViewById(R.id.button_test_next).setOnClickListener(this);

        ClColor1 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_1);
        ClColor2 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_2);
        ClColor3 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_3);
        ClColor4 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_4);
        ClColor5 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_5);
        ClColor6 = (ConstraintLayout)findViewById(R.id.constraintlayout_color_6);

        FlColor1 = (FrameLayout)findViewById(R.id.frameLayout_color_box_1);
        FlColor2 = (FrameLayout)findViewById(R.id.frameLayout_color_box_2);
        FlColor3 = (FrameLayout)findViewById(R.id.frameLayout_color_box_3);
        FlColor4 = (FrameLayout)findViewById(R.id.frameLayout_color_box_4);
        FlColor5 = (FrameLayout)findViewById(R.id.frameLayout_color_box_5);
        FlColor6 = (FrameLayout)findViewById(R.id.frameLayout_color_box_6);

        FlColorDark1 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_1);
        FlColorDark2 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_2);
        FlColorDark3 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_3);
        FlColorDark4 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_4);
        FlColorDark5 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_5);
        FlColorDark6 = (FrameLayout)findViewById(R.id.frameLayout_color_dark_6);

        FlColorLight1 = (FrameLayout)findViewById(R.id.frameLayout_color_light_1);
        FlColorLight2 = (FrameLayout)findViewById(R.id.frameLayout_color_light_2);
        FlColorLight3 = (FrameLayout)findViewById(R.id.frameLayout_color_light_3);
        FlColorLight4 = (FrameLayout)findViewById(R.id.frameLayout_color_light_4);
        FlColorLight5 = (FrameLayout)findViewById(R.id.frameLayout_color_light_5);
        FlColorLight6 = (FrameLayout)findViewById(R.id.frameLayout_color_light_6);

        ClColor1.setOnClickListener(this);
        ClColor2.setOnClickListener(this);
        ClColor3.setOnClickListener(this);
        ClColor4.setOnClickListener(this);
        ClColor5.setOnClickListener(this);
        ClColor6.setOnClickListener(this);

    }

    private void setAllColorButtonReset(){
        FlColor1.setBackgroundResource(0);
        FlColor2.setBackgroundResource(0);
        FlColor3.setBackgroundResource(0);
        FlColor4.setBackgroundResource(0);
        FlColor5.setBackgroundResource(0);
        FlColor6.setBackgroundResource(0);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_test_next:
                //Toast.makeText(this,"적용예정입니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.constraintlayout_color_1:
                setAllColorButtonReset();
                FlColor1.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight1.getBackground()).getColor());
                break;
            case R.id.constraintlayout_color_2:
                setAllColorButtonReset();
                FlColor2.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight2.getBackground()).getColor());
                break;
            case R.id.constraintlayout_color_3:
                setAllColorButtonReset();
                FlColor3.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight3.getBackground()).getColor());
                break;
            case R.id.constraintlayout_color_4:
                setAllColorButtonReset();
                FlColor4.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight4.getBackground()).getColor());
                break;
            case R.id.constraintlayout_color_5:
                setAllColorButtonReset();
                FlColor5.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight5.getBackground()).getColor());
                break;
            case R.id.constraintlayout_color_6:
                setAllColorButtonReset();
                FlColor6.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setColor(((ColorDrawable) FlColorLight6.getBackground()).getColor());
                break;

        }

        //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("다음을 눌러주세요.");
        ((Button)findViewById(R.id.button_test_next)).setEnabled(true);
    }
}
