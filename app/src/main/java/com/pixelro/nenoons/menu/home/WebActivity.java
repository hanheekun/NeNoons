package com.pixelro.nenoons.menu.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.R;

import java.net.URISyntaxException;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = WebActivity.class.getSimpleName();

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

    private String mUrl;
    private String mToken;
    private Dialog alertIsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        alertIsp = new AlertDialog.Builder(WebActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("알림")
                .setMessage("모바일 ISP 어플리케이션이 설치되어 있지 않습니다. \n설치를 눌러 진행 해 주십시요.\n취소를 누르면 결제가 취소 됩니다.")
                .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ISP 설치 페이지 URL
                        mWebView.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(WebActivity.this, "(-1)결제를 취소 하셨습니다.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).create();


        Intent intent = getIntent();
        mUrl = intent.getExtras().getString("url");
        mToken = intent.getExtras().getString("token");

        findViewById(R.id.button_arrow_back_background).setOnClickListener(this);

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webView);

//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                String key = "token";
//                String val = mToken;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                    view.evaluateJavascript("localStorage.setItem('" + key + "','" + val + "');", null);
//                } else {
//                    view.loadUrl("javascript:localStorage.setItem('" + key + "','" + val + "');");
//                }
//
//            }
//
//        });


        mWebView.setWebViewClient(new INIP2PWebView()); // 이니시스 결제창 뜨도록 수정
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
        mWebSettings.setMixedContentMode(mWebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // 쿠키 설정
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(mWebView, true);

        mWebView.loadUrl(mUrl); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        //mWebView.loadUrl("https://nenoons.com/app-main"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
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
        switch (view.getId()) {
            case R.id.button_arrow_back_background:
                finish();
                break;
        }
    }


    private class INIP2PWebView extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            String key = "token";
            String val = mToken;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                view.evaluateJavascript("localStorage.setItem('" + key + "','" + val + "');", null);
            } else {
                view.loadUrl("javascript:localStorage.setItem('" + key + "','" + val + "');");
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
                //삼성카드 안심클릭을 위해 추가
                if (url.startsWith("ispmobile://")) finish();
            } catch (ActivityNotFoundException e) {
                //url prefix가 ispmobile 일겨우만 alert를 띄움
                if (url.startsWith("ispmobile://")) {
                    view.loadData("<html><body></body></html>", "text/html", "euc-kr");
                    alertIsp.show();
                    return true;
                }
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if (!url.startsWith("http://") && !url.startsWith("https://")
                    && !url.startsWith("javascript:")) {
                Intent intent;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Log.d("<INICIS_TEST>", "intent getDataString : " + intent.getDataString());
                } catch (URISyntaxException ex) {
                    Log.e("<INICIS_TEST>", "URI syntax error : " + url + ":" + ex.getMessage());
                    return false;
                }
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    /* ISP어플이 현재 폰에 없다면 아래 처리에서
                     * 알림을 통해 처리하도록 하였습니다.
                     * 삼성카드 및 기타 안심클릭에서는
                     * 카드사 웹페이지에서 알아서 처리하기때문에
                     * WEBVIEW에서는 별다른 처리를 하지 않아도 처리됩니다.
                     */
                    if (url.startsWith("ispmobile://")) {
                        //onCreateDialog에서 정의한 ISP 어플리케이션 알럿을 띄워줍니다.
                        //(ISP 어플리케이션이 없을경우)

//                        return false;
                        view.loadData("<html><body></body></html>", "text/html", "euc-kr");
                        alertIsp.show();
                        return true;
                    } else if (url.startsWith("intent")) {
                        //일부카드사의 경우 ,intent:// 형식의 intent 스키마를 내려주지 않음
                        //ex) 현대카드 intent:hdcardappcardansimclick://
                        try {
                            Intent tempIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            String strParams = tempIntent.getDataString();
                            Intent intent1 = new Intent(Intent.ACTION_VIEW);
                            intent1.setData(Uri.parse(strParams));
                            startActivity(intent1);
                            return true;
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Intent intent2 = null;
                            try {
                                intent2 = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent.setData(Uri.parse("market://details?id=" + intent2.getPackage()));
                                startActivity(marketIntent);
                            } catch (Exception e2) {
                                e1.printStackTrace();
                            }
                            return true;
                        }
                    }
                }
            } else {
//                view.loadUrl(url);
                Uri uri = Uri.parse(url);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    view.loadData("<html><body></body></html>", "text/html", "euc-kr");
                    alertIsp.show();
                    return true;
                }
                return false;
            }
            return true;
        }
    }
}
