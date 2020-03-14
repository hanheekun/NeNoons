package com.pixelro.eyelab.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.login.LoginHelloFragment;

public class Test01Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = LoginHelloFragment.class.getSimpleName();
    private View mView;

    Adapter adapter;
    ViewPager viewPager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_01, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_01_next).setOnClickListener(this);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                NavHostFragment.findNavController(FirstFragment.this)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });

        viewPager = (ViewPager)(getActivity().findViewById(R.id.viewpager_test_guide));
        adapter = new Adapter(getContext());
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().finish();
                //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
                break;
            case R.id.button_test_01_next:
                //NavHostFragment.findNavController(Test01Fragment.this).navigate(R.id.action_navigation_test_01_to_navigation_test_02);

                NavHostFragment.findNavController(Test01Fragment.this).navigate(R.id.navigation_test_02);

                break;
        }
    }
}
