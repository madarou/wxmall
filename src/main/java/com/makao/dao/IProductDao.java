package com.makao.dao;

import java.util.List;
import java.util.Map;

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
	 * 从Product_cityId_areaId中获取产品id的库存和销量
	 */
	public String getInventoryAndSV(int cityId, int areaId, String id);

	public int updateInventory(String tableName, String productid,
			String inventN);

	/**
	 * @param cityId
	 * @param areaId
	 * @param keyword
	 * @param cat
	 * @return
	 * 根据关键字和分类搜索产品
	 */
	public List<Product> searchProduct(int cityId, int areaId, String keyword,
			String cat);

	/**
	 * @param table
	 * @return
	 * 查询库存小于最低库存的商品
	 */
	public List<Product> queryThreholds(String table);

	/**
	 * @param keyword
	 * @return
	 *搜索总库里的商品
	 */
	public List<Product> searchRepProduct(String keyword);

	/**
	 * @param string
	 * @param productId
	 * @param num
	 * @return
	 * 给商品补货
	 */
	public int supplyProduct(String string, int productId, int num);

	/**
	 * @param string
	 * @param productId
	 * @param num
	 * @return
	 * 更新库存为num，同时将supply设为0
	 */
	public int suppliedProduct(String string, int productId, int num);

	/**
	 * @param tableName
	 * @param productid
	 * @param saled
	 * @return
	 * 销量修改为saled
	 */
	public int updateSalesVolume(String tableName, String productid,
			int saled);

	/**
	 * @param tableName
	 * @param prodcutId
	 * @return
	 * 区域管理员删除商品
	 */
	public int deleteProduct(String tableName, int prodcutId);
	/**
	 * @param offset
	 * @param pageSize
	 * @return
	 * 从Product表中返回第offset+1开始的pageSize条记录
	 */
	public List<Product> queryFromToIndexOffset(int offset, int pageSize);

	/**
	 * @param cityId
	 * @param areaId
	 * @param offset
	 * @param pageSize
	 * @return
	 * 从Product_cityid_areaId表中返回第offset+1开始的pageSize条记录
	 */
	public List<Product> queryFromToIndexOffset(int cityId, int areaId,
			int offset, int pageSize);
}