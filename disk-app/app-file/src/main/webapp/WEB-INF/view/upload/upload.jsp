<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${staticpath }/js/webuploader-0.1.5/webuploader.css?v=20160528" />
<!--引入JS-->
<script type="text/javascript" src="${staticpath }/js/md5.js"></script>
<script type="text/javascript" src="${staticpath }/js/webuploader-0.1.5/webuploader.js"></script>
</head>
<body>
<!-- 上传 -->
<div id="con_upload" class="outlayer">
    <div class="upload_top">
		<div id="picker">选择文件</div>
    	<span title="${currentFolder.folderName }"><fmt:message key="i18n_global_to" bundle="${i18n}" />：${fn:length(currentFolder.folderName) > 30 ? fn:substring(currentFolder.folderName, 0, 30) : currentFolder.folderName }${fn:length(currentFolder.folderName) > 30 ? "……" : ""}</span>
        <div class="select_contain"><span><fmt:message key="i18n_global_same_file_name" bundle="${i18n}" />：</span>
            <div class="select_down">
                <p><fmt:message key="i18n_global_overlay_file" bundle="${i18n}" /></p>
                <div>
                    <p date-text="<fmt:message key="i18n_global_cancel_upload" bundle="${i18n}" />" date-value="1" class="selected"><fmt:message key="i18n_global_cancel_upload" bundle="${i18n}" /></p>
                    <p date-text="<fmt:message key="i18n_global_overlay_file" bundle="${i18n}" />" date-value="2"><fmt:message key="i18n_global_overlay_file" bundle="${i18n}" /></p>
                    <p date-text="<fmt:message key="i18n_global_upgraded_version" bundle="${i18n}" />" date-value="3"><fmt:message key="i18n_global_upgraded_version" bundle="${i18n}" /></p>                         
                </div>
            </div>
        </div>
    </div>
    <table class="tabs_th" style="background:#ececec">
        <tr>               
            <th width="50%"><fmt:message key="i18n_global_file_name" bundle="${i18n}" /></th>
            <th width="20%"><fmt:message key="i18n_global_file_size" bundle="${i18n}" /></th>
            <th width="15%"><fmt:message key="i18n_global_file_status" bundle="${i18n}" /></th>
            <th width="15%"><fmt:message key="i18n_global_file_operate" bundle="${i18n}" /></th>    
        </tr>
    </table>
    <ul class="tabs_upload">
        
    </ul>
    <div class="jindubox">
        <p><fmt:message key="i18n_global_upload_progress" bundle="${i18n}" /><span class="jindu_out"><i class="jindu_in" style="width:0%"></i></span><span class="txt"><i id="showTotalProgress">0%</i>,<fmt:message key="i18n_global_finish_upload" bundle="${i18n}" /><i id="showUploadFileSize">0M</i>,<fmt:message key="i18n_global_total_file_size" bundle="${i18n}" /><i id="showSelectFileTotalSize">0M</i></span></p>
    </div>
</div>
<div class="layui-layer-btn" style="padding-top: 10px;">
<p class="remark"><i class="tips_zs"></i><fmt:message key="i18n_global_single_file_support_max_filesize" bundle="${i18n}" />${maxSize }MB<br/>${uploadTypeDescribe }</p>
	<a class="layui-layer-btn0" id="upload" style="margin-right: 10px;width:60px;text-align:center;"><fmt:message key="i18n_global_upload" bundle="${i18n}" /></a>
</div>
</body>
<script type="text/javascript">
var fileList = ${fileList};
var validateRule = ${validateRule};//文件类型验证规则
var uploadModel = ${uploadModel};//验证模式
var maxSize = ${maxSize};//单个允许最大大小
var usableDiskSize = ${usableDiskSize};
var isPromptMsg = false;//是否弹框提示
var msg = "<fmt:message key="i18n_global_cannot_add_upload_list" bundle="${i18n}" />";//提示内容
var msg1 = "";//提示分类1
var msg2 = "";//提示分类2
var msg3 = "";//提示分类3
var msg4 = "";//提示分类4
var count = 1;//计数器
var selectFileTotalSize = 0;//选择文件的总大小
var uploadFileSize = 0;//已上传文件大小
var currentUploadFileProgress = 0;//当前上传文件进度百分比
var currentUploadFileSize = 0;//当前上传文件大小
var uploadFailSize = 0;//上传失败的文件的大小

/**
* 判断目录中是否存在了该文件
*/
function getFileIsExists(fileName){
	if(fileList.length == 0){
		return false;
	}
	for(var i = 0;i < fileList.length;i++){
		if(fileName == fileList[i]){
			return true;
		}
	}
	return false;
}

