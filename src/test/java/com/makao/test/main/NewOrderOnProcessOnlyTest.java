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
public class NewOrderOnProcessOnlyTest {

	@Test
	public void test() throws InterruptedException {
		
		JSONObject result = null;
		
		String neworder = "http://localhost:8080/orderOn/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		String order = "{\"productIds\":\"2=3.50=3,3=4.00=1\",\"productNames\":\"海南小番茄=3.50=3,广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"17638372821\",\"address\":\"上海复旦大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"14.5\",\"comment\":\"越快越好\",\"status\":\"待处理\",\"cityarea\":\"常州-某某区\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		
		String order2 = "{\"productIds\":\"3=4.00=1\",\"productNames\":\"广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"status\":\"配送中\",\"cityarea\":\"常州-某某区\",\"userId\":1,\"areaId\":1,\"cityId\":1}";

		result = HttpUtils.doPostStr(neworder,order);
		assertEquals("200",result.get("msg"));
		Thread.sleep(3000);
		result = HttpUtils.doPostStr(neworder,order2);
		assertEquals("200",result.get("msg"));
	}

}
