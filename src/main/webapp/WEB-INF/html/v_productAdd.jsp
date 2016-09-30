<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>区域后台管理系统</title>
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
  <li><a href="/orderOn/v_query_process/${id}/1?token=${token}" class="website_icon">订单管理</a></li>
  <li><a href="/user/v_usermanage/${id}/1?token=${token}" class="admin_icon">会员管理</a></li>
  <li><a href="/product/v_manage/${id}/1?token=${token}" class="admin_icon">商品管理</a></li>
  <li><a href="/vendor/v_bindwx/${id}?token=${token}" class="set_icon">绑定微信</a></li>
  <li><a href="/vendor/logout/?token=${token}" class="quit_icon">安全退出</a></li>
 </ul>
</header>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <h2><a href="#">社享网</a></h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/v_query_queue/${id}/1?token=${token}">排队订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}/1?token=${token}">待处理订单</a></dd>
    <dd><a href="/orderOn/v_query_distributed/${id}/1?token=${token}">已配送订单</a></dd>
    <dd><a href="/orderOff/v_query_confirm/${id}/1?token=${token}">已收货订单</a></dd>
    <dd><a href="/orderOff/v_query_refund/${id}/1?token=${token}">待退货订单</a></dd>
    <dd><a href="/orderOff/v_query_cancel/${id}/1?token=${token}">已取消订单</a></dd>
    <!-- <dd><a href="#">未支付订单</a></dd> -->
    <!-- <dd><a href="#">绑定微信号</a></dd> -->
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/v_new/${id}?token=${token}" class="active">商品添加</a></dd>
    <dd><a href="/product/v_manage/${id}/1?token=${token}">商品管理</a></dd>
    <dd><a href="/product/v_catalog/${id}?token=${token}">分类管理</a></dd>
    <dd><a href="/product/v_promotion/${id}?token=${token}">综合配置</a></dd>
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

 	<!--是否要复制弹出框-->
     <script>
     $(document).ready(function(){
    	var vendorId_toDel = 0;//要复制的商品id
     //弹出文本性提示框
     $(".copyProduct").click(function(){
       $(".confirm_copy_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       id_toCopy = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmCopy").click(function(){
    	 if(id_toCopy==0){
    		 alert("请重新选择要复制的产品");
    		 return false;
    	 }
		 $(".confirm_copy_pop_bg").fadeOut();
		 $(".repository_pop_bg").fadeOut();
      	 $("#proname").val($("#copyName-"+id_toCopy).text());
      	 $("#proorigin").val($("#copyOrigin-"+id_toCopy).text());
      	$("#proprice").val($("#copyPrice-"+id_toCopy).text());
      	$("#promarketprice").val($("#copyMarketPrice-"+id_toCopy).text());
      	$("#prostandard").val($("#copyStandard-"+id_toCopy).text());
      	$("#prodescription").val($("#copyDescription-"+id_toCopy).text());
      	
      	var coverSUrlO = $("#copyCoverSUrl-"+id_toCopy).attr("src").split("/")[3];
      	$("#serverImgNames").val(coverSUrlO);
      	$("#uploads").attr("src", "/static/upload/"+coverSUrlO);
      	var coverBUrlO = $("#copyCoverBUrl-"+id_toCopy).text();
      	$("#serverImgNameb").val(coverBUrlO);
      	$("#uploadb").attr("src", "/static/upload/"+coverBUrlO);
      	
      	var subdetailUrlO = $("#copySubdetailUrl-"+id_toCopy).text();
      	if(subdetailUrlO!=null && subdetailUrlO!=""){
     	   $("#uploadd1").attr("src", "/static/upload/"+subdetailUrlO);
     	   $("#serverImgNamed1").val(subdetailUrlO);
        }
      	var detailUrlO = $("#copyDetailUrl-"+id_toCopy).text();
      	$("#serverImgNamed2").val(detailUrlO);
      	 $("#uploadd2").attr("src", "/static/upload/"+detailUrlO);
       });
     //弹出：取消或关闭按钮
     $("#cancelCopy").click(function(){
       $(".confirm_copy_pop_bg").fadeOut();
       $(".repository_pop_bg").fadeOut();
       id_toCopy=0;
       });
     });
     </script>
     <section class="confirm_copy_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="small_pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          认要复制该商品信息到正在编辑的商品添加内容中么？<br/>
		  注意：其中分类、序号、库存不会被复制请自行完善。
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确定" id="confirmCopy" class="input_btn trueBtn"/>
          <input type="button" value="返回" id="cancelCopy" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
     <!-- 是否要复制弹出框 -->

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
     $("#proRepository").click(function(){
       $(".repository_pop_bg").fadeIn();
       });
     //弹出：取消或关闭按钮
     $("#closeRep").click(function(){
       $(".repository_pop_bg").fadeOut();
       });
     });
     </script>
     <section class="repository_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>总后台商品库</h3>
       <!--content-->
       <div class="pop_cont_input">
          <table class="table">
	       <tr>
	        <th>缩略图</th>
	        <th>商品名称</th>
	        <th>出售价</th>
	        <th>市场价</th>
	        <th>规格</th>
	        <th>操作</th>
	       </tr>
	       <c:forEach var="item" items="${products}" varStatus="status">
	         	<tr>
	         		<td><img id="copyCoverSUrl-${item.id}" style="width:50px;height:50px" alt="缩略图" src="/static/upload/${item.coverSUrl}"></td>
	         		<td id="copyName-${item.id}">${item.productName}</td>
	         		<td id="copyPrice-${item.id}">${item.price}</td>
	         		<td id="copyMarketPrice-${item.id}">${item.marketPrice}</td>
	         		<td id="copyStandard-${item.id}">${item.standard}</td>
	         		<td style="text-align:center">
			           <button class="linkStyle copyProduct" id="showPopTxt-${item.id}">复制</button>
			        </td>
			        <td id="copyDescription-${item.id}" style="display:none">${item.description}</td>
			        <td id="copyOrigin-${item.id}" style="display:none">${item.origin}</td>
			        <td id="copyCoverBUrl-${item.id}" style="display:none">${item.coverBUrl}</td>
			        <td id="copySubdetailUrl-${item.id}" style="display:none">${item.subdetailUrl}</td>
			        <td id="copyDetailUrl-${item.id}" style="display:none">${item.detailUrl}</td>
	         	</tr>
			</c:forEach> 
	      </table>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        注意：其中分类、序号、库存不会被复制请自行完善。
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="关闭" id="closeRep" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->
          
     <!-- 去商品库下载 -->
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="去商品库下载" id="proRepository" class="input_btn trueBtn"/>
       </div>
     </section>
     <!-- 去商品库下载 -->
 	<script>
     $(document).ready(function(){
    	 var loginUserId = $("#loginUserId").val();
    	 
		 $("#prosave").click(function(){
			 	var productName = $.trim($("#proname").val());
			 	var origin = $.trim($("#proorigin").val());
			 	var catalog = $('input:radio[name=procatalog]:checked').val();
			 	var label = $('input:radio[name=prolabel]:checked').val();
			 	var standard = $.trim($("#prostandard").val());
			 	var price = $.trim($("#proprice").val());
			 	var marketPrice = $.trim($("#promarketprice").val());
			 	var inventory = $.trim($("#proinventory").val());//分库里必须有库存值，没有则为0
			 	var threhold = $.trim($("#prothrehold").val());
			 	var prethrehold = $.trim($("#proprethrehold").val());
			 	var isShow = $('input:radio[name=proisshow]:checked').val();
			 	var showWay = $('input:radio[name=proshowway]:checked').val();
			 	var sequence = $.trim($("#prosequence").val());
			 	var description = $.trim($("#prodescription").val());
			 	
			 	var coverSUrl = $("#serverImgNames").val();
			 	var coverBUrl = $("#serverImgNameb").val();
			 	var subdetailUrl = $("#serverImgNamed1").val();
			 	var detailUrl = $("#serverImgNamed2").val();
			 	
			 	if(productName == "" || origin=="" || standard=="" || price=="" || marketPrice=="" || inventory== "" || sequence==""){
			 		alert("产品名称、原产地、规格、价格、库存、市场价以及排序不能为空");
			 		return false;
			 	}
			 	if(coverSUrl == "" || coverBUrl == "" || detailUrl==""){
			 		alert("商品正方形、长方形缩略图、详情2图片必须上传");
			 		return false;
			 	}
			 	$.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/product/vnew/"+loginUserId,
		  	          dataType: "json",
		  	          data: JSON.stringify({"productName":productName,"origin":origin,"catalog":catalog,"label":label,"standard":standard,"price":price,
		  	        		"marketPrice":marketPrice,"inventory":inventory,"isShow":isShow,"showWay":showWay,"sequence":sequence,"description":description,
		  	        		"coverSUrl":coverSUrl,"coverBUrl":coverBUrl,"subdetailUrl":subdetailUrl,"detailUrl":detailUrl,"threhold":threhold,"prethrehold":prethrehold}),
		  	          success: function(data){
		  	        	  if(data.msg=="200"){
		  	        		  alert("商品添加成功");
		  	        		  window.location.reload();
		  	        	  }
		  	        	  else if(data.msg=="201"){
		  	        		  alert("商品添加失败");
		  	        	  }
		  	          }
		    	 	});
			 });
		 });
     </script>
   <section>
      <ul class="ulColumn2" style="padding-left:22%">
       <li>
        <span class="item_name" style="width:120px;">商品名称：</span>
        <input type="text" id="proname" class="textbox textbox_295 length_input_20" placeholder="如'海南小番茄'"/>
       </li>
        <li>
        <span class="item_name" style="width:120px;">原产地：</span>
        <input type="text" id="proorigin" class="textbox textbox_295 length_input_20" placeholder="如'海南'"/>
       </li>
       <li>
        <span class="item_name" id="catalogs" style="width:120px;">商品分类：</span>
         <c:forEach var="item" items="${catalogs}" varStatus="status">
         	<c:choose> 
  				<c:when test="${status.first==true}">   
  					<label class="single_selection"><input type="radio" name="procatalog" value="${item.name}" checked="checked"/>${item.name}</label>
				</c:when> 
				<c:otherwise>   
					<label class="single_selection"><input type="radio" name="procatalog" value="${item.name}"/>${item.name}</label>
				</c:otherwise> 
			</c:choose> 
         </c:forEach>
         <a href="/product/v_catalog/${id}?token=${token}">添加分类</a>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品标签：</span>
        <label class="single_selection"><input type="radio" name="prolabel" value="无标签" checked="checked"/>无标签</label>
        <label class="single_selection"><input type="radio" name="prolabel" value="绿色食品"/>绿色食品</label>
        <label class="single_selection"><input type="radio" name="prolabel" value="小产区"/>小产区</label>
        <label class="single_selection"><input type="radio" name="prolabel" value="新人福利"/>新人福利</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品规格：</span>
        <input type="text" id="prostandard" class=" textbox_295 length_input_30" placeholder="如'一份250克','一份足2斤'"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">售价(￥)：</span>
        <input type="text" id="proprice" class=" textbox_295 price_input" placeholder="10.00"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">市场价(￥)：</span>
        <input type="text" id="promarketprice" class="textbox textbox_295 price_input" placeholder="12.00"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">预设库存：</span>
        <input type="text" id="proprethrehold" class="textbox textbox_295 inventory_input" placeholder="" value="0"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">当前库存：</span>
        <input type="text" id="proinventory" class="textbox textbox_295 inventory_input" placeholder="" value="0"/>
       </li>
					<li><span class="item_name" style="width: 120px;">最低库存：</span>
						<input type="text" id="prothrehold"
						class="inventory_input textbox_295" placeholder="" value="0" /></li>
					<li>
        <span class="item_name" style="width:120px;">上架状态：</span>
        <label class="single_selection"><input type="radio" name="proisshow" value="yes" checked="checked"/>上架</label>
        <label class="single_selection"><input type="radio" name="proisshow" value="no"/>下架</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">展现形式：</span>
        <label class="single_selection"><input type="radio" name="proshowway" value="s" checked="checked"/>正方形</label>
        <label class="single_selection"><input type="radio" name="proshowway" value="b"/>长方形</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品排序：</span>
        <input type="text" id="prosequence" class="textbox textbox_295 inventory_input" value="0" placeholder="输入整数，值越大越前"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品简介：</span>
        <input type="text" id="prodescription" class="textbox textbox_295 length_input_20" placeholder="一句话十字以内"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">缩略图：</span>
        <span><img alt="(400 x 400)" id="uploads" src="" style="height:100px;width:100px;cursor:pointer"/></span>
        <span><img alt="(480 X 240)" id="uploadb" src="" style="height:100px;width:200px;cursor:pointer"/></span>
		<div id="fileDivs">
		     <input id="fileToUploads" style="display: none" type="file" name="upfiles">
		</div>
		<input type="hidden" id="serverImgNames"/>
		<div id="fileDivb">
		     <input id="fileToUploadb" style="display: none" type="file" name="upfileb">
		</div>
		<input type="hidden" id="serverImgNameb"/>
       </li>
					<li><span class="item_name" style="width: 120px;">商品详情1：</span>
						<img alt="详情1" id="uploadd1" src=""
						style="height: 170px; width: 305px; cursor: pointer">
						<div id="fileDivd1">
							<input id="fileToUploadd1" style="display: none" type="file"
								name="upfiled1">
						</div> <input type="hidden" id="serverImgNamed1" />
					</li>
					<li>
						<span class="item_name" style="width: 120px;">商品详情2：</span>
						<img alt="详情2" id="uploadd2" src=""
						style="height: 300px; width: 305px; cursor: pointer">
						<div id="fileDivd2">
							<input id="fileToUploadd2" style="display: none" type="file"
								name="upfiled2">
						</div> <input type="hidden" id="serverImgNamed2" />
					</li>
		<li>
        <span class="item_name" style="width:120px;"></span>
        <input type="button" id="prosave" value="保存" class="link_btn"/>
       </li>
      </ul>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>

 <!-- 添加s缩略图 -->
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
     $("#uploads").on('click', function() {  
         $('#fileToUploads').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivs').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgs',  
             secureuri:false,  
             fileElementId:'fileToUploads',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 //把图片替换  
                 /* var obj = jQuery.parseJSON(data);  
                 $("#upload").attr("src", "../image/"+obj.fileName);   */
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNames").val(data.imgName);
                     $("#uploads").attr("src", "/static/upload/"+data.imgName);
                     var imgSrc = $("#uploads").attr("src");
                     var require = {wid:400,hei:400};
                     checkImageSize(imgSrc,function(w,h){
                 		if(w!=require.wid || h!=require.hei){
                 			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
                 			$("#serverImgNames").val("");
                 			$("#uploads").attr("src", "");
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
   <!-- 添加s缩略图 -->
   
   <!-- 添加b缩略图 -->
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
     $("#uploadb").on('click', function() {  
         $('#fileToUploadb').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivb').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgb',  
             secureuri:false,  
             fileElementId:'fileToUploadb',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNameb").val(data.imgName);
                     $("#uploadb").attr("src", "/static/upload/"+data.imgName);
                     var imgSrc = $("#uploadb").attr("src");
                     var require = {wid:480,hei:240};
                     checkImageSize(imgSrc,function(w,h){
                 		if(w!=require.wid || h!=require.hei){
                 			alert("图片尺寸不符合! 请上传"+require.wid+"x"+require.hei+"尺寸的图片");
                 			$("#serverImgNameb").val("");
                 			$("#uploadb").attr("src", "");
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
   <!-- 添加b缩略图 -->
   <!-- 添加详情1图 -->
    <script>
     $(document).ready(function(){     
     $("#uploadd1").on('click', function() {  
         $('#fileToUploadd1').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivd1').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgd1',  
             secureuri:false,  
             fileElementId:'fileToUploadd1',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNamed1").val(data.imgName);
                     $("#uploadd1").attr("src", "/static/upload/"+data.imgName);
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
   <!-- 添加详情1图 -->
      <!-- 添加详情2图 -->
    <script>
     $(document).ready(function(){ 
     $("#uploadd2").on('click', function() {  
         $('#fileToUploadd2').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivd2').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgd2',  
             secureuri:false,  
             fileElementId:'fileToUploadd2',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNamed2").val(data.imgName);
                     $("#uploadd2").attr("src", "/static/upload/"+data.imgName);
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
   <!-- 添加详情2图 -->
<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
