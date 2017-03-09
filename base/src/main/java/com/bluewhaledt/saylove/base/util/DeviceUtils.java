package com.bluewhaledt.saylove.base.util;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * 手机设备相关工具类
 */
public class DeviceUtils {

    /**
     * 获取设备序列号
     *需要 {@code Manifest.permission.READ_PHONE_STATE} 权限
     * @param context Context
     * @return 设备序列号
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取CPU信息.
     *
     * @return "CPU核心个数 x CPU频率"
     */
    public static String getCpuInfo() {
        return getCpuCoreCount() + " x " + getCpuFrequency();
    }

    /**
     * 获取CPU核心个数.
     *
     * @return
     */
    private static int getCpuCoreCount() {
        int coreCount = 1;
        try {
            String cpuDiePath = "/sys/devices/system/cpu";
            File dir = new File(cpuDiePath);
            String[] cpuFiles = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return Pattern.matches("cpu\\d{1}", name);
                }
            });
            if (cpuFiles != null && cpuFiles.length > 0) {
                coreCount = cpuFiles.length;
            }
        } catch (Exception e) {
            DebugUtils.error(e.getMessage(), e);
        }
        return coreCount;
    }

    /**
     * 获取CPU频率.
     *
     * @return
     */
    private static String getCpuFrequency() {
        String cpuFreq = "";
        BufferedReader bufferedReader = null;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            cpuFreq = bufferedReader.readLine();
            // convert from Kb to Gb
            float tempFreq = Float.valueOf(cpuFreq.trim());
            cpuFreq = tempFreq / (1000 * 1000) + "Gb";
            return cpuFreq;
        } catch (Exception e) {
            return StringUtils.isEmpty(cpuFreq) ? "N/A" : cpuFreq + "Kb";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得系统总内存大小.
     *
     * @param context
     * @return
     */
    public static String getSystemTotalMemory(Context context) {
        // 系统内存信息文件
        String memInfoFilePath = "/proc/meminfo";
        String firstLine;
        String[] arrayOfString;
        long initialMemory = 0;
        BufferedReader localBufferedReader = null;
        try {
            FileReader localFileReader = new FileReader(memInfoFilePath);
            localBufferedReader = new BufferedReader(
                    localFileReader, 10240);
            // 读取meminfo第一行, 系统总内存大小
            firstLine = localBufferedReader.readLine();
            arrayOfString = firstLine.split("\\s+");
            // 获得系统总内存, 单位是KB, 乘以1024转换为Byte
            initialMemory = Long.valueOf(arrayOfString[1].trim()) * 1024;
        } catch (Exception e) {
            DebugUtils.error(e.getMessage(), e);
            // ignore.
        } finally {
            if (localBufferedReader != null) {
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        // 内存大小规格化, Byte转换为KB或者MB
        return Formatter.formatFileSize(context, initialMemory);
    }

    /**
     * 获取系统当前可用内存.
     *
     * @param context
     * @return
     */
    public static String getSystemAvailMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 内存大小规格化, Byte转换为KB或者MB
        return Formatter.formatFileSize(context, memoryInfo.availMem);
    }
//
//    /**
//     * 读取RAW文件内容
//     *
//     * @param resid
//     * @param encoding
//     * @return
//     */
//    public static String getRawFileContent(int resid, String encoding) {
//        InputStream is = null;
//        Context context = ZhenaiApplication.getContext();
//        try {
//            is = context.getResources().openRawResource(resid);
//        } catch (Exception e) {
//        }
//        if (is != null) {
//            ByteArrayBuffer bab = new ByteArrayBuffer(1024);
//            int read;
//            try {
//                while ((read = is.read()) != -1) {
//                    bab.append(read);
//                }
//                return EncodingUtils.getString(bab.toByteArray(), encoding);
//            } catch (UnsupportedEncodingException e) {
//            } catch (IOException e) {
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//        return "";
//    }
//
//    /**
//     * 获取渠道全称
//     *
//     * @return
//     */
//    public static String getChannel() {
//        return getRawFileContent(R.raw.channel, "utf-8");
//    }
//
//    /**
//     * 获取子渠道
//     *
//     * @return
//     */
//    public static String getSubChannel() {
//        String channel = getRawFileContent(R.raw.channel, "utf-8");
//        String[] arr = channel.split("_");
//        if (arr.length > 1) {
//            return arr[1];
//        }
//        return "";
//    }
//
//    /**
//     * 获取主渠道
//     *
//     * @return
//     */
//    public static String getMainChannel() {
//        String channel = getRawFileContent(R.raw.channel, "utf-8");
//        String[] arr = channel.split("_");
//        return arr[0];
//    }
}
