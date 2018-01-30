package com.makao.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.makao.entity.Catalog;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.IBannerService;
import com.makao.service.IProductService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;
import com.makao.utils.RedisUtil;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/product")
public class ProductController {
	private static final Logger logger = Logger.getLogger(ProductController.class);
	@Resource
	private IProductService productService;
	@Resource
	private IAreaService areaService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private IBannerService bannerService;
	@Resource
	private ISupervisorService supervisorService;
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * @param id
	 * @param cityId
	 * @param areaId
	 * @return
	 * curl -X GET 'http://localhost:8080/wxmall/product/1/1/1'
	 */
	@RequestMapping(value="/{id:\\d+}/{cityId:\\d+}/{areaId:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Object get(@PathVariable("id") int id,@PathVariable("cityId") int cityId,@PathVariable("areaId") int areaId)
	{
		JSONObject jsonObject = new JSONObject();
		logger.info("获取商品信息id=" + id);
		Product product = (Product)this.productService.getById(id,cityId,areaId);
		Area a = this.areaService.getById(areaId);
		if(a!=null)
			product.setPhone(a.getPhoneNumber());
		else
			product.setPhone("18817912915");
		jsonObject.put("msg", "200");
		jsonObject.put("product", product);//不用序列化，方便前端jquery遍历
		return jsonObject;
	}
	
	@RequestMapping(value="/{id:\\d+}/{vendorId:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Product get(@PathVariable("id") int id,@PathVariable("vendorId") int vendorId)
	{
		Product Product = null;
		Vendor vendor = this.vendorService.getById(vendorId);
		if(vendor!=null){
			logger.info("获取商品信息id=" + id);
			Product = (Product)this.productService.getById(id,vendor.getCityId(),vendor.getAreaId());
		}
		return Product;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.productService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除商品信息成功id=" + id);
        	jsonObject.put("msg", "删除商品信息成功");
		}
		else{
			logger.info("删除商品信息失败id=" + id);
        	jsonObject.put("msg", "删除商品信息失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param paramObject
	 * @return
	 * 给商品点赞
	 */
	@AuthPassport
	@RequestMapping(value = "/like", method = RequestMethod.POST)
    public @ResponseBody
    Object like(@RequestBody JSONObject paramObject) {
		int cityId = paramObject.getIntValue("cityId");
		int areaId = paramObject.getIntValue("areaId");
		int productId = paramObject.getIntValue("productId");
		int res = this.productService.like("Product_"+cityId+"_"+areaId, productId);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("商品点赞成功，被赞商品" + cityId +" "+areaId+" "+productId);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("商品点赞失败，被赞商品" + cityId +" "+areaId+" "+productId);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	/**
	 * @param paramObject
	 * @return
	 * 超级管理员补货
	 */
	@AuthPassport
	@RequestMapping(value = "/supply/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object supply(@RequestBody JSONObject paramObject) {
		int cityId = paramObject.getIntValue("cityid");
		int areaId = paramObject.getIntValue("areaid");
		int productId = paramObject.getIntValue("productid");
		int num = paramObject.getIntValue("num");
		int res = this.productService.supplyProduct("Product_"+cityId+"_"+areaId, productId,num);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("商品设置补货成功，商品：" + cityId +" "+areaId+" "+productId);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("商品设置补货失败，商品：" + cityId +" "+areaId+" "+productId);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	/**
	 * @param paramObject
	 * @return
	 * 区域管理员操作后完成补货
	 */
	@AuthPassport
	@RequestMapping(value = "/supplied/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object supplied(@RequestBody JSONObject paramObject) {
		int cityId = paramObject.getIntValue("cityid");
		int areaId = paramObject.getIntValue("areaid");
		int productId = paramObject.getIntValue("productid");
		int num = paramObject.getIntValue("num");
		String k = "pi_"+cityId+"_"+areaId+"_"+productId;
		//先从缓存中获取到该商品的库存
		Object object = redisUtil.redisQueryObject(k);
		if(object==null){//缓存里面如果没有，从数据库里读
			String inv_sv = this.productService.getInventoryAndSV(cityId, areaId, productId+"");
			if(inv_sv!=null){
				redisUtil.redisSaveInventory(k, inv_sv.split("_")[0]);
				redisUtil.redisSaveInventory("sv_"+cityId+"_"+areaId+"_"+productId, inv_sv.split("_")[1]);
			}		}
		//加入缓存中
		List<Object> rt = redisUtil.addInventoryTx(
				k, num);
		//再从缓存读出
		String inventN = redisUtil.redisQueryObject(k);
		JSONObject jsonObject = new JSONObject();
		if(inventN!=null&&!"".equals(inventN)){
			int res = this.productService.suppliedProduct("Product_"+cityId+"_"+areaId, productId,Integer.valueOf(inventN));
			if(res==0){
				logger.info("商品补货成功，商品：" + cityId +" "+areaId+" "+productId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
		}
		else{
			logger.info("商品补货失败，商品：" + cityId +" "+areaId+" "+productId);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	/**
	 * @param Product
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"number":"海南千禧小番茄","catalog":"水果","price":"12.00","standard":"一份足2斤","marketPrice":"30.00","inventory":12,"sequence":3,"status":"库存紧张","origin":"海南","salesVolume":7637,"likes":3972,"areaId":1,"cityId":1}' 'http://localhost:8080/wxmall/product/new'
	 */
	@AuthPassport
	@RequestMapping(value = "/vnew/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("vendorid") int vendorid,@RequestBody Product Product) {
		Vendor vendor = this.vendorService.getById(vendorid);
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			Product.setAreaId(vendor.getAreaId());
			Product.setCityId(vendor.getCityId());
			int res = this.productService.insert(Product);
			if(res!=0){
				//将商品库存放入缓存
				redisUtil.redisSaveInventory("pi_"+Product.getCityId()+"_"+Product.getAreaId()+"_"+res, String.valueOf(Product.getInventory()));
				redisUtil.redisSaveInventory("sv_"+Product.getCityId()+"_"+Product.getAreaId()+"_"+res, String.valueOf(Product.getSalesVolume()));
				logger.info("增加商品成功name=" + Product.getProductName());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("增加商品成功失败name=" + Product.getProductName());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	/**
	 * @param vendorid
	 * @param Product
	 * @return
	 * 修改商品信息
	 */
	@AuthPassport
	@RequestMapping(value = "/vedit/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vedit(@PathVariable("vendorid") int vendorid,@RequestBody Product Product) {
		Vendor vendor = this.vendorService.getById(vendorid);
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			Product.setAreaId(vendor.getAreaId());
			Product.setCityId(vendor.getCityId());
			//如果inventory字段值不为0，说明有修改inventory，注意此时的inventory值是增加或减少的量，不是最终设置的数量
			int intv = Product.getInventory();
			String key = "pi_"+vendor.getCityId()+"_"+vendor.getAreaId()+"_"+Product.getId();
			//if(intv!=0){//从缓存中取出现有的库存
				Object o = redisUtil.redisQueryObject(key);
				if(o==null){//缓存里面如果没有，从数据库里读
					String inv_sv = this.productService.getInventoryAndSV(vendor.getCityId(), vendor.getAreaId(), Product.getId()+"");
					if(inv_sv!=null){
						redisUtil.redisSaveInventory("pi_"+vendor.getCityId()+"_"+vendor.getAreaId()+"_"+Product.getId(), inv_sv.split("_")[0]);
						redisUtil.redisSaveInventory("sv_"+vendor.getCityId()+"_"+vendor.getAreaId()+"_"+Product.getId(), inv_sv.split("_")[1]);
					}
				}
				int actualInv = Integer.valueOf(redisUtil.redisQueryObject(key))+intv;
				Product.setInventory(actualInv);
			//}
			Product.setStatus("2");
			//如果现在库存大于现在最低库存
			if(Product.getInventory()<=Product.getThrehold())
				Product.setStatus("1");
			if(Product.getInventory()<=0)
				Product.setStatus("0");
			int res = this.productService.update(Product);
			if(res==0){
				//数据库中修改成功后，再更新缓存中的库存
				if(intv!=0){
					redisUtil.addInventoryTx(key, intv);
				}
				logger.info("修改商品成功name=" + Product.getProductName());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("修改商品成功失败name=" + Product.getProductName());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	@AuthPassport
	@RequestMapping(value = "/sedit/{superid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sedit(@PathVariable("superid") int superid,@RequestBody Product Product) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int res = this.productService.updateRepProduct(Product);
			if(res==0){
				logger.info("修改商品成功name=" + Product.getProductName());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("修改商品成功失败name=" + Product.getProductName());
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
	 * 下架产品
	 */
	@AuthPassport
	@RequestMapping(value = "/vnotshow/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vnotshow(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int prodcutId = paramObject.getInteger("productId");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			String tableName = "Product_"+vendor.getCityId()+"_"+vendor.getAreaId();
			int res = this.productService.notShowProduct(tableName,prodcutId);
			if(res==0){
				logger.info("商品下架成功id=" + prodcutId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("商品下架失败id=" + prodcutId);
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
	 * 上架产品
	 */
	@AuthPassport
	@RequestMapping(value = "/vshow/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vshow(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int prodcutId = paramObject.getInteger("productId");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			String tableName = "Product_"+vendor.getCityId()+"_"+vendor.getAreaId();
			int res = this.productService.showProduct(tableName,prodcutId);
			if(res==0){
				logger.info("商品上架成功id=" + prodcutId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("商品上架失败id=" + prodcutId);
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@AuthPassport
	@RequestMapping(value = "/vdelete/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vdelete(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int prodcutId = paramObject.getInteger("productId");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			String tableName = "Product_"+vendor.getCityId()+"_"+vendor.getAreaId();
			int res = this.productService.deleteProduct(tableName,prodcutId);
			if(res==0){
				String inventoryKey = "pi_"+vendor.getCityId()+"_"+vendor.getAreaId()+"_"+prodcutId;
				String salesVolumeKey = "sv_"+vendor.getCityId()+"_"+vendor.getAreaId()+"_"+prodcutId;
				Object object = redisUtil.redisQueryObject(inventoryKey);
				if(object!=null){
					redisUtil.redisDeleteKey(inventoryKey);
					Object object2 = redisUtil.redisQueryObject(salesVolumeKey);
					if(object2!=null)
						redisUtil.redisDeleteKey(salesVolumeKey);
				}
				logger.info("商品删除成功id=" + prodcutId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("商品删除失败id=" + prodcutId);
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }

	@AuthPassport
	@RequestMapping(value = "/sdelete/{supervisorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object sdelete(@PathVariable("supervisorid") int supervisorid,@RequestBody JSONObject paramObject) {
		Supervisor supervisor = this.supervisorService.getById(supervisorid);
		int prodcutId = paramObject.getInteger("productId");
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			String tableName = "Product";
			int res = this.productService.deleteProduct(tableName,prodcutId);
			if(res==0){
				logger.info("商品删除成功id=" + prodcutId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("商品删除失败id=" + prodcutId);
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@AuthPassport
	@RequestMapping(value = "/snew/{supervisorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object addBySupervisor(@PathVariable("supervisorid") int superid,@RequestBody Product product) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int res = this.productService.insertToWhole(product);
			
			logger.info("超级管理员添加产品页面完成："+product.getProductName());
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
	

	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Product Product) {
		int res = this.productService.update(Product);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改商品信息成功id=" + Product.getId());
        	jsonObject.put("msg", "修改商品信息成功");
		}
		else{
			logger.info("修改商品信息失败id=" + Product.getId());
        	jsonObject.put("msg", "修改商品信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Product> Products = null;
		//则根据关键字查询
		Products = this.productService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询商品信息完成");
        return Products;
    }
	
	/**
	 * @param areaId
	 * @return
	 * 根据cityId和areaId查出所有商品，即到指定的Product_cityId_areaId表里查
	 * curl -X GET 'http://localhost:8080/wxmall/product/query/1/1'
	 */
	@RequestMapping(value = "/all/{cityId:\\d+}/{areaId:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByCityAreaId(@PathVariable("cityId")int cityId,@PathVariable("areaId")int areaId) {
		List<Product> products = new ArrayList<Product>();
		JSONObject jsonObject = new JSONObject();
		//则根据关键字查询
		products = this.productService.queryByCityAreaId(cityId,areaId);
		Area a = this.areaService.getById(areaId);
		List<String> catalog = new ArrayList<String>();
		String areaphone = "";
		if(a!=null&&a.getCatalogs()!=null&&!"".equals(a.getCatalogs())){
			String[] catalogs = a.getCatalogs().split(",");
			for(String c : catalogs){
				catalog.add(c.split("=")[0]);
			}
			areaphone=a.getPhoneNumber();
		}
		List<Banner> banners = this.bannerService.queryByAreaId(areaId);
		logger.info("获取城市 "+cityId+" 和区域 "+areaId+"下的所有商品信息完成");
		jsonObject.put("msg", "200");
		jsonObject.put("products", products);//不用序列化，方便前端jquery遍历
		jsonObject.put("catalog", catalog);
		jsonObject.put("banners", banners);
		jsonObject.put("areaphone",areaphone);
		return jsonObject;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Product> Products = null;
		//则查询返回所有
		Products = this.productService.queryAll();
		logger.info("查询所有商品信息完成");
        return Products;
    }
	
	/**
	 * @param files
	 * @param request
	 * @return
	 * 上传正方形缩略图
	 */
	@RequestMapping(value = "/uploadImgs", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadImgs(@RequestParam("upfiles") CommonsMultipartFile[] files, HttpServletRequest request) {
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
	 * 上传长方形缩略图
	 */
	@RequestMapping(value = "/uploadImgb", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadImgb(@RequestParam("upfileb") CommonsMultipartFile[] files, HttpServletRequest request) {
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
	 * 上传详情1图
	 */
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
	
	@RequestMapping(value = "/search/{id:\\d+}", method = RequestMethod.GET)
  public @ResponseBody
  ModelAndView search(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="catalog", required=false) String cat) throws UnsupportedEncodingException {
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		cat = new String(cat.getBytes("iso8859-1"),"utf-8");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_productSearch");
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		Vendor vendor = this.vendorService.getById(id);
		List<Product> products = null;
		List<Catalog> catalogs = new ArrayList<Catalog>();
		if(vendor!=null){
			products = this.productService.searchProduct(vendor.getCityId(),vendor.getAreaId(),keyword,cat);
			Area area = this.areaService.getById(vendor.getAreaId());
			if(area!=null){
				String catalogStr = area.getCatalogs();
				if(catalogStr!=null && !"".equals(catalogStr.trim())){
					String[] catalogList = catalogStr.split(",");
					for(String c : catalogList){
						Catalog cc = new Catalog();
						cc.setName(c.split("=")[0]);
						cc.setSequence(c.split("=")[1]);
						catalogs.add(cc);
					}
				}
			}
			modelAndView.addObject("city_id", vendor.getCityId());
			modelAndView.addObject("area_id", vendor.getAreaId());
		}
	    modelAndView.addObject("products", products);  
	    modelAndView.addObject("catalogs", catalogs); 
	    
		return modelAndView;
	}
	
	/**
	 * @param id
	 * @param token
	 * @param keyword
	 * @param cat
	 * @return
	 * @throws UnsupportedEncodingException
	 * 总后台搜索商品库
	 */
	@RequestMapping(value = "/searchrep/{id:\\d+}", method = RequestMethod.GET)
	  public @ResponseBody
	  ModelAndView searchrep(@PathVariable("id") int id,
				@RequestParam(value = "token", required = false) String token,
				@RequestParam(value="keyword", required=false) String keyword) throws UnsupportedEncodingException {
			keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("s_productSearch");
			if (token == null) {
				return modelAndView;
			}
			modelAndView.addObject("id", id);
			modelAndView.addObject("token", token);
			List<Product> ps = this.productService.searchRepProducts(keyword);
			//这里假设放一些东西进去
			logger.info("搜索商品库信息完成(keyword):"+keyword);
		    modelAndView.addObject("products", ps);    
		    modelAndView.addObject("id", id);
		    modelAndView.addObject("token", token);
		    
			return modelAndView;
		}
	
	/**
	 * @return
	 * 这个是从所有Product_cityId表里查所有分库里的商品
	 */
	@RequestMapping(value = "/s_queryall", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView query_All() {
		//先查出所有area，从而找到所有cityId_areaId来确定Product_cityId_areaId表
		List<Area> areas = this.areaService.queryAll();
		List<Product> products = new LinkedList<Product>();
		for(Area a : areas){
			List<Product> ps = this.productService.queryByCityAreaId(a.getCityId(),a.getId());
			if(ps!=null)
				products.addAll(ps);
		}
		//这里假设放一些东西进去
		logger.info("查询所有商品信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("products", products);  
	    modelAndView.setViewName("s_productList");  
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 没有分页，返回所有
	 */
	@RequestMapping(value = "/s_products/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView queryRepproducts(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.setViewName("s_productList");  
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		List<Product> ps = this.productService.queryRepProducts();
		//这里假设放一些东西进去
		logger.info("查询商品库信息完成");
	    modelAndView.addObject("products", ps);  
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @param showPage
	 * @return
	 * 增加了分页
	 */
	@RequestMapping(value = "/s_products/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView queryRepproductsIndex(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.setViewName("s_productList");  
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		List<Product> ps = null;
		int pageCount = 0;//需要分页的总数
		Supervisor supervisor = this.supervisorService.getById(id);
		if(supervisor!=null){
			int recordCount = this.productService.getRecordCount();
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			//int position=(showPage-1)*MakaoConstants.PAGE_SIZE+1;
			//ps = this.productService.queryFromToIndex(position,position+MakaoConstants.PAGE_SIZE-1);
			//上面的方式根据id来限制查询范围，如果记录被删除后，会影响分页
			int offset = (showPage-1)*MakaoConstants.PAGE_SIZE;
			ps = this.productService.queryFromToIndexOffset(offset,MakaoConstants.PAGE_SIZE);
		}
		//这里假设放一些东西进去
		logger.info("查询商品库信息完成");
	    modelAndView.addObject("products", ps);  
	    modelAndView.addObject("pageCount", pageCount); 
	    return modelAndView;
    }
	
	@RequestMapping(value = "/s_catalogs/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView query_Catalogs() {
		//这里假设放一些东西进去
		List<Product> products = new ArrayList<Product>();
		Product oo = new Product();
		oo.setProductName("ddddddddd");
		products.add(oo);
		logger.info("查询所有有效订单信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("products", products);  
	    modelAndView.setViewName("s_productCatalog");  
	    return modelAndView;
    }
	
	/**
	 * @return
	 * 
	 */
	@RequestMapping(value = "/s_new/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView add(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_productAdd");  
		if(token==null){
			return modelAndView;
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token);  
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查看所有区域下需要补货的商品
	 */
	@RequestMapping(value = "/s_threhold/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView queryThrehold(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.setViewName("s_productThrehold");  
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		//获取所有区域，遍历它们
		List<Area> areas = this.areaService.queryAll();
		List<Product> ps = new LinkedList<Product>();
		if(areas==null){
			modelAndView.addObject("products", ps);  
		    return modelAndView;
		}			
		for(Area a : areas){
			String table = "Product_"+a.getCityId()+"_"+a.getId();
			List<Product> pros= this.productService.queryThreholds(table);
			//总后台需要在商品列表里显示其所在城市区域，但product里没有该字段，这里用description字段来存放
			if(pros!=null){
				for(Product p:pros){
					p.setDescription(a.getCityName()+a.getAreaName()+"(电话:"+a.getPhoneNumber()+")");
				}
				ps.addAll(pros);
			}
		}
		logger.info("查询商品库信息完成");
	    modelAndView.addObject("products", ps);  
	    return modelAndView;
    }
	
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 跳转时要获取当前area已有的catalog，已有的总库里的所有商品
	 */
	@RequestMapping(value = "/v_new/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaAdd(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_productAdd");  
		if(token==null){
			return modelAndView;
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token);   
	    //Vendor vendor = this.vendorService.getById(id);
	    //这里是返回后台商品库的所有商品，即从Product表中获取
		List<Product> products = this.productService.queryRepProducts();
 
	    modelAndView.addObject("products", products);  
	    Vendor vendor = this.vendorService.getById(id);
		List<Catalog> catalogs = new ArrayList<Catalog>();
		if(vendor!=null)
		{
			Area area = this.areaService.getById(vendor.getAreaId());
			if(area!=null){
				String catalogStr = area.getCatalogs();
				if(catalogStr!=null && !"".equals(catalogStr.trim())){
					String[] catalogList = catalogStr.split(",");
					for(String c : catalogList){
						Catalog cc = new Catalog();
						cc.setName(c.split("=")[0]);
						cc.setSequence(c.split("=")[1]);
						catalogs.add(cc);
					}
				}
			}
		}
		modelAndView.addObject("catalogs", catalogs);
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 除了产品列表，还需要catalog列表，因为里面有编辑产品的选项
	 * 这个方法是没有加入分页的，返回所有的商品列表
	 */
//	@RequestMapping(value = "/v_manage/{id:\\d+}", method = RequestMethod.GET)
//    public @ResponseBody
//    ModelAndView areaManage(@PathVariable("id") int id,
//			@RequestParam(value = "token", required = false) String token) {
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("v_productManage");
//		if (token == null) {
//			return modelAndView;
//		}
//		modelAndView.addObject("id", id);
//		modelAndView.addObject("token", token);
//		Vendor vendor = this.vendorService.getById(id);
//		List<Product> products = null;
//		List<Catalog> catalogs = new ArrayList<Catalog>();
//		if(vendor!=null){
//			products = this.productService.queryByCityAreaId(vendor.getCityId(),vendor.getAreaId());
//			Area area = this.areaService.getById(vendor.getAreaId());
//			if(area!=null){
//				String catalogStr = area.getCatalogs();
//				if(catalogStr!=null && !"".equals(catalogStr.trim())){
//					String[] catalogList = catalogStr.split(",");
//					for(String c : catalogList){
//						Catalog cc = new Catalog();
//						cc.setName(c.split("=")[0]);
//						cc.setSequence(c.split("=")[1]);
//						catalogs.add(cc);
//					}
//				}
//			}
//			modelAndView.addObject("city_id", vendor.getCityId());
//			modelAndView.addObject("area_id", vendor.getAreaId());
//		}
//	    modelAndView.addObject("products", products);  
//	    modelAndView.addObject("catalogs", catalogs); 
//	    
//		return modelAndView;
//	}
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 因为要加入分页，所以进入产品管理时要先获取总的商品数，计算出总页数
	 * 并根据showPage显示对应页的记录
	 */
	@RequestMapping(value = "/v_manage/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaManageIndex(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_productManage");
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		Vendor vendor = this.vendorService.getById(id);
		List<Product> products = null;
		List<Catalog> catalogs = new ArrayList<Catalog>();
		int pageCount = 0;//需要分页的总数
		if(vendor!=null){
			int recordCount = this.productService.getRecordCount(vendor.getCityId(),vendor.getAreaId());
			System.out.println("total product count:"+recordCount);
			//需要分页的总数
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//int showPage = 1;
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			//int position=(showPage-1)*MakaoConstants.PAGE_SIZE+1;
			//products = this.productService.queryFromToIndex(vendor.getCityId(),vendor.getAreaId(),position,position+MakaoConstants.PAGE_SIZE-1);
			//上面的方式根据id来限制查询范围，如果记录被删除后，会影响分页
			int offset = (showPage-1)*MakaoConstants.PAGE_SIZE;
			products = this.productService.queryFromToIndexOffset(vendor.getCityId(),vendor.getAreaId(),offset,MakaoConstants.PAGE_SIZE);
			Area area = this.areaService.getById(vendor.getAreaId());
			if(area!=null){
				String catalogStr = area.getCatalogs();
				if(catalogStr!=null && !"".equals(catalogStr.trim())){
					String[] catalogList = catalogStr.split(",");
					for(String c : catalogList){
						Catalog cc = new Catalog();
						cc.setName(c.split("=")[0]);
						cc.setSequence(c.split("=")[1]);
						catalogs.add(cc);
					}
				}
			}
			modelAndView.addObject("city_id", vendor.getCityId());
			modelAndView.addObject("area_id", vendor.getAreaId());
		}
	    modelAndView.addObject("products", products);  
	    modelAndView.addObject("catalogs", catalogs); 
	    modelAndView.addObject("pageCount", pageCount); 
	    
		return modelAndView;
	}
	
	@RequestMapping(value = "/v_catalog/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaCatalog(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_productCatalog");
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		Vendor vendor = this.vendorService.getById(id);
		List<Catalog> catalogs = new ArrayList<Catalog>();
		if(vendor!=null)
		{
			Area area = this.areaService.getById(vendor.getAreaId());
			if(area!=null){
				String catalogStr = area.getCatalogs();
				if(catalogStr!=null && !"".equals(catalogStr.trim())){
					String[] catalogList = catalogStr.split(",");
					for(String c : catalogList){
						Catalog cc = new Catalog();
						cc.setName(c.split("=")[0]);
						cc.setSequence(c.split("=")[1]);
						catalogs.add(cc);
					}
				}
			}
		}
		modelAndView.addObject("catalogs", catalogs);
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 获取当前area下的Banner配置，并跳转到综合配置页面
	 */
	@RequestMapping(value = "/v_promotion/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaPromotion(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_promotionManage");
		if (token == null) {
			return modelAndView;
		}
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		Vendor vendor = this.vendorService.getById(id);
		List<Banner> banners = null;
		if(vendor!=null){
			banners = this.bannerService.queryByAreaId(vendor.getAreaId());
		}
		modelAndView.addObject("banners", banners);
		return modelAndView;
    }
	
}
