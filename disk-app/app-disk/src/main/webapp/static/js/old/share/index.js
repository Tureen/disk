var isOper = true;


function searchFile(){
	$("#queryForm").submit();
}

//禁止右键菜单功能
document.oncontextmenu = function(){
    return false;   
}

/*$("body:not(.right_menu)").mousedown(function(e){
	var key = e.which;
	alert(key)
    if(key == 1) {
    	$(".right_menu").hide();
    }
});*/

$(".right_menu").click(function(event){    
    event=event||window.event;    
    event.stopPropagation();    
});    
    
//点击层外，隐藏这个层。由于层内的事件停止了冒泡，所以不会触发这个事件    
$(document).click(function(e){                         
    $(".right_menu").hide();    
});

	// 浏览器的可见宽度
	function getClientWidth() {
	var clientWidth = 0;
	if (document.body.clientWidth && document.documentElement.clientWidth) {
	clientWidth = (document.body.clientWidth < document.documentElement.clientWidth) ? document.body.clientWidth: document.documentElement.clientWidth;
	} else {
	clientWidth = (document.body.clientWidth > document.documentElement.clientWidth) ? document.body.clientWidth: document.documentElement.clientWidth;
	}
	return clientWidth;
	}
	
	// 浏览器的可见高度
	function getClientHeight() {
	var clientHeight = 0;
	if (document.body.clientHeight && document.documentElement.clientHeight) {
	clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight: document.documentElement.clientHeight;
	} else {
	clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight: document.documentElement.clientHeight;
	}
	return clientHeight;
	}

