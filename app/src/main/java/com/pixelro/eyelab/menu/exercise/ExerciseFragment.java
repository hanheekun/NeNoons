package com.pixelro.eyelab.menu.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ex01.Ex01Activity;
import com.pixelro.eyelab.menu.exercise.ex02.Ex02Activity;
import com.pixelro.eyelab.menu.exercise.ex03.Ex03Activity;
import com.pixelro.eyelab.menu.exercise.ex04.Ex04Activity;
import com.pixelro.eyelab.menu.exercise.history.ExHistoryActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class ExerciseFragment extends Fragment implements View.OnClickListener {

    private ExerciseViewModel exerciseViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View mView;

    private TextView TvDate;
    private TextView Tvnumber;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        mView = root;

        root.findViewById(R.id.button_ex_01).setOnClickListener(this);
        root.findViewById(R.id.button_ex_02).setOnClickListener(this);
        root.findViewById(R.id.button_ex_03).setOnClickListener(this);
        root.findViewById(R.id.button_ex_04).setOnClickListener(this);
        root.findViewById(R.id.button_ex_history).setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int preDayOfMonth = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY,0);

//        if (dayOfMonth == preDayOfMonth){
//
//        }
//        else {
//            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY,dayOfMonth);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_5_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_6_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_7_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_8_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_9_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_10_COMPLETE,false);
//
//            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
//
//            editor.commit();
//        }

        TvDate = (TextView)root.findViewById(R.id.textView_ex_date);

        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
        // 년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy.MM.dd");
        TvDate.setText(fourteen_format.format(date_now));

        Tvnumber = (TextView)root.findViewById(R.id.textView_ex_number);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.button_ex_01_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.button_ex_01);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.button_ex_02_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.button_ex_02);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.button_ex_03_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.button_ex_03);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.button_ex_04_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.button_ex_04);
        }

        //
        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        Tvnumber.setText("");
        String s= "오늘 ";
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
        Tvnumber.append(ss1);
        s= "" + curTotalEXNumber;
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,s.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);// set color
        Tvnumber.append(ss1);
        s= " 개의 눈 운동을 하셨네요.";
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
        Tvnumber.append(ss1);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.button_ex_01:
                Intent intent = new Intent(getActivity(), Ex01Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_02:
                intent = new Intent(getActivity(), Ex02Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_03:
                intent = new Intent(getActivity(), Ex03Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_04:
                intent = new Intent(getActivity(), Ex04Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_history:
                intent = new Intent(getActivity(), ExHistoryActivity.class);
                startActivity(intent);
                break;
        }

    }
}
