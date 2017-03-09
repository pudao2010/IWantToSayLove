package com.bluewhaledt.saylove.ui.visitor.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.ui.visitor.service.VisitorService;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class VisitToMeModel implements IBaseMode<Visitor> {

    @Override
    public void getDataList(Context context, int pageIndex, int pageSize, BaseSubscriber<ZAResponse<ResultEntity<Visitor>>> subscriber) {
        VisitorService service = ZARetrofit.getInstance(context).getRetrofit().create(VisitorService.class);
        Observable<ZAResponse<ResultEntity<Visitor>>> observable = service.getVisitMeList(pageIndex,pageSize);
        HttpMethod.toSubscribe(observable,subscriber);
    }



}
