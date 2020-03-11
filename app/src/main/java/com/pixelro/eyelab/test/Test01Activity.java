package com.pixelro.eyelab.test;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.pixelro.eyelab.BaseActivity;
import com.pixelro.eyelab.R;
import com.pixelro.eyelab.distance.EyeDistanceMeasureService;
import com.pixelro.eyelab.distance.IEyeDistanceMeasureServiceCallback;

public class Test01Activity  extends BaseActivity  implements IEyeDistanceMeasureServiceCallback {

    private final static String TAG = Test01Activity.class.getSimpleName();
    private TextView mTvData;

    private static final int REQUEST_CAMERA = 1;
    private static final int SEND_THREAD_DISTANCE_MEASURE_COMPLETE = 0;

    private ServiceConnection mConnection = null;
    private EyeDistanceMeasureService mEyeDistanceMeasureService = null;
    private boolean mBindStatus = false;
    private SendMassgeHandler mMainHandler = null;
    private Test01Activity mThis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG, "Camera Permission is already allowed");
            initService();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        //mTvData = (TextView) findViewById(R.id.textView_test01_distance);

        mThis = this;

        showDialog();

    }

    @Override
    protected void onStart() {  // added by Alex, 2018.08.20
        super.onStart();
        bindService();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1f;
        getWindow().setAttributes(params);

    }

    @Override
    public void onDestroy() {   // added by Alex, 2018.08.20
        // 서비스가 종료될 때 실행
        super.onDestroy();
        unbindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void initService() {    // added by Alex, 2018.08.20
        mMainHandler = new SendMassgeHandler();
        if (isServiceRunningCheck() == false) {
            Log.d(TAG, "mBaseBleCommServiceForActivity.isServiceRunningCheck() is false");
            initEyeDistanceMeasureServiceConnect();
            startBleCommunicationService();
            bindService();
        } else {
            Log.d(TAG, "BleCommunicationService is already running...");
        }
    }

    class SendMassgeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if (msg == null) {
                Log.e(TAG, "msg is null");
                return;
            }

            switch (msg.what) {
                case SEND_THREAD_DISTANCE_MEASURE_COMPLETE:
                    Log.d(TAG, "msg.what=SEND_THREAD_DISTANCE_MEASURE_COMPLETE");
                    Bundle bundle = msg.getData();
                    final int distance = bundle.getInt("distance");
                    int radius = (distance / 10) - 10;  //20cm ~ 40 cm를 유효한 범위로 잡기위해 radius값 조절
                    Log.i(TAG, "SEND_THREAD_DISTANCE_MEASURE_COMPLETE:radius = " + distance);
                    //setBackGroundBlur(radius);
                    //Intent intent = new Intent(getApplicationContext(),LogInActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ///////mTvData.setText(distance + "  C2M");

                        }
                    });


                    Log.i(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! = " + distance);
                    break;

                default:
                    break;
            }
        }
    }

    public boolean isServiceRunningCheck() {
        Log.d(TAG, "called");
        ActivityManager manager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("BleCommunicationService".equals(service.service.getClassName())) {
                Log.d(TAG, "BleCommunicationService is already running");
                return true;
            }
        }

        Log.d(TAG, "BleCommunicationService has been not run");
        return false;
    }

    public void initEyeDistanceMeasureServiceConnect() {
        Log.d(TAG, "called");
        mConnection = new ServiceConnection() {
            // Called when the connection with the service is established
            public void onServiceConnected(ComponentName className, IBinder service) {
                Log.d(TAG, "onServiceConnected:called");
                EyeDistanceMeasureService.EyeDistanceMeasureServiceBinder binder = (EyeDistanceMeasureService.EyeDistanceMeasureServiceBinder) service;
                mEyeDistanceMeasureService = binder.getService(); //서비스 받아옴
                if (mEyeDistanceMeasureService != null) { // & mStartFlag == true) {
                    mBindStatus = true;
                    mEyeDistanceMeasureService.registerCallback_IEyeDistanceMeasureServiceCallback(TAG, mThis);
                } else {
                    Log.e(TAG, "onServiceConnected:mBleCommService is null");
                }
            }

            // Called when the connection with the service disconnects unexpectedly
            public void onServiceDisconnected(ComponentName className) {
                Log.d(TAG, "onServiceDisconnected:called");
                mBindStatus = false;
                mEyeDistanceMeasureService = null;
            }
        };
    }

    public void startBleCommunicationService() {
        // 이동할 컴포넌트
        if (isServiceRunningCheck() != true) {
            Intent intent = new Intent(
                    //getApplicationContext(),//현재제어권자
                    this,
                    EyeDistanceMeasureService.class);
            if (intent != null) {
                Log.d(TAG, "calling startService(...)");
                startService(intent); // 서비스 시작
            } else {
                Log.e(TAG, "intent is null");
            }
        } else {
            Log.d(TAG, "BleCommunicationService is already run");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("camera permission authorized");
                        } else {
                            //resultText.setText("camera permission denied");
                        }
                    }
                }
                break;

        }
    }

    public void bindService() {
        Log.d(TAG, "called:mBindStatus=" + mBindStatus);
        if (mBindStatus == false) {
            Intent intent = new Intent(
                    //getApplicationContext(),//현재제어권자
                    this,
                    EyeDistanceMeasureService.class); // 이동할 컴포넌트

            if (intent != null && mConnection != null) {
                Log.d(TAG, "calling bindService(...)");
                bindService(intent, mConnection, getApplicationContext().BIND_AUTO_CREATE);
            } else {
                Log.e(TAG, "intent might be null or mConnection might be null");
            }
        } else {
            Log.d(TAG, "mBindStatus is not false");
        }
    }

    private void unbindService() {
        Log.d(TAG, "called:mBindStatus=" + mBindStatus);

        if (mConnection != null) {
            Log.d(TAG, "called:1");
            if (mBindStatus == true) {
                Log.d(TAG, "called:2");
                unbindService(mConnection);
                Log.d(TAG, "called:3");
            } else {
                Log.d(TAG, "mBindStatus is not true");
            }
        } else {
            Log.e(TAG, "mConnection is null");
        }

        Log.d(TAG, "called:4");
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);
                ////////mTvData.setText(distance + "cm");
                Log.d(TAG, "????????????????????????????????");
            }


        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void showDialog() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(this);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_test);

        dlg.setCancelable(false);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button okButton = (Button) dlg.findViewById(R.id.button_test_ok);
        final Button cancelButton = (Button) dlg.findViewById(R.id.button_test_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Test01Activity.this, "확인 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Test01Activity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
