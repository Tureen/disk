$(document).ready(function(){
	var t = $("#treeDemo");
	if(t.length!=0){
		t = $.fn.zTree.init(t, setting, zNodes);
	}
	var roleStr = $("#roleStr").val();
	if(roleStr != undefined){
		var roleArr = roleStr.split(',');
		for (var i = 0, len = roleArr.length; i < len; i++){
			$("#check"+roleArr[i]).attr("checked","checked");
		}
	}
	var permissionStr = $("#permissionStr").val();
	if(permissionStr != undefined){
		var permissionArr = permissionStr.split(',');
	}
	//判断是新增还是修改
	var savetype = $("#savetype").val();
	if(savetype==1){
		$("#permissionAll").find("input[type='checkbox']").each(function(){
			$(this).attr("checked", true);
		});
	}else{
		if(permissionArr!=null && permissionArr.length>0){
			for (var i = 0, len = permissionArr.length; i < len; i++){
				$("#checkpermission"+permissionArr[i]).attr("checked","checked");
				$("#checkpermission"+permissionArr[i]).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",true);
			}
		}
	}
	
	//全选
	$("#selectAll").click(function(){
		var checkbool = false;
		$("#permissionAll").find("input[type='checkbox']").each(function(){
			if(!$(this).prop("checked")){
				checkbool = true;
			}
		});
		$("#permissionAll").find("input[type='checkbox']").each(function(){
			$(this).prop("checked",checkbool);
		});
	});
	
	$(".permission-list dt input:checkbox").click(function(){
		$(this).closest("dl").find("dd input:checkbox").prop("checked",$(this).prop("checked"));
		if($(this).prop("checked")){
			$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",true);
		}else{
			var l=$(this).parents(".permission-list").find("input:checked").length;
			if(l==1){
				$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",false);
			}
		}
	});
	
	$("#form-admin-admin-add").validate({
		rules:{
			clusterName:{
				required:true,
			},
			clusterIp:{
				required:true,
			},
			clusterCode:{
				required:true,
			},
			filePath:{
				required:true,
			},
			fileUrl:{
				required:true,
			},
			sharePath:{
				required:true,
			},
			reserveSpace:{
				required:true,
			}
		},
		messages:{
			clusterName:"名称不能为空",
			clusterIp:"ip地址不能为空",
			clusterCode:"集群编号不能为空",
			filePath:"文件存储目录不能为空",
			fileUrl:"文件服务器地址不能为空",
			sharePath:"共享目录不能为空",
			reserveSpace:"预留空间大小不能为空"
		},
		onkeyup:false,
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
							layer.msg(data.codeInfo,{icon: 2,time:2000});
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
	$("#deptName").val(treeNode.name);
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