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
    <dd><a href="/orderOn/v_query_queue/${id}?token=${token}">排队订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}?token=${token}">待处理订单</a></dd>
    <dd><a href="/orderOff/v_query_done/${id}?token=${token}">已完成订单</a></dd>
    <dd><a href="/orderOff/v_query_refund/${id}?token=${token}">待退货订单</a></dd>
    <dd><a href="/orderOff/v_query_cancel/${id}?token=${token}">已取消订单</a></dd>
    <!-- <dd><a href="#">未支付订单</a></dd> -->
    <!-- <dd><a href="#">绑定微信号</a></dd> -->
   </dl>
  </li>
   <li>
   <dl>
    <dt>商品信息</dt>
    <!--当前链接则添加class:active-->
    <dd><a href="/product/v_new/${id}?token=${token}">商品添加</a></dd>
    <dd><a href="/product/v_manage/${id}?token=${token}">商品管理</a></dd>
    <dd><a href="/product/v_catalog/${id}?token=${token}">分类管理</a></dd>
    <dd><a href="/product/v_promotion/${id}?token=${token}">综合配置</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/v_usermanage/${id}?token=${token}" class="active">用户管理</a></dd>
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
       <h3>修改会员信息</h3>
       <!--content-->
       <div class="pop_cont_input">
          <ul>
           <li>
            <span>会员姓名</span>
            <input type="text" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">联系电话</span>
            <input type="text" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">会员积分</span>
            <input type="text" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">详细地址</span>
            <input type="text" placeholder="" class="textbox"/>
           </li>
          </ul>
       </div>
       <!--以pop_cont_text分界-->
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确定" class="input_btn trueBtn"/>
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->     
     <!--tabStyle-->
     <script>
     $(document).ready(function(){
		 //tab
		 $(".admin_tab li a").click(function(){
		  var liindex = $(".admin_tab li a").index(this);
		  $(this).addClass("active").parent().siblings().find("a").removeClass("active");
		  $(".admin_tab_cont").eq(liindex).fadeIn(150).siblings(".admin_tab_cont").hide();
		 });
		 });
     </script>
    
     <section>
     <!--  <ul class="admin_tab">
       <li><a class="active">用户管理</a></li>
      </ul> -->
      <!--tabCont-->
      <div class="admin_tab_cont" style="display:block;">
         <section style="text-align:right">
          <input type="text" class="textbox textbox_225" placeholder="请输入手机号、ID、姓名查询"/>
          <input type="button" value="搜索" class="group_btn"/>
         </section>
        <section>
          <table class="table">
           <tr>
            <th>ID</th>
	        <th>姓名</th>
	        <th>联系电话</th>
	        <th>积分</th>
	        <th>详细地址</th>
	        <th>等级</th>
	        <th>注册时间</th>
	        <!-- <th>操作</th> -->
           </tr>
           <c:forEach var="item" items="${users}" varStatus="status">
         	<tr>
         		<td>${item.id}</td>
         		<td>${item.userName}</td>
         		<td>${item.phoneNumber}</td>
         		<td>${item.point}</td>
         		<td>${item.address}</td>
         		<td>${item.rank}</td>
         		<td>${item.registTime}</td>
         		<%-- <td style="text-align:center">
		           <button class="linkStyle editUser" id="showPopTxt${item.id}">修改</button>
		        </td> --%>
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
      </div>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
 </div>
</section>
</body>
</html>
