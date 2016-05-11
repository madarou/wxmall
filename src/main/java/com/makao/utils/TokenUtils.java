package com.makao.utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月10日
 */
public class TokenUtils {
//	private static final Random RANDOM = new Random();  
//	private static String generateGUID()  
//    {  
//        return new BigInteger(165, RANDOM).toString(36).toUpperCase();  
//    } 
	public static String setToken(String role){
		String s = java.util.UUID.randomUUID().toString();
		if("supervisor".equals(role))
			s = s + "s";
		else if("vendor".equals(role))
			s = s + "v";
		
		return s;
	}
	
	public static void main(String[] args){
		//System.out.println(TokenUtils.generateGUID());
		System.out.println(java.util.UUID.randomUUID().toString());
	}
}
