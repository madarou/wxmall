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
import com.makao.entity.City;
import com.makao.service.ICityService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/city")
public class CityController {
	private static final Logger logger = Logger.getLogger(CityController.class);
	@Resource
	private ICityService cityService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody City get(@PathVariable("id") Integer id)
	{
		logger.info("获取城市信息id=" + id);
		City city = (City)this.cityService.getCityById(id);
		return city;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.cityService.deleteCity(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除城市信息成功id=" + id);
        	jsonObject.put("msg", "删除城市信息成功");
		}
		else{
			logger.info("删除城市信息失败id=" + id);
        	jsonObject.put("msg", "删除城市信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody City city) {
		int res = this.cityService.insertCity(city);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加城市成功id=" + city.getId());
        	jsonObject.put("msg", "增加城市成功");
		}
		else{
			logger.info("增加城市成功失败id=" + city.getId());
        	jsonObject.put("msg", "增加城市失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody City city) {
		//注册用户的代码
		int res = this.cityService.updateCity(city);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改城市信息成功id=" + city.getId());
        	jsonObject.put("msg", "修改城市信息成功");
		}
		else{
			logger.info("修改城市信息失败id=" + city.getId());
        	jsonObject.put("msg", "修改城市信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<City> cities = null;
		//则根据关键字查询
		cities = this.cityService.queryCityByName(name);
		logger.info("根据关键字: '"+name+"' 查询城市信息完成");
        return cities;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<City> cities = null;
		//则查询返回所有
		cities = this.cityService.queryAllCities();
		logger.info("查询所有城市信息完成");
        return cities;
    }
}
