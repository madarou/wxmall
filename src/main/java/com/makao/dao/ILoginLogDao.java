package com.makao.dao;

import java.util.List;

import com.makao.entity.LoginLog;

public interface ILoginLogDao {

    public int insert(LoginLog loginLog);

    public LoginLog getById(int id);

    public int update(LoginLog loginLog);
    
    public List<LoginLog> queryAll();
    
    public List<LoginLog> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}