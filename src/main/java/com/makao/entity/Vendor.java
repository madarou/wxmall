package com.makao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月5日
 */
@Entity  
@Table 
public class Vendor implements Serializable {
	private static final long serialVersionUID = 2882450560807218843L;
	private int id;
	private String userName;
	private String password;
	private String avatarUrl;
	private int areaId;
	private int cityId;
	private String	cityArea;//城市名-区域名，方便直接使用
	private String lastIp;//上次登录ip
	private Timestamp lastTime;//上次登录时间
	private String isLock;//账号是否冻结
	//private Area area;
	
	@Id
    @GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=30,unique=true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(length=35)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(length=50)
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	@Column
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	@Column
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	@Column(length=30)
	public String getCityArea() {
		return cityArea;
	}
	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}
	@Column(length=30)
	public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	@Column
	public Timestamp getLastTime() {
		return lastTime;
	}
	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}
	@Column(length=5)
	public String getIsLock() {
		return isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}
//	@ManyToOne
//	@JoinColumn(name="areaId")
//	public Area getArea() {
//		return area;
//	}
//	public void setArea(Area area) {
//		this.area = area;
//	}
}
