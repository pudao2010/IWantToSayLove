package com.bluewhaledt.saylove.ui.pay.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.pay.entity.PayCategories;
import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.entity.PurchasePageData;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/9.
 */

public interface PayService {

    /**
     * 获取支付方式列表
     */
    @FormUrlEncoded
    @POST(Url.GET_PAY_TYPE)
    Observable<ZAResponse<PayCategories>> getPayType();

    /**
     * 生成订单号
     *
     * @param payType        支付方式ID（1 支付宝；2 支付宝wap支付；3 微信旧支付；4 银联支付；7 微信统一支付）
     * @param paymentChannel 支付来源
     * @param count          购买个数
     * @param productId      产品ID
     * @param flagBit        支付标记 - 用于判断是否需要额外赠送服务
     * @param platform       平台
     * @param ext            其他一些附加字段
     * @return
     */
    @FormUrlEncoded
    @POST(Url.CREATE_ORDER)
    Observable<ZAResponse<PayOrder>> createOrder(@Field("payTypeId") int payType,
                                                 @Field("paymentChannel") int paymentChannel,
                                                 @Field("count") int count,
                                                 @Field("productId") int productId,
                                                 @Field("flagBit") int flagBit,
                                                 @Field("platform") int platform,
                                                 @Field("ext") String ext);

    @POST(Url.PRODUCT_LIST)
    Observable<ZAResponse<PurchasePageData>> getProductList();

    @POST(Url.GET_VERIFY_PRODUCT)
    Observable<ZAResponse<VerifyProduct>> getVerifyProduct();


}
