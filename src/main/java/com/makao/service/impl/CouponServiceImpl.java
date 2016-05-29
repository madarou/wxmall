package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ICouponDao;
import com.makao.entity.Coupon;
import com.makao.service.ICouponService;

@Service
public class CouponServiceImpl implements ICouponService {
	@Resource
	private ICouponDao couponDao;
	public Coupon getById(int id) {
		return this.couponDao.getById(id);
	}
	@Override
	public int insert(Coupon coupon) {
		return this.couponDao.insert(coupon);
	}
	
	@Override
	public int deleteById(int id, int cityid) {
		return this.couponDao.deleteById(id, cityid);
	}
	
	@Override
	public int update(Coupon coupon) {
		return this.couponDao.update(coupon);
	}
	
	@Override
	public List<Coupon> queryAll() {
		return this.couponDao.queryAll();
	}
	@Override
	public List<Coupon> queryByName(String name) {
		return this.couponDao.queryByName(name);
	}
	@Override
	public List<Coupon> queryAll(String tableName) {
		return this.couponDao.queryAll(tableName);
	}
	
//	@Override
//	public void testor() {
//		this.couponDao.testor();
//	}
}
