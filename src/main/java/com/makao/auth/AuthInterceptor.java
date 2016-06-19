package com.makao.auth;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.makao.controller.UserController;
import com.makao.utils.TokenManager;
import com.makao.utils.TokenUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月10日 简单的角色权限验证：
 *       1.因为restful是无状态的，为了记录登录情况，在登录时为用户生成一个token，对于标注了AuthPassport注解的
 *       请求，都会通过该拦截器拦截验证
 *       2.拦截器除了进行用户是否登录验证外，还进行角色权限验证。在token中记录用户名，身份(在其登录时访问的url路径中获取)，登录时间。
 *       3.验证流程： 对于访问AuthPassport注解的url，从request中获取其token，
 *       1)如果为空，拦截，返回到登录页面(从url路径中判断将其导向到supervisor还是vendor)
 *       2)token不为空，查找服务器缓存是否有该token
 *       A)没有，拦截，返回到登录页面(从url路径中判断将其导向到supervisor还是vendor)
 *       B)服务器有该token，解析出登录用户名，用户身份，登录时间 a)检验登录时间与当前时间差，判断token是否过期
 *       b)检查用户访问路径是否与服务器中该token解析出的用户身份对应
 * 
 *       对于访问Supervisor专用接口注解的url，出上面的检查外，还要必须是supervisor身份
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(AuthInterceptor.class);
	@Autowired
	private TokenManager tokenManager;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();// 获取到的是/product/1这类地址
		String method = request.getMethod();

		System.out.println("***********拦截器************");
		//测试时统一通过
		return true;
//		
//		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {//判断该请求的路径对应的方法有没有AuthPassport标注，即有没有需要验证
//			AuthPassport authPassport = ((HandlerMethod) handler)
//					.getMethodAnnotation(AuthPassport.class);
//
//			// 没有声明需要权限,或者声明不验证权限
//			if (authPassport == null || authPassport.validate() == false)
//				return true;
//			else {
//				response.setHeader("content-type", "text/html;charset=UTF-8");
//				response.setCharacterEncoding("UTF-8");
//				PrintWriter out = response.getWriter();
//				String page = "";
//				
//				// 要验证的都有token，没有token则失败
//				String token = request.getParameter("token");
//				if (token == null || "".equals(token.trim())) {
//					logger.info("*****"+url+" 的 "+method+" 方法需要验证token，但token不存在*****");
//					page="未登录";
//					out.write(page);
//					return false;
//				}
//				String type = "";// 请求是用户、区域管理还是超级管理
//				if (token.length() == 36) {
//					type = "u";
//				} else if (token.length() > 36
//						&& "v".equals(token.substring(token.length() - 1))) {// vendor请求
//					type = "v";
//				} else {
//					type = "s";
//				}
//				boolean isValid = false;
//				switch (type) {
//				case "u":
//					logger.info("handle user auth");
//					isValid =  tokenManager.checkUserToken(token);
//					if(!isValid){
//						page="需要重新登录";
//						out.write(page);
//					}
//					return isValid;
//				case "v":
//					logger.info("handle vendor auth");
//					isValid =  tokenManager.checkToken(token);
//					if(!isValid){
//						page="需要重新登录";
//						out.write(page);
//					}
//					return isValid;
//				case "s":
//					logger.info("handle supervisor auth");
//					isValid =  tokenManager.checkToken(token);
//					if(!isValid){
//						page="需要重新登录";
//						out.write(page);
//					}
//					return isValid;
//				default:
//					page="没有登录，需要重新登录";
//					out.write(page);
//					return false;
//				}
//				// 在这里实现自己的权限验证逻辑，这里模拟从servletContext中获取supervisor的登录信息
//				// Object o =
//				// request.getServletContext().getAttribute("supervisor");
//
//				/*
//				 * // 截取到请求地址的前几位，判断是supervisor登录还是vendor登录，从而定向对应登录页面让其重新登录 //
//				 * 返回到登录界面 String role = url.substring(getCharacterPosition(url,
//				 * 2), getCharacterPosition(url, 3)); String loginUrl = role +
//				 * "login"; // 开始验证token // 从header中获取token String token =
//				 * request.getHeader("token"); if (token == null ||
//				 * "".equals(token.trim())) {// 如果token为空 // 返回到登录界面
//				 * response.sendRedirect(loginUrl); return false; } else {//
//				 * 如果token不为空 Long loginTime = (Long)
//				 * request.getServletContext() .getAttribute(token);// 登录时设置的时间
//				 * if (loginTime == null) { response.sendRedirect(loginUrl);
//				 * return false; } if (System.currentTimeMillis() - loginTime >
//				 * token_pirate_interval) {// 超过了有效期
//				 * response.sendRedirect(loginUrl); return false; } if
//				 * ("s".equals(token.substring(token.length() - 1))) { if
//				 * (!"/supervisor".equals(role)) {// 因为role前面还有个/ // 如果角色不对，禁止访问
//				 * response.sendRedirect(loginUrl); return false; } } if
//				 * ("v".equals(token.substring(token.length() - 1))) { if
//				 * (!"/vendor".equals(role)) {// 因为role前面还有个/ // 如果角色不对，禁止访问
//				 * response.sendRedirect(loginUrl); return false; } } }
//				 */
//			}
//		} else {
//			return true;
//		}
//		// return true;
	}

	/**
	 * @param string
	 * @param position
	 * @return 在string里找'/'第position次出现的坐标
	 */
	private static int getCharacterPosition(String string, int position) {
		// 这里是获取"/"符号的位置
		Matcher slashMatcher = Pattern.compile("/").matcher(string);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			// 当"/"符号第三次出现的位置
			if (mIdx == position) {
				break;
			}
		}
		return slashMatcher.start();
	}
}
