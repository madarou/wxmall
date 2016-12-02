package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ICouponOnDao;
import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.service.ICouponOnService;

@Service
public class CouponOnServiceImpl implements ICouponOnService {
	@Resource
	private ICouponOnDao couponOnDao;
	public CouponOn getById(int id) {
		return this.couponOnDao.getById(id);
	}
	@Override
	public int insert(CouponOn couponOn) {
		return this.couponOnDao.insert(couponOn);
	}
	
	@Override
	public int deleteById(int id) {
		return this.couponOnDao.deleteById(id);
	}
	
	@Override
	public int update(CouponOn couponOn) {
		return this.couponOnDao.update(couponOn);
	}
	
	@Override
	public List<CouponOn> queryAll() {
		return this.couponOnDao.queryAll();
	}
	@Override
	public List<CouponOn> queryByName(String name) {
		return this.couponOnDao.queryByName(name);
	}
	@Override
	public List<CouponOn> queryAllByUserId(String tableName, int userid) {
		return this.couponOnDao.queryAllByUserId(tableName, userid);
	}
	@Override
	public CouponOn queryByCouponId(String tableName, int couponid) {
		return this.couponOnDao.queryByCouponId(tableName, couponid);
	}
	@Override
	public List<CouponOn> expireCoupons(int cityid) {
		return this.couponOnDao.expireCoupons(cityid);
	}
	
//	@Override
//	public void testor() {
//		this.couponOnDao.testor();
//	}
}
