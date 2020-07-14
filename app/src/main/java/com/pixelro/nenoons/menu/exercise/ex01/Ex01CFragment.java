package com.pixelro.nenoons.menu.exercise.ex01;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Ex01CFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_03_c, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_test_prev).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,++curTotalEXNumber);

        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,true);
        editor.commit();

        // 운동 정보 전달
        ExProfile exProfile = new ExProfile();
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyyMMddHHmmss");
        exProfile.type = ExProfile.Type.TYPE_1;
        exProfile.date = formatNowDate.format(nowDate);
        exProfile.level = ((Ex01Activity)getActivity()).mCurLevel;

        // 서버연결 20200715

        String token = getToken(getActivity());

        // ExProfile 전달


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_test_prev:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_01, new Ex01AFragment()).commit();
                break;
            case R.id.button_test_next:
                getActivity().finish();
                break;
        }
    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }
}
