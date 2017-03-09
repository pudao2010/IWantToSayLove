package com.bluewhaledt.saylove.photo.uploader.impl;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.photo.uploader.IMultiUploader;
import com.bluewhaledt.saylove.photo.uploader.IMultiUploaderListener;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.task.listener.IUploadTaskListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多文件上传器接口实现
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public class MultiPhotoUploader extends BaseUploader implements IMultiUploader {

    private final List<String> mSrcPaths;    // 文件本地路径
    private final boolean[] mUploadResults;  // 各文件上传结果
    private final String[] mReportFileNames; // 反馈给服务器的各文件腾讯云路径
    private final long[] mItemSentBytes;     // 每个文件已发送字节数
    private volatile int mSentCount;         // 已发送文件数
    private int mTotalCount;                 // 总文件数
    private volatile long mSentBytes;        // 已发送字节数
    private long mTotalBytes;                // 总字节数
    private String mErrorMsg;                // 错误信息，只保留一个
    private List<String> mSuccessList;       // 上传至腾讯云成功的文件的本地路径
    private List<String> mFailedList;        // 上传至腾讯云失败的文件的本地路径

    private PutObjectRequest mRequest;
    private IMultiUploaderListener mListener;

    public MultiPhotoUploader(Context context, List<String> srcPaths) {
        super(context);
        mSrcPaths = srcPaths;
        mTotalCount = mSrcPaths.size();
        mUploadResults = new boolean[mTotalCount];
        mReportFileNames = new String[mTotalCount];
        mItemSentBytes = new long[mTotalCount];

        for (int i = 0; i < mTotalCount; i++) {
            String srcPath = mSrcPaths.get(i);
            long bytes = FileUtils.getFileOrDirSize(new File(srcPath));
            mTotalBytes += bytes;
        }
    }

    /**
     * 上传下一个文件
     */
    private void uploadNext() {
        mSignGetter.getSign(mContext, this, mSrcPaths.get(mSentCount));
    }

    @Override
    public void start() {
        uploadNext();
    }

    @Override
    public void pause() {
        super.pause();
        if (mRequest != null) {
            mClient.pauseTask(mRequest.getRequestId());
        }
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

        int index = mSrcPaths.indexOf(srcPath);
        mReportFileNames[index] = cosPath.substring(cosPath.lastIndexOf("/") + 1);

        mRequest = createRequest(bucket, sign, cosPath, srcPath);
        QQListener listener = new QQListener();
        listener.index = index;
        mRequest.setListener(listener);

        mClient.putObject(mRequest);
    }

    @Override
    public void getSignSuccess(UploadSignEntity entity) {

    }

    /**
     * 某个文件上传至腾讯云失败，反馈错误信息给服务器后，告诉uploader该文件处理完成，继续上传下一个文件
     *
     * @param isSuccess
     */
    @Override
    public void callItemFinished(boolean isSuccess) {
        mUploadResults[mSentCount] = isSuccess;
        mSentCount++;

        MultiUploaderEvent event = new MultiUploaderEvent();
        event.uploaderId = MultiPhotoUploader.this.hashCode();
        event.isItemFinished = true;
        event.sentCount = mSentCount;
        event.totalCount = mTotalCount;
        EventBus.getDefault().post(event);

        if (mSentCount == mTotalCount) {
            List<String> reportSuccessList = new ArrayList<>();
            mSuccessList = new ArrayList<>();
            mFailedList = new ArrayList<>();
            for (int i = 0; i < mTotalCount; i++) {
                if (mUploadResults[i]) {
                    reportSuccessList.add(mReportFileNames[i]);
                    mSuccessList.add(mSrcPaths.get(i));
                } else {
                    mFailedList.add(mSrcPaths.get(i));
                }
            }
            mResultReporter.reportSuccess(mContext, this, reportSuccessList);
        } else {
            uploadNext();
        }
    }

    /**
     * 所有文件上传结束（可能部分成功部分失败），将最终汇总结果反馈给服务器后，告诉uploader所有步骤均完成
     */
    @Override
    public void callAllFinished() {
        MultiUploaderEvent event = new MultiUploaderEvent();
        event.uploaderId = MultiPhotoUploader.this.hashCode();
        event.isAllFinished = true;
        event.errorMsg = mErrorMsg;
        EventBus.getDefault().post(event);
    }

    @Override
    public void release() {
        super.release();
        mListener = null;
        mRequest = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MultiUploaderEvent event) {
        if (event.uploaderId == this.hashCode() && mListener != null) {
            if (event.isAllFinished) {
                mListener.onAllFinished(mSuccessList, mFailedList);
                release();
            } else if (event.isItemFinished) {
                mListener.onStepProgress(event.sentCount, event.totalCount);
            } else {
                mListener.onPercentProgress(event.sentBytes, event.totalBytes);
            }
        }
    }

    public void setListener(IMultiUploaderListener listener) {
        mListener = listener;
    }

    /**
     * 发布进度（百分比）
     *
     * @param index          当前是第几个文件
     * @param itemSentBytes  该文件已发送字节数
     * @param itemTotalBytes 该文件总字节数
     */
    private void publishPercentProgress(int index, long itemSentBytes, long itemTotalBytes) {
        long itemLastSentBytes = mItemSentBytes[index];
        long increase = itemSentBytes - itemLastSentBytes;
        mSentBytes += increase;
        mItemSentBytes[index] = itemSentBytes;

        MultiUploaderEvent event = new MultiUploaderEvent();
        event.uploaderId = MultiPhotoUploader.this.hashCode();
        event.sentBytes = mSentBytes;
        event.totalBytes = mTotalBytes;
        EventBus.getDefault().post(event);
    }

    /**
     * 发布进度（阶段式）
     *
     * @param index     当前是第几个文件
     * @param isSuccess 该文件上传成功或失败
     * @param errorMsg  错误信息
     */
    private void publishStepProgress(int index, boolean isSuccess, String errorMsg) {
        if (mErrorMsg == null) {
            mErrorMsg = errorMsg;
        }
        callItemFinished(isSuccess);
    }

    /**
     * 自定义的腾讯云上传接口监听器
     */
    private class QQListener implements IUploadTaskListener {
        int index;

        @Override
        public void onProgress(COSRequest cosRequest, long itemSentBytes, long itemTotalBytes) {
            publishPercentProgress(index, itemSentBytes, itemTotalBytes);
        }

        @Override
        public void onCancel(COSRequest cosRequest, COSResult cosResult) {

        }

        @Override
        public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
            publishStepProgress(index, true, null);
        }

        @Override
        public void onFailed(COSRequest cosRequest, COSResult cosResult) {
            publishStepProgress(index, false, cosResult.msg);
            mResultReporter.reportError(mContext, MultiPhotoUploader.this, cosResult.code,
                    cosResult.msg);
        }
    }
}
