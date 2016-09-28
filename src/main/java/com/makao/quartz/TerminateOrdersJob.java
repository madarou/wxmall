package com.makao.quartz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.makao.entity.City;
import com.makao.entity.OrderOff;
import com.makao.service.ICityService;
import com.makao.service.IOrderOffService;

/**
 * @author makao
 *在确认后货一定时间后，自动将订单状态设为已完成，已完成的订单不能发起退货
 */
public class TerminateOrdersJob {
	@Resource
	private IOrderOffService orderOffService;
	@Resource
	private ICityService cityService;
	public void execute(){
		//List<String> term_orders = new ArrayList<String>();
		//获取所有城市id
		List<City> cities = this.cityService.queryAll();
		for(City c : cities){
			//获取所有满足条件的订单并设置对应的状态
			List<OrderOff> temp = this.orderOffService.terminateOrders(c.getId());
//			if(temp!=null)//注意addAll方法必须先判断是否为null，否则加入null元素时会报NPE
//				term_orders.addAll(temp);
		}
	}
}
