package com.bluewhaledt.saylove.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.PageIndex;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.pay.PayActivity;
import com.bluewhaledt.saylove.ui.pay.constant.PayType;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.pay.presenter.BaseSurePayPresenter;
import com.bluewhaledt.saylove.ui.pay.service.PayService;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;
import com.bluewhaledt.saylove.ui.recommend.view.VerifyGuideClickListener;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.HeadPortraitActivity;
import com.bluewhaledt.saylove.ui.register_login.login.LoginActivity;
import com.bluewhaledt.saylove.ui.register_login.real_name.RealNameActivity;
import com.bluewhaledt.saylove.ui.register_login.regist.BaseInfoEditActivity;
import com.bluewhaledt.saylove.ui.register_login.regist.OnRegisterExitListener;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/2.
 */

public class DialogUtil {

    public static void showPurchaseVipDialog(final Context context, int titleId, int tipsId, final int fromPage) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.fragment_msg_dialog_content)
                .setBtnPanelView(R.layout.fragment_msg_dialog_btn_panel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        switch (btnId) {
                            case R.id.btn_join:
                                Intent intent = new Intent(context, PayActivity.class);
                                if (fromPage == PageIndex.MESSAGE_PAGE){
                                    intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_MESSAGE_PAGE_READ_MSG);
                                    EventStatistics.recordLog(ResourceKey.MESSAGE_PAGE, ResourceKey.MessagePage.PURCHASE_VIP_DIALOG_CLICK_OK_BTN);
                                }else if (fromPage == PageIndex.HEART_BEAT_TO_ME_PAGE){
                                    intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_HEART_BEAT_TO_ME_PAGE);
                                    EventStatistics.recordLog(ResourceKey.HEART_BEAT_TO_ME_PAGE, ResourceKey.HeartBeatToMePage.PURCHASE_VIP_DIALOG_CLICK_OK_BTN);
                                }else if (fromPage == PageIndex.VISIT_TO_ME_PAGE){
                                    intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_VISIT_TO_ME_PAGE);
                                    EventStatistics.recordLog(ResourceKey.VISIT_TO_ME_PAGE, ResourceKey.VisitToMePage.PURCHASE_VIP_DIALOG_CLICK_OK_BTN);
                                }else if (fromPage == PageIndex.CHAT_PAGE){
                                    intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_CHAT_PAGE_SEND_MSG);
                                    EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_BTN);
                                }
                                dialogInterface.dismiss();

                                context.startActivity(intent);
                                break;
                        }
                    }
                })
                .setText(R.id.tv_title, titleId)
                .setText(R.id.tv_tips, tipsId)
                .showCloseBtn(true)
                .setCanCancelOutside(false)
                .show();
    }

    public static void showExtended3FreeTimeChatPurchaseVipDialog(Context context,int fromPage) {
        if (!PreferenceUtil.getBoolean(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.EXTENDED_FREE_TIME_FIRST, false)) {
            DialogUtil.showPurchaseVipDialog(context,
                    R.string.fragment_chat_detail_dialog_3free_purchase_title, R.string.fragment_chat_detail_dialog_3free_purchase_tips,fromPage);
            PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.EXTENDED_FREE_TIME_FIRST, true);
        } else {
            DialogUtil.showPurchaseVipDialog(context,
                    R.string.dialog_purchase_vip_text, R.string.dialog_purchase_vip_tips,fromPage);
        }
    }

    public static void showRecordAudioMsgFailDialog(final Context context) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.fragment_chat_detail_record_voice_fail)
                .setDialogSize(145.0f, 145.0f)
                .setWindowBackground(R.color.transparent)
                .setLifeTime(3)
                .show();
    }

    /**
     * 支付方式选择弹窗
     *
     * @param context
     * @param msg     提示内容
     * @param price   购买产品的价格
     */
    public static void showChosePayTypeDialog(final Activity context, final IBaseSurePayView iPayView,
                                              String msg, String price, final int paymentChannel, final int productId) {
        price = "￥" + price;
        new BaseDialog(context)
                .setCustomerContent(R.layout.dialog_chose_pay_type)
                .setText(R.id.tv_msg, msg)
                .setText(R.id.tv_price, price)
                .showCloseBtn(true)
                .setCanCancelOutside(false)
                .setBtnPanelView(R.layout.dialog_chose_pay_type_btn_panel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        BaseSurePayPresenter presenter;
                        switch (i) {
                            case R.id.btn_zhi_fu_bao_pay:
                                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPayPage.IDENTIFICATION_PAY_ZHIHUBAO);
                                presenter = new BaseSurePayPresenter(context, iPayView);
                                presenter.createOrder(PayType.AliPay, paymentChannel, productId);
                                break;
                            case R.id.btn_we_chat_pay:
                                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPayPage.IDENTIFICATION_PAY_WECHAT);
                                presenter = new BaseSurePayPresenter(context, iPayView);
                                presenter.createOrder(PayType.WeChatPay, paymentChannel, productId);
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 认证购买弹窗
     *
     * @param context
     * @param iPayView 回调界面接口，通知支付情况
     */
    public static void  showVerifyPayTypeDialog(final Activity context, final IBaseSurePayView iPayView) {
        PayService service = ZARetrofit.getInstance(context).getRetrofit().create(PayService.class);
        Observable<ZAResponse<VerifyProduct>> observable = service.getVerifyProduct();
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VerifyProduct>>(new ZASubscriberListener<ZAResponse<VerifyProduct>>() {

            @Override
            public void onBegin() {
                super.onBegin();
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).showProgress();
                }
            }

            @Override
            public void onSuccess(ZAResponse<VerifyProduct> response) {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).dismissProgress();
                }
                if (response.data != null) {
                    showChosePayTypeDialog(context, iPayView, context.getResources().getString(R.string.credit_msg), response.data.price, PaymentChannel.DEFAULT, response.data.productId);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).dismissProgress();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).dismissProgress();
                }
            }
        }));
    }


    public static void showAlreadyRegistedDialog(final Context context, final OnRegisterExitListener listener) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.fragment_regist_already_regist)
                .showCloseBtn(false)
                .setBtnPanelView(R.layout.fragment_regist_already_regist_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.btn_already_regist_cancle:
                                dialogInterface.dismiss();
                                break;
                            case R.id.btn_login_rightnow:
                                EventStatistics.recordLog(ResourceKey.PHONE_VERIFY_PAGE, ResourceKey.PhoneVerifyPage.PHONE_VERIFY_LOGIN);
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.putExtra(Constants.IS_FROM_REGIST_PAGE, true);
                                context.startActivity(intent);
                                listener.onExit();
                                break;
                        }
                    }
                }).show();

    }

    public static void showWifiTipsDialog(Context context, final View.OnClickListener sureOnClickListener) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.dialog_wifi_tips_conten_layout)
                .setBtnPanelView(R.layout.dialog_wifi_tips_btn_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.continue_btn:
                                if (sureOnClickListener != null) {
                                    sureOnClickListener.onClick(null);
                                }
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                }).showCloseBtn(false).show();
    }


    public static void showVideoDeleteDialog(Context context, final View.OnClickListener sureOnClickListener) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.dialog_wifi_tips_conten_layout)
                .setBtnPanelView(R.layout.dialog_wifi_tips_btn_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.continue_btn:
                                if (sureOnClickListener != null) {
                                    sureOnClickListener.onClick(null);
                                }
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                }).showCloseBtn(false).show();
    }

    public static void showNetWorkDisableDialog(Context context) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.dialog_network_disable_conten_layout)
                .setBtnPanelView(R.layout.dialog_network_disable_btn_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.sure_btn:
                                dialogInterface.dismiss();
                                break;

                        }
                    }
                }).showCloseBtn(false).show();
    }

    /**
     * 仕明添加,跳转到支付页面购买会员
     */
    public static void showOpenGoPay(final Context context) {
        final BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setCustomerContent(R.layout.fragment_recomend_layout_open_member)
                .setViewOnClickListener(R.id.fragment_open_member_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PayActivity.class);
                        intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_HEART_BEAT);
                        context.startActivity(intent);
                        baseDialog.dismiss();

                    }
                })
                 .showCloseBtn(true)
                .setCanCancelOutside(false)
                .show();
    }

    /**
     * 仕明添加，跳转到 注册的页面
     */
    public static void showGoRegister(final Context context) {
        EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TouristDetailPage.TOURIST_DETAIL_PAGE_RIGEST_DIALOG);
        new BaseDialog(context)
                .setCustomerContent(R.layout.fragment_regist_text_dialog)
                .setDialogSize(270.0f, 150.0f)
                .setBtnPanelView(R.layout.fragment_regist_text_dialog_bottom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.btn_already_regist_cancle:
                                dialogInterface.dismiss();
                                break;
                            case R.id.btn_login_rightnow:
                                EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TouristDetailPage.TOURIST_DETAIL_PAGE_RIGEST_DIALOG_BTN);
                                context.startActivity(new Intent(context, BaseInfoEditActivity.class));
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();
    }
    /**
     * 仕明添加，提醒用户不要退出我们的实名认证
     * */
    public static void backRealName(final Context context, final boolean isFromInfoPage, final boolean isFromRegistPage) {
        EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPage.IDENTIFICATION_BACK_DIALOG);
        new BaseDialog(context)
                .setCustomerContent(R.layout.item_donot_leave_dialog)
