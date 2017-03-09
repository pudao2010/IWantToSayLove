package com.bluewhaledt.saylove.ui.info;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.custom_imageview.ViewPagerFixed;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.entity.MsgEntity;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.InfoService;
import com.bluewhaledt.saylove.ui.info.adapter.PhotoPreviewAdapter;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 图片预览
 *
 * @author huliang
 * @date 2016/10/31
 */
public class PhotoPreviewFragment extends BaseFragment implements View.OnClickListener,
        PhotoPreviewAdapter.OnItemEvent {

    private ViewPagerFixed pagerPhoto;
    private TextView tvCount;
    private PhotoPreviewAdapter mAdapter;
    public List<PhotoEntity> pictures = new ArrayList<>();
    private int pos;
    private boolean isShowDelete;
    private InfoService mService;
    private boolean isDelete;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_photo_preview_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initViewData();
        bindListener();
    }

    @SuppressWarnings("unchecked")
    public void init() {

        List<PhotoEntity> tempList = (List<PhotoEntity>) getArguments().getSerializable(IntentConstants.PHOTOS);
        pos = getArguments().getInt(IntentConstants.POS, pos);
        isShowDelete = getArguments().getBoolean(IntentConstants.SHOW_DELETE_BTN, false);
        if (tempList == null || tempList.isEmpty()) {
            finish();
            return;
        }
        pictures.addAll(tempList);
        if (pos < 0) {
            pos = 0;
        }
        setTitleBarBackground(getResources().getColor(R.color.black));
        showTitleBarUnderline(false);
        setTitleBarLeftBtnImage(R.drawable.icon_back_white_selector);
        if (isShowDelete) {
            setTitleBarRightBtnImage(R.mipmap.icon_viedo_rubbish);
            setTitleBarRightBtnListener(this);
            mService = ZARetrofit.getService(getContext(), InfoService.class);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pagerPhoto = (ViewPagerFixed) view.findViewById(R.id.pager_photo);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
    }


    public void initViewData() {
        mAdapter = new PhotoPreviewAdapter(getActivity(), this);
        mAdapter.setPictures(pictures);

        pagerPhoto.setAdapter(mAdapter);

        if (pictures.size() < 1) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setText((pos + 1) + "/" + pictures.size());
        }
        pagerPhoto.setCurrentItem(pos);
    }


    public void bindListener() {
        pagerPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tvCount.setText((position + 1) + "/" + pictures.size());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (getView() != null) {
            getView().findViewById(R.id.layout).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout:
                finish();
                break;
            case R.id.zhenai_lib_titlebar_right_text:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除")
                        .setMessage("确定要删除图片吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int currentPos = pagerPhoto.getCurrentItem();
                                deletePhoto(currentPos);
                            }
                        });
                builder.create().show();

                break;
        }
    }

    private void notifyDataChange(int pos) {
        mAdapter.setPictures(pictures);
        mAdapter.notifyDataSetChanged();
        if (pictures.size() < 1) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setText((pos + 1) + "/" + pictures.size());
        }
    }

    @Override
    public void onItemClick() {
        finish();
    }


    @Override
    public void onStartFragment() {
        isDelete=false;
    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {
        if(isDelete){
            EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_PHOTOS));
        }
    }
    @Override
    public void onDestroyFragment() {

    }

    public void deletePhoto(final int currentPos){

        if (currentPos >= pictures.size()) {
            return;
        }

        Observable<ZAResponse<MsgEntity>> observable = mService.deletePhoto(pictures.get(currentPos).photoId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<MsgEntity>>
                (new ZASubscriberListener<ZAResponse<MsgEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<MsgEntity> response) {
                        if(response.data!=null && !TextUtils.isEmpty(response.data.msg)){
                            ToastUtils.toast(getContext(),response.data.msg);
                        }
                        isDelete=true;
                        pictures.remove(currentPos);
                        if (currentPos > 0) {
                            pagerPhoto.setCurrentItem(currentPos - 1);
                        }
                        notifyDataChange(pagerPhoto.getCurrentItem());
                        if (pictures.size() == 0) {
                            finish();
                        }
                    }
                }));
    }

}
