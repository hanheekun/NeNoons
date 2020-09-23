package com.pixelro.nenoons.menu.exercise.ex02;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01Activity;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Ex02CFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected SharedPreferencesManager mSm;
   // private ImageView Iv_coin, Iv_coin2, Iv_coin3;
    private View mView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_02_c, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context mContext =getContext();
        mView =view;
     /*   Animation coin_anim, bounce_anim,bounce_anim2;

        bounce_anim2=AnimationUtils.loadAnimation(getContext(),R.anim.bounce_anim2);
        bounce_anim =AnimationUtils.loadAnimation(getContext(),R.anim.bounce_anim);
        coin_anim = AnimationUtils.loadAnimation(getContext(),R.anim.coin_anim);

        Iv_coin = (ImageView)view.findViewById(R.id.imageView_coin_bounce1);
        Iv_coin.startAnimation(coin_anim);

        Iv_coin2 =(ImageView)view.findViewById(R.id.imageView_coin_bounce2);
        Iv_coin2.startAnimation(bounce_anim);

        Iv_coin3 =(ImageView)view.findViewById(R.id.imageView_coin_bounce3);
        Iv_coin3.startAnimation(bounce_anim2); */

        view.findViewById(R.id.button_ex2_prev).setOnClickListener(this);
        view.findViewById(R.id.button_ex2_next).setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,++curTotalEXNumber);

        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,true);
        editor.commit();

        // 운동 정보 전달
        ExProfile exProfile = new ExProfile();
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyyMMddHHmmss");
        exProfile.type = ExProfile.Type.TYPE_2;
        exProfile.date = formatNowDate.format(nowDate);
        exProfile.level = ((Ex02Activity)getActivity()).mCurLevel;

        String token = getToken(getActivity());


        // 전달
        HashMap<String, String> param = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param.put("token", token);    //PARAM
        param.put("date", exProfile.date);    //PARAM
        param.put("type", String.valueOf(exProfile.type));    //PARAM
        param.put("level", String.valueOf(exProfile.level));    //PARAM
        param.put("reserved1", String.valueOf(exProfile.reserved1));    //PARAM
        param.put("reserved2", String.valueOf(exProfile.reserved2));    //PARAM
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

                //Toast.makeText(mContext, "error = " + error + " msg = " + msg, Toast.LENGTH_SHORT).show();

                if (error != "null") {
                    //Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                    System.out.println("저장 실패");
                }
                else if (msg != "null") {
                    //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    System.out.println("저장 성공");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                //Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_exercise", handler).execute(param);
//        new HttpTask("http://192.168.1.162:4002/android/update_user_exercise", handler).execute(param);

        mSm = new SharedPreferencesManager(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();

        Typeface face = mSm.getFontTypeface();
        ((TextView)mView.findViewById(R.id.textView_b82)).setTypeface(face);

        ((Button)mView.findViewById(R.id.button_ex2_next)).setTypeface(face);
        ((Button)mView.findViewById(R.id.button_ex2_prev)).setTypeface(face);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_ex2_prev:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_ex_02, new Ex02AFragment()).commit();
                break;
            case R.id.button_ex2_next:
                getActivity().finish();
                break;
        }
    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }
}
