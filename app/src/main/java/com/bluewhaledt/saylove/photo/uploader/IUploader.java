package com.bluewhaledt.saylove.photo.uploader;

import com.bluewhaledt.saylove.entity.UploadSignEntity;

/**
 * 腾讯云上传器接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface IUploader {
    void start();

    void pause();

    void resume();

    void cancel();

    /**
     * 接受从后台获取的腾讯云所需参数
     *
     * @param bucket  腾讯云bucket
     * @param sign    腾讯云签名
     * @param cosPath 文件对应的腾讯云路径
     * @param srcPath 文件本地路径
     */
    void deliverParams(String bucket, String sign, String cosPath, String srcPath);

    void getSignSuccess(UploadSignEntity entity);
}
