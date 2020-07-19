package com.pixelro.nenoons.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.account.AccountHelloFragment;

import java.util.Timer;
import java.util.TimerTask;

public class Test06CompleteFragment extends Fragment{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    public Timer mTimer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_06_complete, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                NavHostFragment.findNavController(Test06CompleteFragment.this).navigate(R.id.action_navigation_test_06_complete_to_navigation_test_07_result);
            }
        },2500);
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimer.cancel();
    }

}
