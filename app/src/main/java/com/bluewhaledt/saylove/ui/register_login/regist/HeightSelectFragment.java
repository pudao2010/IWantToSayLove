package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.register_login.ProfileEditBaseFragment;
import com.bluewhaledt.saylove.ui.register_login.widget.GridPicker;
import com.bluewhaledt.saylove.ui.register_login.widget.GridPickerAdapter;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login.Fragment
 * @文件名: HeightSelectFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/29 10:26
 * @描述： 身高资料填写
 */
public class HeightSelectFragment extends ProfileEditBaseFragment {

    private GridPickerAdapter<Integer>[] mAdapters;
    private ScrollView mScrollView;
    private LinearLayout mLayoutContent;
    private int mLastPickedAdapterTag = - 1;

    private GridPickerAdapter.OnItemSelectedListener mListener = new GridPickerAdapter.
            OnItemSelectedListener() {
        @Override
        public void onItemSelected(int adapterTag, int position, Object item) {
            if (adapterTag != mLastPickedAdapterTag && mLastPickedAdapterTag >= 0) {
                GridPickerAdapter adapter = mAdapters[mLastPickedAdapterTag];
                adapter.resetChecked();
                adapter.notifyDataSetChanged();
            }
            mLastPickedAdapterTag = adapterTag;

                Bundle arguments = getArguments();
            if (item instanceof Integer) {
                arguments.putInt(Constants.HEIGHT, (Integer) item);
                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.HEIGHT, item);
            }
            startFragment(MaritalStatusFragment.class, arguments);
        }
    };
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.HEIGHT_SELECT_PAGE);
    }

    @Override
    protected void initView() {
        assignViews();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        initViewData();
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_height_layout;
    }

    private void assignViews() {
        view = getView();
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_view_grid_picker_page);
        mLayoutContent = (LinearLayout) view.findViewById(R.id.ll_content);
    }

    public void initViewData() {
        final int minHeight = 130, maxHeight = 209;

        final int GRID_VIEW_COUNT = maxHeight / 10 - minHeight / 10 + 1;
        mAdapters = new GridPickerAdapter[GRID_VIEW_COUNT];
        for (int i = 0; i < GRID_VIEW_COUNT; i++) {
            addGridView(minHeight, maxHeight, i, i);
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                Bundle arguments = getArguments();
                int genderNum = arguments.getInt(Constants.GENDER, 0);
                int scrollToItem = 0;
                if (genderNum == 2){
                     scrollToItem = 3;
                }else if (genderNum == 1){
                     scrollToItem = 4;
                }
                View child = mLayoutContent.getChildAt(scrollToItem);
                int top = child.getTop();
                int bottom = child.getBottom();
                int scrollToY = top - (2*(bottom - top) / 3 );// 上一个gridPicker的底部露出一部分
                mScrollView.scrollTo(0, scrollToY);
            }
        });

    }

    private void addGridView(int minHeight, int maxHeight, int loopIndex, int adapterTag) {
        GridPickerAdapter<Integer> adapter = new GridPickerAdapter<>(getActivity());
        adapter.setTag(adapterTag);
        adapter.setBackgroundColor(getResources().getColor(R.color.white), getResources().getColor(R.color.common_black_color));
        adapter.setTextColor(getResources().getColor(R.color.common_black_color),
                getResources().getColor(R.color.color_saylove));
        adapter.setData(getHeights(minHeight, maxHeight, loopIndex));
        adapter.setColumnCount(4);
        adapter.setAutoFill(true);
        adapter.setPaddings(new int[]{0, DensityUtils.dp2px(getContext(), 15),
                0, DensityUtils.dp2px(getContext(), 15)});
        adapter.setOnItemSelectedListener(mListener);
        mAdapters[loopIndex] = adapter;

        GridPicker<Integer> gridPicker = (GridPicker<Integer>) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_grid_view_with_left_title, null);
        gridPicker.setTitle(getSimplifiedHeight(minHeight, maxHeight, loopIndex));
        gridPicker.setUnit(getContext().getString(R.string.cm));
        gridPicker.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtils.dp2px(getContext(), loopIndex == 0 ? 0 : 0.5f);
        mLayoutContent.addView(gridPicker, params);
    }

    private List<Integer> getHeights(int min, int max, int loopIndex) {
        final int INT_10 = 10;

        int start = ((min + INT_10 * loopIndex) / INT_10) * INT_10;
        int end = Math.min(start + INT_10 - 1, max);
        if (loopIndex == 0) {
            start = min;
        }

        List<Integer> list = new ArrayList<>(INT_10);
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        return list;
    }

    private String getSimplifiedHeight(int min, int max, int loopIndex) {
        final int INT_10 = 10;

        int simplifiedYear = ((min + INT_10 * loopIndex) / INT_10) * INT_10;
        return String.valueOf(simplifiedYear);
    }

}
