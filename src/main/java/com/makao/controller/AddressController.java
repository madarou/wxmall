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
import com.makao.entity.Address;
import com.makao.entity.User;
import com.makao.service.IAddressService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/address")
public class AddressController {
	private static final Logger logger = Logger.getLogger(AddressController.class);
	@Resource
	private IAddressService addressService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Address get(@PathVariable("id") Integer id)
	{
		logger.info("获取地址信息id=" + id);
		Address address = (Address)this.addressService.getAddressById(id);
		return address;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.addressService.deleteAddress(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除地址信息成功id=" + id);
        	jsonObject.put("msg", "删除地址信息成功");
		}
		else{
			logger.info("删除地址信息失败id=" + id);
        	jsonObject.put("msg", "删除地址信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Address Address) {
		int res = this.addressService.insertAddress(Address);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加地址成功id=" + Address.getId());
        	jsonObject.put("msg", "增加地址成功");
		}
		else{
			logger.info("增加地址成功失败id=" + Address.getId());
        	jsonObject.put("msg", "增加地址失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Address Address) {
		int res = this.addressService.updateAddress(Address);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改地址信息成功id=" + Address.getId());
        	jsonObject.put("msg", "修改地址信息成功");
		}
		else{
			logger.info("修改地址信息失败id=" + Address.getId());
        	jsonObject.put("msg", "修改地址信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Address> addresses = null;
		//则根据关键字查询
		addresses = this.addressService.queryAddressByName(name);
		logger.info("根据关键字: '"+name+"' 查询地址信息完成");
        return addresses;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Address> addresses = null;
		//则查询返回所有
		addresses = this.addressService.queryAllAddresss();
		logger.info("查询所有地址信息完成");
        return addresses;
    }
}
