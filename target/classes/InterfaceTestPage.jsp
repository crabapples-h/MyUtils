<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<% 
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	/* System.out.println(basePath); */

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接口测试页面</title>
<style type="text/css">
		*{
			margin: 0;
			padding: 0;
		}
		.head{
			width: 100%;
			height: 5rem;
			background-color: red;
		}
		.left{
			width: 20%;
			float: left;
			height: 10rem;
			margin-top: 1rem;
			overflow-y:scroll;
		}
		.right{
			width: 75%;
			height: 20rem;
			float: left;
			margin-top: 1rem;
			margin-left: 1rem;
		}
		.down{
			width:79.5%; 
			height: 40%;
			/* background-color: pink; */
			/* border: 1px dotted red; */
			/* border: 1px dashed red; */
			position:absolute ; 
			bottom: 10%;
			left:20%;
		}
		.down_inner{
			width:100%; 
			height: 90%;
			border: 1px dotted red;
			overflow-y:scroll;
		}
		#cl_date{
			width:6rem;
			height: 3rem;
			background-color: pink;
		}
		.but{
			width: 100%;
			height: 2rem;
			display: block;
			margin-top: 1rem;
		}
		table{
			border-collapse:collapse;
			width: 100%;
			border: 1px solid #000;
		}
		.table_td{
			border: 1px solid #000;
			line-height: 1.5rem;
			text-align: center;
			width: 50%;
		}
		.hid{
			display: none;
		}
	</style>
	<script src="static/js/jquery.min.js"></script>
	<script>
		function getTable(e) {
			var but_id = $(e).attr('id');
			$('.data_table').each(function () {
				$(this).addClass('hid');
				if($(this).attr('id')==but_id){
					$(this).removeClass('hid');
				}
			});
		}
	</script>
</head>
<body>
	<div>
		<div class="head"></div>
		<div class="left">
			<c:forEach items="${dataInterfaces}" var="model" varStatus="status">
				<input type="button" value="${status.index }、${model.text}" class="but" id="${status.index }" onclick="getTable(this)">
			</c:forEach>
		</div>
		<div class="right">
			<c:forEach items="${dataInterfaces}" var="model" varStatus="status">
				<form action="${model.address}" enctype="multipart/form-data" method="post" id="f_${status.index}">
					<c:if test="${status.index eq 0}">
						<c:set value="data_table" var="clazz"></c:set>
					</c:if>
					<c:if test="${status.index ne 0}">
						<c:set value="hid data_table" var="clazz"></c:set>
					</c:if>
					<table id="${status.index }" class="${clazz}">
					<tr>
						<td class="table_td">接口地址</td>
						<td class="table_td">/${model.address}</td>
					</tr>
					<tr>
						<td class="table_td">接口作用</td>
						<td class="table_td">${model.text}</td>
					</tr>
					<tr>
						<td class="table_td">请求方式</td>
						<td class="table_td">${model.method}</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">参数列表</td>
					</tr>
					<c:forEach items="${model.params}" var="model_">
							<tr>
								<td class="table_td">${model_.name}</td>
								<td class="table_td"><input name="${model_.value}" class="data" type="${model_.type}" value="${model_.defaultValue}"></td>
							</tr>
					</c:forEach>
						<tr>
							<td class="table_td">
								<input type="button" value="Object" onclick="postData(this);" style="height: 2rem;width: 6rem;">
								<input type="button" value="FormData" onclick="postFormData(this);" style="height: 2rem;width: 6rem;">
								<input type="button" value="稳定Object" onclick="postData2(this);" style="height: 2rem;width: 6rem;">
							</td>
							<td class="table_td">
								<input type="submit" value="直接提交" style="height: 2rem;width: 6rem;"/>
								<input type="reset" value="重置" style="height: 2rem;width: 6rem;">
							</td>
						</tr>
				</table>
				</form>
			</c:forEach>
		</div>
		
		<div class="down">
			<input type="button" value="清屏" id="cl_date"/>
			<div id="rDate" class="down_inner"></div>
		</div>
	</div>
	<script>
	function getHref(e) {
		var href = '<%=basePath%>'+$($($($($($(e).parent()[0]).parent()[0]).parent()[0]).parent()[0]).parent()[0]).attr('action');
		return href;
	}
	
	function getObj(e) {
		var data = $($($($($($(e).parent()[0]).parent()[0]).parent()[0]).parent()[0]).parent()[0]);
		var obj = new Object();
			$($($($(data).children()[0]).children()[0]).children()).each(function () {
				$(this).each(function() {
					$($(this).children()[1]).each(function() {
						if($(this).children()[0]){
							$(this).each(function() {
								if($($(this).children()[0]).attr('name')){
									$(obj).attr($($(this).children()[0]).attr('name'),$($(this).children()[0]).val());
								}
							});
						}
					});
				});
			});
		return obj;
	}
	
	function getFiles(e) {
		var data = $($($($($($(e).parent()[0]).parent()[0]).parent()[0]).parent()[0]).parent()[0]);
		var files = new Array();
			$($($($(data).children()[0]).children()[0]).children()).each(function () {
				$(this).each(function() {
					$($(this).children()[1]).each(function() {
						if($(this).children()[0]){
							$(this).each(function() {
								if($($(this).children()[0]).attr('name')){
									if($($(this).children()[0]).attr('type')=='file'){
										files.push($(this).children()[0].files[0]);
									}
								}
							});
						}
					});
				});
			});
		return files;
	}
	</script>
	
	<script>
		$('#cl_date').click(function () {
			$('#rDate').text("");
		});
	</script>
	
