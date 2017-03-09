package com.bluewhaledt.saylove.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.pay.entity.PayCategories;
import com.bluewhaledt.saylove.ui.pay.entity.PurchasePageData;
import com.bluewhaledt.saylove.ui.pay.presenter.ProductPagePresenter;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;
import com.bluewhaledt.saylove.ui.pay.view.IProductPageViewAction;
import com.bluewhaledt.saylove.ui.pay.view.PrivilegeItemView;
import com.bluewhaledt.saylove.ui.pay.view.ProductItemView;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.NumberUtil;
import com.bluewhaledt.saylove.util.PhotoUrlUtils;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener, IProductPageViewAction, IBaseSurePayView {

    private ProductPagePresenter presenter;

    private int paymentChannel = PaymentChannel.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setTitle(R.string.activity_pay_title);
        setTitleBarLeftBtnListener(this);

        paymentChannel = getIntent().getIntExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.DEFAULT);
        presenter = new ProductPagePresenter(this);
        presenter.getProductList(this);

        EventStatistics.recordLog(ResourceKey.PURCHASE_PAGE, ResourceKey.PurchasePage.PURCHASE_PAGE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhenai_lib_titlebar_left_text:
                finish();
                break;
        }

    }

    @Override
    public void onProductDataReceived(PurchasePageData data) {
        dismissProgress();
        ImageLoaderFactory.getImageLoader()
                .with(this)
                .load(PhotoUrlUtils.format(data.avatar, PhotoUrlUtils.TYPE_3))
                .placeholder(R.mipmap.default_avatar_feman)
                .circle()
                .into((ImageView) findViewById(R.id.iv_avatar));
        TextView nickNameTv = (TextView) findViewById(R.id.tv_nick_name);
        if (data.isVip) {
            nickNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_me_member_vip, 0);
        } else {
            nickNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        nickNameTv.setText(data.nickName);

        ((TextView) findViewById(R.id.tv_effective_dates)).setText(data.vipServiceTime);
        LinearLayout productsListContainer = (LinearLayout) findViewById(R.id.ll_products_container);
        int index = 0;
        for (final PurchasePageData.Product product : data.products) {
            final ProductItemView itemView = new ProductItemView(this);
            itemView.setLimitTime(product.amount + product.uint);
            itemView.setPrice(NumberUtil.changeNumber(product.money));
            if (index == 0) {
                itemView.setPriceColor(getResources().getColor(R.color.color_f0cf62));
                itemView.showAveragePerDay(true);
                itemView.setAveragePerDay(product.dayPrice);
                itemView.setAveragePerDayTextColor(getResources().getColor(R.color.color_f0cf62));
                itemView.setPurchaseBg(R.drawable.activity_pay_purchase_btn_light_selector);
                itemView.setPurchaseBtnTextColor(getResources().getColor(R.color.color_f0cf62));

            } else {
                itemView.setPriceColor(getResources().getColor(R.color.black));
                itemView.showAveragePerDay(false);
                itemView.setPurchaseBg(R.drawable.activity_pay_purchase_btn_selector);
                itemView.setPurchaseBtnTextColor(getResources().getColor(R.color.black));
            }
            if (data.isVip) {
                itemView.setPurchaseBtnText(R.string.purchase_more);
            } else {
                itemView.setPurchaseBtnText(R.string.purchase_text);
            }

            itemView.setPurchaseBtnOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (product.money == 488) {
                        EventStatistics.recordLog(ResourceKey.PURCHASE_PAGE, ResourceKey.PurchasePage.CLICK_488);
                    } else if (product.money == 368) {
                        EventStatistics.recordLog(ResourceKey.PURCHASE_PAGE, ResourceKey.PurchasePage.CLICK_368);
                    } else if (product.money == 268) {
                        EventStatistics.recordLog(ResourceKey.PURCHASE_PAGE, ResourceKey.PurchasePage.CLICK_268);
                    }
                    DialogUtil.showChosePayTypeDialog(PayActivity.this, PayActivity.this, product.productName + "：",
                            NumberUtil.changeNumber(product.money), paymentChannel, product.productId);

                }
            });

            productsListContainer.addView(itemView);
            index++;
        }

        LinearLayout privilegeContainer = (LinearLayout) findViewById(R.id.ll_privilege_container);
        for (PurchasePageData.Privilege privilege : data.privileges) {
            PrivilegeItemView itemView = new PrivilegeItemView(this);
            itemView.setLeftIcon(privilege.icon);
            itemView.setTips(privilege.desc);
            itemView.setTitle(privilege.name);
            privilegeContainer.addView(itemView);
        }


    }

    @Override
    public void showLoadProgress() {
        showProgress();
    }

    @Override
    public void dismissLoadProgress() {
        dismissProgress();
    }

    @Override
    public void showHasPayIn24HoursDialog(String windowContent) {

    }

    @Override
    public void paySuccess() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        Intent data = new Intent(BroadcastActions.PAY_VIP_SUCCESS);
        manager.sendBroadcast(data);
        finish();
    }

    @Override
    public void payFailed(String msg) {
        ToastUtils.toast(this, msg);
    }

    @Override
    public void getPayTypeSuccess(PayCategories payCategories) {

    }
}
