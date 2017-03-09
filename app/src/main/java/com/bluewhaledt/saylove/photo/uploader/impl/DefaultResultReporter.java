package com.bluewhaledt.saylove.photo.uploader.impl;

import android.content.Context;

import com.bluewhaledt.saylove.photo.uploader.IUploader;
import com.bluewhaledt.saylove.photo.uploader.IUploaderResultReporter;

import java.util.List;

/**
 * 反馈“上传至腾讯云”步骤的结果给服务器的接口默认实现
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public class DefaultResultReporter implements IUploaderResultReporter {
    @Override
    public void reportSuccess(Context context, IUploader uploader, List<String> cosPaths) {

    }

    @Override
    public void reportError(Context context, final IUploader uploader, final int errorCode, final String errorMsg) {


    }
}
