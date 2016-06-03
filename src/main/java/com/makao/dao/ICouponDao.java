package com.makao.dao;

import java.util.List;

import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.entity.User;

public interface ICouponDao {

    public int insert(Coupon coupon);

    public Coupon getById(int id, int cityid);

    public int update(Coupon coupon);
    
    public List<Coupon> queryAll();
    
    public List<Coupon> queryByName(String name);

	public void testor();

	public int deleteById(int id, int cityid);

	public List<Coupon> queryAll(String tableName);

	public Coupon queryByCouponId(String tableName, int couponid);

	public int exchangeCoupon(Coupon coupon, User user);
}