<script>
	function postFormData(e) {
		var obj = new Object();
		var files = new Array();
		var href = getHref(e);
		var fd = $($($($($($(e).parent()[0]).parent()[0]).parent()[0]).parent()[0]).parent()[0]);
		fd = $(fd).attr('id');
		var da = new FormData(document.getElementById(fd));
		sendFormData(href,da);
	}
	function sendFormData(url,da) {
		$.ajax({
			url:url,
			type:'POST',
			data:da,
			processData : false,
			contentType : false, 
			dataType:'JSON',
			success:function(re){
				console.log(re);
				$('#rDate').text("");
				$('#rDate').text(JSON.stringify(re));
			},
			error:function(err){
				console.log(err);
			}
		}); 
	}
</script>
	
	<script>
			function postData(e) {
				var obj = new Object();
				var files = new Array();
				var href = getHref(e);
				var obj = getObj(e);
				var files = getFiles(e);
				
				/* $(obj).attr('img',files[0]); */ 
				var fil = files[0];
				$(obj).attr('img',fil); 
				console.log(obj);
				console.log(files);
				send(href,obj);
			}
			
			function postData2(e) {
				var obj = new Object();
				var href = getHref(e);
				var obj = getObj(e);
				
				console.log(obj);
				send2(href,obj);
			}
	</script>
	
	<script>
	function send(url,da) {
		$.ajax({
			url:url,
			type:'POST',
			data:da,
			processData : false,
			contentType : false,
			/* contentType : 'multipart/form-data;', */
			dataType:'JSON',
			success:function(re){
				console.log(re);
				$('#rDate').text("");
				$('#rDate').text(JSON.stringify(re));
			},
			error:function(err){
				console.log(err);
			}
		}); 
	}
		/* 	crossDomain: true,*/
	function send2(url,da) {
		$.ajax({
			url:url,
			type:'POST',
			data:da,
			/* processData : false,
			contentType : false,  */
			dataType:'JSON',
			success:function(re){
				console.log(re);
				$('#rDate').text("");
				$('#rDate').text(JSON.stringify(re));
			},
			error:function(err){
				console.log(err);
			}
		}); 
	}
	</script>
	<script>
		$(function () {
			var hei = $(window).height()-$('.head').height()-30+'px';
			$('.left').css('height',hei);
		})
	</script>
	
	<!-- <script>
		var formData = new FormData($("#passengerForm")[0]);
		$.ajax({  
            url: "${basePath}/order/importPassengerExcel.json" ,  
            type: 'POST',  
            data: formData,  
            async: false,  
            cache: false,  
            contentType: false,  
            processData: false,  
            success: function (data) {  
                if(data.messageFlag.flag=="1"){
                 
                 layer.msg("导入成功",{time:2000});
            }else{
                    layer.msg("导入错误",{time:2000});
                }
            },  
            error: function (data) {  
                layer.msg("导入错误",{time:2000});
            }  
       });
	</script>
	<script>
	$(document).ready(function(){
		var u='https://yeah.qq.com/s.html?q=104998';
		$.ajax({
			url:u,
			crossDomain: true,
			dataType:'json',
			processData: false, 
			type:'get',
			success:function(data){
				},
			error:function(err) {
				}
			});
		});
	</script> -->
</body>
</html>