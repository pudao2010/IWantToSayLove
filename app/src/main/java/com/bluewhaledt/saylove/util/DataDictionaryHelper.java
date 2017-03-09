package com.bluewhaledt.saylove.util;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.StreamUtils;
import com.bluewhaledt.saylove.base.util.StringUtils;
import com.bluewhaledt.saylove.entity.DictionaryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import static com.bluewhaledt.saylove.ZhenaiApplication.getContext;

/**
 * Created by zhenai-liliyan on 16/11/23.
 */

public class DataDictionaryHelper {

    /**
     * 数据字典文件名
     */
    private static final String DATA_DIC_FILENAME = "data_dictionary.json";
    /**
     * 缓存数据字典json对象
     */
    private static SoftReference<JSONObject> dataJson;

    /**
     * 年龄数据
     */
    public static final String AGE_SPAN = "ageSpan";

    /**
     * 性别
     */
    public static final String SEX = "sex";

    /**
     * 省份数据
     */
    public static final String PROVINCES = "province";

    public static final String CITIES = "city";

    public static final String DISTRICTS = "district";

    public static final String HEIGHT = "heightNoLimited";

    public static final String SALARY = "salaryObject";

    public static final String SALARY_FRO_REGISTER = "salaryRegister";

    public static final String EDUCATION = "education";

    public static final String MARRIAGE = "marriage";

    public static final String AUTHENTICATION = "authentication";

    public static final String CHILDREN = "children";

    public static final String BODY_FEMALE = "bodyFemale";

    public static final String BODY_MAN = "bodyMan";

    public static final String JOB = "profession";

    public static final String HOUSE = "house";

    public static final String CAR = "vehicleConditionArray";

    /**
     * 星座
     */
    public static final String ZODIAC = "constellation";
    /**
     * 生肖
     */
    public static final String CHINESE_ZODIAC = "animals";

    public static final String FAITH = "belief";

    /**
     * 民族
     */
    public static final String NATION = "stock";

    public static final String AVATAR = "facePhoto";

    public static final String LOGIN_STATUS = "loginSituation";

    /**
     * 抽烟
     */
    public static final String SMOKING = "smoking";

    public static final String DRINKING = "drinking";

    public static final String YEAR_INCOME="yearIncome";

    public static final String REQUIRE_YEAR_INCOME="requireYearIncome";


//    public static ArrayList<DictionaryBean> getDataByType(String type) {
//        JSONObject dataJson = getDataDicJson();
//        if (dataJson != null) {
//            try {
//                JSONArray typeData = dataJson.getJSONObject("data").getJSONArray(type);
//                return parseDicItem(typeData);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    public static JSONArray getDataByType(String type) {
        JSONObject dataJson = getDataDicJson();
        if (dataJson != null) {
            try {
                JSONArray typeData = dataJson.getJSONObject("data").getJSONArray(type);
                return typeData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static ArrayList<DictionaryBean> parseDicItem(JSONArray jsonItem, FirstItemType type) throws JSONException {
        ArrayList<DictionaryBean> items = new ArrayList<>();
        for (int i = 0; i < jsonItem.length(); i++) {
            JSONObject json = jsonItem.getJSONObject(i);
            DictionaryBean bean = new DictionaryBean(json.getString("value"), json.getInt("key"));
            items.add(bean);
        }
        String noLimit = ZhenaiApplication.getInstance().getResources().getString(R.string.no_limit);
        String pleaseSelect = ZhenaiApplication.getInstance().getResources().getString(R.string.please_select);
        if (type == FirstItemType.NO_LIMIT && items.size() > 0) {
            if (items.get(0).value.equals(pleaseSelect)) {
                items.get(0).value = noLimit;
            } else if (!items.get(0).value.equals(noLimit)) {
                DictionaryBean bean = new DictionaryBean(noLimit, -1);
                items.add(0, bean);
            }

        } else if (type == FirstItemType.CHOOSE && items.size() > 0) {
            if (items.get(0).value.equals(noLimit)) {
                items.get(0).value = pleaseSelect;
            } else if (!items.get(0).value.equals(pleaseSelect)) {
                DictionaryBean bean = new DictionaryBean(pleaseSelect, -1);
                items.add(0, bean);
            }
        }else if(type==FirstItemType.NONE && items.size()>0){
            if (items.get(0).value.equals(noLimit) || items.get(0).value.equals(pleaseSelect)) {
                items.remove(0);
            }
        }
        return items;
    }

    /**
     * 获取数据字典JSON.
     *
     * @return
     * @throws
     */
    private static JSONObject getDataDicJson() {
        if (dataJson != null && dataJson.get() != null) {
            return dataJson.get();
        }
        InputStream in = null;
        JSONObject json = null;
        try {
            // 从应用缓存文件夹下加载
            in = getContext().openFileInput(DATA_DIC_FILENAME);
//            in = ZhenaiApplication.getInstance().getResources().getAssets().open(DATA_DIC_FILENAME);
            json = parseInputStreamToJSONObject(in);
            dataJson = new SoftReference<>(json);
        } catch (Exception e) {
            // 从缓存加载失败则从'Assets'文件夹下加载
            try {
                in = getContext().getAssets().open(DATA_DIC_FILENAME);
                json = parseInputStreamToJSONObject(in);
                dataJson = new SoftReference<>(json);
            } catch (Exception e1) {
                DebugUtils.error(e1.getMessage(), e1);
            }
        } finally {
            StreamUtils.closeInputStream(in);
        }

        return json;
    }

    private static JSONObject parseInputStreamToJSONObject(InputStream in) throws Exception {
        String jsonString = StreamUtils.convertStreamToString(in);
        JSONObject json = null;
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                json = null;
            }
        }

        return json;
    }

    public enum FirstItemType {
        NO_LIMIT, CHOOSE, NORMAL,NONE
    }
}
