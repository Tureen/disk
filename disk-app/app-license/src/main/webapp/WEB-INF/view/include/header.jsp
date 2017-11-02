<%@ page language="java" pageEncoding="UTF-8"%>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> <span id="parentName"></span> <span class="c-gray en">&gt;</span> <span id="titleName"></span> <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<script type="text/javascript">
$(function(){
	var parentName = $('#_selected', parent.document).parent().parent().parent().parent().find('span').html();
	var titleName = $('#_selected', parent.document).html();
	$("#parentName").html(parentName);
	$("#titleName").html(titleName);
})
</script>