package com.pixelro.eyelab.menu.home;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pixelro.eyelab.EYELAB;
import com.pixelro.eyelab.MainActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.menu.care.o2o.O2OActivity;
import com.pixelro.eyelab.menu.exercise.ExerciseViewModel;
import com.pixelro.eyelab.test.TestActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private ExerciseViewModel exerciseViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView TvAddress;

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

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

        // 웹뷰 시작
        mWebView = (WebView) root.findViewById(R.id.webView_home);

        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        //mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        //mWebView.loadUrl("http://webapp.pixelro.com"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        mWebView.loadUrl("https://nenoons.com/app-main"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });

//        mWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
//
//                if (mTest){
//
//
//
//                    Uri uri = Uri.parse(view.getUrl()); //url을 uri로 변경
//
//                    //if(uri.getPath().contains("/test/page")){  // 현재 uri의 path에 컨테인만 읽기
//
//                        Intent intent = new Intent(getActivity(), WebActivity.class); // 새창을 여는 액티비티나, 팝업일때 이용하면 용이합니다.
//                        intent.putExtra("url",view.getUrl());
//                        startActivity(intent);
//
//                    //}
//                }
//                else
//                {
//                    mTest = true;
//                }
//
//
//
//                super.doUpdateVisitedHistory(view, url, isReload);
//            }
//
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////
////                Uri uri = Uri.parse(view.getUrl()); //url을 uri로 변경
////
////                if(uri.getPath().contains("/test/page")){  // 현재 uri의 path에 컨테인만 읽기
////
////                    Intent intent = new Intent(getActivity(), WebActivity.class); // 새창을 여는 액티비티나, 팝업일때 이용하면 용이합니다.
////                    intent.putExtra("url",view.getUrl());
////                    startActivity(intent);
////
////                    return true;
////                }
////
////                return super.shouldOverrideUrlLoading(view, request);
////            }
//        });

//        mWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                try {
//
//                    // do whatever you want to do on a web link click
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//
//        });

        //mWebView.setWebViewClient(new MyWebViewClient());

        TvAge = (TextView)root.findViewById(R.id.textView_home_age);
        TvExNumber = (TextView)root.findViewById(R.id.textView_home_ex_number);
        TvScreenTime = (TextView)root.findViewById(R.id.textView_home_screen_time);

        if (savedInstanceState == null) {
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl("https://nenoons.com/app-main");
        } else {
            mWebView.restoreState(savedInstanceState);
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mWebView.saveState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

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
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(getActivity().USAGE_STATS_SERVICE);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);

        long totalMin;
        int min;
        int hour;

        if (stats == null || stats.isEmpty()) {
            // Usage access is not enabled
        }
        else {

            long tatalTimeMS = 0;

            for (UsageStats stat:stats) {
                tatalTimeMS += stat.getTotalTimeInForeground();
            }
            //Toast.makeText(getActivity(),"total time" + (tatalTimeMS/1000)/3600,Toast.LENGTH_SHORT).show();
            totalMin = (tatalTimeMS/1000)/60;
            min = (int)(totalMin % 60);
            hour = (int)(totalMin / 60);

            Toast.makeText(getActivity(),"Hour : " + hour + ", Min : " + min + " Total EX : " + curTotalEXNumber,Toast.LENGTH_SHORT).show();
        }

        sharedPreferences = getActivity().getSharedPreferences(EYELAB.APPDATA.NAME_TEST,MODE_PRIVATE);
        int distance = sharedPreferences.getInt(EYELAB.APPDATA.TEST.LAST_DISTANCE,20);




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
}
