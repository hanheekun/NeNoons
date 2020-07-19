package com.pixelro.nenoons.menu.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.menu.home.WebActivity;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_my_logout:

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

                Intent mainIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(mainIntent);

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
                intent.putExtra("url", "https://www.nenoons.com/app-qua");
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

        }
    }
}
