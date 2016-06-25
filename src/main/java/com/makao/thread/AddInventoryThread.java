package com.makao.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.makao.utils.RedisUtil;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月25日
 */
public class AddInventoryThread implements Runnable{	
	private String key;//商品的在缓存中的key
	private int productNum;//要加回去的商品数量
	private RedisUtil redisUtil;
	public AddInventoryThread(String key, int productNum, RedisUtil redisUtil){
		this.key = key;
		this.productNum = productNum;
		this.redisUtil = redisUtil;
	}
	@Override
	public void run() {
		redisUtil.addInventoryTx(this.key, this.productNum);
	}

}
