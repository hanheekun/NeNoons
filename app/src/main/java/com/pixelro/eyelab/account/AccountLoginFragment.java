package com.pixelro.eyelab.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper;
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory;
import com.apollographql.apollo.exception.ApolloException;
import com.auth0.android.jwt.JWT;
import com.com.pixelro.eyelab.AllMembersQuery;
import com.com.pixelro.eyelab.SignInMutation;
import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.FirstDialog;
import com.pixelro.eyelab.MainActivity;
import com.pixelro.eyelab.Profile;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.server.JWTUtils;
import com.pixelro.eyelab.test.TestActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;


import static android.content.Context.MODE_PRIVATE;

public class AccountLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = AccountLoginFragment.class.getSimpleName();
    private View mView;

    private Button BtnLogin;
    private EditText EtEmail;
    private EditText EtPass;
    private Switch SwLoginSave;
    private Context mContext;

    //private SharedPreferences appData;

    private String masterKeyAlias;
    private SharedPreferences sharedPreferences;

    // 서버 로그인
    private ApolloClient apolloClient;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(EYELAB.APPDATA.TOKEN, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key,"");
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

        view.findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        view.findViewById(R.id.textView_account_login_forget).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_facebook).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_google).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_qq).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_kakao).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_naver).setOnClickListener(this);
        view.findViewById(R.id.imageButton_account_login_wechat).setOnClickListener(this);
        view.findViewById(R.id.button_account_login_login).setOnClickListener(this);


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

                // 로그인 시도
                setApollo();
                signIn(EtEmail.getText().toString().trim(),EtPass.getText().toString().trim());




                // 로그인 성공

                //http set header
                    //post

                break;
