$(document).ready(function(){
	var t = $("#treeDemo");
	t = $.fn.zTree.init(t, setting, zNodes);
	
});

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
		onClick:onClick
	}
};

function onClick(e, treeId,treeNode){
	$("#deptName").val(treeNode.name);
	$("#deptId").val(treeNode.id);
}

function showMenu() {
	$("#deptName").val("");
	var cityObj = $("#deptName");
	var cityOffset = $("#deptName").position();
	
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent")) {
		if((event.target.className).indexOf('switch')==-1){
			hideMenu();
		}
	}
}



//关闭打开的layer
function layerClose(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.location.reload();
}