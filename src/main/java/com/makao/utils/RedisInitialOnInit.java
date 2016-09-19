package com.makao.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.makao.controller.TestController;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月24日
 * 系统启动时，初始化缓存
 * 系统退出时，缓存写入数据库
 */

public class RedisInitialOnInit implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(RedisInitialOnInit.class);
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 * 服务器正常退出的话，会执行该函数
	 * 这里可以将缓存最后的数据写入数据库
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ApplicationContext application=WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()); 
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) application.getBean("redisTemplate");
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		if(redisTemplate.hasKey("inventory")){
			redisTemplate.delete("inventory");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ApplicationContext application=WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()); 
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) application.getBean("redisTemplate");
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		if(redisTemplate.hasKey("inventory")){
			redisTemplate.delete("inventory");
		}
		//这里不能用set(key, value)，因为已经使用了StringRedisSerializer，Long类型不能cast成String
		//而如果使用默认的JDKSerializer，increment会在加时报错，因为JDKSerializer把Long值序列化成了不
		//规则的一些字串，不能直接拿出来做加减。如果key不存在，直接增加会从0开始加
//		vop.increment("inventory", 20);
//		logger.info("set inventory to: "+ 20);
	}

}
