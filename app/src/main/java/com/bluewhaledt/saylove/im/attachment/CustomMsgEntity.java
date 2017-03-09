package com.bluewhaledt.saylove.im.attachment;


import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.Map;

/**
 * Created by zhenai-liliyan on 16/10/31.
 */

public class CustomMsgEntity extends BaseEntity {

    public int type = CustomMsgType.DEFAULT;

    public String data;

    public Map<String, String> ext;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }

}
