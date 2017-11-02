<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
</body>
<script type="text/javascript">
$(function(){
	//中间件，调用最上级页面方法：此处为调用协作文件页面websocket发布
	top.fMain(${teamworkMessageId}); // execute main function  
	//父页面刷新
	top.refresh();
	top.layer.closeAll();
});
//parent.parent.fMain(); // execute main function  
</script>
</html>