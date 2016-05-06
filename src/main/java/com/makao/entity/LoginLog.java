package com.makao.entity;
/**
 * @description: LoginLog_date(记录每日的用户登陆信息，只记录不重复的用户，不记录一个用户同一天的重复登陆，这块可能要多线程)
 * @author makao
 * @date 2016年5月6日
 */
public class LoginLog {
	private int id;
	private int userId;//登录用户的主键
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
