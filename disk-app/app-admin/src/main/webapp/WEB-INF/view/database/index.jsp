<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
$(function(){
	$("#export").click(function(){
		var statu = confirm("确定备份数据库吗?");
        if(!statu){
            return false;
        }
		$.ajax({
			url :  basepath + "/database/export",
			dataType : "JSON",
			data : null,
			type: 'POST',
			async : false,
			cache:false,
			success : function (data){
				if(data.code==1014){
					layer.msg('数据库备份失败', {
						icon : 5,
						time : 1000
					});
					return;
				}
				if(checkErrorCode(data.code)){
					layer.msg('数据库备份成功', {
						icon : 6,
						time : 1000
					});
					setTimeout(function(){
						window.location.reload();
					},1000)
				}
			}
		});
	});

	$(".download").click(function(){
		var params = $(this).parent().find("input :last").val();
		window.location.href = basepath + "/database/download?params=" + params;
	});
	
	$("#import").click(function(){
		$("#file").click();
	});
	
	$("#file").change(function(){
		var fileName = $("#file").val();
		fileName = fileName.split("\\")[fileName.split("\\").length-1];
		$("#fileValue").val(fileName);
		if($("#file").val() == null || $("#file").val() ==""){
			$("#importBtn").hide();
		}else{
			$("#importBtn").show();
		}
	});
	
	$("#importBtn").click(function(){
		 var _url = $("#queryForm").attr('action');
		 var rdata = new FormData($("#queryForm")[0]);
		 var statu = confirm("确定还原数据库吗?");
	        if(!statu){
	            return false;
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
						layer.msg('数据库还原成功', {
							icon : 6,
							time : 1000
						});
						setTimeout(function(){
							window.location.reload();
						},1000)
					} else {
						layer.msg(data.codeInfo, {
							icon : 5,
							time : 1000
						});
						return false;
					}
				},
				error : function(data) {
					alert("数据库还原失败");
				}
			});
	});
	
	$(".reduction").click(function(){
		var backId = $(this).parent().parent().find("td :first").html();
		var _url = $("#queryForm").attr('action')+"?backupId="+backId;
		var statu = confirm("确定还原数据库吗?");
        if(!statu){
            return false;
        }
		$.ajax({
			url : _url,
			type : 'POST',
			data : null,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			success : function(data) {
				if (data.code == 1000) {
					layer.msg('数据库还原成功', {
						icon : 6,
						time : 1000
					});
					setTimeout(function(){
						window.location.reload();
					},1000)
				} else {
					layer.msg(data.codeInfo, {
						icon : 5,
						time : 1000
					});
					return false;
				}
			},
			error : function(data) {
				alert("数据库还原失败");
			}
		});
	});
	
});
</script>
<div class="page-container dataTables_wrapper">	
	<form method="post" action="${ctx}/database/import"  id="queryForm" class="form form-horizontal" enctype="multipart/form-data">
		<table id="DataTables_Table_0" class="tab_627">
			<thead >
			</thead>
			<tbody >
			    <tr>
			    	<th width="20%">备份数据库:</th>
			    	<td width="80%">
					    <input type="button" class="tab_627_btn" id="export" value="备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份" title="执行数据库备份" />
				    </td>
			    </tr>
			    <tr>
			    	<th width="20%">还原数据库:</th>
			    	<td width="80%" >
					    <input type="button" class="tab_627_btn" id="import" value="选择文件" title="请选择还原的数据库文件" />
					    <input type="text" style="height: 25px" placeholder="请选择还原的数据库文件" id="fileValue" value= "" readonly="readonly"/>
					    <input type="file" name="file" id="file" style="display: none" />&nbsp;&nbsp;
					    <input type="button" style="display: none" id="importBtn" class="tab_627_btn" value="还原" title="执行数据库还原" />
				    </td>
			    </tr>
			</tbody>
		</table>
		<div align="center"><br>
		</div>
	</form>
	
	<form method="post" action="${ctx}/database/index" id="beanForm">
		<table id="DataTables_Table_0" class="table table-border table-bordered table-bg table-sort">
			<thead>
				<tr>
					<th scope="col" colspan="6">备份数据列表</th>
				</tr>
				<tr class="text-c">
					<th width="40">ID</th>
					<th width="100">备份名称</th>
					<th width="200">备份地址</th>
					<th width="40">创建人</th>
					<th width="40">创建时间</th>
					<th width="40">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${query.list}" var="backup" >
						<tr class="text-c">
							<td>${backup.id }</td>
							<td>${backup.sqlName }</td>
							<td>${backup.sqlPath }</td>
							<td>${backup.createAdmin }</td>
							<td><fmt:formatDate value="${backup.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
							<td><input type="button" class="tab_628_btn download" value="下载">&nbsp;&nbsp;
							<input type="button" class="tab_628_btn reduction" value="还原">
					    	<input type="hidden" value="${backup.params }"></td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>