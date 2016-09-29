package com.makao.dao;

import java.util.List;

import com.makao.entity.Vendor;

public interface IVendorDao {

    public int insert(Vendor vendor);

    public Vendor getById(int id);

    public int update(Vendor vendor);
    
    public List<Vendor> queryAll();
    
    public List<Vendor> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	/**
	 * @param areaId
	 * @return
	 * 根据areaId获取其下所有vendor
	 */
	public List<Vendor> getByAreaId(int areaId);

	public Vendor queryVendorByName(String userName);
}