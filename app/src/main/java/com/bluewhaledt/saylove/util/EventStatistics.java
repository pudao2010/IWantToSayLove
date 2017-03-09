package com.bluewhaledt.saylove.util;

import com.bluewhaledt.saylove.BuildConfig;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.network.utils.ChannelUtils;
import com.bluewhaledt.saylove.network.utils.NetworkDeviceUtils;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/26.
 *
 */

public class EventStatistics {

    /**
     * 日志打桩
     * @param resourceKey 资源标识符(哪个页面或者功能)
     * @param accessPointDesc 打桩点，同一个resourceKey可以有不同的打桩点
     */
    public static synchronized void recordLog(String resourceKey,String accessPointDesc) {
        if (BuildConfig.DEBUG){
            return;
        }

        ZAAccount account = AccountManager.getInstance().getZaAccount();
        int uid = 0;
        if (account != null)
            uid = account.uid;
        ViewLogService service = ZARetrofit.getService(ZhenaiApplication.getContext(), ViewLogService.class);
        int channel = Integer.parseInt(ChannelUtils.getMainChannel(ZhenaiApplication.getContext()));
        int subChannel = Integer.parseInt(ChannelUtils.getSubChannel(ZhenaiApplication.getContext()));

        Observable observable = service.viewLog(Url.EVENT_LOG, "SayLove_"+resourceKey,accessPointDesc, uid,channel,subChannel, ResourceKey.APP_PLATFORM, NetworkDeviceUtils.getMacAddress(),0,0,0,"","","");
        HttpMethod.toSubscribe(observable, new BaseSubscriber(new ZASubscriberListener<ZAResponse<Void>>() {
            @Override
            public void onSuccess(ZAResponse<Void> response) {
                DebugUtils.d("EventStatistics",response.isError + "");
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                DebugUtils.d("EventStatistics",errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                DebugUtils.d("EventStatistics",e.getMessage());
            }
        }));
    }
    public interface ViewLogService {

        /**
         *
         * @param url 上报地址
         * @param resourceKey 资源标识符(必需加上应用的前缀，高端应用统一加上"SayLove_")
         * @param accessPointDesc 打桩点，同一个resourceKey可以有不同的打桩点
         * @param mid 用户ID
         * @param channel 渠道ID
         * @param subId  子渠道
         * @param platform 平台
         * @param sParam 设备唯一标识
         * @param ext1 扩展字段1，整型
         * @param ext2 扩展字段2，整型
         * @param ext3 扩展字段3，整型
         * @param ext4 扩展字段4，字符串
         * @param ext5 扩展字段5，字符串
         * @param ext6 扩展字段6，字符串
         * @return
         */
        @GET
        Observable<ZAResponse<Void>> viewLog(@retrofit2.http.Url String url,
                                             @Query("resourceKey") String resourceKey,
                                             @Query("accessPointDesc") String accessPointDesc,
                                             @Query("mid") int mid,
                                             @Query("channel") int channel,
                                             @Query("subId") int subId,
                                             @Query("platform") int platform,
                                             @Query("sParam") String sParam,
                                             @Query("ext1") int ext1,
                                             @Query("ext2") int ext2,
                                             @Query("ext3") int ext3,
                                             @Query("ext4") String ext4,
                                             @Query("ext5") String ext5,
                                             @Query("ext6") String ext6);
    }
}
