package com.makao.entity;
/**
 * @description: Point_cityId(静态积分，即记录每种操作分别对应多少的积分)
 * @author makao
 * @date 2016年5月6日
 */
public class Point {
	private int id;
	private String source;//积分来源			比如'新用户关注''每日登陆''下单赠送'，还有'兑换礼品'或'兑换优惠券'消耗，不能重复，可做主键	
	private int point;//本次来源对应积分	可能为负，即消耗的积分
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
}
