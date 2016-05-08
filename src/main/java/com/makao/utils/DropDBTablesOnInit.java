package com.makao.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.InitializingBean;

import com.makao.utils.ClearDBUtil;

/**
 * @description: 测试时使用，在spring启动时清空数据库，保证测试干净
 * @author makao
 * @date 2016年5月8日
 */
public class DropDBTablesOnInit implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent context){
		System.out.println("***********Clean DB**************");
		ClearDBUtil.dropDBTables("wxmall");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
