package com.bluewhaledt.saylove.video_record.recoder;

import android.os.Handler;

import com.bluewhaledt.saylove.video_record.audio.AudioEncoder;
import com.bluewhaledt.saylove.video_record.audio.AudioSoftwareController;
import com.bluewhaledt.saylove.video_record.camera.CameraRecordRenderer;
import com.bluewhaledt.saylove.video_record.helper.CombineUtil;
import com.bluewhaledt.saylove.video_record.helper.FileHelper;
import com.bluewhaledt.saylove.video_record.show_view.CameraSurfaceView;
import com.bluewhaledt.saylove.video_record.video.EncoderConfig;

/**
 * 有滤镜模式下的音视频录制
 * Created by rade.chan on 2016/12/13.
 */

public class FilterAvRecorder extends BaseAvRecorder {
    private CameraSurfaceView mCameraSurfaceView;
    private AudioEncoder mAudioEncoder;
    private AudioSoftwareController mAudioController;

    public FilterAvRecorder(CameraSurfaceView surfaceView) {
        this.mCameraSurfaceView = surfaceView;
    }


    @Override
    public void startRecord() {
        CameraRecordRenderer renderer = mCameraSurfaceView.getRenderer();
//        float scale = 480 / (ZhenaiApplication.getInstance().getScreenWidth() * 1.0f);
//        int statusBarHeight = 0;
//        int resourceId = ZhenaiApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            statusBarHeight = ZhenaiApplication.getContext().getResources().getDimensionPixelSize(resourceId);
//        }
//        int height = (int) ((ZhenaiApplication.getInstance().getScreenHeight() -
//                statusBarHeight) * scale) / 10 * 10;   //确保占满全屏,留整，否则会crash
        renderer.setEncoderConfig(new EncoderConfig(FileHelper.createTempVideo(), 480, 640,
                1024 * 1024 /* 1 Mb/s */));
        setVideoIsRecord(true);

        mAudioEncoder = new AudioEncoder(FileHelper.createTempAudio());
        mAudioController = new AudioSoftwareController();
        mAudioController.setAudioEncoder(mAudioEncoder);
        mAudioEncoder.setAudioSoftwareController(mAudioController);
        mAudioController.startPolling();
    }

    @Override
    public void stopRecord(final boolean shouldCallback) {
        if (mAudioEncoder != null) {
            mAudioController.stopPolling();
            mAudioEncoder.stop();
            mAudioEncoder = null;
        }
        setVideoIsRecord(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CombineUtil.combineVideoAndAudio();     //合成视频和音频
                FileHelper.deleteTempFile();
                if (shouldCallback && mediaStatusListener != null) {
                    mediaStatusListener.onMediaFileCreate(FileHelper.getRecordVideoPath());
                }
            }
        }, 500);
    }

    @Override
    public void releaseResource() {
        if (mAudioEncoder != null) {
            mAudioController.stopPolling();
            mAudioEncoder.stop();
            mAudioEncoder = null;
        }
        setVideoIsRecord(false);
    }

    @Override
    public void giveUpRecord() {
        if (mAudioEncoder != null) {
            mAudioController.stopPolling();
            mAudioEncoder.stop();
            mAudioEncoder = null;
        }
        setVideoIsRecord(false);
    }

    @Override
    public void startRequestAudio() {
        mAudioEncoder = new AudioEncoder(FileHelper.createTempAudio());
        mAudioController = new AudioSoftwareController();
        mAudioController.setAudioEncoder(mAudioEncoder);
        mAudioEncoder.setAudioSoftwareController(mAudioController);
        mAudioController.startPolling();
    }

    @Override
    public void stopRequestAudio() {
        if (mAudioEncoder != null) {
            mAudioController.stopPolling();
            mAudioEncoder.stop();
            mAudioEncoder = null;
        }
    }


    private void setVideoIsRecord(final boolean isRecord) {
        mCameraSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mCameraSurfaceView.getRenderer().setRecordingEnabled(isRecord);
            }
        });
    }
}
