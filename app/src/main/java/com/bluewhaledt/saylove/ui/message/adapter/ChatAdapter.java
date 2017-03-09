package com.bluewhaledt.saylove.ui.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.im.attachment.BaseCustomAttachment;
import com.bluewhaledt.saylove.im.attachment.CustomMsgType;
import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.info.constant.ComeFrom;
import com.bluewhaledt.saylove.ui.message.emoji.MoonUtil;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
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

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private final int CHAT_TYPE_OTHER_TEXT = 1;

    private final int CHAT_TYPE_ME_TEXT = 2;

    private final int CHAT_TYPE_OTHER_VOICE = 3;

    private final int CHAT_TYPE_ME_VOICE = 4;

    /**
     * 是否锁定对方消息
     */
    private final int CHAT_TYPE_OTHER_LOCK = 5;

    /**
     * 我主动发送视频话题消息
     */
    private final int CHAT_TYPE_ME_VIDEO_TOPIC = 6;

    /**
     * 收到对方发送过来的视频话题消息
     */
    private final int CHAT_TYPE_OTHER_VIDEO_TOPIC = 7;


    private Context mContext;

    private ZAArray<ChatData> mChats;

    private AudioPlayer player;

    private ExtendedData otherSideInfo;

    private BaseFragment fragment;

    private boolean isLock;

    private ZAAccount account;


    public ChatAdapter(BaseFragment fragment, ZAArray<ChatData> chats, boolean isLock) {
        if (chats != null) {
            mChats = chats;
        } else {
            mChats = new ZAArray<>();
        }

        mContext = fragment.getActivity();
        player = new AudioPlayer(mContext);
        this.fragment = fragment;
        this.isLock = isLock;
        account = AccountManager.getInstance().getZaAccount();
    }

    private SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    public void addDataHead(ArrayList<ChatData> data) {
        initDate(data);
        mChats.addAll(0, data);
        notifyDataSetChanged();
    }

    public void setOtherSideInfo(ExtendedData otherSideInfo) {
        this.otherSideInfo = otherSideInfo;
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

    public void updateAttachmentMessage(int index) {
        notifyItemChanged(index);
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

    public void addData(ChatData data) {
        ArrayList<ChatData> chats = new ArrayList<>();
        chats.add(data);
        initDate(chats);
        mChats.add(data);
        notifyDataSetChanged();

//        String date = "";
//        if (mChats.size() > 0) {
//            IMMessage preMessage = mChats.get(mChats.size() - 1).message;
//            if (data.message.getTime() - preMessage.getTime() > 30 * 60 * 1000) {
//                date = format.format(new Date(data.message.getTime()));
//            }
//        } else {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(data.message.getTime());
//
//            if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
//                date = format.format(new Date(data.message.getTime()));
//            } else {
//                date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
//            }
//        }
//        data.setDate(date);
//        if (data.message.getDirect() == MsgDirectionEnum.In && data.message.getMsgType() == MsgTypeEnum.text) {
//            data.setReadStatus(true);
//        }
//        mChats.add(data);
//        notifyDataSetChanged();

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
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View child;
        switch (viewType) {
            case CHAT_TYPE_OTHER_TEXT:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_other, parent, false);
                return new OtherViewHolder(child);
            case CHAT_TYPE_ME_TEXT:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_me, parent, false);
                return new MeViewHolder(child);
            case CHAT_TYPE_OTHER_VOICE:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_other_voice, parent, false);
                return new OtherVoiceViewHolder(child);
            case CHAT_TYPE_ME_VOICE:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_me_voice, parent, false);
                return new MeVoiceViewHolder(child);
            case CHAT_TYPE_OTHER_LOCK:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_other_lock, parent, false);
                return new OtherLockViewHolder(child);
            case CHAT_TYPE_ME_VIDEO_TOPIC:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_me_video_topic, parent, false);
                return new MeVideoTopicViewHolder(child);
            case CHAT_TYPE_OTHER_VIDEO_TOPIC:
                child = inflater.inflate(R.layout.fragment_chat_detail_list_item_other_video_topic, parent, false);
                return new OtherVideoTopicViewHolder(child);
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
            case CHAT_TYPE_OTHER_VOICE:
                initOtherVoiceData((OtherVoiceViewHolder) holder, chat);
                break;
            case CHAT_TYPE_ME_VOICE:
                initMeVoiceData((MeVoiceViewHolder) holder, chat);
                break;
            case CHAT_TYPE_OTHER_LOCK:
                initOtherLock((OtherLockViewHolder) holder, chat);
                break;
            case CHAT_TYPE_OTHER_VIDEO_TOPIC:
                initOtherVideoTopicData((OtherVideoTopicViewHolder) holder, chat);
                break;
            case CHAT_TYPE_ME_VIDEO_TOPIC:
                initMeVideoTopicData((MeVideoTopicViewHolder) holder, chat);
                break;
        }

    }

    private void initMeVideoTopicData(MeVideoTopicViewHolder holder, ChatData chat) {
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        VideoTopicAttachment attachment = (VideoTopicAttachment) chat.message.getAttachment();
        holder.videoTitleTV.setText(attachment.content.title);

        MoonUtil.identifyFaceExpression(mContext, holder.chatTV, attachment.content.content, ImageSpan.ALIGN_BOTTOM);
        holder.chatTV.setMovementMethod(LinkMovementMethod.getInstance());

        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_man;
        } else {
            defaultAvatar = R.mipmap.default_avatar_feman;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(otherSideInfo.otherAvatar)
                .placeholder(account.sex == 1 ? R.mipmap.default_avatar_feman:R.mipmap.default_avatar_man)
//                .load(attachment.content.pic)
                .into(holder.videoPicIV);

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);
        holder.videoContainer.setOnClickListener(this);
        holder.videoContainer.setTag(attachment);
