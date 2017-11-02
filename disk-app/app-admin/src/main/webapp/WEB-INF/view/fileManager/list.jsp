<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript">
	var identity ='${identity}';
	var token = '${token}';
</script>
<script type="text/javascript" src="${staticpath }/js/fileManager/list.js?V=20160513"></script>
</head>
<body>
<div class="page-container">
	<div class="cl pd-5 bg-1 bk-gray"> 
		<span class="l">
			<a id="addFileUploadBtn" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i>文件上传 </a>
		</span>
	</div>
</div>
</body>
</html>