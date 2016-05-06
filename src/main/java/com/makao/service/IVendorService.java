package com.makao.service;

import java.util.List;

import com.makao.entity.Vendor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IVendorService {

	com.makao.entity.Vendor getVendorById(Integer id);

	int deleteVendor(Integer id);

	int insertVendor(Vendor vendor);

	int updateVendor(Vendor vendor);

	List<Vendor> queryVendorByName(String name);

	List<Vendor> queryAllVendors();

}
