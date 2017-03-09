package com.bluewhaledt.saylove.photo.impl;

import com.bluewhaledt.saylove.entity.UploadSignEntity;

/**
 * Created by rade.chan on 2016/12/3.
 */

public interface IPhotoInvokeHandler {
    void showUploadProgress(String msg);

    void closeUploadProgress();

    void uploadSuccess(int type, String srcPath, String cosPath);

    void uploadFail(int type,String msg);

    void uploadSignSuccess(UploadSignEntity signEntity);

    boolean isIntercept();

    boolean isSetFilePrivate();
}
