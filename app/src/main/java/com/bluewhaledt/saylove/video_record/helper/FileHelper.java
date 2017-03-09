package com.bluewhaledt.saylove.video_record.helper;

import com.bluewhaledt.saylove.constant.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by rade.chan on 2016/12/9.
 */

public class FileHelper {

    public static final String VIDEO_PATH = Constants.APP_FOLDER + "/.video/";
    public static final String OUTPUT_TEMP_VIDEO = "temp.mp4";
    public static final String OUTPUT_TEMP_AUDIO = "temp.m4a";
    public static String recordName;


    public static String getTempVideoPath() {
        return VIDEO_PATH + OUTPUT_TEMP_VIDEO;
    }

    public static String getTempAudioPath() {
        return VIDEO_PATH + OUTPUT_TEMP_AUDIO;
    }

    public static String getRecordVideoPath() {
        return VIDEO_PATH + recordName;
    }


    public static void deleteAllFile() {
        File file = new File(VIDEO_PATH);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }


    public static File createTempVideo() {
        checkIsCreate();
        return new File(getTempVideoPath());
    }

    public static File createTempAudio() {
        checkIsCreate();
        return new File(getTempAudioPath());
    }

    public static void deleteTempFile() {
        File tempVideo = getTempVideoFile();
        if (tempVideo.exists()) {
            tempVideo.delete();
        }

        File tempAudio = getTempAudioFile();
        if (tempAudio.exists()) {
            tempAudio.delete();
        }
    }


    public static File createRecordMedia() {
        checkIsCreate();
        recordName = System.currentTimeMillis() + "_video.mp4";
        File file = new File(getRecordVideoPath());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;

    }


    private static void checkIsCreate() {
        File dir = new File(VIDEO_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    public static File getTempVideoFile() {
        return new File(getTempVideoPath());
    }

    public static File getTempAudioFile() {
        return new File(getTempAudioPath());
    }

    public static File getRecordFile() {
        return new File(getRecordVideoPath());
    }
}
