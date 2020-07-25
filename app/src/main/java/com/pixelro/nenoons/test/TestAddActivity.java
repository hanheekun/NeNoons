package com.pixelro.nenoons.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.TestProfile;

public class TestAddActivity extends AppCompatActivity{

    private TestProfile mTestProfile;
    private TextView TvResult1;
    private TextView TvResult2;
    private TextView TvResult3;
    private TextView TvResult4;
    private TextView TvResult5;
    private TextView TvResultAge;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add);

        findViewById(R.id.button_arrow_back_background).setOnClickListener(v -> finish());
        TvResult1 = (TextView)findViewById(R.id.textView_test_add_1);
        TvResult2 = (TextView)findViewById(R.id.textView_test_add_2);
        TvResult3 = (TextView)findViewById(R.id.textView_test_add_3);
        TvResult4 = (TextView)findViewById(R.id.textView_test_add_4);
        TvResult5 = (TextView)findViewById(R.id.textView_test_add_5);
        TvResultAge = (TextView)findViewById(R.id.textView_test_add_age);

        mTestProfile = (TestProfile)getIntent().getSerializableExtra("TestProfile");

        // 거리에 따른 나이
        if (mTestProfile.distance <= 22){
            TvResultAge.setText("노안나이\n44세 이하");
        }
        else if(mTestProfile.distance >= 23 && mTestProfile.distance <= 30){
            TvResultAge.setText("노안나이\n40대 중반");
        }
        else if(mTestProfile.distance >= 31 && mTestProfile.distance <= 37){
            TvResultAge.setText("노안나이\n40대 후반");
        }
        else if(mTestProfile.distance >= 38 && mTestProfile.distance <= 47){
            TvResultAge.setText("노안나이\n50세");
        }
        else if(mTestProfile.distance >= 48 && mTestProfile.distance <= 57){
            TvResultAge.setText("노안나이\n50대 초반");
        }
        else if(mTestProfile.distance >= 58 && mTestProfile.distance <= 67){
            TvResultAge.setText("노안나이\n50대 중반");
        }
        else if(mTestProfile.distance >= 68){
            TvResultAge.setText("노안나이\n56세 이상");
        }



        TvResult1.setText(mTestProfile.distance + " cm");

        if (mTestProfile.redgreen == TestProfile.Redgreen.RED){
            TvResult2.setText("적색");
        }
        else{
            TvResult2.setText("녹색");
        }

        TvResult3.setText("대비감도 " + mTestProfile.brightNumber+"개");

        TvResult4.setTextColor(mTestProfile.color);

        if (mTestProfile.font == TestProfile.Font.FONT_2){
            TvResult5.setTypeface(getResources().getFont(R.font.dall01r));
        }
        else if (mTestProfile.font == TestProfile.Font.FONT_3){
            TvResult5.setTypeface(getResources().getFont(R.font.dall01b));
        }
        else if (mTestProfile.font == TestProfile.Font.FONT_4){
            TvResult5.setTypeface(getResources().getFont(R.font.dall01eb));
        }





    }

}
