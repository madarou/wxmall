package com.makao.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

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
import com.makao.entity.City;
import com.makao.entity.Supervisor;
import com.makao.service.ICityService;
import com.makao.service.ISupervisorService;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/city")
public class CityController {
	private static final Logger logger = Logger.getLogger(CityController.class);
	@Resource
	private ICityService cityService;
	@Resource
	private ISupervisorService supervisorService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody City get(@PathVariable("id") Integer id)
	{
		logger.info("获取city信息id=" + id);
		City city = (City)this.cityService.getById(id);
		return city;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.cityService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除city信息成功id=" + id);
        	jsonObject.put("msg", "删除city信息成功");
		}
		else{
			logger.info("删除city信息失败id=" + id);
        	jsonObject.put("msg", "删除city信息失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param city
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"cityName":"上海"}' 'http://localhost:8080/wxmall/city/new'
	 */
	@RequestMapping(value = "/new/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("id") int superid, @RequestBody City city) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int res = this.cityService.insert(city);
			if(res==0){
				logger.info("增加city成功id=" + city.getId());
	        	jsonObject.put("msg", "200");
	        	 return jsonObject;
			}
			else{
				logger.info("增加city成功失败id=" + city.getId());
	        	jsonObject.put("msg", "201");
	        	 return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
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
//                //拿到输出流，同时重命名上传的文件  
//                FileOutputStream os = new FileOutputStream(imgFolder + realImgName);  
//                //拿到上传文件的输入流  
//                FileInputStream in = (FileInputStream) upfile.getInputStream();  
//                 
//                //以写字节的方式写文件  
//                int b = 0;  
//                while((b=in.read()) != -1){  
//                    os.write(b);  
//                }  
//                os.flush();  
//                os.close();  
//                in.close();  
            	File tofile = new File(imgFolder + realImgName);
            	upfile.transferTo(tofile);
                int finaltime = (int) System.currentTimeMillis();  
                System.out.println("上传图片用时:"+(finaltime - pre));  
                logger.info("图片 "+realImgName+" 成功写入本地文件");
                jsonObject.put("msg", "上传成功");
                jsonObject.put("imgName", realImgName);
                return jsonObject;
                
            } catch (Exception e) {  
                e.printStackTrace();  
                System.out.println("图片"+realImgName+"写入本地文件出错");  
                logger.error("图片 "+realImgName+" 写入本地文件出错" + e);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }  
		}
        jsonObject.put("msg", "上传失败");
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody City city) {
		//注册用户的代码
		int res = this.cityService.update(city);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改city信息成功id=" + city.getId());
        	jsonObject.put("msg", "修改city信息成功");
		}
		else{
			logger.info("修改city信息失败id=" + city.getId());
        	jsonObject.put("msg", "修改city信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<City> cities = null;
		//则根据关键字查询
		cities = this.cityService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询city信息完成");
        return cities;
    }
	
	@RequestMapping(value = "/queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll(@PathVariable("id") int superid) {
		Supervisor supervisor = this.supervisorService.getById(superid);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			List<City> cities = null;
			//则查询返回所有
			cities = this.cityService.queryAll();
			logger.info("查询所有city信息完成");
			
			jsonObject.put("msg", "200");
			jsonObject.put("cities", cities);//不用序列化，方便前端jquery遍历
			return jsonObject;
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
		@RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    Object all() {
		JSONObject jsonObject = new JSONObject();
			List<City> cities = null;
			//则查询返回所有
			cities = this.cityService.queryAll();
			logger.info("查询所有city信息完成");
			
			jsonObject.put("msg", "200");
			jsonObject.put("cities", cities);//不用序列化，方便前端jquery遍历
			return jsonObject;

    }
}
