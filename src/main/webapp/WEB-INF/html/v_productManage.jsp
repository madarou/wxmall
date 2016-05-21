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
<script src="static/js/jquery.zclip.js" type="text/javascript"></script>
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
     
     <!-- 上架下架提示框 -->
      <script>
     $(document).ready(function(){
    	var showHandle_Id = 0;//要上下架的商品id
    	var showAction = "";
     //弹出文本性提示框
     $(".showOrNot").click(function(){
       $(".del_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       showAction = clickedId.split("-")[0];//值为notShow或show
       showHandle_Id = clickedId.split("-")[1];
       });
     //弹出：确认按钮
     $("#confirmDel").click(function(){
    	 if(showHandle_Id==0){
    		 alert("请重新选择下架的商品");
    		 return false;
    	 }
    	 if(showAction=="notShow"){//下架操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/product/vnotshow/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"productId":showHandle_Id}),
     	          success: function(data){
     	        	  //var cities = JSON.stringify(data.cities);
     	        	  if(data.msg=="200"){
     	        		  alert("下架成功");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	}); 
    	 }
    	 else if(showAction=="show"){//上架操作
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/product/vshow/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"productId":showHandle_Id}),
     	          success: function(data){
     	        	  //var cities = JSON.stringify(data.cities);
     	        	  if(data.msg=="200"){
     	        		  alert("上架成功");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	}); 
    	 }
        	
       $(".del_pop_bg").fadeOut();
       showHandle_Id=0;
       showAction = "";
       });
     //弹出：取消或关闭按钮
     $("#cancelDel").click(function(){
       $(".del_pop_bg").fadeOut();
       showHandle_Id=0;
       showAction = "";
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
          确认要继续操作吗?
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="confirmDel" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelDel" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
      <!-- 上架下架提示框 -->

     <!-- 搜索 -->
     <section style="text-align:right">
      <input type="text" class="textbox textbox_225" placeholder="海南西红柿"/>
      <select class="select">
       <option>选择分类...</option>
       <option>水果</option>
       <option>食材</option>
      </select>
      <input type="button" id="search" value="搜索" class="group_btn"/>
      <a href="/product/v_new/${id}?token=${token}" style="margin-left: 30px">添加商品</a>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>缩略图</th>
        <th>商品名称</th>
        <th>商品分类</th>
        <th>出售价</th>
        <th>库存</th>
        <th>销量</th>
        <th>操作</th>
       </tr>
       <c:forEach var="item" items="${products}" varStatus="status">
         	<tr>
         		<td><img style="width:50px;height:50px" alt="缩略图" src="/static/upload/${item.coverSUrl}"></td>
         		<td>${item.productName}</td>
         		<td>${item.catalog}</td>
         		<td>${item.price}</td>
         		<td>${item.inventory}</td>
         		<td>${item.salesVolume}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editProduct" id="showPopTxt-${item.id}">编辑</button>|
		           <c:choose> 
		  				<c:when test="${item.isShow=='yes'||item.isShow==null}">   
		  					<button class="linkStyle showOrNot" id="notShow-${item.id}">下架</button>
						</c:when> 
						<c:otherwise>   
							<button class="linkStyle showOrNot" id="show-${item.id}">上架</button>
						</c:otherwise> 
					</c:choose>|
		           <button class="linkStyle copyProduct" id="copy-${item.id}" style="position: relative;">复制链接</button>
		        </td>
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

<script>
//点击文本框复制其内容到剪贴板上方法
$(document).ready(function(){
	var copyHandle_Id = 0;
	$(".copyProduct").zclip({
		path: '/static/js/zclip/ZeroClipboard.swf',
		copy: function(){
			var clickedId = $(this).attr("id");
		    copyHandle_Id = clickedId.split("-")[1];
		    return "/product/"+copyHandle_Id+"/"+$("#loginUserId").val();
		},
		afterCopy: function(){
			if(copyHandle_Id!=0){
				alert("产品链接已复制到剪切板");
				copyHandle_Id = 0
			}
		}
	});
})
</script>

<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
