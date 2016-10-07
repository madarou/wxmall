package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.Supervisor;
import com.makao.entity.TokenModel;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.ICityService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.EncryptUtils;
import com.makao.utils.MakaoConstants;
import com.makao.utils.TokenManager;
import com.makao.utils.TokenUtils;
import com.makao.weixin.utils.QRCodeUtil;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/supervisor")
public class SupervisorController {
	private static final Logger logger = Logger.getLogger(SupervisorController.class);
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private IAreaService areaService;
	@Resource
	private ICityService cityService;
	@Resource
	private IVendorService vendorService;
	@Autowired
	private TokenManager tokenManager;
	
	/**
	 * @param userName
	 * @param password
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"darou","password":"test"}' 'http://localhost:8080/wxmall/supervisor/login'
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody Object login(@RequestBody JSONObject paramObject,HttpServletRequest request, HttpServletResponse response)
	{
		String userName = paramObject.getString("userName");
		String password = paramObject.getString("password");
		JSONObject jsonObject = new JSONObject();
		//实际上线时用，要验证
//		if(userName==null || userName.trim().length() <=0 || password==null || password.trim().length() <=0){
//			jsonObject.put("msg", "用户或密码为空");
//	        return jsonObject; 
//		}
//		//密码加密，前端忙，前端加密暂时未作
//		logger.info("supervisor登录name=" + userName);
//		Supervisor supervisor = this.supervisorService.queryByName(userName);
//		if(supervisor!=null){
//			//if(supervisor.getPassword().equals(encryptedPassword)){
//			if(EncryptUtils.passwordEncryptor.checkPassword(password, supervisor.getPassword())){
//				jsonObject.put("msg", "登录成功");
//				//生成token
//				TokenModel tm = tokenManager.createToken(supervisor.getId(), "s");
//				
//				logger.info("supervisor登录成功name=" + userName);
//				jsonObject.put("supervisor", supervisor);//实验表明这里supervisor不需要json化
//				jsonObject.put("id", supervisor.getId());
//				jsonObject.put("token", tm.getToken());
//				return jsonObject;
//			}
//			else{
//				jsonObject.put("msg", "用户名或密码错误");
//				return jsonObject;
//			}
//		}
//		jsonObject.put("msg", "用户不存在");
//		return jsonObject;
		
		//测试时用，不验证
		jsonObject.put("msg", "登录成功");
		//String tokenstring = TokenUtils.setToken("supervisor");使用TokenManage替代TokenUtils
		TokenModel tm = tokenManager.createToken(1, "s");
		String tokenstring = tm.getToken();
		jsonObject.put("id", 1);
		jsonObject.put("token",tokenstring);
		//request.getServletContext().setAttribute(tokenstring, System.currentTimeMillis());
		return jsonObject;
	}
	@AuthPassport
	@RequestMapping(value="/index/{id:\\d+}",method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token, HttpServletRequest request)
	{
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_index");  
		if(token==null){
			return modelAndView;
		}
	    modelAndView.addObject("id", id);  	    
	    TokenModel tm = (TokenModel) request.getAttribute("tokenmodel");
	    if(tm==null){
	    	if(MakaoConstants.DEBUG){
	    		 modelAndView.addObject("token","dfsdfdfdfd");   
	    	    modelAndView.setViewName("s_index");  
	    		return modelAndView;
	    	}
	    	modelAndView.setViewName("s_login"); 
	    	return modelAndView;
	    }
	    modelAndView.addObject("token", tm.getToken());  
	    modelAndView.setViewName("s_index");  
		return modelAndView;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(@RequestParam(value="token", required=false) String token, HttpServletRequest request)
	{
		//从缓存中清除token对应的TokenModel
		return "s_login";
	}
	@RequestMapping(value="",method = RequestMethod.GET)
	public String main()
	{
		return "s_login";
	}
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Supervisor get(@PathVariable("id") Integer id)
	{
		logger.info("获取supervisor信息id=" + id);
		Supervisor Supervisor = (Supervisor)this.supervisorService.getById(id);
		return Supervisor;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.supervisorService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除supervisor信息成功id=" + id);
        	jsonObject.put("msg", "删除supervisor信息成功");
		}
		else{
			logger.info("删除supervisor信息失败id=" + id);
        	jsonObject.put("msg", "删除supervisor信息失败");
		}
        return jsonObject;
    }
	/**
	 * @param Supervisor
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"darou","password":"test"}' 'http://localhost:8080/wxmall/supervisor/new'
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Supervisor Supervisor) {
		String encryptedPassword = EncryptUtils.passwordEncryptor.encryptPassword(Supervisor.getPassword());
		System.out.println("*************"+encryptedPassword+"*************");
		Supervisor.setPassword(encryptedPassword);
		int res = this.supervisorService.insert(Supervisor);
		JSONObject jsonObject = new JSONObject();
		if(res>0){
			String ticket = QRCodeUtil.generateQRCode(res+10000);//二维码参数值只支持1-100000，为与vendor区分，加上10000
			Supervisor.setTicket(ticket);
			this.supervisorService.update(Supervisor);
			logger.info("增加supervisor成功id=" + res);
        	jsonObject.put("msg", "增加supervisor成功");
		}
		else{
			logger.info("增加supervisor失败");
        	jsonObject.put("msg", "增加supervisor失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Supervisor Supervisor) {
		int res = this.supervisorService.update(Supervisor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改supervisor信息成功id=" + Supervisor.getId());
        	jsonObject.put("msg", "修改supervisor信息成功");
		}
		else{
			logger.info("修改supervisor信息失败id=" + Supervisor.getId());
        	jsonObject.put("msg", "修改supervisor信息失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param name
	 * @return
	 * 因为用户名直接作为登录名，所以不能重复，查询时返回的不是list，sql使用精准查询
	 */
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		Supervisor supervisor = null;
		//则根据关键字查询
		supervisor = this.supervisorService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询supervisor信息完成");
        return supervisor;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Supervisor> Supervisors = null;
		//则查询返回所有
		Supervisors = this.supervisorService.queryAll();
		logger.info("查询所有supervisor信息完成");
        return Supervisors;
    }
	
	/**
	 * @param area
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"areaName":"张江","cityName":"上海","catalogs":"水果=食材=零食=省钱","cityId":1}' 'http://localhost:8080/wxmall/supervisor/newarea'
	 */
	@RequestMapping(value = "/newarea", method = RequestMethod.POST)
    public @ResponseBody
    Object newArea(@RequestBody Area area) {
		area.setProductTable(area.getAreaName()+"_"+area.getCityId()+"_product");
		area.setClosed("no");
		int res = this.areaService.insert(area);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加area成功id=" + area.getId());
        	jsonObject.put("msg", "增加area成功");
		}
		else{
			logger.info("增加area失败id=" + area.getId());
        	jsonObject.put("msg", "增加area失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param city
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"cityName":"上海"}' 'http://localhost:8080/wxmall/supervisor/newcity'
	 */
	@RequestMapping(value = "/newcity", method = RequestMethod.POST)
    public @ResponseBody
    Object newCity(@RequestBody City city) {
		int res = this.cityService.insert(city);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加city成功id=" + city.getId());
        	jsonObject.put("msg", "增加city成功");
		}
		else{
			logger.info("增加city失败id=" + city.getId());
        	jsonObject.put("msg", "增加city失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param vendor
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"马靠","areaId":1,"cityId":1,"cityArea":"上海张江"}' 'http://localhost:8080/wxmall/supervisor/newvendor'
	 */
	@RequestMapping(value = "/newvendor", method = RequestMethod.POST)
    public @ResponseBody
    Object newVendor(@RequestBody Vendor vendor) {
		vendor.setIsLock("no");
		vendor.setPassword(EncryptUtils.passwordEncryptor.encryptPassword("shygxx"));
		int res = this.vendorService.insert(vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加vendor成功id=" + vendor.getId());
        	jsonObject.put("msg", "增加vendor成功");
		}
		else{
			logger.info("增加vendor失败id=" + vendor.getId());
        	jsonObject.put("msg", "增加vendor失败");
		}
        return jsonObject;
    }
}
