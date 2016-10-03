package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IOrderOffDao;
import com.makao.entity.Comment;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOffService;

@Service
public class OrderOffServiceImpl implements IOrderOffService {
	@Resource
	private IOrderOffDao orderOffDao;
	public OrderOff getById(int id) {
		return this.orderOffDao.getById(id);
	}
	@Override
	public int insert(OrderOff orderOff) {
		return this.orderOffDao.insert(orderOff);
	}
	
	@Override
	public int deleteById(int id) {
		return this.orderOffDao.deleteById(id);
	}
	
	@Override
	public int update(OrderOff orderOff) {
		return this.orderOffDao.update(orderOff);
	}
	
	@Override
	public List<OrderOff> queryAll(String tableName) {
		return this.orderOffDao.queryAll(tableName);
	}
	@Override
	public List<OrderOff> queryByName(String name) {
		return this.orderOffDao.queryByName(name);
	}
	@Override
	public List<OrderOff> queryConfirmGetByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryConfirmGetByAreaId(tableName, areaId);
	}
	@Override
	public List<OrderOff> queryCancelByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryCancelByAreaId(tableName, areaId);
	}
	@Override
	public List<OrderOff> queryRefundByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryRefundByAreaId(tableName, areaId);
	}
	@Override
	public int refundOrder(int cityId, int orderid) {
		return this.orderOffDao.refundOrder(cityId,orderid);
	}
	@Override
	public int finishRefundOrder(int cityId, int orderid) {
		return this.orderOffDao.finishRefundOrder(cityId, orderid);
	}
	@Override
	public int cancelRefundOrder(int cityId, int orderid, String vcomment) {
		return this.orderOffDao.cancelRefundOrder(cityId, orderid, vcomment);
	}

	@Override
	public List<OrderOff> queryAllCanceledAndReturned(String tableName) {
		return this.orderOffDao.queryAllCanceledAndReturned(tableName);
	}
	@Override
	public OrderOff finishReturnOrder(int cityId, int orderid) {
		return this.orderOffDao.finishReturnOrder(cityId, orderid);
	}
	@Override
	public List<OrderOff> queryByUserId(String tableName, int userid) {
		return this.orderOffDao.queryByUserId(tableName, userid);
	}
	@Override
	public OrderOff queryByOrderId(String tableName, int orderid) {
		return this.orderOffDao.queryByOrderId(tableName, orderid);
	}
	@Override
	public OrderOff returnOrder(int cityid, int orderid) {
		return this.orderOffDao.returnOrder(cityid,orderid);
	}
	@Override
	public int getConfirmRecordCount(int cityId, int areaId) {
		return this.orderOffDao.getConfirmRecordCount(cityId, areaId);
	}
	@Override
	public int getReturnRecordCount(int cityId, int areaId) {
		return this.orderOffDao.getReturnRecordCount(cityId, areaId);
	}
	@Override
	public int getCancelRecordCount(int cityId, int areaId) {
		return this.orderOffDao.getCancelRecordCount(cityId, areaId);
	}
	@Override
	public int getRecordCount(int cityid) {
		return this.orderOffDao.getRecordCount(cityid);
	}
	@Override
	public int getCanceledAndReturnedRecordCount(int cityid) {
		return this.orderOffDao.getCanceledAndReturnedRecordCount(cityid);
	}
	@Override
	public int updateComment(Comment comment) {
		return this.orderOffDao.updateComment(comment);
		
	}
	@Override
	public List<OrderOff> inventoryBackCanceledAndReturned(String string) {
		return this.orderOffDao.inventoryBackCanceledAndReturned(string);
	}
	@Override
	public List<OrderOff> terminateOrders(int cityid) {
		return this.orderOffDao.terminateOrders(cityid);
	}
	@Override
	public int queryNeedRefundNumber(String string) {
		return this.orderOffDao.queryNeedRefundNumber(string);
	}
	
//	@Override
//	public void testor() {
//		this.orderOffDao.testor();
//	}
}
