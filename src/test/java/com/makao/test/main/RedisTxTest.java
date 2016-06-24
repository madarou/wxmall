package com.makao.test.main;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月23日
 */
public class RedisTxTest {
	@Test
	public void test() {
		Task thread1=new Task("T1");
		Task thread2=new Task("T2");
		thread1.start();
		thread2.start();
	}
	public static void main(String[] args){
		Task thread1=new Task("T1");
		Task thread2=new Task("T2");
		Task thread3=new Task("T3");
		//Task thread4=new Task("T4");
		thread1.start();
		thread2.start();
		thread3.start();
		//thread4.start();
	}
}
class Task extends Thread{
	private String name;
    public Task(String name) {
       this.name=name;
    }
	@Override
	public void run() {
		 for (int i = 0; i < 5; i++) {
	            System.out.println(name + "运行  :  " + i);
	            doGet("http://localhost:8080/test/redistx");
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
		
	}
	
	public static void doGet(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			//执行http get请求，并获得response
			HttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				String result = EntityUtils.toString(entity,"UTF-8");
//				jsonObject = JSONObject.fromObject(result);
//			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return jsonObject;
	}
}