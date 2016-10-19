<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>区域后台管理系统</title>
<link rel="shortcut icon" href="/static/images/icon/favicon.ico" type="image/x-icon" />
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style>
  td{text-align: center;}
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
<iframe name="mframe" src="/orderOn/hasNew/${id}/?token=${token}" frameborder="0" scrolling="no" width="100%" height="70px" onload="document.all['mframe'].style.height=mframe.document.body.scrollHeight"></iframe>
 
<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
<h2 style="height: 35px;"><iframe name="myframe" src="/vendor/index/title/${id}/?token=${token}" frameborder="0" scrolling="no" width="100%" height="100%" onload="document.all['myframe'].style.height=myframe.document.body.scrollHeight"></iframe></h2>
<ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/v_query_queue/${id}/1?token=${token}">排队中订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}/1?token=${token}">待处理订单</a></dd>
    <dd><a href="/orderOn/v_query_distributed/${id}/1?token=${token}">已配送订单</a></dd>
    <dd><a href="/orderOff/v_query_confirm/${id}/1?token=${token}">已收货订单</a></dd>
    <dd><a href="/orderOff/v_query_refund/${id}/1?token=${token}">待退货订单</a></dd>
    <dd><a href="/orderOff/v_query_teminaled/${id}/1?token=${token}">已完成订单</a></dd>
    <dd><a href="/orderOff/v_query_cancel/${id}/1?token=${token}">已取消/已退货</a></dd>
    <!-- <dd><a href="#">未支付订单</a></dd> -->
    <!-- <dd><a href="#">绑定微信号</a></dd> -->
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/v_new/${id}?token=${token}">商品添加</a></dd>
    <dd><a href="/product/v_manage/${id}/1?token=${token}">商品管理</a></dd>
    <dd><a href="/product/v_catalog/${id}?token=${token}">分类管理</a></dd>
    <dd><a href="/product/v_promotion/${id}?token=${token}" class="active">综合配置</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/v_usermanage/${id}/1?token=${token}">用户管理</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>礼券管理</dt>
    <dd><a href="/gift/v_giftmanage/${id}?token=${token}">礼品配置</a></dd>
    <dd><a href="/vendor/v_bindwx/${id}?token=${token}">绑定微信号</a></dd>
    <!-- <dd><a href="#">优惠券配置</a></dd> -->
   </dl>
  </li>
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
    	 var bHandle_Id = 0;//要上下架的商品id
    	 var productUrlO = "";
    	 var imgUrlO = "";
     //弹出文本性提示框
     $(".popEdit").click(function(){
       $(".pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       bHandle_Id = clickedId.split("-")[1];
       productUrlO=$("#producturl-"+bHandle_Id).text();
       imgUrlO=$("#imgurl-"+bHandle_Id).text();
       $("#proUrl").val(productUrlO);
       $("#upload").attr("src","/static/upload/"+imgUrlO);
       });
     //弹出：确认按钮
     $("#saveBtn").click(function(){
    	 if(bHandle_Id==0){
    		 alert("请重新选择分类");
    		 return false;
    	 }
    	 var productUrlN = $.trim($("#proUrl").val());
    	 var imgUrlN = $("#serverImgName").val();
    	 if(productUrlN == productUrlO && imgUrlN==imgUrlO){
    		 alert("并未修改");
    		 return false;
    	 }
    	 if(imgUrlN==""){
    		 alert("banner图片不能为空");
    		 return false;
    	 }
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/banner/vedit/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"bannerId":bHandle_Id,"productUrl":productUrlN,"imgUrl":imgUrlN}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("编辑成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
    	        		  alert("编辑失败");
    	        		  window.location.reload();
    	        	  }else if(data.msg=="401"){
    	        		     alert("需要重新登录");
    	        	  }
     	          }
       	 	}); 
       $(".pop_bg").fadeOut();
       bHandle_Id=0;
       productUrlO = "";
  	   imgUrlO = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelBtn").click(function(){
       $(".pop_bg").fadeOut();
       bHandle_Id=0;
       productUrlO = "";
  	   imgUrlO = "";
       });
     });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>设置置顶链接</h3>
       <!--content-->
        <section style="margin:10px">
	        <ul class="ulColumn2">
	         <li>
	          <span class="item_name">主&nbsp;推&nbsp;活&nbsp;动&nbsp;：</span>
	          <input type="text" id="proUrl" class="textbox textbox_295" placeholder="商品详情连接"/>
	         </li>
	         <li>
	          		<span class="item_name">banner图片：</span>
						<img alt="请上传比例480*240，大小小于50M的图片" id="upload" src=""
								style="height: 100px; width: 305px; cursor: pointer">
						<div id="fileDiv">
						<input id="fileToUpload" style="display: none" type="file"
										name="upfile">
					</div> <input type="hidden" id="serverImgName" />
	         </li>
	         <li>
	          <div class="btm_btn">
		          <input type="button" value="确定" id="saveBtn" class="input_btn trueBtn"/>
		          <input type="button" value="返回" id="cancelBtn" class="input_btn falseBtn"/>
		      </div>
	         </li>
	        </ul>
	     </section>
      </div>
     </section>
     <!--结束：弹出框效果-->
        <!-- 添加banner图 -->
    <script>
     $(document).ready(function(){ 
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
             url:'/banner/uploadImg',  
             secureuri:false,  
             fileElementId:'fileToUpload',//file标签的id  
             dataType: 'json',//返回数据的类型  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgName").val(data.imgName);
                     $("#upload").attr("src", "/static/upload/"+data.imgName);
                     var imgSrc = $("#upload").attr("src");
                     var require = {wid:480,hei:240};
                     checkImageSize(imgSrc,function(w,h){
                 		if(w!=require.wid || h!=require.hei){
                 			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
                 			$("#serverImgName").val("");
                 			$("#upload").attr("src", "");
                 		}
                 	 });
                 }
                 else if(data.msg=="201"){
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
   <!-- 添加banner图 -->
     
     <section>
      <table class="table">
       <tr>
        <th>分类名称</th>
        <th>操作</th>
        <th>操作&nbsp;|&nbsp;状态</th>
       </tr>
       	<c:forEach var="item" items="${banners}" varStatus="status">
         	<tr>
         		<td id="name-${item.id}">${item.catalogName}</td>
         		<td style="text-align:center">
		           <button class="linkStyle popEdit" id="showPopTxt-${item.id}">编辑</button>
		        </td>
         		<td id="status-${item.id}">
         			<c:choose> 
		  				<c:when test="${item.status!=''&&item.status=='上线中'}">   
		  					<button class="linkStyle upOrdown" id="down-${item.id}">下线</button>&nbsp;&nbsp;${item.status}
						</c:when> 
						<c:when test="${item.status!=''&&item.status=='下线中'}">   
		  					<button class="linkStyle upOrdown" id="up-${item.id}">上线</button>&nbsp;&nbsp;${item.status}
						</c:when> 
						<c:otherwise>   
							<%-- <button class="linkStyle" id="up-${item.id}">未配置</button>&nbsp;&nbsp; --%>未配置
						</c:otherwise> 
					</c:choose>
         		</td>
         		<td id="producturl-${item.id}" style="display:none">${item.productUrl}</td>
         		<td id="imgurl-${item.id}" style="display:none">${item.imgUrl}</td>
         	</tr>
		</c:forEach> 
       </table>
     <!--  <aside class="paging">
       <a>第一页</a>
       <a>1</a>
       <a>2</a>
       <a>3</a>
       <a>…</a>
       <a>1004</a>
       <a>最后一页</a>
      </aside> -->
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>


     <!-- 上架下架提示框 -->
      <script>
     $(document).ready(function(){
    	var upHandle_Id = 0;//要上下架的商品id
    	var upAction = "";
     //弹出文本性提示框
     $(".upOrdown").click(function(){
       $(".del_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       upAction = clickedId.split("-")[0];
       upHandle_Id = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
    	 if(upHandle_Id==0){
    		 alert("请重新选择上下架的商品");
    		 return false;
    	 }
    
    	 if(upAction=="down"){//下线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/banner/vdown/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"bannerId":upHandle_Id}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("下线成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
    	        		  alert("下线失败");
    	        		  window.location.reload();
    	        	  }
     	          }
       	 	}); 
    	 }
    	 else if(upAction=="up"){//上线操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/banner/vup/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"bannerId":upHandle_Id}),
     	          success: function(data){
     	        	  //var cities = JSON.stringify(data.cities);
     	        	  if(data.msg=="200"){
     	        		  alert("上线成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
     	        		  alert("上线失败");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	}); 
    	 }
        	
       $(".del_pop_bg").fadeOut();
       upHandle_Id=0;
       upAction = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       upHandle_Id=0;
       upAction = "";
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
          确认要继续操作吗?
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
      <!-- 上架下架提示框 -->
      
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
