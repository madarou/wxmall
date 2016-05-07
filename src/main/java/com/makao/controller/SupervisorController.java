package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.ICityService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.EncryptUtils;

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
	
	/**
	 * @param userName
	 * @param password
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"darou","password":"test"}' 'http://localhost:8080/wxmall/supervisor/login'
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody Object login(@RequestBody JSONObject paramObject)
	{
		String userName = paramObject.getString("userName");
		String password = paramObject.getString("password");
		JSONObject jsonObject = new JSONObject();
		if(userName==null || userName.trim().length() <=0 || password==null || password.trim().length() <=0){
			jsonObject.put("msg", "用户或密码为空");
	        return jsonObject; 
		}
		//密码加密，前端忙，前端加密暂时未作
		logger.info("超级管理员登录name=" + userName);
		Supervisor supervisor = this.supervisorService.queryByName(userName);
		if(supervisor!=null){
			//if(supervisor.getPassword().equals(encryptedPassword)){
			if(EncryptUtils.passwordEncryptor.checkPassword(password, supervisor.getPassword())){
				jsonObject.put("msg", "登录成功");
				logger.info("超级管理员登录成功name=" + userName);
				jsonObject.put("supervisor", supervisor);//实验表明这里supervisor不需要json化
			}
			else{
				jsonObject.put("msg", "密码错误");
			}
		}
		return jsonObject;
	}
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Supervisor get(@PathVariable("id") Integer id)
	{
		logger.info("获取超级管理员信息id=" + id);
		Supervisor Supervisor = (Supervisor)this.supervisorService.getById(id);
		return Supervisor;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.supervisorService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除超级管理员信息成功id=" + id);
        	jsonObject.put("msg", "删除超级管理员信息成功");
		}
		else{
			logger.info("删除超级管理员信息失败id=" + id);
        	jsonObject.put("msg", "删除超级管理员信息失败");
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
		if(res==0){
			logger.info("增加超级管理员成功id=" + Supervisor.getId());
        	jsonObject.put("msg", "增加超级管理员成功");
		}
		else{
			logger.info("增加超级管理员成功失败id=" + Supervisor.getId());
        	jsonObject.put("msg", "增加超级管理员失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Supervisor Supervisor) {
		int res = this.supervisorService.update(Supervisor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改超级管理员信息成功id=" + Supervisor.getId());
        	jsonObject.put("msg", "修改超级管理员信息成功");
		}
		else{
			logger.info("修改超级管理员信息失败id=" + Supervisor.getId());
        	jsonObject.put("msg", "修改超级管理员信息失败");
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
		logger.info("根据关键字: '"+name+"' 查询超级管理员信息完成");
        return supervisor;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Supervisor> Supervisors = null;
		//则查询返回所有
		Supervisors = this.supervisorService.queryAll();
		logger.info("查询所有超级管理员信息完成");
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
