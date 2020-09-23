package com.pixelro.nenoons.menu.exercise.ex02;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;

import static android.content.Context.MODE_PRIVATE;

public class Ex02AFragment extends Fragment implements View.OnClickListener{

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    private View mView;
    protected SharedPreferencesManager mSm;

    private Button BtnLevelL_b;
    private Button BtnLevelM_b;
    private Button BtnLevelH_b;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_02_a, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_ex_b_next).setOnClickListener(this);

        BtnLevelL_b = (Button)view.findViewById(R.id.btn_ex_b_l);
        BtnLevelM_b = (Button)view.findViewById(R.id.btn_ex_b_m);
        BtnLevelH_b = (Button)view.findViewById(R.id.btn_ex_b_h);

        BtnLevelL_b.setOnClickListener(this);
        BtnLevelM_b.setOnClickListener(this);
        BtnLevelH_b.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        resetLevelButton();
        int preLevel = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_2_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW);
        if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW){
            BtnLevelL_b.setEnabled(false);
        }
        else if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_MID){
            BtnLevelM_b.setEnabled(false);
        }
        else if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_HIGH){
            BtnLevelH_b.setEnabled(false);
        }

        mSm = new SharedPreferencesManager(getActivity());
    }


    @Override
    public void onResume(){
        super.onResume();
        Typeface face = mSm.getFontTypeface();
        BtnLevelH_b.setTypeface(face);
        BtnLevelL_b.setTypeface(face);
        BtnLevelM_b.setTypeface(face);
        ((Button)mView.findViewById(R.id.button_ex_b_next)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_b69)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_b76)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_b79)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_b81)).setTypeface(face);
    }

    private void resetLevelButton(){
        BtnLevelL_b.setEnabled(true);
        BtnLevelM_b.setEnabled(true);
        BtnLevelH_b.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_close_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_ex_b_next:

                // set time, level
                if (!BtnLevelL_b.isEnabled()){
                    ((Ex02Activity)getActivity()).mCurLevel = EX_LEVEL_L;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_2_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW);
                    editor.commit();
                }

                if (!BtnLevelM_b.isEnabled()){
                    ((Ex02Activity)getActivity()).mCurLevel = EX_LEVEL_M;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_2_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_MID);
                    editor.commit();
                }
                if (!BtnLevelH_b.isEnabled()){
                    ((Ex02Activity)getActivity()).mCurLevel = EX_LEVEL_H;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_2_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_HIGH);
                    editor.commit();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02BFragment()).commit();

                break;
             case R.id.btn_ex_b_l:
                resetLevelButton();
                BtnLevelL_b.setEnabled(false);
                break;
            case R.id.btn_ex_b_m:
                resetLevelButton();
                BtnLevelM_b.setEnabled(false);
                break;
            case R.id.btn_ex_b_h:
                resetLevelButton();
                BtnLevelH_b.setEnabled(false);
                break;
        }
    }
}
