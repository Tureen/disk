$(function(){
	var permissionStr = $("#permissionStr").val();
	var permissionArr = permissionStr.split(',');
	for (var i = 0, len = permissionArr.length; i < len; i++){
		$("#check"+permissionArr[i]).attr("checked","checked");
	}
	
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
	$(".permission-list2 dd input:checkbox").click(function(){
		var l =$(this).parent().parent().find("input:checked").length;
		var l2=$(this).parents(".permission-list").find(".permission-list2 dd").find("input:checked").length;
		if($(this).prop("checked")){
			$(this).closest("dl").find("dt input:checkbox").prop("checked",true);
			$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",true);
		}
		else{
			if(l==0){
				$(this).closest("dl").find("dt input:checkbox").prop("checked",false);
			}
			if(l2==0){
				$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",false);
			}
		}
	});
	
	$("#form-admin-role-add").validate({
		rules:{
			roleName:{
				required:true,
			},
		},
		messages:{
			roleName:"角色名称不能为空",
		},
		onkeyup:false,
		focusCleanup:false,
		success:"valid",
		submitHandler:function(form){
			var _url = $("#form-admin-role-add").attr('action');
			$.ajax({
				url :  _url,
				dataType : "JSON",
				data : $("#form-admin-role-add").serialize(),
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
	parent.location.reload();
}