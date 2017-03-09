package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * 视频封面类
 * Created by rade.chan on 2016/11/28.
 */

public class VideoIndexEntity extends BaseEntity {

    public String cover;                         //封面
    public String createTime;                   //创建时间
    public int likeId=-1;                        // 点赞的id  不为-1 则表示已经赞过
    public int likeNum;                         // 点赞次数
    public String mp4hd;                        //高清
    public String mp4ld;                        //低清
    public String mp4sd;                        //标清
    public String topicContent;                 //话题内容
    public int topicId;                         //话题id
    public String updateTime;                    //更新时间
    public int userId;                          //用户id
    public VideoUserInfo userInfo;              //详细用户资料
    public String videoId;                      //视频id
    public int viewNum;                         //观看次数
    public String message;
    public int state;                       // 1待审核 2正常  3审核未通过 4已删除

    public int shield;                          //1-没有遮罩，2-遮罩1（以后可能会有其他遮罩）


    public static class VideoUserInfo extends BaseEntity{
        public String avatar;               //头像
        public boolean isCar;               //车辆是否认证
        public boolean isDegree;            //学历是否认证
        public boolean isHouse;             //房产是否认证
        public boolean isVIP;               //是否vip
        public boolean isZM;                //是否芝麻认证
        public String nickName;              //昵称
        public int sex;                     //性别
        public String imAccId;              //im session ID

        @Override
        public String[] uniqueKey() {
            return new String[0];
        }
    }



    @Override
    public String[] uniqueKey() {
        return new String[]{videoId};
    }
}
