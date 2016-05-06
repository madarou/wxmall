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
import com.makao.entity.LoginLog;
import com.makao.service.ILoginLogService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController {
	private static final Logger logger = Logger.getLogger(LoginLogController.class);
	@Resource
	private ILoginLogService loginLogService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody LoginLog get(@PathVariable("id") Integer id)
	{
		logger.info("获取登录记录信息id=" + id);
		LoginLog LoginLog = (LoginLog)this.loginLogService.getLoginLogById(id);
		return LoginLog;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.loginLogService.deleteLoginLog(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除登录记录信息成功id=" + id);
        	jsonObject.put("msg", "删除登录记录信息成功");
		}
		else{
			logger.info("删除登录记录信息失败id=" + id);
        	jsonObject.put("msg", "删除登录记录信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody LoginLog LoginLog) {
		int res = this.loginLogService.insertLoginLog(LoginLog);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加登录记录成功id=" + LoginLog.getId());
        	jsonObject.put("msg", "增加登录记录成功");
		}
		else{
			logger.info("增加登录记录成功失败id=" + LoginLog.getId());
        	jsonObject.put("msg", "增加登录记录失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody LoginLog LoginLog) {
		int res = this.loginLogService.updateLoginLog(LoginLog);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改登录记录信息成功id=" + LoginLog.getId());
        	jsonObject.put("msg", "修改登录记录信息成功");
		}
		else{
			logger.info("修改登录记录信息失败id=" + LoginLog.getId());
        	jsonObject.put("msg", "修改登录记录信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<LoginLog> LoginLogs = null;
		//则根据关键字查询
		LoginLogs = this.loginLogService.queryLoginLogByName(name);
		logger.info("根据关键字: '"+name+"' 查询登录记录信息完成");
        return LoginLogs;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<LoginLog> LoginLogs = null;
		//则查询返回所有
		LoginLogs = this.loginLogService.queryAllLoginLogs();
		logger.info("查询所有登录记录信息完成");
        return LoginLogs;
    }
}
