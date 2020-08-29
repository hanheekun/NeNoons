package com.pixelro.nenoons.menu.home;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pixelro.nenoons.GpsTracker;
import com.pixelro.nenoons.R;
import com.pixelro.nenoons.SharedPreferencesManager;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressWebViewActivity extends AppCompatActivity {

    private WebView browser;
    private GpsTracker gpsTracker;
    private SharedPreferencesManager mSm;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {

            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_web_view);

        browser = (WebView) findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        //browser.loadUrl("file:///android_asset/daum.html");
        //browser.loadUrl("http://www.daddyface.com/public/daum.html");
        //browser.loadUrl("http://cdn.rawgit.com/jolly73-df/DaumPostcodeExample/master/DaumPostcodeExample/app/src/main/assets/daum.html");
        browser.loadUrl("http://www.inspond.com/daum.html");
        // 경고! 위 주소대로 서비스에 사용하시면 파일이 삭제됩니다.
        // 꼭 자신의 웹 서버에 해당 파일을 복사해서 주소를 변경해 사용하세요.

        Button btnGPSAddress = (Button)findViewById(R.id.button_home_gps_address);
        btnGPSAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gpsTracker = new GpsTracker(AddressWebViewActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);

                //Toast.makeText(AddressWebViewActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude + ", " + address, Toast.LENGTH_LONG).show();

                // 주소 저장
                mSm.setAddress(makeShortAddress(address));

                //  위도 경도 엔키노 전달
                String token = mSm.getToken();
                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("token", token);    //PARAM
                param.put("gpsAddress", address);    //PARAM
                param.put("gpsLatitude", String.valueOf(latitude));    //PARAM
                param.put("gpsLongitude", String.valueOf(longitude));    //PARAM
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



                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                });
                // API 주소와 위 핸들러 전달 후 실행.
                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_gps", handler).execute(param);
//                            new HttpTask("http://192.168.1.162:4002/android/update_user_gps", handler).execute(param);


            }
        });

        mSm = new SharedPreferencesManager(this);

    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }

    public String makeShortAddress(String address){

        address = address.substring(5);

        if (address.length() > 18){
            return address.substring(0,18) + "...";
        }
        else
        {
            return address;
        }
    }
}
