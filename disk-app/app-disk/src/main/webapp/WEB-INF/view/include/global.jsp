<!DOCTYPE HTML>
<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="staticpath" value="${ctx}/static" />
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<c:set var="webname" value="<%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.TITLE.getKey()) %>" />
	<script type="text/javascript" src="${staticpath}/js/common/i18n_zh.js?v=20161122"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<c:set var="webname" value="<%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.TITLE_ENGLISH.getKey()) %>" />
	<script type="text/javascript" src="${staticpath}/js/common/i18n_en.js?v=20161122"></script>
</c:if>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>${webname }</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<LINK rel="Bookmark" href="${ctx}/favicon.ico" >
<LINK rel="Shortcut Icon" href="${ctx}/favicon.ico" />
<link href="${staticpath}/css/common.css?v=20170214" rel="stylesheet" type="text/css" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<script type="text/javascript" src="${staticpath}/js/common/fileType.js"></script>
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${staticpath}/js/common/all.js"></script>
<script type="text/javascript" src="${plugins}/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${staticpath}/js/common/messageEvent.min.js"></script>
<script type="text/javascript" src="${plugins}/common/side/context-menu.js"></script>
<script type="text/javascript" src="${staticpath}/js/common/common.js?v=20161212"></script>
<script type="text/javascript" src="${staticpath}/js/common/dateformat.js"></script>
<!-- <script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d64baea2a20c7e87f45862188ceac754";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script> -->

</head>