<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
<link rel="shortcut icon" href="/static/images/icon/favicon.ico" type="image/x-icon" />
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style type="text/css">
    .btm_btn{text-align: center;}
    .btm_btn .input_btn{display:inline-block;height:35px;border:none;background:none;padding:0 20px;}
    .btm_btn .trueBtn{background:#19a97b;color:white;border:1px #19a97b solid;border-radius:2px;}
</style>
<!--[if lt IE 9]>
<script src="static/js/html5.js"></script>
<![endif]-->
<script src="static/js/jquery.js"></script>
<script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="static/js/ajaxfileupload.js" type="text/javascript"></script>
<script src="static/js/input_constrain.js" type="text/javascript"></script>
<script>
	(function($){
		$(window).load(function(){
			
			$("a[rel='load-content']").click(function(e){
				e.preventDefault();
				var url=$(this).attr("href");
				$.get(url,function(data){
					$(".content .mCSB_container").append(data); //load new content inside .mCSB_container
					//scroll-to appended content 
					$(".content").mCustomScrollbar("scrollTo","h2:last");
				});
			});
			
			$(".content").delegate("a[href='top']","click",function(e){
				e.preventDefault();
				$(".content").mCustomScrollbar("scrollTo",$(this).attr("href"));
			});
			
		});
	})(jQuery);
</script>
</head>
<body>
<iframe name="mframe" src="/orderOff/hasNew/${id}/?token=${token}" frameborder="0" scrolling="no" width="100%" height="70px" onload="document.all['mframe'].style.height=mframe.document.body.scrollHeight"></iframe>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
  <h2>社享网</h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/s_queryall/${id}/1?token=${token}">所有未完成订单</a></dd>
    <dd><a href="/orderOff/s_queryall/${id}/1?token=${token}">所有已完成订单</a></dd>
    <dd><a href="/orderOff/s_query_refund/${id}/1?token=${token}">退款订单</a></dd>
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/s_products/${id}/1?token=${token}">商品库</a></dd>
    <dd><a href="/product/s_threhold/${id}?token=${token}">待补货商品</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/s_queryall/${id}/1?token=${token}">会员中心</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>区域管理</dt>
    <dd><a href="/area/s_queryall/${id}?token=${token}">区域设置</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>账号管理</dt>
    <dd><a href="/vendor/s_queryall/${id}?token=${token}">账号管理</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>礼券管理</dt>
    <dd><a href="/coupon/s_queryall/${id}?token=${token}" class="active">优惠券管理</a></dd>
   </dl>
  <li>
   <p class="btm_infor">© 社享网 版权所有</p>
  </li>
 </ul>
</aside>
<section class="rt_wrap content mCustomScrollbar">
 <div class="rt_content">
     <section>
        <!-- <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
        <hr/>
     </section>

     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
		 //弹出文本性提示框
		 $(".popAdd").click(function(){
			 //$("#ccity").empty();
	    	 //$("#ccity").get(0).options.add(new Option("选择城市","选择城市"));
	    	 $.ajax({
	    		 type: "GET",
		          contentType: "application/json",
		          url: "/city/queryall/"+$("#loginUserId").val(),
		          dataType: "json",
		          success: function(data){
		        	  var cities = JSON.stringify(data.cities);
		        	  if(data.msg=="200"){
		        		  $.each(data.cities,function(i, val){
		        			  //$("#ccity").get(0).options.add(new Option(val.cityName,val.id));
		        			  $("#cityspan").append(val.cityName+'<input class="citycheck" style="margin-right:10px" type="checkbox" title="'+val.cityName+'" value="'+val.id+'"/>'
								);
		        		  });
		        	  }
		          }
	    	 });
			 $(".pop_bg").fadeIn();
			 });
		 //弹出：确认按钮
		 $("#saveBtn").click(function(){
			 /* var cityId = $("#ccity").val();//这种方式获取的是value
	    	 if(cityId=="选择城市"){
	    		 alert("请选择所属城市");
	    		 return false;
	    	 } */
			 //获取复选框选中的城市
			 var citys = $("#cityspan").children(".citycheck");
	    	 var cityName ="";//使用cityid-cityid的拼接作为cityName字段
	    	 $.each(citys,function(i, val){
	    		 if($(val).is(':checked')) {
	    			 cityName = cityName + $(val).val()+"#_#"+$(val).attr("title")+"-_-";
	    		 }
	    	 });
	    	 if(cityName==""){
	    		 alert("请选择生效城市");
	    		 return false;
	    	 }
	    	 cityName=cityName.substr(0,cityName.length-3);
	    	 //var cityName = $("#ccity").find("option:selected").text();
			 var name = $.trim($("#cname").val());
			 var type = $.trim($("#ctype").val());
			 var amount = $.trim($("#camount").val());
			 var point = $.trim($("#cpoint").val());
			 var restrict = $.trim($("#crestrict").val());
			 var comment = $.trim($("#ccomment").val());
			 var isshow = $("#cisshow").val();
			 var coverSUrl = $.trim($("#serverImgNamed1").val());
			 var coverBUrl = $.trim($("#serverImgNamed2").val());
			 if(name=="" || amount=="" || point=="" || restrict=="" || comment=="" || coverSUrl=="" || coverBUrl==""){
				 alert("名称、面值、消耗积分、使用限制、简单说明、封面和详情图不能为空");
				 return false;
			 }
			 $.ajax({
		          type: "POST",
		          contentType: "application/json",
		          url: "/coupon/new/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
		          data: JSON.stringify({"name":name,"type":type,"cityId":0,"cityName":cityName,"amount":amount,"point":point,"restrict":restrict,
		        	  					"comment":comment,"isShow":isshow,"coverSUrl":coverSUrl,"coverBUrl":coverBUrl}),
		          dataType: "json",
		          success: function(data){
		                  if(data.msg=="200"){
		                	  alert("增加优惠券成功");
		                	  window.location.reload();
		                  }else if(data.msg=="401"){
		                	  alert("需要重新登录");
		                  }
		                  else{
		                	  alert("增加优惠券失败");
		                  }
		          }
		      });
			 
			 });
		 //弹出：取消或关闭按钮
		 $("#cancelBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 $("#cname").val("");
			 $("#camount").val("");
			 $("#cpoint").val("");
			 $("#crestrict").val("");
			 $("#ccomment").val("");
			 $("#cisshow").val("");
			 $("#serverImgNamed1").val("");
			 $("#serverImgNamed2").val("");
			 $("#uploadd1").attr("src","");
			 $("#uploadd2").attr("src","");
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加优惠券</h3>
       <!--content-->
             <div class="pop_cont_input" style="overflow: scroll;width:500px;height:450px;padding:0px">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text" style="padding:0px">
             <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name" style="width:120px;">名称：</span>
		        <input type="text" id="cname" class=" textbox_295 length_input_20" placeholder="如'10元代金券'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">类型：</span>
		        <input type="text" id="ctype" class=" textbox_295" style="background-color:rgba(0,0,0,0.1)" disabled="disabled" value="代金券兑换"/>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">面值(￥)：</span>
		        <input type="text" id="camount" class=" textbox_295 coupon_amount_input" placeholder="如'10'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">消耗积分：</span>
		        <input type="text" id="cpoint" class=" textbox_295 point_input" placeholder="如'20'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">使用限制：</span>
		        <input type="text" id="crestrict" class=" textbox_295 coupon_restrict_input" placeholder="消费满X元才能使用就填X"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">简单说明：</span>
		        <input type="text" id="ccomment" class=" textbox_295 length_input_50" placeholder="如'新用户欢迎礼券'"/>
		       </li>
		       <li>
		       <!--  <span class="item_name" style="width:120px;">生效城市：</span> -->
				<div><div class="item_name" style="width:120px;float:left">生效城市：</div><div id="cityspan" style="float:left;margin-left: 4px;min-height: 26px;width:295px;border:1px #139667 solid;padding:5px; vertical-align:middle;"></div></div>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">是否上线：</span>
		        <select class="select" id="cisshow">  
		        	<option value="no">暂不上线</option>
		        	<option value="yes">立即上线</option>
				</select>
		       </li>
							<li><span class="item_name" style="width: 120px;">封面图：</span>
								<img alt="(182 x 182)" id="uploadd1" src=""
								style="height: 100px; width: 100px; cursor: pointer">
								<div id="fileDivd1">
									<input id="fileToUploadd1" style="display: none" type="file"
										name="upfiled1">
								</div> <input type="hidden" id="serverImgNamed1" />
							</li>
							<li>
								<span class="item_name" style="width: 120px;">详细图：</span>
								<img alt="(1040 x 420)" id="uploadd2" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd2">
									<input id="fileToUploadd2" style="display: none" type="file"
										name="upfiled2">
								</div> <input type="hidden" id="serverImgNamed2" />
							</li>
		      </ul>
		     </section>
         </div>
        </div>
        <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="saveBtn" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelBtn" class="input_btn falseBtn"/>
         </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 删除分类弹出框 -->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	 var id_toDel = 0;//要删除的id
     //弹出文本性提示框
     $(".delCoupon").click(function(){
       $(".del_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       id_toDel = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
    	 if(id_toDel==0){
    		 alert("请重新选择要删除的优惠券");
    		 return false;
    	 }
    	 var cityId = $("#cityId-"+id_toDel).text();
         $.ajax({
    		  type: "POST",
  	          contentType: "application/json",
  	          url: "/coupon/delete/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
  	          dataType: "json",
  	          data:JSON.stringify({"couponId":id_toDel,"cityId":cityId}),
  	          success: function(data){
  	        	  if(data.msg=="200"){
  	        		  alert("删除优惠券成功");
  	        		  window.location.reload();
  	        		  id_toDel=0;
  	        	  }
  	        	  else if(data.msg=="201"){
  	        		  alert("删除优惠券失败");
  	        		  window.location.reload();
  	        		  id_toDel=0;
  	        	  }else if(data.msg=="401"){
  	        		  alert("需要重新登录");
  	        	  }
  	          }
    	 	});
       $(".del_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       id_toDel=0;
       });
     });
     </script>
     <section class="del_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          确认要删除该优惠券吗？
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="继续删除" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
     <!-- 删除分类弹出框 -->
     
      <!-- 上架下架提示框 -->
      <script>
     $(document).ready(function(){
    	var upHandle_Id = 0;//要上下架的优惠券id
    	var upAction = "";
     //弹出文本性提示框
     $(".upOrdown").click(function(){
       $(".coupon_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       upAction = clickedId.split("-")[0];
       upHandle_Id = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmAction").click(function(){
    	 if(upHandle_Id==0){
    		 alert("请重新选择上下架的优惠券");
    		 return false;
    	 }
    	var cityid = $("#cityId-"+upHandle_Id).text();
    	 if(upAction=="down"){//下线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/coupon/sdown/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"couponId":upHandle_Id,"cityId":cityid}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("下线成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
    	        		  alert("下线失败");
    	        		  window.location.reload();
    	        	  }else if(data.msg=="401"){
    	        		  alert("需要重新登录");
    	        	  }
     	          }
       	 	}); 
    	 }
    	 else if(upAction=="up"){//上线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/coupon/sup/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"couponId":upHandle_Id,"cityId":cityid}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("上线成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
     	        		  alert("上线失败");
     	        		  window.location.reload();
     	        	  }else if(data.msg=="401"){
     	        		  alert("需要重新登录");
     	        	  }
     	          }
       	 	}); 
    	 }
        	
       $(".coupon_pop_bg").fadeOut();
       upHandle_Id=0;
       upAction = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelAction").click(function(){
       $(".coupon_pop_bg").fadeOut();
       upHandle_Id=0;
       upAction = "";
       });
     });
     </script>
     <section class="coupon_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          确认要继续操作吗？
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="继续" id="confirmAction" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelAction" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
    <!--  上下线 -->
    
    <!-- 编辑优惠券 -->
 <script>
     $(document).ready(function(){
    	var editHandle_Id = 0;//要编辑的id
    	var nameO = "";
    	var amountO = "";
    	var pointO = 0;
    	var restrictO = 0;
    	var commentO = "";
    	var coverSUrlO = "";
    	var coverBUrlO = "";
    	var cityNameO = "";
    	var cityIdO = 0;
    	var isShowO = "";
    	//弹出文本性提示框
     $(".editCoupon").click(function(){
       $(".editproduct_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       editHandle_Id = clickedId.split("-")[1];
       //填充编辑框里的字段
       nameO = $.trim($("#name-"+editHandle_Id).text());
       amountO = $.trim($("#amount-"+editHandle_Id).text());
       pointO = $.trim($("#point-"+editHandle_Id).text());
       restrictO = $.trim($("#restrict-"+editHandle_Id).text());
       commentO = $.trim($("#comment-"+editHandle_Id).text());
       coverSUrlO =  $.trim($("#coverSUrl-"+editHandle_Id).text());
       coverBUrlO = $.trim($("#coverBUrl-"+editHandle_Id).text());
       cityNameO = $.trim($("#cityName-"+editHandle_Id).text());
       cityIdO = $.trim($("#cityId-"+editHandle_Id).text());
       isShowO = $.trim($("#isShow-"+editHandle_Id).text());

       $("#coname").val(nameO);
       $("#coamount").val(amountO);
       $("#copoint").val(pointO);
       $("#corestrict").val(restrictO);
       $("#cocomment").val(commentO);
       $("#cocity").empty();
  	   $("#cocity").get(0).options.add(new Option(cityNameO,cityIdO));

       $("#cuploadd1").attr("src", "/static/upload/"+coverSUrlO);
       $("#cserverImgNamed1").val(coverSUrlO);
       $("#cuploadd2").attr("src", "/static/upload/"+coverBUrlO);
       $("#cserverImgNamed2").val(coverBUrlO);
       $("#coisshow").val(isShowO);
       });
     //弹出：确认按钮
     $("#confirmEdit").click(function(){
    	 if(editHandle_Id==0){
    		 alert("请重新选择优惠券");
    		 return false;
    	 }
    	 	var name = $.trim($("#coname").val());
		 	var amount = $.trim($("#coamount").val());
		 	var point = $.trim($("#copoint").val());
		 	var restrict = $.trim($("#corestrict").val());
		 	var comment = $.trim($("#cocomment").val());
		 	var isshow = $("#coisshow").val();
		 	
		 	var coverSUrl = $("#cserverImgNamed1").val();
		 	var coverBUrl = $("#cserverImgNamed2").val();
		 	
		 	if(name == "" || amount=="" || point=="" || restrict=="" || comment==""){
		 		alert("名称、面值、消耗积分、使用限制、简单介绍不能为空");
		 		return false;
		 	}
		 	if(coverSUrl == "" || coverBUrl == ""){
		 		alert("封面图、详情图片必须上传");
		 		return false;
		 	}
		 	if(name == nameO && amount==amountO && point==pointO && restrict==restrictO && isshow == isShowO && 
		 			comment==commentO && coverSUrl==coverSUrlO && coverBUrl==coverBUrlO){
		 		alert("并未做修改");
		 		return false;
		 	}
    		$.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/coupon/sedit/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"couponId":editHandle_Id,"name":name,"amount":amount,"point":point,"restrict":restrict,"isShow":isshow,
	  	        		"comment":comment,"coverSUrl":coverSUrl,"coverBUrl":coverBUrl,"cityId":cityIdO}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("优惠券修改成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
    	        		  alert("商品修改失败");
    	        		  window.location.reload();
    	        	  }else if(data.msg=="401"){
    	        			alert("需要重新登录");
    	        	  }
     	          }
       	 	});   	
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
       	 nameO = "";
    	 amountO = "";
    	 pointO = 0;
    	 restrictO = 0;
    	 commentO = "";
    	 coverSUrlO = "";
    	 coverBUrlO = "";
    	 cityNameO = "";
    	 cityIdO = 0;
    	 isShowO = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelEdit").click(function(){
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
       	 nameO = "";
	   	 amountO = "";
	   	 pointO = 0;
	   	 restrictO = 0;
	   	 commentO = "";
	   	 coverSUrlO = "";
	   	 coverBUrlO = "";
	   	 cityNameO = "";
	   	 cityIdO = 0;
	   	 isShowO = "";
       });
     });
     </script>
     <section class="editproduct_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>商品管理——编辑</h3>
       <!--content-->
       <div class="pop_cont_input" style="overflow: scroll;width:500px;height:450px;padding:0px">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text" style="padding:0px">
             <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name" style="width:120px;">名称：</span>
		        <input type="text" id="coname" class="textbox_295 length_input_20" placeholder="如'10元代金券'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">类型：</span>
		        <input type="text" id="cotype" class="textbox_295" style="bcolor:grey" disabled="disabled" value="代金券兑换"/>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">面值(￥)：</span>
		        <input type="text" id="coamount" class=" textbox_295 coupon_amount_input" placeholder="如'10'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">消耗积分：</span>
		        <input type="text" id="copoint" class=" textbox_295 point_input" placeholder="如'20'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">使用限制：</span>
		        <input type="text" id="corestrict" class=" textbox_295 coupon_restrict_input" placeholder="消费满X元才能使用就填X"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">简单说明：</span>
		        <input type="text" id="cocomment" class=" textbox_295 length_input_50" placeholder="如'新用户欢迎礼券'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">生效城市：</span>
		        <select class="select" id="cocity" disabled="disabled">  
				</select>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">是否上线：</span>
		        <select class="select" id="coisshow">  
		        	<option value="no">暂不上线</option>
		        	<option value="yes">立即上线</option>
				</select>
		       </li>
							<li><span class="item_name" style="width: 120px;">封面图：</span>
								<img alt="(182 x 182)" id="cuploadd1" src=""
								style="height: 100px; width: 100px; cursor: pointer">
								<div id="cfileDivd1">
									<input id="cfileToUploadd1" style="display: none" type="file"
										name="cupfiled1">
								</div> <input type="hidden" id="cserverImgNamed1" />
							</li>
							<li>
								<span class="item_name" style="width: 120px;">详细图：</span>
								<img alt="(1040 x 420)" id="cuploadd2" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="cfileDivd2">
									<input id="cfileToUploadd2" style="display: none" type="file"
										name="cupfiled2">
								</div> <input type="hidden" id="cserverImgNamed2" />
							</li>
		      </ul>
		     </section>
         </div>
        </div>
        <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="confirmEdit" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelEdit" class="input_btn falseBtn"/>
         </div>
       </div>
     </section>
