package com.pixelro.eyelab.test;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.account.AccountHelloFragment;

public class Test04ColorFragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;
    private ConstraintLayout ClColor1;
    private FrameLayout FlColor1;
    private FrameLayout FlColor2;
    private FrameLayout FlColor3;
    private FrameLayout FlColor4;
    private FrameLayout FlColor5;
    private FrameLayout FlColor6;
    private FrameLayout FlColor7;
    private TextView TvColor1;
    private TextView TvColor2;
    private TextView TvColor3;
    private TextView TvColor4;
    private TextView TvColor5;
    private TextView TvColor6;

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
        FlColor2 = (FrameLayout)view.findViewById(R.id.frameLayout_color_2);
        FlColor3 = (FrameLayout)view.findViewById(R.id.frameLayout_color_3);
        FlColor4 = (FrameLayout)view.findViewById(R.id.frameLayout_color_4);
        FlColor5 = (FrameLayout)view.findViewById(R.id.frameLayout_color_5);
        FlColor6 = (FrameLayout)view.findViewById(R.id.frameLayout_color_6);
        ClColor1.setOnClickListener(this);
        FlColor2.setOnClickListener(this);
        FlColor3.setOnClickListener(this);
        FlColor4.setOnClickListener(this);
        FlColor5.setOnClickListener(this);
        FlColor6.setOnClickListener(this);
        FlColor1 = (FrameLayout)view.findViewById(R.id.frameLayout_color_box_1);
//        TvColor1 = (TextView)view.findViewById(R.id.textView_color_1);
//        TvColor2 = (TextView)view.findViewById(R.id.textView_color_2);
//        TvColor3 = (TextView)view.findViewById(R.id.textView_color_3);
//        TvColor4 = (TextView)view.findViewById(R.id.textView_color_4);
//        TvColor5 = (TextView)view.findViewById(R.id.textView_color_5);
//        TvColor6 = (TextView)view.findViewById(R.id.textView_color_6);

    }

    private void setAllColorButtonReset(){
        FlColor1.setBackgroundResource(0);
//        TvColor2.setBackgroundResource(0);
//        TvColor3.setBackgroundResource(0);
//        TvColor4.setBackgroundResource(0);
//        TvColor5.setBackgroundResource(0);
//        TvColor6.setBackgroundResource(0);
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
                //((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable) FlColor1.getBackground()).getColor();
                break;
            case R.id.frameLayout_color_2:
                setAllColorButtonReset();
                TvColor2.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColor2.getBackground()).getColor();
                break;
            case R.id.frameLayout_color_3:
                setAllColorButtonReset();
                TvColor3.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColor3.getBackground()).getColor();
                break;
            case R.id.frameLayout_color_4:
                setAllColorButtonReset();
                TvColor4.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColor4.getBackground()).getColor();
                break;
            case R.id.frameLayout_color_5:
                setAllColorButtonReset();
                TvColor5.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColor5.getBackground()).getColor();
                break;
            case R.id.frameLayout_color_6:
                setAllColorButtonReset();
                TvColor6.setBackgroundResource(R.drawable.test_color_outline);
                ((TestActivity)getActivity()).mCurrentSelectedColor = ((ColorDrawable)FlColor6.getBackground()).getColor();
                break;

        }
    }
}
