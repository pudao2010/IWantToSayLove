package com.bluewhaledt.saylove.ui.info.info_modify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.picker_view.view.BasePickerView;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.DictionaryBean;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.info.base.BaseProfileFragment;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_modify.presenter.InfoModifyPresenter;
import com.bluewhaledt.saylove.ui.info.info_modify.view.IModifyView;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.util.CityConvertUtil;
import com.bluewhaledt.saylove.util.DataDictionaryHelper;
import com.bluewhaledt.saylove.util.DictionaryUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import java.util.HashMap;

/**
 * Created by rade.chan on 2016/11/29.
 */

public class RequireInfoModifyFragment extends BaseProfileFragment implements View.OnClickListener, IModifyView {

    private ItemLayout ageLayout;
    private ItemLayout heightLayout;
    private ItemLayout incomeLayout;
    private ItemLayout workCityLayout;
    private ItemLayout nativeProvinceLayout;
    private ItemLayout marryStatusLayout;

    private HashMap<String, String> params = new HashMap<>();

    private InfoModifyPresenter mPresenter;
    private BasePickerView pickView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        initData();
        EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.REQUIRE_INFO_MODIFY_PAGE);
    }


    private void initData() {
        mPresenter = new InfoModifyPresenter(getActivity(), this);
        loadRequireInfo();
    }

    private void loadRequireInfo() {
        mPresenter.getRequireInfo("");
    }

    private void initView() {
        setTitle(R.string.item_other_require);
        ageLayout = (ItemLayout) mContentView.findViewById(R.id.require_age_item_layout);
        heightLayout = (ItemLayout) mContentView.findViewById(R.id.require_height_item_layout);
        incomeLayout = (ItemLayout) mContentView.findViewById(R.id.require_income_item_layout);
        workCityLayout = (ItemLayout) mContentView.findViewById(R.id.require_work_city_item_layout);
        nativeProvinceLayout = (ItemLayout) mContentView.findViewById(R.id.require_native_item_layout);
        marryStatusLayout = (ItemLayout) mContentView.findViewById(R.id.require_marry_status_item_layout);

        setTitleBarRightBtnText(R.string.save);
        setTitleBarRightBtnListener(this);
    }

    private void initListener() {
        ageLayout.setOnClickListener(this);
        heightLayout.setOnClickListener(this);
        incomeLayout.setOnClickListener(this);
        workCityLayout.setOnClickListener(this);
        nativeProvinceLayout.setOnClickListener(this);
        marryStatusLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.require_age_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_AGE);
                pickView = DictionaryUtil.showAgeWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        entity != null ? entity.minAge : -1,
                        entity != null ? entity.maxAge : -1,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                try {

                                    int start = item1.key;
                                    int end = item2.key;
                                    if (start == -1 && end == -1) {
                                        ageLayout.setRightText(getString(R.string.no_limit));
                                    } else if (start == -1) {
                                        ageLayout.setRightText(end+getString(R.string.age_unit)+"以下");
                                    } else if (end == -1) {
                                        ageLayout.setRightText(start+getString(R.string.age_unit)+"以上");

                                    } else if (end > start) {
                                        ageLayout.setRightText(item1.value + "-" + item2.value + getString(R.string.age_unit));

                                    } else {
                                        ToastUtils.toast(getContext(), getString(R.string.please_select_correct_range));
                                    }

                                    if(start==-1 || end==-1 || end>start){
                                        params.put(KEY_REQUIRE_MIN_AGE, String.valueOf(item1.key));
                                        entity.minAge = item1.key;
                                        params.put(KEY_REQUIRE_MAX_AGE, String.valueOf(item2.key));
                                        entity.maxAge = item2.key;
                                    }

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                break;
            case R.id.require_height_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_HEIGHT);
                int sex = AccountManager.getInstance().getZaAccount().sex;
                pickView = DictionaryUtil.showHeightPickerView(getContext(),
                        sex == 1 ? 2 : 1,
                        entity != null ? entity.minHeight : -1,
                        entity != null ? entity.maxHeight : -1,
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                try {
                                    int start = item1.key;
                                    int end = item2.key;
                                    if (start == -1 && end == -1) {
                                        heightLayout.setRightText(getString(R.string.no_limit));
                                    } else if (start == -1) {
                                        heightLayout.setRightText(end+Constants.UNIT_HEIGHT+"以下");
                                    } else if (end == -1) {
                                        heightLayout.setRightText(start+Constants.UNIT_HEIGHT+"以上");
                                    } else if (end > start) {
                                        heightLayout.setRightText(item1.value + "-" + item2.value + Constants.UNIT_HEIGHT);
                                        params.put(KEY_REQUIRE_MIN_HEIGHT, String.valueOf(item1.key));
                                        entity.minHeight = item1.key;
                                        params.put(KEY_REQUIRE_MAX_HEIGHT, String.valueOf(item2.key));
                                        entity.maxHeight = item2.key;
                                    } else {
                                        ToastUtils.toast(getContext(), getString(R.string.please_select_correct_range));
                                    }

                                    if(start==-1 || end==-1 || end>start){
                                        params.put(KEY_REQUIRE_MIN_HEIGHT, String.valueOf(item1.key));
                                        entity.minHeight = item1.key;
                                        params.put(KEY_REQUIRE_MAX_HEIGHT, String.valueOf(item2.key));
                                        entity.maxHeight = item2.key;
                                    }

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                break;
            case R.id.require_income_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_INCOME);
                pickView = DictionaryUtil.showRequireYearIncomeWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        entity != null ? entity.salary : -1,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                incomeLayout.setRightText(item.value);

                                params.put(KEY_REQUIRE_MIN_SALARY, String.valueOf(item.key));
                                entity.salary = item.key;
                            }
                        });
                break;
            case R.id.require_work_city_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_WORK_CITY);
                pickView = DictionaryUtil.showNativePlacePickerView(getContext(),getResources().getString(R.string.work_city),
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        entity.workCity != 0 ? entity.workCity : ZhenaiApplication.mCityCode,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                String city = CityConvertUtil.convertName(item1.value, item2.value);
                                workCityLayout.setRightText(city);
                                params.put(KEY_REQUIRE_WORK_CITY, String.valueOf(item2.key));
                                entity.workCity = item2.key;
                            }
                        });

                break;
            case R.id.require_native_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_NATIVE_CITY);
                pickView = DictionaryUtil.showNativePlacePickerView(getContext(),getResources().getString(R.string.native_place),
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        entity.workCity != 0 ? entity.hometown : ZhenaiApplication.mCityCode,
                        new DictionaryUtil.OnDoubleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item1, DictionaryBean item2) {
                                String city = CityConvertUtil.convertName(item1.value, item2.value);
                                nativeProvinceLayout.setRightText(city);
                                params.put(KEY_REQUIRE_NATIVE_CITY, String.valueOf(item2.key));
                                entity.hometown = item2.key;
                            }
                        });
                break;

            case R.id.require_marry_status_item_layout:
                EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_MARRY_STATUS);
                pickView = DictionaryUtil.showMaritalStatusWheelPickerView(getContext(),
                        DataDictionaryHelper.FirstItemType.NO_LIMIT,
                        entity != null ? entity.marryState : -1,
                        new DictionaryUtil.OnSingleSelectItemsCallback() {
                            @Override
                            public void onSelectItems(DictionaryBean item) {
                                marryStatusLayout.setRightText(item.value);
                                params.put(KEY_REQUIRE_MARRY_STATE, String.valueOf(item.key));
                                entity.marryState = item.key;
                            }
                        });
                break;
            case R.id.zhenai_lib_titlebar_right_text:
                if (params.size() > 0) {
                    mPresenter.modifyRequireInfo(params);
                } else {
                    ToastUtils.toast(getContext(), R.string.modify_nothing);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pickView != null && pickView.isShowing()) {
            pickView.dismiss();
        }
    }

    private RequireInfoEntity entity;

    @Override
    public void showItemInfo(BaseEntity baseEntity) {
        if (!(baseEntity instanceof RequireInfoEntity)) {
            return;
        }
        entity = (RequireInfoEntity) baseEntity;

        ageLayout.setRightText(entity.getAge());
        heightLayout.setRightText(entity.getHeight());
        incomeLayout.setRightText(entity.getSalary());
        workCityLayout.setRightText(entity.getWorkCity());
        nativeProvinceLayout.setRightText(entity.getNativePlace());
        marryStatusLayout.setRightText(entity.getMarryState());

    }

    @Override
    public void modifySuccess() {
        EventStatistics.recordLog(ResourceKey.REQUIRE_INFO_MODIFY_PAGE,ResourceKey.RequireInfoModifyPage.REQUIRE_INFO_PAGE_FINISH_NUM);
        params.clear();
//        loadRequireInfo();
        finish();
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_other_require_modify_layout;
    }
}
