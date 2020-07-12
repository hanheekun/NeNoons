package com.pixelro.nenoons.menu.exercise.ex01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

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
    public static Integer[] mBalloonImage = {
            R.drawable.balloon_1, R.drawable.balloon_2, R.drawable.balloon_3, R.drawable.balloon_4, R.drawable.balloon_5
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

    private TextView TvGuide;

    private int mTimerCount = 0;
    private static final int mTimerCountMAX = 10;

    ProgressBar mProgressBar;
    private ImageView IvBalloon;

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

        //Toast.makeText(getActivity(),"level = " + ((Ex01Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

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

        TvGuide = (TextView)view.findViewById(R.id.textView_ex_1_guide_balloon);
        TvGuide.setText("정면 카메라를 보면서\n화면과의 거리를 55cm보다 멀리하세요");

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar_count);
        mProgressBar.setProgress(0);

        IvBalloon = (ImageView)view.findViewById(R.id.imageView_ex_1_balloon);
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

                    TvGuide.setText("정면 카메라를 보면서\n화면과의 거리를 55cm보다 멀리하세요");

                    if (distance < 53){
                        if (mTimer == null){

                        }
                        else {
                            TvCntLong.setText("" + mTimerCountMAX);
                            mTimerCount = 0;
                            mTimer.cancel();
                            mTimer = null;

                            IvBalloon.setImageResource(mBalloonImage[0]);
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

                    TvGuide.setText("정면 카메라를 보면서\n화면과의 거리를 20cm로 유지하세요");

                    if (distance > 22){
                        if (mTimer == null){

                        }
                        else {
                            TvCntShort.setText("" + mTimerCountMAX);
                            mTimerCount = 0;
                            mTimer.cancel();
                            mTimer = null;

                            IvBalloon.setImageResource(mBalloonImage[0]);
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

                            if (mTimerCount == 2){
                                IvBalloon.setImageResource(mBalloonImage[1]);
                            }
                            else if (mTimerCount == 5){
                                IvBalloon.setImageResource(mBalloonImage[2]);
                            }
                            else if (mTimerCount == 8){
                                IvBalloon.setImageResource(mBalloonImage[3]);
                            }
                            else if (mTimerCount == 11){
                                IvBalloon.setImageResource(mBalloonImage[4]);
                            }

                            if (mTimerCount > mTimerCountMAX){
                                mTimerCount = 0;
                                mMode = SHORT;
                                mTimer.cancel();
                                mTimer = null;
                                TvCntLong.setText("" + mTimerCountMAX);

                                if (CbSound.isChecked()){
                                    MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                    //float speed = 0.5f;
                                    //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                    player.start();
                                }

                                if (CbVibrator.isChecked()){
                                    mVibrator.vibrate(1000);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        IvBalloon.setImageResource(mBalloonImage[0]);
                                    }
                                }, 500);  // 2000은 2초를 의미합니다.

                            }

                        }
                        else if (mMode == SHORT){
                            TvCntShort.setText("" + (mTimerCountMAX - (mTimerCount++)));

                            if (mTimerCount == 2){
                                IvBalloon.setImageResource(mBalloonImage[1]);
                            }
                            else if (mTimerCount == 5){
                                IvBalloon.setImageResource(mBalloonImage[2]);
                            }
                            else if (mTimerCount == 8){
                                IvBalloon.setImageResource(mBalloonImage[3]);
                            }
                            else if (mTimerCount == 11){
                                IvBalloon.setImageResource(mBalloonImage[4]);
                            }

                            if (mTimerCount > mTimerCountMAX){
                                mTimerCount = 0;
                                mMode = LONG;
                                mTimer.cancel();
                                mTimer = null;
                                TvCntShort.setText("" + mTimerCountMAX);

                                mCount++;

                                TvCount.setText(""+ (mCountMax - mCount));


                                if (mCountMax == 3){

                                    if (mCount == 1){
                                        mProgressBar.setProgress(33);
                                    }
                                    else if (mCount == 2){
                                        mProgressBar.setProgress(66);
                                    }
                                    else if (mCount == 3){
                                        mProgressBar.setProgress(100);
                                    }
                                }
                                else if (mCountMax == 4){
                                    if (mCount == 1){
                                        mProgressBar.setProgress(25);
                                    }
                                    else if (mCount == 2){
                                        mProgressBar.setProgress(50);
                                    }
                                    else if (mCount == 3){
                                        mProgressBar.setProgress(75);
                                    }
                                    else if (mCount == 4){
                                        mProgressBar.setProgress(100);
                                    }
                                }
                                else if (mCountMax == 5){
                                    if (mCount == 1){
                                        mProgressBar.setProgress(20);
                                    }
                                    else if (mCount == 2){
                                        mProgressBar.setProgress(40);
                                    }
                                    else if (mCount == 3){
                                        mProgressBar.setProgress(60);
                                    }
                                    else if (mCount == 4){
                                        mProgressBar.setProgress(80);
                                    }
                                    else if (mCount == 5){
                                        mProgressBar.setProgress(100);
                                    }
                                }


                                if (CbSound.isChecked()){
                                    MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                    //float speed = 0.5f;
                                    //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                    player.start();
                                }

                                if (CbVibrator.isChecked()){
                                    mVibrator.vibrate(1000);
                                }

                                Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    public void run() {
                                        IvBalloon.setImageResource(mBalloonImage[0]);
                                    }
                                }, 500);  // 2000은 2초를 의미합니다.

                                if (mCount == mCountMax ){
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
