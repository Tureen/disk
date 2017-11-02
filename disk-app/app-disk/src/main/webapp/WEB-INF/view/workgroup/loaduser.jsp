<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link href="${staticpath}/css/workgroup.css?v=20170116" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${plugins}/ztree/zTreeStyle/zTreeStyle.css?v=20170209" type="text/css">
<script type="text/javascript" src="${plugins}/ztree/jquery.ztree.core.js"></script> 
<script type="text/javascript" src="${staticpath}/js/workgroup/loaduser.js?v=20170114"></script>
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
</style>
<script type="text/javascript">
var zNodes = [];
<c:forEach items="${departmentList}" var="dept">
 var data = { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", <c:if test="${dept.id=='00'}">open: true</c:if><c:if test="${dept.id!='00'}">open: false</c:if> };
 zNodes.push(data);
</c:forEach>
</script>
<form method="post" id="SubForm">
<div class="userLeft_main">
	<div class="userLeft">
		<input id="workgroupId" type="hidden" value="${workgroupId }">
		<input id="employeeAddObjs" type="hidden" value="${employeeAddObjs }">
		<input type="hidden" name="deptId" value="" id="deptId">
	    <ul id="treeDemo" class="ztree"></ul>
	    <!-- 左侧目录树  -->
	    <input type="hidden" id="new_departmentId" value="0"/>
  </div>
</div>
  <div class="userRight">
    <div class="selectContent selectContent2">
      <h2><fmt:message key="w_employee_list" bundle="${i18n}"/></h2>
      <div class="select_bar">
        <input type="hidden" id="userKey"/>
        <input class="text" type="text" name="employeeName" placeholder="<fmt:message key="w_employee_name" bundle="${i18n}"/>" id="userKey1"/>
        <!-- <div class="float"><span class="float mr10">查找范围:</span>
          <div class="dropdown">
            <input type="hidden" value="0" id="searchScope"/>
            <input class="text select" type="type" value="用户列表" readonly />
            <ul class="submenu">
              <li value="0"><a href="javascript:void(0);">用户列表</a></li>
              <li value="1"><a href="javascript:void(0);">我收藏的用户</a></li>
              <li value="2"><a href="javascript:void(0);">最近添加过的用户</a></li>
            </ul>
          </div>
        </div> -->
        <input id="search" class="button submit" type="button" value="<fmt:message key="search" bundle="${i18n}"/>"/>
      </div>
      <div class="select_title">
        <div class="column column_id">ID</div>
        <div class="column column_name"><fmt:message key="w_name" bundle="${i18n}"/></div>
        <div class="column column_department"><fmt:message key="w_dept" bundle="${i18n}"/></div>
        <div class="column column_email"><fmt:message key="w_email" bundle="${i18n}"/></div>
      </div>
     <ul class="select_list user_list show_div" id="Setuserlist">
     	<%-- <c:forEach items="${query.list }" var="employee">
	     	<li code="${employee.id }" class="show_employee">
	     		<div class="column column_id">${employee.id }&nbsp;</div>
	     		<div class="column column_name">${employee.employeeName }&nbsp;</div>
	     		<div class="column column_department" title="综合管理部">${employee.deptName }&nbsp;</div>
	     		<div class="column column_email">${employee.employeeEmail }&nbsp;</div>
	   		</li>
   		</c:forEach> --%>
     </ul>
      <div class="select_page">
        <div class="float">
          <select id="new_pageSize" name="pageSize">
            <option>10</option>
            <option>20</option>
            <option>30</option>
            <option>40</option>
            <option>50</option>
          </select>
          <a id="page_frist" class="page_frist disabled" href="javascript:void(0);"><fmt:message key="w_first" bundle="${i18n}"/></a>
          <a id="page_prev" class="page_prev disabled" href="javascript:void(0);"><fmt:message key="w_previous" bundle="${i18n}"/></a>
          <div class="input_text"> <span><fmt:message key="w_numero" bundle="${i18n}"/></span>
            <input name="pageIndex" class="text" type="text" id="new_page" value="1" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
            <span><fmt:message key="w_altogether" bundle="${i18n}"/><span id="new_totalpage"></span><fmt:message key="w_page" bundle="${i18n}"/></span></div>
          <a id="page_next" class="page_next" href="javascript:void(0);"><fmt:message key="w_next" bundle="${i18n}"/></a>
          <a id="page_last" class="page_last" href="javascript:void(0);"><fmt:message key="w_last" bundle="${i18n}"/></a>
          <a id="page_refresh" class="page_refresh" href="javascript:void(0);"><fmt:message key="refresh" bundle="${i18n}"/></a>
        </div>
        <div class="page_total"><fmt:message key="w_display" bundle="${i18n}"/><span id="total_1">1</span><fmt:message key="w_to" bundle="${i18n}"/><span id="total_2">30</span>,<fmt:message key="w_altogether" bundle="${i18n}"/><span id="new_totalCount"></span><fmt:message key="w_record" bundle="${i18n}"/></div>
      </div>
    </div>
    <div class="selectContent">
      <h2><fmt:message key="w_select_employee" bundle="${i18n}"/></h2>
      <div class="select_title">
        <div class="column column_id">ID</div>
        <div class="column column_name"><fmt:message key="w_name" bundle="${i18n}"/></div>
        <div class="column column_department"><fmt:message key="w_dept" bundle="${i18n}"/></div>
        <div class="column column_email"><fmt:message key="w_email" bundle="${i18n}"/></div>
      </div>
      <input type="hidden" id="selectUserList" value="" />
      <ul class="select_list select_user_list add_div" id="selectUser">
      	<%-- <c:forEach items="${query.list }" var="employee">
	      	<li onclick="delectuser(1,'综合管理部','超级管理员','admin');return false;" class="add_employee" code="">
	      		<div class="column column_id">1&nbsp;</div><div class="column column_name">超级管理员</div>
	      		<div class="column column_username">admin&nbsp;</div><div class="column column_department" title="综合管理部">综合管理部&nbsp;</div>
	      		<div class="column column_del list_del"><a href="javascript:void(0);" title="删除">×</a></div>
	   		</li>
   		</c:forEach> --%>
      </ul>
    </div>
  </div>
</form>