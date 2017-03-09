package com.bluewhaledt.saylove.ui.info.info_modify;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.picker_view.view.BasePickerView;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.DictionaryBean;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.info.base.BaseProfileFragment;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_modify.presenter.InfoModifyPresenter;
import com.bluewhaledt.saylove.ui.info.info_modify.view.IModifyView;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.util.CityConvertUtil;
import com.bluewhaledt.saylove.util.DataDictionaryHelper;
import com.bluewhaledt.saylove.util.DictionaryUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by rade.chan on 2016/11/29.
 */

public class BasicInfoModifyFragment extends BaseProfileFragment implements View.OnClickListener, IModifyView {

    private ItemLayout nickNameLayout;
    private ItemLayout idLayout;
    private ItemLayout birthLayout;
    private ItemLayout heightLayout;

    private ItemLayout incomeLayout;
    private ItemLayout workCityLayout;
    private ItemLayout nativeProvinceLayout;
    private ItemLayout professionLayout;
    private ItemLayout marryStatusLayout;
    private ItemLayout childrenLayout;
    private ItemLayout drinkingLayout;
    private ItemLayout smokingLayout;
    private ItemLayout sexLayout;

    private HashMap<String, String> params = new HashMap<>();

    private InfoModifyPresenter mPresenter;
    private BasePickerView pickView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        initData();
        EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BASIC_INFO_MODIFY_PAGE);
    }

    private void initData() {
        mPresenter = new InfoModifyPresenter(getActivity(), this);
        loadUserInfo();
    }

    private void loadUserInfo() {
        mPresenter.getUserInfo("");
    }

    private void initView() {
        setTitle(R.string.item_basic_info);
        nickNameLayout = find(R.id.nick_name_item_layout);
        idLayout = find(R.id.id_item_layout);
        birthLayout = find(R.id.born_day_item_layout);
        sexLayout = find(R.id.sex_item_layout);
        heightLayout = find(R.id.height_item_layout);
        incomeLayout = find(R.id.income_item_layout);
        workCityLayout = find(R.id.work_city_item_layout);
        nativeProvinceLayout = find(R.id.native_place_item_layout);
        professionLayout = find(R.id.profession_item_layout);
        marryStatusLayout = find(R.id.marry_status_item_layout);
        childrenLayout = find(R.id.child_item_layout);
        drinkingLayout = find(R.id.drinking_item_layout);
        smokingLayout = find(R.id.smoking_item_layout);

        setTitleBarRightBtnText(R.string.save);
        setTitleBarRightBtnListener(this);
    }

    private void initListener() {
        nickNameLayout.setOnClickListener(this);
        heightLayout.setOnClickListener(this);
        incomeLayout.setOnClickListener(this);
        workCityLayout.setOnClickListener(this);
        nativeProvinceLayout.setOnClickListener(this);
        professionLayout.setOnClickListener(this);
        childrenLayout.setOnClickListener(this);
        drinkingLayout.setOnClickListener(this);
        smokingLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nick_name_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_NICKNAME);
                showNicknameDialog();
                break;
            case R.id.height_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_HEIGHT);
                pickView = DictionaryUtil.showSingleHeightPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.height : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                heightLayout.setRightText(item.value + Constants.UNIT_HEIGHT);
                                params.put(KEY_MINE_HEIGHT, String.valueOf(item.key));
                                currentEntity.height = item.key;
                            }
                        });

                break;
            case R.id.income_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_INCOME);
                pickView = DictionaryUtil.showYearIncomeWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.salary : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                incomeLayout.setRightText(item.value);
                                params.put(KEY_MINE_SALARY, String.valueOf(item.key));
                                currentEntity.salary = item.key;
                            }
                        });

                break;
            case R.id.work_city_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_WORK_CITY);
                pickView = DictionaryUtil.showNativePlacePickerView(getContext(),getResources().getString(R.string.work_city),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity.workcity !=0 ? currentEntity.workcity : ZhenaiApplication.mCityCode,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                String city = CityConvertUtil.convertName(item1.value, item2.value);
                                workCityLayout.setRightText(city);
                                params.put(KEY_MINE_WORK_CITY, String.valueOf(item2.key));
                                currentEntity.workcity = item2.key;
                            }
                        });
                break;
            case R.id.native_place_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_NATIVE_CITY);
                pickView = DictionaryUtil.showNativePlacePickerView(getContext(),getResources().getString(R.string.native_place),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity.hometown != 0 ? currentEntity.hometown : ZhenaiApplication.mCityCode,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                String city = CityConvertUtil.convertName(item1.value, item2.value);
                                nativeProvinceLayout.setRightText(city);
                                params.put(KEY_MINE_HOMETOWN, String.valueOf(item2.key));
                                currentEntity.hometown = item2.key;
                            }
                        });
                break;
            case R.id.profession_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_PROFESSION);
                pickView = DictionaryUtil.showJobWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.occupation : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                professionLayout.setRightText(item.value);
                                params.put(KEY_MINE_OCCUPATION, String.valueOf(item.key));
                                currentEntity.occupation = item.key;
                            }
                        });
                break;
            case R.id.marry_status_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_MARRY_STATUS);
                pickView = DictionaryUtil.showMaritalStatusWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.marryState : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                marryStatusLayout.setRightText(item.value);
                                params.put(KEY_MINE_MARRY_STATE, String.valueOf(item.key));
                                currentEntity.marryState = item.key;
                            }
                        });
                break;
            case R.id.child_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_HAS_CHILD);
                pickView = DictionaryUtil.showChildrenWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.haveChild : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                childrenLayout.setRightText(item.value);
                                params.put(KEY_MINE_HAVE_CHILD, String.valueOf(item.key));
                                currentEntity.haveChild = item.key;
                            }
                        });
                break;
            case R.id.drinking_item_layout:
                EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_DRINK);
                pickView = DictionaryUtil.showDrinkingWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.drinkState : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                drinkingLayout.setRightText(item.value);
                                params.put(KEY_MINE_DRINK_STATE, String.valueOf(item.key));
                                currentEntity.drinkState = item.key;
                            }
                        });

                break;
            case R.id.smoking_item_layout:
                pickView = DictionaryUtil.showSmokingWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NONE,
                        currentEntity != null ? currentEntity.smokeState : -2,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                smokingLayout.setRightText(item.value);
                                params.put(KEY_MINE_SMOKE_STATE, String.valueOf(item.key));
                                currentEntity.smokeState = item.key;
                            }
                        });
                break;
            case R.id.zhenai_lib_titlebar_right_text:
                if (params.size() > 0) {
                    mPresenter.modifyMyInfo(params);
                } else {
                    ToastUtils.toast(getContext(), R.string.modify_nothing);
                }
                break;
        }
    }

    /**
     * 昵称
     */
    private void showNicknameDialog() {
        final EditText editText = new EditText(getContext());
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        if (!TextUtils.isEmpty(nickNameLayout.getRightText())) {
            editText.setText(nickNameLayout.getRightText());
            editText.setSelection(nickNameLayout.getRightText().length());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(R.string.nickname)
                .setView(editText, 42, 20, 42, 20)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nickname = editText.getText().toString().trim();
                                if (!TextUtils.isEmpty(nickname) && !nickNameLayout.getRightText().equals(nickname)) {
                                    if (nickname.length() > 1) {
                                        nickNameLayout.setRightText(nickname);
                                        params.put(KEY_MINE_NICKNAME, nickname);
                                    } else {
                                        ToastUtils.toast(getActivity(), getString(R.string.nick_name_too_short));
                                    }
                                }

                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }


    @Override
    public int getContentRes() {
        return R.layout.activity_basic_info_modify_layout;
    }

    private UserInfoEntity currentEntity;

    @Override
    public void showItemInfo(BaseEntity baseEntity) {
        if (!(baseEntity instanceof UserInfoEntity)) {
            return;
        }
        currentEntity = (UserInfoEntity) baseEntity;
        if (!TextUtils.isEmpty(currentEntity.nickName)) {
            nickNameLayout.setRightText(currentEntity.nickName);
        } else {
            nickNameLayout.setRightText("会员" + currentEntity.userId);
            currentEntity.nickName = "会员" + currentEntity.userId;
        }
        idLayout.setRightText(String.valueOf(currentEntity.userId));
        birthLayout.setRightText(currentEntity.birthday);
        heightLayout.setRightText(!TextUtils.isEmpty(currentEntity.heightDesc) ? currentEntity.heightDesc : getString(R.string.select_nothing));
        incomeLayout.setRightText(!TextUtils.isEmpty(currentEntity.salaryDesc) ? currentEntity.salaryDesc : getString(R.string.select_nothing));
        workCityLayout.setRightText(!TextUtils.isEmpty(currentEntity.workcityDesc) ? currentEntity.workcityDesc : getString(R.string.select_nothing));
        nativeProvinceLayout.setRightText(!TextUtils.isEmpty(currentEntity.hometownDesc) ? currentEntity.hometownDesc : getString(R.string.select_nothing));
        professionLayout.setRightText(!TextUtils.isEmpty(currentEntity.occupationDesc) ? currentEntity.occupationDesc :getString(R.string.select_nothing));
        marryStatusLayout.setRightText(!TextUtils.isEmpty(currentEntity.marryDesc) ? currentEntity.marryDesc : getString(R.string.select_nothing));
        childrenLayout.setRightText(!TextUtils.isEmpty(currentEntity.haveChildDesc) ? currentEntity.haveChildDesc : getString(R.string.select_nothing));
        drinkingLayout.setRightText(!TextUtils.isEmpty(currentEntity.drinkDesc) ? currentEntity.drinkDesc : getString(R.string.select_nothing));
        smokingLayout.setRightText(currentEntity.getSmoking());
        if (currentEntity.sex == 1) {
            sexLayout.setRightText(getResources().getString(R.string.man_text));
        } else {
            sexLayout.setRightText(getResources().getString(R.string.female_text));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pickView != null && pickView.isShowing()) {
            pickView.dismiss();
        }
    }

    @Override
    public void modifySuccess() {
        isModify = true;
        params.clear();
        EventStatistics.recordLog(ResourceKey.BASIC_INFO_MODIFY_PAGE,ResourceKey.BasicInfoModifyPage.BASIC_INFO_PAGE_FINISH_NUM);
        // loadUserInfo();
        finish();
    }


    @Override
    public void onStopFragment() {
        super.onStopFragment();
        if (isModify) {
            EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_BASIC_INFO));
        }
    }
}
