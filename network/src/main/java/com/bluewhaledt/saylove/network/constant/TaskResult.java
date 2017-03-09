package com.bluewhaledt.saylove.network.constant;


/**
 * 任务执行结果.
 *
 * @param <T> 任务返回结果的数据类型
 * @author DengZhaoyong
 * @version 1.0.0
 * @date 2012-9-12
 */
public class TaskResult<T> {

    /**
     * 任务执行成功
     */
    public static final int OK = 1;
    /**
     * 已到达末页
     */
    public static final int ENDPAGE = 11;
    /**
     * 任务执行失败
     */
    public static final int FAILED = -1;
    /**
     * 任务执行成功, 但返回结果为空
     */
    public static final int EMPTY = -2;
    /**
     * 任务执行抛出错误
     */
    public static final int ERROR = -3;
    /**
     * 任务异常
     */
    public static final int EXCEPTION = -11;
    /**
     * 需要重新登录（修改密码后）的标识
     */
    public static final String STATUS_RE_LOGIN = "-00004";

    /**
     * 用户未登陆
     */
    public static final String STATUS_NO_LOGIN="-00003";

    /**
     * -22>>不符合对方的择偶年龄要求
     */
    public static final int EXCEPTION_22 = -22;
    /**
     * -23>>邮件QA测试地区
     */
    public static final int EXCEPTION_23 = -23;
    /**
     * -24>>邮件QA测试地区，界面刷新
     */
    public static final int EXCEPTION_24 = -24;
    /**
     * -25>>体验版测试-体验版升级正式版补差价
     */
    public static final int EXCEPTION_25 = -25;

    /**
     * 用户已存在
     */
    public static final int PHONE_EXIST = -51001;

    /**
     * 任务ID
     */
    private int mTaskKey = -1;

    /**
     * 列表总页数
     */
    private int mTotalPage = 0;

    /**
     * 任务执行完成后的结果码
     */
    private int mCode = 0;

    /**
     * 返回的数据
     */
    private T mData = null;

    /**
     * 服务器返回的提示信息(当返回值为 'OK' or 'FAILED', 从该字段取信息)
     */
    private String mMsg = "";

    /**
     * 服务器返回的错误信息(当返回值为 'ERROR', 从该字段取信息)
     */
    private String mErrorMsg = "";

    /**
     * 当任务执行抛出异常时, 此属性不为空
     */
    private Exception mException = null;

    /**
     * 当前任务的标志
     */
    private String taskFlag = "";

    /**
     * @return 任务执行完成后的结果码
     */
    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    /**
     * @return 任务执行完成后列表总页数
     */
    public int getTotalPage() {
        return mTotalPage;
    }

    /**
     * 旧版本推荐页才返回,新版本统一在data结构体内
     *
     * @param totalPage
     */
    public void setTotalPage(int totalPage) {
        this.mTotalPage = totalPage;
    }

    /**
     * @return 任务返回的数据
     */
    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }

    public String getTaskFlag() {
        return taskFlag;
    }

    public void setTaskFlag(String flag) {
        this.taskFlag = flag;
    }

    /**
     * @return 当任务执行抛出异常时, 该方法返回结果不为空.
     */
    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        this.mException = exception;
    }

//	public String getMsg() {
//		if (!NetworkDeviceUtils.isNetworkConnected(ZhenaiApplication.getContext())) {
//			return TextUtils.isEmpty(mMsg) ? ZhenaiApplication.getContext()
//					.getString(R.string.no_network_connected) : mMsg;
//		}
//		return mMsg;
//
//	}
//
//	public void setMsg(String msg) {
//		this.mMsg = msg;
//	}
//
//	public String getErrorMsg() {
//		if (!NetworkStringUtils.isEmpty(mErrorMsg)) {
//			return mErrorMsg;
//		}
//		return mException == null ? "" : mException.getMessage();
//	}

    public void setErrorMsg(String errorMsg) {
        this.mErrorMsg = errorMsg;
    }

    public int getTaskKey() {
        return mTaskKey;
    }

    public void setTaskKey(int taskKey) {
        this.mTaskKey = taskKey;
    }

    public boolean isSuccess() {
        return mCode == OK;
    }

}
