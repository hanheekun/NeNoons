package com.pixelro.nenoons.test;

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
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountHelloFragment;

public class Test05FontFragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    private ConstraintLayout Flfont1;
    private ConstraintLayout Flfont2;
    private ConstraintLayout Flfont3;
    private ConstraintLayout Flfont4;

    private FrameLayout FlFont1;
    private FrameLayout FlFont2;
    private FrameLayout FlFont3;
    private FrameLayout FlFont4;
    private FrameLayout FlFont5;
    private FrameLayout FlFont6;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_05_font, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev).setOnClickListener(this);

        Flfont1 = (ConstraintLayout)view.findViewById(R.id.frameLayout_font_1);
        Flfont2 = (ConstraintLayout)view.findViewById(R.id.frameLayout_font_2);
        Flfont3 = (ConstraintLayout)view.findViewById(R.id.frameLayout_font_3);
        Flfont4 = (ConstraintLayout)view.findViewById(R.id.frameLayout_font_4);

        Flfont1.setOnClickListener(this);
        Flfont2.setOnClickListener(this);
        Flfont3.setOnClickListener(this);
        Flfont4.setOnClickListener(this);

        Flfont1.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont2.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont3.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont4.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);

        FlFont1 = (FrameLayout)view.findViewById(R.id.frameLayout_font_box_1);
        FlFont2 = (FrameLayout)view.findViewById(R.id.frameLayout_font_box_2);
        FlFont3 = (FrameLayout)view.findViewById(R.id.frameLayout_font_box_3);
        FlFont4 = (FrameLayout)view.findViewById(R.id.frameLayout_font_box_4);
    }

    private void setAllColorButtonReset(){
        FlFont1.setBackgroundResource(0);
        FlFont2.setBackgroundResource(0);
        FlFont3.setBackgroundResource(0);
        FlFont4.setBackgroundResource(0);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next:
                NavHostFragment.findNavController(Test05FontFragment.this).navigate(R.id.action_navigation_test_05_font_to_navigation_test_06_complete);
                break;
            case R.id.button_test_prev:
                getActivity().onBackPressed();
                break;
            case R.id.frameLayout_font_1:
                setAllColorButtonReset();
                FlFont1.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mTestProfile.font = TestProfile.Font.FONT_1;
                break;
            case R.id.frameLayout_font_2:
                setAllColorButtonReset();
                FlFont2.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mTestProfile.font = TestProfile.Font.FONT_2;
                break;
            case R.id.frameLayout_font_3:
                setAllColorButtonReset();
                FlFont3.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mTestProfile.font = TestProfile.Font.FONT_3;
                break;
            case R.id.frameLayout_font_4:
                setAllColorButtonReset();
                FlFont4.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mTestProfile.font = TestProfile.Font.FONT_4;
                break;
        }

        //((TextView)mView.findViewById(R.id.textView_test_02_command)).setText("완료를 눌러주세요.");
        ((Button)mView.findViewById(R.id.button_test_next)).setEnabled(true);
    }
}
