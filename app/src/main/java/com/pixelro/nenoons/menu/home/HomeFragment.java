package com.pixelro.nenoons.menu.home;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.nenoons.EYELAB;
import com.pixelro.nenoons.ExProfile;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.TestProfile;
import com.pixelro.nenoons.menu.exercise.ExerciseViewModel;
import com.pixelro.nenoons.server.HttpTask;
import com.pixelro.nenoons.test.TestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = HomeFragment.class.getSimpleName();

    private HomeViewModel homeViewModel;
    private ExerciseViewModel exerciseViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView TvAddress;

    private WebView mHomeWebView;
    private WebSettings mHomeWebViewSettings;

    private WebView mBannerWebView;
    private WebSettings mBannerWebViewSettings;

    private Bundle mHomeWebViewBundle;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
        Geocoder geocoder;

    protected SharedPreferencesManager mSm;
    private TextView TvExNumber;
    private TextView TvScreenTime;

    public boolean mTest = false;
    private TextView TvAge,Tv_test;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        root.findViewById(R.id.view_main_age_result_btn).setOnClickListener(this);
//        root.findViewById(R.id.imageView_app_link_test).setOnClickListener(this);

        geocoder = new Geocoder(getActivity().getApplicationContext());

        mSm = new SharedPreferencesManager(getActivity());


        root.findViewById(R.id.button_home_address).setOnClickListener(this);
        TvAddress = (TextView) root.findViewById(R.id.textView_home_address);


        // home 웹뷰 시작
        mHomeWebView = (WebView) root.findViewById(R.id.webView_home);
        mHomeWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mHomeWebViewSettings = mHomeWebView.getSettings(); //세부 세팅 등록
        mHomeWebViewSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mHomeWebViewSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mHomeWebViewSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mHomeWebViewSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mHomeWebViewSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mHomeWebViewSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mHomeWebViewSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mHomeWebViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mHomeWebViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mHomeWebViewSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mHomeWebView.loadUrl("https://www.nenoons.com/app-home-page"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
//        mHomeWebView.loadUrl("http://192.168.1.162:3001/app-home-page"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        mHomeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String urlPath = request.getUrl().toString();
                System.out.println(">>>>>>>>>>>>>>    " + urlPath);
                Intent intent = new Intent(getActivity(), WebActivity.class);
                ////////////////////////////////////////////////////////////////////////
                // home 에서 o2o 바로 연결
                ////////////////////////////////////////////////////////////////////////
                intent.putExtra("url", urlPath);
                intent.putExtra("token", getToken(getActivity()));
                getActivity().startActivity(intent);

                return true;
            }
        });


        // 배너 웹뷰 시작
        mBannerWebView = (WebView) root.findViewById(R.id.webView_home_ad);
        mBannerWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mBannerWebViewSettings = mBannerWebView.getSettings(); //세부 세팅 등록
        mBannerWebViewSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mBannerWebViewSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mBannerWebViewSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mBannerWebViewSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mBannerWebViewSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mBannerWebViewSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mBannerWebViewSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mBannerWebViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mBannerWebViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mBannerWebViewSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mBannerWebView.loadUrl("https://www.nenoons.com/app-banner");
        mBannerWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String urlPath = request.getUrl().toString();
                System.out.println(">>>>>>>>>>>>>>    " + urlPath);
                Intent intent = new Intent(getActivity(), WebActivity.class);
                ////////////////////////////////////////////////////////////////////////
                // home 에서 o2o 배너 바로 연결
                ////////////////////////////////////////////////////////////////////////
                intent.putExtra("url", urlPath);
                intent.putExtra("token", getToken(getActivity()));
                getActivity().startActivity(intent);

                return true;
            }

        });

