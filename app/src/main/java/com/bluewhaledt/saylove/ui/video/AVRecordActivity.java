package com.bluewhaledt.saylove.ui.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.AudioOpenFailEvent;
import com.bluewhaledt.saylove.event.CameraOpenFailEvent;
import com.bluewhaledt.saylove.ui.video.entity.VideoTopicEntity;
import com.bluewhaledt.saylove.ui.video.presenter.VideoRecordPresenter;
import com.bluewhaledt.saylove.ui.video.view.IVideoRecordView;
import com.bluewhaledt.saylove.ui.video.widget.TouchSurfaceView;
import com.bluewhaledt.saylove.ui.video.widget.VideoRecordView;
import com.bluewhaledt.saylove.ui.video.widget.VideoTopicLayout;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.bluewhaledt.saylove.util.VideoPlayUtils;
import com.bluewhaledt.saylove.util.WifiStatusController;
import com.bluewhaledt.saylove.video_record.filter.FilterManager;
import com.bluewhaledt.saylove.video_record.helper.FileHelper;
import com.bluewhaledt.saylove.video_record.recoder.BaseAvRecorder;
import com.bluewhaledt.saylove.video_record.recoder.FilterAvRecorder;
import com.bluewhaledt.saylove.video_record.recoder.SystemAvRecorder;
import com.bluewhaledt.saylove.video_record.show_view.CameraSurfaceView;
import com.bluewhaledt.saylove.video_record.upload.UploadService;
import com.bluewhaledt.saylove.video_record.video.TextureMovieEncoder;
import com.bluewhaledt.saylove.widget.CommonDialog;
import com.bluewhaledt.saylove.widget.horizontal_wheelview.AbstractWheel;
import com.bluewhaledt.saylove.widget.horizontal_wheelview.WheelHorizontalView;
import com.bluewhaledt.saylove.widget.horizontal_wheelview.adapters.ArrayWheelAdapter;
import com.bluewhaledt.saylove.widget.horizontal_wheelview.listener.OnWheelChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频录制页
 * Created by rade.chan on 2016/12/8.
 */

public class AVRecordActivity extends BaseActivity implements View.OnClickListener, IVideoRecordView, Handler.Callback {

    private WheelHorizontalView mFilterWheelView;                       //滤镜
    private ArrayWheelAdapter<String> adapter;                          // 滤镜 adapter
    private ImageView mCloseView;                                         //关闭按钮
    private CameraSurfaceView mFilterSurfaceView;                       //滤镜模式下的surfaceView
    private SurfaceView mSystemSurfaceView;                             //非滤镜模式下的surfaceView  4.3以下手机
    private VideoRecordView mVideoRecordView;                           //录制的按钮


    private SurfaceView mPlaySurfaceView;                               //播放的surfaceView
    private VideoPlayUtils videoPlayUtils;                              //播放工具类


    private VideoTopicLayout mVideoTopicLayout;                         //话题 layout
    private List<VideoTopicEntity> topicList = new ArrayList<>();        //话题list
    private int currentTopic = 0;                                       //当前话题位置
    private BaseAvRecorder avRecorder;                               // 音视频录制类

    private VideoRecordPresenter presenter;

    private TextView maskOperationView;                                //遮罩操作按钮
    private View maskView;                                               //遮罩

    private static final int HIDE_FILTER_VIEW = 1;                     //隐藏遮罩

    private static final int SHOW_TIME_DURATION = 3000;                //遮罩显示的时间
    private Handler mainHandler;

    private static final int NORMAL = 0;                                 //未录制的状态
    private static final int RECORDING = 1;                             //录制中的状态
    private static final int RECORD_FINISH = 2;                         //录制完成状态
    private int currentStatus = NORMAL;                                 //当前状态
    private View mGuideView;                                            //引导view

    private boolean isCameraOpenFail = false;
    private boolean isAudioOpenFail = false;


