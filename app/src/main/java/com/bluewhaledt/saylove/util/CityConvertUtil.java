package com.bluewhaledt.saylove.util;

import android.text.TextUtils;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;

/**
 * Created by rade.chan on 2016/12/20.
 */

public class CityConvertUtil {
    public static String convertName(String item1, String item2) {
        if (!TextUtils.isEmpty(item2)) {
            if (item1.equals(ZhenaiApplication.getContext().getString(R.string.no_limit)) &&
                    item2.equals(ZhenaiApplication.getContext().getString(R.string.no_limit)) ) {
               return ZhenaiApplication.getContext().getString(R.string.no_limit);
            } else if (item2.endsWith("区")) {
                return item1;
            } else if (item1.contains("重庆")) {
                return item1;
            } else {
                return item1 + item2;
            }
        }
        return item1;
    }
}
