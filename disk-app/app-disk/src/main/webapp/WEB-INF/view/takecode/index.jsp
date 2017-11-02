<%@page import="com.yunip.model.disk.TakeCode"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${ctx}/plugins/clipboard/clipboard.min.js?v=20161011"></script>
<script type="text/javascript" src="${staticpath}/js/takecode/pindex.js?v=20170103"></script>
<script type="text/javascript">
var fileServiceUrl = '${fileServiceUrl}';

function searchFile(){
	$("#queryForm").submit();
}

$(function(){
	
	$(".copyadd").click(function(){
		var id = $(this).attr("code");
		layer.open({
			type: 2,
			title :copy,
			area : ['800px', '380px'],
			shadeClose : false, //点击遮罩关闭
			content: basepath + '/takecode/getTakeCodePage?id='+id
		});
		$(".layui-layer-page").css("height", "189px");
	});
		
});


</script>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an">
        	<div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
            		<div class="crumbs">
            			<ul>
            			<li><i class="tip_m"></i></li>
            			<li><a href=""><fmt:message key="takecode_space" bundle="${i18n}"/></a></li>
            			</ul>
           			</div>
                    <div class="action_btn">
                    	<ul>
                    		<li class="remove_qx"><i class="ac_t10"></i><fmt:message key="t_remove" bundle="${i18n}"/></li>
                    	</ul>
                   	</div>
                    <%-- <div class="r_search_contain">
						<form action="${ctx}/takecode/index" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="文件名" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
	                </div> --%>
                 </div>
            </div>
            <div class="content content_an">
                
                <c:if test="${fn:length(query.list) == 0}">
	                <div id="noneContent" class="tab_contain noline mt10">
	                    <c:if test="${empty query.queryName}">
			               <p class="nofile"><img src="${staticpath}/images/notakecode.png" alt=""><br><span><i></i><fmt:message key="t_no_code" bundle="${i18n}"/></span></p>
			            </c:if>
			            <c:if test="${not empty query.queryName}">
			               <p class="nofile"><br><span><i><img src="${staticpath}/images/noselect.png" alt=""></i><fmt:message key="t_no_query" bundle="${i18n}"/></span></p>
			            </c:if>
	               </div>
                </c:if>
	            <div id="mainContent" <c:if test="${fn:length(query.list) > 0}">style="display: block"</c:if>
	            		<c:if test="${fn:length(query.list) == 0}">style="display: none"</c:if>>
	                <table class="tabs_th">
	                    <tr>
	                        <th width="5%"><i class="tab_check" id="allCheck" for=""><input class="none" type="checkbox"></i></th>
	                        <th width="20%"><fmt:message key="file_name" bundle="${i18n}"/></th>
	                        <th width="10%"><fmt:message key="extracted_code" bundle="${i18n}"/></th>
	                        <th width="10%"><fmt:message key="t_remain_download_num" bundle="${i18n}"/></th>
	                        <th width="15%"><fmt:message key="t_failure_time" bundle="${i18n}"/></th>
	                        <th width="15%"><fmt:message key="t_create_time" bundle="${i18n}"/></th>
	                        <th width="15%"><fmt:message key="t_remark" bundle="${i18n}"/></th>
	                        <th width="10%"><fmt:message key="operation" bundle="${i18n}"/></th>
	                    </tr>
	                </table>
	                <div class="tab_contain">
		                <table class="tabs" id="tabs">
		                	<c:forEach items="${query.list }" var="takecode" varStatus="status">
		                		<c:if test="${query.list[status.index-1].takeCode!=takecode.takeCode }">
				                    <tr>
				                        <td width="5%"><i class="tab_check" name="folderCheck" value="${takecode.fileId}" for=""><input class="none" type="checkbox" value="${takecode.id }"><input class="none" name="code" value="${takecode.takeCode }"></i></td>
				                        <td width="20%">
				                        	<p class="filebox">
				                        		<c:if test="${takecode.takeCode !=query.list[status.index+1].takeCode}">
					                        		<i class="tipbg  tips_bg_${takecode.fileType }"></i>
					                        		<span id="takecodename${takecode.id }" class="file_name <c:if test="${takecode.fileType == 1 }">imageFile</c:if><c:if test="${takecode.fileType > 1 }">previewFile</c:if>" fileId="${takecode.fileId}"><a class="remove_check" href="javascript:void(0);">${takecode.fileName }</a></span>
				                        		</c:if>
				                        		<c:if test="${takecode.takeCode ==query.list[status.index+1].takeCode}">
					                        		<i class="tipbg  tips_bg_complex"></i>
					                        		<span id="takecodename${takecode.id }"><a href="javascript:void(0);">${takecode.fileName }</a><fmt:message key="t_etc" bundle="${i18n}"/></span>
				                        		</c:if>
				                        	</p>
				                        </td>
				                        <td width="10%">${takecode.takeCode }</td>
				                        <td width="10%"><c:if test="${empty takecode.remainDownloadNum}"><fmt:message key="t_unlimited" bundle="${i18n}"/></c:if><c:if test="${not empty takecode.remainDownloadNum }">${takecode.remainDownloadNum }</c:if></td>
				                        <c:set var="nowday">
												<fmt:formatDate value="${takecode.effectiveTime}" pattern="yyyy" type="date"/>
										</c:set>
				                        <td width="15%">
				                        	<c:if test="${nowday >3000 }"><fmt:message key="t_timeless" bundle="${i18n}"/></c:if>
				                        	<c:if test="${nowday <3000 }">
			                        			<fmt:formatDate value="${takecode.effectiveTime}" pattern="yyyy-MM-dd HH:mm" />
			                        		</c:if>
			                        	</td>
				                        <td width="15%">
		                        			<fmt:formatDate value="${takecode.createTime}" pattern="yyyy-MM-dd HH:mm" />
			                        	</td>
				                        <td width="15%" title="${takecode.remark }"><c:if test="${fn:length(takecode.remark)>16}">${fn:substring(takecode.remark, 0, 16)}...</c:if><c:if test="${fn:length(takecode.remark)<=16}">${takecode.remark }</c:if></td>
										<td width="10%"><a href="javascript:void(0);" code="${takecode.id }" class="copyadd"><fmt:message key="copy" bundle="${i18n}"/></a></td>
				                    </tr>
			                   	</c:if>
		                    </c:forEach>
		                </table>
	                </div>
            </div>
            <div class="bottom_txt bottom_txt_an"><fmt:message key="selected" bundle="${i18n}"/><i id="folderCount">0</i><fmt:message key="t_code_num" bundle="${i18n}"/></div>
        </div>
    </div>
</div>
<!-- 删除 -->
<div class="outlayer con_remove_qx" style="display: none">
    <p class="color_7d mt20"><span class="color_31"  id="move"></span></p>
</div>
</body>
</html>