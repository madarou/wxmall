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
<title>区域后台管理系统</title>
<meta name="author" content="DeathGhost" />
<meta http-equiv="refresh" content="300">
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<!--[if lt IE 9]>
<script src="static/js/html5.js"></script>
<![endif]-->
<script src="static/js/jquery.js"></script>
<script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="static/js/LodopFuncs.js"></script>
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
  <li><a href="/orderOn/v_query_process/${id}/1?token=${token}" class="website_icon">订单管理</a></li>
  <li><a href="/user/v_usermanage/${id}/1?token=${token}" class="admin_icon">会员管理</a></li>
  <li><a href="/product/v_manage/${id}/1?token=${token}" class="admin_icon">商品管理</a></li>
  <li><a href="/vendor/v_bindwx/${id}?token=${token}" class="set_icon">绑定微信</a></li>
  <li><a href="/vendor/logout/?token=${token}" class="quit_icon">安全退出</a></li>
 </ul>
</header>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <h2><a href="#">社享网</a></h2>
 <ul>
  <li>
   <dl>
    <dt>订单信息</dt>
    <dd><a href="/orderOn/v_query_queue/${id}/1?token=${token}">排队订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}/1?token=${token}" class="active">待处理订单</a></dd>
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

     <!--弹出订单详细框效果-->
     <script>
     $(document).ready(function(){
    	 var LODOP; //打印控件
    	var orderId_toView = 0;
    	
     //弹出文本性提示框
     $(".viewOrder").click(function(){
    	$("#productList").html('<tr><td colspan="3">购买商品信息</td></tr><tr><td>商品名称</td><td>单价</td><td>数量</td></tr>');
       $(".pop_bg").fadeIn();
       var clickedId = $(this).attr("id");
       //orderId_toView = clickedId.charAt(clickedId.length-1);
       orderId_toView = clickedId.split("-")[1];
       $("#oname_phone").text($("#receiverName-"+orderId_toView).text()+"  "+$("#phoneNumber-"+orderId_toView).text());
       $("#onumber").text($("#viewPopTxt-"+orderId_toView).text());
       $("#oreceiveTime").text($("#receiveTime-"+orderId_toView).text());
       $("#oaddress").text($("#address-"+orderId_toView).text());
       $("#ototalPrice").text($("#totalPrice-"+orderId_toView).text());
       $("#ocouponPrice").text($("#couponPrice-"+orderId_toView).text());
       $("#ocomment").text($("#comment-"+orderId_toView).text());
       $("#vendorcomment").val($("#vcomment-"+orderId_toView).text());
       
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
     
     function CreatePrintPage(orderid) {  
         /* var json = {"title":"XXXXX订单信息", "name":"张三", "phone": "138123456789", "orderTime": "2012-10-11 15:30:15",   
         "orderNo": "20122157481315", "shop":"XX连锁", "total":25.10,"totalCount":6,  
         "goodsList":[  
         {"name":"菜心(无公害食品)", "price":5.00, "count":2, "total":10.08},   
         {"name":"菜心(无公害食品)", "price":5.00, "count":2, "total":10.02},   
         {"name":"旺菜", "price":4.50, "count":1, "total":4.50},  
         {"name":"黄心番薯(有机食品)", "price":4.50, "count":1, "total":4.50}  
         ]  
         } */     
         var hPos=10,//小票上边距  
         pageWidth=570,//小票宽度  
         rowHeight=15,//小票行距  
         //获取控件对象  
         LODOP=getLodop();   
         //初始化   
         LODOP.PRINT_INIT("订单"+orderid);  
         //添加小票标题文本  
         LODOP.ADD_PRINT_TEXT(hPos,65,pageWidth,rowHeight,"订单详情");  
         //上边距往下移  
         hPos+=rowHeight;  
           
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"姓名:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#receiverName-"+orderid).text());  
         //hPos+=rowHeight; //电话不换行  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"电话:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#phoneNumber-"+orderid).text());  
         hPos+=rowHeight;
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"地址:");  
         LODOP.ADD_PRINT_TEXT(hPos,30,pageWidth,rowHeight,$("#address-"+orderid).text());  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"下单时间:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#orderTime-"+orderid).text().substr(5));  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"配送时段:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#receiveTime-"+orderid).text().substr(5)); 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"订单编号:");  
         LODOP.ADD_PRINT_TEXT(hPos,60,pageWidth,rowHeight,$("#viewPopTxt-"+orderid).text());  
 
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         hPos+=5;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"商品名称");  
         LODOP.ADD_PRINT_TEXT(hPos,70,pageWidth,rowHeight,"单价");  
         LODOP.ADD_PRINT_TEXT(hPos,110,pageWidth,rowHeight,"数量");  
         LODOP.ADD_PRINT_TEXT(hPos,140,pageWidth,rowHeight,"小计");  
         hPos+=rowHeight;  
         //遍历json的商品数组  
         /* for(var i=0;i<json.goodsList.length;i++){  
               
             if(json.goodsList[i].name.length<4){  
                 LODOP.ADD_PRINT_TEXT(hPos,1,pageWidth,rowHeight,json.goodsList[i].name);  
             }else {  
                 //商品名字过长,其他字段需要换行  
                 LODOP.ADD_PRINT_TEXT(hPos,1,pageWidth,rowHeight,json.goodsList[i].name);  
                 hPos+=rowHeight;  
             }  
             LODOP.ADD_PRINT_TEXT(hPos,70,pageWidth,rowHeight,json.goodsList[i].price);  
             LODOP.ADD_PRINT_TEXT(hPos,115,pageWidth,rowHeight,json.goodsList[i].count);  
             LODOP.ADD_PRINT_TEXT(hPos,140,pageWidth,rowHeight,json.goodsList[i].total);  
             hPos+=rowHeight;  
             } */  
         var productNames = $("#productNames-"+orderid).text();
         var productList = productNames.split(",");
         $.each(productList,function(index,item){
      	   var pname = item.split("=")[0];
      	   var pprice = item.split("=")[1];
      	   var pnumber = item.split("=")[2];
	       if(pname.length<=4){  
	             LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,pname);  
	         }else {  
	             //商品名字过长,其他字段需要换行  
	             LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,pname);  
	             hPos+=rowHeight;  
	       }
	       LODOP.ADD_PRINT_TEXT(hPos,70,pageWidth,rowHeight,pprice);  
           LODOP.ADD_PRINT_TEXT(hPos,115,pageWidth,rowHeight,pnumber);  
           LODOP.ADD_PRINT_TEXT(hPos,140,pageWidth,rowHeight,pprice*pnumber);  
           hPos+=rowHeight;  
         });
         LODOP.ADD_PRINT_LINE(hPos,2, hPos, pageWidth,2, 1);  
         hPos+=5;  
         //合计  
         LODOP.ADD_PRINT_TEXT(hPos,90,pageWidth,rowHeight,"优惠券:￥"+$("#couponPrice-"+orderid).text()); 
         hPos+=rowHeight; 
         LODOP.ADD_PRINT_TEXT(hPos,95,pageWidth,rowHeight,"合计:"+$("#totalPrice-"+orderid).text());  
           
         //hPos+=rowHeight;  
         //LODOP.ADD_PRINT_TEXT(hPos,2,pageWidth,rowHeight,(new Date()).toLocaleDateString()+" "+(new Date()).toLocaleTimeString())  
         hPos+=rowHeight;  
         LODOP.ADD_PRINT_TEXT(hPos,0,pageWidth,rowHeight,"谢谢惠顾,欢迎下次光临!(社享网)");  
         //初始化打印页的规格  
         LODOP.SET_PRINT_PAGESIZE(3,pageWidth,30,"订单详情");  
         LODOP.PRINT();
           
     };    
     //弹出：确认按钮
     $(".trueBtn").click(function(){
    	//LODOP=getLodop();  
 		/* LODOP.PRINT_INIT("订单:"+orderId_toView);
 		LODOP.SET_PRINT_PAGESIZE(3,"58mm","10mm","CreateCustomPage");
 		LODOP.ADD_PRINT_TEXT(0,0,58,50,"打印页面部分内容"); */
 		if(orderId_toView==0){
 			alert("请重新选择要打印的订单");
 			return false;
 		}
 		CreatePrintPage(orderId_toView);   
 		//LODOP.PRINT();	
       $(".pop_bg").fadeOut();
       orderId_toView=0;
       //window.location.reload();
       });
     //弹出：取消或关闭按钮
     $(".falseBtn").click(function(){
       $(".pop_bg").fadeOut();
       orderId_toView=0;
       window.location.reload();
       });
     
	     //添加商户备注
	     $("#subComment").click(function(){
			 var vcontent = $.trim($("#vendorcomment").val());
			 if(vcontent.length>0){
				 $.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/orderOn/vcomment/"+$("#loginUserId").val(),
		  	          dataType: "json",
		  	          data: JSON.stringify({"orderid":orderId_toView,"vcomment":vcontent}),
		  	          success: function(data){
		  	        	  if(data.msg=="200"){
		  	        		  alert("备注添加成功");
		  	        	  }
		  	          }
		    	 	});
			 }
			 else{
				 alert("请输入备注内容");
			 }
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
	        <span class="item_name">备注：</span><input type="text" id="vendorcomment" class="textbox_295" placeholder="如'用户临时改变收货地址'"/>
	        <button class="linkStyle" id="subComment">提交</button>
	       </div>
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认并打印" class="input_btn trueBtn"/>
        <input type="button" value="关闭" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->
     
      <!-- 取消订单 -->
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
       //orderId_toCancel = clickedId.charAt(clickedId.length-1);
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
  	        	  else{
  	        		 alert("订单取消失败，请重试");
 	        		  window.location.reload();//刷新页面
 	        		 orderId_toCancel=0;
  	        	  }
  	          }
    	 	});
       });
     //弹出：取消或关闭按钮
     $("#cancelCancel").click(function(){
       $(".del_pop_bg").fadeOut();
       orderId_toCancel=0;
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
						<div class="pop_cont_text">确认要取消该订单吗?
						</div>
						  <section>
						      <ul class="ulColumn2">
						       <li>
						        <span class="item_name">备注：</span>
						        <input type="text" id="vcomment" class=" textbox_225" placeholder="如'用户电话联系取消'"/>
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
			
	  <!-- 点了配送或完成时的弹框 -->
     <script>
     $(document).ready(function(){
    	var orderId_toHandle = 0;//要操作的订单
    	var handleType;//是配送还是完成
     //弹出文本性提示框
     $(".handleOrder").click(function(){
       $(".handle_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       //orderId_toHandle = clickedId.charAt(clickedId.length-1);
       orderId_toHandle = clickedId.split("-")[1];
		//handleType = clickedId.substring(0,clickedId.length-1);
       handleType = clickedId.split("-")[0];
       });
     //弹出：确认按钮
     $("#confirmHandle").click(function(){
    	 if(orderId_toHandle==0){
    		 alert("请重新选择要操作的订单");
    		 return false;
    	 }
    	 if(handleType=="distribute"){//如果是配送
    		 $.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/orderOn/vdistribute/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"orderid":orderId_toHandle}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  //alert("删除区域管理员账号成功");
     	        		  alert("订单开始配送");
     	        		  window.location.reload();//刷新页面
     	        		  orderId_toCancel=0;
     	        	  }
     	          }
       	 	});
    	 }
    	 else if(handleType=="finish"){//如果是完成
    		 $.ajax({
          		  type: "POST",
        	          contentType: "application/json",
        	          url: "/orderOn/vfinish/"+$("#loginUserId").val(),
        	          dataType: "json",
        	          data: JSON.stringify({"orderid":orderId_toHandle}),
        	          success: function(data){
        	        	  if(data.msg=="200"){
        	        		  //alert("删除区域管理员账号成功");
        	        		  alert("订单完成");
        	        		  window.location.reload();//刷新页面
        	        		  orderId_toCancel=0;
        	        	  }
        	          }
          	 	});
    	 }
        	
       $(".handle_pop_bg").fadeOut();
       });
     //弹出：取消或关闭按钮
     $("#cancelHandle").click(function(){
       $(".handle_pop_bg").fadeOut();
       orderId_toHandle=0;
       });
     });
     </script>
			<section class="handle_pop_bg">
				<div class="pop_cont">
					<!--title-->
					<h3>温馨提示</h3>
					<!--content-->
					<div class="small_pop_cont_input">
						<!--以pop_cont_text分界-->
						<div class="pop_cont_text">确定要继续吗?
						</div>
						<!--bottom:operate->button-->
						<div class="btm_btn">
							<input type="button" value="确定" id="confirmHandle"
								class="input_btn trueBtn" /> <input type="button" value="关闭"
								id="cancelHandle" class="input_btn falseBtn" />
						</div>
					</div>
				</div>
			</section>
			<!-- 配送或完成订单 --> 
			
	     
     <section>
      <div class="page_title">
       <a class="fr top_rt_btn" href="/orderOn/v_query_process/${id}?token=${token}">刷新</a>
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
        <th>接单操作</th>
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
         		<td>
         			<c:if test="${item.status=='3'}">
         				<button class="linkStyle handleOrder" id="distribute-${item.id}">配送</button>|
         				<button class="linkStyle" id="finishPopTxt-${item.id}" style="color:grey;cursor:default">完成</button>
         			</c:if>
         			<c:if test="${item.status=='4'}">
         				<button class="linkStyle" id="distributePopTxt-${item.id}" style="color:grey;cursor:default">配送</button>|
         				<button class="linkStyle handleOrder" id="finish-${item.id}">完成</button>
         			</c:if>
         		</td>
		        <td><button class="linkStyle cancelOrder" id="cancelPopTxt-${item.id}">
					${pageScope.orderStates[pageScope.index]}
				</button></td>
		        <td id="productNames-${item.id}" style="display:none">${item.productNames}</td>
		        <td id="address-${item.id}" style="display:none">${item.address}</td>
		         <td id="comment-${item.id}" style="display:none">${item.comment}</td>
		         <td id="vcomment-${item.id}" style="display:none">${item.vcomment}</td>
         	</tr>
		</c:forEach> 
      </table>
      <aside class="paging">
       <a href="/orderOn/v_query_process/${id}/1?token=${token}">第一页</a>
       <c:forEach var="item" begin="1" end="${pageCount}">
		   <a href="/orderOn/v_query_process/${id}/${item}?token=${token}">${item}</a>
	   </c:forEach>
       <a href="/orderOn/v_query_process/${id}/${pageCount}?token=${token}">最后一页</a>
      </aside>
     </section>
	</div>
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="token" value="${token}"></input>
</body>
</html>
