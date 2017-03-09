package com.bluewhaledt.saylove.photo;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.photo.impl.IBasePhotoAction;
import com.bluewhaledt.saylove.photo.impl.IPhotoInvokeHandler;
import com.bluewhaledt.saylove.photo.impl.IPhotoType;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploaderListener;
import com.bluewhaledt.saylove.photo.utils.TakePhotoImplTest;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Map;

/**
 * Created by rade.chan on 2016/12/3.
 */

public abstract class BasePhotoActivity extends BaseActivity implements IBasePhotoAction, IPhotoInvokeHandler,IPhotoType,
        TakePhoto.TakeResultListener, InvokeListener {

    private PhotoProxy photoProxy;
    private int currentSelectPicType = PHOTO_TYPE_COMMON;  //当前图片选择类型

    private TakePhoto mTakePhoto;
    private CropOptions mCropOptions;
    private InvokeParam mInvokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        photoProxy = new PhotoProxy(this, null, this);
        if (savedInstanceState != null) {
            String path = savedInstanceState.getString(IntentConstants.PHOTO_SELECT_PATH);
            photoProxy.setPath(path);
        }

        mCropOptions = new CropOptions.Builder()
                .setAspectX(Constants.PHOTO_MIN_WIDTH)
                .setAspectY(Constants.PHOTO_MIN_HEIGHT)
                .setOutputX(Constants.PHOTO_MIN_WIDTH)
                .setOutputY(Constants.PHOTO_MIN_HEIGHT)
                .setWithOwnCrop(true)
                .create();
    }

    @Override
    public boolean isSetFilePrivate() {
        return false;
    }

    /**
     * 获取当前图片的类型
     * @return
     */
    public int getCurrentSelectPicType() {
        return currentSelectPicType;
    }

    /**
     * 设置当前图片的类型
     * @param currentSelectPicType
     */
    public void setCurrentSelectPicType(int currentSelectPicType) {
        this.currentSelectPicType = currentSelectPicType;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IntentConstants.PHOTO_SELECT_PATH, photoProxy.getPath());
    }

    /**
     * 显示上传的进度
     * @param msg
     */
    @Override
    public void showUploadProgress(String msg) {
        showProgress(msg);
    }

    /**
     * 关闭上传进度
     */
    @Override
    public void closeUploadProgress() {
        dismissProgress();
    }

    /**
     * 上传成功  子类可重写
     */
    @Override
    public void uploadSuccess(int type, String srcPath, String cosPath) {
        dismissProgress();
        ToastUtils.toast(this, R.string.upload_success);
    }

    /**
     * 上传失败 子类可重写
     */
    @Override
    public void uploadFail(int type,String msg) {
        dismissProgress();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.toast(this, msg);
        } else {
            ToastUtils.toast(this, R.string.upload_fail);
        }
    }

    @Override
    public void uploadSignSuccess(UploadSignEntity signEntity) {

    }

    @Override
    public boolean isIntercept() {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //继承了super
        super.onActivityResult(requestCode,resultCode,data);
        photoProxy.dealActivityResult(requestCode, resultCode, data);
        if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, "暂不支持该图片格式哦", Toast.LENGTH_SHORT).show();
        }
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     * @param params
     * @param listener
     */
    @Override
    public void uploadPicture(Map<String, String> params, ISingleUploaderListener listener) {
        photoProxy.uploadPicture(params, listener);
    }

    @Override
    public void uploadPicture(String avatarUrl, int type) {
        photoProxy.uploadPicture(avatarUrl,type);
    }

    @Override
    public void uploadPicture(Map<String, String> params) {
        photoProxy.uploadPicture(params);
    }

    /**
     上传图片
     * @param avatarUrl 图片本地地址
     * @param type        图片类型 1普通照，2头像，3学历，4车照，5房产
     * @param listener    回调
     */
    @Override
    public void uploadPicture(String avatarUrl, int type, ISingleUploaderListener listener) {
        photoProxy.uploadPicture(avatarUrl, type, listener);
    }

    /**
     * 打开相机 默认打开后置摄像头
     * @param photoName
     */
    @Override
    public void goCameraPhoto(final String photoName) {
//        photoProxy.goCameraPhoto(photoName);
        mPermissionHelper.requestPermissions("请授予相机权限！", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                String path = FileUtils.getSavePath(BasePhotoActivity.this, photoName);
                File file = FileUtils.createFile(path);
                getTakePhoto().onPickFromCaptureWithCrop(Uri.fromFile(file), mCropOptions);
            }

            @Override
            public void doAfterDenied(String... permission) {
                Log.e("=====", "doAfterDenied");
            }
        }, Manifest.permission.CAMERA);

    }

    /**
     * 打开前置摄像头
     * @param photoName
     */
    @Override
    public void goFrontCameraPhoto(String photoName) {
//        photoProxy.goFrontCameraPhoto(photoName);
//        openFrontFacingCameraGingerbread();
//        open();
//        String path = FileUtils.getSavePath(this, photoName);
//        File file = FileUtils.createFile(path);
//        getTakePhoto().onPickFromDocumentsWithCrop(Uri.fromFile(file), mCropOptions);
        goCameraPhoto(photoName);
    }
    public void open(){
        this.getPermissionHelper().requestPermissions("请授予相机权限！", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
//                path = FileUtils.getSavePath(mContext, photoName);
////                File file = FileUtils.createFile(path);
//                if (file.exists()) {
//                    file.delete();
//                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);  //打开前置摄像头 只在某些机型起作用
                startActivity(intent);

            }

            @Override
            public void doAfterDenied(String... permission) {
                Log.e("=====", "doAfterDenied");
            }
        }, Manifest.permission.CAMERA);
    }
    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
//                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
//                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }
    /**
     * 去往相册
     */
    @Override
    public void goAlbum() {
//        photoProxy.goAlbum();

        String path = FileUtils.getSavePath(this, System.currentTimeMillis() + Constants.SAVE_PIC_FORMAT);
        File file = FileUtils.createFile(path);
        getTakePhoto().onPickFromGalleryWithCrop(Uri.fromFile(file), mCropOptions);
    }

    @Override
    public void takeSuccess(TResult result) {
        // 请重写此方法
        if(result!=null && result.getImage()!=null) {
            getPhotoSuccess(result.getImage().getPath());
        }else{
            getPhotoFail();
        }
    }
    public abstract  void getPhotoSuccess(String path);

    public abstract  void getPhotoFail();
    @Override
    public void takeFail(TResult result, String msg) {
        getPhotoFail();
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            mInvokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,mInvokeParam,this);
    }

    protected TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(
                    new TakePhotoImplTest(this, this));
        }
        return mTakePhoto;
    }
}
