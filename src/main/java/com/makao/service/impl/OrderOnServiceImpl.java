package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IOrderOnDao;
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOnService;

@Service
public class OrderOnServiceImpl implements IOrderOnService {
	@Resource
	private IOrderOnDao orderOnDao;
	public OrderOn getById(int id) {
		return this.orderOnDao.getById(id);
	}
	@Override
	public int insert(OrderOn orderOn) {
		return this.orderOnDao.insert(orderOn);
	}
	
	@Override
	public int deleteById(int id) {
		return this.orderOnDao.deleteById(id);
	}
	
	@Override
	public int update(OrderOn orderOn) {
		return this.orderOnDao.update(orderOn);
	}
	
	@Override
	public List<OrderOn> queryAll() {
		return this.orderOnDao.queryAll();
	}
	@Override
	public List<OrderOn> queryByName(String name) {
		return this.orderOnDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.orderOnDao.testor();
//	}
}
