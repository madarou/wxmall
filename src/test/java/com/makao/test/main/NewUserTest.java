package com.makao.test.main;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.makao.test.utils.HttpUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月18日
 */
public class NewUserTest {

	@Test
	public void test() {
		JSONObject result = null;
		String newuser = "http://localhost:8080/user/new";
		String user = "{\"userName\":\"马靠\",\"openid\":\"fefewr13e2d23e23dwq\",\"areaId\":1,\"areaName\":\"张江\",\"cityId\":1,\"cityName\":\"上海\",\"point\":20,\"receiveName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海复旦大学\",\"addLabel\":\"家\",\"rank\":\"中级\"}";
		
		result = HttpUtils.doPostStr(newuser,user);
		assertEquals("200",result.get("msg"));
	}

}
