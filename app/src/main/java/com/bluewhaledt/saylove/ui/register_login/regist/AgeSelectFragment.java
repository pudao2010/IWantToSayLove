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
 * @文件名: AgeSelectFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/29 10:26
 * @描述： 年龄资料填写
 */
public class AgeSelectFragment extends ProfileEditBaseFragment {

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
                arguments.putString(Constants.AGE, ((Integer) item) + "-01-01");
                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AGE, item);
            }
            startFragment(HeightSelectFragment.class, arguments);
        }
    };
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.AGE_SELECT_PAGE);
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
        return R.layout.fragment_age_layout;
    }

    private void assignViews() {
        view = getView();
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_view_grid_picker_page);
        mLayoutContent = (LinearLayout) view.findViewById(R.id.ll_content);
    }

    public void initViewData() {
        final int minAge = 1940, maxAge = 1998;

        final int GRID_VIEW_COUNT = maxAge / 10 - minAge / 10 + 1;
        mAdapters = new GridPickerAdapter[GRID_VIEW_COUNT];
        for (int i = 0; i < GRID_VIEW_COUNT; i++) {
            addGridView(minAge, maxAge, i, i);
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                int scrollToItem = 0;
                scrollToItem = 3;
                View child = mLayoutContent.getChildAt(scrollToItem);
                int top = child.getTop();
                int bottom = child.getBottom();
                int scrollToY = top - (2*(bottom - top) / 3 );// 上一个gridPicker的底部露出一部分
                mScrollView.scrollTo(0, scrollToY);
            }
        });

    }

    private void addGridView(int minAge, int maxAge, int loopIndex, int adapterTag) {
        GridPickerAdapter<Integer> adapter = new GridPickerAdapter<>(getActivity());
        adapter.setTag(adapterTag);
        adapter.setBackgroundColor(getResources().getColor(R.color.white), getResources().getColor(R.color.common_black_color));
        adapter.setTextColor(getResources().getColor(R.color.common_black_color),
                getResources().getColor(R.color.color_saylove));
        adapter.setData(getAges(minAge, maxAge, loopIndex));
        adapter.setColumnCount(4);
        adapter.setAutoFill(true);
        adapter.setPaddings(new int[]{0, DensityUtils.dp2px(getContext(), 15),
                0, DensityUtils.dp2px(getContext(), 15)});
        adapter.setOnItemSelectedListener(mListener);
        mAdapters[loopIndex] = adapter;

        GridPicker<Integer> gridPicker = (GridPicker<Integer>) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_grid_view_with_left_title, null);
        gridPicker.setTitle(getSimplifiedAge(minAge, maxAge, loopIndex));
        gridPicker.setUnit(getContext().getString(R.string.year));
        gridPicker.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtils.dp2px(getContext(), loopIndex == 0 ? 0 : 0.5f);
        mLayoutContent.addView(gridPicker, params);
    }

    private List<Integer> getAges(int min, int max, int loopIndex) {
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

    private String getSimplifiedAge(int min, int max, int loopIndex) {
        final int INT_10 = 10;

        int simplifiedYear = ((min + INT_10 * loopIndex) / INT_10) * INT_10;
        return String.valueOf(simplifiedYear);
    }

}
