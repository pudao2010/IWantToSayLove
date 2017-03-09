package com.bluewhaledt.saylove.ui.video.view;

import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;

/**
 * Created by rade.chan on 2016/12/16.
 */

public interface IVideoPlayView {
    void gotoPayPage();
    void praiseSuccess(LikeEntity entity);
    void praiseFail(String errorCode,String errorMsg);
    void cancelLikeSuccess();
    void cancelLikeFail();
    void deleteVideoSuccess();
    void getVideoInfoSuccess(VideoIndexEntity indexEntity);
    void getVideoFail(String msg);


}
