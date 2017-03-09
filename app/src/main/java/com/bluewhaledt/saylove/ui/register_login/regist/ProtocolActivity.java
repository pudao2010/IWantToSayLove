package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;
import android.webkit.WebView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.regist
 * @文件名: ProtocolActivity
 * @创建者: YanChao
 * @创建时间: 2016/12/15 15:29
 * @描述： 服务协议界面
 */
public class ProtocolActivity extends BaseActivity{

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_layout);
        assignViews();
    }

    private void assignViews() {
        showTitleBar(true);
        setTitle(R.string.saylove_protocol);
        mWebView = (WebView) findViewById(R.id.wv_prorocol);
        //允许JavaScript执行
        mWebView.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        mWebView.loadUrl("file:///android_asset/ServicePolicy.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
//        mWebView.addJavascriptInterface(new Contact(), "contact");
    }


}
