package com.bluewhaledt.saylove.ui.info.info_modify;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.base.widget.picker_view.OptionsPickerView;
import com.bluewhaledt.saylove.base.widget.picker_view.view.BasePickerView;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.DictionaryBean;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.InfoService;
import com.bluewhaledt.saylove.ui.info.base.BaseProfileFragment;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.info.widget.ItemLayout;
import com.bluewhaledt.saylove.util.DataDictionaryHelper;
import com.bluewhaledt.saylove.util.DictionaryUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

/**
 * Created by rade.chan on 2016/11/29.
 */

public class ModifyVerifyFragment extends BaseProfileFragment implements View.OnClickListener {

    private ImageView mBannerView;
    private ItemLayout selectItemLayout;
    private ItemLayout extraItemLayout;
    private ImageView exampleImgView;
    private ImageView uploadImgView;
    private ImageView addBtnView;
    private ImageView waterMarkIV;
    private Button commitBtn;
    private TextView mVerifyTipsView;
    private TextView verifyStatusTV;

    private String avatarUrl;
    private HashMap<String, String> params = new HashMap<>();
    private VerifyInfoEntity verifyInfoEntity;
    private BasePickerView pickerView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initViews();
        initListener();
        Observable<ZAResponse<VerifyInfoEntity>> observable = ZARetrofit.getService(getActivity(), InfoService.class).queryVerifyDetail(sourceFrom);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VerifyInfoEntity>>(new ZASubscriberListener<ZAResponse<VerifyInfoEntity>>() {
            @Override
            public void onSuccess(ZAResponse<VerifyInfoEntity> response) {
                verifyInfoEntity = response.data;
                initViewData();
            }
        }));
    }

    private void init() {
        switch (sourceFrom) {
            case FROM_EDUCATION_VERIFY:
                EventStatistics.recordLog(ResourceKey.EDUCATION_VERIFY_INFO_PAGE,ResourceKey.EDUCATION_VERIFY_INFO_PAGE);
                break;
            case FROM_CAR_VERIFY:
                EventStatistics.recordLog(ResourceKey.CAR_VERIFY_INFO_PAGE,ResourceKey.CAR_VERIFY_INFO_PAGE);
                break;
            case FROM_HOUSE_VERIFY:
                EventStatistics.recordLog(ResourceKey.HOUSE_VERIFY_INFO_PAGE,ResourceKey.HOUSE_VERIFY_INFO_PAGE);
                break;
        }
    }

    @Override
    public boolean isSetFilePrivate() {
        return true;
    }

    private boolean isCanModify(boolean showToast) {
        if (verifyInfoEntity != null) {
            switch (sourceFrom) {
                case FROM_EDUCATION_VERIFY:
                    if (verifyInfoEntity.degreeVerifyStatus == 1) {//审核中
                        if (showToast) {
                            ToastUtils.toast(getContext(), getString(R.string.is_verify_detail, getString(R.string.education)));
                        }
                        verifyStatusTV.setVisibility(View.VISIBLE);

                        verifyStatusTV.setText(verifyInfoEntity.degreeVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.GONE);

                        return false;
                    } else if (verifyInfoEntity.degreeVerifyStatus == 3) {//审核不通过
                        verifyStatusTV.setVisibility(View.VISIBLE);
                        verifyInfoEntity.degreeVerifyStatusDesc = "审核不通过，请重新上传";
                        verifyStatusTV.setText(verifyInfoEntity.degreeVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.VISIBLE);
                        return true;
                    } else if (verifyInfoEntity.degreeVerifyStatus == 2) {//审核通过
                        addBtnView.setVisibility(View.GONE);
                        return true;
                    }

                    break;
                case FROM_CAR_VERIFY:
                    if (verifyInfoEntity.carVerifyStatus == 1) {//审核中
                        if (showToast) {
                            ToastUtils.toast(getContext(), getString(R.string.is_verify_detail, getString(R.string.item_car_verify)));
                        }
                        verifyStatusTV.setVisibility(View.VISIBLE);
                        verifyStatusTV.setText(verifyInfoEntity.carVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.GONE);
                        return false;

                    } else if (verifyInfoEntity.carVerifyStatus == 3) {//审核不通过
                        verifyStatusTV.setVisibility(View.VISIBLE);
                        verifyInfoEntity.carVerifyStatusDesc = "审核不通过，请重新上传";
                        verifyStatusTV.setText(verifyInfoEntity.carVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.VISIBLE);
                        return true;
                    } else if (verifyInfoEntity.carVerifyStatus == 2) {//审核通过
                        addBtnView.setVisibility(View.GONE);
                    }
                    break;
                case FROM_HOUSE_VERIFY:
                    if (verifyInfoEntity.houseVerifyStatus == 1) {//审核中
                        if (showToast) {
                            ToastUtils.toast(getContext(), getString(R.string.is_verify_detail, getString(R.string.item_house_verify)));
                        }
                        verifyStatusTV.setVisibility(View.VISIBLE);
                        verifyStatusTV.setText(verifyInfoEntity.houseVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.GONE);

                        return false;
                    } else if (verifyInfoEntity.houseVerifyStatus == 3) {//审核不通过
                        verifyStatusTV.setVisibility(View.VISIBLE);
                        verifyInfoEntity.houseVerifyStatusDesc = "审核不通过，请重新上传";
                        verifyStatusTV.setText(verifyInfoEntity.houseVerifyStatusDesc);
                        addBtnView.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.VISIBLE);
                        return true;
                    } else if (verifyInfoEntity.houseVerifyStatus == 2) {//审核通过
                        addBtnView.setVisibility(View.GONE);
                    }

                    break;
            }
        }
        return true;
    }


    private void initViews() {
        mBannerView = find(R.id.banner_view);
        selectItemLayout = find(R.id.select_item_view);
        extraItemLayout = find(R.id.extra_select_item_view);
        exampleImgView = find(R.id.example_img_view);
        uploadImgView = find(R.id.upload_img_view);
        addBtnView = find(R.id.add_btn_img_view);
        commitBtn = find(R.id.commit_btn);
        mVerifyTipsView = find(R.id.verify_tips_view);
        verifyStatusTV = find(R.id.tv_verify_status);
        waterMarkIV = find(R.id.iv_water_mark);

    }

    @Override
    public void getPhotoSuccess(String path) {
        super.getPhotoSuccess(path);
        avatarUrl = path;
        if (!TextUtils.isEmpty(avatarUrl)) {
            ImageLoaderFactory.getImageLoader().with(getContext()).
                    load(avatarUrl).into(uploadImgView);
        }
        addBtnView.setVisibility(View.GONE);
        params.put(KEY_PHOTO_NAMES, avatarUrl);
        verifyStatusTV.setVisibility(View.GONE);

        switch (sourceFrom) {
            case FROM_EDUCATION_VERIFY:
                verifyInfoEntity.degreeVerifyStatus = 0;
                break;
            case FROM_CAR_VERIFY:
                verifyInfoEntity.carVerifyStatus = 0;
                break;
            case FROM_HOUSE_VERIFY:
                verifyInfoEntity.houseVerifyStatus = 0;
                break;
        }
    }

    @Override
    public void uploadSuccess(int type, String srcPath, String cosPath) {
       // super.uploadSuccess(type, srcPath, cosPath);
        closeUploadProgress();
        ToastUtils.toast(getActivity(),getString(R.string.verify_commit_tips));
        avatarUrl = null;
        EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VERIFY));
        finish();
    }

    @Override
    public void uploadFail(int type, String msg) {
        super.uploadFail(type, msg);
    }

    private void initListener() {
        selectItemLayout.setOnClickListener(this);
        extraItemLayout.setOnClickListener(this);
        addBtnView.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        verifyStatusTV.setOnClickListener(this);
        uploadImgView.setOnClickListener(this);
    }

    private void initViewData() {

        String pic = "";
        switch (sourceFrom) {
            case FROM_CAR_VERIFY:
                mBannerView.setImageResource(R.mipmap.car_verify_banner);
                selectItemLayout.setLeftText(getString(R.string.car_model));
                mVerifyTipsView.setText(getString(R.string.car_verify_tips));
                setTitle(R.string.item_car_verify);
                if (!isCanModify(false)) {
                    selectItemLayout.setRightIconVisibility(View.GONE);
                }

                exampleImgView.setImageResource(R.mipmap.verified_car);
                if (verifyInfoEntity.carVerifyStatus != 3) {
                    pic = verifyInfoEntity.carPicUrl;
                    if (verifyInfoEntity.carVerifyStatus == 2){
                        waterMarkIV.setVisibility(View.VISIBLE);
                    }
                }

                if (!TextUtils.isEmpty(verifyInfoEntity.brand.trim())) {
                    selectItemLayout.setRightText(verifyInfoEntity.brand);
                    params.put(KEY_CAR_BRAND, verifyInfoEntity.brand);
                } else {
                    selectItemLayout.setRightText(getString(R.string.please_input));
                }


                break;
            case FROM_EDUCATION_VERIFY:
                mBannerView.setImageResource(R.mipmap.education_verify_banner);
                selectItemLayout.setLeftText(getString(R.string.graduation_school));
                mVerifyTipsView.setText(getString(R.string.education_verify_tips));
                extraItemLayout.setVisibility(View.VISIBLE);
                setTitle(R.string.item_education_verify);
                if (!isCanModify(false)) {
                    selectItemLayout.setRightIconVisibility(View.GONE);
                    extraItemLayout.setRightIconVisibility(View.GONE);
                }

                extraItemLayout.setRightText(verifyInfoEntity.educationDesc);

                exampleImgView.setImageResource(R.mipmap.verified_aducation);
                if (verifyInfoEntity.degreeVerifyStatus != 3) {
                    pic = verifyInfoEntity.degreePicUrl;
                    if (verifyInfoEntity.degreeVerifyStatus == 2){
                        waterMarkIV.setVisibility(View.VISIBLE);
                    }
                }


                if (verifyInfoEntity.education != 0) {
                    params.put(KEY_EDUCATION, verifyInfoEntity.education + "");
                }

                if (!TextUtils.isEmpty(verifyInfoEntity.academy.trim())) {
                    selectItemLayout.setRightText(verifyInfoEntity.academy);
                    params.put(KEY_ACADEMY, verifyInfoEntity.academy);
                } else {
                    selectItemLayout.setRightText(getString(R.string.please_input));
                }

                break;
            case FROM_HOUSE_VERIFY:
                mBannerView.setImageResource(R.mipmap.house_verify_banner);
                selectItemLayout.setLeftText(getString(R.string.house_place));
                mVerifyTipsView.setText(getString(R.string.house_verify_tips));
                setTitle(R.string.item_house_verify);
                if (!isCanModify(false)) {
                    selectItemLayout.setRightIconVisibility(View.GONE);
                }
                selectItemLayout.setRightText(verifyInfoEntity.house);
                exampleImgView.setImageResource(R.mipmap.verified_house);
                if (verifyInfoEntity.houseVerifyStatus != 3) {
                    pic = verifyInfoEntity.housePicUrl;
                    if (verifyInfoEntity.houseVerifyStatus == 2){
                        waterMarkIV.setVisibility(View.VISIBLE);
                    }
                }

                if (verifyInfoEntity.city != 0) {
                    params.put(KEY_HOUSE_CITY, verifyInfoEntity.city + "");
                }

                break;

        }

        ImageLoaderFactory.getImageLoader().with(getContext()).load(pic).into(uploadImgView);


    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_profile_verify_layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_item_view:
                switch (sourceFrom) {
                    case FROM_CAR_VERIFY:
                        if (isCanModify(true)) {
                            EventStatistics.recordLog(ResourceKey.CAR_VERIFY_INFO_PAGE,ResourceKey.CarVerifyPage.CAR_VERIFY_PAGE_FILL_CAR);
                            showEditTextLayout(getString(R.string.car_model), selectItemLayout.getRightText());
                        }
                        break;
                    case FROM_HOUSE_VERIFY:
                        if (isCanModify(true)) {
                            EventStatistics.recordLog(ResourceKey.HOUSE_VERIFY_INFO_PAGE,ResourceKey.HouseVerifyPage.HOUSE_VERIFY_PAGE_CITY);
                            pickerView = DictionaryUtil.showPlacePickerView(getContext(),"房产所在地",
                                    DataDictionaryHelper.FirstItemType.NORMAL,
                                    verifyInfoEntity.city != 0 ? verifyInfoEntity.city : ZhenaiApplication.mCityCode,
                                    new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
                                        @Override
                                        public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                                            if (item3 == null) {
                                                selectItemLayout.setRightText(item1.value + " " + item2.value);
                                                params.put(KEY_HOUSE_CITY, String.valueOf(item2.key));
                                            } else {
                                                selectItemLayout.setRightText(item2.value + " " + item3.value);
                                                params.put(KEY_HOUSE_CITY, String.valueOf(item3.key));
                                            }
                                        }
                                    });
                        }
                        break;
                    case FROM_EDUCATION_VERIFY:
                        if (isCanModify(true)) {
                            EventStatistics.recordLog(ResourceKey.EDUCATION_VERIFY_INFO_PAGE,ResourceKey.EducationVerifyPage.EDUCATION_VERIFY_PAGE_SCHOOL);
                            showEditTextLayout(getString(R.string.graduation_school), selectItemLayout.getRightText());
                        }
                        break;
                }
                break;
            case R.id.extra_select_item_view:
                if (sourceFrom == FROM_EDUCATION_VERIFY && isCanModify(true)) {
                    EventStatistics.recordLog(ResourceKey.EDUCATION_VERIFY_INFO_PAGE,ResourceKey.EducationVerifyPage.EDUCATION_VERIFY_PAGE_EDUCATION);
                    int key = -2;
                    if (params.get(KEY_EDUCATION) != null) {
                        key = Integer.parseInt(params.get(KEY_EDUCATION));
                    }

                    pickerView = DictionaryUtil.showEducationWheelPickerView(getContext(),
                            DataDictionaryHelper.FirstItemType.NONE,
                            key,
                            new DictionaryUtil.OnSingleSelectItemsCallback() {
                                @Override
                                public void onSelectItems(DictionaryBean item) {
                                    extraItemLayout.setRightText(item.value);
                                    verifyInfoEntity.educationDesc = item.value;
                                    params.put(KEY_EDUCATION, String.valueOf(item.key));

                                }
                            });

                }
                break;
            case R.id.add_btn_img_view:
            case R.id.tv_verify_status:
            case R.id.upload_img_view:
                if (isCanModify(true)) {
                    int type = PHOTO_TYPE_COMMON;
                    switch (sourceFrom) {
                        case FROM_CAR_VERIFY:
                            type = PHOTO_TYPE_CAR;
                            EventStatistics.recordLog(ResourceKey.CAR_VERIFY_INFO_PAGE,ResourceKey.CarVerifyPage.CAR_VERIFY_PAGE_UPLOAD_PHOTO);
                            break;
                        case FROM_HOUSE_VERIFY:
                            EventStatistics.recordLog(ResourceKey.HOUSE_VERIFY_INFO_PAGE,ResourceKey.HouseVerifyPage.HOUSE_VERIFY_PAGE_UPLOAD_PHOTO);
                            type = PHOTO_TYPE_HOUSE;
                            break;
                        case FROM_EDUCATION_VERIFY:
                            EventStatistics.recordLog(ResourceKey.EDUCATION_VERIFY_INFO_PAGE,ResourceKey.EducationVerifyPage.EDUCATION_VERIFY_PAGE_UPLOAD_PHOTO);
                            type = PHOTO_TYPE_EDUCATION;
                            break;
                    }
                    showPhotoDialog(type);
                }
                break;
            case R.id.commit_btn:
                if (isCanModify(true) && checkCanSubmit()) {
                    switch (sourceFrom) {
                        case FROM_CAR_VERIFY:
                            EventStatistics.recordLog(ResourceKey.CAR_VERIFY_INFO_PAGE,ResourceKey.CarVerifyPage.CAR_VERIFY_PAGE_COMMIT);
                            break;
                        case FROM_HOUSE_VERIFY:
                            EventStatistics.recordLog(ResourceKey.HOUSE_VERIFY_INFO_PAGE,ResourceKey.HouseVerifyPage.HOUSE_VERIFY_PAGE_COMMIT);
                            break;
                        case FROM_EDUCATION_VERIFY:
                            EventStatistics.recordLog(ResourceKey.EDUCATION_VERIFY_INFO_PAGE,ResourceKey.EducationVerifyPage.EDUCATION_VERIFY_PAGE_COMMIT);
                            break;
                    }
                    showProgress();
                    uploadPicture(params);
                }
                break;
        }
    }


    private void showPhotoDialog(final int type) {
        new BaseDialog(getActivity())
                .setBtnPanelView(R.layout.dialog_album_select_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        dialogInterface.dismiss();
                        switch (btnId) {
                            case R.id.take_photo_view:
                                setCurrentSelectPicType(type);
                                goCameraPhoto(System.currentTimeMillis() + Constants.SAVE_PIC_FORMAT);
                                break;
                            case R.id.from_album_view:
                                setCurrentSelectPicType(type);
                                goAlbum();
                                break;
                        }
                    }
                }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pickerView != null && pickerView.isShowing()) {
            pickerView.dismiss();
        }
    }

    /**
     * 填写对话框
     */
    private void showEditTextLayout(String title, String initValue) {
        final EditText editText = new EditText(getContext());
        if (sourceFrom == FROM_EDUCATION_VERIFY) {
            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    String speChat = "[\u4e00-\u9fa5a-zA-Z0-9]+";
                    Pattern pattern = Pattern.compile(speChat);
                    Matcher matcher = pattern.matcher(source.toString());
                    if (matcher.find()) {
                        return null;
                    } else {
                        return "";
                    }
                }
            };
            editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});

        } else {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }

        if (!TextUtils.isEmpty(initValue.trim()) && !initValue.equals(getString(R.string.please_input))) {
            editText.setText(initValue);
            try {
                editText.setSelection(initValue.length());
            }catch (IndexOutOfBoundsException e){

            }

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(title)
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
                                String value = editText.getText().toString().trim();
                                if (!TextUtils.isEmpty(value)) {
                                    selectItemLayout.setRightText(value);

                                    switch (sourceFrom) {
                                        case FROM_EDUCATION_VERIFY:
                                            params.put(KEY_ACADEMY, value);
                                            verifyInfoEntity.academy = value;
                                            break;
                                        case FROM_CAR_VERIFY:

                                            params.put(KEY_CAR_BRAND, value);
                                            verifyInfoEntity.brand = value;

                                            break;
                                    }
                                }

                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }

    private boolean checkCanSubmit() {
        switch (sourceFrom) {
            case FROM_EDUCATION_VERIFY:
                if (TextUtils.isEmpty(avatarUrl)) {
                    ToastUtils.toast(getContext(), R.string.no_select_education_pic);
                    return false;
                }
                if (params.containsKey(KEY_ACADEMY) && !TextUtils.isEmpty(params.get(KEY_ACADEMY))
                        && params.containsKey(KEY_EDUCATION) && !TextUtils.isEmpty(params.get(KEY_EDUCATION))) {
                    params.put(KEY_PHOTO_TYPE, String.valueOf(PHOTO_TYPE_EDUCATION));
                    return true;
                }
                break;
            case FROM_CAR_VERIFY:
                if (TextUtils.isEmpty(avatarUrl)) {
                    ToastUtils.toast(getContext(), R.string.no_select_car_pic);
                    return false;
                }
                if (params.containsKey(KEY_CAR_BRAND) && !TextUtils.isEmpty(params.get(KEY_CAR_BRAND))) {
                    params.put(KEY_PHOTO_TYPE, String.valueOf(PHOTO_TYPE_CAR));
                    return true;
                }
                break;
            case FROM_HOUSE_VERIFY:
                if (TextUtils.isEmpty(avatarUrl)) {
                    ToastUtils.toast(getContext(), R.string.no_select_house_pic);
                    return false;
                }
                if (params.containsKey(KEY_HOUSE_CITY) && !TextUtils.isEmpty(params.get(KEY_HOUSE_CITY))) {
                    params.put(KEY_PHOTO_TYPE, String.valueOf(PHOTO_TYPE_HOUSE));
                    return true;
                }
                break;
        }
        ToastUtils.toast(getActivity(), R.string.please_fill_completion);
        return false;
    }
}
