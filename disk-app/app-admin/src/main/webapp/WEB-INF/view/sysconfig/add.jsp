<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<style type="text/css">
.menuContent{
    height:100px;
    width:428px;
    overflow-x:hidden;
    overflow-y:scroll;
    background-color:#F0F0F0;
    border:1px solid #f0f0f0;
    z-index:999;
}
</style>
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<script type="text/javascript">
$(document).ready(function(){
	$("#queryForm").validate({
		rules:{
			configKey:{
				required:true,
			}
		},
		messages:{
			configKey:"属性值不能为空"
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
</script>
<title>修改权限</title>
</head>
<body>
<div class="page-container">
  <form action="${ctx }/commonbasedata/addsysconfig" method="post"  id="queryForm" class="form form-horizontal">
    <div class="row cl">
      <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>配置属性：</label>
      <div class="formControls col-xs-8 col-sm-7">
        <input type="text" class="input-text" value="" id="configKey" name="configKey" placeholder="" onclick="showMenu('${permission.id}'); return false;" >
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>属性值：</label>
      <div class="formControls col-xs-8 col-sm-7">
        <input type="text" class="input-text" value="" name="configValue" placeholder=""  name="permissionName" id="permissionName">
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
        <input class="btn btn-primary radius" id="btnAdd" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>
</body>
</html>