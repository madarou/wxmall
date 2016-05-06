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
import com.makao.entity.Vendor;
import com.makao.service.IVendorService;

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
		logger.info("获取区域管理员信息id=" + id);
		Vendor Vendor = (Vendor)this.vendorService.getVendorById(id);
		return Vendor;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.vendorService.deleteVendor(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除区域管理员信息成功id=" + id);
        	jsonObject.put("msg", "删除区域管理员信息成功");
		}
		else{
			logger.info("删除区域管理员信息失败id=" + id);
        	jsonObject.put("msg", "删除区域管理员信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Vendor Vendor) {
		int res = this.vendorService.insertVendor(Vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加区域管理员成功id=" + Vendor.getId());
        	jsonObject.put("msg", "增加区域管理员成功");
		}
		else{
			logger.info("增加区域管理员成功失败id=" + Vendor.getId());
        	jsonObject.put("msg", "增加区域管理员失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Vendor Vendor) {
		int res = this.vendorService.updateVendor(Vendor);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改区域管理员信息成功id=" + Vendor.getId());
        	jsonObject.put("msg", "修改区域管理员信息成功");
		}
		else{
			logger.info("修改区域管理员信息失败id=" + Vendor.getId());
        	jsonObject.put("msg", "修改区域管理员信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Vendor> Vendors = null;
		//则根据关键字查询
		Vendors = this.vendorService.queryVendorByName(name);
		logger.info("根据关键字: '"+name+"' 查询区域管理员信息完成");
        return Vendors;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Vendor> Vendors = null;
		//则查询返回所有
		Vendors = this.vendorService.queryAllVendors();
		logger.info("查询所有区域管理员信息完成");
        return Vendors;
    }
}
