package com.makao.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月5日
 */
@Entity  
@Table 
public class Area implements Serializable {
	private static final long serialVersionUID = 2942031673601219900L;
	private int id;
	private String areaName;
	private int cityId;//所属城市id
	private String cityName;//所属城市名，便于进入商城后左上角直接展示，不用现查
	private String catalogs;//当前area下的商品类别名称集合，在导航栏显示用，以'名称1=名称2'存储，方便直接展示，因为各area的商品单独放一张product表，表内catalog不会重名
	private String productTable;//该area对应的商品列表的名字，以areaName+'_'+cityId+'_product'为名称，超级管理员增加该area记录时建表。用cityId不用名还是防止重复城市名和区域名的情况出现
	private String closed;//当前是否暂停服务，值为'yes'或'no'，关门的可暂不显示，或设为灰色
	//private List<Vendor> vendors;
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=30)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Column
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	@Column(length=30)
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Column(length=50)
	public String getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(String catalogs) {
		this.catalogs = catalogs;
	}
	@Column(length=50)
	public String getProductTable() {
		return productTable;
	}
	public void setProductTable(String productTable) {
		this.productTable = productTable;
	}
	@Column(length=5)
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
//	@OneToMany(mappedBy="area")
//	public List<Vendor> getVendors() {
//		return vendors;
//	}
//	public void setVendors(List<Vendor> vendors) {
//		this.vendors = vendors;
//	}
}
