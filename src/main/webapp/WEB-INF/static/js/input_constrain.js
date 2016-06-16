$(document).ready(function(){
	//商品名称，原产地，分类，库存状态，商品简介，城市名称，vendor名，会员名，优惠券名
	 $(".length_input_20").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>20){
			 $(this).val($(this).val().substring(0,20));
		 }
	 });
	 //商品规格
	 $(".length_input_30").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>30){
			 $(this).val($(this).val().substring(0,30));
		 }
	 });
	 //售价，市场价
	 $(".price_input").on('blur',function(){
		 //var reg = new RegExp("^[0-9]+(\\.{1})([0-9]{2})$");
		 var reg = /[0-9]+(\.{1})([0-9]{2})$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入0.00或00.00格式的数字");
		     $(this).val("");
		     $(this).focus();
		 }  
	 });
	//经纬度
	 $(".titude_input").on('blur',function(){
		 var reg = /^\d+(\.|\d+)*$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入0.00或00.00格式的数字");
		     $(this).val("");
		     $(this).focus();
		 }  
		 if(content.length>12){
			 alert("长度不能大于12");
			 $(this).focus();
		 }
	 });
	//服务电话
	 $(".phone_input").on('blur',function(){
		 var reg = /^(\d+){11}$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入正确格式的手机号码");
		     $(this).val("");
		     $(this).focus();
		 } 
	 });
	//积分
	 $(".point_input").on('blur',function(){
		 var reg = /^(\d+)+$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入正确格式的积分");
		     $(this).val("");
		     $(this).focus();
		 } 
	 });
	 //收货地址
	 $(".length_input_120").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>120){
			 $(this).val($(this).val().substring(0,120));
		 }
	 });
	 //会员等级，商品分类名
	 $(".length_input_10").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>10){
			 $(this).val($(this).val().substring(0,10));
		 }
	 });
	//优惠券面值，不超过10位数的整数
	 $(".coupon_amount_input").on('blur',function(){
		 var reg = /^(\d){1,10}$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入10位以下的整数");
		     $(this).val("");
		     $(this).focus();
		 } 
	 });
	//优惠券使用限制
	 $(".coupon_restrict_input").on('blur',function(){
		 var reg = /^(\d+)+$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入整数");
		     $(this).val("");
		     $(this).focus();
		 } 
	 });
	 //优惠券简单说明
	 $(".length_input_50").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>50){
			 $(this).val($(this).val().substring(0,50));
		 }
	 });
	 //商品库存，商品排序，分类名的排序，整数
	 $(".inventory_input").on('blur',function(){
		 var reg = /^(\d+)+$/g;
		 var content = $.trim($(this).val());
		 if(content!="" && !reg.test(content)){  
		     alert("请输入正确格式的整数");
		     $(this).val("");
		     $(this).focus();
		 } 
	 });
});