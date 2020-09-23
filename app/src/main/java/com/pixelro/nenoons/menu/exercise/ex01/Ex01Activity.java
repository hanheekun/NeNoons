package com.pixelro.nenoons.menu.exercise.ex01;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixelro.nenoons.R;
import com.pixelro.nenoons.distance.EyeDistanceMeasureService;
import com.pixelro.nenoons.distance.IEyeDistanceMeasureServiceCallback;
import com.pixelro.nenoons.menu.exercise.ExCancelDialog;

public class Ex01Activity extends AppCompatActivity  implements IEyeDistanceMeasureServiceCallback {

    private final static String TAG = Ex01Activity.class.getSimpleName();

    public final static int EX_LEVEL_L = 1;
    public final static int EX_LEVEL_M = 2;
    public final static int EX_LEVEL_H = 3;

    public int mCurLevel = EX_LEVEL_L;

    private static final int REQUEST_CAMERA = 1;
    private static final int SEND_THREAD_DISTANCE_MEASURE_COMPLETE = 0;

    private ServiceConnection mConnection = null;
    private EyeDistanceMeasureService mEyeDistanceMeasureService = null;
    private boolean mBindStatus = false;
    private SendMassgeHandler mMainHandler = null;
    private Ex01Activity mThis = null;

    public int mCurrentDistance = 0;
    public float mLeftOpenValue =1.0f;
    public float mRightOpenValue =1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_01);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG, "Camera Permission is already allowed");
            initService();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        mThis = this;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_ex_01, new Ex01AFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        ExCancelDialog dlg = new ExCancelDialog(Ex01Activity.this);
        dlg.setOnResultEventListener(new ExCancelDialog.OnResultEventListener() {
            @Override
            public void ResultEvent(boolean result) {
                if (result){
                    finish();
                }
                else {
                }
            }
        });
        dlg.showDialog();
    }

    @Override
    protected void onStart() {  // added by Alex, 2018.08.20
        super.onStart();
        bindService();

//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.screenBrightness = 1f;
//        getWindow().setAttributes(params);

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
        mMainHandler = new Ex01Activity.SendMassgeHandler();
        if (isServiceRunningCheck() == false) {
            Log.d(TAG, "mBaseBleCommServiceForActivity.isServiceRunningCheck() is false");
            initEyeDistanceMeasureServiceConnect();
            startBleCommunicationService();
            bindService();
        } else {
            Log.d(TAG, "BleCommunicationService is already running...");
        }
    }
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EyeDistanceMeasureService.ACTION_DATA_AVAILABLE.equals(action)) {
                float Left = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT,0);
                float Right = intent.getFloatExtra(EyeDistanceMeasureService.EXTRA_DATA_FLOAT,0);
                int distance = intent.getIntExtra(EyeDistanceMeasureService.EXTRA_DATA, 0);
                mCurrentDistance = distance;
                mRightOpenValue = Right;
                mLeftOpenValue = Left;
                Log.d(TAG, "????????????????????????????????");
            }


        }
    };

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
                    final float LeftEyeOpenProb = bundle.getFloat("LeftEyeValue");
                    final float RightEyeOpenProb = bundle.getFloat("RightEyeValue");
                    int radius = (distance / 10) - 10;  //20cm ~ 40 cm를 유효한 범위로 잡기위해 radius값 조절
                    Log.i(TAG, "SEND_THREAD_DISTANCE_MEASURE_COMPLETE:radius = " + distance);
                    Log.i(TAG, "SEND_THREAD_DISTANCE_MEASURE_COMPLETE: EyeOpenValue =:("+LeftEyeOpenProb+","+RightEyeOpenProb+")");
                    //setBackGroundBlur(radius);
                    //Intent intent = new Intent(getApplicationContext(),LogInActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mCurrentDistance = distance;
                            mLeftOpenValue = LeftEyeOpenProb;
                            mRightOpenValue = RightEyeOpenProb;

                        }
                    });


                    Log.i(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! = " + distance);
                    break;

                default:
                    break;
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EyeDistanceMeasureService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
