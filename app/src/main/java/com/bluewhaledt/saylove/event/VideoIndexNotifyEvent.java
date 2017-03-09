package com.bluewhaledt.saylove.event;

/**
 * Created by rade.chan on 2016/12/20.
 */

public class VideoIndexNotifyEvent {
    private String videoId;
    private int likeId;
    private boolean isRefresh;

    public VideoIndexNotifyEvent(String videoId, int likeId) {
        this.videoId = videoId;
        this.likeId = likeId;
    }

    public VideoIndexNotifyEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public int getLikeId() {
        return likeId;
    }

    public String getVideoId() {
        return videoId;
    }
}
