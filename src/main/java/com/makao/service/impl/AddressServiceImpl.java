package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IAddressDao;
import com.makao.entity.Address;
import com.makao.service.IAddressService;

@Service
public class AddressServiceImpl implements IAddressService {
	@Resource
	private IAddressDao addressDao;
	public Address getById(int id) {
		return this.addressDao.getById(id);
	}
	@Override
	public int insert(Address address) {
		return this.addressDao.insert(address);
	}
	
	@Override
	public int deleteById(int id) {
		return this.addressDao.deleteById(id);
	}
	
	@Override
	public int update(Address address) {
		return this.addressDao.update(address);
	}
	
	@Override
	public List<Address> queryAll() {
		return this.addressDao.queryAll();
	}
	@Override
	public List<Address> queryByName(String name) {
		return this.addressDao.queryByName(name);
	}
	@Override
	public List<Address> queryByUserId(int userid) {
		return this.addressDao.queryByUserId(userid);
	}
	
//	@Override
//	public void testor() {
//		this.addressDao.testor();
//	}
}
