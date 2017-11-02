var isOper = true;


function searchFile(){
	$("#queryForm").submit();
}

$(function(){
    
    //刷新按钮
    $(".ac_refresh").live('click',function(){
    	refresh();
    });
    
    $(".tab_check").live("click",function(){
    	if(!isOper){
    		return;
    	}
    	$(this).toggleClass("tab_checked");
    	selectCheckBox($(this));
     });
     
    
     $(".tabs tr td:nth-child(2)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
		 if(!isOper){
    		return;
    	 }
    	 selectCheckBox(obj);
     });
     
     $(".tabs tr td:nth-child(3)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
    	 if(!isOper){
     		return;
     	 }
    	 selectCheckBox(obj);
     });
     
     //防止点击文件夹与点击选中框冲突(鼠标移动到文件夹与离开文件夹事件)
     $(".remove_check").live("mouseover",function(){
    	 $(this).parent().parent().parent().parent().find("td :first i").removeClass("tab_check");
    	 $(this).parent().parent().parent().parent().find("td :first i").addClass("tab_no_check");
     });
     $(".remove_check").live("mouseout",function(){
    	 $(this).parent().parent().parent().parent().find("td :first i").removeClass("tab_no_check");
    	 $(this).parent().parent().parent().parent().find("td :first i").addClass("tab_check");
     });
     
    /**
     * 下载
     */
     $(".ac_down").live("click",function(){
    	var data = getCheckSelected();
		if(data.folders.length + data.files.length == 0){
			layer.msg(please_select_a_file_or_directory_download,{icon: 5,time:1000});
			return false;
		}
		if((data.folders.length + data.files.length > 1) || (data.folders != null && data.folders.length > 0)){
			$("#showMsg").html(wait_packing_files);
	  		$(".ts_box").show();
		}
  		setTimeout(function(){
  			$.ajax({
  	   			url: basepath+"/takecode/download?takeCode="+$("#takecodeindex").val(),
  	   	        dataType:"json",
  	   	        type: 'POST',
  	   	     	data: JSON.stringify(data),
  	   	     	contentType: "application/json",
  	   	        async: false, 
  	   	        success: function(result){
  	   	        	if((data.folders.length + data.files.length > 1) || (data.folders != null && data.folders.length > 0)){
		  	   	   		$(".ts_box").hide();
		  	 		}
  	   		        if (result.code == 1000) {
  	   		        	window.location.href = result.result;
  	   		        }else{
  	   		        	layer.msg(result.codeInfo,{icon: 5,time:1500});
  	   		        }
  	   	        }
  	   		 });
  		}, 100);
     });
    
     
     /**
      * 图片预览
      */
      $(".imageFile").live("click",function(){
        var fileIds = new Array();
        var names = new Array();
        var index = 0 ;
        var clickFileId = $(this).parent().parent().prev().find('i').attr('value');
    	$(".imageFile").each(function(i){
    		var fileId = $(this).parent().parent().prev().find('i').attr('value');
    		if(clickFileId == fileId){
    			index = i;
    		}
    		fileIds.push(fileId);
    		names.push($(this).find("a").html());
    	});
    	var fileIdString  = fileIds.join(",");
		$.ajax({
   			url: basepath+"/takecode/read",
   	        dataType:"json",
   	        type: 'POST',
   	     	data: {"fileIdString":fileIdString},
   	        async: false, 
   	        success: function(data){
   	            checkErrorCode(data);
   	            var showArray = new Array();
   	            if(data.result.length > 0){
   	            	for(var i = 0 ; i < data.result.length; i++ ){
   	            		var obj = {href:data.result[i],title:names[i],type:'image'};
   	            	    showArray.push(obj);
   	            	}
   	            	$.fancybox.open(showArray, {
   	            		preload : 0,
   	            		index : index
   	          		});
   	            }
   	        }
   	   	});
      });
     
   	
   	/**
 	 * 预览
 	 */
 	$(".previewFile").live("click", function(){
 		var fileId = $(this).attr("fileId");
 		if(fileId == ""){
 			layer.msg(please_click_on_the_file_preview,{icon: 5,time:1000});
 	   		return false;
 		}
 		var fileName = $(this).find("a").html();
 		//判断是否为文档文档编辑(true)还是word相关文档编辑(false)
 		var index = fileName.lastIndexOf(".");
    	var bool = false;
		if(index > 0){
			var lastFileName = fileName.substring(index);
			 for(var i in fileType){
            	if(fileType[i] == lastFileName){
            		bool = true;
            		break;
            	}
            }
		}
		if(bool){
			layer.open({
                type: 2,
                title :document_preview+'-'+fileName,
                area: ['800px', '600px'],
                shadeClose: false, //点击遮罩关闭
                maxmin:true,
                content: fileServiceUrl + "/fileManager/onlineEditTxt?params="+ fileId + "&read=0"
                ,btn: [close]
            });
		} else {
			//word 等相关文档
	 		$.ajax({
				url: basepath+"/personal/preview",
		        dataType:"json",
		        type: "POST",
		     	data: {fileId: fileId},
		        async: false, 
		        success: function(result){
			        if (result.code == 1000 && result.result == "SUCCESS") {
			        	window.open(result.codeInfo);
			        }else{
			        	layer.msg(result.codeInfo,{icon: 5,time:1000});
			        }
		        }
	 		});
		}
 	});
 	
 	/**
 	 * 在线编辑
 	 */
 	$(".ac_edit").on("click", function(){
 		var fileId = $(".tab_contain").find(".tab_checked").attr('value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg(please_select_a_file_edited_online,{icon: 5,time:1000});
 	   		return false;
 		}
 		$.ajax({
			url: basepath + "/personal/onlineEdit",
	        dataType:"json",
	        type: "POST",
	     	data: {fileId: fileId},
	        async: false, 
	        success: function(data){
		        if (data.code == 1000) {
		        	window.open(data.result);
		        }else{
		        	layer.msg(data.codeInfo,{icon: 5,time:1000});
		        }
	        }
 		});
 	});
 	
 	//创建提取码
 	$(".set_tiqu").click(function(){
 		var data = getCheckSelected();
 		if(data.files.length != 1 || data.folders.length > 0){
 			var fileIds = '';
 			for(var i=0;i<data.files.length;i++){
 				fileIds +=data.files[i].id +",";
 			}
 			if(data.files.length>0){
 				fileIds = fileIds.substring(0,fileIds.length-1);
 			}
 			createTakeCodeMore(fileIds);
 			return;
 		}
 		var id = data.files[0].id;
 		createTakeCode(id);
	});
 	
 	//移除提取码(批量)（自有）
    $('.remove_qx').on('click', function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	var data = getCheckSelectedTakeCode();
    	if(data.takecodes.length == 0){
    		layer.msg(i18n_select_record,{icon: 5,time:1000});
	   		 return false;
	   	}
    	//构建移除展示
    	var nameArray = new Array();
    	for(var i=0;i<data.takecodes.length;i++){
    		var r = "#takecodename"+data.takecodes[i].id;
        	var takecodename = $(r).html();
        	nameArray.push(takecodename);
    	}
    	if(nameArray.length == 1){
    		$("#move").html(i18n_delete_confirm1+nameArray+i18n_delete_confirm2);
    	} else {
    		$("#move").html(i18n_deletes_confirm1+nameArray.length+i18n_deletes_confirm2);
    	}
        layer.open({
            type: 1,
            title :i18n_delete_extract,
            area: ['600px', '183px'],
            shadeClose: true, //点击遮罩关闭
            content: $('.con_remove_qx')
            ,btn: [determine, close],
            yes: function(index, layero){
            	$.ajax({
            		url: basepath+"/takecode/delete",
                    dataType:"json",
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    async: false, 
                    success: function(data){
                    	if (data.code == 1000) {
                    		layer.closeAll();
                    		layer.msg(i18n_delete_success,{icon: 1,time:1500});
            	        	setTimeout(function(){
            	        		window.location.reload();
            	        	}, 1500);
            	        }
            	  }}); 
          	  }
        });
    });
})

