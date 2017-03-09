package com.bluewhaledt.saylove.util;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.widget.picker_view.OptionsDividePickView;
import com.bluewhaledt.saylove.base.widget.picker_view.OptionsPickerView;
import com.bluewhaledt.saylove.entity.DictionaryBean;
import com.bluewhaledt.saylove.ui.register_login.widget.City;
import com.bluewhaledt.saylove.ui.register_login.widget.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bluewhaledt.saylove.util.DataDictionaryHelper.FirstItemType.NORMAL;
import static com.bluewhaledt.saylove.util.DataDictionaryHelper.FirstItemType.NO_LIMIT;

/**
 * Created by zhenai-liliyan on 16/11/23.
 */

public class DictionaryUtil {


    private static ArrayList<DictionaryBean> getProvinces(DataDictionaryHelper.FirstItemType type) {
        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        try {
            return DataDictionaryHelper.parseDicItem(array, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ArrayList<ArrayList<DictionaryBean>> getCities() {
        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        ArrayList<ArrayList<DictionaryBean>> cities = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                cities.add(DataDictionaryHelper.parseDicItem(array.getJSONObject(i).getJSONArray(DataDictionaryHelper.CITIES), NORMAL));
            }

            String noLimit = ZhenaiApplication.getInstance().getResources().getString(R.string.no_limit);
            DictionaryBean bean = new DictionaryBean(noLimit, -1);
            ArrayList<DictionaryBean> cells = new ArrayList<>();
            cells.add(bean);
            cities.add(0, cells);

            return cities;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ArrayList<ArrayList<DictionaryBean>> getCities(DataDictionaryHelper.FirstItemType type) {
        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        ArrayList<ArrayList<DictionaryBean>> cities = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                cities.add(DataDictionaryHelper.parseDicItem(array.getJSONObject(i).getJSONArray(DataDictionaryHelper.CITIES), type));
            }
            if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {
                String pleaseSelect = ZhenaiApplication.getInstance().getResources().getString(R.string.select_nothing);
                DictionaryBean bean = new DictionaryBean(pleaseSelect, -1);
                ArrayList<DictionaryBean> cells = new ArrayList<>();
                cells.add(bean);
                cities.add(0, cells);
            } else if (type == NO_LIMIT) {
                String noLimit = ZhenaiApplication.getInstance().getResources().getString(R.string.no_limit);
                DictionaryBean bean = new DictionaryBean(noLimit, -1);
                ArrayList<DictionaryBean> cells = new ArrayList<>();
                cells.add(bean);
                cities.add(0, cells);
            }
            return cities;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<ArrayList<ArrayList<DictionaryBean>>> getDistricts(DataDictionaryHelper.FirstItemType type) {
        ArrayList<ArrayList<ArrayList<DictionaryBean>>> districts = new ArrayList<>();

        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        String pleaseSelect = ZhenaiApplication.getInstance().getResources().getString(R.string.select_nothing);
        String noLimit = ZhenaiApplication.getInstance().getResources().getString(R.string.no_limit);
        if (array != null) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONArray citiesArray = array.getJSONObject(i).getJSONArray(DataDictionaryHelper.CITIES);
                    ArrayList<ArrayList<DictionaryBean>> districtsInCity = new ArrayList<>();
                    for (int j = 0; j < citiesArray.length(); j++) {
                        if (citiesArray.getJSONObject(j).has(DataDictionaryHelper.DISTRICTS)
                                && !citiesArray.getJSONObject(j).isNull(DataDictionaryHelper.DISTRICTS)) {
                            JSONArray districtArray = citiesArray.getJSONObject(j).getJSONArray(DataDictionaryHelper.DISTRICTS);
                            ArrayList<DictionaryBean> data = DataDictionaryHelper.parseDicItem(districtArray, type);
                            if (data.size() <= 0) {//如果当前没有区这一级，则根据调用者的type类型，决定是否需要添加默认选项
                                if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {//添加"请选择"
                                    DictionaryBean bean = new DictionaryBean(pleaseSelect, -1);
                                    data.add(bean);
                                } else if (type == NO_LIMIT) {//添加"不限"
                                    DictionaryBean bean = new DictionaryBean(noLimit, -1);
                                    data.add(bean);
                                }
                            }
                            districtsInCity.add(data);

                        } else { //如果当前没有区这一级，则根据调用者的type类型，决定是否需要添加默认选项
                            if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {
                                ArrayList<DictionaryBean> data = new ArrayList<>();
                                DictionaryBean bean = new DictionaryBean(pleaseSelect, -1);
                                data.add(bean);
                                districtsInCity.add(data);

                            } else if (type == NO_LIMIT) {
                                ArrayList<DictionaryBean> data = new ArrayList<>();
                                DictionaryBean bean = new DictionaryBean(noLimit, -1);
                                data.add(bean);
                                districtsInCity.add(data);
                            }
                        }

                    }

                    //判断城市选择第一项是否增加了"请选择"，或"不限"，如果有则为整个districts添加一栏，保持数据个数对得上
                    if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {
                        DictionaryBean pleaseBean = new DictionaryBean(pleaseSelect, -1);
                        ArrayList<DictionaryBean> cells = new ArrayList<>();
                        cells.add(pleaseBean);
                        districtsInCity.add(0, cells);
                    } else if (type == NO_LIMIT) {

                        DictionaryBean noLimitBean = new DictionaryBean(noLimit, -1);
                        ArrayList<DictionaryBean> cells = new ArrayList<>();
                        cells.add(noLimitBean);
                        districtsInCity.add(0, cells);
                    }
                    districts.add(districtsInCity);
                }
                if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {
                    DictionaryBean bean = new DictionaryBean(pleaseSelect, -1);
                    ArrayList<DictionaryBean> cells = new ArrayList<>();
                    ArrayList<ArrayList<DictionaryBean>> items = new ArrayList<>();
                    cells.add(bean);
                    items.add(cells);
                    districts.add(0, items);
                } else if (type == NO_LIMIT) {
                    DictionaryBean bean = new DictionaryBean(noLimit, -1);
                    ArrayList<DictionaryBean> cells = new ArrayList<>();
                    ArrayList<ArrayList<DictionaryBean>> items = new ArrayList<>();
                    cells.add(bean);
                    items.add(cells);
                    districts.add(0, items);
                }
                DebugUtils.d("", "=====<>");
                return districts;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Province getProvinceMatches(String provinceStr) {
        String suffix1 = "省";
        String suffix2 = "市";
        if (provinceStr.contains(suffix1)) {
            provinceStr = provinceStr.replace(suffix1, "");
        } else if (provinceStr.contains(suffix2)) {
            provinceStr = provinceStr.replace(suffix2, "");
        }

        Iterator<Province> iterator = getProvinceBeans(DataDictionaryHelper.FirstItemType.NORMAL).iterator();
        while (iterator.hasNext()) {
            Province next = iterator.next();
            if (next.value.equals(provinceStr)) {
                return next;
            }
        }
        return null;
    }

    public static City getCityMatches(Province parentProvince, String cityOrDistrictStr) {
        if (parentProvince == null) {
            for (Province province : getProvinceBeans(DataDictionaryHelper.FirstItemType.NONE)) {
                City city = getCityMatches(province.cities, cityOrDistrictStr);
                if (city != null) {
                    return city;
                }
            }
        } else {
            return getCityMatches(parentProvince.cities, cityOrDistrictStr);
        }
        return null;
    }

    public static City getCityMatches(List<City> cities, String cityOrDistrictStr) {
        String suffix1 = "自治州";
        String suffix2 = "市"; // 内蒙古的旗不作处理
        if (!cityOrDistrictStr.endsWith(suffix1) && cityOrDistrictStr.endsWith(suffix2)) {
            cityOrDistrictStr = cityOrDistrictStr.substring(0, cityOrDistrictStr.length() - 1);
        }

        if (cities != null) {
            for (City city : cities) {
                if (city.value.equals(cityOrDistrictStr)) {
                    return city;
                }
            }
        }
        return null;
    }

    public static ArrayList<Province> getProvinceBeans(DataDictionaryHelper.FirstItemType type) {
        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        ArrayList<Province> provincesResult = new ArrayList<>();

        try {
            ArrayList<DictionaryBean> provinces = DataDictionaryHelper.parseDicItem(array, DataDictionaryHelper.FirstItemType.NONE);
            for (int i = 0; i < array.length(); i++) {
                ArrayList<DictionaryBean> cities = DataDictionaryHelper.parseDicItem(array.getJSONObject(i).getJSONArray(DataDictionaryHelper.CITIES), type);
                Province province = new Province();
                province.key = provinces.get(i).key;
                province.value = provinces.get(i).value;
                province.cities = new ArrayList<>();
                for (DictionaryBean bean : cities) {
                    City city = new City();
                    city.key = bean.key;
                    city.value = bean.value;
                    province.cities.add(city);
                }
                provincesResult.add(province);

            }
            if (type == DataDictionaryHelper.FirstItemType.CHOOSE) {
                Province province = new Province();
                province.key = -1;
                province.value = ZhenaiApplication.getInstance().getResources().getString(R.string.select_nothing);
                province.cities = new ArrayList<>();
                provincesResult.add(0, province);
            } else if (type == NO_LIMIT) {
                Province province = new Province();
                province.key = -1;
                province.value = ZhenaiApplication.getInstance().getResources().getString(R.string.no_limit);
                province.cities = new ArrayList<>();
                provincesResult.add(0, province);
            }
            return provincesResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provincesResult;

    }


    /**
     * 获取数据字典里面相应key的数据
     *
     * @param dataType 数据字典的key字段，如想获取数据字典里面的省份数据(province)
     * @param type     返回的数据第一项是否需要加入"请选择"，"不限"或不加等
     * @return 数据结果
     */
    private static ArrayList<DictionaryBean> getDictionaryData(String dataType, DataDictionaryHelper.FirstItemType type) {
        JSONArray array = DataDictionaryHelper.getDataByType(dataType);
        try {
            return DataDictionaryHelper.parseDicItem(array, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getSingleValueByKey(String dataType, int key) {
        ArrayList<DictionaryBean> list = getDictionaryData(dataType, NO_LIMIT);
        if (list != null && list.size() > 0) {
            for (DictionaryBean bean : list) {
                if (bean.key == key) {
                    return bean.value;
                }
            }
        }
        return "";
    }

    /**
     * 根据code查询性别
     *
     * @param code
     * @return 返回性别字符串
     */
    public static String getSexByCode(int code) {
        JSONArray sexArray = DataDictionaryHelper.getDataByType(DataDictionaryHelper.SEX);
        for (int i = 0; i < sexArray.length(); i++) {
            JSONObject sexObj = sexArray.optJSONObject(i);
            int sexKey = sexObj.optInt("key");
            if (code == sexKey) {
                return sexObj.optString("value");
            }
        }
        return null;
    }


    /**
     * 根据code查找省市
     *
     * @param code
     * @return 返回地址
     */
    public static String getLocationByCode(int code) {
        JSONArray array = DataDictionaryHelper.getDataByType(DataDictionaryHelper.PROVINCES);
        int prefix5digit = code / 1000;                 // 地区编码前5位
        String otherCountrySuffix = "00000";        // 国外地区后5位为00000
        if (array != null) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject provinceObj = array.optJSONObject(i);
                    int provinceKey = provinceObj.optInt("key");
                    if (provinceKey == code) {
                        return provinceObj.optString("value");
                    }

                    if ((provinceKey / 1000) == prefix5digit || String.valueOf(provinceKey).endsWith(otherCountrySuffix)) {
                        JSONArray citiesArray = provinceObj.getJSONArray(DataDictionaryHelper.CITIES);
                        for (int j = 0; j < citiesArray.length(); j++) {
                            JSONObject cityObj = citiesArray.optJSONObject(j);
                            int cityKey = cityObj.optInt("key");
                            if (cityKey == code) {
                                return provinceObj.optString("value") + " " + cityObj.optString("value");
                            }
                            if (cityObj.has(DataDictionaryHelper.DISTRICTS)
                                    && !cityObj.isNull(DataDictionaryHelper.DISTRICTS)) {
                                JSONArray districtArray = cityObj.optJSONArray(DataDictionaryHelper.DISTRICTS);
                                for (int k = 0; k < districtArray.length(); k++) {
                                    JSONObject districtObj = districtArray.optJSONObject(k);
                                    int districtKey = districtObj.optInt("key");
                                    if (districtKey == code) {
                                        return cityObj.optString("value") + " " + districtObj.optString("value");
                                    }
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 只有一个滚轮的选择框
     *
     * @param context
     * @param titleId
     * @param dataType
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    private static OptionsDividePickView showSinglePickerView(final Context context, int titleId, String dataType, int key, DataDictionaryHelper.FirstItemType type, final OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        OptionsDividePickView<DictionaryBean> pvOptions = new OptionsDividePickView<>(context);
        final ArrayList<DictionaryBean> data
                = DictionaryUtil.getDictionaryData(dataType, type);
        int defaultPosition = getDefaultSelectPosition(data, key);
        pvOptions.setPicker(data);
        pvOptions.setTitle(context.getResources().getString(titleId));
        pvOptions.setCyclic(false);
        pvOptions.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onSingleSelectItemsCallback.onSelectItems(item1);
            }
        });
        pvOptions.setSelectOptions(defaultPosition);
        pvOptions.show();
        return pvOptions;
    }

    private static int getDefaultSelectPosition(ArrayList<DictionaryBean> beans, int key) {
        int defaultPosition = 0;
        for (DictionaryBean bean : beans) {
            if (bean.key == key) {
                return defaultPosition;
            }
            defaultPosition++;
        }
        return 0;
    }

    public static String getValueByKey(int key, String dataType) {
        final ArrayList<DictionaryBean> data
                = DictionaryUtil.getDictionaryData(dataType, DataDictionaryHelper.FirstItemType.NONE);
        for (DictionaryBean bean : data) {
            if (bean.key == key) {
                return bean.value;
            }
        }
        return "";
    }

    /**
     * 登陆情况选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showLoginStatusWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.login_status, DataDictionaryHelper.LOGIN_STATUS, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 形象照选择框
     *
     * @param context
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showAvatarWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.avatar, DataDictionaryHelper.AVATAR, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 民族
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showNationWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.nation, DataDictionaryHelper.NATION, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 信仰选择框
     *
     * @param context
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showFaithWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.faith, DataDictionaryHelper.FAITH, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 生肖选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showChineseZodiacWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.chinese_zodiac, DataDictionaryHelper.CHINESE_ZODIAC, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 星座选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showZodiacWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.zodiac, DataDictionaryHelper.ZODIAC, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 买车状况
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showCarWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.car, DataDictionaryHelper.CAR, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 住房状况
     *
     * @param context
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showHouseWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.house, DataDictionaryHelper.HOUSE, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 职业选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showJobWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.job, DataDictionaryHelper.JOB, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 有无孩子选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showChildrenWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.children, DataDictionaryHelper.CHILDREN, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 身份认证选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showIDAuthenticationWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.id_authentication, DataDictionaryHelper.AUTHENTICATION, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 婚姻状况选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showMaritalStatusWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.marital_status, DataDictionaryHelper.MARRIAGE, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 饮酒情况
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showDrinkingWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.drinking, DataDictionaryHelper.DRINKING, key, type, onSingleSelectItemsCallback);
    }


    /**
     * 饮酒情况
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showSmokingWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.smoking, DataDictionaryHelper.SMOKING, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 学历选择框
     *
     * @param context
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showEducationWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.education, DataDictionaryHelper.EDUCATION, key, type, onSingleSelectItemsCallback);
    }

    public interface OnSingleSelectItemsCallback {
        void onSelectItems(DictionaryBean item);
    }

    public interface OnDoubleSelectItemsCallback {
        void onSelectItems(DictionaryBean item1, DictionaryBean item2);
    }

    /**
     * 用户身高选择框
     *
     * @param context
     * @param sex                         根据性别有不同的选项，性别 1男 2女
     * @param leftKey
     * @param rightKey
     * @param type
     * @param onDoubleSelectItemsCallback
     */
    public static OptionsDividePickView showHeightPickerView(final Context context, int sex, int leftKey, int rightKey,
                                                             DataDictionaryHelper.FirstItemType type, final OnDoubleSelectItemsCallback onDoubleSelectItemsCallback) {
        OptionsDividePickView<DictionaryBean> pvOptions = new OptionsDividePickView<>(context);
        final ArrayList<DictionaryBean> heights = DictionaryUtil.getDictionaryData(DataDictionaryHelper.HEIGHT, type);
        int leftIndex = getDefaultSelectPosition(heights, leftKey);
        int rightIndex = getDefaultSelectPosition(heights, rightKey);

        pvOptions.setPicker(heights, heights);
        pvOptions.setTitle(context.getResources().getString(R.string.height));
        pvOptions.setCyclic(false, false);
        pvOptions.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onDoubleSelectItemsCallback.onSelectItems(item1, item2);
            }
        });
        if (leftIndex != 0 || rightIndex != 0) {
            pvOptions.setSelectOptions(leftIndex, rightIndex);
        } else {
            if (sex == 1) {//男
                DictionaryBean beanLeft = new DictionaryBean("150", 150);
                int left = heights.indexOf(beanLeft);
                DictionaryBean beanRight = new DictionaryBean("180", 180);
                int right = heights.indexOf(beanRight);
                pvOptions.setSelectOptions(left, right);
            } else {
                DictionaryBean beanLeft = new DictionaryBean("150", 150);
                int left = heights.indexOf(beanLeft);
                DictionaryBean beanRight = new DictionaryBean("170", 170);
                int right = heights.indexOf(beanRight);
                pvOptions.setSelectOptions(left, right);
            }
        }

        pvOptions.show();
        return pvOptions;
    }


    /**
     * 用户身高选择框  (单栏)
     *
     * @param context
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showSingleHeightPickerView(final Context context, DataDictionaryHelper.FirstItemType type,
                                                                   int key, final OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        OptionsDividePickView<DictionaryBean> pvOptions = new OptionsDividePickView<>(context);
        final ArrayList<DictionaryBean> heights = DictionaryUtil.getDictionaryData(DataDictionaryHelper.HEIGHT, type);
        int selectIndex = getDefaultSelectPosition(heights, key);
        pvOptions.setPicker(heights);
        pvOptions.setTitle(context.getResources().getString(R.string.height));
        pvOptions.setCyclic(false);
        pvOptions.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onSingleSelectItemsCallback.onSelectItems(item1);
            }
        });
        if (selectIndex != 0) {
            pvOptions.setSelectOptions(selectIndex);
        } else {
            DictionaryBean bean = new DictionaryBean("150", 150);
            int index = heights.indexOf(bean);
            pvOptions.setSelectOptions(index);
        }
        pvOptions.show();
        return pvOptions;
    }

    /**
     * @param context
     * @param sex                         根据性别有不同的选项，性别 1男 2女
     * @param selectedPositionKey
     * @param type
     * @param onSingleSelectItemsCallback
     */
    public static void showWeightPickerView(final Context context, int sex, int selectedPositionKey,
                                            DataDictionaryHelper.FirstItemType type, final OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        final ArrayList<DictionaryBean> weight = new ArrayList<>();
        if (sex == 1) {//男
            weight.addAll(DictionaryUtil.getDictionaryData(DataDictionaryHelper.BODY_MAN, type));
        } else {
            weight.addAll(DictionaryUtil.getDictionaryData(DataDictionaryHelper.BODY_FEMALE, type));
        }
        OptionsDividePickView<DictionaryBean> pvOptions = new OptionsDividePickView<>(context);
        pvOptions.setTitle(context.getResources().getString(R.string.weight));
        pvOptions.setPicker(weight);
        pvOptions.setCyclic(false);
        pvOptions.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onSingleSelectItemsCallback.onSelectItems(item1);
            }
        });
        int defaultSelectIndex = getDefaultSelectPosition(weight, selectedPositionKey);
        pvOptions.setSelectOptions(defaultSelectIndex);

        pvOptions.show();
    }

    /**
     * 两个滚轮的选择框
     *
     * @param context
     * @param titleId
     * @param dataType
     * @param type
     * @param leftSelectedKey             默认选中项的key
     * @param rightSelectedKey            默认选中项的key
     * @param onDoubleSelectItemsCallback
     */
    private static OptionsDividePickView showDoubleWheelNoLinkPickerView(final Context context, int titleId, String dataType,
                                                                         DataDictionaryHelper.FirstItemType type, int leftSelectedKey, int rightSelectedKey,
                                                                         final OnDoubleSelectItemsCallback onDoubleSelectItemsCallback) {
        OptionsDividePickView<DictionaryBean> pvOptions = new OptionsDividePickView<>(context);
        final ArrayList<DictionaryBean> data = getDictionaryData(dataType, type);
        int leftSelectedIndex = getDefaultSelectPosition(data, leftSelectedKey);
        int rightSelectedIndex = getDefaultSelectPosition(data, rightSelectedKey);

        pvOptions.setPicker(data, data);
        pvOptions.setTitle(context.getResources().getString(titleId));
        pvOptions.setCyclic(false, false);

        pvOptions.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onDoubleSelectItemsCallback.onSelectItems(item1, item2);
            }
        });
        pvOptions.setSelectOptions(leftSelectedIndex, rightSelectedIndex);
        pvOptions.show();
        return pvOptions;
    }

