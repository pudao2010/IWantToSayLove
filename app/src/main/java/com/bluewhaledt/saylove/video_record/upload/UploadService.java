package com.bluewhaledt.saylove.video_record.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.ActivityManager;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.NetWorkErrorCode;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.CommonDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by rade.chan on 2016/12/15.
 */

public class UploadService extends Service {

    private final static int NOTIFICATION_ID = 100;         //通知id

    public static boolean isUpload = false;                 //全局变量，当前是否在上传

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private String filePath;                                    //文件路径

    private static final int SAVE_TRY_COUNT = 3;        //保存文件失败的最多尝试次数
    private int saveFailCount = 0;                      //保存失败次数
    private int maskId;                                 //遮罩id
    private View tipsView;                              //提示view
    private Handler mHandler;
    private String topicId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            filePath = intent.getStringExtra(IntentConstants.FILE_PATH);
            maskId = intent.getIntExtra(IntentConstants.VIDEO_MASK_ID, 1);
            topicId = intent.getStringExtra(IntentConstants.VIDEO_TOPIC_ID);
            start();
        } else {
            stopService();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 结束service
     */
    private void stopService() {
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }


    /**
     * 移除提示的view
     */
    private void removeTipsView() {
        if (tipsView != null) {
            tipsView.clearAnimation();
            WindowManager windowManager = (WindowManager) ZhenaiApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(tipsView);
            tipsView = null;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeTipsView();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (notificationManager != null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
        isUpload=false;
    }

    /**
     * 准备上传工作
     */
    private void prepareUpload(String filePath) {
        mHandler.removeCallbacksAndMessages(null);
        if (tipsView == null) {
            showTipsView();
        }
        isUpload = false;
        saveFailCount = 0;
        mHandler.postDelayed(new Runnable() {       //默认20s后设置通知栏为可取消，将上传标志为false 防止bug出现用户无法去除通知栏
            @Override
            public void run() {
                if (isMyServiceRunning(UploadService.class)) {
                    isUpload = false;
                    setNotificationCancelAble();
                }
            }
        }, 20 * 1000);
        initNotification();
        startUpload();


    }

    /**
     * 开始上传
     */
    private void startUpload() {
        AliUploadVideoUtil.getInstance().setListener(new AliUploadVideoUtil.UploadListener() {
            @Override
            public void uploadFail(String errorCode, String errorMsg) { //上传失败
                updateNotificationText(getString(R.string.upload_video_fail));
                isUpload = false;
                if (!TextUtils.isEmpty(errorCode) && errorCode.equals("RequestTimeTooSkewed")) {    //本地时间不正确
                    Toast(getString(R.string.upload_time_is_error));
                    stopService();
                } else if (!TextUtils.isEmpty(errorCode) && errorCode.equals(NetWorkErrorCode.VIDEO_UPLOAD_REACH_LIMIT)) {  //上传达到上限
                    showLimitDialog(errorMsg);
                } else {
                    showUploadFailDialog();
                }

            }

            @Override
            public void uploadProgress(float percent) { //上传进度监测
                updateNotificationText(getString(R.string.upload_percent) + percent + "%");
            }

            @Override
            public void uploadSuccess(String name) {     //上传成功，保存到服务器
                updateNotificationText(getString(R.string.upload_video_success));
                AliUploadVideoUtil.getInstance().saveVideoInfo(topicId,name, maskId);
                isUpload = false;
            }

            @Override
            public void saveSuccess() { //保存服务器成功
                Toast(getString(R.string.upload_video_success));
                EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VIDEO));
                stopService();
                isUpload = false;
            }

            @Override
            public void saveFail(final String fileName) {   //保存失败
                isUpload = false;
                saveFailCount++;
                if (saveFailCount <= SAVE_TRY_COUNT) {
                    mHandler.postDelayed(new Runnable() {    //此处保存失败,延迟一秒重试
                        @Override
                        public void run() {
                            AliUploadVideoUtil.getInstance().saveVideoInfo(topicId,fileName, maskId);
                        }
                    }, 1000);
                } else {
                    showUploadFailDialog();
                }

            }
        });
        AliUploadVideoUtil.getInstance().uploadVideo(filePath);
        isUpload = true;
    }


    /**
     * 初始化前台通知
     */
    private void initNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.prepare_upload_video))
                .setAutoCancel(false)
                .setOngoing(true);
        Notification notification = notificationBuilder.build();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        //启动为前台服务
        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * 动态更改通知栏的文字
     *
     * @param text
     */
    private void updateNotificationText(String text) {
        if (notificationManager != null) {
            notificationBuilder.setContentTitle(text);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }

    }


    /**
     * 设置通知栏为可取消
     */
    private void setNotificationCancelAble() {
        if (notificationManager != null) {
            notificationBuilder.setAutoCancel(true)
                    .setOngoing(false);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }

    }

    /**
     * 检测当前的servic 是否在运行
     *
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        android.app.ActivityManager manager = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 开始
     */
    private void start() {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                prepareUpload(filePath);
            } else {
                stopService();
            }
        } else {
            stopService();
        }
    }


    /**
     * 上传达到最大限制  显示dialog
     *
     * @param msg
     */
    private void showLimitDialog(final String msg) {
        final BaseActivity currentActivity = ActivityManager.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new CommonDialog(currentActivity).hideCancelBtn().setContent(msg).setListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            stopService();
                        }
                    }).show();
                }
            });

        } else {
            Toast(msg);
            stopService();
        }
    }

    /**
     * 显示上传失败对话框
     */
    private void showUploadFailDialog() {
        final BaseActivity currentActivity = ActivityManager.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE, ResourceKey.VideoRecordPage.VIDEO_RECORD_RE_UPLOAD_DIALOG);
                    new BaseDialog(currentActivity)
                            .setCustomerContent(R.layout.dialog_upload_fail_conten_layout)
                            .setBtnPanelView(R.layout.dialog_upload_fail_btn_layout, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    switch (id) {
                                        case R.id.re_upload_btn:
                                            start();
                                            break;
                                        case R.id.cancel_btn:
                                            stopService();
                                            break;
                                    }
                                    dialogInterface.dismiss();
                                }
                            }).showCloseBtn(false).show();
                }
            });

        } else {
            Toast(getString(R.string.upload_video_fail));
            stopService();
        }


    }

    /**
     * toast
     *
     * @param msg
     */
    private void Toast(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.toast(getApplicationContext(), msg);
            }
        });
    }


    /**
     * 显示顶部tips提示框
     */
    public void showTipsView() {
        try {
            WindowManager windowManager = (WindowManager) ZhenaiApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();
            windowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            windowManagerParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // 不设置这个弹出框的透明遮罩显示为黑色
            windowManagerParams.format = PixelFormat.TRANSLUCENT;
            // 调整悬浮窗口至左上角，便于调整坐标
            windowManagerParams.gravity = Gravity.TOP | Gravity.LEFT;

            // 以屏幕左上角为原点，设置x、y初始值
            windowManagerParams.x = 0;
            windowManagerParams.y = 0;
            // 设置悬浮窗口长宽数据
            int height = ZhenaiApplication.getContext().getResources().getDimensionPixelSize(R.dimen.upload_service_view_height);
            windowManagerParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            windowManagerParams.height = height;
            tipsView = LayoutInflater.from(ZhenaiApplication.getContext()).inflate(R.layout.item_upload_dialog_tips_layout, null);
            windowManager.addView(tipsView, windowManagerParams);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeTipsView();
                }
            }, 3000);

        } catch (Exception e) {
            Toast(getString(R.string.upload_video_tips));
            tipsView = null;
        }


    }


}
