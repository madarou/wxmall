package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IProductDao;
import com.makao.entity.Product;
import com.makao.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {
	@Resource
	private IProductDao productDao;
	public Product getById(int id) {
		return this.productDao.getById(id);
	}
	@Override
	public int insert(Product product) {
		return this.productDao.insert(product);
	}
	
	@Override
	public int deleteById(int id) {
		return this.productDao.deleteById(id);
	}
	
	@Override
	public int update(Product product) {
		return this.productDao.update(product);
	}
	
	@Override
	public List<Product> queryAll() {
		return this.productDao.queryAll();
	}
	@Override
	public List<Product> queryByName(String name) {
		return this.productDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.productDao.testor();
//	}
}
