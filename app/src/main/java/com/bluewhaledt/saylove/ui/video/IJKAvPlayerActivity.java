package com.bluewhaledt.saylove.ui.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.NetWorkErrorCode;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.event.VideoIndexNotifyEvent;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.info.constant.ComeFrom;
import com.bluewhaledt.saylove.ui.message.ChatDetailActivity;
import com.bluewhaledt.saylove.ui.message.ChatDetailFragment;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.presenter.VideoPlayPresenter;
import com.bluewhaledt.saylove.ui.video.view.IVideoPlayView;
import com.bluewhaledt.saylove.ui.video.widget.VideoTopicLayout;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.CommonDialog;

import org.greenrobot.eventbus.EventBus;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频播放类
 * Created by rade.chan on 2016/12/15.
 */

public class IJKAvPlayerActivity extends BaseActivity implements View.OnClickListener, IVideoPlayView {

    private IjkMediaPlayer ijkMediaPlayer;                                  //B站软解码播放器
    private SurfaceView mSurfaceView;                                       //视频渲染surfaceview
    private ImageView closeView;                                            //关闭按钮
    private ImageView reportOrDeleteView;                                   //举报或删除按钮
    private ImageView heartView;                                            //心动按钮
    private ImageView chatView;                                              //聊天按钮
    private ImageView userDetailView;                                       //用户资料按钮
    private TextView nickNameTv;                                            //昵称 view
    private TextView verifyTv;                                              //认证信息 view
    private ImageView avatarView;                                           //头像
    private VideoTopicLayout avTopicLayout;                                 //话题layout
    private View bottomButtonLayout;                                        //底部心动、聊天、三资页按钮layout

    private VideoPlayPresenter mPresenter;
    private FrameLayout containerLayout;                                    //包裹视频container layout
    private ImageView coverImgView;                                         //封面imgview
    private View mMaskView;                                                 //遮罩
    private TextView mHeartCountTv;                                         //心动数目 view


