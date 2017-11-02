<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${plugins}/lib/My97DatePicker/WdatePicker.js" />
<script type="text/javascript" src="${plugins}/lib/My97DatePicker/WdatePicker.js"></script> 
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript" src="${staticpath }/js/file/look.js"></script>
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
<script type="text/javascript">
var zNodes = [];
<c:forEach items="${departmentList}" var="dept">
  var data = { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true };
  zNodes.push(data);
</c:forEach>
</script>
<div class="page-container dataTables_wrapper">
    <form action="${ctx }/file/look" method="post" id="beanForm">
		<div >
			<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" placeholder="起始日期 "  value="${query.startDate }" name = "startDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			-
			<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" placeholder="终止日期" value="${query.endDate }" name = "endDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			<input type="text" class="input-text" style="width:250px" placeholder="用户名 " name = "employeeName" id="" value="${query.employeeName }">
			<input type="text" class="input-text" style="width:250px" placeholder="文件名(可以包含拓展名)" id="" name="queryName" value="${query.queryName }">
			<input type="text" style="width:200px" class="input-text" value="${query.deptName }" id="deptName" name="deptName" placeholder="选择部门" onclick="showMenu(); return false;" readOnly="true" >
	        	<input type="hidden" name="deptId" value="${query.deptId }" id="deptId">
				<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
					<ul id="treeDemo" class="ztree" style="margin-top:0; width:175px;"></ul>
	         	</div>
			<button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜用户</button>
		</div>
	<br>
	<div>
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="80">文件名</th>
				<th width="100">大小</th>
				<th width="40">用户名</th>
				<th width="90">上传时间</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${query.list }" var="file">
		   <tr class="text-c">
				<td>${file.fileName}</td>
				<td>${file.showFileSize}</td>
				<td>${file.createAdmin}</td>
				<td><fmt:formatDate value="${file.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
			</tr>
		   </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</div>
	</form>
</body>
</html>