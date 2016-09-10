package com.makao.utils;
/**
 * @description: TODO
 * @author makao
 * @date 2016年6月17日
 */
public class MakaoConstants {
	public static final int PAGE_SIZE = 10;//商品列表，券列表，用户列表等分页每页的记录数
	public static final long TOKEN_EXPIRE = 10;//系统自定义token在缓存中保留的时间(暂设为秒)
	public static final String SERVER_DOMAIN = "http://m1.shequvip.com";//最后部署上线的服务器的域名，用于设置微信回调页面
	
	public static final long ORDER_EXPIRE = 15;//下单等待支付的时间限制，单位为分
	
	public static final String DEFAULT_CITY_NAME="上海";//主页默认进来的城市
	public static final String DEFAULT_AREA_NAME="张江";
	public static final int DEFAULT_CITY_ID=1;
	public static final int DEFAULT_AREA_ID=1;
}
