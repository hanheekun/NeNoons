/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixelro.nenoons.distance;

import android.graphics.PointF;
import android.util.Log;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks the eye positions and state over time, managing an underlying graphic which renders googly
 * eyes over the source video.<p>
 *
 * To improve eye tracking performance, it also helps to keep track of the previous landmark
 * proportions relative to the detected face and to interpolate landmark positions for future
 * updates if the landmarks are missing.  This helps to compensate for intermediate frames where the
 * face was detected but one or both of the eyes were not detected.  Missing landmarks can happen
 * during quick movements due to camera image blurring.
 */
public class GooglyFaceTracker extends Tracker<Face> {
    private String TAG = "GooglyFaceTracker";

    private static final float EYE_CLOSED_THRESHOLD = 0.4f;
    private final IFaceEventListener faceEventListener;


    //private GraphicOverlay mOverlay;
    //private com.eyegalaxy.eyegalaxydemo.GooglyEyesGraphic mEyesGraphic;

    // Record the previously seen proportions of the landmark locations relative to the bounding box
    // of the face.  These proportions can be used to approximate where the landmarks are within the
    // face bounding box if the eye landmark is missing in a future update.
    private Map<Integer, PointF> mPreviousProportions = new HashMap<>();

    // Similarly, keep track of the previous eye open state so that it can be reused for
    // intermediate frames which lack eye landmarks and corresponding eye state.
    private boolean mPreviousIsLeftOpen = true;
    private boolean mPreviousIsRightOpen = true;

    DistanceMeasurer mDistanceMeasurer = null;
    EyeOpenChecker mEyeOpenChecker = null;

    //==============================================================================================
    // Methods
    //==============================================================================================

    public GooglyFaceTracker(IFaceEventListener faceEventListener) {
        //mOverlay = overlay;
        this.faceEventListener = faceEventListener;

        if(mDistanceMeasurer == null)
            mDistanceMeasurer = new DistanceMeasurer();
        if(mEyeOpenChecker == null)
            mEyeOpenChecker = new EyeOpenChecker();
    }

    /**
     * Resets the underlying googly eyes graphic and associated physics state.
     */
    @Override
    public void onNewItem(int id, Face face) {
        //mEyesGraphic = new com.eyegalaxy.eyegalaxydemo.GooglyEyesGraphic(mOverlay);
    }

    /**
     *
     * IFaceEventListener 를 등록하면, 얼굴 디텍션을 할때마다 업데이트 되는 부분<p>
     * 오른쪽,왼쪽 눈이 감겼는지 여부도 알려주나, 실제 큰 도움은 없음<p>
     * 왼쪽, 오른쪽의 위치 값을 IFaceEventListener 에 알려줌.<p>
     *
     * @param detectionResults
     * @param face
     */
    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
        Log.d(TAG, "onUpdate:called.");
        //mOverlay.add(mEyesGraphic);
        //DLog.d("Face detected");
        updatePreviousProportions(face);

        PointF leftPosition = getLandmarkPosition(face, Landmark.LEFT_EYE);
        PointF rightPosition = getLandmarkPosition(face, Landmark.RIGHT_EYE);

        float leftOpenScore = face.getIsLeftEyeOpenProbability();
        boolean isLeftOpen;
        if (leftOpenScore == Face.UNCOMPUTED_PROBABILITY) {
            isLeftOpen = mPreviousIsLeftOpen;
        } else {
            isLeftOpen = (leftOpenScore > EYE_CLOSED_THRESHOLD);
            mPreviousIsLeftOpen = isLeftOpen;
        }

        float rightOpenScore = face.getIsRightEyeOpenProbability();
        boolean isRightOpen;
        if (rightOpenScore == Face.UNCOMPUTED_PROBABILITY) {
            isRightOpen = mPreviousIsRightOpen;
        } else {
            isRightOpen = (rightOpenScore > EYE_CLOSED_THRESHOLD);
            mPreviousIsRightOpen = isRightOpen;
        }

        //DLog.d("Left Eye Pos = " + leftPosition + " / Right Eye Pos = " + rightPosition);

        //calculate distance
        mDistanceMeasurer.calculateDistance(leftPosition, rightPosition);
        //Opencheck
        mEyeOpenChecker.EyeOpenUpdate(leftOpenScore,rightOpenScore);
        //update data to listener
        faceEventListener.intEvent(mDistanceMeasurer.mEyetoEyeDistance, mDistanceMeasurer.mEyetoUserDistance);
        faceEventListener.floatEvent(mEyeOpenChecker.mLeftEyeOpenProb, mEyeOpenChecker.mRightEyeOpenProb);

        //mEyesGraphic.updateEyes(leftPosition, isLeftOpen, rightPosition, isRightOpen, faceEventListener);
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily (e.g., if the face was momentarily blocked from
     * view).
     */
    @Override
    public void onMissing(FaceDetector.Detections<Face> detectionResults) {
        /*try {
            mOverlay.remove(mEyesGraphic);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
    }

    /**
     * Called when the face is assumed to be gone for good. Remove the googly eyes graphic from
     * the overlay.
     */
    @Override
    public void onDone() {
        /*try {
            mOverlay.remove(mEyesGraphic);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
    }

    //==============================================================================================
    // Private
    //==============================================================================================

    private void updatePreviousProportions(Face face) {
        for (Landmark landmark : face.getLandmarks()) {
            PointF position = landmark.getPosition();
            float xProp = (position.x - face.getPosition().x) / face.getWidth();
            float yProp = (position.y - face.getPosition().y) / face.getHeight();
            mPreviousProportions.put(landmark.getType(), new PointF(xProp, yProp));
        }
    }

    /**
     * Finds a specific landmark position, or approximates the position based on past observations
     * if it is not present.
     */
    private PointF getLandmarkPosition(Face face, int landmarkId) {
        for (Landmark landmark : face.getLandmarks()) {
            if (landmark.getType() == landmarkId) {
                return landmark.getPosition();
            }
        }

        PointF prop = mPreviousProportions.get(landmarkId);
        if (prop == null) {
            return null;
        }

        float x = face.getPosition().x + (prop.x * face.getWidth());
        float y = face.getPosition().y + (prop.y * face.getHeight());
        return new PointF(x, y);
    }
}