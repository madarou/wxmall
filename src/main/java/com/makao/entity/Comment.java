package com.makao.entity;

import java.sql.Date;

/**
 * @description: 商品评论
 * @author makao
 * @date 2016年5月5日
 */
public class Comment {
	private int id;
	private String userName;
	private String usesrImgUrl;
	private Date date;
	private String likes;
	private String content;
	private int productId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUsesrImgUrl() {
		return usesrImgUrl;
	}
	public void setUsesrImgUrl(String usesrImgUrl) {
		this.usesrImgUrl = usesrImgUrl;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
