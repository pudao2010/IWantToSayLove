package com.bluewhaledt.saylove.base.widget.listitem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.base.R;
import com.bluewhaledt.saylove.base.util.DensityUtils;

/*
 *  @项目名：  zhenai_app 
 *  @包名：    com.zhenai.saylove.pageofmine.views
 *  @文件名:   ListItemLayout
 *  @创建者:   Coco
 *  @创建时间:  2016/11/15 17:56
 *  @描述：我的页面条目控件抽取
 */
public class ListItemLayout extends LinearLayout {

    private RelativeLayout mContainerLayout;
    private ImageView mListItemLeftIcon;
    private TextView mListItemTitle;
    private TextView mListItemContent;
    private ImageView mListItemRightIcon;
    private ImageView mListItemArrow;
    private View mLine;

    public ListItemLayout(Context context) {
        this(context, null);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 设置点击效果
//        TypedValue outValue = new TypedValue();
//        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
//        setBackgroundResource(outValue.resourceId);
        setClickable(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.zhenai_library_list_item_layout, this);

        initViews();

        initAttrs(attrs);
    }

    private void initViews() {
        mContainerLayout = (RelativeLayout) findViewById(R.id.list_item_container);
        mListItemLeftIcon = (ImageView) findViewById(R.id.list_item_left_icon);
        mListItemTitle = (TextView) findViewById(R.id.list_item_title);
        mListItemArrow = (ImageView) findViewById(R.id.list_item_arrow);
        mListItemContent = (TextView) findViewById(R.id.list_item_content);
        mListItemRightIcon = (ImageView) findViewById(R.id.list_item_count_icon);
        mLine = findViewById(R.id.line);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ListItemLayout);

        int paddingLeft = array.getDimensionPixelOffset(R.styleable.ListItemLayout_paddingLeft, 0);
        int paddingTop = array.getDimensionPixelOffset(R.styleable.ListItemLayout_paddingTop, 0);
        int paddingRight = array.getDimensionPixelOffset(R.styleable.ListItemLayout_paddingRight, 0);
        int paddingBottom = array.getDimensionPixelOffset(R.styleable.ListItemLayout_paddingBottom, 0);
        mContainerLayout.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        //设置左边图标可见
        boolean isLeftIconVisible = array.getBoolean(R.styleable.ListItemLayout_leftIconVisible, true);
        mListItemLeftIcon.setVisibility(isLeftIconVisible ? VISIBLE : GONE);

        // 设置左边图标
        Drawable mLeftIcon = array.getDrawable(R.styleable.ListItemLayout_leftIcon);
        if (mLeftIcon != null) {
            mListItemLeftIcon.setImageDrawable(mLeftIcon);
            mListItemLeftIcon.setVisibility(View.VISIBLE);
        }

        // 设置标题
        CharSequence titleText = array.getText(R.styleable.ListItemLayout_titleText);
        if (titleText != null) {
            mListItemTitle.setText(titleText);
        }

        //设置标题字体大小
        float titleSize = array.getDimension(R.styleable.ListItemLayout_titleSize,
                DensityUtils.sp2px(getContext(), 14));
        mListItemTitle.setTextSize(DensityUtils.px2sp(getContext(), titleSize));

        //设置标题字体颜色
        int titleColor = array.getColor(R.styleable.ListItemLayout_titleColor,
                getResources().getColor(android.R.color.black));
        mListItemTitle.setTextColor(titleColor);

