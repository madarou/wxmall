package com.makao.weixin.po;

/**
 * @author makao
 * @date 2016年4月17日
 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
 */
public class AccessToken {
	private String token;
	private int expiresIn;
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
	
}
