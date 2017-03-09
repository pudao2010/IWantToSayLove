package com.bluewhaledt.saylove.entity;


import com.bluewhaledt.saylove.base.widget.picker_view.model.IPickerViewData;
import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by Sai on 15/11/22.
 */
public class DictionaryBean extends BaseEntity implements IPickerViewData {
    public String value;
    public int key;

    public DictionaryBean(String value, int key){
        this.value = value;
        this.key = key;
    }


    //这个用来显示在PickerView上面的字符串,PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return value;
    }

    @Override
    public String[] uniqueKey() {
        return new String[]{key+""};
    }
}
