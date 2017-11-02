<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<link href="${staticpath}/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${plugins}/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${plugins}/common/side/context-menu.js"></script>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/plugins/clipboard/clipboard.min.js?v=20161011"></script>
<script type="text/javascript" src="${staticpath}/js/common/browserUtils.js"></script>
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_zh.js?v=20161122"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_en.js?v=20161122"></script>
</c:if>
</head>
<style>
.labelku span i.label_chose{cursor:pointer;}
</style>
<body>
<script type="text/javascript">
$(function(){
	var urlArr = window.location.href.split('/');
	$("#address").val("http://"+urlArr[1]+urlArr[2]+basepath+"/takecode/pick");
	//$("#codepath").val("http://"+urlArr[1]+urlArr[2]+basepath+"/takecode/pick?takeCode="+$("#takecodestr").val());
	
	//构建复制文本
	var text_copy = $(".textarea1 p:eq(0)").html() + "\r" 
		+ $(".textarea1 p:eq(1)").html() + "\r"
		+ $(".textarea1 p:eq(2)").html() + "\r" 
		+ $(".textarea1 p:eq(3)").find("span").html() + $(".textarea1 p:eq(3)").find("input").val() + "\r" 
		+ $(".textarea1 p:eq(4)").find("span:eq(0)").html() + $(".textarea1 p:eq(4)").find("span:eq(1)").attr("title");
	
	$("#copy").attr("data-clipboard-text",text_copy);
	
	var ieVersion = BrowserUtils.isLessThanOrEqualIEBrowerVersion("8.0");
	if(!ieVersion){
			//复制插件
			var clipboard = new Clipboard('#copy');
			clipboard.on('success', function(e) {
				console.info('Action:', e);
		        layer.msg(copy_success,{icon: 1,time:1000});
		        /* setTimeout(function(){
		        	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    		parent.layer.close(index); //再执行关闭 
		        },1000); */
		    });
	}
	
	
	$("#copy").click(function(){
		if(ieVersion){
			$("#codepath").focus();
			$("#codepath").select();
			layer.msg(i18n_copy_limit,{icon: 5,time:2000});
			return;
		}
	});
	
	$("#cancel").click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭 
	});
});
</script>
<!-- 设置提取码 02   2016-6-14 -->
<div class="outlayer con_tiqu" id="con_settqm2">
	<input id="takecodestr" type="hidden" value="${takecode.takeCode }">
    
    <div class="set_tqmbg" style="height: 200px">
    	<p class="color_7d"><fmt:message key="generate_link_prompt" bundle="${i18n}"/></p>
       <div class="creat_tqm"><i></i><span><fmt:message key="t_create_success" bundle="${i18n}"/></span></div>
       <div  class="textarea1" style=" overflow-y: hidden;">
              <p><fmt:message key="t_end_time" bundle="${i18n}"/>：<fmt:formatDate value="${takecode.createTime}" pattern="yyyy-MM-dd" /> <fmt:message key="t_to" bundle="${i18n}"/> <fmt:formatDate value="${takecode.effectiveTime}" pattern="yyyy-MM-dd" /></p>  
              <p><fmt:message key="t_remain_download_num" bundle="${i18n}"/>：<c:if test="${empty takecode.remainDownloadNum }"><fmt:message key="t_unlimited" bundle="${i18n}"/></c:if><c:if test="${not empty takecode.remainDownloadNum }">${takecode.remainDownloadNum }</c:if></p>
              <p><fmt:message key="t_takecode" bundle="${i18n}"/>：${takecode.takeCode } </p>
              <p><span><fmt:message key="copy_address" bundle="${i18n}"/>：</span><input id="address" style="width: 80%;color: #b5b5b5;" type="text" readonly="readonly"></p>
              <p><span><fmt:message key="t_remark" bundle="${i18n}"/>：</span><span title="${takecode.remark }"><c:if test="${fn:length(takecode.remark)>32}">${fn:substring(takecode.remark, 0, 32)}...</c:if><c:if test="${fn:length(takecode.remark)<=32}">${takecode.remark }</c:if></span></p>
         </div>
    </div>
    
</div>
<div class="layui-layer-btn" style="padding:10px 10px 12px;">
	    <a href="javascript:void(0);" class="layui-layer-btn0" id="copy" 
	   			data-clipboard-text="">
	    	<fmt:message key="copy_text" bundle="${i18n}"/>
	    </a>
	    <a id="cancel" class="layui-layer-btn1"><fmt:message key="cancel" bundle="${i18n}"/></a>
    </div>
</body>
</html>


