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
import com.makao.entity.Supervisor;
import com.makao.service.ISupervisorService;

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
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Supervisor get(@PathVariable("id") Integer id)
	{
		logger.info("获取超级管理员信息id=" + id);
		Supervisor Supervisor = (Supervisor)this.supervisorService.getSupervisorById(id);
		return Supervisor;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.supervisorService.deleteSupervisor(id);
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
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Supervisor Supervisor) {
		int res = this.supervisorService.insertSupervisor(Supervisor);
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
		int res = this.supervisorService.updateSupervisor(Supervisor);
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
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Supervisor> Supervisors = null;
		//则根据关键字查询
		Supervisors = this.supervisorService.querySupervisorByName(name);
		logger.info("根据关键字: '"+name+"' 查询超级管理员信息完成");
        return Supervisors;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Supervisor> Supervisors = null;
		//则查询返回所有
		Supervisors = this.supervisorService.queryAllSupervisors();
		logger.info("查询所有超级管理员信息完成");
        return Supervisors;
    }
}
