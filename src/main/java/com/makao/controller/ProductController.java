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
import com.makao.entity.Product;
import com.makao.service.IProductService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	private static final Logger logger = Logger.getLogger(ProductController.class);
	@Resource
	private IProductService productService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Product get(@PathVariable("id") Integer id)
	{
		logger.info("获取商品信息id=" + id);
		Product Product = (Product)this.productService.getById(id);
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
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Product Product) {
		int res = this.productService.insert(Product);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加商品成功id=" + Product.getId());
        	jsonObject.put("msg", "增加商品成功");
		}
		else{
			logger.info("增加商品成功失败id=" + Product.getId());
        	jsonObject.put("msg", "增加商品失败");
		}
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
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Product> Products = null;
		//则查询返回所有
		Products = this.productService.queryAll();
		logger.info("查询所有商品信息完成");
        return Products;
    }
}
