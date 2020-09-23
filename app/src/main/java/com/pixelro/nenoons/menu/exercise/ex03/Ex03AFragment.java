package com.pixelro.nenoons.menu.exercise.ex03;

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

public class Ex03AFragment extends Fragment implements View.OnClickListener{

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    private View mView;

    private Button BtnLevelL;
    private Button BtnLevelM;
    private Button BtnLevelH;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    protected SharedPreferencesManager mSm;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_03_a, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        view.findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        view.findViewById(R.id.button_ex_c_next).setOnClickListener(this);

        BtnLevelL = (Button)view.findViewById(R.id.btn_ex_c_l);
        BtnLevelM = (Button)view.findViewById(R.id.btn_ex_c_m);
        BtnLevelH = (Button)view.findViewById(R.id.btn_ex_c_h);

        BtnLevelL.setOnClickListener(this);
        BtnLevelM.setOnClickListener(this);
        BtnLevelH.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        resetLevelButton();
        int preLevel = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_3_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW);
        if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW){
            BtnLevelL.setEnabled(false);
        }
        else if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_MID){
            BtnLevelM.setEnabled(false);
        }
        else if (preLevel == EYELAB.APPDATA.EXERCISE.EX_LEVEL_HIGH){
            BtnLevelH.setEnabled(false);
        }

        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();
        Typeface face = mSm.getFontTypeface();
        BtnLevelH.setTypeface(face);
        BtnLevelL.setTypeface(face);
        BtnLevelM.setTypeface(face);
        ((Button)mView.findViewById(R.id.button_ex_c_next)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_c69)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_c76)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_c79)).setTypeface(face);
        ((TextView)mView.findViewById(R.id.textView_c81)).setTypeface(face);
    }

    private void resetLevelButton(){
        BtnLevelL.setEnabled(true);
        BtnLevelM.setEnabled(true);
        BtnLevelH.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_close_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_ex_c_next:

                // set time, level
                if (!BtnLevelL.isEnabled()){
                    ((Ex03Activity)getActivity()).mCurLevel = EX_LEVEL_L;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_3_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_LOW);
                    editor.commit();
                }

                if (!BtnLevelM.isEnabled()){
                    ((Ex03Activity)getActivity()).mCurLevel = EX_LEVEL_M;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_3_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_MID);
                    editor.commit();
                }
                if (!BtnLevelH.isEnabled()){
                    ((Ex03Activity)getActivity()).mCurLevel = EX_LEVEL_H;
                    editor.putInt(EYELAB.APPDATA.EXERCISE.EX_3_LEVEL,EYELAB.APPDATA.EXERCISE.EX_LEVEL_HIGH);
                    editor.commit();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_03, new Ex03BFragment()).commit();

                break;
             case R.id.btn_ex_c_l:
                resetLevelButton();
                BtnLevelL.setEnabled(false);
                break;
            case R.id.btn_ex_c_m:
                resetLevelButton();
                BtnLevelM.setEnabled(false);
                break;
            case R.id.btn_ex_c_h:
                resetLevelButton();
                BtnLevelH.setEnabled(false);
                break;
        }
    }
}
