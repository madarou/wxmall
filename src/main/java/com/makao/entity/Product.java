package com.makao.entity;
/**
 * @description: TODO
 * @author makao
 * @date 2016年5月5日
 */
public class Product {
	private int id;
	private String productName;
	private String catalog;//商品所属的类别,如水果、食材
	private String showWay="s";//商品展示方式,默认值为's'(小图),值为'b'时为大图展示，空值也小图
	private String price;//实际卖价
	private String standard;//商品规格,如'一份250克'
	private String marketPrice;//对比的超市价格
	private String label;//商品标签			在商品左上角显示的如'新用户专享''绿色食品''小产区'等
	private String coverSUrl;//封面小图地址		小图也用于购物车里展示，所以必须有小图
	private String coverBUrl;//封面大图地址		大图封面也用于商品详细页面顶部展示，所以不管哪种展示，必须有大图
	private int inventory;//库存量
	private int sequence;//排序顺序			整形值，越大在商品列表里越靠前
	private String status;//货源状态			值可为空、'库存紧张'、'暂时缺货'
	private String description;//商品简述			详细页面，有的商品在名称下一行有简介,如'顺滑口感，齿颊留香'
	private String origin;//原产地
	private int salesVolume;//销量	
	private int likes;//点赞数
	private String subdetailUrl;//有些详细介绍前还有一个图片
	private String detailUrl;//详细介绍图片地址
	private String isShow="yes";//否在商品列表显示	值为'yes'或'no'，与status有区别，是完全不在列表中显示
	private int areaId;//所属的网点名，方便直接查找确定其数据库表
	private int cityId;//同上
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getShowWay() {
		return showWay;
	}
	public void setShowWay(String showWay) {
		this.showWay = showWay;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public int getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(int salesVolume) {
		this.salesVolume = salesVolume;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
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
	public String getSubdetailUrl() {
		return subdetailUrl;
	}
	public void setSubdetailUrl(String subdetailUrl) {
		this.subdetailUrl = subdetailUrl;
	}
}
