package com.pixelro.nenoons.menu.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SplashActivity;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.account.AccountDialog;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01Activity;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01BFragment;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestHistoryActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;

    public ProgressDialog mLoadingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);
        mContext = getApplicationContext();
        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        List<String> list = new ArrayList<>();

        // simple list view
        ListView listview = (ListView)findViewById(R.id.listview_home_history);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        // 서버연결 20200715

        //////////////////////////////////////////////////////////////////////////////////////////
        // 측정 기록 불러오기
        //////////////////////////////////////////////////////////////////////////////////////////
        String token = getToken(this);

        // 0 현재 month, -1 지난달
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
                JSONArray jList = j.getJSONArray("list");
                if (jList.length()==0) {
                    // 목록이 없음
                    return true;
                }
                // progress 종료

                if (error != "null") {
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                    System.out.println("목록 실패");
                }
                ArrayList testProfileList = new ArrayList();

                // 목록 변환및 저장
                for (int i=0;i<jList.length();i++) {
                    JSONObject jTest = (JSONObject) jList.get(i);
                    TestProfile testProfile = new TestProfile();
                    testProfile.date = jTest.getString("date");  // 이 날짜를 파싱해서 어레이에 집어넣으면 됩니다.
                    testProfile.distance = jTest.getInt("distance");
                    testProfile.redgreen = jTest.getInt("redgreen");
                    testProfile.background = jTest.getInt("background");
                    testProfile.font = jTest.getInt("font");
                    testProfile.bright = jTest.getInt("bright");
                    testProfile.reserved1 = jTest.getInt("reserved1");
                    testProfile.reserved2 = jTest.getInt("reserved2");
                    testProfileList.add(testProfile); // 이 어레이를 전달해서 사용하세요
                }
                System.out.println(error);
                System.out.println(jList);
                System.out.println(testProfileList);

            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/list_user_test", handler).execute(param);
//        new HttpTask("http://192.168.1.162:4002/android/list_user_test", handler).execute(param);



        // 측정 기록  progress 시작
        mLoadingProgressDialog = ProgressDialog.show(this, "", "불러오는 중...", true, true);

        // 불러오기 완료 test
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                // progress 종료
                if (mLoadingProgressDialog != null) mLoadingProgressDialog.dismiss();

                //받은 결과에서 측정 시간과 거리 사용
                int distance_1 = 56;
                String data_1 = "20200703140000";
                int distance_2 = 40;
                String data_2 = "20200704093000";

                //List<TestProfile> history = new ArrayList<>();

                //리스트뷰에 보여질 아이템을 추가
                list.add(data_1 + "    " + distance_1);
                list.add(data_2 + "    " + distance_2);

                adapter.notifyDataSetChanged();

            }
        }, 1000);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_arrow_back_background:
                onBackPressed();
                break;
        }
    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }
}
