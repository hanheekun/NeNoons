package com.pixelro.eyelab.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.MainActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.SplashActivity;

public class AccountLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountLoginFragment.class.getSimpleName();
    private View mView;

    Button BtnLogin;
    EditText EtEmail;
    EditText EtPass;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.textView_account_login_forget).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_facebook).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_google).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_qq).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_kakao).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_naver).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_wechat).setOnClickListener(this);
        view.findViewById(R.id.button_account_login_login).setOnClickListener(this);


        BtnLogin = (Button)(getActivity().findViewById(R.id.button_account_login_login));
        BtnLogin.setOnClickListener(this);


//        EtEmail = (EditText)(getActivity().findViewById(R.id.editText_account_login_email));
//        EtEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(EtEmail.length() > 0 && EtPass.length() > 0){
//                    BtnLogin.setEnabled(true);
//                }
//                else {
//                    BtnLogin.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        EtPass = (EditText)(getActivity().findViewById(R.id.editText_account_login_password));
//        EtPass.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(EtEmail.length() > 0 && EtPass.length() > 0 ){
//                    BtnLogin.setEnabled(true);
//                }
//                else {
//                    BtnLogin.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.textView_account_login_forget:
                NavHostFragment.findNavController(AccountLoginFragment.this).navigate(R.id.action_navigation_account_login_to_navigation_account_find);
                break;
            case R.id.imageButton_account_login_facebook:
                break;
            case R.id.imageButton_account_login_google:
                break;
            case R.id.imageButton_account_login_qq:
                break;
            case R.id.imageButton_account_login_kakao:
                break;
            case R.id.imageButton_account_login_naver:
                break;
            case R.id.imageButton_account_login_wechat:
                break;
            case R.id.button_account_login_login:
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();
                break;



        }
    }
}
