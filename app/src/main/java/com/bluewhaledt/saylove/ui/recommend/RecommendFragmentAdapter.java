package com.bluewhaledt.saylove.ui.recommend;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

import java.util.ArrayList;

/**
 * 描述：adapter
 * 作者：shiming_li
 * 时间：2016/11/28 13:59
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public class RecommendFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private View mView;
    private Context mContext;
    //动画开始的标记
    private boolean mFlag = true;
    private ArrayList<Integer> mList=new ArrayList<>();
    private ZAArray<RecommendEntity.ListBean> mData;
    private float[] mCurrentPosition = new float[2];
    public RecommendFragmentAdapter(Context context, ArrayList<RecommendEntity.ListBean> info) {
        mContext = context;
        mData = new ZAArray<>();
        if (info != null && !info.isEmpty()){
            mData.addAll(info);
        }

    }
    public ArrayList<Integer> getMListPosition(){
        return mList;
    }
    public ArrayList<RecommendEntity.ListBean> getArraylist() {
        return mData;
    }
    public boolean addRecommendData(ArrayList<RecommendEntity.ListBean> info) {
        int oldSize = mData.size();
        mData.addAll(info);
        int newSize = mData.size();
        notifyDataSetChanged();
        return oldSize==newSize;
    }
    public void clearRecommendData() {
        mData.clear();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_recommend_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder  myViewHolder = (MyViewHolder) holder;
//        myViewHolder.setIsRecyclable(false);
//         "sex": 1,   1   就是男的   2  就是女的 么
        final RecommendEntity.ListBean bean = mData.get(position);
        int sex = bean.sex;
        int placeholderPic;
        if (sex == 1) {
            placeholderPic = R.mipmap.img_home_avatar_male;
        } else {
            placeholderPic = R.mipmap.img_home_avatar_female;
        }
        myViewHolder.mPersonName.setText(bean.nickName);
        myViewHolder.mPersonAge.setText(bean.age + "岁");
        myViewHolder.mSalary.setText(bean.salary);
        myViewHolder.mWhere.setText(bean.workcity);
        myViewHolder.mHeight.setText(bean.height);
        boolean mIsVIP = bean.isVIP;
        //解决复用的问题
        if (mIsVIP) {
            if (mList != null&&!mList.contains(position)) {
                mList.add(position);
            }
        }
        if (mList!=null) {
            myViewHolder.mIsVip.setVisibility(mList.contains(position) ? View.VISIBLE : View.INVISIBLE);
        }

        decideCodition(bean, myViewHolder);
      // 背景
        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(bean.avatar,PhotoUrlUtils.TYPE_6))
                .placeholder(placeholderPic)
                .error(placeholderPic)
                .blurMinBitmap(30)//越大越模糊30
                .into(myViewHolder.mIvPersonIconBG);
        //头像
        ImageLoaderFactory.getImageLoader()
                .with(mContext)
                .load(PhotoUrlUtils.format(bean.avatar,PhotoUrlUtils.TYPE_10))
                // TODO: 2016/12/24
//                .round(DensityUtils.dp2px(mContext, 2))
                .placeholder(placeholderPic)
                .error(placeholderPic)
                .into(myViewHolder.mIvPersonIcon);
        //已经心动过了，变颜色
        if (bean.likeId != -1) {
//            mIsHeatPerson.add(position);
            setHotRed(myViewHolder);
        } else {
            setNoHotRed(myViewHolder);
        }
        myViewHolder.mIvHotRedContainer.setOnClickListener(this);
        myViewHolder.mIvHotRedContainer.setTag(R.id.tag_first,myViewHolder);
        myViewHolder.mIvHotRedContainer.setTag(R.id.tag_second,bean);
        myViewHolder.mIvMsg.setOnClickListener(this);
        myViewHolder.mIvMsg.setTag(R.id.item_fragment_recommend_iv_msg,bean);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_fragment_recommend_iv_msg:
                onBtnClickListener.onBtnClick(view);
                break;
            case R.id.item_fragment_recommend_iv_hot_framelaout_container:
                onHotBtnClickListener.onBtnClick(view);
                break;
        }
    }
    public interface OnHotButtonClickListener {
        void onBtnClick(View view);
    }
    public void setOnHotButtonClickListener(OnHotButtonClickListener listener){
        onHotBtnClickListener =listener;
    }
    public interface OnButtonClickListener {
        void onBtnClick(View view);
    }
    private OnButtonClickListener onBtnClickListener;
    private OnHotButtonClickListener onHotBtnClickListener;
    public void setOnButtonClickListener(OnButtonClickListener listener){
        onBtnClickListener =listener;
    }
    //点赞成功了回调参数了 ，这个参数要post上去
    private void decideCodition(RecommendEntity.ListBean bean, MyViewHolder myViewHolder) {
        TextView codition = myViewHolder.mCodition;
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
    public void setNoHotRed(MyViewHolder myViewHolder) {
        myViewHolder.mIvHotRed.setVisibility(View.INVISIBLE);
        myViewHolder.mIvHot.setVisibility(View.VISIBLE);
        myViewHolder.mIvHotBiside.setVisibility(View.INVISIBLE);
    }
    public void setHotRed(MyViewHolder myViewHolder) {
        myViewHolder.mIvHot.setVisibility(View.INVISIBLE);
        myViewHolder.mIvHotRed.setVisibility(View.VISIBLE);
        myViewHolder.mIvHotBiside.setVisibility(View.VISIBLE);
    }
    //做动画的时候，不能开启动画 方法废弃
    public void translateAnimation(final View v) {
//        ImageView view = myViewHolder.mIvHotBiside;
        if (!mFlag){
            return;
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", 0.0f, -200f);
        translationY.setDuration(1000);
        translationY.setRepeatCount(0);// 重复次数
        ObjectAnimator rotation = ObjectAnimator.ofFloat(v, "rotation", 0.0f, -65f);
        rotation.setDuration(1000);
        rotation.setRepeatCount(0);// 重复次数
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.0f);
        alpha.setDuration(1000);
        final ObjectAnimator translationX = ObjectAnimator.ofFloat(v, "translationX", 0.0f, -100f);
        translationX.setDuration(1000);
        translationX.setRepeatCount(0);// 重复次数
        PropertyValuesHolder xpvh = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.4f);
        PropertyValuesHolder ypvh = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.4f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v, xpvh, ypvh);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(0);// 重复次数
        AnimatorSet set = new AnimatorSet();
        set.play(translationX).with(translationY).with(alpha).with(rotation).with(objectAnimator);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mFlag = false;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.INVISIBLE);
                mFlag = true;
            }
            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();

    }

    /**
     * 曲线自己画，透明度动画，用属性动画
     * @param v
     */
    public void doAnimation(final View v){
        if (!mFlag){
            return;
        }
        float startX =0;
        float startY = 0;
        float toX = -100 ;
        float toY = -200;
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        //前两个参数的意思是我们的曲线弯曲的方向
        // Random random=new Random();
        // int i = random.nextInt((int) (startX + toY));//让曲线更加的平滑，用随机数去管理 但是我们的这个数是一个负数
//        Random random=new Random();
//        int i = random.nextInt(-(int) (startY + toY) / 2);
//        DebugUtils.d("shiming",i+"ddd");
        path.quadTo(startX, (startY + toY)/2, toX, toY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                v.setTranslationX(mCurrentPosition[0]);
                v.setTranslationY(mCurrentPosition[1]);
            }
        });
        ObjectAnimator rotation = ObjectAnimator.ofFloat(v, "rotation", 0.0f, -50f);
        rotation.setDuration(1000);
        rotation.setRepeatCount(0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.1f);
        alpha.setDuration(1000);
        PropertyValuesHolder xpvh = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.4f);
        PropertyValuesHolder ypvh = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.4f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v, xpvh, ypvh);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(0);
        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimator).with(alpha).with(rotation).with(objectAnimator);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mFlag = false;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.INVISIBLE);
                mFlag = true;
            }
            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvHot;
        public ImageView mIvHotRed;
        private FrameLayout mIvHotRedContainer;
        public ImageView mIvHotBiside;
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