//移除提取码（自有）
function remove(id,name){
	var takecodeArray = new Array();
	var takecodeJson = {
   			"id" : id
   		}
	takecodeArray.push(takecodeJson);
	var data = {
			"takecodes" : takecodeArray
		}
	$("#move").html("“"+name+"”");
    layer.open({
        type: 1,
        title :i18n_delete_extract,
        area: ['600px', '183px'],
        shadeClose: false, //点击遮罩关闭
        content: $('.con_remove_qx')
        ,btn: [determine, close],
        yes: function(index, layero){
        	$.ajax({
        		url: basepath+"/takecode/delete",
                dataType:"json",
                type: 'POST',
                data: JSON.stringify(data),
                contentType: "application/json",
                async: false, 
                success: function(data){
                	if (data.code == 1000) {
                		layer.closeAll();
                		layer.msg(i18n_delete_success,{icon: 1,time:2000});
        	        	setTimeout(function(){
        	        		window.location.reload();
        	        	}, 2000);
        	        }
        	  }}); 
      	  }
    });
}

//选中操作后选中的提取码数量（自有）
function getCheckSelectedTakeCode(){
   var takecodeArray = new Array();
   $("i[name='folderCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var id= $(this).find("input[type='checkbox']").val();
	   		var takeCode= $(this).find("input[name='code']").val();
	   		var takecodeJson = {
	   			"id" : id,
	   			"takeCode" : takeCode
	   		}
	   		takecodeArray.push(takecodeJson);
	   	 }
    });
    var data = {
		"takecodes" : takecodeArray
	}
    return data;
}

