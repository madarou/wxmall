package com.makao.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.makao.dao.impl.OrderOnDaoImpl;
import com.makao.entity.OrderState;

/**
 * @author makao
 * 自定义时间，主要用于收货时间与当前时间的比较
 *
 */
public class TimeUtil {
	private static final Logger logger = Logger.getLogger(TimeUtil.class);
	public static void main(String args[]) throws ParseException {
	       int i= compare_date("12-11 09:21", "12-11 09:59");
	       System.out.println("i=="+i);
	       DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	       System.out.println(df.format(new Date()));
	       
	       System.out.println(minitesDiff("2016-x-10 14:21"));
	       System.out.println("2012-12-11 09:21".substring(0, 16));
	       
	       System.out.println("10.00".split("\\.")[0]+"10.00".split("\\.")[1]);
	       System.out.println(OrderState.CANCELED.getCode());
	       JSONObject jb = new JSONObject();
	       jb.put("productIds", new String[] {"1","2"});jb.put("nums", new String[] {"3","1"});jb.put("receiverName", "郭德纲");jb.put("phoneNumber", "17638372821");jb.put("address", "上海复旦大学");jb.put("receiveTime", "2016-05-21 15:00-18:00");jb.put("couponId", 0);jb.put("cityarea", "上海张江");jb.put("userId", 1);jb.put("areaId", 1);jb.put("cityId", 1);jb.put("status", OrderState.QUEUE.getCode()+"");
	       System.out.println(jb.toString());
	    }
	
	/**
	 * @param timestring
	 * @return
	 * 获取目标时间与当前时间的分钟差
	 */
	public static int minitesDiff(String timestring){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date dt1 = df.parse(timestring);
			return (int) ((dt1.getTime()-(new Date()).getTime())/60000);
		} catch (ParseException e) {
			logger.error(e.getMessage(),e);
		}
		return Integer.MAX_VALUE;
	}

	    public static int compare_date(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("MM-dd hh:mm");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            System.out.println((dt1.getTime()-dt2.getTime())/60000);
	            if (dt1.getTime() > dt2.getTime()) {
	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
}
