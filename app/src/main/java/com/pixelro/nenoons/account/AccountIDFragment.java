package com.pixelro.nenoons.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.OptionalBoolean;
import com.pixelro.nenoons.BaseFragment;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.pixelro.nenoons.account.AccountLoginFragment.removeKey;
import static com.pixelro.nenoons.account.AccountLoginFragment.setString;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

// google
// Client ID : 622454092611-df5frr6t2jffrdr4o9t2d1kmof2ia844.apps.googleusercontent.com
// Client Secret : c-J35XbjSdXtNuzBL61x8B7M

public class AccountIDFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener{
    private final static String TAG = AccountIDFragment.class.getSimpleName();

    private EditText EtEmail;
    private EditText EtPass;
    private EditText EtPassCfm;
    private TextView TvPassCompare;
    private Button BtnNext;

    //private AccountDialog mDlg;
    private PersonalProfile mPersonalProfile;

    // google
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    //kakao
    private SessionCallback callback;
    private LoginButton loginButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_id, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mSm = new SharedPreferencesManager(getActivity());

        mPersonalProfile = ((AccountActivity)getActivity()).mPersonalProfile;

        // next button
        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        BtnNext = (Button) (view.findViewById(R.id.button_account_id_next));
        BtnNext.setOnClickListener(this);
        BtnNext.setEnabled(false);

        // emial login
        EtEmail = (EditText) (view.findViewById(R.id.editText_account_login_email));
        EtEmail.addTextChangedListener(textWatcher);
        EtEmail.setOnFocusChangeListener(this);
        EtPass = (EditText) (view.findViewById(R.id.editText_account_login_pass));
        EtPass.addTextChangedListener(textWatcher);
        EtPass.setOnFocusChangeListener(this);
        EtPassCfm = (EditText) (view.findViewById(R.id.editText_account_login_email_conf));
        EtPassCfm.addTextChangedListener(textWatcher);
        EtPassCfm.setOnFocusChangeListener(this);
        TvPassCompare = (TextView) (mView.findViewById(R.id.textView_account_id_pass_compare));

        // sns login
        view.findViewById(R.id.imageButton_account_id_facebook).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_id_google).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_id_qq).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_id_kakao).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_id_naver).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_id_wechat).setOnClickListener(this);

        // 로그인 실패 메세지
        //mDlg = new AccountDialog(getActivity());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        view.findViewById(R.id.button_google_test).setOnClickListener(this);

        view.findViewById(R.id.button_google_logout_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                //mAuth.signOut();
                mAuth.getCurrentUser().delete();

                //FirebaseAuth.getInstance().signOut();
                //Toast.makeText(getActivity(),"로그아웃",Toast.LENGTH_SHORT).show();
            }
        });

//        // SDK 초기화
//        KakaoSDK.init(new KakaoAdapter() {
//
//            @Override
//            public IApplicationConfig getApplicationConfig() {
//                return new IApplicationConfig() {
//                    @Override
//                    public Context getApplicationContext() {
//                        return GlobalApplication::getGlobalApplicationContext();
//                    }
//                };
//            }
//        });

        // kakao
        loginButton = view.findViewById(R.id.login_button_activity);
        loginButton.setSuportFragment(this); // set fragment for LoginButton

        String keyHash = com.kakao.util.helper.Utility.getKeyHash(getActivity());
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
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
                                    // kakao 가입 진행
                                    //////////////////////////////////////////////////////////////////////////////

                                    // progress 시작
                                    mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

                                    // email, sns_name, sns_ID 저장
                                    mPersonalProfile.email = email;
                                    mPersonalProfile.sns_name = "kakao";
                                    mPersonalProfile.sns_ID = ""+resultKakao.getId();

                                    // email, sns_name, sns_ID 로 회원 가입
                                    HashMap<String, String> param = new HashMap<String, String>();
                                    // 파라메터는 넣기 예
                                    param.put("email", mPersonalProfile.email);    //PARAM
                                    param.put("sns", mPersonalProfile.sns_name);    //PARAM
                                    param.put("snsId", mPersonalProfile.sns_ID);    //PARAM
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

                                                // 가입 정보 저장
                                                mSm.setEmail(mPersonalProfile.email);
                                                mSm.setSNSID(mPersonalProfile.sns_ID);
                                                mSm.setSNSName(mPersonalProfile.sns_name);
                                                mSm.setSNSLogin(true);

                                                // 다음 페이지 전환
                                                System.out.println("메인액티비티 시작");
                                                NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                                            } else {
                                                // 이메일 회원가입 실패
                                                removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);

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



     //Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();

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

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

