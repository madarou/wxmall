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
import com.makao.service.IAreaService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/area")
public class AreaController {
	private static final Logger logger = Logger.getLogger(AreaController.class);
	@Resource
	private IAreaService areaService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Area get(@PathVariable("id") Integer id)
	{
		logger.info("获取area信息id=" + id);
		Area Area = (Area)this.areaService.getById(id);
		return Area;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.areaService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除area信息成功id=" + id);
        	jsonObject.put("msg", "删除area信息成功");
		}
		else{
			logger.info("删除area信息失败id=" + id);
        	jsonObject.put("msg", "删除area信息失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param Area
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"areaName":"张江","cityName":"上海","catalogs":"水果=食材=零食=省钱","cityId":1}' 'http://localhost:8080/wxmall/area/new'
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Area area) {
		area.setProductTable(area.getAreaName()+"_"+area.getCityId()+"_product");
		area.setClosed("no");
		int res = this.areaService.insert(area);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加area成功id=" + area.getId());
        	jsonObject.put("msg", "增加area成功");
		}
		else{
			logger.info("增加area成功失败id=" + area.getId());
        	jsonObject.put("msg", "增加area失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Area Area) {
		int res = this.areaService.update(Area);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改area信息成功id=" + Area.getId());
        	jsonObject.put("msg", "修改area信息成功");
		}
		else{
			logger.info("修改area信息失败id=" + Area.getId());
        	jsonObject.put("msg", "修改area信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Area> areas = null;
		//则根据关键字查询
		areas = this.areaService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询area信息完成");
        return areas;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Area> areas = null;
		//则查询返回所有
		areas = this.areaService.queryAll();
		logger.info("查询所有area信息完成");
        return areas;
    }
}