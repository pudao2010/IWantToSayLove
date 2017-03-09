package com.bluewhaledt.saylove.imageloader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bluewhaledt.saylove.imageloader.base.DiskCacheType;
import com.bluewhaledt.saylove.imageloader.base.IBitmapTransformation;
import com.bluewhaledt.saylove.imageloader.base.ImageLoader;
import com.bluewhaledt.saylove.imageloader.base.ImageLoaderListener;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author hechunshan
 * @date 2016/11/25
 */
public class GlideImageLoader implements ImageLoader {

    private Context mContext;

    private RequestManager mRequestManager;

    private DrawableRequestBuilder<String> mDrawableRequestBuilder;

    private DrawableTypeRequest<Integer> mDrawableTypeRequest;

    private ArrayList<Transformation> mTransformations = new ArrayList<>();

    @Override
    public GlideImageLoader with(Context context) {
        mContext = context;
        mRequestManager = Glide.with(context);
        return this;
    }

    @Override
    public GlideImageLoader with(Activity activity) {
        mContext = activity;
        mRequestManager = Glide.with(activity);
        return this;
    }

    @Override
    public GlideImageLoader with(Fragment fragment) {
        mContext = fragment.getContext();
        mRequestManager = Glide.with(fragment);
        return this;
    }

    @Override
    public GlideImageLoader load(String url) {
        if (mRequestManager == null) {
            throw new RuntimeException("You must call with() method first!");
        }
        mDrawableRequestBuilder = mRequestManager.load(url);
        mDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
        return this;
    }

    @Override
    public ImageLoader load(int resId) {
        if (mRequestManager == null) {
            throw new RuntimeException("You must call with() method first!");
        }
        mDrawableTypeRequest = (DrawableTypeRequest<Integer>) mRequestManager.fromResource().load(resId);
        mDrawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
        return this;
    }

    @Override
    public GlideImageLoader thumbnail(float sizeMultiplier) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.thumbnail(sizeMultiplier);
        return this;
    }

    @Override
    public GlideImageLoader centerCrop() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.centerCrop();
        return this;
    }

    @Override
    public GlideImageLoader fitCenter() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.fitCenter();
        return this;
    }

    @Override
    public GlideImageLoader crossFade() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.crossFade();
        return this;
    }

    @Override
    public GlideImageLoader crossFade(int duration) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.crossFade(duration);
        return this;
    }

    @Override
    public GlideImageLoader crossFade(Animation animation, int duration) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.crossFade(animation, duration);
        return this;
    }

    @Override
    public GlideImageLoader crossFade(int animationId, int duration) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.crossFade(animationId, duration);
        return this;
    }

    @Override
    public GlideImageLoader dontAnimate() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.dontAnimate();
        return this;
    }

    @Override
    public GlideImageLoader animate(int animationId) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.animate(animationId);
        return this;
    }

    @Override
    public GlideImageLoader animate(Animation animation) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.animate(animation);
        return this;
    }

    @Override
    public GlideImageLoader placeholder(int resourceId) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.placeholder(resourceId);
        return this;
    }

    @Override
    public GlideImageLoader placeholder(Drawable drawable) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.placeholder(drawable);
        return this;
    }

    @Override
    public GlideImageLoader fallback(Drawable drawable) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.fallback(drawable);
        return this;
    }

    @Override
    public GlideImageLoader fallback(int resourceId) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.fallback(resourceId);
        return this;
    }

    @Override
    public GlideImageLoader error(int resourceId) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.error(resourceId);
        return this;
    }

    @Override
    public GlideImageLoader error(Drawable drawable) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.error(drawable);
        return this;
    }

    @Override
    public GlideImageLoader listener(final ImageLoaderListener listener) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if (listener != null) {
                    listener.onLoadingFailed(e);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (listener != null) {
                    listener.onLoadingComplete(resource);
                }
                return false;
            }
        });
        return this;
    }

    @Override
    public GlideImageLoader diskCacheType(DiskCacheType type) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        boolean cacheSource = type.cacheSource();
        boolean cacheResult = type.cacheResult();
        if (cacheSource && cacheResult) {
            mDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
        } else if (cacheSource) {
            mDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        } else if (cacheResult) {
            mDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
        } else {
            mDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        return this;
    }

    @Override
    public GlideImageLoader memoryCache(boolean cache) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.skipMemoryCache(!cache);
        return this;
    }

    @Override
    public GlideImageLoader override(int width, int height) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.override(width, height);
        return this;
    }

    @Override
    public GlideImageLoader blur(int radius) {
        if (mDrawableRequestBuilder == null && mDrawableTypeRequest == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mTransformations.add(new BlurTransformation(mContext, radius));
        return this;
    }
    /**
    * 李仕明加的东西   为了加载一张模糊的图，同时保证这个图的一个像素的字节为2，而不是4
     * start
    * */
    @Override
    public ImageLoader blurMinBitmap(int radius) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mTransformations.add(new MyBlurTransformation(mContext, radius));
        return this;

    }
    /*end */

    @Override
    public GlideImageLoader square() {
        mTransformations.add(new CropSquareTransformation(mContext));
        return this;
    }

    @Override
    public GlideImageLoader circle() {
        if (mDrawableRequestBuilder == null && mDrawableTypeRequest == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mTransformations.add(new CropCircleTransformation(mContext));
        return this;
    }

    @Override
    public GlideImageLoader round(int radius) {
        if (mDrawableRequestBuilder == null && mDrawableTypeRequest == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mTransformations.add(new RoundedCornersTransformation(mContext, radius, 0));
        return this;
    }

    @Override
    public GlideImageLoader transform(IBitmapTransformation transformation) {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mTransformations.add(new GlideTransform(mContext, transformation));
        return this;
    }


    @Override
    public GlideImageLoader dontTransform() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        mDrawableRequestBuilder.dontTransform();
        return this;
    }

    @Override
    public GlideImageLoader into(ImageView view) {
        if (mDrawableRequestBuilder == null && mDrawableTypeRequest == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        try {
            if (mDrawableRequestBuilder != null){
                int size = mTransformations.size();
                if (size != 0) {
                    Transformation[] transformations = new Transformation[size];
                    for (int i = 0; i < size; i++) {
                        transformations[i] = mTransformations.get(i);
                    }
                    mDrawableRequestBuilder.bitmapTransform(transformations);
                }
                mDrawableRequestBuilder.into(view);
            } else if (mDrawableTypeRequest != null){
                int size = mTransformations.size();
                if (size != 0) {
                    Transformation[] transformations = new Transformation[size];
                    for (int i = 0; i < size; i++) {
                        transformations[i] = mTransformations.get(i);
                    }
                    mDrawableTypeRequest.bitmapTransform(transformations);
                }
                mDrawableTypeRequest.into(view);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public GlideImageLoader preload() {
        if (mDrawableRequestBuilder == null) {
            throw new RuntimeException("You must call load() method first!");
        }
        try {
            int size = mTransformations.size();
            if (size != 0) {
                Transformation[] transformations = new Transformation[size];
                for (int i = 0; i < size; i++) {
                    transformations[i] = mTransformations.get(i);
                }
                mDrawableRequestBuilder.bitmapTransform(transformations);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawableRequestBuilder.preload();
        return this;
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public File getPhotoCacheFile(Context context) {
        File photoCacheDir = Glide.getPhotoCacheDir(context);
        return photoCacheDir;
    }

}
