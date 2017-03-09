package com.bluewhaledt.saylove.video_record.controller;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.video_record.helper.FileHelper;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/13.
 */

public class AvRecorderController implements SurfaceHolder.Callback {

    private final static int CAMERA_TYPE_FAIL = 0; // 无效摄像头

    private final static int CAMERA_TYPE_BACK = 1; // 后置摄像头

    private final static int CAMERA_TYPE_FRONT = 2;// 前置摄像头

    private final static int VIDEO_LENGTH = 15 * 1000;// 最大时长 单位毫秒

    private int mCurrentCameraType = CAMERA_TYPE_FRONT;

    private SurfaceView mVideoSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera = null;

    private int outPutWidth = 480;
    private int outPutHeight = 640;
    private float outputRatio = ZhenaiApplication.getInstance().getScreenHeight() * 1.0f / ZhenaiApplication.getInstance().getScreenWidth();
    ;


    public AvRecorderController(SurfaceView surfaceView) {
        this.mVideoSurfaceView = surfaceView;
        initMediaRecord();
    }

    private void initMediaRecord() {
        mVideoSurfaceView.getHolder().setType(
                SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder = mVideoSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
    }

    public void startRecordVideo() {
        FileHelper.createRecordMedia();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        try {
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            // 设置音频录入源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置视频图像的录入源
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录入媒体的输出格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置音频的编码格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // 设置视频的编码格式
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setVideoEncodingBitRate(1024 * 1024);

            float scale = 480 / (ZhenaiApplication.getInstance().getScreenWidth() * 1.0f);
            int statusBarHeight = 0;
            int resourceId = ZhenaiApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = ZhenaiApplication.getContext().getResources().getDimensionPixelSize(resourceId);
            }
            int height = (int) ((ZhenaiApplication.getInstance().getScreenHeight() -
                    statusBarHeight) * scale) / 10 * 10;


            mMediaRecorder.setVideoSize(outPutHeight, outPutWidth); //更改方向，height width 更换


        } catch (Exception e) {
            e.printStackTrace();

            mMediaRecorder.reset();
            releaseCamera();

            return;
        }

        if (Build.VERSION.SDK_INT >= 9) {
            mMediaRecorder.setOrientationHint(270);
        }
        // 设置录制视频文件的输出路径
        mMediaRecorder.setMaxDuration(VIDEO_LENGTH);// 设置最大时长
        mMediaRecorder.setOutputFile(FileHelper.getRecordVideoPath());
        // 设置捕获视频图像的预览界面
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {

            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecorder();
                }
            }
        });
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                // 发生错误，停止录制
                if (mMediaRecorder != null) {
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                    mMediaRecorder = null;
                }
            }
        });
        // 准备、开始
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
    }


    public void stopRecorder() {
        // 如果正在录制，停止并释放资源
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if (mCamera == null) {
            try {
                if (!openCamera()) {
                    return;
                }
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                releaseCamera();
            }
        }
    }


    private boolean openCamera() {
        try {
            releaseCamera();
            int cameraIndex = -1;
            if (mCurrentCameraType == CAMERA_TYPE_FRONT) {
                // 切换到后置
                cameraIndex = FindFrontCamera();
                mCurrentCameraType = CAMERA_TYPE_FRONT;
            }
            if (cameraIndex == -1 || mCurrentCameraType == CAMERA_TYPE_BACK) {
                // 切换到前置
                cameraIndex = FindBackCamera();
                mCurrentCameraType = CAMERA_TYPE_BACK;
            }
            if (Build.VERSION.SDK_INT >= 9 && cameraIndex != -1) {
                mCamera = Camera.open(cameraIndex);
            } else {
                mCamera = Camera.open();
            }
            setCameraDisplayOrientation(cameraIndex, mCamera);
            findBestOutVideoSize();
        } catch (Exception e) {
            mCurrentCameraType = CAMERA_TYPE_FAIL;
            e.printStackTrace();
            releaseCamera();
            return false;
        }
        return true;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private int FindFrontCamera() {
        if (Build.VERSION.SDK_INT >= 9) {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras(); // get cameras number
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            }
        }
        return -1;
    }

    public void setOutputRatio(float ratio) {
        this.outputRatio = ratio;
    }

    public void setCameraDisplayOrientation(int cameraId, Camera camera) {
        if (Build.VERSION.SDK_INT <= 8) {
            return;
        }
        Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = 0;
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);


    }

    private void findBestOutVideoSize() {
        if (mCamera != null && mCamera.getParameters() != null) {
            List<Camera.Size> list = mCamera.getParameters().getSupportedVideoSizes();
            if (list != null) {
                float currentRatio = 100;       //设一个比较大的值
                for (int i = 0; i < list.size(); i++) {
                    float tempRatio = list.get(i).width * 1.0f / list.get(i).height;
                    if (Math.abs(outputRatio - tempRatio) < Math.abs(outputRatio - currentRatio)) {
                        currentRatio = list.get(i).width * 1.0f / list.get(i).height;
                        outPutWidth = list.get(i).height;
                        outPutHeight = list.get(i).width;
                    }

                }
            }
        }

    }

    private int FindBackCamera() {
        if (Build.VERSION.SDK_INT >= 9) {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras(); // get cameras number
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            }
        }
        return -1;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }
}
