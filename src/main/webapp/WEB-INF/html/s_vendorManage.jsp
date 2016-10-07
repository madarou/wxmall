<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style type="text/css">
    .btm_btn{text-align: center;}
    .btm_btn .input_btn{display:inline-block;height:35px;border:none;background:none;padding:0 20px;}
    .btm_btn .trueBtn{background:#19a97b;color:white;border:1px #19a97b solid;border-radius:2px;}
    td{text-align: center;}
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
    <dd><a href="/vendor/s_queryall/${id}?token=${token}" class="active">账号管理</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>礼券管理</dt>
    <dd><a href="/coupon/s_queryall/${id}?token=${token}">优惠券管理</a></dd>
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
	
	<!-- 瞬间消失的提示框 -->
     <section class="loading_area">
      <div class="loading_cont">
       <div class="loading_icon"><i></i><i></i><i></i><i></i><i></i></div>
       <div class="loading_txt"><mark id="tips">操作成功</mark></div>
      </div>
     </section>
     <!-- 瞬间消失的提示框 -->
     

     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	 var vendorId_toEdit = 0;
    	 var oldVendorName = "";
    	 var oldVendorPwd = "";

		 //弹出文本性提示框
		 $(".editvendor").click(function(){
			 //获取被点击的行的id，即vendorId
			 var clickedId = $(this).attr("id");
       		 vendorId_toEdit = clickedId.split("-")[1];
       		 //填充用户名和密码
       		 var firstTD = $(this).parent().parent().find("td").first();//根据编辑按钮定位到用户名td
       		 $("#editvendorName").val(firstTD.html());
       		 oldVendorName = firstTD.html();
       		 var secondTD = $(this).parent().parent().find("td").eq(1);//根据编辑按钮定位到用户名td
      		 $("#editpassword").val(secondTD.html());
      		 oldVendorPwd = secondTD.html();
			 
			 $(".pop_bg").fadeIn();
			 });
		 //弹出：确认按钮
		 $("#saveBtn").click(function(){
			 var newVendorName = $.trim($("#editvendorName").val());
			 var newVendorPwd = $.trim($("#editpassword").val());
			 if(oldVendorName==newVendorName && newVendorPwd==oldVendorPwd){//用户并未修改则不提交
				 alert("并未做修改");
				 return false;
			 }
			 
			 if(newVendorPwd!=oldVendorPwd){//如果修改了密码，则全部提交
				 $.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/vendor/update"+"/?token="+$("#loginToken").val(),
		  	          dataType: "json",
		  	          data: JSON.stringify({"id":vendorId_toEdit,"userName":newVendorName,"password":newVendorPwd}),
		  	          success: function(data){
		  	        	  //var cities = JSON.stringify(data.cities);
		  	        	  if(data.msg=="200"){
		  	        		  alert("修改管理员账号成功");
		  	        		  window.location.reload();
		  	        	  }
		  	          }
		    	 	});
			 }
			 else if(oldVendorName!=newVendorName){//如果密码没修改，则不要提交密码，否则又会被加密
				 $.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/vendor/update"+"/?token="+$("#loginToken").val(),
		  	          dataType: "json",
		  	          data: JSON.stringify({"id":vendorId_toEdit,"userName":newVendorName,"password":""}),
		  	          success: function(data){
		  	        	  //var cities = JSON.stringify(data.cities);
		  	        	  if(data.msg=="200"){
		  	        		  alert("修改区域管理员账号成功");
		  	        		  window.location.reload();
		  	        	  }
		  	          }
		    	 	});
			 }
			 $(".pop_bg").fadeOut();
			 oldVendorName = "";
	    	 oldVendorPwd = "";
	    	 vendorId_toEdit = 0;
			 });
		 //弹出：取消或关闭按钮
		 $("#cancelBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 oldVendorName = "";
	    	 oldVendorPwd = "";
	    	 vendorId_toEdit = 0;
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>编辑账户</h3>
       <!--content-->
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">账号名称:</span>
		        <input type="text" id="editvendorName" class="length_input_20  textbox_225" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">账号密码:</span>
		        <input type="text" id="editpassword" class="length_input_20  textbox_225" placeholder=""/>
		       </li>
		       <!-- <li>
		        <span class="item_name">负责区域:</span>
		       		<select class="select" id="editvendorcity">  
				    </select>
				    <select class="select" id="editvendorarea">  
				    </select>
		       </li> -->
		      </ul>
		    </section>
         </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="保存" id="saveBtn" class="input_btn trueBtn"/>
        <input type="button" value="取消" id="cancelBtn" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 删除分类弹出框 -->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	var vendorId_toDel = 0;//要删除的用户
     //弹出文本性提示框
     $(".delvendor").click(function(){
       $(".del_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       vendorId_toDel = clickedId.split("-")[1];

       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
    	 if(vendorId_toDel==0){
    		 alert("请重新选择要删除的账户");
    		 return false;
    	 }
        	$.ajax({
    		  type: "DELETE",
  	          contentType: "application/json",
  	          url: "/vendor/"+vendorId_toDel+"/?token="+$("#loginToken").val(),
  	          dataType: "json",
  	          success: function(data){
  	        	  //var cities = JSON.stringify(data.cities);
  	        	  if(data.msg=="200"){
  	        		  alert("删除区域管理员账号成功");
  	        		  window.location.reload();
  	        		  vendorId_toDel=0;
  	        	  }
  	          }
    	 	});
       $(".del_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       vendorId_toDel=0;
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
          确认删除该区域管理员账户?
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
	<!-- 添加账号 -->
    <script>
     $(document).ready(function(){
    	  $('#vendorcity').change(function(){
    	    	 //alert($(this).children('option:selected').val());
    	    	 var cityId = $(this).children('option:selected').val();
    	    	 //if(cityId=="选择城市"){
    	    		 $("#vendorarea").empty();
    	        	 $("#vendorarea").get(0).options.add(new Option("选择区域","选择区域"));
    	    	//	 return;
    	    	 //}
    	    	 $.ajax({
    	    		 type: "POST",
    		          contentType: "application/json",
    		          url: "/area/querybycity/"+$("#loginUserId").val(),
    		          dataType: "json",
    		          data:JSON.stringify({"cityId":cityId}),
    		          success: function(data){
    		        	  //var areas = JSON.stringify(data.areas);
    		        	  if(data.msg=="200"){
    		        		  $.each(data.areas,function(i, val){
    		        			  $("#vendorarea").get(0).options.add(new Option(val.areaName,val.id));
    		        		  });
    		        	  }
    		          }
    	    	 });
    	     });
     //添加账号
     $("#addVendor").click(function(){
    	 $("#vendorcity").empty();
    	 $("#vendorcity").get(0).options.add(new Option("选择城市","选择城市"));
    	 $("#vendorarea").empty();
    	 $("#vendorarea").get(0).options.add(new Option("选择区域","选择区域"));
    	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/city/queryall/"+$("#loginUserId").val(),
	          dataType: "json",
	          success: function(data){
	        	  //var cities = JSON.stringify(data.cities);
	        	  if(data.msg=="200"){
	        		  $.each(data.cities,function(i, val){
	        			  $("#vendorcity").get(0).options.add(new Option(val.cityName,val.id));
	        		  });
	        	  }
	          }
    	 });
/*     	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/area/queryall/"+$("#loginUserId").val(),
	          dataType: "json",
	          success: function(data){
	        	  //var areas = JSON.stringify(data.areas);
	        	  if(data.msg=="200"){
	        		  $.each(data.areas,function(i, val){
	        			  $("#vendorarea").get(0).options.add(new Option(val.areaName,val.id));
	        		  });
	        	  }
	          }
    	 }); */
       $(".addvendor_pop_bg").fadeIn();
       });
    
     $("#vendorAdd").click(function(){
    	 var vendorname = $.trim($("#vendorName").val());
    	 if(vendorname==""){
    		 alert("账户名不能为空");
    		 return false;
    	 }
    	 var password = $.trim($("#password").val());
    	 if(vendorname==""){
    		 alert("密码不能为空");
    		 return false;
    	 }
    	 var cityId = $("#vendorcity").val();//这种方式获取的是value
    	 if(cityId=="选择城市"){
    		 alert("请选择所属城市");
    		 return false;
    	 }
    	 var cityname = $("#vendorcity").find("option:selected").text();//这种方式获取的是选项的text文本
    	 var areaId = $("#vendorarea").val();//这种方式获取的是value
    	 if(areaId=="选择区域"){
    		 alert("请选择所属区域");
    		 return false;
    	 }
    	 var areaname = $("#vendorarea").find("option:selected").text();
    	 $.ajax({
	          type: "POST",
	          contentType: "application/json",
	          url: "/vendor/new/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
	          data: JSON.stringify({"userName":vendorname,"password":password,"cityId":cityId,"areaId":areaId,"cityName":cityname,"areaName":areaname}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加区域管理员账号成功");
	                	  window.location.reload();
	                  }
	                  else{
	                	  alert("增加区域管理员账号失败");
	                  }
	          }
	      });
       $(".addvendor_pop_bg").fadeOut();
       $("#vendorName").val("");
       $("#password").val("");
       });
   
     $("#vendorCancel").click(function(){
       $(".addvendor_pop_bg").fadeOut();
       });
     });
     </script>
     <section class="addvendor_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加账号</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">账号名称:</span>
		        <input type="text" id="vendorName" class="length_input_20 textbox_225" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">账号密码:</span>
		        <input type="text" id="password" class="length_input_20 textbox_225" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">负责区域:</span>
		       		<select class="select" id="vendorcity">  
				    </select>
				    <select class="select" id="vendorarea">  
				    </select>
		       </li>
		      </ul>
		    </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="vendorAdd" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="vendorCancel" class="input_btn falseBtn"/>
         </div>
        </div>
        </div>
     </section>
     <!-- 添加账号结束 -->
     
     
      <!--解除微信绑定弹出框效果-->
     <script>
     $(document).ready(function(){
    	var vendorId_toUnbind = 0;//要解绑的用户
     //弹出文本性提示框
     $(".unBind").click(function(){
       $(".unbind_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       vendorId_toUnbind = clickedId.split("-")[1];

       });
     //弹出：确认按钮
     $("#confirmUnbind").click(function(){
    	 if(vendorId_toUnbind==0){
    		 alert("请重新选择要解除的账户");
    		 return false;
    	 }
        	$.ajax({
    		  type: "POST",
  	          contentType: "application/json",
  	          url: "/vendor/unbind/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
  	          data: JSON.stringify({"vendorId":vendorId_toUnbind}),
	          dataType: "json",
  	          success: function(data){
  	        	  if(data.msg=="200"){
  	        		  alert("解绑成功");
  	        		  window.location.reload();
  	        		vendorId_toUnbind=0;
  	        	  }
  	          }
    	 	});
       $(".unbind_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelUnbind").click(function(){
       $(".unbind_pop_bg").fadeOut();
       vendorId_toUnbind=0;
       });
     });
     </script>
     <section class="unbind_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          确认解绑该区域管理员账户与其微信吗?
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="继续" id="confirmUnbind" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelUnbind" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
     <!-- 删除分类弹出框 -->
     
     
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加账号" id="addVendor" class="input_btn trueBtn"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>账户名</th>
        <th>密码</th>
        <th>区域</th>
        <th>操作</th>
        <th>微信</th>
       </tr>
       	<c:forEach var="item" items="${vendors}" varStatus="status">
         	<tr>
         		<td>${item.userName}</td>
         		<td>********</td>
         		<td>${item.cityName}-${item.areaName}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editvendor" id="showPopTxt-${item.id}">编辑</button>|
		           <button class="linkStyle delvendor" id="delPopTxt-${item.id}">删除</button>
		        </td>
		        <td style="text-align:center">
		           <c:choose> 
		  				<c:when test="${empty item.openid}">   
		  					<button class="linkStyle" style="color:grey;cursor:default">未绑定</button>
						</c:when> 
						<c:otherwise>   
		  					<button class="linkStyle unBind" id="unbind-${item.id}">解除绑定</button>
						</c:otherwise> 
					</c:choose>
		        </td>
         	</tr>
		</c:forEach> 
      </table>
     </section>
    
     <section style="text-align:center">
        <img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}"/><br/>
         <span class="item_name" style="width:120px;">扫描二维码实现绑定微信号，接收订单退货完成消息!</span>
     </section><br/>
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
