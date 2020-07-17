package com.pixelro.nenoons.menu.exercise.ex03;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Ex03CFragment extends Fragment implements View.OnClickListener{

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
        Context mContext =getContext();

        view.findViewById(R.id.button_test_prev).setOnClickListener(this);
        view.findViewById(R.id.button_test_next).setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,++curTotalEXNumber);

        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,true);
        editor.commit();

        // 운동 정보 전달
        ExProfile exProfile = new ExProfile();
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyyMMddHHmmss");
        exProfile.type = ExProfile.Type.TYPE_3;
        exProfile.date = formatNowDate.format(nowDate);
        exProfile.level = ((Ex03Activity)getActivity()).mCurLevel;

        // 서버연결 20200715

        String token = getToken(getActivity());

        // ExProfile 전달
        HashMap<String, String> param = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param.put("token", token);    //PARAM
        param.put("date", exProfile.date);    //PARAM
        param.put("type", String.valueOf(exProfile.type));    //PARAM
        param.put("level", String.valueOf(exProfile.level));    //PARAM
        param.put("reserved1", String.valueOf(exProfile.reserved1));    //PARAM
        param.put("reserved2", String.valueOf(exProfile.reserved2));    //PARAM무ㅈ
        Handler handler = new Handler(message -> {

            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            System.out.println(result);
            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                String msg = j.getString("msg");
                System.out.println(error);
                System.out.println(msg);

                // progress 종료

                if (error != "null") {
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                    System.out.println("저장 실패");
                }
                else if (msg != "null") {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    System.out.println("저장 성공");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_exercise", handler).execute(param);
//        new HttpTask("http://192.168.1.162:4002/android/update_user_exercise", handler).execute(param);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_test_prev:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_03, new Ex03AFragment()).commit();
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