        // 设置标题左边距
        int titleMarginLeft = (int) array.getDimension(R.styleable.ListItemLayout_titleMarginLeft, 0);
        RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) mListItemTitle.getLayoutParams();
        titleParams.leftMargin = titleMarginLeft;
        mListItemTitle.setLayoutParams(titleParams);

        // 设置内容
        CharSequence contentText = array.getText(R.styleable.ListItemLayout_contentText);
        if (contentText != null) {
            setTextContent(contentText);
        }

        // 设置内容hint
        CharSequence contentHint = array.getText(R.styleable.ListItemLayout_contentHint);
        if (contentHint != null) {
            setTextContentHint(contentHint);
        }

        //设置内容字体大小
        float contentSize = array.getDimension(R.styleable.ListItemLayout_contentSize,
                DensityUtils.sp2px(getContext(), 14));
        mListItemContent.setTextSize(DensityUtils.px2sp(getContext(), contentSize));

        // 设置内容字体颜色
        int textColor = array.getColor(R.styleable.ListItemLayout_contentColor,
                getResources().getColor(android.R.color.black));
        mListItemContent.setTextColor(textColor);

        //设置内容可见
        boolean contentVisibility = array.getBoolean(R.styleable.ListItemLayout_contentVisibility, false);
        mListItemContent.setVisibility(contentVisibility ? VISIBLE : GONE);

        // 设置内容右边距
        RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) mListItemContent.getLayoutParams();
        contentParams.rightMargin = (int) array.getDimension(R.styleable.ListItemLayout_contentMarginRight,
                DensityUtils.dp2px(getContext(), 14));
        mListItemContent.setLayoutParams(contentParams);

        // 设置箭头可见
        boolean isArrowVisibility = array.getBoolean(R.styleable.ListItemLayout_arrowIconVisibility, true);
        mListItemArrow.setVisibility(isArrowVisibility ? VISIBLE : GONE);

        // 设置箭头图标
        Drawable mArrowIcon = array.getDrawable(R.styleable.ListItemLayout_arrowIcon);
        if (mArrowIcon != null) {
            mListItemArrow.setImageDrawable(mArrowIcon);
            mListItemArrow.setVisibility(VISIBLE);
        }

        // 设置右边图标
        Drawable mRightIcon = array.getDrawable(R.styleable.ListItemLayout_rightForIcon);
        if (mRightIcon != null) {
            mListItemRightIcon.setImageDrawable(mRightIcon);
            mListItemRightIcon.setVisibility(View.VISIBLE);
        }
        // 设置右边图标右边距
        RelativeLayout.LayoutParams rightIconParams = (RelativeLayout.LayoutParams) mListItemContent.getLayoutParams();
        rightIconParams.rightMargin = (int) array.getDimension(R.styleable.ListItemLayout_rightIconMarginRight,
                DensityUtils.dp2px(getContext(), 14));
        mListItemContent.setLayoutParams(rightIconParams);

        // 设置右边图标显示隐藏
        boolean isRightIconVisibility = array.getBoolean(R.styleable.ListItemLayout_rightIconVisibility, false);
        mListItemRightIcon.setVisibility(isRightIconVisibility ? VISIBLE : GONE);

        //显示隐藏分割线
        boolean isLineVisibility = array.getBoolean(R.styleable.ListItemLayout_lineVisibility, true);
        mLine.setVisibility(isLineVisibility ? VISIBLE : GONE);

        // 设置分割线左右边距
        LayoutParams lineParams = (LayoutParams) mLine.getLayoutParams();
        lineParams.leftMargin = array.getDimensionPixelOffset(R.styleable.ListItemLayout_lineMarginLeft, 0);
        lineParams.rightMargin = array.getDimensionPixelOffset(R.styleable.ListItemLayout_lineMarginRight, 0);
        mLine.setLayoutParams(lineParams);

        array.recycle();
    }

    /**
     * 设置左边图标
     *
     * @param resId 图标ID
     */
    public void setLeftIcon(@DrawableRes int resId) {
        mListItemLeftIcon.setImageResource(resId);
    }

    /**
     * 设置左边图标
     *
     * @param drawable 图标Drawable
     */
    public void setLeftIcon(Drawable drawable) {
        mListItemLeftIcon.setImageDrawable(drawable);
    }

    /**
     * 显示隐藏左边图标
     *
     * @param visibility View.GONE-View.INVISIBLE-View.VISIBLE
     */
    public void setLeftIconVisible(int visibility) {
        mListItemLeftIcon.setVisibility(visibility);
    }

    /**
     * 设置文本标题
     *
     * @param resId 标题内容ID
     */
    public void setTextTitle(@StringRes int resId) {
        setTextTitle(getResources().getString(resId));
    }

    /**
     * 设置文本标题
     *
     * @param charSequence 标题字符串
     */
    public void setTextTitle(CharSequence charSequence) {
        mListItemTitle.setText(charSequence);
    }

    /**
     * 设置文本标题大小
     *
     * @param textSize sp
     */
    public void setTitleTextSize(int textSize) {
        mListItemTitle.setTextSize(textSize);
    }

    /**
     * 设置文本标题颜色
     *
     * @param resId sp
     */
    public void setTitleTextColor(@ColorRes int resId) {
        mListItemTitle.setTextColor(getResources().getColor(resId));
    }

    /**
     * 返回标题内容
     *
     * @return
     */
    public CharSequence getTextTitle() {
        return mListItemTitle.getText();
    }

    /**
     * 设置文本内容
     *
     * @param charSequence
     */
    public void setTextContent(CharSequence charSequence) {
        mListItemContent.setText(charSequence);
    }

    /**
     * 设置文本内容
     *
     * @param resId
     */
    public void setTextContent(@StringRes int resId) {
        mListItemContent.setText(getResources().getString(resId));
    }

    /**
     * 设置文本内容
     *
     * @param charSequence
     */
    public void setTextContentHint(CharSequence charSequence) {
        mListItemContent.setHint(charSequence);
    }

    /**
     * 设置文本内容
     *
     * @param resId
     */
    public void setTextContentHint(@StringRes int resId) {
        mListItemContent.setHint(getResources().getString(resId));
    }

    /**
     * 设置内容字体大小
     *
     * @param textSize sp
     */
    public void setContextTextSize(int textSize) {
        mListItemContent.setTextSize(textSize);
    }

    /**
     * 设置内容字体颜色
     *
     * @param resId
     */
    public void setContentTextColor(@ColorRes int resId) {
        mListItemContent.setTextColor(getResources().getColor(resId));
    }

    /**
     * 返回文本内容
     */
    public CharSequence getTextContent() {
        return mListItemContent.getText();
    }

    /**
     * 设置右边图标
     *
     * @param resId 图标ID
     */
    public void setRightIcon(@DrawableRes int resId) {
        mListItemRightIcon.setImageResource(resId);
    }

    /**
     * 设置右边图标
     *
     * @param drawable 图标Drawable
     */
    public void setRightIcon(Drawable drawable) {
        mListItemRightIcon.setImageDrawable(drawable);
    }

    /**
     * 显示隐藏右边图标
     *
     * @param visibility View.GONE-View.INVISIBLE-View.VISIBLE
     */
    public void setRightIconVisible(int visibility) {
        mListItemRightIcon.setVisibility(visibility);
    }

    /**
     * 取得右边图标
     *
     * @return
     */
    public ImageView getContentIcon() {
        return mListItemRightIcon;
    }

    /**
     * 显示隐藏箭头图标
     *
     * @param visibility View.GONE-View.INVISIBLE-View.VISIBLE
     */
    public void setArrowIconVisible(int visibility) {
        mListItemArrow.setVisibility(visibility);
    }
}
