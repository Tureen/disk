function hideNode() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	
	var nodeList = zTree.getNodesByParam("pId", "00");
	for(var i=0;i<nodeList.length;i++){
		zTree.expandNode(nodeList[i],false);
	}
}
function showNode() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var nodeList = zTree.getNodesByParam("pId", "00");
	for(var i=0;i<nodeList.length;i++){
		zTree.expandNode(nodeList[i],true);
	}
}

$('body').bind('treeNodeClick', function(e, treeId, node) {
});

function onRightClick(e,treeId, treeNode){
	if(treeNode){
		$("#deptId").val(treeNode.id);
		onbodyremove();
		removeOperateMenu();
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		zTree.selectNode(treeNode);
		addOperateMenu(treeId,treeNode);
	}
}

function onbodyremove(){
   $('body').click(function(e){
   	  var btnIds = $('.button.add,.button.edit,.button.remove').parent().attr('id');
   	  var targetId = $(e.target).attr('id');
   	  if(btnIds != targetId){
   	  	 removeOperateMenu();
   	  }
   })
}

function removeOperateMenu(){
	if($('.button.add').length>0){
		$('.button.add').remove();
	}
	if($('.button.edit').length>0){
		$('.button.edit').remove();
	}
	if($('.button.remove').length>0){
		$('.button.remove').remove();
	}
}

function addOperateMenu(treeId, treeNode){
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='添加部门' onfocus='this.blur();'></span>";
	if (treeNode.editNameFlag || $("#editBtn_"+treeNode.tId).length>0) return;
	var editStr = "<span class='button edit' id='editBtn_" + treeNode.tId
		+ "' title='修改部门' onfocus='this.blur();'></span>";
	if (treeNode.editNameFlag || $("#removeBtn_"+treeNode.tId).length>0) return;
	var removeStr = "<span class='button remove' id='removeBtn_" + treeNode.tId
		+ "' title='删除部门' onfocus='this.blur();'></span>";
	if(treeNode.level == 5){
		sObj.after(removeStr);
		sObj.after(editStr);
	}else{
		sObj.after(removeStr);
		sObj.after(editStr);
		sObj.after(addStr);
	}
	var addbtn = $("#addBtn_"+treeNode.tId);
	$("#deptName").val('');
	if (addbtn) addbtn.bind("click", function(){
		removeOperateMenu();
		// 标志1为添加
		$("#type").val("1");
		// 添加弹出层代码
		la = layer.open({
			type:1,
			title:"添加部门",
			area:"350px",
			content:$("#addOrEdit"),
			end:function(e){
			}
		});
	});
	
	
	$("#cancelBtn").click(function(){
		layer.close(la);
	})
	var editbtn = $("#editBtn_"+treeNode.tId);
	if (editbtn) editbtn.bind("click", function(){
		removeOperateMenu();
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		$("#type").val("2");
		$("#deptName").val(treeNode.name);
		// 添加弹出层代码
		la = layer.open({
			type:1,
			title:"修改部门",
			area:"350px",
			content:$("#addOrEdit"),
			end:function(e){
			}
		});
		return false;
	});
	
	var removebtn = $("#removeBtn_"+treeNode.tId);
	if (removebtn) removebtn.bind("click", function(){
		removeOperateMenu();
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		if(confirm("确认删除部门 '" + treeNode.name + "'?")){
			// 检查是否可以删除的条件
		    $.ajax({
				type:"POST",
				url:basepath + "/dept/delDept/"+treeNode.id,
				dataType:"json", 
			    data: data,
			    async: false,
				success:function(data){
					if(data.code=="1000"){
						layer.msg("已删除",{icon: 1,time:1000});
						zTree.removeNode(treeNode);
					} else {
						layer.msg(data.codeInfo,{icon: 2,time:1000});
					}
				}
			}); 
		}
		return false;
	});		
}

function addOrEdit(){
	var deptName = $("#deptName").val();
	var bool = checkNumber(deptName);
	if(!bool){
		return false;
	}
	var type = $("#type").val();
	var ajaxPath = basepath;
	var jsData;
	if(type == "1"){
		// 添加部门
		ajaxPath = basepath + "/dept/addDept";
		jsData = {
			"deptName":$("#deptName").val(),
			"parentId":$("#deptId").val()
		}
	} else {
		// 修改部门
		ajaxPath = basepath + "/dept/editDept";
		jsData = {
			"deptName":$("#deptName").val(),
			"id":$("#deptId").val()
		}
	} 
 	 $.ajax({
		type:"POST",
		url:ajaxPath,
		dataType:"json", 
	    data: jsData,
	    async: false,
		success:function(data){
			if(data.code=="1000"){
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = zTree.getSelectedNodes();
				if(type == "1" && nodes.length > 0){
					zTree.addNodes(nodes[0], {id: data.result.id, 
						pId:data.result.parentId, name:data.result.deptName});
					layer.close(la);
				} else if(type == "2" && nodes.length > 0) {
					nodes[0].name = jsData.deptName;
					zTree.updateNode(nodes[0]);
					layer.close(la);
				}
			}
		}
	}); 
	return false;
}

function checkNumber(name){
	var str = name.substring(0,1);
	if(name.trim().length == 0){
		layer.msg('部门名称不能为空！',{icon: 0,time:1000});
		return false;
	}
	if(!isNaN(str)){
		layer.msg('部门第一个字不能为数字!',{icon: 0,time:1000});
		return false;
	}
	return true;
}

function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.expandNode(treeNode,true);
	var zTree = $.fn.zTree.getZTreeObj("tree");
	demoIframe.attr("src", basepath + "/dept/treeright/" + treeNode.id);
	return true;
}		