//        if (webViewBundle == null) {
//            mBannerWebView.loadUrl("https://www.nenoons.com/app-banner");
//            Log.v(TAG, "webViewBundle == null");
//        } else {
//            mBannerWebView.restoreState(webViewBundle);
//            Log.v(TAG, "webViewBundle != null;");
//        }
//
//        if (savedInstanceState == null) {
//            mHomeWebView.setWebViewClient(new WebViewClient());
//            mHomeWebView.getSettings().setJavaScriptEnabled(true);
//            mHomeWebView.loadUrl("https://nenoons.com/app-main");
//        } else {
//            mHomeWebView.restoreState(savedInstanceState);
//        }


        TvAge = (TextView) root.findViewById(R.id.textView_home_age);
        Tv_test = (TextView) root.findViewById(R.id.textView_home_17);
        TvExNumber = (TextView) root.findViewById(R.id.textView_home_ex_number);
        TvScreenTime = (TextView) root.findViewById(R.id.textView_home_screen_time);


        root.findViewById(R.id.button_home_test).setOnClickListener(this);
        root.findViewById(R.id.button_home_test_history).setOnClickListener(this);


        // 주소 전송 픽셀로 초기화
        String token = getToken(getContext());
        HashMap<String, String> param = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param.put("token", token);    //PARAM
        param.put("gpsAddress", "경기도 의왕시 이미로 40");    //PARAM
        param.put("gpsLatitude", String.valueOf(37.400627));    //PARAM
        param.put("gpsLongitude", String.valueOf(126.99122899999999));    //PARAM
        Handler handler = new Handler(message -> {

            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            System.out.println(result);
            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                String msg = j.getString("msg");
                System.out.println(error);
                System.out.println(error == null);

                if (error == "null" && msg != "null") {

                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_gps", handler).execute(param);
//                            new HttpTask("http://192.168.1.162:4002/android/update_user_gps", handler).execute(param);


        // 운동 기록 오늘 날짜 업데이트 하기
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        ArrayList<ExProfile> mExProfileList = new ArrayList<>();
        // 0 현재 month, -1 지난달
        HashMap<String, String> param2 = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param2.put("token", token);    //PARAM
        param2.put("month", "0");    //PARAM
        Handler handler2 = new Handler(message -> {

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
                refrashBoard();


            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                //Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/list_user_exercise", handler2).execute(param2);


        // 측정 기록 마지막 기록 가지고 오기
        // 0 현재 month, -1 지난달
        HashMap<String, String> param3 = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param3.put("token", token);    //PARAM
        param3.put("month", "0");    //PARAM
        Handler handler3 = new Handler(message -> {

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
                    //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    System.out.println("목록 실패");
                }
                ArrayList<TestProfile> testProfileList = new ArrayList<>();

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

                for(TestProfile testProfile : testProfileList){
                    sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putInt(EYELAB.APPDATA.TEST.LAST_DISTANCE,testProfile.distance);
                    editor.commit();
                }

                refrashBoard();

            } catch (JSONException e) {
                e.printStackTrace();
                // 실패
                //Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/list_user_test", handler3).execute(param3);


        // name 가지고 오기
        HashMap<String, String> param4 = new HashMap<String, String>();
        // 파라메터는 넣기 예
        param4.put("token", token);    //PARAM
        Handler handler4 = new Handler(message -> {

            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            System.out.println(result);
            try {
                JSONObject j = new JSONObject(result);
                String error = j.getString("error");
                String name = j.getString("name");
                System.out.println(error);
                System.out.println(error == null);

                if (error == "null" && name != "null") {

                    //Toast.makeText(getActivity(), "name = " + name , Toast.LENGTH_SHORT).show();
                    SharedPreferencesManager sfm = new SharedPreferencesManager(getActivity());
                    sfm.setName(name);

                } else {
                    //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        });

        // API 주소와 위 핸들러 전달 후 실행.
        new HttpTask("https://nenoonsapi.du.r.appspot.com/android/myname", handler4).execute(param4 );

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

        //mHomeWebView.saveState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        //mHomeWebViewBundle = new Bundle();
        //mHomeWebView.saveState(mHomeWebViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();

        refrashBoard();
        Typeface face = mSm.getFontTypeface();
        TvAddress.setText(mSm.getAddress());
        Tv_test.setTypeface(face);

        mBannerWebView.loadUrl("https://www.nenoons.com/app-banner");
//        if (mBannerWebView !=null) {
//            mAdWebView.restoreState(webViewBundle);
//        }
    }

    public void refrashBoard() {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int preDayOfMonth = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY, 0);

        if (dayOfMonth == preDayOfMonth) {

        } else {
            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY, dayOfMonth);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_5_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_6_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_7_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_8_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_9_COMPLETE, false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_10_COMPLETE, false);

            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER, 0);

            editor.commit();
        }

        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER, 0);

        final long now = System.currentTimeMillis();

        Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
        // 년월일시분초 14자리 포멧
        SimpleDateFormat fourteen_format = new SimpleDateFormat("HH");
        int HH = Integer.parseInt(fourteen_format.format(date_now));

        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -HH);   // 1

        final UsageStatsManager mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(getActivity().USAGE_STATS_SERVICE);
        //final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now-500, now);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
        //final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(), now);

        long totalMin;
        int min;
        int hour;

        if (stats == null || stats.isEmpty()) {
            // Usage access is not enabled
            long tatalTimeMS = 0;
        } else {

            long tatalTimeMS = 0;

            for (UsageStats stat : stats) {
                tatalTimeMS += stat.getTotalTimeInForeground();
            }
            //Toast.makeText(getActivity(),"total time" + (tatalTimeMS/1000)/3600,Toast.LENGTH_SHORT).show();
            totalMin = (tatalTimeMS / 1000) / 60;

            //totalMin = totalMin / 2;

            min = (int) (totalMin % 60);
            hour = (int) (totalMin / 60);

            //Toast.makeText(getActivity(),"Hour : " + hour + ", Min : " + min + " Total EX : " + curTotalEXNumber,Toast.LENGTH_SHORT).show();

            TvExNumber.setText("");
            String s = "" + curTotalEXNumber;
            SpannableString ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0); // set size
            TvExNumber.append(ss1);
            s = "번";
            ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0, s.length(), 0); // set size
            TvExNumber.append(ss1);

            //TvExNumber.setText(curTotalEXNumber + " 번");

            TvScreenTime.setText("");
            s = "" + hour;
            ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s = "시간";
            ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0, s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s = "" + min;
            ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s = "분";
            ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0, s.length(), 0); // set size
            TvScreenTime.append(ss1);

            //TvScreenTime.setText(hour+"시 "+min+" 분");
        }

        //
        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST, MODE_PRIVATE);
        int distance = sharedPreferences.getInt(EYELAB.APPDATA.TEST.LAST_DISTANCE, -1);

        String ageNumber = "";
        String ageText = "";

        if(distance > 0){
            // 거리에 따른 나이
            if (distance <= 22) {
                ageNumber = "44";
                ageText = "세 이하";
            } else if (distance >= 23 && distance <= 30) {
                ageNumber = "44";
                ageText = "세 이하";
            } else if (distance >= 31 && distance <= 37) {
                ageNumber = "40";
                ageText = "대 후반";
            } else if (distance >= 38 && distance <= 47) {
                ageNumber = "50";
                ageText = "세";
            } else if (distance >= 48 && distance <= 57) {
                ageNumber = "50";
                ageText = "대 초반";
            } else if (distance >= 58 && distance <= 67) {
                ageNumber = "50";
                ageText = "대 중반";
            } else if (distance >= 68) {
                ageNumber = "56";
                ageText = "세 이상";
            }
        }
        else {
            ageNumber = "미측정";
            ageText = "";
        }


        TvAge.setText("");
        SpannableString ss1 = new SpannableString(ageNumber);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, ageNumber.length(), 0); // set size
        TvAge.append(ss1);
        ss1 = new SpannableString(ageText);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, ageText.length(), 0); // set size
        TvAge.append(ss1);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.view_main_age_result_btn:
