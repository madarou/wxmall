package com.makao.weixin.main;

import net.sf.json.JSONObject;

import com.makao.weixin.po.AccessToken;
import com.makao.weixin.utils.AccessTokenUtil;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.WeixinConstants;

/**
 * @author makao
 *管理员绑定账号时需要带参数的二维码，这里生成这个二维码，
 *程序获得ticket后，使用https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET到浏览器里下载图片
 */
public class QRCodeCreater {
	private static final int scene_id = 1;//管理员绑定的二维码参数
	public static void main(String[] args) {
		AccessToken token = AccessTokenUtil.getAccessToken();
		System.out.println(token.getToken());
		System.out.println(token.getExpiresIn());
		String url = WeixinConstants.QR_CODE_CREATE_URL.replace("ACCESS_TOKEN", token.getToken());
		JSONObject parameter = new JSONObject();
		parameter.put("action_name", "QR_LIMIT_SCENE");//永久二维码
		JSONObject scene_id=new JSONObject();scene_id.put("scene_id", QRCodeCreater.scene_id);
		JSONObject scene = new JSONObject();scene.put("scene", scene_id);
		parameter.put("action_info", scene);
		//根据文档，创建菜单以POST方式提交请求，参数为json格式的菜单对象,string型
		JSONObject jsonObject = HttpUtil.doPostStr(url, parameter.toString());
		if(jsonObject != null){
			System.out.println("创建二维码成功");
			System.out.println("ticket: "+jsonObject.getString("ticket"));
			//System.out.println("expire_seconds: "+jsonObject.getString("expire_seconds"));//永久的没有该字段
			System.out.println("url: "+jsonObject.getString("url"));
		}else{
			System.out.println("错误");
		}

	}

}
