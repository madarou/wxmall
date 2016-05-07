package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IOrderOnDao;
import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class OrderOnDaoImpl implements IOrderOnDao {

	@Override
	public int insert(OrderOn orderOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OrderOn getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrderOn orderOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderOn> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderOn> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
