package com.makao.utils;
/**
 * @description: TODO
 * @author makao
 * @date 2016年5月14日
 * 生成唯一订单号
 */
public class OrderNumberUtils {
	public static String generateOrderNumber(){
		int machineId = 1;
		int hashCodeV = java.util.UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0){
			hashCodeV = - hashCodeV;
		}
		return machineId+String.format("%015d", hashCodeV);
	}
}
