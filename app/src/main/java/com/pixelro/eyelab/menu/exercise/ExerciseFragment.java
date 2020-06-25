package com.pixelro.eyelab.menu.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.exercise.ex01.Ex01Activity;
import com.pixelro.eyelab.menu.exercise.history.ExHistoryActivity;

public class ExerciseFragment extends Fragment implements View.OnClickListener {

    private ExerciseViewModel exerciseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        root.findViewById(R.id.button_ex_01).setOnClickListener(this);
        root.findViewById(R.id.button_ex_02).setOnClickListener(this);
        root.findViewById(R.id.button_ex_03).setOnClickListener(this);
        root.findViewById(R.id.button_ex_04).setOnClickListener(this);
        root.findViewById(R.id.button_ex_history).setOnClickListener(this);



        return root;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.button_ex_01:
                Intent intent = new Intent(getActivity(), Ex01Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_02:
                intent = new Intent(getActivity(), Ex02Activity2.class);
                startActivity(intent);
                break;
            case R.id.button_ex_03:
                intent = new Intent(getActivity(), Ex03Activity2.class);
                startActivity(intent);
                break;
            case R.id.button_ex_04:
                intent = new Intent(getActivity(), Ex04Activity2.class);
                startActivity(intent);
                break;
            case R.id.button_ex_history:
                intent = new Intent(getActivity(), ExHistoryActivity.class);
                startActivity(intent);
                break;
        }

    }
}
