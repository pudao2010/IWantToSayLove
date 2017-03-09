package com.bluewhaledt.saylove.photo.uploader.impl;

/**
 * 单文件上传事件
 *
 * @author yintaibing
 * @date 2016/11/21
 */
public class SingleUploaderEvent {
    public int uploaderId;
    public boolean isSuccess, isFailed;
    public long sentBytes, totalBytes;
    public String errorMsg;
}
