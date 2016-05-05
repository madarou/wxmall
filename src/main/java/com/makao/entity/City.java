package com.makao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月5日
 */
public class City implements Serializable {
	private static final long serialVersionUID = 3703334101577769038L;
	private int id;
	private String cityName;
	private String avatarUrl;
	private String areas;//以'area1id=area1名,area2id=area2名'存储，方便在确定城市后直接展示
	@Id
    @GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=30)
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Column(length=50)
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	@Column(length=255)
	public String getAreas() {
		return areas;
	}
	public void setAreas(String areas) {
		this.areas = areas;
	}
}
