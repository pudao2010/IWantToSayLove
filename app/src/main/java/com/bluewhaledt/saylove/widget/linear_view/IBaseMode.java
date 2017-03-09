package com.bluewhaledt.saylove.widget.linear_view;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public interface IBaseMode<E> {

    void getDataList(Context context, int pageIndex, int pageSize, BaseSubscriber<ZAResponse<ResultEntity<E>>> subscriber);

}
