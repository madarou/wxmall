<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	String[] orderStates={"","未支付","排队中","待处理","配送中","已配送","已收货","已取消","退货申请中","退货中","已退货","已取消退货","已退款","已评价",};
	pageContext.setAttribute("orderStates",orderStates); 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>区域后台管理系统</title>
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
<!--header-->
<header>
 <h1><img src="static/images/admin_logo.png"/></h1>
 <ul class="rt_nav">
  <li><a href="#" class="set_icon">订单提醒</a></li>
  <li><a href="login.php" class="quit_icon">安全退出</a></li>
 </ul>
</header>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <h2><a href="index.php">常州-某某区</a></h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/v_query_queue/${id}/1?token=${token}">排队订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}/1?token=${token}">待处理订单</a></dd>
    <dd><a href="/orderOn/v_query_distributed/${id}/1?token=${token}" class="active">已配送订单</a></dd>
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
          <!--以pop_cont_text分界-->
	       <div class="pop_cont_text">
	       <span class="item_name">备注：</span>在用户确认收货或一定时间后，已配送订单会进入已收货订单列表
	       </div>
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
       <!--  <input type="button" value="确认并打印" class="input_btn trueBtn"/> -->
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->
     
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
       orderId_toCancel = clickedId.split("-")[1];

       });
     //弹出：确认按钮
     $("#confirmCancel").click(function(){
    	 if(orderId_toCancel==0){
    		 alert("请重新选择要取消的订单");
    		 return false;
    	 }
        	$.ajax({
    		  type: "POST",
  	          contentType: "application/json",
  	          url: "/orderOn/vcancel/"+$("#loginUserId").val(),
  	          dataType: "json",
  	          data: JSON.stringify({"orderid":orderId_toCancel,"vcomment":$.trim($("#vcomment").val())}),
  	          success: function(data){
  	        	  if(data.msg=="200"){
  	        		  //alert("删除区域管理员账号成功");
  	        		  alert("订单取消成功");
  	        		  window.location.reload();//刷新页面
  	        		  orderId_toCancel=0;
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
					<div class="pop_cont_input">
						<!--以pop_cont_text分界-->
						<div class="pop_cont_text">确认要取消该订单吗?
						</div>
						  <section>
						      <ul class="ulColumn2">
						       <li>
						        <span class="item_name">备注：</span>
						        <input type="text" id="vcomment" class="textbox textbox_295" placeholder="如'用户电话联系取消'"/>
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
	     
     <section>
      <div class="page_title">
       <a class="fr top_rt_btn" href="/orderOn/v_query_distributed/${id}?token=${token}">刷新</a>
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
        <th>订单状态</th>
       </tr>
       	<c:forEach var="item" items="${orders}" varStatus="status">
       		<c:set var="index" value="${item.status}" ></c:set>
         	<tr>
         		<td><button class="linkStyle viewOrder" id="viewPopTxt-${item.id}">${item.number}</button></td>
         		<td id="totalPrice-${item.id}">￥${item.totalPrice}</td>
         		<td id="couponPrice-${item.id}">${item.couponPrice}</td>
         		<td id="receiverName-${item.id}">${item.receiverName}</td>
         		<td id="phoneNumber-${item.id}">${item.phoneNumber}</td>
         		<td id="orderTime-${item.id}">${item.orderTime}</td>
         		<td id="receiveTime-${item.id}">${item.receiveTime}</td>
		        <td><button class="linkStyle" id="cancelPopTxt-${item.id}" style="color:grey;cursor:default">
		        	${pageScope.orderStates[pageScope.index]}
		        </button></td>
		        <td id="productNames-${item.id}" style="display:none">${item.productNames}</td>
		        <td id="address-${item.id}" style="display:none">${item.address}</td>
		         <td id="comment-${item.id}" style="display:none">${item.comment}</td>
         	</tr>
		</c:forEach> 
      </table>
      <aside class="paging">
       <a href="/orderOn/v_query_distributed/${id}/1?token=${token}">第一页</a>
       <c:forEach var="item" begin="1" end="${pageCount}">
		   <a href="/orderOn/v_query_distributed/${id}/${item}?token=${token}">${item}</a>
	   </c:forEach>
       <a href="/orderOn/v_query_distributed/${id}/${pageCount}?token=${token}">最后一页</a>
      </aside>
     </section>
	</div>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="token" value="${token}"></input>
</body>
</html>
