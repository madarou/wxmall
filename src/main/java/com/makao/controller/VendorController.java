package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Supervisor;
import com.makao.entity.TokenModel;
import com.makao.entity.Vendor;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.EncryptUtils;
import com.makao.utils.MakaoConstants;
import com.makao.utils.RedisUtil;
import com.makao.utils.TokenManager;
import com.makao.utils.TokenUtils;
import com.makao.weixin.utils.QRCodeUtil;

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
	@Resource
	private ISupervisorService supervisorService;
	@Autowired
	private TokenManager tokenManager;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody Object login(@RequestBody JSONObject paramObject,HttpServletRequest request, HttpServletResponse response)
	{
		String userName = paramObject.getString("userName");
		String password = paramObject.getString("password");
		JSONObject jsonObject = new JSONObject();
		if(userName==null||password==null||"".equals(userName)||"".equals(password))
		{
			jsonObject.put("msg", "201");
			return jsonObject;
		}
		Vendor v = this.vendorService.queryVendorByName(userName);
		if(v==null){
			jsonObject.put("msg", "202");
			return jsonObject;
		}
		boolean pass = EncryptUtils.passwordEncryptor.checkPassword(password, v.getPassword());
		if(!pass){
			jsonObject.put("msg", "203");
			return jsonObject;
		}
		jsonObject.put("msg", "200");
		TokenModel tm = tokenManager.createToken(v.getId(), "v");

		String tokenstring = tm.getToken();
		jsonObject.put("id", v.getId());
		jsonObject.put("token", tokenstring);
		return jsonObject;
	}
	@AuthPassport
	@RequestMapping(value="/index/{id:\\d+}",method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token, HttpServletRequest request)
	{
		ModelAndView modelAndView = new ModelAndView();  
		 
		if(token==null){
			modelAndView.setViewName("v_login"); 
			return modelAndView;
		}
		modelAndView.addObject("id", id);  	    
	    TokenModel tm = (TokenModel) request.getAttribute("tokenmodel");
	    if(tm==null){
	    	if(MakaoConstants.DEBUG){
	    		 modelAndView.addObject("token","dfsdfdfdfd");   
	    	    modelAndView.setViewName("v_index");  
	    		return modelAndView;
	    	}
	    	modelAndView.setViewName("v_login"); 
	    	return modelAndView;
	    }
	    modelAndView.addObject("token", tm.getToken());  
	    modelAndView.setViewName("v_index");  
		return modelAndView;
	}
	
	
	@RequestMapping(value="/index/title/{id:\\d+}",method = RequestMethod.GET)
	public ModelAndView title(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token, HttpServletRequest request)
	{
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_title");  
		if(token==null){
			return modelAndView;
		}
	   Vendor v = this.vendorService.getById(id);
	   if(v!=null){
		   modelAndView.addObject("areaName", v.getCityName()+v.getAreaName());
	   }
	   else{
		   modelAndView.addObject("areaName", "美逆人生");
	   }
		return modelAndView;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(@RequestParam(value="token", required=false) String token)
	{	
		//从缓存中清除token
		tokenManager.deleteToken(token);
		return "v_login";
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
	
	@AuthPassport
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
	@AuthPassport
	@RequestMapping(value = "/new/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("id") int id, @RequestBody Vendor vendor) {
		Supervisor supervisor = this.supervisorService.getById(id);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			vendor.setIsLock("no");
			vendor.setIsDelete("no");
			vendor.setPassword(EncryptUtils.passwordEncryptor.encryptPassword(vendor.getPassword()));
			int res = this.vendorService.insert(vendor);
			if(res!=0){
				String ticket = QRCodeUtil.generateQRCode(res);
				vendor.setTicket(ticket);
				this.vendorService.update(vendor);
				logger.info("增加vendor成功id=" + vendor.getId());
	        	jsonObject.put("msg", "200");
	        	 return jsonObject;
			}
			else{
				logger.info("增加vendor成功失败id=" + vendor.getId());
	        	jsonObject.put("msg", "201");
	        	 return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	@AuthPassport
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody JSONObject paramObject,@RequestParam(value = "token", required = false) String token) {
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
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 解绑微信号
	 */
	@AuthPassport
	@RequestMapping(value = "/unbind/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object unbind(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int vendorid = paramObject.getInteger("vendorId");
		Vendor vendor = this.vendorService.getById(vendorid);
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			vendor.setOpenid(null);
			int res = this.vendorService.update(vendor);
			if(res==0){
				logger.info("解绑vendor微信成功id=" + vendor.getId());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("解绑vendor微信失败id=" + vendor.getId());
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
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView query_All(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		 ModelAndView modelAndView = new ModelAndView();  
			modelAndView.setViewName("s_vendorManage");  
			if(token==null){
				return modelAndView;
			}

		Supervisor supervisor = this.supervisorService.getById(id);
		List<Vendor> vendors = null;
		if(supervisor!=null){
			vendors = this.vendorService.queryAll();
			modelAndView.addObject("ticket", supervisor.getTicket());
		}
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token);   
	    modelAndView.addObject("vendors", vendors);  
	    
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
		Vendor vendor = (Vendor)this.vendorService.getById(id);
		if(vendor!=null){
			modelAndView.addObject("ticket", vendor.getTicket());
			modelAndView.addObject("id", id);
			modelAndView.addObject("token", token);
		}
		return modelAndView;
    }
}
