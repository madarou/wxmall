package com.makao.dao;

import java.util.List;

import com.makao.entity.Supervisor;

public interface ISupervisorDao {

    public int insert(Supervisor supervisor);

    public Supervisor getById(int id);

    public int update(Supervisor supervisor);
    
    public List<Supervisor> queryAll();
    
    public List<Supervisor> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}