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
 <h2><a href="index.php">超级社区</a></h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/squeryall">所有未处理订单</a></dd>
    <dd><a href="/orderOff/squeryall">所有已处理订单</a></dd>
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/squeryall">商品列表</a></dd>
    <dd><a href="/product/scatalogs">商品分类</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/squeryall">会员中心</a></dd>
    <!-- <dd><a href="#">添加会员</a></dd>
    <dd><a href="#">会员等级</a></dd>
    <dd><a href="#">资金管理</a></dd> -->
   </dl>
  </li>
  <li>
   <dl>
    <dt>账号管理</dt>
    <dd><a href="/vendor/squeryall" class="active">账号管理</a></dd>
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

     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
		 //弹出文本性提示框
		 $("#showPopTxt").click(function(){
			 $(".pop_bg").fadeIn();
			 });
		 //弹出：确认按钮
		 $("#saveBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 //弹出：取消或关闭按钮
		 $("#cancelBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加分类</h3>
       <!--content-->
       <div class="pop_cont_input">
        <ul>
         <li>
          <span>分类名称</span>
          <input type="text" placeholder="如'水果'" class="textbox"/>
         </li>
         <li>
          <span class="ttl">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
          <input type="text" placeholder="请填写整数，从大到小排序" class="textbox"/>
         </li>
        </ul>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        这里是文字性提示信息！
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
     //弹出文本性提示框
     $("#delPopTxt").click(function(){
       $(".del_pop_bg").fadeIn();
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
       $(".del_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       });
     });
     </script>
     <section class="del_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          确认删除？删除后该分类下的所有记录将取消与该分类的关联
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="继续删除" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
     </section>
     <!-- 删除分类弹出框 -->
	<!-- 添加账号 -->
    <script>
     $(document).ready(function(){
     //添加账号
     $("#addVendor").click(function(){
    	 $("#vendorcity").empty();
    	 $("#vendorcity").get(0).options.add(new Option("选择城市","选择城市"));
    	 $("#vendorarea").empty();
    	 $("#vendorarea").get(0).options.add(new Option("选择区域","选择区域"));
    	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/city/queryall",
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
    	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/area/queryall",
	          dataType: "json",
	          success: function(data){
	        	  //var areas = JSON.stringify(data.areas);
	        	  if(data.msg=="200"){
	        		  $.each(data.areas,function(i, val){
	        			  $("#vendorarea").get(0).options.add(new Option(val.areaName,val.id));
	        		  });
	        	  }
	          }
    	 });
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
	          url: "/vendor/new",
	          data: JSON.stringify({"userName":vendorname,"cityId":cityId,"areaId":areaId,"cityArea":cityname+areaname}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加区域管理员账号成功");
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
       <div class="pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name">账号名称:</span>
		        <input type="text" id="vendorName" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name">账号密码:</span>
		        <input type="text" id="password" placeholder=""/>
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
     </section>
     <!-- 添加账号结束 -->
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
	          url: "/city/new",
	          data: JSON.stringify({"cityName":cityname,"avatarUrl":imgname}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加城市成功");
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
                     alert("图片可用");
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
     </section>
     <!-- 添加城市结束 -->
     <!-- 添加区域 -->
    <script>
     $(document).ready(function(){
     //添加账号
     $("#addArea").click(function(){
    	 $("#cityselect").empty();
    	 $("#cityselect").get(0).options.add(new Option("选择城市","选择城市"));
    	 $.ajax({
    		 type: "GET",
	          contentType: "application/json",
	          url: "/city/queryall",
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
	          url: "/area/new",
	          data: JSON.stringify({"areaName":areaname,"cityName":cityname,"cityId":cityId}),
	          dataType: "json",
	          success: function(data){
	                  if(data.msg=="200"){
	                	  alert("增加区域成功");
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
     </section>
     <!-- 添加区域结束 -->
     
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加账号" id="addVendor" class="input_btn trueBtn"/>
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
       <%-- <tr>
        <td>${vendors }</td>
        <td>********</td>
        <td>
          <select class="select" disabled="disabled">
           <option>常州</option>
          </select>
           <select class="select" disabled="disabled">
           <option>某某区</option>
          </select>
        </td>
        <td style="text-align:center">
           <button class="linkStyle" id="showPopTxt">编辑</button>|
           <button class="linkStyle" id="delPopTxt">删除</button>
        </td>
       </tr> --%>
       	<c:forEach var="item" items="${vendors}" varStatus="status">
         	<tr>
         		<td>${item.userName}</td>
         		<td>${item.password}</td>
         		<td>${item.cityArea}</td>
         		<td style="text-align:center">
		           <button class="linkStyle" id="showPopTxt">编辑</button>|
		           <button class="linkStyle" id="delPopTxt">删除</button>
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

     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
</body>
</html>
