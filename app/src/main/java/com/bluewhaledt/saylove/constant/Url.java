package com.bluewhaledt.saylove.constant;

/**
 * @author hechunshan
 * @date 2016/11/14
 */
public class Url {

    /**
     * 获取数据字典
     */
    public static final String SYS_DATA_DICTIONARY = "sys/data-syscode-dictionary.do";

    /**
     * 查询版本信息
     */
    public static final String SYS_VERSION = "sys/version.do";

    /**
     * 登录用户获取banner广告--邮件列表banner
     */
    public static final String GET_EMAIL_BANNERS = "profile/getBanners.do";

    /**
     * 推荐
     */
    public static final String RECOMMEND_LISTVIEW = "nearby/getRecommand.do";


    /**
     * 创建订单接口
     */
    public static final String CREATE_ORDER = "payment/createCommonOrder.do";

    /**
     * 获取支付类型
     */
    public static final String GET_PAY_TYPE = "payment/getPayType.do";

    /**
     * 提交设备信息
     */
    public static final String COMMIT_DEVICE_INFO = "sys/insert-install-record.do";

    /**
     * 获取欢迎页面背景图片
     */
    public static final String GET_WELCOME_IMAGES = "payment/getImages.do";

    /**
     * 登录
     */
    public static final String ACCOUNT_LOGIN = "login/login.do";
    /**
     * 推荐-推荐 新版的首页实现这个推荐的
     * http://10.10.10.12:2800/nearby/getnewrecommand.do
     */
    public static final String NEW_RECOMMED = "nearby/getnewrecommand.do";
    /**
     * 动态密码登陆
     */
    public static final String DYNAMIC_LOGIN_AUTO_LOGIN = "login/dynamicLogin.do";
    /**
     * 新版的择偶条件
     * /profile/getObjectData.do
     */
    public static final String SELECT_FRIEND = "profile/getObjectData.do ";
    /**
     * 推荐的bannerurl
     * /profile/getObjectData.do
     */
    public static final String RECOMEND_BANNER = "profile/getDisplayAds.do";
    /**
     * 发现页bannner和icon数据url
     */
    public static final String FOUND_BANNERANDICON = "found/getFoundDataNew.do";

    /**
     * 获取"消息"、"我"页面相关的未读数和导航栏通知
     */
    public static final String QUERY_UNREAD_COUNT = "message/qryUnreadCount.do";

    /**
     * 邮件列表
     */
    public static final String LIST_MAIL = "mail/listMail.do";

    /**
     * 我的个人资料预览
     */
    public static final String MY_PROFILE = "profile/getProfileData.do";
    /**
     * 发现页面的话题照片的地址
     */
    public static final String FOUND_HOT_PIC = "photo/getTopicsIntroduce.do";

    /**
     * 用户登录助手
     */
    public static final String LOGIN_HELPER = "login/loginHelper.do";

    /**
     * 删除邮件
     */
    public static final String EMAIL_DELETE_ALL = "mail/deleteAllMailSendBySender.do";

    /**
     * 获取邮件表情列表
     */
    public static final String GET_EMAIL_EXPRESSION_LIST = "mail/getMailExpression.do";

    /**
     * 获取邮件可用表情列表
     */
    public static final String GET_EMAIL_EXPRESSION_SETS = "mail/getMailExpressionSets.do";

    /**
     * 获取用户标签
     */
    public static final String SEARCH_GET_LABEL_CATEGORIES = "search/getSearchLabel.do";
    /**
     * 获取当前用户择偶条件(对应的下标值)
     */
    public static final String USER_GET_USER_MATE = "profile/getObjectData.do";

    /**
     * 修改当前用户择偶条件
     */
    public static final String USER_SET_USER_MATE = "profile/modifyObjInfo.do";

    /**
     * 搜索
     */
    public static final String SEARCH_SEARCH = "search/search.do";


    /**
     * 获取注册token
     */
    public static final String GET_REGISTER_TOKEN = "register/getRegTk.do";

