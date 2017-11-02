<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${staticpath }/js/stream-v1.8/css/stream-v1.css?v=20160830" />
<style type="text/css">
.stream-browse-drag-files-area span {
	font-size: 12px;
}

.myFont{
	font-size: 20px !important;
	font-weight: bold;
	line-height: 26px;
	color: red !important;
}

.myFont1{
	font-size: 14px !important;
	font-weight: bold;
	color: #c1c1c1 !important;
}

.tabs_upload li p.p2 {
    width: 35%;
}

.tabs_upload li .filebox span.name_upload{
	width: 320px;
}

.streamOutlayer{
	padding: 0px 20px 0px 20px;
}

.layui-layer-btn {
    margin-top: -3px;
    border-top: 0px solid #e5e5e5;
    text-align: right;
    padding: 0 10px 12px;
    pointer-events: auto;
}
</style>
<script id="uploadAreaMessage" type="text/template">
<ul class="tabs_upload" style="height: 369px;">
	<div style="padding-top:60px;" id="selectText"><img src="${ctx}/static/images/addFile.png" width="150px" height="150px" /><br/><fmt:message key="i18n_pcclient_upload_html_1" bundle="${i18n}" /></div>
</ul>
</script>
<script id="UploadFileModule" type="text/template">
<li id="#FILE_ID#">
	<div class="upload_jdt" style="width:0%"></div>
	<p class="filebox"><i class="tipbg tips_bg_#FILE_ICON#"></i><span class="name_upload" title="#FILE_NAME#">#FILE_NAME#</span></p>
	<p class="p2">#FILE_SIZE#</p>
	<p class="p4"><a href="javascript:void(0);" class="upload_no"></a></p>
</li>
</script>
</head>
<body>
<!-- 上传 -->
<div id="con_upload" class="streamOutlayer">
    <div id="i_select_files" title="<fmt:message key="i18n_global_upload_prompt_1" bundle="${i18n}" />" style="padding: 0px 0px;">
    
    </div>
</div>
<div class="layui-layer-btn">
	<p class="streamRemark"><i class="tips_zs"></i><fmt:message key="i18n_global_single_file_support_max_filesize" bundle="${i18n}" />${maxSize }MB<br/>${uploadTypeDescribe }</p>
	<a id="upload" onclick="javascript:_t.upload();" class="layui-layer-btn0"><fmt:message key="i18n_global_upload_start" bundle="${i18n}" /></a>
	<a id="stop" onclick="javascript:_t.stop();" class="layui-layer-btn0"><fmt:message key="i18n_global_upload_stop" bundle="${i18n}" /></a>
	<a id="stop" onclick="javascript:_t.cancel();" class="layui-layer-btn0"><fmt:message key="i18n_global_cancel_upload" bundle="${i18n}" /></a>
