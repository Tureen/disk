<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<title><fmt:message key="i18n_global_app_start_preview_service" bundle="${i18n}" /></title>
<script type="text/javascript">
$(document).load(
	"${ctx}/authorize/login", 
	{
		identity: '${identity}',
		token: '${token}'
	}, 
	function(response,status,xhr){
		if(status == "success"){
			window.location.href = "${url}";
		}else{
			layer.msg(i18n_app_cannot_start_preview_service, {icon: 7,time:3000});
		}
	}
);
</script>
</head>
<body>

</body>
</html>