function selectCheckBox(obj){
	var _class = obj.attr('class');
	if(_class == undefined){
		return ;
	}
	if(_class.indexOf('tab_checked') == -1){
		$("#allCheck").removeClass("tab_checked");
	}
    //计算选中记录条数
    var folderArray = new Array();
    var fileArray = new Array();
    var id = obj.attr('id');
    $(".tabs tr").attr("style","");
   	$("i[name='folderCheck']").each(function(){
        if(id == 'allCheck'){ 
         	$(this).attr('class',_class);
        }
     	if($(this).attr('class').indexOf('tab_checked') != -1){
     		$(this).parent().parent().attr("style","background: #f3f3f3;");
     	   folderArray.push($(this).find("input[type='checkbox']").val());
     	}
    });
    $("i[name='fileCheck']").each(function(){
   	  if(id == 'allCheck'){ 
        $(this).attr('class',_class);
      }
	  if($(this).attr('class').indexOf('tab_checked') != -1){
		  $(this).parent().parent().attr("style","background: #f3f3f3;");
		 var objFile = {
		      id : $(this).find("input[type='checkbox']").val(),
			  name : $(this).parent().next().find('.file_name').find('a').html()
		 }
	     fileArray.push(objFile);
	  }
    });
    showOperBtn(folderArray);
    //判断是否全选
    $("#fileCount").html(fileArray.length);
    $("#folderCount").html(folderArray.length);
    //看是否全部选中
    var bool = true;
    $(".tab_contain").find('.tab_check').each(function(){
		var aa = $(this).attr('value');
		var check_class = $(this).attr('class');
		if(!(check_class.indexOf('tab_checked') != -1)){
			var aa = $(this).attr('value');
			bool = false;
			return false;
  		}		
    });
    if(bool){
    	$("#allCheck").addClass('tab_checked');
    }
}

