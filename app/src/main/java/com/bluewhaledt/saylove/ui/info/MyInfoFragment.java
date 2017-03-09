package com.bluewhaledt.saylove.ui.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.ui.info.adapter.PhotoAndVideoAdapter;
import com.bluewhaledt.saylove.ui.info.base.BaseInfoFragment;
import com.bluewhaledt.saylove.ui.info.base.ISourceFrom;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;
import com.bluewhaledt.saylove.ui.info.entity.PhotoListEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_modify.BasicInfoModifyFragment;
import com.bluewhaledt.saylove.ui.info.info_modify.ModifyEditTextFragment;
import com.bluewhaledt.saylove.ui.info.info_modify.ModifyVerifyFragment;
import com.bluewhaledt.saylove.ui.info.info_modify.RequireInfoModifyFragment;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.ui.info.widget.PhotoAndVideoLayout;
import com.bluewhaledt.saylove.ui.pay.PayActivity;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.register_login.real_name.RealNameActivity;
import com.bluewhaledt.saylove.ui.setting.SettingFragment;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.ExpandTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rade.chan on 2016/11/28.
 */

public class MyInfoFragment extends BaseInfoFragment implements ISourceFrom,
        PhotoAndVideoAdapter.OnPhotoAndVideoLayoutListener, PhotoAndVideoLayout.OnLoadMoreListener {

    private PhotoAndVideoLayout photoWidgetLayout;
    private PhotoAndVideoLayout videoWidgetLayout;

    private String avatarUrl;
    private ItemLayout basicInfoEditItemView;
    private ItemLayout requireOtherEditItemView;
    private ItemLayout introduceMyselfItemView;
    private ItemLayout idCardVerifyItemView;
    private ItemLayout educationVerifyItemView;
    private ItemLayout carVerifyItemView;
    private ItemLayout houseVerifyItemView;
    private ItemLayout toPayMemberItemView;

    private List<PhotoEntity> photoList = new ArrayList<>();
    private List<VideoIndexEntity> videoList = new ArrayList<>();


    private View avatarVerifyStatusView;
    private TextView avatarVerifyStatusTv;

    private View introduceLayout;
    private ExpandTextView introduceTextView;
    private VerifyInfoEntity verifyInfoEntity;

    private int photoMaxSize = 15;
    private int currentPhotoPage;
    private int currentVideoPage;
    private TextView avatarNotPassTips;
    private NestedScrollView scrollView;
    private boolean isAvatarNotPass = false;


    @Override
    protected void initView() {
        super.initView();
        showTitleLeftIcon(false);
        scrollView = find(R.id.scroll_view);
        photoWidgetLayout = find(R.id.photo_widget_layout);
        videoWidgetLayout = find(R.id.video_widget_layout);
        basicInfoEditItemView = find(R.id.item_basic_info_view);
        requireOtherEditItemView = find(R.id.item_other_require_view);
        introduceMyselfItemView = find(R.id.item_introduce_view);
        idCardVerifyItemView = find(R.id.item_id_card_view);
        educationVerifyItemView = find(R.id.item_education_view);
        carVerifyItemView = find(R.id.item_car_view);
        houseVerifyItemView = find(R.id.item_house_view);
        toPayMemberItemView = find(R.id.item_to_pay_member_view);
        avatarVerifyStatusView = find(R.id.avatar_not_pass_view);
        avatarNotPassTips = find(R.id.tv_avatar_not_pass_tips);
        avatarVerifyStatusTv = find(R.id.verify_status_tv);
        introduceLayout = find(R.id.introduce_detail_view);
        introduceTextView = find(R.id.introduce_text_view);
        titleRightIconView.setImageResource(R.drawable.icon_setting_selector);
        overLayoutRightIconView.setImageResource(R.drawable.icon_setting_selector);

    }


    @Override
    public void getPhotoSuccess(String path) {
        avatarUrl = path;
        uploadPicture(avatarUrl, getCurrentSelectPicType());
    }

    @Override
    public void getPhotoFail() {
        ToastUtils.toast(getActivity(), getString(R.string.get_photo_fail));
    }

    @Override
    public void uploadFail(int type, String errorMsg) {
        super.uploadFail(type, errorMsg);
    }

    @Override
    public void uploadSuccess(int type, String srcPath, String cosPath) {
        super.uploadSuccess(type, srcPath, cosPath);
        if (type == PHOTO_TYPE_AVATAR) {
            loadBasicInfo();
        } else if (type == PHOTO_TYPE_COMMON) {
            loadPhotoInfo(Constants.FIRST_PAGE);
        }
    }

    @Override
    protected void initViewData() {
        super.initViewData();
    }

    @Override
    protected int getContentRes() {
        return R.layout.fragment_my_info_layout;
    }

    @Override
    protected void init() {
        super.init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter(BroadcastActions.PAY_VIP_SUCCESS));

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastActions.PAY_VIP_SUCCESS)) {
                loadBasicInfo();
            }
        }
    };


    @Override
    protected void initListener() {
        super.initListener();
        avatarView.setOnClickListener(this);
        basicInfoEditItemView.setOnClickListener(this);
        requireOtherEditItemView.setOnClickListener(this);
        introduceMyselfItemView.setOnClickListener(this);
        idCardVerifyItemView.setOnClickListener(this);
        educationVerifyItemView.setOnClickListener(this);
        carVerifyItemView.setOnClickListener(this);
        houseVerifyItemView.setOnClickListener(this);
        toPayMemberItemView.setOnClickListener(this);
        titleRightIconView.setOnClickListener(this);
        photoWidgetLayout.addItemListener(this);
        videoWidgetLayout.addItemListener(this);
        photoWidgetLayout.addOnLoadMoreListener(this);
        videoWidgetLayout.addOnLoadMoreListener(this);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                avatarNotPassTips.setVisibility(View.GONE);
                return false;
            }
        });
    }

    private void showPhotoDialog(final int type) {
        new BaseDialog(getActivity())
                .setBtnPanelView(R.layout.dialog_album_select_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        dialogInterface.dismiss();
                        switch (btnId) {
                            case R.id.take_photo_view:
                                setCurrentSelectPicType(type);
                                goCameraPhoto(System.currentTimeMillis() + Constants.SAVE_PIC_FORMAT);
                                break;
                            case R.id.from_album_view:
                                setCurrentSelectPicType(type);
                                goAlbum();
                                break;
                        }
                    }
                }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar_view:
                if (isAvatarNotPass){
                    EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MyInfoPage.MY_INFO_PAGE_RE_UPLOAD_AVATAR);
                }else {
                    EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MyInfoPage.MY_INFO_PAGE_AVATAR_CLICK);
                }
                showPhotoDialog(PHOTO_TYPE_AVATAR);
                break;
            case R.id.item_basic_info_view:
                startFragment(BasicInfoModifyFragment.class, null);
                break;
            case R.id.item_other_require_view:
                startFragment(RequireInfoModifyFragment.class, null);
                break;
            case R.id.item_introduce_view:
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.MODIFY_FROM, FROM_INTRODUCE_MYSELF);
                bundle.putString(IntentConstants.CURRENT_VALUE, introduceTextView.getText());
                startFragment(ModifyEditTextFragment.class, bundle);
                break;
            case R.id.item_id_card_view:
                if (verifyInfoEntity != null && !verifyInfoEntity.verifiedIdentity) {
                    Intent intent = new Intent(getActivity(), RealNameActivity.class);
                    intent.putExtra(Constants.IS_FROM_INFOPAGE, true);
                    startActivity(intent);
                }

                break;
            case R.id.item_education_view:
