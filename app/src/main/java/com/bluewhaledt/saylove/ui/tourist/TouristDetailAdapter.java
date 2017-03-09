package com.bluewhaledt.saylove.ui.tourist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * 描述：TODO
 * 作者：shiming_li
 * 时间：2016/12/12 14:29
 * 包名：com.zhenai.saylove_icon.ui.tourist
 * 项目名：SayLove
 */
public class TouristDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private  ZAArray<TouristEntity.TouristEntityListBean> mData;
    private ArrayList<Integer> mList=new ArrayList<>();
    public TouristDetailAdapter(Context context, ArrayList<TouristEntity.TouristEntityListBean> list) {
        mContext=context;
        mData = new ZAArray<>();
        if (list != null && !list.isEmpty()){
            mData.addAll(list);
        }
    }

    public boolean addRecommendData(ArrayList<TouristEntity.TouristEntityListBean> list) {
        int oldSize = mData.size();
        mData.addAll(list);
        int newSize = mData.size();
        notifyDataSetChanged();
        return oldSize==newSize;
    }

    public void clearRecommendData() {
        mData.clear();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recommend_layout, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder  myViewHolder = (MyViewHolder) holder;
//         "sex": 1,   1   就是男的   2  就是女的 么
        final TouristEntity.TouristEntityListBean bean = mData.get(position);
        int sex = bean.sex;
        int placeholderPic;
//        String mLieKIdm= bean.likeId+"";
        if (sex == 1) {
            placeholderPic = R.mipmap.img_home_avatar_male;
        } else {
            placeholderPic = R.mipmap.img_home_avatar_female;
        }
        boolean isVIP = bean.isVIP;
        if (isVIP) {
            if (mList != null) {
                mList.add(position);
            }
        }
        myViewHolder.mIsVip.setVisibility(mList.contains(position)?View.VISIBLE:View.INVISIBLE);
        myViewHolder.mPersonName.setText(bean.nickName);
        myViewHolder.mPersonAge.setText(bean.age + "岁");
        myViewHolder.mSalary.setText(bean.salary);
        myViewHolder.mWhere.setText(bean.workcity);
        myViewHolder.mHeight.setText(bean.height);

        decideCodition(bean, myViewHolder);

//            背景
        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(bean.avatar,PhotoUrlUtils.TYPE_6))
                .placeholder(placeholderPic)
                .error(placeholderPic)
                .blurMinBitmap(30)//越大越模糊
                .into(myViewHolder.mIvPersonIconBG);
        //头像
        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(bean.avatar,PhotoUrlUtils.TYPE_10))
                .placeholder(placeholderPic)
                .error(placeholderPic)
//                .round(DensityUtils.dp2px(mContext, 4))
                .into(myViewHolder.mIvPersonIcon);
        //已经心动过了，变颜色
        if (bean.likeId != -1) {
            setHotRed(myViewHolder);
        } else {
            setNoHotRed(myViewHolder);
        }
        myViewHolder.mIvHotRedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TouristDetailPage.TOURIST_DETAIL_PAGE_HOT_BTN);
                DialogUtil.showGoRegister(mContext);
            }
        });
        myViewHolder.mIvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TouristDetailPage.TOURIST_DETAIL_PAGE_CHAT_BTN);
                DialogUtil.showGoRegister(mContext);
            }
        });
    }
    private void setNoHotRed(MyViewHolder myViewHolder) {
        myViewHolder.mIvHotRed.setVisibility(View.INVISIBLE);
        myViewHolder.mIvHot.setVisibility(View.VISIBLE);
        myViewHolder.mIvHotBiside.setVisibility(View.INVISIBLE);
    }
    private void setHotRed(MyViewHolder myViewHolder) {
        myViewHolder.mIvHot.setVisibility(View.INVISIBLE);
        myViewHolder.mIvHotRed.setVisibility(View.VISIBLE);
        myViewHolder.mIvHotBiside.setVisibility(View.VISIBLE);
    }
    private void decideCodition(TouristEntity.TouristEntityListBean bean, MyViewHolder myViewHolder) {
        TextView codition = myViewHolder.mCodition;
        ImageView isVip = myViewHolder.mIsVip;
        //vip、
        boolean isCar = bean.isCar;
        boolean isDegree = bean.isDegree;
        boolean isHouse = bean.isHouse;
        boolean isZM = bean.isZM;
        StringBuffer sb = new StringBuffer();
        if (isZM) {
            sb.append("身份");
        }
        if (isCar) {
            sb.append("、车");
        }
        if (isHouse) {
            sb.append("、房");
        }
        if (isDegree) {
            myViewHolder.mDegree.setVisibility(View.VISIBLE);
            myViewHolder.mDegreeBackLine.setVisibility(View.VISIBLE);
            myViewHolder.mDegree.setText(bean.degree);
            sb.append("、学历");
        }
        codition.setText("已认证" + sb);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public ArrayList<TouristEntity.TouristEntityListBean>  getArraylist() {
        return mData;
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvHot;
        private ImageView mIvHotRed;
        private FrameLayout mIvHotRedContainer;
        private ImageView mIvHotBiside;
        private RelativeLayout mAllContainer;
        private ImageView mIvPersonIcon;
        private ImageView mIvMsg;
        private ImageView mIvPersonIconBG;
        private TextView mPersonName;
        private TextView mPersonAge;
        private TextView mSalary;
        private TextView mHeight;
        private TextView mWhere;
        private TextView mCodition;
        private TextView mDegree;
        private TextView mDegreeBackLine;
        private ImageView mIsVip;
        MyViewHolder(View view) {
            super(view);
            mIvHot = (ImageView) view.findViewById(R.id.item_fragment_recommend_iv_hot);
            mIvHotRed = (ImageView) view.findViewById(R.id.item_fragment_recommend_iv_hot_red);
            mIvHotRedContainer = (FrameLayout) view.findViewById(R.id.item_fragment_recommend_iv_hot_framelaout_container);
            mIvHotBiside = (ImageView) view.findViewById(R.id.item_fragment_recommend_iv_hot_red_biside);
            mAllContainer = (RelativeLayout) view.findViewById(R.id.item_fragment_ll_container);
            mIvPersonIcon = (ImageView) view.findViewById(R.id.fragment_recommend_layout_iv_icon);
            mIvPersonIconBG = (ImageView) view.findViewById(R.id.fragment_recommend_layout_iv_bg);
            mIvMsg = (ImageView) view.findViewById(R.id.item_fragment_recommend_iv_msg);
            mPersonName = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_name);
            mPersonAge = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_age);
            mSalary = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_salary);
            mHeight = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_height);
            mWhere = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_where);
            mCodition = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_decide_codition);
            mDegree = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_degree);
            mDegreeBackLine = (TextView) view.findViewById(R.id.item_fragment_recommend_tv_degree_line);
            mIsVip = (ImageView) view.findViewById(R.id.item_fragment_recommend_im_isvip);

        }


    }
}
