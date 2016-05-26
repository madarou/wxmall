package com.makao.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.Supervisor;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.ISupervisorService;
import com.makao.service.IUserService;
import com.makao.service.IVendorService;

@Controller
@RequestMapping("/user")
public class UserController {
	/** 日志实例 */
    private static final Logger logger = Logger.getLogger(UserController.class);
	@Resource
	private IUserService userService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	
//	@RequestMapping("/showUser")
//	public String toIndex(HttpServletRequest request,Model model){
//		int userId = Integer.parseInt(request.getParameter("id"));
//		User user = this.userService.getUserById(userId);
//		model.addAttribute("user", user);
//		return "showUser";
//	}
	/**
	 * @param id
	 * @return
	 * curl -X GET 'http://localhost:8080/wxmall/user/1'
	 */
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody User get(@PathVariable("id") Integer id)
	{
		logger.info("获取人员信息id=" + id);
		User user = (User)this.userService.getById(id);
		return user;
	}
	
	/**
	 * @param id
	 * @return
	 * curl -X DELETE 'http://localhost:8080/wxmall/user/2'
	 */
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        //实际删除的代码放这里
        int res = this.userService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除人员信息成功id=" + id);
        	jsonObject.put("msg", "删除人员信息成功");
		}
		else{
			logger.info("删除人员信息失败id=" + id);
        	jsonObject.put("msg", "删除人员信息失败");
		}
        return jsonObject;
    }
	
	//不要用/add,可能会被当成广告被浏览器屏蔽
	/**
	 * @param user
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"userName":"darou","password":"test","age":13}' 'http://localhost:8080/wxmall/user/new'
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody User user) {
		//注册用户的代码
		user.setRegistTime(new Timestamp(System.currentTimeMillis()));
		int res = this.userService.insert(user);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("注册人员信息成功id=" + user.getId());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("注册人员信息失败id=" + user.getId());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	
	/**
	 * @param user
	 * @return
	 * curl -l -H "Content-type: application/json" -X POST -d '{"id":3,"userName":"darou","password":"test2","age":14}' 'http://localhost:8080/wxmall/user/update'
	 * 注意update时要传id才能确定update哪个，且像age这种updatable=false的字段不会被新值修改
	 */
	@RequestMapping(value = "/update/{superid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@PathVariable("superid") int superid,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int id = paramObject.getIntValue("id");
			String userName = paramObject.getString("userName");
			String phoneNumber = paramObject.getString("phoneNumber");
			int point = paramObject.getIntValue("point");
			String address = paramObject.getString("address");
			String rank = paramObject.getString("rank");
			User user = this.userService.getById(id);
			if(user!=null){
				user.setUserName(userName);
				user.setPhoneNumber(phoneNumber);
				user.setPoint(point);
				user.setAddress(address);
				user.setRank(rank);
				int res = this.userService.update(user);
				if(res==0){
					logger.info("修改人员信息成功id=" + user.getId());
		        	jsonObject.put("msg", "200");
		        	return jsonObject;
				}
				else{
					logger.info("修改人员信息失败id=" + user.getId());
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	/**
	 * @param name
	 * @return
	 * curl -X GET 'http://localhost:8080/wxmall/user/query/da'
	 */
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<User> users = null;
		//则根据关键字查询
		users = this.userService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询人员信息完成");
        return users;
    }
	
	/**
	 * @param name
	 * @return
	 * curl -X GET 'http://localhost:8080/wxmall/user/query'
	 */
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<User> users = null;
		//则查询返回所有
		users = this.userService.queryAll();
		logger.info("查询所有人员信息完成");
        return users;
    }
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object query_All(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("s_userManage");
		if (token == null) {
			return modelAndView;
		}
		List<User> users = null;
		users = this.userService.queryAll();
		logger.info("查询所有用户信息完成");
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
	    modelAndView.addObject("users", users);  
	    return modelAndView;
    }
	
	@RequestMapping(value = "/v_datamanage/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView dataManage(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_dataManage");
		if (token == null) {
			return modelAndView;
		}
		//查询该area下所有用户，即当前area是该vendor负责的area
		Vendor vendor = this.vendorService.getById(id);
		List<User> users = null;
		if(vendor!=null)
			users = this.userService.queryByAreaId(vendor.getAreaId());
		
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		modelAndView.addObject("users", users);
		return modelAndView;
    }
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    void testor() {
		this.userService.testor();
    }
}
