package com.bluewhaledt.saylove.video_record.recoder;

/**
 * Created by rade.chan on 2016/12/13.
 */

public abstract class BaseAvRecorder {

    protected OnMediaStatusListener mediaStatusListener;

    public void setMediaStatusListener(OnMediaStatusListener listener) {
        this.mediaStatusListener = listener;
    }


    public abstract void startRecord();

    public abstract void stopRecord(boolean shouldCallback);

    public abstract void releaseResource();

    public abstract  void giveUpRecord();


    public interface OnMediaStatusListener {
        void onMediaFileCreate(String filePath);
    }

    public abstract  void startRequestAudio();

    public abstract  void stopRequestAudio();
}
