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
<title>超级管理员系统</title>
<link rel="shortcut icon" href="/static/images/icon/favicon.ico" type="image/x-icon" />
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<!--[if lt IE 9]>
<script src="static/js/html5.js"></script>
<![endif]-->
<script src="static/js/jquery.js"></script>
<script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>
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
    <dd><a href="/coupon/s_queryall/${id}?token=${token}">优惠券管理</a></dd>
   </dl>
   </li>
   <li>
   <dl>
    <dt>订单统计</dt>
    <dd><a href="/orderOff/s_querydealed/${id}?token=${token}">成交订单统计</a></dd>
    <dd><a href="/orderOff/s_queryreturned/${id}?token=${token}" class="active">退货订单统计</a></dd>
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
       <!--  <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
        <hr/>
     </section>

      <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	var orderId_toView = 0;
    	
     //弹出文本性提示框
     $(".viewOrder").click(function(){
    	$("#productList").html('<tr><td colspan="3">购买商品信息</td></tr><tr><td>商品名称</td><td>单价</td><td>数量</td></tr>');
       $(".pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       orderId_toView = clickedId.split("-")[1];
       $("#oname_phone").text($("#receiverName-"+orderId_toView).text()+"  "+$("#phoneNumber-"+orderId_toView).text());
       $("#onumber").text($("#viewPopTxt-"+orderId_toView).text());
       $("#oreceiveTime").text($("#receiveTime-"+orderId_toView).text());
       $("#oaddress").text($("#address-"+orderId_toView).text());
       $("#ototalPrice").text($("#totalPrice-"+orderId_toView).text());
       $("#ocouponPrice").text($("#couponPrice-"+orderId_toView).text());
       $("#ocomment").text($("#comment-"+orderId_toView).text());
       
     //商品详细列表
  	   var table= $("#productList");
       var productNames = $("#productNames-"+orderId_toView).text();
       var productList = productNames.split(",");
       $.each(productList,function(index,item){
    	   var pname = item.split("=")[0];
    	   var pprice = item.split("=")[1];
    	   var pnumber = item.split("=")[2];
    	   table.append("<tr><td>"+pname+"</td><td>"+pprice+"</td><td>"+pnumber+"</td></tr>");
       });
       		
       });
     //弹出：确认按钮
     $(".trueBtn").click(function(){
       $(".pop_bg").fadeOut();
       orderId_toView=0;
       });
     //弹出：取消或关闭按钮
     $(".falseBtn").click(function(){
       $(".pop_bg").fadeOut();
       orderId_toView=0;
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
          	<tr><td colspan="3">购买商品信息</td></tr>
          	<tr><td>商品名称</td><td>单价</td><td>数量</td></tr>
          </table>
          
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        <!-- <span class="item_name">备注：</span><input type="text" id="vendorcomment" class="textbox textbox_295" placeholder="如'用户电话联系取消'"/> -->
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
            <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->
     <!-- 发起订单查询 -->
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
    	  
    	  $("#search").click(function(){
    		  var cityId = $("#vendorcity").children('option:selected').val();
    		  var areaId = $("#vendorarea").children('option:selected').val();
    		  if(cityId=="选择城市" || areaId =="选择区域"){
    			  alert("请选择查询的城市和区域");return false;
    		  }
    		  var fromdate = $.trim($("#fromdate").val());
    		  var todate = $.trim($("#todate").val());
    		  if(fromdate=="" || todate ==""){
    			  alert("请选择查询的时间区间");return false;
    		  }
    		  if(fromdate>todate){
    			  alert("起始日期不能大于结束日期");return false;
    		  }
    		  $("#export").unbind("click").css('color','grey').css('cursor','default');;
    		  $.ajax({
 	    		 type: "POST",
 		          contentType: "application/json",
 		          url: "/orderOff/queryreturned/"+$("#loginUserId").val(),
 		          dataType: "json",
 		          data:JSON.stringify({"cityid":cityId,"areaid":areaId,"fromdate":fromdate,"todate":todate}),
 		          success: function(data){
 		        	 var table = $("#otable");
 		        	 $("#otable tbody").html("");
 		        	  if(data.msg=="200"){
 		        		  if(data.orders==null||data.orders.length==0){
 		        			 $("#totalT").html("0.00元");
 		        			 table.append('<tr><td colspan="8">没有满足条件的订单</td></tr>');return;
 		        		  }
 		        		 $("#totalT").html(data.totalT+"元");
 		        		 $.each(data.orders,function(i, val){
 		        			 var state = "已退款";
 		        			 var tr = '<tr>'
 		        			 			+'<td><button class="linkStyle viewOrder" id="viewPopTxt-'+val.id+'">'+val.number+'</button></td>'
 		        			 			+'<td id="totalPrice-'+val.id+'">'+val.totalPrice+'</td>'
 		        			 			+'<td id="couponPrice-'+val.id+'">'+val.couponPrice+'</td>'
 		        			 			+'<td id="receiverName-'+val.id+'">'+val.receiverName+'</td>'
 		        			 			+'<td id="phoneNumber-'+val.id+'">'+val.phoneNumber+'</td>'
 		        			 			+'<td id="orderTime-'+val.id+'">'+val.orderTime+'</td>'
 		        			 			+'<td id="finalTime-'+val.id+'">'+val.finalTime+'</td>'
 		        			 			+'<td id="status-'+val.id+'">'+state+'</td>'
 		        			 			+'<td id="productNames-'+val.id+'" style="display:none">'+val.productNames+'</td>'
 		        				        +'<td id="address-'+val.id+'" style="display:none">'+val.address+'</td>'
 		        				        +'<td id="comment-'+val.id+'" style="display:none">'+val.comment+'</td>'
 		        				        +'<td id="receiveTime-'+val.id+'" style="display:none">'+val.receiveTime+'</td>'
 		        			 		+'</tr>';
 		        			 table.append(tr);
 		        		 });
 		        		 var exportBtn = $("#export");
 		        		 exportBtn.css('color','white').css('cursor','pointer');
 		        		 exportBtn.removeAttr("disabled");
 		        		 exportBtn.click(function(){
 		        			var form = $('#exportform');
 		        			$('#cid').val(cityId);$('#aid').val(areaId);$('#fromd').val(fromdate);$('#tod').val(todate);
 		        			form.submit();
 		        			$(this).attr("disabled","true");
 		        			$(this).css('color','grey').css('cursor','default');
 		        		 });
 		        		var orderId_toView = 0;
 		           	
 		        	     //弹出文本性提示框
 		        	     $(".viewOrder").click(function(){
 		        	    	$("#productList").html('<tr><td colspan="3">购买商品信息</td></tr><tr><td>商品名称</td><td>单价</td><td>数量</td></tr>');
 		        	       $(".pop_bg").fadeIn();
 		        	       var clickedId = $(this).attr("id");
 		        	       orderId_toView = clickedId.split("-")[1];
 		        	       $("#oname_phone").text($("#receiverName-"+orderId_toView).text()+"  "+$("#phoneNumber-"+orderId_toView).text());
 		        	       $("#onumber").text($("#viewPopTxt-"+orderId_toView).text());
 		        	       $("#oreceiveTime").text($("#receiveTime-"+orderId_toView).text());
 		        	       $("#oaddress").text($("#address-"+orderId_toView).text());
 		        	       $("#ototalPrice").text($("#totalPrice-"+orderId_toView).text());
 		        	       $("#ocouponPrice").text($("#couponPrice-"+orderId_toView).text());
 		        	       $("#ocomment").text($("#comment-"+orderId_toView).text());
 		        	       
 		        	     //商品详细列表
 		        	  	   var table= $("#productList");
 		        	       var productNames = $("#productNames-"+orderId_toView).text();
 		        	       var productList = productNames.split(",");
 		        	       $.each(productList,function(index,item){
 		        	    	   var pname = item.split("=")[0];
 		        	    	   var pprice = item.split("=")[1];
 		        	    	   var pnumber = item.split("=")[2];
 		        	    	   table.append("<tr><td>"+pname+"</td><td>"+pprice+"</td><td>"+pnumber+"</td></tr>");
 		        	       });
 		        	       		
 		        	       });
 		        	     //弹出：确认按钮
 		        	     $(".trueBtn").click(function(){
 		        	       $(".pop_bg").fadeOut();
 		        	       orderId_toView=0;
 		        	       });
 		        	     //弹出：取消或关闭按钮
 		        	     $(".falseBtn").click(function(){
 		        	       $(".pop_bg").fadeOut();
 		        	       orderId_toView=0;
 		        	       });
 		        	  }
 		          }
 	    	 });
    	  });
     });
     </script>
      <div class="" style="text-align:center">
      		<section>
		      <ul class="ulColumn2">
		       <li>
			        <span class="item_name">所在区域:</span>
		      		<select class="select" id="vendorcity">  
		      			<option value="选择城市">选择城市</option>
		      			<c:forEach var="item" items="${cities}" varStatus="status">
		      				<option value="${item.id}">${item.cityName}</option>
		      			</c:forEach>
		      			<option></option>
					</select>
					<select class="select" id="vendorarea"> 
						<option value="选择区域">选择区域</option> 
					</select>
					
					<span class="item_name">查询日期区间:</span>
		        	<input type="date" style="padding:0 6px;border: 1px #139667 solid;line-height: 30px;font-size: 12px;vertical-align: middle" id="fromdate" value=""/>
				    至
				    <input type="date" style="padding:0 6px;border: 1px #139667 solid;line-height: 30px;font-size: 12px;vertical-align: middle" id="todate" value=""/>
				    <input type="button" id="search" value="查询" class="link_btn"/>
				    <form id="exportform" action="/orderOff/export2/${id}/?token=${token}" method="post" style="display:inline">
		      			<input type="hidden" name="cid" id="cid"/>
		      			<input type="hidden" name="aid" id="aid"/>
		      			<input type="hidden" name="fromd" id="fromd"/>
		      			<input type="hidden" name="tod" id="tod"/>
		      			<input type="button" id="export" value="导出xls" class="link_btn" style="color:grey;cursor:default"/>
		      		</form>
		      		(备注:包含所有退货后已退款的订单、支付后取消且已退款的订单)
		       </li>
		      </ul>
		    </section>
		    
      		<div style="float:right;font-weight: bold;font-size: 14px;"><span class="item_name">全部退货金额累计:</span><span class="item_name" style="color:red" id="totalT">0.00元</span></div>
      	  </div>
      <table id="otable" class="table">
      <thead>
       <tr>
        <th>订单编号</th>
        <th>总金额</th>
        <th>优惠券抵扣</th>
        <th>收货人</th>
        <th>联系电话</th>
        <th>下单时间</th>
        <th>完成时间</th>
        <th>订单状态</th>
       </tr>
       </thead>
       	<%-- <c:forEach var="item" items="${ordersOff}" varStatus="status">
       		<c:set var="index" value="${item.finalStatus}" ></c:set>  
         	<tr>
         		<td><button class="linkStyle viewOrder" id="viewPopTxt-${item.id}">${item.number}</button></td>
         		<td id="totalPrice-${item.id}">￥${item.totalPrice}</td>
         		<td id="couponPrice-${item.id}">${item.couponPrice}</td>
         		<td id="receiverName-${item.id}">${item.receiverName}</td>
         		<td id="phoneNumber-${item.id}">${item.phoneNumber}</td>
         		<td id="orderTime-${item.id}">${item.orderTime}</td>
         		<td id="receiveTime-${item.id}">${item.receiveTime}</td>
         		<td id="cityara-${item.id}">${item.cityarea}</td>
		        <td id="status-${item.id}">${pageScope.orderStates[pageScope.index]}</td>
		        <td id="productNames-${item.id}" style="display:none">${item.productNames}</td>
		        <td id="address-${item.id}" style="display:none">${item.address}</td>
		        <td id="comment-${item.id}" style="display:none">${item.comment}</td>
         	</tr>
		</c:forEach>  --%>
      </table>
      </div>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->

<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
