package com.pixelro.nenoons.account;

import android.os.Bundle;

import com.pixelro.nenoons.BaseActivity;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;

public class AccountActivity extends BaseActivity{

    private final static String TAG = AccountActivity.class.getSimpleName();

    public PersonalProfile mPersonalProfile = new PersonalProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

    }

}