<!-- 编辑产品 -->

     <!-- 搜索 -->
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加优惠券" class="input_btn trueBtn popAdd"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>名称</th>
        <th>兑换积分</th>
        <th>类型</th>
        <th>抵用面值</th>
        <th>使用限制</th>
        <th>所属城市</th>
        <th>状态</th>
        <th>操作</th>
       </tr>
       	<c:forEach var="item" items="${coupons}" varStatus="status">
         	<tr>
         		<td id="name-${item.id}">${item.name}</td>
         		<td id="point-${item.id}">${item.point}</td>
         		<td id="type-${item.id}">${item.type}</td>
         		<td id="amount-${item.id}">${item.amount}</td>
         		<td id="restrict-${item.id}">${item.restrict}</td>
         		<td id="cityName-${item.id}">${item.cityName}</td>
         		<td style="text-align:center">
		           <c:choose> 
		  				<c:when test="${item.isShow=='no'}">   
		  					<button class="linkStyle" style="color:grey;cursor:default">下线中</button>|
		  					<button class="linkStyle upOrdown" id="up-${item.id}">上线</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle" style="color:grey;cursor:default">上线中</button>|
		  					<button class="linkStyle upOrdown" id="down-${item.id}">下线</button>
						</c:otherwise> 
					</c:choose>
		        </td>
		        <td style="text-align:center">
		        	<button class="linkStyle editCoupon" id="edit-${item.id}">编辑</button>|
		        	<button class="linkStyle delCoupon" id="del-${item.id}">删除</button>
		        </td>
		        <td id="cityId-${item.id}" style="display:none">${item.cityId}</td>
		        <td id="coverSUrl-${item.id}" style="display:none">${item.coverSUrl}</td>
		        <td id="coverBUrl-${item.id}" style="display:none">${item.coverBUrl}</td>
		        <td id="isShow-${item.id}" style="display:none">${item.isShow}</td>
		        <td id="comment-${item.id}" style="display:none">${item.comment}</td>
         	</tr>
		</c:forEach> 
      </table>
     </section>

    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<script>
