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
.en-zh{
	color: gray;
	margin-left: -30px;
}
</style>
<div class="page-container dataTables_wrapper">	
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_629">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="20%">前台网站标题</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.TITLE.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.TITLE.getKey())%>"><span class="en-zh">(中)</span>
			    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=BasicsInfoCode.TITLE_ENGLISH.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.TITLE_ENGLISH.getKey())%>"><span class="en-zh">(英)</span>
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">前台网站备案信息</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.WEB_RECORD.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.WEB_RECORD.getKey())%>"><span class="en-zh">(中)</span>
			    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=BasicsInfoCode.WEB_RECORD_ENGLISH.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.WEB_RECORD_ENGLISH.getKey())%>"><span class="en-zh">(英)</span>
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">前台主页导航</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.WEB_NAVIGATION.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.WEB_NAVIGATION.getKey())%>"><span class="en-zh">(中)</span>
			    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=BasicsInfoCode.WEB_NAVIGATION_ENGLISH.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.WEB_NAVIGATION_ENGLISH.getKey())%>"><span class="en-zh">(英)</span>
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">后台网站标题</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.ADMIN_TITLE.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADMIN_TITLE.getKey())%>">
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">后台网站备案信息</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.ADMIN_WEB_RECORD.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADMIN_WEB_RECORD.getKey())%>">
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">后台主页导航</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.ADMIN_WEB_NAVIGATION.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ADMIN_WEB_NAVIGATION.getKey())%>">
		    		</td>
			    </tr>
			    <tr>
			    	<th width="20%">移动端主页导航</th>
			    	<td width="80%">
			    		<input type="text" name="<%=BasicsInfoCode.MOBILE_NAVIGATION.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.MOBILE_NAVIGATION.getKey())%>">
		    		</td>
			    </tr>
			    <tr>
			    	<th>默认网站语言</th>
			    	<td>
			    		<input type="radio" name="<%=BasicsInfoCode.LOCALLANGUAGE.getKey()%>" value="<%=LanguageType.CHINA.getCode() %>" <c:if test="<%=(LanguageType.CHINA.getCode()).equals(basicsInfoMap.get(BasicsInfoCode.LOCALLANGUAGE.getKey()))%>">checked="checked"</c:if> />简体中文
			    		<input type="radio" name="<%=BasicsInfoCode.LOCALLANGUAGE.getKey()%>" value="<%=LanguageType.ENGLISH.getCode() %>" <c:if test="<%=(LanguageType.ENGLISH.getCode()).equals(basicsInfoMap.get(BasicsInfoCode.LOCALLANGUAGE.getKey()))%>">checked="checked"</c:if> />美国英语
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