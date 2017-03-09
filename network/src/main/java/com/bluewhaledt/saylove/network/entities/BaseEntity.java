package com.bluewhaledt.saylove.network.entities;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 所有数据实体的基类.<br>
 * <p>
 * 如果子类要根据JsonObject实例化一个对象, 则需要实现 {@link /entity.Builder}接口,
 * 并提供getBuilder()方法返回实体构造器.
 * </p>
 *
 * @author DengZhaoyong
 * @version 1.0.0
 * @date 2012-9-13
 */
public abstract class BaseEntity implements Serializable {

    /**
     * 是否是同一个对象的判断条件
     */
    public abstract String[] uniqueKey();

    @Override
    public boolean equals(Object o) {
        String[] uniqueKey = uniqueKey();
        if (uniqueKey != null && uniqueKey.length > 0) {
            BaseEntity user = (BaseEntity) o;
            String[] modelKey = user.uniqueKey();
            for (int i = 0; i < uniqueKey.length; i++) {
                String unique = uniqueKey[i];
                if (TextUtils.isEmpty(unique) || !unique.equals(modelKey[i])) {
                    return super.equals(o);
                }
            }
            return true;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        String[] uniqueKey = uniqueKey();
        if (uniqueKey != null && uniqueKey.length > 0) {
            int result = 0;
            for (int i = 0; i < uniqueKey.length; i++) {
                String unique = uniqueKey[i];
                if (!TextUtils.isEmpty(unique)) {
                    result = 31 * result + unique.hashCode();
                }
            }
            if (result == 0) {
                return super.hashCode();
            } else {
                return result;
            }
        } else {
            return super.hashCode();
        }
    }

}
