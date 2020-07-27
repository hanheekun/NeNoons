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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.pixelro.nenoons.BaseFragment;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.server.HttpTask;
import com.pixelro.nenoons.server.JWTUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import static android.content.Context.MODE_PRIVATE;

public class AccountLoginFragment extends BaseFragment implements View.OnClickListener {

    private final static String TAG = AccountLoginFragment.class.getSimpleName();
    private View mView;

    private Button BtnLogin;
    private EditText EtEmail;
    private EditText EtPass;
    private Switch SwLoginSave;
    private Context mContext;
    private SharedPreferencesManager mSm;

    //private SharedPreferences appData;

    private String masterKeyAlias;
    //private SharedPreferences sharedPreferences;

    //private AccountDialog mDlg;
    private PersonalProfile mPersonalProfile;

    // google
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    //kakao
    private AccountLoginFragment.SessionCallback callback;
    private LoginButton loginButton;

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

        mPersonalProfile = ((AccountActivity)getActivity()).mPersonalProfile;

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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // kakao
        loginButton = view.findViewById(R.id.login_button_activity);
        loginButton.setSuportFragment(this); // set fragment for LoginButton

        String keyHash = com.kakao.util.helper.Utility.getKeyHash(getActivity());
        callback = new AccountLoginFragment.SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //redirectSignupActivity();
            //Toast.makeText(getActivity(), "onSessionOpened()", Toast.LENGTH_SHORT).show();
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
                //Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response resultKakao) {
                            Log.i("KAKAO_API", "사용자 아이디: " + resultKakao.getId());

