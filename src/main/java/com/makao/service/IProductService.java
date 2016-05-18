package com.makao.service;

import java.util.List;

import com.makao.entity.Product;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IProductService {

	int insert(Product product);

	int update(Product product);

	List<Product> queryByName(String name);

	List<Product> queryAll();

	Product getById(int id, int cityId, int areaId);

	int deleteById(int id);

	List<Product> queryByCityAreaId(int i, int j);

	int insertToWhole(Product product);

}
