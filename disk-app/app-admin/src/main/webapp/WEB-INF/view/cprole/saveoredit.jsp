<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/cprole/saveoredit.js?v=20"></script>
<article class="page-container">
	<form <c:if test="${empty role.id }">action="${ctx}/cprole/save"</c:if>
	<c:if test="${not empty role.id }">action="${ctx}/cprole/edit"</c:if>
	 method="post" class="form form-horizontal" id="form-admin-role-add">
		<input type="hidden" value="${role.id }" id="roleId" name="id">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色名称：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${role.roleName }" placeholder="" id="roleName" name="roleName" >
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色描述：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${role.roleDesc }" placeholder="" id="roleDesc" name="roleDesc">
			</div>
		</div>
		<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">系统权限：</label>
				<input type="hidden" value="${permissionStr }" id="permissionStr">
				<c:set var="index" value="0"/>
				<div class="formControls col-xs-8 col-sm-7" id="permissionAll">
					<dl class="permission-list">
						<dt>
								<label>
									<input type="checkbox" value="1" id="checkOne" code="allCheck">
									基础功能</label>
									<a id="selectAll" style="position: relative;float: right;" href="javascript:;"><font color="green">全选</font></a>
						</dt>
						<dd>
								<dl class="cl permission-list2">
									<dt>
										<c:forEach items="${permissions}" var="permission">
											<c:if test="${permission.permissionCode == 1}">
											  	<c:set var="index" value="${index + 1 }"/>
												<label class="">
													<input type="checkbox" value="${permission.id }" id="check${permission.id }" code="allCheck" name="cpPermissions[${index }].id">
													${permission.permissionName }</label>
											</c:if>
										</c:forEach>
									</dt>
								</dl>
						</dd>
					</dl>
					<dl class="permission-list">
						<dt>
								<label>
									<input type="checkbox" value="2" id="checkTwo" >
									文件传输</label>
						</dt>
						<dd>
								<dl class="cl permission-list2">
									<dt>
										<c:forEach items="${permissions}" var="permission">
											<c:if test="${permission.permissionCode == 2}">
											  	<c:set var="index" value="${index + 1 }"/>
												<label class="">
													<input type="checkbox" value="${permission.id }" id="check${permission.id }" code="allCheck" name="cpPermissions[${index }].id">
													${permission.permissionName }</label>
											</c:if>
										</c:forEach>
									</dt>
								</dl>
						</dd>
					</dl>
					<dl class="permission-list">
						<dt>
								<label>
									<input type="checkbox" value="3" id="checkThree" >
									扩展功能</label>
						</dt>
						<dd>
								<dl class="cl permission-list2">
									<dt>
										<c:forEach items="${permissions}" var="permission">
											<c:if test="${permission.permissionCode == 3}">
											  	<c:set var="index" value="${index + 1 }"/>
												<label class="">
													<input type="checkbox" value="${permission.id }" id="check${permission.id }" code="allCheck" name="cpPermissions[${index }].id">
													${permission.permissionName }</label>
											</c:if>
										</c:forEach>
									</dt>
								</dl>
						</dd>
					</dl>
				</div>
			</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="submit" class="btn btn-success radius" id="admin-role-save" name="admin-role-save"><i class="icon-ok"></i> 确定</button>
			</div>
		</div>
	</form>
</article>
</body>
</html>