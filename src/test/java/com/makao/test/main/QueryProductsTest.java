package com.makao.test.main;

import static org.junit.Assert.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.makao.test.utils.HttpUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月9日
 */
public class QueryProductsTest {

	@Test
	public void test() {
		JSONArray result = null;
		String getproducts_bycityareaId = "http://localhost:8080/wxmall/product/query/1/1";
		
		result = HttpUtils.doGetArray(getproducts_bycityareaId);
		System.out.println(result);
		
		JSONObject res = null;
		//一次是产品Id，cityId和areaId，后两者是为了确定哪张product表
		String getproduct_byId = "http://localhost:8080/wxmall/product/1/1/1";
		res = HttpUtils.doGetObject(getproduct_byId);
		System.out.println(res);
	}

}
