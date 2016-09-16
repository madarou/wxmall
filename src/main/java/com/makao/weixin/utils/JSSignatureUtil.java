package com.makao.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.makao.controller.UserController;
import com.makao.weixin.po.AccessToken;
import com.makao.weixin.po.JSSignature;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月9日
 * JSSDK的签名生成
 */
public class JSSignatureUtil {
	private static final Logger logger = Logger.getLogger(JSSignatureUtil.class);
	//private static String jsapi_ticket = freshJsApiTicket(AccessTokenUtil.getAccessToken().getToken());
	private static JSSignature jSSignature = getJsApiTicket(AccessTokenUtil.getAccessToken().getToken());
	/**
	 * @param access_token
	 * @return
	 * 重新获取ticket
	 */
	public static JSSignature getJsApiTicket(String access_token) {
		if(jSSignature==null||jSSignature.isExpired()){
			jSSignature = new JSSignature();
			 String requestUrl = WeixinConstants.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
		        // 发起GET请求获取凭证
		        JSONObject jsonObject = HttpUtil.doGetObject(requestUrl);
		        if (null != jsonObject) {
		            try {
		                String ticket = jsonObject.getString("ticket");
		                System.out.println("jsapi_ticket: "+ticket);
		                jSSignature.setTicket(ticket);
		                jSSignature.setExpires_in(jsonObject.getInt("expires_in"));
		            } catch (JSONException e) {
		                // 获取token失败
		            	logger.error("获取token失败 errcode:{"+jsonObject.getInt("errcode")+"} errmsg:{"+jsonObject.getString("errmsg")+"}");
		            }
		        }
		        return jSSignature;
		}
		return jSSignature;
    }
	
	/**
	 * @return
	 * 获取现有的jsapi_ticket
	 */
	public static JSSignature getJsApiTicket(){
		return jSSignature;
	}
	
	/**
	 * 重新设置ticket
	 */
//	public static String resetJsApiTicket(){
//		jsapi_ticket = freshJsApiTicket(AccessTokenUtil.getAccessToken().getToken());//因为ticket和token目前的过期时间都是2小时，所以获取新ticket的同时也重新获取token
//		return jsapi_ticket;
//	}
	
    public static Map<String, String> getSignature(String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String str;
        String signature = "";
 
        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + getJsApiTicket(AccessTokenUtil.getAccessToken().getToken()).getTicket() +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
 
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
 
        ret.put("url", url);
        ret.put("jsapi_ticket", getJsApiTicket(AccessTokenUtil.getAccessToken().getToken()).getTicket());
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
 
        return ret;
    }
 
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
 
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
 
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
