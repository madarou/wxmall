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
    <dd><a href="/orderOff/s_query_refund/${id}/1?token=${token}" class="active">退款订单</a></dd>
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
    <dd><a href="/orderOff/s_queryreturned/${id}?token=${token}">退货订单统计</a></dd>
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

	<!--订单详情弹出框效果-->
     <script>
     $(document).ready(function(){
    	var orderId_toView = 0;
    	
     //弹出文本性提示框
     $(".viewOrder").click(function(){
    	$("#productList").html('<tr><td colspan="4">购买商品信息</td></tr><tr><td>商品名称</td><td>规格</td><td>单价</td><td>数量</td></tr>');
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
          	<tr><td colspan="4">购买商品信息</td></tr>
          	<tr><td>商品名称</td><td>规格</td><td>单价</td><td>数量</td></tr>
          </table>
          
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        <!-- <span class="item_name">备注：</span><input type="text" id="vendorcomment" class="textbox textbox_295" placeholder="如'用户电话联系取消'"/> -->
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
       <!--  <input type="button" value="确认并打印" class="input_btn trueBtn"/> -->
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：订单详情弹出框效果-->
     
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	 var orderId = 0;
		 //弹出文本性提示框
		 $(".editOrder").click(function(){
			 $(".del_pop_bg").fadeIn();
			 var clickedId = $(this).attr("id");
		     orderId = clickedId.split("-")[1];
			 });
		 //弹出：确认按钮
		 $("#confirmEdit").click(function(){
			 if(orderId==0){
	    		 alert("请重新选择要操作的订单");
	    		 return false;
	    	 }
			 var cityid = $("#cityId-"+orderId).text();
	        	$.ajax({
	    		  type: "POST",
	  	          contentType: "application/json",
	  	          url: "/orderOff/srefund/"+$("#loginUserId").val()+"/?token="+$("#loginToken").val(),
	  	          dataType: "json",
	  	          data: JSON.stringify({"orderid":orderId,"cityid":cityid}),
	  	          success: function(data){
	  	        	  if(data.msg=="200"){
	  	        		  alert("操作成功");
	  	        		  window.location.reload();//刷新页面
	  	        	  }
	  	        	  else if(data.msg=="201"){
	  	        		  alert("操作失败");
	  	        	  }else if(data.msg=="401"){
	  	        	     alert("需要重新登录");
	  	        		}
	  	          }
	    	 	});
			 $(".del_pop_bg").fadeOut();
			 orderId=0;
			 });
		 //弹出：取消或关闭按钮
		 $("#cancelEdit").click(function(){
			 $(".del_pop_bg").fadeOut();
			 orderId=0;
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
						<div class="pop_cont_text">请确认已经通过微信支付后台进行退款再点此操作
						</div>
						<!--bottom:operate->button-->
						<div class="btm_btn">
							<input type="button" value="确认" id="confirmEdit"
								class="input_btn trueBtn" /> <input type="button" value="关闭"
								id="cancelEdit" class="input_btn falseBtn" />
						</div>
					</div>
				</div>
			</section>
     <!--结束：弹出框效果-->

     <section>
      <div class="page_title">
       <b>所有区域下已取消或已退货后，需要退款操作的订单：</b><a class="fr top_rt_btn" href="/orderOff/s_query_refund/${id}?token=${token}">刷新</a>
      </div>
      <table class="table">
       <tr>
        <th>订单编号</th>
        <th>总金额</th>
        <th>优惠券抵扣</th>
        <th>收货人</th>
        <th>联系电话</th>
        <th>下单时间</th>
        <th>配送时段</th>
        <th>退款操作</th>
        <th>退款状态</th>
        <th>订单状态</th>
       </tr>
       	<c:forEach var="item" items="${orderOff}" varStatus="status">
       	<c:set var="index" value="${item.finalStatus}" ></c:set>   
         	<tr>
         		<td><button class="linkStyle viewOrder" id="viewPopTxt-${item.id}">${item.number}</button></td>
         		<td id="totalPrice-${item.id}">￥${item.totalPrice}</td>
         		<td id="couponPrice-${item.id}">${item.couponPrice}</td>
         		<td id="receiverName-${item.id}">${item.receiverName}</td>
         		<td id="phoneNumber-${item.id}">${item.phoneNumber}</td>
         		<td id="orderTime-${item.id}">${item.orderTime}</td>
         		<td id="receiveTime-${item.id}">${item.receiveTime}</td>
         		<td style="text-align:center">
		            <c:choose> 
		  				<c:when test="${item.refundStatus=='待退款'}">   
		  					<button class="linkStyle editOrder" id="showPopTxt-${item.id}">立即处理</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle" id="show-${item.id}" style="color:grey;cursor:default">处理完成</button>
						</c:otherwise> 
					</c:choose>
		        </td>
		        <td>${item.refundStatus}</td>
		        <td>${pageScope.orderStates[pageScope.index]}</td>
		        <td id="cityId-${item.id}" style="display:none">${item.cityId}</td>
		        <td id="areaId-${item.id}" style="display:none">${item.areaId}</td>
		         <td id="productNames-${item.id}" style="display:none">${item.productNames}</td>
		         <td id="productIds-${item.id}" style="display:none">${item.productIds}</td>
		        <td id="address-${item.id}" style="display:none">${item.address}</td>
		         <td id="comment-${item.id}" style="display:none">${item.comment}</td>
         	</tr>
		</c:forEach> 
      </table>
      <aside class="paging">
       <a href="/orderOff/s_query_refund/${id}/1?token=${token}">第一页</a>
       <c:forEach var="item" begin="1" end="${pageCount}">
		   <a href="/orderOff/s_query_refund/${id}/${item}?token=${token}">${item}</a>
	   </c:forEach>
       <a href="/orderOff/s_query_refund/${id}/${pageCount}?token=${token}">最后一页</a>
      </aside>
     </section>
     <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="loginToken" value="${token}"></input>
</body>
</html>
