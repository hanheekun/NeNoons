package com.pixelro.nenoons.menu.exercise.ex02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Ex02BFragment extends BaseFragment implements View.OnClickListener {

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;


    private Vibrator mVibrator;
    private View mView;
    public Timer mTimer;
    private float mLeftEyeOpenValues= 1.2f;
    private float mRightEyeOpenValues=1.2f;
    ProgressBar mProgressBar;
    private ImageView ivEye;
    private int BlinkMode =1;
    private int previous_state =1;
    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private int mCountMax = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_02_b, container, false);
    }
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                float Left = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT, 0);
                float Right = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT, 0);

                mRightEyeOpenValues = Right;
                mLeftEyeOpenValues = Left;

                if (mLeftEyeOpenValues != 0 && mRightEyeOpenValues != 0) { // Default 값이 안나오게 하기 위해
                   // 현재 previous =1; / Blink =1;

                    if (previous_state == 1 && BlinkMode == 0) { // 이전에 뜨고 있다가 현재 감음

                        TvCount.setText(mCount + 1 + "회");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivEye.setImageResource(mArrorwImage[0]);
                            }
                        }, 300);

                        if (mCountMax == 30)
                            mProgressBar.setProgress((int) (mCount * 3.3));
                        else if (mCountMax == 25)
                            mProgressBar.setProgress((int) (mCount * 4.0));
                        else if (mCountMax == 20)
                            mProgressBar.setProgress((int) (mCount * 5.0));

                        if (CbSound.isChecked()) {
                            MediaPlayer player = MediaPlayer.create(getActivity(), R.raw.ddiring);
                            //float speed = 0.5f;
                            //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                            player.start();
                        }

                        if (CbVibrator.isChecked()) {
                            mVibrator.vibrate(200);
                        }
                        previous_state = 0;
                    }

                    else if (previous_state == 0 && BlinkMode == 0) { // 계속 눈을 감는 상태
                        ivEye.setImageResource(mArrorwImage[0]);
                        previous_state = 0;
                        if (mRightEyeOpenValues > 0.7 && mLeftEyeOpenValues > 0.7)
                            BlinkMode = 1;
                    }

                    else if (previous_state == 0 && BlinkMode == 1) { // 이전에 눈을 감고 있다가 방금 뜬 상태
                        mCount++;
                        previous_state = 1;
                        ivEye.setImageResource(mArrorwImage[1]);
                    }

                     else if (previous_state == 1 && BlinkMode == 1) { // 계속 눈을 뜬 상태
                        ivEye.setImageResource(mArrorwImage[1]);
                        previous_state = 1;
                        if (mRightEyeOpenValues <= 0.05 && mLeftEyeOpenValues <= 0.05) { // 눈을 감았다고 판단
                            BlinkMode = 0;
                        }
                    }
                }

                if (mCount >= mCountMax) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02CFragment()).commit();
                }
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
        TvCount = (TextView)view.findViewById(R.id.textView_ex_count);
        //TvCount.setText(""+ mCountMax);
        ivEye = (ImageView) view.findViewById(R.id.imageView_ex_2_eye);
        mTimer = new Timer();

        if (((Ex02Activity)getActivity()).mCurLevel == EX_LEVEL_L){
            mCountMax = 20;
        }
        else if (((Ex02Activity)getActivity()).mCurLevel == EX_LEVEL_M){
           mCountMax =25;
        }
        else if (((Ex02Activity)getActivity()).mCurLevel == EX_LEVEL_H){
           mCountMax =30;
        }


        //Toast.makeText(getActivity(),"level = " + ((Ex02Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

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
        ((TextView)mView.findViewById(R.id.textView_ex_2_guide)).setTypeface(face);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
        mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
