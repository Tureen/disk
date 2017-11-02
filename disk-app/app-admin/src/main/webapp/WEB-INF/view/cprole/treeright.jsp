<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<script type="text/javascript">
$(function(){
	var employeeIdStr = $("#employeeIdStr").val();
	var employeeIdArr = employeeIdStr.split(',');
	for (var i = 0, len = employeeIdArr.length; i < len; i++){
		$("#check"+employeeIdArr[i]).attr("checked","checked");
	}
	
	/* $("#beanForm").validate({
		rules:{
			roleName:{
				required:true,
			},
		},
		messages:{
			roleName:"角色名称不能为空",
		},
		onkeyup:false,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			$.ajax({
				url :  basepath + "/cprole/saveemployee",
				dataType : "JSON",
				data : $("#beanForm").serialize(),
				type: 'POST',
				async : false,
				cache:false,
				success : function (data){
					if(checkErrorCode(data.code)){
						layer.msg('操作成功!',{icon: 1,time:1000});
						setTimeout(layerClose,1000);
				    }
				}
			});
		}
	}); */
	
	$("#admin-role-save").click(function(){
		$.ajax({
			url :  basepath + "/cprole/saveemployee",
			dataType : "JSON",
			data : $("#beanForm").serialize(),
			type: 'POST',
			async : false,
			cache:false,
			success : function (data){
				if(checkErrorCode(data.code)){
					layer.msg('操作成功!',{icon: 1,time:1000});
					setTimeout(layerClose,1000);
			    }
			}
		});
	});
});

function selectCheck(){
	$("[code='employeeCheck']").each(function(){
		if ($(this).is(":checked")) {
			var id = $(this).val();
		}
	});
}

//关闭打开的layer
function layerClose(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.location.reload();
}
</script>
<body>
<script type="text/javascript" src="${staticpath }/js/cprole/treeright.js?v=20160828"></script>
<div class="dataTables_wrapper">
	<form method="post" action="${ctx}/cprole/treeright/${query.deptId}" id="beanForm">
		<input type="hidden" id="employeeIdStr" value="${employeeIdStr}">
		<input type="hidden" name="id" value="${roleId }">
		<div class="cl pd-5 bg-1 bk-gray"> 
			<button type="button" class="btn btn-success radius" id="admin-role-save" name="admin-role-save" style="height:30px;padding:2px 10px; font-size:14px;"><i class="icon-ok"></i>提交分配</button>
			<span class="r">共有数据：<strong>${query.recordCount }</strong> 条</span>
		</div>
		<table class="table table-border table-bordered table-bg table-sort" style="max-height:490px;    overflow-y: scroll;">
			<thead>
				<tr>
					<th scope="col" colspan="5">${department.deptName }</th>
				</tr>
				<tr class="text-c">
					<th width="20%"><input type="checkbox" name="" id=""></th>
					<th width="10%">ID</th>
					<th width="30%">手机号</th>
					<th width="20%">员工名称</th>
					<th width="20%">直属部门</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="index" value="0"/>
				<c:forEach items="${query.list}" var="emp" >
					<input type="hidden" name="allEmployees[${index}].id" value="${emp.id }">
			     <tr class="text-c">
					<td><input type="checkbox" code="employeeCheck" name="employees[${index}].id" value="${emp.id }" id="check${emp.id}"></td>
					<td>${emp.id }</td>
					<td>${emp.employeeMobile }</td>
					<td>${emp.employeeName }</td>
					<td class="td-status">
						<span class="label label-primary radius" style="width:100px" >${emp.deptName }</span>
					</td>
				 </tr>
				 <c:set var="index" value="${index + 1 }"/>
			    </c:forEach>
			</tbody>
		</table>
	</form>
</div>
</body>
</html>