<!DOCTYPE HTML>
<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="staticpath" value="${ctx}/static" />
<html>
<head>
<script type="text/javascript">var basepath ='${ctx}';</script>
<link href="${staticpath}/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${staticpath}/js/common/all.js"></script>
<script type="text/javascript" src="${plugins}/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${plugins}/common/side/context-menu.js"></script>
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_zh.js?v=20161122"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_en.js?v=20161122"></script>
</c:if>
<style>
.labelku span i.label_chose{cursor:pointer;}
</style>
<script type="text/javascript">
	function regainFile(id){
		$.ajax({
			url: basepath+"/teamworkfile/regainFile?id="+id,
	        dataType:"json",
	        type: 'POST',
	        async: false, 
	        success: function(data){
		        if (data.code == 1000) {
		        	layer.msg(recovery_successful,{icon: 1,time:1500});
		        	setTimeout(function(){
		        		parent.location.reload();
		        	}, 1500);
		        }
	        }
		 });
	}
</script>
</head>
<body>
<div class="con_version outlayer">
    <p class="mb20 bold"><fmt:message key="current_version" bundle="${i18n}"/>:V${query.fileVersion/10}  </p>
    <table class="tabs_th">
        <tr>               
            <th width="40%"><fmt:message key="historical_version" bundle="${i18n}"/></th>
            <th width="30%"><fmt:message key="modified_time" bundle="${i18n}"/></th>
            <th><fmt:message key="operation" bundle="${i18n}"/></th>       
        </tr>
    </table>
    <div class="box_tabs">
        <table class="tabs upload_tab1">
        	<c:forEach items="${versions }" var="version">
        	    <tr>            
	                <td width="40%">V${version.fileVersion/10}</td>
	                <td width="30%"><fmt:formatDate value="${version.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	                <td><a href="javascript:regainFile('${version.id }');"><fmt:message key="recovery" bundle="${i18n}"/></a></td>
	            </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>