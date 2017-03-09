package com.bluewhaledt.saylove.ui.info;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.info.adapter.PhotoAndVideoAdapter;
import com.bluewhaledt.saylove.ui.info.base.BaseInfoFragment;
import com.bluewhaledt.saylove.ui.info.constant.ComeFrom;
import com.bluewhaledt.saylove.ui.info.entity.ItemInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;
import com.bluewhaledt.saylove.ui.info.entity.PhotoListEntity;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyItemInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_detail.ThirdPartyVerifyFragment;
import com.bluewhaledt.saylove.ui.info.widget.PhotoAndVideoLayout;
import com.bluewhaledt.saylove.ui.info.widget.ThirdPartyInfoItemLayout;
import com.bluewhaledt.saylove.ui.info.widget.VerifyWrapperLayout;
import com.bluewhaledt.saylove.ui.message.ChatDetailActivity;
import com.bluewhaledt.saylove.ui.message.ChatDetailFragment;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.ExpandTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rade.chan on 2016/11/30.
 */

public class ThirdPartyFragment extends BaseInfoFragment {

    private PhotoAndVideoLayout mPhotoAndVideoLayout;
    private VerifyWrapperLayout mVerifyWrapperLayout;
    private ThirdPartyInfoItemLayout mThirdPartyInfoItemLayout;
    private ThirdPartyInfoItemLayout mRequireInfoItemLayout;

    private ExpandTextView mIntroduceTv;

    private List<PhotoEntity> photoAndVideoList = new ArrayList<>();
    private List<PhotoEntity> photoList = new ArrayList<>();
    private List<VerifyItemInfoEntity> verifyList = new ArrayList<>();
    private View praiseView;
    private View chatView;
    private TextView praiseTextView;
    private UserInfoEntity userInfoEntity;
    private ImageView mIvAnim;
    private boolean mIsDoAnim = false;

    private List<VideoIndexEntity> videoList = new ArrayList<>();
    private boolean photoHasResponse = false;
    private boolean videoHasResponse = false;

    private ImageView mIsMvp;
    private int comeFrom;

    private int currentPhotoPage;
    private int currentVideoPage;