    /**
     * 工资选择框
     *
     * @param context
     * @param type
     * @param leftSelectedKey             默认选中项的key
     * @param rightSelectedKey            默认选中项的key
     * @param onDoubleSelectItemsCallback
     */
    public static void showSalaryWheelPickerView(Context context,
                                                 DataDictionaryHelper.FirstItemType type, int leftSelectedKey, int rightSelectedKey,
                                                 DictionaryUtil.OnDoubleSelectItemsCallback onDoubleSelectItemsCallback) {
        DictionaryUtil.showDoubleWheelNoLinkPickerView(context, R.string.salary_month, DataDictionaryHelper.SALARY, type, leftSelectedKey, rightSelectedKey, onDoubleSelectItemsCallback);
    }

    /**
     * 自己工资选择  单栏
     *
     * @param context
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static void showRegisterSalaryWheelPickerView(Context context,
                                                         DataDictionaryHelper.FirstItemType type,
                                                         int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        DictionaryUtil.showSinglePickerView(context, R.string.income, DataDictionaryHelper.SALARY_FRO_REGISTER, key, type, onSingleSelectItemsCallback);
    }


    /**
     * 年薪选择  单栏
     *
     * @param context
     * @param type
     * @param key                         默认选中的值
     * @param onSingleSelectItemsCallback
     */
    public static OptionsDividePickView showYearIncomeWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.income, DataDictionaryHelper.YEAR_INCOME, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 择偶年薪选择  单栏
     * @param context
     * @param type
     * @param key
     * @param onSingleSelectItemsCallback
     * @return
     */
    public static OptionsDividePickView showRequireYearIncomeWheelPickerView(Context context, DataDictionaryHelper.FirstItemType type, int key, OnSingleSelectItemsCallback onSingleSelectItemsCallback) {
        return DictionaryUtil.showSinglePickerView(context, R.string.income, DataDictionaryHelper.REQUIRE_YEAR_INCOME, key, type, onSingleSelectItemsCallback);
    }

