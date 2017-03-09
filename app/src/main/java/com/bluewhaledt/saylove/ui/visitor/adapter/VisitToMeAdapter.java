package com.bluewhaledt.saylove.ui.visitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class VisitToMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private boolean isVip;

    //心动点赞
//    private final int HEART_BEAT_PRAISE = 1;
//    //视频点赞
//    private final int VIDEO_PRAISE = 2;


    private final int NORMAL_TYPE = 1;
    private final int TIPS_VIEW_TYPE = 2;
    private final int TOP_TIPS_VIEW_ITEM = 3;


    private ZAArray<Visitor> data;

    private int totalMsgCount;

    public VisitToMeAdapter(Context context, ArrayList<Visitor> data) {
        this.context = context;
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        if (data == null || data.isEmpty()) {
            this.data = new ZAArray<>();
        } else {
            this.data = new ZAArray<>();
            this.data.addAll(data);
            if (!isVip){

                if (data.size() > 1){
                    this.data.add(0, new Visitor(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                    this.data.add(2, new Visitor(1001));//增加第一项数据，用户占位，显示"共有XX位个人对你感兴趣..."这个item
                }else if (data.size() == 1){
                    this.data.add(0,new Visitor(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                }
            }
        }

    }

    public Visitor getItemAtPosition(int position) {
        return data.get(position);
    }

    /**
     * 当前会员不是vip时，需要展示在"共有XX位个人对你感兴趣..."中的xx数据
     *
     * @param totalMsgCount 多少个
     */
    public void setTotalMsgCount(int totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
        if (!isVip) {
            notifyItemChanged(2);
        }
    }

    public void updateVipStatus() {
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
    }

    public void addData(ArrayList<Visitor> data) {
        boolean isEmpty = false;
        if (this.data.isEmpty() && !data.isEmpty()) {
            isEmpty = true;
        }
        this.data.addAll(data);

        if (isEmpty && !isVip) {
            if (data.size() > 1){
                this.data.add(0, new Visitor(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                this.data.add(2, new Visitor(1001));//增加第一项数据，用户占位，显示"共有XX位个人对你感兴趣..."这个item
            }else if (data.size() == 1){
                this.data.add(0,new Visitor(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        if (viewType == TIPS_VIEW_TYPE) {
            itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_list_tips_item, parent, false);
            return new TipsViewHolder(itemView);
        } else if (viewType == TOP_TIPS_VIEW_ITEM) {
            itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_tips, parent, false);
            return new TopTipsViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.fragment_visitor_list_item, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TIPS_VIEW_TYPE) {
            initTipsView((TipsViewHolder) holder, position);
        } else if (type == TOP_TIPS_VIEW_ITEM) {

        } else {
            initNormalView((ItemViewHolder) holder, position);
        }
    }

    private void initNormalView(ItemViewHolder holder, int position) {
        Visitor itemData = this.data.get(position);
        int defaultAvatar;
        if (itemData.sex == 2) {
            defaultAvatar = R.mipmap.default_avatar_feman;
        } else {
            defaultAvatar = R.mipmap.default_avatar_man;
        }

        if (!itemData.read) {
            holder.readPointIV.setVisibility(View.VISIBLE);
        } else {
            holder.readPointIV.setVisibility(View.GONE);
        }

        if (!isVip) {
            if (position != 1) {
                ImageLoaderFactory.getImageLoader()
                        .with(context)
                        .load(PhotoUrlUtils.format(itemData.avatar, PhotoUrlUtils.TYPE_3))
                        .placeholder(defaultAvatar)
                        .blur(15)
                        .circle()
                        .into(holder.avatarIV);
                holder.lockIV.setVisibility(View.VISIBLE);
                holder.userInfoContainerView.setVisibility(View.GONE);
                holder.notVipTipsTV.setVisibility(View.VISIBLE);
            } else {//第一条免费是可见的
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
                holder.readPointIV.setVisibility(View.GONE);
            }
        } else {
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
        }



        holder.nikeNameTV.setText(itemData.nickName);
        holder.timeTV.setText(itemData.viewTime);
    }

    @Override
    public int getItemViewType(int position) {
        if (!isVip) {
            if (position == 0) {
                return TOP_TIPS_VIEW_ITEM;
            } else if (position == 2) {
                return TIPS_VIEW_TYPE;
            }
        }
        return NORMAL_TYPE;
    }

    private void initTipsView(TipsViewHolder holder, int position) {
        String count = totalMsgCount + "";
        String text = context.getString(R.string.fragment_heart_beat_tips_view_text);
        int index = text.indexOf("%s");
        String textFormat = String.format(text, count);
        SpannableString ss = new SpannableString(textFormat);
        ss.setSpan(new AbsoluteSizeSpan(20, true), index, count.length() + index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_f0cf62)), index, count.length() + index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.messageTV.setText(ss);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV;

        public TipsViewHolder(View itemView) {
            super(itemView);
            messageTV = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    public static class TopTipsViewHolder extends RecyclerView.ViewHolder {

        public TopTipsViewHolder(View itemView) {
            super(itemView);
        }
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
