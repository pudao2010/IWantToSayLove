package com.bluewhaledt.saylove.ui.info.info_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.ui.info.base.BaseProfileFragment;
import com.bluewhaledt.saylove.ui.info.entity.VerifyItemInfoEntity;
import com.bluewhaledt.saylove.ui.info.widget.VerifyWrapperLayout;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/1.
 */

public class ThirdPartyVerifyFragment extends BaseProfileFragment {

    private VerifyWrapperLayout mVerifyWrapperLayout;

    @Override
    public int getContentRes() {
        return R.layout.fragment_other_verify_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        if (getArguments() != null) {
            List<VerifyItemInfoEntity> verifyList = (List<VerifyItemInfoEntity>) getArguments().getSerializable(IntentConstants.VERIFY_INFO_ID);
            mVerifyWrapperLayout.addVerifyInfo(verifyList, VerifyWrapperLayout.TYPE_VERIFY_DETAIL);
        }
    }

    private void initView() {
        setTitle(R.string.verify_info);
        mVerifyWrapperLayout = find(R.id.verify_wrapper_layout);
    }
}
