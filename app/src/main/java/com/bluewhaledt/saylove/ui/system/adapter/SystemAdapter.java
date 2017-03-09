package com.bluewhaledt.saylove.ui.system.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.info.constant.ComeFrom;
import com.bluewhaledt.saylove.ui.message.emoji.MoonUtil;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhenai-liliyan on 16/11/29.
 */

public class SystemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private final int CHAT_TYPE_OTHER_TEXT = 1;

    private final int CHAT_TYPE_ME_TEXT = 2;

    private BaseActivity activity;

    private ZAArray<ChatData> mChats;
    private ZAAccount account;

    public SystemAdapter(BaseActivity activity, ZAArray<ChatData> chats) {
        if (chats != null) {
            mChats = chats;
        } else {
            mChats = new ZAArray<>();
        }

        this.activity = activity;
        account = AccountManager.getInstance().getZaAccount();
    }

    private SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    public void addDataHead(ArrayList<ChatData> data) {
        initDate(data);
        mChats.addAll(0, data);
        notifyDataSetChanged();
    }

    private void initDate(ArrayList<ChatData> data) {
        for (int position = 0; position < data.size(); position++) {
            String date = null;
            IMMessage message = data.get(position).message;
            if (position - 1 >= 0) {
                IMMessage preMessage = data.get(position - 1).message;
                if (message.getTime() - preMessage.getTime() > 30 * 60 * 1000) {
                    date = format.format(new Date(message.getTime()));
                }
            } else {
                if (mChats.size() > 0) {
                    IMMessage preMessage = mChats.get(mChats.size() - 1).message;
                    if (data.get(position).message.getTime() - preMessage.getTime() > 30 * 60 * 1000) {
                        date = format.format(new Date(data.get(position).message.getTime()));
                    }
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(message.getTime());

                    if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                        date = format.format(new Date(message.getTime()));
                    } else {
                        date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                    }
                }
            }
            data.get(position).setDate(date);
            if (message.getDirect() == MsgDirectionEnum.In && message.getMsgType() == MsgTypeEnum.text) {
                data.get(position).setReadStatus(true);
            }
        }
    }

    public void addData(ArrayList<ChatData> data) {
        initDate(data);
        mChats.addAll(data);
        notifyDataSetChanged();
    }

    public int findChatById(String uuid) {
        int i = 0;
        for (ChatData data : mChats) {
            if (data.message.getUuid().equals(uuid)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int findChatById(ChatData data) {
        return mChats.indexOf(data);
    }

    public void updateChatStatus(ChatData chat) {
        int index = mChats.indexOf(chat);
        if (index != -1) {
            ChatData data = mChats.get(index);
            data.message.setStatus(chat.message.getStatus());
            notifyDataSetChanged();
        }

    }

    public void updateAttachmentMessage(int index) {
        notifyItemChanged(index);
    }

    public void addData(ChatData data) {
        ArrayList<ChatData> chats = new ArrayList<>();
        chats.add(data);
        initDate(chats);
        mChats.add(data);
        notifyDataSetChanged();

    }

    public void clearData() {
        mChats.clear();
        notifyDataSetChanged();
    }

    public ChatData getItemAtPosition(int position) {
        if (mChats != null && mChats.size() - 1 >= position) {
            return mChats.get(position);
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View child;
        switch (viewType) {
            case CHAT_TYPE_OTHER_TEXT:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_other, parent, false);
                return new OtherViewHolder(child);
            case CHAT_TYPE_ME_TEXT:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_me, parent, false);
                return new MeViewHolder(child);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatData chat = mChats.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case CHAT_TYPE_OTHER_TEXT:
                initOtherData((OtherViewHolder) holder, chat);
                break;
            case CHAT_TYPE_ME_TEXT:
                initMeData((MeViewHolder) holder, chat);
                break;
        }

    }

    private void initOtherData(OtherViewHolder holder, ChatData chat) {
        MoonUtil.identifyFaceExpression(activity, holder.chatTV, chat.message.getContent(), ImageSpan.ALIGN_BOTTOM);
        holder.chatTV.setMovementMethod(LinkMovementMethod.getInstance());
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        ImageLoaderFactory.getImageLoader()
                .with(activity).
                load(R.mipmap.saylove_small)
                .circle()
                .into(holder.avatarIV);
//        holder.avatarIV.setOnClickListener(this);
//        holder.avatarIV.setTag(R.id.iv_avatar, chat);
    }

    private void initMeData(MeViewHolder holder, ChatData chat) {
        MoonUtil.identifyFaceExpression(activity, holder.chatTV, chat.message.getContent(), ImageSpan.ALIGN_BOTTOM);
        holder.chatTV.setMovementMethod(LinkMovementMethod.getInstance());
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        holder.progressBar.setVisibility(View.GONE);
        holder.sendFailedIV.setVisibility(View.GONE);
        if (chat.message.getStatus() == MsgStatusEnum.sending) {
            holder.progressBar.setVisibility(View.VISIBLE);
        } else if (chat.message.getStatus() == MsgStatusEnum.fail) {
            holder.sendFailedIV.setVisibility(View.VISIBLE);
            holder.sendFailedIV.setOnClickListener(this);
            holder.sendFailedIV.setTag(R.id.iv_send_failed, chat);
            holder.sendFailedIV.setTag(holder);

        }

        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_man;
        } else {
            defaultAvatar = R.mipmap.default_avatar_feman;
        }
        ImageLoaderFactory.getImageLoader()
                .with(activity)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
//        holder.avatarIV.setOnClickListener(this);
//        holder.avatarIV.setTag(R.id.iv_avatar, chat);
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatData chat = mChats.get(position);
        if (chat.message.getDirect() == MsgDirectionEnum.Out) {
            return CHAT_TYPE_ME_TEXT;
        } else {
            return CHAT_TYPE_OTHER_TEXT;
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.iv_send_failed:
                if (view.getTag() != null) {
                    if (view.getTag() instanceof MeViewHolder) {
                        MeViewHolder holder = (MeViewHolder) view.getTag();
                        ChatData data = (ChatData) view.getTag(R.id.iv_send_failed);
                        data.message.setStatus(MsgStatusEnum.sending);
                        IMUtil.resendMessage(data.message);
                        initMeData(holder, data);
                    }
                }

                break;

            case R.id.iv_avatar:
                hideSoftInput();
                if (view.getTag() != null) {
                    ChatData chatData = (ChatData) view.getTag(R.id.iv_avatar);

                    Bundle bundle = new Bundle();
                    if (chatData.message.getDirect() == MsgDirectionEnum.Out) {
                        bundle.putString(IntentConstants.USER_ID, AccountManager.getInstance().getZaAccount().uid + "");
                        bundle.putInt(ComeFrom.COME_FROM, ComeFrom.COME_FROM_CHAT_DETAIL_PAGE_ME);

                    }
                    activity.startFragment(ThirdPartyFragment.class, bundle);

                }

                break;
        }
    }

    private static class MeViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        View dateLL;
        ImageView avatarIV;
        TextView chatTV;
        ProgressBar progressBar;
        ImageView sendFailedIV;

        MeViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            chatTV = (TextView) itemView.findViewById(R.id.tv_chat);
            dateLL = itemView.findViewById(R.id.ll_date);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            sendFailedIV = (ImageView) itemView.findViewById(R.id.iv_send_failed);
        }
    }

    private static class OtherViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        View dateLL;
        ImageView avatarIV;
        TextView chatTV;


        OtherViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            chatTV = (TextView) itemView.findViewById(R.id.tv_chat);
            dateLL = itemView.findViewById(R.id.ll_date);
        }
    }

    private void hideSoftInput() {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }


    }

}



















