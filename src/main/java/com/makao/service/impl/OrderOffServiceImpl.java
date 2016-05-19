package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IOrderOffDao;
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
	public List<OrderOff> queryAll() {
		return this.orderOffDao.queryAll();
	}
	@Override
	public List<OrderOff> queryByName(String name) {
		return this.orderOffDao.queryByName(name);
	}
	@Override
	public List<OrderOff> queryDoneByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryDoneByAreaId(tableName, areaId);
	}
	@Override
	public List<OrderOff> queryCancelByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryCancelByAreaId(tableName, areaId);
	}
	
//	@Override
//	public void testor() {
//		this.orderOffDao.testor();
//	}
}
