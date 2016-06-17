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

	List<Product> queryRepProducts();

	int notShowProduct(String tableName, int prodcutId);
	int showProduct(String tableName, int prodcutId);

	int updateRepProduct(Product product);
	
	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 获取总的记录数
	 */
	int getRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @param position
	 * @param i
	 * @return
	 * 返回从指定下标开始到指定下标结束的所有记录
	 */
	List<Product> queryFromToIndex(int cityId, int areaId, int from, int to);

	/**
	 * @return
	 * 获取产品仓库，即Product表中的记录总数
	 */
	int getRecordCount();

	/**
	 * @param position
	 * @param i
	 * @return
	 * 从Product表中返回从指定下标开始到指定下标结束的所有记录
	 */
	List<Product> queryFromToIndex(int from, int to);
}
