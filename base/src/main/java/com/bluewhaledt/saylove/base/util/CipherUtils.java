package com.bluewhaledt.saylove.base.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密工具类
 */
public class CipherUtils {
    private static final String MD5 = "MD5";
    private static final String DES = "DES";
    private static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};

    /**
     * Base64编码
     *
     * @param src 待编码的字符串
     * @return 加密结果
     */
    public static String base64Encode(String src) {
        return Base64.encodeToString(src.getBytes(), Base64.DEFAULT);
    }

    /**
     * Base64解码
     *
     * @param src 待解码的字符串
     * @return 解密结果
     */
    public static String base64Decode(String src) {
        return new String(Base64.decode(src, Base64.DEFAULT));
    }

    /**
     * 对文件全文生成MD5摘要
     *
     * @param path 文件路径
     * @return MD5摘要码
     */
    public static String md5Abstract(String path) {

        File file = new File(path);
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            // 32位加密
            byte[] b = md.digest();
            return byteToHexString(b);

            // 16位加密
            // return buf.toString().substring(8, 24);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取字符串MD5
     *
     * @param src 源字符串
     * @return
     */
    public static String md5(String src) {
        try {
            byte[] strTemp = src.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance(MD5);
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            return byteToHexString(md);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     *
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     */

    private static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = HEX_DIGITS[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        return Base64.encodeToString(bt, Base64.DEFAULT);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     */
    public static String decrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte[] buf;
        byte[] bt = null;
        try {
            buf = Base64.decode(data, Base64.DEFAULT);
            bt = decrypt(buf, key.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bt == null) {
            return null;
        }
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data 源数据
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}
