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
	
	/**
	 * 备份文件上传到阿里云
	 */
	$(".backupOss").click(function(){
		var backId = $(this).parent().parent().find("td :first").html();
        if(confirm("确定备份至阿里云对象存储OSS平台吗?已经备份过文件再次备份将会覆盖！")){
        	var index = layer.load(2, {shade:[0.2]});//加载中
        	$.ajax({
    			url : basepath + "/ossdatabase/uploadOss",
    			type : "POST",
    			data : {'backupId' : backId},
    			dataType : "json",
    			async : true,
    			cache : false,
    			success : function(data) {
    				if (data.code == 1000) {
    					layer.msg('备份至阿里云对象存储OSS平台成功！', {
    						icon : 6,
    						time : 3000
    					});
    					layer.close(index);//关闭加载中
    					setTimeout(function(){
    						window.location.reload();
    					},1000)
    				} else {
    					layer.close(index);//关闭加载中
    					layer.msg(data.codeInfo, {
    						icon : 5,
    						time : 5000
    					});
    					return false;
    				}
    			},
    			error : function(data) {
    				layer.close(index);//关闭加载中
    				alert("备份至阿里云对象存储OSS平台失败！");
    			}
    		});
        }
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
			</tbody>
		</table>
		<div align="center"><br>
		</div>
	</form>
	
	<form method="post" action="${ctx}/ossdatabase/index" id="beanForm">
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
							<td>
								<input type="button" class="tab_627_btn backupOss" value="上传至阿里云" title="将数据库文件上传至阿里云对象存储OSS平台进行备份" />&nbsp;&nbsp;
						    	<input type="hidden" value="${backup.params }">
					    	</td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</form>
</div>
</body>
</html>