package com.bluewhaledt.saylove.photo.impl;

import com.bluewhaledt.saylove.photo.uploader.ISingleUploaderListener;

import java.util.Map;

/**
 * Created by rade.chan on 2016/12/3.
 */

public interface IBasePhotoAction {

    void uploadPicture(String avatarUrl, int type);

    void uploadPicture(Map<String,String> params);

    void uploadPicture(String avatarUrl, int type, ISingleUploaderListener listener);

    void uploadPicture(Map<String,String> params, ISingleUploaderListener listener);

    void goCameraPhoto(String photoName);

    void goFrontCameraPhoto(String photoName);

    void goAlbum();



}
