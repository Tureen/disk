<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${plugins}/lib/My97DatePicker/WdatePicker.js" />
<script type="text/javascript" src="http://lib.h-ui.net/My97DatePicker/WdatePicker.js"></script> 
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript">
function changeActionType(){
	$("#beanForm").submit();
}
</script>
<div class="page-container dataTables_wrapper">
    <form action="${ctx }/customer/log" method="post" id="beanForm">
	<span class="select-box" style="width:250px">
		<select class="select"  name="actionType" onchange="changeActionType()">
			<option value="" selected disabled="disabled">行为类型</option>
			<option value="">全部</option>
			<c:forEach items="${enum }" var="licenseLogType">
		    	<option <c:if test="${query.actionType==licenseLogType.type }">selected="selected"</c:if> value="${licenseLogType.type }">${licenseLogType.desc }</option>
		    </c:forEach>
		</select>
	</span>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="15%">行为类型</th>
				<th width="40">操作内容</th>
				<th width="10%">操作人</th>
				<th width="15%">操作人IP</th>
				<th width="20%">操作时间</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${query.list }" var="licenseLog">
		   <tr class="text-c">
				<td width="15%">
					<c:forEach items="${enum }" var="licenseLogType">
				    	<c:if test="${licenseLogType.type==licenseLog.actionType}">${licenseLogType.desc }</c:if>
				    </c:forEach>
				</td>
				<td width="40">${licenseLog.operContent}</td>
				<td width="10%">${licenseLog.operAdmin}</td>
				<td width="15%">${licenseLog.operIp}</td>
				<td width="20%"><fmt:formatDate value="${licenseLog.operTime}" pattern="yyyy-MM-dd HH:mm" /></td>
			</tr>
		   </c:forEach>
		</tbody>
	</table>
	</div>
	</form>
</body>
</html>