    private VideoIndexEntity entity;                                        //视频信息
    private String videoId;                                                 //视频id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_av_play_layout);
        showTitleBar(false);
        initView();
        initListener();
        initVideo();
        mPresenter = new VideoPlayPresenter(this, this);
        init(getIntent());
        EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VIDEO_PLAY_PAGE);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        containerLayout.removeAllViews();
        mSurfaceView = null;            //singletask 调用，此处置空，否则会重新回调holder 的 callback
        release();
        init(intent);
    }


    /**
     * 初始化
     *
     * @param intent
     */
    private void init(Intent intent) {
        entity = (VideoIndexEntity) intent.getSerializableExtra(IntentConstants.VIDEO_INFO);
        videoId = intent.getStringExtra(IntentConstants.VIDEO_ID);

        if (entity == null && videoId == null) {        //视频不存在
            ToastUtils.toast(this, getString(R.string.video_is_not_exist));
            finish();
            return;
        }

        if (entity != null) {
            if (entity.state == 4) {        //视频被删除了
                ToastUtils.toast(this, entity.message);
                finish();
                return;
            }
            initSurface();
            initViewData();
            mPresenter.addViewNum(entity.videoId);
        } else {                     //视频信息为空，则根据视频id 请求视频信息
            mPresenter.getVideoInfo(videoId);
            mPresenter.addViewNum(videoId);
        }

    }


    /**
     * 初始化视频库
     */
    private void initVideo() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    /**
     * 成功获取视频信息
     *
     * @param indexEntity
     */
    @Override
    public void getVideoInfoSuccess(VideoIndexEntity indexEntity) {
        entity = indexEntity;
        if (entity.state == 4) {
            ToastUtils.toast(this, entity.message);
            finish();
            return;
        }
        initSurface();
        initViewData();
        initListener();
    }

    /**
     * 获取视频信息失败
     *
     * @param msg
     */
    @Override
    public void getVideoFail(String msg) {
        ToastUtils.toast(this, msg);
        finish();
    }


    /**
     * 检测是否是自己的账号
     *
     * @return
     */
    private boolean isMe() {
        ZAAccount account = AccountManager.getInstance().getZaAccount();
        if (entity != null && account != null) {
            return entity.userId == account.uid;
        }
        return false;
    }

    private void initViewData() {

        if (isMe()) {   //根据角色显示删除按钮或举报按钮
            reportOrDeleteView.setImageResource(R.mipmap.icon_viedo_rubbish);
            bottomButtonLayout.setVisibility(View.INVISIBLE);
        } else {
            reportOrDeleteView.setImageResource(R.drawable.icon_video_report_selector);
            bottomButtonLayout.setVisibility(View.VISIBLE);
        }

        int defaultGender = R.mipmap.default_avatar_feman;
        if (entity.userInfo.sex == 1) {
            defaultGender = R.mipmap.default_avatar_man;
        }
        ImageLoaderFactory.getImageLoader().with(this).load(entity.userInfo.avatar).circle().placeholder(defaultGender).
                into(avatarView);

        if (entity.likeId > -1) {       //检测是否赞过
            setIsHeart(true);
        }

        if (!TextUtils.isEmpty(entity.topicContent)) {
            avTopicLayout.setVisibility(View.VISIBLE);
            avTopicLayout.addText(entity.topicContent);
        }
        String nickName = entity.userInfo.nickName;
        if (TextUtils.isEmpty(nickName)) {
            entity.userInfo.nickName = getString(R.string.member) + entity.userId;
        }
        if (entity.userInfo.isVIP) {
            nickNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_me_crown, 0);
        } else {
            nickNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        nickNameTv.setText(entity.userInfo.nickName);

        if (entity.shield == 2) {   //是否开启遮罩
            mMaskView.setVisibility(View.VISIBLE);
        }

        mHeartCountTv.setText(String.valueOf(entity.likeNum));
        initVerifyData();

    }

    /**
     * 初始化认证信息
     */
    private void initVerifyData() {
        StringBuilder builder = new StringBuilder();
        if (entity.userInfo.isZM) {
            builder.append(getString(R.string.verify_id_card));
        }
        if (entity.userInfo.isHouse) {
            if (builder.toString().length() > 0) {
                builder.append("、");
            }
            builder.append(getString(R.string.verify_house));
        }
        if (entity.userInfo.isCar) {
            if (builder.toString().length() > 0) {
                builder.append("、");
            }
            builder.append(getString(R.string.verify_car));
        }

        if (entity.userInfo.isDegree) {
            if (builder.toString().length() > 0) {
                builder.append("、");
            }
            builder.append(getString(R.string.verify_education));
        }

        if (builder.toString().length() > 0) {
            String text = builder.toString();
            builder.setLength(0);
            builder.append("已认证").append(text);
        } else {
            builder.append("未认证");
        }
        ImageLoaderFactory
                .getImageLoader()
                .with(this)
                .load(entity.cover)
                .placeholder(new ColorDrawable(Color.TRANSPARENT))
                .into(coverImgView);
        verifyTv.setText(builder.toString());
    }

    /**
     * 初始化listener
     */
    private void initListener() {
        reportOrDeleteView.setOnClickListener(this);
        closeView.setOnClickListener(this);
        heartView.setOnClickListener(this);
        chatView.setOnClickListener(this);
        userDetailView.setOnClickListener(this);
        avatarView.setOnClickListener(this);
    }

    /**
     * 初始化视频的surface
     */
    private void initSurface() {
        showProgress(getString(R.string.buffering));
        mSurfaceView = new SurfaceView(this);
        containerLayout.removeAllViews();
        containerLayout.addView(mSurfaceView);
        mSurfaceView.getHolder().addCallback(mSurfaceHolderCallBack);
    }


    /**
     * 开启视频播放
     */
    private void openVideo() {
        release();
        try {
            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setDataSource(entity.mp4sd);
            ijkMediaPlayer.setDisplay(mSurfaceView.getHolder());
            ijkMediaPlayer.setLooping(true);
            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            initVideoListener();
            ijkMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.toast(this, getString(R.string.video_play_error));
            finish();
        }
    }

    /**
     * 释放播放器
     */
    private void release() {
        if (ijkMediaPlayer != null) {
            if (ijkMediaPlayer.isPlaying()) {
                ijkMediaPlayer.stop();
            }
            ijkMediaPlayer.reset();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }

    /**
     * 将显示置空
     */
    private void releaseWithoutStop() {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.setDisplay(null);
    }

    /**
     * 初始化video 播放的listener
     */
    private void initVideoListener() {
        ijkMediaPlayer.setOnPreparedListener(mPreparedListener);
        ijkMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        ijkMediaPlayer.setOnCompletionListener(mCompletionListener);
        ijkMediaPlayer.setOnErrorListener(mErrorListener);
        ijkMediaPlayer.setOnInfoListener(mInfoListener);
        ijkMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
    }

    /**
     * 播放信息回调
     */
    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:

                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:

                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:

                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:

                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            ;
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:

                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:

                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:

                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:

                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:

                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:

                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START: //首次开始
                            coverImgView.setVisibility(View.GONE);
                            dismissProgress();
                            break;
                    }
                    return true;
                }
            };


    /**
     * 尺寸改变回调
     */
    private IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {

                }
            };

    /**
     * buffer 回调
     */
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {

                }
            };

    /**
     * 播放错误回调
     */
    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    ToastUtils.toast(IJKAvPlayerActivity.this, getString(R.string.video_play_error));
                    finish();
                    return true;

                }
            };

    /**
     * 播放完成回调
     */
    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {

                }
            };

    /**
     * 播放准备回调
     */
    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {


        }
    };


    /**
     * 显示举报框
     */
    private void showReportLayout() {
        if (entity != null) {
            new BaseDialog(this)
                    .setBtnPanelView(R.layout.dialog_user_report_layout, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int btnId) {
                            dialogInterface.dismiss();
                            switch (btnId) {
                                case R.id.report_user_view:
                                    mPresenter.getReportData(IJKAvPlayerActivity.this, entity.userId, 1);
                                    break;
                            }
                        }
                    }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();
        }
    }

    private void initView() {
        avatarView = (ImageView) findViewById(R.id.avatar_view);
        closeView = (ImageView) findViewById(R.id.close_view);
        reportOrDeleteView = (ImageView) findViewById(R.id.report_view);
        heartView = (ImageView) findViewById(R.id.heart_view);
        chatView = (ImageView) findViewById(R.id.chat_view);
        userDetailView = (ImageView) findViewById(R.id.user_detail_view);
        nickNameTv = (TextView) findViewById(R.id.nick_name_view);
        verifyTv = (TextView) findViewById(R.id.verify_info_tv);
        avTopicLayout = (VideoTopicLayout) findViewById(R.id.video_question_layout);
        containerLayout = (FrameLayout) findViewById(R.id.surface_container_layout);
        coverImgView = (ImageView) findViewById(R.id.cover_img_view);
        mMaskView = findViewById(R.id.mask_layout);
        mHeartCountTv = (TextView) findViewById(R.id.heart_count_tv);
        bottomButtonLayout = findViewById(R.id.bottom_operation_layout);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_view:
                if (entity != null) {
                    EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VideoPlayPage.VIDEO_PLAY_CHAT_BTN);
                    Bundle args = new Bundle();
                    ExtendedData info = new ExtendedData();
                    info.otherSessionId = entity.userInfo.imAccId;
                    info.otherAvatar = entity.userInfo.avatar;
                    info.otherMemberId = entity.userId;
                    info.nikeName = entity.userInfo.nickName;
                    boolean isVip = AccountManager.getInstance().getZaAccount().isVip;
                    info.isLockMessage = !isVip;
                    info.isFromVideoPage = true;
                    info.videoMessage = entity.topicContent;
                    info.videoTitle = "评论" + entity.userInfo.nickName + "的回答";
                    info.videoPic = entity.cover;
                    info.videoId = entity.videoId;

                    args.putSerializable(ChatDetailFragment.EXTENDED_DATA, info);
                    Intent intent = new Intent(this, ChatDetailActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }
                break;
            case R.id.report_view:
                if (isMe()) {
                    showDeleteDialog();
                } else {
                    EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VideoPlayPage.VIDEO_PLAY_REPORT_BTN);
                    showReportLayout();
                }
                break;
            case R.id.heart_view:
                if (entity != null) {
                    EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VideoPlayPage.VIDEO_PLAY_PRAISE_BTN);
                    if (entity.likeId > -1) {
                        setIsHeart(false);
                        mPresenter.cancelPraise(String.valueOf(entity.likeId));
                    } else {
                        setIsHeart(true);
                        mPresenter.addPraise(String.valueOf(entity.userId), entity.videoId);
                    }
                }
                break;
            case R.id.avatar_view:
                EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VideoPlayPage.VIDEO_PLAY_AVATAR);
            case R.id.user_detail_view:
                if (view.getId() == R.id.user_detail_view) {
                    EventStatistics.recordLog(ResourceKey.VIDEO_PLAY_PAGE, ResourceKey.VideoPlayPage.VIDEO_PLAY_USER_DETAIL_BTN);
                }
                if (isMe())
                    return;
                if (entity != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentConstants.USER_ID, entity.userId + "");
                    bundle.putInt(ComeFrom.COME_FROM, ComeFrom.COME_FROM_VIDEO_PLAY_PAGE);

                    Intent detailIntent = new Intent(IJKAvPlayerActivity.this, RouterActivity.class);
                    detailIntent.putExtra(IntentConstants.REDIRECT_TARGET, RouterActivity.REDIRECT_TO_THIRD_PARTY);
                    detailIntent.putExtra(IntentConstants.REDIRECT_BUNDLE, bundle);
                    startActivity(detailIntent);
                }
                break;
            case R.id.close_view:
                finish();
                break;
        }
    }


    /**
     * 显示是否删除是否的对话框
     */
    private void showDeleteDialog() {
        if (entity != null) {
            new CommonDialog(this).setContent(getString(R.string.sure_to_delete_video)).setListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    switch (id) {
                        case R.id.sure_btn:
                            mPresenter.deleteVideo(entity.videoId);
                            break;
                    }
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.pause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /**
     * 去往支付页
     */
    @Override
    public void gotoPayPage() {
        DialogUtil.showOpenGoPay(this);
    }

    /**
     * 心动成功
     *
     * @param likeEntity
     */
    @Override
    public void praiseSuccess(LikeEntity likeEntity) {
        entity.likeId = likeEntity.likeId;
        entity.likeNum++;
        EventBus.getDefault().post(new VideoIndexNotifyEvent(entity.videoId, entity.likeId));
        // ToastUtils.toast(this, getResources().getString(R.string.fragment_third_party_praise_success));
        mHeartCountTv.setText(String.valueOf(entity.likeNum));

    }

    /**
     * 取消心动成功
     */
    @Override
    public void cancelLikeSuccess() {
        entity.likeId = -1;
        if (entity.likeNum > 0) {
            entity.likeNum--;
            EventBus.getDefault().post(new VideoIndexNotifyEvent(entity.videoId, entity.likeId));
        }
        //  ToastUtils.toast(this, getResources().getString(R.string.fragment_third_party_cancel_praise_text));
        mHeartCountTv.setText(String.valueOf(entity.likeNum));
    }

    /**
     * 取消心动失败
     */
    @Override
    public void cancelLikeFail() {
        setIsHeart(true);
    }

    /**
     * 心动失败
     *
     * @param errorCode
     * @param errorMsg
     */
    @Override
    public void praiseFail(String errorCode, String errorMsg) {
        setIsHeart(false);
        if (!TextUtils.isEmpty(errorCode)) {
            if (errorCode.equals(NetWorkErrorCode.TO_PAY_MEMBER)) {
                DialogUtil.showOpenGoPay(IJKAvPlayerActivity.this);
            } else if (errorCode.equals(NetWorkErrorCode.VIDEO_HEART_REACH_LIMIT)) {
                String text = errorMsg;
                if (TextUtils.isEmpty(text)) {
                    text = getString(R.string.fragment_third_party_praise_error_text);
                }
                new CommonDialog(IJKAvPlayerActivity.this).setContent(text).hideCancelBtn().
                        setListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                ToastUtils.toast(IJKAvPlayerActivity.this, errorMsg);
            }
        }
    }

    /**
     * 删除视频成功
     */
    @Override
    public void deleteVideoSuccess() {
        ToastUtils.toast(this, getResources().getString(R.string.delete_video_success));
        EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VIDEO));
        EventBus.getDefault().post(new VideoIndexNotifyEvent(true));
        finish();
    }

    /**
     * 设置是否点赞
     *
     * @param isPraise
     */
    private void setIsHeart(boolean isPraise) {
        if (isPraise) {
            heartView.setImageResource(R.drawable.video_red_heart_selector);
        } else {
            heartView.setImageResource(R.drawable.icon_video_heart_selector);
        }
    }


    private SurfaceHolder.Callback mSurfaceHolderCallBack = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            openVideo();
            ijkMediaPlayer.start();
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseWithoutStop();
        }
    };


}
