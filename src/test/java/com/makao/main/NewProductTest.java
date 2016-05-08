package com.makao.main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.makao.utils.ClearDBUtil;
import com.makao.utils.ShellExecutor;

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
public class NewProductTest {

	@Test
	public void test() {
		String newSupervisor = "curl l -H \"Content-type: application/json\" -X POST -d '{\"userName\":\"darou\",\"password\":\"test\"}' 'http://localhost:8080/wxmall/supervisor/new'";
		ShellExecutor.callShell(newSupervisor);
	}
	
//	@After
//	public void tearDown() throws Exception {
//		ClearDBUtil cd = new ClearDBUtil();
//		cd.clearDB();
//	}
}
