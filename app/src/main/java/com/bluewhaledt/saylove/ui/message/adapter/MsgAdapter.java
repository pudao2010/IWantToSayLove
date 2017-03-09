package com.bluewhaledt.saylove.ui.message.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.message.entity.MessageData;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/17.
 */

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private final int HEART_BEAT_ITEM_VIEW = 1;

    private final int VISITOR_ITEM_VIEW = 2;

    private final int MESSAGE_ITEM_VIEW = 3;

    private Context mContext;

    private ZAArray<MessageData> mData;

    private int heartBeatUnReadCount;

    private int visitorUnReadCount;

    private BaseFragment fragment;

    public MsgAdapter(BaseFragment fragment, ZAArray<MessageData> data) {
        mContext = fragment.getActivity();
        this.fragment = fragment;
        if (data == null) {
            mData = new ZAArray<>();
        } else {
            mData = data;
        }


    }

    public MessageData getItemAtPosition(int position) {
        return mData.get(position);
    }

    public void addData(ArrayList<MessageData> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public MessageData isContains(MessageData data){
        if (mData.indexOf(data) != -1){
            return mData.get(mData.indexOf(data));
        }
        return null;
    }

    public void addAndUpdateData(ArrayList<MessageData> data) {
        for (MessageData messageData : data) {
            if (mData.contains(messageData)) {
                mData.remove(messageData);

            }
            mData.add(0, messageData);
        }
        notifyDataSetChanged();
    }

    public void addData(MessageData data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void removeData(MessageData data) {
        if (mData.contains(data)) {
            mData.remove(data);
            notifyDataSetChanged();
        }

    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void updateHeartBeatRedDot(int count) {
        heartBeatUnReadCount = count;
        notifyItemChanged(0);
    }

    public void updateVisitorRedDot(int count) {
        visitorUnReadCount = count;
        notifyItemChanged(1);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View child;
        switch (viewType) {
            case HEART_BEAT_ITEM_VIEW:
                child = inflater.inflate(R.layout.fragment_message_of_list_item_heart_beat, parent, false);
                return new HeartBeatViewHolder(child);
            case VISITOR_ITEM_VIEW:
                child = inflater.inflate(R.layout.fragment_message_of_list_item_visitor, parent, false);
                return new VisitorViewHolder(child);
            case MESSAGE_ITEM_VIEW:
                child = inflater.inflate(R.layout.fragment_message_of_list_item_message, parent, false);
                return new MessageViewHolder(child);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            HeartBeatViewHolder heartBeatViewHolder = (HeartBeatViewHolder) holder;
            if (heartBeatUnReadCount > 0) {
                heartBeatViewHolder.redPointIV.setVisibility(View.VISIBLE);
            } else {
                heartBeatViewHolder.redPointIV.setVisibility(View.INVISIBLE);
            }

        } else if (position == 1) {
            VisitorViewHolder visitorViewHolder = (VisitorViewHolder) holder;
            if (visitorUnReadCount > 0) {
                visitorViewHolder.redPointTV.setVisibility(View.VISIBLE);
            } else {
                visitorViewHolder.redPointTV.setVisibility(View.INVISIBLE);
            }
        } else {
            MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            int judgePosition = position - 2;
            if (judgePosition == 0) {
                messageViewHolder.messageContainer.setBackgroundResource(R.drawable.fragment_msg_list_of_top_left_right_corners);
                messageViewHolder.divider.setVisibility(View.VISIBLE);
            } else if (judgePosition == mData.size() - 1) {
                messageViewHolder.messageContainer.setBackgroundResource(R.drawable.fragment_msg_list_of_bottom_left_right_conert);
                messageViewHolder.divider.setVisibility(View.GONE);
            } else {
                messageViewHolder.messageContainer.setBackgroundResource(R.drawable.fragment_msg_list_of_middle_bg);
                messageViewHolder.divider.setVisibility(View.VISIBLE);
            }
            MessageData data = mData.get(judgePosition);

            int defaultAvatar;
            if (data.user.sex == 1){
                defaultAvatar = R.mipmap.default_avatar_man;
            }else{
                defaultAvatar = R.mipmap.default_avatar_feman;
            }
            ImageLoaderFactory.getImageLoader()
                    .with(mContext)
                    .load(PhotoUrlUtils.format(data.user.avatar,PhotoUrlUtils.TYPE_3))
                    .placeholder(defaultAvatar)
                    .circle()
                    .into(messageViewHolder.avatarIV);
            messageViewHolder.avatarIV.setOnClickListener(this);
            messageViewHolder.avatarIV.setTag(R.id.iv_avatar,data);
            if (!data.user.readable) {
                messageViewHolder.lockIV.setVisibility(View.VISIBLE);
                String temp = data.user.age + "岁 " + "| " + data.user.workCity + " | " + data.user.salary;
                messageViewHolder.messageTV.setText(temp);
            } else {
                messageViewHolder.lockIV.setVisibility(View.GONE);
                messageViewHolder.messageTV.setText(data.message);
            }



            messageViewHolder.dateTV.setText(data.date);
            messageViewHolder.nickNameTV.setText(data.user.nickname);
            if (data.unreadCount > 0) {
                if (data.unreadCount < 100) {
                    messageViewHolder.redPointTV.setVisibility(View.VISIBLE);
                    messageViewHolder.redPointTV.setText(data.unreadCount + "");
                } else {
                    messageViewHolder.redPointTV.setVisibility(View.VISIBLE);
                    messageViewHolder.redPointTV.setText(99 + "+");
                }

            } else {
                messageViewHolder.redPointTV.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEART_BEAT_ITEM_VIEW;
        } else if (position == 1) {
            return VISITOR_ITEM_VIEW;
        } else {
            return MESSAGE_ITEM_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 2;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_avatar:
                if (view.getTag(R.id.iv_avatar) != null){
                    MessageData data = (MessageData) view.getTag(R.id.iv_avatar);
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentConstants.USER_ID,data.user.userId + "");
                    fragment.startFragment(ThirdPartyFragment.class,bundle);
                }
                break;
        }
    }

    private static class HeartBeatViewHolder extends RecyclerView.ViewHolder {
        ImageView redPointIV;

        HeartBeatViewHolder(View view) {
            super(view);
            redPointIV = (ImageView) view.findViewById(R.id.iv_red_point);

        }
    }

    private static class VisitorViewHolder extends RecyclerView.ViewHolder {
        ImageView redPointTV;

        VisitorViewHolder(View view) {
            super(view);
            redPointTV = (ImageView) view.findViewById(R.id.iv_red_point);

        }
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView nickNameTV;
        TextView dateTV;
        TextView redPointTV;
        TextView messageTV;
        ImageView avatarIV;
        ImageView lockIV;
        View messageContainer;
        View divider;

        MessageViewHolder(View view) {
            super(view);
            redPointTV = (TextView) view.findViewById(R.id.tv_unread_count);
            avatarIV = (ImageView) view.findViewById(R.id.iv_avatar);
            nickNameTV = (TextView) view.findViewById(R.id.tv_nick_name);
            messageTV = (TextView) view.findViewById(R.id.tv_message);
            dateTV = (TextView) view.findViewById(R.id.tv_date);
            lockIV = (ImageView) view.findViewById(R.id.iv_lock);
            messageContainer = view.findViewById(R.id.fl_message_container);
            divider = view.findViewById(R.id.divider);


        }
    }

}
