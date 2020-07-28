package com.pixelro.nenoons.menu.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.nenoons.BaseFragment;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.menu.home.WebActivity;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private MyViewModel myViewModel;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
//        final TextView textView = root.findViewById(R.id.text_my);
//        myViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PackageInfo pi = null;
        try {
            pi = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = pi.versionName;

        TextView version = (TextView)mView.findViewById(R.id.textView_my_ver);
        version.setText(""+versionName);

        TextView email = (TextView)mView.findViewById(R.id.textView_my_email);
        email.setText(mSm.getEmail());

        mView.findViewById(R.id.button_my_logout).setOnClickListener(this);
        mView.findViewById(R.id.button_my_my).setOnClickListener(this);
        mView.findViewById(R.id.button_my_basket).setOnClickListener(this);
        mView.findViewById(R.id.button_my_color).setOnClickListener(this);
        mView.findViewById(R.id.button_my_font).setOnClickListener(this);
        mView.findViewById(R.id.button_my_faq).setOnClickListener(this);
        mView.findViewById(R.id.button_my_notice).setOnClickListener(this);
        mView.findViewById(R.id.button_my_qna).setOnClickListener(this);
        mView.findViewById(R.id.button_my_tos).setOnClickListener(this);
        mView.findViewById(R.id.button_my_push).setOnClickListener(this);
        mView.findViewById(R.id.button_my_unregister).setOnClickListener(this);

        TextView tvName = (TextView)mView.findViewById(R.id.textView_my_name);

        tvName.setText(mSm.getName());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_my_logout:

                resetLoginInfo();

                Intent mainIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(mainIntent);
                getActivity().finish();

                break;
            case R.id.button_my_unregister:

                // 회원 탈퇴

                // 회원 탈퇴 요청하기
                SharedPreferencesManager sfm = new SharedPreferencesManager(getActivity());
                String token = sfm.getToken();
                String email = sfm.getEmail();
                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("token", token);    //PARAM
                param.put("email", email);    //PARAM
                Handler handler = new Handler(message -> {


                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);
                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String msg = j.getString("msg");
                        System.out.println(error);
                        System.out.println(error == null);

                        if (error == "null" && msg != "null") {

                            //Toast.makeText(getActivity(), "name = " + name , Toast.LENGTH_SHORT).show();
                            // 회원 탈퇴 완료

                            resetLoginInfo();

                            Intent intent = new Intent(getActivity(), AccountActivity.class);
                            startActivity(intent);
                            getActivity().finish();


                        } else {
                            //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                });

                // API 주소와 위 핸들러 전달 후 실행.
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/with_draw", handler).execute(param);
                // 서버연결 20200728 token과 email 전송 후 handler로 들어오지 않습니다.
                // 서버연결 20200728 회원 탈퇴를 한번 요청하고나면 운동기록, 테스트 기록 불러오기도 작동하지 않습니다.

                break;
            case R.id.button_my_my:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "https://www.nenoons.com/app-my-page");
                intent.putExtra("token", mSm.getToken());
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_basket:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "https://www.nenoons.com/app-cart");
                intent.putExtra("token", mSm.getToken());
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_faq:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "https://www.nenoons.com/app-faq");
                intent.putExtra("token", mSm.getToken());
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_qna:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "https://www.nenoons.com/app-qna");
                intent.putExtra("token", mSm.getToken());
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_notice:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "https://www.nenoons.com/app-notice");
                intent.putExtra("token", mSm.getToken());
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_color:
                intent = new Intent(getActivity(), MyColorActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_font:
                intent = new Intent(getActivity(), MyFontActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_tos:
                intent = new Intent(getActivity(), MyTosActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.button_my_push:
                intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                //.putExtra(Settings.EXTRA_CHANNEL_ID, MY_CHANNEL_ID);
                startActivity(intent);
                break;

        }
    }

    void resetLoginInfo(){
        // logout, reset first login
        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.FIRST_LOGIN);
        editor.remove(EYELAB.APPDATA.ACCOUNT.LOGINNING);
        editor.remove(EYELAB.APPDATA.ACCOUNT.TOKEN);
        editor.commit();

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);
        editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        editor.commit();

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove(EYELAB.APPDATA.TEST.LAST_DISTANCE);
        editor.commit();

        SharedPreferencesManager sfm = new SharedPreferencesManager(getActivity());
        sfm.removeName();

    }
}
