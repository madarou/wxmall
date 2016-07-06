package com.makao.entity;

/**
 * @description: TODO
 * @author makao
 * @date 2016年7月6日
 */
public class SmallOrder{
	private String productIds;//所有购买的商品id	以'id1,id2,id3'连接
	private String nums;//商品数量
	private String receiverName;//收货人姓名
	private String phoneNumber;//收货人手机号
	private String address;//收货地址，只是用户填写的部分，后面有cityarea了
	private String receiveTime;//收货时间段			如'2016-04-05 9时-12时'
	private int couponId;//使用的优惠券Id
	private String cityarea;//城市+区域名		方便后台直接显示
	private int userId;//对应User表里的Id
	private int areaId;//Area表里的Id，区域卖家登录时的查询条件
	private int cityId;//用于在访问数据库时，直接确定数据库表名Order_cityId_on，直接从前端传来用
	public String getProductIds() {
		return productIds;
	}
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
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