//获取图片尺寸，并验证是否满足尺寸大小
function checkImageSize(url,callback){
		var img = new Image();
		img.src = url;
		// 如果图片被缓存，则直接返回缓存数据
		if(img.complete){
		    callback(img.width, img.height);
		}else{
			// 完全加载完毕的事件
		    img.onload = function(){
				callback(img.width, img.height);
		    }
	    }
 }
$("#uploadd1").on('click', function() {  
    $('#fileToUploadd1').click();  
});
//这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
$('#fileDivd1').on('change',function() {  
    $.ajaxFileUpload({  
        url:'/coupon/uploadImgd1',  
        secureuri:false,  
        fileElementId:'fileToUploadd1',//file标签的id  
        dataType: 'json',//返回数据的类型  
        success: function (data, status) {  
            if(data.msg=="200"){
                //alert("图片可用");
                $("#serverImgNamed1").val(data.imgName);
                $("#uploadd1").attr("src", "/static/upload/"+data.imgName);
                var imgSrc = $("#uploadd1").attr("src");
                var require = {wid:182,hei:182};
                checkImageSize(imgSrc,function(w,h){
            		if(w!=require.wid || h!=require.hei){
            			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
            			$("#serverImgNamed1").val("");
            			$("#uploadd1").attr("src", "");
            		}
            	 });
            }
            else if(data.msg=="图片不符合"){
           	 alert("图片不符合");
            }
            if(typeof(data.error) != 'undefined') {  
                if(data.error != '') {  
                    alert(data.error);  
                } else {  
                    alert(data.msg);  
                }  
            }
         }, 
        error: function (data, status, e) {  
            alert(e);  
        }  
    });  
});
$("#uploadd2").on('click', function() {  
    $('#fileToUploadd2').click();  
});
//这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
$('#fileDivd2').on('change',function() {  
    $.ajaxFileUpload({  
        url:'/coupon/uploadImgd2',  
        secureuri:false,  
        fileElementId:'fileToUploadd2',//file标签的id  
        dataType: 'json',//返回数据的类型  
        success: function (data, status) {  
            if(data.msg=="200"){
                //alert("图片可用");
                $("#serverImgNamed2").val(data.imgName);
                $("#uploadd2").attr("src", "/static/upload/"+data.imgName);
                var imgSrc = $("#uploadd2").attr("src");
                var require = {wid:1040,hei:420};
                checkImageSize(imgSrc,function(w,h){
            		if(w!=require.wid || h!=require.hei){
            			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
            			$("#serverImgNamed2").val("");
            			$("#uploadd2").attr("src", "");
            		}
            	 });
            }
            else if(data.msg=="图片不符合"){
           	 alert("图片不符合");
            }
            if(typeof(data.error) != 'undefined') {  
                if(data.error != '') {  
                    alert(data.error);  
                } else {  
                    alert(data.msg);  
                }  
            }
         }, 
        error: function (data, status, e) {  
            alert(e);  
        }  
    });  
});

