package com.makao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.IVendorService;
import com.makao.utils.EncryptUtils;
import com.makao.utils.TokenUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/vendor")
public class VendorController {
	private static final Logger logger = Logger.getLogger(VendorController.class);
	@Resource
	private IVendorService vendorService;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody Object login(@RequestBody JSONObject paramObject,HttpServletRequest request, HttpServletResponse response)
	{
		String userName = paramObject.getString("userName");
		String password = paramObject.getString("password");
		JSONObject jsonObject = new JSONObject();
		System.out.println(userName);
		System.out.println(password);
		jsonObject.put("msg", "登录成功");
		String tokenstring = TokenUtils.setToken("vendor");
		jsonObject.put("id", 1);
		jsonObject.put("token",tokenstring);
		request.getServletContext().setAttribute(tokenstring, System.currentTimeMillis());
		return jsonObject;
	}

	@RequestMapping(value="/index/{id:\\d+}",method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token, HttpServletRequest request)
	{
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_index");  
		if(token==null){
			return modelAndView;
		}
		System.out.println(id);
		System.out.println(token);
		System.out.println(request.getServletContext().getAttribute(token));
		
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token);   
		return modelAndView;
	}
	
	@RequestMapping(value="",method = RequestMethod.GET)
	public String main()
	{
		return "v_login";
	}
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Vendor get(@PathVariable("id") Integer id)
	{
		logger.info("获取vendor信息id=" + id);
		Vendor Vendor = (Vendor)this.vendorService.getById(id);
		return Vendor;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.vendorService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除vendor信息成功id=" + id);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("删除vendor信息失败id=" + id);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	/**
	 * @param Vendor
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"马靠","areaId":1,"cityId":1,"cityArea":"上海张江"}' 'http://localhost:8080/wxmall/vendor/new'
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Vendor vendor) {
		vendor.setIsLock("no");
		vendor.setIsDelete("no");
		vendor.setPassword(EncryptUtils.passwordEncryptor.encryptPassword("shygxx"));
		int res = this.vendorService.insert(vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加vendor成功id=" + vendor.getId());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加vendor成功失败id=" + vendor.getId());
        	jsonObject.put("msg", "200");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody JSONObject paramObject) {
		int id = paramObject.getIntValue("id");
		String vendorname = paramObject.getString("userName");
		String password = paramObject.getString("password");
		Vendor vendor = this.vendorService.getById(id);
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			vendor.setUserName(vendorname);
			if(!"".equals(password))
				vendor.setPassword(EncryptUtils.passwordEncryptor.encryptPassword(password));
			int res = this.vendorService.update(vendor);
			if(res==0){
				logger.info("修改vendor信息成功id=" + vendor.getId());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("修改vendor信息失败id=" + vendor.getId());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Vendor> Vendors = null;
		//则根据关键字查询
		Vendors = this.vendorService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询vendor信息完成");
        return Vendors;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Vendor> Vendors = null;
		//则查询返回所有
		Vendors = this.vendorService.queryAll();
		logger.info("查询所有vendor信息完成");
        return Vendors;
    }
	
	@RequestMapping(value = "/s_queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object query_All() {
		List<Vendor> vendors = null;
		vendors = this.vendorService.queryAll();
		logger.info("查询所有vendor信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("vendors", vendors);  
	    modelAndView.setViewName("s_vendorManage");  
	    return modelAndView;
    }
	
	@RequestMapping(value = "/v_bindwx/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView bindWeixin(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_bindWeixin");
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		return modelAndView;
    }
}