//版本
function showVersion(id,version,name){
    layer.open({
        type: 2,
        title :i18n_file_version,
        area: ['800px', '460px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/personal/version?fileId='+id+"&fileVersion="+version
        ,btn: [close]
    	/*,
        yes: function(index, layero){
      	    //按钮【新建文件夹】的回调
       	var body = layer.getChildFrame('body', index);
        }*/
    });
}

//提取码添加前置（时间设定）
function intoTakeCode(){
    layer.open({
        type: 1,
        title :set_extraction_code,
        area: ['800px', '380px'],
        shadeClose: false, //点击遮罩关闭
        content: $(".con_tiqu")
        ,btn: [close]
    });
}

//创建提取码
function createTakeCode(id){
	//获取失效时间
	var timestr = $("#effectivetime").val();
	layer.closeAll();
    layer.open({
        type: 2,
        title :set_extraction_code,
        area: ['800px', '380px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/takecode/toinsert/'+id+'?timestr='+timestr
        ,btn: [close]
    });
}

//创建提取码(多个)
function createTakeCodeMore(fileIds){
	//获取失效时间
	var timestr = $("#effectivetime").val();
	layer.closeAll();
	layer.open({
		type: 2,
		title :set_extraction_code,
		area: ['800px', '380px'],
		shadeClose: false, //点击遮罩关闭
		content: basepath + '/takecode/toinsertmore?fileIds='+fileIds+'&timestr='+timestr,
		btn: [determine, close]
	});
}

function openFolderWindow(){
	var data = getCheckSelected();
	if(data.folders.length + data.files.length == 0){
		 layer.msg(please_check_the_file_or_folder,{icon: 5,time:1500});
		 return false;
	}
	var title = getTitle();
    layer.open({
        type: 2,
        title :title,
        area: ['800px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/personal/folderTree'
        ,btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确定】的回调
      	    var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var jsonFolder = iframeWin.callSelectFolderBack() //得到iframe页的body内容
			//计算选中记录条数
		    var data = getCheckSelected(jsonFolder.folderId)
			//移动
			var bool = checkFolderOrFile(jsonFolder);
			layer.closeAll();
			if(bool){
				operSameName(data);
			} else {
				layer.msg(cannot_move_or_copy_the_file_to_itself_or_its_sub_directory,{icon: 5,time:1500});
				return ;
			}
      	  }
    });
}

/**复制或者移动检测(不能移动到子目录或者当前目录)**/
function checkFolderOrFile(jsonFolder){
	var bool = true;
	var selectFolderId = jsonFolder.folderId;
	var selectFolderCode = jsonFolder.folderCode;
	var sessionEmployeeId = $("#sessionEmployeeId").val();
   //判断文件夹
   $("i[name='folderCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var pFolderId = $(this).find("input[name='pFolderId']").val();
	   		var folderCode = $(this).find("input[name='code']").val();
	   		var employeeId = $(this).find("input[name='employeeId']").val();
	   		//判断选中的文件夹为当前文件夹或者付文件夹 或者子文件夹都是不能通过
	   	    //当前文件夹
	   		if(selectFolderId == pFolderId && sessionEmployeeId == employeeId){
	   			bool = false;
	   		}
	   		/*//父文件夹
	   		if(selectFolderCode.length < folderCode.length){
	   			var pfolderCode = folderCode.substring(0,selectFolderCode.length);
	   			if(pfolderCode == selectFolderCode){
	   				bool = false;
	   			}
	   		}*/
	   		//子文件夹或者包含的子文件夹
	   		if(selectFolderCode.length > folderCode.length){
	   			var pfolderCode = selectFolderCode.substring(0,folderCode.length);
	   			if(pfolderCode == folderCode  && sessionEmployeeId == employeeId){
	   				bool = false;
	   			}
	   		}
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var folderId= $(this).find("input[name='folderId']").val();
	   		var employeeId = $(this).find("input[name='employeeId']").val();
	   		//只要判断文件夹Id
	   		if(selectFolderId == folderId && employeeId == sessionEmployeeId){
	   			bool = false;
	   		}
	   	 }
    });
    return bool;
}

//选中操作后选中的文件和文件夹数量
function getCheckSelected(folderId){
   var folderArray = new Array();
   var fileArray = new Array();
   $("i[name='folderCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var id= $(this).find("input[type='checkbox']").val();
	   		var folderJson = {
	   			"id" : id
	   		}
	   		folderArray.push(folderJson);
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var id= $(this).find("input[type='checkbox']").val();
	   		var fileJson = {
	   			"id" : id
	   		}
	   		fileArray.push(fileJson);
	   	 }
    });
    var data = {
		"folders" : folderArray,
		"files" : fileArray,
		"id" : folderId
	}
    return data;
}


//选中操作后选中的文件和文件夹名称
function getCheckSelectedName(){
   var nameArray = new Array();
   $("i[name='folderCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var folderName= $(this).parent().next().find('a').html();
	   		nameArray.push(folderName);
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var fileName= $(this).parent().next().find('.file_name').html();
	   		nameArray.push(fileName);
	   	 }
    });
    return nameArray;
}

