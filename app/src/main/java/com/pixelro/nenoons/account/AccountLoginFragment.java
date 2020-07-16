package com.pixelro.nenoons.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.auth0.android.jwt.JWT;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.server.HttpTask;
import com.pixelro.nenoons.server.JWTUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import static android.content.Context.MODE_PRIVATE;

public class AccountLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountLoginFragment.class.getSimpleName();
    private View mView;

    private Button BtnLogin;
    private EditText EtEmail;
    private EditText EtPass;
    private Switch SwLoginSave;
    private Context mContext;
    public ProgressDialog mLoginProgressDialog;
    private SharedPreferencesManager mSm;

    //private SharedPreferences appData;

    private String masterKeyAlias;
    //private SharedPreferences sharedPreferences;

    // 서버 로그인
//    private ApolloClient apolloClient;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, "");
        return value;
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

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
        mContext = this.getContext();
        mView = view;
        mSm = new SharedPreferencesManager(getActivity());

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.textView_account_login_forget).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_facebook).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_google).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_qq).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_kakao).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_naver).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_wechat).setOnClickListener(this);
        view.findViewById(R.id.button_account_login_login).setOnClickListener(this);


        SwLoginSave = (Switch) (view.findViewById(R.id.switch_account_login_save));

        BtnLogin = (Button) (getActivity().findViewById(R.id.button_account_login_login));
        BtnLogin.setOnClickListener(this);

        EtEmail = (EditText) (getActivity().findViewById(R.id.editText_account_login_email));
        EtPass = (EditText) (getActivity().findViewById(R.id.editText_account_login_password));

        //edit 창에 입력이 있어야지만 로그인 버는 활성화
        EtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (EtEmail.length() > 0 && EtPass.length() > 0) {
                    BtnLogin.setEnabled(true);
                } else {
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
                if (EtEmail.length() > 0 && EtPass.length() > 0) {
                    BtnLogin.setEnabled(true);
                } else {
                    BtnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 로그인 정보 저장
//        try {
//            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            sharedPreferences = EncryptedSharedPreferences.create(
//                    "secret_shared_prefs",
//                    masterKeyAlias,
//                    getContext(),
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            );
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_LOGIN, MODE_PRIVATE);

        // 기종 login 정보 load
        loadEmailLoginInfo();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

                // 로그인중 progress 시작
                mLoginProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("email", EtEmail.getText().toString().trim());    //PARAM
                param.put("password", EtPass.getText().toString().trim());    //PARAM
                Handler handler = new Handler(message -> {

                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);

                    // progress 종료
                    if (mLoginProgressDialog != null) mLoginProgressDialog.dismiss();

                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String token = j.getString("token");
                        System.out.println(error);
                        System.out.println(error == null);
                        System.out.println(token);



                        if (error == "null" && token != "null") {

                            // 로그인 성공
                            Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();

                            // email 로그인 정보 저장
                            saveEmailLoginInfo();

                            // 로그인 성공 저장
                            mSm.setToken(token);
                            mSm.setLoginning(true);

                            // 토큰 저장
                            System.out.println("메인액티비티 시작");

                            // 메인 화면 전환
                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(mainIntent);
                            getActivity().finish();

                        } else {
                            // 로그인 실패
                            mSm.removeToken();
                            AccountDialog mDlg = new AccountDialog(getActivity(),"로그인 정보를\r\n확인해 주세요.", "돌아가기");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 로그인 실패
                        mSm.removeToken();
                        AccountDialog mDlg = new AccountDialog(getActivity(),"로그인 정보를\r\n확인해 주세요.", "돌아가기");
                    }
                    return true;
                });
                // API 주소와 위 핸들러 전달 후 실행.
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/signin", handler).execute(param);
//                new HttpTask("http://192.168.1.162:4002/android/signin", handler).execute(param);


                break;
        }
    }

    // save login data
    private void saveEmailLoginInfo() {

        // save or delete login data
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (SwLoginSave.isChecked()) {
            editor.putBoolean(EYELAB.APPDATA.ACCOUNT.SAVE_EMAIL_LOGIN_INFO, true);
            editor.putString(EYELAB.APPDATA.ACCOUNT.EMAIL, EtEmail.getText().toString().trim());
            editor.putString(EYELAB.APPDATA.ACCOUNT.PASS, EtPass.getText().toString().trim());
        } else {
            // login but email,pass delete
            editor.remove(EYELAB.APPDATA.ACCOUNT.SAVE_EMAIL_LOGIN_INFO);
            editor.remove(EYELAB.APPDATA.ACCOUNT.EMAIL);
            editor.remove(EYELAB.APPDATA.ACCOUNT.PASS);
        }
        editor.commit();
    }

    // load login data
    private void loadEmailLoginInfo() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.ACCOUNT.SAVE_EMAIL_LOGIN_INFO, false)) {
            SwLoginSave.setChecked(true);
            EtEmail.setText(sharedPreferences.getString(EYELAB.APPDATA.ACCOUNT.EMAIL, ""));
            EtPass.setText(sharedPreferences.getString(EYELAB.APPDATA.ACCOUNT.PASS, ""));
        } else {
            EtEmail.setText("");
            EtPass.setText("");
        }
    }


}
