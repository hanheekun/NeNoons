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
import com.pixelro.nenoons.account.AccountHelloFragment;

public class Test01Dis06Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

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

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev).setOnClickListener(this);

        ((TextView)view.findViewById(R.id.textView_test_03_distance)).setText(""+((TestActivity)getActivity()).mCurrentDistance+"cm");

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next:
                NavHostFragment.findNavController(Test01Dis06Fragment.this).navigate(R.id.action_navigation_test_01_dis_06_to_navigation_test_02_gr_01);
                break;
            case R.id.button_test_prev:
                getActivity().onBackPressed();
                break;
        }
    }
}