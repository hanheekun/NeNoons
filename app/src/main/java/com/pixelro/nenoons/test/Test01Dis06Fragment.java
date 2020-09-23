package com.pixelro.nenoons.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.account.AccountHelloFragment;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;

import org.w3c.dom.Text;


public class Test01Dis06Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    protected SharedPreferencesManager mSm;
    Adapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_01_dis_06, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next_06).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev_06).setOnClickListener(this);

        if(((TestActivity)getActivity()).mCurrentDistance !=0)
        ((TextView)view.findViewById(R.id.textView_test_03_distance)).setText(""+((TestActivity)getActivity()).mCurrentDistance+"cm");

        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();
        Typeface face =mSm.getFontTypeface();

        ((TextView)mView.findViewById(R.id.textView37_06)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView37_06)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_test_02_command_06)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView39_06)).setTypeface(face);
        ((Button)mView.findViewById(R.id.button_test_next_06)).setTypeface(face);
        ((Button)mView.findViewById(R.id.button_test_prev_06)).setTypeface(face);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_close_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next_06:
                NavHostFragment.findNavController(Test01Dis06Fragment.this).navigate(R.id.action_navigation_test_01_dis_06_to_navigation_test_02_gr_01);
                break;
            case R.id.button_test_prev_06:
                getActivity().onBackPressed();
                break;
        }
    }
}
