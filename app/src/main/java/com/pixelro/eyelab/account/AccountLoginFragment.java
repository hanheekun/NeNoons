package com.pixelro.eyelab.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.pixelro.eyelab.FirstDialog;
import com.pixelro.eyelab.MainActivity;
import com.pixelro.eyelab.Profile;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.test.TestActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static android.content.Context.MODE_PRIVATE;

public class AccountLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountLoginFragment.class.getSimpleName();
    private View mView;

    private Button BtnLogin;
    private EditText EtEmail;
    private EditText EtPass;
    private Switch SwLoginSave;

    //private SharedPreferences appData;

    private String masterKeyAlias;
    private SharedPreferences sharedPreferences;

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

        // test
        view.findViewById(R.id.button_login_test_1).setOnClickListener(this);
        view.findViewById(R.id.button_login_test_2).setOnClickListener(this);

        SwLoginSave = (Switch)(view.findViewById(R.id.switch_account_login_save));

        BtnLogin = (Button)(getActivity().findViewById(R.id.button_account_login_login));
        BtnLogin.setOnClickListener(this);

        EtEmail = (EditText)(getActivity().findViewById(R.id.editText_account_login_email));
        EtPass = (EditText)(getActivity().findViewById(R.id.editText_account_login_password));

        //edit 창에 입력이 있어야지만 로그인 버는 활성화
        EtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(EtEmail.length() > 0 && EtPass.length() > 0){
                    BtnLogin.setEnabled(true);
                }
                else {
                    BtnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(EtEmail.length() > 0 && EtPass.length() > 0 ){
                    BtnLogin.setEnabled(true);
                }
                else {
                    BtnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 로그인 정보 저장
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharedPreferences = getActivity().getSharedPreferences("appData", MODE_PRIVATE);
        load();

        // test
        String a =  Profile.AppData.EMAIL;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.textView_account_login_forget:    // 비밀번호 찾기
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

                // 로그인 성공

                //http set header
                    //post


                // 메인 화면 전환
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();

                // 로그인 저장
                save();

                break;
            case R.id.button_login_test_1:

                FirstDialog dlg = new FirstDialog(getActivity());
                dlg.setOnResultEventListener(new FirstDialog.OnResultEventListener() {
                    @Override
                    public void ResultEvent(boolean result) {
                        if (result){
                            Intent intent = new Intent(MainActivity.this, TestActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                dlg.showDialog();

                break;
            case R.id.button_login_test_2:
                break;

        }
    }

    // save login data
    private void save() {
        // save or delete login data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (SwLoginSave.isChecked()){
            editor.putBoolean(Profile.AppData.SAVE_LOGIN_DATA, SwLoginSave.isChecked());
            editor.putString(Profile.AppData.EMAIL, EtEmail.getText().toString().trim());
            editor.putString(Profile.AppData.PASS, EtPass.getText().toString().trim());
        }
        else {
            // reset
            editor.remove("SAVE_LOGIN_DATA");
            editor.remove("EMAIL");
            editor.remove("PASS");
        }

        editor.commit();
    }

    // load login data
    private void load() {

        SwLoginSave.setChecked(sharedPreferences.getBoolean("SAVE_LOGIN_DATA", false));
        EtEmail.setText(sharedPreferences.getString("EMAIL", ""));
        EtPass.setText(sharedPreferences.getString("PASS", ""));

    }

}
