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
<script src="static/js/ajaxfileupload.js" type="text/javascript"></script>
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
    <dd><a href="/product/v_manage/${id}?token=${token}" class="active">商品管理</a></dd>
    <dd><a href="/product/v_catalog/${id}?token=${token}">分类管理</a></dd>
    <dd><a href="/product/v_promotion/${id}?token=${token}">综合配置</a></dd>
   </dl>
  </li>
  <li>
   <dl>
    <dt>会员管理</dt>
    <dd><a href="/user/v_usermanage/${id}?token=${token}">用户管理</a></dd>
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
         		<td><img id="pcoversurl-${item.id}" style="width:50px;height:50px" alt="缩略图" src="/static/upload/${item.coverSUrl}"></td>
         		<td id="pproductname-${item.id}">${item.productName}</td>
         		<td id="pcatalog-${item.id}">${item.catalog}</td>
         		<td id="pprice-${item.id}">${item.price}</td>
         		<td id="pinventory-${item.id}">${item.inventory}</td>
         		<td id="psalesvolume-${item.id}">${item.salesVolume}</td>
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
		        <td style="display:none" id="pshowway-${item.id}">${item.showWay}</td>
		        <td style="display:none" id="pstandard-${item.id}">${item.standard}</td>
		        <td style="display:none" id="pmarketprice-${item.id}">${item.marketPrice}</td>
		        <td style="display:none" id="plabel-${item.id}">${item.label}</td>
		        <td style="display:none" id="pcoverburl-${item.id}">${item.coverBUrl}</td>
		        <td style="display:none" id="psequence-${item.id}">${item.sequence}</td>
		        <td style="display:none" id="pstatus-${item.id}">${item.status}</td>
		        <td style="display:none" id="pdescription-${item.id}">${item.description}</td>
		        <td style="display:none" id="porigin-${item.id}">${item.origin}</td>
		        <td style="display:none" id="plikes-${item.id}">${item.likes}</td>
		        <td style="display:none" id="psubdetailurl-${item.id}">${item.subdetailUrl}</td>
		        <td style="display:none" id="pdetailurl-${item.id}">${item.detailUrl}</td>
		        <td style="display:none" id="pshowstatus-${item.id}">${item.isShow}</td>
		        <td style="display:none" id="pcityid-${item.id}">${item.cityId}</td>
		        <td style="display:none" id="pareaid-${item.id}">${item.areaId}</td>
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
				copyHandle_Id = 0;
			}
		}
	});
})
</script>
<!-- 编辑产品 -->
 <script>
     $(document).ready(function(){
    	var editHandle_Id = 0;//要编辑的商品id
    	var productNameO = "";
    	var originO = "";
    	var catalogO = "";
    	var labelO = "";
    	var standardO = "";
    	var marketPriceO = "";
    	var priceO = "";
    	var showWayO = "";
    	var coverSUrlO = "";
    	var coverBUrlO = "";
    	var inventoryO = 0;
    	var sequenceO = 0;
    	var statusO = "";
    	var descriptionO = "";
    	var salesVolumeO = 0;
    	var likesO = 0;
    	var subdetailUrlO = "";
    	var detailUrlO = "";
    	var isShowO = "";
    	var areaIdO = 0;
    	var cityIdO = 0;
    	//弹出文本性提示框
     $(".editProduct").click(function(){
       $(".editproduct_pop_bg").fadeIn();
       //alert($(this).attr("id"));可以获取到当前被点击的按钮的id
       var clickedId = $(this).attr("id");
       editHandle_Id = clickedId.split("-")[1];
       //填充编辑框里的字段
       productNameO = $.trim($("#pproductname-"+editHandle_Id).text());
       originO = $.trim($("#porigin-"+editHandle_Id).text());
       catalogO = $.trim($("#pcatalog-"+editHandle_Id).text());
       labelO = $.trim($("#plabel-"+editHandle_Id).text());
       standardO = $.trim($("#pstandard-"+editHandle_Id).text());
       marketPriceO = $.trim($("#pmarketprice-"+editHandle_Id).text());
       priceO = $.trim($("#pprice-"+editHandle_Id).text());
       showWayO = $.trim($("#pshowway-"+editHandle_Id).text());
       coverSUrlO = $("#pcoversurl-"+editHandle_Id).attr("src").split("/")[3];
       coverBUrlO = $.trim($("#pcoverburl-"+editHandle_Id).text());
       inventoryO = $.trim($("#pinventory-"+editHandle_Id).text());
       sequenceO = $.trim($("#psequence-"+editHandle_Id).text());
       statusO = $.trim($("#pstatus-"+editHandle_Id).text());
       descriptionO = $.trim($("#pdescription-"+editHandle_Id).text());
       salesVolumeO = $.trim($("#psalesvolume-"+editHandle_Id).text());
       likesO = $.trim($("#plikes-"+editHandle_Id).text());
       subdetailUrlO = $.trim($("#psubdetailurl-"+editHandle_Id).text());
       detailUrlO = $.trim($("#pdetailurl-"+editHandle_Id).text());
       isShowO = $.trim($("#pshowstatus-"+editHandle_Id).text());
       areaIdO = $.trim($("#pareaid-"+editHandle_Id).text());
       cityIdO = $.trim($("#pcityid-"+editHandle_Id).text());

       $("#proname").val(productNameO);
       $("input[type=radio][value="+catalogO+"]").attr("checked",'checked');
       $("input[type=radio][value="+labelO+"]").attr("checked",'checked');
       $("input[type=radio][value="+isShowO+"]").attr("checked",'checked');
       $("input[type=radio][value="+showWayO+"]").attr("checked",'checked');
       $("#proorigin").val(originO);
       $("#prostandard").val(standardO);
       $("#promarketprice").val(marketPriceO);
       $("#proprice").val(priceO);
       $("#proinventory").val(inventoryO);
       $("#prosequence").val(sequenceO);
       $("#prodescription").val(descriptionO);

       $("#uploads").attr("src", "/static/upload/"+coverSUrlO);
       $("#serverImgNames").val(coverSUrlO);
       $("#uploadb").attr("src", "/static/upload/"+coverBUrlO);
       $("#serverImgNameb").val(coverBUrlO);
       if(subdetailUrlO!=""){
    	   $("#uploadd1").attr("src", "/static/upload/"+subdetailUrlO);
    	   $("#serverImgNamed1").val(subdetailUrlO);
       }
       $("#uploadd2").attr("src", "/static/upload/"+detailUrlO);
       $("#serverImgNamed2").val(detailUrlO);
       });
     //弹出：确认按钮
     $("#confirmEdit").click(function(){
    	 if(editHandle_Id==0){
    		 alert("请重新选择商品");
    		 return false;
    	 }
    	 	var productName = $.trim($("#proname").val());
		 	var origin = $.trim($("#proorigin").val());
		 	var catalog = $('input:radio[name=procatalog]:checked').val();
		 	var label = $('input:radio[name=prolabel]:checked').val();
		 	var standard = $.trim($("#prostandard").val());
		 	var price = $.trim($("#proprice").val());
		 	var marketPrice = $.trim($("#promarketprice").val());
		 	var inventory = $.trim($("#proinventory").val());//分库里必须有库存值，没有则为0
		 	var isShow = $('input:radio[name=proisshow]:checked').val();
		 	var showWay = $('input:radio[name=proshowway]:checked').val();
		 	var sequence = $.trim($("#prosequence").val());
		 	var description = $.trim($("#prodescription").val());
		 	
		 	var coverSUrl = $("#serverImgNames").val();
		 	var coverBUrl = $("#serverImgNameb").val();
		 	var subdetailUrl = $("#serverImgNamed1").val();
		 	var detailUrl = $("#serverImgNamed2").val();
		 	
		 	if(productName == "" || origin=="" || standard=="" || price=="" || marketPrice=="" || inventory== "" || sequence==""){
		 		alert("产品名称、原产地、规格、价格、库存、市场价以及排序不能为空");
		 		return false;
		 	}
		 	if(coverSUrl == "" || coverBUrl == "" || detailUrl==""){
		 		alert("商品正方形、长方形缩略图、详情2图片必须上传");
		 		return false;
		 	}
		 	if(productName == productNameO && origin==originO && standard==standardO && price==priceO && showWay==showWayO &&
		 			marketPrice==marketPriceO && inventory== inventoryO && sequence==sequenceO && label==labelO &&
		 			catalog==catalogO && isShow==isShowO && description==descriptionO && coverSUrl==coverSUrlO
		 			&& coverBUrl==coverBUrlO && subdetailUrl==subdetailUrlO && detailUrl==detailUrlO){
		 		alert("并未做修改");
		 		return false;
		 	}
    	 
    		$.ajax({
       		  type: "POST",
     	          contentType: "application/json",
     	          url: "/product/vedit/"+$("#loginUserId").val(),
     	          dataType: "json",
     	          data: JSON.stringify({"id":editHandle_Id,"productName":productName,"origin":origin,"catalog":catalog,"label":label,"standard":standard,"price":price,
	  	        		"marketPrice":marketPrice,"inventory":inventory,"isShow":isShow,"showWay":showWay,"sequence":sequence,"description":description,
	  	        		"coverSUrl":coverSUrl,"coverBUrl":coverBUrl,"subdetailUrl":subdetailUrl,"detailUrl":detailUrl}),
     	          success: function(data){
     	        	  if(data.msg=="200"){
     	        		  alert("商品修改成功");
     	        		  window.location.reload();
     	        	  }
     	          }
       	 	});     	
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
       });
     //弹出：取消或关闭按钮
     $("#cancelEdit").click(function(){
       $(".editproduct_pop_bg").fadeOut();
       	 editHandle_Id=0;
       });
     });
     </script>
     <section class="editproduct_pop_bg">
      <div class="pop_cont">
       <!--title-->
       <h3>商品管理——编辑</h3>
       <!--content-->
       <div class="pop_cont_input" style="overflow: scroll;width:500px;height:450px;padding:0px">
       <!--以pop_cont_text分界-->
         <div class="pop_cont_text" style="padding:0px">
             <section>
		      <ul class="ulColumn2">
		       <li>
		        <span class="item_name" style="width:120px;">商品名称：</span>
		        <input type="text" id="proname" class="textbox textbox_295" placeholder="如'海南小番茄'"/>
		       </li>
		        <li>
		        <span class="item_name" style="width:120px;">原产地：</span>
		        <input type="text" id="proorigin" class="textbox textbox_295" placeholder="如'海南'"/>
		       </li>
		       <li>
		        <span class="item_name" id="catalogs" style="width:120px;">商品分类：</span>
		         <c:forEach var="item" items="${catalogs}" varStatus="status">
		         	<label class="single_selection"><input type="radio" name="procatalog" value="${item.name}"/>${item.name}</label>
		         </c:forEach>
		         <a href="/product/v_catalog/${id}?token=${token}">添加分类</a>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">商品标签：</span>
		        <label class="single_selection"><input type="radio" name="prolabel" value="无标签" checked="checked"/>无标签</label>
		        <label class="single_selection"><input type="radio" name="prolabel" value="绿色食品"/>绿色食品</label>
		        <label class="single_selection"><input type="radio" name="prolabel" value="小产区"/>小产区</label>
		        <label class="single_selection"><input type="radio" name="prolabel" value="新人福利"/>新人福利</label>
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
		        <label class="single_selection"><input type="radio" name="proisshow" value="yes" checked="checked"/>上架</label>
		        <label class="single_selection"><input type="radio" name="proisshow" value="no"/>下架</label>
		       </li>
		       <li>
		        <span class="item_name" style="width:120px;">展现形式：</span>
		        <label class="single_selection"><input type="radio" name="proshowway" value="s" checked="checked"/>正方形</label>
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
		        <span><img alt="正方形" id="uploads" src="" style="height:100px;width:100px;cursor:pointer"/></span>
		        <span><img alt="长方形" id="uploadb" src="" style="height:100px;width:200px;cursor:pointer"/></span>
				<div id="fileDivs">
				     <input id="fileToUploads" style="display: none" type="file" name="upfiles">
				</div>
				<input type="hidden" id="serverImgNames"/>
				<div id="fileDivb">
				     <input id="fileToUploadb" style="display: none" type="file" name="upfileb">
				</div>
				<input type="hidden" id="serverImgNameb"/>
		       </li>
							<li><span class="item_name" style="width: 120px;">商品详情1：</span>
								<img alt="详情1" id="uploadd1" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd1">
									<input id="fileToUploadd1" style="display: none" type="file"
										name="upfiled1">
								</div> <input type="hidden" id="serverImgNamed1" />
							</li>
							<li>
								<span class="item_name" style="width: 120px;">商品详情2：</span>
								<img alt="详情2" id="uploadd2" src=""
								style="height: 100px; width: 305px; cursor: pointer">
								<div id="fileDivd2">
									<input id="fileToUploadd2" style="display: none" type="file"
										name="upfiled2">
								</div> <input type="hidden" id="serverImgNamed2" />
							</li>
				<!-- <li>
		        <span class="item_name" style="width:120px;"></span>
		        <input type="button" id="prosave" value="保存" class="link_btn"/>
		       </li> -->
		      </ul>
		     </section>
         </div>
         <!--bottom:operate->button-->
         <div class="btm_btn">
          <input type="button" value="确认" id="confirmEdit" class="input_btn trueBtn"/>
          <input type="button" value="取消" id="cancelEdit" class="input_btn falseBtn"/>
         </div>
        </div>
       </div>
     </section>
