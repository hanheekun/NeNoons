package com.pixelro.nenoons.distance;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.vision.CameraSource;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.pixelro.nenoons.R;

import java.io.IOException;

//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;

//import com.pixelro.eyecare.adapter.IFaceEventListener;
//import com.pixelro.eyecare.faceDetector.GooglyFaceTracker;

// have to add phone value, sensor value should be considered (rainbell todo)
public class EyeDistanceMeasureService extends Service implements SensorEventListener {
    private String TAG = "EyeDistanceMeasureService";

    public final static String ACTION_DATA_AVAILABLE =
            "com.pixelro.eyecare.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.pixelro.eyecare.EXTRA_DATA";
    public final static String EXTRA_DATA_FLOAT =
            "com.pixelro.eyecare.EXTRA_DATA_FLOAT";

    private CameraSource mCameraSource = null;
    private boolean mCameraFacing = true;   //have to check camera is facing or not. if not facing, have to notify to user
    private IFaceEventListener faceEventListener;
    Handler mHandler = new Handler(Looper.getMainLooper());

    //Orientation Sensor was depricated from API 20, change it as using RotationMatrix
    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private final IBinder mBinder = new EyeDistanceMeasureServiceBinder();
    private IEyeDistanceMeasureServiceCallback mIEyeDistanceMeasureServiceCallback = null;
    private String mCallerName = "";

    public EyeDistanceMeasureService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate called");
        super.onCreate();

