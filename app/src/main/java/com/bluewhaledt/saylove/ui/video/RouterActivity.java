package com.bluewhaledt.saylove.ui.video;

import android.os.Bundle;

import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;


/**
 * 中转activity  需要通过activity 中转的fragment 再此跳转
 * Created by rade.chan on 2016/12/19.
 */

public class RouterActivity extends BaseActivity {

    public static final int REDIRECT_TO_THIRD_PARTY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        int target = getIntent().getIntExtra(IntentConstants.REDIRECT_TARGET, 0);
        Bundle bundle = getIntent().getBundleExtra(IntentConstants.REDIRECT_BUNDLE);
        switch (target) {
            case REDIRECT_TO_THIRD_PARTY:
                if (bundle != null) {
                    startFragment(ThirdPartyFragment.class, bundle);
                }
                break;
            default:
                finish();
        }
    }
}
