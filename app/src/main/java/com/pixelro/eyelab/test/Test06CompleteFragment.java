package com.pixelro.eyelab.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.account.AccountHelloFragment;

import java.util.Timer;
import java.util.TimerTask;

public class Test06CompleteFragment extends Fragment{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    public static Integer[] mDotProgress= {R.drawable.dot_3_1,R.drawable.dot_3_2,R.drawable.dot_3_3};
    public ImageView IvDotProgress;
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

        IvDotProgress = (ImageView)(view.findViewById(R.id.imageView_dot_progress));

        mTimer = new Timer();
        mTimer.schedule(new setDotProgress(1),700);
        mTimer.schedule(new setDotProgress(2),1400);
        mTimer.schedule(new goNext(),2100);
    }

    class setDotProgress extends TimerTask {

        private int mLevel;

        public setDotProgress(int level){
            mLevel = level;
        }

        @Override
        public void run() {
            if (mLevel == 0 || mLevel == 1 || mLevel == 2){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IvDotProgress.setImageResource(mDotProgress[mLevel]);
                    }
                });


            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimer.cancel();
    }

    class goNext extends TimerTask {

        @Override
        public void run() {
            NavHostFragment.findNavController(Test06CompleteFragment.this).navigate(R.id.action_navigation_test_06_complete_to_navigation_test_07_result);
        }
    }
}
