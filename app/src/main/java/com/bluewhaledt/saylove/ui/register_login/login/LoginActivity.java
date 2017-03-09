package com.bluewhaledt.saylove.ui.register_login.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseActivity;
import com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText;
import com.bluewhaledt.saylove.ui.register_login.regist.EntranceActivity;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;


/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login
 * @文件名: LoginActivity
 * @创建者: YanChao
 * @创建时间: 2016/11/25 17:50
 * @描述： 登录页
 */
public class LoginActivity extends RegistAndLoginBaseActivity implements ILoginView, View.OnFocusChangeListener, TextWatcher, View.OnClickListener {

    private static final int RESET_REQUEST_CODE = 100;
    private MaterialEditText mLoginPhoneEditTextView;
    private ImageView mPhoneIcon;
    private ImageView mPhoneLine;
    private MaterialEditText mLoginPasswordEditTextView;
    private ImageView mPwdIcon;
    private ImageView mLinePwd;
    private Button mLogin;
    private TextView mForgetPasswordTv;
    private LoginPresenter mLoginPersenter;
    private String mPhoneNum;
    private boolean isPhoneEmpty;
    private boolean isPhoneTextFocused;
    private String mPassWord;
    private boolean isPwdEmpty;
    private boolean isPwdTextFocused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        assignViews();
        init();
        initData();
        initListener();
        EventStatistics.recordLog(ResourceKey.LOGIN_PAGE, ResourceKey.LOGIN_PAGE);
        mPermissionHelper.requestPermissions("SD卡权限没有打开",new PermissionHelper.PermissionListener(){

            @Override
            public void doAfterGrand(String... permission) {

            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void init() {
        mLoginPersenter = new LoginPresenter(this,this);
    }

    private void assignViews() {
        showTitleBar(true);
        setTitle(R.string.login);
        mLoginPhoneEditTextView = (MaterialEditText) findViewById(R.id.login_phone_edit_text_view);
        mPhoneIcon = (ImageView) findViewById(R.id.phone_icon);
        mPhoneLine = (ImageView) findViewById(R.id.phone_line);
        mLoginPasswordEditTextView = (MaterialEditText) findViewById(R.id.login_password_edit_text_view);
        mPwdIcon = (ImageView) findViewById(R.id.pwd_icon);
        mLinePwd = (ImageView) findViewById(R.id.line_pwd);
        mLogin = (Button) findViewById(R.id.login);
        mForgetPasswordTv = (TextView) findViewById(R.id.forget_password_tv);
    }

    private void initData() {
        Intent intent = getIntent();
        boolean isFromRegist = intent.getBooleanExtra(Constants.IS_FROM_REGIST_PAGE, false);
        if (isFromRegist) {
            String registedPhoneNum = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.REGISTED_PHONE, "");
            if (!TextUtils.isEmpty(registedPhoneNum)){
                mLoginPhoneEditTextView.setText(registedPhoneNum);
                mLoginPasswordEditTextView.requestFocus();
            }
        }else {
            String lastPhoneNum = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PHONE, "");
            String lastPassword = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PWD, "");
            if (!TextUtils.isEmpty(lastPhoneNum)) {
                mLoginPhoneEditTextView.setText(lastPhoneNum);
                mLoginPhoneEditTextView.setSelection(lastPhoneNum.length());
                mLoginPasswordEditTextView.requestFocus();
                if (!TextUtils.isEmpty(lastPassword)) {
                    mLoginPasswordEditTextView.setText(lastPassword);
                    mLoginPasswordEditTextView.setSelection(lastPassword.length());
                }
            }
        }
        checkEditTextStatus();
    }

    private void initListener() {
        mLoginPhoneEditTextView.setOnFocusChangeListener(this);
        mLoginPhoneEditTextView.addTextChangedListener(this);
        mLoginPasswordEditTextView.setOnFocusChangeListener(this);
        mLoginPasswordEditTextView.addTextChangedListener(this);
        mLogin.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
        setTitleBarLeftBtnListener(this);
    }

    private boolean checkIsValid() {
       return mLoginPersenter.checkIsValid(mPhoneNum, mPassWord);
    }

    @Override
    public void loginSuccess() {
        EventStatistics.recordLog(ResourceKey.LOGIN_PAGE, ResourceKey.LoginPage.LOGIN_SUCCESS);
        IMUtil.loginIMByGetAccount();
        finish();
    }

//    private void loginIM(){
//        CommonModel.getIMAccount(new BaseSubscriber<ZAResponse<IMAccount>>(new ZASubscriberListener<ZAResponse<IMAccount>>() {
//            @Override
//            public void onSuccess(ZAResponse<IMAccount> response) {
//                if (response.data != null){
//                    IMAccount account = response.data;
//                    IMUtil.login(account.imAccount,account.token);
//                }
//            }
//        }));
//    }

    @Override
    public void loginFailed(String errCode, String ErrorMsg) {
        if (errCode != null){
            ToastUtils.toast(this, ErrorMsg);
        }
    }

    @Override
    public void loginError(Throwable e) {

    }

    private void checkEditTextStatus(){
        mPhoneNum = mLoginPhoneEditTextView.getText().toString();
        isPhoneEmpty = TextUtils.isEmpty(mPhoneNum);
        isPhoneTextFocused = mLoginPhoneEditTextView.isFocused();
        mPassWord = mLoginPasswordEditTextView.getText().toString();
        isPwdEmpty = TextUtils.isEmpty(mPassWord);
        isPwdTextFocused = mLoginPasswordEditTextView.isFocused();
        mPhoneIcon.setImageResource(!isPhoneEmpty || isPhoneTextFocused ? R.drawable.icon_register_phone_golden : R.drawable.icon_register_phone_gray);
        mPwdIcon.setImageResource(!isPwdEmpty || isPwdTextFocused ? R.drawable.icon_register_password_golden : R.drawable.icon_register_password_gray);
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login:
                if (checkIsValid()) {
                mLoginPersenter.login(mLoginPhoneEditTextView.getText().toString().trim(), mLoginPasswordEditTextView.getText().toString().trim(), false);
                }
                break;
            case R.id.forget_password_tv:

                startActivityForResult(new Intent(this,ResetPwdActivity.class),RESET_REQUEST_CODE);
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                startActivity(new Intent(this, EntranceActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,EntranceActivity.class));
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RESET_REQUEST_CODE  && resultCode == -1){
            String phoneNum = data.getExtras().getString(Constants.RESET_PWD_PHONENUM);
            mLoginPhoneEditTextView.setText(phoneNum);
            mLoginPasswordEditTextView.setText("");
            mLoginPasswordEditTextView.requestFocus();
        }
    }

}
