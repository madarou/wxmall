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
<iframe name="mframe" src="/orderOn/hasNew/${id}/?token=${token}" frameborder="0" scrolling="no" width="100%" height="70px" onload="document.all['mframe'].style.height=mframe.document.body.scrollHeight"></iframe>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <h2 style="height: 35px;"><iframe name="myframe" src="/vendor/index/title/${id}/?token=${token}" frameborder="0" scrolling="no" width="100%" height="100%" onload="document.all['myframe'].style.height=myframe.document.body.scrollHeight"></iframe></h2>
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
    <dd><a href="/gift/v_giftmanage/${id}?token=${token}" class="active">礼品配置</a></dd>
    <dd><a href="/vendor/v_bindwx/${id}?token=${token}">绑定微信号</a></dd>
    <!-- <dd><a href="#">优惠券配置</a></dd> -->
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
        <!-- <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
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
    	 var loginUserId = $("#loginUserId").val();
    	 var showTips = function(content){
 			$("#tips").text(content);
 			$(".loading_area").fadeIn();
             $(".loading_area").fadeOut(500);
 		}
     //弹出文本性提示框
     $("#showPopTxt").click(function(){
       $(".pop_bg").fadeIn();
       });
     //弹出：确认按钮
     $("#saveBtn").click(function(){
    	 	var name = $.trim($("#name").val());
		 	var point = $.trim($("#point").val());
		 	var inventory = $.trim($("#inventory").val());
		 	
		 	if(name == "" || point=="" || inventory==""){
		 		alert("名称、所需积分、数量不能为空");
		 		return false;
		 	}
		 	$.ajax({
	    		  type: "POST",
	  	          contentType: "application/json",
	  	          url: "/gift/vnew/"+loginUserId,
	  	          dataType: "json",
	  	          data: JSON.stringify({"name":name,"point":point,"inventory":inventory}),
	  	          success: function(data){
	  	        	  if(data.msg=="200"){
	  	        		  //alert("删除区域管理员账号成功");
	  	        		  showTips("增加礼品成功");
	  	        		  window.location="/gift/v_giftmanage/"+loginUserId+"?token="+$("#token").val();
	  	        	  }else if(data.msg=="401"){
	  	        	     alert("需要重新登录");
	  	        	}
	  	          }
	    	 	});
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
            <input type="text" class="textbox textbox_295" id="name" placeholder="请输入礼品名称(不超过8个字)"/>
            
           </li>
          <li>
            <span class="item_name" style="width:120px;">兑换积分：</span>
            <input type="text" class="textbox textbox_295" id="point" placeholder="请输入兑换积分"/>
         
           </li>
           <li>
            <span class="item_name" style="width:120px;">礼品数量：</span>
            <input type="text" class="textbox textbox_295" id="inventory" placeholder="请输入礼品数量"/>
         
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
     <section style="text-align:right">
      <div class="btm_btn">
        <input type="button" id="showPopTxt" value="添加礼品" class="input_btn trueBtn"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>缩略图</th>
        <th>名称</th>
        <th>兑换积分</th>
        <th>兑换类型</th>
        <th>礼品数量</th>
        <th>已兑换</th>
        <th>操作</th>
       </tr>
       <c:forEach var="item" items="${gifts}" varStatus="status">
         	<tr>
         		<td>缩略图</td>
         		<td>${item.name}</td>
         		<td>${item.point}</td>
         		<td>${item.type}</td>
         		<td>${item.inventory}</td>
         		<td>${item.inventory-item.remain}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editGift" id="showPopTxt-${item.id}">编辑</button>|
		            <button class="linkStyle delGift" id="showPopTxt-${item.id}">删除</button>
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
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="token" value="${token}"></input>
</body>
</html>
