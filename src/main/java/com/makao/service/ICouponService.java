package com.makao.service;

import java.util.List;

import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.entity.History;
import com.makao.entity.User;

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

	Coupon getById(int id, int cityId);

	int deleteById(int id, int cityid);

	List<Coupon> queryAll(String tableName);

	Coupon queryByCouponId(String tableName, int couponid);

	int exchangeCoupon(Coupon coupon, User user);

	/**
	 * @param string
	 * @param userid
	 * @return
	 * 查询用户兑换历史
	 */
	List<History> queryHistory(String string, int userid);

}
