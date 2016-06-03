package com.makao.entity;

import java.sql.Date;

/**
 * @description: Coupon_cityId_on(未失效的优惠券)
 * @author makao
 * @date 2016年5月6日
 */
public class CouponOn {
	private int id;
	private String name;//名称				如'10元代金券'
	private String amount;//优惠券面值
	private String coverSUrl;//小图地址
	private String coverBUrl;//大图地址
	private int point;//对应消耗的积分
	private Date from;//起始有效期			领券的日期
	private Date to;//截止有效期
	private int restrict;//使用限制			至少消费到xx元才能使用，在购物车时根据此限制加载可用券，如'消费满10元'
	private String comment;//简单说明			如'新用户欢迎礼券','圣诞节专属礼券'等	
	private String cityName;//所属的城市			便于后台加载所有券时显示
	private int cityId;//
	private int userId;//优惠券拥有者
	private String type;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public int getRestrict() {
		return restrict;
	}
	public void setRestrict(int restrict) {
		this.restrict = restrict;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