$("#cuploadd1").on('click', function() {  
    $('#cfileToUploadd1').click();  
});
//这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
$('#cfileDivd1').on('change',function() {  
    $.ajaxFileUpload({  
        url:'/coupon/cuploadImgd1',  
        secureuri:false,  
        fileElementId:'cfileToUploadd1',//file标签的id  
        dataType: 'json',//返回数据的类型  
        success: function (data, status) {  
            if(data.msg=="200"){
                //alert("图片可用");
                $("#cserverImgNamed1").val(data.imgName);
                $("#cuploadd1").attr("src", "/static/upload/"+data.imgName);
                var imgSrc = $("#cuploadd1").attr("src");
                var require = {wid:182,hei:182};
                checkImageSize(imgSrc,function(w,h){
            		if(w!=require.wid || h!=require.hei){
            			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
            			$("#cserverImgNamed1").val("");
            			$("#cuploadd1").attr("src", "");
            		}
            	 });
            }
            else if(data.msg=="图片不符合"){
           	 alert("图片不符合");
            }
            if(typeof(data.error) != 'undefined') {  
                if(data.error != '') {  
                    alert(data.error);  
                } else {  
                    alert(data.msg);  
                }  
            }
         }, 
        error: function (data, status, e) {  
            alert(e);  
        }  
    });  
});
$("#cuploadd2").on('click', function() {  
    $('#cfileToUploadd2').click();  
});
//这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
$('#cfileDivd2').on('change',function() {  
    $.ajaxFileUpload({  
        url:'/coupon/cuploadImgd2',  
        secureuri:false,  
        fileElementId:'cfileToUploadd2',//file标签的id  
        dataType: 'json',//返回数据的类型  
        success: function (data, status) {  
            if(data.msg=="200"){
                //alert("图片可用");
                $("#cserverImgNamed2").val(data.imgName);
                $("#cuploadd2").attr("src", "/static/upload/"+data.imgName);
                var imgSrc = $("#cuploadd2").attr("src");
                var require = {wid:1040,hei:420};
                checkImageSize(imgSrc,function(w,h){
            		if(w!=require.wid || h!=require.hei){
            			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
            			$("#cserverImgNamed2").val("");
            			$("#cuploadd2").attr("src", "");
            		}
            	 });
            }
            else if(data.msg=="图片不符合"){
           	 alert("图片不符合");
            }
            if(typeof(data.error) != 'undefined') {  
                if(data.error != '') {  
                    alert(data.error);  
                } else {  
                    alert(data.msg);  
                }  
            }
         }, 
        error: function (data, status, e) {  
            alert(e);  
        }  
    });  
});
</script>
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
