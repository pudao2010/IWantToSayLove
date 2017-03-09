package com.bluewhaledt.saylove.ui.info.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.presenter.UserInfoPresenter;
import com.bluewhaledt.saylove.ui.info.view.IUserInfoView;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.ui.tab.TabPhotoBaseFragment;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

/**
 * Created by rade.chan on 2016/11/30.
 */

public abstract class BaseInfoFragment extends TabPhotoBaseFragment implements IUserInfoView, View.OnClickListener {

    protected ImageView avatarView;
    protected View mContentView;
    protected UserInfoPresenter presenter;

    protected TextView idTextView;
    protected TextView nickNameTextView;

    protected ImageView titleRightIconView;
    protected ImageView titleLeftIconView;
    protected NestedScrollView scrollView;
    protected View overLayLayout;
    protected TextView overLayNickTextView;
    protected ImageView overLayoutRightIconView;
    protected ImageView overLayLeftIconView;

    protected String userId = "";


    private static final int SCROLL_HEIGHT_LIMIT = ZhenaiApplication.getInstance().getScreenHeight() / 3;
    public boolean mTouristFlag;
    private ImageView mIsVip;
    private ItemLayout basicInfoView;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showTitleBar(false);
        if (mContentView == null) {
            mContentView = inflater.inflate(getContentRes(), container, false);
        }
        return mContentView;
    }

    protected abstract int getContentRes();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
        initListener();
    }

    protected void init() {
        if (getArguments() != null) {
            userId = getArguments().getString(IntentConstants.USER_ID, "");
            //仕明添加
            mTouristFlag = getArguments().getBoolean(IntentConstants.FROM_TOURIST_FRAGMENT,false);
        }
    }

    @Override
    public void getPhotoSuccess(String path) {

    }

    @Override
    public void getPhotoFail() {

    }

    protected void initListener() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float alpha = scrollY * 1.0f / SCROLL_HEIGHT_LIMIT;
                overLayLayout.setAlpha(alpha);
                if (alpha > 0) {
                    overLayLayout.setVisibility(View.VISIBLE);
                } else {
                    overLayLayout.setVisibility(View.GONE);
                }
            }
        });
        overLayLayout.setOnClickListener(this);     //空实现 获得焦点，避免点击时，触发底下的view
        overLayoutRightIconView.setOnClickListener(this);

    }

    protected void initViewData() {
        presenter = new UserInfoPresenter(getActivity(), this);
//        loadUserData();
        loadAllData();
    }

    protected void loadUserData() {
        loadBasicInfo();
        loadPhotoInfo(Constants.FIRST_PAGE);
        loadVerifyInfo();
        loadVideo(Constants.FIRST_PAGE);
        if (!TextUtils.isEmpty(userId)) {
            presenter.getRequireInfo(userId);
        }
    }

    private void loadAllData() {
        presenter.loadAllData(userId);
    }

    protected void loadVerifyInfo() {
        presenter.getVerifyInfo(userId);
    }

    protected void loadBasicInfo() {
        presenter.getUserInfo(userId);
    }

    protected void loadPhotoInfo(int page) {
        presenter.getPhotos(page,userId);
    }

    @Override
    public void showRequireInfo(RequireInfoEntity entity) {

    }

    protected void loadVideo(int page) {
        presenter.getVideoInfo(page,userId);
    }

    protected void initView() {
        avatarView = find(R.id.avatar_view);
        titleLeftIconView = find(R.id.left_icon_view);
        titleRightIconView = find(R.id.right_icon_view);
        scrollView = find(R.id.scroll_view);
        overLayLayout = find(R.id.overlay_layout);
        idTextView = find(R.id.id_view);
        mIsVip = find(R.id.item_info_header_isvip);
        nickNameTextView = find(R.id.nick_name_view);
        overLayNickTextView = find(R.id.overlay_nick_name_view);
        overLayoutRightIconView = find(R.id.overlay_right_icon_view);
        overLayLeftIconView = find(R.id.overlay_left_icon_view);

    }

    protected void showTitleLeftIcon(boolean isShow) {
        if (isShow) {
            titleLeftIconView.setVisibility(View.VISIBLE);
        } else {
            titleLeftIconView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onFirstUserVisible() {
        initViewData();
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


    //    public String avatar;
//    public String imAccId;
    public UserInfoEntity userInfoEntity;

    @Override
    public void showBasicInfo(UserInfoEntity entity) {
        int avatarDefault = R.mipmap.default_avatar_feman;
        if (entity.sex == Constants.SEX_MALE) {
            avatarDefault = R.mipmap.default_avatar_man;
        }
        if (entity.vip){
            mIsVip.setVisibility(View.VISIBLE);
        }
//        avatar = entity.avatarUrl;
//        imAccId = entity.imAccId;
        userInfoEntity = entity;

        ImageLoaderFactory
                .getImageLoader()
                .with(getActivity())
                .load(PhotoUrlUtils.format(entity.avatarUrl, PhotoUrlUtils.TYPE_10))
                .circle()
                .placeholder(avatarDefault)
                .into(avatarView);

        idTextView.setText(getString(R.string.id) + ":" + entity.userId);
        if (!TextUtils.isEmpty(entity.nickName)) {
            nickNameTextView.setText(entity.nickName);
            overLayNickTextView.setText(entity.nickName);
        } else {
            nickNameTextView.setText("会员" + entity.userId);
            overLayNickTextView.setText("会员" + entity.userId);
        }
    }

    public void addPraise(BaseSubscriber subscriber) {
        presenter.addPraise(userId, subscriber);
    }

    public void cancelPraise(long likeId, BaseSubscriber subscriber) {
        presenter.cancelPraise(likeId, subscriber);
    }


    @SuppressWarnings("unchecked")
    protected <T extends View> T find(int id) {
        return (T) mContentView.findViewById(id);
    }
}
