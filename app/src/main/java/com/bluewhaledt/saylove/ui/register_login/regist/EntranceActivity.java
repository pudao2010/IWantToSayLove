package com.bluewhaledt.saylove.ui.register_login.regist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseActivity;
import com.bluewhaledt.saylove.ui.register_login.login.LoginActivity;
import com.bluewhaledt.saylove.ui.tourist.TouristSexActivity;
import com.bluewhaledt.saylove.util.BDLocationController;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.LocationEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
     * @项目名： SayLove
     * @包名： com.zhenai.saylove_icon.ui.register_login.login
     * @文件名: EntranceActivity
     * @创建者: YanChao
     * @创建时间: 2016/11/28 17:44
     * @描述：  登录注册入口
     */
    public  class EntranceActivity extends RegistAndLoginBaseActivity implements View.OnClickListener {

        private TextView mTvVisitor;
        private Button mBtnLogin;
        private Button mBtnRegist;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            showTitleBar(false);
            setContentView(R.layout.activity_entrance_layout);
            assignViews();
            initListener();
            initData();
            EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.REGISTER_AND_LOGIN_PAGE);
        }

        private void initData() {
            mPermissionHelper.requestPermissions("定位权限没有打开",new PermissionHelper.PermissionListener(){

                @Override
                public void doAfterGrand(String... permission) {
                    DebugUtils.d("yan", "startLocation in EntranceActivity");
                    BDLocationController.getInstance().startLocation();
                }

                @Override
                public void doAfterDenied(String... permission) {

                }
            }, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION);

        }

        private void initListener() {
                mBtnRegist.setOnClickListener(this);
                mBtnLogin.setOnClickListener(this);
                mTvVisitor.setOnClickListener(this);
            }


        private void assignViews() {
            mTvVisitor = (TextView) findViewById(R.id.tv_visitor);
            mBtnLogin = (Button) findViewById(R.id.btn_login);
            mBtnRegist = (Button) findViewById(R.id.btn_regist);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BDLocationController.getInstance().stopLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread (LocationEvent event){
        if (event != null ) {
            BDLocation bdLocation = event.getBdLocation();
            ZhenaiApplication.mBDLocation = bdLocation;
        }
    }

    @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.btn_regist:
                    EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.REGISTER_BTN);
                    Intent intent = new Intent(this, BaseInfoEditActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_login:
                    EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.LOGIN_BTN);
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case R.id.tv_visitor:
                    EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.VISITOR_BTN);
                    startActivity(new Intent(this, TouristSexActivity.class));
                    break;
            }
        }
    }