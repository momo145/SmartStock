package com.app.sinkinchan.smartstock.mina.constant;

public class MinaConstant {


	public static final String SERVER_IP_ADDRESS = "192.168.1.102";
	public static final int SERVER_POST = 1235;
	public static final String MINA_MESSAGE_KEY = "mina message key";
	// key
	/**
	 * 用户添加或删除书本时用到的传值key
	 */
	public static final String USER_BOOK_KEY = "user book key";
	/**
	 * 用户key
	 */
	public static final String USER_KEY = "user key";
	/**
	 * 连接到服务器
	 */
	public static final int CONNECT = 1000;
	/**
	 * 用户登录
	 */
	public static final int USER_LOGIN = 1001;
	/**
	 * 列出公共社区
	 */
	public static final int LIST_DISCUSSION = 1002;
	/**
	 * 列出帖子
	 */
	public static final int LIST_POST = 1003;
	/**
	 * 列出回复
	 */
	public static final int LIST_REPLYS = 1004;
	/**
	 * 发表一个帖子
	 */
	public static final int PUT_UP_A_POST = 1005;
	/**
	 * 发表一个回复
	 */
	public static final int PUT_UP_A_REPLY = 1006;
	/**
	 * 点赞或者踩
	 */
	public static final int UPDATE_A_LIKE=1007;
	/**
	 * 一键登陆
	 */
	public static final int ONE_KEY_LOGIN=1008;
	/**
	 * 登录失败,昵称重复
	 */
	public static final int LOGIN_ERROR_NAME_REPEAT = 1009;
	/**
	 * 一键注册
	 */
	public static final int ONE_KEY_REGISTER = 1010;
	/**
	 * 用户添加一本书
	 */
	public static final int USER_ADD_BOOK = 1011;
	/**
	 * 用户删除一本书
	 */
	public static final int USER_DELETE_BOOK = 1012;
	/**
	 * 推荐书本
	 */
	public static final int RECOMMEND_THE_BOOK = 1013;
	/**
	 * 同步用户书本
	 */
	public static final int SYN_USERBOOK = 1014;
	/**
	 * 获取版本号,是否有更新
	 */
	public static final int GET_VERSION = 1015;
}
