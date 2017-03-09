package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.register_login.ProfileEditBaseFragment;
import com.bluewhaledt.saylove.ui.register_login.widget.City;
import com.bluewhaledt.saylove.ui.register_login.widget.Province;
import com.bluewhaledt.saylove.ui.register_login.widget.WheelView;
import com.bluewhaledt.saylove.util.DataDictionaryHelper;
import com.bluewhaledt.saylove.util.DictionaryUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login.Fragment
 * @文件名: CitySelectFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/28 21:23
 * @描述： 所在城市选择页面
 */
public class CitySelectFragment extends ProfileEditBaseFragment implements View.OnClickListener {

    private TextView mTvLivingCity;
    private WheelView mWvProvince;
    private WheelView mWvCity;
    private Button mBtnNext;
    private FragmentActivity mActivity;
    private int mLocationCode = -1;
    private BDLocation mBDLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.DISTRICT_SELECT_PAGE);
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_city_layout;
    }

    @Override
    protected void initView() {
        assignViews();
    }

    @Override
    protected void initData() {
        mActivity = getActivity();
        initWheelViewConfig();
        initWheelViewData();
    }


    protected void initListener() {
        mBtnNext.setOnClickListener(this);
        mWvProvince.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onItemSelected(int selectedIndex, Object item) {
                        Province province = ((Province) item);
                        mWvCity.setItems(province.cities);
                        mWvCity.setSelection(0);
            }
        });
        mWvCity.setOnWheelViewListener(new WheelView.OnWheelViewListener<City>() {
            @Override
            public void onItemSelected(int selectedIndex, City city) {
                    mLocationCode = city.key;
                    EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.DISTRICT_SELECT_WHEEL);
            }
        });
    }


    private void assignViews() {
        View view = getView();
        mTvLivingCity = (TextView) view.findViewById(R.id.tv_livingcity);
        mWvProvince = (WheelView) view.findViewById(R.id.wv_provinve);
        mWvCity = (WheelView) view.findViewById(R.id.wv_city);
        mBtnNext = (Button) view.findViewById(R.id.btn_next);
    }


    private void initWheelViewConfig() {
        mWvProvince.setMinimumWidth(DensityUtils.dp2px(mActivity, 50));
        mWvProvince.setSelectorColor(getResources().getColor(R.color.basic_info_divider_color));
        mWvProvince.setSelectorStrokeWidth(DensityUtils.dp2px(mActivity, 3f));
        mWvProvince.setSelectorVerticalPadding(DensityUtils.dp2px(mActivity, 12));
        mWvProvince.setTextSize(18);
        mWvProvince.setOffset(2);
        mWvProvince.setTextEms(4);

        mWvCity.setMinimumWidth(DensityUtils.dp2px(mActivity, 50));
        mWvCity.setSelectorColor(getResources().getColor(R.color.basic_info_divider_color));
        mWvCity.setSelectorStrokeWidth(DensityUtils.dp2px(mActivity, 3f));
        mWvCity.setSelectorVerticalPadding(DensityUtils.dp2px(mActivity, 12));
        mWvCity.setTextSize(18);
        mWvCity.setOffset(2);
        mWvCity.setTextEms(5);
    }
    
    private void initWheelViewData() {

        ArrayList<Province> provinces = DictionaryUtil.getProvinceBeans(DataDictionaryHelper.FirstItemType.NORMAL);
        mWvProvince.setItems(provinces);
        BDLocation location = ZhenaiApplication.mBDLocation;
        if (location != null) {
            DebugUtils.d("yan", "location != null");
            String provinceStr = location.getProvince();
            String cityOrDistrictStr = location.getCity();
            String districtStr = location.getDistrict();

            Province provinceMatches = DictionaryUtil.getProvinceMatches(provinceStr);
            Province cityMatchesProvince = DictionaryUtil.getProvinceMatches(cityOrDistrictStr);
            DebugUtils.d("yanchao", provinceStr + cityOrDistrictStr + districtStr);
            City city = DictionaryUtil.getCityMatches(provinceMatches, cityOrDistrictStr);
            if (cityMatchesProvince != null) {
                int position = provinces.indexOf(cityMatchesProvince);
                if (position != -1) {
                    mWvProvince.setSelection(position);
                }
                    List<City> cities = cityMatchesProvince.cities;
                    City districtMatchescity = DictionaryUtil.getCityMatches(cityMatchesProvince, districtStr);
                    if (districtMatchescity != null && cities != null){
                        int districtMatchescityPosition = cities.indexOf(districtMatchescity);
                        mWvCity.setSelection(districtMatchescityPosition);
                        mLocationCode = districtMatchescity.key;
                    }else{
                        mLocationCode = cities.get(0).key;
                    }
                }else if (provinceMatches!= null){
                    int position1 = provinces.indexOf(provinceMatches);
                    if (position1 != -1){
                        mWvProvince.setSelection(position1);

                    }
                List<City> cities1 = provinceMatches.cities;
                if (city != null && cities1!= null) {
                    int cityPosition = cities1.indexOf(city);
                    mWvCity.setSelection(cityPosition);
                    mLocationCode = city.key;
                } else {
                    mLocationCode = cities1.get(0).key;
                }
                }

        }else {
            mWvCity.setItems(provinces.get(0).cities);
            DebugUtils.d("yan", "location == null");
        }

        ZhenaiApplication.mCityCode = mLocationCode;
        DebugUtils.d("yan", mLocationCode + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.DISTRICT_SELECT_NEXT);
                String provinceStr = mWvProvince.getSelectedItem();
                String cityStr = mWvCity.getSelectedItem();
                DebugUtils.d("yan", provinceStr+"==="+cityStr);
                Province provinceMatches = DictionaryUtil.getProvinceMatches(provinceStr);
                int key = -1;
                if (provinceMatches!= null){
                    City cityMatches = DictionaryUtil.getCityMatches(provinceMatches, cityStr);
                    if (cityMatches!= null){
                    key = cityMatches.key;
                    }
                }
                Bundle arguments = getArguments();
                arguments.putInt(Constants.CITY_CODE, key);
                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.WORK_CITY,key);
                startFragment(AgeSelectFragment.class,arguments);
                break;
        }
    }


}
