package com.bluewhaledt.saylove.ui.info.view;

import com.bluewhaledt.saylove.ui.info.entity.PhotoListEntity;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;

/**
 * Created by rade.chan on 2016/11/28.
 */

public interface IUserInfoView {
    void showBasicInfo(UserInfoEntity userInfoEntity);

    void handleError(String errorCode,String errorMsg);

    void showPhotoList(int page,PhotoListEntity photoListEntity);

    void loadMorePhotoFail(int page);

    void showRequireInfo(RequireInfoEntity requireInfoEntity);

    void showVerifyInfo(VerifyInfoEntity verifyInfoEntity);

    void showVideoInfo(int page,VideoIndexListEntity videoIndexListEntity);

    void loadMoreVideoFail(int page);
}
