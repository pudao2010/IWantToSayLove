package com.bluewhaledt.saylove.ui.heartbeat.adapter;

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
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class HeartBeatToMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;

    private boolean isVip;

    //心动点赞
    private final int HEART_BEAT_PRAISE = 1;
    //视频点赞
    private final int VIDEO_PRAISE = 2;


    private final int NORMAL_TYPE = 1;
    private final int TIPS_VIEW_TYPE = 2;
    private final int TOP_TIPS_VIEW_ITEM = 3;


    private ZAArray<HeartBeatItem> data;

    private int totalMsgCount;

    public HeartBeatToMeAdapter(Context context,ArrayList<HeartBeatItem> data){
        this.context = context;
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        if (data == null || data.isEmpty()){
            this.data = new ZAArray<>();
        }else{
            this.data = new ZAArray<>();
            this.data.addAll(data);
            if (!isVip){
                if (data.size() > 1){
                    this.data.add(0,new HeartBeatItem(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                    this.data.add(2,new HeartBeatItem(1001));//增加第一项数据，用户占位，显示"共有XX位个人对你感兴趣..."这个item
                }else if (data.size() == 1){
                    this.data.add(0,new HeartBeatItem(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                }

            }
        }

    }

    /**
     * 当前会员不是vip时，需要展示在"共有XX位个人对你感兴趣..."中的xx数据
     * @param totalMsgCount 多少个
     */
    public void setTotalMsgCount(int totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
        if (!isVip){
            notifyItemChanged(2);
        }
    }

    public void clearData(){
        data.clear();
    }

    public void addData(ArrayList<HeartBeatItem> data){
        boolean isEmpty = false;
        if (this.data.isEmpty() && !data.isEmpty()){
            isEmpty = true;
        }
        this.data.addAll(data);

        if (isEmpty && !isVip){
            if (this.data.size() > 1){
                this.data.add(0,new HeartBeatItem(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
                this.data.add(2,new HeartBeatItem(1001));//增加第一项数据，用户占位，显示"共有XX位个人对你感兴趣..."这个item
            }else if (data.size() == 1){
                this.data.add(0,new HeartBeatItem(1000));//增加第一项数据，用户占位，显示"每天免费展示一位对你感兴趣的人"这个item
            }
        }

        notifyDataSetChanged();
    }

    public void updateVipStatus(){
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        notifyDataSetChanged();
    }

    public HeartBeatItem getItemAtPosition(int position){
        return data.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        if (viewType == TIPS_VIEW_TYPE){
            itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_list_tips_item,parent,false);
            return new TipsViewHolder(itemView);
        }else if (viewType == TOP_TIPS_VIEW_ITEM){
            itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_tips,parent,false);
            return new TopTipsViewHolder(itemView);
        }else {
            itemView = inflater.inflate(R.layout.fragment_heart_beat_to_me_list_item,parent,false);
            return new ItemViewHolder(itemView);
        }



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TIPS_VIEW_TYPE){
            initTipsView((TipsViewHolder) holder,position);
        }else if (type == TOP_TIPS_VIEW_ITEM){

        }else{
            initNormalView((ItemViewHolder) holder,position);
        }

    }

    private void initTipsView(TipsViewHolder holder,int position){
        String count = totalMsgCount + "";
        String text = context.getString(R.string.fragment_heart_beat_tips_view_text);
        int index = text.indexOf("%s");
        String textFormat = String.format(text,count);
        SpannableString ss = new SpannableString(textFormat);
        ss.setSpan( new AbsoluteSizeSpan( 20, true ), index, count.length() + index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_f0cf62)),index, count.length() + index,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.messageTV.setText(ss);
    }

    private void initNormalView(ItemViewHolder holder,int position){
        HeartBeatItem itemData = this.data.get(position);

        int defaultAvatar;
        if (itemData.sex == 2){
            defaultAvatar = R.mipmap.default_avatar_feman;
        }else{
            defaultAvatar = R.mipmap.default_avatar_man;
        }

        if (!itemData.read){
            holder.readPointIV.setVisibility(View.VISIBLE);
        }else{
            holder.readPointIV.setVisibility(View.GONE);
        }

        if (!isVip){
            if (position != 1){
                ImageLoaderFactory.getImageLoader()
                        .with(context)
                        .load(PhotoUrlUtils.format(itemData.avatar,PhotoUrlUtils.TYPE_3))
                        .placeholder(defaultAvatar)
                        .blur(15)
                        .circle()
                        .into(holder.avatarIV);
                holder.lockIV.setVisibility(View.VISIBLE);
            }else{//第一条数据不锁并不显示红点
                ImageLoaderFactory.getImageLoader()
                        .with(context)
                        .load(PhotoUrlUtils.format(itemData.avatar,PhotoUrlUtils.TYPE_3))
                        .placeholder(defaultAvatar)
                        .circle()
                        .into(holder.avatarIV);
                holder.lockIV.setVisibility(View.GONE);
                holder.readPointIV.setVisibility(View.GONE);
            }
        }else{
            ImageLoaderFactory.getImageLoader()
                    .with(context)
                    .load(PhotoUrlUtils.format(itemData.avatar,PhotoUrlUtils.TYPE_3))
                    .placeholder(defaultAvatar)
                    .circle()
                    .into(holder.avatarIV);
            holder.lockIV.setVisibility(View.GONE);
        }



        holder.nikeNameTV.setText(itemData.nickName);
        if (itemData.likeType == HEART_BEAT_PRAISE){
            holder.contentTV.setText(context.getResources().getString(R.string.fragment_heart_beat_adapter_item_praise_text));
            holder.messageContainer.setVisibility(View.GONE);
        } else if (itemData.likeType == VIDEO_PRAISE){
            holder.contentTV.setText(context.getResources().getString(R.string.fragment_heart_beat_adapter_item_praise_video_text));
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
    public int getItemViewType(int position) {
        if (!isVip){
            if (position == 0){
                return TOP_TIPS_VIEW_ITEM;
            }else if (position == 2){
                return TIPS_VIEW_TYPE;
            }
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_message_container:
                if (view.getTag() != null){
                    HeartBeatItem itemData = (HeartBeatItem) view.getTag();
                    if (itemData.likeType == VIDEO_PRAISE){
                        ActivityRedirectUtil.gotoAvPlayActivity(context,itemData.detailId + "");
                    }

                }
                break;
        }
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder{

        public TextView messageTV;
        public TipsViewHolder(View itemView) {
            super(itemView);
            messageTV = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    public static class TopTipsViewHolder extends RecyclerView.ViewHolder{

        public TopTipsViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView readPointIV;

        public ImageView avatarIV;

        public TextView nikeNameTV;

        public TextView contentTV;

        public TextView timeTV;

        public View messageContainer;

        public ImageView messagePicIV;

        public TextView messageTV;

        public ImageView lockIV;

        public ItemViewHolder(View itemView) {
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
