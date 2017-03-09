package com.bluewhaledt.saylove.ui.register_login.regist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.network.constant.TaskResult;
import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseFragment;
import com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText;
import com.bluewhaledt.saylove.ui.register_login.others.LinkTouchMovementMethod;
import com.bluewhaledt.saylove.ui.register_login.others.TouchableSpan;
import com.bluewhaledt.saylove.ui.register_login.real_name.RealNameActivity;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.bluewhaledt.saylove.util.RxCountDown;

import rx.Subscriber;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login.Fragment
 * @文件名: RegisterFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/29 15:57
 * @描述： 手机号注册页
 */
public class RegisterFragment extends RegistAndLoginBaseFragment implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener, IRegistView, OnRegisterExitListener {

    private MaterialEditText mRegisterPhoneEditTextView;
    private ImageView mPhoneIcon;
    private ImageView mLine;
    private Button mBtnGetVerificationCode;
    private MaterialEditText mRegisterVerificationCodeEditText;
    private ImageView mVerifyCodeIcon;
    private ImageView mLineVerifyCode;
    private MaterialEditText mRegisterPasswordEditTextView;
    private ImageView mPwdIcon;
    private ImageView mLinePwd;
    private Button mRegisterNext;
    private TextView mTvRegistProtocol;
    private RegisterPresenter mRegisterPresenter;
    private String mPhoneNum;
    private boolean isPhoneEmpty;
    private boolean isPhoneTextFocused;
    private String mPassWord;
    private boolean isPwdEmpty;
    private boolean isPwdTextFocused;
    private String mVerifyCode;
    private boolean isVerifyCodeEmpty;
    private boolean isVerifyTextFocused;
    public String registedPhoneNum;
    private Bundle arguments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.PHONE_VERIFY_PAGE, ResourceKey.PHONE_VERIFY_PAGE);
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_register_layout;
    }

    @Override
    protected void initView() {
        forceOpenSoftKeyboard();
        assignViews();
        initProtocolTextColor();
    }

    private   void forceOpenSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void assignViews() {
        showTitleBar(true);
        setTitle(R.string.regist);
        View view = getView();
        mRegisterPhoneEditTextView = (MaterialEditText) view.findViewById(R.id.register_phone_edit_text_view);
        mPhoneIcon = (ImageView) view.findViewById(R.id.phone_icon);
        mLine = (ImageView) view.findViewById(R.id.line);
        mBtnGetVerificationCode = (Button) view.findViewById(R.id.btn_get_verification_code);
        mRegisterVerificationCodeEditText = (MaterialEditText) view.findViewById(R.id.register_verification_code_edit_text);
        mVerifyCodeIcon = (ImageView) view.findViewById(R.id.verify_code_icon);
        mLineVerifyCode = (ImageView) view.findViewById(R.id.line_verify_code);
        mRegisterPasswordEditTextView = (MaterialEditText) view.findViewById(R.id.register_password_edit_text_view);
        mPwdIcon = (ImageView) view.findViewById(R.id.pwd_icon);
        mLinePwd = (ImageView)view. findViewById(R.id.line_pwd);
        mRegisterNext = (Button) view.findViewById(R.id.register_next);
        mTvRegistProtocol = (TextView) view.findViewById(R.id.tv_regist_protocol);
    }

    protected void initData() {
        arguments = getArguments();
        mRegisterPresenter = new RegisterPresenter(getActivity(), this);
        checkEditTextStatus();
        mRegisterPhoneEditTextView.requestFocus();
    }

    @Override
    public void onDestroyFragment() {
        super.onDestroyFragment();
        DebugUtils.d("yan", "onDestroyFragment");
    }

    protected void initListener() {
        mRegisterPhoneEditTextView.addTextChangedListener(this);
        mRegisterPhoneEditTextView.setOnFocusChangeListener(this);
        mRegisterPasswordEditTextView.addTextChangedListener(this);
        mRegisterPasswordEditTextView.setOnFocusChangeListener(this);
        mRegisterVerificationCodeEditText.addTextChangedListener(this);
        mRegisterVerificationCodeEditText.setOnFocusChangeListener(this);
        mBtnGetVerificationCode.setOnClickListener(this);
        mRegisterNext.setOnClickListener(this);
    }

    private void initProtocolTextColor() {
        String protocolText = getString(R.string.register_protocol_text);
        String lightText = getString(R.string.mini_protocol_text);
        int lightIndex = protocolText.indexOf(lightText);
        SpannableStringBuilder builder = new SpannableStringBuilder(protocolText);
        builder.setSpan(new TouchableSpan(getResources().getColor(R.color.register_protocol_light_color),getResources().getColor(R.color.Register_protocol_pressed_color),getResources().getColor(R.color.white)) {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProtocolActivity.class));
            }
        }, lightIndex, lightIndex + lightText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvRegistProtocol.setText(builder);
        mTvRegistProtocol.setMovementMethod(LinkTouchMovementMethod.getInstance());
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
        mPhoneNum = mRegisterPhoneEditTextView.getText().toString();
        isPhoneEmpty = TextUtils.isEmpty(mPhoneNum);
        isPhoneTextFocused = mRegisterPhoneEditTextView.isFocused();
        mPassWord = mRegisterPasswordEditTextView.getText().toString();
        isPwdEmpty = TextUtils.isEmpty(mPassWord);
        isPwdTextFocused = mRegisterPasswordEditTextView.isFocused();
        mVerifyCode = mRegisterVerificationCodeEditText.getText().toString();
        isVerifyCodeEmpty = TextUtils.isEmpty(mVerifyCode);
        isVerifyTextFocused = mRegisterVerificationCodeEditText.isFocused();
        mPhoneIcon.setImageResource(!isPhoneEmpty || isPhoneTextFocused ? R.drawable.icon_register_phone_golden : R.drawable.icon_register_phone_gray);
        mVerifyCodeIcon.setImageResource(!isVerifyCodeEmpty || isVerifyTextFocused ? R.drawable.icon_register_code_golden: R.drawable.icon_register_code_gray );
        mPwdIcon.setImageResource(!isPwdEmpty || isPwdTextFocused ? R.drawable.icon_register_password_golden : R.drawable.icon_register_password_gray);
    }

    private boolean checkIsValid() {
        return mRegisterPresenter.checkIsValid(mRegisterPhoneEditTextView.getText().toString(),
                                                            mRegisterPasswordEditTextView.getText().toString(),
                                                            mRegisterVerificationCodeEditText.getText().toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String phone = mRegisterPhoneEditTextView.getText().toString();
        String verfycode = mRegisterVerificationCodeEditText.getText().toString();
        switch (id) {
            case R.id.btn_get_verification_code:
                boolean phoneIsValid = mRegisterPresenter.checkPhoneIsValid(phone);
                if (phoneIsValid) {
                    mRegisterPresenter.phoneVerify(phone);
                }else{
                    ToastUtils.toast(getActivity(),R.string.correct_phoneNum_needed);
                }
                break;
            case R.id.register_next:
                if (checkIsValid()) {
                EventStatistics.recordLog(ResourceKey.PHONE_VERIFY_PAGE, ResourceKey.PhoneVerifyPage.PHONE_VERIFY_NEXT);
                String pwd = mRegisterPasswordEditTextView.getText().toString();
                int gender = arguments.getInt(Constants.GENDER);
                int city_code = arguments.getInt(Constants.CITY_CODE);
                String year = arguments.getString(Constants.AGE);
                int height = arguments.getInt(Constants.HEIGHT);
                int marital = arguments.getInt(Constants.MARITAL);
                int salary = arguments.getInt(Constants.SALARY);
                mRegisterPresenter.regist(phone,verfycode,gender,
                                                            city_code,height,marital,
                                                            salary,pwd,year);
                DebugUtils.d("yan", phone+"=phone"+gender + "=gender" + city_code +"=city_code" + height + "=height" + marital + "=marital" + salary + "=salary" + pwd + "=pwd" + year + "=year");
                }
                break;
        }
    }

    @Override
    public void onSuccess() {
        EventStatistics.recordLog(ResourceKey.PHONE_VERIFY_PAGE, ResourceKey.PhoneVerifyPage.PHONE_VERIFY_SUCCESS);
        IMUtil.loginIMByGetAccount();
        ToastUtils.toast(getActivity(), R.string.regist_successful);
        Intent intent=new Intent(getActivity(),RealNameActivity.class);
        intent.putExtra(Constants.IS_FROM_REGIST_PAGE, true);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void resendCodeSuccess() {
        ToastUtils.toast(getActivity(), R.string.verify_code_send);
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
    public void requestCodeFailed(String errorCode, String errorMsg) {
        //根据返回的错误码，判断响应方式与反应内容errorMsg的使用方式
        if (errorCode == null){
            ToastUtils.toast(getActivity(), R.string.no_network_connected);
        }else if (TaskResult.PHONE_EXIST == Integer.parseInt(errorCode)){
            registedPhoneNum = mRegisterPhoneEditTextView.getText().toString();
            PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.REGISTED_PHONE, registedPhoneNum);
            DialogUtil.showAlreadyRegistedDialog(getActivity(), this);
            EventStatistics.recordLog(ResourceKey.PHONE_VERIFY_PAGE, ResourceKey.PhoneVerifyPage.PHONE_VERIFY_DIALOG);
        }else{
            ToastUtils.toast(getActivity(), errorMsg);
        }
    }

    @Override
    public void onExit() {
        getActivity().finish();
    }
}
