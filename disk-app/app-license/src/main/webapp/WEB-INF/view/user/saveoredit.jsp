<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<style type="text/css">
.menuContent{
    height:100px;
    overflow-x:hidden;
    overflow-y:scroll;
    background-color:#F0F0F0;
    border:1px solid #f0f0f0;
    z-index: 999;
}
</style>
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript" src="${staticpath }/js/user/saveoredit.js"></script>
<script type="text/javascript">
var zNodes = [];
 <c:forEach items="${departmentList}" var="dept">
  var data = { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true };
  zNodes.push(data);
</c:forEach>
</script>
<article class="page-container">

	<form <c:if test="${empty admin.id }">action="${ctx}/admin/save"</c:if>
	<c:if test="${not empty admin.id }">action="${ctx}/admin/edit"</c:if> method="post" class="form form-horizontal" id="form-admin-admin-add">
		<input type="hidden" value="${isAdmin }" id="isAdmin" name="isAdmin">
		<input type="hidden" value="${employee.id }" id="id" name="id">
		<input type="hidden" value="${roleStr }" id="roleStr">
		<c:set value="<%=IsAdminType.GLY.getType() %>" var="adminType"></c:set>
		<c:set value="<%=IsAdminType.YG.getType() %>" var="employeeType"></c:set>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>手机号码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${admin.mobile }" placeholder="" id="employeeMobile" name="employeeMobile" datatype="*4-16" nullmsg="手机号码不能为空">
			</div>
		</div>
		<c:if test="${isAdmin==employeeType}">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>员工编号：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.employeeCode }" placeholder="" id="employeeCode" maxlength="16" name="employeeCode" datatype="*4-16" nullmsg="员工编号不能为空">
				</div>
			</div>
		</c:if>
		<c:if test="${empty employee}">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>初始化密码：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="123456" placeholder="" readonly="readonly" datatype="*4-16">
				</div>
			</div>
		</c:if>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>姓名：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${employee.employeeName }" placeholder="" id="employeeName" name="employeeName">
			</div>
		</div>
		<c:if test="${isAdmin==adminType}">
			<input type="hidden" name="deptId" value="00">
		</c:if>
		<c:if test="${isAdmin==employeeType}">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>部门：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.deptName }" id="deptName" name="deptName" placeholder="" onclick="showMenu(); return false;" readOnly="true" >
		        	<input type="hidden" name="deptId" value="${employee.deptId }" id="deptId">
					<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
						<ul id="treeDemo" class="ztree" style="margin-top:0; width:401px;"></ul>
		         	</div>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>性别：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="radio" class="input-radio" value="0" placeholder="" id="employeeNan" name="employeeSex" <c:if test="${empty employee}">checked="checked"</c:if> <c:if test="${employee.employeeSex==0 }">checked="checked"</c:if>  datatype="*4-16" nullmsg="请选择性别">男
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" class="input-radio" value="1" placeholder="" id="employeeNv" name="employeeSex" <c:if test="${employee.employeeSex==1 }">checked="checked"</c:if> datatype="*4-16" nullmsg="请选择性别">女
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">身份证：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.idCard }" placeholder="" id="idCard" name="idCard" datatype="*4-16" nullmsg="身份证不能为空">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">微信：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.employeeWechat }" placeholder="" id="employeeWechat" name="employeeWechat" datatype="*4-16" nullmsg="微信不能为空">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">QQ：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.employeeQq }" placeholder="" id="employeeQq" name="employeeQq" datatype="*4-16" nullmsg="QQ不能为空">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">固定电话：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<input type="text" class="input-text" value="${employee.employeeTelephone }" placeholder="" id="employeeTelephone" name="employeeTelephone" datatype="*4-16" nullmsg="QQ不能为空">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">自我介绍：</label>
				<div class="formControls col-xs-8 col-sm-7">
					<textarea class="input-text" cols="2" id="introduction" name="introduction"  datatype="*4-16">${employee.introduction }</textarea>
				</div>
			</div>
		</c:if>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色设置：</label>
		　     <c:set var="index" value="0"/>
			<c:if test="${isAdmin==adminType}">
				<div class="formControls col-xs-8 col-sm-7">
					<dl class="permission-list">
						<dt>
							<label><input type="checkbox" >全选</label>
						</dt>
						<dd>
							<c:forEach items="${roleList}" var="role">
								<c:if test="${role.id!=2}">
									<label class="c-orange">
										<input type="checkbox" value="${role.id }" id="check${role.id }" name="roles[${index }].id">
										${role.roleName }</label>
								    <c:set var="index" value="${index + 1 }"/>
							    </c:if>
							</c:forEach>
						</dd>
					</dl>
				</div>
		    </c:if>
		    <c:if test="${isAdmin==employeeType}">
		    	<div class="formControls col-xs-8 col-sm-7">
			    	<c:forEach items="${roleList}" var="role">
			    		<c:if test="${role.id==2}">
							<input type="checkbox" value="${role.id }" id="check${role.id }" name="roles[${index }].id">${role.roleName }
						    <c:set var="index" value="${index + 1 }"/>
					    </c:if>
				    </c:forEach>
			    </div>
		    </c:if>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="submit" class="btn btn-success radius" id="admin-admin-save" name="admin-admin-save"><i class="icon-ok"></i> 确定</button>
			</div>
		</div>
	</form>
</article>
</body>
</html>
