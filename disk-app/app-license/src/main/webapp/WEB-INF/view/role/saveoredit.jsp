<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/role/saveoredit.js"></script>
<article class="page-container">
	<form <c:if test="${empty role.id }">action="${ctx}/role/save"</c:if>
	<c:if test="${not empty role.id }">action="${ctx}/role/edit"</c:if>
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
			<div class="formControls col-xs-8 col-sm-7">
			　     <c:set var="index" value="0"/>
				<c:forEach items="${permissions}" var="permission">
				   <c:set var="index" value="${index + 1 }"/>
					<c:if test="${permission.permissionType == 1}">
						<dl class="permission-list">
							<dt>
									<label>
										<input type="checkbox" value="${permission.id }" id="check${permission.id }" name="permissions[${index }].id">
										${permission.permissionName }</label>
							</dt>
							<dd>
								<c:forEach items="${permission.permissions}" var="zpermission">
								    <c:set var="index" value="${index + 1 }"/>
									<dl class="cl permission-list2">
										<dt>
											<label class="">
												<input type="checkbox" value="${zpermission.id }" id="check${zpermission.id }" name="permissions[${index }].id">
												${zpermission.permissionName }</label>
										</dt>
										<dd>
											<c:forEach items="${zpermission.permissions}" var="ypermission" varStatus="state">
											    <c:set var="index" value="${index + 1 }"/>
												<label class="c-orange">
													<input type="checkbox" value="${ypermission.id }" id="check${ypermission.id }" name="permissions[${index }].id" id="user-Character-0-0-0">
													${ypermission.permissionName }</label>
													<c:if test="${state.index%2!=0 && state.index != 0 }"><br></c:if>
											</c:forEach>
										</dd>
									</dl>
								</c:forEach>
							</dd>
						</dl>
					</c:if>
				</c:forEach>
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