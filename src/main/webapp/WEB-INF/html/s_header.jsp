<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style>
.change:before{content:"I";color: red !important;}
</style>
</head>
<body style="text-align:center">
<header>
 <h1><img src="static/images/admin_logo.png"/></h1>
 <ul class="rt_nav">
  <li><a href="/orderOff/s_query_refund/${id}/1?token=${token}" target="_top" class="website_icon" id="ordermanage">订单管理</a></li>
  <li><a href="/user/s_queryall/${id}/1?token=${token}" target="_top" class="admin_icon">会员管理</a></li>
  <li><a href="/product/s_products/${id}/1?token=${token}" target="_top" class="product_icon">商品管理</a></li>
  <li><a href="/vendor/s_queryall/${id}?token=${token}" target="_top" class="set_icon">账号设置</a></li>
  <li><a href="/supervisor/logout?token=${token}" target="_top" class="quit_icon">安全退出</a></li>
 </ul>
 </header>
 <input type="hidden" value="${onumber}" id="number"></input>
 <script>
 	if(document.getElementById("number").value>0){
 		var target = document.getElementById("ordermanage");
 		target.style.cssText="color:red;font-size:13px";
 		target.className="change";
 	}
	function myrefresh()
	{
	       window.location.reload();
	}
	setTimeout('myrefresh()',90000); //指定xx秒刷新一次
	</script>
 </body>