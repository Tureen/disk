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
});

</script>
<div class="page-container dataTables_wrapper">
	<div class="cl pd-5 bg-1 bk-gray"> 
		<span class="l">
			<a href="javascript:;" onclick="admin_permission_add('添加系统配置','${ctx}/commonbasedata/toaddsysconfig','1','','400')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加系统配置</a>
			<a href="javascript:;" class="btn btn-primary radius reload_sysconfig">重载配置</a>
		</span>
		<span class="r">共有数据：<strong>${query.recordCount }</strong> 条</span>
	</div>
				<table id="DataTables_Table_0" class="table table-border table-bordered table-bg table-sort">
					<thead>
						<tr>
							<th>配置代码</th>
							<th>配置名称</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${query.list}" var="sysConfig">
							<tr>
								<td>${sysConfig.configKey}</td>
								<td>${sysConfig.configValue}</td>
								<td class="td-manage">
									<a title="编辑" href="javascript:;" onclick="admin_permission_edit('系统配置编辑','${ctx}/commonbasedata/toeditsysconfig?configCode=${sysConfig.configCode}&configKey=${sysConfig.configKey}','1','','400')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> 
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>