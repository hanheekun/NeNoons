package com.pixelro.eyelab.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.R;

public class AccountProfileFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountProfileFragment.class.getSimpleName();
    private View mView;
    DatePickerDialog mDialog;

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

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.button_account_profile_next).setOnClickListener(this);
        view.findViewById(R.id.editText_account_profile_gender).setOnClickListener(this);
        view.findViewById(R.id.editText_account_profile_birthday).setOnClickListener(this);

        mDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, listener, 1970, 1, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.editText_account_profile_gender:
                CharSequence info[] = new CharSequence[]{getString(R.string.account_join_profile_gender_male), getString(R.string.account_join_profile_gender_female)};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                break;
                            case 1:

                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.editText_account_profile_birthday:
                mDialog.show();
                break;
            case R.id.button_account_profile_next:
                NavHostFragment.findNavController(AccountProfileFragment.this).navigate(R.id.action_navigation_account_profile_to_navigation_account_survey);

                // data 가져오는 부분 추가 예정

                break;
        }
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //TvBirthday.setText( year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
        }
    };


}
