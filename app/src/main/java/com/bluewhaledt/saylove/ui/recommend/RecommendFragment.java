package com.bluewhaledt.saylove.ui.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_modify.ModifyVerifyFragment;
import com.bluewhaledt.saylove.ui.message.ChatDetailActivity;
import com.bluewhaledt.saylove.ui.message.ChatDetailFragment;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;
import com.bluewhaledt.saylove.ui.recommend.presenter.RecommendPresenter;
import com.bluewhaledt.saylove.ui.recommend.view.IRecommendView;
import com.bluewhaledt.saylove.ui.recommend.view.VerifyGuideClickListener;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.tab.TabBaseFragment;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.bluewhaledt.saylove.ui.info.base.ISourceFrom.FROM_CAR_VERIFY;
import static com.bluewhaledt.saylove.ui.info.base.ISourceFrom.FROM_EDUCATION_VERIFY;
import static com.bluewhaledt.saylove.ui.info.base.ISourceFrom.FROM_HOUSE_VERIFY;

/**
 * 描述：推荐入口的fragment的样式
 * 作者：shiming_li
 * 时间：2016/11/25 17:34
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public class RecommendFragment extends TabBaseFragment implements IRecommendView, View.OnClickListener {


    private View mView;
    private SwipeRecyclerView mRecyclerView;
    private RecommendPresenter mPresenter;
    private RecommendFragmentAdapter mAdapter;
    private boolean mFlag = true;
    private RecommendEntity.ListBean mListBean;
    private int mAvatarStatus;
    private String mNickName;
    private LinearLayout mHasNoRecommendDataContainer;
    private Button mButTryAgain;
    private boolean isFristGetData=true;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showTitleBar(false);
//        setMidelImgShow(true);
//        showTitleBarLeftBtn(false);
        mView = inflater.inflate(R.layout.item_fragment_recommend_layout, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPresenter = new RecommendPresenter(getActivity(), this);
        initView();
        initListener();
        /*新需求添加*********start********引导认证弹窗*/
        checkIsNeedGuide();
        /*新需求添加*********end********引导认证弹窗*/

    }

    private void checkIsNeedGuide() {
        if (AccountManager.getInstance().getZaAccount().popCertificate) {
            DialogUtil.showVerifyGuideDialog(getActivity(), new VerifyGuideClickListener() {
                @Override
                public void onVerifyEducationClicked() {
                    Bundle educationVerify = new Bundle();
                    educationVerify.putInt(IntentConstants.MODIFY_FROM, FROM_EDUCATION_VERIFY);
                    VerifyInfoEntity verifyInfoEntity = new VerifyInfoEntity();
                    educationVerify.putSerializable(IntentConstants.VERIFY_INFO_ID, verifyInfoEntity);
                    startFragment(ModifyVerifyFragment.class, educationVerify);
                }

                @Override
                public void onVerifyHouseClicked() {
                    Bundle houseVerify = new Bundle();
                    houseVerify.putInt(IntentConstants.MODIFY_FROM, FROM_HOUSE_VERIFY);
                    VerifyInfoEntity verifyInfoEntity = new VerifyInfoEntity();
                    houseVerify.putSerializable(IntentConstants.VERIFY_INFO_ID, verifyInfoEntity);
                    startFragment(ModifyVerifyFragment.class, houseVerify);
                }

                @Override
                public void onVerifyCarClicked() {
                    Bundle carVerify = new Bundle();
                    carVerify.putInt(IntentConstants.MODIFY_FROM, FROM_CAR_VERIFY);
                    VerifyInfoEntity verifyInfoEntity = new VerifyInfoEntity();
                    carVerify.putSerializable(IntentConstants.VERIFY_INFO_ID, verifyInfoEntity);
                    startFragment(ModifyVerifyFragment.class, carVerify);
                }
            });
        }
    }

    private void initListener() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecommendFragmentAdapter(getActivity(), new ZAArray<RecommendEntity.ListBean>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRecomendEntity();

            }

            @Override
            public void onLoadMore() {
                mPresenter.getRecomendLoadMoreEntity();

            }
        });

        //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
        //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
        //进入页面要加载数据的话，让它自定下拉刷新一次
        mRecyclerView.setRefreshing(true);
        mRecyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ArrayList<RecommendEntity.ListBean> list = mAdapter.getArraylist();
                Bundle bundle = new Bundle();
                mListBean = list.get(position);
                bundle.putString(IntentConstants.USER_ID, list.get(position).userId + "");
                startFragment(ThirdPartyFragment.class, bundle);
