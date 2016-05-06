package com.makao.service;

import java.util.List;

import com.makao.entity.Vendor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IVendorService {

	int insert(Vendor vendor);

	int update(Vendor vendor);

	List<Vendor> queryByName(String name);

	List<Vendor> queryAll();

	Vendor getById(int id);

	int deleteById(int id);

}
