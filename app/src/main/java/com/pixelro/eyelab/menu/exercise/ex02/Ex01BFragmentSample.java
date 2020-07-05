package com.pixelro.eyelab.menu.exercise.ex02;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;

public class Ex01BFragmentSample extends Fragment{
/*
    public final static int EX_LEVEL_L = 0;
    public final static int EX_LEVEL_M = 1;
    public final static int EX_LEVEL_H = 2;

    private Vibrator mVibrator;

    private TextToSpeech mTTS;

    private View mView;

    public Timer mTimer;

    private TimerTask mTimerTest;

    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

    private CheckBox CbSound;
    private CheckBox CbVibrator;
    private TextView TvCount;

    private int mCount = 0;
    private static final int mCountMax = 30;

    MediaPlayer mPlayer;
    SoundPool mSoundPool;
    int mSoundPoolID = 0;
    AudioManager  mAudioManager;

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
        TvCount = (TextView)view.findViewById(R.id.textView_ex_1_count);
        TvCount.setText(""+ mCountMax);

        mPlayer = MediaPlayer.create(getActivity(),R.raw.ddiring);



//        view.findViewById(R.id.button_ex_01_b).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_ex_01, new Ex01CFragment()).commit();
//            }
//        });
//
//        view.findViewById(R.id.button_ex_01_b_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_ex_01, new Ex01AFragment()).commit();
//            }
//        });

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



        Toast.makeText(getActivity(),"level = " + ((Ex02Activity)getActivity()).curLevel,Toast.LENGTH_SHORT).show();

        view.findViewById(R.id.button_ex_1_1).setOnClickListener(this);
        view.findViewById(R.id.button_ex_1_2).setOnClickListener(this);
        view.findViewById(R.id.button_ex_1_3).setOnClickListener(this);
        view.findViewById(R.id.button_ex_1_4).setOnClickListener(this);
        view.findViewById(R.id.button_ex_1_5).setOnClickListener(this);

        // for 진동
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

        // TTS
        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    mTTS.setLanguage(Locale.KOREAN);
                }
            }
        });

        //mAudioManager = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        //mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  //벨
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
//        }
//        else {
//            mSoundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
//        }
//
//        mSoundPoolID = mSoundPool.load(getActivity(), R.raw.ddiring, 1);

    }

    @Override
    public void onStop() {
        super.onStop();

//        mSoundPool.release();
//        mSoundPool = null;
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

                                TvCount.setText(""+ (mCountMax - mCount));

                                if (CbSound.isChecked()){
                                    MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            player.start();
                                        }
                                    });
                                }

                                if (CbVibrator.isChecked()){
                                    mVibrator.vibrate(200); // 0.5초간 진동
                                }

                                // arrow image animation
                                final ImageView iv = (ImageView) mView.findViewById(R.id.imageView36);
                                final Runnable r = new Runnable() {
                                    int i = 0;

                                    @Override
                                    public void run() {

                                        if (i == 0) {
                                            iv.setImageResource(mArrorwImage[i]);
                                            iv.postDelayed(this, 200);// reset
                                            i++;
                                        }
                                        else
                                        {
                                            iv.setImageResource(mArrorwImage[i]);
                                        }

                                    }
                                };
                                iv.post(r); // set first

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
            case R.id.button_ex_1_1:
                mVibrator.vibrate(500); // 0.5초간 진동
                break;
            case R.id.button_ex_1_2:
                mTTS.setPitch(1.0f);         // 음성 톤은 기본 설정
                mTTS.setSpeechRate(1.0f);    // 읽는 속도를 0.5빠르기로 설정
                // editText에 있는 문장을 읽는다.
                mTTS.speak("박자에 맞춰 눈을 깜빡이세요.",TextToSpeech.QUEUE_FLUSH, null, null);
                break;
            case R.id.button_ex_1_3:
                MediaPlayer player = MediaPlayer.create(getActivity(),R.raw.ddiring);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        player.start();
                    }
                });


                //mSoundPool.play(mSoundPoolID, 1f, 1f, 0, 0, 1f);

//                mSoundPool.setOnLoadCompleteListener (new SoundPool.OnLoadCompleteListener() {
//                    @Override
//                    public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
//                        mSoundPool.play(mSoundPoolID, 1f, 1f, 0, 0, 1f);
//                    }
//                });

                //mSoundPool.play(mSoundPoolID, 1f, 1f, 0, 0, 1f);
                break;
            case R.id.button_ex_1_4:
                // arrow image animation
                final ImageView iv = (ImageView) mView.findViewById(R.id.imageView36);
                final Runnable r = new Runnable() {
                    int i = 0;

                    @Override
                    public void run() {

                        if (i == 0) {
                            iv.setImageResource(mArrorwImage[i]);
                            iv.postDelayed(this, 200);// reset
                            i++;
                        }
                        else
                        {
                            iv.setImageResource(mArrorwImage[i]);
                        }

                    }
                };
                iv.post(r); // set first

                break;
            case R.id.button_ex_1_5:
                // arrow image animation
                final ProgressBar mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar2);
                final Runnable rr = new Runnable() {
                    int i = 0;

                    @Override
                    public void run() {

                        if (i <= 100) {
                            mProgressBar.setProgress(i);
                            mProgressBar.postDelayed(this, 300);// reset
                            i += 10;
                        }

                    }
                };
                mProgressBar.post(rr); // set first
                break;
        }
    }


 */
}
