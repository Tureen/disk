<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${staticpath }/css/common.css?v=20160914" />
<!--引入JS-->
<script type="text/javascript" src="${staticpath }/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${staticpath}/js/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${staticpath }/js/common.js?v=20161010"></script>
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/i18n/i18n_zh.js?v=20161118"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/i18n/i18n_en.js?v=20161118"></script>
</c:if>
