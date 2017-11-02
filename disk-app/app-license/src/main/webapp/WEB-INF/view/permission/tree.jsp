<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<table class="table">
	<tr>
		<td width="200" class="va-t"><ul id="treeDemo" class="ztree"></ul></td>
		<td class="va-t"><IFRAME ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO width=100%  height=700px SRC="${ctx}/permission/toinsertpermission/1"></IFRAME></td>
	</tr>
</table>
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript" src="${staticpath }/js/permission/tree.js"></script>
<script type="text/javascript">
var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: ""
		}
	},
	callback: {
		beforeClick: beforeClick
	}
};
	
var zNodes = [];
 <c:forEach items="${allfuncs}" var="petree">
  var data = { id: "<c:out value="${petree.id}"/>", pId: "<c:out value="${petree.permissionFid}"/>", name: "<c:out value="${petree.permissionName}"/>",type: "<c:out value="${petree.permissionType}"/>", open: true };
  zNodes.push(data);
</c:forEach>
</script>
</body>
</html>