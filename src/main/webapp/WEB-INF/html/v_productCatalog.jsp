<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>区域后台管理系统</title>
<link rel="shortcut icon" href="/static/images/icon/favicon.ico" type="image/x-icon" />
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
<script src="static/js/input_constrain.js" type="text/javascript"></script>
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
    <dd><a href="/orderOn/v_query_queue/${id}/1?token=${token}">排队中订单</a></dd>
    <dd><a href="/orderOn/v_query_process/${id}/1?token=${token}">待处理订单</a></dd>
    <dd><a href="/orderOn/v_query_distributed/${id}/1?token=${token}">已配送订单</a></dd>
    <dd><a href="/orderOff/v_query_confirm/${id}/1?token=${token}">已收货订单</a></dd>
    <dd><a href="/orderOff/v_query_refund/${id}/1?token=${token}">待退货订单</a></dd>
    <dd><a href="/orderOff/v_query_teminaled/${id}/1?token=${token}">已完成订单</a></dd>
    <dd><a href="/orderOff/v_query_cancel/${id}/1?token=${token}">已取消/已退货</a></dd>
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
    <dd><a href="/product/v_catalog/${id}?token=${token}" class="active">分类管理</a></dd>
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
       <!--  <h3 style="text-align:right;">欢迎您，某某管理员</h3> -->
        <hr/>
     </section>

     <!--添加分类弹出框效果-->
     <script>
     $(document).ready(function(){
     //弹出文本性提示框
     $(".popAdd").click(function(){
       $(".pop_bg").fadeIn();
       });
     //弹出：确认按钮
     $("#saveBtn").click(function(){
      
       var catname = $.trim($("#catname").val());
       var catsequence = $.trim($("#catsequence").val());
       if(catname == "" || catsequence == ""){
    	   alert("分类名称、排序不能为空");
    	   return false;
       }
       $.ajax({
 		  type: "POST",
	          contentType: "application/json",
	          url: "/area/vnewcatalog/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
	          dataType: "json",
	          data: JSON.stringify({"name":catname,"sequence":catsequence}),
	          success: function(data){
	        	  if(data.msg=="200"){
	        		  alert("分类添加成功");
	        		  window.location.reload();
	        	  }
	        	  else if(data.msg=="202"){
	        		  alert("分类名称不能重复");
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
       <h3>添加分类</h3>
       <!--content-->
       <div class="small_pop_cont_input">
        <ul>
         <li>
          <span>分类名称</span>
          <input type="text" id="catname" placeholder="如'水果'" class="textbox_225 length_input_10"/>
         </li>
         <li>
          <span class="ttl">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
          <input type="text" id="catsequence" placeholder="请填写整数，从大到小排序" class="textbox_225 inventory_input"/>
         </li>
        </ul>
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="保存" id="saveBtn" class="input_btn trueBtn"/>
        <input type="button" value="取消" id="cancelBtn" class="input_btn falseBtn"/>
       </div>
      </div>
     </section>
     <!--结束：弹出框效果-->

     <!-- 修改分类弹出框 -->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	var catName_toEdit="";//要修改的分类名
    	 var nameOld = "";
    	var sequenceOld = "";
	     //弹出文本性提示框
	     $(".popEdit").click(function(){
	       $(".editcat_pop_bg").fadeIn();
	       var clickedId = $(this).attr("id");
	       catName_toEdit = clickedId.split("-")[1];
	       nameOld = $("#name-"+catName_toEdit).text();
	       sequenceOld = $("#sequence-"+catName_toEdit).text();
	    	$("#cat_name").val(nameOld);
	    	$("#cat_sequence").val(sequenceOld);
	       });
	     //弹出：确认按钮
	     $("#saveEdit").click(function(){
	    	 var nameNew = $.trim($("#cat_name").val());
	    	 var sequenceNew = $.trim($("#cat_sequence").val());
	         if(nameNew=="" || sequenceNew==""){
	        	 alert("分类名称、排序不能为空");
	        	 return false;
	         }
	         if(nameNew==nameOld && sequenceNew==sequenceOld){
	        	 alert("并未做修改");
	        	 return false;
	         }
	         $.ajax({
	    		  type: "POST",
	   	          contentType: "application/json",
	   	          url: "/area/veditcatalog/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
	   	          dataType: "json",
	   	          data: JSON.stringify({"oldname":nameOld,"newname":nameNew,"sequence":sequenceNew}),
	   	          success: function(data){
	   	        	  if(data.msg=="200"){
	   	        		  alert("分类修改成功");
	   	        		  window.location.reload();
	   	        	  }
	   	        	  else if(data.msg=="202"){
	   	        		  alert("分类名称不能重复");
	   	        	  }else if(data.msg=="401"){
	   	        	     alert("需要重新登录");
	   	        	}
	   	          }
	    	 	});
	    	 $(".editcat_pop_bg").fadeOut();
	       	 catName_toEdit="";
	       	 nameOld ="";
		     sequenceOld="";
	       });
	     //弹出：取消或关闭按钮
	     $("#cancelEdit").click(function(){
	       $(".editcat_pop_bg").fadeOut();
	       catName_toEdit="";
	       nameOld ="";
	       sequenceOld="";
	       });
     });
     </script>
     <section class="editcat_pop_bg">
      <div class="pop_cont">
       <!--title-->
       		<h3>修改分类</h3>
       		<!--content-->
	       <div class="small_pop_cont_input">
	        <ul>
	         <li>
	          <span>分类名称</span>
	          <input type="text" id="cat_name" placeholder="如'水果'" class="textbox_225 length_input_10"/>
	         </li>
	         <li>
	          <span class="ttl">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
	          <input type="text" id="cat_sequence" placeholder="请填写整数，从大到小排序" class="textbox_225 inventory_input"/>
	         </li>
	        </ul>
	       </div>
	       <!--以pop_cont_text分界-->
	       <div class="pop_cont_text">
	        修改后该分类下的所有记录都将更改
	       </div>
	       <!--bottom:operate->button-->
	       <div class="btm_btn">
	        <input type="button" value="保存" id="saveEdit" class="input_btn trueBtn"/>
	        <input type="button" value="取消" id="cancelEdit" class="input_btn falseBtn"/>
	       </div>
        </div>
     </section>
     <!-- 修改分类弹出框 -->
     
     <!-- 删除分类弹出框 -->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
    	var catName_toDel="";//要删除的分类名
	     //弹出文本性提示框
	     $(".popDel").click(function(){
	       $(".del_pop_bg").fadeIn();
	       var clickedId = $(this).attr("id");
	       catName_toDel = clickedId.split("-")[1];
	       });
	     //弹出：确认按钮
	     $("#confirmDel").click(function(){
	       if(catName_toDel==""){
	    	   alert("请重新选择要删除的分类");
	    	   return false;
	       }
	       $.ajax({
	    		  type: "POST",
	   	          contentType: "application/json",
	   	          url: "/area/vdeletecatalog/"+$("#loginUserId").val()+"/?token="+$("#token").val(),
	   	          dataType: "json",
	   	          data: JSON.stringify({"name":catName_toDel}),
	   	          success: function(data){
	   	        	  if(data.msg=="200"){
	   	        		  alert("分类删除成功");
	   	        		  window.location.reload();
	   	        	  }
	   	        	  else if(data.msg=="201"){
	   	        		  alert("分类删除失败");
	   	        		  window.location.reload();
	   	        	  }else if(data.msg=="401"){
	   	        	     alert("需要重新登录");
	   	        	}
	   	          }
	    	 	});
	       $(".del_pop_bg").fadeOut();
	       catName_toDel="";
	       });
	     //弹出：取消或关闭按钮
	     $("#cancelDel").click(function(){
	       $(".del_pop_bg").fadeOut();
	       catName_toDel="";
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
         <div class="pop_cont_text">
          确认删除？删除后该分类下的所有记录将取消与该分类的关联
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
        <input type="button" value="添加分类" class="input_btn trueBtn popAdd"/>
       </div>
     </section><br/>

     <section>
      <table class="table">
       <tr>
        <th>分类名称</th>
        <th>排序</th>
        <th>操作</th>
       </tr>
       <!-- <tr>
        <td></td>
        <td></td>
        <td style="text-align:center">
           <button class="linkStyle popAdd" id="showPopTxt">编辑</button>|
           <button class="linkStyle" id="delPopTxt">删除</button>
        </td>
       </tr> -->
       	<c:forEach var="item" items="${catalogs}" varStatus="status">
         	<tr>
         		<td id="name-${item.name}">${item.name}</td>
         		<td id="sequence-${item.name}">${item.sequence}</td>
         		<td style="text-align:center">
		           <button class="linkStyle popEdit" id="showPopTxt-${item.name}">编辑</button>|
		           <button class="linkStyle popDel" id="delPopTxt-${item.name}">删除</button>
		        </td>
         	</tr>
		</c:forEach> 
       </table>
      <!-- <aside class="paging">
       <a>第一页</a>
       <a>1</a>
       <a>2</a>
       <a>3</a>
       <a>…</a>
       <a>1004</a>
       <a>最后一页</a>
      </aside> -->
     </section>
    <!--结束：以下内容则可删除，仅为素材引用参考-->
 </div>
</section>
<input type="hidden" id="loginUserId" value="${id}"></input>
<input type="hidden" id="token" value="${token}"></input>
</body>
</html>
