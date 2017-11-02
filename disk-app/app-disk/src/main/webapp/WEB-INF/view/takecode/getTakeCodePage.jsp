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
<script type="text/javascript" src="${ctx}/plugins/clipboard/clipboard.min.js?v=20161011"></script>
<script type="text/javascript" src="${staticpath}/js/common/browserUtils.js?v=2"></script>
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
<body>
<script type="text/javascript">
$(function(){
	var ieVersion = BrowserUtils.isLessThanOrEqualIEBrowerVersion("8.0");
	if(!ieVersion){
			//复制插件
			var clipboard = new Clipboard('#copy');
			clipboard.on('success', function(e) {
		        layer.msg(copy_success,{icon: 1,time:1000});
		        setTimeout(function(){
		        	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    		parent.layer.close(index); //再执行关闭 
		        },1000);
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
<div class="outlayer" id="con_copyadd">
    <div class="tqm_box"> 
        <p><label><fmt:message key="t_take_link" bundle="${i18n}"/></label><input id="codepath" class="input_style01" type="text" value="${takeCodeUrl }"></p>
    </div>
    <div class="layui-layer-btn" style="padding:10px 10px 12px">
	    <a href="javascript:void(0);" class="layui-layer-btn0" id="copy" data-clipboard-action="copy" data-clipboard-target="#codepath"><fmt:message key="copy" bundle="${i18n}"/></a>
	    <a id="cancel" class="layui-layer-btn1"><fmt:message key="cancel" bundle="${i18n}"/></a>
    </div>
</div>
</body>
</html>


