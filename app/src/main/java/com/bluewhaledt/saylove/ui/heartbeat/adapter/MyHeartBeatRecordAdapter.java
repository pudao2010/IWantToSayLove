package com.bluewhaledt.saylove.ui.heartbeat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class MyHeartBeatRecordAdapter extends RecyclerView.Adapter<MyHeartBeatRecordAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context context;

    //心动点赞
    private final int HEART_BEAT_PRAISE = 1;
    //视频点赞
    private final int VIDEO_PRAISE = 2;

    private ZAArray<HeartBeatItem> data;

    public MyHeartBeatRecordAdapter(Context context, ArrayList<HeartBeatItem> data) {
        this.context = context;
        if (data == null || data.isEmpty()) {
            this.data = new ZAArray<>();
        } else {
            this.data = new ZAArray<>();
            this.data.addAll(data);
        }
    }

    public void clearData() {
        data.clear();
    }

    public void addData(ArrayList<HeartBeatItem> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public HeartBeatItem getItemAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public MyHeartBeatRecordAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_list_item, parent, false);
        return new ItemViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyHeartBeatRecordAdapter.ItemViewHolder holder, int position) {
        initNormalView(holder, position);
    }

    private void initNormalView(ItemViewHolder holder, int position) {
        HeartBeatItem itemData = this.data.get(position);
        int defaultAvatar;
        if (itemData.sex == 2) {
            defaultAvatar = R.mipmap.default_avatar_feman;
        } else {
            defaultAvatar = R.mipmap.default_avatar_man;
        }
        ImageLoaderFactory.getImageLoader()
                .with(context)
                .load(PhotoUrlUtils.format(itemData.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.lockIV.setVisibility(View.GONE);

        holder.nikeNameTV.setText(itemData.nickName);
        String heOrShe = itemData.sex == 2 ? "她":"他";
        if (itemData.likeType == HEART_BEAT_PRAISE) {
            String text = String.format(context.getResources().getString(R.string.fragment_heart_beat_adapter_item_praise_my_text),heOrShe,heOrShe);
            holder.contentTV.setText(text);
            holder.messageContainer.setVisibility(View.GONE);
        } else if (itemData.likeType == VIDEO_PRAISE) {
            String text = String.format(context.getResources().getString(R.string.fragment_heart_beat_adapter_item_praise_video_my_text),heOrShe);
            holder.contentTV.setText(text);
            holder.messageContainer.setVisibility(View.VISIBLE);
            holder.messageTV.setText(itemData.detailContent);
            ImageLoaderFactory.getImageLoader()
                    .with(context)
                    .load(itemData.detailPhoto)
                    .into(holder.messagePicIV);
            holder.messageContainer.setOnClickListener(this);
            holder.messageContainer.setTag(itemData);

        }

        holder.timeTV.setText(itemData.likeTime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message_container:
                if (view.getTag() != null) {
                    HeartBeatItem itemData = (HeartBeatItem) view.getTag();
                    if (itemData.likeType == VIDEO_PRAISE) {
                        ActivityRedirectUtil.gotoAvPlayActivity(context, itemData.detailId + "");
                    }
                }
                break;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView readPointIV;

        ImageView avatarIV;

        TextView nikeNameTV;

        TextView contentTV;

        TextView timeTV;

        View messageContainer;

        ImageView messagePicIV;

        TextView messageTV;

        ImageView lockIV;

        ItemViewHolder(View itemView) {
            super(itemView);
            readPointIV = (ImageView) itemView.findViewById(R.id.iv_red_point);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            nikeNameTV = (TextView) itemView.findViewById(R.id.tv_nick_name);
            contentTV = (TextView) itemView.findViewById(R.id.tv_content);
            timeTV = (TextView) itemView.findViewById(R.id.tv_time);
            messageContainer = itemView.findViewById(R.id.ll_message_container);
            messagePicIV = (ImageView) itemView.findViewById(R.id.iv_message_pic);
            messageTV = (TextView) itemView.findViewById(R.id.tv_message);
            lockIV = (ImageView) itemView.findViewById(R.id.iv_lock);
        }
    }
}
