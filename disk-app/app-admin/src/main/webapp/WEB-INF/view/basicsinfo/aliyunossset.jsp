<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.yunip.enums.basics.BasicsInfoCode"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript">
$(function(){
	$("#formTable").validate({
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
<style>
/*为了jquery validate错误提示样式正确而重写*/
label.error{
	position:static !important;
}
</style>
<div class="page-container dataTables_wrapper">	
	<form action="${ctx }/basicsinfo/edit" method="post" id="formTable">
		<table id="DataTables_Table_0" class="tab_627">
		<%java.util.HashMap<String,String> basicsInfoMap = (java.util.HashMap<String,String>)request.getAttribute("map"); %>
			<thead >
			</thead>
			<tbody>
				<tr>
			    	<th width="20%">Bucket名称</th>
			    	<td width="80%">
			    		<input type="text" maxlength="50" name="<%=BasicsInfoCode.ALIYUN_OSS_BUCKET_NAME.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ALIYUN_OSS_BUCKET_NAME.getKey())%>" class="required" />
			    		（阿里云对象存储OSS平台中创建的Bucket名称）
			    	</td>
			    </tr>
				<tr>
			    	<th>OSS访问域名</th>
			    	<td class="mr20">
			    		<c:set var="endpoint" value="<%=basicsInfoMap.get(BasicsInfoCode.ALIYUN_OSS_DOMAIN_ENDPOINT.getKey())%>"/>  
						<select name="<%=BasicsInfoCode.ALIYUN_OSS_DOMAIN_ENDPOINT.getKey()%>" style="width: 189px;">
							<c:forEach items="${endpointList }" var="item">
								<option value="${item.extranetEndpoint }" ${item.extranetEndpoint == endpoint ? "selected='selected'" : "" }>${item.chineseName }</option>
							</c:forEach>
						</select>
						（对应创建Bucket时选择的地域）
					</td>
			    </tr>
			    <tr>
			    	<th>Access Key ID</th>
			    	<td>
			    		<input type="text" maxlength="50" name="<%=BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_ID.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_ID.getKey())%>" class="required" />
			    		（阿里云对象存储OSS平台Access Key管理中创建的Access Key ID）
			    	</td>
			    </tr>
			    <tr>
			    	<th>Access Key Secret</th>
			    	<td>
			    		<input type="text" maxlength="50" name="<%=BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_SECRET.getKey()%>" value="<%=basicsInfoMap.get(BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_SECRET.getKey())%>" class="required" />
			    		（阿里云对象存储OSS平台Access Key管理中创建的Access Key Secret）
			    	</td>
			    </tr>
			</tbody>
		</table>
		<div align="center"><br>
			<input type="submit" value="保存设置" id="Button1" class="tab_627_btn" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="进入阿里云" class="tab_627_btn" onclick="javascript:window.open('http://www.aliyun.com/product/oss')" />
		</div>
	</form>
</div>
</body>
</html>