package com.bluewhaledt.saylove.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.base.R;
import com.bluewhaledt.saylove.base.util.PermissionHelper;
import com.bluewhaledt.saylove.base.widget.dialog.SayLoveLoadingDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;

import static android.R.id.message;

/**
 * Created by zhenai on 2015/9/28.
 *
 */
public class BaseActivity extends AppCompatActivity implements ActionEventInterface {

    /**
     * 标题栏容器
     */
    private View titleBarLayout;
    /**
     * 内容容器
     */
    private FrameLayout containerLayout;

    public SayLoveLoadingDialog mProgressDialog;

    public PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置此activity的根布局，子类的布局将添加到此根布局内
        super.setContentView(R.layout.zhenai_library_base_layout);
        setFitsSystemStatusBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPermissionHelper = new PermissionHelper(this);
        initView();
        ActivityManager.getInstance().addActivity(this);
    }

    public PermissionHelper getPermissionHelper() {
        return mPermissionHelper;
    }

    /**
     * 如果需要设置沉浸式状态栏，可以重写这个方法在里面进行设置
     */
    protected void setFitsSystemStatusBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentTags.clear();
        ActivityManager.getInstance().removeActivity(this);
    }

    public void setContentView(int resId) {
        View v = getLayoutInflater().inflate(resId, null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        containerLayout.addView(v, lp);
    }

    public void setContentView(View view) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        containerLayout.addView(view, lp);
    }

    public void setContentView(BaseFragment fragment) {
        fragment.setEventInterface(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.zhenai_lib_container_layout, fragment, fragment.getClass().getSimpleName());
        ft.commit();

        fragmentTags.add(fragment.getClass().getSimpleName());

    }

    private void initView() {
        titleBarLayout = findViewById(R.id.zhenai_lib_titlebar_layout);
        containerLayout = (FrameLayout) findViewById(R.id.zhenai_lib_container_layout);
        titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showTitleBar(boolean isShow) {
        if (isShow) {
            titleBarLayout.setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.setVisibility(View.GONE);
        }
    }

    protected void showTitleBarUnderline(boolean showUnderline) {
        titleBarLayout.findViewById(R.id.titlebar_underline).setVisibility(showUnderline ? View.VISIBLE : View.GONE);
    }

    public void showTitleBarLeftBtn(boolean show) {
        if (show) {
            titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text).setVisibility(View.GONE);
        }
    }

    protected void setTitle(String text) {
        ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_title_text)).setText(text);
    }

    public void setTitle(int textId) {
        ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_title_text)).setText(textId);
    }

    protected void setTitleBarLeftBtnImage(int imgId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        leftTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnImage(Bitmap bitmap) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        leftTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnText(int textId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        leftTV.setText(textId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarLeftBtnText(String text) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        leftTV.setText(text);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarLeftBtn(int textId, int imgId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        leftTV.setText(textId);
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnListener(View.OnClickListener listener) {
        titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text).setOnClickListener(listener);
    }

    protected void setTitleBarRightBtnListener(View.OnClickListener listener) {
        titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_text).setOnClickListener(listener);
    }

    protected void setTitleBarRightBtnImage(int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_text));
        rightTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnImage(Bitmap bitmap) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_text));
        rightTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnText(int textId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_text));
        rightTV.setText(textId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarRightBtn(int textId, int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_left_text));
        rightTV.setText(textId);
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnText(String text) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_text));
        rightTV.setText(text);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }


    protected void showRedDot(boolean isShow) {
        if (isShow) {
            titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_red_dot).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.zhenai_lib_titlebar_right_red_dot).setVisibility(View.GONE);
        }
    }

    public View getTitleBar() {
        return titleBarLayout;
    }

    public void startFragment(Class<? extends BaseFragment> clazz, Bundle args) {
        startFragment(clazz, args, false);
    }

    /**
     * 用于保存用户已经启动的fragment任务栈
     */
    private LinkedList<String> fragmentTags = new LinkedList<String>();

    public void startFragment(Class<? extends BaseFragment> clazz, Bundle args, boolean putStack) {
        try {

            final BaseFragment fragment = clazz.newInstance();
            fragment.setEventInterface(this);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (!fragmentTags.isEmpty()) {
                String preFragmentTag = fragmentTags.getLast();
                final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);
                ft.hide(preFragment);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_left_out);
                        preFragment.getView().startAnimation(leftOutAnim);
                    }
                });
            }

            ft.add(R.id.zhenai_lib_container_layout, fragment, clazz.getSimpleName());
            fragmentTags.add(clazz.getSimpleName());
            if (putStack) {
                ft.addToBackStack(null);//将当前的fragment压入栈中，以便将返回或者用户回退界面时能返回上一个页面
            }
            ft.commitAllowingStateLoss();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Animation rightInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_right_in);
                    fragment.getView().startAnimation(rightInAnim);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyHandler handler = new MyHandler();

    private static class MyHandler extends Handler {

    }

    protected void startFragmentForResult(BaseFragment startForResultFragment, Class<? extends BaseFragment> clazz, Bundle args, int requestCode, boolean putStack) {
        startForResultFragment.isHandleForResult = true;
        startForResultFragment.requestCode = requestCode;
        startFragment(clazz, args, putStack);
    }

    private void hindFragments() {

    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }


    @Override
    public void onEvent(ActionMode mode, final BaseFragment currentFragment, Class<? extends BaseFragment> clazz, Bundle args, int requestCode, int resultCode, Intent data) {
        switch (mode) {
            case START_ACTIVITY_FOR_RESULT:
                startFragmentForResult(currentFragment, clazz, args, requestCode, true);
                break;
            case FINISH:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 0) {
                    fragmentTags.pollLast();
                    if (!fragmentTags.isEmpty()) {
                        String preFragmentTag = fragmentTags.getLast();
                        final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_left_in);
                                preFragment.getView().startAnimation(leftInAnim);

                                Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_right_out);
                                currentFragment.getView().startAnimation(rightOutAnim);
                            }
                        });
                        fragmentTransaction.show(preFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        if (preFragment.isHandleForResult) {
//                            preFragment.resultCode = currentFragment.resultCode;
//                            preFragment.intentData = currentFragment.intentData;
                            preFragment.onActivityResult(preFragment.requestCode, currentFragment.resultCode, currentFragment.intentData);
                        }
                        fragmentManager.popBackStack();//返回上一fragment

                    } else {
                        finish();
                    }


                } else {
                    finish();
                }
                break;
            case START_ACTIVITY:
                startFragment(clazz, args, true);
                break;
            case SET_RESULT:
                currentFragment.resultCode = resultCode;
                currentFragment.intentData = data;
                break;
            case START_ACTIVITY_AND_FINISH:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_left_out);
                        currentFragment.getView().startAnimation(leftOutAnim);
                    }
                });
                ft.remove(currentFragment);
                ft.commitAllowingStateLoss();
                fragmentTags.pollLast();
                fm.popBackStack();
                startFragment(clazz, args, true);
                break;
            case START_ACTIVITY_FRO_SINGLE_TASK:
                int index = fragmentTags.indexOf(clazz.getSimpleName());
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                if (index != -1) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_left_out);
                            currentFragment.getView().startAnimation(leftOutAnim);
                        }
                    });
                    int fragmentCount = fm2.getBackStackEntryCount();
                    for (int i = fragmentCount; i > index; i--) {
                        fm2.popBackStack();
                        fragmentTags.pollLast();
                    }
                    final String topFragmentTag = fragmentTags.getLast();
                    final BaseFragment topFragment = (BaseFragment) fm2.findFragmentByTag(topFragmentTag);
                    ft2.show(topFragment);
                    ft2.commitAllowingStateLoss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Animation rightInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_right_in);
                            topFragment.getView().startAnimation(rightInAnim);
                        }
                    });
                } else {
                    startFragment(clazz, args, true);
                }


                break;
        }
    }

    protected void removeAllFragment() {
        fragmentTags.clear();
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                /**如果fragment自己消化了返回按钮事件，则不做处理*/
                String curFragmentTag = fragmentTags.getLast();
                final BaseFragment curFragment = (BaseFragment) fragmentManager.findFragmentByTag(curFragmentTag);
                boolean consume = curFragment.onKeyDown(keyCode, event);
                if (!consume) {
//                    ft.setCustomAnimations(R.anim.zhenai_library_slide_left_in, R.anim.zhenai_library_slide_right_out);
                    fragmentTags.pollLast();

                    if (!fragmentTags.isEmpty()) {
                        String preFragmentTag = fragmentTags.getLast();
                        final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_left_in);
                                preFragment.getView().startAnimation(leftInAnim);

                                Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.zhenai_library_slide_right_out);
                                curFragment.getView().startAnimation(rightOutAnim);
                            }
                        });
                        ft.show(preFragment);
                        ft.commitAllowingStateLoss();
                        fragmentManager.popBackStack();
                        return true;
                    } else {
                        fragmentManager.popBackStack();
                        return super.onKeyDown(keyCode, event);
                    }

                } else {
                    return true;
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // start--------------软键盘控制代码----------------

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 强制开关软键盘
     */
    public void toggleSoftInputFoced() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void showProgress() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new SayLoveLoadingDialog(this, getString(R.string.waiting));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.hideMsg();
        mProgressDialog.show();
    }

    protected void showProgress(String message) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new SayLoveLoadingDialog(this, message);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.show();
    }

    protected void showProgress(@StringRes int resId) {
        if (isFinishing()) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new SayLoveLoadingDialog(this, getString(resId));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(getString(resId));
        mProgressDialog.show();
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
