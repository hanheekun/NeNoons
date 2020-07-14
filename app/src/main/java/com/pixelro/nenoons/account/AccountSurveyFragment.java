package com.pixelro.nenoons.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;

public class AccountSurveyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountSurveyFragment.class.getSimpleName();
    private View mView;

    private PersonalProfile mPersonalProfile;

    // Get reference of widgets from XML layout
    Spinner SpLeft;
    Spinner SpRight;

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

        mPersonalProfile = ((AccountActivity)getActivity()).mPersonalProfile;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_account_survey_next).setOnClickListener(this);


        SpLeft = (Spinner)( view.findViewById(R.id.spinner_survey_left));
        SpRight = (Spinner)( view.findViewById(R.id.spinner_survey_right));

        SurveyAdapter adapter = new SurveyAdapter(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_left));
        SpLeft.setAdapter(adapter);
        adapter = new SurveyAdapter(getContext(),R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_right));
        SpRight.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_account_survey_next:

                // profile에 정보 입력 // 아직 내용 check 안함
                if(((RadioButton)mView.findViewById(R.id.radioButton_glasses_1)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.NONE;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_glasses_2)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.GLASSESS;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_glasses_3)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.FAR_VISION;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_glasses_4)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.CONTACT;
                }

                mPersonalProfile.left = (String)SpLeft.getSelectedItem();
                mPersonalProfile.right = (String)SpRight.getSelectedItem();

                mPersonalProfile.status = "";
                if(((CheckBox)mView.findViewById(R.id.checkBox_status_1)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.MYOPIA + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_status_2)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.EMMETROPIA + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_status_3)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.ASTIGMATISM + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_status_4)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.HYPEROPIA + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_status_5)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.UNKNOWN + " ";
                }

                mPersonalProfile.surgery = "";
                if(((CheckBox)mView.findViewById(R.id.checkBox_surgery_1)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.LASIKLASEK + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_surgery_2)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.OLD + " ";
                }
                if(((CheckBox)mView.findViewById(R.id.checkBox_surgery_3)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.CATARACT + " ";
                }

                if(((RadioButton)mView.findViewById(R.id.radioButton_exercise_1)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.YES;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_exercise_2)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.NO;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_exercise_3)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.SOMETIMES;
                }

                if(((RadioButton)mView.findViewById(R.id.radioButton_food_1)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.YES;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_food_2)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.NO;
                }
                else if(((RadioButton)mView.findViewById(R.id.radioButton_food_3)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.SOMETIMES;
                }

                ////////////////////////////////////////////////////////////
                // 회원 정보 전달
                ////////////////////////////////////////////////////////////
                //String name = mProfile.name0;



                // main 으로 이동
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();

                // 로그인 페이지 전환
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.nav_login_fragment, new AccountLoginFragment()).commit();

                break;


        }
    }

    // 선택되면 글씨 색 바꾸기
    class SurveyAdapter extends ArrayAdapter<String>{

        public SurveyAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

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
                tv.setTextColor(Color.rgb(1,67,190));
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;

            if (position == 0) {
                tv.setTextColor(Color.BLACK);
            }
            else {
                tv.setTextColor(Color.rgb(1,67,190));
            }
            return view;
        }
    }
}
