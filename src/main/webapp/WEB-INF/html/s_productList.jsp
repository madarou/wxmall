<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style type="text/css">td{text-align: center;}</style>
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
    <dd><a href="/orderOn/s_queryall/${id}?token=${token}">所有未完成订单</a></dd>
    <dd><a href="/orderOff/s_queryall/${id}?token=${token}">所有已完成订单</a></dd>
    <dd><a href="/orderOff/s_query_refund/${id}?token=${token}">退款订单</a></dd>
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/s_products/${id}?token=${token}" class="active">商品库</a></dd>
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
    <dd><a href="/coupon/s_queryall/${id}?token=${token}">优惠券管理</a></dd>
   </dl>
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
		 $(".trueBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 //弹出：取消或关闭按钮
		 $(".falseBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>订单详情</h3>
       <!--content-->
       <div class="pop_cont_input">
          <table class="table">
              <tr>
                <td>订单编号</td>
                <td>2016283737282892</td>
                <td>下单时间</td>
                <td>2016-04-12</td>
              </tr>
              <tr>
                <td>地址</td>
                <td>开心公寓xxx号</td>
                <td>收货人</td>
                <td>郭德纲</td>
              </tr>
              <tr>
                <td>联系电话</td>
                <td>18763645373</td>
                <td>送货方式</td>
                <td>送货上门</td>
              </tr>
              <tr>
                <td>支付方式</td>
                <td>微信支付</td>
                <td>是否付款</td>
                <td>已付款</td>
              </tr>
              <tr>
                <td>优惠券抵扣</td>
                <td>￥13.00</td>
                <td>备注</td>
                <td>尽快送达</td>
              </tr>
              <tr>
                <td>总价</td>
                <td colspan="3">￥36.00</td>
              </tr>
          </table>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        提示：接单前请确认库存是否足够。
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认并打印" class="input_btn trueBtn"/>
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 搜索 -->
     <section style="text-align:right">
      <input type="text" class=" textbox_225 length_input_20" placeholder="海南西红柿"/>
      <input type="button" value="搜索" class="group_btn"/>
      <a href="/product/s_new/${id}?token=${token}" style="margin-left: 30px">添加商品</a>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>缩略图</th>
        <th>商品名称</th>
        <th>出售价</th>
        <th>市场价</th>
        <th>操作</th>
       </tr>
        <c:forEach var="item" items="${products}" varStatus="status">
         	<tr>
         		<td><img id="pcoversurl-${item.id}" style="width:50px;height:50px" alt="缩略图" src="/static/upload/${item.coverSUrl}"></td>
         		<td id="pproductname-${item.id}">${item.productName}</td>
         		<td id="pprice-${item.id}">${item.price}</td>
         		<td id="pmarketprice-${item.id}">${item.marketPrice}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editProduct" id="showPopTxt-${item.id}">编辑</button>|
		           <button class="linkStyle delProduct" id="delPopTxt-${item.id}">删除</button>
		        </td>
		        
		        <td style="display:none" id="pstandard-${item.id}">${item.standard}</td>
		        <td style="display:none" id="pcoverburl-${item.id}">${item.coverBUrl}</td>
		         <td style="display:none" id="pdescription-${item.id}">${item.description}</td>
		        <td style="display:none" id="porigin-${item.id}">${item.origin}</td>
		        <td style="display:none" id="psubdetailurl-${item.id}">${item.subdetailUrl}</td>
		        <td style="display:none" id="pdetailurl-${item.id}">${item.detailUrl}</td>
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
 
 <!-- 编辑产品 -->
 <script>
     $(document).ready(function(){
    	var editHandle_Id = 0;//要编辑的商品id
    	var productNameO = "";
    	var originO = "";
    	var standardO = "";
    	var marketPriceO = "";
    	var priceO = "";
    	var coverSUrlO = "";
    	var coverBUrlO = "";
    	var descriptionO = "";
    	var subdetailUrlO = "";
    	var detailUrlO = "";
    	//弹出文本性提示框
     $(".editProduct").click(function(){
       $(".editproduct_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       editHandle_Id = clickedId.split("-")[1];
       //填充编辑框里的字段
       productNameO = $.trim($("#pproductname-"+editHandle_Id).text());
       originO = $.trim($("#porigin-"+editHandle_Id).text());
       standardO = $.trim($("#pstandard-"+editHandle_Id).text());
       marketPriceO = $.trim($("#pmarketprice-"+editHandle_Id).text());
       priceO = $.trim($("#pprice-"+editHandle_Id).text());
       coverSUrlO = $("#pcoversurl-"+editHandle_Id).attr("src").split("/")[3];;
       coverBUrlO = $.trim($("#pcoverburl-"+editHandle_Id).text());
       descriptionO = $.trim($("#pdescription-"+editHandle_Id).text());
       subdetailUrlO = $.trim($("#psubdetailurl-"+editHandle_Id).text());
       detailUrlO = $.trim($("#pdetailurl-"+editHandle_Id).text());

       $("#proname").val(productNameO);
       $("#proorigin").val(originO);
       $("#prostandard").val(standardO);
       $("#promarketprice").val(marketPriceO);
       $("#proprice").val(priceO);
       $("#prodescription").val(descriptionO);

       $("#uploads").attr("src", "/static/upload/"+coverSUrlO);
       $("#serverImgNames").val(coverSUrlO);
       $("#uploadb").attr("src", "/static/upload/"+coverBUrlO);
       $("#serverImgNameb").val(coverBUrlO);
       if(subdetailUrlO!=""){
    	   $("#uploadd1").attr("src", "/static/upload/"+subdetailUrlO);
    	   $("#serverImgNamed1").val(subdetailUrlO);
       }
       $("#uploadd2").attr("src", "/static/upload/"+detailUrlO);
       $("#serverImgNamed2").val(detailUrlO);
       });
     //弹出：确认按钮
     $("#confirmEdit").click(function(){
    	 if(editHandle_Id==0){
    		 alert("请重新选择商品");
    		 return false;
    	 }
    	 	var productName = $.trim($("#proname").val());
		 	var origin = $.trim($("#proorigin").val());
		 	var catalog = $('input:radio[name=procatalog]:checked').val();
		 	var label = $('input:radio[name=prolabel]:checked').val();
		 	var standard = $.trim($("#prostandard").val());
		 	var price = $.trim($("#proprice").val());
		 	var marketPrice = $.trim($("#promarketprice").val());
		 	var description = $.trim($("#prodescription").val());
		 	
		 	var coverSUrl = $("#serverImgNames").val();
		 	var coverBUrl = $("#serverImgNameb").val();
		 	var subdetailUrl = $("#serverImgNamed1").val();
		 	var detailUrl = $("#serverImgNamed2").val();
		 	
		 	if(productName == "" || origin=="" || standard=="" || price=="" || marketPrice==""){
		 		alert("产品名称、原产地、规格、价格、市场价不能为空");
		 		return false;
		 	}
		 	if(coverSUrl == "" || coverBUrl == "" || detailUrl==""){
		 		alert("商品正方形、长方形缩略图、详情2图片必须上传");
		 		return false;
		 	}
		 	if(productName == productNameO && origin==originO && standard==standardO && price==priceO &&
		 			marketPrice==marketPriceO && description==descriptionO && coverSUrl==coverSUrlO
		 			&& coverBUrl==coverBUrlO && subdetailUrl==subdetailUrlO && detailUrl==detailUrlO){
		 		alert("并未做修改");
		 		return false;
		 	}
    		$.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/product/sedit/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"id":editHandle_Id,"productName":productName,"origin":origin,"standard":standard,"price":price,
	  	        		"marketPrice":marketPrice,"description":description,
	  	        		"coverSUrl":coverSUrl,"coverBUrl":coverBUrl,"subdetailUrl":subdetailUrl,"detailUrl":detailUrl}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("商品修改成功");
     	        		  window.location.reload();
     	        	  }
     	        	  else if(data.msg=="201"){
    	        		  alert("商品修改失败");
    	        		  window.location.reload();
    	        	  }
     	          }
       	 	});     	
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
       });
     //弹出：取消或关闭按钮
     $("#cancelEdit").click(function(){
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
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
		        <span class="item_name" style="width:120px;">商品名称：</span>
		        <input type="text" id="proname" class=" textbox_295 length_input_20" placeholder="如'海南小番茄'"/>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">原产地：</span>
		        <input type="text" id="proorigin" class=" textbox_295 length_input_20" placeholder="如'海南'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">商品规格：</span>
		        <input type="text" id="prostandard" class=" textbox_295 length_input_30" placeholder="如'一份250克','一份足2斤'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">售价(￥)：</span>
		        <input type="text" id="proprice" class=" textbox_295 price_input" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">市场价(￥)：</span>
		        <input type="text" id="promarketprice" class=" textbox_295 price_input" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">商品简介：</span>
		        <input type="text" id="prodescription" class=" textbox_295 length_input_20" placeholder="一句话十字以内"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">缩略图：</span>
		        <span><img alt="正方形" id="uploads" src="" style="height:100px;width:100px;cursor:pointer"/></span>
		        <span><img alt="长方形" id="uploadb" src="" style="height:100px;width:200px;cursor:pointer"/></span>
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
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd1">
									<input id="fileToUploadd1" style="display: none" type="file"
										name="upfiled1">
								</div> <input type="hidden" id="serverImgNamed1" />
							</li>
							<li>
								<span class="item_name" style="width: 120px;">商品详情2：</span>
								<img alt="详情2" id="uploadd2" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd2">
									<input id="fileToUploadd2" style="display: none" type="file"
										name="upfiled2">
								</div> <input type="hidden" id="serverImgNamed2" />
							</li>
				<!-- <li>
		        <span class="item_name" style="width:120px;"></span>
		        <input type="button" id="prosave" value="保存" class="link_btn"/>
		       </li> -->
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
<!-- 添加s缩略图 -->
    <script>
     $(document).ready(function(){     
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
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
