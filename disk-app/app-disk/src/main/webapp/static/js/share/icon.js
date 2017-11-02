

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
	
	//移除权限
	$(".ac_reauth").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		if(!isOper){
		       return;
		    }
		delAuthorityBatch();
	});
	
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
	
	//排序
    /*$(".array_item a").on("click",function(){
        $(this).addClass("active").siblings().removeClass("active");
    });*/
    
    //勾选文件夹or文件右上方框
    $(".array_list li i").on("click",function(){
        $(this).siblings(".tips_img").toggleClass("active");
        $(this).toggleClass("yes");
        selectCheckBox($(this));
    });
	
	//点击文件夹进入下级列表
    $(".tfile").click(function(){
    	if(!isOper){
    		return;
    	}
    	var folderId = $(this).attr("key-value");
    	window.location.href=basepath + "/share/indexinto/"+folderId;
    });
	
    //全选按钮
    $(".tab_check").live("click",function(){
    	if(!isOper){
    		return;
    	}
    	$(this).toggleClass("tab_checked");
    	selectCheckBox($(this));
     });
     
    //创建文件夹时，判断该隐藏域值，如果是0,能创建，如果是1，不能执行创建文件夹js(无用，isOper会判断是否能点击新建文件夹)
	//$("#addIndex").val(0);
	
    //鼠标点击文件or文件夹(不包括右上方框)，若是左键点击，隐藏右键菜单，若是右键点击，展示右键菜单
	$(".tips_img").mousedown(function(e){
	    var key = e.which;
	    if(!isOper){
		    return;
		}
	    if(key == 3){
	    	var _top =e.clientY;
	    	var _left =e.clientX;
	    	$(this).parent().find("i[name='folderCheck']").addClass("yes");
	    	$(this).parent().find("i[name='fileCheck']").addClass("yes");
	    	$(this).addClass('active')
	        selectCheckBox($(this).next());
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
	
	//文件标签
    $('.ac_sign').on('click', function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	if(!isOper){
		    return;
		}
    	var data = getCheckSelected();
    	if(data.folders.length>0 || data.files.length!=1){
    		layer.msg(please_check_a_single_file,{icon: 5,time:1500});
    		return;
    	}
        layer.open({
            type: 2,
            title :file_label,
            area: ['800px', '450px'],
            shadeClose: false, //点击遮罩关闭
            content: basepath + '/sign/list/'+data.files[0].id
            ,btn: [determine, close],
            yes: function(index, layero){
          	    //按钮【确认】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addFileSign() //得到iframe页的body内容
    			layer.closeAll();
    			layer.msg(set_tag_success,{icon: 1,time:1500});
            	setTimeout(function(){
            		refresh();
            	}, 1500);
          	  }
        });
    });
    
  //新建文件夹及重命名回车事件
    $('.input_filename').live('keydown',function(e){
    	if(e.keyCode==13){
    		$(this).next().trigger("click");
    	}
	});
    
    //创建文件夹
    $(".ac_folder").live('click',function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	if(!isOper){
    		return;
    	}
        $(".addFolder").show();
        //选中input
        $(".input_filename").eq(0).focus();
        $(".input_filename").eq(0).select();
        //设置为false，其他一些事件不允许执行
        disOper();
    });
    
    //重命名
    $(".ac_rename").live('click',function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	if(!isOper){
    		return;
    	}
    	var data = getCheckSelected();
    	if(data.files.length + data.folders.length!=1){
    		layer.msg(please_check_a_single_file_or_folder,{icon: 5,time:1500});
    		return false;
    	}
		
		// 显示
		var fileType = $(".yes").prev().attr('class');
		$(".yes").parent().find(".txt_name").hide();
		$(".yes").parent().find(".txt_name").next().show();
		if (fileType != undefined && fileType.indexOf('tfile') != -1) {
			// 文件夹
			$(".yes").parent().find(".input_filename").select();
		} else {
			// 文件
			var fileName = $(".yes").parent().find(".txt_name").find('a').html();
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
			var objText = $(".yes").parent().find(".txt_name").next().find('.input_filename');
			objText.val(fileName);
			objText.select();
		}
		disOper();
    });
    
    //刷新按钮
    $(".ac_refresh").live('click',function(){
    	refresh();
    });
    
    //重命名或者添加的确定按钮(无用)
    /*$(".filebox").find(".yes").click(function(){
    	openOper();
        $(this).parents(".edit_name").hide().siblings(".file_name").show();
    }); */
    
	 //删除
	 $('.ac_del').on('click', function(){
		 if(!checkPermission(this)){
	    		return;
	    	}
		if(!isOper){
	       return;
	    }
	    delFolderFile();
	 });
    
	 //提取码
	 $(".ac_take").on('click',function(){
		 if(!checkPermission(this)){
	    		return;
	    	}
		 if(!isOper){
		    return;
		 }
		 intoTakeCode();
	 });
	 
	 //分享按钮
	 $(".ac_share").on('click',function(){
		 if(!checkPermission(this)){
	    		return;
	    	}
		 if(!isOper){
		    return;
		 }
		 var data = getCheckSelected();
		 if(data.files.length + data.folders.length == 0){
			 layer.msg(please_check_the_file_or_folder,{icon: 5,time:1500});
			 return;
		 }
		 var bool = false;
		 $(".yes").each(function(){
			  var size = $(this).parent().find('.sharetip').length;
			  if(size > 0){
				  bool = true;
				  return false;
			  }
		 });
		 if(bool && data.files.length + data.folders.length >= 2){
			 layer.msg(differences_select_auth,{icon: 5,time:1500});
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
    $(".false").live("click",function(){
        var data  = $(this).parents('li');
        if(!data.hasClass('addFolder')){
        	//取消
        	data.find(".txt_name").show();
        	data.find(".txt_name").next().hide();
        } else {
     	    //添加的取消
        	data.hide();
        	data.find(".input_filename").val(new_folder);
        } 
        openOper();
    });

     
    /**
     * 下载
     */
     $(".ac_down").live("click",function(){
    	 if(!checkPermission(this)){
     		return;
     	}
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
  	   		        	window.location.href = result.codeInfo;
  	   		        }
  	   	        }
  	   		 });
  		}, 100);
     });
    
     
     /**
      * 图片预览
      */
      $(".imageFile").live("click",function(){
        var clickFileId = $(this).attr('key-value');
        loadImage(clickFileId);
      });
     
      /**
       * 图片预览
       */
       $(".imageFileA").live("click",function(){
           var clickFileId = $(this).attr('key-value');
    	   loadImage(clickFileId);
       });
      
     
     /**
      * 上传文件
      */
 	$('#uploadFile').on('click', function(){
 		if(!checkPermission(this)){
    		return;
    	}
 		var data_url = fileServiceUrl + '/authorize/toUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
 	});
 	
    /**
     * 上传文件夹
     */
	$('#uploadFolder').on('click', function(){
		if(!checkPermission(this)){
    		return;
    	}
		var data_url = fileServiceUrl + '/authorize/toFolderUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
	});
 	
 	/**
 	 * 预览
 	 */
 	$(".previewFile").live("click", function(){
 		var fileId = $(this).attr("key-value");
 		if(fileId == ""){
 			layer.msg(please_click_on_the_file_preview,{icon: 5,time:1000});
 	   		return false;
 		}
 		var fileName = $(this).parents('li').find(".txt_name").find('a').html();
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
		        	checkErrorCode(result);
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
 		if(!checkPermission(this)){
    		return;
    	}
 		var fileId = $(".yes").prev().attr('key-value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg(please_select_a_file_edited_online,{icon: 5,time:1000});
 	   		return false;
 		}
 		var fileName = $(".yes").parent().find(".txt_name").find('a').html();
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
			                title :online_editing,
			                area: ['800px', '600px'],
			                shadeClose: false, //点击遮罩关闭
			                maxmin:true,
			                content: data.result
			                ,btn: [modify, close],
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
 			layer.msg(unable_open_multiple_files_the_sametime,{icon: 5,time:1000});
 	   		return false;
 		}
 		//判断是文件夹还是文件
 		if(data.folders.length == 1){
 			//文件夹直接跳转
 			window.location.href = basepath + "/personal/index?folderId="+ data.folders[0].id;
 		} else {
 			//文件 （分为两种， 第一种为图片 ， 第二种为文件）
 			//1.图片
 			var obj = $(".yes").prev().parent().find('.imageFile').attr("key-value");
 			if(obj != undefined && obj != 'undefined'){
 				var fileIds = new Array();
 	 		    var names = new Array();
 	 		    var index = 0 ;
 	 		    var clickFileId = obj;
 	 		    $(".imageFile").each(function(i){
 	 		    	var fileId = $(this).attr('key-value');
 	 		    	if(clickFileId == fileId){
 	 		    		index = i;
 	 		    	}
 	 		    	fileIds.push(fileId);
 	 		    	names.push($(this).parent().find(".txt_name").find('a').html());
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
 	 		    				/*helpers : {
 	 		    					thumbs : {
 	 		    						width: 75,
 	 		    						height: 50
 	 		    					}
 	 		   	      			},*/
 	 		   	      			preload : 0,
 	 		   	      			index : index
 	 		   	      		});
 	 		   	        }
 	 		   	    }
 	 		   	});
 	 		} else {
 	 			var fileId = $(".yes").prev().attr("key-value");
 	 	 		if(fileId == ""){
 	 	 			layer.msg(please_click_on_the_file_preview,{icon: 5,time:1000});
 	 	 	   		return false;
 	 	 		}
 	 	 		var fileName = $(".yes").parent().find(".txt_name").find('a').html();
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
 	 				                title :online_editing,
 	 				                area: ['800px', '600px'],
 	 				                shadeClose: false, //点击遮罩关闭
 	 				                maxmin:true,
 	 				                content: fileServiceUrl + "/fileManager/onlineEditTxt?params="+ fileId + "&read=0"
	 				                ,btn: [close]
 	 				                /*content: fileServiceUrl + "/fileManager/onlineEditTxt?params="+ fileId
 	 				                ,btn: [modify, close],
 	 				                yes: function(index, layero){
 	 				              	    //按钮【确认】的回调
 	 				                	var iframeWindow = window[layero.find('iframe')[0]['name']];
 	 			                        //获取要发送给框架页面的消息
 	 				                	messageEvent.postMessage(iframeWindow, "edit", '*'); 
 	 				                }*/
 	 				            });
 	 						} else {
 	 							window.open(result.codeInfo);
 	 						}
 	 			        }else{
 	 			        	layer.msg(result.codeInfo,{icon: 5,time:1000});
 	 			        }
 	 		        }
 	 	 		});
 	 		}
 		}
 	});
 	
})

