package com.bluewhaledt.saylove.ui.info.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.widget.custom_imageview.TouchImageView;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private ArrayList<View> mViewList = new ArrayList<View>();
    private Context mContext;
    private OnItemEvent onItemEvent;


    public PhotoPreviewAdapter(Context context, OnItemEvent onBannerItemEvent) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.onItemEvent = onBannerItemEvent;
    }


    public void setPictures(List<PhotoEntity> list) {
        mViewList.clear();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String photoUrl = list.get(i).photoUrl;
                View view = initItemView(photoUrl);
                view.setTag(photoUrl);
                mViewList.add(view);
            }
        }
    }

    /**
     * @param url 内容图片
     */
    private View initItemView(String url) {
        final View view = mInflater.inflate(R.layout.item_photo_preview, null);
        final TouchImageView imageView;
        imageView = (TouchImageView) view.findViewById(R.id.iv_photo);
        ImageLoaderFactory
                .getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(url, PhotoUrlUtils.TYPE_20))
                .placeholder(new ColorDrawable(Color.TRANSPARENT))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemEvent != null){
                    onItemEvent.onItemClick();
                }
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return mViewList.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnItemEvent {
        void onItemClick();
    }

}
