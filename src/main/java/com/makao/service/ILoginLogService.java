package com.makao.service;

import java.util.List;

import com.makao.entity.LoginLog;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ILoginLogService {

	int insert(LoginLog loginLog);

	int update(LoginLog loginLog);

	List<LoginLog> queryByName(String name);

	List<LoginLog> queryAll();

	LoginLog getById(int id);

	int deleteById(int id);

}
