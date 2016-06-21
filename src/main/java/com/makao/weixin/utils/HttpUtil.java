package com.makao.weixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 */
public class HttpUtil {
	/**
	 * @param url
	 * @return
	 * 发送get请求，返回json对象
	 */
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
	
	/**
	 * @param url
	 * @return
	 * 发送get请求，返回json列表
	 */
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
	
	/**
	 * @param url
	 * @param outStr
	 * @return
	 * 发送Post请求，返回一个json对象
	 */
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
	
	/**
	 * @param url
	 * @param outStr
	 * @return
	 * 已xml格式的String的参数发送Post请求，返回String结果
	 */
	public static String doPostXml(String url, String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String res = null;
		//设置post参数
		StringEntity en = new StringEntity(outStr,"UTF-8");
		en.setContentType("text/xml");
		httpPost.setEntity(en);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				res = EntityUtils.toString(entity,"UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * @param url
	 * @param outStr
	 * @return
	 * 已xml格式的String发送Post请求，返回xml格式的结果，解析结果为map中的键值对
	 */
	public static Map<String, String> doPostXmlAndParse(String url, String outStr) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		Map<String, String> res = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		//设置post参数
		StringEntity en = new StringEntity(outStr,"UTF-8");
		en.setContentType("text/xml");
		httpPost.setEntity(en);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				InputStream ins = entity.getContent();
				Document doc;
				try {
					doc = reader.read(ins);
				} catch (DocumentException e1) {
					e1.printStackTrace();
					return null;
				}
				Element root = doc.getRootElement();
				List<Element> list = root.elements();
				for(Element e : list){
					res.put(e.getName(), e.getText());
				}
				ins.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static Map<String, String> parseXmlRequest(HttpServletRequest request) throws IOException, DocumentException {
	    // 解析结存储HashMap
	    Map<String, String> map = new HashMap<String, String>();
	    InputStream inputStream = request.getInputStream();
	    // 读取输入流
	    SAXReader reader = new SAXReader();
	    Document document = reader.read(inputStream);
	    // xml根元素
	    Element root = document.getRootElement();
	    // 根元素所节点
	    List<Element> elementList = root.elements();
	    // 遍历所节点
	    for (Element e : elementList)
	        map.put(e.getName(), e.getText());
	    // 释放资源
	    inputStream.close();
	    inputStream = null;
	    return map;
	}
}
