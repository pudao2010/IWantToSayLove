package com.bluewhaledt.saylove.photo.uploader.impl;

/**
 * 多文件上传事件
 *
 * @author yintaibing
 * @date 2016/11/21
 */
public class MultiUploaderEvent {
    public int uploaderId;
    public boolean isAllFinished;
    public boolean isItemFinished;
    public int sentCount, totalCount;
    public long sentBytes, totalBytes;
    public String errorMsg;
}
