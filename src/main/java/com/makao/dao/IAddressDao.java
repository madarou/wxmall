package com.makao.dao;

import java.util.List;

import com.makao.entity.Address;

public interface IAddressDao {

    public int insert(Address address);

    public Address getById(int id);

    public int update(Address address);
    
    public List<Address> queryAll();
    
    public List<Address> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<Address> queryByUserId(int userid);

	public List<Address> queryByCityAreaUserId(int cityid, int areaid,
			int userid);
}