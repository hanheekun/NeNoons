package com.pixelro.eyelab.distance;


import android.os.Bundle;
import android.util.Log;


public interface IEyeDistanceMeasureServiceCallback {
    public final static String TAG = IEyeDistanceMeasureServiceCallback.class.getSimpleName();
    public String mCallerName = "";
    public boolean mEnable_IBleCommunicationServiceCallback = false;

    public enum EVENT_EyeDistanceMeasureService {
        UNKNOWN,
        DISTANCE_MEASER_COMPLETE,
        SERVICE_BIND_COMPLETE,
    }

    public default void onEvent_IEyeDistanceMeasureServiceCallback(EVENT_EyeDistanceMeasureService event, Bundle bundle) {
        if(bundle != null) {
            Log.d(TAG, String.format("[default method] onEvent_IEyeDistanceMeasureServiceCallback:event=%s, bundle.size=%d", event, bundle.size()));
        } else {
            Log.d(TAG, String.format("[default method] onEvent_IEyeDistanceMeasureServiceCallback:called:event=%s, bundle=null", event));
        }
    }
}

