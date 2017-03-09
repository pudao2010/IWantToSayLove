package com.bluewhaledt.saylove.base.widget.listener;

import android.view.View;

/**
 * Created by zhenai-liliyan on 16/11/29.
 */

public interface OnItemClickListener {
    void onItemClick(View itemView, int position);

    void onLongClickItem(View itemView, int position);
}
