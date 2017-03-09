package com.bluewhaledt.saylove.ui.register_login.login;

import android.content.Context;
import android.text.TextUtils;

import com.bluewhaledt.saylove.base.util.AccountUtils;

/**
 * Created by yanchao on 2016/12/9.
 */

public class RegisterAndLoginBasePresenter {
    protected Context mContext;

    public boolean checkPhoneIsValid(String phone) {
        return AccountUtils.checkPhoneIsValid(phone);
    }

    public boolean checkPasswordIsValid(String password) {
        return !AccountUtils.isTooShort(password, 6);
    }

    public boolean checkVerifyCodeIsValid(String VerifuCode){
        return AccountUtils.checkVerifyCodeIsValid(VerifuCode);
    }

    public boolean checkButtonIsEnable(boolean... results) {
        boolean isEnable = true;
        for (boolean result : results) {
            if (!result) {
                isEnable = false;
                break;
            }
        }
        return isEnable;
    }

    public boolean checkTextIsNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

    public boolean checkIsEqualIgnoreCase(String text1, String text2) {
        return checkTextIsNotEmpty(text1) && checkTextIsNotEmpty(text2)
                && text1.trim().equalsIgnoreCase(text2.trim());
    }

    public boolean checkIsEqual(String text1, String text2) {
        return checkTextIsNotEmpty(text1) && checkTextIsNotEmpty(text2)
                && text1.trim().equals(text2.trim());
    }
}
