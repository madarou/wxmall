package com.makao.utils;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.makao.entity.TokenModel;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月18日
 */
/**
 *
 */
@Component
public class TokenManager {
	@Autowired
	private RedisTemplate<String, TokenModel> redisTemplate;
	private static final Logger logger = Logger.getLogger(TokenManager.class);
	
	/**
	 * @param userid
	 * @param role
	 * @return
	 * 给supervisor和vendor创建token
	 */
	public TokenModel createToken(int userid, String role){
		String s = java.util.UUID.randomUUID().toString();
		String userRole = "v";
		if("s".equals(role)){
			s = s + "s";
			userRole = "s";
		}
		else if("v".equals(role)){
			s = s + "v";
		}
		TokenModel tm = new TokenModel(userid,userRole,s);
		logger.info("generated token is: "+s);
		ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
		vop.set(s, tm, MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS);
		return tm;
	}
	
	/**
	 * @param tokenString
	 * @return
	 * 根据tokenString从缓存获取TokenModel
	 */
	public TokenModel getToken(String tokenString){
		if (tokenString==null || "".equals(tokenString.trim())) {
            return null;
        }
        ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
        TokenModel tm = vop.get(tokenString.trim());
        logger.info("get TM from redis:"+tm);
        return tm;
	}
	
	/**
	 * @param tm
	 * @param tokenString
	 * @return
	 * 使用前面getToken方法获取到的TokenModel进行验证token
	 */
	public boolean checkToken(TokenModel tm, String tokenString){
		if (tm == null || !tm.getToken().equals(tokenString.trim())) {
        	logger.info("check failed: token string not match");
            return false;
        }
		 //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS));
        return true;
	}
	/**
	 * @param key
	 * @param tokenString
	 * @return
	 * 验证管理员token
	 * 这个方法是前面两个方法的综合，之所以使用前面两个方法是因为：
	 * 拆分开后可以通过getToken()向外部返回TokenModel，紧接着验证成功后可以将该TokenModel
	 * 放入request中供后面的请求使用里面的诸如userid，openid等信息
	 */
	public boolean checkToken(String tokenString) {
        if (tokenString==null || "".equals(tokenString.trim())) {
            return false;
        }
        ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
        TokenModel tm = vop.get(tokenString.trim());
        logger.info("get TM from redis:"+tm);
        if (tm == null || !tm.getToken().equals(tokenString.trim())) {
        	logger.info("check failed: token string not match");
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS));
        return true;
	}
	
	/***************************/
	/*	以下是用户User的token操作*/
	/***************************/
	/**
	 * @param userid
	 * @param openid
	 * @return
	 * 创建普通用户的token，只有36位，必须有微信的openid
	 */
	public TokenModel createUserToken(int userid, String openid){
		String s = java.util.UUID.randomUUID().toString();
		String userRole = "u";
		TokenModel tm = new TokenModel(userid,userRole,s);
		tm.setOpenid(openid);
		logger.info("generated user token is: "+s);
		ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
		vop.set(s, tm, MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS);
		return tm;
	}
	
	/**
	 * @param tokenString
	 * @return
	 * 根据tokenString从缓存获取TokenModel
	 * 该方法跟admin的getToken是一样的，由于checkToken有些不同，所以这里还是把它分开
	 */
	public TokenModel getUserToken(String tokenString){
		if (tokenString==null || "".equals(tokenString.trim())) {
            return null;
        }
        ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
        TokenModel tm = vop.get(tokenString.trim());
        logger.info("get TM from redis:"+tm);
        return tm;
	}
	
	/**
	 * @param tm
	 * @param tokenString
	 * @return
	 * 使用前面getToken方法获取到的TokenModel进行验证token
	 */
	public boolean checkUserToken(TokenModel tm, String tokenString){
		if (tm == null || !tm.getToken().equals(tokenString.trim())) {
        	logger.info("check failed: token string not match");
            return false;
        }
		if(tm.getOpenid()==null || "".equals(tm.getOpenid().trim())){
        	logger.info("check user token failed: no openid found");
        	return false;
        }
		 //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS));
        return true;
	}
	
	/**
	 * @param key
	 * @param tokenString
	 * @return
	 * 验证普通用户token
	 */
	public boolean checkUserToken(String tokenString) {
        if (tokenString==null || "".equals(tokenString.trim())) {
            return false;
        }
        ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
        TokenModel tm = vop.get(tokenString.trim());
        if (tm == null || !tm.getToken().equals(tokenString.trim())) {
        	logger.info("check user token failed: token string not match");
            return false;
        }
        if(tm.getOpenid()==null || "".equals(tm.getOpenid().trim())){
        	logger.info("check user token failed: no openid found");
        	return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.HOURS));
        return true;
	}
	
	public void deleteToken(String key){
		redisTemplate.delete(key);
	}
}
