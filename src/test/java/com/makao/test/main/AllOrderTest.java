package com.makao.test.main;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.makao.test.utils.HttpUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月14日
 * 依赖于city，因为需要cityId确定表
 */
public class AllOrderTest {

	@Test
	public void test() {
		JSONObject result = null;
		String newsupervisor = "http://localhost:8080/supervisor/new";
		String supervisor = "{\"userName\":\"darou\",\"password\":\"test\"}";
		
		String newcity = "http://localhost:8080/city/new/1";
		String city = "{\"cityName\":\"上海\"}";
		
		String newarea = "http://localhost:8080/area/new/1";
		String area = "{\"areaName\":\"张江\",\"cityName\":\"上海\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":1}";
		
		String newvendor = "http://localhost:8080/vendor/new/1";
		String vendor = "{\"userName\":\"马靠\",\"areaId\":1,\"cityId\":1,\"cityName\":\"上海\",\"areaName\":\"张江\"}";
		
		result = HttpUtils.doPostStr(newsupervisor,supervisor);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newcity,city);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newarea,area);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newvendor,vendor);
		assertEquals("200",result.get("msg"));
		
		String neworderon = "http://localhost:8080/orderOn/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		//排队订单
		String orderon_queue = "{\"productIds\":\"5\",\"productNames\":\"新疆葡萄=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		result = HttpUtils.doPostStr(neworderon,orderon_queue);
		assertEquals("200",result.get("msg"));
		
		//要被配送api配送的订单
		String orderon = "{\"productIds\":\"2,3\",\"productNames\":\"海南小番茄=3.50=3,广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"17638372821\",\"address\":\"上海复旦大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"14.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		String distributeorderon1 = "http://localhost:8080/orderOn/vdistribute/1";
		String orderon1id = "{\"orderid\":1}";
		result = HttpUtils.doPostStr(neworderon,orderon);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(distributeorderon1,orderon1id);
		assertEquals("200",result.get("msg"));
		
		//要被配送完成API finish的订单
		String orderon2 = "{\"productIds\":\"3\",\"productNames\":\"广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		String finishorderon2 = "http://localhost:8080/orderOn/vfinish/1";
		String orderon2id = "{\"orderid\":2}";
		result = HttpUtils.doPostStr(neworderon,orderon2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(finishorderon2,orderon2id);
		assertEquals("200",result.get("msg"));
		
		//要被收货api确认收货的订单
		String orderon3 = "{\"productIds\":\"4\",\"productNames\":\"海南西瓜=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		String confirmorderon3 = "http://localhost:8080/orderOn/confirm/1/4/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		result = HttpUtils.doPostStr(neworderon,orderon3);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doGetObject(confirmorderon3);
		assertEquals("200",result.get("msg"));
		
		
		//要先被确认收货，然后被申请退货的订单
		String orderon4 = "{\"productIds\":\"5\",\"productNames\":\"美国冬瓜=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		String confirmorderon4 = "http://localhost:8080/orderOn/confirm/1/5/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		result = HttpUtils.doPostStr(neworderon,orderon4);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doGetObject(confirmorderon4);
		assertEquals("200",result.get("msg"));
		//在orderOff表里它的id是2，因为前面confirm了一个order进入到了off表里，状态为已收货
		String returnorderoff1 = "http://localhost:8080/orderOff/return/1/2/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		result = HttpUtils.doGetObject(returnorderoff1);
		assertEquals("200",result.get("msg"));
		
	}

}
