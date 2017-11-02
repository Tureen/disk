<%@page import="com.yunip.enums.basics.LanguageType"%>
<%@page import="com.yunip.enums.basics.BasicsBool"%>
<%@page import="com.yunip.enums.basics.BasicsStatus"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.yunip.enums.basics.BasicsInfoCode"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script language="javascript" type="text/javascript" src="${plugins}/lib/color-picke/js/jquery.colorPicker.min.js"/></script>
<link rel="stylesheet" href="${plugins}/lib/color-picke/css/colorPicker.css" type="text/css" />
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
<div class="page-container dataTables_wrapper">	
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap =  (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody >
				<tr>
			    	<th width="20%">是否开启水印</th>
			    	<td width="80%" class="mr20">
			    		<span><input type="radio" name="<%=BasicsInfoCode.IS_WATER_MARK.getKey()%>" value="<%=BasicsBool.YES.getBool() %>" <c:if test="<%=(BasicsBool.YES.getBool()).equals(basicsInfoMap.get(BasicsInfoCode.IS_WATER_MARK.getKey()))%>">checked="checked"</c:if> />是</span>
						<input type="radio" name="<%=BasicsInfoCode.IS_WATER_MARK.getKey()%>" value="<%=BasicsBool.NO.getBool() %>" <c:if test="<%=(BasicsBool.NO.getBool()).equals(basicsInfoMap.get(BasicsInfoCode.IS_WATER_MARK.getKey()))%>">checked="checked"</c:if> />否
					</td>
			    </tr>
			    <tr>
			    	<th>文字水印内容</th>
			    	<td><input type="text" maxlength="50" name="<%=BasicsInfoCode.WATER_MARK_TEXT.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.WATER_MARK_TEXT.getKey())%>" /></td>
			    </tr>
			    <tr>
			    	<th>文字水印颜色</th>
			    	<td><input id="showColorValue"  type="text" name="<%=BasicsInfoCode.TEXT_COLOR.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.TEXT_COLOR.getKey())%>" style="width:50px;float: left;" readonly="readonly" /><input id="color" type="text" name="color" value="<%=basicsInfoMap.get(BasicsInfoCode.TEXT_COLOR.getKey())%>" /></td>
			    </tr>
			    <tr>
			    	<th>对齐方式</th>
			    	<td>
			    		<select name="<%=BasicsInfoCode.ALIGN.getKey()%>">
				    		<c:set var="type" value="<%=basicsInfoMap.get(BasicsInfoCode.ALIGN.getKey())%>"/>  
			    			<c:forEach items="${alignenum}" var="alignObj">
		    					<option value="${alignObj.type }" <c:if test="${alignObj.type==type }">selected="selected"</c:if> >${alignObj.desc }</option>
			    			</c:forEach>
			    		</select>
			    	</td>
			    </tr>  
			    <tr>
			    	<th>水印文字大小</th>
			    	<td>
			    		<select name="<%=BasicsInfoCode.FONT_SIZE.getKey()%>">
			    			<c:set var="sizetype" value="<%=basicsInfoMap.get(BasicsInfoCode.FONT_SIZE.getKey())%>"/>  
			    			<c:forEach items="${fontsizeenum}" var="fontsizeObj">
		    					<option value="${fontsizeObj.type }" <c:if test="${fontsizeObj.type==sizetype }">selected="selected"</c:if> >${fontsizeObj.desc }</option>
			    			</c:forEach>
			    			<c:forEach var="item" varStatus="status" begin="0" end="14">
    							<option value="${2*status.index+12}" <c:if test="${(2*status.index+12)==sizetype}">selected="selected"</c:if> >${2*status.index+12}</option>
    						</c:forEach>
			    		</select>
			    	</td>
			    </tr>  
			    <tr>
			    	<th>水印文字倾斜（旋转）角度</th>
			    	<td><input type="text" onkeyup='this.value=this.value.replace(/\D/gi,"")' maxlength="3" name="<%=BasicsInfoCode.ROTATION.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ROTATION.getKey())%>"> (请输入数字 0 ~ 360)</td>
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