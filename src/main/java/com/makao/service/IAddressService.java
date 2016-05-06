package com.makao.service;

import java.util.List;

import com.makao.entity.Address;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IAddressService {

	int insertAddress(Address address);

	int updateAddress(Address address);

	List<Address> queryAddressByName(String name);

	List<Address> queryAllAddresss();

	Address getAddressById(Integer id);

	int deleteAddress(Integer id);

}
