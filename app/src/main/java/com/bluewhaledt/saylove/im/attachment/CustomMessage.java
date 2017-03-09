package com.bluewhaledt.saylove.im.attachment;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.NIMAntiSpamOption;

import java.util.Map;

/**
 * 自定义Message 仅用于界面显示
 */

public class CustomMessage implements IMMessage {
    private String mUuid;
    private String mSessionId;
    private String mFromAccount;
    private MsgAttachment mAttachment;
    private Map<String, Object> mLocalExtension;

    @Override
    public String getUuid() {
        return mUuid;
    }

    @Override
    public boolean isTheSame(IMMessage message) {
        return false;
    }

    @Override
    public String getSessionId() {
        return mSessionId;
    }

    @Override
    public SessionTypeEnum getSessionType() {
        return SessionTypeEnum.P2P;
    }

    @Override
    public String getFromNick() {
        return null;
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return MsgTypeEnum.custom;
    }

    @Override
    public MsgStatusEnum getStatus() {
        return null;
    }

    @Override
    public void setStatus(MsgStatusEnum status) {

    }

    @Override
    public void setDirect(MsgDirectionEnum direct) {

    }

    @Override
    public MsgDirectionEnum getDirect() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public void setFromAccount(String account) {
        mFromAccount = account;
    }

    @Override
    public String getFromAccount() {
        return mFromAccount;
    }

    @Override
    public void setAttachment(MsgAttachment attachment) {
        this.mAttachment = attachment;
    }

    @Override
    public MsgAttachment getAttachment() {
        return mAttachment;
    }

    @Override
    public AttachStatusEnum getAttachStatus() {
        return null;
    }

    @Override
    public void setAttachStatus(AttachStatusEnum attachStatus) {

    }

    @Override
    public CustomMessageConfig getConfig() {
        return null;
    }

    @Override
    public void setConfig(CustomMessageConfig config) {

    }

    @Override
    public Map<String, Object> getRemoteExtension() {
        return null;
    }

    @Override
    public void setRemoteExtension(Map<String, Object> remoteExtension) {

    }

    @Override
    public Map<String, Object> getLocalExtension() {
        return mLocalExtension;
    }

    @Override
    public void setLocalExtension(Map<String, Object> localExtension) {
        this.mLocalExtension = localExtension;
    }

    @Override
    public String getPushContent() {
        return null;
    }

    @Override
    public void setPushContent(String pushContent) {

    }

    @Override
    public Map<String, Object> getPushPayload() {
        return null;
    }

    @Override
    public void setPushPayload(Map<String, Object> pushPayload) {

    }

    @Override
    public MemberPushOption getMemberPushOption() {
        return null;
    }

    @Override
    public void setMemberPushOption(MemberPushOption pushOption) {

    }

    @Override
    public boolean isRemoteRead() {
        return false;
    }

    @Override
    public int getFromClientType() {
        return 0;
    }

    @Override
    public NIMAntiSpamOption getNIMAntiSpamOption() {
        return null;
    }

    @Override
    public void setNIMAntiSpamOption(NIMAntiSpamOption nimAntiSpamOption) {

    }
}
