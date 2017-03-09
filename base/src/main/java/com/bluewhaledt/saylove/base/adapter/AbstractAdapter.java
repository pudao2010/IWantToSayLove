package com.bluewhaledt.saylove.base.adapter;

import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * adapter基类
 *
 * @param <T>
 * @author huliang
 * @version 1.0.0
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {
    private ArrayList<T> mItems = new ArrayList<T>();

    /**
     * 清除数据
     */
    public void recycle() {
        mItems.clear();
    }

    /**
     * 添加数据
     *
     * @param items
     * @param isRefresh 是否刷新
     */
    public void add(ArrayList<T> items, boolean isRefresh) {
//        mItems.clear();
        mItems.addAll(items);
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public abstract View getView(int position, View convertView, ViewGroup parent);



    /**
     * 查找view
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

}