                            UserAccount kakaoAccount = resultKakao.getKakaoAccount();
                            if (kakaoAccount != null) {

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if (email != null) {
                                    Log.i("KAKAO_API", "email: " + email);

                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                } else {
                                    // 이메일 획득 불가
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }


                                // 정보 전달 완료
                                // 로그인 시작
                                if(email != null){

                                    //////////////////////////////////////////////////////////////////////////////
                                    // 카카오 가입 진행
                                    //////////////////////////////////////////////////////////////////////////////

                                    // 로그인중 progress 시작
                                    mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

                                    // email, pass 임시 저장
                                    mPersonalProfile.email = email;
                                    mPersonalProfile.sns_name = "kakao";
                                    mPersonalProfile.sns_ID = ""+resultKakao.getId();

                                    // email, pass 로 회원 가입
                                    HashMap<String, String> param = new HashMap<String, String>();
                                    // 파라메터는 넣기 예
                                    param.put("email", mPersonalProfile.email);    //PARAM
                                    param.put("sns", mPersonalProfile.sns_name);    //PARAM
                                    param.put("snsId", mPersonalProfile.sns_ID);    //PARAM
                                    //param.put("name", EtPass.getText().toString().trim());    //PARAM
                                    Handler handler = new Handler(message -> {
                                        Bundle bundle = message.getData();
                                        String result = bundle.getString("result");
                                        System.out.println(result);

                                        // progress 종료
                                        if (mProgressDialog != null) mProgressDialog.dismiss();

                                        try {
                                            JSONObject j = new JSONObject(result);
                                            String error = j.getString("error");
                                            String token = j.getString("token");
                                            System.out.println(error);
                                            System.out.println(error == null);
                                            System.out.println(token);

                                            if (error == "null" && token != "null") {

                                                //Toast.makeText(mContext, "이메일 가입 성공", Toast.LENGTH_SHORT).show();

                                                // 토큰 저장
                                                mSm.setToken(token);

                                                // 로그인 성공 저장
                                                mSm.setLoginning(true);

                                                mSm.setEmail(mPersonalProfile.email);

                                                mSm.setSNSID(mPersonalProfile.sns_ID);

                                                mSm.setSNSName(mPersonalProfile.sns_name);

                                                mSm.setSNSLogin(true);

                                                // 다음 페이지 전환
                                                System.out.println("메인액티비티 시작");
                                                NavHostFragment.findNavController(AccountLoginFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                                            } else {
                                                // 이메일 회원가입 실패
                                                removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                                                //AccountDialog mDlg = new AccountDialog(getActivity(),"이메일 정보를\r\n확인해 주세요.", "돌아가기");
                                                new AccountDialog(getActivity(),error, "돌아가기");
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            // 로그인 실패
                                            removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                                            AccountDialog mDlg = new AccountDialog(getActivity(),"이메일 정보를\r\n확인해 주세요.", "돌아가기");
                                        }
                                        return true;
                                    });
                                    // API 주소와 위 핸들러 전달 후 실행.
                                    new HttpTask("https://nenoonsapi.du.r.appspot.com/android/sns_signup", handler).execute(param);
                                }

                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getActivity(), "onSessionOpened()", Toast.LENGTH_SHORT).show();

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Snackbar.make(mView.findViewById(R.id.fragment_account_id), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Snackbar.make(mView.findViewById(R.id.fragment_account_id), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {

            //Toast.makeText(getActivity(),"email = "+user.getEmail()+" ID : "+user.getUid(),Toast.LENGTH_LONG).show();

            //////////////////////////////////////////////////////////////////////////////
            // google 가입 진행
            //////////////////////////////////////////////////////////////////////////////

            // 로그인중 progress 시작
            mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

            // email, pass 임시 저장
            mPersonalProfile.email = user.getEmail();
            mPersonalProfile.sns_name = "google"; // 임시로 사용
            mPersonalProfile.sns_ID = user.getUid();

            // email, pass 로 회원 가입
            HashMap<String, String> param = new HashMap<String, String>();
            // 파라메터는 넣기 예
            param.put("email", mPersonalProfile.email);    //PARAM
            param.put("sns", mPersonalProfile.sns_name);    //PARAM
            param.put("snsId", mPersonalProfile.sns_ID);    //PARAM
            //param.put("name", EtPass.getText().toString().trim());    //PARAM
            Handler handler = new Handler(message -> {
                Bundle bundle = message.getData();
                String result = bundle.getString("result");
                System.out.println(result);

                // progress 종료
                if (mProgressDialog != null) mProgressDialog.dismiss();

                try {
                    JSONObject j = new JSONObject(result);
                    String error = j.getString("error");
                    String token = j.getString("token");
                    System.out.println(error);
                    System.out.println(error == null);
                    System.out.println(token);

                    if (error == "null" && token != "null") {

                        //Toast.makeText(mContext, "이메일 가입 성공", Toast.LENGTH_SHORT).show();

                        // 토큰 저장
                        mSm.setToken(token);

                        // 로그인 성공 저장
                        mSm.setLoginning(true);

                        mSm.setEmail(mPersonalProfile.email);

                        mSm.setSNSID(mPersonalProfile.sns_ID);

                        mSm.setSNSName(mPersonalProfile.sns_name);

                        mSm.setSNSLogin(true);

                        // 다음 페이지 전환
                        System.out.println("메인액티비티 시작");
                        NavHostFragment.findNavController(AccountLoginFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                    } else {
                        // 이메일 회원가입 실패
                        removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                        //AccountDialog mDlg = new AccountDialog(getActivity(),"이메일 정보를\r\n확인해 주세요.", "돌아가기");
                        new AccountDialog(getActivity(),error, "돌아가기");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    // 로그인 실패
                    removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                    AccountDialog mDlg = new AccountDialog(getActivity(),"이메일 정보를\r\n확인해 주세요.", "돌아가기");
                }
                return true;
            });
            // API 주소와 위 핸들러 전달 후 실행.
            new HttpTask("https://nenoonsapi.du.r.appspot.com/android/sns_signup", handler).execute(param);


//            Intent intent = new Intent(this, AfterActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.textView_account_login_forget:    // 비밀번호 찾기
                //NavHostFragment.findNavController(AccountLoginFragment.this).navigate(R.id.action_navigation_account_login_to_navigation_account_find);
                Toast.makeText(getActivity(),"준비중 입니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton_account_login_facebook:
                break;
            case R.id.imageButton_account_login_google:
                Toast.makeText(getActivity(),"준비중 입니다.",Toast.LENGTH_SHORT).show();
                //signIn();
                break;
            case R.id.imageButton_account_login_qq:
                break;
            case R.id.imageButton_account_login_kakao:
                Toast.makeText(getActivity(),"준비중 입니다.",Toast.LENGTH_SHORT).show();
                //loginButton.performClick();
                break;
            case R.id.imageButton_account_login_naver:
                break;
            case R.id.imageButton_account_login_wechat:
                break;
            case R.id.button_account_login_login:

                // 로그인중 progress 시작
                mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("email", EtEmail.getText().toString().trim());    //PARAM
                param.put("password", EtPass.getText().toString().trim());    //PARAM
                Handler handler = new Handler(message -> {

                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);

                    // progress 종료
                    if (mProgressDialog != null) mProgressDialog.dismiss();

                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String token = j.getString("token");
                        System.out.println(error);
                        System.out.println(error == null);
                        System.out.println(token);

                        if (error == "null" && token != "null") {

                            // 로그인 성공
                            //Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();

                            // email 로그인 정보 저장
                            saveEmailLoginInfo();

                            // 로그인 성공 저장
                            mSm.setToken(token);
                            mSm.setLoginning(true);

                            mSm.setEmail(EtEmail.getText().toString().trim());
                            // 이름 받아오기 필요함

                            // 토큰 저장
                            System.out.println("메인액티비티 시작");

                            // 메인 화면 전환
                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(mainIntent);
                            getActivity().finish();

                        } else {
                            // 로그인 실패
                            mSm.removeToken();
                            AccountDialog mDlg = new AccountDialog(getActivity(),error, "돌아가기");
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
