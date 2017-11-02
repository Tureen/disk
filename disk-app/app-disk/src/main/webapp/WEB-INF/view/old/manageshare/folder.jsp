<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript" src="${plugins }/ztree/jquery.ztree.core.js"></script><!-- zTree -->
<link href="${plugins }/ztree/zTreeStyle/zTreeStyle.css?v=20160241" rel="stylesheet" type="text/css" /><!-- zTree -->
<link href="${plugins }/ztree/demo.css" rel="stylesheet" type="text/css" /><!-- zTree -->
<!-- 移动 -->
<div class="outlayer" id="con_move">
	<div class="tree_container">
     <ul id="treeDemo" class="ztree"></ul>
     </div>
</div>
    <SCRIPT type="text/javascript">
        
    
        var zNodes =[
                 { id:0, pId:0, code:"" ,name:"公共空间", open:true , isParent:true ,"icon": basepath+"/static/images/tip_files.png"},
        ];
       
		var setting = {
			view: {
				selectedMulti: false,
				showLine: false
			},
			simpleData:{
				idkey:"id"
			},
			async: {
				enable: true,
				url:basepath +　"/manageshare/jsonFolderTree",
				autoParam: ["id"],
				dataFilter: filter
			},
			callback: {
				beforeClick: beforeClick,
				beforeAsync: beforeAsync,
				onAsyncError: onAsyncError,
				onAsyncSuccess: onAsyncSuccess
			}
		};

		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}
		function beforeClick(treeId, treeNode) {
			/* if (!treeNode.isParent) {
				return true;
			} else {
				return true;
			} */
		}
		var log, className = "dark";
		function beforeAsync(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
			return true;
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
		}
		
		function showLog(str) {
			if (!log) log = $("#log");
			log.append("<li class='"+className+"'>"+str+"</li>");
			if(log.children("li").length > 8) {
				log.get(0).removeChild(log.children("li")[0]);
			}
		}
		function getTime() {
			var now= new Date(),
			h=now.getHours(),
			m=now.getMinutes(),
			s=now.getSeconds(),
			ms=now.getMilliseconds();
			return (h+":"+m+":"+s+ " " +ms);
		}

		function refreshNode(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			type = e.data.type,
			silent = e.data.silent,
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				layer.msg('请先选择一个父节点！',{icon: 5,time:1000});
			}
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.reAsyncChildNodes(nodes[i], type, silent);
				if (!silent) zTree.selectNode(nodes[i]);
			}
		}

		$(document).ready(function(){
			initTree();
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var node = treeObj.getNodeByParam("id", "0" ,null);
			treeObj.selectNode(node);
			treeObj.expandNode(node,true);
		});
		
		function initTree(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("#refreshNode").bind("click", {type:"refresh", silent:false}, refreshNode);
			$("#refreshNodeSilent").bind("click", {type:"refresh", silent:true}, refreshNode);
			$("#addNode").bind("click", {type:"add", silent:false}, refreshNode);
			$("#addNodeSilent").bind("click", {type:"add", silent:true}, refreshNode);		
		}
		
		//回调创建文件夹
		function callCreateBack(){
		    //获取选中的节点
		    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getSelectedNodes();
            var folderId = 0 
			if (nodes !=null && nodes.length > 0) {
				folderId = treeObj.getSelectedNodes()[0].id;
			}
		}
		
		//回调获取选中的文件夹ID
		function callSelectFolderBack(){
		    //获取选中的节点
		    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getSelectedNodes();
            var folderId = 0 
			if (nodes !=null && nodes.length > 0) {
				folderId = treeObj.getSelectedNodes()[0].id;
				var code = treeObj.getSelectedNodes()[0].code;
				var jsonFolder = {
					"folderId" : folderId,
					"folderCode" : code
				}
				return jsonFolder;
			}
            return null;
		}
		//-->
	</SCRIPT>
</body>
</html>