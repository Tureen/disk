<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link href="${staticpath}/css/workgroup.css?v=20170116" rel="stylesheet" type="text/css" />
<style type="text/css">
.is_add{
	background: #fbec88;
}
.ztree li a {
    margin: 0;
    padding: 1px 2px;
    cursor: pointer;
    height: 22px;
    line-height: 22px;
    color: #333;
    background-color: transparent;
    vertical-align: top;
    display: inline-block;
}
.ztree li a.curSelectedNode, .ztree li a.curSelectedNode_Edit, .ztree li a.tmpTargetNode_inner {
    background-color: #ffe6b0;
    color: #000;
    padding: 0 1px;
    border: 1px solid #ffb951;
    height: 22px;
}
.selectContent{
    height: 350px;
    border: 1px solid #e0dbdb;
    margin: 10px 15px;
}
.select_list {
    height: 325px;
}
.userRight {
    padding: 0px;
    width: 681px;
}
.tl-header, .select_list li {
    height: 30px;
    line-height: 30px;
}
</style>
<script>
$(function(){
    var i = 1;
    $(".add_div .column_id").each(function(){
    	$(this).text(i++);
    });
});
</script>
<form method="post" id="SubForm">
  <div class="userRight">
    <div class="selectContent">
      <div class="select_title">
      	<div class="column column_id">ID</div>
        <div class="column column_workgroup_name"><fmt:message key="w_workgroup_name" bundle="${i18n}" /></div>
        <div class="column column_workgroup_admin"><fmt:message key="w_group_administrator" bundle="${i18n}" /></div>
        <div class="column column_workgroup_application_time"><fmt:message key="w_application_time" bundle="${i18n}" /></div>
        <div class="column column_workgroup_approval_time"><fmt:message key="w_approval_time" bundle="${i18n}" /></div>
        <div class="column column_workgroup_valid_status"><fmt:message key="w_valid_status" bundle="${i18n}" /></div>
      </div>
      <input type="hidden" id="selectUserList" value="" />
      <ul class="select_list select_user_list add_div" id="selectUser">
      	<c:forEach items="${query.list }" var="workgroupApply">
	      	<li class="add_employee">
	      		<c:if test="${workgroupApply.workgroupName==null }">
	      			<div class="column column_id">${workgroupApply.id }</div>
	      			<div class="column column_workgroup_delete"><-&nbsp;&nbsp;<fmt:message key="w_workgroup_is_deleted" bundle="${i18n}" />&nbsp;&nbsp;-></div>
	      		</c:if>
	      		<c:if test="${workgroupApply.workgroupName!=null }">
	      			<div class="column column_id">${workgroupApply.id }</div>
		      		<div class="column column_workgroup_name" title="${workgroupApply.workgroupName }">${workgroupApply.workgroupName }&nbsp;</div>
			        <div class="column column_workgroup_admin" title="${workgroupApply.createEmployeeName }">${workgroupApply.createEmployeeName }&nbsp;</div>
			        <div class="column column_workgroup_application_time"><fmt:formatDate value="${workgroupApply.createTime }" pattern="yyyy-MM-dd HH:mm" />&nbsp;</div>
			        <div class="column column_workgroup_approval_time"><fmt:formatDate value="${workgroupApply.updateTime }" pattern="yyyy-MM-dd HH:mm" />&nbsp;</div>
			        <div class="column column_workgroup_valid_status">
			        	<c:forEach items="${enum }" var="workgroupStatus">
					    	<c:if test="${workgroupStatus.status==workgroupApply.applyStatus}"><span style="color: ${workgroupStatus.colorStyle}" code="${workgroupApply.applyStatus }"><fmt:message key="${workgroupStatus.desc}" bundle="${i18n}" /></span></c:if>
					    </c:forEach>
				    </div>
	      		</c:if>
	   		</li>
   		</c:forEach>
      </ul>
    </div>
  </div>
</form>