package com.makao.entity;

import java.sql.Date;

/**
 * @description: 所有礼品类型，因为礼品和优惠券不同，兑换了优惠券没使用会存on表，使用存off表，而兑换了礼品就表示使用了
 * @author makao
 * @date 2016年5月6日
 */
public class Gift {
	private int id;
	private String name;//如'20元话费充值'
	private String coverSUrl;//小图地址
	private String coverBUrl;//大图地址
	private int point;//消耗的积分
	private String comment;//简单介绍
	private Date from;//上线日期
	private Date to;//截止兑换日期		如果后台统一控制上下线的话，可以不要这个日期
	private int inventory;//礼品总量	
	private int remain;//剩余数量			如果为0，则可以灰掉，或者自动下线。这块要服务器缓存
	private String type;//类型				值为'礼品兑换'，与Coupon中的'代金券兑换对应'
	private int areaId;//区域Id			属于哪个区域的gift
	private String areaName;//区域名
	private int cityId;//属于哪个city的gift，主要用于确定Gift_cityId中的cityId来确定表
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
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public int getRemain() {
		return remain;
	}
	public void setRemain(int remain) {
		this.remain = remain;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}
