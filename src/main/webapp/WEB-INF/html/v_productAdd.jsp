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
    <dd><a href="/orderOn/v_query/${id}?token=${token}">未处理订单</a></dd>
    <dd><a href="/orderOff/v_query/${id}?token=${token}">已处理订单</a></dd>
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
    <dd><a href="/user/v_datamanage/${id}?token=${token}">数据管理</a></dd>
    <dd><a href="#">优惠券配置</a></dd>
    <dd><a href="/gift/v_giftmanage/${id}?token=${token}">礼品配置</a></dd>
    <dd><a href="/vendor/v_bindwx/${id}?token=${token}">绑定微信号</a></dd>
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
	<!-- 瞬间消失的提示框 -->
     <section class="loading_area">
      <div class="loading_cont">
       <div class="loading_icon"><i></i><i></i><i></i><i></i><i></i></div>
       <div class="loading_txt"><mark id="tips">操作成功</mark></div>
      </div>
     </section>
     <!-- 瞬间消失的提示框 -->
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
       <h3>订单详情</h3>
       <!--content-->
       <div class="pop_cont_input">
          <table class="table">
              <tr>
                <td>订单编号</td>
                <td>2016283737282892</td>
                <td>下单时间</td>
                <td>2016-04-12</td>
              </tr>
              <tr>
                <td>地址</td>
                <td>开心公寓xxx号</td>
                <td>收货人</td>
                <td>郭德纲</td>
              </tr>
              <tr>
                <td>联系电话</td>
                <td>18763645373</td>
                <td>送货方式</td>
                <td>送货上门</td>
              </tr>
              <tr>
                <td>支付方式</td>
                <td>微信支付</td>
                <td>是否付款</td>
                <td>已付款</td>
              </tr>
              <tr>
                <td>优惠券抵扣</td>
                <td>￥13.00</td>
                <td>备注</td>
                <td>尽快送达</td>
              </tr>
              <tr>
                <td>总价</td>
                <td colspan="3">￥36.00</td>
              </tr>
          </table>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
        提示：接单前请确认库存是否足够。
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认并打印" class="input_btn trueBtn"/>
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

 	<script>
     $(document).ready(function(){
    	 var loginUserId = $("#loginUserId").val();
    	 var showTips = function(content){
    			$("#tips").text(content);
    			$(".loading_area").fadeIn();
                $(".loading_area").fadeOut(500);
    		}
		 $("#prosave").click(function(){
			 	var productName = $.trim($("#proname").val());
			 	var catalog = $('input:radio[name=procatalog]:checked').html();
			 	var label = $('input:radio[name=prolabel]:checked').html();
			 	var standard = $.trim($("#prostandard").val());
			 	var price = $.trim($("#proprice").val());
			 	var marketPrice = $.trim($("#promarketprice").val());
			 	var inventory = $.trim($("#proinventory").val());//分库里必须有库存值，没有则为0
			 	var isShow = $('input:radio[name=proisshow]:checked').val();
			 	var showWay = $('input:radio[name=proshowway]:checked').val();
			 	var sequence = $.trim($("#prosequence").val());
			 	var description = $.trim($("#prodescription").val());
			 	
			 	if(productName == "" || standard=="" || price=="" || marketPrice=="" || inventory== "" || sequence==""){
			 		alert("产品名称、规格、价格、库存、市场价以及排序不能为空");
			 		return false;
			 	}
			 	$.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/product/vnew/"+loginUserId,
		  	          dataType: "json",
		  	          data: JSON.stringify({"productName":productName,"catalog":catalog,"label":label,"standard":standard,"price":price,
		  	        		"marketPrice":marketPrice,"inventory":inventory,"isShow":isShow,"showWay":showWay,"sequence":sequence,"description":description}),
		  	          success: function(data){
		  	        	  if(data.msg=="200"){
		  	        		  //alert("删除区域管理员账号成功");
		  	        		  showTips("增加商品成功信息成功");
		  	        		 // window.location="/user/squeryall";
		  	        	  }
		  	          }
		    	 	});
			 });
		 });
     </script>
   <section>
      <ul class="ulColumn2">
       <li>
        <span class="item_name" style="width:120px;">商品名称：</span>
        <input type="text" id="proname" class="textbox textbox_295" placeholder="如'海南小番茄'"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品分类：</span>
         <label class="single_selection"><input type="radio" name="procatalog" checked="true"/>水果</label>
        <label class="single_selection"><input type="radio" name="procatalog"/>食材</label>
        <label class="single_selection"><input type="radio" name="procatalog"/>零食</label>
         <label class="single_selection"><input type="radio" name="procatalog"/>省钱</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品标签：</span>
        <label class="single_selection"><input type="radio" name="prolabel" checked="true"/>绿色食品</label>
        <label class="single_selection"><input type="radio" name="prolabel"/>小产区</label>
        <label class="single_selection"><input type="radio" name="prolabel"/>新人福利</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品规格：</span>
        <input type="text" id="prostandard" class="textbox textbox_295" placeholder="如'一份250克','一份足2斤'"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">售价(￥)：</span>
        <input type="text" id="proprice" class="textbox textbox_295" placeholder=""/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">市场价(￥)：</span>
        <input type="text" id="promarketprice" class="textbox textbox_295" placeholder=""/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">库存：</span>
        <input type="text" id="proinventory" class="textbox textbox_295" placeholder="" value="0"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">上架状态：</span>
        <label class="single_selection"><input type="radio" name="proisshow" value="yes" checked="true"/>上架</label>
        <label class="single_selection"><input type="radio" name="proisshow" value="no"/>下架</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">展现形式：</span>
        <label class="single_selection"><input type="radio" name="proshowway" value="s" checked="true"/>正方形</label>
        <label class="single_selection"><input type="radio" name="proshowway" value="b"/>长方形</label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品排序：</span>
        <input type="text" id="prosequence" class="textbox textbox_295" value="0" placeholder="输入整数，值越大越前"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品简介：</span>
        <input type="text" id="prodescription" class="textbox textbox_295" placeholder="一句话十字以内"/>
       </li>
       <li>
        <span class="item_name" style="width:120px;">缩略图：</span>
        <label class="uploadImg">
         <input type="file"/>
         <span>正方形</span>
        </label>

        <label class="uploadImg">
         <input type="file"/>
         <span style="margin: 0 20px 0 20px;">长方形</span>
        </label>
       </li>
        <li>
        <span class="item_name" style="width:120px;">商品详情：</span>
        <label class="uploadImg">
         <input type="file"/>
         <span style="margin: 0 60px 0 60px;">详情1</span>
        </label>
       </li>
       <li>
        <span class="item_name" style="width:120px;">商品详情：</span>
        <label class="uploadImg">
         <input type="file"/>
         <span style="margin: 0 60px 0 60px;">详情2</span>
        </label>
       </li>
       <li>
        <span class="item_name" style="width:120px;"></span>
        <input type="button" id="prosave" value="保存" class="link_btn"/>
       </li>
      </ul>
     </section>

    

     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
