package com.makao.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Area;
import com.makao.entity.PointLog;
import com.makao.entity.Supervisor;
import com.makao.entity.TokenModel;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.IPointService;
import com.makao.service.ISupervisorService;
import com.makao.service.IUserService;
import com.makao.service.IVendorService;
import com.makao.utils.MakaoConstants;
import com.makao.utils.TokenManager;
import com.makao.utils.TokenUtils;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.JSSignatureUtil;
import com.makao.weixin.utils.WeixinConstants;

@CrossOrigin(origins = "*", maxAge = 3600)
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
	private IAreaService areaService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private IPointService pointService;
	
//	@Autowired
//	private StringRedisTemplate redisTemplate;
	@Autowired
	private TokenManager tokenManager;
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
	public @ResponseBody Object get(@PathVariable("id") Integer id)
	{
		JSONObject jsonObject = new JSONObject();
		logger.info("获取人员信息id=" + id);
		User user = (User)this.userService.getById(id);
		jsonObject.put("msg", "200");
		jsonObject.put("user", user);//不用序列化，方便前端jquery遍历
		return jsonObject;
	}
	
	/**
	 * @param openid
	 * @param request
	 * @param response
	 * @throws IOException
	 * 每次用户访问商城的初始页面，这里会请求微信网页验证scope=snsapi_base的地址，获取到用户的openid
	 */
	@RequestMapping(value="/snsapi_userinfo",method = RequestMethod.GET)
	public void snsapi_base(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "";
		
		String code = request.getParameter("code");
		//如果用户不同意授权
		if(code==null || "".equals(code)){
			page = "没有获得您的授权，无法浏览商城";
			out.write(page);
			return;
		}
		logger.info("code:"+code);
		String get_token_url = WeixinConstants.AUTH_TOKEN_URL.replace("APPID", WeixinConstants.APPID)
				.replace("SECRET", WeixinConstants.APPSECRET).replace("CODE", code);
		net.sf.json.JSONObject jsonObject = HttpUtil.doGetObject(get_token_url);
		String access_token = jsonObject.getString("access_token");
		logger.info("weixin auth access_token: "+access_token);
		String openid = jsonObject.getString("openid");
		logger.info("weixin user openid: "+openid);
		//使用该openid去数据库里查询用户信息
		User user = this.userService.checkLogin(openid);
		//String token = TokenUtils.setToken("user");使用新的TokenManager就不用旧的TokenUtils了
		//把token放到服务器缓存以备后面验证，这部分内容放到if语句中，因为需要存用户id，新用户需要插入后才知道id
		if(user!=null){
			int cityid = user.getCityId();
			int areaid = user.getAreaId();
			String cityname = user.getCityName();
			String areaname = user.getAreaName();
			//Area area = this.areaService.getById(areaid);
			//String catalogs = area.getCatalogs();
			int useid = user.getId();
			TokenModel tm = tokenManager.createUserToken(useid, openid);
			String token = tm.getToken();
			page = "<!DOCTYPE html>"
					+ "<html>"
						+ "<head>"
							+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">"
							+ "<meta name=\"format-detection\" content=\"telephone=no\" />"
							+ "<title>Fruit</title>"
							+ "<link rel=\"stylesheet\" href=\"/css/index.css\" />"
							+ "<link rel=\"stylesheet\" href=\"/css/animate.css\" />"
							+ "<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\" />"
						    + "<link rel=\"stylesheet\" href=\"/css/iconfont.css\" />"
							+ "<style>"
								+".flash{position:fixed;top:0;left:0;background-color:gray;width:100%;height:100%;z-index:99999;background:<!-- url(\"./img/flash.png\") --> no-repeat center bottom !important;"
								+"background-size:100% !important}"
								+"#jump{position:absolute;right:10%;top:5%;"
								+"width:4.2rem;height:2.4rem;border-radius:5rem;background:#6DBFD5;text-align:center;line-height:2.4rem;color:#fff;-webkit-user-select:none;}"		
							+"</style>"
						+ "</head>"
						+ "<body>"
							+"<div class=\"flash\" id=\"flash\">开场动画<input id=\"jump\" type=\"button\" value=\"跳过(2s)\" onclick=\"disp();\"/></div>"
							+ "<div class=\"page\" id=\"root\">"
							+ "</div>"
							+ "<script>"
								+ "var URL = '"+MakaoConstants.SERVER_DOMAIN+"';"
								+ "var IMG_URL = '"+MakaoConstants.SERVER_DOMAIN+"/static/upload/';"
								+ "var user_id = "+useid+";"
								+ "var cityid="+cityid+";"
								+ "var cityname='"+cityname+"';"
								+ "var areaid="+areaid+";"
								+ "var areaname='"+areaname+"';"
								+ "var token='"+token+"';"
								
								+"var count=2;"
								+"function showTime(count) {"
									+"console.log(count);"
									+"document.getElementById(\"jump\").value=\"跳过(\"+count+\"s)\";"
									+"if (count >= 0) {"
										+"count -= 1;"
										+"setTimeout(function () {"
											+"showTime(count);"
										+"}, 1400);"
									+"}" 
								+"}"
								+"showTime(count);"
								+"function disp(){"
									+"var box=document.getElementById(\"flash\");"
									+"box.style.display=\"none\";" 
								+"}"
								+"setTimeout(\"disp()\",4200);"
							+ "</script>"
							+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
							+ "<script src=\"//cdn.bootcss.com/react/0.14.8/react.js\"></script>"
						    + "<script src=\"/js/lib/react-dom.js\"></script>"
						    + "<script src=\"/js/lib/promise.js\"></script>"
							+ "<script src=\"/static/bundle.js\"></script>"
						+ "</body>"
					+ "</html>";
			out.write(page);
			out.flush();out.close();
		}
		else{//如果系统的数据库里没有该用户记录，则要发送snsapi_userinfo请求获取用户的基本信息
			String get_userinfo_url = WeixinConstants.AUTH_USERINFO_URL.replace("ACCESS_TOKEN", access_token).
					replace("OPENID",openid);
			net.sf.json.JSONObject userObject = HttpUtil.doGetObject(get_userinfo_url);
			System.out.println(userObject.toString());
			String open_id = userObject.getString("openid");
			//将得到的用户信息存入系统的数据库里
			if(open_id!=null && !"".equals(open_id)){
				User u = new User();
				u.setUserName(userObject.getString("nickname"));
				u.setOpenid(open_id);
				u.setAvatarUrl(userObject.getString("headimgurl"));
				u.setRank("初级会员");
				u.setRegistTime(new Timestamp(System.currentTimeMillis()));
				u.setPoint(MakaoConstants.INITIAL_POINT);
				u.setCityId(MakaoConstants.DEFAULT_CITY_ID);
				u.setAreaId(MakaoConstants.DEFAULT_AREA_ID);
				u.setCityName(MakaoConstants.DEFAULT_CITY_NAME);
				u.setAreaName(MakaoConstants.DEFAULT_AREA_NAME);
				//Area area = this.areaService.getById(MakaoConstants.DEFAULT_AREA_ID);
				//String catalogs = area.getCatalogs();
				int u_id = this.userService.insert(u);
				TokenModel tm = tokenManager.createUserToken(u_id, openid);
				String token = tm.getToken();
				page = "<!DOCTYPE html>"
						+ "<html>"
							+ "<head>"
								+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
								+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
								+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">"
								+ "<meta name=\"format-detection\" content=\"telephone=no\" />"
								+ "<title>Fruit</title>"
								+ "<link rel=\"stylesheet\" href=\"/css/index.css\" />"
								+ "<link rel=\"stylesheet\" href=\"/css/animate.css\" />"
								+ "<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\" />"
							    + "<link rel=\"stylesheet\" href=\"/css/iconfont.css\" />"
							    + "<style>"
									+".flash{position:fixed;top:0;left:0;background-color:gray;width:100%;height:100%;z-index:99999;background:<!-- url(\"./img/flash.png\") --> no-repeat center bottom !important;"
									+"background-size:100% !important}"
									+"#jump{position:absolute;right:10%;top:5%;"
									+"width:4.2rem;height:2.4rem;border-radius:5rem;background:#6DBFD5;text-align:center;line-height:2.4rem;color:#fff;-webkit-user-select:none;}"		
								+"</style>"
							+ "</head>"
							+ "<body>"
								+"<div class=\"flash\" id=\"flash\">开场动画<input id=\"jump\" type=\"button\" value=\"跳过(2s)\" onclick=\"disp();\"/></div>"
								+ "<div class=\"page\" id=\"root\">"
								+ "</div>"
								+ "<script>"
									+ "var URL = '"+MakaoConstants.SERVER_DOMAIN+"';"
									+ "var IMG_URL = '"+MakaoConstants.SERVER_DOMAIN+"/static/upload/';"
									+ "var user_id = "+u_id+";"
									+ "var cityid="+MakaoConstants.DEFAULT_CITY_ID+";"
									+ "var cityname='"+MakaoConstants.DEFAULT_CITY_NAME+"';"
									+ "var areaid="+MakaoConstants.DEFAULT_AREA_ID+";"
									+ "var areaname='"+MakaoConstants.DEFAULT_AREA_NAME+"';"
									+ "var token='"+token+"';"
									+"var count=2;"
									+"function showTime(count) {"
										+"console.log(count);"
										+"document.getElementById(\"jump\").value=\"跳过(\"+count+\"s)\";"
										+"if (count >= 0) {"
											+"count -= 1;"
											+"setTimeout(function () {"
												+"showTime(count);"
											+"}, 1400);"
										+"}" 
									+"}"
									+"showTime(count);"
									+"function disp(){"
										+"var box=document.getElementById(\"flash\");"
										+"box.style.display=\"none\";" 
									+"}"
									+"setTimeout(\"disp()\",4200);"
								+ "</script>"
								+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
								+ "<script src=\"//cdn.bootcss.com/react/0.14.8/react.js\"></script>"
							    + "<script src=\"/js/lib/react-dom.js\"></script>"
							    + "<script src=\"/js/lib/promise.js\"></script>"
								+ "<script src=\"/static/bundle.js\"></script>"
							+ "</body>"
						+ "</html>";
				out.write(page);
				out.flush();out.close();
				//写入积分记录
	    		PointLog pl = new PointLog();
	    		pl.setName("首次登录");
	    		pl.setPoint(MakaoConstants.INITIAL_POINT);
	    		pl.setGetDate(new Date(System.currentTimeMillis()));
	    		pl.setCityId(MakaoConstants.DEFAULT_CITY_ID);
	    		pl.setUserId(u_id);
	    		this.pointService.insertPointLog(pl);
			}
		}
	}
	@RequestMapping(value="/login",method = RequestMethod.GET)
	public void login(@RequestParam(value="openid",required = false) String openid, HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "";
		if(openid==null||"".equals(openid)){
			page="非法登录";
			out.write(page);
			return;
		}
		User user = this.userService.checkLogin(openid);
		String token = TokenUtils.setToken("user");
		//把token放到服务器缓存以备后面验证
		if(user==null){
			page = "<!DOCTYPE html>"
					+ "<html>"
						+ "<head>"
							+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">"
							+ "<meta name=\"format-detection\" content=\"telephone=no\" />"
							+ "<title>Fruit</title>"
							+ "<link rel=\"stylesheet\" href=\"/css/index.css\" />"
							+ "<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\" />"
						+ "</head>"
						+ "<body>"
							+ "<div class=\"page\" id=\"root\">"
							+ "</div>"
							+ "<script>"
								+ "window.cityid="+MakaoConstants.DEFAULT_CITY_ID+";"
								+ "window.cityname='"+MakaoConstants.DEFAULT_CITY_NAME+"';"
								+ "window.areaid="+MakaoConstants.DEFAULT_AREA_ID+";"
								+ "window.areaname='"+MakaoConstants.DEFAULT_AREA_NAME+"';"
								+ "window.token='"+token+"';"
							+ "</script>"
							+ "<script src=\"/static/bundle.js\"></script>"
						+ "</body>"
					+ "</html>";
			//这里后面可以添加注册的工作，第一次登录进来的用户写入数据库
		}
		else{
			int cityid = user.getCityId();
			int areaid = user.getAreaId();
			String cityname = user.getCityName();
			String areaname = user.getAreaName();
			System.out.println(cityname);
			System.out.println(areaname);
			page = "<!DOCTYPE html>"
					+ "<html>"
						+ "<head>"
							+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">"
							+ "<meta name=\"format-detection\" content=\"telephone=no\" />"
							+ "<title>Fruit</title>"
							+ "<link rel=\"stylesheet\" href=\"/css/index.css\" />"
							+ "<link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\" />"
						+ "</head>"
						+ "<body>"
							+ "<div class=\"page\" id=\"root\">"
							+ "</div>"
							+ "<script>"
								+ "window.cityid="+cityid+";"
								+ "window.cityname='"+cityname+"';"
								+ "window.areaid="+areaid+";"
								+ "window.areaname='"+areaname+"';"
								+ "window.token='"+token+"';"
							+ "</script>"
							+ "<script src=\"/static/bundle.js\"></script>"
						+ "</body>"
					+ "</html>";
		}
/*		
		//为前端页面能够使用JSSDK设置签名
	    String appId = WeixinConstants.APPID;
	    Map<String, String> wxConfig = JSSignatureUtil.getSignature("http://madarou1.ngrok.cc/user/login/?openid=3c5d3acb-31b9-480d-944a-516e74390ed8");
	    //将生成的订单需要在提交时使用的信息返回到前端页面
//	    response.setHeader("content-type", "text/html;charset=UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		PrintWriter out = response.getWriter();
		page = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
							+ "<meta charset=\"utf-8\">"
							+ "<title>订单支付</title>"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=0\">"
							+ "<link rel=\"stylesheet\" href=\"/css/weixin.css\">"
						+ "</head>"
						+"<body>"
							+ "<div class=\"wxapi_container\">"
								+"<div class=\"lbox_close wxapi_form\">"
									+ "<h3 id=\"menu-pay\">微信支付接口</h3>"
									+ "<span class=\"desc\">发起一个微信支付请求</span>"
									+ "<button class=\"btn btn_primary\" id=\"chooseWXPay\">支付订单</button>"
								+"</div>"
							+ "</div>"
						+ "</body>"
						+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
						+"<script>"
							+ "wx.config({"
								+ "debug: true,"
								+ "appId: '"+appId+"',"
								+ "timestamp: "+wxConfig.get("timestamp")+","
								+ "nonceStr: '"+wxConfig.get("nonceStr")+"',"
								+ "signature: '"+wxConfig.get("signature")+"',"
								+ "jsApiList: ["
									+ "'chooseWXPay'"
								+ "]"
							+ "});"
							+ "wx.ready(function () {"
								+ "var btn = document.getElementById(\"chooseWXPay\");"
								+ "btn.onclick=function(){"
									+ "alert('click success');"
									+ "wx.chooseWXPay({"
										+ "timestamp: 1414723227,"
										+ "nonceStr: 'noncestr',"
										+ "package: 'addition=action_id%3dgaby1234%26limit_pay%3d&bank_type=WX&body=innertest&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F120.204.206.246%2Fcgi-bin%2Fmmsupport-bin%2Fnotifypay&out_trade_no=1414723227818375338&partner=1900000109&spbill_create_ip=127.0.0.1&total_fee=1&sign=432B647FE95C7BF73BCD177CEECBEF8D',"
										+ "signType: 'MD5',"
										+ "paySign: 'bd5b1933cda6e9548862944836a9b52e8c9a2b69'"
									+ "});"
								+ "}"
							+ "});"
							+ "wx.error(function (res) {"
								+ "alert(res.errMsg);"
							+ "});"
						+ "</script>"
					+ "</html>";
*/
		out.write(page);
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
		if(res!=0){
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
	@AuthPassport
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 分页返回用户列表
	 */
	@RequestMapping(value = "/s_queryall/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object query_Paging(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("s_userManage");
		if (token == null) {
			return modelAndView;
		}
		List<User> users = null;
		int pageCount = 0;//需要分页的总数
		Supervisor supervisor = this.supervisorService.getById(id);
		if(supervisor!=null){
			int recordCount = this.userService.getRecordCount();
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int position=(showPage-1)*MakaoConstants.PAGE_SIZE+1;
			users = this.userService.queryFromToIndex(position,position+MakaoConstants.PAGE_SIZE-1);
		}
		
		logger.info("查询所有用户信息完成");
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
	    modelAndView.addObject("users", users);
	    modelAndView.addObject("pageCount", pageCount); 
	    return modelAndView;
    }
	
	@RequestMapping(value = "/search/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView search(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value="keyword", required=false) String keyword) throws UnsupportedEncodingException {
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_userSearch");
		if (token == null) {
			return modelAndView;
		}
		//查询该area下所有用户，即当前area是该vendor负责的area
		Vendor vendor = this.vendorService.getById(id);
		List<User> users = null;
		if(vendor!=null)
			users = this.userService.searchUser(vendor.getAreaId(),keyword);
		
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		modelAndView.addObject("users", users);
		return modelAndView;
    }
	
	@RequestMapping(value = "/v_usermanage/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView dataManage(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_userManage");
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加分页功能
	 */
	@RequestMapping(value = "/v_usermanage/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView dataManagePaging(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_userManage");
		if (token == null) {
			return modelAndView;
		}
		//查询该area下所有用户，即当前area是该vendor负责的area
		Vendor vendor = this.vendorService.getById(id);
		List<User> users = null;
		int pageCount = 0;//需要分页的总数
		if(vendor!=null){
			users = this.userService.queryByAreaId(vendor.getAreaId());
			int recordCount = this.userService.getRecordCountByAreaId(vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(users.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(users.size()-1);
			users = users.subList(from, to+1);
		}
		
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		modelAndView.addObject("users", users);
		modelAndView.addObject("pageCount", pageCount);
		return modelAndView;
    }
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    void testor() {
		//this.userService.testor();
//		ValueOperations<String, String> vop = redisTemplate.opsForValue();
//		String key = "user1";
//	    String v = "use1 StringRedisTemplate set k v";
//	    vop.set(key, v, 10, TimeUnit.SECONDS);
//	    String value = vop.get(key);
//	    System.out.println("redis set: "+ value);
		TokenModel tm = tokenManager.createToken(1,"u");
    }
}
