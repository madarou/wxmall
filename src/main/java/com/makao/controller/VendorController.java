package com.makao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.IVendorService;
import com.makao.utils.EncryptUtils;

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
        	jsonObject.put("msg", "删除vendor信息成功");
		}
		else{
			logger.info("删除vendor信息失败id=" + id);
        	jsonObject.put("msg", "删除vendor信息失败");
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
		vendor.setPassword(EncryptUtils.passwordEncryptor.encryptPassword("shygxx"));
		int res = this.vendorService.insert(vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加vendor成功id=" + vendor.getId());
        	jsonObject.put("msg", "增加vendor成功");
		}
		else{
			logger.info("增加vendor成功失败id=" + vendor.getId());
        	jsonObject.put("msg", "增加vendor失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Vendor Vendor) {
		int res = this.vendorService.update(Vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改vendor信息成功id=" + Vendor.getId());
        	jsonObject.put("msg", "修改vendor信息成功");
		}
		else{
			logger.info("修改vendor信息失败id=" + Vendor.getId());
        	jsonObject.put("msg", "修改vendor信息失败");
		}
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
	
	@RequestMapping(value = "/squeryall", method = RequestMethod.GET)
    public @ResponseBody
    Object query_All() {
		List<User> orderOns = new ArrayList<User>();
		User oo = new User();
		oo.setAddress("ddddddddd");
		orderOns.add(oo);
		logger.info("查询所有有效订单信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("ordersOn", orderOns);  
	    modelAndView.setViewName("s_vendorManage");  
	    return modelAndView;
    }
}
