package com.pixelro.nenoons.menu.exercise.ex04;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
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

import java.util.Timer;
import java.util.TimerTask;

public class Ex04BFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = Ex04BFragment.class.getSimpleName();

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    private Vibrator mVibrator;
    private View mView;
    public Timer mTimer;

    private boolean isClosed = false;

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private static final int mCountMax = 3;

    private int mPathNumber = 0;
    private static final int mPathNumberMax = 13;
    private int mPathMoveTimeMS = 0;

    ProgressBar mProgressBar;

    ImageView image;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_04_b, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG,"onViewCreated in");

        mView = view;
        CbSound = (CheckBox)view.findViewById(R.id.checkBox_ex_sound);
        //CbSound.setOnCheckedChangeListener(this);
        CbVibrator = (CheckBox)view.findViewById(R.id.checkBox_ex_vibrator);
        //CbVibrator.setOnCheckedChangeListener(this);
        TvCount = (TextView)view.findViewById(R.id.textView_ex_count);
        TvCount.setText("0/3");

        mTimer = new Timer();

        if (((Ex04Activity)getActivity()).mCurLevel == EX_LEVEL_L){
            mTimer.schedule(TimaerTaskMaker(),0,2100);
            mPathMoveTimeMS = 2100;
        }
        else if (((Ex04Activity)getActivity()).mCurLevel == EX_LEVEL_M){
            mTimer.schedule(TimaerTaskMaker(),0,1400);
            mPathMoveTimeMS = 1400;
        }
        else if (((Ex04Activity)getActivity()).mCurLevel == EX_LEVEL_H){
            mTimer.schedule(TimaerTaskMaker(),0,700);
            mPathMoveTimeMS = 700;
        }

        //Toast.makeText(getActivity(),"level = " + ((Ex04Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        // for 진동
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

        image = (ImageView)mView.findViewById(R.id.imageView_test_02_red);

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar_count);
        mProgressBar.setProgress(0);

    }


    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    private TimerTask TimaerTaskMaker (){
        TimerTask timerTest = new TimerTask() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        if (mPathNumber == mPathNumberMax)
                        {

                            mCount++;
                            if (mCount == 1) {
                                TvCount.setText("1/3");
                                mProgressBar.setProgress(33);
                            }
                            if (mCount == 2) {
                                TvCount.setText("2/3");
                                mProgressBar.setProgress(66);
                            }

                            mPathNumber = 0;

                            if (CbSound.isChecked()){
                                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                //float speed = 0.5f;
                                //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                player.start();
                            }

                            if (CbVibrator.isChecked()){
                                mVibrator.vibrate(500);
                            }

                        }

                        if (mCount == mCountMax){
                            mTimer.cancel();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_ex_04, new Ex04CFragment()).commit();
                                }
                            }, 10);  // 2000은 2초를 의미합니다.

                        }

                        if (mPathNumber == 0){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", -490); // -440
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 1){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 590); //500
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 2){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -590);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 3){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 0);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 4){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 0);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 5){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 490);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 6){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 590);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 7){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -590);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 8){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 0);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 9){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 0);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 10){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 590);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 11){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -590);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }
                        else if (mPathNumber == 12){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AnimatorSet resAniSet = new AnimatorSet();
                                    ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -0);
                                    resAniSet.play(red);
                                    resAniSet.setDuration(mPathMoveTimeMS);
                                    resAniSet.start();
                                }
                            });
                        }

                        mPathNumber++;




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
