package com.makao.dao;

import java.util.List;

import com.makao.entity.Product;

public interface IProductDao {

    public int insert(Product product);

    public Product getById(int id);

    public int update(Product product);
    
    public List<Product> queryAll();
    
    public List<Product> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<Product> queryByCityAreaId(String cityId, String areaId);
}