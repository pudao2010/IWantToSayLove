package com.bluewhaledt.saylove.ui.message;

import android.content.Intent;
import android.os.Bundle;

import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.im.IMUtil;

/**
 * Created by zhenai-liliyan on 16/12/10.
 */

public class ChatDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IMUtil.login();
        showTitleBar(false);
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        setContentView(fragment);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        removeAllFragment();
    }
}
