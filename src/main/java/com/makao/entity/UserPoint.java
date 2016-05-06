package com.makao.entity;

import java.sql.Date;

/**
 * @description: UserPoint_cityId(实际积分记录，记录每个用户的积分获取和消耗历史)
 * @author makao
 * @date 2016年5月6日
 */
public class UserPoint {
	private int id;
	private String source;//积分来源
	private int point;//获取或消耗的积分	是根据source在Point_cityId中查寻到的对应积分
	private int userId;//积分获取或消耗者
	private Date date;//获取或使用时间		对于每日登陆的积分，要先检查这个用户这个日期是否有登陆 (从下面的LoginLog_date中查)，登陆过了就不再记录
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
