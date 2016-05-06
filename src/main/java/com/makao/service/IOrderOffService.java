package com.makao.service;

import java.util.List;

import com.makao.entity.OrderOff;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IOrderOffService {

	com.makao.entity.OrderOff getOrderOffById(Integer id);

	int deleteOrderOff(Integer id);

	int insertOrderOff(OrderOff orderOff);

	int updateOrderOff(OrderOff orderOff);

	List<OrderOff> queryOrderOffByName(String name);

	List<OrderOff> queryAllOrderOffs();

}
