package com.makao.service;

import java.util.List;

import com.makao.entity.Coupon;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponService {

	int insertCoupon(Coupon coupon);

	int updateCoupon(Coupon coupon);

	List<Coupon> queryCouponByName(String name);

	List<Coupon> queryAllCoupons();

	com.makao.entity.Coupon getCouponById(Integer id);

	int deleteCoupon(Integer id);

}