//创建文件夹
function createFolder(obj){
	if($("#addIndex").val() != 0){
		return;
	}
	var folderName = $(obj).parent().find("input[type='text']").val();
	var parentId = $("#parentId").val();
	if(folderName == ''){
		layer.msg(folder_name_cannot_be_empty,{icon: 5,time:1000});
		return false;
	}
	//检测特殊字符
	if(!illegalChar(folderName)){
		return;
	}
	var data = {
		"parentId"   : parentId,
		"folderName" : folderName
	}
	$.ajax({
		url: basepath+"/personal/createFolder",
        dataType:"json",
        type: 'POST',
        data:data,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	layer.msg('创建成功！',{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1500);
	        }
        }
	 });
	 $(this).parents("tr").remove().siblings("tr").show();
	 $("#addIndex").val(1);
}


//最终操作方法
function operFolderFile(initData){
	var operType = $("#operType").val();
	if(operType == 1){
		moveFolderFile(initData);
	}else if(operType == 2) {
		//复制
		copyFolderFile(initData);
	}
}


//复制文件
function copyFolderFile(initData){
	$("#showMsg").html("正在复制文件，请稍后...");
	$(".ts_box").show();
	setTimeout(function(){
		 //在进行管理
		 $.ajax({
			url: basepath+"/personal/copyManageFolder",
	        dataType:"json",
	        type: 'POST',
	        data: JSON.stringify(initData),
	        contentType: "application/json",
	        async: false, 
	        success: function(data){
	        	$(".ts_box").hide();
	        	checkErrorCode(data);
		        if (data.code == 1000) {
		            layer.msg(copy_success,{icon: 1,time:1500});
		        	setTimeout(function(){
		        		refresh();
		        	}, 1500);
		        }
	     }}); 
	}, 500);
}

//移动文件
function moveFolderFile(initData){
	$("#showMsg").html("正在移动文件，请稍后...");
	$(".ts_box").show();
	setTimeout(function(){
		 //在进行管理
		 $.ajax({
			url: basepath+"/personal/moveManageFolder",
	        dataType:"json",
	        type: 'POST',
	        data: JSON.stringify(initData),
	        contentType: "application/json",
	        async: false, 
	        success: function(data){
	        	$(".ts_box").hide();
	        	checkErrorCode(data);
		        if (data.code == 1000) { 
		        	layer.msg('移动成功！',{icon: 1,time:1500});
		        	setTimeout(function(){
		        		refresh();
		        	}, 1500);
		        }
	     }});  
	}, 500);
}

function operSameName(initData){
	$.ajax({
		url: basepath+"/personal/getSameName",
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(initData),
        contentType: "application/json",
        async: false, 
        success: function(data){
	        if (data.code == 1000) {
	            //进行验证
	            if(data.result.oldFolders.length > 0){
	            	//操作文件夹
	            	operSameFolder(initData, data);
	            } else if (data.result.oldFiles.length > 0){
	            	//操作文件
	            	operSameFile(initData, data);
	            } else {
	            	//直接调用操作
	            	operFolderFile(initData);
	            }
	        }
	  }}); 
}

//操作同名文件夹
function operSameFolder(initData, data){
	  var lastJsonFolders = new Array();
	  //先添加不重名的文件夹
	  for(var i = 0 ; i < initData.folders.length; i++){
		  var operFolder = initData.folders[i];
		  //表示为不同名
		  var bool = false;
		  for(var j = 0; j < data.result.oldFolders.length; j++){
			  var sameFolder = data.result.oldFolders[j];
			  if(operFolder.id == sameFolder.id){
				  bool = true;
				  break;
			  }
		  }
		  if(!bool){
			  lastJsonFolders.push(operFolder);
		  }
	  }
	  sameFolders = data.result.oldFolders;
	  if(sameFolders.length > 0){
		  var indexWin  = 0;
		  openSameFolderWindow(indexWin, initData, data, lastJsonFolders);
	  }
}

