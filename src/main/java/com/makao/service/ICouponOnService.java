package com.makao.service;

import java.util.List;

import com.makao.entity.CouponOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponOnService {

	int insert(CouponOn couponOn);

	int update(CouponOn couponOn);

	List<CouponOn> queryByName(String name);

	List<CouponOn> queryAll();

	CouponOn getById(int id);

	int deleteById(int id);

}
