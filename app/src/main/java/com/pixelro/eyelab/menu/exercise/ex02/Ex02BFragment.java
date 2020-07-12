package com.pixelro.eyelab.menu.exercise.ex02;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.eyelab.R;

import java.util.Timer;
import java.util.TimerTask;

public class Ex02BFragment extends Fragment implements View.OnClickListener {

    public final static int EX_LEVEL_L = 0;
    public final static int EX_LEVEL_M = 1;
    public final static int EX_LEVEL_H = 2;

    private Vibrator mVibrator;
    private View mView;
    public Timer mTimer;

    ProgressBar mProgressBar;

    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private static final int mCountMax = 30;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_02_b, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        CbSound = (CheckBox)view.findViewById(R.id.checkBox_ex_sound);
        //CbSound.setOnCheckedChangeListener(this);
        CbVibrator = (CheckBox)view.findViewById(R.id.checkBox_ex_vibrator);
        //CbVibrator.setOnCheckedChangeListener(this);
        TvCount = (TextView)view.findViewById(R.id.textView_ex_count);
        //TvCount.setText(""+ mCountMax);

        mTimer = new Timer();

        if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_L){
            mTimer.schedule(TimaerTaskMaker(),1000,1000);
        }
        else if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_M){
            mTimer.schedule(TimaerTaskMaker(),1000,766);
        }
        else if (((Ex02Activity)getActivity()).curLevel == EX_LEVEL_H){
            mTimer.schedule(TimaerTaskMaker(),1000,400);
        }

        //Toast.makeText(getActivity(),"level = " + ((Ex02Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        // for 진동
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

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



                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //TvCount.setText(""+ (mCountMax - mCount));
                                TvCount.setText(mCount+1 + "회");
                                mProgressBar.setProgress((int) (mCount*3.3));

                                if (CbSound.isChecked()){
                                    MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                    //float speed = 0.5f;
                                    //player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                                    player.start();
                                }

                                if (CbVibrator.isChecked()){
                                    mVibrator.vibrate(200);
                                }

                                // arrow image animation
                                final ImageView iv = (ImageView) mView.findViewById(R.id.imageView_ex_2_eye);
                                final Runnable r = new Runnable() {
                                    int i = 0;

                                    @Override
                                    public void run() {

                                        if (i == 0) {
                                            iv.setImageResource(mArrorwImage[i]);
                                            iv.postDelayed(this, 200);
                                            i++;
                                        }
                                        else
                                        {
                                            iv.setImageResource(mArrorwImage[i]);
                                        }

                                    }
                                };
                                iv.post(r);

                            }
                        });

                        mCount++;

                        if (mCount == mCountMax){
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02CFragment()).commit();
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
