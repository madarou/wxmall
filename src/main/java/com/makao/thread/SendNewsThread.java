package com.makao.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.makao.weixin.utils.AccessTokenUtil;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.WeixinConstants;

public class SendNewsThread implements Runnable {
	private static final Logger logger = Logger.getLogger(SendNewsThread.class);
	private String fromUserName;
	private String toUserOpenid;

	public SendNewsThread(String fromUserName, String toUserOpenid){
		this.fromUserName = fromUserName;
		this.toUserOpenid = toUserOpenid;
	}
	
	public SendNewsThread(String toUserOpenid){
		this.fromUserName = WeixinConstants.MSG_FROM_USERNAME;
		this.toUserOpenid = toUserOpenid;
	}
	
	/**
	 * 用户支付成功后给他推送模板消息
	 */
	private void sendOrderCreatedMessage(){
		String requestUrl = WeixinConstants.MUBAN_MSG_URL.replace("ACCESS_TOKEN", AccessTokenUtil.resetToken().getToken());
		JSONObject result = HttpUtil.doPostStr(requestUrl, orderCreatedMsg());
		logger.info("send order created mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
	}
	
	/**
	 * @return
	 * 用户下单且支付成功后推送的模板现象
	 */
	private String orderCreatedMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_CREATED_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "恭喜你购买成功！");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		temp.put("value", "上海张江");
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		temp.put("value", "微信支付");
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", "0.01元");
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		temp.put("value", "0分");
		temp.put("color", "#173177");
		data.put("keyword4", temp);
		
		temp = new JSONObject();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		temp.put("value", df.format(new Date()));
		temp.put("color", "#173177");
		data.put("keyword5", temp);
		
		temp = new JSONObject();
		temp.put("value", "欢迎下次再来！");
		temp.put("color", "#173177");
		data.put("keyword5", temp);
		
		msg.put("data", data);
		
		return msg.toString();
	}
	@Override
	public void run() {
		sendOrderCreatedMessage();
	}

}
