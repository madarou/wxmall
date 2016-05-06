package com.makao.service;

import java.util.List;

import com.makao.entity.Product;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IProductService {

	com.makao.entity.Product getProductById(Integer id);

	int deleteProduct(Integer id);

	int insertProduct(Product product);

	int updateProduct(Product product);

	List<Product> queryProductByName(String name);

	List<Product> queryAllProducts();

}
