package com.pixelro.nenoons.menu.home;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
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
import com.pixelro.nenoons.MainActivity;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.menu.exercise.ExerciseViewModel;
import com.pixelro.nenoons.test.TestActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private TextView TvAge;
    private TextView TvExNumber;
    private TextView TvScreenTime;

    public boolean mTest = false;

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

        root.findViewById(R.id.button_home_address).setOnClickListener(this);
        TvAddress = (TextView)root.findViewById(R.id.textView_home_address);

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


        TvAge = (TextView)root.findViewById(R.id.textView_home_age);
        TvExNumber = (TextView)root.findViewById(R.id.textView_home_ex_number);
        TvScreenTime = (TextView)root.findViewById(R.id.textView_home_screen_time);


        root.findViewById(R.id.button_home_test).setOnClickListener(this);

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

        mBannerWebView.loadUrl("https://www.nenoons.com/app-banner");
//        if (mBannerWebView !=null) {
//            mAdWebView.restoreState(webViewBundle);
//        }
    }

    public void refrashBoard(){
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_EXERCISE,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int preDayOfMonth = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY,0);

        if (dayOfMonth == preDayOfMonth){

        }
        else {
            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY,dayOfMonth);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_1_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_2_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_3_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_4_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_5_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_6_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_7_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_8_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_9_COMPLETE,false);
            editor.putBoolean(EYELAB.APPDATA.EXERCISE.EX_10_COMPLETE,false);

            editor.putInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);

            editor.commit();
        }

        int curTotalEXNumber = sharedPreferences.getInt(EYELAB.APPDATA.EXERCISE.EX_DAY_NUMBER,0);

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
        }
        else {

            long tatalTimeMS = 0;

            for (UsageStats stat:stats) {
                tatalTimeMS += stat.getTotalTimeInForeground();
            }
            //Toast.makeText(getActivity(),"total time" + (tatalTimeMS/1000)/3600,Toast.LENGTH_SHORT).show();
            totalMin = (tatalTimeMS/1000)/60;

            //totalMin = totalMin / 2;

            min = (int)(totalMin % 60);
            hour = (int)(totalMin / 60);

            //Toast.makeText(getActivity(),"Hour : " + hour + ", Min : " + min + " Total EX : " + curTotalEXNumber,Toast.LENGTH_SHORT).show();

            TvExNumber.setText("");
            String s= "" + curTotalEXNumber;
            SpannableString ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0,s.length(), 0); // set size
            TvExNumber.append(ss1);
            s= "번";
            ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
            TvExNumber.append(ss1);

            //TvExNumber.setText(curTotalEXNumber + " 번");

            TvScreenTime.setText("");
            s= "" + hour;
            ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0,s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s= "시간";
            ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s= ""+min;
            ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2f), 0,s.length(), 0); // set size
            TvScreenTime.append(ss1);
            s= "분";
            ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0,s.length(), 0); // set size
            TvScreenTime.append(ss1);

            //TvScreenTime.setText(hour+"시 "+min+" 분");
        }

        //
        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST,MODE_PRIVATE);
        int distance = sharedPreferences.getInt(EYELAB.APPDATA.TEST.LAST_DISTANCE,20);

        String ageNumber = "";
        String ageText = "";

        // 거리에 따른 나이
        if (distance <= 22){
            ageNumber = "44";
            ageText = "세 이하";
        }
        else if(distance >= 23 && distance <= 30){
            ageNumber = "44";
            ageText = "세 이하";
        }
        else if(distance >= 31 && distance <= 37){
            ageNumber = "40";
            ageText = "대 후반";
        }
        else if(distance >= 38 && distance <= 47){
            ageNumber = "50";
            ageText = "세";
        }
        else if(distance >= 48 && distance <= 57){
            ageNumber = "50";
            ageText = "대 초반";
        }
        else if(distance >= 58 && distance <= 67){
            ageNumber = "50";
            ageText = "대 중반";
        }
        else if(distance >= 68){
            ageNumber = "56";
            ageText = "세 이상";
        }

        TvAge.setText("");
        SpannableString ss1=  new SpannableString(ageNumber);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,ageNumber.length(), 0); // set size
        TvAge.append(ss1);
        ss1=  new SpannableString(ageText);
        ss1.setSpan(new RelativeSizeSpan(1f), 0,ageText.length(), 0); // set size
        TvAge.append(ss1);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
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

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){

            case SEARCH_ADDRESS_ACTIVITY:

                if(resultCode == Activity.RESULT_OK){

                    String data = intent.getExtras().getString("data");
                    if (data != null)

                        data = data.substring(data.indexOf(',')+2,data.lastIndexOf('(')-1);



                    List<Address> list = null;

                    try {
                        list = geocoder.getFromLocationName(
                                data, // 지역 이름
                                10); // 읽을 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            TvAddress.setText("해당되는 주소 정보는 없습니다");
                        } else {
                            //TvAddress.setText(list.get(0).getLatitude() + ", " + list.get(0).getLongitude());
                            TvAddress.setText(data);
                            //          list.get(0).getCountryName();  // 국가명
                            //          list.get(0).getLatitude();        // 위도
                            //          list.get(0).getLongitude();    // 경도

                        }
                    }

                }
                break;

        }

    }

    public String getToken(Context context){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }

    public void setToken(Context context, String token){
        (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit().putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token);
    }
}
