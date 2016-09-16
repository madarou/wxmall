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
    <dd><a href="/orderOn/v_query_distributed/${id}/1?token=${token}">已配送订单</a></dd>
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
    <dd><a href="/vendor/v_bindwx/${id}?token=${token}" class="active">绑定微信号</a></dd>
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
     //弹出文本性提示框
     $("#showPopTxt").click(function(){
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
       <h3>添加礼品</h3>
       <!--content-->
        <section style="padding-right: 30px;">
          <ul class="ulColumn2">
            <li>
              <span class="item_name" style="width:120px;">礼品图片：</span>
              <label class="uploadImg">
               <input type="file"/>
               <span>上传图片</span>
              </label>
             </li>
           <li>
            <span class="item_name" style="width:120px;">礼品名称：</span>
            <input type="text" class="textbox textbox_295" placeholder="请输入礼品名称(不超过8个字)"/>
            
           </li>
          <li>
            <span class="item_name" style="width:120px;">兑换积分：</span>
            <input type="text" class="textbox textbox_295" placeholder="请输入兑换积分"/>
         
           </li>
           <li>
            <span class="item_name" style="width:120px;">礼品数量：</span>
            <input type="text" class="textbox textbox_295" placeholder="请输入礼品数量"/>
         
           </li>
           
           <li>
             <div class="btm_btn">
              <input type="button" value="保存" id="saveBtn" class="input_btn trueBtn"/>
              <input type="button" value="取消" id="cancelBtn" class="input_btn falseBtn"/>
             </div>
           </li>
          </ul>
         </section>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 搜索 -->
     <section style="text-align:center">
        <img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}"/><br/>
         <span class="item_name" style="width:120px;">立即绑定个人微信号接收订单!
        扫描左侧二维码实现绑定微信号后，可接收订单信息</span>
     </section><br/>


 </div>
</section>
</body>
</html>
