package com.makao.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.makao.controller.TestController;
import com.makao.entity.OrderOn;
import com.makao.entity.TokenModel;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月24日
 */
@Component
public class RedisUtil {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private static final Logger logger = Logger.getLogger(RedisUtil.class);
	
	/** 
     * @param <T> 
     * @description 获取 redis中的对象 
     * @param 
     * @return void 
     * @throws 
     */  
    public static <T> T redisQueryObject(final RedisTemplate<String, Object> redisTemplate, final String key) {  
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        return (T) ops.get(key);  
    }  
    /** 
     * @description 删除redis中指定的key 
     * @param 
     * @return void 
     * @throws 
     */  
    public static void redisDeleteKey(final RedisTemplate<String, Object> redisTemplate, final String key) {  
        redisTemplate.delete(key);  
    }  
  
    /** 
     * @description 保存对象到redis 
     * @param key 
     * @return object 
     * @throws 
     */  
    public static void redisSaveObject(final RedisTemplate<String, Object> redisTemplate, final String key,  
            final Object object) {  
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        ops.getAndSet(key, object);  
    }  
  
    /** 
     * @description 保存对象到redis 
     * @param key 
     * @return object 
     * @throws 
     */  
    public static void redisSaveObject(final RedisTemplate<String, Object> redisTemplate, final String key,  
            final Object object, int time) {  
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        ops.set(key, object, time, TimeUnit.MINUTES);  
    }  
  
