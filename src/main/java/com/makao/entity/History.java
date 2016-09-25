package com.makao.entity;

import java.sql.Date;

/**
 * @author makao
 *代金券兑换历史
 */
public class History {
	private int id;
	private String name;//名称				如'10元代金券'
	private String amount;//优惠券面值
	private int point;//对应消耗的积分
	private Date from;//起始有效期			领券的日期
	private Date to;//截止有效期
	private int restrict;//使用限制			至少消费到xx元才能使用，在购物车时根据此限制加载可用券，如'消费满10元'
	private int cityId;//
	private int userId;//优惠券拥有者
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
