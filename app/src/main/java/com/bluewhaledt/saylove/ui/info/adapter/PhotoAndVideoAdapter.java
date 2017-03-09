package com.bluewhaledt.saylove.ui.info.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.List;

/**
 * Created by rade.chan on 2016/11/28.
 */

public class PhotoAndVideoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PhotoEntity> mList;
    private boolean mIsShowUpload;
    private OnPhotoAndVideoLayoutListener mListener;

    private static final int TYPE_UPLOAD = 10;

    private int mUploadType;
    private int defaultAvatar = R.mipmap.img_home_detail_avatar_male;

    public PhotoAndVideoAdapter(Context context, List<PhotoEntity> list, boolean isShowUpload, int uploadType) {
        this.mContext = context;
        this.mList = list;
        this.mIsShowUpload = isShowUpload;
        this.mUploadType = uploadType;
        ZAAccount account = AccountManager.getInstance().getZaAccount();
        if (account != null && account.sex == 2) {
            defaultAvatar = R.mipmap.img_home_detail_avatar_female;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_UPLOAD) {
            View uploadView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_view_layout, parent, false);
            return new UploadHolder(uploadView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_layout, parent, false);
            return new PhotoHolder(view);
        }
    }

    public void setOnPhotoAndVideoLayoutListener(OnPhotoAndVideoLayoutListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isUploadPosition(position)) {
            UploadHolder uploadHolder = (UploadHolder) holder;
            uploadHolder.uploadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onUploadClick(mUploadType);
                    }
                }
            });
        } else {
            final int pos = (mIsShowUpload ? position - 1 : position);
            PhotoEntity entity = mList.get(pos);
            PhotoHolder photoHolder = (PhotoHolder) holder;
            ImageLoaderFactory
                    .getImageLoader()
                    .with(mContext).load(PhotoUrlUtils.format(entity.photoUrl, PhotoUrlUtils.TYPE_3))
                    .placeholder(defaultAvatar)
                    .into(photoHolder.avatarView);

            if (entity.type == PhotoEntity.TYPE_VIDEO) {
                photoHolder.playIconView.setVisibility(View.VISIBLE);
//                if(entity.shiedId==2){
//                    photoHolder.maskLayout.setVisibility(View.VISIBLE);
//                }else{
//                    photoHolder.maskLayout.setVisibility(View.GONE);
//                }
            } else {
                photoHolder.playIconView.setVisibility(View.GONE);
         //       photoHolder.maskLayout.setVisibility(View.GONE);
            }
            photoHolder.avatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(mUploadType, pos);
                    }
                }
            });
        }
    }

    private boolean isUploadPosition(int position) {
        return mIsShowUpload && position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isUploadPosition(position)) {
            return TYPE_UPLOAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : (mIsShowUpload ? mList.size() + 1 : mList.size());
    }

    private class UploadHolder extends RecyclerView.ViewHolder {
        View uploadView;

        UploadHolder(View itemView) {
            super(itemView);
            uploadView = itemView.findViewById(R.id.upload_view);
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView avatarView;
        private View playIconView;
        private View maskLayout;

        PhotoHolder(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.avatar_image_view);
            playIconView = itemView.findViewById(R.id.play_icon_view);
            maskLayout=itemView.findViewById(R.id.mask_layout);
        }
    }

    public interface OnPhotoAndVideoLayoutListener {
        void onUploadClick(int uploadType);

        void onItemClick(int uploadType, int position);

    }
}
