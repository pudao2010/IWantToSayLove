package com.bluewhaledt.saylove.ui.register_login.account;


import android.text.TextUtils;

import com.bluewhaledt.saylove.base.util.ObjectStringConverter;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

/**
 * Created by yan.chao on 2016/11/28.
 */

public class AccountManager {

    private static AccountManager accountManager;
    private ZAAccount zaAccount;

    private AccountManager() {

    }

    public static AccountManager getInstance() {
        if (accountManager == null) {
            synchronized (AccountManager.class) {
                if (accountManager == null) {
                    accountManager = new AccountManager();
                }
            }
        }
        return accountManager;
    }

    public void setAccount(ZAAccount account) {
        this.zaAccount = account;
            String accountStr = ObjectStringConverter.objectToString(account);
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.USER_INFO, accountStr);
    }

    public ZAAccount getZaAccount() {
        if (zaAccount == null) {
            try {
                String accountStr = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.USER_INFO, "");
                if (!TextUtils.isEmpty(accountStr)) {
                    zaAccount = (ZAAccount) ObjectStringConverter.stringToObject(accountStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
                zaAccount = new ZAAccount();
            }
        }
        return zaAccount;
    }
}