<!-- 编辑产品 -->

<!-- 添加s缩略图 -->
    <script>
     $(document).ready(function(){     
     $("#uploads").on('click', function() {  
         $('#fileToUploads').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivs').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgs',  
             secureuri:false,  
             fileElementId:'fileToUploads',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 //把图片替换  
                 /* var obj = jQuery.parseJSON(data);  
                 $("#upload").attr("src", "../image/"+obj.fileName);   */
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNames").val(data.imgName);
                     $("#uploads").attr("src", "/static/upload/"+data.imgName);
                 }
                 else if(data.msg=="201"){
                	 alert("图片不符合");
                 }
                 if(typeof(data.error) != 'undefined') {  
                     if(data.error != '') {  
                         alert(data.error);  
                     } else {  
                         alert(data.msg);  
                     }  
                 }
              }, 
             error: function (data, status, e) {  
                 alert(e);  
             }  
         });  
     });
     });
   </script>
   <!-- 添加s缩略图 -->
   
   <!-- 添加b缩略图 -->
    <script>
     $(document).ready(function(){     
     $("#uploadb").on('click', function() {  
         $('#fileToUploadb').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivb').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgb',  
             secureuri:false,  
             fileElementId:'fileToUploadb',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNameb").val(data.imgName);
                     $("#uploadb").attr("src", "/static/upload/"+data.imgName);
                 }
                 else if(data.msg=="201"){
                	 alert("图片不符合");
                 }
                 if(typeof(data.error) != 'undefined') {  
                     if(data.error != '') {  
                         alert(data.error);  
                     } else {  
                         alert(data.msg);  
                     }  
                 }
              }, 
             error: function (data, status, e) {  
                 alert(e);  
             }  
         });  
     });
     });
   </script>
   <!-- 添加b缩略图 -->
   <!-- 添加详情1图 -->
    <script>
     $(document).ready(function(){     
     $("#uploadd1").on('click', function() {  
         $('#fileToUploadd1').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivd1').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgd1',  
             secureuri:false,  
             fileElementId:'fileToUploadd1',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNamed1").val(data.imgName);
                     $("#uploadd1").attr("src", "/static/upload/"+data.imgName);
                 }
                 else if(data.msg=="201"){
                	 alert("图片不符合");
                 }
                 if(typeof(data.error) != 'undefined') {  
                     if(data.error != '') {  
                         alert(data.error);  
                     } else {  
                         alert(data.msg);  
                     }  
                 }
              }, 
             error: function (data, status, e) {  
                 alert(e);  
             }  
         });  
     });
     });
   </script>
   <!-- 添加详情1图 -->
      <!-- 添加详情2图 -->
    <script>
     $(document).ready(function(){ 
     $("#uploadd2").on('click', function() {  
         $('#fileToUploadd2').click();  
     });
     //这里必须绑定到file的父元素上，否则change事件只会触发一次，即在页面不刷新的情况下，只能上传一次图片，原因http://blog.csdn.net/wc0077/article/details/42065193
     $('#fileDivd2').on('change',function() {  
         $.ajaxFileUpload({  
             url:'/product/uploadImgd2',  
             secureuri:false,  
             fileElementId:'fileToUploadd2',//file标签的id  
             dataType: 'json',//返回数据的类型  
             //data:{name:'logan'},//一同上传的数据  
             success: function (data, status) {  
                 if(data.msg=="200"){
                     //alert("图片可用");
                     $("#serverImgNamed2").val(data.imgName);
                     $("#uploadd2").attr("src", "/static/upload/"+data.imgName);
                 }
                 else if(data.msg=="201"){
                	 alert("图片不符合");
                 }
                 if(typeof(data.error) != 'undefined') {  
                     if(data.error != '') {  
                         alert(data.error);  
                     } else {  
                         alert(data.msg);  
                     }  
                 }
              }, 
             error: function (data, status, e) {  
                 alert(e);  
             }  
         });  
     });
     });
   </script>
   <!-- 添加详情2图 -->
   
<input type="hidden" id="loginUserId" value="${id}"></input>
</body>
</html>
