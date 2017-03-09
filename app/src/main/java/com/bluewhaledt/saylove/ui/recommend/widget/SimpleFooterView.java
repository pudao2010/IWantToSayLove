package com.bluewhaledt.saylove.ui.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;


/**
 * 描述：上啦刷新的样式
 * 作者：shiming_li
 * 时间：2016/11/25 17:34
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public class SimpleFooterView extends BaseFooterView{

    private TextView mText;

    private ProgressBar progressBar;
    private View mView;

    public SimpleFooterView(Context context) {
        this(context, null);
    }

    public SimpleFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_view, this);
        progressBar = (ProgressBar) mView.findViewById(R.id.footer_view_progressbar);
        mText = (TextView) mView.findViewById(R.id.footer_view_tv);
    }


    /**
     * 加载更多的时候，我们试图先展示的是一个进度条的
     * 然后才是文字
     */
    @Override
    public void onLoadingMore() {
        mView.setVisibility(VISIBLE);
        progressBar.setVisibility(VISIBLE);
        mText.setVisibility(GONE);
//        showNoView();
    }

    @Override
    public void showNoView() {
        progressBar.setVisibility(GONE);
        mText.setVisibility(GONE);
////        DebugUtils.d("shiming","gon");
//        mView.setVisibility(GONE);
    }

    //和上面相反的效果图
    public void showText(){
        mView.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        mText.setVisibility(VISIBLE);
    }
//    /**
//     * 数据太少后调用
//     */
//
//    public void showNoView(){
//        progressBar.setVisibility(GONE);
//        mText.setVisibility(GONE);
//    }
    /**************文字自行修改或根据传入的参数动态修改****************/

    @Override
    public void onNoMore(CharSequence message) {
        showText();
//        DebugUtils.d("shiming","message"+message);
        mText.setText(message);
    }

    @Override
    public void onError(CharSequence message) {
        showText();
        mText.setText("啊哦，好像哪里不对劲!");
    }

    @Override
    public void onNetChange(boolean isAvailable) {
        showText();
        mText.setText("网络连接不通畅!");
    }
}
