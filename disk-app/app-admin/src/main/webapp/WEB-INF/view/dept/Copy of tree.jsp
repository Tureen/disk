<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyleDept.css" type="text/css">
<style type="text/css">
.btn1 {
   font-size: 9pt;
    color: #003399;
    border: 1px #003399 solid;
    color: #006699;
    border-bottom: #93bee2 1px solid;
    border-left: #93bee2 1px solid;
    border-right: #93bee2 1px solid;
    border-top: #93bee2 1px solid;
    /* background-image: url(../images/bluebuttonbg.gif); */
    background-color: #e8f4ff;
    cursor: hand;
    font-style: normal;
    width: 95px;
    height: 22px;
}
</style>
<table class="table">
	<tr>
		<td width="200" class="va-t">
			<div style="padding: 5px;"><button class="btn1" onclick="showNode()">展开</button><button class="btn1" onclick="hideNode()">收起</button></div>
			<ul id="treeDemo" class="ztree"></ul>
		</td>
		<td class="va-t" style="height: 500px"><IFRAME ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO width=100%  height=800px SRC="${ctx}/dept/treeright/00"></IFRAME></td>
	</tr>
</table>
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript" src="${staticpath }/js/dept/tree.js"></script>
<script>
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
			beforeClick: beforeClick,
			onRightClick:onRightClick
		}
	};
	
var zNodes = [];
<c:forEach items="${depts}" var="dept">
var data = 
  { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true ,
		<c:choose>
			<c:when test="${dept.parentId=='0'}">icon:"${staticpath }/images/icon-53.png"</c:when>
			<c:otherwise>icon:"${staticpath }/images/icon-54.png"</c:otherwise>
		</c:choose>
	};
zNodes.push(data);
</c:forEach>

$(document).ready(function(){
	var t = $("#treeDemo");
	t = $.fn.zTree.init(t, setting, zNodes);
	demoIframe = $("#testIframe");
	demoIframe.bind("load", loadReady);
});
</script>
</body>
</html>
<div>
   <div id ="addOrEdit" style= "display: none;padding:10px;">
		<table class="formtable" style="table-layout:fixed;">
			<tbody>
				<tr>
					<td class="label" style="width:30%;text-align:center;">部门名称：</td>
					<td class="inputs" style="width:59%;text-align:left;">
						<input type="text" class="input1" name="deptName" id="deptName" maxlength = "32"/><span class="tc-f00">*</span>
						<input type="hidden" class="input1" name="deptId" id="deptId" />
						<input type="hidden" class="input1" id="type" value = ""/>
					</td>
				</tr>
				<tr>
				    <td colspan="2" style="padding-top:5px;text-align: center;">
				    	<input type="hidden" class="input1" name="rcode" id="rcode" value="SYSTEM" />
				    	<input type="button" id="" class="btn2" onclick="addOrEdit()" value="确定" style="margin-right:20px;"/>
				    	<input type="button" id="cancelBtn" class="btn2" style="background:#F8C471;" value="取消"/>
				    </td>
				</tr>
			</tbody>
		</table>
	</div>
</div> 