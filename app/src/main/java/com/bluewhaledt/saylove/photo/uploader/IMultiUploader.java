package com.bluewhaledt.saylove.photo.uploader;

/**
 * 多文件上传器接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface IMultiUploader extends IUploader {
    /**
     * 某个文件上传至腾讯云失败，反馈错误信息给服务器后，告诉uploader该文件处理完成，继续上传下一个文件
     *
     * @param isSuccess
     */
    void callItemFinished(boolean isSuccess);

    /**
     * 所有文件上传结束（可能部分成功部分失败），将最终汇总结果反馈给服务器后，告诉uploader所有步骤均完成
     */
    void callAllFinished();
}
