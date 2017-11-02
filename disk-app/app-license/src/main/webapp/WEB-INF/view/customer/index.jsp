<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${plugins}/lib/My97DatePicker/WdatePicker.js" />
<script type="text/javascript" src="http://lib.h-ui.net/My97DatePicker/WdatePicker.js"></script> 
<script type="text/javascript" src="${staticpath }/js/customer/index.js?v=211"></script>
<div class="page-container dataTables_wrapper">
	<form method="post" action="${ctx}/customer/index" id="beanForm">
		<div> 日期范围：
					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" placeholder="起始日期 "  value="${query.startDate }" name = "startDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
					-
					<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" placeholder="终止日期" value="${query.endDate }" name = "endDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
					<input type="text" class="input-text" style="width:250px" placeholder="客户名称 " name ="customerName" id="" value="${query.customerName }">
					<input type="text" class="input-text" style="width:250px" placeholder="联系人" id="" name="contacts" value="${query.contacts }">
					<input type="text" class="input-text" style="width:250px" placeholder="联系人电话" id="" name="contactsMobile" value="${query.contactsMobile }">
					<button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜用户</button>
		</div>
		<br>
		<div class="cl pd-5 bg-1 bk-gray"> 
		<span class="l"> 
		<a class="btn btn-primary radius" href="javascript:;" onclick="admin_admin_add('添加授权客户','${ctx}/customer/tosave','800','600')"><i class="Hui-iconfont">&#xe600;</i> 添加授权客户</a> 
		</span> <span class="r">共有数据：<strong>${query.recordCount}</strong> 条</span> </div>
		<table class="table table-border table-bordered table-hover table-bg table-sort">
			<thead>
				<tr>
					<th scope="col" colspan="10">客户端信息</th>
				</tr>
				<tr class="text-c">
					<th width="5%">ID</th>
					<th width="10%">客户名称</th>
					<th width="10%">联系人</th>
					<th width="10%">联系人电话</th>
					<th width="20%">授权码</th>
					<th width="20%">允许员工数秘钥<span style="color: gray;font-size: 2px">(单独使用)</span></th>
					<th width="5%">授权时长</th>
					<th width="5%">允许注册员工数</th>
					<th width="10%">创建时间</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody>
			    <c:forEach items="${query.list}" var="customer" >
			     <tr class="text-c">
					<td>${customer.id }</td>
					<td>${customer.customerName }</td>
					<td>${customer.contacts }</td>
					<td>${customer.contactsMobile }</td>
					<td>${customer.licenseCode }#${customer.registerKey }</td>
					<td>${customer.registerKey }</td>
					<td>
						<c:if test="${customer.licenseHour/24 >= 365000}">  
    							不限
						</c:if> 
						<c:if test="${customer.licenseHour/24 < 365000}">  
						     <fmt:parseNumber integerOnly="true" value="${customer.licenseHour/24}" />天
						</c:if>
					</td>
					<td>
						<c:if test="${customer.registerNum == 9999}">  
    							不限
						</c:if> 
						<c:if test="${customer.registerNum != 9999}">  
						     ${customer.registerNum }人
						</c:if>
					</td>
					<td><fmt:formatDate value="${customer.createTime }" pattern="yyyy-MM-dd HH:mm" /></td>
					<td class="td-manage">
					<a title="编辑" href="javascript:;" onclick="admin_admin_add('编辑详情','${ctx}/customer/toedit?id=${customer.id}','800','600')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
					&nbsp;<a title="日志" href="javascript:;" onclick="admin_admin_add('日志','${ctx}/customer/log?customerId=${customer.id}','800','600')" style="text-decoration:none"><i class="Hui-iconfont">&#xe687;</i></a>
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