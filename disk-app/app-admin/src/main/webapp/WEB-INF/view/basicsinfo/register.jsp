<%@page import="com.yunip.enums.common.SortType"%>
<%@page import="com.yunip.enums.disk.SpaceType"%>
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
			    	<th width="20%">登录后默认进入空间</th>
			    	<td width="80%">
			    		<input type="radio" name="<%=BasicsInfoCode.TOLOGINSPAGE.getKey()%>" value="<%=SpaceType.DISKSPACE.getType()%>" <c:if test="<%=(SpaceType.DISKSPACE.getType()).equals(basicsInfoMap.get(BasicsInfoCode.TOLOGINSPAGE.getKey()))%>">checked="checked"</c:if> /><%=SpaceType.DISKSPACE.getDesc() %>
						<input type="radio" name="<%=BasicsInfoCode.TOLOGINSPAGE.getKey()%>" value="<%=SpaceType.PERSONALSPACE.getType()%>" <c:if test="<%=(SpaceType.PERSONALSPACE.getType()).equals(basicsInfoMap.get(BasicsInfoCode.TOLOGINSPAGE.getKey()))%>">checked="checked"</c:if> /><%=SpaceType.PERSONALSPACE.getDesc() %>
						<input type="radio" name="<%=BasicsInfoCode.TOLOGINSPAGE.getKey()%>" value="<%=SpaceType.COMSHARESPACE.getType()%>" <c:if test="<%=(SpaceType.COMSHARESPACE.getType()).equals(basicsInfoMap.get(BasicsInfoCode.TOLOGINSPAGE.getKey()))%>">checked="checked"</c:if> /><%=SpaceType.COMSHARESPACE.getDesc() %>
			    	</td>
			    </tr> 
			    <tr>
			    	<th width="20%">默认文件列表排序方式</th>
			    	<td width="80%">
			    		<select name="<%=BasicsInfoCode.SORTTYPE.getKey()%>">
				    		<c:set var="type" value="<%=basicsInfoMap.get(BasicsInfoCode.SORTTYPE.getKey())%>"/>  
			    			<c:forEach items="${sortenum}" var="sortObj">
		    					<option value="${sortObj.type }" <c:if test="${sortObj.type==type }">selected="selected"</c:if> >${sortObj.desc }</option>
			    			</c:forEach>
			    		</select>
			    	</td>
			    </tr> 
			    <tr>
			    	<th>默认个人空间使用大小</th>
			    	<td><input type="text" onkeyup='this.value=this.value.replace(/\D/gi,"")' maxlength="16" name="<%=BasicsInfoCode.PSPACE.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.PSPACE.getKey())%>"><span style="color: gray;margin-left: 5px">MB</span></td>
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