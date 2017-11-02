<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<style type="text/css">
.menuContent{
    height:100px;
    width:428px;
    overflow-x:hidden;
    overflow-y:scroll;
    background-color:#F0F0F0;
    border:1px solid #f0f0f0;
    z-index:999;
}
</style>
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript" src="${staticpath }/js/permission/edit.js"></script>
<script type="text/javascript">
var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false,
		showIcon: false,
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: ""
		}
	},
	callback: {
		onClick:onClick
	}
};
var zNodes = [];
 <c:forEach items="${allfuncs}" var="petree">
 <c:if test="${petree.permissionType<3}">
  var data = { id: "<c:out value="${petree.id}"/>", pId: "<c:out value="${petree.permissionFid}"/>", name: "<c:out value="${petree.permissionName}"/>", open: true ,type:"<c:out value="${petree.permissionType}"/>"};
  zNodes.push(data);
  </c:if>
</c:forEach>

function onClick(e, treeId,treeNode){
	$("#permissionFName").val(treeNode.name);
	$("#permissionFid").val(treeNode.id);
	<c:forEach items="${enum}" var="permissionEnum">
	var xy = '${permissionEnum.type}';
	if(treeNode.type==xy-1){
		$("#sPermissionType").val("${permissionEnum.desc}");
		$("#permissionType").val("${permissionEnum.type}");
	}
	</c:forEach>
}
</script>
<title>修改权限</title>
</head>
<body>
<div class="page-container">
  <form action="${ctx }/permission/edit" method="post" class="form form-horizontal" id="permissionForm">
  	<input type="hidden" name="id" value="${permission.id }">
  	<div class="row cl">
      <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限类型：</label>
      <div class="formControls col-xs-8 col-sm-7">
      	<input type="hidden" name="permissionType" value="${permission.permissionType }" id="permissionType">
        <input type="text" class="input-text" 
        <c:forEach items="${enum}" var="permissionEnum">
        	<c:if test="${permission.permissionType==0 }">value="最上级权限"</c:if>
      		<c:if test="${permission.permissionType==permissionEnum.type }">value="${permissionEnum.desc}"</c:if>
      	</c:forEach>
         placeholder=""  id="sPermissionType" readonly="readonly">
      </div>
      <div class="col-xs-8 col-sm-7"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>上级目录：</label>
      <div class="formControls col-xs-8 col-sm-7">
        <input type="text" class="input-text" value="${permission.permissionFname }" id="permissionFName" placeholder="" onclick="showMenu('${permission.id}'); return false;" readOnly="true" >
        <input type="hidden" name="permissionFid" value="${permission.permissionFid }" id="permissionFid">
        <div id="menuContent" class="menuContent" style="display:none; position: absolute;">
					<ul id="treeDemo" class="ztree" style="margin-top:0; width:533.5px;"></ul>
       	</div>
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限名称：</label>
      <div class="formControls col-xs-8 col-sm-7">
        <input type="text" class="input-text" value="${permission.permissionName }" placeholder=""  name="permissionName" id="permissionName">
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
        <input class="btn btn-primary radius" id="btnAdd" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>
</body>
</html>