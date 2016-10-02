package com.makao.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.City;
import com.makao.entity.Comment;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.ICommentService;
import com.makao.service.IOrderOffService;
import com.makao.service.IProductService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.thread.SendMSGThread;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/orderOff")
public class OrderOffController {
	private static final Logger logger = Logger.getLogger(OrderOffController.class);
	@Resource
	private IOrderOffService orderOffService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private ICityService cityService;
	@Resource
	private IProductService productService;
	@Resource
	private ICommentService commentService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody OrderOff get(@PathVariable("id") Integer id)
	{
		logger.info("获取失效订单信息id=" + id);
		OrderOff OrderOff = (OrderOff)this.orderOffService.getById(id);
		return OrderOff;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.orderOffService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除失效订单信息成功id=" + id);
        	jsonObject.put("msg", "删除失效订单信息成功");
		}
		else{
			logger.info("删除失效订单信息失败id=" + id);
        	jsonObject.put("msg", "删除失效订单信息失败");
		}
        return jsonObject;
    }
	/**
	 * @param OrderOff
	 * @return
	 * 该方法只是测试时生成数据用，因为OrderOff里的东西都是在OrderOn里直接移过去的
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody OrderOff OrderOff) {
        OrderOff.setNumber(OrderNumberUtils.generateOrderNumber());
		OrderOff.setOrderTime(new Timestamp(System.currentTimeMillis()));
		OrderOff.setFinalTime(new Timestamp(System.currentTimeMillis()));
		OrderOff.setPayType("微信安全支付");//现在只有这种支付方式
		OrderOff.setReceiveType("送货上门");//现在只有这种收货方式
		int res = this.orderOffService.insert(OrderOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加失效订单成功id=" + OrderOff.getNumber());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加失效订单成功失败id=" + OrderOff.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/all/{cityid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object all(@PathVariable("cityid") int cityid, @PathVariable("userid") int userid) {
		JSONObject jsonObject = new JSONObject();
		List<OrderOff> os = this.orderOffService.queryByUserId("Order_"+cityid+"_off", userid);
		logger.info("查询用户id："+userid+"的所有失效订单信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", os);
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid) {
		JSONObject jsonObject = new JSONObject();
		OrderOff os = this.orderOffService.queryByOrderId("Order_"+cityid+"_off", orderid);
		List<SmallProduct> sps = new ArrayList<SmallProduct>();
		if(os!=null){
			//重新组装商品列表，方便前端直接显示
			String[] pro_ids = os.getProductIds().split(",");
			String[] pro_names = os.getProductNames().split(",");
			String pcomments = os.getPcomments();
			String[] coms = null;
			if(pcomments!=null&&!"".equals(pcomments))
			{	
				coms = pcomments.split(",");
			}
			for(int i=0; i<pro_ids.length; i++){
				String pid = pro_ids[i];
				String[] names = pro_names[i].split("=");
				Product p = (Product)this.productService.getById(Integer.valueOf(pid),os.getCityId(),os.getAreaId());
				SmallProduct sp = new SmallProduct();
				sp.setId(pid);
				sp.setName(names[0]);//这里要显示当时下单时的名称，防止后面修改了商品名后，引起误会
				//sp.setImage(p.getCoverSUrl());
				sp.setCityId(os.getCityId());
				sp.setAreaId(os.getAreaId());
				sp.setImage(p.getCoverSUrl());
				sp.setPrice(names[1]);//这里要显示当时用户下单时的价格，而不是现在商品实际的价格
				sp.setNumber(names[2]);
				if(coms!=null&&coms.length>0)
				{
					for(String c : coms){
						if(c!=null&&!"".equals(c)){
							String proid = c.split("=")[0];
							if(pid.equals(proid)){
								String comid = c.split("=")[1];
								Comment com = this.commentService.getByCityAreaComentId(os.getCityId(),os.getAreaId(),Integer.valueOf(comid));
								sp.setComment(com.getContent());
							}
						}
					}
				}
				sps.add(sp);
			}
			
		}
		logger.info("查询失效订单id："+orderid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("order", os);
		jsonObject.put("products", sps);
		return jsonObject;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 用户发起退货请求，将订单状态设置为退货申请中
	 */
	@AuthPassport
	@RequestMapping(value = "/return/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object returnOrder(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid,
    		@RequestParam(value="token", required=false) String token) {
		JSONObject jsonObject = new JSONObject();
		OrderOff res = this.orderOffService.returnOrder(cityid, orderid);
		if(res!=null){
			logger.info("发起退货申请成功，订单id=" + orderid + " 所属城市id:"+cityid);
        	jsonObject.put("msg", "200");
        	// 通知vendor
        	List<Vendor> vs = this.vendorService.getByAreaId(res.getAreaId());
        	for(Vendor v : vs){
        		String openid = v.getOpenid();
        		if(openid!=null&&!"".equals(openid)){
        			SendMSGThread snt = new SendMSGThread(openid,res,6);
        			new Thread(snt, "send order created mb msg thread").start();
        		}
        	}
		}
		else{
			logger.info("发起退货申请失败，订单id=" + orderid + " 所属城市id:"+cityid);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody OrderOff OrderOff) {
		int res = this.orderOffService.update(OrderOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改失效订单信息成功id=" + OrderOff.getId());
        	jsonObject.put("msg", "修改失效订单信息成功");
		}
		else{
			logger.info("修改失效订单信息失败id=" + OrderOff.getId());
        	jsonObject.put("msg", "修改失效订单信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<OrderOff> OrderOffs = null;
		//则根据关键字查询
		OrderOffs = this.orderOffService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询失效订单信息完成");
        return OrderOffs;
    }
	
//	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
//    public @ResponseBody
//    Object queryAll() {
//		List<OrderOff> OrderOffs = null;
//		//则查询返回所有
//		OrderOffs = this.orderOffService.queryAll();
//		logger.info("查询所有失效订单信息完成");
//        return OrderOffs;
//    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 处理退货订单，将其状态设置为退货中
	 */
	@RequestMapping(value = "/vrefund/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vrefund(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOffService.refundOrder(vendor.getCityId(),orderid);
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
	 * @param id
	 * @param paramObject
	 * @return
	 * 处理需要退款的订单，将其状态设置为已退款
	 */
	@RequestMapping(value = "/srefund/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object srefund(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		int cityid = paramObject.getIntValue("cityid");
		JSONObject jsonObject = new JSONObject();
		Supervisor supervisor = this.supervisorService.getById(id);
		if(supervisor!=null){
			int res = this.orderOffService.finishRefundOrder(cityid,orderid);
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
	 * @param id
	 * @param paramObject
	 * @return
	 * 设置退货订单的状态从退货中变成已退货
	 */
	@RequestMapping(value = "/vfinish/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vfinish(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			OrderOff res = this.orderOffService.finishReturnOrder(vendor.getCityId(),orderid);
			if(res!=null){
				//给超级管理员推送完成退货的消息
				List<Supervisor> ss = this.supervisorService.queryAll();
				for(Supervisor s : ss){
					String openid = s.getOpenid();
					if(openid!=null&&!"".equals(openid)){
						SendMSGThread snt = new SendMSGThread(openid,res,5);
						new Thread(snt, "send order need refund mb msg thread").start();
					}
				}
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
	 * @param id
	 * @param paramObject
	 * @return
	 * 将退货中的订单从退货申请中或者已退货变成已取消退货
	 */
	@RequestMapping(value = "/vcancel/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vcancel(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		String vcomment = paramObject.getString("vcomment");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOffService.cancelRefundOrder(vendor.getCityId(),orderid,vcomment);
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
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_All(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAll("Order_"+c.getId()+"_off");
				if(os!=null)
					orderOffs.addAll(os);
			}
		}
		logger.info("查询所有已完成的订单信息完成");
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("ordersOff", orderOffs);   
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 总后台返回所有已完成的订单，分页
	 */
	@RequestMapping(value = "/s_queryall/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_All_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		int pageCount = 0;
		int recordCount = 0;
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAll("Order_"+c.getId()+"_off");
				if(os!=null){
					orderOffs.addAll(os);
					int rc = this.orderOffService.getRecordCount(c.getId());
					recordCount += rc;
				}
				pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
				//如果要显示第showPage页，那么游标应该移动到的position的值是：
				int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
				int to=(orderOffs.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orderOffs.size()-1);
				orderOffs = orderOffs.subList(from, to+1);
			}
		}
		logger.info("查询所有已完成的订单信息完成");
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("ordersOff", orderOffs);   
	    modelAndView.addObject("pageCount", pageCount);
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已收货的订单
	 */
	@RequestMapping(value = "/v_query_confirm/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_confirm");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryConfirmGetByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加了分页
	 */
	@RequestMapping(value = "/v_query_confirm/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_confirm");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryConfirmGetByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getConfirmRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);

		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders); 
	    modelAndView.addObject("pageCount", pageCount);   
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消的、已退货和已取消退货的订单
	 */
	@RequestMapping(value = "/v_query_cancel/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vquerycancel(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_cancel");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryCancelByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消，已退货和已取消退货的订单，增加了分页
	 */
	@RequestMapping(value = "/v_query_cancel/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vquerycancel_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_cancel");  
		if(token==null){
			return modelAndView;
		}
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryCancelByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getCancelRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);

		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders); 
	    modelAndView.addObject("pageCount", pageCount);   
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询退货中、退货申请中的订单
	 */
	@RequestMapping(value = "/v_query_refund/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vqueryrefund(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryRefundByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加分页
	 */
	@RequestMapping(value = "/v_query_refund/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vqueryrefund_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryRefundByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getReturnRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);

		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders); 
	    modelAndView.addObject("pageCount", pageCount);   
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消(卖家取消)的和已退货的订单，在退款订单里显示，因为这些是需要退款的
	 */
	@RequestMapping(value = "/s_query_refund/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView squeryrefund(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAllCanceledAndReturned("Order_"+c.getId()+"_off");
				if(os!=null)
					orderOffs.addAll(os);
			}
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orderOff", orderOffs);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消(卖家取消)的和已退货的订单，在退款订单里显示，因为这些是需要退款的，增加了分页
	 */
	@RequestMapping(value = "/s_query_refund/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView squeryrefund_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		int pageCount = 0;
		int recordCount = 0;
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAllCanceledAndReturned("Order_"+c.getId()+"_off");
				if(os!=null){
					orderOffs.addAll(os);
					int rc = this.orderOffService.getCanceledAndReturnedRecordCount(c.getId());
					recordCount += rc;
				}
				pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
				//如果要显示第showPage页，那么游标应该移动到的position的值是：
				int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
				int to=(orderOffs.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orderOffs.size()-1);
				orderOffs = orderOffs.subList(from, to+1);
			}
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orderOff", orderOffs);  
	    modelAndView.addObject("pageCount", pageCount);
		return modelAndView;
    }
	
	private class SmallProduct{
		private String id;
		private String name;
		private String image;
		private String price;
		private String number;
		private int cityId;
		private int areaId;
		private String comment;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public int getCityId() {
			return cityId;
		}
		public void setCityId(int cityId) {
			this.cityId = cityId;
		}
		public int getAreaId() {
			return areaId;
		}
		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
	}
	
}
