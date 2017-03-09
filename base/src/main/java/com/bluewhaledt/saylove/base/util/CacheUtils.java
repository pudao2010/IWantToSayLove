package com.bluewhaledt.saylove.base.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * 数据缓存工具类
 */
public class CacheUtils {

    /**
     * 写入本地缓存
     *
     * @param context Context
     * @param key     缓存文件名（未经过MD5加密）
     * @param content 存入的内容
     */
    public static void write(Context context, String key, String content) {
        FileOutputStream fosContent = null;
        try {
            // 存放缓存内容
            fosContent = context.openFileOutput(CipherUtils.md5(key), Context.MODE_PRIVATE);
            fosContent.write(content.getBytes("UTF-8"));
            fosContent.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fosContent != null) {
                    fosContent.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本地读取缓存
     *
     * @param context Context
     * @param key     缓存文件名（未经过MD5加密）
     * @return 缓存数据
     */
    public static String read(Context context, String key) {
        String contentCache = null;
        BufferedReader br = null;
        try {
            File contentFile = getCacheFile(context, key);
            if (!contentFile.exists()) {
                return null;
            }
            br = new BufferedReader(new FileReader(contentFile), 10 * 1024);
            contentCache = br.readLine();
        } catch (Exception e) {
            contentCache = null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentCache;
    }

    /**
     * 删除某项缓存
     *
     * @param context Context
     * @param key     魂村文件名（未经过MD5加密）
     */
    public static void delete(Context context, String key) {
        File cache = getCacheFile(context, key);
        if (cache.exists()) {
            cache.delete();
        }
    }

    /**
     * 清除所有缓存
     *
     * @param context Context
     */
    public static void clear(Context context) {
        File cacheDir = context.getFilesDir();
        File[] caches = cacheDir.listFiles();
        for (File cache : caches) {
            if (cache.exists()) {
                cache.delete();
            }
        }
    }

    /**
     * 获取缓存文件
     *
     * @param context Context
     * @param key     未经过MD5加密的缓存文件名
     * @return 缓存文件File对象
     */
    private static File getCacheFile(Context context, String key) {
        String md5 = CipherUtils.md5(key);
        return new File(context.getFilesDir(), md5 == null ? "" : md5);
    }
}
