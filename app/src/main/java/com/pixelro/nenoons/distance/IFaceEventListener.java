package com.pixelro.nenoons.distance;

/**
 * 페이스 디텍터에서 사용되는 리스너
 *
 */

public abstract class IFaceEventListener {
    public abstract void intEvent(int eyeToEyeDistance, int eyeToCameraDistance);
    public abstract void floatEvent(float LeftEye, float RightEye);

}
