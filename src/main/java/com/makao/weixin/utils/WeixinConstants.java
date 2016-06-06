package com.makao.weixin.utils;
/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
public class WeixinConstants {
	public static final String APPID = "wx454e62d4ba842c70";
	public static final String APPSECRET = "ea2fa885539a667e302a92bb86b54e55";
	
	//测试用的订阅号，因为个人订阅号有权限限制，如不能请求图片上传的URL等，只能用测试号代替
	//public static final String APPID_TEST = "wx454e62d4ba842c70";
	//public static final String APPSECRET_TEST = "ea2fa885539a667e302a92bb86b54e55";
	//获取access_token，get
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//自定义菜单创建，post
	public static final String CREATEMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		
}
