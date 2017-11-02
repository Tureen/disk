<%@page import="com.yunip.enums.company.IsAdminType"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
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
$(function(){
	$("#subBtn").click(function(){
		var oldpass = $("#oldpass").val();
    	var newpass = $("#newpass").val();
    	var confirmpass = $("#confirmpass").val();
    	var adminId = $("#adminId").val();
    	if(oldpass==null||oldpass==""){
    		alert(1)
    		layer.msg('请输入旧密码！',{icon: 5,time:2000});
    		return;
    	}
    	if(newpass==null||newpass==""){
    		layer.msg('请输入新密码！',{icon: 5,time:2000});
    		return;
    	}
    	if(confirmpass==null||confirmpass==""){
    		layer.msg('请再次输入新密码！',{icon: 5,time:2000});
    		return;
    	}
    	if(newpass!=confirmpass){
    		layer.msg('两次输入新密码不一致！',{icon: 5,time:2000});
    		return;
    	}
    	if(confirmpass.length < 6){
       		layer.msg('密码不能低于6位长度！',{icon: 5,time:2000});
       		return;
    	}
    	var data = {
    			"password" : oldpass,
    			"newPassword" : newpass,
    			"confirmPassword" : confirmpass,
    			"adminId" : adminId
    	}
		//检验旧密码是否一致
		$.ajax({
			url: basepath+"/admin/check",
			dataType : "JSON",
			data : data,
			type: 'POST',
			async : false,
			cache:false,
	        success: function(data){
				if(checkErrorCode(data.code)){
					if(data.code=="1000"){
						layer.msg('操作成功!',{icon: 1,time:1000});
						setTimeout(layerClose,1000);
					} else {
						layer.msg(data.codeInfo,{icon: 2,time:1000});
					}
			    }
	        }
		});
	});
});

//关闭打开的layer
function layerClose(){
	parent.location.href="${ctx}/login/index";
}
</script>
<article class="page-container">
	<input type="hidden" id="adminId" value="${adminId }">
	<form method="post" class="form form-horizontal" id="form-admin-admin-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>原密码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${admin.mobile }" id="oldpass" type="password" maxlength="20"  placeholder="请输入原始密码" datatype="*4-16" nullmsg="原始密码不能为空">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>新密码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input class="input-text" value="${admin.mobile }" id="newpass" type="password" maxlength="20" placeholder="请输入新密码" datatype="*4-16" nullmsg="新密码不能为空">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>确认新密码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input class="input-text" value="${admin.mobile }" id="confirmpass" type="password" maxlength="20" placeholder="请再次输入新密码" datatype="*4-16" nullmsg="再次输入新密码不能为空">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="button" class="btn btn-success radius" id="subBtn" name="admin-admin-save"><i class="icon-ok"></i> 确定</button>
			</div>
		</div>
	</form>
</article>
</body>
</html>
