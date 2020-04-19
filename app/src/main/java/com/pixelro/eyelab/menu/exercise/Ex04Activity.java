package com.pixelro.eyelab.menu.exercise;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.pixelro.eyelab.BaseActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.distance.IEyeDistanceMeasureServiceCallback;

import java.util.Timer;
import java.util.TimerTask;

import static androidx.constraintlayout.widget.ConstraintSet.BOTTOM;

public class Ex04Activity extends BaseActivity  implements IEyeDistanceMeasureServiceCallback, SeekBar.OnSeekBarChangeListener {

    private final static String TAG = Ex04Activity.class.getSimpleName();

    private SeekBar SbRingSeek;
    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ImageView ivRingLeft;
    private ImageView ivRingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_04);

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
