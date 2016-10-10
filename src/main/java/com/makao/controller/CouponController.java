package com.makao.controller;

import java.io.File;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Area;
import com.makao.entity.Banner;
import com.makao.entity.City;
import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.entity.History;
import com.makao.entity.OrderOn;
import com.makao.entity.PointLog;
import com.makao.entity.Supervisor;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.ICouponService;
import com.makao.service.ISupervisorService;
import com.makao.service.IUserService;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/coupon")
public class CouponController {
	private static final Logger logger = Logger.getLogger(CouponController.class);
	@Resource
	private ICouponService couponService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private ICityService cityService;
	@Resource
	private IUserService userService;
	
//	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
//	public @ResponseBody Coupon get(@PathVariable("id") Integer id)
//	{
//		logger.info("获取优惠券信息id=" + id);
//		Coupon Coupon = (Coupon)this.couponService.getById(id);
//		return Coupon;
//	}
	
//	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
//    public @ResponseBody
//    Object delete(@PathVariable("id") Integer id) {
//        int res = this.couponService.deleteById(id);
//        JSONObject jsonObject = new JSONObject();
//		if(res==0){
//			logger.info("删除优惠券信息成功id=" + id);
//        	jsonObject.put("msg", "删除优惠券信息成功");
//		}
//		else{
//			logger.info("删除优惠券信息失败id=" + id);
//        	jsonObject.put("msg", "删除优惠券信息失败");
//		}
//        return jsonObject;
//    }
	@AuthPassport
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object postdelete(@PathVariable("id") int id,@RequestBody JSONObject paramObject) {
		int couponid = paramObject.getInteger("couponId");
		int cityid = paramObject.getInteger("cityId");
		Supervisor supervisor = this.supervisorService.getById(id);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
	        int res = this.couponService.deleteById(couponid,cityid);
			if(res==0){
				logger.info("删除优惠券信息成功id=" + couponid);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("删除优惠券信息失败id=" + couponid);
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	@AuthPassport
	@RequestMapping(value = "/new/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("id") int id, @RequestBody Coupon Coupon) {
		Supervisor supervisor = this.supervisorService.getById(id);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			String[] cities = Coupon.getCityName().split("-_-");
			for(String c : cities){
				int cid = Integer.valueOf(c.split("#_#")[0]);
				String cname = c.split("#_#")[1];
				Coupon cc = new Coupon();
				BeanUtils.copyProperties(Coupon, cc);
				cc.setCityId(cid);cc.setCityName(cname);
				int res = this.couponService.insert(cc);
				if(res==0){
					logger.info("在城市 "+cname+" 增加优惠券成功，面值=" + Coupon.getName());
				}
				else{
					logger.info("在城市 "+cname+" 增加优惠券失败，面值=" + Coupon.getName());
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}
			}
			jsonObject.put("msg", "200");
			return jsonObject;
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@RequestMapping(value = "/all/{cityid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object all(@PathVariable("cityid") int cityid) {
        JSONObject jsonObject = new JSONObject();
		List<Coupon> os = this.couponService.queryAll("Coupon_"+cityid);
		logger.info("查询城市id："+cityid+"的所有静态coupon完成");
		jsonObject.put("msg", "200");
		jsonObject.put("coupons", os);
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{couponid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("couponid") int couponid) {
		JSONObject jsonObject = new JSONObject();
		Coupon coupon = this.couponService.queryByCouponId("Coupon_"+cityid, couponid);
		logger.info("查询优惠券id："+couponid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("coupon", coupon);
		return jsonObject;
    }
	
	@AuthPassport
	@RequestMapping(value = "/exchange/{cityid:\\d+}/{couponid:\\d+}/{userid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object exchange(@PathVariable("cityid") int cityid,@PathVariable("couponid") int couponid,@PathVariable("userid") int userid,
    		@RequestParam(value="token", required=false) String token) {
        JSONObject jsonObject = new JSONObject();
        Coupon coupon = this.couponService.queryByCouponId("Coupon_"+cityid, couponid);
        User user = this.userService.getById(userid);
        
        if(coupon!=null && user!=null){
        	if(user.getPoint()-coupon.getPoint() < 0){
        		jsonObject.put("msg", "202");
        		jsonObject.put("detail", "积分不足");
            	return jsonObject;
        	}
        	int res = this.couponService.exchangeCoupon(coupon, user);
        	if(res==0){
    			logger.info("兑换优惠券成功面值=" + coupon.getAmount() + "，所属城市id:"+cityid);
            	jsonObject.put("msg", "200");
            	return jsonObject;
    		}
        }
        logger.info("兑换优惠券失败id=" + couponid+" 所属城市id:"+cityid);
        jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	/**
	 * @param cityid
	 * @return
	 * 用户查询兑换历史
	 */
	@RequestMapping(value = "/history/{cityid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object history(@PathVariable("cityid") int cityid,@PathVariable("userid") int userid) {
        JSONObject jsonObject = new JSONObject();
		List<History> hs = this.couponService.queryHistory("Exchange_"+cityid,userid);
		logger.info("查询城市id："+cityid+" 下userid="+userid+"的所有兑换历史完成");
		jsonObject.put("msg", "200");
		jsonObject.put("history", hs);
		return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Coupon Coupon) {
		int res = this.couponService.update(Coupon);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改优惠券信息成功id=" + Coupon.getId());
        	jsonObject.put("msg", "修改优惠券信息成功");
		}
		else{
			logger.info("修改优惠券信息失败id=" + Coupon.getId());
        	jsonObject.put("msg", "修改优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Coupon> Coupons = null;
		//则根据关键字查询
		Coupons = this.couponService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询优惠券信息完成");
        return Coupons;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Coupon> Coupons = null;
		//则查询返回所有
		Coupons = this.couponService.queryAll();
		logger.info("查询所有优惠券信息完成");
        return Coupons;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 下线优惠券
	 */
	@AuthPassport
	@RequestMapping(value = "/sdown/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sdown(@PathVariable("id") int id,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(id);
		int couponId = paramObject.getInteger("couponId");
		int cityId = paramObject.getInteger("cityId");
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			Coupon coupon = this.couponService.getById(couponId, cityId);
			if(coupon!=null){
				coupon.setIsShow("no");
				int res = this.couponService.update(coupon);
				if(res==0){
					logger.info("Coupon下线成功id=" + couponId);
		        	jsonObject.put("msg", "200");
		        	return jsonObject;
				}
				else{
					logger.info("Coupon下线失败id=" + couponId);
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
	}
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 上线优惠券
	 */
	@AuthPassport
	@RequestMapping(value = "/sup/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sup(@PathVariable("id") int id,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(id);
		int couponId = paramObject.getInteger("couponId");
		int cityId = paramObject.getInteger("cityId");
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			Coupon coupon = this.couponService.getById(couponId, cityId);
			if(coupon!=null){
				coupon.setIsShow("yes");
				int res = this.couponService.update(coupon);
				if(res==0){
					logger.info("Coupon上线成功id=" + couponId);
		        	jsonObject.put("msg", "200");
		        	return jsonObject;
				}
				else{
					logger.info("Coupon上线失败id=" + couponId);
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
	}
	@AuthPassport
	@RequestMapping(value = "/sedit/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sedit(@PathVariable("id") int id,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(id);
		int couponId = paramObject.getInteger("couponId");
		String name = paramObject.getString("name");
		String amount = paramObject.getString("amount");
		String comment = paramObject.getString("comment");
		String coverSUrl = paramObject.getString("coverSUrl");
		String coverBUrl = paramObject.getString("coverBUrl");
		String isShow = paramObject.getString("isShow");
		int point = paramObject.getInteger("point");
		int restrict = paramObject.getInteger("restrict");
		int cityId = paramObject.getInteger("cityId");
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			Coupon coupon = this.couponService.getById(couponId, cityId);
			if(coupon!=null){
				coupon.setName(name);
				coupon.setIsShow(isShow);
				coupon.setAmount(amount);
				coupon.setComment(comment);
				coupon.setCoverSUrl(coverSUrl);
				coupon.setCoverBUrl(coverBUrl);
				coupon.setPoint(point);
				coupon.setRestrict(restrict);
				int res = this.couponService.update(coupon);
				if(res==0){
					logger.info("Coupon修改成功id=" + couponId);
		        	jsonObject.put("msg", "200");
		        	return jsonObject;
				}
				else{
					logger.info("Coupon修改失败id=" + couponId);
		        	jsonObject.put("msg", "201");
		        	return jsonObject;
				}
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
	}
	
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		 ModelAndView modelAndView = new ModelAndView();  
			modelAndView.setViewName("s_coupon");  
			if(token==null){
				return modelAndView;
			}
			Supervisor supervisor = this.supervisorService.getById(id);
			List<City> cites = this.cityService.queryAll();
			List<Coupon> coupons = new LinkedList<Coupon>();
			if(supervisor!=null){
				for(City c : cites){
					List<Coupon> os = this.couponService.queryAll("Coupon_"+c.getId());
					if(os!=null)
						coupons.addAll(os);
				}
				logger.info("查询所有coupon信息完成");
			}
		    modelAndView.addObject("id", id);  
		    modelAndView.addObject("token", token);   
			modelAndView.addObject("coupons", coupons);
			return modelAndView;
    }
	
	@RequestMapping(value = "/uploadImgd1", method = RequestMethod.POST)
   public @ResponseBody
   Object uploadImgd1(@RequestParam("upfiled1") CommonsMultipartFile[] files, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(files==null || files.length==0){
			jsonObject.put("msg", "图片不符合");
			return jsonObject;
		}
		CommonsMultipartFile upfile = files[0];
		String fileName = upfile.getOriginalFilename();
		//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
       String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
       System.out.println(request.getServletContext().getRealPath("/"));
       if(!("jpg".equals(ext)) && !("png".equals(ext)) && !("jpeg".equals(ext))){
       	jsonObject.put("msg", "图片不符合");
       	return jsonObject;
       }
       //使用订单号生成器生成一个唯一的编号作为图片的名称
       String picUniqueName = OrderNumberUtils.generateOrderNumber();
       //将图片存入文件系统upload文件夹
       if(!upfile.isEmpty()){  
       	System.out.println("正在上传图片fileName---------->" + upfile.getOriginalFilename());
       	String imgFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/upload/";
       	String realImgName = picUniqueName+"_"+upfile.getOriginalFilename();
       	if(realImgName.length()>50){//名字长过数据库设置的50，则截掉后面
       		realImgName = realImgName.substring(0, 46)+"."+ext;
       	}
           int pre = (int) System.currentTimeMillis();  
           try {  
           	File tofile = new File(imgFolder + realImgName);
           	upfile.transferTo(tofile);
               int finaltime = (int) System.currentTimeMillis();  
               System.out.println("上传图片用时:"+(finaltime - pre));  
               logger.info("图片 "+realImgName+" 成功写入本地文件");
               jsonObject.put("msg", "200");
               jsonObject.put("imgName", realImgName);
               return jsonObject;
               
           } catch (Exception e) {  
               e.printStackTrace();  
               System.out.println("图片"+realImgName+"写入本地文件出错");  
               logger.error("图片 "+realImgName+" 写入本地文件出错" + e);
               jsonObject.put("msg", "201");
               return jsonObject;
           }  
		}
       jsonObject.put("msg", "201");
       return jsonObject;
   }
	
	/**
	 * @param files
	 * @param request
	 * @return
	 * 上传详情2图
	 */
	@RequestMapping(value = "/uploadImgd2", method = RequestMethod.POST)
   public @ResponseBody
   Object uploadImgd2(@RequestParam("upfiled2") CommonsMultipartFile[] files, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(files==null || files.length==0){
			jsonObject.put("msg", "图片不符合");
			return jsonObject;
		}
		CommonsMultipartFile upfile = files[0];
		String fileName = upfile.getOriginalFilename();
		//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
       String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
       System.out.println(request.getServletContext().getRealPath("/"));
       if(!("jpg".equals(ext)) && !("png".equals(ext)) && !("jpeg".equals(ext))){
       	jsonObject.put("msg", "图片不符合");
       	return jsonObject;
       }
       //使用订单号生成器生成一个唯一的编号作为图片的名称
       String picUniqueName = OrderNumberUtils.generateOrderNumber();
       //将图片存入文件系统upload文件夹
       if(!upfile.isEmpty()){  
       	System.out.println("正在上传图片fileName---------->" + upfile.getOriginalFilename());
       	String imgFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/upload/";
       	String realImgName = picUniqueName+"_"+upfile.getOriginalFilename();
       	if(realImgName.length()>50){//名字长过数据库设置的50，则截掉后面
       		realImgName = realImgName.substring(0, 46)+"."+ext;
       	}
           int pre = (int) System.currentTimeMillis();  
           try {  
           	File tofile = new File(imgFolder + realImgName);
           	upfile.transferTo(tofile);
               int finaltime = (int) System.currentTimeMillis();  
               System.out.println("上传图片用时:"+(finaltime - pre));  
               logger.info("图片 "+realImgName+" 成功写入本地文件");
               jsonObject.put("msg", "200");
               jsonObject.put("imgName", realImgName);
               return jsonObject;
               
           } catch (Exception e) {  
               e.printStackTrace();  
               System.out.println("图片"+realImgName+"写入本地文件出错");  
               logger.error("图片 "+realImgName+" 写入本地文件出错" + e);
               jsonObject.put("msg", "201");
               return jsonObject;
           }  
		}
       jsonObject.put("msg", "201");
       return jsonObject;
   }
	@RequestMapping(value = "/cuploadImgd1", method = RequestMethod.POST)
	   public @ResponseBody
	   Object cuploadImgd1(@RequestParam("cupfiled1") CommonsMultipartFile[] files, HttpServletRequest request) {
			JSONObject jsonObject = new JSONObject();
			if(files==null || files.length==0){
				jsonObject.put("msg", "图片不符合");
				return jsonObject;
			}
			CommonsMultipartFile upfile = files[0];
			String fileName = upfile.getOriginalFilename();
			//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
	       String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	       System.out.println(request.getServletContext().getRealPath("/"));
	       if(!("jpg".equals(ext)) && !("png".equals(ext)) && !("jpeg".equals(ext))){
	       	jsonObject.put("msg", "图片不符合");
	       	return jsonObject;
	       }
	       //使用订单号生成器生成一个唯一的编号作为图片的名称
	       String picUniqueName = OrderNumberUtils.generateOrderNumber();
	       //将图片存入文件系统upload文件夹
	       if(!upfile.isEmpty()){  
	       	System.out.println("正在上传图片fileName---------->" + upfile.getOriginalFilename());
	       	String imgFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/upload/";
	       	String realImgName = picUniqueName+"_"+upfile.getOriginalFilename();
	       	if(realImgName.length()>50){//名字长过数据库设置的50，则截掉后面
	       		realImgName = realImgName.substring(0, 46)+"."+ext;
	       	}
	           int pre = (int) System.currentTimeMillis();  
	           try {  
	           	File tofile = new File(imgFolder + realImgName);
	           	upfile.transferTo(tofile);
	               int finaltime = (int) System.currentTimeMillis();  
	               System.out.println("上传图片用时:"+(finaltime - pre));  
	               logger.info("图片 "+realImgName+" 成功写入本地文件");
	               jsonObject.put("msg", "200");
	               jsonObject.put("imgName", realImgName);
	               return jsonObject;
	               
	           } catch (Exception e) {  
	               e.printStackTrace();  
	               System.out.println("图片"+realImgName+"写入本地文件出错");  
	               logger.error("图片 "+realImgName+" 写入本地文件出错" + e);
	               jsonObject.put("msg", "201");
	               return jsonObject;
	           }  
			}
	       jsonObject.put("msg", "201");
	       return jsonObject;
	   }
		
		/**
		 * @param files
		 * @param request
		 * @return
		 * 上传详情2图
		 */
		@RequestMapping(value = "/cuploadImgd2", method = RequestMethod.POST)
	   public @ResponseBody
	   Object cuploadImgd2(@RequestParam("cupfiled2") CommonsMultipartFile[] files, HttpServletRequest request) {
			JSONObject jsonObject = new JSONObject();
			if(files==null || files.length==0){
				jsonObject.put("msg", "图片不符合");
				return jsonObject;
			}
			CommonsMultipartFile upfile = files[0];
			String fileName = upfile.getOriginalFilename();
			//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
	       String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	       System.out.println(request.getServletContext().getRealPath("/"));
	       if(!("jpg".equals(ext)) && !("png".equals(ext)) && !("jpeg".equals(ext))){
	       	jsonObject.put("msg", "图片不符合");
	       	return jsonObject;
	       }
	       //使用订单号生成器生成一个唯一的编号作为图片的名称
	       String picUniqueName = OrderNumberUtils.generateOrderNumber();
	       //将图片存入文件系统upload文件夹
	       if(!upfile.isEmpty()){  
	       	System.out.println("正在上传图片fileName---------->" + upfile.getOriginalFilename());
	       	String imgFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/upload/";
	       	String realImgName = picUniqueName+"_"+upfile.getOriginalFilename();
	       	if(realImgName.length()>50){//名字长过数据库设置的50，则截掉后面
	       		realImgName = realImgName.substring(0, 46)+"."+ext;
	       	}
	           int pre = (int) System.currentTimeMillis();  
	           try {  
	           	File tofile = new File(imgFolder + realImgName);
	           	upfile.transferTo(tofile);
	               int finaltime = (int) System.currentTimeMillis();  
	               System.out.println("上传图片用时:"+(finaltime - pre));  
	               logger.info("图片 "+realImgName+" 成功写入本地文件");
	               jsonObject.put("msg", "200");
	               jsonObject.put("imgName", realImgName);
	               return jsonObject;
	               
	           } catch (Exception e) {  
	               e.printStackTrace();  
	               System.out.println("图片"+realImgName+"写入本地文件出错");  
	               logger.error("图片 "+realImgName+" 写入本地文件出错" + e);
	               jsonObject.put("msg", "201");
	               return jsonObject;
	           }  
			}
	       jsonObject.put("msg", "201");
	       return jsonObject;
	   }
}
