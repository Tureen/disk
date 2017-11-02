<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/dept/treeright.js?v=20161032"></script>
<div class="dataTables_wrapper">
	<form method="post" action="${ctx}/dept/treeright/${query.deptId}" id="beanForm">
	<div >
		<input type="text" class="input-text" style="width:150px" placeholder="员工ID " name = "id" id="" value="${query.id }" maxlength="16">
		<input type="text" class="input-text" style="width:250px" placeholder="员工名称 " name = "employeeName" id="" value="${query.employeeName }" maxlength="16">
		<input type="text" class="input-text" style="width:250px" placeholder="手机号 " name = "employeeMobile" id="" value="${query.employeeMobile }" maxlength="16">
		<button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜用户</button>
	</div>
	<br>
	<div class="cl pd-5 bg-1 bk-gray"> 
		<span class="l"> 
			<a class="btn btn-primary radius" href="javascript:;" onclick="admin_admin_add('添加员工','${ctx}/admin/toinsert/${adminType[1].type }','800','')"><i class="Hui-iconfont">&#xe600;</i> 添加员工</a> 
			<a class="btn btn-primary radius" href="javascript:;" onclick="exportExcel()"><i class="Hui-iconfont">&#xe640;</i> 导出员工</a> 
			<a class="btn btn-primary radius" href="javascript:;" onclick="admin_admin_inport('导入员工','${ctx}/dept/toImport/${query.deptId}','','','350')"><i class="Hui-iconfont">&#xe645;</i> 导入员工</a> 
		</span>
		<span class="r">共有数据：<strong>${query.recordCount }</strong> 条</span>
	</div>
	<form method="post" action="${ctx}/dept/treeright/${query.deptId}" id="beanForm">
	<table class="table table-border table-bordered table-bg table-sort">
		<thead>
			<tr>
				<th scope="col" colspan="7">${department.deptName }</th>
			</tr>
			<tr class="text-c">
				<th width="40">ID</th>
				<th width="200">手机号</th>
				<th width="100">员工名称</th>
				<th width="100">直属部门</th>
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
				<td class="td-status">
					<span class="label label-primary radius" style="width: 50%;" >${emp.deptName }</span>
				</td>
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
							<a style="text-decoration:none" onClick="member_start(this,'${adminType[1].type}','${emp.id }')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>
				</c:if>
				<c:if test="${emp.adminValidStatus == '1'}">
					<a style="text-decoration:none" onClick="member_stop(this,'${adminType[1].type}','${emp.id }')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>
				</c:if>
				<a title="编辑" href="javascript:;" onclick="admin_admin_edit('员工编辑','${ctx}/dept/toupdate/${adminType[1].type }/${emp.id}','1')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
				<a title="删除" href="javascript:;" onclick="admin_admin_del(this,'${ctx}/dept/delete/${adminType[1].type }/${emp.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
			 </tr>
		    </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>