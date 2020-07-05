package com.pixelro.eyelab.menu.exercise.ex02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ex01.Ex01AFragment;

import static android.content.Context.MODE_PRIVATE;

public class Ex02CFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_02_c, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_test_prev).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,true);
        editor.commit();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_test_prev:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02AFragment()).commit();
                break;
            case R.id.button_test_next:
                getActivity().finish();
                break;
        }
    }
}
