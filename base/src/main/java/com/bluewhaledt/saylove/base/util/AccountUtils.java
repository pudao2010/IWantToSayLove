package com.bluewhaledt.saylove.base.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账号工具类
 */
public class AccountUtils {

    /**
     * 是否风险账号
     *
     * @param account                账号
     * @param password               密码
     * @param requiredPasswordLength 所要求的密码长度
     * @return 是否是风险账号
     */
    public static boolean isRiskAccount(String account, String password, int
            requiredPasswordLength) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return true;
        }
        // 13位且纯数字 的密码
        if (isPureNum(password)) {
            return true;
        }
        // 13位且纯字母 的密码
        if (isPureLetter(password)) {
            return true;
        }
        //小于指定长度
        if (isTooShort(password, requiredPasswordLength)) {
            return true;
        }
        // 与账号相同
        if (isSameAsAccount(account, password)) {
            return true;
        }
        return false;
    }

    /**
     * 是否纯数字
     *
     * @param password 密码
     * @return 是否是纯数字
     */
    public static boolean isPureNum(String password) {
        Pattern numPattern = Pattern.compile("[0-9]*");
        Matcher isNum = numPattern.matcher(password);
        if (isNum.matches() && password.length() < 13) {
            return true;
        }
        return false;
    }

    /**
     * 是否纯字母
     *
     * @param password 密码
     * @return 是否是纯字母
     */
    public static boolean isPureLetter(String password) {
        Pattern letterPattern = Pattern.compile("[a-zA-Z]*");
        Matcher isLetter = letterPattern.matcher(password);
        if (isLetter.matches() && password.length() < 13) {
            return true;
        }
        return false;
    }

    /**
     * 是否纯字符
     *
     * @param password 密码
     * @return 是否是纯字符
     */
    public static boolean isPureCharacter(String password) {
        Pattern characterPattern = Pattern.compile("[^\\w]*");
        Matcher isCharacter = characterPattern.matcher(password);
        if (isCharacter.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 是否小于指定长度
     *
     * @param password 密码
     * @return 是否是小于指定长度
     */
    public static boolean isTooShort(String password, int requiredLength) {
        if (password == null) {
            return true;
        }
        return password.length() < requiredLength;
    }




    /**
     * 是否与账号名称相同
     *
     * @param account  账号
     * @param password 密码
     * @return 是否与账号名称相同
     */
    public static boolean isSameAsAccount(String account, String password) {
        if (StringUtils.isEmpty(account)) {
            if (StringUtils.isEmpty(password)) {
                return true;
            } else {
                return password.trim().equals(account);
            }
        } else {
            if (StringUtils.isEmpty(password)) {
                return account.trim().equals(password);
            } else {
                return account.trim().equals(password.trim());
            }
        }
    }

    /**
     * 检测号码是否有效
     * @param phone
     * @return
     */
    public static  boolean checkPhoneIsValid(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() == 11 && (phone.startsWith("13") || phone.startsWith("15")
                    || phone.startsWith("17") || phone.startsWith("18")||phone.startsWith("14"))) {
                return true;
            }
        }
        return false;
    }

    public static  boolean checkVerifyCodeIsValid(String verifyCode){
        if (!TextUtils.isEmpty(verifyCode)){
            if (verifyCode.length() == 4){
                return true;
            }
        }
        return false;
    }
}
