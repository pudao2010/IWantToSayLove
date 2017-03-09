package com.bluewhaledt.saylove.photo.uploader;

/**
 * 单文件上传器监听器
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface ISingleUploaderListener {
    /**
     * 进度回调（百分比式）
     *
     * @param sentBytes  已发送字节数
     * @param totalBytes 总字节数
     */
    void onProgress(long sentBytes, long totalBytes);

    /**
     * 成功回调
     */
    void onSuccess(String srcPath, String cosPath);

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    void onFailed(String errorMsg);
}
