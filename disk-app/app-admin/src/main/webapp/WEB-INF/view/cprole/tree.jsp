<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
    <TITLE> ZTREE DEMO - Simple Data</TITLE>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/demo.css?v=2016" type="text/css">
    <link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/metroStyle/metroStyle.css?v=2015" type="text/css">
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.exedit-3.5.js"></script>
    <SCRIPT type="text/javascript">
    
    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    	demoIframe = $("#testIframe");
        
    });
    
      <!--
      var setting = {
          view: {
              selectedMulti: false,
              showIcon: false,
          	  showLine: false
          },
          data: {
              simpleData: {
                  enable: true,
              	idKey: "id",
  				pIdKey: "pId",
  				rootPId: ""
              }
          },
          edit: {
              enable: false,
          },
          callback: {
  			beforeClick: beforeClick
  		}
      };

      var zNodes = [];
      <c:forEach items="${depts}" var="dept">
      var data = 
        { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true};
      zNodes.push(data);
      </c:forEach>

      function showIconForTree(treeId, treeNode) {
      	return treeNode.level != 2;
      };
      
      var newCount = 1;
      
      function beforeClick(treeId, treeNode) {
    		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    		zTree.expandNode(treeNode,true);
    		var zTree = $.fn.zTree.getZTreeObj("tree");
    		demoIframe.attr("src", basepath + "/cprole/treeright/" + treeNode.id+"?roleId="+${roleId});
    		return true;
      }	
        //-->
    </SCRIPT>
</HEAD>
<BODY>
<table class="table" style="height: 100%">
	<tr>
		<td width="251px" class="va-t" style="padding:0;">
			<div class="content_wrap">
			   <div class="zTreeDemoBackground left">
			       <div class="zzjg_top">组织架构</div>
			       <ul id="treeDemo" class="ztree"></ul>
			   </div>
			</div>
		</td>
		<td class="va-t" style="height: 100%"><IFRAME ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO width=100%  height=100% SRC="${ctx}/cprole/treeright/00?roleId=${roleId}"></IFRAME></td>
	</tr>
</table>
</BODY>
</HTML>