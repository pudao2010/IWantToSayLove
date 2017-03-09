package com.bluewhaledt.saylove.imageloader.base;

import android.graphics.drawable.Drawable;

/**
 * Created by toney on 2016/10/25.
 */
public interface ImageLoaderListener {

    void onLoadingFailed(Exception e);

    void onLoadingComplete(Drawable resource);
}
