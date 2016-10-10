package com.makao.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.weixin.utils.AccessTokenUtil;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.WeixinConstants;

public class SendMSGThread implements Runnable {
	private static final Logger logger = Logger.getLogger(SendMSGThread.class);
	private String fromUserName;
	private String toUserOpenid;
	private OrderOn order;
	private OrderOff orderOff;
	private int msgType;//1为下单支付成功时的消息，2为开始配送时的提示消息，3为配送完成时的提示消息

	public SendMSGThread(String fromUserName, String toUserOpenid, OrderOn order, int msgType){
		this.fromUserName = fromUserName;
		this.toUserOpenid = toUserOpenid;
		this.order = order;
		this.msgType = msgType;
	}
	
	public SendMSGThread(String toUserOpenid, OrderOn order, int msgType){
		this.fromUserName = WeixinConstants.MSG_FROM_USERNAME;
		this.toUserOpenid = toUserOpenid;
		this.order = order;
		this.msgType = msgType;
	}
	
	public SendMSGThread(String toUserOpenid, OrderOff order, int msgType){
		this.fromUserName = WeixinConstants.MSG_FROM_USERNAME;
		this.toUserOpenid = toUserOpenid;
		this.orderOff = order;
		this.msgType = msgType;
	}
	
	/**
	 * 推送模板消息
	 */
	private void sendMBMsg(){
		String requestUrl = WeixinConstants.MUBAN_MSG_URL.replace("ACCESS_TOKEN", AccessTokenUtil.getAccessToken().getToken());
		switch(this.msgType){
		case 1:
			JSONObject result = HttpUtil.doPostStr(requestUrl, orderCreatedMsg());
			logger.info("send order created mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		case 2:
			result = HttpUtil.doPostStr(requestUrl, orderSendingMsg());
			logger.info("send order sending mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		case 3:
			result = HttpUtil.doPostStr(requestUrl, orderFinishedMsg());
			logger.info("send order finished mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		case 4:
			result = HttpUtil.doPostStr(requestUrl, orderPrepareMsg());
			logger.info("send order prepare mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		case 5:
			result = HttpUtil.doPostStr(requestUrl, orderNeedRefundMsg());
			logger.info("send order need refund mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		case 6:
			result = HttpUtil.doPostStr(requestUrl, orderNeedReturnMsg());
			logger.info("send order need return mb msg to "+toUserOpenid+", result: "+result.getString("errmsg"));
			break;
		default:
			break;
		}
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
		temp.put("value", "您的社享网订单已付款成功，请等待发货！");
		temp.put("color", "#173177");
		data.put("first", temp);
		logger.info("order: "+order.getNumber());
		temp = new JSONObject();
		temp.put("value", order.getTotalPrice()+"元");
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String[] products = this.order.getProductNames().split(",");
		for(String s : products){
			sb.append(s.split("=")[0]).append("*").append(s.split("=")[2]).append(",");
		}
		temp.put("value", sb.substring(0, sb.length()-1));
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", order.getAddress()+","+order.getReceiverName()+","+order.getPhoneNumber());
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		temp.put("value", order.getNumber());
		temp.put("color", "#173177");
		data.put("keyword4", temp);
		
		temp = new JSONObject();
		temp.put("value", "我们会按时为您配送，祝您购物愉快!");
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	/**
	 * @return
	 * 开始配送时推送的模板消息
	 */
	private String orderSendingMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_SENDING_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "您购买的商品即将配送，请注意查收");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String[] products = this.order.getProductNames().split(",");
		for(String s : products){
			sb.append(s.split("=")[0]).append("*").append(s.split("=")[2]).append(",");
		}
		temp.put("value", sb.substring(0, sb.length()-1));
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getNumber());
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", order.getSender());
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		temp.put("value", order.getSenderPhone());
		temp.put("color", "#173177");
		data.put("keyword4", temp);
		
		temp = new JSONObject();
		temp.put("value", "请保持您的联系方式畅通，等待收货！");
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	/**
	 * @return
	 * 配送完成后推送的模板消息
	 */
	private String orderFinishedMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_FINISHED_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "您好，您的商品已经配送成功！");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getNumber());
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String[] products = this.order.getProductNames().split(",");
		for(String s : products){
			sb.append(s.split("=")[0]).append("*").append(s.split("=")[2]).append(",");
		}
		temp.put("value", sb.substring(0, sb.length()-1));
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getTotalPrice()+"元");
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		temp.put("value", df.format(new Date()));
		temp.put("color", "#173177");
		data.put("keyword4", temp);
		
		temp = new JSONObject();
		temp.put("value", "如有任何疑问，请拨打："+order.getSenderPhone());
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	/**
	 * @return
	 * 订单配送时间要到时，提前推送的准备订单的模板消息给配送员
	 */
	private String orderPrepareMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_PREPARE_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "您好，近期您有一笔即将配送的订单：");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getNumber());
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getReceiveTime());
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", this.order.getAddress()+" "+this.order.getReceiverName()+" "+order.getPhoneNumber());
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		temp.put("value", "请及时登录后台系统，准备订单！");
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	/**
	 * @return
	 * 退货或付钱后取消的订单需要退款，通知超级管理员
	 */
	private String orderNeedRefundMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_REFUND_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "您好，近期您有一笔需要退款的订单：");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		temp.put("value", this.orderOff.getTotalPrice());
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String[] products = this.orderOff.getProductNames().split(",");
		for(String s : products){
			sb.append(s.split("=")[0]).append("*").append(s.split("=")[2]).append(",");
		}
		temp.put("value", sb.substring(0, sb.length()-1));
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", this.orderOff.getNumber());
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		temp.put("value", "请及时登录后台系统和商户系统，完成退款！");
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	/**
	 * @return
	 * 用户发起退货时，vendor收到退货通知
	 */
	private String orderNeedReturnMsg(){
		JSONObject msg = new JSONObject();
		msg.put("touser", this.toUserOpenid);
		msg.put("template_id", WeixinConstants.ORDER_RETURN_MBMSG);
		msg.put("url", "www.baidu.com");
		JSONObject data = new JSONObject();
		JSONObject temp = new JSONObject();
		temp.put("value", "您好，近期您有一笔需要退货的订单：");
		temp.put("color", "#173177");
		data.put("first", temp);
		
		temp = new JSONObject();
		temp.put("value", this.orderOff.getNumber());
		temp.put("color", "#173177");
		data.put("keyword1", temp);
		
		temp = new JSONObject();
		temp.put("value", this.orderOff.getReceiverName());
		temp.put("color", "#173177");
		data.put("keyword2", temp);
		
		temp = new JSONObject();
		temp.put("value", this.orderOff.getPhoneNumber());
		temp.put("color", "#173177");
		data.put("keyword3", temp);
		
		temp = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String[] products = this.orderOff.getProductNames().split(",");
		for(String s : products){
			sb.append(s.split("=")[0]).append("*").append(s.split("=")[2]).append(",");
		}
		temp.put("value", sb.substring(0, sb.length()-1));
		temp.put("color", "#173177");
		data.put("keyword4", temp);
		
		temp = new JSONObject();
		temp.put("value", "请及时联系客户咨询退货原因，并登录后台系统完成退货！");
		temp.put("color", "#173177");
		data.put("remark", temp);
		
		
		msg.put("data", data);
		
		return msg.toString();
	}
	
	@Override
	public void run() {
		sendMBMsg();	
	}

}
