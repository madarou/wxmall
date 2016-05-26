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
  <li><a href="#" class="website_icon">订单管理</a></li>
  <li><a href="#" class="admin_icon">会员管理</a></li>
  <li><a href="#" class="admin_icon">商品管理</a></li>
  <li><a href="#" class="set_icon">账号设置</a></li>
  <li><a href="login.php" class="quit_icon">安全退出</a></li>
 </ul>
</header>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <h2><a href="#">超级社区</a></h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/s_queryall/${id}?token=${token}">所有未处理订单</a></dd>
    <dd><a href="/orderOff/s_queryall/${id}?token=${token}">所有已处理订单</a></dd>
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/s_products/${id}?token=${token}">商品库</a></dd>
    <dd><a href="/product/s_catalogs/${id}?token=${token}">商品分类</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/s_queryall/${id}?token=${token}">会员中心</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>账号管理</dt>
    <dd><a href="/vendor/s_queryall/${id}?token=${token}">账号管理</a></dd>
   </dl>
  </li>
  <li>
   <p class="btm_infor">© 优格信息 版权所有</p>
  </li>
 </ul>
</aside>
<section class="rt_wrap content mCustomScrollbar">
 <div class="rt_content">
     <section>
        <h3 style="text-align:right;">欢迎您，某某管理员</h3>
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
       <div class="pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">城市名称:</span>
		        <input type="text" id="cityName" placeholder=""/>
		        <!-- <span class="errorTips" id="nocityname"></span> -->
		       </li>
		       <li>
		        <span class="item_name">城市logo:</span>
		        <!-- <label class="uploadImg" id="upload">
		         <span>上传图片</span>
		        </label> -->
		        <img alt="上传图片" id="upload" src="" style="height:100px;width:100px;cursor:pointer">
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
    	 var cityname = $("#cityselect").find("option:selected").text();//这种方式获取的是选项的text文本
    	 $.ajax({
	          type: "POST",
	          contentType: "application/json",
	          url: "/area/new/"+$("#loginUserId").val(),
	          data: JSON.stringify({"areaName":areaname,"cityName":cityname,"cityId":cityId}),
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
       <div class="pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">区域名称:</span>
		        <input type="text" id="areaName" placeholder=""/>
		        <!-- <span class="errorTips" id="nocityname"></span> -->
		       </li>
		       <li>
		        <span class="item_name">所属城市:</span>
		       		<select class="select" id="cityselect">
				       
				    </select>
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
     
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加城市" id="addCity" class="input_btn trueBtn"/>
        <input type="button" value="添加区域" id="addArea" class="input_btn trueBtn"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>账户名</th>
        <th>密码</th>
        <th>区域</th>
        <th>操作</th>
       </tr>
       	<c:forEach var="item" items="${vendors}" varStatus="status">
         	<tr>
         		<td>${item.userName}</td>
         		<td>${item.password}</td>
         		<td>${item.cityName}-${item.areaName}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editvendor" id="showPopTxt${item.id}">编辑</button>|
		           <button class="linkStyle delvendor" id="delPopTxt${item.id}">删除</button>
		        </td>
         	</tr>
		</c:forEach> 
      </table>
      <aside class="paging">
       <a>第一页</a>
       <a>1</a>
       <a>2</a>
       <a>3</a>
       <a>…</a>
       <a>1004</a>
       <a>最后一页</a>
      </aside>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
