<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	String[] orderStates={"","未支付","排队中","待处理","配送中","已配送","已收货","已取消","退货申请中","退货中","已退货","已取消退货","已退款","已完成",};
	pageContext.setAttribute("orderStates",orderStates); 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>区域后台管理系统</title>
<link rel="shortcut icon" href="/static/images/icon/favicon.ico" type="image/x-icon" />
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<!--[if lt IE 9]>
<script src="static/js/html5.js"></script>
<![endif]-->
<script src="static/js/jquery.js"></script>
<script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="static/js/LodopFuncs.js"></script>
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

<section class="rt_wrap content mCustomScrollbar">
 <div class="rt_content">
     <section>
        <!-- <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
        <hr/>
     </section>

     <!--弹出订单详细框效果-->
     <script>
     $(document).ready(function(){
    	 var LODOP; //打印控件
    	var orderId_toView = 0;
    	
     //弹出文本性提示框
     $(".viewOrder").click(function(){
    	$("#productList").html('<tr><td colspan="4">购买商品信息</td></tr><tr><td>商品名称</td><td>规格</td><td>单价</td><td>数量</td></tr>');
       $(".pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       //orderId_toView = clickedId.charAt(clickedId.length-1);
       orderId_toView = clickedId.split("-")[1];
       $("#oname_phone").text($("#receiverName-"+orderId_toView).text()+"  "+$("#phoneNumber-"+orderId_toView).text());
       $("#onumber").text($("#viewPopTxt-"+orderId_toView).text());
       $("#oreceiveTime").text($("#receiveTime-"+orderId_toView).text());
       $("#oaddress").text($("#address-"+orderId_toView).text());
       $("#ototalPrice").text($("#totalPrice-"+orderId_toView).text());
       $("#ocouponPrice").text($("#couponPrice-"+orderId_toView).text());
       $("#ocomment").text($("#comment-"+orderId_toView).text());
       $("#vendorcomment").val($("#vcomment-"+orderId_toView).text());
       
     //商品详细列表
  	   var table= $("#productList");
  	//由于商品规格没有在订单里，所以这里需要现根据cityId，areaid和商品id去商品表里查
  	   var productIds = $("#productIds-"+orderId_toView).text();
  	   var productIdList = productIds.split(",");
  	   var cid = $("#cityId-"+orderId_toView).text();
  	   var aid = $("#areaId-"+orderId_toView).text();
       var productNames = $("#productNames-"+orderId_toView).text();
       var productList = productNames.split(",");
       $.each(productList,function(index,item){
    	   var pname = item.split("=")[0];
    	   var pprice = item.split("=")[1];
    	   var pnumber = item.split("=")[2];
    	 //查询规格
    	   $.ajax({
	    		  type: "GET",
	  	          contentType: "application/json",
	  	          url: "/product/"+productIdList[index]+"/"+cid+"/"+aid,
	  	          dataType: "json",
	  	          success: function(data){
	  	        	  if(data.msg=="200"){
	  	        		table.append("<tr><td>"+pname+"</td><td>"+data.product.standard+"</td><td>"+pprice+"</td><td>"+pnumber+"</td></tr>");
	  	        	  }
	  	        	  else{
	  	        		table.append("<tr><td>"+pname+"</td><td>默认规格</td><td>"+pprice+"</td><td>"+pnumber+"</td></tr>");
	  	        	  }
	  	          }
	    	 	});
       });
       		
       });
     
     function CreatePrintPage(orderid) {      
         var hPos=5, //小票上边距  
         pageWidth=570,//小票宽度  
         rowHeight=15,//小票行距  
         //获取控件对象  
         LODOP=getLodop();   
         //初始化   
         LODOP.PRINT_INIT("订单"+orderid);  
         LODOP.SET_PRINT_STYLE("FontSize",12);
         //添加小票标题文本  
         LODOP.ADD_PRINT_TEXT(hPos,65,pageWidth,rowHeight,"社享网"); 
         //上边距往下移  
         hPos+=rowHeight;  hPos+=5; 
         LODOP.SET_PRINT_STYLE("FontSize",10);
           
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"姓名:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#receiverName-"+orderid).text());  
         //hPos+=rowHeight; //电话不换行  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"电话:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#phoneNumber-"+orderid).text());  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"地址:");  
         var add = $("#address-"+orderid).text();
         if(add!=null&&add.length<=12)
         	LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,add);  
         else if(add!=null&&add.length>12&&add.length<=14){
        	hPos+=rowHeight;
        	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add);
         }
         else{
        	hPos+=rowHeight;
         	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add.substr(0,14)); 
         	hPos+=rowHeight;
         	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add.substr(14)); 
         }
        	 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"下单时间:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#orderTime-"+orderid).text().substr(5));  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"配送时段:");  
         if($("#receiveTime-"+orderid).text()=="立即配送")
        	 LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#receiveTime-"+orderid).text()); 
         else
         	 LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#receiveTime-"+orderid).text().substr(5)); 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"订单编号:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#viewPopTxt-"+orderid).text());  
 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         hPos+=5;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"商品名称");  
         LODOP.ADD_PRINT_TEXT(hPos,70,pageWidth,rowHeight,"单价");  
         LODOP.ADD_PRINT_TEXT(hPos,110,pageWidth,rowHeight,"数量");  
         LODOP.ADD_PRINT_TEXT(hPos,140,pageWidth,rowHeight,"小计");  
         hPos+=rowHeight;  
     
         var productNames = $("#productNames-"+orderid).text();
         var productList = productNames.split(",");
         $.each(productList,function(index,item){
      	   var pname = item.split("=")[0];
      	   var pprice = item.split("=")[1];
      	   var pnumber = item.split("=")[2];
	       if(pname.length<=4){  
	             LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,pname);  
	         }else {  
	             //商品名字过长,其他字段需要换行  
	             LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,pname);  
	             hPos+=rowHeight;  
	       }
	       LODOP.ADD_PRINT_TEXT(hPos,70,pageWidth,rowHeight,pprice);  
           LODOP.ADD_PRINT_TEXT(hPos,115,pageWidth,rowHeight,pnumber);  
           LODOP.ADD_PRINT_TEXT(hPos,140,pageWidth,rowHeight,pprice*pnumber);  
           hPos+=rowHeight;  
         });
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         hPos+=5;  
         //合计  
         LODOP.ADD_PRINT_TEXT(hPos,90,pageWidth,rowHeight,"优惠券:￥"+$("#couponPrice-"+orderid).text()); 
         hPos+=rowHeight; 
         LODOP.ADD_PRINT_TEXT(hPos,95,pageWidth,rowHeight,"合计:"+$("#totalPrice-"+orderid).text());  
           
         //hPos+=rowHeight;  
         //LODOP.ADD_PRINT_TEXT(hPos,2,pageWidth,rowHeight,(new Date()).toLocaleDateString()+" "+(new Date()).toLocaleTimeString())  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,15,pageWidth,rowHeight,"谢谢惠顾,欢迎下次光临!");  
         
         //回执
         hPos+=rowHeight; 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         hPos+=5;  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"姓名:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#receiverName-"+orderid).text());  
         //hPos+=rowHeight; //电话不换行  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"电话:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#phoneNumber-"+orderid).text());  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"地址:");  
         var add = $("#address-"+orderid).text();
         if(add!=null&&add.length<=12)
         	LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,add);  
         else if(add!=null&&add.length>12&&add.length<=14){
        	hPos+=rowHeight;
        	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add);
         }
         else{
        	hPos+=rowHeight;
         	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add.substr(0,14)); 
         	hPos+=rowHeight;
         	LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,add.substr(14)); 
         }
        	 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"下单时间:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#orderTime-"+orderid).text().substr(5));  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"配送时段:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#receiveTime-"+orderid).text().substr(5)); 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"订单编号:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#viewPopTxt-"+orderid).text());  
         hPos+=rowHeight; 
         hPos+=rowHeight;   
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         //初始化打印页的规格  
         LODOP.SET_PRINT_PAGESIZE(3,pageWidth,30,"社享网");  
         LODOP.PRINT();
           
     };    
     //弹出：确认按钮
     $(".printtrueBtn").click(function(){
 		if(orderId_toView==0){
 			alert("请重新选择要打印的订单");
 			return false;
 		}
 		CreatePrintPage(orderId_toView);   
       $(".pop_bg").fadeOut();
       orderId_toView=0;
       });
     //弹出：取消或关闭按钮
     $(".printfalseBtn").click(function(){
       $(".pop_bg").fadeOut();
       orderId_toView=0;
       window.location.reload();
       });
     
	     //添加商户备注，由于搜索页面可能出现从on到off致使id发生变化的情况，所以此处统一使用number
	     $("#subComment").click(function(){
			 var vcontent = $.trim($("#vendorcomment").val());
			 var orderNumber = $('#viewPopTxt-'+orderId_toView).html();
			 if(vcontent.length>0 && orderNumber.length>0){
				 $.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/orderOn/vcomment2/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
		  	          dataType: "json",
		  	          data: JSON.stringify({"number":orderNumber,"vcomment":vcontent}),
		  	          success: function(data){
		  	        	  if(data.msg=="200"){
		  	        		  alert("备注添加成功");
		  	        	  }else if(data.msg=="401"){
		  	        	     alert("需要重新登录");
		  	        	}else{
		  	        		alert("备注添加失败");
		  	        	}
		  	          }
		    	 	});
			 }
			 else{
				 alert("请输入备注内容");
			 }
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
          	<tr><td colspan="3">订单详情</td></tr>
          	<tr><td>联系方式</td><td colspan="2" id="oname_phone"></td></tr>
          	<tr><td>订单编号</td><td colspan="2" id="onumber"></td></tr>
          	<tr><td>配送时段</td><td colspan="2" id="oreceiveTime"></td></tr>
          	<tr><td>详细地址</td><td colspan="2" id="oaddress"></td></tr>
          	<tr><td>支付金额</td><td colspan="2" id="ototalPrice"></td></tr>
          	<tr><td>卡券抵扣</td><td colspan="2" id="ocouponPrice"></td></tr>
          	<tr><td>备注</td><td colspan="2" id="ocomment"></td></tr>
          </table>
          <table class="table" id="productList">
          	<tr><td colspan="4">购买商品信息</td></tr>
          	<tr><td>商品名称</td><td>规格</td><td>单价</td><td>数量</td></tr>
          </table>
          <!--以pop_cont_text分界-->
	       <div class="pop_cont_text">
	        <span class="item_name">备注：</span><input type="text" id="vendorcomment" class="textbox_295" placeholder="如'用户临时改变收货地址'"/>
	        <button class="linkStyle" id="subComment">提交</button>
	       </div>
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认并打印" class="input_btn printtrueBtn"/>
        <input type="button" value="关闭" class="input_btn printfalseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->
     
      <!-- 取消订单 -->
     <script>
     $(document).ready(function(){
    	 var showTips = function(content){
  			$("#tips").text(content);
  			$(".loading_area").fadeIn();
              $(".loading_area").fadeOut(1500);
  		}
    	var orderId_toCancel = 0;//要取消的订单
     //弹出文本性提示框
     $(".cancelOrder").click(function(){
       $(".del_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       //orderId_toCancel = clickedId.charAt(clickedId.length-1);
       orderId_toCancel = clickedId.split("-")[1];
       handleType = clickedId.split("-")[0];
       });
     //弹出：确认按钮
     $("#confirmCancel").click(function(){
    	 if(orderId_toCancel==0){
    		 alert("请重新选择要取消的订单");
    		 return false;
    	 }
    	 if(handleType=="cancelPopTxt"){//如果是取消订单
        	$.ajax({
    		  type: "POST",
  	          contentType: "application/json",
  	          url: "/orderOn/vcancel/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
  	          dataType: "json",
  	          data: JSON.stringify({"orderid":orderId_toCancel,"vcomment":$.trim($("#vcomment").val())}),
  	          success: function(data){
  	        	  if(data.msg=="200"){
  	        		  //alert("删除区域管理员账号成功");
  	        		  alert("订单取消成功");
  	        		  window.location.reload();//刷新页面
  	        		  orderId_toCancel=0;
  	        	  }else if(data.msg=="401"){
  	        	     alert("需要重新登录");
  	        	  }
  	        	  else{
  	        		 alert("订单取消失败，请重试");
 	        		  window.location.reload();//刷新页面
 	        		 orderId_toCancel=0;
  	        	  }
  	          }
    	 	});
    	 }
    	 else if(handleType=="cancelPopTxtr"){//如果是取消退货
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/orderOff/vcancel/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"orderid":orderId_toCancel,"vcomment":$.trim($("#vcomment").val())}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  //alert("删除区域管理员账号成功");
     	        		  alert("退货取消成功");
     	        		  window.location.reload();//刷新页面
     	        		  orderId_toCancel=0;
     	        	  }else if(data.msg=="401"){
     	        	     alert("需要重新登录");
     	        	  }else{
     	        		  alert("取消订单失败");
     	        		  window.location.reload();//刷新页面
     	        	  }
     	          }
       	 	});
      	 }
       });
     //弹出：取消或关闭按钮
     $("#cancelCancel").click(function(){
       $(".del_pop_bg").fadeOut();
       orderId_toCancel=0;
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
						<div class="pop_cont_text">确认要取消吗?
						</div>
						  <section>
						      <ul class="ulColumn2">
						       <li>
						        <span class="item_name">备注：</span>
						        <input type="text" id="vcomment" class=" textbox_225" placeholder="如'用户电话联系取消'"/>
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
			<!-- 取消订单 --> 
			
	  <!-- 点了配送或完成时的弹框 -->
     <script>
     $(document).ready(function(){
    	var orderId_toHandle = 0;//要操作的订单
    	var handleType;//是配送还是完成
     //弹出文本性提示框
     $(".handleOrder").click(function(){
       $(".handle_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       //orderId_toHandle = clickedId.charAt(clickedId.length-1);
       orderId_toHandle = clickedId.split("-")[1];
		//handleType = clickedId.substring(0,clickedId.length-1);
       handleType = clickedId.split("-")[0];
       });
     //弹出：确认按钮
     $("#confirmHandle").click(function(){
    	 if(orderId_toHandle==0){
    		 alert("请重新选择要操作的订单");
    		 return false;
    	 }
    	 if(handleType=="distribute"){//如果是配送
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/orderOn/vdistribute/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"orderid":orderId_toHandle}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  //alert("删除区域管理员账号成功");
     	        		  alert("订单开始配送");
     	        		  window.location.reload();//刷新页面
     	        		  orderId_toCancel=0;
     	        	  }else if(data.msg=="401"){
     	        	     alert("需要重新登录");
     	        	 }
     	          }
       	 	});
    	 }
    	 else if(handleType=="finish"){//如果是完成
    		 $.ajax({
          		  type: "POST",
        	          contentType: "application/json",
        	          url: "/orderOn/vfinish/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
        	          dataType: "json",
        	          data: JSON.stringify({"orderid":orderId_toHandle}),
        	          success: function(data){
        	        	  if(data.msg=="200"){
        	        		  alert("订单配送完成");
        	        		  window.location.reload();//刷新页面
        	        		  orderId_toCancel=0;
        	        	  }else if(data.msg=="401"){
        	        		     alert("需要重新登录");
        	        	  }
        	          }
          	 	});
    	 }
    	 else if(handleType=="refund"){//如果是开始退货
    		 $.ajax({
          		  type: "POST",
        	          contentType: "application/json",
        	          url: "/orderOff/vrefund/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
        	          dataType: "json",
        	          data: JSON.stringify({"orderid":orderId_toHandle}),
        	          success: function(data){
        	        	  if(data.msg=="200"){
        	        		  //alert("删除区域管理员账号成功");
        	        		  alert("订单开始退货");
        	        		  window.location.reload();//刷新页面
        	        		  orderId_toCancel=0;
        	        	  }else if(data.msg=="401"){
        	        	     alert("需要重新登录");
        	        	 }
        	          }
          	 	});
    	 }
    	 else if(handleType=="finishr"){//如果是完成退货
    		 $.ajax({
         		  type: "POST",
       	          contentType: "application/json",
       	          url: "/orderOff/vfinish/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
       	          dataType: "json",
       	          data: JSON.stringify({"orderid":orderId_toHandle}),
       	          success: function(data){
       	        	  if(data.msg=="200"){
       	        		  alert("订单退货完成");
       	        		  window.location.reload();//刷新页面
       	        		  orderId_toCancel=0;
       	        	  }else if(data.msg=="401"){
       	        		     alert("需要重新登录");
       	        	  }
       	          }
         	 	});
    	 }
    	 else if(handleType=="processPopTxt"){//如果是立即处理
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/orderOn/vprocess/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"orderid":orderId_toHandle}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("订单进入待处理列表");
     	        		  window.location.reload();//刷新页面
     	        		  orderId_toProcess=0;
     	        	  }else if(data.msg=="401"){
     	        	     alert("需要重新登录");
     	        	}
     	          }
       	 	});
    	 }
        	
       $(".handle_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelHandle").click(function(){
       $(".handle_pop_bg").fadeOut();
       orderId_toHandle=0;
       });
     });
     </script>
			<section class="handle_pop_bg">
				<div class="pop_cont">
					<!--title-->
					<h3>温馨提示</h3>
					<!--content-->
					<div class="small_pop_cont_input">
						<!--以pop_cont_text分界-->
						<div class="pop_cont_text">确定要继续吗?
						</div>
						<!--bottom:operate->button-->
						<div class="btm_btn">
							<input type="button" value="确定" id="confirmHandle"
								class="input_btn trueBtn" /> <input type="button" value="关闭"
								id="cancelHandle" class="input_btn falseBtn" />
						</div>
					</div>
				</div>
			</section>
			<!-- 配送或完成订单 --> 
			
	     
     <section>
      <div class="page_title">
       <b>在所有状态列表中搜索到的订单：</b>
      </div>
      <table class="table">
       <tr>
        <th>订单编号</th>
        <th>总金额</th>
        <th>卡券抵扣</th>
        <th>收货人</th>
        <th>联系电话</th>
        <th>下单时间</th>
        <th>配送时段</th>
        <th>操作</th>
        <th>订单状态</th>
       </tr>
       <c:if test="${orderOn!=null}">
       		<c:set var="index" value="${orderOn.status}" ></c:set>
         	<tr>
         		<td><button class="linkStyle viewOrder" id="viewPopTxt-${orderOn.id}">${orderOn.number}</button></td>
         		<td id="totalPrice-${orderOn.id}">￥${orderOn.totalPrice}</td>
         		<td id="couponPrice-${orderOn.id}">${orderOn.couponPrice}</td>
         		<td id="receiverName-${orderOn.id}">${orderOn.receiverName}</td>
         		<td id="phoneNumber-${orderOn.id}">${orderOn.phoneNumber}</td>
         		<td id="orderTime-${orderOn.id}">${orderOn.orderTime}</td>
         		<td id="receiveTime-${orderOn.id}">${orderOn.receiveTime}</td>
         		<td>
         			<c:if test="${orderOn.status=='2'}">
         				<button class="linkStyle handleOrder" id="processPopTxt-${orderOn.id}">立即处理</button>
         			</c:if>
         			<c:if test="${orderOn.status=='3'}">
         				<button class="linkStyle handleOrder" id="distribute-${orderOn.id}">配送</button>|
         				<button class="linkStyle" id="finishPopTxt-${orderOn.id}" style="color:grey;cursor:default">完成</button>
         			</c:if>
         			<c:if test="${orderOn.status=='4'}">
         				<button class="linkStyle" id="distributePopTxt-${orderOn.id}" style="color:grey;cursor:default">配送</button>|
         				<button class="linkStyle handleOrder" id="finish-${orderOn.id}">完成</button>
         			</c:if>
         		</td>
		        <td>
					<c:choose> 
		  				<c:when test="${orderOn.status=='5'}">   
		  					<button class="linkStyle" style="cursor:default;color:grey" id="cancelPopTxt-${orderOn.id}">
								${pageScope.orderStates[pageScope.index]}
							</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle cancelOrder" id="cancelPopTxt-${orderOn.id}">
								${pageScope.orderStates[pageScope.index]}
							</button>
						</c:otherwise> 
					</c:choose>		        
		        </td>
		        <td id="productNames-${orderOn.id}" style="display:none">${orderOn.productNames}</td>
		        <td id="address-${orderOn.id}" style="display:none">${orderOn.address}</td>
		         <td id="comment-${orderOn.id}" style="display:none">${orderOn.comment}</td>
		         <td id="vcomment-${orderOn.id}" style="display:none">${orderOn.vcomment}</td>
		          <td id="productIds-${orderOn.id}" style="display:none">${orderOn.productIds}</td>
		          <td id="cityId-${orderOn.id}" style="display:none">${orderOn.cityId}</td>
		         <td id="areaId-${orderOn.id}" style="display:none">${orderOn.areaId}</td>
         	</tr>
		</c:if>
		
		<c:if test="${orderOff!=null}">
       		<c:set var="index" value="${orderOff.finalStatus}" ></c:set>
         	<tr>
         		<td><button class="linkStyle viewOrder" id="viewPopTxt-${orderOff.id}">${orderOff.number}</button></td>
         		<td id="totalPrice-${orderOff.id}">￥${orderOff.totalPrice}</td>
         		<td id="couponPrice-${orderOff.id}">${orderOff.couponPrice}</td>
         		<td id="receiverName-${orderOff.id}">${orderOff.receiverName}</td>
         		<td id="phoneNumber-${orderOff.id}">${orderOff.phoneNumber}</td>
         		<td id="orderTime-${orderOff.id}">${orderOff.orderTime}</td>
         		<td id="receiveTime-${orderOff.id}">${orderOff.receiveTime}</td>
         		<td>
         			<c:if test="${orderOff.finalStatus=='8'}">
         				<button class="linkStyle handleOrder" id="refund-${orderOff.id}">退货</button>|
         				<button class="linkStyle" id="finishPopTxtr-${orderOff.id}" style="color:grey;cursor:default">完成</button>
         			</c:if>
         			<c:if test="${orderOff.finalStatus=='9'}">
         				<button class="linkStyle" id="distributePopTxt-${orderOff.id}" style="color:grey;cursor:default">退货</button>|
         				<button class="linkStyle handleOrder" id="finishr-${orderOff.id}">完成</button>
         			</c:if>
         		</td>
		        <td>
		        	<c:choose> 
		  				<c:when test="${orderOff.finalStatus=='6' or orderOff.finalStatus=='7' or orderOff.finalStatus=='10' or orderOff.finalStatus=='11' or orderOff.finalStatus=='12' or orderOff.finalStatus=='13'}">   
		  					<button class="linkStyle" style="cursor:default;color:grey" id="cancelPopTxt-${orderOff.id}">
								${pageScope.orderStates[pageScope.index]}
							</button>
						</c:when> 
						<c:otherwise>   
							 <button class="linkStyle cancelOrder" id="cancelPopTxtr-${orderOff.id}">
								${pageScope.orderStates[pageScope.index]}
							</button>
						</c:otherwise> 
					</c:choose>	
		       </td>
		        <td id="productNames-${orderOff.id}" style="display:none">${orderOff.productNames}</td>
		        <td id="address-${orderOff.id}" style="display:none">${orderOff.address}</td>
		         <td id="comment-${orderOff.id}" style="display:none">${orderOff.comment}</td>
		         <td id="vcomment-${orderOff.id}" style="display:none">${orderOff.vcomment}</td>
		         <td id="cityId-${orderOff.id}" style="display:none">${orderOff.cityId}</td>
		         <td id="areaId-${orderOff.id}" style="display:none">${orderOff.areaId}</td>
		         <td id="productIds-${orderOff.id}" style="display:none">${orderOff.productIds}</td>
         	</tr>
		</c:if>
      </table>
     </section>
	</div>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="token" value="${token}"></input>
</body>
</html>
