package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ICouponOnDao;
import com.makao.entity.CouponOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponOnDaoImpl implements ICouponOnDao {

	@Override
	public int insert(CouponOn couponOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CouponOn getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(CouponOn couponOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CouponOn> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CouponOn> queryByName(String name) {
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
