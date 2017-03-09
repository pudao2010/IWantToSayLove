package com.bluewhaledt.saylove.ui.info.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ui.info.adapter.PhotoAndVideoAdapter;
import com.bluewhaledt.saylove.ui.info.entity.PhotoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rade.chan on 2016/11/28.
 */

public class PhotoAndVideoLayout extends FrameLayout {

    private RecyclerView mPhotoRecyclerView;
    private PhotoAndVideoAdapter mPhotoAdapter;
    private TextView mTitleView;

    private Context mContext;
    private List<PhotoEntity> mList = new ArrayList<>();
    private boolean mIsShowUpload;
    private boolean mIsShowLineView;
    private int mUploadType;


    public static final int UPLOAD_TYPE_PHOTO = 1;
    public static final int UPLOAD_TYPE_VIDEO = 2;

    private boolean isLoading = false;
    private boolean isCanLoadMore = false;

    private OnLoadMoreListener loadMoreListener;


    public PhotoAndVideoLayout(Context context) {
        this(context, null);
    }

    public PhotoAndVideoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PhotoAndVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoAndVideoLayout);
        mIsShowUpload = typedArray.getBoolean(R.styleable.PhotoAndVideoLayout_isShowUpload, false);
        mIsShowLineView = typedArray.getBoolean(R.styleable.PhotoAndVideoLayout_isShowLineView, true);
        mUploadType = typedArray.getInt(R.styleable.PhotoAndVideoLayout_uploadType, UPLOAD_TYPE_PHOTO);
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.widget_photo_layout, this, false);
        addView(view);
        initView(view);
    }

    private void initView(View rootView) {
        mTitleView = (TextView) rootView.findViewById(R.id.title_view);
        mPhotoRecyclerView = (RecyclerView) rootView.findViewById(R.id.photo_recycler_view);
        mPhotoAdapter = new PhotoAndVideoAdapter(mContext, mList, mIsShowUpload, mUploadType);
        if (!mIsShowLineView) {
            rootView.findViewById(R.id.top_line_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.bottom_line_view).setVisibility(View.GONE);
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public void addOnLoadMoreListener(OnLoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        boolean isSlidingToLast = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading && isCanLoadMore) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部
                if (lastVisibleItem == (totalItemCount - 1)) {
                    //加载更多功能的代码
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore(mUploadType);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
            if (dx > 0) {
                //大于0表示正在向右滚动
                isSlidingToLast = true;
            } else {
                //小于等于0表示停止或向左滚动
                isSlidingToLast = false;
            }

        }
    };


    public void addItemListener(PhotoAndVideoAdapter.OnPhotoAndVideoLayoutListener listener) {
        mPhotoAdapter.setOnPhotoAndVideoLayoutListener(listener);
    }


    public void addItem(List<PhotoEntity> list, String title) {
        mList.clear();
        mList.addAll(list);
        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setText(title);
        }
        mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        // mPhotoRecyclerView.addItemDecoration(new SpacesItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.picture_between_dimen)));
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mPhotoRecyclerView.addOnScrollListener(mScrollListener);
    }


    public interface OnLoadMoreListener {
        void onLoadMore(int uploadType);
    }


}
