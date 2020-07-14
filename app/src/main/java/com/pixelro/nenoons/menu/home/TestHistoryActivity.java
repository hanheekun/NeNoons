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

import com.auth0.android.jwt.JWT;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SplashActivity;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.account.AccountActivity;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01Activity;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01BFragment;

import java.util.ArrayList;
import java.util.List;

public class TestHistoryActivity extends AppCompatActivity implements View.OnClickListener{

    public ProgressDialog mLoadingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        List<String> list = new ArrayList<>();

        // simple list view
        ListView listview = (ListView)findViewById(R.id.listview_home_history);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        //////////////////////////////////////////////////////////////////////////////////////////
        // 측정 기록 불러오기
        //////////////////////////////////////////////////////////////////////////////////////////
        String token = getToken(this);

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
