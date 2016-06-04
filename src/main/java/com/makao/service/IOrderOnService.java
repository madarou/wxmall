package com.makao.service;

import java.util.List;

import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IOrderOnService {

	int insert(OrderOn orderOn);

	int update(OrderOn orderOn);

	List<OrderOn> queryByName(String name);

	List<OrderOn> queryAll(String tableName);

	OrderOn getById(int id);

	int deleteById(int id);

	List<OrderOn> queryQueueByAreaId(String tableName, int areaId);

	int cancelOrder(int cityId, int orderid, String vcomment);

	List<OrderOn> queryProcessByAreaId(String tableName, int areaId);

	int distributeOrder(int cityId, int orderid);

	int finishOrder(int cityId, int orderid);

	List<OrderOn> queryByUserId(String string, int userid);

	OrderOn queryByOrderId(String string, int orderid);
	//已配送的但尚未确认收货的订单列表
	List<OrderOn> queryDistributedByAreaId(String string, int areaId);
	//确认收货
	public int confirmGetOrder(int cityid, int orderid);

}