//        holder.avatarIV.setTag(myAvatar);
    }

    private void initOtherVideoTopicData(OtherVideoTopicViewHolder holder, ChatData chat) {
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }
        VideoTopicAttachment attachment = (VideoTopicAttachment) chat.message.getAttachment();
        holder.videoTitleTV.setText(attachment.content.title);

        MoonUtil.identifyFaceExpression(mContext, holder.chatTV, attachment.content.content, ImageSpan.ALIGN_BOTTOM);
        holder.chatTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.videoContainer.setOnClickListener(this);
        holder.videoContainer.setTag(attachment);
        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_man;
        } else {
            defaultAvatar = R.mipmap.default_avatar_feman;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(account.avator)
                .placeholder(defaultAvatar)
                .into(holder.videoPicIV);

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);

    }

    private void initOtherLock(OtherLockViewHolder holder, ChatData chat) {
        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_feman;
        } else {
            defaultAvatar = R.mipmap.default_avatar_man;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);
//        holder.chatTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PayActivity.class);
//                mContext.startActivity(intent);
//
//            }
//        });
    }


    private void initOtherVoiceData(OtherVoiceViewHolder holder, ChatData chat) {
        AudioAttachment attachment = (AudioAttachment) chat.message.getAttachment();
        holder.voiceLengthTV.setText(attachment.getDuration() / 1000 + "\"");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.msgContentLL.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(mContext, 80);
        if (attachment.getDuration() / 1000 > 3) {
            layoutParams.width += (attachment.getDuration() / 1000 - 3) * cellWidth;
            if (layoutParams.width > DensityUtils.dp2px(mContext, 250)) {
                layoutParams.width = DensityUtils.dp2px(mContext, 250);
            }
            holder.msgContentLL.setLayoutParams(layoutParams);
        }
        holder.msgContentLL.setLayoutParams(layoutParams);


        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_feman;
        } else {
            defaultAvatar = R.mipmap.default_avatar_man;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        if (chat.getReadStatus()) {
            holder.redPointIV.setVisibility(View.GONE);
        } else {
            holder.redPointIV.setVisibility(View.VISIBLE);
        }
//        AnimationDrawable anim = (AnimationDrawable) holder.voiceAnimIV.getDrawable();
//        anim.stop();
////        anim.selectDrawable(0);
//        if (chat.isAnimPlaying) {
//            holder.voiceAnimIV.setEnabled(true);
//            if (!anim.isRunning()) {
//                anim.start();
//            }
//        }else{
//            holder.voiceAnimIV.setEnabled(false);
//        }
        if (chat.isAnimPlaying) {
            holder.voiceAnimIV.setImageResource(R.drawable.fragment_chat_detail_voice_other_anim);
            AnimationDrawable anim = (AnimationDrawable) holder.voiceAnimIV.getDrawable();
            if (!anim.isRunning()) {
                anim.start();
            }
        } else {//此处的写法是为了兼容小米3等手机在滑动页面时，语音消息动画会自动播放的问题。当不播放动画时，将其背景设备为一张图片并将之前播放的动画停止
            if (holder.voiceAnimIV.getDrawable() != null && holder.voiceAnimIV.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable anim = (AnimationDrawable) holder.voiceAnimIV.getDrawable();
                anim.stop();
            }
            holder.voiceAnimIV.setImageResource(R.mipmap.fragment_chat_detail_other_sound_3);
        }

        holder.msgContentLL.setOnClickListener(this);
        holder.msgContentLL.setTag(holder);
        holder.msgContentLL.setTag(R.id.ll_msg_content, chat);

        AttachStatusEnum attachStatus = chat.message.getAttachStatus();
        if (attachStatus == AttachStatusEnum.transferred) {
            holder.msgContentLL.setBackgroundResource(R.drawable.fragment_chat_detail_msg_other_selector);
        } else {
            holder.msgContentLL.setBackgroundResource(R.drawable.fragment_chat_detail_other_msg_bg_press);
        }
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);

    }

    private void initMeVoiceData(MeVoiceViewHolder holder, ChatData chat) {
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_man;
        } else {
            defaultAvatar = R.mipmap.default_avatar_feman;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);

        if (chat.isAnimPlaying) {
            holder.voiceAnimIV.setImageResource(R.drawable.fragment_chat_detail_voice_me_anim);//此处的写法是为了兼容小米3等手机在滑动页面时，语音消息动画会自动播放的问题
            AnimationDrawable anim = (AnimationDrawable) holder.voiceAnimIV.getDrawable();
            if (!anim.isRunning()) {
                anim.start();
            }
        } else {
            //此处的写法是为了兼容小米3等手机在滑动页面时，语音消息动画会自动播放的问题。当不播放动画时，将其背景设备为一张图片并将之前播放的动画停止
            if (holder.voiceAnimIV.getDrawable() != null && holder.voiceAnimIV.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable anim = (AnimationDrawable) holder.voiceAnimIV.getDrawable();
                anim.stop();
            }
            holder.voiceAnimIV.setImageResource(R.mipmap.fragment_chat_detail_me_sound_3);
        }
        AudioAttachment attach = (AudioAttachment) chat.message.getAttachment();
        holder.voiceLengthTV.setText(attach.getDuration() / 1000 + "\"");
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
        holder.msgContentLL.setOnClickListener(this);
        holder.msgContentLL.setTag(holder);
        holder.msgContentLL.setTag(R.id.ll_msg_content, chat);

        AttachStatusEnum attachStatus = chat.message.getAttachStatus();
        if (attachStatus == AttachStatusEnum.transferred) {
            holder.msgContentLL.setBackgroundResource(R.drawable.fragment_chat_detail_msg_me_selector);
        } else {
            holder.msgContentLL.setBackgroundResource(R.drawable.fragment_chat_detail_me_msg_bg_press);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.msgContentLL.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(mContext, 80);
        if (attach.getDuration() / 1000 > 3) {
            layoutParams.width += (attach.getDuration() / 1000 - 3) * cellWidth;
            if (layoutParams.width > DensityUtils.dp2px(mContext, 250)) {
                layoutParams.width = DensityUtils.dp2px(mContext, 250);
            }
            holder.msgContentLL.setLayoutParams(layoutParams);
        }
        holder.msgContentLL.setLayoutParams(layoutParams);

        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);
    }


    private int cellWidth = 3;

    private void initOtherData(OtherViewHolder holder, ChatData chat) {
        MoonUtil.identifyFaceExpression(mContext, holder.chatTV, chat.message.getContent(), ImageSpan.ALIGN_BOTTOM);
        holder.chatTV.setMovementMethod(LinkMovementMethod.getInstance());
        if (TextUtils.isEmpty(chat.getDate())) {
            holder.dateLL.setVisibility(View.GONE);
        } else {
            holder.dateLL.setVisibility(View.VISIBLE);
            holder.dateTV.setText(chat.getDate());
        }

        int defaultAvatar;
        if (account.sex == 1) {
            defaultAvatar = R.mipmap.default_avatar_feman;
        } else {
            defaultAvatar = R.mipmap.default_avatar_man;
        }

        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);
    }

    private void initMeData(MeViewHolder holder, ChatData chat) {
        MoonUtil.identifyFaceExpression(mContext, holder.chatTV, chat.message.getContent(), ImageSpan.ALIGN_BOTTOM);
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
                .with(mContext)
                .load(PhotoUrlUtils.format(chat.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(defaultAvatar)
                .circle()
                .into(holder.avatarIV);
        holder.avatarIV.setOnClickListener(this);
        holder.avatarIV.setTag(R.id.iv_avatar, chat);
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatData chat = mChats.get(position);
        if (chat.message.getDirect() == MsgDirectionEnum.Out) {
            if (chat.message.getMsgType() == MsgTypeEnum.text) {
                return CHAT_TYPE_ME_TEXT;
            } else if (chat.message.getMsgType() == MsgTypeEnum.audio) {
                return CHAT_TYPE_ME_VOICE;
            } else if (chat.message.getMsgType() == MsgTypeEnum.custom) {
                BaseCustomAttachment attachment = (BaseCustomAttachment) chat.message.getAttachment();
                if (attachment.entity.type == CustomMsgType.VIDEO_TOPIC) {
                    return CHAT_TYPE_ME_VIDEO_TOPIC;
                } else {
                    return CHAT_TYPE_ME_TEXT;
                }

            } else {
                return CHAT_TYPE_ME_TEXT;
            }
        } else {
            if (isLock) {
                return CHAT_TYPE_OTHER_LOCK;
            } else {
                if (chat.message.getMsgType() == MsgTypeEnum.text) {
                    return CHAT_TYPE_OTHER_TEXT;
                } else if (chat.message.getMsgType() == MsgTypeEnum.audio) {
                    return CHAT_TYPE_OTHER_VOICE;
                } else if (chat.message.getMsgType() == MsgTypeEnum.custom) {
                    BaseCustomAttachment attachment = (BaseCustomAttachment) chat.message.getAttachment();
                    if (attachment.entity.type == CustomMsgType.VIDEO_TOPIC) {
                        return CHAT_TYPE_OTHER_VIDEO_TOPIC;
                    } else {
                        return CHAT_TYPE_OTHER_TEXT;
                    }
                } else {
                    return CHAT_TYPE_OTHER_TEXT;
                }
            }

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
                    } else if (view.getTag() instanceof MeVoiceViewHolder) {
                        MeVoiceViewHolder holder = (MeVoiceViewHolder) view.getTag();
                        ChatData data = (ChatData) view.getTag(R.id.iv_send_failed);
                        data.message.setStatus(MsgStatusEnum.sending);
                        IMUtil.resendMessage(data.message);
                        initMeVoiceData(holder, data);
                    }

                }

                break;
            case R.id.ll_msg_content:
                hideSoftInput();
                if (view.getTag() != null) {
                    final ChatData chatData = (ChatData) view.getTag(R.id.ll_msg_content);
                    View itemView = null;
                    if (view.getTag() instanceof MeVoiceViewHolder) {
                        MeVoiceViewHolder holder = (MeVoiceViewHolder) view.getTag();
                        itemView = holder.itemView;
                        holder.voiceAnimIV.setImageResource(R.drawable.fragment_chat_detail_voice_me_anim);
                    } else if (view.getTag() instanceof OtherVoiceViewHolder) {
                        OtherVoiceViewHolder holder = (OtherVoiceViewHolder) view.getTag();
                        itemView = holder.itemView;
                        holder.voiceAnimIV.setImageResource(R.drawable.fragment_chat_detail_voice_other_anim);
                    }
                    if (itemView != null) {
                        if (chatData.message.getMsgType() == MsgTypeEnum.audio) {
                            AudioAttachment attach = (AudioAttachment) chatData.message.getAttachment();
                            ImageView voiceAnimIV = (ImageView) itemView.findViewById(R.id.iv_voice_anim);
                            if (chatData.message.getDirect() == MsgDirectionEnum.In) {
                                ImageView redPointIV = (ImageView) itemView.findViewById(R.id.iv_red_point);
                                redPointIV.setVisibility(View.GONE);
                                chatData.setReadStatus(true);
                            }
                            final AnimationDrawable anim = (AnimationDrawable) voiceAnimIV.getDrawable();
                            if (chatData.isAnimPlaying) {
                                player.stop();
                                anim.stop();
                                anim.selectDrawable(0);
                                chatData.isAnimPlaying = false;
                            } else {
                                stopOtherPlayAnim();
//                                if (!anim.isRunning()) {
//
//                                }
                                anim.start();
                                if (attach.getPath() != null) {
                                    player.setDataSource(attach.getPath());
                                } else {
                                    player.setDataSource(attach.getUrl());
                                }

                                player.setOnPlayListener(new OnPlayListener() {
                                    @Override
                                    public void onPrepared() {

                                    }

                                    @Override
                                    public void onCompletion() {
                                        player.stop();
                                        anim.stop();
                                        anim.selectDrawable(0);
                                        chatData.isAnimPlaying = false;
                                        if (view.getTag() instanceof MeVoiceViewHolder) {
                                            initMeVoiceData((MeVoiceViewHolder) view.getTag(), chatData);
                                        } else if (view.getTag() instanceof OtherVoiceViewHolder) {
                                            initOtherVoiceData((OtherVoiceViewHolder) view.getTag(), chatData);
                                        }
                                    }

                                    @Override
                                    public void onInterrupt() {
                                        player.stop();
                                        anim.stop();
                                        anim.selectDrawable(0);
                                        chatData.isAnimPlaying = false;
                                        if (view.getTag() instanceof MeVoiceViewHolder) {
                                            initMeVoiceData((MeVoiceViewHolder) view.getTag(), chatData);
                                        } else if (view.getTag() instanceof OtherVoiceViewHolder) {
                                            initOtherVoiceData((OtherVoiceViewHolder) view.getTag(), chatData);
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        player.stop();
                                        anim.stop();
                                        anim.selectDrawable(0);
                                        chatData.isAnimPlaying = false;
                                        if (view.getTag() instanceof MeVoiceViewHolder) {
                                            initMeVoiceData((MeVoiceViewHolder) view.getTag(), chatData);
                                        } else if (view.getTag() instanceof OtherVoiceViewHolder) {
                                            initOtherVoiceData((OtherVoiceViewHolder) view.getTag(), chatData);
                                        }
                                    }

                                    @Override
                                    public void onPlaying(long curPosition) {
                                        DebugUtils.d("player", "curPosition:" + curPosition);
                                    }
                                });
                                player.start(AudioManager.STREAM_MUSIC);
                                chatData.isAnimPlaying = true;
                            }
                        }
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

                    } else {
                        EventStatistics.recordLog(ResourceKey.CHAT_PAGE,ResourceKey.ChatPage.CLICK_AVATAR);
                        bundle.putString(IntentConstants.USER_ID, otherSideInfo.otherMemberId + "");
                    }
                    fragment.startFragment(ThirdPartyFragment.class, bundle);

                }

                break;
            case R.id.ll_video_topic_container:
                hideSoftInput();
                if (view.getTag() != null) {
                    VideoTopicAttachment attachment = (VideoTopicAttachment) view.getTag();
                    ActivityRedirectUtil.gotoAvPlayActivity(mContext, attachment.content.videoId);
                }
                break;
        }
    }

    public void stopOtherPlayAnim() {
        for (int i = 0; i < mChats.size(); i++) {
            ChatData chatData = mChats.get(i);
            if (chatData.isAnimPlaying) {
                chatData.isAnimPlaying = false;
                notifyItemChanged(i);
            }
        }
    }

    public void stopPlay() {
        if (player != null && player.isPlaying()) {
            player.stop();
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

    private static class MeVoiceViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV;
        View dateLL;
        ImageView avatarIV;
        TextView voiceLengthTV;
        ImageView voiceAnimIV;
        ProgressBar progressBar;
        ImageView sendFailedIV;
        View msgContentLL;

        MeVoiceViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            voiceLengthTV = (TextView) itemView.findViewById(R.id.tv_voice_length);
            voiceAnimIV = (ImageView) itemView.findViewById(R.id.iv_voice_anim);
            dateLL = itemView.findViewById(R.id.ll_date);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            sendFailedIV = (ImageView) itemView.findViewById(R.id.iv_send_failed);
            msgContentLL = itemView.findViewById(R.id.ll_msg_content);
        }
    }

    private static class OtherVoiceViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV;
        View dateLL;
        ImageView avatarIV;
        TextView voiceLengthTV;
        ImageView voiceAnimIV;
        ImageView redPointIV;
        View msgContentLL;

        OtherVoiceViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            voiceLengthTV = (TextView) itemView.findViewById(R.id.tv_voice_length);
            voiceAnimIV = (ImageView) itemView.findViewById(R.id.iv_voice_anim);
            redPointIV = (ImageView) itemView.findViewById(R.id.iv_red_point);
            dateLL = itemView.findViewById(R.id.ll_date);
            msgContentLL = itemView.findViewById(R.id.ll_msg_content);
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

    private static class OtherLockViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarIV;
        TextView chatTV;

        OtherLockViewHolder(View itemView) {
            super(itemView);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
            chatTV = (TextView) itemView.findViewById(R.id.tv_chat);
        }
    }

    private static class OtherVideoTopicViewHolder extends RecyclerView.ViewHolder {
        TextView chatTV;
        ImageView videoPicIV;
        TextView videoTitleTV;
        View videoContainer;
        TextView dateTV;
        View dateLL;
        ImageView avatarIV;

        public OtherVideoTopicViewHolder(View itemView) {
            super(itemView);
            chatTV = (TextView) itemView.findViewById(R.id.tv_chat);
            videoPicIV = (ImageView) itemView.findViewById(R.id.iv_video_pic);
            videoTitleTV = (TextView) itemView.findViewById(R.id.tv_video_title);
            videoContainer = itemView.findViewById(R.id.ll_video_topic_container);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            dateLL = itemView.findViewById(R.id.ll_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    private static class MeVideoTopicViewHolder extends RecyclerView.ViewHolder {
        TextView chatTV;
        ImageView videoPicIV;
        TextView videoTitleTV;
        View videoContainer;
        TextView dateTV;
        View dateLL;
        ImageView avatarIV;

        public MeVideoTopicViewHolder(View itemView) {
            super(itemView);
            chatTV = (TextView) itemView.findViewById(R.id.tv_chat);
            videoPicIV = (ImageView) itemView.findViewById(R.id.iv_video_pic);
            videoTitleTV = (TextView) itemView.findViewById(R.id.tv_video_title);
            videoContainer = itemView.findViewById(R.id.ll_video_topic_container);
            dateTV = (TextView) itemView.findViewById(R.id.tv_date);
            dateLL = itemView.findViewById(R.id.ll_date);
            avatarIV = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }


    private void hideSoftInput() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.getCurrentFocus() != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }


    }

}



















