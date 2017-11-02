<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<style type="text/css">
.menuContent{
    height:100px;
    overflow-x:hidden;
    overflow-y:scroll;
    background-color:#F0F0F0;
    border:1px solid #f0f0f0;
    z-index: 999;
}
</style>
<script type="text/javascript">
$(function(){
	$("#admin-admin-save").click(function () {
	    var formData = new FormData($( "#queryForm" )[0]);  
	     $.ajax({  
	          url: basepath + '/dept/import',
	          type: 'POST',  
	          data: formData,  
	          dataType:"json",
	          async: false,  
	          cache: false,  
	          contentType: false,  
	          processData: false,  
	          success: function (data) {  
	        	  if(data.code == 1000){
	        			layer.msg('上传成功',{icon: 6,time:1000});
	        			layerClose();
	        		}else{
	        		    layer.msg(data.codeInfo,{icon: 5,time:1000});
	        			return false;
	        		} 
	          },  
	          error: function (data) {  
	              alert("上传失败");  
	          }  
	     }); 
	});
	  
});
</script>
<div class="page-container">
  <form method="post"  id="queryForm" class="form form-horizontal" enctype="multipart/form-data">
 <div class="row cl">
            <input type="hidden" class="input-text" value="${department.id }" name = "deptId"/>
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>选择部门：</label>
			<div class="formControls col-xs-8 col-sm-7">
			<input type="text" class="input-text" value="${department.deptName }" readonly="readonly" />
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>下载模版：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<a class="btn btn-primary radius" href="${ctx }/download/import.xlsx"><i class="Hui-iconfont">&#xe640;</i>下载模版</a> 
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>上传文件：</label>
			<div class="formControls col-xs-8 col-sm-7">
			<span class="btn-upload form-group">
			<input class="input-text upload-url" type="text" name="uploadfile-2" id="uploadfile-2" readonly  datatype="*" nullmsg="请添加附件！" style="width:200px">
			<a href="javascript:void();" class="btn btn-primary upload-btn"><i class="Hui-iconfont">&#xe642;</i> 浏览文件</a>
			<input type="file" multiple name="file" class="input-file">
			</span> 
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="button" class="btn btn-success radius" id="admin-admin-save" name="admin-admin-save"><i class="icon-ok"></i> 确定</button>
			</div>
		</div>
  </form>
</div>
</body>
</html>
