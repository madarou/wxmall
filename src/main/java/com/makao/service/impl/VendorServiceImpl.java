package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.makao.dao.IVendorDao;
import com.makao.entity.Vendor;
import com.makao.service.IVendorService;
import com.makao.weixin.main.WeixinServlet;

@Service
public class VendorServiceImpl implements IVendorService {
	private static final Logger logger = Logger.getLogger(VendorServiceImpl.class);
	@Resource
	private IVendorDao vendorDao;
	public Vendor getById(int id) {
		logger.info("VendorServiceImp getById,"+id);
		return this.vendorDao.getById(id);
	}
	@Override
	public int insert(Vendor vendor) {
		return this.vendorDao.insert(vendor);
	}
	
	@Override
	public int deleteById(int id) {
		return this.vendorDao.deleteById(id);
	}
	
	@Override
	public int update(Vendor vendor) {
		return this.vendorDao.update(vendor);
	}
	
	@Override
	public List<Vendor> queryAll() {
		return this.vendorDao.queryAll();
	}
	@Override
	public List<Vendor> queryByName(String name) {
		return this.vendorDao.queryByName(name);
	}
	@Override
	public List<Vendor> getByAreaId(int areaId) {
		return this.vendorDao.getByAreaId(areaId);
	}
	@Override
	public Vendor queryVendorByName(String userName) {
		return this.vendorDao.queryVendorByName(userName);
	}
	
//	@Override
//	public void testor() {
//		this.vendorDao.testor();
//	}
}