//开启文件夹同名窗口
//@param indexWin 开启第几个窗口 @initData操作的数据DATA  @data 所有同名文件夹对象  @lastJsonFolders 最终操作的集合
function openSameFolderWindow(indexWin, initData, data, lastJsonFolders){
	var oldFolders = data.result.oldFolders;
	var nowFolders = data.result.nowFolders;
	$(".folderNameClass").show();
	$("#oldFolder").show();
	$("#nowFolder").show();
	$("#oldFile").hide();
	$("#nowFile").hide();
	//设置显示
	$("#oldFolderName").html(oldFolders[indexWin].folderName);
	var updateOldTime = getSmpFormatDateByLong(oldFolders[indexWin].updateTime,true);
	$("#oldUpdateTime").html(updateTime);
	//设置显示
	$("#nowFolderName").html(nowFolders[indexWin].folderName);
	var updateTime = getSmpFormatDateByLong(nowFolders[indexWin].updateTime,true);
	$("#nowUpdateNowTime").html(updateTime);
	var syNumber = oldFolders.length - indexWin;
	if(syNumber > 1){
		$("#moreSameName").show()
		$("#sySame").html(syNumber - 1)
	} else {
		$("#moreSameName").hide()
	}
	var title = getTitle();
	//复制到
    layer.open({
        type: 1,
        title :title,
        area: ['600px', '420px'],
        shadeClose: true, //点击遮罩关闭
        content: $('.con_copy')
        ,btn: [skip,keep_two_folders,close],
        yes:function(){
        	//跳过按钮
        	layer.closeAll();
        	//保持两个文件夹
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index == -1){
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFolders.length > indexWin){
            		openSameFolderWindow(indexWin, initData, data, lastJsonFolders);
            	} else {
            		//调用文件同名弹窗  代表同名文件夹结束
            		initData.folders = lastJsonFolders;
            		operSameFile(initData, data);
            	}
        	}
        },btn2:function(){
        	//跳过按钮
        	layer.closeAll();
        	//保持两个文件夹
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index != -1){
        		//一选中 则不需要再执行弹窗
        		for(var i = indexWin ; i < sameFolders.length; i++){
        			//进行选中设值
        		    sameFolder = sameFolders[i];
        			var jsonFolder = {
        			    "id" : sameFolder.id
        			}
        			lastJsonFolders.push(jsonFolder);
        		}
        		//调用文件同名弹窗  代表同名文件夹结束
        		initData.folders = lastJsonFolders;
        		operSameFile(initData, data);
        	} else {
        		//进行选中设值
    		    sameFolder = sameFolders[indexWin];
    		    var jsonFolder = {
       			    "id" : sameFolder.id
       			}
       			lastJsonFolders.push(jsonFolder);
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFolders.length > indexWin){
            		openSameFolderWindow(indexWin, initData, data, lastJsonFolders);
            	} else {
            		//调用文件同名弹窗 代表同名文件夹结束
            		initData.folders = lastJsonFolders;
            		operSameFile(initData, data);
            	}
        	}
        }
    });
}

//操作同名文件
function operSameFile(initData, data){
	  var lastJsonFiles = new Array();
	  //先添加不重名的文件
	  for(var i = 0 ; i < initData.files.length; i++){
		  var operFile = initData.files[i];
		  //表示为不同名
		  var bool = false;
		  for(var j = 0; j < data.result.oldFiles.length; j++){
			  var sameFile = data.result.oldFiles[j];
			  if(sameFile.id == operFile.id){
				  bool = true;
				  break;
			  }
		  }
		  if(!bool){
			  lastJsonFiles.push(operFile);
		  }
	  }
	  //全局变量
	  sameFiles = data.result.oldFiles;
	  if(sameFiles.length > 0){
		  var indexWin  = 0;
		  layer.closeAll();
		  openSameFileWindow(indexWin, initData, data, lastJsonFiles);
	  } else {
		  //直接执行操作
		  operFolderFile(initData);
	  }
}

