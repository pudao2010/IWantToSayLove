package com.bluewhaledt.saylove.ui.visitor.presenter;

import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.ui.visitor.model.MyVisitListModel;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class MyVisitListPresenter extends LinearBasePresenter<Visitor> {

    public MyVisitListPresenter(ILinearBaseView<Visitor> actionView) {
        super(actionView);
    }

    @Override
    public IBaseMode<Visitor> createModel() {
        return new MyVisitListModel();
    }
}
