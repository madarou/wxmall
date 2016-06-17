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
public class BasicTestSuite_Test {

	@Test
	public void test() throws InterruptedException {
		String cityLogo = "1000000495269125_0241020.jpg";
		String userHead = "1003234393232034_head.jpg";
		String productSCover = "1000001767118287_putao_s.jpg";
		String productBCover = "1000000442918849_putao.jpg";
		String productDetail2 = "1000000334128290_putao.jpg";
		JSONObject result = null;
		String newsupervisor = "http://localhost:8080/supervisor/new";
		String supervisor = "{\"userName\":\"darou\",\"password\":\"test\"}";
		
		String newcity = "http://localhost:8080/city/new/1";
		String city = "{\"cityName\":\"上海\",\"avatarUrl\":\""+cityLogo+"\"}";
		
		String newarea = "http://localhost:8080/area/new/1";
		String area = "{\"areaName\":\"张江\",\"cityName\":\"上海\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":1}";
		
		String newvendor = "http://localhost:8080/vendor/new/1";
		String vendor = "{\"userName\":\"马靠\",\"areaId\":1,\"cityId\":1,\"cityName\":\"上海\",\"areaName\":\"张江\"}";
		
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
	
		String newuser = "http://localhost:8080/user/new";
		String user = "{\"userName\":\"马买家\",\"openid\":\"3c5d3acb-31b9-480d-944a-516e74390ed8\",\"avatarUrl\":\""+userHead+"\",\"areaId\":1,\"areaName\":\"张江\",\"cityId\":1,\"cityName\":\"上海\",\"point\":20,\"receiveName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海复旦大学\",\"addLabel\":\"家\",\"rank\":\"中级\"}";
		String newaddress = "http://localhost:8080/address/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		String address = "{\"userId\":1,\"userName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海张江\",\"detailAddress\":\"华佗路280弄23号\",\"label\":\"宿舍\",\"isDefault\":\"yes\"}";
		
		String newprodcut11_1 = "http://localhost:8080/product/vnew/1";
		String product11_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut11_2 = "http://localhost:8080/product/vnew/1";
		String product11_2 = "{\"productName\":\"普通葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"10.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"13.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"海南\",\"isShow\":\"yes\",\"showWay\":\"b\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut22_1 = "http://localhost:8080/product/vnew/2";
		String product22_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":2,\"cityId\":2,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String neworderon = "http://localhost:8080/orderOn/new/?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		String orderon = "{\"productIds\":\"2,3\",\"productNames\":\"海南小番茄=3.50=3,广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"17638372821\",\"address\":\"上海复旦大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"14.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		String orderon2 = "{\"productIds\":\"3\",\"productNames\":\"广东蜜桃=4.00=1\",\"receiverName\":\"郭德纲\",\"phoneNumber\":\"12928872821\",\"address\":\"哥伦比亚大学\",\"receiveTime\":\"2016-05-21 15:00-18:00\",\"couponId\":3,\"couponPrice\":\"2.00\",\"totalPrice\":\"22.5\",\"comment\":\"越快越好\",\"cityarea\":\"上海张江\",\"userId\":1,\"areaId\":1,\"cityId\":1}";
		
		String finishorderon2 = "http://localhost:8080/orderOn/vfinish/1";
		String orderon2id = "{\"orderid\":2}";
		
		String newcoupon = "http://localhost:8080/coupon/new/1";
		String coupon = "{\"name\":\"10元代金券\",\"amount\":\"10\",\"point\":20,\"restrict\":10,\"isShow\":\"yes\",\"type\":\"代金券兑换\",\"cityId\":1,\"cityName\":\"上海\",\"comment\":\"新用户欢迎礼券\",\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\"}";
	
		String exchangecoupon = "http://localhost:8080/coupon/exchange/1/1/1?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
		
		
		
		result = HttpUtils.doPostStr(newsupervisor,supervisor);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newcity,city);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newarea,area);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newvendor,vendor);
		assertEquals("200",result.get("msg"));
		
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
		
		result = HttpUtils.doPostStr(newuser,user);
		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newuser,user);
//		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newaddress,address);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
//		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
//		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newprodcut11_2,product11_2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newprodcut22_1,product22_1);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(neworderon,orderon);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(neworderon,orderon2);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(finishorderon2,orderon2id);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newcoupon,coupon);
		assertEquals("200",result.get("msg"));
		
		Thread.sleep(1000);
		result = HttpUtils.doGetObject(exchangecoupon);
		assertEquals("200",result.get("msg"));
	}
	
//	@After
//	public void tearDown() throws Exception {
//		ClearDBUtil.dropDBTables("wxmall");;
//	}
}
