package com.bluewhaledt.saylove.ui.info.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.InfoService;
import com.bluewhaledt.saylove.ui.info.entity.PhotoListEntity;
import com.bluewhaledt.saylove.ui.info.entity.ReportData;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.info.view.IUserInfoView;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.service.RecommendService;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func5;
import rx.schedulers.Schedulers;

/**
 * Created by rade.chan on 2016/11/29.
 */

public class UserInfoPresenter {
    private InfoService mService;
    private IUserInfoView userInfoView;
    private Context mContext;

    public UserInfoPresenter(Context context, IUserInfoView view) {
        this.userInfoView = view;
        mService = ZARetrofit.getService(context, InfoService.class);
        mContext = context;
    }

    private class DataModel {
        public ZAResponse<UserInfoEntity> userInfoEntityResponse;
        public ZAResponse<PhotoListEntity> photoListEntityResponse;
        public ZAResponse<VerifyInfoEntity> verifyInfoEntityResponse;
        public ZAResponse<RequireInfoEntity> requireInfoEntityResponse;
        public ZAResponse<VideoIndexListEntity> videoIndexListEntityResponse;
    }

    public void loadAllData(String objId) {
        Observable<ZAResponse<UserInfoEntity>> observable1 = mService.getUserInfo(objId);
        observable1.subscribeOn(Schedulers.newThread());
        Observable<ZAResponse<PhotoListEntity>> observable2 = mService.getUserPhotos(Constants.FIRST_PAGE, Constants.PAGE_COUNT, objId);
        observable2.subscribeOn(Schedulers.newThread());
        Observable<ZAResponse<VerifyInfoEntity>> observable3 = mService.getVerifyInfo(objId);
        observable3.subscribeOn(Schedulers.newThread());
        Observable<ZAResponse<RequireInfoEntity>> observable4 = mService.getRequireConditions(objId);
        observable4.subscribeOn(Schedulers.newThread());

        Observable<ZAResponse<VideoIndexListEntity>> observable5 = null;
        if (!TextUtils.isEmpty(objId)) {
            observable5 = mService.getOtherVideoList(objId, Constants.FIRST_PAGE);
        } else {
            observable5 = mService.getMyVideoList(Constants.FIRST_PAGE);
        }
        observable5.subscribeOn(Schedulers.newThread());


        Observable.zip(observable1, observable2, observable3, observable4, observable5, new Func5<ZAResponse<UserInfoEntity>, ZAResponse<PhotoListEntity>,
                ZAResponse<VerifyInfoEntity>, ZAResponse<RequireInfoEntity>, ZAResponse<VideoIndexListEntity>, DataModel>() {
            @Override
            public DataModel call(ZAResponse<UserInfoEntity> userInfoEntityZAResponse, ZAResponse<PhotoListEntity> photoListEntityZAResponse, ZAResponse<VerifyInfoEntity> verifyInfoEntityZAResponse,
                                  ZAResponse<RequireInfoEntity> requireInfoEntityZAResponse, ZAResponse<VideoIndexListEntity> videoInfoEntity) {
                DataModel dataModel = new DataModel();
                dataModel.photoListEntityResponse = photoListEntityZAResponse;
                dataModel.requireInfoEntityResponse = requireInfoEntityZAResponse;
                dataModel.userInfoEntityResponse = userInfoEntityZAResponse;
                dataModel.verifyInfoEntityResponse = verifyInfoEntityZAResponse;
                dataModel.videoIndexListEntityResponse = videoInfoEntity;
                return dataModel;
            }
        })
                .compose(HttpMethod.<DataModel>defaultHandler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        ((BaseActivity) mContext).showProgress();
                    }
                })
                .subscribe(new Subscriber<DataModel>() {
                    @Override
                    public void onCompleted() {
                        ((BaseActivity) mContext).dismissProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((BaseActivity) mContext).dismissProgress();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataModel data) {
                        ((BaseActivity) mContext).dismissProgress();
                        if (data.userInfoEntityResponse != null) {
                            if (data.userInfoEntityResponse.isError){
                                userInfoView.handleError(data.userInfoEntityResponse.errorCode,data.userInfoEntityResponse.errorMessage);
                                return;
                            }else{
                                userInfoView.showBasicInfo(data.userInfoEntityResponse.data);
                            }

                        }
                        if (data.photoListEntityResponse != null) {
                            userInfoView.showPhotoList(Constants.FIRST_PAGE, data.photoListEntityResponse.data);
                        }
                        if (data.verifyInfoEntityResponse != null) {
                            userInfoView.showVerifyInfo(data.verifyInfoEntityResponse.data);
                        }
                        if (data.requireInfoEntityResponse != null) {
                            userInfoView.showRequireInfo(data.requireInfoEntityResponse.data);
                        }
                        if (data.videoIndexListEntityResponse != null ) {
                            userInfoView.showVideoInfo(Constants.FIRST_PAGE,data.videoIndexListEntityResponse.data);
                        }
                    }
                });
    }


    public void getUserInfo(String objId) {
        Observable<ZAResponse<UserInfoEntity>> observable = mService.getUserInfo(objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<UserInfoEntity>>
                (new ZASubscriberListener<ZAResponse<UserInfoEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<UserInfoEntity> response) {
                        if (response.data != null) {
                            userInfoView.showBasicInfo(response.data);
                        }
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        userInfoView.handleError(errorCode,errorMsg);
                    }
                }));
    }

    public void getPhotos(final int page, String objId) {
        Observable<ZAResponse<PhotoListEntity>> observable = mService.getUserPhotos(page, Constants.PAGE_COUNT, objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<PhotoListEntity>>
                (new ZASubscriberListener<ZAResponse<PhotoListEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<PhotoListEntity> response) {
                        if (response.data != null) {
                            userInfoView.showPhotoList(page, response.data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        int currentPage = page;
                        currentPage--;
                        if (currentPage < Constants.FIRST_PAGE) {
                            currentPage = Constants.FIRST_PAGE;
                        }
                        userInfoView.loadMorePhotoFail(currentPage);
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        super.onFail(errorCode, errorMsg);
                        int currentPage = page;
                        currentPage--;
                        if (currentPage < Constants.FIRST_PAGE) {
                            currentPage = Constants.FIRST_PAGE;
                        }
                        userInfoView.loadMorePhotoFail(currentPage);
                    }
                }));
    }

    public void getRequireInfo(String objId) {
        Observable<ZAResponse<RequireInfoEntity>> observable = mService.getRequireConditions(objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<RequireInfoEntity>>
                (new ZASubscriberListener<ZAResponse<RequireInfoEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<RequireInfoEntity> response) {
                        if (response.data != null) {
                            userInfoView.showRequireInfo(response.data);
                        }
                    }

                }));
    }

    public void getVerifyInfo(String objId) {
        Observable<ZAResponse<VerifyInfoEntity>> observable = mService.getVerifyInfo(objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VerifyInfoEntity>>
                (new ZASubscriberListener<ZAResponse<VerifyInfoEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<VerifyInfoEntity> response) {
                        if (response.data != null) {
                            userInfoView.showVerifyInfo(response.data);
                        }
                    }

                }));
    }


    public void addPraise(String objId, BaseSubscriber subscriber) {
        Observable<ZAResponse<LikeEntity>> observable = mService.addPraise(objId);
        HttpMethod.toSubscribe(observable, subscriber);
    }

    public void cancelPraise(long likeId, BaseSubscriber subscriber) {
        RecommendService service = ZARetrofit.getInstance(ZhenaiApplication.getContext()).getRetrofit().create(RecommendService.class);
        Observable<ZAResponse> zaResponseObservable = service.cancelTouchHeart(likeId + "");
        HttpMethod.toSubscribe(zaResponseObservable, subscriber);
    }

    public void getReportData(final Context context, long objectId, int type) {
        Observable<ZAResponse<ReportData>> observable = mService.reportUser(objectId, type);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<ReportData>>(new ZASubscriberListener<ZAResponse<ReportData>>() {
            @Override
            public void onSuccess(ZAResponse<ReportData> response) {
                ToastUtils.toast(context, response.data.msg);
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                ToastUtils.toast(context, errorMsg);
            }
        }));
    }

    public void getVideoInfo(final int page,String userId) {
        Observable<ZAResponse<VideoIndexListEntity>> observable = null;
        if (!TextUtils.isEmpty(userId)) {
            observable = mService.getOtherVideoList(userId, page);
        } else {
            observable = mService.getMyVideoList(page);
        }
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoIndexListEntity>>(new ZASubscriberListener<ZAResponse<VideoIndexListEntity>>() {
            @Override
            public void onSuccess(ZAResponse<VideoIndexListEntity> response) {
                if (response.data != null) {
                    userInfoView.showVideoInfo(page,response.data);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                int currentPage = page;
                currentPage--;
                if (currentPage < Constants.FIRST_PAGE) {
                    currentPage = Constants.FIRST_PAGE;
                }
                userInfoView.loadMoreVideoFail(currentPage);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                int currentPage = page;
                currentPage--;
                if (currentPage < Constants.FIRST_PAGE) {
                    currentPage = Constants.FIRST_PAGE;
                }
                userInfoView.loadMoreVideoFail(currentPage);
            }
        }));
    }


}
