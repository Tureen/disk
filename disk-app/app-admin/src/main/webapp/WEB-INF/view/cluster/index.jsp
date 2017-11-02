<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript" src="${staticpath }/js/cluster/index.js?v=20160921"></script>
<script>
</script>
<div class="page-container dataTables_wrapper">
	<iframe src="${ctx}/basicsinfo/tocluster" id="myiframe" width="100%" scrolling="no" frameborder="0"></iframe>
	<div class="cl pd-5 bg-1 bk-gray"> 
	<span class="l"> 
	<a class="btn btn-primary radius" href="javascript:;" onclick="admin_admin_add('添加文件服务器','${ctx}/cluster/toinsert','800','')"><i class="Hui-iconfont">&#xe600;</i> 添加文件服务器</a>
	</span> <span class="r">共有数据：<strong>${query.recordCount}</strong> 条</span> </div>
	<form method="post" action="${ctx}/cluster/index" id="beanForm">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr>
				<th scope="col" colspan="10">文件服务器管理</th>
			</tr>
			<tr class="text-c">
				<th width="40">ID</th>
				<th width="100">名称</th>
				<th width="100">ip地址</th>
				<th width="100">集群编号</th>
				<th width="100">文件存储目录</th>
				<th width="200">文件服务器地址</th>
				<th width="100">共享目录</th>
				<th width="100">预留空间大小(MB)</th>
				<th width="100">备注</th>
				<th width="70">操作</th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${query.list}" var="cluster" >
		     <tr class="text-c">
				<td>${cluster.id }</td>
				<td>${cluster.clusterName }</td>
				<td>${cluster.clusterIp }</td>
				<td>${cluster.clusterCode }</td>
				<td>${cluster.filePath }</td>
				<td>${cluster.fileUrl }</td>
				<td>${cluster.sharePath }</td>
				<td>${cluster.reserveSpace }</td>
				<td>${cluster.remark }</td>
				<td class="td-manage">
				<a title="编辑" href="javascript:;" onclick="admin_admin_edit('文件服务器编辑','${ctx}/cluster/toupdate/${cluster.id}','1')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
				<a title="删除" href="javascript:;" onclick="admin_admin_del(this,'${ctx}/cluster/delete/${cluster.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
			 </tr>
		    </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>