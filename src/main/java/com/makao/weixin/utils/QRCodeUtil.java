package com.makao.weixin.utils;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.makao.controller.VendorController;
import com.makao.weixin.main.QRCodeCreater;
import com.makao.weixin.po.AccessToken;

/**
 * @author makao
 *管理员绑定账号时需要带参数的二维码，这里生成这个二维码，
 *程序获得ticket后，使用https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET到浏览器里下载图片
 */
public class QRCodeUtil {
	private static final Logger logger = Logger.getLogger(QRCodeUtil.class);
	/**
	 * @param vendorid
	 * @return
	 * 生成绑定vendorid的带参数的二维码，并返回其ticket,参数就是vendorid
	 */
	public static String generateQRCode(int vendorid){
		AccessToken token = AccessTokenUtil.getAccessToken();
		String url = WeixinConstants.QR_CODE_CREATE_URL.replace("ACCESS_TOKEN", token.getToken());
		JSONObject parameter = new JSONObject();
		parameter.put("action_name", "QR_LIMIT_SCENE");//永久二维码
		JSONObject scene_id=new JSONObject();scene_id.put("scene_id", vendorid);
		JSONObject scene = new JSONObject();scene.put("scene", scene_id);
		parameter.put("action_info", scene);
		//根据文档，创建菜单以POST方式提交请求，参数为json格式的菜单对象,string型
		JSONObject jsonObject = HttpUtil.doPostStr(url, parameter.toString());
		if(jsonObject != null){
			logger.info("创建二维码成功");
			logger.info("ticket: "+jsonObject.getString("ticket"));
			//System.out.println("expire_seconds: "+jsonObject.getString("expire_seconds"));//永久的没有该字段
			logger.info("url: "+jsonObject.getString("url"));
			return jsonObject.getString("ticket");
		}else{
			logger.info("vender id:"+vendorid+" 生成二维码错误");
			return null;
		}
	}
}
