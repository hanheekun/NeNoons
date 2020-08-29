package com.pixelro.nenoons.menu.my;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.PersonalProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MySurveyActivity extends AppCompatActivity   implements View.OnClickListener{

    private SharedPreferencesManager mSm;
    private PersonalProfile mPersonalProfile;
    private ProgressDialog mProgressDialog;

    // Get reference of widgets from XML layout
    Spinner SpLeft;
    Spinner SpRight;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_survey);

        mSm = new SharedPreferencesManager(this);

        mPersonalProfile = new PersonalProfile();

        findViewById(R.id.button_arrow_close_background).setOnClickListener(this);
        findViewById(R.id.button_account_survey_next).setOnClickListener(this);

        SpLeft = (Spinner)( findViewById(R.id.spinner_survey_left));
        SpRight = (Spinner)( findViewById(R.id.spinner_survey_right));

        MySurveyActivity.SurveyAdapter adapter = new MySurveyActivity.SurveyAdapter(this,R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_left));
        SpLeft.setAdapter(adapter);
        adapter = new MySurveyActivity.SurveyAdapter(this,R.layout.spinner_item,(String[])getResources().getStringArray(R.array.survey_right));
        SpRight.setAdapter(adapter);

        // 기존 정보 가지고 오기
        // name 가지고 오기
        HashMap<String, String> param = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param.put("token", mSm.getToken());    //PARAM
        Handler handler = new Handler(message -> {

            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            System.out.println(result);
            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                String save = j.getString("save");
                System.out.println(error);
                System.out.println(error == null);

                if (error == "null" && save != "null") {

                  // 이전 정보로 버튼 셋팅
                    String[] savedSurvey = save.split(",");
                    if (savedSurvey[0].compareTo("1") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_glasses_1)).setChecked(true);
                    }
                    else if (savedSurvey[0].compareTo("2") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_glasses_2)).setChecked(true);
                    }
                    else if (savedSurvey[0].compareTo("3") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_glasses_3)).setChecked(true);
                    }
                    else if (savedSurvey[0].compareTo("4") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_glasses_4)).setChecked(true);
                    }

                    if (savedSurvey[1] != "0"){
                        SpLeft.setSelection(Integer.parseInt(savedSurvey[1]));
                    }
                    if (savedSurvey[2] != "0"){
                        SpRight.setSelection(Integer.parseInt(savedSurvey[2]));
                    }

                    if ((Byte.parseByte(savedSurvey[3]) & 1) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_status_1)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[3]) & 2) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_status_2)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[3]) & 4) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_status_3)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[3]) & 8) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_status_4)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[3]) & 16) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_status_5)).setChecked(true);
                    }

                    if ((Byte.parseByte(savedSurvey[4]) & 1) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_surgery_1)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[4]) & 2) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_surgery_2)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[4]) & 4) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_surgery_3)).setChecked(true);
                    }
                    if ((Byte.parseByte(savedSurvey[4]) & 8) != 0) {
                        ((CheckBox)findViewById(R.id.checkBox_surgery_4)).setChecked(true);
                    }

                    if (savedSurvey[5].compareTo("1") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_exercise_1)).setChecked(true);
                    }
                    else if (savedSurvey[5].compareTo("2") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_exercise_2)).setChecked(true);
                    }
                    else if (savedSurvey[5].compareTo("3") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_exercise_3)).setChecked(true);
                    }

                    if (savedSurvey[6].compareTo("1") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_food_1)).setChecked(true);
                    }
                    else if (savedSurvey[6].compareTo("2") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_food_2)).setChecked(true);
                    }
                    else if (savedSurvey[6].compareTo("3") == 0){
                        ((RadioButton)findViewById(R.id.radioButton_food_3)).setChecked(true);
                    }

                } else {
                    //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        });

        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/get_user_survey_simple", handler).execute(param);



    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_arrow_close_background:
                onBackPressed();
                break;
            case R.id.button_account_survey_next:

                // 정보 저장
                byte saveTemp = 0x0;

                // profile에 정보 입력 // 아직 내용 check 안함
                if(((RadioButton)findViewById(R.id.radioButton_glasses_1)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.NONE;
                    mPersonalProfile.survey_save += "1,";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_glasses_2)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.GLASSESS;
                    mPersonalProfile.survey_save += "2,";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_glasses_3)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.FAR_VISION;
                    mPersonalProfile.survey_save += "3,";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_glasses_4)).isChecked()){
                    mPersonalProfile.glasses = PersonalProfile.Glasses.CONTACT;
                    mPersonalProfile.survey_save += "4,";
                }
                else{
                    mPersonalProfile.survey_save += "0,";
                }

                mPersonalProfile.left = (String)SpLeft.getSelectedItem();
                mPersonalProfile.survey_save += SpLeft.getSelectedItemPosition() + ",";
                mPersonalProfile.right = (String)SpRight.getSelectedItem();
                mPersonalProfile.survey_save += SpRight.getSelectedItemPosition() + ",";

                mPersonalProfile.status = "";
                if(((CheckBox)findViewById(R.id.checkBox_status_1)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.MYOPIA + " ";
                    saveTemp |= 0x1;
                }
                if(((CheckBox)findViewById(R.id.checkBox_status_2)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.EMMETROPIA + " ";
                    saveTemp |= 0x2;
                }
                if(((CheckBox)findViewById(R.id.checkBox_status_3)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.ASTIGMATISM + " ";
                    saveTemp |= 0x4;
                }
                if(((CheckBox)findViewById(R.id.checkBox_status_4)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.HYPEROPIA + " ";
                    saveTemp |= 0x8;
                }
                if(((CheckBox)findViewById(R.id.checkBox_status_5)).isChecked()){
                    mPersonalProfile.status += PersonalProfile.Status.UNKNOWN + " ";
                    saveTemp |= 0x10;
                }

                mPersonalProfile.survey_save += (int)saveTemp + ",";

                saveTemp = 0x0;

                mPersonalProfile.surgery = "";
                if(((CheckBox)findViewById(R.id.checkBox_surgery_1)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.LASIKLASEK + " ";
                    saveTemp |= 0x1;
                }
                if(((CheckBox)findViewById(R.id.checkBox_surgery_2)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.OLD + " ";
                    saveTemp |= 0x2;
                }
                if(((CheckBox)findViewById(R.id.checkBox_surgery_3)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.CATARACT + " ";
                    saveTemp |= 0x4;
                }
                if(((CheckBox)findViewById(R.id.checkBox_surgery_4)).isChecked()){
                    mPersonalProfile.surgery += PersonalProfile.Surgery.NONE + " ";
                    saveTemp |= 0x8;
                }

                mPersonalProfile.survey_save += (int)saveTemp + ",";

                if(((RadioButton)findViewById(R.id.radioButton_exercise_1)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.YES;
                    mPersonalProfile.survey_save += "1,";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_exercise_2)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.NO;
                    mPersonalProfile.survey_save += "2,";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_exercise_3)).isChecked()){
                    mPersonalProfile.exercise = PersonalProfile.Excercise.SOMETIMES;
                    mPersonalProfile.survey_save += "3,";
                }
                else {
                    mPersonalProfile.survey_save += "0,";
                }

                if(((RadioButton)findViewById(R.id.radioButton_food_1)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.YES;
                    mPersonalProfile.survey_save += "1";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_food_2)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.NO;
                    mPersonalProfile.survey_save += "2";
                }
                else if(((RadioButton)findViewById(R.id.radioButton_food_3)).isChecked()){
                    mPersonalProfile.food = PersonalProfile.Food.SOMETIMES;
                    mPersonalProfile.survey_save += "3";
                }
                else {
                    mPersonalProfile.survey_save += "0";
                }

                // 서버연결 20200715

                ////////////////////////////////////////////////////////////
                // 회원 정보 전달
                ////////////////////////////////////////////////////////////

                mProgressDialog = ProgressDialog.show(this, "", "전송중...", true, true);

                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("token", mSm.getToken());    //PARAM
                param.put("glasses", mPersonalProfile.glasses);    //PARAM
                param.put("left", mPersonalProfile.left);    //PARAM
                param.put("right", mPersonalProfile.right);    //PARAM
                param.put("status", mPersonalProfile.status);    //PARAM
                param.put("surgery", mPersonalProfile.surgery);    //PARAM
                param.put("exercise", mPersonalProfile.exercise);    //PARAM
                param.put("food", mPersonalProfile.food);    //PARAM
                param.put("save", mPersonalProfile.survey_save);    //PARAM
                Handler handler = new Handler(message -> {

                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);

                    // progress 종료
                    if (mProgressDialog != null) mProgressDialog.dismiss();

                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String msg = j.getString("msg");
                        System.out.println(error);
                        System.out.println(msg);

                        // progress 종료

                        if (error != "null") {
                            //Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            System.out.println("저장 실패");
                        }
                        else if (msg != "null") {
                            //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            System.out.println("저장 성공");

                            mSm.setName(mPersonalProfile.name);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 실패
                        //Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                // API 주소와 위 핸들러 전달 후 실행.
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_survey_simple", handler).execute(param);
//                new HttpTask("http://192.168.1.162:4002/android/update_user_survey", handler).execute(param);



                // main 으로 이동
                finish();

                // 로그인 페이지 전환
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.nav_login_fragment, new AccountLoginFragment()).commit();

                break;


        }
    }

    // 선택되면 글씨 색 바꾸기
    class SurveyAdapter extends ArrayAdapter<String> {

        public SurveyAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public boolean isEnabled(int position){
            if(position == 0)
            {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            }
            else
            {
                return true;
            }
        }
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if(position == 0){
                // Set the hint text color gray
                tv.setTextColor(Color.rgb(1,67,190));
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;

            if (position == 0) {
                tv.setTextColor(Color.BLACK);
            }
            else {
                tv.setTextColor(Color.rgb(1,67,190));
            }
            return view;
        }
    }

}
