package com.bluewhaledt.saylove.photo.uploader.impl;

import android.content.Context;

import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.photo.uploader.ISignGetter;
import com.bluewhaledt.saylove.photo.uploader.IUploader;
import com.bluewhaledt.saylove.photo.uploader.IUploaderResultReporter;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.PutObjectRequest;

import org.greenrobot.eventbus.EventBus;

/**
 * 文件上传器基类
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public abstract class BaseUploader implements IUploader {

    protected Context mContext;
    protected COSClient mClient;
    protected ISignGetter mSignGetter;
    protected IUploaderResultReporter mResultReporter;
    protected boolean mIsPaused;
    protected boolean mIsCanceled;

    BaseUploader(Context context) {
        mContext = context.getApplicationContext();
        init(mContext);
    }

    @Override
    public void pause() {
        mIsPaused = true;
    }

    @Override
    public void resume() {
        mIsPaused = false;
    }

    @Override
    public void cancel() {
        mIsCanceled = true;
    }

    protected void release() {
        EventBus.getDefault().unregister(this);
        mContext = null;
        mClient = null;
        mSignGetter = null;
        mResultReporter = null;
    }

    public void setSignGetter(ISignGetter signGetter) {
        mSignGetter = signGetter;
    }

    public void setResultReporter(IUploaderResultReporter resultReporter) {
        mResultReporter = resultReporter;
    }

    public boolean isPaused() {
        return mIsPaused;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    /**
     * 创建一个腾讯云上传请求对象
     *
     * @param bucket  腾讯云bucket
     * @param sign    腾讯云签名
     * @param cosPath 腾讯云路径
     * @param srcPath 文件本地路径
     * @return PutObjectRequest
     */
    protected PutObjectRequest createRequest(String bucket, String sign, String cosPath, String
            srcPath) {
        PutObjectRequest request = new PutObjectRequest();
        request.setBucket(bucket);
        request.setSign(sign);
        request.setCosPath(cosPath);
        request.setSrcPath(srcPath);
        return request;
    }

    private void init(Context context) {
        COSClientConfig config = new COSClientConfig();
        config.setEndPoint(COSEndPoint.COS_TJ);
        // 目前不需要持久化，传的persistenceId为空
        mClient = new COSClient(context, Constants.QCLOUD_APPID, config, null);

        EventBus.getDefault().register(this);
    }
}

