package com.makao.weixin.utils;

import net.sf.json.JSONObject;

import com.makao.weixin.po.AccessToken;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
public class AccessTokenUtil {
	private static AccessToken token = getAccessToken();
	
	public static AccessToken getToken(){
		return token;
	}
	public static void resetToken(){
		token = getAccessToken();
	}
	/**
	 * @return
	 * 获取access_token
	 */
	private static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		
		//修改成实际的URL
		String url = WeixinConstants.ACCESS_TOKEN_URL.replace("APPID", WeixinConstants.APPID).replace("APPSECRET", WeixinConstants.APPSECRET);
		JSONObject jsonObject = HttpUtil.doGetObject(url);
		if(jsonObject != null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
}
