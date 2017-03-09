package com.bluewhaledt.saylove.ui.recommend.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bluewhaledt.saylove.R;

/**
 * 描述：封装了我们的SwipeRefreshLayout和RecyclerView
 * 作者：shiming_li
 * 时间：2016/11/25 17:34
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public class SwipeRecyclerView extends FrameLayout
                implements SwipeRefreshLayout.OnRefreshListener{

    private View mEmptyView;
    private BaseFooterView mFootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private LayoutManager mLayoutManager;
    private OnLoadListener mListener;
    private SpanSizeLookup mSpanSizeLookup;
    private DataObserver mDataObserver;
    private WrapperAdapter mWrapperAdapter;

    private boolean isEmptyViewShowing;
    private boolean isLoadingMore;
    private boolean isLoadMoreEnable;
    private boolean isRefreshEnable;

    private String TAG = this.getClass().getSimpleName();

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupSwipeRecyclerView();
    }

    private void setupSwipeRecyclerView() {
        isEmptyViewShowing = false;
        isRefreshEnable = true;
        isLoadingMore = false;
        isLoadMoreEnable = true;
        mFootView = new SimpleFooterView(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_swipe_recyclerview, this);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mLayoutManager = recyclerView.getLayoutManager();
        mFootView.showNoView();

        mRefreshLayout.setOnRefreshListener(this);
        recyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                DebugUtils.d("shiming","recylview的onscroolstatechange");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断是否需要做操作，如果是正在加载更多，或者是刷新的话，我们直接返回
                //不做操作
//                DebugUtils.d("shiming","recylview的onScrolled");
                if(!isLoadMoreEnable || isRefreshing() || isLoadingMore){
                    return;
                }

                int firstCompletelyVisible = 0;
                int lastCompletelyVisible = 0;
                //得到最后可见的一个条目，那边
                mLayoutManager = recyclerView.getLayoutManager();
                if(mLayoutManager instanceof LinearLayoutManager){
//                    lastCompletelyVisible = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
                    lastCompletelyVisible = ((LinearLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
                    firstCompletelyVisible = ((LinearLayoutManager)mLayoutManager).findFirstCompletelyVisibleItemPosition();

                }else if(mLayoutManager instanceof GridLayoutManager){
//                    lastCompletelyVisible = ((GridLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
                    lastCompletelyVisible = ((GridLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
                    firstCompletelyVisible = ((GridLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
                }else if(mLayoutManager instanceof StaggeredGridLayoutManager){
                    int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPositions(into);
                    lastCompletelyVisible = findMax(into);
                }

                int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
                if(childCount > 1 && lastCompletelyVisible == childCount - 1 && (lastCompletelyVisible - firstCompletelyVisible) < childCount - 1){
                    if(mListener != null){
//                        DebugUtils.d("shiming","进来了么");
                        isLoadingMore = true;
                        onLoadingMore();
                        mListener.onLoadMore();

                    }
                }else{
                    if (mFootView != null){
                        mFootView.showNoView();
                    }
                }
            }
        });
    }

    private int lastY;
    //下拉刷新的view是否展示
    private boolean isRefreshViewShow;
    private int lastVisible = 0;
    private boolean isUpDrag;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (isLoadingMore){//如果正在加载更多，则不进行拦截
            if (isRefreshEnable){//如果设置了可以下拉刷新，此处防止在加载更多时不可以同时下拉刷新
                mRefreshLayout.setEnabled(false);
            }
            return super.onInterceptTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            lastY = (int) event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            int firstVisible = 0;
            if(mLayoutManager instanceof LinearLayoutManager){
                firstVisible = ((LinearLayoutManager)mLayoutManager).findFirstCompletelyVisibleItemPosition();
                lastVisible = ((LinearLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
            }
            if (event.getY() > lastY && firstVisible == 0){
                isRefreshViewShow = true;
            }else if (event.getY() > lastY){
                isUpDrag = true;
            }
        }

        int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
        if (lastY - event.getY() > 50 && !isUpDrag  && !isRefreshViewShow && event.getAction() == MotionEvent.ACTION_MOVE ){
            if (lastVisible == childCount - 1){
                if(mListener != null){
                    isLoadingMore = true;
                    onLoadingMore();
                    return true;
                }
            }
        }else if (lastY - event.getY() > 50 && !isUpDrag  &&!isRefreshViewShow && event.getAction() == MotionEvent.ACTION_UP ){
            if (lastVisible == childCount - 1){
                if(mListener != null){
                    mListener.onLoadMore();
                    return false;
                }
            }

        }else if (event.getAction() == MotionEvent.ACTION_UP ){
            isRefreshViewShow = false;
            lastVisible = 0;
            isUpDrag = false;
            mFootView.showNoView();
//            mFootView.onNoMore("我是有底线的！");
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isUpDrag  &&!isRefreshViewShow && event.getAction() == MotionEvent.ACTION_UP ){
            int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
            if (lastVisible == childCount - 1){
                if(mListener != null){
                    mListener.onLoadMore();
                    return true;
                }
            }

        }else if (event.getAction() == MotionEvent.ACTION_UP ){
            isRefreshViewShow = false;
            lastVisible = 0;
            isUpDrag = false;
            mFootView.showNoView();
        }
        return super.onTouchEvent(event);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 设置能否刷新
     *
     */
    public void setRefreshEnable(boolean refreshEnable){
        isRefreshEnable = refreshEnable;
        mRefreshLayout.setEnabled(isRefreshEnable);
    }

    public boolean getRefreshEnable(){
        return isRefreshEnable;
    }

    /**
     * 设置能否加载更多
     * 设置为true的话，滑动到底部自动的触发加载更多
     *
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        if(!loadMoreEnable){
            stopLoadingMore();
        }
        isLoadMoreEnable = loadMoreEnable;
    }

    /**
     * set布局管理器
     */
    public void setLayoutManager(LayoutManager layout){
        getRecyclerView().setLayoutManager(layout);
    }

    /**
     * get is refreshing
     * @return
     */
    public boolean isRefreshing(){
        return mRefreshLayout.isRefreshing();
    }

    /**
     * get is loading more
     * @return
     */
    public boolean isLoadingMore(){
        return isLoadingMore;
    }
    public void setLoadingMore(boolean flags){
        isLoadingMore=true;
    }
    /**
     * is empty view showing
     * @return
     */
    public boolean isEmptyViewShowing(){
        return isEmptyViewShowing;
    }

    /**
     * you may need set some other attributes of swipeRefreshLayout
     * @return
     *     swipeRefreshLayout
     */
    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return mRefreshLayout;
    }

    /**
     * you may need set some other attributes of RecyclerView
     * @return
     *     RecyclerView
     */
    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    /**
     * set load more listener
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener){
        mListener = listener;
    }

    /**
     * support for GridLayoutManager
     * @param spanSizeLookup
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup){
        this.mSpanSizeLookup = spanSizeLookup;
    }

    /**
     * set the footer view
     * @param footerView
     *        the view to be showing when pull up
     */
    public void setFooterView(BaseFooterView footerView){
        if(footerView != null) {
            this.mFootView = footerView;
        }
    }

    /**
     * set a empty view like listview
     * @param emptyView
     *        the view to be showing when the intentData set size is zero
     */
    public void setEmptyView(View emptyView){
        if(mEmptyView != null){
            removeView(mEmptyView);
        }
        this.mEmptyView = emptyView;

        if(mDataObserver != null) {
            mDataObserver.onChanged();
        }
    }

    /**
     * set adapter to recyclerView
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter){
        if(adapter != null) {
            if(mDataObserver == null){
                mDataObserver = new DataObserver();
            }
            mWrapperAdapter = new WrapperAdapter(adapter);
            recyclerView.setAdapter(mWrapperAdapter);
            adapter.registerAdapterDataObserver(mDataObserver);
            mDataObserver.onChanged();
        }
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter(){
        return mWrapperAdapter.getInnerAdapter();
    }

    /**
     * refresh or load more completed
     */
    public void complete(){
        mRefreshLayout.setRefreshing(false);
        stopLoadingMore();
    }

    /**
     * set refreshing
     * if you want load intentData when first in, you can setRefreshing(true)
     * after {@link #setOnLoadListener(OnLoadListener)}
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing){
        mRefreshLayout.setRefreshing(refreshing);
        if(refreshing && !isLoadingMore && mListener != null){
            mListener.onRefresh();
        }
    }

    /**
     * stop loading more without animation
     */
    public void stopLoadingMore(){
        isLoadingMore = false;
        if (isRefreshEnable){
            mRefreshLayout.setEnabled(true);
        }
        if(mWrapperAdapter != null) {
            mFootView.showNoView();
            mWrapperAdapter.notifyItemRemoved(mWrapperAdapter.getItemCount());
        }
    }
    
    /**
     * call method {@link OnLoadListener#onRefresh()}
     */
    @Override
    public void onRefresh() {
        if(mListener != null){

            //reset footer view status loading
//            if(mFootView != null){
//                mFootView.onLoadingMore();
////                DebugUtils.d("shiming","onrefresh in ");
////                mFootView.showNoView();
//            }

            mListener.onRefresh();
        }
    }

    /**
     * {@link FooterViewListener#onNetChange(boolean isAvailable)}
     * call when network is available or not available
     */
    public void onNetChange(boolean isAvailable) {
        if(mFootView != null){
            mFootView.onNetChange(isAvailable);
        }
    }

    /**
     * {@link FooterViewListener#onLoadingMore()}
     * call when you need change footer view to loading status
     */
    public void onLoadingMore() {
        if(mFootView != null){
            mFootView.onLoadingMore();
        }
    }

    /**
     * {@link FooterViewListener#onNoMore(CharSequence message)}
     * call when no more intentData add to list
     */
    public void onNoMore(CharSequence message) {
        if(mFootView != null){
            mFootView.onNoMore(message);
        }
    }
    //没有数据的视图
    public void showNoView() {
        if(mFootView != null){
            mFootView.showNoView();
        }
    }

    /**
     * {@link FooterViewListener#onError(CharSequence message)}
     * call when you need show error message
     */
    public void onError(CharSequence message) {
        if(mFootView != null){
            mFootView.onError(message);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onLongClickItem(View itemView, int position);
    }

    private class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener, OnLongClickListener{

        public static final int TYPE_FOOTER = 0x100;

        RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

        public WrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
            this.mInnerAdapter = adapter;
        }

        public boolean isLoadMoreItem(int position){
            return isLoadMoreEnable && position == getItemCount() - 1;
        }

        public RecyclerView.Adapter<RecyclerView.ViewHolder> getInnerAdapter() {
            return mInnerAdapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(TYPE_FOOTER == viewType){
                return new FooterViewHolder(mFootView);
            }
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(isLoadMoreItem(position)){
                return;
            }
            holder.itemView.setTag(holder.itemView.getId(), position);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            mInnerAdapter.onBindViewHolder(holder, position);
        }


        @Override
        public int getItemViewType(int position) {
            if(isLoadMoreItem(position)){
                return TYPE_FOOTER;
            }else{
                return mInnerAdapter.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            int count = mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();

            //without loadingMore when adapter size is zero
            if(count == 0){
                return 0;
            }
            return isLoadMoreEnable ? count + 1 : count;
        }

        @Override
        public long getItemId(int position) {
           return mInnerAdapter.getItemId(position);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && isLoadMoreItem(holder.getLayoutPosition()))
            {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            mInnerAdapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            mInnerAdapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        boolean isLoadMore = isLoadMoreItem(position);
                        if(mSpanSizeLookup != null && !isLoadMore){
                            return mSpanSizeLookup.getSpanSize(position);
                        }
                        return isLoadMore ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return mInnerAdapter.onFailedToRecycleView(holder);
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            mInnerAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            mInnerAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            mInnerAdapter.onViewRecycled(holder);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag(view.getId());
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = (int) view.getTag(view.getId());
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onLongClickItem(view, position);
            }
            return true;
        }
    }

    /**
     * ViewHolder of footerView
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * a inner class used to monitor the dataSet change
     * <p>
     * because wrapperAdapter do not know when wrapperAdapter.mInnerAdapter
     * <p>
     * dataSet changed, these method are final
     */
    class DataObserver extends RecyclerView.AdapterDataObserver{

        @Override
        public void onChanged() {
            super.onChanged();
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if(adapter != null && mEmptyView != null){

                int count = 0;
                if(isLoadMoreEnable && adapter.getItemCount() != 0){
                    count ++;
                }
                if(adapter.getItemCount() == count){
                    isEmptyViewShowing = true;
                    if(mEmptyView.getParent() == null){
                        FrameLayout.LayoutParams params = new LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;

                        addView(mEmptyView, params);
                    }

                    recyclerView.setVisibility(GONE);
                    mEmptyView.setVisibility(VISIBLE);
                }else{
                    isEmptyViewShowing = false;
                    mEmptyView.setVisibility(GONE);
                    recyclerView.setVisibility(VISIBLE);
                }
            }
            mWrapperAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
           mWrapperAdapter.notifyItemRangeRemoved(fromPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

    }

    public interface OnLoadListener {

        void onRefresh();

        void onLoadMore();
    }
}
