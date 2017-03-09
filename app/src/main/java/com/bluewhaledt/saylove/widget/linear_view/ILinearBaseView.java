package com.bluewhaledt.saylove.widget.linear_view;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public interface ILinearBaseView<E extends BaseEntity>{

    void refreshData(ArrayList<E> data);

    void loadMoreDate(ArrayList<E> data);

    void emptyData(String msg);

    void totalDataInfo(ResultEntity<E> resultEntity);


}
