package com.makao.quartz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.makao.entity.City;
import com.makao.entity.OrderOn;
import com.makao.service.ICityService;
import com.makao.service.IOrderOnService;
import com.makao.service.IVendorService;
import com.makao.thread.BatchSendMSGThread;

public class ApproachOrdersJob {
	@Resource
	private IOrderOnService orderOnService;
	@Resource
	private ICityService cityService;
	@Resource
	private IVendorService vendorService;
	public void execute(){
		List<OrderOn> processed_orders = new ArrayList<OrderOn>();
		//获取所有城市id
		List<City> cities = this.cityService.queryAll();
		for(City c : cities){
			System.out.println(c.getCityName());
			//获取所有满足条件的订单并设置对应的状态
			List<OrderOn> temp = this.orderOnService.approachOrders(c.getId());
			if(temp!=null)//注意addAll方法必须先判断是否为null，否则加入null元素时会报NPE
				processed_orders.addAll(temp);
		}
		if(processed_orders!=null&&processed_orders.size()>0){
			BatchSendMSGThread snt = new BatchSendMSGThread(processed_orders, vendorService);
			new Thread(snt, "batch send order preprare mb msg thread").start();
		}
	}

}
