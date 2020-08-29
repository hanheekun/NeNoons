package com.pixelro.nenoons.menu.my;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.test.Test05FontFragment;
import com.pixelro.nenoons.test.TestActivity;

public class MyFontActivity extends AppCompatActivity   implements View.OnClickListener{

    private SharedPreferencesManager mSm;

    private ConstraintLayout Flfont1;
    private ConstraintLayout Flfont2;
    private ConstraintLayout Flfont3;
    private ConstraintLayout Flfont4;

    private FrameLayout FlFont1;
    private FrameLayout FlFont2;
    private FrameLayout FlFont3;
    private FrameLayout FlFont4;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_font);

        mSm = new SharedPreferencesManager(this);

        findViewById(R.id.button_test_next).setOnClickListener(this);

        Flfont1 = (ConstraintLayout)findViewById(R.id.frameLayout_font_1);
        Flfont2 = (ConstraintLayout)findViewById(R.id.frameLayout_font_2);
        Flfont3 = (ConstraintLayout)findViewById(R.id.frameLayout_font_3);
        Flfont4 = (ConstraintLayout)findViewById(R.id.frameLayout_font_4);

        Flfont1.setOnClickListener(this);
        Flfont2.setOnClickListener(this);
        Flfont3.setOnClickListener(this);
        Flfont4.setOnClickListener(this);

//        Flfont1.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
//        Flfont2.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
//        Flfont3.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
//        Flfont4.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);

        FlFont1 = (FrameLayout)findViewById(R.id.frameLayout_font_box_1);
        FlFont2 = (FrameLayout)findViewById(R.id.frameLayout_font_box_2);
        FlFont3 = (FrameLayout)findViewById(R.id.frameLayout_font_box_3);
        FlFont4 = (FrameLayout)findViewById(R.id.frameLayout_font_box_4);

    }

    private void setAllColorButtonReset(){
        FlFont1.setBackgroundResource(0);
        FlFont2.setBackgroundResource(0);
        FlFont3.setBackgroundResource(0);
        FlFont4.setBackgroundResource(0);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_test_next:
                Toast.makeText(this,"적용예정입니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.frameLayout_font_1:
                setAllColorButtonReset();
                FlFont1.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setFont(TestProfile.Font.FONT_1);
                break;
            case R.id.frameLayout_font_2:
                setAllColorButtonReset();
                FlFont2.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setFont(TestProfile.Font.FONT_2);
                break;
            case R.id.frameLayout_font_3:
                setAllColorButtonReset();
                FlFont3.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setFont(TestProfile.Font.FONT_3);
                break;
            case R.id.frameLayout_font_4:
                setAllColorButtonReset();
                FlFont4.setBackgroundResource(R.drawable.test_color_outline);
                mSm.setFont(TestProfile.Font.FONT_4);
                break;
        }

        //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("완료를 눌러주세요.");
        ((Button)findViewById(R.id.button_test_next)).setEnabled(true);
    }
}
