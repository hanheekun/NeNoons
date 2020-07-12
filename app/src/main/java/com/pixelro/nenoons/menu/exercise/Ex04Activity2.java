package com.pixelro.nenoons.menu.exercise;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.pixelro.nenoons.BaseActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.distance.IEyeDistanceMeasureServiceCallback;

public class Ex04Activity2 extends BaseActivity  implements IEyeDistanceMeasureServiceCallback, SeekBar.OnSeekBarChangeListener {

    private final static String TAG = Ex04Activity2.class.getSimpleName();

    private SeekBar SbRingSeek;
    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ImageView ivRingLeft;
    private ImageView ivRingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_042);

        SbRingSeek = (SeekBar)findViewById(R.id.seekBar_ex4);
        SbRingSeek.setOnSeekBarChangeListener(this);
        ivRingLeft = (ImageView)findViewById(R.id.imageView_ex_4_ring_left);
        ivRingRight = (ImageView)findViewById(R.id.imageView_ex_4_ring_right);

        applyConstraintSet.clone((ConstraintLayout)findViewById(R.id.constraint_ex_4));

    }



    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float bias = (float)(0.47 - 0.0027*progress);
        applyConstraintSet.setHorizontalBias(R.id.imageView_ex_4_ring_left, bias);
        bias = (float)(0.53 + 0.0027*progress);
        applyConstraintSet.setHorizontalBias(R.id.imageView_ex_4_ring_right, bias);
        applyConstraintSet.applyTo((ConstraintLayout)findViewById(R.id.constraint_ex_4));

        Log.d(TAG,"onProgressChanged = "+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
