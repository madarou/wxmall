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

	OrderOn distributeOrder(int cityId, int orderid);

	OrderOn finishOrder(int cityId, int orderid);

	List<OrderOn> queryByUserId(String string, int userid);

	OrderOn queryByOrderId(String string, int orderid);
	//已配送的但尚未确认收货的订单列表
	List<OrderOn> queryDistributedByAreaId(String string, int areaId);
	//确认收货
	public int confirmGetOrder(int cityid, int orderid);

	/**
	 * @param areaid 
	 * @return
	 * 查询排队订单的记录数
	 */
	int getQueueRecordCount(int cityid, int areaid);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询待处理和处理中的记录数
	 */
	int getProcessRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询已配送的记录总数
	 */
	int getDistributedRecordCount(int cityId, int areaId);

	/**
	 * @param cityid
	 * @return
	 * 返回Order_cityid_on的记录总数
	 */
	int getRecordCount(int cityid);

	/**
	 * @param cityid
	 * @param orderid
	 * 已付款后，将订单状态从未支付改为排队中
	 */
	OrderOn confirmMoney(String cityid, String orderid);

	/**
	 * @param number
	 * @return
	 * 查询订单是否已经存在
	 */
	boolean isExist(int cityId, String orderNum);

	/**
	 * @param cityId
	 * @param orderid
	 * @return
	 * 将订单状态从排队中改为待处理
	 */
	OrderOn processOrder(int cityId, String orderid);

	/**
	 * @param cityid 
	 * @return
	 * 从数据库中找出需要将状态从排队中改为待处理的订单，将其状态设为待处理
    	当配送时间起点-准备时间<=当前时间时的订单满足条件
	 */
	List<OrderOn> approachOrders(int cityid);
	
	/**
	 * @param cityid
	 * @return
	 * 订单配送完一定时间后，如果其状态还是已配送(即用户没有手动确认收货)，自动将其状态改为已收货
	 */
	List<String> confirmOrders(int cityid);

	/**
	 * @param cityId
	 * @param orderid
	 * @param vcomment
	 * @return
	 * 商户为订单添加备注
	 */
	int vcommentOrder(int cityId, int orderid, String vcomment);

	/**
	 * @param id
	 * @return
	 * 数据库中查到所有15分钟内未支付或支付失败的订单，同时删除它们，并且返回订单列表
	 */
	List<OrderOn> unPaidOrders(int id);

}
