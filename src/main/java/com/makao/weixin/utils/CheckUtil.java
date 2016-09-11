package com.makao.weixin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
public class CheckUtil {
	private static final String token = "shexiangvip";
	/**
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * 根据微信流程，验证微信服务器传来的signature
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce){
		String[] arr = new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(arr);
		
		//生成字符串
		StringBuffer content = new StringBuffer();
		for(int i=0; i<arr.length;i++){
			content.append(arr[i]);
		}
		
		//sha1加密
		String temp = SHA1(content.toString());
		return signature.equals(temp);
	}
	
	/**
	 * @param sha1加密算法
	 * @return
	 */
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
