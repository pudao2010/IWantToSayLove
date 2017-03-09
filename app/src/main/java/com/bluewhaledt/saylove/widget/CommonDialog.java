package com.bluewhaledt.saylove.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;

/**
 * Created by rade.chan on 2016/12/19.
 */

public class CommonDialog extends BaseDialog {


    public CommonDialog(Context context) {
        super(context);
        setCustomerContent(R.layout.dialog_common_conten_layout);
        setBtnPanelView(R.layout.dialog_common_btn_layout);
    }

    public CommonDialog setContent(String text) {
        TextView contentView=(TextView) findViewById(R.id.content_tv);
        if(contentView!=null) {
            contentView.setText(text);
        }
        return this;
    }

    public CommonDialog setSureBtn(String text) {
        Button sureBtn=(Button)findViewById(R.id.sure_btn);
        if(sureBtn!=null) {
            sureBtn.setText(text);
        }
        return this;
    }

    public CommonDialog hideCancelBtn(){
        Button cancelBtn=(Button)findViewById(R.id.cancel_btn);
        if(cancelBtn!=null) {
            cancelBtn.setVisibility(View.GONE);
        }
        findViewById(R.id.line_divider).setVisibility(View.GONE);
        return this;
    }

    public CommonDialog setCancelBtn(String text) {
        Button cancelBtn=(Button)findViewById(R.id.cancel_btn);
        if(cancelBtn!=null) {
            cancelBtn.setText(text);
        }
        return this;
    }


}
