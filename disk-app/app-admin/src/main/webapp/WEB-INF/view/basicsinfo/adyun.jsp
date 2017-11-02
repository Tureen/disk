<%@page import="com.yunip.enums.basics.BasicsBool"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.HashMap"%>
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
			var data = $("#formTable").serialize();
			var ischeck = $("#isopencheck").parent().find("input[type='checkbox']").is(':checked');
			if(ischeck){
				$("#isopen").val("<%=BasicsBool.YES.getBool()%>");
			}else{
				$("#isopen").val("<%=BasicsBool.NO.getBool()%>");
			}
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
<div class="page-container dataTables_wrapper">
	
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="20%"></th>
			    	<td width="80%">
			    		<input id="isopencheck" type="checkbox"  <c:if test="<%=(BasicsBool.YES.getBool()).equals(basicsInfoMap.get(BasicsInfoCode.ISOPENAD.getKey()))%>">checked="checked"</c:if>>启用前台AD整合
			    		<input id="isopen" type="hidden" name="<%=BasicsInfoCode.ISOPENAD.getKey()%>"  value="">
			    	</td>
			    </tr> 
			    <tr>
			    	<th>AD域IP地址 ：</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.ADIP.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADIP.getKey())%>">（必填）</td>
			    </tr> 
			    <tr>
			    	<th>AD域控制器（DC）：</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.ADDC.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADDC.getKey())%>">（必填）</td>
			    </tr> 
			    <tr>
			    	<th>AD帐户用户名：</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.ADUSERNAME.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADUSERNAME.getKey())%>">（必填）</td>
			    </tr> 
			    <tr>
			    	<th>AD帐户密码：</th>
			    	<td><input type="text" name="<%=BasicsInfoCode.ADPASSWORD.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADPASSWORD.getKey())%>">（必填）</td>
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