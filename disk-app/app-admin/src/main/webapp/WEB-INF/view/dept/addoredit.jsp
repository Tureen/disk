<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${staticpath }/js/dept/saveoredit.js?v=201"></script>
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
<script type="text/javascript">
$(document).ready(function(){
	$("#queryForm").validate({
		rules:{
			deptName:{
				required:true,
			},
			deptShowName:{
				required:true,
			}
		},
		messages:{
			deptName:"部门名称不能为空",
			deptShowName:"父级部门不能为空"
		},
		onkeyup:false,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			if(!isNaN($("#deptName").val())){
				layer.msg('部门第一个字不能为数字!',{icon: 0,time:1000});
				return false;
			}
			var _url = basepath + '/dept/addDept';
			if($("#id").val().length > 0){
				_url = basepath + '/dept/editDept';
			}
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#queryForm").serialize(),
				type: 'POST',
				async : false,
				cache:false,
				success : function (data){
					if(checkErrorCode(data.code)){
						if(data.code=="1000"){
							layer.msg('操作成功!',{icon: 1,time:1000});
							setTimeout(function(){
								layerClose();
							},1000); 
						} else {
							layer.msg(data.codeInfo,{icon: 2,time:1000});
						}
				    }
				}
			});
		}
	});
});
 
 var zNodes = [];
 <c:forEach items="${depts}" var="dept">
  var data = { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true };
  zNodes.push(data);
</c:forEach>
</script>
</head>
<body>
<div class="page-container">	
  <form method="post"  id="queryForm" class="form form-horizontal">
    <div class="row cl">
			<label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>上级部门：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${parent.deptName }" id="deptShowName" name="deptShowName" placeholder="" 
				<c:if test="${empty parent}">onclick="showMenu(); return false;"</c:if> readOnly="true" >
	        	<input type="hidden" name="parentId" value="${parent.id }" id="deptId">
	        	<input type="hidden" name="id" value="${department.id }" id="id">
				<div id="menuContent" class="menuContent" style="display:none; position: absolute;width:300px;">
					<ul id="treeDemo" class="ztree" style="margin-top:0; width:100px;"></ul>
	         	</div>
			</div>
	</div>
    <div class="row cl">
      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>部门名称：</label>
      <div class="formControls col-xs-8 col-sm-7">
        <input type="text" class="input-text" placeholder="" maxlength="16"  name="deptName" value="${department.deptName }" id="deptName">
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
        <input class="btn btn-success radius" id="btnAdd" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>
</body>
</html>