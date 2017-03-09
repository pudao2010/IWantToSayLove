package com.bluewhaledt.saylove.imageloader.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * Created by toney on 2016/10/26.
 */

public interface IBitmapTransformation {
    public Bitmap transform(Bitmap source, ImageView imageView);

    public Context getContext();
}
