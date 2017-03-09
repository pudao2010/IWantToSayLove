package com.bluewhaledt.saylove.util;

import android.os.Environment;

import com.bluewhaledt.saylove.base.util.FileUtils;

import java.io.File;

/**
 * @author hechunshan
 * @date 2016/11/15
 */
public class FileConfig {
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();

    /**
     * 主目录
     */
    public static final String HOME_FOLDER = ROOT_DIR + File.separator + "saylove";

    /**
     * 图片主目录
     */
    public static final String HOME_PHOTO_FOLDER = HOME_FOLDER + File.separator + ".images";

    /**
     * 视频主目录
     */
    public static final String HOME_VIDEO_FOLDER = HOME_FOLDER + File.separator + ".video";

    /**
     * log主目录
     */
    public static final String HOME_LOG_FOLDER = HOME_FOLDER + File.separator + ".log";

    /**
     * lib主目录
     */
    public static final String HOME_LIBRARY_FOLDER = HOME_FOLDER + File.separator + ".libs";

    /**
     * 存放用户语音
     */
    public static final String VOICE_FILES_FOLDER = HOME_FOLDER + File.separator + "audio" + File.separator;

    public static final String HOME_WELCOME_FILE = HOME_PHOTO_FOLDER + File.separator + "welcome_file.png";

    public static final String HOME_ZHIMA_BANNER_FILE = HOME_PHOTO_FOLDER + File.separator + "zhima_banner_file.png";

    public static final String HOME_SERVICE_INTRODUCTION_BANNER_FILE = HOME_PHOTO_FOLDER + File.separator + "service_introduction_file.png";

    public static void initFolder() {
        FileUtils.createFolder(HOME_FOLDER, false);
        FileUtils.createFolder(HOME_PHOTO_FOLDER, false);
        FileUtils.createFolder(HOME_VIDEO_FOLDER, false);
        FileUtils.createFolder(HOME_LOG_FOLDER, false);
        FileUtils.createFolder(HOME_LIBRARY_FOLDER, false);
//        FileUtils.createFile(VOICE_FILES_FOLDER,false);
        File file = new File(VOICE_FILES_FOLDER);
        if (!file.exists()){
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
    }

}
