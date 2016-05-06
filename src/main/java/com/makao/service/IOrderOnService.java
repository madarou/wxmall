package com.makao.service;

import java.util.List;

import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IOrderOnService {

	com.makao.entity.OrderOn getOrderOnById(Integer id);

	int deleteOrderOn(Integer id);

	int insertOrderOn(OrderOn orderOn);

	int updateOrderOn(OrderOn orderOn);

	List<OrderOn> queryOrderOnByName(String name);

	List<OrderOn> queryAllOrderOns();

}
