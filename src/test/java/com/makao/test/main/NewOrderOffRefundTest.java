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
public class NewOrderOffRefundTest {

	@Test
	public void test() throws InterruptedException {
		
		JSONObject result = null;
		
		String newsupervisor = "http://localhost:8080/supervisor/new";
		String supervisor = "{\"userName\":\"darou\",\"password\":\"test\"}";
		
		String newuser = "http://localhost:8080/user/new";
		String user = "{\"userName\":\"马靠\",\"openid\":\"fefewr13e2d23e23dwq\",\"areaId\":1,\"areaName\":\"张江\",\"cityId\":1,\"cityName\":\"上海\",\"point\":20,\"receiveName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海复旦大学\",\"addLabel\":\"家\",\"rank\":\"中级\"}";
		
		String newcity = "http://localhost:8080/city/new/1";
		String city = "{\"cityName\":\"上海\"}";
		
		String newarea = "http://localhost:8080/area/new/1";
		String area = "{\"areaName\":\"张江\",\"cityName\":\"上海\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":1}";
		
		String newvendor = "http://localhost:8080/vendor/new/1";
		String vendor = "{\"userName\":\"马靠\",\"areaId\":1,\"cityId\":1,\"cityName\":\"上海\",\"areaName\":\"张江\"}";
	

		String neworder = "http://localhost:8080/orderOff/new";
		String order = "{\"productIds\":\"2=3.50=3,3=4.00=1\",\"productNames\":\"海南小番茄=3.50=3,广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"17638372821\",\"address\":\"上海复旦大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"14.5\",\"comment\":\"越快越好\",\"finalStatus\":\"已取消\",\"refundStatus\":\"待退款\",\"cityarea\":\"常州-某某区\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		
		String order2 = "{\"productIds\":\"3=4.00=1\",\"productNames\":\"广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"finalStatus\":\"已退货\",\"refundStatus\":\"待退款\",\"cityarea\":\"常州-某某区\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		
		result = HttpUtils.doPostStr(newsupervisor,supervisor);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newuser,user);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newcity,city);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newarea,area);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newvendor,vendor);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(neworder,order);
		assertEquals("200",result.get("msg"));
		Thread.sleep(3000);
		result = HttpUtils.doPostStr(neworder,order2);
		assertEquals("200",result.get("msg"));
	}

}