//    private void updateUI(FirebaseUser account) {
//
//        Toast.makeText(getActivity(),"email = "+account.getEmail()+" ID : "+account.getUid(),Toast.LENGTH_LONG).show();
//        //FirebaseAuth.getInstance().signOut();
//    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {

            //Toast.makeText(getActivity(),"email = "+user.getEmail()+" ID : "+user.getUid(),Toast.LENGTH_LONG).show();

            //////////////////////////////////////////////////////////////////////////////
            // google 가입 진행
            //////////////////////////////////////////////////////////////////////////////

            // progress 시작
            mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

            // email, sns_name, sns_ID 저장
            mPersonalProfile.email = user.getEmail();
            mPersonalProfile.sns_name = "google"; // 임시로 사용
            mPersonalProfile.sns_ID = user.getUid();

            // email, sns_name, sns_ID 로 회원 가입
            HashMap<String, String> param = new HashMap<String, String>();
            // 파라메터는 넣기 예
            param.put("email", mPersonalProfile.email);    //PARAM
            param.put("sns", mPersonalProfile.sns_name);    //PARAM
            param.put("snsId", mPersonalProfile.sns_ID);    //PARAM
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

                        // 가입 정보 저장
                        mSm.setEmail(mPersonalProfile.email);
                        mSm.setSNSID(mPersonalProfile.sns_ID);
                        mSm.setSNSName(mPersonalProfile.sns_name);
                        mSm.setSNSLogin(true);

                        // 다음 페이지 전환
                        System.out.println("메인액티비티 시작");
                        NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                    } else {
                        // 이메일 회원가입 실패
                        removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);

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
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.editText_account_login_email:
            case R.id.editText_account_login_pass:
            case R.id.editText_account_login_email_conf:
                setButton();
                break;
        }
    }

    // 이메일 정규식
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //이메일 검사
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void setButton() {
        if (EtEmail.length() > 0 && EtPass.length() > 0 && validateEmail(EtEmail.getText().toString().trim()) && EtPass.getText().toString().equals(EtPassCfm.getText().toString())) {
            BtnNext.setEnabled(true);
        } else {
            BtnNext.setEnabled(false);
        }

        if (EtPass.length() == 0 && EtPassCfm.length() == 0 ){
            TvPassCompare.setText("");
        } else if (EtPass.getText().toString().equals(EtPassCfm.getText().toString())){
            TvPassCompare.setTextColor(Color.rgb(31,220,73));
            TvPassCompare.setText(R.string.account_join_id_pass_compare_ok);
        } else {
            TvPassCompare.setTextColor(Color.RED);
            TvPassCompare.setText(R.string.account_join_id_pass_compare_no);
        }
    }

    // EidtText가 눌릴때마다 감지하는 부분
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setButton();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    public void onClick(View view) {
        Context mContext =getContext();

        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.imageButton_account_id_facebook:
                break;
            case R.id.imageButton_account_id_google:
                //Toast.makeText(getActivity(),"준비중 입니다.",Toast.LENGTH_SHORT).show();
                signIn();
                break;
            case R.id.imageButton_account_id_kakao:
                //Toast.makeText(getActivity(),"준비중 입니다.",Toast.LENGTH_SHORT).show();
                loginButton.performClick();

                break;
            case R.id.imageButton_account_id_naver:
                break;
            case R.id.button_account_id_next:

                if (EtPass.getText().toString().length() < 8){
                    new AccountDialog(getActivity(), "비밀번호는 8자리 이상으로 해주세요.", "돌아가기");
                    break;
                }

                // 서버연결 20200715

                //////////////////////////////////////////////////////////////////////////////
                // email 가입 진행
                //////////////////////////////////////////////////////////////////////////////

                // 로그인중 progress 시작
                mProgressDialog = ProgressDialog.show(getActivity(), "", "로그인중...", true, true);

                // email, pass 임시 저장
                mPersonalProfile.email = EtEmail.getText().toString();
                mPersonalProfile.password = EtPass.getText().toString();

                // email, pass 로 회원 가입
                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("email", EtEmail.getText().toString().trim());    //PARAM
                param.put("password", EtPass.getText().toString().trim());    //PARAM
//                param.put("name", EtPass.getText().toString().trim());    //PARAM
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

                            // 다음 페이지 전환
                            System.out.println("메인액티비티 시작");
                            NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

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
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/signup", handler).execute(param);
//                new HttpTask("http://192.168.1.162:4002/android/signin", handler).execute(param);


                //////////////////////////////////////////////////////////////////////////////
                // 가입 error message
                //////////////////////////////////////////////////////////////////////////////
                //mDlg.showDialog("이미 가입되었습니다.\r\n확인해 주세요.","돌아가기");

                break;
            case R.id.editText_account_login_email:
            case R.id.editText_account_login_pass:
            case R.id.editText_account_login_email_conf:
                setButton();
                break;
            case R.id.button_google_test:
                signIn();
                break;
        }
    }

    private void LoginSuccessProcessEmail() {

        // email 로그인 정보 저장
//        saveEmailLoginInfo();

        // 로그인 성공 저장
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING, true);

        // 메인 화면 전환
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(mainIntent);
        getActivity().finish();
    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }

    public void setToken(Context context, String token){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token);
        editor.commit();
    }

    public Boolean getLoginning(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,false);
    }

    public void setLoginning(Context context, Boolean bool){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,bool);
        editor.commit();
    }


}
