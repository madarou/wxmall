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
		vop.set(s, tm, MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
		return tm;
	}
	
	/**
	 * @param key
	 * @param tokenString
	 * @return
	 * 验证管理员token
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
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS));
        return true;
	}
	
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
		vop.set(s, tm, MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
		return tm;
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
        System.out.println(redisTemplate.boundValueOps(tokenString.trim()).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS));
        return true;
	}
	
	public void deleteToken(String key){
		redisTemplate.delete(key);
	}
}
