package com.bluewhaledt.saylove.imageloader.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.File;

/**
 * @author hechunshan
 * @date 2016/11/25
 */
public interface ImageLoader {

    ImageLoader with(Context context);

    ImageLoader with(Activity activity);

    ImageLoader with(Fragment fragment);

    ImageLoader load(String url);

    ImageLoader load(int resId);

    ImageLoader thumbnail(float sizeMultiplier);

    ImageLoader centerCrop();

    ImageLoader fitCenter();

    ImageLoader crossFade();

    ImageLoader crossFade(int duration);

    ImageLoader crossFade(Animation animation, int duration);

    ImageLoader crossFade(int animationId, int duration);

    ImageLoader dontAnimate();

    ImageLoader animate(int animationId);

    ImageLoader animate(Animation animation);

    ImageLoader placeholder(int resourceId);

    ImageLoader placeholder(Drawable drawable);

    ImageLoader fallback(Drawable drawable);

    ImageLoader fallback(int resourceId);

    ImageLoader error(int resourceId);

    ImageLoader error(Drawable drawable);

    ImageLoader listener(ImageLoaderListener listener);

    ImageLoader diskCacheType(DiskCacheType type);

    ImageLoader memoryCache(boolean cache);

    ImageLoader override(int width, int height);

    ImageLoader square();

    ImageLoader circle();

    ImageLoader round(int radius);

    ImageLoader blur(int radius);

    /**
     * 李仕明加的东西   为了加载一张模糊的图，同时保证这个图的一个像素的字节为2，而不是4
     * start
     * */
    ImageLoader blurMinBitmap(int radius);
    /*end*/

    ImageLoader transform(IBitmapTransformation transformation);

    ImageLoader dontTransform();

    ImageLoader into(ImageView view);

    ImageLoader preload();

     void clearMemoryCache(Context context);

     void clearDiskCache(Context context);

    File getPhotoCacheFile(Context context);

}
