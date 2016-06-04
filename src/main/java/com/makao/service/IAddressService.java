package com.makao.service;

import java.util.List;

import com.makao.entity.Address;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IAddressService {

	int insert(Address address);

	int update(Address address);

	List<Address> queryByName(String name);

	List<Address> queryAll();

	Address getById(int id);

	int deleteById(int id);

	List<Address> queryByUserId(int userid);

}
