package com.pixelro.nenoons.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountHelloFragment;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

public class Test03Bright02Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                if(distance !=0)
                ((TextView)mView.findViewById(R.id.textView_test_03_distance_br2)).setText(distance + "cm");
                //((TestActivity)getActivity()).mCurrentDistance = distance;
            }
        }
    };
    protected SharedPreferencesManager mSm;
    private TextView Tv_br1, Tv_br2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_03_bright_02, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next_br2).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev_br2).setOnClickListener(this);

        view.findViewById(R.id.imageView17).setOnClickListener(this);
        view.findViewById(R.id.imageView21).setOnClickListener(this);
        view.findViewById(R.id.imageView22).setOnClickListener(this);
        view.findViewById(R.id.imageView23).setOnClickListener(this);

        Tv_br1 = (TextView) view.findViewById(R.id.textView37_br2);
        Tv_br2 = (TextView) view.findViewById(R.id.textView_test_02_command_br2);

        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        Typeface face = mSm.getFontTypeface();
        Tv_br1.setTypeface(face);
        Tv_br1.setTypeface(face);
        ((Button)mView.findViewById(R.id.button_test_next_br2)).setTypeface(face);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_close_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next_br2:
                ((TestActivity)getActivity()).mTestProfile.bright = 0;
                if (((CheckBox)mView.findViewById(R.id.imageView17)).isChecked()){
                    ((TestActivity)getActivity()).mTestProfile.bright += TestProfile.Bright.BRIGHT_1;
                    ((TestActivity)getActivity()).mTestProfile.brightNumber++;
                }
                if (((CheckBox)mView.findViewById(R.id.imageView21)).isChecked()){
                    ((TestActivity)getActivity()).mTestProfile.bright += TestProfile.Bright.BRIGHT_2;
                    ((TestActivity)getActivity()).mTestProfile.brightNumber++;
                }
                if (((CheckBox)mView.findViewById(R.id.imageView22)).isChecked()){
                    ((TestActivity)getActivity()).mTestProfile.bright += TestProfile.Bright.BRIGHT_3;
                    ((TestActivity)getActivity()).mTestProfile.brightNumber++;
                }
                if (((CheckBox)mView.findViewById(R.id.imageView23)).isChecked()){
                    ((TestActivity)getActivity()).mTestProfile.bright += TestProfile.Bright.BRIGHT_4;
                    ((TestActivity)getActivity()).mTestProfile.brightNumber++;
                }

                NavHostFragment.findNavController(Test03Bright02Fragment.this).navigate(R.id.action_navigation_test_03_diff_to_navigation_test_04_color);
                break;
            case R.id.button_test_prev_br2:
                getActivity().onBackPressed();
                break;
        }

        //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("다음을 눌러주세요.");
        ((Button)mView.findViewById(R.id.button_test_next_br2)).setEnabled(true);
    }
}
