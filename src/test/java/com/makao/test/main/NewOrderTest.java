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
public class NewOrderTest {

	@Test
	public void test() {
		JSONObject result = null;
		String neworder = "http://localhost:8080/orderOn/new";
		String order = "{\"productIds\":\"2=3.50=3,3=4.00=1\",\"productNames\":\"海南小番茄=3.50=3,广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"17638372821\",\"address\":\"上海复旦大学\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"14.5\",\"comment\":\"越快越好\",\"status\":\"未确认\",\"cityarea\":\"常州-某某区\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		
		result = HttpUtils.doPostStr(neworder,order);
		assertEquals("200",result.get("msg"));
	}

}
