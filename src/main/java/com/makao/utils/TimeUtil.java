package com.makao.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	       
	       String aa= "a";
	       System.out.println(aa.split(",")[0]);
	       
	       Date dt1 = df.parse("2016-09-17 15:40:04.095");
	       System.out.println(dt1);
	       
	       Timestamp t = new Timestamp((System.currentTimeMillis()));
	       System.out.println(t.toString());
	       Date dt2 = df.parse(t.toString());
	       System.out.println(dt2);
	       
	       List<String> l = null;
	       for(String s:l){
	    	   System.out.println(s);
	       }
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
