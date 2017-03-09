package com.bluewhaledt.saylove.widget;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.util.AudioRecorderUtil;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public class VoiceRecordBtn extends Button implements View.OnTouchListener {

    private AudioRecorderUtil audioRecorderUtil;
    private BaseDialog recordVoiceDialog;
    private Rect mRect;
    private boolean inside;
    //    private int minAudioLength;
    private int currentRecordLength;


    public VoiceRecordBtn(Context context) {
        super(context, null);
    }

    public VoiceRecordBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    //标记此次事件是否整个忽略
    private boolean isPermissionAction = false;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!PermissionHelper.hasPermissions(getContext(), Manifest.permission.RECORD_AUDIO)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ((BaseActivity) getContext()).mPermissionHelper.showMessageDialog("语音权限没有授权");
                isPermissionAction = true;
            }
            return false;
        }
        if (isPermissionAction) {//此处过滤掉授权框出现时的录音动作
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                isPermissionAction = false;
            }
            return false;
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnVoiceRecordCallback != null) {
                    mOnVoiceRecordCallback.startRecord(true);
                }
                handleDownEvent();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mRect == null) {
                    mRect = new Rect();
                    view.getLocalVisibleRect(mRect);
                }
                inside = mRect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
                if (inside) {
                    recordVoiceDialog.setText(R.id.tv_msg, R.string.fragment_chat_detail_dialog_voice_record);
                    recordVoiceDialog.setTextColor(R.id.tv_msg, Color.WHITE);
                } else {
                    recordVoiceDialog.setText(R.id.tv_msg, R.string.fragment_chat_detail_dialog_voice_release);
                    recordVoiceDialog.setTextColor(R.id.tv_msg, Color.RED);
                }


                break;
            case MotionEvent.ACTION_UP:
                recordVoiceDialog.dismiss();
                recordVoiceDialog = null;
                if (inside) {
                    audioRecorderUtil.stopRecord();
                } else {
                    audioRecorderUtil.cancelRecord();
                }
                setText(getResources().getString(R.string.fragment_chat_detail_voice_btn));
                if (mOnVoiceRecordCallback != null) {
                    mOnVoiceRecordCallback.startRecord(false);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                recordVoiceDialog.dismiss();
                recordVoiceDialog = null;
                audioRecorderUtil.cancelRecord();
                setText(getResources().getString(R.string.fragment_chat_detail_voice_btn));
                if (mOnVoiceRecordCallback != null) {
                    mOnVoiceRecordCallback.startRecord(false);
                }
                break;
        }
        return true;
    }

    private void handleDownEvent() {
        if (audioRecorderUtil == null) {
            audioRecorderUtil = new AudioRecorderUtil();
            audioRecorderUtil.setOnAudioStatusUpdateListener(new AudioRecorderUtil.OnAudioStatusUpdateListener() {
                @Override
                public void onUpdate(double db, long time) {
                    currentRecordLength = (int) (time / 1000);
                    StringBuilder sb = new StringBuilder();
                    if (currentRecordLength / 60 == 0) {
                        sb.append("00:");
                        if (currentRecordLength % 60 >= 10) {
                            sb.append(currentRecordLength % 60);
                        } else {
                            sb.append("0").append(currentRecordLength % 60);
                        }

                    } else {
                        if (currentRecordLength / 60 < 10) {
                            sb.append("0").append(currentRecordLength / 60).append(":");
                            if (currentRecordLength % 60 >= 10) {
                                sb.append(currentRecordLength % 60);
                            } else {
                                sb.append("0").append(currentRecordLength % 60);
                            }
                        } else {
                            sb.append(currentRecordLength / 60).append(":");
                            if (currentRecordLength % 60 >= 10) {
                                sb.append(currentRecordLength % 60);
                            } else {
                                sb.append("0").append(currentRecordLength % 60);
                            }
                        }
                    }

                    recordVoiceDialog.setText(R.id.tv_record_time, sb.toString());

                    int level = (int) (db % 5);
                    if (level < 0) {
                        level = 1;
                    } else if (level > 4) {
                        level = 4;
                    }
                    switch (level) {
                        case 1:
                            recordVoiceDialog.setImageResource(R.id.iv_voice_volume, R.mipmap.fragment_chat_detail_voice_1);
                            break;
                        case 2:
                            recordVoiceDialog.setImageResource(R.id.iv_voice_volume, R.mipmap.fragment_chat_detail_voice_2);
                            break;
                        case 3:
                            recordVoiceDialog.setImageResource(R.id.iv_voice_volume, R.mipmap.fragment_chat_detail_voice_3);
                            break;
                        case 4:
                            recordVoiceDialog.setImageResource(R.id.iv_voice_volume, R.mipmap.fragment_chat_detail_voice_4);
                            break;
                    }
                }

                @Override
                public void onStop(String filePath) {
                    if (mOnVoiceRecordCallback != null) {
                        mOnVoiceRecordCallback.onRecordSuccess(filePath, currentRecordLength * 1000);
                    }
                    currentRecordLength = 0;
                }
            });
        }

        recordVoiceDialog = new BaseDialog(getContext())
                .setCustomerContent(R.layout.fragment_chat_detail_record_voice)
                .setDialogSize(145.0f, 145.0f)
                .setWindowBackground(R.color.transparent);
        recordVoiceDialog.setCanCancelOutside(false);
        recordVoiceDialog.show();
        audioRecorderUtil.startRecord();
    }

    private OnVoiceRecordCallback mOnVoiceRecordCallback;

    public void setOnVoiceRecordCallback(OnVoiceRecordCallback onVoiceRecordCallback) {
        this.mOnVoiceRecordCallback = onVoiceRecordCallback;
    }

    public interface OnVoiceRecordCallback {
        void onRecordSuccess(String filePath, long length);
        void startRecord(boolean start);
    }
}
