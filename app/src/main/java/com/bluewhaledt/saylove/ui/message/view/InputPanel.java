package com.bluewhaledt.saylove.ui.message.view;

import android.Manifest;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.util.StringUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.ui.message.emoji.EmoticonPickerView;
import com.bluewhaledt.saylove.ui.message.emoji.IEmoticonSelectedListener;
import com.bluewhaledt.saylove.ui.message.emoji.MoonUtil;
import com.bluewhaledt.saylove.util.CheckPermissionUtils;
import com.bluewhaledt.saylove.widget.VoiceRecordBtn;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class InputPanel extends FrameLayout implements View.OnClickListener, IEmoticonSelectedListener {

    private ImageView voiceIV;
    private EditText inputET;
    private ImageView smileIV;
    private ImageView keyboardIV;
    private VoiceRecordBtn voiceBtn;
    private ImageView textTypeIV;
    private Button sendBtn;
    private EmoticonPickerView emoticonPickerView;
    private IActionCallback callback;
    private View emotionSwitchContainer;

    public InputPanel(Context context) {
        super(context, null);
    }

    public InputPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View child = inflater.inflate(R.layout.chat_input_panel, this, false);
        addView(child);

        emotionSwitchContainer = findViewById(R.id.fl_emotion);
        emoticonPickerView = (EmoticonPickerView) findViewById(R.id.epv_emoji_picker);
        emoticonPickerView.setWithSticker(true);


        voiceIV = (ImageView) findViewById(R.id.iv_voice_type);
        voiceIV.setOnClickListener(this);
        inputET = (EditText) findViewById(R.id.et_input);
        inputET.setOnClickListener(this);

        //此处防止用户在选择表情时点出输入框后弹出键盘
        inputET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((motionEvent.getAction() == MotionEvent.ACTION_UP) && smileIV.getVisibility() == View.GONE) {
                    inputET.cancelLongPress();
                    return true;
                }
                return false;

            }
        });
        inputET.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MoonUtil.replaceEmoticons(getContext(), s, start, count);
                int editEnd = inputET.getSelectionEnd();
                inputET.removeTextChangedListener(this);
                while (StringUtils.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                inputET.setSelection(editEnd);
                inputET.addTextChangedListener(this);
            }
        });
        smileIV = (ImageView) findViewById(R.id.iv_smile);
        smileIV.setOnClickListener(this);
        keyboardIV = (ImageView) findViewById(R.id.iv_keyboard);
        keyboardIV.setOnClickListener(this);

        voiceBtn = (VoiceRecordBtn) findViewById(R.id.btn_voice);
        voiceBtn.setOnVoiceRecordCallback(new VoiceRecordBtn.OnVoiceRecordCallback() {
            @Override
            public void onRecordSuccess(String filePath, long length) {
                if (callback != null) {
                    callback.onRecordAudioSuccess(filePath, length);
                }
            }

            @Override
            public void startRecord(boolean start) {
                if (callback != null) {
                    callback.startRecord(start);
                }
            }

        });
        textTypeIV = (ImageView) findViewById(R.id.iv_text_type);
        textTypeIV.setOnClickListener(this);
        sendBtn = (Button) findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(this);

        inputET.clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputET.getWindowToken(), 0);
    }

    public void setOnlyTextModel(){
        findViewById(R.id.fl_voice_text_switch).setVisibility(GONE);
        emotionSwitchContainer.setVisibility(GONE);
    }

    public void resetView() {
        hideSoftInput();
        hideEmojiLayout();
        smileIV.setVisibility(View.VISIBLE);
        keyboardIV.setVisibility(View.GONE);
    }

    private void hideSoftInput() {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(inputET.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void hideEmojiLayout() {
        if (emoticonPickerView != null) {
            emoticonPickerView.setVisibility(View.GONE);
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_voice_type:
                voiceBtn.setVisibility(View.VISIBLE);
                inputET.setVisibility(View.GONE);
//                keyboardIV.setVisibility(View.GONE);
//                smileIV.setVisibility(View.GONE);
                textTypeIV.setVisibility(View.VISIBLE);
                voiceIV.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                emotionSwitchContainer.setVisibility(GONE);
                hideSoftInput();
                hideEmojiLayout();
                if (!PermissionHelper.hasPermissions(getContext(), Manifest.permission.RECORD_AUDIO)) {
                    ((BaseActivity) getContext()).mPermissionHelper.requestPermissions("语音权限没有授权", new PermissionHelper.PermissionListener() {
                        @Override
                        public void doAfterGrand(String... permission) {

                        }

                        @Override
                        public void doAfterDenied(String... permission) {

                        }
                    }, Manifest.permission.RECORD_AUDIO);
                }else {
                    CheckPermissionUtils.getInstance().CheckRecordPermission(getContext());
                }


                if (callback != null) {
                    callback.switchTextAndVoiceBtn(false);
                }
                break;
            case R.id.iv_text_type:
                voiceBtn.setVisibility(View.GONE);
                inputET.setVisibility(View.VISIBLE);
                smileIV.setVisibility(View.VISIBLE);
                keyboardIV.setVisibility(View.GONE);
                textTypeIV.setVisibility(View.GONE);
                voiceIV.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);
                emotionSwitchContainer.setVisibility(VISIBLE);
                if (callback != null) {
                    callback.switchTextAndVoiceBtn(true);
                }
                break;
            case R.id.et_input:
                break;
            case R.id.iv_smile:
                smileIV.setVisibility(View.GONE);
                keyboardIV.setVisibility(View.VISIBLE);
                toggleEmojiLayout();
                break;
            case R.id.iv_keyboard:
                smileIV.setVisibility(View.VISIBLE);
                keyboardIV.setVisibility(View.GONE);
                toggleEmojiLayout();
                break;
            case R.id.btn_send:
                String content = inputET.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.toast(getContext(), getResources().getString(R.string.fragment_chat_detail_input_not_empty));
                    return;
                }
                inputET.setText("");
                if (callback != null) {
                    callback.sendBtnClick(content);
                }
                break;

        }
    }

    // 点击表情，切换到表情布局
    private void toggleEmojiLayout() {
        if (emoticonPickerView == null || emoticonPickerView.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
            smileIV.setVisibility(View.VISIBLE);
            keyboardIV.setVisibility(View.GONE);
        }
    }

    private void showEmojiLayout() {
        hideSoftInput();
        inputET.requestFocus();
        emoticonPickerView.setVisibility(View.VISIBLE);
        emoticonPickerView.show(this);
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = inputET.getText();
        if (key.equals("/DEL")) {
            inputET.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = inputET.getSelectionStart();
            int end = inputET.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {
        ToastUtils.toast(getContext(), "categoryName:" + categoryName + ";stickerName:" + stickerName);
    }

    public void setActionCallback(IActionCallback callback) {
        this.callback = callback;
    }

    public interface IActionCallback {
        void sendBtnClick(String content);

        void onRecordAudioSuccess(String filePath, long length);

        void switchTextAndVoiceBtn(boolean isText);

        void startRecord(boolean start);
    }
}
