package com.makao.test.main;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Test;

import com.makao.test.utils.ClearDBUtil;
import com.makao.test.utils.HttpUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 * 1.清空数据库
	2.增加supervisor
	3.增加city
	4.增加area(会建对应的product表)
	5.增加vendor
	6.增加product
 */
public class NewSupervisorTest {

	@Test
	public void test() {
		JSONObject result = null;
		String newsupervisor = "http://localhost:8080/supervisor/new";
		String supervisor = "{\"userName\":\"darou\",\"password\":\"test\"}";
		
		result = HttpUtils.doPostStr(newsupervisor,supervisor);
		assertEquals("增加supervisor成功",result.get("msg"));
	}
	
//	@After
//	public void tearDown() throws Exception {
//		ClearDBUtil.dropDBTables("wxmall");;
//	}
}
