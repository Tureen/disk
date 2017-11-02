<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${staticpath}/js/takecode/index.js?v=20170103"></script>
<script type="text/javascript">
var fileServiceUrl = '${fileServiceUrl}';
function pickTakeCode(){
	$("#queryForm").submit();
}
</script>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<!-- 提示框 begin -->
<div class="ts_box" style="display: none">
    <i class="ts_tips_loading"></i><span id="showMsg"><fmt:message key="t_copy_on" bundle="${i18n}"/></span>
</div>
<div class="box_main"> 
	<input id="takecodeindex" type="hidden" value="<%=request.getParameter("takeCode")%>">
    <div class="right_part">
        <div class="main_con main_con_an" style="margin-left: 0px;"> 
            <div class="r_topcon r_topcon_an" style="left:0">
            <div class="actionbox cf">
                <div class="crumbs"><!-- <i class="tip_a"></i> -->
	                <ul>
	                	<li><i class="tip_m"></i></li>
	                	<li><a href="${ctx}/login/index"><fmt:message key="t_take_file" bundle="${i18n}"/></a></li>
	                	<li>
		                	<a  class="cutword" href="">&nbsp;&gt;&nbsp;<fmt:message key="t_takecode" bundle="${i18n}"/>：
		                		
		                		<%
								   String s = request.getParameter("takeCode");
								   if(s==null || "".equals(s))
								       s=" ";
								%>
								<%=s %>
		                	</a>
	                	</li>
	                </ul>
	                

              
               	</div>
               	
               	</div>
	                <div class="top_filetype">
	                	<c:if test="${fn:length(query.list)>1}">
	                		<p></p>
	                    	<p><i class="tips_file_type"></i>${query.list[0].fileName } <fmt:message key="t_etc" bundle="${i18n}"/></p>
		                    <p>
		                    	<span><fmt:message key="t_share" bundle="${i18n}"/>：${query.list[0].createAdmin }</span>
		                    	<span><fmt:message key="t_share_time" bundle="${i18n}"/>：<fmt:formatDate value="${query.list[0].createTime}" pattern="yyyy-MM-dd" /></span>
		                    	<span><fmt:message key="t_remain_download_num" bundle="${i18n}"/> : <c:if test="${empty query.list[0].remainDownloadNum }"><fmt:message key="t_unlimited" bundle="${i18n}"/></c:if><c:if test="${not empty query.list[0].remainDownloadNum }">${query.list[0].remainDownloadNum }</c:if> </span>
		                    	<span><fmt:message key="t_remark" bundle="${i18n}"/> : <span title="${query.list[0].remark }"><c:if test="${fn:length(query.list[0].remark)>8}">${fn:substring(query.list[0].remark, 0, 32)}...</c:if><c:if test="${fn:length(query.list[0].remark)<=32}">${query.list[0].remark }</c:if></span></span>
	                    	</p>
	                    </c:if>
	                    <c:if test="${fn:length(query.list)==1}">
		                    <p>
		                    	<i class="tipbg tips_bg_${query.list[0].fileType }"></i>
		                    	${query.list[0].fileName }
		                   	</p>
		                    <p>
		                    	<span><fmt:message key="t_share" bundle="${i18n}"/>：${query.list[0].createAdmin }</span>
		                    	<span><fmt:message key="t_share_time" bundle="${i18n}"/>：<fmt:formatDate value="${query.list[0].createTime}" pattern="yyyy-MM-dd" /></span>
		                    	<span><fmt:message key="t_remain_download_num" bundle="${i18n}"/> : <c:if test="${empty query.list[0].remainDownloadNum }"><fmt:message key="t_unlimited" bundle="${i18n}"/></c:if><c:if test="${not empty query.list[0].remainDownloadNum }">${query.list[0].remainDownloadNum }</c:if> </span>
		                    	<span><fmt:message key="t_remark" bundle="${i18n}"/> : <span title="${query.list[0].remark }"><c:if test="${fn:length(query.list[0].remark)>8}">${fn:substring(query.list[0].remark, 0, 32)}...</c:if><c:if test="${fn:length(query.list[0].remark)<=32}">${query.list[0].remark }</c:if></span></span>
	                    	</p>
	                   	</c:if>
	                </div>
	                <div class="tq_btnbox">
	                <div class="tq_divbox"><a class="" href="javascript:void(0);" style="width: 88px;margin:5px 0 0 20px;"><i></i><fmt:message key="download" bundle="${i18n}"/></a></div>
	                <form action="${ctx}/takecode/pick" method="post" id="queryForm">
                            <div class="tq_search">
                            <input type="text" placeholder="<fmt:message key="take_code_prompt" bundle="${i18n}"/>" id="queryName" name="takeCode" value="${query.queryName}"/>
							<a class="magnifier" onclick="pickTakeCode()" href="javascript:void(0);"></a></div>
                         </form>
	                
	                </div>
                         
            </div>
            <div class="content content_an" style="left:15px;top:190px;">
           		<c:if test="${fn:length(query.list) == 0}">
	                <div id="noneContent" class="tab_contain noline mt10">
	                    <c:if test="${empty query.queryName}">
			               <p class="nofile"><img src="${staticpath}/images/nofile.png" alt=""><br><span><i></i><fmt:message key="t_no_file" bundle="${i18n}"/></span></p>
			            </c:if>
			            <c:if test="${not empty query.queryName}">
			               <p class="nofile"><br><span><i><img src="${staticpath}/images/noselect.png" alt=""></i><fmt:message key="t_no_query_file" bundle="${i18n}"/></span></p>
			            </c:if>
	               </div>
                </c:if>
                <div id="mainContent" <c:if test="${fn:length(query.list) > 0}">style="display: block"</c:if>
            			<c:if test="${fn:length(query.list) == 0}">style="display: none"</c:if>>
                	<!-- 表格行 -->
                    <table class="tabs_th">
                         <tr> 
                            <th width="5%"><i id="allCheck" class="tab_check tab_checked" for=""><input class="none" type="checkbox"></i></th>
                            <th width="55%"><fmt:message key="file_name" bundle="${i18n}"/></th>
                            <th width="20%"><fmt:message key="size" bundle="${i18n}"/></th>
                            <th width="20%"><fmt:message key="update_time" bundle="${i18n}"/><span  id="sort" class="date_tip_up"></span></th>
                        </tr>
                    </table>
                    <div class="tab_contain">
                        <!-- 表格 -->
                        <table class="tabs" id="tabs" >
                        	<c:forEach items="${query.list }" var="takecode">
	                            <tr class="trfile">
	                                <td width="5%"><i class="tab_check tab_checked" name="fileCheck" value="${takecode.fileId}" for=""><input class="none" type="checkbox" value="${takecode.fileId }"></i></td>
	                                <td width="55%"><p class="filebox"><i class="tipbg tips_bg_${takecode.fileType }"></i><span class="file_name <c:if test="${takecode.fileType == 1 }">imageFile</c:if><c:if test="${takecode.fileType > 1 }">previewFile</c:if>" fileId="${takecode.fileId}"><a class="remove_check" href="javascript:void(0);">${takecode.fileName }</a></span></p></td>
	                                <td width="20%">${takecode.showFileSize }</td>
	                                <td width="20%"><fmt:formatDate value="${takecode.fUpdateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	                            </tr>
                            </c:forEach>
                        </table>
	                </div>   
	            </div>
	            <div class="bottom_txt bottom_txt_an" style="left:0px;"><fmt:message key="selected" bundle="${i18n}"/><i id="fileCount">${fn:length(query.list)}</i><fmt:message key="files" bundle="${i18n}"/></div>
	        </div>
    	</div>
	</div>
</div>
</body>
</html>