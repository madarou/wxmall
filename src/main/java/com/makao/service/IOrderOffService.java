package com.makao.service;

import java.util.List;

import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IOrderOffService {

	int insert(OrderOff orderOff);

	int update(OrderOff orderOff);

	List<OrderOff> queryByName(String name);

	List<OrderOff> queryAll(String tableName);

	OrderOff getById(int id);

	int deleteById(int id);
	//已确认收货的订单列表
	List<OrderOff> queryConfirmGetByAreaId(String string, int areaId);

	List<OrderOff> queryCancelByAreaId(String tableName, int areaId);

	/**
	 * @param tableName
	 * @param areaId
	 * @return
	 * 退货申请中和退货中的订单
	 */
	List<OrderOff> queryRefundByAreaId(String tableName, int areaId);

	int refundOrder(int cityId, int orderid);

	int finishRefundOrder(int cityId, int orderid);

	int cancelRefundOrder(int cityId, int orderid, String vcomment);

	List<OrderOff> queryAllCanceledAndReturned(String string);

	int finishReturnOrder(int cityId, int orderid);

	List<OrderOff> queryByUserId(String tableName, int userid);

	OrderOff queryByOrderId(String tableName, int orderid);
	//发起退货申请
	int returnOrder(int cityid, int orderid);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询已退货的记录数
	 */
	int getConfirmRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询退货申请中和退货中的订单记录数
	 */
	int getReturnRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 已取消，已退货，已取消退货的记录数
	 */
	int getCancelRecordCount(int cityId, int areaId);

}
