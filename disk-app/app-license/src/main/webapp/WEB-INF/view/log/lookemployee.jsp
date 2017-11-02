<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${plugins}/lib/My97DatePicker/WdatePicker.js" />
<script type="text/javascript" src="http://lib.h-ui.net/My97DatePicker/WdatePicker.js"></script> 
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<div class="page-container dataTables_wrapper">
    <form action="${ctx }/log/lookemployee" method="post" id="beanForm">
		<div >
			<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" placeholder="起始日期 "  value="${query.startDate }" name = "startDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			-
			<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" placeholder="终止日期" value="${query.endDate }" name = "endDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			<input type="text" class="input-text" style="width:250px" placeholder="栏目" name="menuName" 	value="${query.menuName }">
			<input type="text" class="input-text" style="width:250px" placeholder="操作人" id="" name="operAdmin" value="${query.operAdmin }">
			<input type="text" class="input-text" style="width:150px" placeholder="操作人ID" id="" name="adminId" value="${query.adminId }"  onkeyup='this.value=this.value.replace(/\D/gi,"")'>
			<button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜日志</button>
		</div>
	<br>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="10%">栏目</th>
				<th width="40">事件</th>
				<th width="20%">操作时间</th>
				<th width="20%">IP</th>
				<th width="10%">操作人</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${query.list }" var="adminlog">
		   <tr class="text-c">
				<td width="10%">
					<c:forEach items="${enum }" var="adminLogType">
				    	<c:if test="${adminLogType.type==adminlog.actionType}">${adminLogType.desc }</c:if>
				    </c:forEach>
				</td>
				<td width="40">${adminlog.operContent}</td>
				<td width="20%"><fmt:formatDate value="${adminlog.operTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				<td width="20%">${adminlog.operIp}</td>
				<td width="10%">${adminlog.operAdmin}</td>
			</tr>
		   </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</div>
	</form>
</body>
</html>