package com.makao.entity;
/**
 * @description: 对应Coupon_cityId表，我的积分中心->积分兑换里显示的优惠券类型，是静态优惠券，即没有被兑换的，兑换之后会在下面Coupon_cityId_on中生成
 * @author makao
 * @date 2016年5月5日
 */
public class Coupon {
	private int id;
	private String name;//名称				如'10元代金券'
	private String amount;//优惠券面值
	private String coverSUrl;//小图地址	
	private String coverBUrl;//大图地址	
	private int point;//对应消耗的积分	
	private int restrict;//使用限制			至少消费到xx元才能使用，在购物车时根据此限制加载可用券，如'消费满10元'
	private String comment;//简单说明			如'新用户欢迎礼券','圣诞节专属礼券'等
	private String cityName;//所属的城市			便于后台加载所有券时显示
	private int cityId;
	private String isShow;//是否在前台显示		值为'yes'或'no'，类似上架或下架
	private String type;//值为'代金券兑换'，与Gift中的'礼品兑换'对应
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
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