    /**
     * 年龄选择框
     *
     * @param context
     * @param type
     * @param leftSelectedKey             默认选中项的key
     * @param rightSelectedKey            默认选中项的key
     * @param onDoubleSelectItemsCallback
     */
    public static OptionsDividePickView showAgeWheelPickerView(Context context,
                                                               DataDictionaryHelper.FirstItemType type, int leftSelectedKey, int rightSelectedKey,
                                                               DictionaryUtil.OnDoubleSelectItemsCallback onDoubleSelectItemsCallback) {
        return DictionaryUtil.showDoubleWheelNoLinkPickerView(context, R.string.age, DataDictionaryHelper.AGE_SPAN, type, leftSelectedKey, rightSelectedKey, onDoubleSelectItemsCallback);
    }

    /**
     * 地区的选择框
     *
     * @param context
     * @param onItemSelectListener
     */
    public static OptionsPickerView showPlacePickerView(final Context context,String title,
                                                        DataDictionaryHelper.FirstItemType type,
                                                        int selectedPlaceKey,
                                                        OptionsPickerView.OnItemSelectListener<DictionaryBean> onItemSelectListener) {
        int provinceIndex = 0;
        int cityIndex = 0;
        int districtIndex = 0;
        boolean isFind = false;
        OptionsPickerView<DictionaryBean> optionsPickerView = new OptionsPickerView<>(context);
        ArrayList<DictionaryBean> nativeProvinces = DictionaryUtil.getProvinces(type);
        ArrayList<ArrayList<DictionaryBean>> nativeCities = DictionaryUtil.getCities(type);

        outerLoop:
        for (ArrayList<DictionaryBean> cities : nativeCities) {
            for (DictionaryBean city : cities) {
                if (city.key == selectedPlaceKey) {
                    isFind = true;
                    break outerLoop;
                }
                cityIndex++;
            }
            provinceIndex++;
            cityIndex = 0;
        }
        ArrayList<ArrayList<ArrayList<DictionaryBean>>> nativeDistricts = DictionaryUtil.getDistricts(type);
        if (!isFind) {
            provinceIndex = 0;
            cityIndex = 0;
            districtIndex = 0;
            outerLoop:
            for (ArrayList<ArrayList<DictionaryBean>> province : nativeDistricts) {
                for (ArrayList<DictionaryBean> city : province) {
                    for (DictionaryBean district : city) {
                        if (district.key == selectedPlaceKey) {
                            isFind = true;
                            break outerLoop;
                        }
                        districtIndex++;
                    }
                    cityIndex++;
                    districtIndex = 0;
                }
                cityIndex = 0;
                provinceIndex++;
            }
        }


        if (!isFind) {
            provinceIndex = 0;
            cityIndex = 0;
            districtIndex = 0;
        }


        optionsPickerView.setPicker(nativeProvinces, nativeCities, nativeDistricts, true);
        optionsPickerView.setTitle(title);
        optionsPickerView.setCyclic(false, false, false);
        optionsPickerView.setSelectOptions(provinceIndex, cityIndex, districtIndex);
        optionsPickerView.setOnItemSelectListener(onItemSelectListener);

        optionsPickerView.show();
        return optionsPickerView;
    }

