package com.bluewhaledt.saylove.ui.register_login.Head_portrait;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.dialog.BaseDialog;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.photo.BasePhotoActivity;
import com.bluewhaledt.saylove.ui.MainActivity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.HeadProtraitEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.RandomNameEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.presenter.HeadProtraitPresenter;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.view.IHeadProtraitView;
import com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText;
import com.bluewhaledt.saylove.ui.register_login.regist.LoginHelperPresenter;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;

import java.util.ArrayList;

/**
 * 描述：上传头像的类
 * 作者：shiming_li
 * 时间：2016/12/14 09:39
 * 包名：com.zhenai.saylove_icon.ui.register_login.Head_portrait
 * 项目名：SayLove
 */
public class HeadPortraitActivity extends BasePhotoActivity implements View.OnClickListener, IHeadProtraitView {
    public final static String NIKE_NAME="nike_name";
    private ImageView mAvatar;
    private com.bluewhaledt.saylove.ui.register_login.material_edittext.MaterialEditText mEdtext;
    private TextView mTextBtn;
    private Button mGoNextBtn;
    private HeadProtraitPresenter mHeadProtraitPresenter;
    private RelativeLayout mAvatarContainer;
    private ArrayList<String> mRanDomName;
    private int index=0;
    private boolean mISLoadPic=true;
    private String mFileName;
    private BaseActivity mActivity;
    private String mNikeName;
    private boolean isComeFromRecommend=false;
    private RelativeLayout mHeadPortraitContainer;
    private ImageView mHeadPortraitBg;
    private ImageView mHeadPortraitBgIcon;
    private String mAvactarUrl;
    private String mNikeNameUp;
    private boolean mIsFromRecommendPage;
    private boolean mIsFromRealNamePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_head_portrait_layout);
        bindsView();
        setTitle(R.string.head_protrait_title);
        setTitleBarLeftBtnListener(this);
        showTitleBar(true);
        initListener();
        initData();
        mHeadProtraitPresenter = new HeadProtraitPresenter(this, this);
        showProgress();
        //不能消失点击的progress
        if (mProgressDialog!=null){
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mHeadProtraitPresenter.getRandomNameEntity();
        EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HEAD_PORTART_PAGE);
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent!= null) {
                mNikeName = intent.getStringExtra(HeadPortraitActivity.NIKE_NAME);
                if (mNikeName != null && mNikeName.length()>0){
                    mEdtext.setText(mNikeName);
                    mEdtext.setSelection(mNikeName.length());
                    isComeFromRecommend=true;
                }
                mIsFromRecommendPage = intent.getBooleanExtra(Constants.IS_FROM_RECOMMEND_PAGE, false);
                mIsFromRealNamePage = intent.getBooleanExtra(Constants.IS_FROM_REALNAME_PAGE, false);
        }
        }
    private void bindsView() {
        mAvatar = (ImageView)findViewById(R.id.avatar_view_head_portrait);
        mEdtext = (MaterialEditText) findViewById(R.id.fragment_head_prortrait_ed_text);
        mTextBtn = (TextView) findViewById(R.id.fragment_head_prortrait_tv_change_edtext);
        mGoNextBtn = (Button) findViewById(R.id.fragment_head_prortrait_btn_gotonext);
        mAvatarContainer = (RelativeLayout) findViewById(R.id.avatar_not_pass_container_view);
        mHeadPortraitContainer = (RelativeLayout) findViewById(R.id.fragment_head_prortrait_container);
        mHeadPortraitBg = (ImageView) findViewById(R.id.avatar_view_head_portrait_bg);
        mHeadPortraitBgIcon = (ImageView) findViewById(R.id.avatar_view_head_portrait_bg_icon);
    }
    private void initListener() {
//        mAvatar.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mGoNextBtn.setOnClickListener(this);
        mHeadPortraitContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_head_prortrait_container:
                EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_UP_LOAD_PIC);
                showPhotoDialog(PHOTO_TYPE_AVATAR);
                break;
            case R.id.fragment_head_prortrait_btn_gotonext:
                EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_BTN_CONFIG);
                choiceToastContent(mEdtext);
                break;
            case R.id.fragment_head_prortrait_tv_change_edtext:
                EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_GET_RANDOM_NAME);
                if (mRanDomName!=null&&mRanDomName.size()>0){
                    if (index>=mRanDomName.size()){
                        mHeadProtraitPresenter.getRandomNameEntity();
                        index=0;
                    }else {
                        String name = mRanDomName.get(index);
                        mEdtext.setText(name);
                        mEdtext.setSelection(name.length());
                        index++;
                    }
                }else {
                    mHeadProtraitPresenter.getRandomNameEntity();
                }
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                DialogUtil.backHeadPortrait(this,mIsFromRealNamePage,mIsFromRecommendPage);

                break;
        }
    }
    private void choiceToastContent(MaterialEditText edtext) {
        String rex="(?!\\d+)[a-zA-Z0-9\\u4e00-\\u9fa5]+";
        if (mISLoadPic&&edtext.getText().toString().length()==0){
            ToastUtils.toast(this,R.string.head_portrait_activity_no_pic);
        }else if (!mISLoadPic&&edtext.getText().toString().length()==0){
            ToastUtils.toast(this,R.string.head_portrait_activity_no_name);
        }else if (!mISLoadPic&&edtext.getText().toString().length()>=0&&!edtext.getText().toString().matches(rex)){
            ToastUtils.toast(this,R.string.head_portrait_activity_wrong_name);
        }else if (!mISLoadPic&&edtext.getText().toString().length()>=0&&edtext.getText().toString().matches(rex)){
            mNikeNameUp = edtext.getText().toString();
            uploadPicture(mAvactarUrl,PHOTO_TYPE_AVATAR);
//            mHeadProtraitPresenter.getProtraitEntity(sp+"",edtext.getText().toString());
        }else if (mISLoadPic){
            ToastUtils.toast(this,R.string.head_portrait_activity_no_pic);
        }
    }
    private void showPhotoDialog(final int type) {
        new BaseDialog(this)
                .setBtnPanelView(R.layout.dialog_album_select_layout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int btnId) {
                        dialogInterface.dismiss();
                        switch (btnId) {
                            case R.id.take_photo_view:
                                setCurrentSelectPicType(type);
//                                goCameraPhoto(System.currentTimeMillis() + Constants.SAVE_PIC_FORMAT);
                                EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_USE_TAKE_PHOTO);
                                goFrontCameraPhoto(System.currentTimeMillis() + Constants.SAVE_PIC_FORMAT);
                                break;
                            case R.id.from_album_view:
                                EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_USE_CAME);
                                setCurrentSelectPicType(type);
                                goAlbum();
                                break;
                        }
                    }
                }).setMatchParent().setWindowAnimation(R.style.Dialog_Float_Animation).setGravity(Gravity.BOTTOM).show();


    }
    //提醒上传成功
    @Override
    public void getHeadPRotraitEntity(HeadProtraitEntity entity) {
        ToastUtils.toast(this,entity.msg);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void getRanDomName(RandomNameEntity entities) {
        dismissProgress();
        mRanDomName=entities.names;
        if (isComeFromRecommend){
            isComeFromRecommend=false;
        }else {
            String name = mRanDomName.get(index);
            mEdtext.setText(name);
            mEdtext.setSelection(name.length());
            index++;
        }
    }


    @Override
    public void getPhotoSuccess(String path) {
        mHeadPortraitBg.setVisibility(View.INVISIBLE);
        mHeadPortraitBgIcon.setVisibility(View.INVISIBLE);
        ImageLoaderFactory.getImageLoader()
                .with(this)
                .load(path)
                .circle()
                .into(mAvatar);
//        mAvatarContainer.setVisibility(View.VISIBLE);
        mAvactarUrl = path;
        mISLoadPic=false;
//        uploadPicture(path,PHOTO_TYPE_AVATAR);
    }

    @Override
    public void getPhotoFail() {

    }

    @Override
    public void uploadSignSuccess(UploadSignEntity signEntity) {
        mFileName = signEntity.fileName;
        LoginHelperPresenter loginHelperPresenter=new LoginHelperPresenter(this);
        loginHelperPresenter.getLoginHelper();
        StringBuffer sp = new StringBuffer();
        sp.append("["+"\"");
        sp.append(mFileName);
        sp.append("\""+"]");
        DebugUtils.d("shiming","uploadpic"+sp.toString()+"+++++++++++++++"+mNikeNameUp);
        if (mRanDomName!=null){
//            // TODO: 2016/12/28 优化的结果
//            if (mRanDomName.contains(mNikeName)){
//
//            }
            for (int i = 0; i <mRanDomName.size() ; i++) {
                if (mRanDomName.get(i)!=null) {
                    if (mRanDomName.get(i).equals(mNikeName)) {//等于了使用了随即的昵称
                        EventStatistics.recordLog(ResourceKey.HEAD_PORTART_PAGE,ResourceKey.HeadPortraritPage.HEAD_PORTRAIT_USE_RANDOM_NAME);
                    }
                }
            }
        }
        mHeadProtraitPresenter.getProtraitEntity(sp+"",mNikeNameUp);
    }
    //先不拦截图片
    @Override
    public boolean isIntercept() {
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void uploadFail(int type,String errorMsg) {
        super.uploadFail(type,errorMsg);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        DialogUtil.backHeadPortrait(this,mIsFromRealNamePage,mIsFromRecommendPage);
    }

}