function loadImage(clickFileId){
	var fileIds = new Array();
    var names = new Array();
    var index = 0 ;
	$(".imageFile").each(function(i){
		var fileId = $(this).attr('key-value');
		if(clickFileId == fileId){
			index = i;
		}
		fileIds.push(fileId);
		names.push($(this).parent().find(".txt_name").find('a').html());
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
}



//选中操作后选中的分享文件id(自有)
function getCheckSelectedOpenId(){
   var authorityArray = new Array();
   $("i[code='authorityCheck']").each(function(){
	   	 if($(this).attr('class') != undefined && $(this).attr('class').indexOf('yes') != -1){
	   		var openId= $(this).parent().find("div input[type='checkbox']").val();
	   		var openType= $(this).parent().find("div input[name='opentype']").val();
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
		 layer.msg(please_select_a_file,{icon: 5,time:1000});
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
		$("#move").html(sure_to_remove+"“"+nameArray+"”？");
	} else {
		$("#move").html(sure_to_remove_selected+nameArray.length+file_or_folder+"?");
	}
	layer.open({
        type: 1,
        title :remove_permissions,
        area: ['600px', '183px'],
        shadeClose: false, //点击遮罩关闭
        content: $('.con_remove_qx')
        ,btn: [determine, close],
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
       	        	layer.msg(remove_success,{icon: 1,time:2000});
    	        	setTimeout(function(){
    	        		window.location.reload();
    	        	}, 2000);
       	        }
           }});  
      	}
    });
}

