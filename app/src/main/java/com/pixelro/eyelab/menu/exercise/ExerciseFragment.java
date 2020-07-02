package com.pixelro.eyelab.menu.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ex01.Ex01Activity;
import com.pixelro.eyelab.menu.exercise.ex02.Ex02Activity;
import com.pixelro.eyelab.menu.exercise.ex03.Ex03Activity;
import com.pixelro.eyelab.menu.exercise.history.ExHistoryActivity;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class ExerciseFragment extends Fragment implements View.OnClickListener {

    private ExerciseViewModel exerciseViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View mView;

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

        if (dayOfMonth == preDayOfMonth){

        }
        else {
            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY,dayOfMonth);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_5_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_6_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_7_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_8_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_9_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_10_COMPLETE,false);
            editor.commit();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.ex_btn_02);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.ex_btn_01);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.ex_btn_02);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.ex_btn_01);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.ex_btn_02);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.ex_btn_01);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.ex_btn_02);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.ex_btn_01);
        }

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
                intent = new Intent(getActivity(), Ex04Activity2.class);
                startActivity(intent);
                break;
            case R.id.button_ex_history:
                intent = new Intent(getActivity(), ExHistoryActivity.class);
                startActivity(intent);
                break;
        }

    }
}
