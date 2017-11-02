$(document).ready(function(){
	
	$("#queryForm").validate({
		rules:{
			customerName:{
				required:true,
			},
			contacts:{
				required:true,
			},
			contactsMobile:{
				required:true,
			},
			clientCode:{
				required:true,
			},
			licenseCode:{
				required:true,
			},
			licenseHour:{
				required:true,
			},
			registerNum:{
				required:true,
			}
		},
		messages:{
			customerName:"客户名称不能为空",
			contacts:"联系人不能为空",
			contactsMobile:"联系人电话不能为空",
			clientCode:"客户端生成编码不能为空",
			licenseCode:"授权码不能为空",
			licenseHour:"授权时间不能为空",
			registerNum:"允许注册员工数不能为空"
		},
		onkeyup:false,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			var _url = $("#queryForm").attr('action');
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#queryForm").serialize(),
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




//关闭打开的layer
function layerClose(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.location.reload();
}