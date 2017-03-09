package com.bluewhaledt.saylove.ui.info.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ui.info.entity.ItemInfoEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/11/30.
 */

public class ThirdPartyInfoAdapter extends RecyclerView.Adapter<ThirdPartyInfoAdapter.ItemHolder> {

    private Context mContext;
    private List<ItemInfoEntity> mList;

    public ThirdPartyInfoAdapter(Context context, List<ItemInfoEntity> list) {
        this.mContext = context;
        this.mList = list;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_third_party_info_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ItemInfoEntity entity = mList.get(position);
        holder.infoContentView.setText(entity.content);
        holder.infoTitleView.setText(entity.title);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView infoTitleView;
        private TextView infoContentView;

        ItemHolder(View itemView) {
            super(itemView);
            infoTitleView = (TextView) itemView.findViewById(R.id.info_title_view);
            infoContentView = (TextView) itemView.findViewById(R.id.info_content_view);
        }
    }
}
