package com.makao.entity;

import java.sql.Timestamp;

/**
 * @description: 订单，对应Order_cityId_on表
 * @author makao
 * @date 2016年5月5日
 */
public class OrderOn {
	private int id;
	private String number;//订单编号			根据年月日和时分秒以及一个随机生成的4为数组成
	private String productIds;//所有购买的商品id	以'商品id1=当时单价=数量,商品id2=当时单价=数量'连接
	private String productNames;//所有商品的名称		以'商品名称1=当时单价=数量,商品名称2=当时单价=数量'连接，所有商品名称中不能含有=
	private Timestamp orderTime;//下单时间
	private String receiverName;//收货人姓名
	private String phoneNumber;//收货人手机号
	private String address;//收货地址，只是用户填写的部分，后面有cityarea了
	private String payType;//支付方式			现默认只有'微信安全支付'
	private String receiveType;//收货方式			现默认只有'送货上门'
	private String receiveTime;//收货时间段			如'2016-04-05 9时-12时'
	private int couponId;//使用的优惠券Id
	private String couponPrice;//优惠券面值			方便在订单里直接显示而不用再多一次查询
	private String totalPrice;//总价				订单总价
	private String freight;//运费				暂时不用，为以后拓展保留
	private String comment;//用户备注
	//订单状态			
	//'未确认'(商家未确认)，'买家已取消'(被买家取消)，'卖家已取消'(被卖家取消)'已确认'(商家已确认订单)，'已配送'(以完成送货)
	//'已收货'(卖家已收货，没有手动收货的在距离配送一段时间内自动变成已收货)
	private String status;
	private String cityarea;//城市+区域名		方便后台直接显示
	private int userId;//对应User表里的Id
	private int areaId;//Area表里的Id，区域卖家登录时的查询条件
	private int cityId;//用于在访问数据库时，直接确定数据库表名Order_cityId_on，直接从前端传来用
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getProductIds() {
		return productIds;
	}
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	public String getProductNames() {
		return productNames;
	}
	public void setProductNames(String productNames) {
		this.productNames = productNames;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCityarea() {
		return cityarea;
	}
	public void setCityarea(String cityarea) {
		this.cityarea = cityarea;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}
