<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<script type="text/javascript">
$(function(){
	$("#fllImport").click(function(){
		$("#frontLoginLogo").click();
	});
	$("#frontLoginLogo").change(function(){
		$("#fllValue").val($("#frontLoginLogo").val());
	});
	
	$("#flbImport").click(function(){
		$("#frontLoginBackground").click();
	});
	$("#frontLoginBackground").change(function(){
		$("#flbValue").val($("#frontLoginBackground").val());
	});
	
	$("#flImport").click(function(){
		$("#frontLogo").click();
	});
	$("#frontLogo").change(function(){
		$("#flValue").val($("#frontLogo").val());
	});
	
	$("#fwlImport").click(function(){
		$("#frontWebLogo").click();
	});
	$("#frontWebLogo").change(function(){
		$("#fwlValue").val($("#frontWebLogo").val());
	});
	
	$("#bllImport").click(function(){
		$("#backLoginLogo").click();
	});
	$("#backLoginLogo").change(function(){
		$("#bllValue").val($("#backLoginLogo").val());
	});
	
	$("#blbImport").click(function(){
		$("#backLoginBackground").click();
	});
	$("#backLoginBackground").change(function(){
		$("#blbValue").val($("#backLoginBackground").val());
	});
	
	$("#bwlImport").click(function(){
		$("#backWebLogo").click();
	});
	$("#backWebLogo").change(function(){
		$("#bwlValue").val($("#backWebLogo").val());
	});
	
	$("#appImport").click(function(){
		$("#andriodLogo").click();
	});
	$("#andriodLogo").change(function(){
		$("#appValue").val($("#andriodLogo").val());
	});
	
	$("#subBtn").click(function(){
		 var _url = $("#queryForm").attr('action');
		 var rdata = new FormData($("#queryForm")[0]);
		 if($("#frontLoginLogo").val()==''
			 && $("#frontLoginBackground").val()==''
				 && $("#frontLogo").val()==''
					 && $("#backLoginLogo").val()==''
						 && $("#backLoginBackground").val()==''
						 	&& $("#andriodLogo").val()==''){
			 layer.msg('上传内容为空,请至少选择上传其中一项图片!', {
					icon : 2,
					time : 1500
				});
			 return;
		 }
		 $.ajax({
				url : _url,
				type : 'POST',
				data : rdata,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					if (data.code == 1000) {
						layer.msg('导入成功', {
							icon : 6,
							time : 1000
						});
					} else {
						layer.msg(data.codeInfo, {
							icon : 5,
							time : 1000
						});
						return false;
					}
				},
				error : function(data) {
					alert("导入失败");
				}
			});
	});
	
});
</script>
<div class="page-container dataTables_wrapper">	
	<form method="post" action="${ctx}/commonbasedata/uplogo"  id="queryForm" class="form form-horizontal" enctype="multipart/form-data">
		<table id="DataTables_Table_0" class="tab_627">
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="20%">前台登录页logo图:</th>
			    	<td width="80%">
					    <input type="text" style="height: 25px" placeholder="请选择上传文件" id="fllValue" value= "" readonly="readonly"/>
			    		<input type="button" class="tab_627_btn" id="fllImport" value="选择图片">
						<input type="file" name="frontLoginLogo" id="frontLoginLogo" style="display: none">
						<span style="color: red">请上传 .png格式图片</span>
				    </td>
			    </tr>
			    <!-- <tr>
			    	<th width="20%">前台登录页背景图:</th>
			    	<td width="80%">
					    <input type="text" style="height: 25px" placeholder="请选择上传文件" id="flbValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="flbImport" value="选择图片">
						<input type="file" name="frontLoginBackground" id="frontLoginBackground" style="display: none">
				    </td>
			    </tr> -->
			   <tr>
			    	<th width="20%">前台首页logo图:</th>
			    	<td width="80%">
					    <input type="text" style="height: 25px" placeholder="请选择上传文件" id="flValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="flImport" value="选择图片">
						<input type="file" name="frontLogo" id="frontLogo" style="display: none">
						<span style="color: red">请上传 .png格式图片</span>
				    </td>
			    </tr>
			   <tr>
			    	<th width="20%">前台网址logo图:</th>
			    	<td width="80%">
					    <input type="text" style="height: 25px" placeholder="请选择上传文件" id="fwlValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="fwlImport" value="选择图片">
						<input type="file" name="frontWebLogo" id="frontWebLogo" style="display: none">
				    </td>
			    </tr>
			    <tr>
			    	<th width="20%">后台登录页logo图:</th>
			    	<td width="80%">
				    	<input type="text" style="height: 25px" placeholder="请选择上传文件" id="bllValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="bllImport" value="选择图片">
						<input type="file" name="backLoginLogo" id="backLoginLogo" style="display: none">
						<span style="color: red">请上传 .png格式图片</span>
				    </td>
			    </tr>
			    <tr>
			    	<th width="20%">后台登录页背景图:</th>
			    	<td width="80%">
					    <input type="text" style="height: 25px" placeholder="请选择上传文件" id="blbValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="blbImport" value="选择图片">
						<input type="file" name="backLoginBackground" id="backLoginBackground" style="display: none">
				    </td>
			    </tr>
			    <tr>
			    	<th width="20%">后台网址logo图:</th>
			    	<td width="80%">
				    	<input type="text" style="height: 25px" placeholder="请选择上传文件" id="bwlValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="bwlImport" value="选择图片">
						<input type="file" name="backWebLogo" id="backWebLogo" style="display: none">
				    </td>
			    </tr>
			    <tr>
			    	<th width="20%">移动端logo图:</th>
			    	<td width="80%">
				    	<input type="text" style="height: 25px" placeholder="请选择上传文件" id="appValue" value= "" readonly="readonly"/>
					    <input type="button" class="tab_627_btn" id="appImport" value="选择图片">
						<input type="file" name="andriodLogo" id="andriodLogo" style="display: none">
						<span style="color: red">请上传 .png格式图片</span>
				    </td>
			    </tr>
			</tbody>
		</table>
		<div align="center"><br>
			<input type="button" id="subBtn" class="tab_627_btn" value="确认上传"/>
			<input type="reset" id="subBtn1" class="tab_627_btn" value="重置上传"/>
		</div>
	</form>
</div>
</body>
</html>