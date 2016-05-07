package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ICouponOffDao;
import com.makao.entity.CouponOff;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponOffDaoImpl implements ICouponOffDao {

	@Override
	public int insert(CouponOff couponOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CouponOff getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(CouponOff couponOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CouponOff> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CouponOff> queryByName(String name) {
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
