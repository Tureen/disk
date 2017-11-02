$(document).ready(function(){
	var t = $("#treeDemo");
	t = $.fn.zTree.init(t, setting, zNodes);
	
	
	$("#form-admin-admin-add").validate({
		rules:{
			employeeMobile:{
				required:true,
			},
			employeeCode:{
				required:true,
			},
			deptName:{
				required:true,
			},
			employeeName:{
				required:true,
			}
		},
		messages:{
			employeeMobile:"手机号码不能为空",
			employeeCode:"员工编号不能为空",
			deptName:"请选择部门",
			employeeName:"姓名不能为空"
		},
		onkeyup:true,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			var _url = $("#form-admin-admin-add").attr('action');
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#form-admin-admin-add").serialize(),
				type: 'POST',
				async : false,
				cache:false,
				success : function (data){
					if(checkErrorCode(data.code)){
						if(data.code=="1000"){
							layer.msg('操作成功!',{icon: 1,time:1000});
							setTimeout(layerClose,1000);
						} else {
							layer.msg(data.codeInfo,{icon: 2,time:1000});
						}
				    }
				}
			});
		}
	});
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
	$("#deptShowName").val(treeNode.name);
	$("#deptId").val(treeNode.id);
}

function showMenu() {
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