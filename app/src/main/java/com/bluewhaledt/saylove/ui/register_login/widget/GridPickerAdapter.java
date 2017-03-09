package com.bluewhaledt.saylove.ui.register_login.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 方格式选择器的适配器
 *
 * @author yintaibing
 * @date 2016/11/3
 */
public class GridPickerAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mDatas;
    private int mBackgroundColorUnchecked, mBackgroundColorChecked;
    private int mTextColorUnchecked, mTextColorChecked;
    private int[] mPaddings;
    private int mColumnCount;
    private boolean mAutoFill;
    private int mAutoFilledCount;
    private int mTag;
    private OnItemSelectedListener<T> mOnItemSelectedListener;

    private View mLastPickedView;
    private int mCheckedIndex = -1;
    private boolean mHasCalcAutoFilledCount;

    public GridPickerAdapter(Context context) {
        mContext = context;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            if (position < mDatas.size() && mOnItemSelectedListener != null) {
                mCheckedIndex = position;
                if (mLastPickedView != null) {
                    mLastPickedView.setBackgroundColor(mBackgroundColorUnchecked);
                    ((TextView) mLastPickedView).setTextColor(mTextColorUnchecked);
                }
                view.setBackgroundColor(mBackgroundColorChecked);
                ((TextView) view).setTextColor(mTextColorChecked);
                mLastPickedView = view;
                mOnItemSelectedListener.onItemSelected(mTag, position, mDatas.get(position));
            }
        }
    };

    @Override
    public int getCount() {
        calcAutoFilledCount();
        return mDatas.size() + (mAutoFill ? mAutoFilledCount : 0);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final TextView tv;
        if (convertView == null) {
            convertView = new TextView(mContext);
            tv = (TextView) convertView;
        } else {
            tv = (TextView) convertView;
        }

        tv.setTag(position);
        tv.setGravity(Gravity.CENTER);
        if (position < mDatas.size()) {
            tv.setText(mDatas.get(position).toString());
        } else {
            tv.setText("");
        }
        tv.setBackgroundColor(position != mCheckedIndex ? mBackgroundColorUnchecked :
                mBackgroundColorChecked);
        tv.setTextColor(position == mCheckedIndex ? mTextColorChecked : mTextColorUnchecked);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        if (mPaddings != null) {
            tv.setPadding(mPaddings[0], mPaddings[1], mPaddings[2], mPaddings[3]);
        }
        tv.setOnClickListener(mOnClickListener);

        return tv;
    }

    @Override
    public void notifyDataSetChanged() {
        mHasCalcAutoFilledCount = false;
        super.notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        mDatas = data;
    }

    public void setBackgroundColor(int unchecked, int checked) {
        mBackgroundColorUnchecked = unchecked;
        mBackgroundColorChecked = checked;
    }

    public void setTextColor(int colorUnchecked, int colorChecked) {
        mTextColorUnchecked = colorUnchecked;
        mTextColorChecked = colorChecked;
    }

    public void setPaddings(int[] padding) {
        mPaddings = padding;
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
    }

    public void setAutoFill(boolean autoFill) {
        mAutoFill = autoFill;
    }

    public void resetChecked() {
        mCheckedIndex = -1;
    }

    public int getCheckedIndex() {
        return mCheckedIndex;
    }

    public void setTag(int tag) {
        mTag = tag;
    }

    public int getTag() {
        return mTag;
    }

    public int getAutoFilledCount() {
        return mAutoFilledCount;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        mOnItemSelectedListener = l;
    }

    private void calcAutoFilledCount() {
        if (!mHasCalcAutoFilledCount) {
            int offset = mDatas.size() % mColumnCount;
            if (mAutoFill && offset != 0) {
                mAutoFilledCount = mColumnCount - offset;
            } else {
                mAutoFilledCount = 0;
            }
            mHasCalcAutoFilledCount = true;
        }
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(int adapterTag, int position, T item);
    }
}
