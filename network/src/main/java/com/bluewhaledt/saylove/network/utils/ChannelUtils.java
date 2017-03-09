package com.bluewhaledt.saylove.network.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 获取渠道信息
 */
public final class ChannelUtils {

    private static final String DEFAULT_CHANNEL = "0_0";

    private static final int SHORT_LENGTH = 2;
    private static final byte[] MAGIC = new byte[]{0x21, 0x5a, 0x58, 0x4b, 0x21}; //!ZXK!

    /**
     * 获取渠道全称
     *
     * @return channel
     */
    public static String getChannel(Context context) {
        return getChannelInternal(context);
    }

    /**
     * 获取主渠道
     *
     * @return mani channel
     */
    public static String getMainChannel(Context context) {
        String channel = getChannelInternal(context);
        String[] arr = channel.split("_");
        return arr[0];
    }

    /**
     * 获取子渠道
     *
     * @return sub channel
     */
    public static String getSubChannel(Context context) {
        String channel = getChannelInternal(context);
        String[] arr = channel.split("_");
        if (arr.length > 1) {
            return arr[1];
        }
        return "0";
    }

    private static String getChannelInternal(Context context) {
        String market = "";
        try {
            market = readChannel(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(market) ? DEFAULT_CHANNEL : market;
    }

    private static boolean isMagicMatched(byte[] buffer) {
        if (buffer.length != MAGIC.length) {
            return false;
        }
        for (int i = 0; i < MAGIC.length; ++i) {
            if (buffer[i] != MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    private static short readShort(DataInput input) throws IOException {
        byte[] buf = new byte[SHORT_LENGTH];
        input.readFully(buf);
        ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort(0);
    }

    private static String readChannel(Context context) throws IOException {
        String channel = "";
        File apk = new File(context.getPackageCodePath());
        RandomAccessFile raf = new RandomAccessFile(apk, "r");
        long index = raf.length();
        byte[] buffer = new byte[MAGIC.length];
        index -= MAGIC.length;
        // read magic bytes
        raf.seek(index);
        raf.readFully(buffer);
        // if magic bytes matched
        if (isMagicMatched(buffer)) {
            index -= SHORT_LENGTH;
            raf.seek(index);
            // read content length field
            int length = readShort(raf);
            if (length > 0) {
                index -= length;
                raf.seek(index);
                // read content bytes
                byte[] bytesComment = new byte[length];
                raf.readFully(bytesComment);
                channel = new String(bytesComment, "UTF-8");
            }
        }
        raf.close();
        return channel;
    }
}
