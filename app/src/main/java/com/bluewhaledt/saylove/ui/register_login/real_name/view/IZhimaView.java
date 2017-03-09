package com.bluewhaledt.saylove.ui.register_login.real_name.view;

import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;

/**
 * 描述：view
 * 作者：shiming_li
 * 时间：2016/12/5 10:42
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name.presenter
 * 项目名：SayLove
 */
public interface IZhimaView {
    void getZhimaData(ZAResponse<ZhimaEntity> data);
    void goToRealNameGoMoneyActivity(String errorCode, String errorMsg);
    void ZhimaCallBackFail(String errorCode,String errorMsg);
    void ZhimaCallBackSuccess();
    void getVerifyProduct(VerifyProduct verifyProduct);
    void getVerifyTips(VerifyTipsEntity entity);
}
