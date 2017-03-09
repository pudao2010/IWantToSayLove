package com.bluewhaledt.saylove.util;

import android.content.Context;
import android.util.Log;

import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.imageloader.base.ImageLoader;
import com.bluewhaledt.saylove.video_record.helper.FileHelper;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove.util
 * @文件名: CacheManager
 * @创建者: YanChao
 * @创建时间: 2016/12/17 17:40
 * @描述： TODO
 */
public class CacheManager {

    private  Long totalCacheSize;
    private Context mContext;
    private static CacheManager instance;
    private ImageLoader mImageLoader;

    private CacheManager(Context context){
        mContext = context;
        mImageLoader = ImageLoaderFactory.getImageLoader();
        totalCacheSize = getTotalCacheSize();
    }

    public static CacheManager getInstance(Context context) {
        if (instance == null){
            synchronized (CacheManager.class){
                if (instance == null){
                    instance = new CacheManager(context);
                }
            }
        }
        return instance;
    }

    private Integer getFileNum(String filePath){
        int fileNum = 0;
        File file0 = new File(filePath);
        if (file0.exists()) {
            File[] files = file0.listFiles();
            for (File file1 : files) {
                if (file1.isFile()) {
                    fileNum++;
                }else{
                    getFileNum(file1.getAbsolutePath());
                }
            }
        }
        return fileNum;
    }

    public Long getTotalSize(){
        return totalCacheSize;
    }

    public void clearPhotoCache(){
        mImageLoader.clearMemoryCache(mContext);
        mImageLoader.clearDiskCache(mContext);
    }

    public void clearAVCache(){
        FileHelper.deleteAllFile();
    }

    private Long getTotalCacheSize(){
        long avSize = FileUtils.getFileOrDirSize(getVideoFile());
        long photoSize = FileUtils.getFileOrDirSize(getPhotoFile());
        return avSize + photoSize;
    }

    private File getVideoFile(){
        return new File(FileHelper.VIDEO_PATH);
    }

    private  File getPhotoFile(){
        return  mImageLoader.getPhotoCacheFile(mContext);
    }

    public void clearCache(final OnClearCacheListener listener) {
        File[] files = {getPhotoFile(), getVideoFile()};
        Observable.from(files)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return RxUtil.listFiles(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<File>() {
                            @Override
                            public void onCompleted() {
                                Log.d("yanc", "onCompleted");
                                listener.onClearSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                    listener.onClearError();
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                Log.d("yanc", "onStart");
                                listener.onClearStart();
                            }

                            @Override
                            public void onNext(File file) {
                                Log.d("yanc", "onNext");
                                file.delete();
                            }
                        }
                );
    }

    public interface OnClearCacheListener{
        void onClearStart();
        void onClearError();
        void onClearSuccess();
    }

}