//                if (verifyInfoEntityResponse != null && !verifyInfoEntityResponse.verifiedDegree) {
//                    Bundle educationVerify = new Bundle();
//                    educationVerify.putInt(IntentConstants.MODIFY_FROM, FROM_EDUCATION_VERIFY);
//                    startFragment(ModifyVerifyFragment.class, educationVerify);
//                }
                Bundle educationVerify = new Bundle();
                educationVerify.putInt(IntentConstants.MODIFY_FROM, FROM_EDUCATION_VERIFY);
                startFragment(ModifyVerifyFragment.class, educationVerify);

                break;
            case R.id.item_car_view:
//                if (verifyInfoEntityResponse != null && !verifyInfoEntityResponse.verifiedCar) {
//                    Bundle carVerify = new Bundle();
//                    carVerify.putInt(IntentConstants.MODIFY_FROM, FROM_CAR_VERIFY);
//                    startFragment(ModifyVerifyFragment.class, carVerify);
//                }
                Bundle carVerify = new Bundle();
                carVerify.putInt(IntentConstants.MODIFY_FROM, FROM_CAR_VERIFY);
                startFragment(ModifyVerifyFragment.class, carVerify);

                break;
            case R.id.item_house_view:
//                if (verifyInfoEntityResponse != null && !verifyInfoEntityResponse.verifiedHouse) {
//                    Bundle houseVerify = new Bundle();
//                    houseVerify.putInt(IntentConstants.MODIFY_FROM, FROM_HOUSE_VERIFY);
//                    startFragment(ModifyVerifyFragment.class, houseVerify);
//                }
                Bundle houseVerify = new Bundle();
                houseVerify.putInt(IntentConstants.MODIFY_FROM, FROM_HOUSE_VERIFY);
                startFragment(ModifyVerifyFragment.class, houseVerify);


                break;
            case R.id.item_to_pay_member_view:
                EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MyInfoPage.MY_INFO_PAGE_PAY_CLICK);
                Intent intent = new Intent(getActivity(), PayActivity.class);
                intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.DEFAULT);
                startActivity(intent);
                break;
            case R.id.overlay_right_icon_view:
            case R.id.right_icon_view:
                startFragment(SettingFragment.class, null);
                break;

        }
    }


    @Override
    public void onUploadClick(int uploadType) {
        if (uploadType == PhotoAndVideoLayout.UPLOAD_TYPE_VIDEO) {      //上传视频
            ActivityRedirectUtil.gotoRecordActivity(getActivity(), mPermissionHelper);
            EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MyInfoPage.MY_INFO_PAGE_VIDEO_BTN);
        } else {
            if (photoList.size() < photoMaxSize) {
                showPhotoDialog(PHOTO_TYPE_COMMON);
            } else {
                ToastUtils.toast(getActivity(), getString(R.string.upload_pic_tips));
            }
            EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MyInfoPage.MY_INFO_PAGE_UPLOAD_AVATAR);
        }
    }


    @Override
    public void onItemClick(int uploadType, int position) {
        if (uploadType == PhotoAndVideoLayout.UPLOAD_TYPE_VIDEO) {      //视频浏览
            VideoIndexEntity entity = videoList.get(position);
            ActivityRedirectUtil.gotoAvPlayActivity(getActivity(), entity);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentConstants.PHOTOS, (Serializable) photoList);
            bundle.putInt(IntentConstants.POS, position);
            bundle.putBoolean(IntentConstants.SHOW_DELETE_BTN, true);
            startFragment(PhotoPreviewFragment.class, bundle);
        }
    }

    @Subscribe
    public void onEvent(RefreshInfoEvent event) {
        if (event != null) {
            if (event.getLoadType() == RefreshInfoEvent.LOAD_ALL_INFO) {
                loadUserData();
            } else if (event.getLoadType() == RefreshInfoEvent.LOAD_BASIC_INFO) {
                loadBasicInfo();
            } else if (event.getLoadType() == RefreshInfoEvent.LOAD_PHOTOS) {
                loadPhotoInfo(Constants.FIRST_PAGE);
            } else if (event.getLoadType() == RefreshInfoEvent.LOAD_VERIFY) {
                loadVerifyInfo();
            } else if (event.getLoadType() == RefreshInfoEvent.LOAD_VIDEO) {
                loadVideo(Constants.FIRST_PAGE);
            }

        }
    }

    @Override
    public void showBasicInfo(UserInfoEntity entity) {
        super.showBasicInfo(entity);
        if (entity.verifyAvatarStatus == 3) {
            avatarVerifyStatusView.setVisibility(View.VISIBLE);
            isAvatarNotPass = true;
            avatarVerifyStatusTv.setText(getString(R.string.verify_not_pass));
            avatarNotPassTips.setVisibility(View.VISIBLE);

        } else {
            isAvatarNotPass = false;
            avatarVerifyStatusView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(entity.introduce)) {
            introduceLayout.setVisibility(View.VISIBLE);
            introduceTextView.setText(entity.introduce);
        } else {
            introduceTextView.setText("");
            introduceLayout.setVisibility(View.GONE);
        }

        if (entity.vip) {
            toPayMemberItemView.setLeftText(getString(R.string.my_service));
        } else {
            toPayMemberItemView.setLeftText(getString(R.string.item_pay_member));
        }

        String finishedRate = entity.finishedRate;
        TextView rightTv = (TextView) basicInfoEditItemView.findViewById(R.id.right_text_view);
        TextPaint tp = rightTv.getPaint();
        tp.setFakeBoldText(true);
        basicInfoEditItemView.setRightText(getString(R.string.info_complete_rate) + finishedRate);
        if (finishedRate.contains("100")) {
            basicInfoEditItemView.setRightTextColor(R.color.third_party_item_right_color);
        } else {
            basicInfoEditItemView.setRightTextColor(R.color.color_saylove);
        }

    }

    @Override
    public void handleError(String errorCode, String errorMsg) {

    }

    @Override
    public void showPhotoList(int page, PhotoListEntity photoListEntity) {
        dismissProgress();
        currentPhotoPage = page;
        photoWidgetLayout.setCanLoadMore(photoListEntity.hasNext);
        photoWidgetLayout.setLoading(false);
        if (photoListEntity.count > 0) {
            photoMaxSize = photoListEntity.count;
        }
        if (page == Constants.FIRST_PAGE) {
            photoList.clear();
        }
        if (photoListEntity.list != null) {
            photoList.addAll(photoListEntity.list);
        }
        photoWidgetLayout.addItem(photoList, "照片(" + photoList.size() + ")");

    }

    /**
     * 加载更多失败
     *
     * @param page
     */
    @Override
    public void loadMorePhotoFail(int page) {
        dismissProgress();
        currentPhotoPage = page;
        photoWidgetLayout.setLoading(false);
    }

    @Override
    public void showVerifyInfo(VerifyInfoEntity verifyInfoEntity) {

        this.verifyInfoEntity = verifyInfoEntity;
        //身份认证
        if (verifyInfoEntity.verifiedIdentity) {
            idCardVerifyItemView.setRightText(getString(R.string.has_verify));
            idCardVerifyItemView.setRightTextColor(R.color.black);
            idCardVerifyItemView.setLeftDrawable(R.mipmap.icon_me_id_golden);
            idCardVerifyItemView.setRightIconVisibility(View.GONE);
            idCardVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
        } else {
            idCardVerifyItemView.setRightText(getString(R.string.click_to_verify));
            idCardVerifyItemView.setLeftDrawable(R.mipmap.icon_me_id_gray);
            idCardVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
        }
        if (verifyInfoEntity.me) {
            if (verifyInfoEntity.degreeVerifyStatus == 2) {
                educationVerifyItemView.setRightText(getString(R.string.has_verify));
                educationVerifyItemView.setLeftDrawable(R.mipmap.icon_me_study_golden);
                educationVerifyItemView.setRightTextColor(R.color.black);
                educationVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                educationVerifyItemView.setLeftDrawable(R.mipmap.icon_me_study_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.degreeVerifyStatusDesc)) {
                    educationVerifyItemView.setRightText(verifyInfoEntity.degreeVerifyStatusDesc);
                } else {
                    educationVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                educationVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }
            //购车认证
            if (verifyInfoEntity.carVerifyStatus == 2) {
                carVerifyItemView.setRightText(getString(R.string.has_verify));
                carVerifyItemView.setLeftDrawable(R.mipmap.icon_me_car_golden);
                carVerifyItemView.setRightTextColor(R.color.black);
                carVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                carVerifyItemView.setLeftDrawable(R.mipmap.icon_me_car_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.carVerifyStatusDesc)) {
                    carVerifyItemView.setRightText(verifyInfoEntity.carVerifyStatusDesc);
                } else {
                    carVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                carVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }

            //购房认证
            if (verifyInfoEntity.houseVerifyStatus == 2) {
                houseVerifyItemView.setRightText(getString(R.string.has_verify));
                houseVerifyItemView.setLeftDrawable(R.mipmap.icon_me_house_golden);
                houseVerifyItemView.setRightTextColor(R.color.black);
                houseVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                houseVerifyItemView.setLeftDrawable(R.mipmap.icon_me_house_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.houseVerifyStatusDesc)) {
                    houseVerifyItemView.setRightText(verifyInfoEntity.houseVerifyStatusDesc);
                } else {
                    houseVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                houseVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }

        } else {

            //学历认证
            if (verifyInfoEntity.verifiedDegree) {
                educationVerifyItemView.setRightText(getString(R.string.has_verify));
                educationVerifyItemView.setLeftDrawable(R.mipmap.icon_me_study_golden);
                educationVerifyItemView.setRightTextColor(R.color.black);
                educationVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                educationVerifyItemView.setLeftDrawable(R.mipmap.icon_me_study_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.degreeVerifyStatusDesc)) {
                    educationVerifyItemView.setRightText(verifyInfoEntity.degreeVerifyStatusDesc);
                } else {
                    educationVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                educationVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }

            //购车认证
            if (verifyInfoEntity.verifiedCar) {
                carVerifyItemView.setRightText(getString(R.string.has_verify));
                carVerifyItemView.setLeftDrawable(R.mipmap.icon_me_car_golden);
                carVerifyItemView.setRightTextColor(R.color.black);
                carVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                carVerifyItemView.setLeftDrawable(R.mipmap.icon_me_car_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.carVerifyStatusDesc)) {
                    carVerifyItemView.setRightText(verifyInfoEntity.carVerifyStatusDesc);
                } else {
                    carVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                carVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }


            //购房认证
            if (verifyInfoEntity.verifiedHouse) {
                houseVerifyItemView.setRightText(getString(R.string.has_verify));
                houseVerifyItemView.setLeftDrawable(R.mipmap.icon_me_house_golden);
                houseVerifyItemView.setRightTextColor(R.color.black);
                houseVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right_without_arrow));
            } else {
                houseVerifyItemView.setLeftDrawable(R.mipmap.icon_me_house_gray);
                if (!TextUtils.isEmpty(verifyInfoEntity.houseVerifyStatusDesc)) {
                    houseVerifyItemView.setRightText(verifyInfoEntity.houseVerifyStatusDesc);
                } else {
                    houseVerifyItemView.setRightText(getString(R.string.click_to_verify));
                }
                houseVerifyItemView.setRightMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_right));
            }
        }

    }

    @Override
    public void showVideoInfo(int page, VideoIndexListEntity videoIndexListEntity) {
        dismissProgress();
        currentVideoPage = page;
        videoWidgetLayout.setCanLoadMore(videoIndexListEntity.hasNext);
        videoWidgetLayout.setLoading(false);

        if (page == Constants.FIRST_PAGE) {
            videoList.clear();
        }
        List<PhotoEntity> tempList = new ArrayList<>();
        if (videoIndexListEntity.list != null) {
            videoList.addAll(videoIndexListEntity.list);
            for (int i = 0; i < videoList.size(); i++) {
                PhotoEntity entity = new PhotoEntity();
                entity.type = PhotoEntity.TYPE_VIDEO;
                entity.photoUrl = videoList.get(i).cover;
                entity.photoId = videoList.get(i).videoId;
                entity.shiedId = videoList.get(i).shield;
                tempList.add(entity);
            }
        }
        videoWidgetLayout.addItem(tempList, "SayLove问答小视频(" + videoIndexListEntity.count + ")");

    }

    @Override
    public void loadMoreVideoFail(int page) {
        dismissProgress();
        currentVideoPage = page;
        videoWidgetLayout.setLoading(false);
    }

    @Override
    public void onDestroyFragment() {
        super.onDestroyFragment();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    /**
     * 加载更多
     *
     * @param uploadType
     */
    @Override
    public void onLoadMore(int uploadType) {
        if (uploadType == PhotoAndVideoLayout.UPLOAD_TYPE_VIDEO) {
            videoWidgetLayout.setLoading(true);
            currentVideoPage += 1;
            loadVideo(currentVideoPage);
        } else {
            photoWidgetLayout.setLoading(true);
            currentPhotoPage += 1;
            loadPhotoInfo(currentPhotoPage);
        }
        showProgress();
    }


}
