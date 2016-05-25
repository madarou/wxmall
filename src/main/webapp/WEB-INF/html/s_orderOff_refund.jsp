<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
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

     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	 var orderId = 0;
		 //弹出文本性提示框
		 $(".editOrder").click(function(){
			 $(".pop_bg").fadeIn();
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
	  	          url: "/orderOff/srefund/"+$("#loginUserId").val(),
	  	          dataType: "json",
	  	          data: JSON.stringify({"orderid":orderId,"cityid":cityid}),
	  	          success: function(data){
	  	        	  if(data.msg=="200"){
	  	        		  alert("操作成功");
	  	        		  window.location.reload();//刷新页面
	  	        	  }
	  	        	  else if(data.msg=="201"){
	  	        		  alert("操作失败");
	  	        	  }
	  	          }
	    	 	});
			 $(".pop_bg").fadeOut();
			 orderId=0;
			 });
		 //弹出：取消或关闭按钮
		 $("#confirmEdit").click(function(){
			 $(".pop_bg").fadeOut();
			 orderId=0;
			 });
		 });
     </script>
     <section class="pop_bg">
				<div class="pop_cont">
					<!--title-->
					<h3>温馨提示</h3>
					<!--content-->
					<div class="pop_cont_input">
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
       <a class="fr top_rt_btn">刷新</a>
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
        <th>接单操作</th>
        <th>退款状态</th>
        <th>订单状态</th>
       </tr>
       	<c:forEach var="item" items="${orderOffs}" varStatus="status">
         	<tr>
         		<td>${item.number}</td>
         		<td>${item.totalPrice}</td>
         		<td>${item.couponPrice}</td>
         		<td>${item.receiverName}</td>
         		<td>${item.phoneNumber}</td>
         		<td>${item.orderTime}</td>
         		<td>${item.receiveTime}</td>
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
		        <td>${item.finalStatus}</td>
		        <td id="cityId-${item.id}" style="display:none">${item.cityId}</td>
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
