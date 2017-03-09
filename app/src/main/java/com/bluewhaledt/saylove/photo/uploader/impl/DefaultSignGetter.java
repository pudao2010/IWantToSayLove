package com.bluewhaledt.saylove.photo.uploader.impl;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.photo.uploader.IMultiUploader;
import com.bluewhaledt.saylove.photo.uploader.ISignGetter;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploader;
import com.bluewhaledt.saylove.photo.uploader.IUploader;
import com.bluewhaledt.saylove.service.QCloudService;

import rx.Observable;

/**
 * 腾讯云签名获取器接口默认实现
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public class DefaultSignGetter implements ISignGetter {
    @Override
    public void getSign(final Context context, final IUploader uploader, final String srcFilePath) {
        QCloudService service = ZARetrofit.getService(context, QCloudService.class);
        Observable<ZAResponse<UploadSignEntity>> observable = service.getQCloudSign(FileUtils
                .getSuffix(srcFilePath));
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<UploadSignEntity>>(new ZASubscriberListener<ZAResponse<UploadSignEntity>>() {
            @Override
            public void onSuccess(final ZAResponse<UploadSignEntity> response) {
                if (response.data != null) {
                    uploader.getSignSuccess(response.data);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploader.deliverParams(response.data.bucket, response.data.sign,
                                    response.data.filePath, srcFilePath);
                        }
                    }).start();

                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                if (uploader instanceof ISingleUploader) {
                    ((ISingleUploader) uploader).callFailed(errorMsg);
                } else if (uploader instanceof IMultiUploader) {
                    ((IMultiUploader) uploader).callItemFinished(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (uploader instanceof ISingleUploader) {
                    ((ISingleUploader) uploader).callFailed(context.getString(R.string.no_network_connected));
                } else if (uploader instanceof IMultiUploader) {
                    ((IMultiUploader) uploader).callItemFinished(false);
                }
            }
        }));
    }
}