//            case R.id.button_login_test_1:
//
////                FirstDialog dlg = new FirstDialog(getActivity());
////                dlg.setOnResultEventListener(new FirstDialog.OnResultEventListener() {
////                    @Override
////                    public void ResultEvent(boolean result) {
////                        if (result){
////                            Intent intent = new Intent(MainActivity.this, TestActivity.class);
////                            startActivity(intent);
////                        }
////                    }
////                });
////                dlg.showDialog();
//
//                break;
//            case R.id.button_login_test_2:
//                break;

        }
    }

    private void LoginResultAction(){
        String token = getString(mContext, "token");

        // 토큰이 있는 경우 디코드 하기
        if (token!=null && !"".equals(token)) {
            try {
                // 로그인 저장
                save();
                // 메인 화면 전환
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();

                String decodeStr = null;
                JWT jwt = new JWT(token);
                boolean isExpired = jwt.isExpired(10); // 10 초 전까지 토큰 종료 여부 판단
                decodeStr = JWTUtils.decoded(token); // 디코드 값
                try {
                    JSONObject jsonObj = JWTUtils.getJson(token,"user");
                    String email = (String) jsonObj.get("email");
                    String name = (String) jsonObj.get("name");
                    String id= (String) jsonObj.get("id");
                    String tel = (String) jsonObj.get("tel");
                    Log.i(">>>>>>>>>>>", email + " " + name + " " + id + " " + tel);

                    decodeStr += "\nJSON :\n" + email + "\n" + id + "\n" + name + "\n" + tel;
                    //((TextView)view.findViewById(R.id.textview_decode)).setText(decodeStr );
                    Toast.makeText(mContext,"로그인 성공",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    //((TextView)view.findViewById(R.id.textview_decode)).setText("decode error : " + e.getLocalizedMessage());
                    Toast.makeText(mContext,"죄송합니다. 잠시 후 다시 로그인 해주세요",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                //((TextView)view.findViewById(R.id.textview_decode)).setText("decode error : " + e.getLocalizedMessage());
                Toast.makeText(mContext,"죄송합니다. 잠시 후 다시 로그인 해주세요",Toast.LENGTH_LONG).show();
            }
        }
        else {
            // 로그인 실패
            Toast.makeText(mContext,"로그인 정보를 확인하세요",Toast.LENGTH_LONG).show();
        }
    }

    // save login data
    private void save() {
        // save or delete login data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (SwLoginSave.isChecked()){
            editor.putBoolean(EYELAB.APPDATA.SAVE_LOGIN_DATA, SwLoginSave.isChecked());
            editor.putString(EYELAB.APPDATA.EMAIL, EtEmail.getText().toString().trim());
            editor.putString(EYELAB.APPDATA.PASS, EtPass.getText().toString().trim());
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 엔키노 로그인 코드
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setApollo() {

        ApolloSqlHelper apolloSqlHelper = ApolloSqlHelper.create(this.getContext(), "db_eyelab");
        NormalizedCacheFactory cacheFactory = new SqlNormalizedCacheFactory(apolloSqlHelper);

        // Create the cache key resolver, this example works well when all types have globally unique ids.
        CacheKeyResolver resolver = new CacheKeyResolver() {
            @NotNull
            @Override
            public CacheKey fromFieldRecordSet(@NotNull ResponseField field, @NotNull Map<String, Object> recordSet) {
                return formatCacheKey((String) recordSet.get("id"));
            }

            @NotNull
            @Override
            public CacheKey fromFieldArguments(@NotNull ResponseField field, @NotNull Operation.Variables variables) {
                return formatCacheKey((String) field.resolveArgument("id", variables));
            }

            private CacheKey formatCacheKey(String id) {
                if (id == null || id.isEmpty()) {
                    return CacheKey.NO_KEY;
                } else {
                    return CacheKey.from(id);
                }
            }
        };

        //Build the Apollo Client , http client
//        String authHeader = "Bearer $accessTokenId";
//        final String authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiY2thM2g0ejhyMDAwMGo2NnczajVvdHMwMCIsImVtYWlsIjoiZXJAZW5raW5vLmNvbSIsIm5hbWUiOiLstZzsmIjsp4AiLCJ0ZWwiOiIwMTAyNDkwODk1NSJ9LCJpYXQiOjE1OTEwODUyMTAsImV4cCI6MTU5MTE3MTYxMH0.yZQgpDbelEwLj4sCuqC_zbf5_bzri9Ee3kcDqUZzWw4";
//        final String authHeader = "";

        String authHeader = "";
        String token = getString(mContext, "token");
        if (token != null && !"".equals(token)) {
            authHeader = "Bearer " + token;
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        final String finalAuthHeader = authHeader;
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", finalAuthHeader)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient okHttpClient = httpClient.build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://pixelro-api.an.r.appspot.com/graphql")
                .normalizedCache(cacheFactory, resolver)
                .okHttpClient(okHttpClient)
                .build();

    }


    private void signIn(String email, String password) {

        // 로그인 결과 return
        int result = EYELAB.LOGIN.ERROR;

//        SignInMutation signInMutation = SignInMutation.builder().email("er@enkino.com").password("1234").build();

        SignInMutation signInMutation = SignInMutation.builder().email(email).password(password).build();
        AllMembersQuery allMembersQuery = AllMembersQuery.builder().build();


        Handler uiHandler = new Handler(Looper.getMainLooper()) {  // 핸들러에 Main Looper를 인자로 전달
            @Override
            public void handleMessage(Message msg) {  // 메인 스레드에서 호출
                Log.d(TAG,"handleMessage : " + msg.what);
                //화면 수정
                //showToken(layout);
            }
        };

        //
        apolloClient.mutate(signInMutation).enqueue(
                new ApolloCallback<SignInMutation.Data>(new ApolloCall.Callback<SignInMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<SignInMutation.Data> response) {
                        Log.i(TAG, response.toString());
                        String error = response.data().signIn().error();
                        if (error==null) {
                            String token = response.data().signIn().token();
                            if (token==null)
                                Log.i(TAG, "token is null");
                            else {
                                Log.i(TAG, token);
                                // 로그인 성공
                                // 토큰 저장
                                setString(mContext,"token",token);
                                //showToken(layout);
                                LoginResultAction();
                            }
                        } else {
                            Log.i(TAG, error);
                            // 로그인 실패
                            // 토큰 삭제
                            removeKey(mContext,"token");
                            LoginResultAction();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e(TAG, e.getMessage(), e);
                        // 로그인 실패
                        // 토큰 삭제
                        removeKey(mContext,"token");
                        LoginResultAction();
                    }
                },uiHandler)
        );

        // 일반 쿼리 사용예
        apolloClient.query(allMembersQuery).enqueue(
                new ApolloCallback<AllMembersQuery.Data>(new ApolloCall.Callback<AllMembersQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AllMembersQuery.Data> response) {
                        Log.i(TAG, response.toString());
                        if (response.data()!=null) {
                            int size = response.data().allMembers().size();
                            Log.i(TAG, "size : " + size);
                            if (size > 0) {
                                for (AllMembersQuery.AllMember member:
                                        response.data().allMembers()) {
                                    Log.i(TAG, "member : " + member.email() +" "+ member.id());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                },uiHandler)
        );

    }



}
