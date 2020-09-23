package com.pixelro.nenoons.test;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.account.AccountHelloFragment;

public class Test02RedGreen01Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    protected SharedPreferencesManager mSm;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_02_rg_01, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next_rg1).setOnClickListener(this);
        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();
        Typeface face = mSm.getFontTypeface();
        ((TextView)mView.findViewById(R.id.button_test_next_rg1)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView37_rg1)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_test_02_command_rg1)).setTypeface(face);


    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_close_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next_rg1:
                NavHostFragment.findNavController(Test02RedGreen01Fragment.this).navigate(R.id.action_navigation_test_02_gr_01_to_navigation_test_02_gr_02);
                break;
        }
    }
}
