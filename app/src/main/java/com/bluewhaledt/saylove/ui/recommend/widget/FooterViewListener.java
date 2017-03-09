package com.bluewhaledt.saylove.ui.recommend.widget;

/**
 * 描述：脚步刷新出来的数据有有问题的时候，想展示的布局接口
 * 作者：shiming_li
 * 时间：2016/11/25 17:34
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public interface FooterViewListener {

    /**
     * 网络不好的时候想要展示的UI
     */
    void onNetChange(boolean isAvailable);

    /**
     * 正常的loading的View
     */
    void onLoadingMore();


    /**
     * 数据太少的loading的View
     */
    void showNoView();

    /**
     * 没有更多数据
     */
    void onNoMore(CharSequence message);

    /**
     *  错误时展示的View
     */
    void onError(CharSequence message);
}