//上传的公共处理方法
function uploadFunc(data_url){
	var folderId = $.trim($("#parentId").val());
	if(folderId == ""){
		layer.msg(please_select_the_directory_upload,{icon: 5,time:1000});
   		return false;
	}
	var data_url = data_url + '&folderId=' + folderId;
	//var data_url = data_url + '&folderId=-1';
    layer.open({
         type: 2,
         title :upload_files,
         area: ['800px', '470px'],
         shadeClose: false, //点击遮罩关闭
         maxmin: true,
         content: data_url,
         cancel: function(){ 
             refresh();
         }
     });
}

// 点击操作
function selectCheckBox(obj){
	var _class = obj.attr('class');
	if(_class == undefined){
		return ;
	}
	var bool = true;
	if(_class.indexOf('tab_checked') == -1){
		$("#allCheck").removeClass("tab_checked");
		bool = false;
	}
    //计算选中记录条数
    var folderArray = new Array();
    var fileArray = new Array();
    var id = obj.attr('id');
   	$("i[name='folderCheck']").each(function(){
        if(id == 'allCheck'){ 
        	if(bool){
        		$(this).prev().addClass('active');
        		$(this).addClass('yes');
        	} else {
        		$(this).prev().removeClass('active');
        		$(this).removeClass('yes');
        	}
        }
     	if($(this).attr('class') != undefined && $(this).attr('class').indexOf('yes') != -1){
     	   folderArray.push($(this).prev().attr('key-value'));
     	}
    });
    $("i[name='fileCheck']").each(function(){
   	  if(id == 'allCheck'){ 
	   		if(bool){
	   			$(this).prev().addClass('active');
        		$(this).addClass('yes');
        	} else {
        		$(this).prev().removeClass('active');
        		$(this).removeClass('yes');
        	}
      }
	  if($(this).attr('class') != undefined && $(this).attr('class').indexOf('yes') != -1){
		 var objFile = {
		      id : $(this).prev().attr('key-value'),
			  name : $(this).parent().find('.txt_name a').html()
		 }
	     fileArray.push(objFile);
	  }
    });
    showOperBtn(folderArray, fileArray);
    //判断是否全选
    $("#fileCount").html(fileArray.length);
    $("#folderCount").html(folderArray.length);
    //看是否全部选中
    var bool = true;
    $(".tips_img").each(function(){
		var check_class =  $(this).next().attr('class');
		if(check_class == undefined || !(check_class.indexOf('yes') != -1)){
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
        title :file_version_management,
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
	var url = basepath + '/takecode/toinsert/'+id+'?timestr='+timestr;
	//获取数量及备注
	var remainDownloadNum = $("#remainDownloadNum").val();
	var remark = $("#remark").val();
	if(remainDownloadNum != undefined){
		url += "&remainDownloadNum="+remainDownloadNum;
	}
	if(remark != undefined){
		url += "&remark="+GB2312UnicodeConverter.ToUnicode(remark);
	}
	layer.closeAll();
    layer.open({
        type: 2,
        title :set_extraction_code,
        area: ['800px', '380px'],
        shadeClose: false, //点击遮罩关闭
        content: url
    });
}

//创建提取码(多个)
function createTakeCodeMore(fileIds){
	//获取失效时间
	var timestr = $("#effectivetime").val();
	var url = basepath + '/takecode/toinsertmore?fileIds='+fileIds+'&timestr='+timestr;
	//获取数量及备注
	var remainDownloadNum = $("#remainDownloadNum").val();
	var remark = $("#remark").val();
	if(remainDownloadNum != undefined){
		url += "&remainDownloadNum="+remainDownloadNum;
	}
	if(remark != undefined){
		url += "&remark="+GB2312UnicodeConverter.ToUnicode(remark);
	}
	layer.closeAll();
	layer.open({
		type: 2,
		title :set_extraction_code,
		area: ['800px', '380px'],
		shadeClose: false, //点击遮罩关闭
		content: url 
	});
}


//选中操作后选中的文件和文件夹数量
function getCheckSelected(folderId){
   var folderArray = new Array();
   var fileArray = new Array();
   $("i[name='folderCheck']").each(function(){
	     var checkClass = $(this).attr('class');
	   	 if(checkClass != undefined && checkClass.indexOf('yes') != -1){
	   		var id= $(this).prev().attr("key-value");
	   		var folderJson = {
	   			"id" : id
	   		}
	   		folderArray.push(folderJson);
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
    	 var checkClass = $(this).attr('class');
	   	 if(checkClass != undefined && checkClass.indexOf('yes') != -1){
	   		var id= $(this).prev().attr("key-value");
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
	     var checkClass = $(this).attr('class');
	   	 if(checkClass != undefined && checkClass.indexOf('yes') != -1){
	   		var folderName= $(this).parent().find("p a").html();
	   		nameArray.push(folderName);
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
    	 var checkClass = $(this).attr('class');
	   	 if(checkClass != undefined && checkClass.indexOf('yes') != -1){
	   		var fileName= $(this).parent().find(".txt_name a").html();
	   		nameArray.push(fileName);
	   	 }
    });
    return nameArray;
}

//创建文件夹
function createFolder(){
	if($("#addIndex").val() != 0){
		return;
	}
	var folderName = $(".addFolder").find("input[type='text']").val();
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
	        	layer.msg(create_success,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1500);
	        }
        }
	 });
	 $("#addIndex").val(1);
}


//重命名同名检测
function operRename(obj, openId, openType){
	var operName = $(obj).prev().val();
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
		var fileName = $(obj).parents('li').find(".txt_name a").html();
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
	        	$("#move").html(directory_exists_same_file_save_two_file);
	        	//存在弹窗
	        	layer.open({
	                type: 1,
	                title :rename,
	                area: ['600px', '183px'],
	                shadeClose: false, //点击遮罩关闭
	                content: $('.con_remove_qx'),
	                btn: [keep_two_folders, cancel],
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

//重命名操作
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
           layer.closeAll();
           $("#showMsg").html(wait_deleting_files);
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
	       	        layer.msg(delete_success,{icon: 1,time:1500});
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
        title :set_share_range,
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
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
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
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

//分享操作
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
function showOperBtn(folderArray, fileArray){
	hideAllBtn();
	if(folderArray.length > 0){
		if(folderArray.length == 1 && fileArray.length == 0){
			//至勾选文件夹文件夹
			//显示下载、复制、移动、移除、分享、重命名
			showAction($(".ac_down"));
			showAction($(".ac_del"));
			showAction($(".ac_rename"));
			showAction($(".ac_share"));
			showAction($(".ac_reauth"));
			showAction($(".openMenu"));
		} 
		//显示下载、复制、移动、移除
		showAction($(".ac_down"));
		showAction($(".ac_del"));
		showAction($(".ac_share"));
		showAction($(".ac_reauth"));
	} else {
		if(fileArray.length > 0 && fileArray.length != 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAction($(".ac_down"));
			showAction($(".ac_del"));
			showAction($(".ac_reauth"));
			showAction($(".ac_take"));
			showAction($(".ac_share"));
		} else if(fileArray.length == 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAllBtn();
			hideAction($(".decompressMenu"));
			hideAction($(".ac_edit"));
			//显示编辑按钮
			var index = fileArray[0].name.lastIndexOf(".");
			if(index > 0){
				var lastFileName = fileArray[0].name.substring(index);
				//判断是否在可以解压的文档格式内
				for(var i in decompressType){
					if(decompressType[i] == lastFileName){
						showAction($(".decompressMenu"));
						break;
					}
				}
				for(var i in editType){
					if(editType[i] == lastFileName){
						showAction($(".ac_edit"));
						return;
					}
				}
			}
		}
	}
}


//隐藏所有操作按钮
function hideAllBtn(){
	hideAction($(".ac_down"));
	hideAction($(".ac_del"));
	hideAction($(".ac_share"));
	hideAction($(".ac_rename"));
	hideAction($(".ac_sign"));
	hideAction($(".ac_take"));
	hideAction($(".ac_share"));
	hideAction($(".ac_reauth"));
	hideAction($(".ac_edit"));
	$(".right_menu").find("li").hide();
}

//显示所有BTN
function showAllBtn(){
	showAction($(".ac_down"));
	showAction($(".ac_del"));
	showAction($(".ac_share"));
	showAction($(".ac_rename"));
	showAction($(".ac_sign"));
	showAction($(".ac_take"));
	showAction($(".ac_share"));
	showAction($(".ac_reauth"));
	$(".right_menu").find("li").show();
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

//个人空间权限按钮,用户关系分配
function permissionHide(){
	var permissionStr = $("#permissionStr").val();
	var permissionArr = permissionStr.split(",");
	if($.inArray("1", permissionArr)==-1){
		$(".ac_folder").addClass("invalid");
		$(".ac_folder.rightmouse").remove();
	}else{
		$(".ac_folder").css('background','#5a98de'); 
		$(".ac_folder").css('border','1px solid #5a98de'); 
		$(".ac_folder").css('color','#fff'); 
		$(".ac_t01").css('background-position','right top'); 
	}
	
	if($.inArray("2", permissionArr)==-1){
		$(".ac_upload").addClass("invalid");
		$(".ac_upload.rightmouse").remove();
	}else{
		$(".ac_upload").css('background','#5a98de'); 
		$(".ac_upload").css('border','1px solid #5a98de'); 
		$(".ac_upload").css('color','#fff'); 
		$(".ac_t02").css('background-position','right -16px'); 
		$(".ac_down_box").css('background-position','right top'); 
	}
	
	if($.inArray("3", permissionArr)==-1){
		$(".ac_down").addClass("invalid");
		$(".ac_down.rightmouse").remove();
	}
	
	if($.inArray("4", permissionArr)==-1){
		$(".ac_move").addClass("invalid");
		$(".ac_move.rightmouse").remove();
	}
	
	if($.inArray("5", permissionArr)==-1){
		$(".ac_copy").addClass("invalid");
		$(".ac_copy.rightmouse").remove();
	}
	
	if($.inArray("6", permissionArr)==-1){
		$(".ac_del").addClass("invalid");
		$(".ac_del.rightmouse").remove();
	}
	
	if($.inArray("7", permissionArr)==-1){
		$(".ac_rename").addClass("invalid");
		$(".ac_rename.rightmouse").remove();
	}
	
	if($.inArray("8", permissionArr)==-1){
		$(".ac_take").addClass("invalid");
		$(".ac_take.rightmouse").remove();
	}
	
	if($.inArray("9", permissionArr)==-1){
		$(".ac_sign").addClass("invalid");
		$(".ac_sign.rightmouse").remove();
	}
	
	if($.inArray("10", permissionArr)==-1){
		$(".ac_share").addClass("invalid");
		$(".ac_share.rightmouse").remove();
	}
	
	if($.inArray("11", permissionArr)==-1){
		$(".ac_edit").addClass("invalid");
		$(".ac_edit.rightmouse").remove();
	}
}