    /** 
     * @description redis保存数据到list 
     * @param 
     * @return void 
     * @throws 
     */  
    public static void redisSaveList(final RedisTemplate<String, Object> redisTemplate, final String key,  
            final Object object, int count) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        ops.rightPush(key, object);  
        if (0 < count)  
            ops.trim(key, 0, count);  
    }  
  
    /** 
     * @param <T> 
     * @description 获取 redis中的list对象 
     * @param 
     * @return 
     * @throws 
     */  
    public static <T> List<T> redisQueryList(final RedisTemplate<String, Object> redisTemplate, final String key,  
            Class<T> claxx) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        List<Object> tempList = ops.range(key, 0, -1);  
        if (CollectionUtils.isEmpty(tempList)) {  
            return null;  
        }  
  
        List<T> resultList = new ArrayList<>();  
        for (Object serl : tempList) {  
            resultList.add(claxx.cast(serl));  
        }  
        tempList.clear();  
        return resultList;  
    }  
  
    /** 
     * @description redis 删除列表中的对象 
     * @param 
     * @return void 
     * @throws 
     */  
    public static void redisDelListValue(final RedisTemplate<String, Object> redisTemplate, final String key,  
            final Serializable value) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        ops.remove(key, 0, value);  
    }  
  
    /** 
     * @param <T> 
     * @description 获取 redis中的对象 
     * @param 
     * @return void 
     * @throws 
     */  
    public <T> T redisQueryObject(final String key) {  
        //ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        //return (T) ops.get(key);  
    	//在spring中redis的string 类型，当值采用JdkSerializationRedisSerializer序列化操作后，使用get获取失败。
    	//这是redis的一个bug.有两种解决办法。第一，采用StringRedisSerializer序列化其值。第二，采用boundValueOps(key).get(0,-1)获取计数key的值
    	try{
    		ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
            return (T) ops.get(key);
    	}catch(Exception e){
    		logger.info("get value by get(0,-1)");
    		logger.error(e.getMessage(),e);
    		return (T)redisTemplate.boundValueOps(key).get(0, -1);
    	}
    }  
  
    /** 
     * @description 删除redis中指定的key 
     * @param 
     * @return void 
     * @throws 
     */  
    public void redisDeleteKey(final String key) {  
        redisTemplate.delete(key);  
    }  
  
    /** 
     * @description 保存对象到redis 
     * @param key 
     * @return object 
     * @throws 
     */  
    public void redisSaveObject(final String key, final Object object) {  
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        ops.getAndSet(key, object);  
    }  
    
    /**
     * @param key
     * @param object
     * 为了使inventory能够通过increment方法加减,StringRedisSerializer为其valueSerializer才行,
     * 取值的时候不需要再声明
     */
    public void redisSaveInventory(final String key, final Object object) {  
    	redisTemplate.setValueSerializer(new StringRedisSerializer());
    	redisTemplate.afterPropertiesSet();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        ops.getAndSet(key, object);  
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    	redisTemplate.afterPropertiesSet();
    }  
  
    /** 
     * @description 保存对象到redis 
     * @param key 
     * @return object 
     * @throws 
     */  
    public void redisSaveObject(final String key, final Object object, int time) {  
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
        ops.set(key, object, time, TimeUnit.MINUTES);  
    }  
  
    /** 
     * @description redis保存数据到list 
     * @param 
     * @return void 
     * @throws 
     */  
    public void redisSaveList(final String key, final Object object, int count) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        ops.rightPush(key, object);  
        if (0 < count)  
            ops.trim(key, 0, count);  
    }  
  
    /** 
     * @param <T> 
     * @description 获取 redis中的list对象 
     * @param 
     * @return 
     * @throws 
     */  
    public <T> List<T> redisQueryList(final String key, Class<T> claxx) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        List<Object> tempList = ops.range(key, 0, -1);  
        if (CollectionUtils.isEmpty(tempList)) {  
            return null;  
        }  
  
        List<T> resultList = new ArrayList<>();  
        for (Object serl : tempList) {  
            resultList.add(claxx.cast(serl));  
        }  
        tempList.clear();  
        return resultList;  
    }  
  
    /** 
     * @description redis 删除列表中的对象 
     * @param 
     * @return void 
     * @throws 
     */  
    public void redisDelListValue(final String key, final Serializable value) {  
        ListOperations<String, Object> ops = redisTemplate.opsForList();  
        ops.remove(key, 0, value);  
    }  
    
    /**
     * @param key
     * @param productNum
     * @return
     * 试图扣除producNum个key所对应的inventory，最后直到扣除成功才返回
     */
    public List<Object> cutInventoryTx(String key, int productNum){
		//execute a transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
		  public List<Object> execute(RedisOperations operations) throws DataAccessException {
			  List<Object> rt = null;
			  while(true){
				  operations.watch(key);
				  operations.multi();
				  //必须用increment的才能在exec()方法得到之后的inventory值，使用set(key,value)方法没有返回，
				  operations.opsForValue().increment(key, 0-productNum);
				  //				    logger.info("decreasing inventory to: "+ inventory);
				  rt = operations.exec();
				  logger.info("exec cut inventory "+key+" by "+productNum+" rt: " + rt);
				  if(rt!=null){
					  //int inventory = (int) rt.get(0);
					  logger.info("exec success rt: " + rt.get(0));
					  break;
				  }
			  }
		    return rt;
		  }
		});
		return txResults;
	}
    
    public List<Object> cutInventoryTx2(String key, int productNum){
		//execute a transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
		  public List<Object> execute(RedisOperations operations) throws DataAccessException {
			  List<Object> rt = null;
			  while(true){
				  operations.watch(key);
				  operations.multi();
				  //必须用increment的才能在exec()方法得到之后的inventory值，使用set(key,value)方法没有返回，
				  operations.opsForValue().increment(key, 0-productNum);
				  //				    logger.info("decreasing inventory to: "+ inventory);
				  rt = operations.exec();
				  logger.info("exec cut inventory "+key+" by "+productNum+" rt: " + rt);
				  if(rt!=null){
					  //int inventory = (int) rt.get(0);
					  logger.info("exec success rt: " + rt.get(0));
					  break;
				  }
			  }
		    return rt;
		  }
		});
		return txResults;
	}
    
    /**
     * @param key
     * @param productNum
     * @return
     * 为key对应的商品在缓存里面增加productNum个库存，直到增加成功为止
     */
    public List<Object> addInventoryTx(String key, int productNum){
		//execute a transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
		  public List<Object> execute(RedisOperations operations) throws DataAccessException {
			  List<Object> rt = null;
			  while(true){
				  operations.watch(key);
				  operations.multi();
				  //必须用increment的才能在exec()方法得到之后的inventory值，使用set(key,value)方法没有返回，
				  operations.opsForValue().increment(key, productNum);
				  rt = operations.exec();
				  logger.info("exec add inventory "+key+" by "+productNum+" rt: " + rt);
				  if(rt!=null){
					  logger.info("exec success rt: " + rt.get(0));
					  break;
				  }
			  }
		    return rt;
		  }
		});
		return txResults;
	}
    
    public List<String> getKeys(String pattern){
    	if(pattern==null||"".equals(pattern))
    		return null;
    	RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
    	Set<byte[]> keys = redisConnection.keys(pattern.getBytes());
    	Iterator<byte[]> it = keys.iterator();
    	List<String> keylist = new ArrayList<String>();
    	while(it.hasNext()){
    	    byte[] data = (byte[])it.next();
    	    String k = new String(data, 0, data.length);
    	    keylist.add(k);
    	    System.out.println(k);
    	}
    	redisConnection.close();
    	return keylist;
    }
    
    public static void main(String[] args){
    	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    	ValueOperations<String, Object> ops = redisTemplate.opsForValue();  
    	OrderOn oo = new OrderOn();oo.setNumber("adfdsfdsf");
        ops.getAndSet("pi_"+1+1, oo);
        OrderOn oo2 = new OrderOn();oo2.setNumber("bbbadfdsfdsf");
        ops.getAndSet("pi_"+1+1, oo2);
    	Set<String> pi_keys = redisTemplate.keys("pi_*");
    	for(String s:pi_keys){
    		System.out.println(s);
    	}
    }
}
