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
	public void test() {
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
		String user = "{\"userName\":\"马买家\",\"openid\":\"lielsjdiwlsnefefsdfew3fdfdfd\",\"avatarUrl\":\""+userHead+"\",\"areaId\":1,\"areaName\":\"张江\",\"cityId\":1,\"cityName\":\"上海\",\"point\":20,\"receiveName\":\"郭德纲\",\"phoneNumber\":\"176382937287\",\"address\":\"上海复旦大学\",\"addLabel\":\"家\",\"rank\":\"中级\"}";

		String newprodcut11_1 = "http://localhost:8080/product/vnew/1";
		String product11_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut11_2 = "http://localhost:8080/product/vnew/1";
		String product11_2 = "{\"productName\":\"普通葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"10.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"13.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"海南\",\"isShow\":\"yes\",\"showWay\":\"b\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":1,\"cityId\":1,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
		String newprodcut22_1 = "http://localhost:8080/product/vnew/2";
		String product22_1 = "{\"productName\":\"水晶葡萄\",\"catalog\":\"水果\",\"label\":\"无标签\",\"price\":\"12.00\",\"standard\":\"一份足2斤\",\"marketPrice\":\"15.00\",\"inventory\":12,\"sequence\":1,\"status\":\"库存紧张\",\"origin\":\"智利\",\"isShow\":\"yes\",\"showWay\":\"s\",\"salesVolume\":7637,\"likes\":3972,\"areaId\":2,\"cityId\":2,\"coverSUrl\":\""+productSCover+"\",\"coverBUrl\":\""+productBCover+"\",\"detailUrl\":\""+productDetail2+"\"}";
	
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
		
		result = HttpUtils.doPostStr(newprodcut11_1,product11_1);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newprodcut11_2,product11_2);
		assertEquals("200",result.get("msg"));
		result = HttpUtils.doPostStr(newprodcut22_1,product22_1);
		assertEquals("200",result.get("msg"));
	}
	
//	@After
//	public void tearDown() throws Exception {
//		ClearDBUtil.dropDBTables("wxmall");;
//	}
}
