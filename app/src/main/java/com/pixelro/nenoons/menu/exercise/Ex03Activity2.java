package com.pixelro.nenoons.menu.exercise;

import android.os.Bundle;
import android.widget.ImageView;

import com.pixelro.nenoons.BaseActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.distance.IEyeDistanceMeasureServiceCallback;

import java.util.Timer;
import java.util.TimerTask;

public class Ex03Activity2 extends BaseActivity  implements IEyeDistanceMeasureServiceCallback {

    private final static String TAG = Ex03Activity2.class.getSimpleName();

    public Timer mTimer;
    // arrow animation
    public static Integer[] mArrorwImage = {
            R.drawable.ex_03_1, R.drawable.ex_03_2
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_032);

        mTimer = new Timer();
        mTimer.schedule(new Ex03Activity2.goNext(),10000);

        // arrow image animation
        final ImageView iv = (ImageView) findViewById(R.id.imageView_ex_03);
        final Runnable r = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                iv.setImageResource(mArrorwImage[i++]);
                if (i >= mArrorwImage.length) {
                    i = 0;
                }

                if (i == 0) {
                    iv.postDelayed(this, 100);// reset
                } else {
                    iv.postDelayed(this, 100);// reset
                }

            }
        };
        iv.post(r); // set first

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
                    ExCompleteDialog dlg = new ExCompleteDialog(Ex03Activity2.this);
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


}