        //int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int rc = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
            Log.d(TAG, "Connect Camera Source to Google Face Detector");
            startCameraSource();
        }
        else {
            Log.e(TAG, "Camera Permission is not allowed");
        }

        // sensor initialize
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand called");

        //register sensor listener
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        if(flags == START_STICKY) {
            Log.d(TAG, "EyeDistanceMeasureService::onStartCommand::flag is START_STICKY");
        } else if(flags == START_NOT_STICKY) {
            Log.d(TAG, "EyeDistanceMeasureService::onStartCommand::flag is START_NOT_STICKY");
        } else if(flags == START_REDELIVER_INTENT) {
            Log.d(TAG, "EyeDistanceMeasureService::onStartCommand::flag is START_REDELIVER_INTENT");
        } else {
            Log.d(TAG, "EyeDistanceMeasureService::onStartCommand::flag is unknown" + String.format("0x%x", flags));
        }
        return START_REDELIVER_INTENT;  // modified by alex, 2018.04.26 - for using the existing intent when this service is forcedly stopped.
        //return START_STICKY;    // added by alex, 2018.02.07
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestory called");

        mSensorManager.unregisterListener(this);
        stopCameraSource();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "Service onBind called");
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    //for sending data to receiver
    private void sendMessage(int value) {
        Intent intent = new Intent("Distance Measure");
        intent.putExtra("distance", value);
        //Log.d("SendMessage : " + value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*
     * 구글의 페이스 디텍터, 생성부
     * 카메라가 너무 멀거나 가까워 눈간 거리가 불일치 하는 경우, 아래 0.35f 를 조정하면 됨
     */

    public int distanceToCM(int DIstance){
        //return (DIstance / 10) + 12;      // modified by Alex, 2019.07.25 - for tuning

        //y = (-4.259414935)*10^(-2) x + 67.85073769;
        //double eyeToCameraDistance = (-4.259414935) * Math.pow(10, -2) * DIstance + 67.85073769;  // 1st
        //double eyeToCameraDistance = (-4.270415834) * Math.pow(10, -2) * DIstance + 67.55303358;    // 2nd
        //double eyeToCameraDistance = (3*Math.pow(10, -5))*Math.pow(DIstance, 2) - (0.0866 * DIstance) + 80.728;

        //3rd : y = 4E-05x^2 - 0.1115x + 97.301
        //double eyeToCameraDistance = (4*Math.pow(10, -5))*Math.pow(DIstance, 2) - (0.1115 * DIstance) + 97.301;

        // 4th
        double eyeToCameraDistance = 20957* Math.pow(DIstance,-0.97);

        int finalDistance = (int) Math.round(eyeToCameraDistance);
        String outInfo = String.format("!!!!!!!!!!!!!!!eyeToEyeDistance:%f, finalDistance=%d", eyeToCameraDistance, finalDistance);

        broadcastUpdate(ACTION_DATA_AVAILABLE, finalDistance);


        Log.i(TAG, outInfo);
        return finalDistance;
    }

    private void broadcastUpdate(final String action, final int data) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, data);
        sendBroadcast(intent);
    }

    private void broadcastUpdate2(final String action, final float data) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA_FLOAT, data);
        sendBroadcast(intent);
    }

    @NonNull
    private FaceDetector createFaceDetector(Context context) {
        // have to check google service is not connected (rainbell todo)
        FaceDetector detector = new FaceDetector.Builder(context)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)     //ALL_CLASSIFICATIONS detect 'eyes open' and 'smiling'
                .setTrackingEnabled(true)
                .setMode(FaceDetector.FAST_MODE)
                .setProminentFaceOnly(mCameraFacing)
                .setMinFaceSize(mCameraFacing ? 0.35f : 0.15f)
                .build();

        Detector.Processor<Face> processor;
        /**
         * 눈 추적 이벤트가 실행되는 경우, 데모 버전이므로 작게 거리 표시를 해줌
         */
        faceEventListener = new IFaceEventListener() {
            @Override
            public void intEvent(final int eyeToEyeDistance, final int eyeToCameraDistance) {
                if(mIEyeDistanceMeasureServiceCallback != null) {
                    Bundle bundle = new Bundle();
                    int adjustedDistance = distanceToCM(eyeToEyeDistance);
                    bundle.putInt("distance", adjustedDistance);
                    Log.i(TAG, "sta2002:eyeToEyeDistance : " + eyeToEyeDistance);
                    mIEyeDistanceMeasureServiceCallback.onEvent_IEyeDistanceMeasureServiceCallback(IEyeDistanceMeasureServiceCallback.EVENT_EyeDistanceMeasureService.DISTANCE_MEASER_COMPLETE, bundle);
                }
                /*
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "EyeToEye : " + eyeToEyeDistance + ", EyeToCamera : " + eyeToCameraDistance);
                        //send distance to display
                        sendMessage(eyeToCameraDistance);
                    }
                });*/
            }
            @Override
            public void floatEvent(final float LeftEyeOpenProb, final float RightEyeOpenProb){
                if(mIEyeDistanceMeasureServiceCallback !=null){
                    Bundle bundle = new Bundle();
                    float Left = LeftEyeOpenProb;
                    float Right = RightEyeOpenProb;
                    bundle.putFloat("LeftEyeValue",Left);
                    bundle.putFloat("RightEyeValue",Right);
                    broadcastUpdate2(ACTION_DATA_AVAILABLE,Left);
                    broadcastUpdate2(ACTION_DATA_AVAILABLE,Right);
                    Log.i(TAG, "sta2002: EyeOpenValue: Left=" +Left +"Right="+Right );
                    mIEyeDistanceMeasureServiceCallback.onEvent_IEyeDistanceMeasureServiceCallback(IEyeDistanceMeasureServiceCallback.EVENT_EyeDistanceMeasureService.EYE_OPEN_CHCEK_COMPLETE, bundle);
                }
            }
        };

        /**
         * 전면 후면 카메라에 따라 프로세서를 설정
         * 여기서는 모두 얼굴 카메라
         */
        if (mCameraFacing) {
            Tracker<Face> tracker = new GooglyFaceTracker(faceEventListener);
            processor = new LargestFaceFocusingProcessor.Builder(detector, tracker).build();
        }
        else {
            MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
                @Override
                public Tracker<Face> create(Face face) {
                    return new GooglyFaceTracker(faceEventListener);
                }
            };
            processor = new MultiProcessor.Builder<>(factory).build();
        }

        /**
         * 생성된 프로세서를 디텍터에 등록
         */
        detector.setProcessor(processor);

        if (!detector.isOperational()) {
            Log.i(TAG, getString(R.string.face_detector_dependency_error));

            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                   Log.i(TAG, getString(R.string.low_storage_error));
            }
        }
        return detector;
    }

    //for camera access
    private void createCameraSource() {
        Context context = this.getApplicationContext();
        FaceDetector detector = createFaceDetector(context);

        int facing = CameraSource.CAMERA_FACING_FRONT;
        if (!mCameraFacing) {
            Log.i(TAG, "Camera is facing back");
            //Toast PopUp to user. Camera is facing back. After changing camera position, start again.
            facing = CameraSource.CAMERA_FACING_BACK;
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setFacing(facing)
                .setRequestedPreviewSize(1024, 768)
                .setRequestedFps(5.0f)  //have to check frame rate and performance
                .setAutoFocusEnabled(true)
                .build();
    }

    /**
     * 카메라 소스 열기
     */
    private void startCameraSource() {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());


        if (mCameraSource != null) {
            try {
                //mPreview.start(mCameraSource, mGraphicOverlay);
                mCameraSource.start();
                Log.d(TAG, "Camera Source started");
            } catch (IOException e) {
                // will be toasted to user (rainbell todo)
                //Log.e("Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void stopCameraSource() {
        if(mCameraSource != null) {
            try {
                mCameraSource.release();
                mCameraSource = null;
            } catch (NullPointerException e) {
                Log.e(TAG, "Error to release camera source");
            }
        }
    }
    /*
     * 센서값이 변하는 경우
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.length);
        }
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.length);
        }
        updateOrientationAngles();
        //Log.d("Sensor Orientation Changed X = " + mOrientationAngles[0]);
        //Log.d("Sensor Orientation Changed Y = " + mOrientationAngles[1]);
        //Log.d("Sensor Orientation Changed Z = " + mOrientationAngles[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        // "mRotationMatrix" now has up-to-date information.
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        // "mOrientationAngles" now has up-to-date information.
    }

    public void registerCallback_IEyeDistanceMeasureServiceCallback(
            String callerName,
            IEyeDistanceMeasureServiceCallback callback_IEyeDistanceMeasureServiceCallback) {
        //액티비티에서 콜백 함수를 등록하기 위함.
        Log.d(TAG, "called:" + String.format("callerName=%s", callerName));

        mCallerName = callerName;
        if (callback_IEyeDistanceMeasureServiceCallback != null) {
            mIEyeDistanceMeasureServiceCallback = callback_IEyeDistanceMeasureServiceCallback;
        } else {
            Log.d(TAG, "there is no instance of callback_IEyeDistanceMeasureServiceCallback.");
        }
    }

    //서비스 바인더 내부 클래스 선언
    public class EyeDistanceMeasureServiceBinder extends Binder {
        public EyeDistanceMeasureService getService() {
            return EyeDistanceMeasureService.this; //현재 서비스를 반환.
        }
    }

}