    private int retryTime = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextureMovieEncoder.initialize(getApplicationContext());
        showTitleBar(false);
        setContentView(R.layout.activity_av_record_layout);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
        initFilter();
        initListener();
        EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VIDEO_RECORD_PAGE);

    }

    /**
     * 判断是否使用系统录制
     *
     * @return
     */
    private boolean isSystemVersion() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2;  //大于等于4.3的版本才可用滤镜
    }


    private void initData() {
        if (isSystemVersion()) {
            avRecorder = new SystemAvRecorder(mSystemSurfaceView);
        } else {
            avRecorder = new FilterAvRecorder(mFilterSurfaceView);
        }
        if (Build.VERSION.SDK_INT < 23) {
            avRecorder.startRequestAudio();
        }
        presenter = new VideoRecordPresenter(this, this);
        presenter.getRandomTopic();
        startGuideAnim();
        mainHandler = new Handler(this);
        mainHandler.sendEmptyMessageDelayed(HIDE_FILTER_VIEW, SHOW_TIME_DURATION);
    }


    /**
     * 设置当前的状态
     *
     * @param currentStatus
     */
    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        if (currentStatus == NORMAL) {
            mVideoTopicLayout.setArrowIconVisible(View.VISIBLE);
            setFilterVisible(true);
            mainHandler.sendEmptyMessageDelayed(HIDE_FILTER_VIEW, SHOW_TIME_DURATION);
            maskOperationView.setVisibility(View.VISIBLE);
            mVideoRecordView.reset();
            if (mFilterSurfaceView != null) {
                mFilterSurfaceView.setVisibility(View.VISIBLE);
                mFilterSurfaceView.onResume();
            }
            if (mSystemSurfaceView != null) {
                mSystemSurfaceView.setVisibility(View.VISIBLE);
            }
            mPlaySurfaceView.setVisibility(View.GONE);
            if (videoPlayUtils != null) {
                videoPlayUtils.release();
            }
            mCloseView.setVisibility(View.VISIBLE);
            mCloseView.setImageResource(R.drawable.icon_video_close_selector);
        } else if (currentStatus == RECORDING || currentStatus == RECORD_FINISH) {
            mGuideView.clearAnimation();
            mGuideView.setVisibility(View.GONE);
            mVideoTopicLayout.setArrowIconVisible(View.INVISIBLE);
            setFilterVisible(false);
            maskOperationView.setVisibility(View.GONE);
            mCloseView.setVisibility(View.GONE);


            if (currentStatus == RECORD_FINISH) {
                mCloseView.setVisibility(View.VISIBLE);
                mCloseView.setImageResource(R.drawable.icon_back_white_selector);
                if (mFilterSurfaceView != null) {
                    mFilterSurfaceView.onPause();
                    mFilterSurfaceView.setVisibility(View.GONE);
                }
                if (mSystemSurfaceView != null) {
                    mSystemSurfaceView.setVisibility(View.GONE);
                }
                mPlaySurfaceView.setVisibility(View.VISIBLE);
            }
        }
    }



    private void initView() {
        mFilterWheelView = (WheelHorizontalView) findViewById(R.id.horizontal_view);
        mCloseView = (ImageView) findViewById(R.id.close_view);
        mVideoRecordView = (VideoRecordView) findViewById(R.id.video_record_btn);
        if (isSystemVersion()) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.system_surface_view_stub);
            viewStub.inflate();
            setFilterVisible(false);
            mSystemSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        } else {
            ViewStub viewStub = (ViewStub) findViewById(R.id.filter_surface_view_stub);
            viewStub.inflate();
            setFilterVisible(true);
            mFilterSurfaceView = (CameraSurfaceView) findViewById(R.id.camera_surface_view);
        }
        mPlaySurfaceView = (SurfaceView) findViewById(R.id.play_surface_view);
        mVideoTopicLayout = (VideoTopicLayout) findViewById(R.id.video_question_layout);
        mGuideView = findViewById(R.id.guide_view);
        maskView = findViewById(R.id.mask_layout);
        maskOperationView = (TextView) findViewById(R.id.mask_operation_view);
    }


    @Override
    public void dismissDialog() {
        dismissProgress();
    }




    /**
     * 滤镜引导动画
     */
    private void startGuideAnim() {
        boolean isFirstTime = PreferenceUtil.getBoolean(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.VIDEO_IS_FIRST_GUIDE, true);
        if (!isSystemVersion() && isFirstTime) {
            int translateX = ZhenaiApplication.getInstance().getScreenWidth() * 2 / 3;
            TranslateAnimation translateAnimation = new TranslateAnimation(0, -translateX, 0, 0);
            translateAnimation.setDuration(1500);
            translateAnimation.setRepeatCount(1);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mGuideView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mGuideView.startAnimation(translateAnimation);
            PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.VIDEO_IS_FIRST_GUIDE, false);
        } else {
            mGuideView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFilterSurfaceView != null) {
            mFilterSurfaceView.onResume();
        }

        if (currentStatus == RECORD_FINISH && videoPlayUtils != null) {
            videoPlayUtils.start();
        }

    }

    private void giveUpRecord() {
        if (currentStatus == RECORDING && avRecorder != null) {
            avRecorder.giveUpRecord();
            setCurrentStatus(NORMAL);
        }
    }

    @Override
    protected void onPause() {
        if (mFilterSurfaceView != null) {
            mFilterSurfaceView.onPause();
        }
        if (currentStatus == RECORD_FINISH && videoPlayUtils != null) {
            videoPlayUtils.pause();
        }
        giveUpRecord();
        super.onPause();
    }

    /**
     * 初始化滤镜
     */
    private void initFilter() {
        adapter = new ArrayWheelAdapter<>(this, getResources().getStringArray(R.array.video_filter));
        adapter.setItemResource(R.layout.item_video_filter_wheel_center_layout);
        adapter.setItemTextResource(R.id.center_text);
        adapter.setCurrentWheelView(mFilterWheelView);
        adapter.setTextColor(getResources().getColor(R.color.white));
        adapter.setUnSelectColor(getResources().getColor(R.color.white));
        mFilterWheelView.setViewAdapter(adapter);
        mFilterWheelView.setCurrentItem(0);
    }


    private void initListener() {
        mCloseView.setOnClickListener(this);
        mVideoRecordView.setOnClickListener(this);
        mFilterWheelView.setEnabled(false); //设为不可滑动
        mFilterWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {

                mFilterSurfaceView.changeFilter(FilterManager.FilterType.typeOfValue(newValue));


            }
        });
        mVideoRecordView.setRecordListener(new VideoRecordView.OnRecordListener() {
            @Override
            public void onRecordFinish() {             //录制完成监听
                if (currentStatus == RECORDING) {
                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_END_NUM);
                    avRecorder.stopRecord(true);
                }

            }

            @Override
            public void onRecordNotEnough() {           //录制不足监听
                ToastUtils.toast(AVRecordActivity.this, getString(R.string.record_is_too_short));
//                avRecorder.giveUpRecord();     重启过快会挂掉
//                avRecorder.startRecord();
            }
        });
        mVideoTopicLayout.addListener(new VideoTopicLayout.OnQuestionLayoutListener() {
            @Override
            public void onNextButtonClick() {           //下一话题按钮
                if (topicList.size() > 1) {
                    currentTopic++;
                    if (currentTopic >= topicList.size()) {
                        currentTopic = 0;
                    }
                    mVideoTopicLayout.addText(topicList.get(currentTopic));
                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_SWITCH_TOPIC_BTN);
                }
            }
        });
        avRecorder.setMediaStatusListener(new BaseAvRecorder.OnMediaStatusListener() {
            @Override
            public void onMediaFileCreate(String filePath) {     //录制完成且文件生成会回调此方法
                setCurrentStatus(RECORD_FINISH);
                videoPlayUtils = new VideoPlayUtils(mPlaySurfaceView, FileHelper.getRecordVideoPath(), true);
                videoPlayUtils.setLooping(true);
                videoPlayUtils.play();
            }
        });
        maskOperationView.setOnClickListener(this);
        if (mFilterSurfaceView != null) {
            mFilterSurfaceView.setOnSlideListener(new TouchSurfaceView.OnSlideListener() {
                @Override
                public void onSlide(int orientation) {          //滑动滤镜
                    if (currentStatus == NORMAL) {
                        int count = adapter.getItemsCount();
                        int currentItem = mFilterWheelView.getCurrentItem();
                        mainHandler.removeCallbacksAndMessages(null);
                        setFilterVisible(true);
                        EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_SWITCH_FILTER_NUM);
                        if (orientation == TouchSurfaceView.ORIENTATION_RIGHT) {
                            currentItem--;
                            if (currentItem < 0) {
                                currentItem = count - 1;
                            }
                            mFilterWheelView.setCurrentItem(currentItem,true);
                        } else if (orientation == TouchSurfaceView.ORIENTATION_LEFT) {
                            currentItem++;
                            if (currentItem >= count) {
                                currentItem = 0;
                            }
                            mFilterWheelView.setCurrentItem(currentItem,true);
                        }
                        mainHandler.sendEmptyMessageDelayed(HIDE_FILTER_VIEW, SHOW_TIME_DURATION);
                    }
                }
            });
        }
    }

    @Subscribe
    public void onEvent(CameraOpenFailEvent event) {
        if (event != null) {
            isCameraOpenFail = event.isOpenFail();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AudioOpenFailEvent event) {
        if (event != null) {
            isAudioOpenFail = event.isOpenFail();
            if (currentStatus == NORMAL) {
                avRecorder.stopRequestAudio();
            } else if (currentStatus == RECORDING && isAudioOpenFail) {
                giveUpRecord();
                showSingleBtnDialog(getString(R.string.please_open_audio));

            }
        }
    }

    private void showSingleBtnDialog(String text) {
        new CommonDialog(AVRecordActivity.this).setContent(text).hideCancelBtn().
                setListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        avRecorder.releaseResource();
        if (mFilterSurfaceView != null) {
            mFilterSurfaceView.onDestroy();
        }
        adapter.setCurrentWheelView(null);
        if (videoPlayUtils != null) {
            videoPlayUtils.release();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_view:
                if (currentStatus == RECORDING || currentStatus == RECORD_FINISH) {       //当前状态为录制或者录制完成，则提示是否放弃
                    showReRecordDialog();
                } else {
                    finish();
                }
                break;
            case R.id.video_record_btn:

                if (currentStatus == RECORD_FINISH) {
                    int status = WifiStatusController.getNetWork(AVRecordActivity.this);
                    if (status == WifiStatusController.NETWORK_DISCONNECTION) {         //网络不可用
                        DialogUtil.showNetWorkDisableDialog(AVRecordActivity.this);
                    } else if (status == WifiStatusController.NETWORK_MOBILE_CONNECTION) {
                        DialogUtil.showWifiTipsDialog(AVRecordActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startUpload();
                            }
                        });
                    } else {
                        startUpload();
                    }

                } else if (currentStatus == NORMAL) {       //开始录制视频
                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_START_NUM);
                    if (!UploadService.isUpload) {          //判断是否在上传  在上传则不可录制
                        if (!isCameraOpenFail && !isAudioOpenFail) {         //检测相机和录音是否正常
                            FileHelper.deleteAllFile();         //删除之前的文件
                            setCurrentStatus(RECORDING);
                            avRecorder.startRecord();           //开始录制
                            mVideoRecordView.clickRecord();     //展示动画
                        } else {
                            if (isCameraOpenFail) {
                                showSingleBtnDialog(getString(R.string.camera_open_fail));
                            } else if (isAudioOpenFail) {
                                showSingleBtnDialog(getString(R.string.please_open_audio));
                            }
                        }
                    } else {
                        showSingleBtnDialog(getString(R.string.video_is_upload));
                    }
                } else if (currentStatus == RECORDING) {
                    mVideoRecordView.clickRecord();
                }
                break;
            case R.id.mask_operation_view:
                if (maskView.getVisibility() == View.VISIBLE) {
                    maskView.setVisibility(View.GONE);
                    maskOperationView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_video_mask_close_selector, 0, 0);
                    maskOperationView.setText(getString(R.string.open_mask));
                } else {
                    maskView.setVisibility(View.VISIBLE);
                    maskOperationView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_video_mask_open_selector, 0, 0);
                    maskOperationView.setText(getString(R.string.close_mask));
                }
                break;

        }
    }

    /**
     * 上传视频
     */
    private void startUpload() {
        File file = FileHelper.getRecordFile();
        if (file.length() > 0) {
            Intent uploadService = new Intent(this, UploadService.class);
            uploadService.putExtra(IntentConstants.FILE_PATH, FileHelper.getRecordVideoPath());
            uploadService.putExtra(IntentConstants.VIDEO_MASK_ID, maskView.getVisibility() == View.VISIBLE ? 2 : 1);

            Intent intent = new Intent(AVRecordActivity.this, AVPublishActivity.class);
            if (mVideoTopicLayout.getCurrentTopicEntity() != null) {
                uploadService.putExtra(IntentConstants.VIDEO_TOPIC_ID, mVideoTopicLayout.getCurrentTopicEntity().topicId + "");
                intent.putExtra(IntentConstants.VIDEO_TOPIC_ID, mVideoTopicLayout.getCurrentTopicEntity().topicId + "");
                intent.putExtra(IntentConstants.VIDEO_TOPIC, mVideoTopicLayout.getCurrentTopicEntity().content);
            }
            startService(uploadService);
            setCurrentStatus(NORMAL);
            startActivity(intent);
            super.finish();
        } else {
            showSingleBtnDialog(getString(R.string.video_is_error));
        }
    }

    /**
     * 获取话题成功
     *
     * @param list
     */
    @Override
    public void getTopicSuccess(List<VideoTopicEntity> list) {
        topicList.clear();
        topicList.addAll(list);
        if (topicList.size() > 0) {
            mVideoTopicLayout.setVisibility(View.VISIBLE);
            mVideoTopicLayout.addText(list.get(0));
        }
    }

    @Override
    public void onBackPressed() {
        if (currentStatus == RECORDING||currentStatus == RECORD_FINISH) {
            showReRecordDialog();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zhenai_library_slide_out_bottom);
    }

    /**
     * 是否重录 或放弃录制的对话框
     */
    private void showReRecordDialog() {
        new BaseDialog(this)
                .setBtnPanelView(R.layout.dialog_video_record_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        dialogInterface.dismiss();
                        switch (btnId) {
                            case R.id.re_record_view:       //重新录制
                                if (currentStatus == RECORDING) {
                                    giveUpRecord();
                                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_GIVE_UP_RECORD);
                                } else {
                                    setCurrentStatus(NORMAL);
                                    EventStatistics.recordLog(ResourceKey.VIDEO_RECORD_PAGE,ResourceKey.VideoRecordPage.VIDEO_RECORD_RE_RECORD);
                                }
                                break;
                            case R.id.give_up_record:
                                finish();
                                break;
                        }
                    }
                }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case HIDE_FILTER_VIEW:
                setFilterVisible(false);
                break;
        }
        return false;
    }


    private void setFilterVisible(boolean isVisible) {
        if (isVisible) {
            if (isSystemVersion()) {
                mFilterWheelView.setVisibility(View.INVISIBLE);
            } else {
                mFilterWheelView.setVisibility(View.VISIBLE);
            }
        } else {
            mFilterWheelView.setVisibility(View.INVISIBLE);
        }
    }


}
