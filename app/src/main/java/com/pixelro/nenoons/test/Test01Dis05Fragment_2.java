package com.pixelro.nenoons.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.account.AccountHelloFragment;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

import java.util.Timer;
import java.util.TimerTask;

public class Test01Dis05Fragment_2 extends Fragment  implements View.OnClickListener, SensorEventListener {

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    private TextView mTvMoving;
    private TextView mTvCount;
    private ConstraintLayout Cl;

    public Timer mTimer;

//    Handler h1;
//    Handler h2;
//    Handler h3;
//    Handler h4;
//    Handler h5;

    // 294129 979797

    // for sensor
    private SensorManager mSensorManager = null;
    private double preSum = 0;
    private double preX = 0;
    private double preY = 0;
    private double preZ = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_01_dis_05_2, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        mTvMoving = (TextView)view.findViewById(R.id.textView_test_02_moving);
        mTvCount = (TextView)mView.findViewById(R.id.textView_test_02_count);
        Cl = (ConstraintLayout)view.findViewById(R.id.layout_test_01_bg);

        view.findViewById(R.id.button_test_next).setOnClickListener(this);

        //Using the Gyroscope & Accelometer
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);


//        h1 = new Handler();
//        h1.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                NavHostFragment.findNavController(Test02Fragment.this).navigate(R.id.action_navigation_test_02_to_navigation_test_03);
//            }
//        }, 7500);
//
//        h2 = new Handler();
//        h2.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("④");
//            }
//        }, 1500);
//        h3 = new Handler();
//        h3.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("③");
//            }
//        }, 3000);
//        h4 = new Handler();
//        h4.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("②");
//            }
//        }, 4500);
//        h5 = new Handler();
//        h5.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("①");
//                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setTextColor(Color.rgb(255,0,0));
//            }
//        }, 6000);

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

        mSensorManager.unregisterListener(this);

//        h1.removeCallbacksAndMessages(null);
//        h2.removeCallbacksAndMessages(null);
//        h3.removeCallbacksAndMessages(null);
//        h4.removeCallbacksAndMessages(null);
//        h5.removeCallbacksAndMessages(null);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                ((TextView)mView.findViewById(R.id.textView_test_01_distance)).setText(distance + "cm");
                ((TestActivity)getActivity()).mCurrentDistance_2 = distance;
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
                break;
            case R.id.button_test_next:
                //NavHostFragment.findNavController(Test01Dis03Fragment.this).navigate(R.id.action_navigation_test_01_dis_03_to_navigation_test_01_dis_04);
                NavHostFragment.findNavController(Test01Dis05Fragment_2.this).navigate(R.id.action_navigation_test_01_dis_05_2_to_navigation_test_01_dis_06_2);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            int sum = (int)((Math.abs(preX - x) + Math.abs(preY - y) + Math.abs(preZ - z))*100);

            //double mAccelCurrent = Math.sqrt(x*x + y*y + z*z);

            if (sum > 60){
                //mTvMoving.setText("moving...");
                mTvMoving.setText("");

                if (mTimer != null){
                    mTimer.cancel();
                    mTimer = null;
                    mTvCount.setText("");
                }

                //Cl.setBackgroundColor(Color.parseColor("#979797"));

            }
            else{
                mTvMoving.setText("");
                //Cl.setBackgroundColor(Color.parseColor("#294129"));
                if (mTimer == null){
                    mTimer = new Timer();
                    mTimer.schedule(new setCountTextView(3),1000);
                    mTimer.schedule(new setCountTextView(2),2000);
                    mTimer.schedule(new setCountTextView(1),3000);
                    mTimer.schedule(new setCountTextView(0),4000);
                }
            }

            preX = x;
            preY = y;
            preZ = z;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class setCountTextView extends TimerTask {

        private int mCount;

        public setCountTextView(int count){
            mCount = count;
        }

        @Override
        public void run() {
            if (mCount == 3)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCount.setText("③");
                        mTvCount.setTextColor(Color.rgb(255,255,255));
                    }
                });
            }
            else if (mCount == 2)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCount.setText("②");
                        mTvCount.setTextColor(Color.rgb(255,255,255));
                    }
                });

            }
            else if (mCount == 1)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCount.setText("①");
                        mTvCount.setTextColor(Color.parseColor("#ffe800"));
                    }
                });
            }
            else if (mCount == 0)
            {
                NavHostFragment.findNavController(Test01Dis05Fragment_2.this).navigate(R.id.action_navigation_test_01_dis_05_2_to_navigation_test_01_dis_06_2);
            }
        }
    }


}
