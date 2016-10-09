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
	只增加基本的supervisor、vendor、city、area和一些商品，不调用用户接口，因为开放了token验证
 */
public class BasicTestSuite_Test3 {

	@Test
	public void test() throws InterruptedException {
		String cityLogo = "10000002343_标准城市logo.jpg";
		String userHead = "1003234393232034_head.jpg";
		String productSCover = "1000000879418434_正方形标准商品图.jpg";
		String productBCover = "1000000493029796_长方形标准商品图.jpg";
		String productDetail2 = "1000001532212560_标准商品详情.jpg";
		
		String couponSCover = "1000001679510380_标准代金券小图.jpg";
		String couponBCover = "1000001679510380_标准代金券大图.jpg";
		
		JSONObject result = null;
		String newsupervisor = "http://localhost:8080/supervisor/new";
		String supervisor = "{\"userName\":\"sxadmin\",\"password\":\"1234abcd\"}";
		String supervisor2 = "{\"userName\":\"sxsuper\",\"password\":\"1234abcd\"}";
		String supervisor3 = "{\"userName\":\"sxgovernor\",\"password\":\"1234abcd\"}";
		String supervisor4 = "{\"userName\":\"sxrobot\",\"password\":\"1234abcd\"}";
		String supervisor5= "{\"userName\":\"sxassistant\",\"password\":\"1234abcd\"}";
		
		String newcity = "http://localhost:8080/city/new/1";
		String city = "{\"cityName\":\"上海\",\"avatarUrl\":\""+cityLogo+"\"}";
		
		String newarea = "http://localhost:8080/area/new/1";
		String area = "{\"areaName\":\"张江\",\"cityName\":\"上海\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":1,\"phoneNumber\":\"13937263847\"}";
		
		String newvendor = "http://localhost:8080/vendor/new/1";
		String vendor = "{\"userName\":\"马靠\",\"password\":\"admin\",\"areaId\":1,\"cityId\":1,\"cityName\":\"上海\",\"areaName\":\"张江\"}";
		
		String newcity2 = "http://localhost:8080/city/new/1";
		String city2 = "{\"cityName\":\"江苏\",\"avatarUrl\":\""+cityLogo+"\"}";
		
		String newarea2 = "http://localhost:8080/area/new/1";
		String area2 = "{\"areaName\":\"常州恐龙园\",\"cityName\":\"江苏\",\"catalogs\":\"水果=0,食材=1,零食=2,省钱=3\",\"cityId\":2,\"phoneNumber\":\"13937263847\"}";
		
		String newvendor2 = "http://localhost:8080/vendor/new/1";
		String vendor2 = "{\"userName\":\"老马\",\"password\":\"admin\",\"areaId\":2,\"cityId\":2,\"cityName\":\"江苏\",\"areaName\":\"常州恐龙园\"}";
		
		String newarea3 = "http://localhost:8080/area/new/1";
		String area3 = "{\"areaName\":\"昆山工业园\",\"cityName\":\"江苏\",\"catalogs\":\"水果=0,食材=1,零食=2\",\"cityId\":2,\"phoneNumber\":\"13937263847\"}";
		
		String newvendor3 = "http://localhost:8080/vendor/new/1";
		String vendor3 = "{\"userName\":\"老王\",\"password\":\"admin\",\"areaId\":3,\"cityId\":2,\"cityName\":\"江苏\",\"areaName\":\"昆山工业园\"}";
	
		String newprodcut11_1 = "http://localhost:8080/product/vnew/1";
		String product11_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.99\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"0\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":0,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut11_2 = "http://localhost:8080/product/vnew/1";
		String product11_2 = "{\"productName\":\"普通葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"10.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"13.00\",\"inventory\":12,\"sequence\":1,\"status\":\"1\",\"origin\":\"海南\",\"isShow\":\"yes\",\"showWay\":\"b\",\"salesVolume\":0,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut22_1 = "http://localhost:8080/product/vnew/2";
		String product22_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"2\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":0,\"likes\":3972,\"areaId\":2,\"cityId\":2,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
		
		String newcoupon = "http://localhost:8080/coupon/new/1";
		String coupon = "{\"name\":\"10元代金券\",\"amount\":\"10\",\"point\":20,\"restrict\":10,\"isShow\":\"yes\",\"type\":\"代金券兑换\",\"cityId\":0,\"cityName\":\"1#_#上海\",\"comment\":\"新用户欢迎礼券\",\"coverSUrl\":\""+couponSCover+"\",\"coverBUrl\":\""+couponBCover+"\"}";
		String newcoupon2 = "http://localhost:8080/coupon/new/1";
		String coupon2 = "{\"name\":\"12元代金券\",\"amount\":\"12\",\"point\":20,\"restrict\":15,\"isShow\":\"yes\",\"type\":\"代金券兑换\",\"cityId\":0,\"cityName\":\"1#_#上海\",\"comment\":\"老用户回馈礼券\",\"coverSUrl\":\""+couponSCover+"\",\"coverBUrl\":\""+couponBCover+"\"}";
		
		result = HttpUtils.doPostStr(newsupervisor,supervisor);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newsupervisor,supervisor2);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newsupervisor,supervisor3);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newsupervisor,supervisor4);
		assertEquals("增加supervisor成功",result.get("msg"));
		result = HttpUtils.doPostStr(newsupervisor,supervisor5);
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
		
