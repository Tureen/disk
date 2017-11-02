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
<script type="text/javascript" src="${staticpath }/js/cluster/saveoredit.js?v=20170113"></script>
<article class="page-container">
	<input type="hidden" value="${type }" id="savetype">
	<form <c:if test="${empty cluster.id }">action="${ctx}/cluster/save"</c:if>
	<c:if test="${not empty cluster.id }">action="${ctx}/cluster/edit"</c:if> method="post" class="form form-horizontal" id="form-admin-admin-add">
		<input type="hidden" name="id" value="${cluster.id }">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>名称：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.clusterName }" placeholder="" id="clusterName" name="clusterName" datatype="*4-16" nullmsg="名称不能为空">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>ip地址：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.clusterIp }" placeholder="" id="clusterIp" maxlength="16" name="clusterIp" datatype="*4-16" nullmsg="ip地址不能为空">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>集群编号：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.clusterCode }" placeholder="" id="clusterCode" name="clusterCode">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>文件存储目录：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.filePath }" placeholder="" id="filePath" name="filePath">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>文件服务器地址：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.fileUrl }" placeholder="" id="fileUrl" name="fileUrl">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>共享目录：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.sharePath }" placeholder="" id="sharePath" name="sharePath">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>预留空间大小（MB）：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.reserveSpace }" placeholder="" id="reserveSpace" name="reserveSpace" onkeyup='this.value=this.value.replace(/\D/gi,"")'>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${cluster.remark }" placeholder="" id="remark" name="remark">
			</div>
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
