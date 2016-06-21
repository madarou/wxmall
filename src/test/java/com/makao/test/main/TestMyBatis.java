package com.makao.test.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.makao.entity.User;
import com.makao.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class TestMyBatis {
	private static Logger logger = Logger.getLogger(TestMyBatis.class);
//	private ApplicationContext ac = null;
//	@Resource
//	private IUserService userService = null;

//	@Before
//	public void before() {
//		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//		userService = (IUserService) ac.getBean("userService");
//	}

//	@Test
//	public void test1() {
//		User user = userService.getById(1);
//		// System.out.println(user.getUserName());
//		// logger.info("值："+user.getUserName());
//		logger.info(JSON.toJSONString(user));
//	}
	@Test
	public void test(){
		System.out.println(System.currentTimeMillis());
		
		//订单号生成
		System.out.println(generateOrderNumber());
		Pattern pattern = Pattern.compile("^(\\d+){2}.(\\d+){2}$");
		Matcher m = pattern.matcher("12.3w");
		if(m.matches()){
			System.out.println("yes");
		}
		else{
			System.out.println("no");
		}
	}
	public static String generateOrderNumber(){
		int machineId = 1;
		int hashCodeV = java.util.UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0){
			hashCodeV = - hashCodeV;
		}
		return machineId+String.format("%015d", hashCodeV);
	}
}