//		上线所有区域
		String openarea = "http://localhost:8080/area/sopen/1/?token=xxxxx";
		String oarea1 = "{\"areaId\":1}";
		String oarea2 = "{\"areaId\":2}";
		String oarea3 = "{\"areaId\":3}";
		result = HttpUtils.doPostStr(openarea,oarea1);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(openarea,oarea2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(openarea,oarea3);
		assertEquals("200",result.get("msg"));
//		上线所有区域
		
		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newprodcut11_2,product11_2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newprodcut22_1,product22_1);
		assertEquals("200",result.get("msg"));
		
		//**********新增优惠券***********
		result = HttpUtils.doPostStr(newcoupon,coupon);
		assertEquals("200",result.get("msg"));
		
		result = HttpUtils.doPostStr(newcoupon2,coupon2);
		assertEquals("200",result.get("msg"));
		//**********新增优惠券***********
		
		
//		//要兑换并且被使用的优惠券
//		String newcoupon2 = "http://localhost:8080/coupon/new/1";
//		String coupon2 = "{\"name\":\"20元代金券\",\"amount\":\"20\",\"point\":20,\"restrict\":10,\"isShow\":\"yes\",\"type\":\"代金券兑换\",\"cityId\":1,\"cityName\":\"上海\",\"comment\":\"老用户回馈礼券\",\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\"}";
//		result = HttpUtils.doPostStr(newcoupon2,coupon2);
//		assertEquals("200",result.get("msg"));
//		String exchangecoupon2 = "http://localhost:8080/coupon/exchange/1/2/1?token=3c5d3acb-31b9-480d-944a-516e74390ed8";
//		Thread.sleep(1000);
//		result = HttpUtils.doGetObject(exchangecoupon2);
//		assertEquals("200",result.get("msg"));

		//假装生成一个已经过期的优惠券
		String newcouponoff = "http://localhost:8080/couponOff/new";
		String couponoff = "{\"name\":\"10元代金券\",\"amount\":\"20\",\"point\":20,\"restrict\":10,\"type\":\"代金券兑换\",\"userId\":1,\"cityId\":1,\"cityName\":\"上海\",\"comment\":\"新用户欢迎礼券\",\"coverSUrl\":\""+couponSCover+"\",\"coverBUrl\":\""+couponBCover+"\"}";
		result = HttpUtils.doPostStr(newcouponoff,couponoff);
		assertEquals("200",result.get("msg"));
		
	}
	
//	@After
//	public void tearDown() throws Exception {
//		ClearDBUtil.dropDBTables("wxmall");;
//	}
}
