package com.bluewhaledt.saylove.photo.uploader;

/**
 * 单文件上传器接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface ISingleUploader extends IUploader {
    /**
     * 将“上传至腾讯云”步骤的结果反馈给服务器，反馈成功了，告诉uploader所有步骤完成
     */
    void callSuccess();

    /**
     * 将“上传至腾讯云”步骤的结果反馈给服务器，反馈失败了，告诉uploader所有步骤完成
     *
     * @param errorMsg 反馈过程中的报错信息
     */
    void callFailed(String errorMsg);


}
