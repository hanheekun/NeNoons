package com.pixelro.nenoons.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.Profile;
import com.pixelro.nenoons.R;

public class AccountProfileFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private final static String TAG = AccountProfileFragment.class.getSimpleName();
    private View mView;
    DatePickerDialog mDialog;

    private EditText EtName;
    private EditText EtGender;
    private EditText EtBirthday;
    private EditText Etjob;
    private EditText EtPhoneNumber;

    private Profile mProfile;

    // profile
    String mName;
    String mGender;
    String mBirthday;
    String mJob;
    String mPhoneNumber;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        mProfile = ((AccountActivity)getActivity()).mProfile;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_account_profile_next).setOnClickListener(this);

        EtName = (EditText)(view.findViewById(R.id.editText_account_profile_name));
        EtGender = (EditText)(view.findViewById(R.id.editText_account_profile_gender));
        EtGender.setOnFocusChangeListener(this);
        EtBirthday = (EditText)(view.findViewById(R.id.editText_account_profile_birthday));
        EtBirthday.setOnFocusChangeListener(this);
        Etjob = (EditText)(view.findViewById(R.id.editText_account_profile_job));
        EtPhoneNumber = (EditText)(view.findViewById(R.id.editText_account_profile_phone));

        mDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, DatePickerListener, 1970, 1, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.button_account_profile_next:

                // 정보 입력 완료 // 아직 내용 check 안함
                mProfile.name = EtName.getText().toString();
                mProfile.gender = EtGender.getText().toString();
                mProfile.birthday = EtBirthday.getText().toString();
                mProfile.job = Etjob.getText().toString();
                mProfile.password = EtPhoneNumber.getText().toString();

                // 다음 페이지 전환
                NavHostFragment.findNavController(AccountProfileFragment.this).navigate(R.id.action_navigation_account_profile_to_navigation_account_survey);

                break;
        }
    }

    private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EtBirthday.setText( year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
        }
    };


    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.editText_account_profile_gender:
                if (b){
                    CharSequence info[] = new CharSequence[]{getString(R.string.account_join_profile_gender_male), getString(R.string.account_join_profile_gender_female)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(info, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    EtGender.setText(R.string.account_join_profile_gender_male);
                                    break;
                                case 1:
                                    EtGender.setText(R.string.account_join_profile_gender_female);
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
            case R.id.editText_account_profile_birthday:
                if (b){
                    mDialog.show();
                }
                break;
        }
    }
}