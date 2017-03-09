package com.bluewhaledt.saylove.ui.pay.order;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by rade.chan on 2016/11/14.
 */

public class WeChatOrder extends BaseOrder {

    public static final String WX_APP_ID = "wx69b2184283fbea93";
    public static final String WE_CHAT_PAY = "wet_chat_pay";

    public static final String PAY_WX_RESULT = "com.bluewhaledt.saylove_icon.pay.wx.result.action";
    public static final String PAY_WX_BUSINESS_KEY = "pay_wx_business_key";
    public static final String PAY_WX_RESULT_CODE = "pay_wx_result_code";

    private String payFrom;     //处理需要根据业务进行定义


    public WeChatOrder(Activity context, IBaseSurePayView surePayView, String payFrom) {
        super(context, surePayView);
        this.payFrom = payFrom;
        registerBroadcast(context);
    }

    public WeChatOrder(Activity context, IBaseSurePayView surePayView) {
        this(context, surePayView, WE_CHAT_PAY);
    }

    @Override
    public void dealOrder(final PayOrder order) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                IWXAPI api = WXAPIFactory.createWXAPI(mContext, WX_APP_ID);
                if (!api.isWXAppInstalled()) {
                    mSurePayView.payFailed(mContext.getResources().getString(R.string.wx_uninstalled_tips));
                    ToastUtils.toast(mContext,
                            mContext.getResources().getString(R.string.wx_uninstalled_tips));
                } else if (!api.isWXAppSupportAPI()) {
                    mSurePayView.payFailed(mContext.getResources().getString(R.string.wx_api_unsupport_tips));
                    ToastUtils.toast(mContext,
                            mContext.getResources().getString(R.string.wx_api_unsupport_tips));
                } else if (api.isWXAppInstalled()) {
                    PayReq req = new PayReq();
                    req.appId = order.appid;
                    req.partnerId = order.partnerid;
                    req.prepayId = order.prepayid;
                    req.nonceStr = order.noncestr;
                    req.timeStamp = order.timestamp;
                    req.packageValue = order.packageValue;
                    req.sign = order.sign;
                    req.transaction = payFrom;
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }
        });

    }


    private void registerBroadcast(final Activity activity) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (PAY_WX_RESULT.equals(intent.getAction())) {
                    int code = intent.getIntExtra(PAY_WX_RESULT_CODE, -1);
                    if (code == 0) {
                        ToastUtils.toast(context,
                                context.getResources()
                                        .getString(R.string.sucess_pay));
                        mSurePayView.paySuccess();
                    } else {//微信支付失败，通过请求弹出失败界面
                        mSurePayView.payFailed("支付失败");
                    }

                    String business = intent.getStringExtra(PAY_WX_BUSINESS_KEY);
                    if (!TextUtils.isEmpty(business)) {
                        if (business.equals(WE_CHAT_PAY)) {//根据不同的入口处理不同的购买后逻辑

                        }
                    }

                }
                context.unregisterReceiver(this);
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAY_WX_RESULT);
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
        activity.registerReceiver(broadcastReceiver, filter);
    }
}
