package com.bluewhaledt.saylove.ui.register_login.real_name;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.MainActivity;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.HeadPortraitActivity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.presenter.ZhimaPreSenter;
import com.bluewhaledt.saylove.ui.register_login.real_name.view.IZhimaView;
import com.bluewhaledt.saylove.ui.register_login.real_name.zhima.CreditAuthHelper;
import com.bluewhaledt.saylove.ui.register_login.regist.LoginHelperPresenter;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：实名认证
 * 作者：shiming_li
 * 时间：2016/11/30 15:00
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name
 * 项目名：SayLove
 */
public class RealNameActivity extends BaseActivity implements View.OnClickListener, IZhimaView, View.OnLayoutChangeListener {

    private Button mGoToNextBtn;
    private MaterialEditText mCardId;
    private MaterialEditText mRealName;
    private ZhimaPreSenter mPreSenter;
    private String mName;
    private String mId;
    private boolean mIsFromInfoPage;
    private ScrollView mScrollView;
    private boolean mFlag=true;
    private int mKeyHeight;
    private boolean mIsFromRegistPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_realname_layout);
        setTitle(R.string.realname_title);
        setTitleBarLeftBtnListener(this);
        //获取屏幕高度
        mKeyHeight = this.getWindowManager().getDefaultDisplay().getHeight() / 3;
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            mIsFromInfoPage = extras.getBoolean(Constants.IS_FROM_INFOPAGE,false);
            mIsFromRegistPage = extras.getBoolean(Constants.IS_FROM_REGIST_PAGE, false);
        }
        initView();
        initListener();
        new LoginHelperPresenter(this).getLoginHelper();
        mPreSenter=new ZhimaPreSenter(this,this);
        DebugUtils.e("shiming", Build.CPU_ABI+"____________芝麻所用的 Build.CPU_ABI 看是不是v7 ");
        EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IDENTIFIICATION_CARD_PAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScrollView.addOnLayoutChangeListener(this);
    }
    private void initListener() {
        mGoToNextBtn.setOnClickListener(this);
//       mRealName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                mRealName.requestFocus();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }
    private void initView() {
        mGoToNextBtn = (Button) findViewById(R.id.realname_fragment_btn_next);
        mCardId = (MaterialEditText) findViewById(R.id.realname_fragment_edtext_real_id);
        mRealName = (MaterialEditText) findViewById(R.id.realname_fragment_edtext_real_name);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.realname_fragment_btn_next:
                EventStatistics.recordLog(ResourceKey.IDENTIFIICATION_CARD_PAGE,ResourceKey.IdentificationCardPage.IDENTIFICATION_GO_NEXT);
                mName = mRealName.getText().toString().trim();
                mId = mCardId.getText().toString().trim();
                if(mRealName.getText().toString().length()<1){
                    ToastUtils.toast(this,
                            R.string.real_name_no_name);
                }else if(mCardId.getText().toString().length()>18 || mCardId.getText().toString().length()<16){
                    ToastUtils.toast(this,
                            R.string.real_name_verify_id_wrong);
                }else if(!isNumeric(mCardId.getText().toString())){
                    ToastUtils.toast(this,
                            R.string.real_name_verify_id_format_wrong);
                }else if(!isNameeric(mRealName.getText().toString())){
                    ToastUtils.toast(this,
                            R.string.real_name_verify_name_format_wrong);
                } else {
                    showProgress();
//                    DebugUtils.d("shiming","第一次启动芝麻");
                    mPreSenter.setZhimaConfig(mName, mId);
                }
                break;

            case R.id.zhenai_lib_titlebar_left_text:
                DialogUtil.backRealName(this,mIsFromInfoPage, mIsFromRegistPage);
                break;
        }
    }

    /**
     * 判断身份证号码格式是否正确
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    /**
     * 判断姓名格式是否正确
     * @param str
     * @return
     */
    public boolean isNameeric(String str){
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]*$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * -00001 参数不能为空
     -55001 您已经认证过了
     -55003 您的验证次数已用完，请购买验证次数
     -55004 提交认证失败
     *
     */
    @Override
    public void getZhimaData(ZAResponse<ZhimaEntity> data) {
        DebugUtils.d("shiming",data.errorCode);
        if (!data.isError) {
            ZhimaPayBack(data.data);
        }
        dismissProgress();

    }
    //{"intentData":{},"errorCode":"-55001","errorMessage":"您已经认证过了","isError":true}
    //{"intentData":{},"errorCode":"-55007","errorMessage":"身份证号码格式有误","isError":true}
    @Override
    public void goToRealNameGoMoneyActivity(String errorCode, String errorMsg) {
        if (errorCode.equals("-55003")){
            Bundle arguments = new Bundle();
            arguments.putString("zhimaUserName",mName);
            arguments.putString("zhimaUserId",mId);
            Intent intent = new Intent(this, RealNameGoMoneyActivity.class);
            intent.putExtras(arguments);
            startActivity(intent);
            //去到付款的页面的话，就会关闭
//            CreditApp.destroy();
//            finish();
        }else if (errorCode.equals("-55001")){//认证失败，这个省份证被认证了
            ToastUtils.toast(this,R.string.real_name_already_verify);

        }else {
            ToastUtils.toast(this,errorMsg);
        }
        dismissProgress();
    }
    @Override
    public void ZhimaCallBackFail(String errorCode, String errorMsg) {
            ToastUtils.toast(this,R.string.real_name_already_verify_fail);
    }
    @Override
    public void ZhimaCallBackSuccess() {
        LoginHelperPresenter loginHelperPresenter=new LoginHelperPresenter(this);
        loginHelperPresenter.getLoginHelper();
        boolean uploadAvator = AccountManager.getInstance().getZaAccount().uploadAvator;
        DebugUtils.d("shiming","芝麻给钱了 ——进到上传头像"+uploadAvator);
        if (uploadAvator){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            //没有头像
            Intent intent=new Intent(this,HeadPortraitActivity.class);
            intent.putExtra(Constants.IS_FROM_REALNAME_PAGE, true);
            startActivity(intent);
        }
        finish();
    }
    @Override
    public void getVerifyProduct(VerifyProduct verifyProduct) {

    }

    @Override
    public void getVerifyTips(VerifyTipsEntity entity) {

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
                    DebugUtils.d("shiming","芝麻认证成功了"+ "blackparams"+blackParams+"sign"+blackSign);
                    mPreSenter.postZhimaCallBack(blackParams,blackSign);
                }
            }
            @Override
            public void onError(Bundle result) {
            }

            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CreditApp.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DialogUtil.backRealName(this,mIsFromInfoPage, mIsFromRegistPage);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > mKeyHeight)){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            mRealName.requestFocus();
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > mKeyHeight)){
        }
    }
}
