package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IVendorDao;
import com.makao.entity.Vendor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class VendorDaoImpl implements IVendorDao {

	@Override
	public int insert(Vendor vendor) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vendor getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Vendor vendor) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Vendor> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vendor> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