    public static String getCityNameByCode(int code) {
//        int provinceIndex = 0;
//        int cityIndex = 0;
//        int districtIndex = 0;
        boolean isFind = false;
//        OptionsPickerView<DictionaryBean> optionsPickerView = new OptionsPickerView<>(context);
//        ArrayList<DictionaryBean> nativeProvinces = DictionaryUtil.getProvinces(DataDictionaryHelper.FirstItemType.NONE);
        StringBuilder sb = new StringBuilder();
        ArrayList<ArrayList<DictionaryBean>> nativeCities = DictionaryUtil.getCities(DataDictionaryHelper.FirstItemType.NONE);

        outerLoop:
        for (ArrayList<DictionaryBean> cities : nativeCities) {
            for (DictionaryBean city : cities) {
                if (city.key == code) {
                    isFind = true;
                    sb.append(city.value);
                    break outerLoop;
                }
//                cityIndex++;
            }
//            provinceIndex++;
//            cityIndex = 0;
        }
        ArrayList<ArrayList<ArrayList<DictionaryBean>>> nativeDistricts = DictionaryUtil.getDistricts(DataDictionaryHelper.FirstItemType.NONE);
        if (!isFind) {
//            provinceIndex = 0;
//            cityIndex = 0;
//            districtIndex = 0;
            outerLoop:
            for (ArrayList<ArrayList<DictionaryBean>> province : nativeDistricts) {
                for (ArrayList<DictionaryBean> city : province) {
                    for (DictionaryBean district : city) {
                        if (district.key == code) {
                            sb.append(district.value);
                            break outerLoop;
                        }
//                        districtIndex++;
                    }
//                    cityIndex++;
//                    districtIndex = 0;
                }
//                cityIndex = 0;
//                provinceIndex++;
            }
        }
        return sb.toString();
    }

