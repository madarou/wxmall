package com.makao.dao;

import java.util.List;

import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;

public interface ICouponOnDao {

    public int insert(CouponOn couponOn);

    public CouponOn getById(int id);

    public int update(CouponOn couponOn);
    
    public List<CouponOn> queryAll();
    
    public List<CouponOn> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<CouponOn> queryAllByUserId(String tableName, int userid);

	public CouponOn queryByCouponId(String tableName, int couponid);

	/**
	 * @param cityid
	 * @return
	 * 将cityid对应的couponOn中要过期的coupon放入couponOff
	 */
	public List<CouponOn> expireCoupons(int cityid);
}