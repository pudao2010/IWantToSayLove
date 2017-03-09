package com.bluewhaledt.saylove.ui.register_login.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseActivity;
import com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.bluewhaledt.saylove.util.RxCountDown;

import rx.Subscriber;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: ResetPwdActivity
 * @创建者: YanChao
 * @创建时间: 2016/12/2 11:01
 * @描述： 重置密码
 */
public class ResetPwdActivity extends RegistAndLoginBaseActivity implements IResetPwdView,TextWatcher, View.OnFocusChangeListener, View.OnClickListener {

    private MaterialEditText mResetpwdPhoneEditTextView;
    private ImageView mPhoneIcon;
    private ImageView mLine;
    private Button mBtnGetVerificationCode;
    private MaterialEditText mResetpwdVerificationCodeEditText;
    private ImageView mVerifyCodeIcon;
    private ImageView mLineVerifyCode;
    private MaterialEditText mResetpwdPasswordEditTextView;
    private ImageView mPwdIcon;
    private ImageView mLinePwd;
    private Button mModify;
    private ResetPwdPresenter mResetPwdPresenter;
    private String phoneNum;
    private String mPhoneNum;
    private boolean isPhoneEmpty;
    private boolean isPhoneTextFocused;
    private String mPassWord;
    private boolean isPwdEmpty;
    private boolean isPwdTextFocused;
    private String mVerifyCode;
    private boolean isVerifyCodeEmpty;
    private boolean isVerifyTextFocused;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd_layout);
        assignViews();
        init();
        initListener();
        EventStatistics.recordLog(ResourceKey.LOGIN_PAGE, ResourceKey.LoginPage.LOGIN_RESETPWD_PAGE);
    }

    private void init() {
        intent = getIntent();
        mResetPwdPresenter = new ResetPwdPresenter(this, this);
        checkEditTextStatus();
    }

    private void initListener() {
        mResetpwdPhoneEditTextView.addTextChangedListener(this);
        mResetpwdPhoneEditTextView.setOnFocusChangeListener(this);
        mResetpwdPasswordEditTextView.addTextChangedListener(this);
        mResetpwdPasswordEditTextView.setOnFocusChangeListener(this);
        mResetpwdVerificationCodeEditText.addTextChangedListener(this);
        mResetpwdVerificationCodeEditText.setOnFocusChangeListener(this);
        mBtnGetVerificationCode.setOnClickListener(this);
        mModify.setOnClickListener(this);
        setTitleBarLeftBtnListener(this);
    }

    private void assignViews() {
        showTitleBar(true);
        setTitle(R.string.reset_pwd);
        mResetpwdPhoneEditTextView = (MaterialEditText) findViewById(R.id.resetpwd_phone_edit_text_view);
        mPhoneIcon = (ImageView) findViewById(R.id.phone_icon);
        mLine = (ImageView) findViewById(R.id.line);
        mBtnGetVerificationCode = (Button) findViewById(R.id.btn_get_verification_code);
        mResetpwdVerificationCodeEditText = (MaterialEditText) findViewById(R.id.resetpwd_verification_code_edit_text);
        mVerifyCodeIcon = (ImageView) findViewById(R.id.verify_code_icon);
        mLineVerifyCode = (ImageView) findViewById(R.id.line_verify_code);
        mResetpwdPasswordEditTextView = (MaterialEditText) findViewById(R.id.resetpwd_password_edit_text_view);
        mPwdIcon = (ImageView) findViewById(R.id.pwd_icon);
        mLinePwd = (ImageView) findViewById(R.id.line_pwd);
        mModify = (Button) findViewById(R.id.modify);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        checkEditTextStatus();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        checkEditTextStatus();
    }

    private void checkEditTextStatus(){
        mPhoneNum = mResetpwdPhoneEditTextView.getText().toString();
        isPhoneEmpty = TextUtils.isEmpty(mPhoneNum);
        isPhoneTextFocused = mResetpwdPhoneEditTextView.isFocused();
        mPassWord = mResetpwdPasswordEditTextView.getText().toString();
        isPwdEmpty = TextUtils.isEmpty(mPassWord);
        isPwdTextFocused = mResetpwdPasswordEditTextView.isFocused();
        mVerifyCode = mResetpwdVerificationCodeEditText.getText().toString();
        isVerifyCodeEmpty = TextUtils.isEmpty(mVerifyCode);
        isVerifyTextFocused = mResetpwdVerificationCodeEditText.isFocused();
        mPhoneIcon.setImageResource(!isPhoneEmpty || isPhoneTextFocused ? R.drawable.icon_register_phone_golden : R.drawable.icon_register_phone_gray);
        mVerifyCodeIcon.setImageResource(!isVerifyCodeEmpty || isVerifyTextFocused ? R.drawable.icon_register_code_golden: R.drawable.icon_register_code_gray );
        mPwdIcon.setImageResource(!isPwdEmpty || isPwdTextFocused ? R.drawable.icon_register_password_golden : R.drawable.icon_register_password_gray);
    }

    private boolean checkIsValid() {
        return mResetPwdPresenter.checkIsValid(mResetpwdPhoneEditTextView.getText().toString(),
                mResetpwdPasswordEditTextView.getText().toString(),mResetpwdVerificationCodeEditText.getText().toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_get_verification_code:
                String mobile = mResetpwdPhoneEditTextView.getText().toString();
                if (mResetPwdPresenter.checkPhoneIsValid(mobile)) {
                    mResetPwdPresenter.getResetVerifyCode(mobile);
                }else{
                    ToastUtils.toast(this,R.string.correct_phoneNum_needed);
                }
                break;
            case R.id.modify:
                if (checkIsValid()) {
                mResetPwdPresenter.performReset(mPhoneNum, mVerifyCode, mPassWord);
                }
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                intent.putExtra(Constants.RESET_PWD_PHONENUM, "");
                setResult(-2, intent);
                ResetPwdActivity.this.finish();
        }
    }

    @Override
    public void onResetSuccess() {
        EventStatistics.recordLog(ResourceKey.LOGIN_PAGE, ResourceKey.LoginPage.LOGIN_RESETPWD_SUCCESS);
        ToastUtils.toast(ResetPwdActivity.this, R.string.reset_successful);
        intent.putExtra(Constants.RESET_PWD_PHONENUM, mPhoneNum);
        setResult(-1, intent);
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PHONE,"");
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PWD,"");
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AUTO_LOGIN,false);
        ResetPwdActivity.this.finish();
    }



    @Override
    public void resendCodeSuccess() {
        ToastUtils.toast(ResetPwdActivity.this,R.string.verify_code_send);
        RxCountDown.countdown(30)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mBtnGetVerificationCode.setText(getString(R.string.get_verify_code));
                        mBtnGetVerificationCode.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        String remainSecondStr = String.valueOf(integer);
                        String text = getString(R.string.code_remain_tips, remainSecondStr);
                        int start = text.indexOf(remainSecondStr);
                        SpannableStringBuilder builder = new SpannableStringBuilder(text);
                        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_saylove)),
                                start, start + remainSecondStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mBtnGetVerificationCode.setText(builder);
                        mBtnGetVerificationCode.setEnabled(false);

                    }
                });
    }

    @Override
    public void onBackPressed() {
        intent.putExtra(Constants.RESET_PWD_PHONENUM, "");
        setResult(-2, intent);
        ResetPwdActivity.this.finish();
    }

}
