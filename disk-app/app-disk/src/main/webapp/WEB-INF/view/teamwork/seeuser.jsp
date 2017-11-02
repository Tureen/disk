<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link href="${staticpath}/css/workgroup.css?v=20170115" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${staticpath}/js/teamwork/seeuser.js?v=20170214"></script>
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
	float:none;
	width:auto;
	padding:0px;
    width: 481px;
}
.tl-header, .select_list li {
    height: 30px;
    line-height: 30px;
}
</style>
<form method="post" id="SubForm">
<input id="teamworkId" type="hidden" value="${teamworkId }">
<input id="employeeAddObjs" type="hidden" value="${employeeAddObjs }">
<input type="hidden" name="deptId" value="" id="deptId">
  <div class="userRight">
    <div class="selectContent">
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