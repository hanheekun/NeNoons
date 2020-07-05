package com.pixelro.eyelab.menu.exercise.ex01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

public class Ex01BFragment extends Fragment implements View.OnClickListener {

    public final static int EX_LEVEL_L = 0;
    public final static int EX_LEVEL_M = 1;
    public final static int EX_LEVEL_H = 2;

    private final static int SHORT = 0;
    private final static int LONG = 1;

    private int mMode = LONG;

    private Vibrator mVibrator;
    private View mView;
    public Timer mTimer = null;

    private boolean isClosed = false;

    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private int mCountMax = 3;
    private int mCloseTime;
    private int mSecTick = 0;
    private int[] mScheduleStartSec = new int[7];
    private ImageView IvEye;

    private TextView TvDistance;

    private TextView TvCntShort;
    private TextView TvCntLong;

    private int mTimerCount = 0;
    private static final int mTimerCountMAX = 10;

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
        TvCount = (TextView)view.findViewById(R.id.textView_ex_count);
        TvCount.setText(""+ mCountMax);

        TvCntLong = (TextView)view.findViewById(R.id.textView_ex_01_count_long);
        TvCntShort = (TextView)view.findViewById(R.id.textView_ex_01_count_short);

        Toast.makeText(getActivity(),"level = " + ((Ex01Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        if (((Ex01Activity)getActivity()).curLevel == EX_LEVEL_L){
            mCountMax = 3;
        }
        else if (((Ex01Activity)getActivity()).curLevel == EX_LEVEL_M){
            mCountMax = 4;
        }
        else if (((Ex01Activity)getActivity()).curLevel == EX_LEVEL_H){
            mCountMax = 5;
        }

        TvDistance = (TextView)  mView.findViewById(R.id.textView_ex_01_distance);

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

        if (mTimer != null){
            mTimer.cancel();
        }

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                TvDistance.setText(distance + "cm");

                // 거리 확인
                if (mMode == LONG){
                    if (distance < 50){
                        if (mTimer == null){

                        }
                        else {
                            TvCntLong.setText("" + mTimerCountMAX);
                            mTimerCount = 0;
                            mTimer.cancel();
                            mTimer = null;
                        }
                    }
                    else {
                        if (mTimer == null){
                            mTimer = new Timer();
                            mTimer.schedule(TimaerTaskMaker(),0,1000);
                        }
                        else {

                        }
                    }
                }
                else if (mMode == SHORT){
                    if (distance > 25){
                        if (mTimer == null){

                        }
                        else {
                            TvCntShort.setText("" + mTimerCountMAX);
                            mTimerCount = 0;
                            mTimer.cancel();
                            mTimer = null;
                        }
                    }
                    else {
                        if (mTimer == null){
                            mTimer = new Timer();
                            mTimer.schedule(TimaerTaskMaker(),0,1000);
                        }
                        else {

                        }
                    }
                }

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

                        if (mMode == LONG){
                            TvCntLong.setText("" + (mTimerCountMAX - (mTimerCount++)));

                            if (mTimerCount > mTimerCountMAX){
                                mTimerCount = 0;
                                mMode = SHORT;
                                mTimer.cancel();
                                mTimer = null;
                                TvCntLong.setText("" + mTimerCountMAX);
                            }

                        }
                        else if (mMode == SHORT){
                            TvCntShort.setText("" + (mTimerCountMAX - (mTimerCount++)));

                            if (mTimerCount > mTimerCountMAX){
                                mTimerCount = 0;
                                mMode = LONG;
                                mTimer.cancel();
                                mTimer = null;
                                TvCntShort.setText("" + mTimerCountMAX);

                                mCount++;

                                TvCount.setText(""+ (mCountMax - mCount));

                                if (mCount == mCountMax ){
                                    // 2초간 멈추게 하고싶다 면
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_ex_01, new Ex01CFragment()).commit();
                                        }
                                    }, 1000);  // 2000은 2초를 의미합니다.

                                }

                            }

                        }



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
