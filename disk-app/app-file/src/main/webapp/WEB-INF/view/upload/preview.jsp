<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html dir="ltr" mozdisallowselectionprint moznomarginboxes>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="google" content="notranslate">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${fileName }</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<c:set var="staticpath" value="${ctx}/static" />
	<script type="text/javascript">
		var basepath ='${ctx}';
		var fileUrl = '${ctx}${url}';
	</script>
	<!-- 引入pdfjs -->
    <link rel="stylesheet" href="${staticpath }/js/pdfjs-1.5.188/web/viewer.css"/>
    <script src="${staticpath }/js/pdfjs-1.5.188/web/compatibility.js"></script>
	<link rel="resource" type="application/l10n" href="${staticpath }/js/pdfjs-1.5.188/web/locale/locale.properties"/>
	<script src="${staticpath }/js/pdfjs-1.5.188/web/l10n.js"></script>
	<script src="${staticpath }/js/pdfjs-1.5.188/build/pdf.js"></script>
    <script src="${staticpath }/js/pdfjs-1.5.188/web/debugger.js"></script>
    <script src="${staticpath }/js/pdfjs-1.5.188/web/viewer.js"></script>
    <c:set var="localvar" value="<%=com.yunip.config.LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
    <script type="text/javascript">
    <c:if test="${localvar=='zh_CN' }">
    	PDFJS.locale = 'zh-CN';
	</c:if>
	<c:if test="${localvar=='en_US' }">
		PDFJS.locale = 'en-US';
	</c:if>
	</script>
  </head>
<%@ include file="/static/js/pdfjs-1.5.188/web/viewer.html"%>