package com.pixelro.nenoons.menu.exercise.ex03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.BaseFragment;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

import java.util.Timer;
import java.util.TimerTask;

public class Ex03BFragment extends BaseFragment implements View.OnClickListener {

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

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
    private float mLeftEyeValue =1.0f;
    private float mRightEyeValue =1.0f;

    private int mCount = 0;
    private static final int mCountMax = 3;
    private int mCloseTime;
    private int mSecTick = 0;
    private int[] mScheduleStartSec = new int[7];
    private ImageView IvEye;

    ProgressBar mProgressBar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_03_b, container, false);
    }
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                float Left = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT, 0);
                float Right = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT, 0);

                if(Left !=0 && Right !=0){
                    mLeftEyeValue =Left;
                    mRightEyeValue =Right;                }
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        // 화면 꺼짐 방지
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CbSound = (CheckBox)view.findViewById(R.id.checkBox_ex_sound);
        //CbSound.setOnCheckedChangeListener(this);
        CbVibrator = (CheckBox)view.findViewById(R.id.checkBox_ex_vibrator);
        //CbVibrator.setOnCheckedChangeListener(this);
        TvCount = (TextView)view.findViewById(R.id.textView_ex3_count);
        TvCount.setText("0 회");

        mTimer = new Timer();
        mTimer.schedule(TimaerTaskMaker(),0,100);

        if (((Ex03Activity)getActivity()).mCurLevel == EX_LEVEL_L){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 60;
            mScheduleStartSec[2] = 110;
            mScheduleStartSec[3] = 160;
            mScheduleStartSec[4] = 210;
            mScheduleStartSec[5] = 260;
            mScheduleStartSec[6] = 270;
        }
        else if (((Ex03Activity)getActivity()).mCurLevel == EX_LEVEL_M){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 110;
            mScheduleStartSec[2] = 160;
            mScheduleStartSec[3] = 260;
            mScheduleStartSec[4] = 310;
            mScheduleStartSec[5] = 410;
            mScheduleStartSec[6] = 420;
        }
        else if (((Ex03Activity)getActivity()).mCurLevel == EX_LEVEL_H){
            mScheduleStartSec[0] = 10;
            mScheduleStartSec[1] = 160;
            mScheduleStartSec[2] = 210;
            mScheduleStartSec[3] = 360;
            mScheduleStartSec[4] = 410;
            mScheduleStartSec[5] = 560;
            mScheduleStartSec[6] = 570;
        }

        //Toast.makeText(getActivity(),"level = " + ((Ex03Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        IvEye = (ImageView) mView.findViewById(R.id.imageView_ex_3_eye);

        // for 진동
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar_count);
        mProgressBar.setProgress(0);

        // 진동, 사운드 이전 기록으로 설정
        if (mSm.getExVibrator()){
            CbVibrator.setChecked(true);
        }
        else {
            CbVibrator.setChecked(false);
        }

        if (mSm.getExSound()){
            CbSound.setChecked(true);
        }
        else {
            CbSound.setChecked(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 진동, 소리 최종 선택 저장
        if(CbVibrator.isChecked()){
            mSm.setExVibrator(true);
        }
        else {
            mSm.setExVibrator(false);
        }

        if(CbSound.isChecked()){
            mSm.setExSound(true);
        }
        else {
            mSm.setExSound(false);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Typeface face = mSm.getFontTypeface();
        TvCount.setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_ex_3_guide)).setTypeface(face);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
        mTimer.cancel();
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

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }
                        }
                        else if (mSecTick == mScheduleStartSec[1]){
                            isClosed = false;

                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("1 회");
                            mProgressBar.setProgress(33);

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }

                            //TvCount.setText("2/3");

                        }
                        else if (mSecTick == mScheduleStartSec[2]){
                            isClosed = true;

                            IvEye.setImageResource(mArrorwImage[0]);

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }
                        }
                        else if (mSecTick == mScheduleStartSec[3]){
                            isClosed = false;
                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("2 회");
                            mProgressBar.setProgress(66);

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }

                            //TvCount.setText("1/3");
                        }
                        else if (mSecTick == mScheduleStartSec[4]){
                            isClosed = true;


                            IvEye.setImageResource(mArrorwImage[0]);

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }
                        }
                        else if (mSecTick == mScheduleStartSec[5]){
                            isClosed = false;


                            IvEye.setImageResource(mArrorwImage[1]);

                            TvCount.setText("3 회");
                            mProgressBar.setProgress(100);

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }

                            //TvCount.setText("0/3");
                        }
                        else if (mSecTick == mScheduleStartSec[6]){

                            isClosed = false;
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_ex_03, new Ex03CFragment()).commit();
                        }

                        // 200923 0.1초마다 진동하는 방식에서 1초마다 울리는 방식으로 변경
                        if (isClosed && (mSecTick%10)==0){

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
