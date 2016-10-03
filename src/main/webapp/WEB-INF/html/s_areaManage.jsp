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
<!--header-->
<header>
 <h1><img src="static/images/admin_logo.png"/></h1>
 <ul class="rt_nav">
  <li><a href="/orderOn/s_queryall/${id}/1?token=${token}" class="website_icon">订单管理</a></li>
  <li><a href="/user/s_queryall/${id}/1?token=${token}" class="admin_icon">会员管理</a></li>
  <li><a href="/product/s_products/${id}/1?token=${token}" class="product_icon">商品管理</a></li>
  <li><a href="/vendor/s_queryall/${id}?token=${token}" class="set_icon">账号设置</a></li>
  <li><a href="/supervisor/logout?token=${token}" class="quit_icon">安全退出</a></li>
 </ul>
</header>

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
    <dd><a href="/area/s_queryall/${id}?token=${token}" class="active">区域设置</a></dd>
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
       <!--  <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
        <hr/>
     </section>
	
     <!-- 添加城市 -->
    <script>
     $(document).ready(function(){
   	//var counter = 0;
     //添加城市
     $("#addCity").click(function(){
       $(".addcity_pop_bg").fadeIn();
       });

     $("#cityAdd").click(function(){
    	 var cityname = $.trim($("#cityName").val());
    	 if(cityname==""){
    		 alert("城市名不能为空");
    		 //$("#nocityname").text("城市名不能为空")
    		 return false;
    	 }
    	 var imgname = $.trim($("#serverImgName").val());
    	 if(imgname==""){
    		 alert("请上传图片");
    		 //$("#nopicname").text("请上传图片")
    		 return false;
    	 }
    	 $.ajax({
	          type: "POST",
	          contentType: "application/json",
	          url: "/city/new/"+$("#loginUserId").val(),
	          data: JSON.stringify({"cityName":cityname,"avatarUrl":imgname}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加城市成功");
	                	  window.location.reload();
	                  }
	                  else{
	                	  alert("增加城市失败");
	                  }
	          }
	      });
	       $(".addcity_pop_bg").fadeOut();
	     	//清空内容
	       $("#upload").attr("src","");
	       $("#serverImgName").val("");
	       $("#cityName").val("");
       });
     
     $("#cityCancel").click(function(){
       $(".addcity_pop_bg").fadeOut();
       });
     
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
     $("#upload").on('click', function() {  
         $('#fileToUpload').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDiv').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/city/uploadImg',  
             secureuri:false,  
             fileElementId:'fileToUpload',//file标签的id  
             dataType: 'json',//返回数据的类型  
             data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 //把图片替换  
                 /* var obj = jQuery.parseJSON(data);  
                 $("#upload").attr("src", "../image/"+obj.fileName);   */
                 if(data.msg=="上传成功"){
                     //alert("图片可用");
                     $("#serverImgName").val(data.imgName);
                     $("#upload").attr("src", "/static/upload/"+data.imgName);
                     var imgSrc = $("#upload").attr("src");
                     var require = {wid:79,hei:79};
                     checkImageSize(imgSrc,function(w,h){
                 		if(w!=require.wid || h!=require.hei){
                 			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
                 			$("#serverImgName").val("");
                 			$("#upload").attr("src", "");
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
     });
     </script>
     <section class="addcity_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加城市</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">城市名称:</span>
		        <input type="text" id="cityName" class="textbox_225 length_input_20" placeholder="布宜诺斯艾利斯"/>
		        <!-- <span class="errorTips" id="nocityname"></span> -->
		       </li>
		       <li>
		        <span class="item_name">城市logo:</span>
		        <!-- <label class="uploadImg" id="upload">
		         <span>上传图片</span>
		        </label> -->
		        <img alt="图片(79 x 79)" id="upload" src="" style="height:100px;width:100px;cursor:pointer">
		        <div id="fileDiv">
		        	<input id="fileToUpload" style="display: none" type="file" name="upfile">
		        </div>
		        <input type="hidden" id="serverImgName"/>
		       <!--  <span class="errorTips" id="nopicname"></span> -->
		       </li>
		       <!-- <li>
		        <span class="item_name" style="width:120px;"></span>
		        <input type="submit" class="link_btn"/>
		       </li> -->
		      </ul>
		    </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="cityAdd" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cityCancel" class="input_btn falseBtn"/>
         </div>
        </div>
        </div>
     </section>
     <!-- 添加城市结束 -->
     <!-- 添加区域 -->
    <script>
     $(document).ready(function(){
     //添加区域
     $("#addArea").click(function(){
    	 $("#cityselect").empty();
    	 $("#cityselect").get(0).options.add(new Option("选择城市","选择城市"));
    	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/city/queryall/"+$("#loginUserId").val(),
	          dataType: "json",
	          success: function(data){
	        	  var cities = JSON.stringify(data.cities);
	        	  if(data.msg=="200"){
	        		  $.each(data.cities,function(i, val){
	        			  $("#cityselect").get(0).options.add(new Option(val.cityName,val.id));
	        		  });
	        	  }
	          }
    	 });
       $(".addarea_pop_bg").fadeIn();
       });
     $("#areaAdd").click(function(){
    	 var areaname = $.trim($("#areaName").val());
    	 if(areaname==""){
    		 alert("区域名不能为空");
    		 return false;
    	 }
    	 var cityId = $("#cityselect").val();//这种方式获取的是value
    	 if(cityId=="选择城市"){
    		 alert("请选择所属城市");
    		 return false;
    	 }
    	 var phoneNumber = $.trim($("#areaphoneNumber").val());
    	 if(phoneNumber==""){
    		 alert("请填写服务电话");
    		 return false;
    	 }
    	 var cityname = $("#cityselect").find("option:selected").text();//这种方式获取的是选项的text文本
    	 var longitude = $.trim($("#arealongitude").val());
    	 var latitude = $.trim($("#arealatitude").val());
    	 
    	 $.ajax({
	          type: "POST",
	          contentType: "application/json",
	          url: "/area/new/"+$("#loginUserId").val(),
	          data: JSON.stringify({"areaName":areaname,"cityName":cityname,"cityId":cityId,"longitude":longitude,"latitude":latitude,"phoneNumber":phoneNumber}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加区域成功");
	                	  window.location.reload();
	                  }
	                  else{
	                	  alert("增加区域失败");
	                  }
	          }
	      });
       $(".addarea_pop_bg").fadeOut();
       $("#areaName").val("");
       });

     $("#areaCancel").click(function(){
       $(".addarea_pop_bg").fadeOut();
       });
     });
     </script>
     <section class="addarea_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加区域</h3>
       <!--content-->
       <div class="small-pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">区域名称:</span>
		        <input type="text" id="areaName" class="length_input_20 textbox_225" placeholder=""/>
		        <!-- <span class="errorTips" id="nocityname"></span> -->
		       </li>
		       <li>
		        <span class="item_name">所属城市:</span>
		       		<select class="select" id="cityselect">
				       
				    </select>
		       </li>
		       <li>
		        <span class="item_name">区域经度:</span>
		        <input type="text" id="arealongitude" class="titude_input textbox_225" placeholder=""/>
		       </li>
		        <li>
		        <span class="item_name">区域纬度:</span>
		        <input type="text" id="arealatitude" class="titude_input textbox_225" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">服务电话:</span>
		        <input type="text" id="areaphoneNumber" class="phone_input textbox_225" placeholder="如'12345678900'"/>
		       </li>
		      </ul>
		    </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="areaAdd" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="areaCancel" class="input_btn falseBtn"/>
         </div>
        </div>
        </div>
     </section>
     <!-- 添加区域结束 -->
     
      <!-- 上线下线提示框 -->
      <script>
     $(document).ready(function(){
    	var openHandle_Id = 0;//要上下线的areaId
    	var openAction = "";
     //弹出文本性提示框
     $(".openOrNot").click(function(){
       $(".del_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       openAction = clickedId.split("-")[0];//值为notShow或show
       openHandle_Id = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
    	 if(openHandle_Id==0){
    		 alert("请重新选择要操作的区域");
    		 return false;
    	 }
    	 if(openAction=="close"){//下线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/area/sclose/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"areaId":openHandle_Id}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("下线成功");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	}); 
    	 }
    	 else if(openAction=="open"){//上线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/area/sopen/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"areaId":openHandle_Id}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("上线成功");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	}); 
    	 }
        	
       $(".del_pop_bg").fadeOut();
       openHandle_Id=0;
       openAction = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       openHandle_Id=0;
       openAction = "";
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
          确认要将本区域相关信息暂时下线（上线）么？<br/>
		  注意:其中所有相关信息将暂时不展示在客户端
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
      <!-- 上线下线提示框 -->
      
       <!-- 编辑区域 -->
    <script>
     $(document).ready(function(){
    	 var edit_areaId = 0;
    	 var oldAreaName = "";
    	 var oldLongitude = "";
    	 var oldLatitude = "";
    	 var oldPhoneNumber = "";
     $(".editArea").click(function(){
    	 var clickedId = $(this).attr("id");
         edit_areaId = clickedId.split("-")[1];
    	 
    	 $("#ecityselect").empty();
    	 var city_name = $("#cityName-"+edit_areaId).text();
    	 var city_id = $("#cityid-"+edit_areaId).text();
    	 $("#ecityselect").get(0).options.add(new Option(city_name,city_id));
		oldAreaName = $("#areaName-"+edit_areaId).text();
		oldLongitude = $("#longitude-"+edit_areaId).text();
		oldLatitude = $("#latitude-"+edit_areaId).text();
		oldPhoneNumber = $("#phoneNumber-"+edit_areaId).text();
		$("#eareaName").val(oldAreaName);
		$("#earealongitude").val(oldLongitude);
		$("#earealatitude").val(oldLatitude);
		$("#eareaphoneNumber").val(oldPhoneNumber);
    	 
       	$(".editarea_pop_bg").fadeIn();
       });
     $("#eareaAdd").click(function(){
    	 var areaname = $.trim($("#eareaName").val());
    	 if(areaname==""){
    		 alert("区域名不能为空");
    		 return false;
    	 }

    	 var phoneNumber = $.trim($("#eareaphoneNumber").val());
    	 if(phoneNumber==""){
    		 alert("请填写服务电话");
    		 return false;
    	 }
    	 var longitude = $.trim($("#earealongitude").val());
    	 var latitude = $.trim($("#earealatitude").val());
    	 if(oldAreaName==areaname && oldLongitude==longitude && oldLatitude==latitude && oldPhoneNumber==phoneNumber){
    		 alert("未作修改");
    		 return false;
    	 }
    	 
    	 $.ajax({
	          type: "POST",
	          contentType: "application/json",
	          url: "/area/edit/"+$("#loginUserId").val(),
	          data: JSON.stringify({"areaId":edit_areaId,"areaName":areaname,"longitude":longitude,"latitude":latitude,"phoneNumber":phoneNumber}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("修改区域成功");
	                	  window.location.reload();
	                  }
	                  else{
	                	  alert("修改区域失败");
	                  }
	          }
	      });
       $(".editarea_pop_bg").fadeOut();
       $("#eareaName").val("");
       $("#eareaphoneNumber").val("");
       $("#earealongitude").val("");
       $("#earealatitude").val("");
 		oldAreaName = "";
  	 	oldLongitude = "";
  	 	oldLatitude = "";
  	 	oldPhoneNumber = "";
       });

     $("#eareaCancel").click(function(){
       $(".editarea_pop_bg").fadeOut();
       $("#eareaName").val("");
       $("#eareaphoneNumber").val("");
       $("#earealongitude").val("");
       $("#earealatitude").val("");
       oldAreaName = "";
 	 	oldLongitude = "";
 	 	oldLatitude = "";
 	 	oldPhoneNumber = "";
       });
     });
     </script>
     <section class="editarea_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>修改区域</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">区域名称:</span>
		        <input type="text" id="eareaName" class="length_input_20 textbox_225" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">所属城市:</span>
		       		<select class="select" id="ecityselect" disabled="disabled">
				       
				    </select>
		       </li>
		       <li>
		        <span class="item_name">区域经度:</span>
		        <input type="text" id="earealongitude" class="titude_input textbox_225"  placeholder=""/>
		       </li>
		        <li>
		        <span class="item_name">区域纬度:</span>
		        <input type="text" id="earealatitude" class="titude_input textbox_225"  placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">服务电话:</span>
		        <input type="text" id="eareaphoneNumber" class="phone_input textbox_225" placeholder="如'12345678900'"/>
		       </li>
		      </ul>
		    </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="eareaAdd" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="eareaCancel" class="input_btn falseBtn"/>
         </div>
        </div>
        </div>
     </section>
     <!-- 编辑区域结束 -->
     
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加城市" id="addCity" class="input_btn trueBtn"/>
        <input type="button" value="添加区域" id="addArea" class="input_btn trueBtn"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>城市</th>
        <th>区域</th>
        <th>发布状态</th>
        <th>操作</th>
       </tr>
       	<c:forEach var="item" items="${areas}" varStatus="status">
         	<tr>
         		<td id="cityName-${item.id}">${item.cityName}</td>
         		<td id="areaName-${item.id}">${item.areaName}</td>
         		<td style="text-align:center">
		           <c:choose> 
		  				<c:when test="${item.closed=='yes'}">   
		  					<button class="linkStyle" style="color:grey;cursor:default">下线中</button>|
		  					<button class="linkStyle openOrNot" id="open-${item.id}">上线</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle" style="color:grey;cursor:default">上线中</button>|
		  					<button class="linkStyle openOrNot" id="close-${item.id}">下线</button>
						</c:otherwise> 
					</c:choose>
		        </td>
		        <td style="text-align:center">
		        	<button class="linkStyle editArea" id="editPop-${item.id}">编辑</button>
		        </td>
		        <td id="longitude-${item.id}" style="display:none">${item.longitude}</td>
		        <td id="latitude-${item.id}" style="display:none">${item.latitude}</td>
		        <td id="phoneNumber-${item.id}" style="display:none">${item.phoneNumber}</td>
		        <td id="cityid-${item.id}" style="display:none">${item.cityId}</td>
         	</tr>
		</c:forEach> 
      </table>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
