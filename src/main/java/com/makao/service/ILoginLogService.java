package com.makao.service;

import java.util.List;

import com.makao.entity.LoginLog;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ILoginLogService {

	com.makao.entity.LoginLog getLoginLogById(Integer id);

	int deleteLoginLog(Integer id);

	int insertLoginLog(LoginLog loginLog);

	int updateLoginLog(LoginLog loginLog);

	List<LoginLog> queryLoginLogByName(String name);

	List<LoginLog> queryAllLoginLogs();

}
