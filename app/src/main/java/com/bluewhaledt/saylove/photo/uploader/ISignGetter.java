package com.bluewhaledt.saylove.photo.uploader;

import android.content.Context;

/**
 * 腾讯云签名获取器接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface ISignGetter {
    /**
     * 获取腾讯云签名
     *
     * @param context     Context
     * @param uploader    Uploader对象
     * @param srcFilePath 本地文件路径
     */
    void getSign(Context context, final IUploader uploader, String srcFilePath);
}
