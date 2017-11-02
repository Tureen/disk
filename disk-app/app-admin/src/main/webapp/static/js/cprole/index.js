/*管理员-角色-添加*/
function admin_role_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*管理员-角色-编辑*/
function admin_role_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*管理员-角色-删除*/
function admin_role_del(obj,url){
	layer.confirm('角色删除须谨慎，确认要删除吗？',function(index){
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
			}
		});
	});
}

function admin_role_status(obj,type,id){
	var _url;
	if(type==1){
		_url = basepath + '/cprole/nomal/' + id;
	}else if(type==0){
		_url = basepath + '/cprole/freeze/' + id;
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
					$(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已停用</span>');
					$(obj).remove();
					layer.msg('已停用!',{icon: 5,time:1000});
				} else if(type == 1){
					$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,'+id+')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>');
					$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
					$(obj).remove();
					layer.msg('已启用!',{icon: 6,time:1000});
				}
			}
		}
	});
}

/*用户-停用*/
function member_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		window.setTimeout(admin_role_status(obj,0,id),500); 
	});
}

/*用户-启用*/
function member_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		window.setTimeout(admin_role_status(obj,1,id),500); 
	});
}