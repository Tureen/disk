
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
	layer.confirm('管理员删除须谨慎，确认要删除吗？',function(index){
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

