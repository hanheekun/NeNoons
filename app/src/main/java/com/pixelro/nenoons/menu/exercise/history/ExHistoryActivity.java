package com.pixelro.nenoons.menu.exercise.history;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ExHistoryActivity extends BaseActivity implements View.OnClickListener, OnChartValueSelectedListener {

    private final static String TAG = ExHistoryActivity_calendar.class.getSimpleName();

    private BarChart chart;
    private TextView TvDate;

    public ProgressDialog mLoadingProgressDialog;

    private int mHistory[][] = new int[32][4];
    private Context mContext;

    private ListView mLvProtocol;
    private ProtocolListAdapter mProtocolListAdapter;

    private ArrayList<ExProfile> mExProfileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history_graph);
        mContext = getApplicationContext();
        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);
        chart = (BarChart)findViewById(R.id.barchart_ex_history);

        TvDate = (TextView)findViewById(R.id.textView_ex_history_date);
        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
        // 년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy.MM");
        TvDate.setText(fourteen_format.format(date_now));


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

            // progress 종료
            if (mLoadingProgressDialog != null) mLoadingProgressDialog.dismiss();

            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                JSONArray jlist = j.getJSONArray("list");
                if (jlist.length()==0) {
                    // 목록이 없음
                    // 그래프 출력
                    setChart();
                    return true;
                }
                // progress 종료

                if (error != "null") {
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                    System.out.println("목록 실패");
                }
                mExProfileList = new ArrayList<>();

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

                //Toast.makeText(mContext, "mExProfileList length = " + mExProfileList.size() , Toast.LENGTH_SHORT).show();

                for(ExProfile exProfile : mExProfileList){
                    int day = Integer.parseInt(exProfile.date.substring(6,8));
                    mHistory[day][exProfile.type-1]++;
                }
                // 그래프 출력
                setChart();

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

        // 그래프 설정
        Description desc ;
        Legend L;

        L = chart.getLegend();
        desc = chart.getDescription();
        desc.setText(""); // this is the weirdest way to clear something!!
        L.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setAxisMaximum(8f); // the axis maximum is 100
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

        // 항목 리스트
        mLvProtocol = findViewById(R.id.listview_ex_history);
        mLvProtocol.setDivider(null); // 중간선 제거
        mLvProtocol.setDividerHeight(0); // 중간선 제거

        // 프로토콜 리스트 생성
        mProtocolListAdapter = new ProtocolListAdapter();
        mLvProtocol.setAdapter(mProtocolListAdapter);
    }

    void setChart(){

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 1; i < mHistory.length ; i++){
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

        //Toast.makeText(this,""+e.getX() + " " + e.getY(),Toast.LENGTH_SHORT).show();

        // 선택한 날 운동 출력
        if (mExProfileList.size()>0){

            ArrayList<ExProfile> selectedExProfileList = new ArrayList<>();
            for(ExProfile exProfile : mExProfileList){
                if (Integer.parseInt(exProfile.date.substring(6,8)) == e.getX()){
                    selectedExProfileList.add(exProfile);
                }
            }

            mProtocolListAdapter.setData(selectedExProfileList);
            // list 출력
            mProtocolListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onNothingSelected() {

    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }

    static class ViewHolder {
        TextView TvDate;
        TextView TvTitleSub;
        TextView TvTitleMain;
    }

    // Adapter for holding devices found through scanning.
    private class ProtocolListAdapter extends BaseAdapter {

        //private ArrayList<BluetoothDevice> mLeDevices;
        private ArrayList<ExProfile> mExDatalList;
        private LayoutInflater mInflator;

        public ProtocolListAdapter() {
            super();
            mExDatalList = new ArrayList<ExProfile>();
            //mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
        }

        public void setData(ArrayList<ExProfile> data) {
            mExDatalList = data;
        }

        public void clear() {
            mExDatalList.clear();
        }

        @Override
        public int getCount() {
            return mExDatalList.size();
        }

        @Override
        public Object getItem(int i) {
            return mExDatalList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_protocol, null);
                viewHolder = new ViewHolder();
                viewHolder.TvDate = (TextView)  view.findViewById(R.id.textView_ex_history_date);
                viewHolder.TvTitleSub = (TextView) view.findViewById(R.id.textView_ex_history_title_sub);
                viewHolder.TvTitleMain = (TextView) view.findViewById(R.id.textView_ex_history_title_main);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            ExProfile data = mExDatalList.get(i);

            String str = "";
            str += data.date.substring(0,4)+"-"+data.date.substring(4,6)+"-"+data.date.substring(6,8)+" "+data.date.substring(8,10)+":"+data.date.substring(10,12)+":"+data.date.substring(12,14);

            viewHolder.TvDate.setText(str);
            if (data.type == ExProfile.Type.TYPE_1){
                viewHolder.TvTitleSub.setText("수정체 근육 탄력 운동");
                viewHolder.TvTitleMain.setText("눈으로 풍선 터트리기");
            }
            else if (data.type == ExProfile.Type.TYPE_2){
                viewHolder.TvTitleSub.setText("눈 근육 스트레칭");
                viewHolder.TvTitleMain.setText("빠른 눈 깜빡이기");
            }
            else if (data.type == ExProfile.Type.TYPE_3){
                viewHolder.TvTitleSub.setText("눈 근육 스트레칭");
                viewHolder.TvTitleMain.setText("눈의 휴식");
            }
            else if (data.type == ExProfile.Type.TYPE_4){
                viewHolder.TvTitleSub.setText("눈 근육 스트레칭");
                viewHolder.TvTitleMain.setText("H 따라하기");
            }




            return view;
        }
    }
}













