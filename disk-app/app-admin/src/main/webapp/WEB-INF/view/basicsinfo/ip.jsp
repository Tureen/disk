<%@page import="com.yunip.enums.basics.BasicsInfoCode"%>
<%@ page language="java" pageEncoding="UTF-8"%>
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
<div class="page-container dataTables_wrapper">
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="20%">禁止登录IP列表</th>
			    	<td width="30%" bgcolor="#fafafa"><textarea name="<%=BasicsInfoCode.NOTHROUGHIP.getKey()%>"><%=basicsInfoMap.get(BasicsInfoCode.NOTHROUGHIP.getKey())%></textarea></td>
			    	<td width="50%" rowspan="3"> <b>说明：</b>您可以添加多个限制IP，每个IP用<font color="red">回车</font>分隔，限制IP的书写方式如202.152.12.1就限制了202.152.12.1这个IP的访问，如202.152.12.*就限制了以202.152.12开头的IP访问，同理*.*.*.*则限制了所有IP的访问。在添加多个IP的时候，请注意最后一个IP的后面不要加回车。</td>
			    </tr> 
			    <tr>
			    	<th>允许登录IP列表</th>
			    	<td bgcolor="#fafafa"><textarea  name="<%=BasicsInfoCode.THROUGHIP.getKey()%>"><%=basicsInfoMap.get(BasicsInfoCode.THROUGHIP.getKey())%></textarea></td>
			    </tr>  
			    <tr>
			    	<th>IP拦截模式</th>
			    	<td  class="mr20">
			    		<span><input type="radio" name="<%=BasicsInfoCode.IPMODE.getKey()%>" value="<%=BasicsInfoCode.NOTHROUGHIP.getKey() %>" <c:if test="<%=(BasicsInfoCode.NOTHROUGHIP.getKey()).equals(basicsInfoMap.get(BasicsInfoCode.IPMODE.getKey()))%>">checked="checked"</c:if> /><%=BasicsInfoCode.NOTHROUGHIP.getValue() %></span>
			    		<input type="radio" name="<%=BasicsInfoCode.IPMODE.getKey()%>" value="<%=BasicsInfoCode.THROUGHIP.getKey() %>" <c:if test="<%=(BasicsInfoCode.THROUGHIP.getKey()).equals(basicsInfoMap.get(BasicsInfoCode.IPMODE.getKey()))%>">checked="checked"</c:if> /><%=BasicsInfoCode.THROUGHIP.getValue() %>
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