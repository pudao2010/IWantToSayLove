package com.bluewhaledt.saylove.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ImageUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.widget.crop.CropImage;
import com.bluewhaledt.saylove.widget.crop.CropImageView;


/**
 * 裁剪图片
 */
public class PhotoCropActivity extends BaseActivity implements
        OnClickListener {

    private CropImageView mImageView;
    private Bitmap mBitmap;
    private CropImage mCrop;
    private Button imgOk;
    private String mPath = "CropImageActivity";
    public int screenWidth = 0;
    public int screenHeight = 0;
    private Bundle bundle;
    public static final int PRE_SUCCESS = 3000;
    public static final int PRE_BACK = 3001;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop_layout);
        init();
        initViews();
        initViewData();

    }


    private void rotate() {
        if (mCrop == null) {
            return;
        }
        mCrop.startRotate(-90.f);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    private void initData(Bundle bundle) {
        mPath = bundle.getString(IntentConstants.PHOTO_SELECT_PATH);
    }


    public void init() {
        screenWidth = ZhenaiApplication.getInstance().getScreenWidth();
        screenHeight = ZhenaiApplication.getInstance().getScreenHeight();
        bundle = getIntent().getExtras();
    }


    public void initViews() {
        mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
        imgOk = (Button) findViewById(R.id.imgOk);
        initData(bundle);
    }


    public void initViewData() {
        setTitleBarRightBtnText(R.string.rotate);
        setTitleBarRightBtnListener(this);

        setTitle(R.string.crop_photo_title);
        try {
            mBitmap = createBitmap(mPath, screenWidth, screenHeight);
            int degree = ImageUtils.gePhotoDegree(mPath);
            if (degree != 0) {
                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();
                Matrix m = new Matrix();
                m.setRotate(degree);
                Bitmap newBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, m, true);// 从新生成图片
                if (newBitmap != mBitmap && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
                mBitmap = newBitmap;
            }
            if (mBitmap == null) {
                ToastUtils.toast(PhotoCropActivity.this,R.string.crop_photo_not_found);
                finish();
            } else {
                resetImageView(mBitmap);
            }
        } catch (Exception e) {
            ToastUtils.toast(PhotoCropActivity.this,R.string.crop_photo_not_found);
            finish();
        }
        imgOk.setOnClickListener(this);
    }




    private void resetImageView(Bitmap b) {
        mImageView.clear();
        mImageView.setImageBitmap(b);
        mImageView.setImageBitmapResetBase(b, true);
        mCrop = new CropImage(this, mImageView, handler);
        mCrop.crop(b);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgOk:
                showProgress();
                Bitmap cropBitmap = mCrop.cropAndSave();
                if (isAvailable(cropBitmap)) {
                    String cropFilePath = CropImage.saveToLocal(cropBitmap, this);
                    photoHandler(cropFilePath);
                } else {
                    ToastUtils.toast(PhotoCropActivity.this, R.string.crop_photo_too_small);
                }
                break;
            case R.id.zhenai_lib_titlebar_right_text:
                rotate();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgress();
    }

    private boolean isAvailable(Bitmap bitmap) {
        if (bitmap.getHeight() < Constants.PHOTO_MIN_HEIGHT || bitmap.getWidth() < Constants.PHOTO_MIN_WIDTH) {
            return false;
        }
        return true;
    }

    private void photoHandler(String path) {
        if (!TextUtils.isEmpty(path)) {
            setResultDate(path);
        } else {
            setResult(RESULT_CANCELED);
            ToastUtils.toast(PhotoCropActivity.this,R.string.crop_photo_failure);
        }
        finish();
    }

    private void setResultDate(String path) {
        Intent intent = new Intent();
        intent.putExtra(IntentConstants.PHOTO_SELECT_PATH, path);
        setResult(RESULT_OK, intent);
    }


    public Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = (int) ratio + 1;
            newOpts.inJustDecodeBounds = false;
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case PRE_BACK:
                finish();
                break;
            case PRE_SUCCESS:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };


}

