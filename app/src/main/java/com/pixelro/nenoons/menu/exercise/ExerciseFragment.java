package com.pixelro.nenoons.menu.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01Activity;
import com.pixelro.nenoons.menu.exercise.ex02.Ex02Activity;
import com.pixelro.nenoons.menu.exercise.ex03.Ex03Activity;
import com.pixelro.nenoons.menu.exercise.ex04.Ex04Activity;
import com.pixelro.nenoons.menu.exercise.history.ExHistoryActivity;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class ExerciseFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private ExerciseViewModel exerciseViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View mView;

    private TextView TvDate;
    private TextView Tvnumber;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        mView = root;

        root.findViewById(R.id.button_ex_01).setOnClickListener(this);
        root.findViewById(R.id.button_ex_02).setOnClickListener(this);
        root.findViewById(R.id.button_ex_03).setOnClickListener(this);
        root.findViewById(R.id.button_ex_04).setOnClickListener(this);
        root.findViewById(R.id.button_ex_history).setOnClickListener(this);

        //Calendar cal = Calendar.getInstance();
        //int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //int preDayOfMonth = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY,0);

//        if (dayOfMonth == preDayOfMonth){
//
//        }
//        else {
//            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY,dayOfMonth);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_5_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_6_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_7_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_8_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_9_COMPLETE,false);
//            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_10_COMPLETE,false);
//
//            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
//
//            editor.commit();
//        }

        TvDate = (TextView)root.findViewById(R.id.textView_ex_date);

        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
        // 년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy.MM.dd");
        TvDate.setText(fourteen_format.format(date_now));

        Tvnumber = (TextView)root.findViewById(R.id.textView_ex_number);

        root.findViewById(R.id.textView_ex_number).setOnLongClickListener(this);


        // 운동 기록 오늘 날짜 업데이트 하기
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        ArrayList<ExProfile> mExProfileList = new ArrayList<>();
        // 0 현재 month, -1 지난달
        String token = getToken(getContext());
        HashMap<String, String> param = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param.put("token", token);    //PARAM
        param.put("month", "0");    //PARAM
        Handler handler = new Handler(message -> {

            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            System.out.println(result);

            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                JSONArray jlist = j.getJSONArray("list");
                if (jlist.length()==0) {
                    // 목록이 없음
                    return true;
                }
                // progress 종료

                if (error != "null") {
                    //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    System.out.println("목록 실패");
                }

                // 목록 변환및 저장
                for (int i=0;i<jlist.length();i++) {
                    JSONObject jEx = (JSONObject) jlist.get(i);
                    ExProfile exProfile = new ExProfile();
                    exProfile.date = jEx.getString("date"); // 이 날짜를 파싱해서 어레이에 집어넣으면 됩니다.
                    exProfile.type = jEx.getInt("type");
                    exProfile.level = jEx.getInt("level");
                    mExProfileList.add(exProfile); // 이 어레이를 전달해서 사용하세요
                    //mExProfileList.add(exProfile); // 이 어레이를 전달해서 사용하세요
                }
                System.out.println(error);
                System.out.println(jlist);
                System.out.println(mExProfileList);

                //Toast.makeText(getActivity(), "mExProfileList length = " + mExProfileList.size() , Toast.LENGTH_SHORT).show();

                int exCnt = 0;

                for(ExProfile exProfile : mExProfileList){
                    int day = Integer.parseInt(exProfile.date.substring(6,8));
                    if (day == dayOfMonth){
                        exCnt++;
                        if (exProfile.type == ExProfile.Type.TYPE_1){
                            sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE, true);
                            editor.commit();
                        }
                        else if (exProfile.type == ExProfile.Type.TYPE_2){
                            sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE, true);
                            editor.commit();
                        }
                        else if (exProfile.type == ExProfile.Type.TYPE_3){
                            sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE, true);
                            editor.commit();
                        }
                        else if (exProfile.type == ExProfile.Type.TYPE_4){
                            sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE, true);
                            editor.commit();
                        }
                    }
                }

                sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER, exCnt);
                editor.commit();

                //게시판 업데이트
                updateBoard();


            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                //Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/list_user_exercise", handler).execute(param);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateBoard();

    }

    public void updateBoard()
    {
        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.button_ex_01_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_01)).setBackgroundResource(R.drawable.button_ex_01);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.button_ex_02_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_02)).setBackgroundResource(R.drawable.button_ex_02);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.button_ex_03_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_03)).setBackgroundResource(R.drawable.button_ex_03);
        }

        if (sharedPreferences.getBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false)){
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.button_ex_04_c);
        }
        else{
            ((Button)mView.findViewById(R.id.button_ex_04)).setBackgroundResource(R.drawable.button_ex_04);
        }

        //
        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        Tvnumber.setText("");
        String s= "오늘 ";
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
        Tvnumber.append(ss1);
        s= "" + curTotalEXNumber;
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,s.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);// set color
        Tvnumber.append(ss1);
        s= " 개의 눈 운동을 하셨네요.";
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
        Tvnumber.append(ss1);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.button_ex_01:
                Intent intent = new Intent(getActivity(), Ex01Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_02:
                intent = new Intent(getActivity(), Ex02Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_03:
                intent = new Intent(getActivity(), Ex03Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_04:
                intent = new Intent(getActivity(), Ex04Activity.class);
                startActivity(intent);
                break;
            case R.id.button_ex_history:
                intent = new Intent(getActivity(), ExHistoryActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {

        switch(v.getId()){
            case R.id.textView_ex_number:
                //resetExNumber();
                //onResume();
                break;
        }

        return false;
    }

    public void resetExNumber(){
        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
        editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);

        editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);
        editor.commit();
    }

    public String getToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN, "");
    }
}