//                Intent intent = new Intent(getContext(), TestActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.imageView_app_link_test:
//                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.lilysnc.pixelro.integral"); startActivity( launchIntent );
//                break;
            case R.id.button_home_address:
                Intent i = new Intent(getContext(), AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                break;
            case R.id.button_home_test:
                i = new Intent(getContext(), TestActivity.class);
                startActivity(i);
                break;
            case R.id.button_home_test_history:
                i = new Intent(getContext(), TestHistoryActivity.class);
                startActivity(i);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Context mContext = getContext();

        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case SEARCH_ADDRESS_ACTIVITY:

                if (resultCode == Activity.RESULT_OK) {

                    String data = intent.getExtras().getString("data");
                    if (data != null)

                        data = data.substring(data.indexOf(',') + 2, data.lastIndexOf('(') - 1);

                    List<Address> list = null;

                    try {
                        list = geocoder.getFromLocationName(
                                data, // 지역 이름
                                10); // 읽을 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            TvAddress.setText("해당되는 주소 정보는 없습니다");
                        } else {
                            //TvAddress.setText(list.get(0).getLatitude() + ", " + list.get(0).getLongitude());

                            data = makeShortAddress(data);

                            TvAddress.setText(data);
                            //          list.get(0).getCountryName();  // 국가명
                            //          list.get(0).getLatitude();        // 위도
                            //          list.get(0).getLongitude();    // 경도
                            // 서버연결 20200715
                            // 주소 변경 서버 연결

                            // 주소 저장

                            mSm.setAddress(data);

                            String token = getToken(getContext());
                            HashMap<String, String> param = new HashMap<String, String>();
                            // 파라메터는 넣기 예
                            param.put("token", token);    //PARAM
                            param.put("gpsAddress", data);    //PARAM
                            param.put("gpsLatitude", String.valueOf(list.get(0).getLatitude()));    //PARAM
                            param.put("gpsLongitude", String.valueOf(list.get(0).getLongitude()));    //PARAM
                            Handler handler = new Handler(message -> {

                                Bundle bundle = message.getData();
                                String result = bundle.getString("result");
                                System.out.println(result);
                                try {
                                    JSONObject j = new JSONObject(result);
                                    String error = j.getString("error");
                                    String msg = j.getString("msg");
                                    System.out.println(error);
                                    System.out.println(error == null);

                                    if (error == "null" && msg != "null") {

                                        //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            });
                            // API 주소와 위 핸들러 전달 후 실행.
                            new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_gps", handler).execute(param);
//                            new HttpTask("http://192.168.1.162:4002/android/update_user_gps", handler).execute(param);

                        }
                    }

                }


                break;

        }


    }

    public String getToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN, "");
    }

    public void setToken(Context context, String token) {
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.TOKEN, token);
        editor.commit();
    }

    public String makeShortAddress(String address){
        if (address.length() > 20){
            return address.substring(0,20) + "...";
        }
        else
        {
            return address;
        }
    }


}
