$(document).ready(function(){
	var t = $("#treeDemo");
	if(t.length>0){
		t = $.fn.zTree.init(t, setting, zNodes);
	}
	var roleStr = $("#roleStr").val();
	if(roleStr != undefined){
		var roleArr = roleStr.split(',');
		for (var i = 0, len = roleArr.length; i < len; i++){
			$("#check"+roleArr[i]).attr("checked","checked");
		}
	}
	
	$(".permission-list dt input:checkbox").click(function(){
		$(this).closest("dl").find("dd input:checkbox").prop("checked",$(this).prop("checked"));
	});
	
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
							layer.msg(data.codeInfo,{icon: 2,time:2500});
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

/*管理员-用户-添加*/
function admin_admin_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*管理员-用户-编辑*/
function admin_admin_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*管理员-用户-删除*/
function admin_admin_del(obj,url){
	layer.confirm('文件服务器删除须谨慎，确认要删除吗？',function(index){
		$.ajax({
			url : url ,
			dataType : "JSON",
			async : false,
			cache:false,
			success : function (data){
				if(data.code!=1000){
					layer.msg(data.codeInfo,{icon:0,time:1000});
				}else{
					$(obj).parents("tr").remove();
					layer.msg('已删除!',{icon:1,time:1000});
				}
				setTimeout(function(){
					window.location.reload();
            	}, 1000);
			}
		});
	});
}

function admin_admin_status(obj,isAdmin,type,id){
	var _url;
	if(type==1){
		_url =basepath+'/admin/nomal/'+isAdmin+"/"+id;
	}else if(type==0){
		_url =basepath+'/admin/freeze/'+isAdmin+"/"+id;
	}
	$.ajax({
		url : _url ,
		dataType : "JSON",
		async : false,
		cache:false,
		success : function (data){
			if(checkErrorCode(data.code)){
				if(type == 0){
					$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_start(this,'+id+')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>');
					$(obj).parents("tr").find(".td-status:eq(1)").html('<span class="label label-defaunt radius">已停用</span>');
					$(obj).remove();
					layer.msg('已停用!',{icon: 6,time:1000});
					setTimeout(function(){
						window.location.reload();
	            	}, 1000);
				} else if(type == 1){
					$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,'+id+')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>');
					$(obj).parents("tr").find(".td-status:eq(1)").html('<span class="label label-success radius">已启用</span>');
					$(obj).remove();
					layer.msg('已启用!',{icon: 6,time:1000});
					setTimeout(function(){
						window.location.reload();
	            	}, 1000);
				}
			}
		}
	});
}

/*用户-停用*/
function member_stop(obj,isAdmin,id){
	layer.confirm('确认要停用吗？',function(index){
		window.setTimeout(admin_admin_status(obj,isAdmin,0,id),500); 
	});
}

/*用户-启用*/
function member_start(obj,isAdmin,id){
	layer.confirm('确认要启用吗？',function(index){
		window.setTimeout(admin_admin_status(obj,isAdmin,1,id),500); 
	});
}

/*用户-还原*/
function member_huanyuan(id){
	layer.confirm('确认要重置密码？',function(index){
		$.ajax({
			url : _url = basepath + '/admin/initPwd/' + id ,
			dataType : "JSON",
			async : false,
			cache:false,
			success : function (data){
				layer.closeAll();
				if(data.code == 1000){
					layer.msg('重置密码为123456!',{icon: 6,time:2000});
				} else {
					layer.msg(data.codeInfo,{icon: 5,time:2000});
				}
			}
		});
		
	});
}
