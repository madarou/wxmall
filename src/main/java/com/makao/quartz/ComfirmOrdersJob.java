package com.makao.quartz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.makao.entity.City;
import com.makao.service.ICityService;
import com.makao.service.IOrderOnService;

/**
 * @author makao
 * 订单配送完一定时间后，如果其状态还是已配送(即用户没有手动确认收货)，自动将其状态改为已收货
 *
 */
public class ComfirmOrdersJob {
	@Resource
	private IOrderOnService orderOnService;
	@Resource
	private ICityService cityService;
	public void execute(){
		List<String> processed_orders = new ArrayList<String>();
		//获取所有城市id
		List<City> cities = this.cityService.queryAll();
		for(City c : cities){
			System.out.println(c.getCityName());
			//获取所有满足条件的订单并设置对应的状态
			List<String> temp = this.orderOnService.confirmOrders(c.getId());
			if(temp!=null)//注意addAll方法必须先判断是否为null，否则加入null元素时会报NPE
				processed_orders.addAll(temp);
		}
		//不需要推送模板消息
//		if(processed_orders!=null&&processed_orders.size()>0){
//			BatchSendMSGThread snt = new BatchSendMSGThread(processed_orders, vendorService);
//			new Thread(snt, "batch send order preprare mb msg thread").start();
//		}
	}
}
