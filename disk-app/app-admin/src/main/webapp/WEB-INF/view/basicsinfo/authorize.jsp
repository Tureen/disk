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
	
	$("#registernumBtn").click(function(){
		if($("#registerkey").val() == null || $("#registerkey").val() == ""){
			layer.msg('请输入授权员工数秘钥！',{icon: 5,time:1500});
			return;
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
	});
	
	$("#reglicenseBtn").click(function(){
		var serCode = $("#serCode").val();
		if(serCode == null || serCode == ""){
			layer.msg('请输入授权时长秘钥！',{icon: 5,time:1500});
			return;
		}
		$.ajax({
			url :  basepath + "/basicsinfo/reglicense?serCode="+serCode,
			dataType : "JSON",
			type: 'GET',
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
	});
	
	function surplusDay(){
		// 获取当前时间戳(以s为单位)
		var timestamp = Date.parse(new Date());
		timestamp = timestamp / 1000;
		// 获取某个时间格式的时间戳(授权时间)
		var stringTime = $("#time").val();
		var timestamp2 = Date.parse(new Date(stringTime));
		timestamp2 = timestamp2 / 1000;
		var surplusDay = (timestamp2*1 - timestamp*1)/60/60/24;
		return surplusDay;
	}
	
	var surplusDay = surplusDay();
	console.log(surplusDay);
	if(surplusDay*1 >= 36500){
		$("#surplusTime input").val("永久");
	}
});

</script>
<style>
.no_input_bolder{
	border: 0px;
	background-color: #fff;
}
</style>
<div class="page-container dataTables_wrapper">
   	<input id="time" type="hidden"  value="<fmt:formatDate value="${licenseDate}" pattern="yyyy-MM-dd HH:00:00" />">
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="40%">授权到期时间</th>
			    	<td id="surplusTime" width="60%"><input class="no_input_bolder" type="text"  value="<fmt:formatDate value="${licenseDate}" pattern="yyyy年 MM月 dd日 HH:00" />" readonly="readonly" style="border: 0px;background-color: #fff;"></td>
			    </tr> 
			    <tr>
			    	<th>授权时间秘钥（修改到期授权时间时使用）</th>
			    	<td><input id="serCode" type="text" maxlength="64" ><span style="color: gray;margin-left: 5px"></span><input type="button" value="确认" id="reglicenseBtn" class="tab_627_btn" style="height: 25px;width: 100px"></td>
			    </tr> 
			    <tr>
			    	<th width="40%">当前可允许使用员工数</th>
			    	<td width="60%"><input class="no_input_bolder" type="text"  value="<c:if test="${registerNum == 9999}">不限</c:if><c:if test="${registerNum == -1}">秘钥错误</c:if><c:if test="${registerNum != 9999 && registerNum != -1}">${registerNum }人</c:if>" readonly="readonly" style="border: 0px;background-color: #fff;<c:if test="${registerNum == -1}">color: red;</c:if>"></td>
			    </tr> 
			    <tr>
			    	<th>当前已使用员工数（不包含“停用”员工）</th>
			    	<td><input type="text" value="${useNum }人" readonly="readonly" style="border: 0px;background-color: #fff"></td>
			    </tr> 
			    <tr>
			    	<th>授权员工数秘钥（扩充人数时使用）</th>
			    	<td><input id="registerkey" type="text" maxlength="32" name="<%=BasicsInfoCode.REGISTER_RESTRICTIONS_KEY.getKey()%>"><span style="color: gray;margin-left: 5px"></span><input type="button" value="确认" id="registernumBtn" class="tab_627_btn" style="height: 25px;width: 100px"></td>
			    </tr> 
			</tbody>
		</table>
	</form>
</div>
<div style="height: 22px;text-align: left;padding: 20px;">
	<font color=#000000><strong>&nbsp;备注：</strong>修改系统授权后，需要管理员重新启动应用程序。</font> 
</div>
</body>
</html>