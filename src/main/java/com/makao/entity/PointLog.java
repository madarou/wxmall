package com.makao.entity;

import java.sql.Date;

/**
 * @author makao
 * 积分记录
 */
public class PointLog {
	private int id;
	private String name;//如'每日登陆''下单消费'
	private int point;//获得的积分
	private Date getDate;//获得积分的时间
	private String comment;//备注，便于不同获得积分的原因填写不同的备注
	private int cityId;
	private int userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Date getGetDate() {
		return getDate;
	}
	public void setGetDate(Date getDate) {
		this.getDate = getDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
