package com.pixelro.nenoons.menu.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.R;

import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity  implements View.OnClickListener {

    private final static String TAG = WebActivity.class.getSimpleName();

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        // MainActivity 의 putExtra로 지정했던 key 값
        String key = intent.getExtras().getString("url");

        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webView);

//        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String key = "token";
                String val = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiY2tjY3pvY296MDAwMDg1OWh0Y3ZuN2lweCIsImVtYWlsIjoiY3NAZW5raW5vLmNvbSIsIm5hbWUiOiLrrLjtmITsoJUiLCJ0ZWwiOiIwMTAtMjczMy04MDk2In0sImlhdCI6MTU5NDY5OTg2MCwiZXhwIjoxNTk1MzA0NjYwfQ.mvJqJuhMmloQkY_ite5qW6344Cosfd58LcCJ-Qp-zC0";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript("localStorage.setItem('"+ key +"','"+ val +"');", null);
                } else {
                    view.loadUrl("javascript:localStorage.setItem('"+ key +"','"+ val +"');");
                }

            }

        });
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

        mWebView.loadUrl(key); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        //mWebView.loadUrl("https://nenoons.com/app-main"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

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
                        onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_arrow_back_background:
                finish();
                break;
        }
    }
}
