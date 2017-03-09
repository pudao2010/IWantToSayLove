package com.bluewhaledt.saylove.video_record.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class CameraHelper {

    //
    public static Camera.Size getOptimalPreviewSize(Camera.Parameters parameters,
                                                    Camera.Size pictureSize, int viewHeight) {

        if (parameters == null || pictureSize == null) {
            return null;
        }

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        Collections.sort(sizes, new CameraPreviewSizeComparator());


        Camera.Size optimalSize = null;
        float targetRatio = pictureSize.width *1.0f / pictureSize.height;
        float minDiff = Integer.MAX_VALUE;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            float ratio = size.width *1.0f / size.height;
            float currentDiff = Math.abs(targetRatio - ratio);
            if (currentDiff < minDiff) {
                minDiff = currentDiff;
                optimalSize = size;
            }
        }

        //Log.e("CameraHelper",
        //        "optimalSize : width=" + optimalSize.width + " height=" + optimalSize.height);
        return optimalSize;
    }

    //  这里只使用于旋转了90度
    public static Rect calculateTapArea(View v, float oldx, float oldy, float coefficient) {

        float x = oldy;
        float y = v.getHeight() - oldx;

        float focusAreaSize = 300;

        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / v.getWidth() * 2000 - 1000);
        int centerY = (int) (y / v.getHeight() * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }
}
