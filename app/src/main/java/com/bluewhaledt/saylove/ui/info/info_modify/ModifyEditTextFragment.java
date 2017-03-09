package com.bluewhaledt.saylove.ui.info.info_modify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.info.base.BaseProfileFragment;
import com.bluewhaledt.saylove.ui.info.info_modify.presenter.InfoModifyPresenter;
import com.bluewhaledt.saylove.ui.info.info_modify.view.IModifyView;
import com.bluewhaledt.saylove.util.EventStatistics;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by rade.chan on 2016/11/29.
 */

public class ModifyEditTextFragment extends BaseProfileFragment implements View.OnClickListener, IModifyView {

    private EditText mEditText;
    private View deleteView;
    private TextView wordTipsTv;
    private static final int MAX_SIZE = 1000;

    private InfoModifyPresenter presenter;


    @Override
    public int getContentRes() {
        return R.layout.fragment_profile_edittext_layout;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListener();
        initViewData();
    }

    private void initViews() {
        if (sourceFrom == FROM_INTRODUCE_MYSELF) {
            EventStatistics.recordLog(ResourceKey.INTRODUCE_MYSELF_PAGE,ResourceKey.INTRODUCE_MYSELF_PAGE);
            setTitle(R.string.item_introduce_myself);
            ViewStub viewStub = find(R.id.introduce_myself_layout);
            viewStub.inflate();
            wordTipsTv = find(R.id.word_tips_tv);
            deleteView = find(R.id.delete_view);
        }
        mEditText = find(R.id.modify_edit_text_view);
        setTitleBarRightBtnText(R.string.save);
        setTitleBarRightBtnListener(this);

    }

    private void initViewData() {
        presenter = new InfoModifyPresenter(getActivity(), this);
        if (sourceFrom == FROM_INTRODUCE_MYSELF) {
            wordTipsTv.setText(mEditText.getText().length() + "/" + MAX_SIZE);
        }

        if(!TextUtils.isEmpty(currentValue)){
            mEditText.setText(currentValue);
            mEditText.setSelection(currentValue.length());
        }
    }

    private void initListener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isModify=true;
                String text = editable.toString();
                if (wordTipsTv != null) {
                    wordTipsTv.setText(text.length() + "/" + MAX_SIZE);
                }

            }
        });

        if (deleteView != null) {
            deleteView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_view:
                mEditText.getText().clear();
                break;
            case R.id.zhenai_lib_titlebar_right_text:
                String text = mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(text) && isModify) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(KEY_MINE_INTRODUCE, text);
                    presenter.modifyMyInfo(params);
                } else {
                    ToastUtils.toast(getContext(), R.string.is_not_fill);
                }
                break;
        }
    }

    @Override
    public void showItemInfo(BaseEntity entity) {

    }

    @Override
    public void modifySuccess() {
        EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_BASIC_INFO));
        EventStatistics.recordLog(ResourceKey.INTRODUCE_MYSELF_PAGE,ResourceKey.IntroduceMyselfPage.INTRODUCE_MYSELF_FINISH_NUM);
        finish();
    }


}
