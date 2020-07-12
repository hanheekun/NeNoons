package com.pixelro.eyelab.account;

import android.os.Bundle;

import com.pixelro.eyelab.BaseActivity;
import com.pixelro.eyelab.Profile;
import com.pixelro.eyelab.R;

public class AccountActivity extends BaseActivity{

    private final static String TAG = AccountActivity.class.getSimpleName();

    public Profile mProfile = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

    }

}
