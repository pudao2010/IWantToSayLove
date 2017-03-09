package com.bluewhaledt.saylove.widget.linear_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public class LinearSwipeRecyclerView<E extends BaseEntity> extends SwipeRecyclerView implements IBaseView,ILinearBaseView<E>{

    private LinearBasePresenter<E> mPresenter;

    private ILinearBaseView<E> receiveDataCallback;

    public LinearSwipeRecyclerView(Context context) {
        this(context, null);
    }

    public LinearSwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearSwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setPresenter(LinearBasePresenter<E> presenter){
        mPresenter = presenter;
        mPresenter.setListViewAction(this);
    }

    private void initView(){

        mPresenter = new LinearBasePresenter<E>(this) {
            @Override
            public IBaseMode<E> createModel() {
                return null;
            }
        };
        mPresenter.setListViewAction(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null){
                    mPresenter.loadFirstPageDate(getContext());
                }
            }

            @Override
            public void onLoadMore() {
                if (mPresenter != null){
                    mPresenter.loadMoreDate(getContext());
                }
            }
        });
    }

    public void setModel(IBaseMode<E> model){
        mPresenter.setModel(model);
    }

    public void setReceiveDataCallback(ILinearBaseView<E> receiveDataCallback) {
        this.receiveDataCallback = receiveDataCallback;
    }

    /**
     * 设置九宫布局
     * @param column 多少列
     */
    public void setGridLayoutManager(int column){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),column);
        setLayoutManager(layoutManager);


    }


    @Override
    public void onRefreshComplete() {
        setRefreshing(false);
    }

    @Override
    public void onLoadMoreComplete() {
        stopLoadingMore();
    }

    @Override
    public void refreshData(ArrayList<E> data) {
        if (receiveDataCallback != null){
            receiveDataCallback.refreshData(data);
        }
    }

    @Override
    public void loadMoreDate(ArrayList<E> data) {
        if (receiveDataCallback != null){
            receiveDataCallback.loadMoreDate(data);
        }
    }

    @Override
    public void emptyData(String msg) {
        if (receiveDataCallback != null){
            receiveDataCallback.emptyData(msg);
        }
    }

    @Override
    public void totalDataInfo(ResultEntity<E> resultEntity) {
        if (receiveDataCallback != null){
            receiveDataCallback.totalDataInfo(resultEntity);
        }
    }
}
