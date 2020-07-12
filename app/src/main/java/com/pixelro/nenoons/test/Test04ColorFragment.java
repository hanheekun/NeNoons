package com.pixelro.nenoons.test;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.account.AccountHelloFragment;

public class Test04ColorFragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    private ConstraintLayout ClColor1;
    private ConstraintLayout ClColor2;
    private ConstraintLayout ClColor3;
    private ConstraintLayout ClColor4;
    private ConstraintLayout ClColor5;
    private ConstraintLayout ClColor6;

    private FrameLayout FlColor1;
    private FrameLayout FlColor2;
    private FrameLayout FlColor3;
    private FrameLayout FlColor4;
    private FrameLayout FlColor5;
    private FrameLayout FlColor6;

    private FrameLayout FlColorLight1;
    private FrameLayout FlColorLight2;
    private FrameLayout FlColorLight3;
    private FrameLayout FlColorLight4;
    private FrameLayout FlColorLight5;
    private FrameLayout FlColorLight6;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_04_color, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev).setOnClickListener(this);

        ClColor1 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_1);
        ClColor2 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_2);
        ClColor3 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_3);
        ClColor4 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_4);
        ClColor5 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_5);
        ClColor6 = (ConstraintLayout)view.findViewById(R.id.constraintlayout_color_6);

        FlColor1 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_1);
        FlColor2 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_2);
        FlColor3 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_3);
        FlColor4 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_4);
        FlColor5 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_5);
        FlColor6 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_6);

        FlColorLight1 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_1);
        FlColorLight2 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_2);
        FlColorLight3 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_3);
        FlColorLight4 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_4);
        FlColorLight5 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_5);
        FlColorLight6 = (FrameLayout)view.findViewById(R.id.frameLayout_color_light_6);

        ClColor1.setOnClickListener(this);
        ClColor2.setOnClickListener(this);
        ClColor3.setOnClickListener(this);
        ClColor4.setOnClickListener(this);
        ClColor5.setOnClickListener(this);
        ClColor6.setOnClickListener(this);

    }

    private void setAllColorButtonReset(){
        FlColor1.setBackgroundResource(0);
        FlColor2.setBackgroundResource(0);
        FlColor3.setBackgroundResource(0);
        FlColor4.setBackgroundResource(0);
        FlColor5.setBackgroundResource(0);
        FlColor6.setBackgroundResource(0);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next:
                NavHostFragment.findNavController(Test04ColorFragment.this).navigate(R.id.action_navigation_test_04_color_to_navigation_test_05_font);
                break;
            case R.id.button_test_prev:
                getActivity().onBackPressed();
                break;
            case R.id.constraintlayout_color_1:
                setAllColorButtonReset();
                FlColor1.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable) FlColorLight1.getBackground()).getColor();
                break;
            case R.id.constraintlayout_color_2:
                setAllColorButtonReset();
                FlColor2.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColorLight2.getBackground()).getColor();
                break;
            case R.id.constraintlayout_color_3:
                setAllColorButtonReset();
                FlColor3.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColorLight3.getBackground()).getColor();
                break;
            case R.id.constraintlayout_color_4:
                setAllColorButtonReset();
                FlColor4.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColorLight4.getBackground()).getColor();
                break;
            case R.id.constraintlayout_color_5:
                setAllColorButtonReset();
                FlColor5.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColorLight5.getBackground()).getColor();
                break;
            case R.id.constraintlayout_color_6:
                setAllColorButtonReset();
                FlColor6.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColorLight6.getBackground()).getColor();
                break;

        }

        //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("다음을 눌러주세요.");
        ((Button)mView.findViewById(R.id.button_test_next)).setEnabled(true);
    }
}
