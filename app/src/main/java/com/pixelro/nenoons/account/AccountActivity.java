package com.pixelro.nenoons.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.Session;
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
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Toast.makeText(this, "onSessionOpened()", Toast.LENGTH_SHORT).show();
//
//        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            Session.getCurrentSession().isOpened()
//            return;
//        }
//
//    }

}