$(function(){
	
	//图形列表
	$(".array_h").click(function(){
		var data_url = basepath + "/share/index?type=1";
		var queryName = $("#queryName").val();
		if(queryName != null && queryName.length > 0){
			data_url = basepath + "/share/listkey?type=1" +"&queryName=" + encodeURI(encodeURI(queryName));
		} else {
			var folderId = $("#parentId").val();
			if(folderId != null && folderId != 0 && folderId.length > 0 ){
				data_url = basepath + "/share/indexinto/"+folderId+"?type=1" ;
			}
		}
		window.location.href = data_url;
	});
	
	//表单列表
	$(".array_v").click(function(){
		var data_url = basepath + "/share/index?type=0";
		var queryName = $("#queryName").val();
		if(queryName != null && queryName.length > 0){
			data_url = basepath + "/share/listkey?type=0" +"&queryName=" + encodeURI(encodeURI(queryName));
		} else {
			var folderId = $("#parentId").val();
			if(folderId != null && folderId != 0 && folderId.length > 0 ){
				data_url = basepath + "/share/indexinto/"+folderId+"?type=0";
			}
		}
		window.location.href = data_url;
	});
	
	//权限隐藏(个人空间功能)
	permissionHide();

	$("#addIndex").val(0);
	
	$(".trfolder").mousedown(function(e){
	    var key = e.which;
	    if(key == 3){
	    	var _top =e.clientY;
	    	var _left =e.clientX;
	    	$(this).find("i[name='folderCheck']").addClass("tab_checked");
	        var obj = $(this).find("i[name='folderCheck']");
	        selectCheckBox(obj);
	        //获取弹出菜单的宽度和高度
	        var menuwidth=$(".right_menu").width();
	        var menuheight=$(".right_menu").height();
	        //获取浏览器可视宽度和高度
	        var clientHeight=getClientHeight();
	        var clientWidth=getClientWidth();
	        //判断当光标靠边时，防止弹出菜单溢出浏览器可视范围
	        if((_left+menuwidth)>clientWidth){_left=clientWidth-menuwidth-100;}
	        if((_top+menuheight)>clientHeight){_top=_top-menuheight;}
	        $(".right_menu").show().css({left:_left,top:_top});
	    } else if(key == 1) {
	    	$(".right_menu").hide();
	    }
	});
	
	$(".trfile").mousedown(function(e){
	    var key = e.which;
	    if(key == 3){
	    	var _top =e.clientY;
	    	var _left =e.clientX;
	    	$(this).find("i[name='fileCheck']").addClass("tab_checked");
	        var obj = $(this).find("i[name='fileCheck']");
	        selectCheckBox(obj);
	        //获取弹出菜单的宽度和高度
	        var menuwidth=$(".right_menu").width();
	        var menuheight=$(".right_menu").height();
	        //获取浏览器可视宽度和高度
	        var clientHeight=getClientHeight();
	        var clientWidth=getClientWidth();
	        //判断当光标靠边时，防止弹出菜单溢出浏览器可视范围
	        if((_left+menuwidth)>clientWidth){_left=clientWidth-menuwidth-10;}
	        if((_top+menuheight)>clientHeight){_top=_top-menuheight;}
	        $(".right_menu").show().css({left:_left,top:_top});
	    } else if(key == 1) {
	    	$(".right_menu").hide();
	    }
	});
	
	//移除权限
	$(".ac_reauth").live("click",function(){
		if(!isOper){
		       return;
		    }
		delAuthorityBatch();
	});
	
	//文件标签
    $('.ac_sign').on('click', function(){
    	if(!isOper){
		    return;
		}
    	var data = getCheckSelected();
    	if(data.folders.length>0 || data.files.length!=1){
    		layer.msg('请勾选单个文件！',{icon: 5,time:1500});
    		return;
    	}
        layer.open({
            type: 2,
            title :'文件标签',
            area: ['800px', '450px'],
            shadeClose: false, //点击遮罩关闭
            content: basepath + '/sign/list/'+data.files[0].id
            ,btn: ['确定', '关闭'],
            yes: function(index, layero){
          	    //按钮【确认】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addFileSign() //得到iframe页的body内容
    			layer.closeAll();
    			layer.msg('设置标签成功！',{icon: 1,time:1500});
            	setTimeout(function(){
            		refresh();
            	}, 1500);
          	  }
        });
    });
    
    //创建文件夹
    $(".ac_folder").live('click',function(){
    	if(!isOper){
    		return;
    	}
        var str=$("#new_tr > tbody").html();
        $("#new_tr > tbody").siblings(".file_name").show();
        $("#tabs").prepend(str)
        $("#mainContent").show();
        $("#noneContent").hide();
        //选中input
        $(".input_filename").eq(1).focus();
        $(".input_filename").eq(1).select();
        disOper();
    });
    
    //重命名
    $(".ac_rename").live('click',function(){
    	if(!isOper){
    		return;
    	}
    	var data = getCheckSelected();
    	if(data.files.length + data.folders.length!=1){
    		layer.msg('请勾选单个文件或者文件夹！',{icon: 5,time:1500});
    		return false;
    	}
    	var obj;
   	    $(".tab_checked").each(function(){
   		   if($(this).attr('id') != 'allCheck'){
   			 obj = $(this);
   		   }
   	    });
		obj.parent().next().find(".edit_name").show().siblings(".file_name").hide();
		// 显示
		var fileType = obj.parent().parent().find('.tab_check').attr('name');
		if (fileType != undefined && fileType.indexOf('folderCheck') != -1) {
			// 文件夹
			var nameText = obj.parent().next().find(".edit_name");
			var name = nameText.find('input').val();
			nameText.find('input').select();
		} else {
			// 文件
			var fileName = obj.parent().next().find(".file_name a").html();
			var objText = obj.parent().next().find(".input_filename");
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
			objText.val(fileName);
			objText.select();
		}
		disOper();
    });
    
    //刷新按钮
    $(".ac_refresh").live('click',function(){
    	refresh();
    });
    
    //重命名或者添加的确定按钮
    $(".filebox").find(".yes").click(function(){
    	openOper();
       /* $(this).parents(".edit_name").hide().siblings(".file_name").show();*/
    }); 
    
	 //移动
	 $('.ac_move').on('click', function(){
	   if(!isOper){
    		return;
       }
	   //表示弹窗为移动操作
	   $("#operType").val(1);
	   openFolderWindow();
	 });
   
	 //复制
	 $('.ac_copy').on('click', function(){
		if(!isOper){
	       return;
	    }
	    //表示弹窗为移动操作
	    $("#operType").val(2);
	    openFolderWindow();
	 });
	 
	 //删除
	 $('.ac_del').on('click', function(){
		if(!isOper){
	       return;
	    }
	    delFolderFile();
	 });
    
	 //提取码
	 $(".ac_take").on('click',function(){
		 if(!isOper){
		    return;
		 }
		 intoTakeCode();
	 });
	 
	 //分享按钮
	 $(".ac_share").on('click',function(){
		 if(!isOper){
		    return;
		 }
		 var data = getCheckSelected();
		 if(data.files.length + data.folders.length == 0){
			 layer.msg('请勾选文件或者文件夹！',{icon: 5,time:1500});
			 return;
		 }
		 var bool = false;
		 $(".tab_checked").each(function(){
			  var size = $(this).parent().next().find('.share').length;
			  if(size > 0){
				  bool = true;
				  return false;
			  }
		 });
		 if(bool && data.files.length + data.folders.length >= 2){
			 layer.msg('勾选的多个文件可能存在权限差异，请单独分享！',{icon: 5,time:1500});
			 return;
		 }
		 
		 if(data.files.length + data.folders.length  >= 2){
			 shareMoreAuth(data);
		 } else {
			 var openType = 1;
			 var openIds = new Array();
			 if(data.files.length > 0){
				 openType = 2;
				 openId = data.files[0].id
			 } else {
				 openId = data.folders[0].id
			 }
			 shareAuth(openId, openType);
		 }
		 
		 
	 });
	 
    //取消重命名
    $(".cancel").live("click",function(){
       //$(this).parents('tr').siblings('tr').eq('tr.length-1').remove();
        if($(this).hasClass('edit')){
        	$(this).parents(".edit_name").hide().siblings(".file_name").show();
        	if($(this).hasClass('folder')){
        		//文件夹
        		var folderName = $(this).parent().parent().find(".file_name").find('a').html();
        		$(this).parents(".edit_name").find('input').val(folderName);
        	} else {
        		//文件
        		var fileName = $(this).parent().parent().find(".file_name").html();
        		$(this).parents(".edit_name").find('input').val(fileName);
        	}
        } else {
     	   //添加的取消
        	$(this).parents("tr").remove();
            $("#folderName").val('');
        } 
        openOper();
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
			layer.msg('请先选择需要下载的文件或者目录！',{icon: 5,time:1000});
			return false;
		}
		if((data.folders.length + data.files.length > 1) || (data.folders != null && data.folders.length > 0)){
			$("#showMsg").html("正在打包文件，请稍后...");
	  		$(".ts_box").show();
		}
  		setTimeout(function(){
  			$.ajax({
  	   			url: basepath+"/personal/download",
  	   	        dataType:"json",
  	   	        type: 'POST',
  	   	     	data: JSON.stringify(data),
  	   	     	contentType: "application/json",
  	   	        async: false, 
  	   	        success: function(result){
  	   	            checkErrorCode(result);
  	   	        	if((data.folders.length + data.files.length > 1) || (data.folders != null && data.folders.length > 0)){
		  	   	   		$(".ts_box").hide();
		  	 		}
  	   		        if (result.code == 1000 && result.result == "SUCCESS") {
  	   		        	window.location.href = fileServiceUrl + "/fileManager/download?params=" + result.codeInfo;
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
   			url: basepath+"/personal/read",
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
      * 上传文件
      */
 	$('#uploadFile').on('click', function(){
 		var data_url = fileServiceUrl + '/authorize/toUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
 	});
 	
    /**
     * 上传文件夹
     */
	$('#uploadFolder').on('click', function(){
		var data_url = fileServiceUrl + '/authorize/toFolderUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
	});
 	
 	/**
 	 * 预览
 	 */
 	$(".previewFile").on("click", function(){
 		var fileId = $(this).attr("fileId");
 		if(fileId == ""){
 			layer.msg('请先点击需要预览的文件！',{icon: 5,time:1000});
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
                title :'文档预览-'+fileName,
                area: ['800px', '600px'],
                shadeClose: false, //点击遮罩关闭
                maxmin:true,
                content: fileServiceUrl + "/fileManager/onlineEditTxt?params="+ fileId + "&read=0"
                ,btn: ['关闭']
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
		        	checkErrorCode(result);
			        if (result.code == 1000 && result.result == "SUCCESS") {
			        	window.open(fileServiceUrl + "/fileManager/previewConvert?params=" + result.codeInfo);
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
 			layer.msg('请选择需要在线编辑的文件！',{icon: 5,time:1000});
 	   		return false;
 		}
 		var fileName = $(".tab_contain").find(".tab_checked").parent().next().find(".input_filename").val();
 		$.ajax({
			url: basepath + "/personal/onlineEdit",
	        dataType:"json",
	        type: "POST",
	     	data: {fileId: fileId},
	        async: false, 
	        success: function(data){
	        	checkErrorCode(data);
		        if (data.code == 1000) {
		        	var index = fileName.lastIndexOf(".");
		        	//判断是否为文档文档编辑(true)还是word相关文档编辑(false)
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
					//文本文档
					if(bool){
						layer.open({
			                type: 2,
			                title :'在线编辑',
			                area: ['800px', '600px'],
			                shadeClose: false, //点击遮罩关闭
			                maxmin:true,
			                content: data.result
			                ,btn: ['修改', '关闭'],
			                yes: function(index, layero){
			              	    //按钮【确认】的回调
			                	var iframeWindow = window[layero.find('iframe')[0]['name']];
		                        //获取要发送给框架页面的消息
			                	messageEvent.postMessage(iframeWindow, "edit", '*'); 
			                }
			            });
					} else {
						//word文档编辑
						window.open(data.result);
					}
		        }else{
		        	layer.msg(data.codeInfo,{icon: 5,time:1000});
		        }
	        }
 		})
 		
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
 	/*********************************************************************右键菜单*****************************************************************************/
 	//打开
 	$(".openMenu").live('click',function(){
 		$(".right_menu").hide();
 		var data = getCheckSelected();
 		if(data.folders.length + data.files.length != 1){
 			layer.msg('无法同时打开多个文件或者文件夹！',{icon: 5,time:1000});
 	   		return false;
 		}
 		//判断是文件夹还是文件
 		if(data.folders.length == 1){
 			//文件夹直接跳转
 			window.location.href = basepath + "/personal/index?folderId="+ data.folders[0].id;
 		} else {
 			//文件 （分为两种， 第一种为图片 ， 第二种为文件）
 			//1.图片
 			var obj = $(".tab_checked").parent().parent().find(".imageFile").attr("fileid");
 			if(obj != undefined && obj != 'undefined'){
 				var fileIds = new Array();
 	 		    var names = new Array();
 	 		    var index = 0 ;
 	 		    var clickFileId = obj;
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
 	 		   		url: basepath+"/personal/read",
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
 	 		} else {
 	 			var fileId = $(".tab_checked").attr("value");
 	 	 		if(fileId == ""){
 	 	 			layer.msg('请先点击需要预览的文件！',{icon: 5,time:1000});
 	 	 	   		return false;
 	 	 		}
 	 	 		var fileName = $(".tab_contain").find(".tab_checked").parent().next().find(".input_filename").val();
 	 	 		$.ajax({
 	 				url: basepath+"/personal/preview",
 	 		        dataType:"json",
 	 		        type: "POST",
 	 		     	data: {fileId: fileId},
 	 		        async: false, 
 	 		        success: function(result){
 	 		        	checkErrorCode(result);
 	 			        if (result.code == 1000 && result.result == "SUCCESS") {
 	 			        	var index = fileName.lastIndexOf(".");
 	 			        	//判断是否为文档文档编辑(true)还是word相关文档编辑(false)
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
 	 						//文本文档
 	 						if(bool){
 	 							layer.open({
 	 				                type: 2,
 	 				                title :'在线编辑',
 	 				                area: ['800px', '600px'],
 	 				                shadeClose: false, //点击遮罩关闭
 	 				                maxmin:true,
 	 				                content: fileServiceUrl + "/fileManager/onlineEditTxt?params="+ fileId
 	 				                ,btn: ['修改', '关闭'],
 	 				                yes: function(index, layero){
 	 				              	    //按钮【确认】的回调
 	 				                	var iframeWindow = window[layero.find('iframe')[0]['name']];
 	 			                        //获取要发送给框架页面的消息
 	 				                	messageEvent.postMessage(iframeWindow, "edit", '*'); 
 	 				                }
 	 				            });
 	 						} else {
 	 							window.open(fileServiceUrl + "/fileManager/previewConvert?params=" + result.codeInfo);
 	 						}
 	 			        }else{
 	 			        	layer.msg(result.codeInfo,{icon: 5,time:1000});
 	 			        }
 	 		        }
 	 	 		});
 	 		}
 		}
 	});
	//解压
 	$(".decompressMenu").click(function(){
 		var fileId = $(".tab_contain").find(".tab_checked").attr('value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg('请选择需要在线解压的文件！',{icon: 5,time:1000});
 	   		return false;
 		}
 		var fileName = $(".tab_contain").find(".tab_checked").parent().next().find(".input_filename").val();
 		$("#showMsg").html("正在解压文件，请稍后...");
  		$(".ts_box").show();
  		 $.ajax({
  			  url: fileServiceUrl + "/fileManager/decompressZip?callback=jsonpCallback",
  			  data: "fileId="+fileId,
  	          dataType: "jsonp",
  	          success: function (data) {
  	          }
  	      })
 	});
})

//解压返回
function jsonpCallback(data){
	checkErrorCode(data);
	if(data.code == 1000){
		layer.msg("解压成功!",{icon: 1,time:1000});
		setTimeout(function(){
    		refresh();
    	}, 1500);
	}
	$(".ts_box").hide();
}


//上传的公共处理方法
function uploadFunc(data_url){
	var folderId = $.trim($("#parentId").val());
	if(folderId == ""){
		layer.msg('请先选择需要上传的目录！',{icon: 5,time:1000});
   		return false;
	}
	var data_url = data_url + '&folderId=' + folderId;
    layer.open({
         type: 2,
         title :'上传文件',
         area: ['800px', '470px'],
         shadeClose: false, //点击遮罩关闭
         maxmin: true,
         content: data_url,
         cancel: function(){ 
             refresh();
         }
     });
}

//选中操作后选中的分享文件id(自有)
function getCheckSelectedOpenId(){
   var authorityArray = new Array();
   $("i[code='authorityCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var openId= $(this).find("input[type='checkbox']").val();
	   		var openType= $(this).find("input[name='opentype']").val();
	   		var authorityJson = {
	   			"openId" : openId,
	   			"openType" : openType
	   		}
	   		authorityArray.push(authorityJson);
	   	 }
    });
    var data = {
		"shares" : authorityArray
	}
    return data;
}

//移除权限(批量)（自有）
function delAuthorityBatch(){
	var data = getCheckSelectedOpenId();
	if(data.shares.length == 0){
		 layer.msg('请先选择文件！',{icon: 5,time:1000});
   		 return false;
   	}
	var dataname = getCheckSelected();
	//构建移除展示
	var nameArray = new Array();
	for(var i=0;i<dataname.folders.length;i++){
		var r = "#foldername"+dataname.folders[i].id;
    	var foldername = $(r).html();
    	nameArray.push(foldername);
	}
	for(var i=0;i<dataname.files.length;i++){
		var r = "#filename"+dataname.files[i].id;
    	var filename = $(r).html();
    	nameArray.push(filename);
	}
	if(nameArray.length == 1){
		$("#move").html("您确定要移除“"+nameArray+"”吗？");
	} else {
		$("#move").html("您确定要移除选中的"+nameArray.length+"个文件吗?");
	}
	layer.open({
        type: 1,
        title :"移除权限",
        area: ['600px', '183px'],
        shadeClose: false, //点击遮罩关闭
        content: $('.con_remove_qx')
        ,btn: ['确定', '关闭'],
        yes: function(index, layero){
       	   //在进行管理
       	   $.ajax({
       		  url: basepath+"/share/moveauthbatch",
              dataType:"json",
              type: 'POST',
              data: JSON.stringify(data),
              contentType: "application/json",
              async: false, 
              success: function(data){
       	        if (data.code == 1000) { 
       	        	layer.closeAll();
       	        	layer.msg('移除成功！',{icon: 1,time:2000});
    	        	setTimeout(function(){
    	        		window.location.reload();
    	        	}, 2000);
       	        }
           }});  
      	}
    });
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
    showOperBtn(folderArray, fileArray);
    //showOperMenu(folderArray, fileArray);
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
        title :"文件版本管理",
        area: ['800px', '460px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/personal/version?fileId='+id+"&fileVersion="+version
        ,btn: ['关闭']
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
        title :'设置提取码',
        area: ['800px', '380px'],
        shadeClose: false, //点击遮罩关闭
        content: $(".con_tiqu")
        ,btn: ['关闭']
    });
}

//创建提取码
function createTakeCode(id){
	//获取失效时间
	var timestr = $("#effectivetime").val();
	layer.closeAll();
    layer.open({
        type: 2,
        title :'设置提取码',
        area: ['800px', '380px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/takecode/toinsert/'+id+'?timestr='+timestr
        ,btn: ['关闭']
    });
}

//创建提取码(多个)
function createTakeCodeMore(fileIds){
	//获取失效时间
	var timestr = $("#effectivetime").val();
	layer.closeAll();
	layer.open({
		type: 2,
		title :'设置提取码',
		area: ['800px', '380px'],
		shadeClose: false, //点击遮罩关闭
		content: basepath + '/takecode/toinsertmore?fileIds='+fileIds+'&timestr='+timestr,
		btn: ['确认', '关闭']
	});
}

function openFolderWindow(){
	var data = getCheckSelected();
	if(data.folders.length + data.files.length == 0){
		 layer.msg('请勾选文件或者文件夹！',{icon: 5,time:1500});
		 return false;
	}
	var title = getTitle();
	var openHtml = getOperTypeHtml();
	$("#operTypeHtml").html(openHtml);
    layer.open({
        type: 2,
        title :title,
        area: ['800px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/personal/folderTree'
        ,btn: ['确定', '关闭'],
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
				layer.msg('不能将文件移动或复制到自身或其子目录下!',{icon: 5,time:1500});
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
	   		var folderId = $(this).find("input[type='checkbox']").val();
	   		var folderCode = $(this).find("input[name='code']").val();
	   		var employeeId = $(this).find("input[name='employeeId']").val();
	   		var pFolderId = $(this).find("input[name='pFolderId']").val();
	   		//判断选中的文件夹为当前文件夹或者付文件夹 或者子文件夹都是不能通过
	   	    //当前文件夹
	   		if(selectFolderId == folderId && sessionEmployeeId == employeeId){
	   			bool = false;
	   		}
	   	    //父文件夹
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
		layer.msg('文件夹名不能为空！',{icon: 5,time:1000});
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
	layer.closeAll();
	$("#showMsg").html("正在复制文件，请稍后...");
	$(".ts_box").show();
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
	            layer.msg('复制成功！',{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1500);
	        }
     }}); 
}

//移动文件
function moveFolderFile(initData){
	layer.closeAll();
	$("#showMsg").html("正在移动文件，请稍后...");
	$(".ts_box").show();
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
	//var updateOldTime = getSmpFormatDateByLong(,true);
	$("#oldUpdateTime").html(oldFolders[indexWin].updateDate);
	//设置显示
	$("#nowFolderName").html(nowFolders[indexWin].folderName);
	//var updateTime = getSmpFormatDateByLong(,true);
	$("#nowUpdateTime").html(nowFolders[indexWin].updateDate);
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
        ,btn: ['跳过','保留两个文件夹','关闭'],
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
	//var updateOldTime = getSmpFormatDateByLong(oldFiles[indexWin].updateTime,true);
	$("#oldUpdateTime").html(oldFiles[indexWin].updateDate);
	//设置显示
	$("#oldFileImg").addClass("tips_bg_"+nowFiles[indexWin].fileType);
	$("#nowFileImg").addClass("tips_bg_"+nowFiles[indexWin].fileType);
	$("#nowFileName").html(nowFiles[indexWin].fileName);
	//var updateNowTime = getSmpFormatDateByLong(nowFiles[indexWin].updateTime,true);
	$("#nowUpdateTime").html(nowFiles[indexWin].updateDate);
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
        ,btn: ['跳过','保留两个文件','关闭'],
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
	var title = '复制到';
	if(operType == 1){
		title = '移动到';
	}
	return title;
}

//获取操作名称
function getOperTypeHtml(){
	var operType = $("#operType").val();
	var operTypeHtml = "复制";
	if(operType == 1){
		operTypeHtml = "移动";
	}
	return operTypeHtml;
}


//重命名操作
function operRename(obj, openId, openType){
	var operName = $(obj).parent().find('input').val();
	if(operName == ''){
		if(openType == 1){
			layer.msg('文件夹名不能为空！',{icon: 5,time:1000});
		} else {
			layer.msg('文件名不能为空！',{icon: 5,time:1000});
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
	//验证是否重名文件
	 $.ajax({
		url: basepath+"/personal/checkSameName",
        dataType:"json",
        type: 'POST',
        data: json,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000 && data.result) {
	        	$("#move").html("此目录下已存在同名文件，是否要保存两个文件");
	        	//存在弹窗
	        	layer.open({
	                type: 1,
	                title :"重命名",
	                area: ['600px', '183px'],
	                shadeClose: false, //点击遮罩关闭
	                content: $('.con_remove_qx'),
	                btn: ['保留两个文件', '取消'],
	                yes: function(index, layero){
	               	   //在进行管理
	                	layer.closeAll();
	                	reNameFunc(json);
	              	},
	              	btn2:function(){
	              		layer.closeAll();
	              		refresh();
	              	}
	        	});
	        } else {
	        	reNameFunc(json);
	        }
	  }});
}

function reNameFunc(data){
	$.ajax({
		url: basepath+"/personal/rename",
        dataType:"json",
        type: 'POST',
        data: data,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	            //进行验证
	        	layer.msg('重命名成功！',{icon: 1,time:1500});
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
		$("#move").html("您确定要删除“"+dataNames+"”吗？");
	} else {
		$("#move").html("您确定要删除选中的"+dataName.length+"个文件或文件夹吗?");
	}
	if(data.folders.length + data.files.length == 0){
		 layer.msg('请勾选文件或者文件夹！',{icon: 5,time:1500});
		 return false;
	}
	layer.open({
        type: 1,
        title :"删除文件",
        area: ['600px', '183px'],
        shadeClose: false, //点击遮罩关闭
        content: $('.con_remove_qx')
        ,btn: ['确定', '关闭'],
        yes: function(index, layero){
           layer.closeAll();
           $("#showMsg").html("正在删除文件，请稍后...");
       	   $(".ts_box").show();
       	   //在进行管理
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
	       	        layer.msg('删除成功！',{icon: 1,time:1500});
		        	setTimeout(function(){
		        		refresh();
		        	}, 1500);
       	        }
           }});  
      	}
    });
}

//分享界面
function shareAuth(openId , openType){
	var dataUrl = basepath + '/personal/authPage?openId='+openId+'&openType='+openType;
    layer.open({
        type: 2,
        title :'设置分享范围',
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: ['确定', '关闭'],
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
        title :'设置分享范围',
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: ['确定', '关闭'],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var data = iframeWin.getSelectedData(); //得到iframe页的body内容
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
  	            layer.msg('操作成功！',{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1000);
  	        }
      }});  
}

//根据勾选数量显示不同操作按钮
function showOperBtn(folderArray, fileArray){
	hideAllBtn();
	if(folderArray.length > 0){
		if(folderArray.length == 1 && fileArray.length == 0){
			//至勾选文件夹文件夹
			//显示下载、复制、移动、移除、分享、重命名
			$(".ac_down").show();
			$(".ac_del").show();
			$(".ac_rename").show();
			$(".ac_share").show();
			$(".ac_reauth").show();
			$(".openMenu").show();
		} 
		//显示下载、复制、移动、移除
		$(".ac_down").show();
		$(".ac_del").show();
		$(".ac_share").show();
		$(".ac_reauth").show();
	} else {
		if(fileArray.length > 0 && fileArray.length != 1){
			//显示下载、复制、移动、移除、分享、重命名
			$(".ac_down").show();
			$(".ac_del").show();
			$(".ac_reauth").show();
			$(".ac_take").show();
		} else if(fileArray.length == 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAllBtn();
			$(".decompressMenu").hide();
			$(".ac_edit").hide();
			//显示编辑按钮
			var index = fileArray[0].name.lastIndexOf(".");
			if(index > 0){
				var lastFileName = fileArray[0].name.substring(index);
				//判断是否在可以解压的文档格式内
				for(var i in decompressType){
					if(decompressType[i] == lastFileName){
						$(".decompressMenu").show();
						break;
					}
				}
				for(var i in editType){
					if(editType[i] == lastFileName){
						$(".ac_edit").show();
						return;
					}
				}
			}
		}
	}
}

//隐藏所有操作按钮
function hideAllBtn(){
	$(".ac_down").hide();
	$(".ac_del").hide();
	$(".ac_share").hide();
	$(".ac_rename").hide();
	$(".ac_sign").hide();
	$(".ac_take").hide();
	$(".ac_share").hide();
	$(".ac_reauth").hide();
	$(".ac_edit").hide();
	$(".right_menu").find("li").hide();
}

//显示所有BTN
function showAllBtn(){
	$(".ac_down").show();
	$(".ac_del").show();
	$(".ac_share").show();
	$(".ac_rename").show();
	$(".ac_sign").show();
	$(".ac_take").show();
	$(".ac_share").show();
	$(".ac_reauth").show();
	$(".right_menu").find("li").show();
}

//个人空间权限按钮,用户关系分配
function permissionHide(){
	var permissionStr = $("#permissionStr").val();
	var permissionArr = permissionStr.split(",");
	if($.inArray("1", permissionArr)==-1){
		$(".ac_folder").remove();
	}
	if($.inArray("2", permissionArr)==-1){
		$(".ac_upload").remove();
	}
	if($.inArray("3", permissionArr)==-1){
		$(".ac_down").remove();
	}
	if($.inArray("4", permissionArr)==-1){
		$(".ac_move").remove();
	}
	if($.inArray("5", permissionArr)==-1){
		$(".ac_copy").remove();
	}
	if($.inArray("6", permissionArr)==-1){
		$(".ac_del").remove();
	}
	if($.inArray("7", permissionArr)==-1){
		$(".ac_rename").remove();
	}
	if($.inArray("8", permissionArr)==-1){
		$(".ac_take").remove();
	}
	if($.inArray("9", permissionArr)==-1){
		$(".ac_sign").remove();
	}
	if($.inArray("10", permissionArr)==-1){
		$(".ac_share").remove();
	}
	if($.inArray("11", permissionArr)==-1){
		$(".ac_edit").remove();
	}
}
