package com.bluewhaledt.saylove.ui.visitor.adapter;

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
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class MyVisitListAdapter extends RecyclerView.Adapter<MyVisitListAdapter.ItemViewHolder> {

    private Context context;


    private ZAArray<Visitor> data;

    public MyVisitListAdapter(Context context, ArrayList<Visitor> data) {
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

    public void addData(ArrayList<Visitor> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public Visitor getItemAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.fragment_visitor_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        initNormalView(holder, position);
    }

    private void initNormalView(ItemViewHolder holder, int position) {
        Visitor itemData = this.data.get(position);
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
        holder.notVipTipsTV.setVisibility(View.GONE);
        holder.userInfoContainerView.setVisibility(View.VISIBLE);
        holder.contentTV.setText(itemData.age + "岁 | " + itemData.workCity + " | " + itemData.salary);
        holder.nikeNameTV.setText(itemData.nickName);
        holder.timeTV.setText(itemData.viewTime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView readPointIV;

        ImageView avatarIV;

        TextView nikeNameTV;

        TextView contentTV;

        TextView timeTV;

        ImageView lockIV;

        TextView notVipTipsTV;

        View userInfoContainerView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            readPointIV = (ImageView) itemView.findViewById(R.id.iv_red_point);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            nikeNameTV = (TextView) itemView.findViewById(R.id.tv_nick_name);
            contentTV = (TextView) itemView.findViewById(R.id.tv_content);
            timeTV = (TextView) itemView.findViewById(R.id.tv_time);
            lockIV = (ImageView) itemView.findViewById(R.id.iv_lock);
            notVipTipsTV = (TextView) itemView.findViewById(R.id.tv_not_vip_tips);
            userInfoContainerView = itemView.findViewById(R.id.ll_user_info_container);
        }
    }
}
