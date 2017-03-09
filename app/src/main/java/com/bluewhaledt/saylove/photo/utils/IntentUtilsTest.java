package com.bluewhaledt.saylove.photo.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.uitl.IntentUtils;
import com.jph.takephoto.uitl.TUtils;

/**
 * Intent工具类用于生成拍照、
 * 从相册选择照片，裁剪照片所需的Intent
 * Author: JPH
 * Date: 2016/6/7 0007 13:41
 */
public class IntentUtilsTest {
    private static final String TAG = IntentUtils.class.getName();

    /**
     *  获取图片多选的Intent
     * @param limit 最多选择图片张数的限制
     * */
    public static Intent getPickMultipleIntent(TContextWrap contextWrap, int limit){
        Intent intent = new Intent(contextWrap.getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, limit>0? limit:1);
        return intent;
    }

    /**
     * 获取裁剪照片的Intent
     * @param targetUri 要裁剪的照片
     * @param outPutUri 裁剪完成的照片
     * @param options 裁剪配置
     * @return
     */
    public static Intent getCropIntentWithOtherApp(Uri targetUri, Uri outPutUri, CropOptions options) {
        boolean isReturnData = TUtils.isReturnData();
        Log.w(TAG, "getCaptureIntentWithCrop:isReturnData:" + (isReturnData ? "true" : "false"));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(targetUri, "image/*");
        intent.putExtra("crop", "true");
        if (options.getAspectX()*options.getAspectY()>0){
            intent.putExtra("aspectX", options.getAspectX());
            intent.putExtra("aspectY", options.getAspectY());
        }
        if (options.getOutputX()*options.getOutputY()>0){
            intent.putExtra("outputX", options.getOutputX());
            intent.putExtra("outputY", options.getOutputY());
        }
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        intent.putExtra("return-data", isReturnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }

    /**
     * 获取拍照的Intent
     *
     *     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
     intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
     intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
     intent.putExtra("android.intent.extras.CAMERA_FACING", 1);  //打开前置摄像头 只在某些机型起作用
     startActivityForResult(intent, REQUEST_CODE_TAKE_PIC);
     intent.putExtra("android.intent.extras.CAMERA_FACING", 1);

     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

     intent.putExtra("camerasensortype", 2); // 调用前置摄像头
     intent.putExtra("autofocus", true); // 自动对焦
     intent.putExtra("fullScreen", false); // 全屏
     intent.putExtra("showActionIcons", false);
     07.
     08.startActivityForResult(intent, PICK_FROM_CAMERA);
     * @return
     */
    public static Intent getCaptureIntent(Uri outPutUri) {
//        Intent intent = new Intent();
//
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);//将拍取的照片保存到指定URI


        DebugUtils.d("shiming","启动摄像头");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);


//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
//        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);  //打开前置摄像头 只在某些机型起作用

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
//        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        DebugUtils.d("shiming","启动摄像头 over");
        return intent;
    }
    /**
     * 获取选择照片的Intent
     * @return
     */
    public static Intent getPickIntentWithGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        return intent;
    }
    /**
     * 获取从文件中选择照片的Intent
     * @return
     */
    public static Intent getPickIntentWithDocuments() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }
}
