package com.bluewhaledt.saylove.photo.uploader.impl;

import android.content.Context;

import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploader;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploaderListener;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.UpdateObjectRequest;
import com.tencent.cos.model.UpdateObjectResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 单文件上传器实现
 *
 * @author yintaibing
 * @date 2016/11/21
 */
public class SinglePhotoUploader extends BaseUploader implements ISingleUploader {

    private String mSrcPath;        // 文件本地路径
    private String mReportFileName; // 反馈给服务器的腾讯云路径

    private PutObjectRequest mRequest;
    private ISingleUploaderListener mListener;
    private UploadSignEntity uploadSignEntity;

    public SinglePhotoUploader(Context context, String srcPath) {
        super(context);
        mSrcPath = srcPath;
    }

    @Override
    public void start() {
        mSignGetter.getSign(mContext, this, mSrcPath);
    }

    @Override
    public void pause() {
        super.pause();
        if (mRequest != null) {
            mClient.pauseTask(mRequest.getRequestId());
        }
    }

    public void updateFile(final String bucket,final String cosPath,final String sign,final String authority){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
                updateObjectRequest.setBucket(bucket);
                updateObjectRequest.setCosPath(cosPath);
                updateObjectRequest.setAuthority(authority);
                updateObjectRequest.setSign(sign);
                updateObjectRequest.setListener(new ICmdTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {


                    }

                    @Override
                    public void onFailed(COSRequest COSRequest, COSResult cosResult) {

                    }
                });

                UpdateObjectResult result= mClient.updateObject(updateObjectRequest);
            }
        }).start();

    }

    @Override
    public void resume() {
        super.resume();
        if (mRequest != null) {
            mClient.resumeTask(mRequest.getRequestId());
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mRequest != null) {
            mClient.cancelTask(mRequest.getRequestId());
        }
    }

    /**
     * 接受从后台获取的腾讯云所需参数
     *
     * @param bucket  腾讯云bucket
     * @param sign    腾讯云签名
     * @param cosPath 文件对应的腾讯云路径
     * @param srcPath 文件本地路径
     */
    @Override
    public void deliverParams(String bucket, String sign, String cosPath, String srcPath) {
        if (isCanceled()) {
            return;
        }

        mReportFileName = cosPath.substring(cosPath.lastIndexOf("/") + 1);
        mRequest = createRequest(bucket, sign, cosPath, srcPath);
        mRequest.setListener(new IUploadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long sentBytes, long totalBytes) {
                SingleUploaderEvent event = new SingleUploaderEvent();
                event.uploaderId = SinglePhotoUploader.this.hashCode();
                event.sentBytes = sentBytes;
                event.totalBytes = totalBytes;
                EventBus.getDefault().post(event);
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if (!isCanceled()) {
                    List<String> reportFileNames = new ArrayList<>(1);
                    reportFileNames.add(mReportFileName);
                    mResultReporter.reportSuccess(mContext, SinglePhotoUploader.this,
                            reportFileNames);
                }
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                mResultReporter.reportError(mContext, SinglePhotoUploader.this, cosResult.code,
                        cosResult.msg);
            }
        });
        mClient.putObject(mRequest);
    }

    @Override
    public void getSignSuccess(UploadSignEntity entity) {
        this.uploadSignEntity =entity;
    }

    public UploadSignEntity getUploadSignEntity() {
        return uploadSignEntity;
    }

    /**
     * 将“上传至腾讯云”步骤的结果反馈给服务器，反馈成功了，告诉uploader所有步骤完成
     */
    @Override
    public void callSuccess() {
        SingleUploaderEvent event = new SingleUploaderEvent();
        event.uploaderId = SinglePhotoUploader.this.hashCode();
        event.isSuccess = true;
        EventBus.getDefault().post(event);
    }

    /**
     * 将“上传至腾讯云”步骤的结果反馈给服务器，反馈失败了，告诉uploader所有步骤完成
     *
     * @param errorMsg 反馈过程中的报错信息
     */
    @Override
    public void callFailed(String errorMsg) {
        SingleUploaderEvent event = new SingleUploaderEvent();
        event.uploaderId = SinglePhotoUploader.this.hashCode();
        event.isFailed = true;
        event.errorMsg = errorMsg;
        EventBus.getDefault().post(event);
    }

    @Override
    public void release() {
        super.release();
        mListener = null;
        mRequest = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SingleUploaderEvent event) {
        if (event.uploaderId == this.hashCode() && mListener != null) {
            if (event.isSuccess) {
                mListener.onSuccess(mRequest.getSrcPath(), mRequest.getCosPath());
                release();
            } else if (event.isFailed) {
                mListener.onFailed(event.errorMsg);
                release();
            } else {
                mListener.onProgress(event.sentBytes, event.totalBytes);
            }
        }
    }

    public void setListener(ISingleUploaderListener listener) {
        mListener = listener;
    }
}
