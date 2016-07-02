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
public class AllTest {

	@Test
	public void test() {
		JSONObject result = null;
		String cityLogo = "10000002343_标准城市logo.jpg";
		String userHead = "1003234393232034_head.jpg";
		String productSCover = "1000000879418434_正方形标准商品图.jpg";
		String productBCover = "1000000493029796_长方形标准商品图.jpg";
		String productDetail2 = "1000001532212560_标准商品详情.jpg";
		
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
		
		String newcity2 = "http://localhost:8080/city/new/1";
		String city2 = "{\"cityName\":\"北京\",\"avatarUrl\":\""+cityLogo+"\"}";
		
		String newarea2 = "http://localhost:8080/area/new/1";
		String area2 = "{\"areaName\":\"海淀\",\"cityName\":\"北京\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":2}";
		
		String newvendor2 = "http://localhost:8080/vendor/new/1";
		String vendor2 = "{\"userName\":\"老马\",\"areaId\":2,\"cityId\":2,\"cityName\":\"北京\",\"areaName\":\"海淀\"}";
		
		String newarea3 = "http://localhost:8080/area/new/1";
		String area3 = "{\"areaName\":\"宣武\",\"cityName\":\"北京\",\"catalogs\":\"水果=0,食材=1,零食=2\",\"cityId\":2}";
		
		String newvendor3 = "http://localhost:8080/vendor/new/1";
		String vendor3 = "{\"userName\":\"老王\",\"areaId\":3,\"cityId\":2,\"cityName\":\"北京\",\"areaName\":\"宣武\"}";
	
		result = HttpUtils.doPostStr(newcity2,city2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newarea2,area2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newvendor2,vendor2);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newarea3,area3);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newvendor3,vendor3);
		assertEquals("200",result.get("msg"));
		
		String newuser = "http://localhost:8080/user/new";
		String user = "{\"userName\":\"马买家\",\"openid\":\"3c5d3acb-31b9-480d-944a-516e74390ed8\",\"avatarUrl\":\""+userHead+"\",\"areaId\":1,\"areaName\":\"张江\",\"cityId\":1,\"cityName\":\"上海\",\"point\":20,\"receiveName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海复旦大学\",\"addLabel\":\"家\",\"rank\":\"中级\"}";
		String newaddress = "http://localhost:8080/address/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		String address = "{\"userId\":1,\"userName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海张江\",\"detailAddress\":\"华佗路280弄23号\",\"label\":\"宿舍\",\"isDefault\":\"yes\"}";
		result = HttpUtils.doPostStr(newuser,user);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newaddress,address);
		assertEquals("200",result.get("msg"));
		
		String neworderon = "http://localhost:8080/orderOn/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		//排队订单
		String orderon_queue = "{\"productIds\":\"5\",\"productNames\":\"新疆葡萄=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		result = HttpUtils.doPostStr(neworderon,orderon_queue);
		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(neworderon,orderon_queue);
//		assertEquals("200",result.get("msg"));
		
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
