package com.bluewhaledt.saylove.ui.register_login.real_name;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.MainActivity;
import com.bluewhaledt.saylove.ui.pay.entity.PayCategories;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.HeadPortraitActivity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.presenter.ZhimaPreSenter;
import com.bluewhaledt.saylove.ui.register_login.real_name.view.IZhimaView;
import com.bluewhaledt.saylove.ui.register_login.real_name.widget.SimpleTooltip;
import com.bluewhaledt.saylove.ui.register_login.real_name.zhima.CreditAuthHelper;
import com.bluewhaledt.saylove.ui.register_login.regist.LoginHelperPresenter;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

/**
 * 描述：实名认证付钱的页面
 * 作者：shiming_li
 * 时间：2016/11/30 17:59
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name
 * 项目名：SayLove
 */
public class RealNameGoMoneyActivity extends BaseActivity implements View.OnClickListener, IZhimaView {

    private TextView mChangeTvTitle;
    private TextView mChangeTvTitleBottom;
    private TextView mPrice;
    private SimpleTooltip mTooltip;
    private Button mBtnGoNext;
    private   ZhimaPreSenter mZhimaPreSenter;
    private String mZhimaUserName;
    private String mZhimaUserId;
    private VerifyTipsEntity mEntity;
    private VerifyTipsEntity mMEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_realname_gotomoner_layout);
        setTitle(R.string.realname_title);
        setTitleBarLeftBtnListener(this);
        initView();
        initListener();
        Bundle arguments = getIntent().getExtras();
        if (arguments!=null) {
            mZhimaUserName = arguments.getString("zhimaUserName");
            mZhimaUserId = arguments.getString("zhimaUserId");
        }
        mZhimaPreSenter = new ZhimaPreSenter(this, this);
        mZhimaPreSenter.getVerifyProduct();
        mZhimaPreSenter.getVerifyTips();
        EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAY_PAGE,ResourceKey.IDENTIFIICATION_CARD_PAY_PAGE);
    }

    private void initView() {
        mChangeTvTitle = (TextView) findViewById(R.id.fragment_realname_gotomoney_tv_change);
        mChangeTvTitleBottom = (TextView)findViewById(R.id.fragment_realname_gotomoney_tv_change_bottom);
        mBtnGoNext = (Button) findViewById(R.id.realname_fragment_btn_next);
         mPrice = (TextView) findViewById(R.id.fragment_realname_gotomoney_tv_price);
        //文字下面加横线
         mChangeTvTitleBottom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
    @Override
    public void getVerifyProduct(VerifyProduct verifyProduct) {
        String initPrice = "¥"+verifyProduct.initPrice;
        String price = verifyProduct.price;
        //文字中间加横线
        SpannableString sp = new SpannableString(initPrice);
        sp.setSpan(new StrikethroughSpan(), 0, initPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mChangeTvTitle.setText(sp);
        mPrice.setText("¥"+price);
    }

    @Override
    public void getVerifyTips(VerifyTipsEntity entity) {
        mMEntity = entity;
    }

    private void initListener() {
        mChangeTvTitleBottom.setOnClickListener(this);
        mBtnGoNext.setOnClickListener(this);
        mChangeTvTitleBottom.getPaint().setAntiAlias(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CreditApp.destroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_realname_gotomoney_tv_change_bottom:
                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPayPage.IDENTIFICATION_WHY_PAY);
                getPopuWindow(view);
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                //退回到上一层
                 finish();
                break;
            case R.id.realname_fragment_btn_next:
//                boolean mFlag = PreferenceUtil.getBoolean(PreferenceFileNames.APP_CONFIG, PreferenceKeys.IS_HAS_CHANCE_AUTHENTICATE, false);
////                DebugUtils.d("shimng",mFlag+"");
//                if (!mFlag) {
//                    PreferenceUtil.saveValue(PreferenceFileNames.APP_CONFIG, PreferenceKeys.IS_HAS_CHANCE_AUTHENTICATE, true);
//                }else {
//                    ToastUtils.toast(mContext, errorMsg);
//                }
                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPayPage.IDENTIFICATION_CHOOSE_PAY_TEPY);
                DialogUtil.showVerifyPayTypeDialog(this, new IBaseSurePayView() {
                    @Override
                    public void getPayTypeSuccess(PayCategories payCategories) {
                        DebugUtils.e("shiming","getPayTypeSuccess  "+ payCategories.payTypes.get(0).title);
                    }

                    @Override
                    public void showHasPayIn24HoursDialog(String windowContent) {
                        DebugUtils.e("shiming","showHasPayIn24HoursDialog  "+ windowContent);
                    }

                    @Override
                    public void paySuccess() {
                        PreferenceUtil.saveValue(PreferenceFileNames.APP_CONFIG, PreferenceKeys.IS_HAS_CHANCE_AUTHENTICATE, true);
                        showProgress();
                        mZhimaPreSenter.setZhimaConfig(mZhimaUserName,mZhimaUserId);
                    }

                    @Override
                    public void payFailed(String msg) {
                        //支付失败的的入口
                        DebugUtils.e("shiming","支付失败的  "+ msg);
                    }
                });
                break;
        }
    }

    private void ZhimaPayBack(ZhimaEntity mZhimaentity) {
        try {
            mZhimaentity.app_id = URLDecoder.decode(mZhimaentity.app_id, "UTF-8");
            mZhimaentity.params = URLDecoder.decode(mZhimaentity.params, "UTF-8");
            mZhimaentity.sign = URLDecoder.decode(mZhimaentity.sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CreditAuthHelper.creditCerify(this, mZhimaentity.app_id, mZhimaentity.params, mZhimaentity.sign, null, new ICreditListener() {
            @Override
            public void onComplete(Bundle bundle) {
                if (bundle != null) {
                    Set<String> keys = bundle.keySet();
                    String blackParams = "";
                    String blackSign = "";
                    for (String key : keys) {
//                            Log.d("tag", key + " = " + bundle.getString(key));
                        if ("params".equals(key)) {
                            blackParams = bundle.getString(key);
                        }
                        if ("sign".equals(key)) {
                            blackSign = bundle.getString(key);
                        }
                    }
//                    DebugUtils.d("shiming","芝麻认证成功了"+ "blackparams"+blackParams+"sign"+blackSign);
                    mZhimaPreSenter.postZhimaCallBack(blackParams,blackSign);
                }
            }

            @Override
            public void onError(Bundle result) {

            }

            @Override
            public void onCancel() {
                DebugUtils.d("shiming","zhimaoncancel");
                //芝麻取消了的话，回到我们身份证认证的界面
                ToastUtils.toast(RealNameGoMoneyActivity.this,"请重新输入您的身份信息");
//                Intent intent = new Intent(RealNameGoMoneyActivity.this,RealNameActivity.class);
//                startActivity(intent);
//                CreditApp.destroy();
                finish();
                dismissProgress();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CreditApp.onActivityResult(requestCode, resultCode, data);
    }

    private void getPopuWindow(View view) {
        SimpleTooltip.Builder builder = new SimpleTooltip.Builder(this)
                .anchorView(view)
                .gravity(Gravity.TOP)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .contentView(R.layout.pop_window);
        TextView tvContent = (TextView) builder.contentView.findViewById(R.id.tv_text);
        if (mMEntity!=null) {
            tvContent.setText(mMEntity.tips);
        }
        mTooltip = builder.build();
        mTooltip.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTooltip != null) {
            mTooltip.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void getZhimaData(ZAResponse<ZhimaEntity> data) {
        ZhimaPayBack(data.data);
    }

    @Override
    public void goToRealNameGoMoneyActivity(String errorCode, String errorMsg) {

    }

    @Override
    public void ZhimaCallBackFail(String errorCode, String errorMsg) {
        //认证失败开启重新输入身份证是否可行
        ToastUtils.toast(this,R.string.real_name_already_verify_fail);
        finish();
    }

    @Override
    public void ZhimaCallBackSuccess() {
        boolean uploadAvator = AccountManager.getInstance().getZaAccount().uploadAvator;
        DebugUtils.d("shiming","上传头像页"+uploadAvator);
        if (uploadAvator){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            //没有头像
            Intent intent=new Intent(this,HeadPortraitActivity.class);
            startActivity(intent);
        }
        LoginHelperPresenter loginHelperPresenter=new LoginHelperPresenter(this);
        loginHelperPresenter.getLoginHelper();
        finish();
    }

}
