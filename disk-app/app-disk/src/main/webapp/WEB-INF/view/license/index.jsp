<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="webname" value="<%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.TITLE.getKey()) %>" />
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';
var editType = Array(".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".wps", ".et", ".dps", ".vsd");
</script>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>${webname }</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link href="${staticpath}/css/adyu.css" type="text/css" rel="stylesheet" />
<LINK rel="Bookmark" href="${ctx}/favicon.ico" >
<LINK rel="Shortcut Icon" href="${ctx}/favicon.ico" />
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${plugins}/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${staticpath}/js/common/common.js?v=2016"></script>
<script>
$(function(){
    
    $("#submitRegister").click(function(){
    	var serCode = $("#serCode").val();
    	var data = {
    			serCode     : serCode
    	};
    	$.ajax({
			url: basepath + "/license/toreg",
	        dataType:"json",
	        type: "POST",
	     	data: data,
	        async: false, 
	        success: function(data){
				if(checkErrorCode(data)){
	        		layer.msg('激活成功！', {
		    			icon : 1,
		    			time : 1500
		    		});
		        	setTimeout(function(){
		        		window.location.href = "${ctx}/login/index"
		        	}, 1500);
	        	}
	        }
 		});
    });
})
</script>
</head>
<body>
	<div class="reg_bg">
	    <div class="contain_reg">
	        <div class="reg_activation"></div>
	        <div class="reg_box" style="height: 100px">
	        	<form action="" method="post">
		            <ul>
		                <li><label for="" class="user_tip"></label><input id="serCode" class="clear_tishi" type="text" placeholder="激活码"></li>
		                <li class="btnbox"><a id="submitRegister" href="javascript:void(0);">激&nbsp;活</a></li>
		            </ul>
	            </form>
	        </div>
	    </div>
	</div>   
</body>
</html>