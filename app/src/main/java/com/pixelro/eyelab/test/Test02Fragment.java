package com.pixelro.eyelab.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.account.AccountHelloFragment;
import com.pixelro.eyelab.distance.EyeDistanceMeasureService;

public class Test02Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    Handler h1;
    Handler h2;
    Handler h3;
    Handler h4;
    Handler h5;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_02, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        h1 = new Handler();
        h1.postDelayed(new Runnable(){
            @Override
            public void run() {
                NavHostFragment.findNavController(Test02Fragment.this).navigate(R.id.action_navigation_test_02_to_navigation_test_03);
            }
        }, 7500);

        h2 = new Handler();
        h2.postDelayed(new Runnable(){
            @Override
            public void run() {
                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("④");
            }
        }, 1500);
        h3 = new Handler();
        h3.postDelayed(new Runnable(){
            @Override
            public void run() {
                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("③");
            }
        }, 3000);
        h4 = new Handler();
        h4.postDelayed(new Runnable(){
            @Override
            public void run() {
                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("②");
            }
        }, 4500);
        h5 = new Handler();
        h5.postDelayed(new Runnable(){
            @Override
            public void run() {
                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setText("①");
                ((TextView)mView.findViewById(R.id.textView_test_02_count)).setTextColor(Color.rgb(255,0,0));
            }
        }, 6000);

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

        h1.removeCallbacksAndMessages(null);
        h2.removeCallbacksAndMessages(null);
        h3.removeCallbacksAndMessages(null);
        h4.removeCallbacksAndMessages(null);
        h5.removeCallbacksAndMessages(null);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                ((TextView)mView.findViewById(R.id.textView_test_02_distance)).setText(distance + "cm");

                Log.d(TAG, "????????????????????????????????");

                ((TestActivity)getActivity()).mCurrentDistance = distance;
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
        }
    }
}
