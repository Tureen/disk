<%@page import="com.yunip.enums.basics.LanguageType"%>
<%@page import="com.yunip.enums.basics.BasicsBool"%>
<%@page import="com.yunip.enums.basics.BasicsStatus"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.yunip.enums.basics.BasicsInfoCode"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript">
/*管理员-权限-编辑*/
function admin_permission_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
function admin_permission_add(title,url,id,w,h){
	layer_show(title,url,w,h);
}

$(function(){
	$(".reload_sysconfig").click(function(){
		$.ajax({
		    type: 'GET',
		    url: basepath + "/commonbasedata/reload" ,
		    success: function(data) {  
		    	layer.msg('加载配置成功！',{icon: 1,time:1500});
		    }
		});
	});
	
	$("#formTable").validate({
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
			var _url = $("#formTable").attr('action');
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#formTable").serialize(),
				type: 'POST',
				async : false,
				cache:false,
				success : function (data){
					if(checkErrorCode(data.code)){
						if(data.code=="1000"){
							layer.msg('操作成功!',{icon: 1,time:1500});
							setTimeout(function(){
								window.location.href=window.location.href;
			            	}, 1500);
						} else {
							layer.msg(data.codeInfo,{icon: 2,time:1500});
						}
				    }
				}
			});
		}
	});
	
});
</script>
<style>
.table-bg tbody td:first-child{
	background-color:#cadee8;
}
.table-bg tbody td:nth-child(2){
	background-color:#e9f2f7;
}
border th,.table-border td{border-bottom:2px solid white}
border th,.table-bordered td{border-left:1px solid white}
</style>
<div class="page-container dataTables_wrapper">	
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th>单个文件上传最大限制 (MB)</th>
			    	<td><input type="text" onkeyup='this.value=this.value.replace(/\D/gi,"")' name="<%=BasicsInfoCode.SIMPLYFILESIZE.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.SIMPLYFILESIZE.getKey())%>"></td>
			    </tr> 
			    <tr>
			    	<th>禁止上传的文件类型</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.NOTHROUGHUPLOAD.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.NOTHROUGHUPLOAD.getKey())%>">（多个类型以 "," 隔开 , 例 : "txt,jpg,pdf"）</td>
			    </tr> 
			    <tr>
			    	<th>允许上传的文件类型</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.THROUGHUPLOAD.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.THROUGHUPLOAD.getKey())%>">（多个类型以 "," 隔开 , 例 : "txt,jpg,pdf"）</td>
			    </tr> 
			    <tr>
			    	<th>上传模式</th>
			    	<td  class="mr20">
			    		<span><input type="radio" name="<%=BasicsInfoCode.UPLOADMODE.getKey()%>" value="<%=BasicsInfoCode.NOTHROUGHUPLOAD.getKey() %>" <c:if test="<%=(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()).equals(basicsInfoMap.get(BasicsInfoCode.UPLOADMODE.getKey()))%>">checked="checked"</c:if> />上传时检查禁止上传的文件类型</span>
			    		<input type="radio" name="<%=BasicsInfoCode.UPLOADMODE.getKey()%>" value="<%=BasicsInfoCode.THROUGHUPLOAD.getKey() %>" <c:if test="<%=(BasicsInfoCode.THROUGHUPLOAD.getKey()).equals(basicsInfoMap.get(BasicsInfoCode.UPLOADMODE.getKey()))%>">checked="checked"</c:if> />上传时检查允许上传的文件类型 
					</td>
			    </tr> 
			</tbody>
		</table>
		<div align="center"><br>
			<input type="submit" value="保存设置" id="Button1" class="tab_627_btn">
		</div>
	</form>
</div>
</body>
</html>