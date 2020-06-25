package com.pixelro.eyelab.menu.exercise;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.pixelro.eyelab.BaseActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.distance.IEyeDistanceMeasureServiceCallback;

import java.util.Timer;
import java.util.TimerTask;

public class Ex02Activity2 extends BaseActivity  implements IEyeDistanceMeasureServiceCallback {

    private final static String TAG = Ex02Activity2.class.getSimpleName();



    public Timer mTimer;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_022);

        mTimer = new Timer();

        mTimer.schedule(new Ex02Activity2.setinstruction(0),0);
        mTimer.schedule(new Ex02Activity2.setinstruction(1),4000);
        mTimer.schedule(new Ex02Activity2.setinstruction(2),8000);
        mTimer.schedule(new Ex02Activity2.setinstruction(3),12000);
        mTimer.schedule(new Ex02Activity2.setinstruction(4),16000);
        mTimer.schedule(new Ex02Activity2.setinstruction(5),20000);
        mTimer.schedule(new Ex02Activity2.setinstruction(6),24000);
        mTimer.schedule(new Ex02Activity2.setinstruction(7),28000);
        mTimer.schedule(new Ex02Activity2.setinstruction(8),32000);
        mTimer.schedule(new Ex02Activity2.setinstruction(9),36000);
        mTimer.schedule(new Ex02Activity2.setinstruction(10),40000);
        mTimer.schedule(new Ex02Activity2.setinstruction(11),44000);
        mTimer.schedule(new Ex02Activity2.setinstruction(12),48000);


        mTimer.schedule(new Ex02Activity2.goNext(),52000);

        image = (ImageView)findViewById(R.id.imageView_test_02_red);


//        AnimatorSet resAniSet = new AnimatorSet();
//        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", -300);
//        resAniSet.play(red);
//        resAniSet.setDuration(3000);
//        resAniSet.start();
//
//        red = ObjectAnimator.ofFloat(image,"translationY", -300);
//        resAniSet.play(red);
//        resAniSet.setDuration(3000);
//        resAniSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        resAniSet.start();


    }



    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    class goNext extends TimerTask {

        public goNext(){}

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ExCompleteDialog dlg = new ExCompleteDialog(Ex02Activity2.this);
                    dlg.setOnResultEventListener(new ExCompleteDialog.OnResultEventListener() {
                        @Override
                        public void ResultEvent(boolean result) {
                            if (result){
                                finish();
                            }
                        }
                    });
                    dlg.showDialog();
                }
            });

        }
    }

    class setinstruction extends TimerTask {

        private int mLevel;

        public setinstruction(int level){
            mLevel = level;
        }

        @Override
        public void run() {
            if (mLevel == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", -275);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });

            }
            else if (mLevel == 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 3){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -0);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 4){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 0);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });

            }
            else if (mLevel == 5){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 6){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 7){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -0);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 8){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 275);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });

            }
            else if (mLevel == 9){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", -200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 10){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 200);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 11){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationY", 0);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }
            else if (mLevel == 12){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet resAniSet = new AnimatorSet();
                        ObjectAnimator red = ObjectAnimator.ofFloat(image,"translationX", 0);
                        resAniSet.play(red);
                        resAniSet.setDuration(3000);
                        resAniSet.start();
                    }
                });
            }

        }
    }



}
