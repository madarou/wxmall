package com.makao.dao;

import java.util.List;

import com.makao.entity.Product;

public interface IProductDao {

    public int insert(Product product);

    public Product getById(int id, int cityId, int areaId);

    public int update(Product product);
    
    public List<Product> queryAll();
    
    public List<Product> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<Product> queryByCityAreaId(int cityId, int areaId);

	public int insertToWhole(Product product);

	public List<Product> queryRepProducts();

	public int notShowProduct(String tableName, int prodcutId);
	public int showProduct(String tableName, int prodcutId);

	public int updateRepProduct(Product product);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 获取总的记录条数
	 */
	public int getRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @param from
	 * @param to
	 * @return
	 * 返回从指定下标开始到指定下标结束的所有记录
	 */
	public List<Product> queryFromToIndex(int cityId, int areaId, int from,
			int to);

	/**
	 * @return
	 * 获取Product表的记录总数
	 */
	public int getRecordCount();

	/**
	 * @param from
	 * @param to
	 * @return
	 * 从Product表中返回从指定下标开始到指定下标结束的所有记录
	 */
	public List<Product> queryFromToIndex(int from, int to);

	/**
	 * @param tableName
	 * @param productId
	 * @return
	 * 给商品点赞
	 */
	public int like(String tableName, int productId);

	/**
	 * @param cityId
	 * @param areaId
	 * @param id
	 * @return
	 * 从Product_cityId_areaId中获取产品id的库存
	 */
	public int getInventory(int cityId, int areaId, String id);

	public int updateInventory(String tableName, String productid,
			String inventN);
}