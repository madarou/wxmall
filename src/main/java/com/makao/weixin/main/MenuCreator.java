package com.makao.weixin.main;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import com.makao.utils.MakaoConstants;
import com.makao.weixin.po.AccessToken;
import com.makao.weixin.po.menu.Button;
import com.makao.weixin.po.menu.ClickButton;
import com.makao.weixin.po.menu.Menu;
import com.makao.weixin.po.menu.ViewButton;
import com.makao.weixin.utils.AccessTokenUtil;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.WeixinConstants;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
public class MenuCreator {
	/**
	 * @return
	 * 创建一个菜单的Demo
	 * 里面包含三个一级菜单clickButton, viewButton和compoundButton
	 * 其中compoundButton还是包含两个子菜单
	 * @throws UnsupportedEncodingException 
	 */
	public static Menu initMenu() throws UnsupportedEncodingException{
		Menu menu = new Menu();
		//进入商城
		ViewButton mallButton = new ViewButton();
		mallButton.setName("进入商城");
		mallButton.setType("view");
		//mallButton.setUrl("http://madarou1.ngrok.cc/user/login/?openid=3c5d3acb-31b9-480d-944a-516e74390ed8");//注意必须加http，否则会报40055错误
		//String login_url = "http://baidu.com";
		String login_url = "http://madarou1.ngrok.cc/user/snsapi_userinfo";
		String url = WeixinConstants.AUTH_URL.replace("APPID", WeixinConstants.APPID).
				replace("REDIRECT_URI", login_url).
				replace("SCOPE","snsapi_userinfo");
		mallButton.setUrl(url);
		//我的订单
		ViewButton orderButton = new ViewButton();
		orderButton.setName("我的订单");
		orderButton.setType("view");
		orderButton.setUrl("http://baidu.com");//注意必须加http，否则会报40055错误
		//关于我们
		ViewButton aboutButton = new ViewButton();
		aboutButton.setName("关于我们");
		aboutButton.setType("view");
		aboutButton.setUrl("http://baidu.com");//注意必须加http，否则会报40055错误
				
		//创建一个一级菜单，来包含scancodePushButton和locationSelectButton
		//注意，如果是包含子菜单的一级菜单，就不用设置Type
		Button compoundButton = new Button();
		compoundButton.setName("超级社区");
		compoundButton.setSub_button(new Button[]{orderButton,aboutButton});
		
		menu.setButton(new Button[]{mallButton,compoundButton});
		return menu;
	}
	
	/**
	 * @return
	 * @throws UnsupportedEncodingException
	 * 创建社享网菜单
	 * 包含两个viewButton和一个compoundButton
	 */
	public static Menu initSheXiangMenu() throws UnsupportedEncodingException{
		Menu menu = new Menu();
		//进入商城
		ViewButton mallButton = new ViewButton();
		mallButton.setName("进入商城");
		mallButton.setType("view");
		String login_url = MakaoConstants.SERVER_DOMAIN+"/user/snsapi_userinfo";
		String url = WeixinConstants.AUTH_URL.replace("APPID", WeixinConstants.APPID).
				replace("REDIRECT_URI", login_url).
				replace("SCOPE","snsapi_userinfo");
		mallButton.setUrl(url);
		//互动社区
		ViewButton orderButton = new ViewButton();
		orderButton.setName("互动社区");
		orderButton.setType("view");
		orderButton.setUrl("http://buluo.qq.com/mobile/barindex.html?_bid=128&_wv=1027&bid=345437");//注意必须加http，否则会报40055错误
		//市场调查
		ViewButton marketButton = new ViewButton();
		marketButton.setName("市场调查");
		marketButton.setType("view");
		marketButton.setUrl("http://form.mikecrm.com/MHeyst");//注意必须加http，否则会报40055错误
		//产品征集
		ViewButton productButton = new ViewButton();
		productButton.setName("产品征集");
		productButton.setType("view");
		productButton.setUrl("http://form.mikecrm.com/oCqqTk");
		
		//创建一个一级菜单，来包含scancodePushButton和locationSelectButton
		//注意，如果是包含子菜单的一级菜单，就不用设置Type
		Button compoundButton = new Button();
		compoundButton.setName("社享服务");
		compoundButton.setSub_button(new Button[]{marketButton,productButton});
		
		menu.setButton(new Button[]{mallButton,orderButton,compoundButton});
		return menu;
	}
	
	/**
	 * @param token
	 * @param menu
	 * @return
	 * 向微信服务器发送POST请求，创建菜单，返回创建后的错误码，
	 * 为0表示创建成功
	 */
	public static int createMenu(String token, String menu){
		int result = 0;
		String url = WeixinConstants.CREATEMENU_URL.replace("ACCESS_TOKEN", token);
		//根据文档，创建菜单以POST方式提交请求，参数为json格式的菜单对象,string型
		JSONObject jsonObject = HttpUtil.doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	//创建菜单只创建一次，不用启动服务器
	public static void main(String[] args) throws UnsupportedEncodingException {
		AccessToken token = AccessTokenUtil.getAccessToken();
		System.out.println(token.getToken());
		System.out.println(token.getExpiresIn());
		//测试创建菜单功能
		int result = createMenu(token.getToken(), JSONObject.fromObject(initSheXiangMenu()).toString());
		if(result==0){
			System.out.println("创建菜单成功");
		}else{
			System.out.println("错误码: " + result);
		}
	}
	
}
