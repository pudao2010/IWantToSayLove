package com.bluewhaledt.saylove.ui.tourist.view;

import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;

/**
 * 描述：vice
 * 作者：shiming_li
 * 时间：2016/12/12 14:43
 * 包名：com.zhenai.saylove_icon.ui.tourist.view
 * 项目名：SayLove
 */
public interface ITouristView {
    void getTouristDataSuccess(TouristEntity entity);
    void getTouristDataFail();
    void getTouristDataSuccessLoadMore(TouristEntity entity);
    void getTouristDataFail(String errorCode, String errorMsg);
    void onError();
    void onErrorLoadmore();
}
