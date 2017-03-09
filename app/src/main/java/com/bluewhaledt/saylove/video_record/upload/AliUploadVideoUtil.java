package com.bluewhaledt.saylove.video_record.upload;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.entity.UploadAliSignEntity;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.VideoService;

import java.io.File;

import rx.Observable;

/**
 * Created by rade.chan on 2016/12/10.
 */

public class AliUploadVideoUtil {

    private static AliUploadVideoUtil instance;

    private UploadListener mUploadListener;
    private VideoService service;


    private AliUploadVideoUtil() {
        service = ZARetrofit.getService(ZhenaiApplication.getContext(), VideoService.class);
    }

    public void setListener(UploadListener listener) {
        this.mUploadListener = listener;
    }


    public static AliUploadVideoUtil getInstance() {
        if (instance == null) {
            synchronized (AliUploadVideoUtil.class) {
                if (instance == null) {
                    instance = new AliUploadVideoUtil();
                }
            }
        }
        return instance;
    }

    private void startUploadVideo(final String filePath, final UploadAliSignEntity uploadSign) {
        final VODUploadClient upload = new VODUploadClient(ZhenaiApplication.getContext());
        VODUploadCallback callback = new VODUploadCallback() {
            @Override
            public void onUploadSucceed(UploadFileInfo info) {
                if (mUploadListener != null) {
                    mUploadListener.uploadSuccess(uploadSign.targetFileName);
                }
            }

            @Override
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                if (mUploadListener != null) {
                    mUploadListener.uploadFail(code,message);
                }
            }

            @Override
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                if (mUploadListener != null) {
                    mUploadListener.uploadProgress(uploadedSize * 100 / totalSize);
                }
            }

            @Override
            public void onUploadTokenExpired() {
                if (mUploadListener != null) {
                    mUploadListener.uploadFail(null,"onUploadTokenExpired");
                }
            }
        };
        upload.init(uploadSign.accessKeyId, uploadSign.accessKeySecret, uploadSign.securityToken, uploadSign.expiration, callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    upload.addFile(filePath, uploadSign.endpoint,
                            uploadSign.bucket, uploadSign.targetFileName);
                    upload.startUpload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void saveVideoInfo(String topicId,final String fileName,int filedId) {
        Observable<ZAResponse> observable = service.saveVideoInfo(topicId, fileName,filedId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>
                (new ZASubscriberListener<ZAResponse>() {

                    @Override
                    public void onSuccess(ZAResponse response) {
                       if(mUploadListener!=null){
                           mUploadListener.saveSuccess();
                       }
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        super.onFail(errorCode, errorMsg);
                        if(mUploadListener!=null){
                            mUploadListener.saveFail(fileName);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mUploadListener!=null){
                            mUploadListener.saveFail(fileName);
                        }
                    }
                }));
    }


    public void uploadVideo(final String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            ToastUtils.toast(ZhenaiApplication.getContext(), "视频文件不存在");
            return;
        }

        Observable<ZAResponse<UploadAliSignEntity>> observable = service.getAliVideoSign();
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<UploadAliSignEntity>>
                (new ZASubscriberListener<ZAResponse<UploadAliSignEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<UploadAliSignEntity> response) {
                        if (response.data != null) {
                            startUploadVideo(filePath, response.data);
                        }
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {

                        if(mUploadListener!=null){
                            mUploadListener.uploadFail(errorCode,errorMsg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mUploadListener!=null){
                            mUploadListener.uploadFail("",ZhenaiApplication.getInstance().getResources().
                                    getString(R.string.no_network_connected));
                        }
                    }
                }));
    }

    public interface UploadListener {
        void uploadFail(String errorCode,String errorMsg);

        void uploadProgress(float percent);

        void uploadSuccess(String name);

        void saveSuccess();

        void saveFail(String fileName);
    }


}
