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
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountHelloFragment;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;

public class Test02RedGreen02Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    RadioGroup rg;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);

                if(distance !=0)
                ((TextView)mView.findViewById(R.id.textView_test_02_distance_rg2)).setText(distance + "cm");
                //((TestActivity)getActivity()).mCurrentDistance = distance;
            }
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_02_rg_02, container, false);
    }
    protected SharedPreferencesManager mSm;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next_rg2).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev).setOnClickListener(this);

        rg = (RadioGroup)mView.findViewById(R.id.radioGroup_test_02);
        rg.setOnClickListener(this);

        view.findViewById(R.id.radioButton_test_02_red).setOnClickListener(this);
        view.findViewById(R.id.radioButton_test_02_green).setOnClickListener(this);

        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

            Typeface face = mSm.getFontTypeface();
            ((TextView)mView.findViewById(R.id.button_test_next_rg2)).setTypeface(face);
            ((TextView)mView.findViewById(R.id.textView37_rg2)).setTypeface(face);
            ((TextView)mView.findViewById(R.id.textView66_rg2)).setTypeface(face);
            ((TextView)mView.findViewById(R.id.textView_test_02_command_rg2)).setTypeface(face);


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
            case R.id.button_test_next_rg2:
                if (rg.getCheckedRadioButtonId() == 0){
                    ((TestActivity)getActivity()).mTestProfile.redgreen = TestProfile.Redgreen.RED;
                }
                else {
                    ((TestActivity)getActivity()).mTestProfile.redgreen = TestProfile.Redgreen.GREEN;
                }
                NavHostFragment.findNavController(Test02RedGreen02Fragment.this).navigate(R.id.action_navigation_test_02_gr_02_to_navigation_test_03_bright_01);
                break;
            case R.id.button_test_prev:
                getActivity().onBackPressed();
                break;
        }

        if ( rg.getCheckedRadioButtonId() != -1){
            //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("다음을 눌러주세요.");
            ((Button)mView.findViewById(R.id.button_test_next_rg2)).setEnabled(true);
        }

    }
}
