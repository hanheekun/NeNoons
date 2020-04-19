package com.pixelro.eyelab.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.account.AccountHelloFragment;

public class Test07Fragment extends Fragment  implements View.OnClickListener{

    private final static String TAG = AccountHelloFragment.class.getSimpleName();
    private View mView;

    private FrameLayout Flfont1;
    private FrameLayout Flfont2;
    private FrameLayout Flfont3;
    private FrameLayout Flfont4;
    private TextView Tvfont1;
    private TextView Tvfont2;
    private TextView Tvfont3;
    private TextView Tvfont4;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_07, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);
        view.findViewById(R.id.button_test_prev).setOnClickListener(this);

        Flfont1 = (FrameLayout)view.findViewById(R.id.frameLayout_font_1);
        Flfont2 = (FrameLayout)view.findViewById(R.id.frameLayout_font_2);
        Flfont3 = (FrameLayout)view.findViewById(R.id.frameLayout_font_3);
        Flfont4 = (FrameLayout)view.findViewById(R.id.frameLayout_font_4);
        Flfont1.setOnClickListener(this);
        Flfont2.setOnClickListener(this);
        Flfont3.setOnClickListener(this);
        Flfont4.setOnClickListener(this);
        Flfont1.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont2.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont3.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);
        Flfont4.setBackgroundColor(((TestActivity)getActivity()).mCurrentSelectedColor);

        Tvfont1 = (TextView)view.findViewById(R.id.textView_font_1);
        Tvfont2 = (TextView)view.findViewById(R.id.textView_font_2);
        Tvfont3 = (TextView)view.findViewById(R.id.textView_font_3);
        Tvfont4 = (TextView)view.findViewById(R.id.textView_font_4);
    }

    private void setAllColorButtonReset(){
        Tvfont1.setBackgroundResource(0);
        Tvfont2.setBackgroundResource(0);
        Tvfont3.setBackgroundResource(0);
        Tvfont4.setBackgroundResource(0);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_test_next:
                NavHostFragment.findNavController(Test07Fragment.this).navigate(R.id.navigation_test_08);
                break;
            case R.id.button_test_prev:
                getActivity().onBackPressed();
                break;
            case R.id.frameLayout_font_1:
                setAllColorButtonReset();
                Tvfont1.setBackgroundResource(R.drawable.test_color_outline);
                break;
            case R.id.frameLayout_font_2:
                setAllColorButtonReset();
                Tvfont2.setBackgroundResource(R.drawable.test_color_outline);
                break;
            case R.id.frameLayout_font_3:
                setAllColorButtonReset();
                Tvfont3.setBackgroundResource(R.drawable.test_color_outline);
                break;
            case R.id.frameLayout_font_4:
                setAllColorButtonReset();
                Tvfont4.setBackgroundResource(R.drawable.test_color_outline);
                break;
        }
    }
}
