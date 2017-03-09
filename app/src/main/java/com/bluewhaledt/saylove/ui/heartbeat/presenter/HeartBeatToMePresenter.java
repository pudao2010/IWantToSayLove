package com.bluewhaledt.saylove.ui.heartbeat.presenter;

import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.ui.heartbeat.model.HeartBeatToMeModel;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class HeartBeatToMePresenter extends LinearBasePresenter<HeartBeatItem> {

    public HeartBeatToMePresenter(ILinearBaseView<HeartBeatItem> actionView) {
        super(actionView);
    }

//    @Override
//    public IBaseMode<ResultEntity<HeartBeatItem>> createModel() {
//        return null;
//    }

    @Override
    public IBaseMode<HeartBeatItem> createModel() {
        return new HeartBeatToMeModel();
    }
}
