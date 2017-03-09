package com.bluewhaledt.saylove.imageloader;

import com.bluewhaledt.saylove.imageloader.base.ImageLoader;
import com.bluewhaledt.saylove.imageloader.glide.GlideImageLoader;

/**
 * @author hechunshan
 * @date 2016/11/25
 */
public class ImageLoaderFactory {

    public static ImageLoader getImageLoader() {
        return new GlideImageLoader();
    }
}
