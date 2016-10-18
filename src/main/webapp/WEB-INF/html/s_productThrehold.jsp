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
<script src="static/js/jquery.zclip.js" type="text/javascript"></script>
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
    <dd><a href="/product/s_threhold/${id}?token=${token}" class="active">待补货商品</a></dd>
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

     <section>
      <table class="table">
       <tr>
        <th>缩略图</th>
        <th>商品名称</th>
        <th>所在区域</th>
        <th>出售价</th>
        <th>库存</th>
        <th>最低库存</th>
        <th>操作</th>
       </tr>
       <c:forEach var="item" items="${products}" varStatus="status">
       		<tr>
         		<td><img id="pcoversurl-${item.id}" style="width:50px;height:50px" alt="缩略图" src="/static/upload/${item.coverSUrl}"></td>
         		<td id="pproductname-${item.id}">${item.productName}</td>
         		<td id="pdescription-${item.id}">${item.description}</td>
         		<td id="pprice-${item.id}">${item.price}</td>
         		<td id="pinventory-${item.id}">${item.inventory}</td>
         		<td id="pthrehold-${item.id}">${item.threhold}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editProduct" id="showPopTxt-${item.id}">查看</button>|
		           <c:choose> 
		  				<c:when test="${item.supply==0}">   
		  					<button class="linkStyle supplyProduct" id="supply-${item.id}">补货</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle" style="color: gray;cursor: default;">已补货</button>
						</c:otherwise> 
					</c:choose>
		        </td>
         		<td style="display:none" id="psalesvolume-${item.id}">${item.salesVolume}</td>
		        <td style="display:none" id="pshowway-${item.id}">${item.showWay}</td>
		        <td style="display:none" id="pstandard-${item.id}">${item.standard}</td>
		        <td style="display:none" id="pmarketprice-${item.id}">${item.marketPrice}</td>
		        <td style="display:none" id="plabel-${item.id}">${item.label}</td>
		        <td style="display:none" id="pcoverburl-${item.id}">${item.coverBUrl}</td>
		        <td style="display:none" id="psequence-${item.id}">${item.sequence}</td>
		        <td style="display:none" id="pstatus-${item.id}">${item.status}</td>
		        <td style="display:none" id="porigin-${item.id}">${item.origin}</td>
		        <td style="display:none" id="plikes-${item.id}">${item.likes}</td>
		        <td style="display:none" id="psubdetailurl-${item.id}">${item.subdetailUrl}</td>
		        <td style="display:none" id="pdetailurl-${item.id}">${item.detailUrl}</td>
		        <td style="display:none" id="pshowstatus-${item.id}">${item.isShow}</td>
		        <td style="display:none" id="pcityid-${item.id}">${item.cityId}</td>
		        <td style="display:none" id="pareaid-${item.id}">${item.areaId}</td>
         		<td style="display:none" id="pcatalog-${item.id}">${item.catalog}</td>
         		<td style="display:none" id="pprethrehold-${item.id}">${item.prethrehold}</td>
         	</tr>
		</c:forEach> 
		
      </table>
     </section>

    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>

	<!-- 补货 -->
   <script>
     $(document).ready(function(){
    	 var showTips = function(content){
  			$("#tips").text(content);
  			$(".loading_area").fadeIn();
              $(".loading_area").fadeOut(1500);
  		}
    	var orderId_toCancel = 0;//要取消的订单
     //弹出文本性提示框
     $(".supplyProduct").click(function(){
       $(".del_pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       orderId_toCancel = clickedId.split("-")[1];

       });
     //弹出：确认按钮
     $("#confirmCancel").click(function(){
    	 if(orderId_toCancel==0){
    		 alert("请重新选择要补货的商品");
    		 return false;
    	 }
    	 var cityid = $("#pcityid-"+orderId_toCancel).text();
    	 var areaid = $("#pareaid-"+orderId_toCancel).text();
    	 if(cityid.length==0 || areaid.length==0){
    		 alert("请重新选择要补货的商品");
    		 return false;
    	 }
    	 var num = $.trim($("#supplynum").val());
    	 if(num==null || num.length==0){
    		 alert("请输入补货数量");
    		 return false;
    	 }
        	$.ajax({
    		  type: "POST",
  	          contentType: "application/json",
  	          url: "/product/supply/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
  	          dataType: "json",
  	          data: JSON.stringify({"cityid":cityid,"areaid":areaid,"productid":orderId_toCancel,"num":num}),
  	          success: function(data){
  	        	  if(data.msg=="200"){
  	        		  alert("商品补货成功");
  	        		  window.location.reload();//刷新页面
  	        		  orderId_toCancel=0;
  	        	  }else if(data.msg=="401"){
  	        	     alert("需要重新登录");
  	        	}
  	          }
    	 	});
       $(".del_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelCancel").click(function(){
       $(".del_pop_bg").fadeOut();
       orderId_toCancel=0;
       });
     });
     </script>
     <!-- 取消订单 -->
			<section class="del_pop_bg">
				<div class="pop_cont">
					<!--title-->
					<h3>温馨提示</h3>
					<!--content-->
					<div class="small_pop_cont_input">
						<!--以pop_cont_text分界-->
						<div class="pop_cont_text">请输入商品的补货量：
						</div>
						  <section>
						      <ul class="ulColumn2">
						       <li>
						        <span class="item_name">数量：</span>
						        <input type="text" id="supplynum" class="inventory_input textbox_225" placeholder="如'10'"/>
						       </li>
						       <li>
						      </ul>
						  </section>
						<!--bottom:operate->button-->
						<div class="btm_btn">
							<input type="button" value="确认" id="confirmCancel"
								class="input_btn trueBtn" /> <input type="button" value="关闭"
								id="cancelCancel" class="input_btn falseBtn" />
						</div>
					</div>
				</div>
			</section>
			<!-- 补货框 --> 

<!-- 编辑产品 -->
 <script>
     $(document).ready(function(){
    	var editHandle_Id = 0;//要编辑的商品id
    	var productNameO = "";
    	var originO = "";
    	var catalogO = "";
    	var labelO = "";
    	var standardO = "";
    	var marketPriceO = "";
    	var priceO = "";
    	var showWayO = "";
    	var coverSUrlO = "";
    	var coverBUrlO = "";
    	var inventoryO = 0;
    	var sequenceO = 0;
    	var statusO = "";
    	var descriptionO = "";
    	var salesVolumeO = 0;
    	var likesO = 0;
    	var subdetailUrlO = "";
    	var detailUrlO = "";
    	var isShowO = "";
    	var areaIdO = 0;
    	var cityIdO = 0;
    	var threholdO = 0;
    	//弹出文本性提示框
     $(".editProduct").click(function(){
       $(".editproduct_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       editHandle_Id = clickedId.split("-")[1];
       //填充编辑框里的字段
       productNameO = $.trim($("#pproductname-"+editHandle_Id).text());
       originO = $.trim($("#porigin-"+editHandle_Id).text());
       catalogO = $.trim($("#pcatalog-"+editHandle_Id).text());
       labelO = $.trim($("#plabel-"+editHandle_Id).text());
       standardO = $.trim($("#pstandard-"+editHandle_Id).text());
       marketPriceO = $.trim($("#pmarketprice-"+editHandle_Id).text());
       priceO = $.trim($("#pprice-"+editHandle_Id).text());
       showWayO = $.trim($("#pshowway-"+editHandle_Id).text());
       coverSUrlO = $("#pcoversurl-"+editHandle_Id).attr("src").split("/")[3];
       coverBUrlO = $.trim($("#pcoverburl-"+editHandle_Id).text());
       inventoryO = $.trim($("#pinventory-"+editHandle_Id).text());
       sequenceO = $.trim($("#psequence-"+editHandle_Id).text());
       statusO = $.trim($("#pstatus-"+editHandle_Id).text());
       descriptionO = $.trim($("#pdescription-"+editHandle_Id).text());
       salesVolumeO = $.trim($("#psalesvolume-"+editHandle_Id).text());
       likesO = $.trim($("#plikes-"+editHandle_Id).text());
       subdetailUrlO = $.trim($("#psubdetailurl-"+editHandle_Id).text());
       detailUrlO = $.trim($("#pdetailurl-"+editHandle_Id).text());
       isShowO = $.trim($("#pshowstatus-"+editHandle_Id).text());
       areaIdO = $.trim($("#pareaid-"+editHandle_Id).text());
       cityIdO = $.trim($("#pcityid-"+editHandle_Id).text());
       threholdO = $.trim($("#pthrehold-"+editHandle_Id).text());
       prethreholdO = $.trim($("#pprethrehold-"+editHandle_Id).text());

       $("#proname").val(productNameO);
       $("input[type=radio][value="+catalogO+"]").attr("checked",'checked');
       $("input[type=radio][value="+labelO+"]").attr("checked",'checked');
       $("input[type=radio][value="+isShowO+"]").attr("checked",'checked');
       $("input[type=radio][value="+showWayO+"]").attr("checked",'checked');
       $("#proorigin").val(originO);
       $("#prostandard").val(standardO);
       $("#promarketprice").val(marketPriceO);
       $("#proprice").val(priceO);
       $("#proinventory").val(inventoryO);
       $("#prothrehold").val(threholdO);
       $("#proprethrehold").val(prethreholdO);
       $("#prosequence").val(sequenceO);
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
       <h3>商品详情</h3>
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
		        <input type="text" id="proprice" class="price_input textbox_295" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">市场价(￥)：</span>
		        <input type="text" id="promarketprice" class="price_input textbox_295" placeholder=""/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">预设库存：</span>
		        <input type="text" id="proprethrehold" class="inventory_input textbox_295" placeholder="" value="0"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">当前库存：</span>
		        <input type="text" id="proinventory" class="inventory_input textbox_295" placeholder="" value="0"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">最低库存：</span>
		        <input type="text" id="prothrehold" class="inventory_input textbox_295" placeholder="" value="0"/>
		       </li>
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
		        <input type="text" id="prosequence" class="inventory_input textbox_295" value="0" placeholder="输入整数，值越大越前"/>
		       </li>
		       <!-- <li>
		        <span class="item_name" style="width:120px;">商品简介：</span>
		        <input type="text" id="prodescription" class="length_input_20 textbox_295" placeholder="一句话十字以内"/>
		       </li> -->
		       <!-- <li>
		        <span class="item_name" style="width:120px;">缩略图：</span>
		        <span><img alt="(240 x 240)" id="uploads" src="" style="height:100px;width:100px;cursor:pointer"/></span>
		        <span><img alt="(480 x 240)" id="uploadb" src="" style="height:100px;width:200px;cursor:pointer"/></span>
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
							</li> -->
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
          <!-- <input type="button" value="确认" id="confirmEdit" class="input_btn trueBtn"/> -->
          <input type="button" value="关闭" id="cancelEdit" class="input_btn falseBtn"/>
         </div>
       </div>
     </section>
<!-- 编辑产品 -->

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
                     var require = {wid:240,hei:240};
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
<input type="hidden" id="loginToken" value="${token}"></input>
<input type="hidden" id="loginCityId" value="${city_id}"></input>
<input type="hidden" id="loginAreaId" value="${area_id}"></input>
</body>
</html>
