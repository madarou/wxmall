package com.makao.weixin.utils;
/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
public class WeixinConstants {
	//诸葛王朗订阅号
	//public static final String APPID = "wx454e62d4ba842c70";
	//public static final String APPSECRET = "ea2fa885539a667e302a92bb86b54e55";
	//优格服务号
	//public static final String APPID = "wxe41d04b7ffb9ea2a";
	//public static final String APPSECRET = "cc515bcc081a76e3b85bbb370c6c7b3c";
	public static final String APPID = "wx930aa432f4dbfa73";
	public static final String APPSECRET = "8ccbf092b608a84683375eecff30c9ca";
	
	public static final String MCHID = "1495743582";//商户号，支付用
	public static final String PAY_KEY = "123456789meinirensheng0123456789";//生成支付签名时所用的商户key
	//统一下单地址，POST
	public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//测试用的订阅号，因为个人订阅号有权限限制，如不能请求图片上传的URL等，只能用测试号代替
	//public static final String APPID_TEST = "wx454e62d4ba842c70";
	//public static final String APPSECRET_TEST = "ea2fa885539a667e302a92bb86b54e55";
	//获取access_token，get
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//JSSDK获取jsapi_ticket的url，get
	public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	//自定义菜单创建，post
	public static final String CREATEMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	//网页授权登录第一步获取code的url
	public static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=1#wechat_redirect";
	//网页授权登录第二步获取access_token和openid的url
	public static final String AUTH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//网页授权登录第三步获取用户基本信息
	public static final String AUTH_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//消息发送方的username，即微信公众号的微信号
	public static final String MSG_FROM_USERNAME = "mnrshop";
	//模板消息发送地址
	public static final String MUBAN_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	//支付成功后推送的模板消息id
	public static final String ORDER_CREATED_MBMSG = "D6jB60OkkbNENosVy3Nv5PqPvJP9Q0tW1ArQdCbVcMU";
	//开始配送订单时的模板消息id
	public static final String ORDER_SENDING_MBMSG = "N39-YCQJJh_oZSdvisN8NCNGeTaOc3dbfR14fBxeQrw";
	//订单完成时的模板消息id
	public static final String ORDER_FINISHED_MBMSG = "C9PI6SI7-YrGWYO9MQabU5PEgh5OBrX-wJlWJLuwU_Q";
	//订单配送时间要到时，提前推送的准备订单的模板消息给配送员
	public static final String ORDER_PREPARE_MBMSG = "Npb82DM2akmPgnTRKu8OBr8UX35KF6a5cFzlR8vXTLU";
	//订单退货完成或付款后取消后，推送给超级管理员退款的模板消息
	public static final String ORDER_REFUND_MBMSG = "CQVylLWcOeOWVnK8EAngsdgVp6tMabfUqQ5iTYAMAqc";
	//用户申请退货后，通知vendor有人要退货的模板消息
	public static final String ORDER_RETURN_MBMSG = "pTAeze_zz3l8mhsazBuXskxo0n6H6PZsor_tSr3PWDQ";
	//生成带参数的二维码
	public static final String QR_CODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	
}
