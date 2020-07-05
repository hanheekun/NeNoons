package com.pixelro.eyelab.menu.exercise.ex03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.distance.EyeDistanceMeasureService;
import com.pixelro.eyelab.menu.exercise.ex02.Ex02Activity;
import com.pixelro.eyelab.menu.exercise.ex02.Ex02CFragment;

import java.util.Timer;
import java.util.TimerTask;

public class Ex03BFragment extends Fragment implements View.OnClickListener {

    public final static int EX_LEVEL_L = 0;
    public final static int EX_LEVEL_M = 1;
    public final static int EX_LEVEL_H = 2;

    private Vibrator mVibrator;
    private View mView;
    public Timer mTimer;

    private boolean isClosed = false;

    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private static final int mCountMax = 3;
    private int mCloseTime;
    private int mSecTick = 0;
    private int[] mScheduleStartSec = new int[7];
    private ImageView IvEye;

    private TextView TvDistance;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_01_b, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        CbSound = (CheckBox)view.findViewById(R.id.checkBox_ex_sound);
        //CbSound.setOnCheckedChangeListener(this);
        CbVibrator = (CheckBox)view.findViewById(R.id.checkBox_ex_vibrator);
        //CbVibrator.setOnCheckedChangeListener(this);
        TvCount = (TextView)view.findViewById(R.id.textView_ex_1_count);
        TvCount.setText(""+ mCountMax);

        mTimer = new Timer();
        mTimer.schedule(TimaerTaskMaker(),0,100);

        if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_L){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 60;
            mScheduleStartSec[2] = 110;
            mScheduleStartSec[3] = 160;
            mScheduleStartSec[4] = 210;
            mScheduleStartSec[5] = 260;
            mScheduleStartSec[6] = 270;
        }
        else if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_M){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 110;
            mScheduleStartSec[2] = 160;
            mScheduleStartSec[3] = 260;
            mScheduleStartSec[4] = 310;
            mScheduleStartSec[5] = 410;
            mScheduleStartSec[6] = 420;
        }
        else if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_H){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 160;
            mScheduleStartSec[2] = 210;
            mScheduleStartSec[3] = 360;
            mScheduleStartSec[4] = 410;
            mScheduleStartSec[5] = 560;
            mScheduleStartSec[6] = 570;
        }

        Toast.makeText(getActivity(),"level = " + ((Ex02Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        IvEye = (ImageView) mView.findViewById(R.id.imageView36);

        TvDistance = (TextView)  mView.findViewById(R.id.textView_ex_03_distance);

        // for 진동
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
        mTimer.cancel();
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                TvDistance.setText(distance + "cm");
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private TimerTask TimaerTaskMaker (){
        TimerTask timerTest = new TimerTask() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        if (mSecTick == mScheduleStartSec[0]){
                            isClosed = true;

//                            if (CbVibrator.isChecked()){
//                                mVibrator.vibrate(200);
//                            }

                            IvEye.setImageResource(mArrorwImage[0]);
                        }
                        else if (mSecTick == mScheduleStartSec[1]){
                            isClosed = false;

                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("2");

                        }
                        else if (mSecTick == mScheduleStartSec[2]){
                            isClosed = true;

                            IvEye.setImageResource(mArrorwImage[0]);
                        }
                        else if (mSecTick == mScheduleStartSec[3]){
                            isClosed = false;
                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("1");
                        }
                        else if (mSecTick == mScheduleStartSec[4]){
                            isClosed = true;


                            IvEye.setImageResource(mArrorwImage[0]);
                        }
                        else if (mSecTick == mScheduleStartSec[5]){
                            isClosed = false;


                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("0");
                        }
                        else if (mSecTick == mScheduleStartSec[6]){
                            isClosed = false;
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02CFragment()).commit();
                        }

                        if (isClosed){
                            if (CbVibrator.isChecked()){
                                mVibrator.vibrate(70);
                            }
                        }

                        mSecTick++;

                    }
                });


            }
        };
        return timerTest;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
