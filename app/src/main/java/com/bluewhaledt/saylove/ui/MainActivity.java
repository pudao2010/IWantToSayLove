package com.bluewhaledt.saylove.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.baidu.location.BDLocation;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.entity.NotificationData;
import com.bluewhaledt.saylove.ui.heartbeat.HeartBeatFragment;
import com.bluewhaledt.saylove.ui.info.base.ISourceFrom;
import com.bluewhaledt.saylove.ui.info.info_modify.ModifyVerifyFragment;
import com.bluewhaledt.saylove.ui.register_login.RegistCloseEvent;
import com.bluewhaledt.saylove.ui.register_login.widget.City;
import com.bluewhaledt.saylove.ui.register_login.widget.Province;
import com.bluewhaledt.saylove.ui.tab.TabsFragment;
import com.bluewhaledt.saylove.util.BDLocationController;
import com.bluewhaledt.saylove.util.DictionaryUtil;
import com.bluewhaledt.saylove.util.LocationEvent;
import com.netease.nimlib.sdk.NimIntent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    private long clickTime = 0L;
    private TabsFragment tabsFragment;

    private Handler handler = new Handler();
    private long delay = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        tabsFragment = new TabsFragment();
        setContentView(tabsFragment);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new RegistCloseEvent(true));
        if (ZhenaiApplication.mCityCode == 0) {
            Observable.timer(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {

                        @Override
                        public void call(Long aLong) {
                            mPermissionHelper.requestPermissions("定位权限没有打开",new PermissionHelper.PermissionListener(){
                                @Override
                                public void doAfterGrand(String... permission) {
                                    DebugUtils.d("yan", "startLocation in MainActivity");
                                    BDLocationController.getInstance().startLocation();
                                }
                                @Override
                                public void doAfterDenied(String... permission) {

                                }
                            }, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION);
                        }

                    });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NimIntent.ACTION_RECEIVE_MSG.equals(action)) {//点击消息通知过来
            if (intent.getSerializableExtra(IMReceiver.DATA) != null){
                final NotificationData data = (NotificationData) intent.getSerializableExtra(IMReceiver.DATA);
                switch (data.type){
                    case 0:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabsFragment.mViewPager.setCurrentItem(2);//跳转到消息页
                            }
                        },delay);
                        break;
                }
            }

        } else if (NimIntent.EXTRA_BROADCAST_MSG.equals(action)) {//点击其他系统通知进来
            if (intent.getSerializableExtra(IMReceiver.DATA) != null){
                final NotificationData data = (NotificationData) intent.getSerializableExtra(IMReceiver.DATA);
                switch (data.type){
                    case 1:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabsFragment.startFragment(HeartBeatFragment.class,null);
                            }
                        },delay);
                        break;
                    case 2:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle educationVerify = new Bundle();
                                educationVerify.putInt(IntentConstants.MODIFY_FROM, ISourceFrom.FROM_EDUCATION_VERIFY);
                                tabsFragment.startFragment(ModifyVerifyFragment.class, educationVerify);
                            }
                        },delay);
                        break;
                    case 3:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle houseVerify = new Bundle();
                                houseVerify.putInt(IntentConstants.MODIFY_FROM, ISourceFrom.FROM_HOUSE_VERIFY);
                                tabsFragment.startFragment(ModifyVerifyFragment.class, houseVerify);
                            }
                        },delay);

                        break;
                    case 4:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle carVerify = new Bundle();
                                carVerify.putInt(IntentConstants.MODIFY_FROM, ISourceFrom.FROM_CAR_VERIFY);
                                tabsFragment.startFragment(ModifyVerifyFragment.class, carVerify);
                            }
                        },delay);

                        break;
                    case 5:
                    case 6:
                    case 7:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabsFragment.mViewPager.setCurrentItem(3);//跳转到我的页面
                            }
                        },delay);
                        break;
                }
            }
        }
        EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_ALL_INFO));

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                return super.onKeyDown(keyCode, event);
            } else {
                if (System.currentTimeMillis() - clickTime > 2000) {
                    ToastUtils.toast(this, getResources().getString(R.string.click_twice_to_exit));
                    clickTime = System.currentTimeMillis();
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread (LocationEvent event){
        if (event != null ) {
            BDLocation bdLocation = event.getBdLocation();
            ZhenaiApplication.mBDLocation = bdLocation;
            int mLocationCode = 0;
            if (bdLocation != null) {
                DebugUtils.d("yan", "location != null");
                String provinceStr = bdLocation.getProvince();
                String cityOrDistrictStr = bdLocation.getCity();
                String districtStr = bdLocation.getDistrict();

                Province provinceMatches = DictionaryUtil.getProvinceMatches(provinceStr);
                Province cityMatchesProvince = DictionaryUtil.getProvinceMatches(cityOrDistrictStr);
                DebugUtils.d("yanchao", provinceStr + cityOrDistrictStr + districtStr);
                City city = DictionaryUtil.getCityMatches(provinceMatches, cityOrDistrictStr);
                if (cityMatchesProvince != null) {
                    List<City> cities = cityMatchesProvince.cities;
                    City districtMatchescity = DictionaryUtil.getCityMatches(cityMatchesProvince, districtStr);
                    if (districtMatchescity != null && cities != null){
                        mLocationCode = districtMatchescity.key;
                    }else{
                        mLocationCode = cities.get(0).key;
                    }
                }else if (provinceMatches!= null){
                    List<City> cities1 = provinceMatches.cities;
                    if (city != null && cities1!= null) {
                        mLocationCode = city.key;
                    } else {
                        mLocationCode = cities1.get(0).key;
                    }
                }

            }
            ZhenaiApplication.mCityCode = mLocationCode;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BDLocationController.getInstance().stopLocation();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
