package com.bluewhaledt.saylove.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ObservableRelativeLayout extends RelativeLayout {

	private LayoutChangeListener mLayoutChangeListener;

	public ObservableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		super.onSizeChanged(w, h, oldW, oldH);
		if (oldH != 0) {
			if (h > oldH) {
				if (mLayoutChangeListener != null) {
					mLayoutChangeListener.onKeyboardHind();
				}
			} else {
				if (mLayoutChangeListener != null) {
					mLayoutChangeListener.onKeyboardShow();
				}
			}
		}
	}

	public void setLayoutChangeListener(LayoutChangeListener mLayoutChangeListener) {
		this.mLayoutChangeListener = mLayoutChangeListener;
	}

	public interface LayoutChangeListener {
		void onKeyboardShow();

		void onKeyboardHind();
	}

}
