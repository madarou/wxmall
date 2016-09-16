package com.makao.weixin.po;

import java.util.Date;

/**
 * @author makao
 * @date 2016年4月17日
 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
 */
public class AccessToken {
	private String token;
	private int expiresIn;
	private Date time = new Date();//获取的时间
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public boolean isExpired(){
		long secends = System.currentTimeMillis() - this.time.getTime();
		return secends > this.expiresIn * 1000-600000 ? true : false;//提前1分钟失效
	}
	
}
