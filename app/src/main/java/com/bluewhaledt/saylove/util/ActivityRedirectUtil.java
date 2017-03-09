package com.bluewhaledt.saylove.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.ui.video.AVRecordActivity;
import com.bluewhaledt.saylove.ui.video.IJKAvPlayerActivity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转activity 处理
 * Created by rade.chan on 2016/12/22.
 */

public class ActivityRedirectUtil {

    /**
     * 去往播放页面
     *
     * @param context
     * @param entity
     */
    public static void gotoAvPlayActivity(final Context context, final VideoIndexEntity entity) {
        int status = WifiStatusController.getNetWork(context);        //检测是否有网络
        if (status == WifiStatusController.NETWORK_DISCONNECTION) {
            DialogUtil.showNetWorkDisableDialog(context);
        } else if (status == WifiStatusController.NETWORK_MOBILE_CONNECTION) {
            DialogUtil.showWifiTipsDialog(context, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, IJKAvPlayerActivity.class);
                    intent.putExtra(IntentConstants.VIDEO_INFO, entity);
                    context.startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(context, IJKAvPlayerActivity.class);
            intent.putExtra(IntentConstants.VIDEO_INFO, entity);
            context.startActivity(intent);
        }
    }


    /**
     * 去往播放页面
     *
     * @param context
     * @param videoId
     */
    public static void gotoAvPlayActivity(final Context context, final String videoId) {
        int status = WifiStatusController.getNetWork(context);        //检测是否有网络
        if (status == WifiStatusController.NETWORK_DISCONNECTION) {
            DialogUtil.showNetWorkDisableDialog(context);
        } else if (status == WifiStatusController.NETWORK_MOBILE_CONNECTION) {
            DialogUtil.showWifiTipsDialog(context, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, IJKAvPlayerActivity.class);
                    intent.putExtra(IntentConstants.VIDEO_ID, videoId);
                    context.startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(context, IJKAvPlayerActivity.class);
            intent.putExtra(IntentConstants.VIDEO_ID, videoId);
            context.startActivity(intent);
        }
    }


    /**
     * 去往录制页面
     * @param activity
     * @param permissionHelper
     */
    public static void gotoRecordActivity(final Activity activity, PermissionHelper permissionHelper) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (permissions.size() != 0) {
                permissionHelper.requestPermissions("请授予视频录制相应权限！", new PermissionHelper.PermissionListener() {
                            @Override
                            public void doAfterGrand(String... permission) {
                                Intent intent = new Intent(activity, AVRecordActivity.class);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.zhenai_library_slide_alpha_in_bottom, R.anim.zhenai_library_alpha_out);

                            }

                            @Override
                            public void doAfterDenied(String... permission) {

                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO);
            }else{
                Intent intent = new Intent(activity, AVRecordActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.zhenai_library_slide_alpha_in_bottom, R.anim.zhenai_library_alpha_out);
            }
        }else{
            if (CheckPermissionUtils.getInstance().CheckRecordPermission(activity) && CheckPermissionUtils.getInstance().checkCameraPermission()){
                Intent intent = new Intent(activity, AVRecordActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.zhenai_library_slide_alpha_in_bottom, R.anim.zhenai_library_alpha_out);
            }
        }
    }
}