    /**
     * 验证手机号码是否已生成ID
     */
    public static final String ACCOUNT_CHECK_VALIDATE_PHONE = "register/checkValidatePhone.do";

    /**
     * 注册
     */
    public static final String ACCOUNT_REGISTER = "register/register.do";

    /**
     * 发送手机验证码
     */
    public static final String SEND_MOBILE_VERIFY_CODE = "register/getMobileVerifyCode.do";

    /**
     * 获取语音验证码
     */
    public static final String GET_VOICE_VERIFY_CODE = "register/getVoiceCode.do";

    /**
     * 验证注册的手机号码
     */
    public static final String ACCOUNT_VERIFY_MOBILE = "register/verifyMobile.do";

    /**
     * QQ帐号登录
     */
    public static final String ACCOUNT_LOGIN_BY_QQ = "login/qqLogin.do";

    /**
     * 绑定QQ帐号
     */
    public static final String ACCOUNT_BIND_QQ = "login/qqBind.do";

    /**
     * 获取登录图片验证码
     */
    public static final String GET_IMAGE_CODE = "login/getImgCode.do";

    /**
     * 手机登录
     */
    public static final String PHONE_LOGIN = "login/login.do";

    /**
     * 手机号初步注册
     */
    public static final String PHONE_REGISTER = "";

    /**
     * 手机验证码
     */
    public static final String PHONE_VERIFY = "register/getVerfyCode.do";

    /**
     * 正式注册用户
     */
    public static final String REGISTER_USER = "register/commitRegister.do";

    /**
     * 获取身份认证token
     */
    public static final String GET_VERIFY_TOKEN = "verify/getVerifyToken.do";

    /**
     * 获取自己或者他人资料
     */
    public static final String GET_INFO = "user/homepage.do";

    /**
     * 获取择偶条件
     */
    public static final String GET_MATE_REQUIRE_CONDITIONS = "user/qryPartnerConditions.do";

    /**
     * 更新或添加择偶条件
     */
    public static final String EDIT_MATE_REQUIRE_CONDITIONS = "user/editMates.do";

    /**
     * 修改个人资料
     */
    public static final String EDIT_MY_INFO = "user/edit.do";

    /**
     * 修改密码前获取短信验证码
     */
    public static final String PWD_VERIFY_CODE = "pwd/getVerfyCode.do";
    /**
     * 重置密码
     */
    public static final String RESET_PWD = "pwd/modifyPwd.do";

    /**
     *
     * 芝麻认证 上传省份证号码
     */
    public static final String ZHIMA_POST_NAME = "zmverify/getVerifyToken.do";
    /**
     * 芝麻认证 的回调参数
     */
    public static final String ZHIMA_CALL_BACK = "zmverify/zmVerifyCallBlack.do";

    /**
     * 获取腾讯云上传图片签名
     */
    public static final String GET_QCLOUD_SIGN = "photo/getPeriodEffectiveSign.do";

    /**
     * 保存照片名称（保存头像、普通照片，各种证件照片）
     */
    public static final String SAVE_PICTURE = "verify/batchSave.do";

    /**
     * 查询普通照相册
     */
    public static final String GET_PHOTOS = "userphoto/qryPhotos.do";


    /**
     * 点赞
     */
    public static final String TOUCH_HEART = "interaction/touchHeart.do";
    /**
     * 取消点赞
     */
    public static final String CANCEL_TOUCH_HEART = "interaction/cancelLike.do";

    /**
     * 推荐的数据
     */
    public static final String GET_RECOMMEND = "recommend/commomRecommend.do";


    /**
     * 获取IM聊天账号
     */
    public static final String GET_IM_ACCOUNT = "chat/getImAccount.do";

    /**
     * 获取聊天用户信息
     */
    public static final String GET_CHAT_USER_INFO = "chat/getChatUserInfo.do";

    /**
     * 聊天列表页(获取心动列表与访客列表是否显示红点)
     */
    public static final String GET_MESSAGE_LIST_UN_READ_COUNT = "chat/chatListPage.do";

