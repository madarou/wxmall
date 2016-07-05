package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Address;
import com.makao.entity.OrderOn;
import com.makao.service.IAddressService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
		Address address = (Address)this.addressService.getById(id);
		return address;
	}
	
	@AuthPassport
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id,@RequestParam(value="token", required=false) String token) {
        int res = this.addressService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除地址信息成功id=" + id);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("删除地址信息失败id=" + id);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@AuthPassport
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestParam(value="token", required=false) String token, @RequestBody Address Address) {
		int res = this.addressService.insert(Address);
		JSONObject jsonObject = new JSONObject();
		if(res!=0){
			logger.info("增加地址成功id=" + res);
        	jsonObject.put("msg", "200");
        	jsonObject.put("id", res);
		}
		else{
			logger.info("增加地址失败id=" + Address.getId());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/all/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object all(@PathVariable("userid") int userid) {
		JSONObject jsonObject = new JSONObject();
		List<Address> os = this.addressService.queryByUserId(userid);
		logger.info("查询用户id："+userid+"的所有地址");
		jsonObject.put("msg", "200");
		jsonObject.put("addresses", os);
		return jsonObject;
    }
	
	@RequestMapping(value = "/all/{cityid:\\d+}/{areaid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object cityareaAddresses(@PathVariable("cityid") int cityid, @PathVariable("areaid") int areaid, @PathVariable("userid") int userid) {
		JSONObject jsonObject = new JSONObject();
		List<Address> os = this.addressService.queryByCityAreaUserId(cityid,areaid,userid);
		logger.info("查询用户id："+userid+"在cityId="+cityid+"和areaId="+areaid+"下的所有地址");
		jsonObject.put("msg", "200");
		jsonObject.put("addresses", os);
		return jsonObject;
    }
	
	@AuthPassport
	@RequestMapping(value = "/edit/{addressid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody Object edit(@PathVariable("addressid") int addressid,@RequestParam(value="token", required=false) String token,
    		@RequestBody JSONObject paramObject) {
		
		String userName = paramObject.getString("userName");
		String phoneNumber = paramObject.getString("phoneNumber");
		String detailAddress = paramObject.getString("detailAddress");
		String label = paramObject.getString("label");
		String isDefault = paramObject.getString("isDefault");
		
		JSONObject jsonObject = new JSONObject();
		Address ad = this.addressService.getById(addressid);
		if(ad!=null){
			ad.setUserName(userName);
			ad.setPhoneNumber(phoneNumber);
			ad.setDetailAddress(detailAddress);
			ad.setLabel(label);
			ad.setIsDefault(isDefault);
			int res = this.addressService.update(ad);
			if(res==0){
				logger.info("更新用户："+userName+" 的id为: "+addressid+" 的地址成功");
				jsonObject.put("msg", "200");
				return jsonObject;
			}
		}
		logger.info("更新用户："+userName+" 的id为: "+addressid+" 的地址失败");
		jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	/**
	 * @param addressid
	 * @param token
	 * @param paramObject
	 * @return
	 * 将地址设为默认
	 */
	@AuthPassport
	@RequestMapping(value = "/default/{addressid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody Object defaultAdress(@PathVariable("addressid") int addressid,@RequestParam(value="token", required=false) String token,
    		@RequestBody JSONObject paramObject) {
		
		JSONObject jsonObject = new JSONObject();
		Address ad = this.addressService.getById(addressid);
		if(ad!=null){
			ad.setIsDefault("yes");
			int res = this.addressService.update(ad);
			if(res==0){
				logger.info("更新地址id为: "+addressid+" 的地址成功为默认地址成功");
				jsonObject.put("msg", "200");
				return jsonObject;
			}
		}
		logger.info("更新地址id为: "+addressid+" 的地址成功为默认地址失败");
		jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Address Address) {
		int res = this.addressService.update(Address);
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
		addresses = this.addressService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询地址信息完成");
        return addresses;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Address> addresses = null;
		//则查询返回所有
		addresses = this.addressService.queryAll();
		logger.info("查询所有地址信息完成");
        return addresses;
    }
}
