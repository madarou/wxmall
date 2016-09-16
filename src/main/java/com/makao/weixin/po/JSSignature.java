package com.makao.weixin.po;

import java.util.Date;

public class JSSignature {
	private String ticket;
	private int expires_in;
	private Date time = new Date();//获取的时间
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isExpired(){
		long secends = System.currentTimeMillis() - this.time.getTime();
		return secends > this.expires_in * 1000-600000 ? true : false;//提前1分钟失效
	}
}
