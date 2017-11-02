$(document).ready(function(){
	$("#permissionForm").validate({
		 rules:{
			 permissionName:{
				required:true,
			},
			permissionCode:{
				required:true,
			},
			permissionUrl:{
				required:true,
			},
			permissionIcon:{
				required:true,
			},
		},
		messages:{
			permissionName:"请填写分类名称",
			permissionCode:"请填写权限编码",
			permissionUrl:"请填写导航URL",
			permissionIcon:"请填写权限图标",
		},
		onkeyup:false,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			var _url = $("#permissionForm").attr('action');
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#permissionForm").serialize(),
				type: 'POST',
				async : false,
				cache:false,
				success : function (data){
					if(checkErrorCode(data.code)){
						layer.msg('操作成功!',{icon: 1,time:1000});
						setTimeout(layerClose,1000);
				    }
				}
			});
		}
	});
});

//关闭打开的layer
function layerClose(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.parent.location.reload();
}