$(function(){
	var userInfo = {};
	userInfo.userId = ${sessionScope.uploadUser.identity };
	userInfo.folderId = ${currentFolder.id};
	userInfo.uploadType = '2';
	userInfo.spaceType = '${spaceType}';
	
	var $btn = $('#upload');
	var state = 'pending';
	var $theUploadList = $(".tabs_upload");
	
	var uniqueFileName = null;        //为了避免每次分片都重新计算唯一标识，放在全局变量中，但如果你的项目需要WU支持多文件上传，那么这里你需要的是一个K/V结构，key可以存file.id
	WebUploader.Uploader.register({
        "before-send-file": "beforeSendFile",
        "before-send": "beforeSend"
	},{
        beforeSendFile: function(file){
            //拿到上传文件的唯一名称，用于断点续传
            uniqueFileName = $.md5(''+userInfo.userId+file.id+file.name+file.type+file.lastModifiedDate+file.size);
        } ,
        beforeSend: function(block){
        	//分片验证是否已传过，用于断点续传
            var task = new $.Deferred();
            $.ajax({
                type: "POST", 
                url: '${ctx}/upload/chunkCheck', 
                data: {
                    type: "chunkCheck", 
                    file: uniqueFileName, 
                    chunkIndex: block.chunk, 
                    size: block.end - block.start
                }, 
                cache: false, 
                timeout: 1000, //todo 超时的话，只能认为该分片未上传过 
                dataType: "json"
            }).then(function(data, textStatus, jqXHR){
            	if(data.code == 1000){
            		if(data.result){   //若存在，返回失败给WebUploader，表明该分块不需要上传
                        task.reject();
                    }else{
                        task.resolve();
                    }
            	}else{
					alert(data.codeInfo);    
            	}
            }, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
                task.resolve();
            });
            return $.when(task);
        } 
    });
	
	var uploader = WebUploader.create({
	    // swf文件路径
	    swf: basepath + '/static/js/webuploader-0.1.5/Uploader.swf',
	    // 文件接收服务端。
	    server: '${ctx}/upload/upLoad',
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: {
            id: '#picker',
            label: i18n_global_add_file
        },
	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    //resize: false,
	    // 初始状态图片上传前不会压缩
	    compress: false,
	 	// 开起分片上传。
	    chunked: true,
	    //如果要分片，分多大一片？ 默认大小为5M(5*1024*1024)
	    chunkSize: 5 * 1024 * 1024,
	    //上传并发数。允许同时最大上传进程数。
	    threads: true,
	    //是否允许在文件传输时提前把下一个文件准备好。 对于一个文件的准备工作比较耗时，比如图片压缩，md5序列化。 如果能提前在当前文件传输期处理，可以节省总体耗时。
	    prepareNextFile: true,
	    //文件上传请求的参数表，每次发送都会发送此对象中的参数
	    formData: userInfo
	});
	
	//当文件被加入队列之前触发
	uploader.on( 'beforeFileQueued', function( file ) {
		if(!illegalChar(file.name)){
			if(msg4 == ""){
				msg4 = count + ".<fmt:message key="i18n_global_file_name_contains_special_characters" bundle="${i18n}" /><br/>";
				count++;
				isPromptMsg = true;
			}
			return false;
		}
		if(!validateFileTypeIsUpload(file.name, uploadModel, validateRule)){
			if(msg1 == ""){
				msg1 = count + ".<fmt:message key="i18n_global_cannot_upload_file_type" bundle="${i18n}" /><br/>";
				count++;
				isPromptMsg = true;
			}
			return false;
		}
		if(!validateAllowUploadMaxSize(file.size, maxSize)){
			if(msg2 == ""){
				msg2 = count + ".<fmt:message key="i18n_global_file_size_exceeds" bundle="${i18n}" />" + maxSize + "MB<br/>";
				count++;
				isPromptMsg = true;
			}
			return false;
		}
		if(parseFloat(file.size) > parseFloat(usableDiskSize)){
			if(msg3 == ""){
				msg3 = count + ".<fmt:message key="i18n_global_available_space_size" bundle="${i18n}" /><br/>";
				count++;
				isPromptMsg = true;
			}
			return false;
		}else{
			usableDiskSize -= file.size;
		}
	})
	
	//当文件被加入队列以后触发
	uploader.on( 'fileQueued', function( file ) {
		$theUploadList.append('<li id="' + file.id + '">' +
				  '<div class="upload_jdt" style="width:0%"></div>' + 
				  '<p class="filebox"><i class="tipbg tips_bg_' + getFileIcon(file.name) + '"></i><span class="name_upload" title="' + file.name + '">' + file.name + '</span></p>' + 
				  '<p class="p2">' + bytesToSize(file.size) + '</p>' + 
				  '<p class="p3"><span class="state"><fmt:message key="i18n_global_wait_upload" bundle="${i18n}" /></span></p>' + 
				  '<p class="p4"><a href="javascript:void(0);" class="upload_no"></a></p>' + 
				  '</li>');
		selectFileTotalSize += file.size;
		$("#showSelectFileTotalSize").text(bytesToSize(selectFileTotalSize));
	});
	
	//当一批文件添加进队列以后触发
	uploader.on( 'filesQueued', function( files ) {
		if(isPromptMsg){//每批次添加结束后，再次添加会进行提醒
			layer.msg(msg + "<br/>" + msg1 + msg2 + msg3 + msg4,{icon:5, time:4000});
		}
		isPromptMsg = false, msg1 = "", msg2 = "", msg3 = "", msg4 = "", count = 1;
	});
	
	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
		var $li = $('#'+file.id).find(".upload_jdt");
		if(userInfo.uploadType == 1 && getFileIsExists(file.name)){//重命名取消上传
			uploader.skipFile(file);
			$('#'+file.id).find(".state").text(i18n_global_cancel_upload);
			$('#'+file.id).find(".p4").remove();
			$li.css( 'width', '100%');
			currentUploadFileProgress = 1;
		}else{
			$('#'+file.id).find(".state").text(i18n_global_uploading);
		    $li.css( 'width', percentage * 100 + '%' );
		    currentUploadFileProgress = percentage;
		}
		if(currentUploadFileProgress == 1){//当前上传的文件已上传完成
			currentUploadFileSize = file.size;
		}else{
			currentUploadFileSize = currentUploadFileProgress * file.size;
		}
		var myProgress = parseInt(parseFloat((uploadFileSize + currentUploadFileSize)/selectFileTotalSize).toFixed(2) * 100)
		$("#showUploadFileSize").text(bytesToSize(uploadFileSize + currentUploadFileSize));
		$("#showTotalProgress").text(myProgress + "%");
		if(myProgress == 100){//合并文件需要时间，进行预留0.01%的处理进度
			$("#showTotalProgress").text("99.99%");
		}else{
			$("#showTotalProgress").text(myProgress + "%");
		}
		$(".jindu_in").css("width", myProgress + "%");
	});
	
	//上传成功触发事件
	uploader.on( 'uploadSuccess', function( file, response) {
		//设置提示内容
		$('#'+file.id).find(".state").text(i18n_global_already_upload).css("color", "green");
		$('#'+file.id).find(".p4").remove();
		uploadFileSize += currentUploadFileSize;//更新已上传总大小
	});

	//上传失败触发事件
	uploader.on( 'uploadError', function( file, reason) {
		$('#'+file.id).find(".state").text(i18n_global_upload_error).css("color", "red");
		uploadFileSize += currentUploadFileSize;//更新已上传总大小
		uploadFailSize += currentUploadFileSize;
	});

	//不管成功或者失败，在文件上传完后都会触发uploadComplete事件
	uploader.on( 'uploadComplete', function( file ) {
	    //$( '#'+file.id ).find('.progress').fadeOut();
	});
	
	//当所有文件上传结束时触发
	uploader.on( 'uploadFinished', function( file ) {
		$("#showTotalProgress").text("100%");//文件合并完成后，总进度更改为100%
	});
	
	//所有的事件触发都会响应到
	uploader.on( 'all', function( type ) {
		//设置上传的参数
		uploader.options.formData = userInfo;
		if ( type === 'startUpload' ) {
		    state = 'uploading';
		} else if ( type === 'stopUpload' ) {
		    state = 'paused';
		} else if ( type === 'uploadFinished' ) {
		    state = 'done';
		}
		
		if ( state === 'uploading' ) {
		    $btn.text(i18n_global_pause);
		} else {
		    $btn.text(i18n_global_upload);
		}
  	});
	
	//切换是否暂停上传
	$btn.on( 'click', function() {
	    if ( state === 'uploading' ) {
	        uploader.stop(true);
	    } else {
	        uploader.upload();
	    }
	});
	
	//删除选择的文件
    $theUploadList.on("click", ".upload_no", function () {
    	var $ele = $(this).parent().parent();
    	var fileId = $ele.attr("id");
    	var delFile = uploader.getFile(fileId);
    	uploader.removeFile(delFile);
    	$ele.remove();
    	usableDiskSize += delFile.size;
    	//更新上传总进度
    	selectFileTotalSize -= delFile.size;
    	$("#showSelectFileTotalSize").text(bytesToSize(selectFileTotalSize));
    	currentUploadFileProgress = 0;
    	currentUploadFileSize = 0;
    	uploadFileSize = uploadFileSize - uploadFailSize;
    	$("#showUploadFileSize").text(bytesToSize(uploadFileSize));
    	var myProgress = parseInt(parseFloat((uploadFileSize + currentUploadFileSize)/selectFileTotalSize).toFixed(2) * 100)
		$("#showTotalProgress").text(myProgress + "%");
		$(".jindu_in").css("width", myProgress + "%");
		uploadFailSize = 0;
    });
	
	//上传弹出层select模拟
    $(".select_down p").on("click",function(e){
       $(".select_down").removeClass("active");
       $(this).parent().addClass("active");
       $(this).next("div").slideDown();
       e.stopPropagation();//阻止冒泡程序
    });
    $(".select_down div p").on("click",function(e){
        var _this = $(this);
        userInfo.uploadType = _this.attr("date-value");
        
        _this.parent('div').siblings('p').text(_this.attr("date-text"));
        $(".select_down p").next("div").slideUp();
        _this.addClass("selected").siblings().removeClass("selected");
        $(".select_down").removeClass("active");
        e.stopPropagation();//阻止冒泡程序
    });
    
    $(document).on("click",function(){
        $(".select_down").removeClass("active");
        $(".select_down > div").slideUp();
    });
});
</script>
</html>