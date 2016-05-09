package com.makao.test.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 */
public class HttpUtils {
	public static JSONObject doGetObject(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			//执行http get请求，并获得response
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONArray doGetArray(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONArray jsonArray = null;
		try {
			//执行http get请求，并获得response
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonArray = JSONArray.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
	
	public static JSONObject doPostStr(String url, String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		//设置post参数
		StringEntity en = new StringEntity(outStr,"UTF-8");
		en.setContentType("application/json");
		httpPost.setEntity(en);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result); 
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	public static void main(String[] args){
		//JSONObject jsonObject = HttpUtils.doGetStr("curl l -H \"Content-type: application/json\" -X POST -d '{\"userName\":\"darou\",\"password\":\"test\"}' 'http://localhost:8080/wxmall/supervisor/new'");
		JSONObject jsonObject = HttpUtils.doPostStr("http://localhost:8080/wxmall/supervisor/new","{\"userName\":\"darou\",\"password\":\"test\"}");
		System.out.println(jsonObject);
	}
}
