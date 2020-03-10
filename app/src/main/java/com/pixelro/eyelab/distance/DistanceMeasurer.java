package com.pixelro.eyelab.distance;

import android.graphics.PointF;
import android.os.Build;
import android.util.Log;

/**
 * Created by rainb on 2017-10-03.
 * Calculate User distance from camera. Left/Right eye position is used
 */

public class DistanceMeasurer {

    //for re-calculate=ing position to draw on camera capture image correctly
    //private static final float EYE_RADIUS_PROPORTION = 0.45f;
    //private static final float IRIS_RADIUS_PROPORTION = EYE_RADIUS_PROPORTION / 2.0f;
    private String TAG = "DistanceMeasurer";

    private float mWidthScaleFactor = 1.0f;
    private float mHeightScaleFactor = 1.0f;
    public int mEyetoEyeDistance = 0;
    public int mEyetoUserDistance = 0;

    public void calculateDistance(PointF leftEye, PointF rightEye) {
        Log.d(TAG, "calculateDistance:called.");
        if(leftEye == null || rightEye == null) {
            Log.e(TAG, "eye position is null");
            return;
        }

        PointF leftPosition = new PointF(translateX(leftEye.x), translateY(leftEye.y));
        PointF rightPosition = new PointF(translateX(rightEye.x), translateY(rightEye.y));

        mEyetoEyeDistance = getEyeDistance(leftPosition, rightPosition);
        //LOG.d(TAG, "Eye to Eye distance : " + mEyetoEyeDistance);

        mEyetoUserDistance = getRealDistance(mEyetoEyeDistance);
        //LOG.d(TAG, "Eye to User distance : " + mEyetoUserDistance);

        //LOG.i(TAG, String.format("mEyetoEyeDistance=%d, mEyetoUserDistance=%d", mEyetoEyeDistance, mEyetoUserDistance));
        //float eyeRadius = EYE_RADIUS_PROPORTION * eyeToEyeDistance;
        //float irisRadius = IRIS_RADIUS_PROPORTION * eyeToEyeDistance;

        return;
    }

    /*
     * 눈 과 눈 사이의 거리를 구하는 공식<p>
     */
    private int getEyeDistance(PointF leftPosition, PointF rightPosition) {
        double dist1 = (rightPosition.x - leftPosition.x) * (rightPosition.x - leftPosition.x) + (rightPosition.y - leftPosition.y) * (rightPosition.y - leftPosition.y);
        int dist2  = (int)( Math.sqrt( dist1) );

        return dist2;
    }

    /*
     * 사용자와 카메라의 거리를 재는 함수<p>
     *     눈거리 = pixel 값 * 0.44 (mm) (Galaxy S7 기준)<p>
     *     사용자거리 = 1667 - 22.06 눈거리 + 0.1156 눈거리**2 - 0.000207 눈거리**3<p>
     */
    private int getRealDistance(int dist1) {
        double eyeDist1 = (double)dist1 * 0.44; //
        double realDist1 = 1667 - 22.06 * eyeDist1 + 0.1156 * eyeDist1* eyeDist1 - 0.000207 * eyeDist1 * eyeDist1 * eyeDist1;
        double realDist2 = 78.03 + 0.73 * realDist1; //rainbell 0105 2nd compensation value update
        //3rd regression equation (0106) C7 = 95.0 + 0.330 Total + 0.001672 Total**2 - 0.000002 Total**3
        double realDist3 = 95.0 + 0.330 * realDist1 + 0.001672 * realDist1 * realDist1 - 0.000002 * realDist1 * realDist1 * realDist1;  //rainbell 0106 3rd compensation value update
        return (int)realDist3;
    }

    private float scaleX(float horizontal) {
        if (mWidthScaleFactor==1.0f) {
            if (Build.MODEL.startsWith( "SM-G930")) {
                mWidthScaleFactor = 3.85f;
            }
            else if (Build.MODEL.startsWith( "SM-G920")) {
                mWidthScaleFactor = 5.775f;
            }
            else {

            }
        }
        return horizontal * mWidthScaleFactor;
    }

    public float scaleY(float vertical) {
        if (mHeightScaleFactor==1.0f)
            mWidthScaleFactor = 3.85f;

        return vertical * mHeightScaleFactor;
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    private float translateX(float x) {
        /*if (mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT) {
            return mOverlay.getWidth() - scaleX(x);
        } else {
            return scaleX(x);
        }*/
        return scaleX(x);
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    private float translateY(float y) {
        return scaleY(y);
    }
}
