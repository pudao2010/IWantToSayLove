package com.bluewhaledt.saylove.photo.uploader;

import java.util.List;

/**
 * 多文件上传器接口
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface IMultiUploaderListener {
    /**
     * 进度回调（阶段式，已上传4/9个）
     *
     * @param sentCount  已发送文件数
     * @param totalCount 总文件数
     */
    void onStepProgress(int sentCount, int totalCount);

    /**
     * 进度回调（百分比式）
     *
     * @param sentBytes  已发送字节数
     * @param totalBytes 总字节数
     */
    void onPercentProgress(long sentBytes, long totalBytes);

    /**
     * 所有文件上传结束的回调
     *
     * @param successPaths 上传成功的文件的路径
     * @param failedPaths  上传失败的文件的路径
     */
    void onAllFinished(List<String> successPaths, List<String> failedPaths);
}
