package com.pixelro.eyelab.menu.exercise.ex01;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.eyelab.R;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class Ex01BFragment extends Fragment implements View.OnClickListener {

    private Vibrator mVibrator;

    private TextToSpeech mTTS;



    private View mView;

    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.eye_ex_1_close, R.drawable.eye_ex_1_open
    };

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

        Toast.makeText(getActivity(),"time = "+((Ex01Activity)getActivity()).setTimeSec + " level = " + ((Ex01Activity)getActivity()).setLevel,Toast.LENGTH_SHORT).show();

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
                player.start();
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
}