</div>
</body>
<!--引入JS-->
<script type="text/javascript" src="${staticpath }/js/stream-v1.8/js/stream-v1.js?v=20160930"></script>
<script type="text/javascript">
	//var maxSize = ${maxSize == "" ? "2048" : maxSize};//单个允许最大大小
	var validateRule = ${validateRule};//文件类型验证规则
	var uploadModel = ${uploadModel};//验证模式
	var maxSize = ${maxSize};//单个允许最大大小
	var usableDiskSize = ${usableDiskSize};
	var isPromptMsg = false;//是否弹框提示
	var msg = i18n_global_select_file_folder_contain;//提示内容
	var msg1 = "";
	var count = 1;//计数器
	
	var config = {
		enabled: true, /** 是否启用文件选择，默认是true */
		customered : true,
		multipleFiles: true,
		tokenURL : "${ctx }/streamUpload/token",
		uploadURL : "${ctx }/streamUpload/upload",
		browseFileId : "i_select_files",
		dragAndDropArea: "i_select_files",
		dragAndDropTips : $('#uploadAreaMessage').text().trim(),
		maxSize : 81920 * 1024 * 1024,
		autoUploading : false,
		totalSize : usableDiskSize,
		postVarsPerFile: {
			folderId : ${currentFolder.id},
			spaceType : "${spaceType}"
		},
		onRepeatedFile: function(f) {
			usableDiskSize += f.size;
			$("#" + f.id).remove();
			layer.msg(i18n_global_folder_upload_text_1 + f.name + i18n_global_folder_upload_text_2 + f.size + i18n_global_folder_upload_text_3 ,{icon:5, time:4000});
			return false;	
		},
		onFileCountExceed: function(selected, limit){
			layer.msg(i18n_global_folder_upload_text_4 + selected + i18n_global_folder_upload_text_5 + limit + i18n_global_folder_upload_text_6 ,{icon:5, time:4000});
		},
		onCancel : function(file) {
			usableDiskSize += file.size;
			$("#" + file.id).remove();
		},
		onSelect: function(list) {
			$("#selectText").hide();
			for(fileSub in list){
				var file = list[fileSub];
				if(!illegalChar(file.name)){
					msg1 = i18n_global_folder_upload_validate_1;
					isPromptMsg = true;
					break;
				}
				if(!validateFileTypeIsUpload(file.name, uploadModel, validateRule)){
					msg1 = i18n_global_folder_upload_validate_2;
					isPromptMsg = true;
					break;
				}
				if(!validateAllowUploadMaxSize(file.size, maxSize)){
					msg1 = i18n_global_folder_upload_validate_3 + maxSize + "MB";
					isPromptMsg = true;
					break;
				}
				if(parseFloat(file.size) > parseFloat(usableDiskSize)){
					msg = i18n_global_folder_upload_validate_4;
					isPromptMsg = true;
					break;
				}else{
					usableDiskSize -= file.size;
				}
			}
			if(isPromptMsg){//每批次添加结束后，再次添加会进行提醒
				msg1 += i18n_global_folder_upload_validate_5;
				layer.msg(msg + msg1 ,{icon:5, time:5000});
				isPromptMsg = false, msg1 = "";
				return false;
			}else{//通过了校验才一同添加进列表
				for(fileSub in list){
					var file = list[fileSub];
					var tmpl = $("#UploadFileModule").text().trim();
					var instance = tmpl.replace(/#FILE_ID#/g, file.id)
					.replace(/#FILE_ICON#/g, getFileIcon(file.name))
					.replace(/#FILE_NAME#/g, file.name)
					.replace(/#FILE_SIZE#/g, _t.formatBytes(file.size));
					$(".tabs_upload").append(instance);
					
					//删除文件
					$("#" + file.id).find(".upload_no").click(function(e) {
						e.stopPropagation();
						_t.cancelOne($(this).parent().parent().attr("id"));
					});
				}
			}
			return true;
		},
		onQueueComplete: function(msg){
			//设置自动滚动条
			var scrollHeight = $(".tabs_upload")[0].scrollHeight;
			$(".tabs_upload").scrollTop(scrollHeight);
			
			layer.msg(i18n_global_folder_upload_finish ,{icon:1, time:3000});
			console.log("send");
			var ipcRenderer = require('electron').ipcRenderer;
			ipcRenderer.on('ping', function() {
				ipcRenderer.sendToHost(i18n_global_folder_upload_finish);
			});
		},
		onUploadProgress: function(file) {
			var $bar = $("#"+file.id).find(".upload_jdt");
			$bar.css("width", file.percent + "%");
		},
		onComplete: function(file) {//单个文件上传完毕的响应事件
			$("#"+file.id).find(".upload_jdt").css("width", "100%");
			$("#"+file.id).find(".p4").hide();
			//设置自动滚动条
			var uploadWindowHeight = $("#i_select_files").height();
			var currentProgressHeight = $("#"+file.id).offset().top;
			if(currentProgressHeight > uploadWindowHeight){
				$(".tabs_upload").scrollTop(currentProgressHeight);
			}
		}
	};
	var _t = new Stream(config);
</script>
</html>