package com.bluewhaledt.saylove.ui.video.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/11/28.
 */

public class VideoIndexAdapter extends RecyclerView.Adapter {

    private List<VideoIndexEntity> mList;
    private Context mContext;
    private boolean isShowHeader = false;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 2;
    private String headerTopicText;
    private int itemRadius;
    private OnVideoItemClickListener mListener;
    private int margin;


    public VideoIndexAdapter(Context context, List<VideoIndexEntity> list) {
        this.mContext = context;
        this.mList = list;
        itemRadius = context.getResources().getDimensionPixelSize(R.dimen.video_index_item_radius);
        margin = context.getResources().getDimensionPixelSize(R.dimen.video_index_item_margin);
    }

    public VideoIndexAdapter(Context context, List<VideoIndexEntity> list, String headerTopic) {
        this.mContext = context;
        this.mList = list;
        this.isShowHeader = true;
        this.headerTopicText = headerTopic;
        itemRadius = context.getResources().getDimensionPixelSize(R.dimen.video_index_item_radius);
        margin = context.getResources().getDimensionPixelSize(R.dimen.video_index_item_margin);
    }

    public void addListener(OnVideoItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public boolean isHeader(int position) {
        return isShowHeader && position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_adapter_header_layout, parent, false);
            return new HeaderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_index_layout, parent, false);
            return new VideoIndexHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (isHeader(position)) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.mTopicContentView.setText("关于\"" + headerTopicText + "\"");
        } else {
            int pos = position;
            VideoIndexHolder videoIndexHolder = (VideoIndexHolder) holder;
            if (isShowHeader) {
                pos = position - 1;
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) videoIndexHolder.mItemLayout.getLayoutParams();
                params.topMargin = 0;
                params.bottomMargin=margin;
                videoIndexHolder.mItemLayout.setLayoutParams(params);
            }


            VideoIndexEntity entity = mList.get(pos);
            int defaultGender = R.mipmap.default_avatar_man;
            if (entity.userInfo.sex == 2) {
                defaultGender = R.mipmap.default_avatar_feman;
            }
            ImageLoaderFactory.getImageLoader().with(mContext).load(entity.cover).round(itemRadius)
                    .placeholder(R.mipmap.video_index_default_cover).into(videoIndexHolder.mCoverImageView);
            ImageLoaderFactory.getImageLoader().with(mContext).load(entity.userInfo.avatar).circle().placeholder(defaultGender).
                    into(videoIndexHolder.mAvatarView);
            videoIndexHolder.mLikeNumTv.setText(String.valueOf(entity.likeNum));
            videoIndexHolder.mViewNumTv.setText(String.valueOf(entity.viewNum));
            StringBuilder builder = new StringBuilder();
            if (entity.userInfo.isZM) {
                builder.append(mContext.getString(R.string.verify_id_card));
            }
            if (entity.userInfo.isHouse) {
                if (builder.toString().length() > 0) {
                    builder.append("、");
                }
                builder.append(mContext.getString(R.string.verify_house));
            }
            if (entity.userInfo.isCar) {
                if (builder.toString().length() > 0) {
                    builder.append("、");
                }
                builder.append(mContext.getString(R.string.verify_car));
            }

            if (entity.userInfo.isDegree) {
                if (builder.toString().length() > 0) {
                    builder.append("、");
                }
                builder.append(mContext.getString(R.string.verify_education));
            }

            if (builder.toString().length() > 0) {
                String text = builder.toString();
                builder.setLength(0);
                builder.append("已认证").append(text);
            } else {
                builder.append("未认证");
            }

            videoIndexHolder.mVerifyTv.setText(builder.toString());
            videoIndexHolder.mTopicTv.setText(entity.topicContent);
            if (entity.shield == 1) {
                videoIndexHolder.mMaskView.setVisibility(View.GONE);
            } else {
                videoIndexHolder.mMaskView.setVisibility(View.VISIBLE);
            }
            if (mListener != null) {
                videoIndexHolder.mCoverImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(isShowHeader ? position - 1 : position);
                    }
                });
            }
        }

    }

    public void clearData() {
        mList.clear();
    }

    @Override
    public int getItemCount() {
        if (isShowHeader) {
            return mList == null ? 1 : mList.size() + 1;
        }
        return mList == null ? 0 : mList.size();
    }

    private class VideoIndexHolder extends RecyclerView.ViewHolder {

        private ImageView mCoverImageView;
        private ImageView mAvatarView;
        private TextView mLikeNumTv;
        private TextView mViewNumTv;
        private TextView mVerifyTv;
        private TextView mTopicTv;
        private View mMaskView;
        private RelativeLayout mItemLayout;

        VideoIndexHolder(View itemView) {
            super(itemView);
            mItemLayout = (RelativeLayout) itemView.findViewById(R.id.item_view);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.cover_image_view);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar_view);
            mLikeNumTv = (TextView) itemView.findViewById(R.id.like_num_tv);
            mViewNumTv = (TextView) itemView.findViewById(R.id.view_num_tv);
            mVerifyTv = (TextView) itemView.findViewById(R.id.verify_tv);
            mTopicTv = (TextView) itemView.findViewById(R.id.topic_tv);
            mMaskView = itemView.findViewById(R.id.mask_layout);

        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView mTopicContentView;

        HeaderHolder(View itemView) {
            super(itemView);
            mTopicContentView = (TextView) itemView.findViewById(R.id.topic_content_view);

        }
    }


    public interface OnVideoItemClickListener {
        void onItemClick(int pos);
    }
}