    /**
     * 获取认证信息
     */
    public static final String GET_VERIFY_INFO = "verify/qryVerifys.do";

    /**
     * 上传头像和昵称
     */
    public static final String COMMIT_AVATOR_NAME = "register/commitAvatorNickName.do";

    /**
     * 删除照片
     */
    public static final String DELETE_PHOTO = "userphoto/updateById.do";

    /**
     * 获取会员购买页信息
     */
    public static final String PRODUCT_LIST = "payment/productList.do";

    /**
     * 支付-获取认证产品信息
     */
    public static final String GET_VERIFY_PRODUCT = "payment/getVerifyProduct.do";

    /**
     * 获取随机昵称
     */
    public static final String GET_RANDOM_NAME = "register/getRandomNickName.do";

    /**
     * 对我心动列表
     */
    public static final String GET_PASSIVE_LIKE = "interaction/listPassiveLikes.do";

    /**
     * 我心动的列表
     */
    public static final String GET_MY_HEART_HEAT_RECORD = "interaction/listActiveLikes.do";

    /**
     * 看过我的列表
     */
    public static final String GET_VISIT_ME_LIST = "interaction/listPassiveViews.do";

    /**
     * 我看过的列表
     */
    public static final String GET_MY_VISIT_TO_LIST = "interaction/listActiveViews.do";

    /**
     * 检查是否可聊天
     */
    public static final String CHECK_CAN_CHAT = "chat/check.do";

       /**
     * 检查是否可聊天
     */
    public static final String TOURIST_RECOMMEDN= "recommend/touristRecommend.do";



    /**
     * 获取阿里云视频上传签名
     */
    public static final String GET_ALI_VIDEO_SIGN="vod/getAliSign.do";

    /**
     * 保存点播视频
     */
    public static final String SAVE_VIDEO_FILE_INFO="vod/addVideo.do";

    /**
     * 获取视频列表
     */
    public static final String GET_VIDEO_INDEX_LIST="vod/getNewVideo.do";

    /**
     * 举报
     */
    public static final String REPORT_USER = "system/report.do";

    /**
     * 获取视频话题
     */
    public static final String GET_RANDOM_TOPIC="topic/getRandomTopicList.do";

    /**
     * 获取视频相似话题
     */
    public static final String GET_SIMILAR_VIDEO="vod/getSimilarVideo.do";

    /**
     * 获取视频播放信息
     */
    public static final String GET_VIDEO_INFO="vod/getVideoPlayInfo.do";

    /**
     * 获取其他人的视频
     */
    public static final String GET_OTHER_VIDEO_LIST="vod/getSomeonesVideoList.do";

    /**
     * 获取自己的视频
     */
    public static final String GET_MY_VIDEO_LIST="vod/getOwnVideoList.do";

    /**
     * 删除视频
     */
    public static final String DELETE_VIDEO="vod/deleteVideo.do";


    /**
     * 增加视频观看数
     */
    public static final String ADD_VIEW_NUM="vod/touchVod.do";

    /**
     * 视频点赞
     */
    public static final String VIDEO_ADD_PRAISE="interaction/likeVideo.do";

    /**
     * 是否可以上传视频
     */
    public static final String CHECK_CAN_UPLOAD_VIDEO ="vod/askForUploadVideo.do";


    /**
     * 获取免费聊天相关
     */
    public static final String GET_FREE_COUNT = "chat/getFreeCount.do";


    public static final String LOGOUT = "login/logout.do";

    /**
     * 获取认证详细信息
     */
    public static final String QUERY_VERIFY_DETAIL = "verify/qryVerifyDetail.do";
    /**
     * 付费的提示的文案接口
     */
    public static final String GET_VERIFYTIPS_CONTENT = "register/getVerifyTips.do";

    /**
     * 日志上报接口
     */
    public static final String EVENT_LOG = "http://cdnlog.zhenai.com/log/";


}
