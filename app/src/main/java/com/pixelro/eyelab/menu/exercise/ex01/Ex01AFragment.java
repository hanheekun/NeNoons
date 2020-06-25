package com.pixelro.eyelab.menu.exercise.ex01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.test.Test01Dis03Fragment;

public class Ex01AFragment extends Fragment implements View.OnClickListener{

    public final static int EX_TIME_30 = 30;
    public final static int EX_TIME_1 = 60;
    public final static int EX_TIME_2 = 120;
    public final static int EX_TIME_5 = 300;
    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    private View mView;

    private Button BtnTime30;
    private Button BtnTime1;
    private Button BtnTime2;
    private Button BtnTime5;
    private Button BtnLevelL;
    private Button BtnLevelM;
    private Button BtnLevelH;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_01_a, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_ex_01_a).setOnClickListener(this);

        BtnTime30 = (Button)view.findViewById(R.id.btn_ex_a_30);
        BtnTime1 = (Button)view.findViewById(R.id.btn_ex_a_1);
        BtnTime2 = (Button)view.findViewById(R.id.btn_ex_a_2);
        BtnTime5 = (Button)view.findViewById(R.id.btn_ex_a_5);
        BtnLevelL = (Button)view.findViewById(R.id.btn_ex_a_l);
        BtnLevelM = (Button)view.findViewById(R.id.btn_ex_a_m);
        BtnLevelH = (Button)view.findViewById(R.id.btn_ex_a_h);

        BtnTime30.setOnClickListener(this);
        BtnTime1.setOnClickListener(this);
        BtnTime2.setOnClickListener(this);
        BtnTime5.setOnClickListener(this);
        BtnLevelL.setOnClickListener(this);
        BtnLevelM.setOnClickListener(this);
        BtnLevelH.setOnClickListener(this);
    }

    private void resetTimeButton(){
        BtnTime30.setEnabled(true);
        BtnTime1.setEnabled(true);
        BtnTime2.setEnabled(true);
        BtnTime5.setEnabled(true);
    }

    private void resetLevelButton(){
        BtnLevelL.setEnabled(true);
        BtnLevelM.setEnabled(true);
        BtnLevelH.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_ex_01_a:

                // set time, level
                if (!BtnTime30.isEnabled()) ((Ex01Activity)getActivity()).setTimeSec = EX_TIME_30;
                if (!BtnTime1.isEnabled()) ((Ex01Activity)getActivity()).setTimeSec = EX_TIME_1;
                if (!BtnTime2.isEnabled()) ((Ex01Activity)getActivity()).setTimeSec = EX_TIME_2;
                if (!BtnTime5.isEnabled()) ((Ex01Activity)getActivity()).setTimeSec = EX_TIME_5;
                if (!BtnLevelL.isEnabled()) ((Ex01Activity)getActivity()).setLevel = EX_LEVEL_L;
                if (!BtnLevelM.isEnabled()) ((Ex01Activity)getActivity()).setLevel = EX_LEVEL_M;
                if (!BtnLevelH.isEnabled()) ((Ex01Activity)getActivity()).setLevel = EX_LEVEL_H;

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_01, new Ex01BFragment()).commit();

                break;
            case R.id.btn_ex_a_30:
                resetTimeButton();
                BtnTime30.setEnabled(false);
                break;
            case R.id.btn_ex_a_1:
                resetTimeButton();
                BtnTime1.setEnabled(false);
                break;
            case R.id.btn_ex_a_2:
                resetTimeButton();
                BtnTime2.setEnabled(false);
                break;
            case R.id.btn_ex_a_5:
                resetTimeButton();
                BtnTime5.setEnabled(false);
                break;
            case R.id.btn_ex_a_l:
                resetLevelButton();
                BtnLevelL.setEnabled(false);
                break;
            case R.id.btn_ex_a_m:
                resetLevelButton();
                BtnLevelM.setEnabled(false);
                break;
            case R.id.btn_ex_a_h:
                resetLevelButton();
                BtnLevelH.setEnabled(false);
                break;
        }
    }
}
