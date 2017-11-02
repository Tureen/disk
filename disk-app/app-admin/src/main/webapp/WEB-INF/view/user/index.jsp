<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript" src="${staticpath }/js/user/index.js?v=20160921"></script>
<div class="page-container dataTables_wrapper">
	<div class="cl pd-5 bg-1 bk-gray"> 
	<span class="l"> 
	<a class="btn btn-primary radius" href="javascript:;" onclick="admin_admin_add('添加管理员','${ctx}/admin/toinsert/${adminType[0].type }','800','')"><i class="Hui-iconfont">&#xe600;</i> 添加管理员</a> 
	</span> <span class="r">共有数据：<strong>${query.recordCount}</strong> 条</span> </div>
	<form method="post" action="${ctx}/admin/index" id="beanForm">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr>
				<th scope="col" colspan="7">管理员管理</th>
			</tr>
			<tr class="text-c">
				<th width="40">ID</th>
				<th width="200">手机号</th>
				<th width="200">员工姓名</th>
				<th width="200">所属部门</th>
				<th width="100">使用状态</th>
				<th width="30">重置密码</th>
				<th width="70">操作</th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${query.list}" var="emp" >
		     <tr class="text-c">
				<td>${emp.id }</td>
				<td>${emp.employeeMobile }</td>
				<td>${emp.employeeName }</td>
				<td>${emp.deptName }</td>
				<td class="td-status">
					<c:if test="${emp.adminValidStatus != '1'}">
							<span class="label label-fail radius">已停用</span>
						</c:if>
					<c:if test="${emp.adminValidStatus == '1'}">
							<span class="label label-success radius">已启用</span>
					</c:if>
				</td>
				<td class="">
				 <a title="重置密码" href="javascript:;" onclick="member_huanyuan('${emp.id }')" style="text-decoration:none"><i class="Hui-iconfont">&#xe66b;</i></a> 
				</td>
				<td class="td-manage">
				<c:if test="${emp.adminValidStatus != '1'}">
							<a style="text-decoration:none" onclick="member_start(this,'${adminType[0].type }','${emp.id }')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>
				</c:if>
				<c:if test="${emp.adminValidStatus == '1'}">
					<a style="text-decoration:none" onclick="member_stop(this,'${adminType[0].type }','${emp.id }')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>
				</c:if>
				<a title="编辑" href="javascript:;" onclick="admin_admin_edit('管理员编辑','${ctx}/admin/toupdate/${adminType[0].type }/${emp.id}','1')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
				<a title="删除" href="javascript:;" onclick="admin_admin_del(this,'${ctx}/admin/delete/${adminType[0].type }/${emp.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
			 </tr>
		    </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>