package com.makao.service;

import java.util.List;

import com.makao.entity.Supervisor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ISupervisorService {

	com.makao.entity.Supervisor getSupervisorById(Integer id);

	int deleteSupervisor(Integer id);

	int insertSupervisor(Supervisor supervisor);

	int updateSupervisor(Supervisor supervisor);

	List<Supervisor> querySupervisorByName(String name);

	List<Supervisor> queryAllSupervisors();

}
