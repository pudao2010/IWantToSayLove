package com.bluewhaledt.saylove.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.ui.register_login.login.LoginActivity;
import com.bluewhaledt.saylove.ui.register_login.login.ResetPwdActivity;
import com.bluewhaledt.saylove.util.CacheManager;
import com.bluewhaledt.saylove.util.EventStatistics;

/**
 * Created by rade.chan on 2016/12/2.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener, CacheManager.OnClearCacheListener {

    private ItemLayout modifyPwdItemLayout;
    private ItemLayout clearCacheItemLayout;
    private ItemLayout aboutItemLayout;
    private Button logoutBtn;
    private CacheManager mediaCacheManager;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        mediaCacheManager = CacheManager.getInstance(getActivity());
        clearCacheItemLayout.setRightLayoutVisibility(View.VISIBLE);
        Long totalCacheSize = mediaCacheManager.getTotalSize();
        clearCacheItemLayout.setRightText(FileUtils.formatFileSize(totalCacheSize));
        EventStatistics.recordLog(ResourceKey.SETTING_PAGE,ResourceKey.SETTING_PAGE);
    }


    private void initView(View view) {
        setTitle(R.string.setting);
        modifyPwdItemLayout = (ItemLayout) view.findViewById(R.id.modify_password_item_layout);
        clearCacheItemLayout = (ItemLayout) view.findViewById(R.id.clear_cache_item_layout);
        aboutItemLayout = (ItemLayout) view.findViewById(R.id.about_item_layout);
        logoutBtn = (Button) view.findViewById(R.id.setting_btn_logout);
    }

    private void initListener() {
        modifyPwdItemLayout.setOnClickListener(this);
        clearCacheItemLayout.setOnClickListener(this);
        aboutItemLayout.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_password_item_layout:
                startActivity(new Intent(getActivity(), ResetPwdActivity.class));
                EventStatistics.recordLog(ResourceKey.SETTING_PAGE,ResourceKey.SettingPage.SETTING_PAGE_PASSWORD_MODIFY);
                break;
            case R.id.clear_cache_item_layout:
                if (clearCacheItemLayout.getRightText().equals(getString(R.string.zero))) {
                    ToastUtils.toast(getActivity(), getString(R.string.no_cache));
                }else{
//                    IMUtil.clearMsgDatabase();
                    mediaCacheManager.clearCache(this);
                }
                break;
            case R.id.about_item_layout:
                startFragment(AboutFragment.class, null);
                break;
            case R.id.setting_btn_logout:
                // TODO: 2016/12/15 退出前有没其他操作
                EventStatistics.recordLog(ResourceKey.SETTING_PAGE,ResourceKey.SettingPage.SETTING_PAGE_LOGOUT);
                LogoutPresenter logoutPresenter = new LogoutPresenter(getActivity());
                logoutPresenter.logout(false);
                IMUtil.logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onClearStart() {
        showProgress(R.string.cleaning);
    }

    @Override
    public void onClearError() {
        dismissProgress();
    }

    @Override
    public void onClearSuccess() {
        dismissProgress();
        clearCacheItemLayout.setRightText(getString(R.string.zero));
    }
}
