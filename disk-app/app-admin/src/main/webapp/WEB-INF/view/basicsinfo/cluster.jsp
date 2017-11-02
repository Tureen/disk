<%@page import="com.yunip.enums.basics.LanguageType"%>
<%@page import="com.yunip.enums.basics.BasicsBool"%>
<%@page import="com.yunip.enums.basics.BasicsStatus"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.yunip.enums.basics.BasicsInfoCode"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script language="javascript" type="text/javascript" src="${plugins}/lib/color-picke/js/jquery.colorPicker.min.js"/></script>
<link rel="stylesheet" href="${plugins}/lib/color-picke/css/colorPicker.css" type="text/css" />
<script type="text/javascript">
$(function(){
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
							$.ajax({
								url :  basepath + "/basicsinfo/clearfile",
								dataType : "JSON",
								data : null,
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
<script type="text/javascript">
$(function() {
    
	    
	$('#color').colorPicker({colors: ["FF8080", "FFFF80", "80FF80", "00FF80", "80FFFF",
        "0080FF", "FF80C0", "FF80FF", "FF0000", "FFFF00",
        "80FF00", "00FF40", "00FFFF", "0080C0", "8080C0",
        "FF00FF", "804040", "FF8040", "00FF00", "008080",
        "004080", "8080FF", "800040", "FF0080", "800000",
        "FF8000", "008000", "008040", "0000FF", "0000A0",
        "800080", "8000FF", "400000", "804000", "004000",
        "004040", "000080", "000040", "400040", "400080",
        "000000", "808000", "808040", "808080", "408080",
        "C0C0C0", "400040", "FFFFFF"]
        ,pickerDefault: "FF0000"
        ,transparency: true
        ,onColorChange : function(id, newValue){
          $("#showColorValue").val(newValue);
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
</head>
<body>
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
				<tr>
			    	<th width="20%">是否开启文件集群</th>
			    	<td width="80%" class="mr20">
			    		<span><input type="radio" name="<%=BasicsInfoCode.ISOPENCLUSTER.getKey()%>" value="<%=BasicsBool.YES.getBool() %>" <c:if test="<%=(BasicsBool.YES.getBool()).equals(basicsInfoMap.get(BasicsInfoCode.ISOPENCLUSTER.getKey()))%>">checked="checked"</c:if> />是</span>
						<input type="radio" name="<%=BasicsInfoCode.ISOPENCLUSTER.getKey()%>" value="<%=BasicsBool.NO.getBool() %>" <c:if test="<%=(BasicsBool.NO.getBool()).equals(basicsInfoMap.get(BasicsInfoCode.ISOPENCLUSTER.getKey()))%>">checked="checked"</c:if> />否
					</td>
			    </tr>
			    <tr>
			    	<th>路由服务器地址</th>
			    	<td><input type="text" maxlength="50" name="<%=BasicsInfoCode.FILEDOMAIN.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.FILEDOMAIN.getKey())%>" /></td>
			    </tr>
			</tbody>
		</table>
		<div align="center"><br>
			<input type="submit" value="保存设置" id="Button1" class="tab_627_btn">
		</div>
	</form>
</body>
</html>