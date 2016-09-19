package com.makao.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.makao.auth.AuthPassport;
import com.makao.entity.OrderOn;
import com.makao.entity.OrderState;
import com.makao.entity.TokenModel;
import com.makao.thread.AddInventoryThread;
import com.makao.utils.OrderNumberUtils;
import com.makao.utils.RedisUtil;
import com.makao.utils.SerializeUtils;
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
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisTemplate<String, Object> stringRedisTemplate;
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
					  int inventory = ((Long)rt.get(0)).intValue();
					  logger.info("rt: " + inventory);
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
		//int inventory = Integer.valueOf(redisUtil.redisQueryObject("inventory"));
		logger.info("inventory: " + redisUtil.redisQueryObject("inventory"));
	}
	
	@RequestMapping(value={"/redistx3"})
	public void executeAdd() throws UnsupportedEncodingException{
		OrderOn orderOn = new OrderOn();
		orderOn.setNumber(OrderNumberUtils.generateOrderNumber());
		orderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
		orderOn.setPayType("微信安全支付");//现在只有这种支付方式
		orderOn.setReceiveType("送货上门");//现在只有这种收货方式
		orderOn.setStatus(OrderState.NOT_PAID.getCode()+"");
		redisUtil.redisSaveObject(orderOn.getNumber(), orderOn, 1);
		redisUtil.redisSaveObject("test", 1, 1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OrderOn o = (OrderOn) SerializeUtils.deserialize(((String)redisUtil.redisQueryObject(orderOn.getNumber())).getBytes("UTF-8"));
		logger.info(((OrderOn)redisUtil.redisQueryObject(orderOn.getNumber())).getStatus());
		logger.info(redisUtil.redisQueryObject("test"));
	}
	
	@RequestMapping(value={"/redistx4"},method = RequestMethod.GET)
	public void testKeys() throws InterruptedException{
		//redisUtil.testKeys();
		ValueOperations<String, Object> ops = stringRedisTemplate.opsForValue();  
    	OrderOn oo = new OrderOn();oo.setNumber("adfdsfdsf");
        ops.getAndSet("pi_"+1+1, oo);
        OrderOn oo2 = new OrderOn();oo2.setNumber("bbbadfdsfdsf");
        ops.getAndSet("pi_"+1+2, oo2);
        Thread.sleep(3000);
    	Set<String> pi_keys = stringRedisTemplate.keys("\\xac\\xed\\x00\\x05t\\x00\\x05pi_11");
    	System.out.println("size:"+pi_keys.size());
    	for(String s:pi_keys){
    		System.out.println(s);
    	}
 
    	redisUtil.redisSaveInventory("pi_"+1+"_"+1+"_"+1, String.valueOf(2));
    	Thread.sleep(2000);
    	//System.out.println(redisUtil.redisQueryObject("pi_1_1_1"));
    	List<Object> rt = redisUtil.cutInventoryTx2(
				"pi_" + 1 + "_" + 1
						+ "_" + 1, 1);
    	System.out.println(((Long) rt.get(0)).intValue());
    	System.out.println(redisUtil.getKeys("*"));
	}
}
