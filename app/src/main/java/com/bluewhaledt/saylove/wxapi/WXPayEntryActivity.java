package com.bluewhaledt.saylove.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bluewhaledt.saylove.ui.pay.order.WeChatOrder;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, WeChatOrder.WX_APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("errorCode", "onPayFinish, errCode = " + req.openId + "transaction:" + req.transaction);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("errorCode", "onPayFinish, errCode = " + resp.errCode + "transaction:" + resp.transaction);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(WeChatOrder.PAY_WX_RESULT);
            if (resp.transaction != null && resp.transaction
                    .equals(WeChatOrder.WE_CHAT_PAY)) {//购买成功后根据不同的业务进行处理
                intent.putExtra(WeChatOrder.PAY_WX_BUSINESS_KEY,resp.transaction);
            }
            //{"data":{"initPrice":"109","iosProductId":"com.bluewhaledt.saylove.idcardcertificate","price":"6.0","productId":103},"errorCode":"","errorMessage":"","isError":false}
            intent.putExtra(WeChatOrder.PAY_WX_RESULT_CODE, resp.errCode);
            sendBroadcast(intent);
            finish();
        }
    }

}