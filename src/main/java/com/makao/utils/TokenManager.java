package com.makao.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
	
	public TokenModel createToken(int userid, String role){
		String s = java.util.UUID.randomUUID().toString();
		String userRole = "u";
		if("s".equals(role)){
			s = s + "s";
			userRole = "s";
		}
		else if("v".equals(role)){
			s = s + "v";
			userRole = "v";
		}
		TokenModel tm = new TokenModel(userid,role,s);
		System.out.println("generated token is: "+s);
		ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
		vop.set(userRole+userid, tm, MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
		return tm;
	}
	
	public boolean checkToken(String key, String tokenString) {
        if (key == null || "".equals(key.trim()) || tokenString==null || "".equals(tokenString.trim())) {
            return false;
        }
        ValueOperations<String, TokenModel> vop = redisTemplate.opsForValue();
        System.out.println("get TM from redis:"+vop.get(key));
        TokenModel tm = vop.get(key);
        if (tm == null || !tm.getToken().equals(tokenString)) {
        	System.out.println("check failed: token string not match");
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        System.out.println(redisTemplate.boundValueOps(key).expire(MakaoConstants.TOKEN_EXPIRE, TimeUnit.SECONDS));
        return true;
	}
}
