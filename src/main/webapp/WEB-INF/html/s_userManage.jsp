<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>超级管理员系统</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="static/css/style.css" />
<style type="text/css">td{text-align: center;}</style>
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
    	 var userId_toEdit = 0;
    	 var isEdit = false;//判断有没有修改字段，没有则不用提交表单
    	 var oldname = "";
    	 var oldphone = "";
    	 var oldpoint = "";
    	 var oldaddress = "";
    	 var oldrank = "";
		 //弹出文本性提示框
		 $(".editUser").click(function(){
			//获取被点击的行的id，即vendorId
			 var clickedId = $(this).attr("id");
       		 userId_toEdit = clickedId.split("-")[1];
       		 $("#editid").val($("#id-"+userId_toEdit).text());
       		 oldname = $("#userName-"+userId_toEdit).text();
       		 $("#editname").val(oldname);
       		 oldphone = $("#phoneNumber-"+userId_toEdit).text();
      		 $("#editphone").val(oldphone);
      		 oldpoint = $("#point-"+userId_toEdit).text();
     		 $("#editpoint").val(oldpoint);
     		 oldaddress = $("#address-"+userId_toEdit).text();
     		 $("#editaddress").val(oldaddress);
     		 oldrank = $("#rank-"+userId_toEdit).text();
    		 $("#editrank").val(oldrank);
      		 
			 $(".pop_bg").fadeIn();
			 });
		 //弹出：确认按钮
		 $(".trueBtn").click(function(){
			 if(oldname!=$("#editname").val() || oldphone!=$("#editphone").val() ||  oldpoint!=$("#editpoint").val()
					 || oldaddress!=$("#editaddress").val() || oldrank!=$("#editrank").val()){
				 isEdit = true;
			 }
			 if(isEdit == true){//如果有修改，则提交
				 $.ajax({
		    		  type: "POST",
		  	          contentType: "application/json",
		  	          url: "/user/update/"+$("#loginUserId").val(),
		  	          dataType: "json",
		  	          data: JSON.stringify({"id":userId_toEdit,"userName":$("#editname").val(),"phoneNumber":$("#editphone").val(),
		  	        	"point":$("#editpoint").val(),"address":$("#editaddress").val(),"rank":$("#editrank").val()}),
		  	          success: function(data){
		  	        	  if(data.msg=="200"){
		  	        		  alert("修改会员信息成功");
		  	        		  window.location.reload();
		  	        	  }
		  	        	  else if(data.msg=="201"){
		  	        		  alert("修改会员信息失败");
		  	        		  window.location.reload();
		  	        	  }
		  	          }
		    	 	});
			 }
			 $(".pop_bg").fadeOut();
			 });
		 //弹出：取消或关闭按钮
		 $(".falseBtn").click(function(){
			 $(".pop_bg").fadeOut();
			 userId_toEdit = 0;
	    	 isEdit = false;//判断有没有修改字段，没有则不用提交表单
	    	 oldname = "";
	    	 oldphone = "";
	    	 oldpoint = "";
	    	 oldaddress = "";
	    	 oldrank = "";
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
	          <span>会员&nbsp;I&nbsp;D&nbsp;</span>
	          <input type="text" id="editid" style="color:grey" placeholder="" disabled="disabled" class="textbox"/>
	       </li>
           <li>
            <span>会员姓名</span>
            <input type="text" style="color:grey" id="editname" placeholder="" disabled="disabled" class="textbox"/>
           </li>
           <li>
            <span class="ttl">联系电话</span>
            <input type="text" id="editphone" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">会员积分</span>
            <input type="text" id="editpoint" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">默认地址</span>
            <input type="text" id="editaddress" placeholder="" class="textbox"/>
           </li>
           <li>
            <span class="ttl">会员等级</span>
            <input type="text" id="editrank" placeholder="" class="textbox"/>
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
        <th>操作</th>
       </tr>
       <c:forEach var="item" items="${users}" varStatus="status">
         	<tr>
         		<td id="id-${item.id}">${item.id}</td>
         		<td id="userName-${item.id}">${item.userName}</td>
         		<td id="phoneNumber-${item.id}">${item.phoneNumber}</td>
         		<td id="point-${item.id}">${item.point}</td>
         		<td id="address-${item.id}">${item.address}</td>
         		<td id="rank-${item.id}">${item.rank}</td>
         		<td id="registTime-${item.id}">${item.registTime}</td>
         		<td style="text-align:center">
		           <button class="linkStyle editUser" id="showPopTxt-${item.id}">修改</button>
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
</body>
</html>