    private boolean photoHasNext = true;
    private boolean videoHasNext = true;
    private boolean fromTourist;
    private float[] mCurrentPosition = new float[2];
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mTouristFlag) {
            EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TOURIST_DETAIL_THRID_PAGE);
        } else {
            EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.THIRD_PARTY_PAGE);
        }
    }

    @Override
    protected int getContentRes() {
        return R.layout.fragment_third_party_layout;
    }

    @Override
    protected void initView() {

        super.initView();
        mIntroduceTv = find(R.id.personal_introduce_view);
        mPhotoAndVideoLayout = find(R.id.photo_and_video_widget_layout);
        mVerifyWrapperLayout = find(R.id.verify_layout);
        mThirdPartyInfoItemLayout = find(R.id.basic_info_layout);
        mRequireInfoItemLayout = find(R.id.require_info_layout);
        praiseView = find(R.id.praise_view);
        chatView = find(R.id.chat_view);
        praiseTextView = find(R.id.praise_text_view);
        mIvAnim = find(R.id.fragment_third_party_iv_anim);
        mIsMvp = find(R.id.item_info_header_isvip);
        titleRightIconView.setImageResource(R.mipmap.icon_report);
        overLayoutRightIconView.setImageResource(R.mipmap.icon_report);
        titleLeftIconView.setVisibility(View.VISIBLE);
        overLayLeftIconView.setVisibility(View.VISIBLE);

        TextView chatTV = find(R.id.tv_chat);
        Drawable drawable = getResources().getDrawable(R.drawable.recommend_hot_selector);
        drawable.setBounds(0, 0, DensityUtils.dp2px(getActivity(), 25), DensityUtils.dp2px(getActivity(), 25));
        chatTV.setCompoundDrawables(drawable, null, null, null);
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_like);
        drawable1.setBounds(0, 0, DensityUtils.dp2px(getActivity(), 25), DensityUtils.dp2px(getActivity(), 25));
        praiseTextView.setCompoundDrawables(drawable1, null, null, null);

        if (comeFrom == ComeFrom.COME_FROM_CHAT_DETAIL_PAGE_ME) {
            find(R.id.bottom_layout).setVisibility(View.GONE);
            overLayoutRightIconView.setVisibility(View.GONE);
            titleRightIconView.setVisibility(View.GONE);

        }
        initViewData();
    }

    @Override
    protected void init() {
        super.init();
        Bundle arguments = getArguments();
        if (arguments != null) {
            comeFrom = arguments.getInt(ComeFrom.COME_FROM, ComeFrom.DEFAULT);
            fromTourist = arguments.getBoolean(IntentConstants.FROM_TOURIST_FRAGMENT);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleLeftIconView.setOnClickListener(this);
        overLayLeftIconView.setOnClickListener(this);
        titleRightIconView.setOnClickListener(this);
        mVerifyWrapperLayout.setOnClickListener(this);
        praiseView.setOnClickListener(this);
        chatView.setOnClickListener(this);
        avatarView.setOnClickListener(this);
        mPhotoAndVideoLayout.addItemListener(new PhotoAndVideoAdapter.OnPhotoAndVideoLayoutListener() {
            @Override
            public void onUploadClick(int uploadType) {

            }

            @Override
            public void onItemClick(int uploadType, int position) {
                PhotoEntity photoEntity = photoAndVideoList.get(position);
                if (!fromTourist) {
                    if (photoEntity.type == PhotoEntity.TYPE_VIDEO) {
                        EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_VIDEO);
                        VideoIndexEntity entity = videoList.get(position);
                        ActivityRedirectUtil.gotoAvPlayActivity(getActivity(), entity);

                    } else {
                        EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_PHOTO);
                        List<PhotoEntity> tempList = new ArrayList<>();
                        tempList.addAll(photoList);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentConstants.PHOTOS, (Serializable) tempList);
                        bundle.putInt(IntentConstants.POS, position - videoList.size());
                        startFragment(PhotoPreviewFragment.class, bundle);
                    }
                } else {
                    if (photoEntity.type == PhotoEntity.TYPE_VIDEO) {       //仕明加统计
                        EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TouristDetailThridPage.TOURIST_DETAIL_THRID_PAGE_VIDEO_BTN);
                    }else{
                        EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TouristDetailThridPage.TOURIST_DETAIL_THRID_PAGE_PHOTO_BTN);
                    }
                    DialogUtil.showGoRegister(getActivity());
                }
            }
        });

        mPhotoAndVideoLayout.addOnLoadMoreListener(new PhotoAndVideoLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int uploadType) {
                mPhotoAndVideoLayout.setLoading(true);
                if (videoHasNext) {
                    currentVideoPage += 1;
                    loadVideo(currentVideoPage);
                }
                if (photoHasNext) {
                    currentPhotoPage += 1;
                    loadPhotoInfo(currentPhotoPage);
                }
                if (videoHasNext || photoHasNext) {
                    showProgress();
                }
            }
        });
    }

    /**
     * 举报
     */
    private void showReportLayout() {
        new BaseDialog(getActivity())
                .setBtnPanelView(R.layout.dialog_user_report_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        dialogInterface.dismiss();
                        switch (btnId) {
                            case R.id.report_user_view:
                                presenter.getReportData(getActivity(), Long.parseLong(userId), 1);
                                break;
                        }
                    }
                }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();

    }

    @Override
    public void onClick(View view) {
        //仕明添加 start
        if (view.getId() == R.id.left_icon_view || view.getId() == R.id.overlay_left_icon_view) {

        } else if (mTouristFlag) {
            switch (view.getId()){
                case R.id.verify_layout:
                    EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TouristDetailThridPage.TOURIST_DETAIL_THRID_PAGE_CARD_ID_DETAIL_BTN);
                    break;
                case R.id.praise_view:
                    EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TouristDetailThridPage.TOURIST_DETAIL_THRID_PAGE_HOT_BTN);
                    break;
                case R.id.chat_view:
                    EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_THRID_PAGE, ResourceKey.TouristDetailThridPage.TOURIST_DETAIL_THRID_PAGE_CHAT_BTN);
                    break;
            }
            DialogUtil.showGoRegister(getActivity());
            return;
        }
        //end
        switch (view.getId()) {
            case R.id.verify_layout:
                EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_VERIFY_INFO);
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentConstants.VERIFY_INFO_ID, (Serializable) verifyList);
                startFragment(ThirdPartyVerifyFragment.class, bundle);
                break;
            case R.id.left_icon_view:
            case R.id.overlay_left_icon_view:
                finish();
                break;
            case R.id.overlay_right_icon_view:
            case R.id.right_icon_view:
                EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_REPORT_BTN);
                showReportLayout();
                break;
            case R.id.praise_view:
                EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_HEART_BTN);
                if (userInfoEntity != null) {
                    if (userInfoEntity.praise) {
                        userInfoEntity.praise = false;
                        setIsPraise(false);
                        cancelPraise(userInfoEntity.likeId, new BaseSubscriber(new ZASubscriberListener<ZAResponse>() {
                            @Override
                            public void onSuccess(ZAResponse response) {
                                LikeEntity likeEntity = new LikeEntity();
                                likeEntity.likeId = -1;
                                EventBus.getDefault().post(likeEntity);
                                ToastUtils.toast(getActivity(), getResources().getString(R.string.fragment_third_party_cancel_praise_text));
                            }

                            @Override
                            public void onFail(String errorCode, String errorMsg) {
                                super.onFail(errorCode, errorMsg);
                                userInfoEntity.praise = true;
                                setIsPraise(true);
                                ToastUtils.toast(getActivity(), errorMsg);

                            }
                        }));
                    } else {
                        setIsPraise(true);
                        userInfoEntity.praise = true;
                        addPraise(new BaseSubscriber(new ZASubscriberListener<ZAResponse<LikeEntity>>() {
                            @Override
                            public void onSuccess(ZAResponse<LikeEntity> response) {
                                LikeEntity data = response.data;
                                userInfoEntity.likeId = data.likeId;
                                EventBus.getDefault().post(data);
                                mIvAnim.setVisibility(View.VISIBLE);
                                doAnimation(mIvAnim);
                                ToastUtils.toast(getActivity(), getResources().getString(R.string.fragment_third_party_praise_success));
                            }

                            @Override
                            public void onFail(String errorCode, String errorMsg) {
                                userInfoEntity.praise = false;
                                setIsPraise(false);
                                //非会员的进入购买页
                                if (errorCode.equals("-56008")) {
                                    ToastUtils.toast(getActivity(), getResources().getString(R.string.fragment_third_party_praise_error_text));
                                } else if (errorCode.equals("-56006")) {
                                    DialogUtil.showOpenGoPay(getActivity());
                                } else {
                                    ToastUtils.toast(getActivity(), errorMsg);
                                }
                            }
                        }));
                    }
                }
                break;
            case R.id.chat_view:
                EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_CHAT_BTN);
                if (userInfoEntity != null) {
                    Bundle args = new Bundle();
                    ExtendedData info = new ExtendedData();
                    info.otherSessionId = userInfoEntity.imSessionId;
                    info.otherAvatar = userInfoEntity.avatarUrl;
                    info.otherMemberId = Long.parseLong(userId);
                    info.nikeName = userInfoEntity.nickName;
                    boolean isVip = AccountManager.getInstance().getZaAccount().isVip;
                    if (isVip) {
                        info.isLockMessage = false;
                    } else {
                        info.isLockMessage = true;
                    }
                    args.putSerializable(ChatDetailFragment.EXTENDED_DATA, info);
                    if (getActivity() instanceof ChatDetailActivity) {//如果当前页面是从聊天详情页面点击头像进来的，则点击时直接结束当前fragment即可
                        finish();
                    } else {
                        Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                        intent.putExtras(args);
                        startActivity(intent);
                    }
//                    startFragmentBySingleTask(ChatDetailFragment.class,args);

                }

                break;
            case R.id.avatar_view:

                EventStatistics.recordLog(ResourceKey.THIRD_PARTY_PAGE, ResourceKey.ThirdPartyPage.THIRD_PARTY_PAGE_AVATAR);

                if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity.avatarUrl)) {
                    List<PhotoEntity> tempList = new ArrayList<>();
                    PhotoEntity entity = new PhotoEntity();
                    entity.photoUrl = userInfoEntity.avatarUrl;
                    tempList.add(entity);
                    Bundle avatarBundle = new Bundle();
                    avatarBundle.putSerializable(IntentConstants.PHOTOS, (Serializable) tempList);
                    avatarBundle.putInt(IntentConstants.POS, 0);
                    startFragment(PhotoPreviewFragment.class, avatarBundle);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DebugUtils.d("shiming", "onKeyDown");
        return super.onKeyDown(keyCode, event);
    }

    private void setIsPraise(boolean isPraise) {
        int padding = getResources().getDimensionPixelSize(R.dimen.third_party_text_drawable_padding);
        if (isPraise) {
            praiseTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_like_red, 0, 0, 0);
            praiseTextView.setCompoundDrawablePadding(padding);
        } else {
            praiseTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_like, 0, 0, 0);
            praiseTextView.setCompoundDrawablePadding(padding);
        }
    }


    @Override
    public void showRequireInfo(RequireInfoEntity entity) {
        super.showRequireInfo(entity);
        List<ItemInfoEntity> infoList = new ArrayList<>();
        boolean showRequireContainerView = false;
        //年龄
        if (!entity.getAge().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.age);
            item.content = entity.getAge();
            infoList.add(item);
            showRequireContainerView = true;
        }

        //身高
        if (!entity.getHeight().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.height);
            item.content = entity.getHeight();
            infoList.add(item);
            showRequireContainerView = true;
        }

        //年收入
        if (!entity.getSalary().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.income);
            item.content = entity.getSalary();
            infoList.add(item);
            showRequireContainerView = true;
        }

        //工作地
        if (!entity.getWorkCity().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.work_city);
            item.content = entity.getWorkCity();
            infoList.add(item);
            showRequireContainerView = true;
        }

        //籍贯
        if (!entity.getNativePlace().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.native_place);
            item.content = entity.getNativePlace();
            infoList.add(item);
            showRequireContainerView = true;
        }


        //婚姻状况
        if (!entity.getMarryState().equals(entity.getDefaultText())) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.marital_status);
            item.content = entity.getMarryState();
            infoList.add(item);
            showRequireContainerView = true;
        }
        if (showRequireContainerView) {
            mRequireInfoItemLayout.addItem(infoList, getString(R.string.item_require_info_title));
        } else {
            mRequireInfoItemLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void showVerifyInfo(VerifyInfoEntity verifyInfoEntity) {
        verifyList.clear();
        //身份认证
        VerifyItemInfoEntity idCarVerify = new VerifyItemInfoEntity();
        idCarVerify.isVerifySuccess = verifyInfoEntity.verifiedIdentity;
        if (verifyInfoEntity.verifiedIdentity) {
            idCarVerify.iconRes = R.mipmap.icon_me_id_golden;
            idCarVerify.showText = verifyInfoEntity.name;
            idCarVerify.showDetailText = verifyInfoEntity.idcardNo;
            idCarVerify.verifyTime = verifyInfoEntity.identityTime;
        } else {
            idCarVerify.iconRes = R.mipmap.icon_me_id_gray;
            idCarVerify.showText = getString(R.string.not_verify);
        }

        idCarVerify.verifyTitle = getString(R.string.item_id_card_verify);
        verifyList.add(idCarVerify);


        //学历认证
        VerifyItemInfoEntity educationVerify = new VerifyItemInfoEntity();
        educationVerify.isVerifySuccess = verifyInfoEntity.verifiedDegree;
        educationVerify.verifyStatus = verifyInfoEntity.degreeVerifyStatus;
        if (verifyInfoEntity.verifiedDegree) {
            educationVerify.iconRes = R.mipmap.icon_me_study_golden;
            educationVerify.showText = verifyInfoEntity.academy;
            educationVerify.showDetailText = "通过学位证认证";
            educationVerify.verifyTime = verifyInfoEntity.degreeTime;
        } else {
            educationVerify.iconRes = R.mipmap.icon_me_study_gray;
            if (!TextUtils.isEmpty(verifyInfoEntity.degreeVerifyStatusDesc)) {
                educationVerify.showText = verifyInfoEntity.degreeVerifyStatusDesc;
            } else {
                educationVerify.showText = getString(R.string.not_verify);
            }
        }
        educationVerify.verifyTitle = getString(R.string.item_education_verify);
        verifyList.add(educationVerify);


        //购车认证
        VerifyItemInfoEntity carVerify = new VerifyItemInfoEntity();
        carVerify.isVerifySuccess = verifyInfoEntity.verifiedCar;
        carVerify.verifyStatus = verifyInfoEntity.carVerifyStatus;
        if (verifyInfoEntity.verifiedCar) {
            carVerify.iconRes = R.mipmap.icon_me_car_golden;
            carVerify.showText = verifyInfoEntity.brand;
            carVerify.showDetailText = "通过行驶证认证";
            carVerify.verifyTime = verifyInfoEntity.carTime;
        } else {
            carVerify.iconRes = R.mipmap.icon_me_car_gray;
            if (!TextUtils.isEmpty(verifyInfoEntity.carVerifyStatusDesc)) {
                carVerify.showText = verifyInfoEntity.carVerifyStatusDesc;
            } else {
                carVerify.showText = getString(R.string.not_verify);
            }

        }
        carVerify.verifyTitle = getString(R.string.item_car_verify);
        verifyList.add(carVerify);


        //购房认证

        VerifyItemInfoEntity houseVerify = new VerifyItemInfoEntity();
        houseVerify.isVerifySuccess = verifyInfoEntity.verifiedHouse;
        houseVerify.verifyStatus = verifyInfoEntity.houseVerifyStatus;
        if (verifyInfoEntity.verifiedHouse) {
            houseVerify.iconRes = R.mipmap.icon_me_house_golden;
            houseVerify.showText = verifyInfoEntity.house;
            houseVerify.showDetailText = "通过购房合同认证";
            houseVerify.verifyTime = verifyInfoEntity.houseTime;
        } else {
            houseVerify.iconRes = R.mipmap.icon_me_house_gray;
            if (!TextUtils.isEmpty(verifyInfoEntity.houseVerifyStatusDesc)) {
                houseVerify.showText = verifyInfoEntity.houseVerifyStatusDesc;
            } else {
                houseVerify.showText = getString(R.string.not_verify);
            }
        }
        houseVerify.verifyTitle = getString(R.string.item_house_verify);
        verifyList.add(houseVerify);


        mVerifyWrapperLayout.addVerifyInfo(verifyList, VerifyWrapperLayout.TYPE_VERIFY);
    }


    @Override
    public void showVideoInfo(int page, VideoIndexListEntity videoIndexListEntity) {
        if (page == Constants.FIRST_PAGE) {
            videoList.clear();
        }
        currentVideoPage = page;
        if (videoIndexListEntity.list != null) {
            videoList.addAll(videoIndexListEntity.list);
        }
        videoHasResponse = true;
        videoHasNext = videoIndexListEntity.hasNext;
        showList();
    }

    @Override
    public void loadMoreVideoFail(int page) {
        currentVideoPage = page;
        videoHasResponse = true;
        showList();
    }

    @Override
    public void showBasicInfo(UserInfoEntity entity) {
        super.showBasicInfo(entity);

        userInfoEntity = entity;

        setIsPraise(entity.praise);
        boolean vip = entity.vip;
        if (vip) {
            mIsMvp.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(entity.introduce)) {
            mIntroduceTv.setText(entity.introduce);
        } else {
            find(R.id.personal_introduce_container).setVisibility(View.GONE);
        }

        idTextView.setText(entity.age + " " + entity.workcityDesc);
        List<ItemInfoEntity> infoList = new ArrayList<>();

        //身高
        if (!TextUtils.isEmpty(entity.heightDesc) && entity.height != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.height);
            item.content = entity.heightDesc;
            infoList.add(item);
        }


        //职业
        if (!TextUtils.isEmpty(entity.occupationDesc) && entity.occupation != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.job);
            item.content = entity.occupationDesc;
            infoList.add(item);
        }

        //年收入
        if (!TextUtils.isEmpty(entity.salaryDesc) && entity.salary != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.income);
            item.content = entity.salaryDesc;
            infoList.add(item);
        }


        //婚姻状况
        if (!TextUtils.isEmpty(entity.marryDesc) && entity.marryState != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.marital_status);
            item.content = entity.marryDesc;
            infoList.add(item);
        }

        //籍贯
        if (!TextUtils.isEmpty(entity.hometownDesc) && entity.hometown != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.native_place);
            item.content = entity.hometownDesc;
            infoList.add(item);
        }

        //是否有孩子
        if (!TextUtils.isEmpty(entity.haveChildDesc) && entity.haveChild != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.children);
            item.content = entity.haveChildDesc;
            infoList.add(item);
        }

        //是否喝酒
        if (!TextUtils.isEmpty(entity.drinkDesc) && entity.drinkState != -1) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.drinking);
            item.content = entity.drinkDesc;
            infoList.add(item);
        }

        //是否吸烟
        if (!TextUtils.isEmpty(entity.getSmoking()) && entity.smokeState > Constants.INFO_VALUE_INVALID) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.smoking);
            item.content = entity.getSmoking();
            infoList.add(item);
        }
        //用户id
        if (!TextUtils.isEmpty(userId)) {
            ItemInfoEntity item = new ItemInfoEntity();
            item.title = getString(R.string.user_id);
            item.content = userId;
            infoList.add(item);
        }


        mThirdPartyInfoItemLayout.addItem(infoList, getString(R.string.item_basic_info));
    }

    @Override
    public void handleError(String errorCode, String errorMsg) {
        if (errorCode.equals("-00014")) {
            ToastUtils.toast(getActivity(), errorMsg);
            finish();
        }
    }


    @Override
    public void showPhotoList(int page, PhotoListEntity photoListEntity) {
        if (page == Constants.FIRST_PAGE) {
            photoList.clear();
        }
        currentPhotoPage = page;
        if (photoListEntity.list != null) {
            photoList.addAll(photoListEntity.list);
        }
        photoHasResponse = true;
        photoHasNext = photoListEntity.hasNext;
        showList();

    }

    @Override
    public void loadMorePhotoFail(int page) {
        currentPhotoPage = page;
        photoHasResponse = true;
        showList();
    }

    private void showList() {
        if (!photoHasNext) {
            photoHasResponse = true;
        }
        if (!videoHasNext) {
            videoHasResponse = true;
        }
        if (photoHasResponse && videoHasResponse) {
            dismissProgress();
            mPhotoAndVideoLayout.setLoading(false);
            mPhotoAndVideoLayout.setCanLoadMore(photoHasNext || videoHasNext);

            photoAndVideoList.clear();
            photoAndVideoList.addAll(photoList);

            for (int i = videoList.size() - 1; i >= 0; i--) {
                PhotoEntity entity = new PhotoEntity();
                entity.type = PhotoEntity.TYPE_VIDEO;
                entity.photoUrl = videoList.get(i).cover;
                entity.photoId = videoList.get(i).videoId;
                entity.shiedId = videoList.get(i).shield;
                photoAndVideoList.add(0, entity);
            }

            if (photoAndVideoList.size() == 0) {
                mPhotoAndVideoLayout.setVisibility(View.GONE);
                return;
            }

            mPhotoAndVideoLayout.setVisibility(View.VISIBLE);
            mPhotoAndVideoLayout.addItem(photoAndVideoList, null);
        }
    }

    public void doAnimation(final View v){
        if (mIsDoAnim){
            return;
        }
        float startX =0;
        float startY = 0;
        float toX = -100 ;
        float toY = -200;
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        //前两个参数的意思是我们的曲线弯曲的方向
        // Random random=new Random();
        // int i = random.nextInt((int) (startX + toY));//让曲线更加的平滑，用随机数去管理
        path.quadTo(startX, (startX + toY)/2, toX, toY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                v.setTranslationX(mCurrentPosition[0]);
                v.setTranslationY(mCurrentPosition[1]);
            }
        });
        ObjectAnimator rotation = ObjectAnimator.ofFloat(v, "rotation", 0.0f, -50f);
        rotation.setDuration(1000);
        rotation.setRepeatCount(0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.1f);
        alpha.setDuration(1000);
        PropertyValuesHolder xpvh = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.4f);
        PropertyValuesHolder ypvh = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.4f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v, xpvh, ypvh);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(0);
        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimator).with(alpha).with(rotation).with(objectAnimator);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIsDoAnim = true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.INVISIBLE);
                mIsDoAnim =false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

}
