package com.bluewhaledt.saylove.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.Settings;

import com.bluewhaledt.saylove.base.util.TimeUtils;

import java.io.File;

/**
 * Created by zhenai-liliyan on 16/12/17.
 */

public class PermissionUtil {

    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 此处处理在系统版本在6.0以下时第三方手机各自实现了权限控制时造成第一次录制时出现问题
     */
    public static void judgeIsNeedOpenAudioPermissionDialog(Context context) {
        boolean isFirst = PreferenceUtil.getBoolean(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.OPEN_RECORD_AUDIO_PERMISSION, true);
        if (isFirst) {
            final MediaRecorder mMediaRecorder = new MediaRecorder();
            try {
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                String filePath = context.getCacheDir() + File.separator + TimeUtils.getCurrentTime() + ".amr";
                mMediaRecorder.setOutputFile(filePath);
                mMediaRecorder.setMaxDuration(1 * 1000);
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                mMediaRecorder.stop();
                mMediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.OPEN_RECORD_AUDIO_PERMISSION, false);
        }
    }

}
