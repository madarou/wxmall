package com.makao.entity;

import java.sql.Date;

/**
 * @description: 已兑换的礼品，只用显示一些简单信息：礼品名称、消耗积分、兑换日期、当前状态(后台人员是否已处理)
 * @author makao
 * @date 2016年5月6日
 */
public class GiftChanged {
	private int id;
	private String name;//名称，如'20元话费充值'
	private int giftId;//引用Gift_cityId表中的主键，前台展示应该不会用到
	private String coverSUrl;//小图地址
	private String coverBUrl;//大图地址
	private int point;//消耗的积分
	private String comment;//简单介绍
	private Date date;//兑换日期
	private String status;//状态				'未完成'(用户已兑换，等待后台人员操作)、'已完成'(后台人员操作完成)
	private int areaId;//区域Id			属于哪个区域的gift
	private String areaName;//区域名
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
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public String getCoverSUrl() {
		return coverSUrl;
	}
	public void setCoverSUrl(String coverSUrl) {
		this.coverSUrl = coverSUrl;
	}
	public String getCoverBUrl() {
		return coverBUrl;
	}
	public void setCoverBUrl(String coverBUrl) {
		this.coverBUrl = coverBUrl;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
