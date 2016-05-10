package com.makao.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月10日
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        //if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
        	System.out.println("***********拦截器if************");
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            
            //没有声明需要权限,或者声明不验证权限
            if(authPassport == null || authPassport.validate() == false)
                return true;
            else{                
                //在这里实现自己的权限验证逻辑，这里模拟从servletContext中获取supervisor的登录信息
            	Object o = request.getServletContext().getAttribute("supervisor");
            	System.out.println("********从request中获取:"+o+"********");
                if(o!=null)//如果验证成功返回true
                    return true;
                else//如果验证失败
                {
                    //返回到登录界面
                    response.sendRedirect("login");
                    return false;
                }       
            }
        //}
//        else{
//        	System.out.println("***********拦截器else************");
//        	response.sendRedirect("login");
//            return false;   
//        }
     }
}