//开启文件同名窗口
//@param indexWin 开启第几个窗口  @data 所有同名文件对象  @lastJsonFiles 最终操作的集合
function openSameFileWindow(indexWin, initData, data, lastJsonFiles){
	var oldFiles = data.result.oldFiles;
	var nowFiles = data.result.nowFiles;
	var syNumber = oldFiles.length - indexWin;
	//设置影藏和显示
	$(".folderNameClass").hide();
	$("#oldFolder").hide();
	$("#nowFolder").hide();
	$("#oldFile").show();
	$("#nowFile").show();
	//设置显示
	$("#oldFileName").html(oldFiles[indexWin].fileName);
	var updateOldTime = getSmpFormatDateByLong(oldFiles[indexWin].updateTime,true);
	$("#oldUpdateTime").html(updateOldTime);
	//设置显示
	$("#oldFileImg").addClass("tips_bg_"+nowFiles[indexWin].fileType);
	$("#nowFileImg").addClass("tips_bg_"+nowFiles[indexWin].fileType);
	$("#nowFileName").html(nowFiles[indexWin].fileName);
	var updateNowTime = getSmpFormatDateByLong(nowFiles[indexWin].updateTime,true);
	$("#nowUpdateTime").html(updateNowTime);
	var syNumber = oldFiles.length - indexWin;
	if(syNumber > 1){
		$("#moreSameName").show()
		$("#sySame").html(syNumber - 1)
	} else {
		$("#moreSameName").hide()
	}
	//复制到
	var operType = $("#operType").val();
	var title = getTitle();
    layer.open({
        type: 1,
        title :title,
        area: ['600px', '420px'],
        shadeClose: true, //点击遮罩关闭
        content: $('.con_copy')
        ,btn: [skip,keep_two_files,close],
        yes:function(){
        	//跳过按钮
        	layer.closeAll();
        	//保持两个文件夹
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index == -1){
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFiles.length > indexWin){
            		openSameFileWindow(indexWin, initData, data, lastJsonFiles);
            	} else {
            		//调用 文件操作接口
            		initData.files = lastJsonFiles;
            		operFolderFile(initData);
            	}
        	}
        },btn2:function(){
        	//跳过按钮
        	layer.closeAll();
        	//保持两个文件夹
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index != -1){
        		//一选中 则不需要再执行弹窗
        		for(var i = indexWin ; i < sameFiles.length; i++){
        			//进行选中设值
        		    sameFile = sameFiles[i];
        			var jsonFile = {
        			    "id" : sameFile.id
        			}
        			lastJsonFiles.push(jsonFile);
        		}
            	//调用 文件操作接口
        		initData.files = lastJsonFiles;
        		operFolderFile(initData);
        	} else {
        		//进行选中设值
    		    sameFile = sameFiles[indexWin];
    		    var jsonFile = {
       			    "id" : sameFile.id
       			}
    		    lastJsonFiles.push(jsonFile);
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFiles.length > indexWin){
            		openSameFileWindow(indexWin, initData, data, lastJsonFiles);
            	} else {
            		//调用 文件操作接口
            		initData.files = lastJsonFiles;
            		operFolderFile(initData);
            	}
        	}
        }
    });
}


//获取弹窗名称
function getTitle(){
	var operType = $("#operType").val();
	var title = copy_to;
	if(operType == 1){
		title = move_to;
	}
	return title;
}

//重命名操作
function operRename(obj, openId, openType){
	var operName = $(obj).parent().find('input').val();
	if(operName == ''){
		if(openType == 1){
			layer.msg(folder_name_cannot_be_empty,{icon: 5,time:1000});
		} else {
			layer.msg(file_name_cannot_be_empty,{icon: 5,time:1000});
		}
		return;
	}
	//检测特殊字符
	if(!illegalChar(operName)){
		return;
	}
	if(openType == 2){
		var fileName = $(obj).parent().parent().find(".file_name a").html();
		if(fileName.lastIndexOf(".") != -1){
			fileName = fileName.substring(fileName.lastIndexOf("."),fileName.length);
		}
		operName = operName + fileName;
		
	}
	var json = {
		name : 	operName,
		openId : openId,
		openType : openType
	}
     $.ajax({
		url: basepath+"/personal/rename",
        dataType:"json",
        type: 'POST',
        data: json,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	            //进行验证
	        	layer.msg(rename_success,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1500);
	        }
	  }}); 
}

