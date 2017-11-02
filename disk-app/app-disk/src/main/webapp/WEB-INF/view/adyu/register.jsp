<!DOCTYPE HTML>
<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->

<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_zh.js?v=20161122"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_en.js?v=20161122"></script>
</c:if>
<script>
$(function(){
    $(".chose_sex").find("span").click(function(){
        $(this).addClass("chosed").siblings().removeClass("chosed");
    });
    
  	//提示清楚
    $('.clear_tishi').live("focus",function(){
 	   $(".ts_txt").remove();
    })
    
    $("#submitRegister").click(function(){
    	var employeeName = $("#employeeName").val();
    	var employeeSex = $(".chosed").attr("value");
    	var employeeCode = $("#employeeCode").val(); 
    	var employeeMobile = $("#employeeMobile").val();
    	var adminId = $("#adminId").val();
    	var accountPwd = $("#accountPwd").val();
    	var confirmPwd = $("#confirmPwd").val();
    	if(employeeName==null || employeeName == ''){
    		var liStr = $(".reg_box ul li").eq(0);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_please_enter_your_name+"</span>");
			return;
    	}
    	if(employeeCode==null || employeeCode == ''){
    		var liStr = $(".reg_box ul li").eq(2);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_please_enter_your_employee_number+"</span>");
			return;
    	}
    	if(employeeMobile==null || employeeMobile == ''){
    		var liStr = $(".reg_box ul li").eq(2);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_please_enter_your_phone_number+"</span>");
			return;
    	}
    	if(accountPwd==null || accountPwd == ''){
    		var liStr = $(".reg_box ul li").eq(4);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_fill_new_pwd+"</span>");
			return;
    	}
    	if(confirmPwd==null || confirmPwd == ''){
    		var liStr = $(".reg_box ul li").eq(5);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_fill_new_pwd_again+"</span>");
			return;
    	}
    	if(accountPwd!=confirmPwd){
    		var liStr = $(".reg_box ul li").eq(5);
			liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+i18n_new_pwd_not_consistent+"</span>");
			return;
    	}
    	var data = {
   			employeeName   : employeeName,
   			employeeSex    : employeeSex,
   			employeeCode   : employeeCode,
   			employeeMobile : employeeMobile,
   			id			   : adminId,
   			accountPwd     : accountPwd,
    	};
    	$.ajax({
			url: basepath + "/adyu/register",
	        dataType:"json",
	        type: "POST",
	     	data: data,
	        async: false, 
	        success: function(data){
				if(data.code=="1000"){
					window.location.href=basepath + '/home/index';
				} else if(data.code == 2023) {
					var liStr = $(".reg_box ul li").eq(2);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2026) {
					var liStr = $(".reg_box ul li").eq(3);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2022) {
					var liStr = $(".reg_box ul li").eq(2);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2024) {
					var liStr = $(".reg_box ul li").eq(3);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2025) {
					var liStr = $(".reg_box ul li").eq(3);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2018) {
					var liStr = $(".reg_box ul li").eq(0);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
				} else if(data.code == 2015) {
					var liStr = $(".reg_box ul li").eq(4);
					liStr.append("<span class=\"ts_txt\"><i class=\"ts_tip\"></i>                 "+data.codeInfo+"</span>");
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
	        <div class="reg_logo"></div>
	        <div class="reg_box">
	        	<form action="">
	        		<input type="hidden" id="adminId" value="${admin.id }">
		            <ul>
		                <li><label for="" class="user_tip"></label><input id="employeeName" class="clear_tishi" type="text" placeholder="<fmt:message key="full_name" bundle="${i18n}"/>"></li>
		                <li class="chose_sex">
		                	<span class="chosed" value="0"><fmt:message key="sex_boy" bundle="${i18n}"/></span>
		                	<span class="chose" value="1"><fmt:message key="sex_girl" bundle="${i18n}"/></span>
		               	</li>
		                <li><label for="" class="num_tip"></label><input id="employeeCode" class="clear_tishi" type="text" placeholder="<fmt:message key="employee_number" bundle="${i18n}"/>"></li>
		                <li><label for="" class="tel_tip"></label><input id="employeeMobile" class="clear_tishi" type="text" placeholder="<fmt:message key="mobile_number" bundle="${i18n}"/>"></li>
		                <li><label for="" class="psw_tip"></label><input id="accountPwd" class="clear_tishi" type="text" placeholder="<fmt:message key="password" bundle="${i18n}"/>"></li>
		                <li><label for="" class="psw_tip"></label><input id="confirmPwd" class="clear_tishi" type="text" placeholder="<fmt:message key="confirm_password" bundle="${i18n}"/>"></li>
		                <li class="btnbox"><a id="submitRegister" href="javascript:void(0);"><fmt:message key="register" bundle="${i18n}"/></a></li>
		            </ul>
	            </form>
	        </div>
	    </div>
	</div>   
</body>
</html>