package com.bluewhaledt.saylove.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.FileUtils;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.util.UrlUtil;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.photo.impl.IBasePhotoAction;
import com.bluewhaledt.saylove.photo.impl.IPhotoInvokeHandler;
import com.bluewhaledt.saylove.photo.impl.IPhotoType;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploader;
import com.bluewhaledt.saylove.photo.uploader.ISingleUploaderListener;
import com.bluewhaledt.saylove.photo.uploader.IUploader;
import com.bluewhaledt.saylove.photo.uploader.impl.DefaultResultReporter;
import com.bluewhaledt.saylove.photo.uploader.impl.DefaultSignGetter;
import com.bluewhaledt.saylove.photo.uploader.impl.SinglePhotoUploader;
import com.bluewhaledt.saylove.service.QCloudService;
import com.tencent.cos.common.COSAuthority;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rade.chan on 2016/12/3.
 */

public class PhotoProxy implements IBasePhotoAction, IPhotoType {

    public static final int REQUEST_CODE_CHOICE_PIC = 1;
    public static final int REQUEST_CODE_TAKE_PIC = 2;
    public static final int REQUEST_CODE_CHOICE_PIC_KITKAT = 4;
    public static final int CROP_PHOTO_REQ = 5;

    private SinglePhotoUploader mUploader;
    private BaseActivity mContext;
    private BaseFragment mFragment;
    private IPhotoInvokeHandler iPhotoInvokeHandler;
    private String path;

