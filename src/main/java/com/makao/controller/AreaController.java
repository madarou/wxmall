package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;

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
import com.makao.entity.Area;
import com.makao.entity.Catalog;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;

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
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	
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
	@RequestMapping(value = "/new/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("id") int superid, @RequestBody Area area) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			area.setClosed("yes");
			int res = this.areaService.insert(area);
			
			if(res==0){
				logger.info("增加area成功id=" + area.getId());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("增加area成功失败id=" + area.getId());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@RequestMapping(value = "/edit/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object edit(@PathVariable("id") int superid, @RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int areaId = paramObject.getInteger("areaId");
			String areaName = paramObject.getString("areaName");
			String longitude = paramObject.getString("longitude");
			String latitude = paramObject.getString("latitude");
			String phoneNumber = paramObject.getString("phoneNumber");

			Area area = this.areaService.getById(areaId);
			if(area!=null){
				area.setAreaName(areaName);
				area.setLongitude(longitude);
				area.setLatitude(latitude);
				area.setPhoneNumber(phoneNumber);
				int res = this.areaService.update(area);
				if(res==0){
					logger.info("修改area成功id=" + area.getId());
		        	jsonObject.put("msg", "200");
		        	return jsonObject;
				}
				else{
					logger.info("修改area成功失败id=" + area.getId());
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}

			}
			
		}
		jsonObject.put("msg", "201");
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
	
	@RequestMapping(value = "/queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll(@PathVariable("id") int superid) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			List<Area> areas = null;
			//则查询返回所有
			areas = this.areaService.queryAll();
			logger.info("查询所有area信息完成");
			jsonObject.put("msg", "200");
			jsonObject.put("areas", areas);//不用序列化，方便前端jquery遍历
			 return jsonObject;
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@RequestMapping(value = "/querybycity/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object queryByCity(@PathVariable("id") int superid, @RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int cityId = paramObject.getInteger("cityId");
			List<Area> areas = null;
			//则查询返回所有
			areas = this.areaService.queryByCityId(cityId);
			logger.info("查询所有area信息完成");
			jsonObject.put("msg", "200");
			jsonObject.put("areas", areas);//不用序列化，方便前端jquery遍历
			 return jsonObject;
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	

	/**
	 * @param vendorid
	 * @param catalog
	 * @return
	 * 增加分类
	 */
	@RequestMapping(value = "/vnewcatalog/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object addcatalog(@PathVariable("vendorid") int vendorid,@RequestBody Catalog catalog) {
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(vendorid);
		if(vendor!=null){
			int areaId = vendor.getAreaId();
			Area area = this.areaService.getById(areaId);
			String[] catalogStr = area.getCatalogs().split(",");
			for(String s : catalogStr){//检查重复性
				if(catalog.getName().equals(s.split("=")[0].trim())){
					jsonObject.put("msg", "202");
					return jsonObject;
				}
			}
			area.setCatalogs(area.getCatalogs()+","+catalog.getName()+"="+catalog.getSequence());
			int res = this.areaService.newCatalog(area);//这里之所以不用update(area)是因为对于catalog的增加还会涉及banner表的增加，而其他性的area的update并不需要
			
			if(res==0){
				logger.info("增加分类成功name=" + catalog.getName());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("增加分类失败name=" + catalog.getName());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		logger.info("增加分类失败name=" + catalog.getName());
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	/**
	 * @param vendorid
	 * @param catalog
	 * @return
	 * 修改分类，需要遍历Product表，修改所有catalog值为oldname的，更改Area表的内容,所有工作必须放在一个事务中完成
	 * 之所以这样设计，是因为更改Catalog的需求很低很低
	 */
	@RequestMapping(value = "/veditcatalog/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object editcatalog(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		JSONObject jsonObject = new JSONObject();
		String oldName = paramObject.getString("oldname");
		String newName = paramObject.getString("newname");
		String sequenceNew = paramObject.getString("sequence");
		
		Vendor vendor = this.vendorService.getById(vendorid);
		int areaId = vendor.getAreaId();
		int cityId = vendor.getCityId();
		String productTable = "Product_"+cityId+"_"+areaId;
		
		Area area = this.areaService.getById(areaId);
		String[] catalogStr = area.getCatalogs().split(",");
		for(String s : catalogStr){//检查重复性
			if(newName.equals(s.split("=")[0].trim())){
				jsonObject.put("msg", "202");
				return jsonObject;
			}
		}
		//area.setCatalogs(area.getCatalogs()+","+catalog.getName()+"="+catalog.getSequence());
		int res = this.areaService.editCatalog(area,oldName,newName,sequenceNew,productTable);
		
		if(res==0){
			logger.info("修改分类成功name=" + newName);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("修改分类失败name=" + newName);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	/**
	 * @param superid
	 * @param paramObject
	 * @return
	 * 下线区域
	 */
	@RequestMapping(value = "/sclose/{supervisorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sclose(@PathVariable("supervisorid") int superid,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int areaId = paramObject.getInteger("areaId");
			int res = this.areaService.closeArea(areaId);
			if(res==0){
				jsonObject.put("msg", "200");
				 return jsonObject;
			}
			else{
				jsonObject.put("msg", "201");
				 return jsonObject;
			}  
		}
		jsonObject.put("msg", "201");
	    return jsonObject;
    }
	
	@RequestMapping(value = "/sopen/{supervisorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sopen(@PathVariable("supervisorid") int superid,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int areaId = paramObject.getInteger("areaId");
			int res = this.areaService.openArea(areaId);
			if(res==0){
				jsonObject.put("msg", "200");
				 return jsonObject;
			}
			else{
				jsonObject.put("msg", "201");
				 return jsonObject;
			}  
		}
		jsonObject.put("msg", "201");
	    return jsonObject;
    }
	
	/**
	 * @param vendorid
	 * @param paramObject
	 * @return
	 * 删除Area中的分类，同时将Product中属于该类的product值设为默认分类
	 */
	@RequestMapping(value = "/vdeletecatalog/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object deletecatalog(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		JSONObject jsonObject = new JSONObject();
		String catalogName = paramObject.getString("name");
		
		Vendor vendor = this.vendorService.getById(vendorid);
		int areaId = vendor.getAreaId();
		int cityId = vendor.getCityId();
		String productTable = "Product_"+cityId+"_"+areaId;
		
		Area area = this.areaService.getById(areaId);
		int res = this.areaService.deleteCatalog(area,catalogName,productTable);
		
		if(res==0){
			logger.info("删除分类成功name=" + catalogName);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("删除分类失败name=" + catalogName);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView squeryall(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_areaManage");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<Area> areas = null;
		if(supervisor!=null){
			//则查询返回所有
			areas = this.areaService.queryAll();
			logger.info("查询所有area信息完成");
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token);   
		modelAndView.addObject("areas", areas);//不用序列化，方便前端jquery遍历
		return modelAndView;
    }

}
