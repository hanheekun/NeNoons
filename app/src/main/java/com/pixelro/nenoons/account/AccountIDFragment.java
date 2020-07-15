package com.pixelro.nenoons.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.pixelro.nenoons.account.AccountLoginFragment.removeKey;
import static com.pixelro.nenoons.account.AccountLoginFragment.setString;

public class AccountIDFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{
    private final static String TAG = AccountIDFragment.class.getSimpleName();

    private View mView;
    private EditText EtEmail;
    private EditText EtPass;
    private EditText EtPassCfm;
    private TextView TvPassCompare;
    private String mEmail;
    private String mPass;
    private Button BtnNext;
    //private SharedPreferences sharedPreferences;
    //private SharedPreferences.Editor editor;

    private PersonalProfile mPersonalProfile;

    AccountDialog mDlg;

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
        mDlg = new AccountDialog(getActivity());
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

                //////////////////////////////////////////////////////////////////////////////
                // SNS 가입 진행
                //////////////////////////////////////////////////////////////////////////////

                // SNS 확인 시작

                // SNS 확인 완료

                // email, id 회원 가입

                // 회원 가입 가능 할 경우 토큰 저장
                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiY2thM2g0ejhyMDAwMGo2NnczajVvdHMwMCIsImVtYWlsIjoiZXJAZW5raW5vLmNvbSIsIm5hbWUiOiLstZzsmIjsp4AiLCJ0ZWwiOiIwMTAyNDkwODk1NSJ9LCJpYXQiOjE1OTEwODUyMTAsImV4cCI6MTU5MTE3MTYxMH0.yZQgpDbelEwLj4sCuqC_zbf5_bzri9Ee3kcDqUZzWw4";
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token);
                editor.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,true);
                editor.commit();

                // 로그인 ing 로 설정


                // 다음 페이지 전환
                NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                //////////////////////////////////////////////////////////////////////////////
                // 가입 error message
                //////////////////////////////////////////////////////////////////////////////
                //mDlg.showDialog("이미 가입되었습니다.\r\n확인해 주세요.","돌아가기");

                break;
            case R.id.imageButton_account_id_google:
                break;
            case R.id.imageButton_account_id_kakao:
                break;
            case R.id.imageButton_account_id_naver:
                break;
            case R.id.button_account_id_next:

                // 서버연결 20200715

                //////////////////////////////////////////////////////////////////////////////
                // email 가입 진행
                //////////////////////////////////////////////////////////////////////////////

                // email, pass 임시 저장
                mPersonalProfile.email = EtEmail.getText().toString();
                mPersonalProfile.password = EtPass.getText().toString();

                // email, pass 로 회원 가입
                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("email", EtEmail.getText().toString().trim());    //PARAM
                param.put("password", EtPass.getText().toString().trim());    //PARAM
                Handler handler = new Handler(message -> {

                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);
                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String token1 = j.getString("token");
                        System.out.println(error);
                        System.out.println(error == null);
                        System.out.println(token1);

                        // progress 종료

                        if (error == "null" && token1 != "null") {

                            Toast.makeText(mContext, "이메일 가입 성공", Toast.LENGTH_SHORT).show();

                            // 토큰 저장
                            setString(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN, token1);
                            System.out.println("메인액티비티 시작");

                            //                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
//                            getActivity().startActivity(mainIntent);
//                            getActivity().finish();

                            // 회원 가입 가능 할 경우 토큰 저장
//                            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiY2thM2g0ejhyMDAwMGo2NnczajVvdHMwMCIsImVtYWlsIjoiZXJAZW5raW5vLmNvbSIsIm5hbWUiOiLstZzsmIjsp4AiLCJ0ZWwiOiIwMTAyNDkwODk1NSJ9LCJpYXQiOjE1OTEwODUyMTAsImV4cCI6MTU5MTE3MTYxMH0.yZQgpDbelEwLj4sCuqC_zbf5_bzri9Ee3kcDqUZzWw4";
                            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token1);
                            editor1.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,true);
                            editor1.commit();

                            // 메인 화면 전환
                            LoginSuccessProcessEmail();

                            // 다음 페이지 전환
                            NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                        } else {
                            // 이메일 회원가입 실패
                            removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                            mDlg.showDialog("이메일 정보를\r\n확인해 주세요.", "돌아가기");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 로그인 실패
                        removeKey(mContext, EYELAB.APPDATA.ACCOUNT.TOKEN);
                        mDlg.showDialog("이메일 정보를\r\n확인해 주세요.", "돌아가기");
                    }
                    return true;
                });
                // API 주소와 위 핸들러 전달 후 실행.
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/signin", handler).execute(param);
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


}
