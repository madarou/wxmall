<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style type="text/css">
    .btm_btn{text-align: center;}
    .btm_btn .input_btn{display:inline-block;height:35px;border:none;background:none;padding:0 20px;}
    .btm_btn .trueBtn{background:#19a97b;color:white;border:1px #19a97b solid;border-radius:2px;}
</style>
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
    <dd><a href="/orderOff/s_query_refund/${id}?token=${token}">退款订单</a></dd>
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/s_products/${id}?token=${token}">商品库</a></dd>
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
		 $(".popAdd").click(function(){
			 $(".pop_bg").fadeIn();
			 });
		 //弹出：确认按钮
		 $("#saveBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 //弹出：取消或关闭按钮
		 $("#cancelBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>添加优惠券</h3>
       <!--content-->
             <div class="pop_cont_input" style="overflow: scroll;width:500px;height:450px;padding:0px">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text" style="padding:0px">
             <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name" style="width:120px;">名称：</span>
		        <input type="text" id="cname" class="textbox textbox_295" placeholder="如'10元代金券'"/>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">面值(￥)：</span>
		        <input type="text" id="camount" class="textbox textbox_295" placeholder="如'10'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">消耗积分：</span>
		        <input type="text" id="cpoint" class="textbox textbox_295" placeholder="如'20'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">使用限制：</span>
		        <input type="text" id="crestrict" class="textbox textbox_295" placeholder="消费满X元才能使用就填X"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">简单说明：</span>
		        <input type="text" id="ccomment" class="textbox textbox_295" placeholder="如'新用户欢迎礼券'"/>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">生效城市：</span>
		        <select class="select" id="ccity">  
				</select>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">是否上线：</span>
		        <select class="select" id="cisshow">  
				</select>
		       </li>
							<li><span class="item_name" style="width: 120px;">封面图：</span>
								<img alt="封面图" id="uploadd1" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd1">
									<input id="fileToUploadd1" style="display: none" type="file"
										name="upfiled1">
								</div> <input type="hidden" id="serverImgNamed1" />
							</li>
							<li>
								<span class="item_name" style="width: 120px;">详细图：</span>
								<img alt="详细图" id="uploadd2" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd2">
									<input id="fileToUploadd2" style="display: none" type="file"
										name="upfiled2">
								</div> <input type="hidden" id="serverImgNamed2" />
							</li>
		      </ul>
		     </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="saveBtn" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelBtn" class="input_btn falseBtn"/>
         </div>
        </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 删除分类弹出框 -->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
     //弹出文本性提示框
     $(".delCoupon").click(function(){
       $(".del_pop_bg").fadeIn();
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
       $(".del_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       });
     });
     </script>
     <section class="del_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>温馨提示</h3>
       <!--content-->
       <div class="pop_cont_input">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text">
          确认要删除该优惠券吗？
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="继续删除" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
     <!-- 删除分类弹出框 -->

     <!-- 搜索 -->
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" value="添加优惠券" class="input_btn trueBtn popAdd"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>名称</th>
        <th>兑换积分</th>
        <th>类型</th>
        <th>抵用面值</th>
        <th>使用限制</th>
        <th>所属城市</th>
        <th>状态</th>
        <th>操作</th>
       </tr>
       	<c:forEach var="item" items="${coupons}" varStatus="status">
         	<tr>
         		<td id="name-${item.id}">${item.name}</td>
         		<td id="point-${item.id}">${item.point}</td>
         		<td id="type-${item.id}">${item.type}</td>
         		<td id="amount-${item.id}">${item.amount}</td>
         		<td id="restrict-${item.id}">${item.restrict}</td>
         		<td id="cityName-${item.id}">${item.cityName}</td>
         		<td style="text-align:center">
		           <c:choose> 
		  				<c:when test="${item.isShow=='no'}">   
		  					<button class="linkStyle" style="color:grey;cursor:default">下线中</button>|
		  					<button class="linkStyle openOrNot" id="open-${item.id}">上线</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle" style="color:grey;cursor:default">上线中</button>|
		  					<button class="linkStyle openOrNot" id="close-${item.id}">下线</button>
						</c:otherwise> 
					</c:choose>
		        </td>
		        <td style="text-align:center">
		        	<button class="linkStyle editCoupon" id="edit-${item.id}">编辑</button>|
		        	<button class="linkStyle delCoupon" id="del-${item.id}">删除</button>
		        </td>
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
</body>
</html>
