package com.makao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity  
@Table 
public class User implements Serializable{
	private static final long serialVersionUID = 4237640736638993145L; 
	
	private Integer id;
    private String userName;
    private String openid;
    private String password;
    private String avatarUrl;//头像地址
    private int areaId;//用户当前所在区域
    private String areaName;
    private int cityId;//用户所在区域
    private String cityName;
    private int point;//当前总积分
    private String phoneNumber;//电话
    private String address;//地址
    
    @Id
    @GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(updatable=false)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
