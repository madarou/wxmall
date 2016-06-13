$(document).ready(function(){
	//商品名称，原产地，分类，库存状态
	 $(".length_input_15").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>15){
			 $(this).val($(this).val().substring(0,15));
		 }
	 });
	 //商品规格
	 $(".length_input_20").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>20){
			 $(this).val($(this).val().substring(0,20));
		 }
	 });
	 //售价，市场价
	 $(".price_input").on('blur',function(){
		 var reg = new RegExp("^[0-9]+(.[0-9]{2})+$");
		 if($(this).val()!="" && !reg.test($(this).val())){  
		     alert("请输入0.00或00.00格式的数字");
		     $(this).val("");
		 }  
	 });
	//商品简介
	 $(".length_input_10").on('input',function(){
		 var len = $.trim($(this).val()).length;
		 if(len>10){
			 $(this).val($(this).val().substring(0,10));
		 }
	 });
});