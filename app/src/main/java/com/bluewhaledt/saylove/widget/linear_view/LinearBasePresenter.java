package com.bluewhaledt.saylove.widget.linear_view;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public abstract class LinearBasePresenter<E extends BaseEntity> implements IBasePresent {
    private int pageIndex = 1;

    private int pageSize = 15;

    private IBaseMode<E> iModel;

    private ILinearBaseView<E> iActionView;

    private IBaseView listViewAction;

    public LinearBasePresenter(ILinearBaseView<E> actionView) {
        iActionView = actionView;
        initModel();
    }

    public void setListViewAction(IBaseView listViewAction) {
        this.listViewAction = listViewAction;
    }

    public void initModel() {
        iModel = createModel();
    }

    public abstract IBaseMode<E> createModel();

    public void setModel(IBaseMode<E> model){
        iModel = model;
    }

    public void loadMoreDate(final Context context) {
        if (iModel == null ) return;
        iModel.getDataList(context, pageIndex, pageSize, new BaseSubscriber<ZAResponse<ResultEntity<E>>>(new ZASubscriberListener<ZAResponse<ResultEntity<E>>>() {
            @Override
            public void onSuccess(ZAResponse<ResultEntity<E>> response) {
                if (response.data != null && response.data.list.size() > 0) {
                    pageIndex++;
                    iActionView.loadMoreDate(response.data.list);
                } else {
                    iActionView.emptyData(context.getResources().getString(R.string.no_date));
                }
                listViewAction.onLoadMoreComplete();

            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                listViewAction.onLoadMoreComplete();
                ToastUtils.toast(context, errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                listViewAction.onLoadMoreComplete();
                ToastUtils.toast(context, R.string.no_network_connected);
            }
        }));
    }

    public void loadFirstPageDate(final Context context) {
        if (iModel == null ) return;
        pageIndex = 1;
        iModel.getDataList(context, pageIndex, pageSize, new BaseSubscriber<ZAResponse<ResultEntity<E>>>(new ZASubscriberListener<ZAResponse<ResultEntity<E>>>() {
            @Override
            public void onSuccess(ZAResponse<ResultEntity<E>> response) {
                if (response.data != null && response.data.list.size() > 0) {
                    pageIndex++;
                    iActionView.refreshData(response.data.list);
                } else {
                    iActionView.emptyData(context.getResources().getString(R.string.no_date));
                }
                iActionView.totalDataInfo(response.data);
                listViewAction.onRefreshComplete();
            }


            @Override
            public void onError(Throwable e) {
                listViewAction.onRefreshComplete();
                ToastUtils.toast(context, R.string.no_network_connected);
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                listViewAction.onRefreshComplete();
                ToastUtils.toast(context, errorMsg);
            }
        }));
    }

}



