//删除
function delFolderFile(){
	var data = getCheckSelected();
	var dataName = getCheckSelectedName();
	var dataNames = dataName.join("、");
	if(dataName.length == 1){
		$("#move").html(sure_to_delete+"“"+dataNames+"”？");
	} else {
		$("#move").html(sure_to_delete_selected+dataName.length+file_or_folder+"?");
	}
	if(data.folders.length + data.files.length == 0){
		 layer.msg(please_check_the_file_or_folder,{icon: 5,time:1500});
		 return false;
	}
	layer.open({
        type: 1,
        title :delete_file,
        area: ['600px', '183px'],
        shadeClose: false, //点击遮罩关闭
        content: $('.con_remove_qx')
        ,btn: [determine, close],
        yes: function(index, layero){
       	   //在进行管理
       	   $("#showMsg").html(wait_deleting_files);
       	   $(".ts_box").show();
	       	setTimeout(function(){
	       	   $.ajax({
	       		  url: basepath+"/personal/delManageFolder",
	              dataType:"json",
	              type: 'POST',
	              data: JSON.stringify(data),
	              contentType: "application/json",
	              async: false, 
	              success: function(data){
	            	$(".ts_box").hide();
	            	checkErrorCode(data);
	       	        if (data.code == 1000) { 
	       	        	layer.closeAll();
		       	        layer.msg(delete_success,{icon: 1,time:1500});
			        	setTimeout(function(){
			        		refresh();
			        	}, 1500);
	       	        }
	           }});  
	       	}, 500);
      	}
    });
}

//分享界面
function shareAuth(openId , openType){
	var dataUrl = basepath + '/personal/authPage?openId='+openId+'&openType='+openType;
    layer.open({
        type: 2,
        title :set_share_range,
        area: ['800px', '500px'],
        shadeClose: true, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var data = iframeWin.getSelectedData(); //得到iframe页的body内容
			ajaxShareAuth(data);
      	}
    });
}

//分享界面
function shareMoreAuth(data){
	var dataUrl = basepath + '/personal/authPage';
	var dataString  = JSON.stringify(data);
	dataUrl = dataUrl + "?data=" + encodeURI(encodeURI(dataString));
    layer.open({
        type: 2,
        title :set_share_range,
        area: ['800px', '500px'],
        shadeClose: true, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var data = iframeWin.getMoreSelectedData(); //得到iframe页的body内容
		/*	var folderData = decodeURI(data.data);
			data.folder = folderData;*/
			ajaxShareAuth(data);
      	}
    });
}

function ajaxShareAuth(data){
	 //加载缓冲
	 $.ajax({
  		 url: basepath+"/personal/shareAuth",
         dataType:"json",
         type: 'POST',
         data: JSON.stringify(data),
         contentType: "application/json",
         async: false, 
         success: function(data){
        	checkErrorCode(data);
  	        if (data.code == 1000) {
  	        	layer.closeAll();
  	            layer.msg(successful_operation,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1000);
  	        }
      }});  
}


//根据勾选数量显示不同操作按钮
function showOperBtn(fileArray){
	if(fileArray.length > 0 ){
		showAction($(".remove_qx"));
	} else{
		hideAction($(".remove_qx"));
	}
}

function showAction(obj){
	 $(obj).each(function(){
		if($(this).is(".rightmouse") && !$(this).is(".invalid")){
			$(this).show();
			$(this).addClass("cancheck");
			return;
		}
		if(!$(this).is(".invalid")){
			$(this).css('background','#5a98de'); 
			$(this).css('border','1px solid #5a98de'); 
			$(this).css('color','#fff'); 
			$(this).find("i:first").css('background-position-x','right'); 
			$(this).addClass("cancheck");
		}
	 });
}
function hideAction(obj){
	$(obj).each(function(){
		if($(this).is(".rightmouse") && !$(this).is(".invalid")){
			$(this).hide();
			$(this).removeClass("cancheck");
			return;
		}
		if(!$(this).is(".invalid")){
			$(this).css('background','#fff'); 
			$(this).css('border','1px solid #c9c9c9'); 
			$(this).css('color','#bebebe'); 
			$(this).find("i:first").css('background-position-x','left'); 
			$(this).removeClass("cancheck");
		}
	});
}

//判断权限按钮是否可执行
function checkPermission(obj){
	//判断是否是上传
	if($(obj).attr("id")=="uploadFile" ||$(obj).attr("id")=="uploadFolder" ){
		obj = $(obj).parent().parent();
	}
	if($(obj).is(".invalid")){
		return false;
	}else if($(obj).is(".cancheck")){
		return true;
	}else{
		return false;
	}
}