    public PhotoProxy(BaseActivity context, BaseFragment fragment, IPhotoInvokeHandler handler) {
        this.mContext = context;
        this.mFragment = fragment;
        this.iPhotoInvokeHandler = handler;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void uploadPicture(String avatarUrl, int type) {
        uploadPicture(avatarUrl, type, null);
    }

    @Override
    public void uploadPicture(Map<String, String> params) {
        uploadPicture(params, null);
    }

    @Override
    public void uploadPicture(final String avatarUrl, final int type, ISingleUploaderListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(KEY_PHOTO_NAMES, avatarUrl);
        params.put(KEY_PHOTO_TYPE, String.valueOf(type));
        uploadPicture(params, listener);

    }

    @Override
    public void uploadPicture(final Map<String, String> params, ISingleUploaderListener listener) {
        if (params == null || params.size() == 0) {
            return;
        }
        String avatarUrl = params.get(KEY_PHOTO_NAMES);
        if (TextUtils.isEmpty(avatarUrl))
            return;
        File file = new File(avatarUrl);
        if (!file.exists())
            return;
        if (file.length() < 25 * 1024 || file.length() > 8 * 1024 * 1024) {     //在25k~8m内的照片可以传
            ToastUtils.toast(ZhenaiApplication.getContext(), ZhenaiApplication.getContext().getString(R.string.picture_size_not_allow));
            return;
        }

        mUploader = new SinglePhotoUploader(mContext, avatarUrl);
        // 获取签名，目前后台只提供了一个接口，使用SignGetter的默认实现
        mUploader.setSignGetter(new DefaultSignGetter());
        // 将上传结果反馈给服务器的接口可能有多种实现，最好使用自定义的ResultReporter
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                iPhotoInvokeHandler.showUploadProgress("正在上传");
            }
        });
        mUploader.setResultReporter(new DefaultResultReporter() {
            @Override
            public void reportSuccess(Context context, final IUploader uploader, List<String> cosPaths) {
                iPhotoInvokeHandler.uploadSignSuccess(mUploader.getUploadSignEntity());
                if (iPhotoInvokeHandler.isIntercept()) {
                    iPhotoInvokeHandler.closeUploadProgress();
                    return;
                }
                if (cosPaths == null || cosPaths.size() == 0)
                    return;

                JSONArray array = new JSONArray();
                for (String pic : cosPaths) {
                    array.put(pic);
                }

                params.put(KEY_PHOTO_NAMES, array.toString());
                if (iPhotoInvokeHandler.isSetFilePrivate()) {
                    UploadSignEntity entity = mUploader.getUploadSignEntity();
                    mUploader.updateFile(entity.bucket, entity.filePath, entity.singleSign, COSAuthority.EWRPRIVATE);
                }

                // 把上传结果上报给服务器
                QCloudService service = ZARetrofit.getService(context, QCloudService.class);
                HttpMethod.toSubscribe(service.savePicNames(params),
                        new BaseSubscriber<ZAResponse<Void>>(new ZASubscriberListener<ZAResponse<Void>>() {
                            @Override
                            public void onSuccess(ZAResponse<Void> response) {
                                // 必须调用callSuccess方法，通知uploader所有步骤全部完成
                                ((ISingleUploader) uploader).callSuccess();

                            }

                            @Override
                            public void onFail(String errorCode, String errorMsg) {
                                // 必须调用callSuccess方法，通知uploader所有步骤全部完成
                                ((ISingleUploader) uploader).callFailed(errorMsg);

                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                ((ISingleUploader) uploader).callFailed(mContext.getResources().getString(R.string.no_network_connected));

                            }
                        }));

            }
        });
        // uploader的监听器是直接运行在主线程的
        if (listener != null) {
            mUploader.setListener(listener);
        } else {
            final int type = Integer.parseInt(params.get(KEY_PHOTO_TYPE));
            mUploader.setListener(new ISingleUploaderListener() {
                @Override
                public void onProgress(long sentBytes, long totalBytes) {
//                    iPhotoInvokeHandler.showUploadProgress("正在上传..." +
//                            ((sentBytes * 100) / totalBytes) + "%");
                }

                @Override
                public void onSuccess(String srcPath, String cosPath) {
                    iPhotoInvokeHandler.closeUploadProgress();
                    iPhotoInvokeHandler.uploadSuccess(type, srcPath, cosPath);
                }

                @Override
                public void onFailed(String errorMsg) {
                    iPhotoInvokeHandler.closeUploadProgress();
                    iPhotoInvokeHandler.uploadFail(type, errorMsg);

                }
            });
        }
        mUploader.start();
    }


    @Override
    public void goCameraPhoto(final String photoName) {
        mContext.getPermissionHelper().requestPermissions("请授予相机权限！", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                path = FileUtils.getSavePath(mContext, photoName);
                File file = FileUtils.createFile(path);
                if (file.exists()) {
                    file.delete();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PIC);

            }

            @Override
            public void doAfterDenied(String... permission) {
                Log.e("=====", "doAfterDenied");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @Override
    public void goFrontCameraPhoto(final String photoName) {
        mContext.getPermissionHelper().requestPermissions("请授予相机权限！", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                path = FileUtils.getSavePath(mContext, photoName);
                File file = FileUtils.createFile(path);
                if (file.exists()) {
                    file.delete();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);  //打开前置摄像头 只在某些机型起作用
                startActivityForResult(intent, REQUEST_CODE_TAKE_PIC);

            }

            @Override
            public void doAfterDenied(String... permission) {
                Log.e("=====", "doAfterDenied");
            }
        }, Manifest.permission.CAMERA);

    }

    @Override
    public void goAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT < 19) {
            startActivityForResult(Intent.createChooser(intent, "请选择照片进行上传"),
                    REQUEST_CODE_CHOICE_PIC);
        } else {
            startActivityForResult(Intent.createChooser(intent, "请选择照片进行上传"),
                    REQUEST_CODE_CHOICE_PIC_KITKAT);
        }
    }

    protected void getPhotoSuccess(String path) {
        Intent intent = new Intent(mContext, PhotoCropActivity.class);
        intent.putExtra(IntentConstants.PHOTO_SELECT_PATH, path);
        startActivityForResult(intent, CROP_PHOTO_REQ);
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, requestCode);
        } else {
            mContext.startActivityForResult(intent, requestCode);
        }
    }

    public void dealActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mContext.getPermissionHelper().requestPermissions("请授予[读写]权限！", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                if (REQUEST_CODE_CHOICE_PIC == requestCode
                        || REQUEST_CODE_TAKE_PIC == requestCode
                        || REQUEST_CODE_CHOICE_PIC_KITKAT == requestCode) {
                    if (resultCode != Activity.RESULT_OK) {
                        return;
                    }
                    Uri dataUrl = null;
                    if (REQUEST_CODE_CHOICE_PIC == requestCode && data != null) {
                        dataUrl = data.getData();
                    } else if (REQUEST_CODE_TAKE_PIC == requestCode) {
                        if (path != null) {
                            dataUrl = Uri.fromFile(new File(path));
                        } else {
                            ToastUtils.toast(mContext, "照片无效,请重试...");
                            return;
                        }
                    }

                    String filePath;
                    if (REQUEST_CODE_CHOICE_PIC_KITKAT == requestCode && data != null) {
                        filePath = UrlUtil.getDocumentPicPath(mContext, data.getData());
                    } else {
                        filePath = UrlUtil.getIntentDataFilePath(mContext, dataUrl);
                    }
                    if (TextUtils.isEmpty(filePath)) {
                        getPhotoFail();
                    } else {
                        File file = new File(filePath);
                        if (file.exists()) {
                            getPhotoSuccess(filePath);
                        } else {
                            getPhotoFail();
                        }
                    }

                }
            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    protected void getPhotoFail() {
        ToastUtils.toast(mContext, R.string.photo_error);
    }

}
