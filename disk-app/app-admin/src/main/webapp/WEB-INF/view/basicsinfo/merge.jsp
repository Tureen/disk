<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.HashMap"%>
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
	
	$("#btnAdd").click(function(){
		var _url = $("#queryForm").attr('action');
		$.ajax({
			url :  _url,
			dataType : "JSON",
			data : $("#queryForm").serialize(),
			type: 'POST',
			async : false,
			cache:false,
			success : function (data){
				if(checkErrorCode(data.code)){
					if(data.code=="1000"){
						layer.msg('操作成功!',{icon: 1,time:1000});
						clearInput();
					} else {
						layer.msg(data.codeInfo,{icon: 2,time:1000});
					}
			    }
			}
		});
	});
});

function clearInput(){
	$(".remove_clear").val("");
}

</script>
<div class="page-container dataTables_wrapper">
	<form action="${ctx }/basicsinfo/merge" method="post"  id="queryForm" class="form form-horizontal">
		<table id="DataTables_Table_0" class="tab_627">
			<thead >
			</thead>
			<tbody >
			   <tr>
			    	<th colspan="2">说明：将两个帐号的文件和设置合并成为一个用户</th>
			    	
			    </tr> 
			    <tr>
			    	<th width="20%">要合并的第一个用户手机号</th>
			    	<td width="80%"><input class="remove_clear" name="firstMobile" type="text" value="">（合并后使用此用户名登录）</td>
			    </tr> 
			    <tr>
			    	<th width="20%">要合并的第二个用户手机号</th>
			    	<td width="80%"><input class="remove_clear" name="secondMobile" type="text" value="">&nbsp;</td>
			    </tr> 
			    <tr>
			    	<th width="20%"><font color="#3333FF">注意</font></th>
			    	<td width="80%">合并后的用户名为第一个用户的用户名<br></td>
			    </tr> 
			</tbody>
		</table>
		<div align="center"><br>
			<input type="button" value="合并账户" id="btnAdd" class="tab_627_btn">
		</div>
	</form>
</div>
</body>
</html>