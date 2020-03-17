package com.pixelro.eyelab.account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pixelro.eyelab.R;

public class AccountSurveyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountSurveyFragment.class.getSimpleName();
    private View mView;

    // Get reference of widgets from XML layout
    Spinner SpGlasses;
    Spinner SpLeft;
    Spinner SpRight;
    Spinner SpStatus;
    Spinner SpSurgery;
    Spinner SpExcercise;
    Spinner SpFood;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_survey, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        SpGlasses = (Spinner)( view.findViewById(R.id.spinner1));
        SpLeft = (Spinner)( view.findViewById(R.id.spinner2));
        SpRight = (Spinner)( view.findViewById(R.id.spinner3));
        SpStatus = (Spinner)( view.findViewById(R.id.spinner4));
        SpSurgery = (Spinner)( view.findViewById(R.id.spinner5));
        SpExcercise = (Spinner)( view.findViewById(R.id.spinner6));
        SpFood = (Spinner)( view.findViewById(R.id.spinner7));


        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner1ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_glasses)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner2ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_left_right)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner3ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_left_right)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner4ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_status)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner5ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_surgery)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner6ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_exercise)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinner7ArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_food)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinner1ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpGlasses.setAdapter(spinner1ArrayAdapter);
        spinner2ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpLeft.setAdapter(spinner2ArrayAdapter);
        spinner3ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpRight.setAdapter(spinner3ArrayAdapter);
        spinner4ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpStatus.setAdapter(spinner4ArrayAdapter);
        spinner5ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpSurgery.setAdapter(spinner5ArrayAdapter);
        spinner6ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpExcercise.setAdapter(spinner6ArrayAdapter);
        spinner7ArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        SpFood.setAdapter(spinner7ArrayAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
        }
    }
}
