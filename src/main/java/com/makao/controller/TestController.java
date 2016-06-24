package com.makao.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.makao.auth.AuthPassport;
import com.makao.entity.TokenModel;
import com.makao.utils.RedisUtil;
import com.makao.utils.TokenManager;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月10日
 */
@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	@Autowired
	private RedisUtil redisUtil;
	
	private static final Logger logger = Logger.getLogger(TestController.class);
	@RequestMapping(value="/supervisor")
	public String supervisor(){
		return "s";
	}
	
	@RequestMapping(value="/vendor")
	public String vendor(){
		return "v_orderOn";
	}
	@RequestMapping(value="/login", method = {RequestMethod.GET})
    public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("message", "Hello World!");  
	    modelAndView.setViewName("login");  
	    return modelAndView;
	}
	
	@AuthPassport
	@RequestMapping(value={"/index"})
	public ModelAndView index(){
	    
	    ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("message", "Hello World!");  
	    modelAndView.setViewName("index");  
	    return modelAndView;
	}

	@RequestMapping(value={"/redistx"})
	public List<Object> executeTransaction(){
		//execute a transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
		  public List<Object> execute(RedisOperations operations) throws DataAccessException {
			  List<Object> rt = null;
			  while(true){
				  operations.watch("inventory");
				  operations.multi();
				  //必须用increment的才能在exec()方法得到之后的inventory值，使用set(key,value)方法没有返回，
				  //无法判断是否执行成功
				  operations.opsForValue().increment("inventory", -1);
				  try {
					  Thread.sleep(4000);
				  } catch (InterruptedException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  //				    logger.info("decreasing inventory to: "+ inventory);
				  rt = operations.exec();
				  logger.info("exec rt: " + rt);
				  if(rt!=null){
					  //int inventory = (int) rt.get(0);
					  logger.info("rt: " + rt.get(0));
					  break;
				  }
			  }
		    return rt;
		  }
		});
		return txResults;
	}
	
	@RequestMapping(value={"/redistx2"})
	public void executeTransaction2(){
		int inventory = Integer.valueOf(redisUtil.redisQueryObject("inventory"));
		logger.info("inventory: " + inventory);
	}
	
}
