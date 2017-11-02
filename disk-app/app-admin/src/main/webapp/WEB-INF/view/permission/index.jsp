<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript" src="${staticpath }/js/permission/index.js"></script>
<div class="page-container dataTables_wrapper">
	<div class="cl pd-5 bg-1 bk-gray"> 
		<span class="l">
			<a href="javascript:;" onclick="admin_permission_add('添加权限节点','${ctx}/permission/treepermission','','800')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加权限节点</a>
		</span>
		<span class="r">共有数据：<strong>${query.recordCount }</strong> 条</span>
	</div>
	<form method="post" action="${ctx}/permission/index" id="beanForm">
		<table id="DataTables_Table_0" class="table table-border table-bordered table-bg table-sort">
			<thead>
				<tr>
					<th scope="col" colspan="5">权限节点</th>
				</tr>
				<tr class="text-c">
					<th width="40">ID</th>
					<th width="200">权限名称</th>
					<th width="200">权限类型</th>
					<th width="40">状态</th>
					<th width="100">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${query.list}" var="permission">
					<c:if test="${permission.permissionType == enum[0].type || permission.permissionType == enum[1].type}">
						<tr class="text-c">
							<td>${permission.id }</td>
							<td>${permission.permissionName }</td>
							<td>
							<c:if test="${permission.permissionType == 0 }">最上级权限</c:if>
							<c:if test="${permission.permissionType != 0 }">
							<c:forEach items="${enum}" var="permissionEnum">
					      		<c:if test="${permission.permissionType == permissionEnum.type }">${permissionEnum.desc}</c:if>
					      	</c:forEach>
							</c:if>
							</td>
							<td class="td-status">
								
								<c:if test="${permission.validStatus != '1'}">
									<span class="label label-fail radius">已停用</span>
								</c:if>
								<c:if test="${permission.validStatus == '1'}">
									<span class="label label-success radius">已启用</span>
								</c:if>
							</td>
							<td class="td-manage">
								<c:if test="${permission.validStatus != '1'}">
									<a style="text-decoration:none" onClick="member_start(this,'${permission.id }')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>
								</c:if>
								<c:if test="${permission.validStatus == '1'}">
									<a style="text-decoration:none" onClick="member_stop(this,'${permission.id }')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>
								</c:if>
								<a title="编辑" href="javascript:;" onclick="admin_permission_edit('权限编辑','${ctx}/permission/toupdatepermission/${permission.id }','1','','300')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>