    /**
     * 籍贯的选择框
     *
     * @param context
     * @param onDoubleSelectItemsCallback
     */
    public static OptionsPickerView showNativePlacePickerView(final Context context,String title,
                                                              DataDictionaryHelper.FirstItemType type,
                                                              int selectedPlaceKey,
                                                              final DictionaryUtil.OnDoubleSelectItemsCallback onDoubleSelectItemsCallback) {
        OptionsPickerView<DictionaryBean> optionsPickerView = new OptionsPickerView<>(context);
        ArrayList<DictionaryBean> nativeProvinces = DictionaryUtil.getProvinces(type);
        ArrayList<ArrayList<DictionaryBean>> nativeCities = null;
        if (type == NO_LIMIT) {
            nativeCities = DictionaryUtil.getCities();
        } else {
            nativeCities = DictionaryUtil.getCities(type);
        }
        int provinceIndex = 0;
        int cityIndex = 0;
        boolean isFind = false;
        outerLoop:
        for (ArrayList<DictionaryBean> cities : nativeCities) {
            for (DictionaryBean city : cities) {
                if (city.key == selectedPlaceKey) {
                    isFind = true;
                    break outerLoop;
                }
                cityIndex++;
            }
            cityIndex = 0;
            provinceIndex++;
        }
        if (!isFind) {
            provinceIndex = 0;
            cityIndex = 0;
        }

        optionsPickerView.setPicker(nativeProvinces, nativeCities, true);
        optionsPickerView.setTitle(title);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setSelectOptions(provinceIndex, cityIndex);


        optionsPickerView.setOnItemSelectListener(new OptionsPickerView.OnItemSelectListener<DictionaryBean>() {
            @Override
            public void onItemSelect(DictionaryBean item1, DictionaryBean item2, DictionaryBean item3) {
                onDoubleSelectItemsCallback.onSelectItems(item1, item2);
            }
        });
        optionsPickerView.show();
        return optionsPickerView;
    }


}
