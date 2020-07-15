package com.pixelro.nenoons.menu.exercise.history;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pixelro.nenoons.BaseActivity;
import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.menu.exercise.ex01.Ex01Activity;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class ExHistoryActivity extends BaseActivity implements View.OnClickListener, OnChartValueSelectedListener {

    private final static String TAG = ExHistoryActivity_calendar.class.getSimpleName();

    private BarChart chart;

    public ProgressDialog mLoadingProgressDialog;

    private int mHistory[][] = new int[31][4];
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history_graph);
        mContext = getApplicationContext();
        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        chart = (BarChart)findViewById(R.id.barchart_ex_history);

        // 서버연결 20200715

        //////////////////////////////////////////////////////////////////////////////////////////
        // 측정 기록 불러오기
        //////////////////////////////////////////////////////////////////////////////////////////
        // 측정 기록  progress 시작
        mLoadingProgressDialog = ProgressDialog.show(this, "", "불러오는 중...", true, true);

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
                JSONArray jlist = j.getJSONArray("list");
                if (jlist.length()==0) {
                    // 목록이 없음
                    return true;
                }
                // progress 종료

                if (error != "null") {
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                    System.out.println("목록 실패");
                }
                ArrayList exProfileList = new ArrayList();

                // 목록 변환및 저장
                for (int i=0;i<jlist.length();i++) {
                    JSONObject jEx = (JSONObject) jlist.get(i);
                    ExProfile exProfile = new ExProfile();
                    exProfile.date = jEx.getString("date"); // 이 날짜를 파싱해서 어레이에 집어넣으면 됩니다.
                    exProfile.type = jEx.getInt("type");
                    exProfile.level = jEx.getInt("level");
                    exProfileList.add(exProfile); // 이 어레이를 전달해서 사용하세요
                }
                System.out.println(error);
                System.out.println(jlist);
                System.out.println(exProfileList);

            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/list_user_exercise", handler).execute(param);
//        new HttpTask("http://192.168.1.162:4002/android/list_user_exercise", handler).execute(param);


        // 불러오기 완료 test
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                // progress 종료
                if (mLoadingProgressDialog != null) mLoadingProgressDialog.dismiss();

                // 불러오기 완료 test

                mHistory[3][0] = 1;
                mHistory[3][2] = 2;
                mHistory[3][3] = 3;
                mHistory[5][1] = 1;
                mHistory[5][3] = 1;
                mHistory[25][2] = 2;
                mHistory[26][1] = 1;
                mHistory[26][2] = 3;


                // 그래프 출력
                setChart();

            }
        }, 1000);

        Description desc ;
        Legend L;

        L = chart.getLegend();
        desc = chart.getDescription();
        desc.setText(""); // this is the weirdest way to clear something!!
        L.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        //leftAxis.setAxisMaximum(8f); // the axis maximum is 100
        //leftAxis.setLabelCount(9, true);
        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        leftAxis.setTextSize(10f);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        //chart.setBackgroundColor(Color.parseColor("#ffd613"));
        chart.setBackgroundColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.setDrawBorders(false);
        chart.setDescription(desc);
        chart.setDrawValueAboveBar(true);
    }

    void setChart(){

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < mHistory.length ; i++){
            int sum = 0;
            for (int j = 0; j < mHistory[0].length ; j++){
                sum += mHistory[i][j];
            }
            entries.add(new BarEntry(i, sum));
        }

        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(Color.parseColor("#ffd613"));
        set.setValueTextColor(Color.rgb(155,155,155));

        BarData data = new BarData(set);

        data.setBarWidth(0.7f); // set custom bar width
        chart.setData(data);
        chart.setOnChartValueSelectedListener(this);

        // usage on whole data object
        data.setValueFormatter(new MyValueFormatter());
    }

    class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

//    private BarDataSet setData() {
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//
//        //entries.add(new BarEntry(0, 2));
//        entries.add(new BarEntry(1, 8));
//        entries.add(new BarEntry(2, 6));
//        entries.add(new BarEntry(3, 10));
//        entries.add(new BarEntry(4, 0));
//        entries.add(new BarEntry(5, 6));
//        entries.add(new BarEntry(6, 3));
//        entries.add(new BarEntry(7, 80));
//        entries.add(new BarEntry(8, 60));
//        entries.add(new BarEntry(9, 50));
//        entries.add(new BarEntry(10, 70));
//        entries.add(new BarEntry(11, 60));
//        entries.add(new BarEntry(12, 30));
//        entries.add(new BarEntry(13, 80));
//        entries.add(new BarEntry(14, 60));
//        entries.add(new BarEntry(15, 50));
//        entries.add(new BarEntry(16, 70));
//        entries.add(new BarEntry(17, 60));
//        entries.add(new BarEntry(18, 30));
//        entries.add(new BarEntry(19, 80));
//        entries.add(new BarEntry(20, 60));
//        entries.add(new BarEntry(21, 50));
//        entries.add(new BarEntry(22, 70));
//        entries.add(new BarEntry(23, 60));
//        entries.add(new BarEntry(24, 30));
//        entries.add(new BarEntry(25, 80));
//        entries.add(new BarEntry(26, 60));
//        entries.add(new BarEntry(27, 50));
//        entries.add(new BarEntry(28, 70));
//        entries.add(new BarEntry(29, 60));
//        entries.add(new BarEntry(30, 30));
//        entries.add(new BarEntry(31, 80));
//
//        BarDataSet set = new BarDataSet(entries, "");
//        set.setColor(Color.parseColor("#ffd613"));
//        set.setValueTextColor(Color.rgb(155,155,155));
//        //set.setDrawValues(false);
//
//        return set;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_arrow_back_background:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        final String x = chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis());

        //Toast.makeText(this,""+e.getX(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {

    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }
}













