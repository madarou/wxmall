package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ICouponDao;
import com.makao.entity.Coupon;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponDaoImpl implements ICouponDao {

	@Override
	public int insert(Coupon coupon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Coupon getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Coupon coupon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Coupon> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> queryByName(String name) {
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
