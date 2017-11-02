<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<style type="text/css">
.layui-layer-btn {
	padding-top: 12px;
}
</style>
<script type="text/javascript">
$(function(){
	layer.alert(
			i18n_global_account_other_where_login, 
		{
			title : i18n_global_account_offline_prompt,
			icon: 4
		},
		function(index){
			layer.close(index);
			window.location.href = "${ctx}/";
		}); 
	});
</script>