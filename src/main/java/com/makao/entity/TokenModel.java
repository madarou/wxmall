package com.makao.entity;

import java.io.Serializable;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月18日
 */
public class TokenModel implements Serializable{
	/**
	 * 序列化就是将一个对象转换为二进制的数据流。这样就可以进行传输，或者保存到文件中。如果一个类的对象要想实现序列化，就必须实现serializable接口
	 */
	private static final long serialVersionUID = 3829468821442657937L;
	private int userId;
	private String role;//用户角色，supervisor(s)/vendor(v)/user(u)
	private String openid;//微信openid
	private String token;//生成的唯一标识
	
	public TokenModel(int userid, String role, String token){
		this.userId = userid;
		this.role = role;
		this.openid = "";
		this.token = token;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
