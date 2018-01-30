package com.makao.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Banner;
import com.makao.entity.Vendor;
import com.makao.service.IBannerService;
import com.makao.service.IVendorService;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/banner")
public class BannerController {
	private static final Logger logger = Logger.getLogger(BannerController.class);
	@Resource
	private IBannerService bannerService;
	@Resource
	private IVendorService vendorService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Banner get(@PathVariable("id") Integer id)
	{
		logger.info("获取地址信息id=" + id);
		Banner banner = (Banner)this.bannerService.getById(id);
		return banner;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.bannerService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除地址信息成功id=" + id);
        	jsonObject.put("msg", "删除地址信息成功");
		}
		else{
			logger.info("删除地址信息失败id=" + id);
        	jsonObject.put("msg", "删除地址信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Banner Banner) {
		int res = this.bannerService.insert(Banner);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加地址成功id=" + Banner.getId());
        	jsonObject.put("msg", "增加地址成功");
		}
		else{
			logger.info("增加地址成功失败id=" + Banner.getId());
        	jsonObject.put("msg", "增加地址失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Banner Banner) {
		int res = this.bannerService.update(Banner);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改地址信息成功id=" + Banner.getId());
        	jsonObject.put("msg", "修改地址信息成功");
		}
		else{
			logger.info("修改地址信息失败id=" + Banner.getId());
        	jsonObject.put("msg", "修改地址信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Banner> banneres = null;
		//则根据关键字查询
		banneres = this.bannerService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询地址信息完成");
        return banneres;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Banner> banneres = null;
		//则查询返回所有
		banneres = this.bannerService.queryAll();
		logger.info("查询所有地址信息完成");
        return banneres;
    }
	
	/**
	 * @return
	 * 根据areaid从Area表中获取某个area里配置的所有分类的banner
	 */
	@RequestMapping(value = "/all/{areaid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryArea(@PathVariable("areaid")int areaid) {
		List<Banner> banners = this.bannerService.queryByAreaId(areaid);
		logger.info("查询areaid="+areaid+"下所有banner信息完成");
        return banners;
    }
	
	/**
	 * @param vendorid
	 * @param paramObject
	 * @return
	 * 下线banner
	 */
	@RequestMapping(value = "/vdown/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vdown(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int bannerId = paramObject.getInteger("bannerId");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			Banner banner = this.bannerService.getById(bannerId);
			banner.setStatus("下线中");
			int res = this.bannerService.update(banner);
			if(res==0){
				logger.info("Banner下线成功id=" + bannerId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("Banner下线失败id=" + bannerId);
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
	 * 上线banner
	 */
	@RequestMapping(value = "/vup/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vup(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int bannerId = paramObject.getInteger("bannerId");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			Banner banner = this.bannerService.getById(bannerId);
			banner.setStatus("上线中");
			int res = this.bannerService.update(banner);
			if(res==0){
				logger.info("Banner上线成功id=" + bannerId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("Banner上线失败id=" + bannerId);
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
	}
	@AuthPassport
	@RequestMapping(value = "/vedit/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vedit(@PathVariable("vendorid") int vendorid,@RequestBody JSONObject paramObject) {
		Vendor vendor = this.vendorService.getById(vendorid);
		int bannerId = paramObject.getInteger("bannerId");
		String productUrl = paramObject.getString("productUrl");
		String imgUrl = paramObject.getString("imgUrl");
		JSONObject jsonObject = new JSONObject();
		if(vendor!=null){
			Banner banner = this.bannerService.getById(bannerId);
			banner.setProductUrl(productUrl);
			banner.setImgUrl(imgUrl);
			banner.setStatus("下线中");
			int res = this.bannerService.update(banner);
			if(res==0){
				logger.info("Banner上线成功id=" + bannerId);
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("Banner上线失败id=" + bannerId);
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
	 * 上传banner图片
	 */
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadImg(@RequestParam("upfile") CommonsMultipartFile[] files, HttpServletRequest request) {
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
