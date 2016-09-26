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
	public Product getById(int id, int cityId, int areaId) {
		return this.productDao.getById(id, cityId, areaId);
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
	@Override
	public List<Product> queryByCityAreaId(int cityId, int areaId) {
		return this.productDao.queryByCityAreaId(cityId,areaId);
	}
	@Override
	public int insertToWhole(Product product) {
		return this.productDao.insertToWhole(product);
	}
	@Override
	public List<Product> queryRepProducts() {
		return this.productDao.queryRepProducts();
	}
	@Override
	public int notShowProduct(String tableName, int prodcutId) {
		return this.productDao.notShowProduct(tableName,prodcutId);
	}
	
	@Override
	public int showProduct(String tableName, int prodcutId) {
		return this.productDao.showProduct(tableName,prodcutId);
	}
	@Override
	public int updateRepProduct(Product product) {
		return this.productDao.updateRepProduct(product);
	}
	@Override
	public int getRecordCount(int cityId, int areaId) {
		return this.productDao.getRecordCount(cityId, areaId);
	}
	@Override
	public List<Product> queryFromToIndex(int cityId, int areaId, int from,
			int to) {
		return this.productDao.queryFromToIndex(cityId, areaId, from, to);
	}
	@Override
	public int getRecordCount() {
		return this.productDao.getRecordCount();
	}
	@Override
	public List<Product> queryFromToIndex(int from, int to) {
		return this.productDao.queryFromToIndex(from, to);
	}
	@Override
	public int like(String tableName, int productId) {
		return this.productDao.like(tableName, productId);
	}
	@Override
	public int getInventory(int cityId, int areaId, String id) {
		return this.productDao.getInventory(cityId, areaId, id);
	}
	@Override
	public int updateInventory(String tableName, String productid,
			String inventN) {
		return this.productDao.updateInventory(tableName, productid,inventN);
	}
	@Override
	public List<Product> searchProduct(int cityId, int areaId, String keyword,
			String cat) {
		return this.productDao.searchProduct(cityId, areaId, keyword, cat);
	}
	@Override
	public List<Product> queryThreholds(String table) {
		return this.productDao.queryThreholds(table);
	}
	@Override
	public List<Product> searchRepProducts(String keyword) {
		return this.productDao.searchRepProduct(keyword);
	}
	
	
//	@Override
//	public void testor() {
//		this.productDao.testor();
//	}
}