//                .setDialogSize(400.0f, 200.0f)
                .setBtnPanelView(R.layout.item_donot_leave_dialog_bottom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.btn_already_regist_cancle:
                                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPage.IDENTIFICATION_GIVEUP);
                                if (isFromInfoPage || isFromRegistPage){
                                    ((RealNameActivity)context).finish();
                                }else {
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    ((RealNameActivity)context).finish();
                                }
                                dialogInterface.dismiss();
                                break;
                            case R.id.btn_login_rightnow:
                                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPage.IDENTIFICATION_CONTINUE_LOCK);
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();
    }
    /**
     * 仕明添加,上传头像的dialog
     */
    public static void gotoUpLoadPic(final Context context, final String nickName) {
        int defaultBitmap=R.mipmap.img_home_dialog_avatar;
        final BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setCustomerContent(R.layout.fragment_recomend_layout_upload_pic).setImageResource(R.id.avatar_view,defaultBitmap)
                .setViewOnClickListener(R.id.fragment_up_load_pic_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, HeadPortraitActivity.class);
                        intent.putExtra(HeadPortraitActivity.NIKE_NAME, nickName);
                        intent.putExtra(Constants.IS_FROM_RECOMMEND_PAGE, true);
                        context.startActivity(intent);
                        baseDialog.dismiss();

                    }
                })
                .showCloseBtn(true)
                .setCanCancelOutside(false)
                .show();
    }

    public static void showVerifyGuideDialog(final Context context, final VerifyGuideClickListener listener){
        final BaseDialog baseDialog = new BaseDialog(context);
        baseDialog
                .setCustomerContent(R.layout.dialog_recommend_verifyguide_content)
                .showCloseBtn(true)
                .setCanCancelOutside(false)
                .setViewOnClickListener(R.id.ll_verify_education, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                baseDialog.dismiss();
                                listener.onVerifyEducationClicked();
                    }
                })
                .setViewOnClickListener(R.id.ll_verify_house, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseDialog.dismiss();
                        listener.onVerifyHouseClicked();
                    }
                })
                .setViewOnClickListener(R.id.ll_verify_car, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseDialog.dismiss();
                        listener.onVerifyCarClicked();
                    }
                })
                .show();
    }

    /**
     *
     * 上传头像的返回拦截
     */
    public static void backHeadPortrait(final Context context, final boolean isFromRealNamePage, final boolean isFromRecommendPage) {
        new BaseDialog(context)
                .setCustomerContent(R.layout.item_donot_leave_dialog_head_portrait)
//                .setDialogSize(400.0f, 200.0f)
                .setBtnPanelView(R.layout.item_donot_leave_dialog_head_portrait_bottom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switch (id) {
                            case R.id.btn_already_regist_cancle_up_load:
                                HeadPortraitActivity contextActivity = (HeadPortraitActivity) context;
                                if (isFromRealNamePage || isFromRecommendPage){
                                }else{
                                    context.startActivity(new Intent(context,LoginActivity.class));
                                }
                                dialogInterface.dismiss();
                                contextActivity.finish();
                                break;
                            case R.id.btn_login_rightnow_up_load:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();

    }
}