//                mAdapter.notifyItemChanged(position);
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });
        mAdapter.setOnButtonClickListener(new RecommendFragmentAdapter.OnButtonClickListener() {
            @Override
            public void onBtnClick(View view) {
                switch (view.getId()) {
                    case R.id.item_fragment_recommend_iv_msg:
                        if (view.getTag(R.id.item_fragment_recommend_iv_msg) != null) {
                            EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECONMEND_CHAT_BTN);
                            RecommendEntity.ListBean bean = (RecommendEntity.ListBean) view.getTag(R.id.item_fragment_recommend_iv_msg);
                            Bundle args = new Bundle();
                            ExtendedData info = new ExtendedData();
                            info.otherSessionId = bean.imAccId;
                            info.otherAvatar = bean.avatar;
                            info.otherMemberId = bean.userId;
                            info.nikeName = bean.nickName;
                            boolean isVip = AccountManager.getInstance().getZaAccount().isVip;
                            if (isVip) {
                                info.isLockMessage = false;
                            } else {
                                info.isLockMessage = true;
                            }
                            args.putSerializable(ChatDetailFragment.EXTENDED_DATA, info);
                            Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                        break;
                    case R.id.item_fragment_recommend_iv_hot_framelaout_container:
                        break;
                }
            }

        });
        mAdapter.setOnHotButtonClickListener(new RecommendFragmentAdapter.OnHotButtonClickListener() {
            @Override
            public void onBtnClick(View view) {
                switch (view.getId()) {
                    case R.id.item_fragment_recommend_iv_hot_framelaout_container:
                        if (view.getTag(R.id.tag_first) != null && view.getTag(R.id.tag_second) != null) {
                            RecommendFragmentAdapter.MyViewHolder myViewHolder = (RecommendFragmentAdapter.MyViewHolder) view.getTag(R.id.tag_first);
                            RecommendEntity.ListBean bean = (RecommendEntity.ListBean) view.getTag(R.id.tag_second);
                            //开启心动了
                            if (myViewHolder.mIvHot.getVisibility() == View.VISIBLE) {
                                DebugUtils.d("shiming", "心动" + bean.userId);
                                EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECOMMENG_HOT_BTN);
                                mPresenter.setHotRead(bean, bean.userId + "", myViewHolder);
                            } else {
                                DebugUtils.d("shiming", "取消心动" + bean.likeId);
                                mPresenter.cancleLike(bean,bean.likeId + "", myViewHolder);
                            }

                        }
                        break;
                }
            }
        });
        mButTryAgain.setOnClickListener(this);
    }
    //心动成功的，三资页回调的心动的id
    @Subscribe
    public void onEventMainThread(LikeEntity event) {
        if (event != null) {
            mListBean.likeId = event.likeId;
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mRecyclerView = (SwipeRecyclerView) mView.findViewById(R.id.fragment_recommend_layout_lv);
        mHasNoRecommendDataContainer = (LinearLayout) mView.findViewById(R.id.fragment_recommend_detail_no_net_work_container);
        mButTryAgain = (Button) mView.findViewById(R.id.fragment_recommend_btn_try_again);

    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onFirstUserVisible() {
        mAvatarStatus = AccountManager.getInstance().getZaAccount().avatarStatus;
        mNickName = AccountManager.getInstance().getZaAccount().nickName;
    }

    @Override
    public void getRecommendEntity(RecommendEntity entity) {
        ArrayList<Integer> mListPosition = mAdapter.getMListPosition();
        if (mListPosition!=null){
            mListPosition.clear();
        }
        if (entity!=null && entity.list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mHasNoRecommendDataContainer.setVisibility(View.GONE);
            mAdapter.clearRecommendData();
            mAdapter.addRecommendData(entity.list);
            mRecyclerView.complete();
            isFristGetData=false;
        } else if (entity!=null&&entity.list.size()==0&&isFristGetData){
            EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECOMMEND_NULL);
            mRecyclerView.setVisibility(View.GONE);
            mHasNoRecommendDataContainer.setVisibility(View.VISIBLE);
        }else {
            ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            mRecyclerView.complete();
        }
        //为了让第一次数据成功的加载，这里需要一个延时的任务
        if (mFlag) {
            //头像不通过
            if (mAvatarStatus == 3) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.gotoUpLoadPic(getActivity(), mNickName);
                    }
                }, 1000);
            }
            mFlag = false;
        }

    }

    @Override
    public void getRecommendLoadMoreEntity(RecommendEntity entity) {
        if (entity!=null && entity.list.size() > 0) {
            boolean hasLoadMore = mAdapter.addRecommendData(entity.list);
            mRecyclerView.stopLoadingMore();
            if (hasLoadMore) {
                ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            }
        } else{
            ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            mRecyclerView.onNoMore(R.string.recommend_show_no_view + "");
            mRecyclerView.stopLoadingMore();
        }
    }
    @Override
    public void getRecommendFail(String errorCode, String errorMsg) {
        ToastUtils.toast(getActivity(), errorMsg);
        if (mRecyclerView!=null){
            mRecyclerView.complete();
        }
    }

    @Override
    public void getRecommendLoadMoreFail(String errorCode, String errorMsg) {
        ToastUtils.toast(getActivity(), errorMsg);
        if (mRecyclerView!=null){
            mRecyclerView.stopLoadingMore();
        }
    }

    @Override
    public void onError() {
        if (mRecyclerView!=null) {
            mRecyclerView.complete();
        }
    }

    @Override
    public void onErrorLoadmore() {
        if (mRecyclerView!=null){
            mRecyclerView.stopLoadingMore();
        }
    }

    @Override
    public void likeSuccess(RecommendEntity.ListBean bean, LikeEntity liekEntity, RecyclerView.ViewHolder viewHolder) {
        RecommendFragmentAdapter.MyViewHolder myViewHolder = (RecommendFragmentAdapter.MyViewHolder) viewHolder;
        bean.setLikeId(liekEntity.likeId);
        mAdapter.setHotRed(myViewHolder);
        mAdapter.doAnimation(myViewHolder.mIvHotBiside);
        mAdapter.notifyDataSetChanged();

    }
    @Override
    public void LikeFail(String errorCode, String errorMsg) {
        //非会员的进入购买页
        if (errorCode.equals("-56008")) {
            ToastUtils.toast(getActivity(), getActivity().getString(R.string.fragment_third_party_praise_error_text));
        } else if (errorCode.equals("-56006")) {
            EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECOMMEND_DIALOG_GO_PAY);
            DialogUtil.showOpenGoPay(getActivity());
        } else {
            ToastUtils.toast(getActivity(), errorMsg);
        }
    }

    @Override
    public void cancleLikeSuccess(RecommendEntity.ListBean bean,ZAResponse zaResponse, RecyclerView.ViewHolder viewHolder) {
        bean.likeId=-1;
        RecommendFragmentAdapter.MyViewHolder myViewHolder = (RecommendFragmentAdapter.MyViewHolder) viewHolder;
        mAdapter.setNoHotRed(myViewHolder);
//        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_recommend_btn_try_again:
                mRecyclerView.setRefreshing(true);
                mRecyclerView.setVisibility(View.VISIBLE);
                mHasNoRecommendDataContainer.setVisibility(View.GONE);
                break;
        }
    }
}
