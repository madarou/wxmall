package com.makao.weixin.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月9日
 * 微信支付使用的signature
 */
public class SignatureUtil {
	public static String createSign(SortedMap<Object,Object> parameters,String key){ 
	    StringBuffer sb = new StringBuffer(); 
	    Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序） 
	    Iterator it = es.iterator(); 
	    while(it.hasNext()) { 
	      Map.Entry entry = (Map.Entry)it.next(); 
	      String k = (String)entry.getKey(); 
	      Object v = entry.getValue(); 
	      if(null != v && !"".equals(v)  
	          && !"sign".equals(k) && !"key".equals(k)) { 
	        sb.append(k + "=" + v + "&"); 
	      } 
	    } 
	    sb.append("key=" + key);
	    String sign = MD5Util.MD5Encode(sb.toString()).toUpperCase(); 
	    return sign; 
	  }
	    
	  public static String getNonceStr() {
	    Random random = new Random();
	    return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)));
	  }
	  
	  public static String getTimeStamp() {
	    return String.valueOf(System.currentTimeMillis() / 1000);
	  }
}
