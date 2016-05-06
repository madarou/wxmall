package com.makao.service;

import java.util.List;

import com.makao.entity.Supervisor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ISupervisorService {

	int insert(Supervisor supervisor);

	int update(Supervisor supervisor);

	List<Supervisor> queryByName(String name);

	List<Supervisor> queryAll();

	Supervisor getById(int id);

	int deleteById(int id);

}
