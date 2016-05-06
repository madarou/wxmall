package com.makao.service;

import java.util.List;

import com.makao.entity.Coupon;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponService {

	int insert(Coupon coupon);

	int update(Coupon coupon);

	List<Coupon> queryByName(String name);

	List<Coupon> queryAll();

	Coupon getById(int id);

	int deleteById(int id);

}
