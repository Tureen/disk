<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript" src="${staticpath }/js/cprole/index.js?v=20"></script>
<div class="page-container dataTables_wrapper">
	<div class="cl pd-5 bg-1 bk-gray"> 
	<span class="l"> 
	<a class="btn btn-primary radius" href="javascript:;" onclick="admin_role_add('添加角色','${ctx}/cprole/toinsert/0','800')"><i class="Hui-iconfont">&#xe600;</i> 添加角色</a> 
	</span> <span class="r">共有数据：<strong>${query.recordCount}</strong> 条</span> </div>
	<form method="post" action="${ctx}/cprole/index" id="beanForm">
	<table class="table table-border table-bordered table-hover table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="5">角色管理</th>
			</tr>
			<tr class="text-c">
				<th width="100">ID</th>
				<th width="200">角色名</th>
				<th width="400">描述</th>
				<th width="100">使用状态</th>
				<th width="200">操作</th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${query.list}" var="role" >
		     <tr class="text-c">
				<td>${role.id }</td>
				<td>${role.roleName }</td>
				<td>${role.roleDesc}</td>
				<td class="td-status">
					<c:if test="${role.validStatus != '1'}">
							<span class="label label-fail radius">已停用</span>
						</c:if>
					<c:if test="${role.validStatus == '1'}">
							<span class="label label-success radius">已启用</span>
					</c:if>
				</td>
				<td class="td-manage">
						<c:if test="${role.validStatus != '1'}">
									<a style="text-decoration:none" onClick="member_start(this,'${role.id }')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>
						</c:if>
						<c:if test="${role.validStatus == '1'}">
							<a style="text-decoration:none" onClick="member_stop(this,'${role.id }')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>
						</c:if>
						<a title="编辑" href="javascript:;" onclick="admin_role_edit('角色编辑','${ctx}/cprole/toupdate/${role.id}','1')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
						<a title="删除" href="javascript:;" onclick="admin_role_del(this,'${ctx}/cprole/delete/${role.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>
						&nbsp;<a title="员工角色分配" href="javascript:;" onclick="admin_role_edit('员工角色分配','${ctx}/cprole/tree?roleId=${role.id }','1','800','550')" style="text-decoration:none"><i class="Hui-iconfont">&#xe645;</i></a>
				</td>
			 </tr>
		    </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>