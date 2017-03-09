package com.bluewhaledt.saylove.photo.uploader;

import android.content.Context;

import java.util.List;

/**
 * 反馈“上传至腾讯云”步骤的结果给服务器的接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface IUploaderResultReporter {
    /**
     * 上传至腾讯云完成后，将腾讯云路径反馈给服务器
     *
     * @param context  Context
     * @param uploader Uploader对象
     * @param cosPaths 腾讯云路径
     */
    void reportSuccess(Context context, IUploader uploader, List<String> cosPaths);

    /**
     * 上传至腾讯云完成（失败）后，将错误信息反馈给服务器
     *
     * @param context   Context
     * @param uploader  Uploader对象
     * @param errorCode 腾讯云错误码
     * @param errorMsg  腾讯云错误信息
     */
    void reportError(Context context, IUploader uploader, int errorCode, String errorMsg);
}
