<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${staticpath }/js/stream-v1.8/css/stream-v1.css?v=20160830" />
</head>
<body>
<!-- 上传 -->
<div id="con_upload" class="streamOutlayer">
    <div id="i_select_files" title="<fmt:message key="i18n_global_upload_prompt_1" bundle="${i18n}" />"></div>
	<div id="i_stream_files_queue" style="width: 100%;"></div>
</div>
<div class="layui-layer-btn" style="padding-top: 10px;">
	<p class="streamRemark"><i class="tips_zs"></i><fmt:message key="i18n_global_single_file_support_max_filesize" bundle="${i18n}" />${maxSize }MB<br/>${uploadTypeDescribe }</p>
	<a id="upload" onclick="javascript:_t.upload();" class="layui-layer-btn0"><fmt:message key="i18n_global_upload_start" bundle="${i18n}" /></a>
	<a id="stop" onclick="javascript:_t.stop();" class="layui-layer-btn0"><fmt:message key="i18n_global_upload_stop" bundle="${i18n}" /></a>
	<a id="stop" onclick="javascript:_t.cancel();" class="layui-layer-btn0"><fmt:message key="i18n_global_cancel_upload" bundle="${i18n}" /></a>
</div>
</body>
<!--引入JS-->
<script type="text/javascript" src="${staticpath }/js/stream-v1.8/js/stream-v1.js?v=20160830"></script>
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
		tokenURL : "${ctx }/streamUpload/token",
		uploadURL : "${ctx }/streamUpload/upload",
		browseFileId : "i_select_files",
		browseFileBtn : "<fmt:message key="i18n_global_upload_prompt_3" bundle="${i18n}" />",
		dragAndDropArea: "i_select_files",
		dragAndDropAreaCopy: "con_upload",
		dragAndDropTips: "<fmt:message key="i18n_global_upload_prompt_2" bundle="${i18n}" />",
		filesQueueId : "i_stream_files_queue",
		filesQueueHeight : 239,
		messagerId : "i_stream_message_container",
		multipleFiles: true,
		maxSize : 81920 * 1024 * 1024,
		autoUploading : false,
		totalSize : usableDiskSize,
		postVarsPerFile: {
			folderId : ${currentFolder.id},
			spaceType : "${spaceType}"
		},
		onRepeatedFile: function(f) {
			layer.msg(i18n_global_folder_upload_text_1 + f.name + i18n_global_folder_upload_text_2 + f.size + i18n_global_folder_upload_text_3 ,{icon:5, time:4000});
			return false;	
		},
		onFileCountExceed: function(selected, limit){
			layer.msg(i18n_global_folder_upload_text_4 + selected + i18n_global_folder_upload_text_5 + limit + i18n_global_folder_upload_text_6 ,{icon:5, time:4000});
		},
		onCancel : function(file) {
			usableDiskSize += file.size;
		},
		onSelect: function(list) {
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
			}
			return true;
		}
	};
	var _t = new Stream(config);
</script>
</html>