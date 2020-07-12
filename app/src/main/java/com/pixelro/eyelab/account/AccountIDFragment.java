package com.pixelro.eyelab.account;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.Profile;
import com.pixelro.eyelab.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Profile mProfile;

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

        mProfile = ((AccountActivity)getActivity()).mProfile;

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
        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                getActivity().onBackPressed();
                break;
            case R.id.imageButton_account_id_facebook:
                break;
            case R.id.imageButton_account_id_google:
                break;
            case R.id.imageButton_account_id_qq:
                break;
            case R.id.imageButton_account_id_kakao:
                break;
            case R.id.imageButton_account_id_naver:
                break;
            case R.id.imageButton_account_id_wechat:
                break;
            case R.id.button_account_id_next:

                // email, pass 가 잘 입력 되었을 경우
                mProfile.email = EtEmail.getText().toString();
                mProfile.password = EtPass.getText().toString();

                mDlg.showDialog("이미 가입되었습니다.\r\n확인해 주세요.");

                // 다음 페이지 전환
                //NavHostFragment.findNavController(AccountIDFragment.this).navigate(R.id.action_navigation_account_id_to_navigation_account_profile);

                break;
            case R.id.editText_account_login_email:
            case R.id.editText_account_login_pass:
            case R.id.editText_account_login_email_conf:
                setButton();
                break;
        }
    }

}
