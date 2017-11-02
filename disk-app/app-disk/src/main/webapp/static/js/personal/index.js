







var isOper = true;


function searchFile(){
	var data_url = basepath + "/personal/index";
	var queryName = $("#queryName").val();
	data_url = data_url +"?queryName=" + encodeURI(encodeURI(queryName));
	window.location.href = data_url;
}

//禁止右键菜单功能
document.oncontextmenu = function(){
    return false;   
};

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
	$(".attr-title").css("display","none");
	$(".attr-detail").css("display","none");
	
	$(".array_h").click(function(){
		var data_url = basepath + "/personal/index?type=1";
		var queryName = $("#queryName").val();
		if(queryName.length > 0){
			data_url = data_url +"&queryName=" + encodeURI(encodeURI(queryName));
		} else {
			var folderId = $("#folderId").val();
			if(folderId != 0 && folderId.length > 0 ){
				data_url = data_url +"&folderId=" + folderId;
			}
		}
		window.location.href = data_url;
	});
	
	$(".array_v").click(function(){
		var data_url = basepath + "/personal/index";
		var queryName = $("#queryName").val();
		if(queryName.length > 0){
			data_url = data_url +"&queryName=" + encodeURI(encodeURI(queryName));
		} else {
			var folderId = $("#folderId").val();
			if(folderId != 0 && folderId.length > 0 ){
				data_url = data_url +"?folderId=" + folderId;
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
	        var obj =  $(this).find("i[name='fileCheck']");
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
           		//var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addFileSign(); //得到iframe页的body内容
    			layer.closeAll();
    			layer.msg(set_tag_success,{icon: 1,time:1500});
            	setTimeout(function(){
            		refresh();
            	}, 1500);
          	  }
        });
    });
    
    //创建文件夹
    $(".ac_folder").live('click',function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	if(!isOper){
    		return;
    	}
        var str=$("#new_tr > tbody").html();
        $("#new_tr > tbody").siblings(".file_name").show();
        $("#tabs").prepend(str);
        $("#mainContent").show();
        $("#noneContent").hide();
        //选中input
        $(".input_filename").eq(1).focus();
        $(".input_filename").eq(1).select();
        disOper();
    });
    
  //新建文件夹及重命名回车事件
    $('.input_filename').live('keydown',function(e){
    	if(e.keyCode==13){
    		$(this).next().trigger("click");
    	}
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
    	var obj = null;
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
			//var name = nameText.find('input').val();
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
	 if(!checkPermission(this)){
    		return;
    	}
	   if(!isOper){
    		return;
       }
	   //表示弹窗为移动操作
	   $("#operType").val(1);
	   openFolderWindow();
	 });
   
	 //复制
	 $('.ac_copy').on('click', function(){
		if(!checkPermission(this)){
    		return;
    	}
		if(!isOper){
	       return;
	    }
	    //表示弹窗为移动操作
	    $("#operType").val(2);
	    openFolderWindow();
	 });
	 
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
		 var data = getCheckSelected();
    	if(data.folders.length>0 || data.files.length==0){
    		layer.msg(please_check_the_file,{icon: 5,time:1500});
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
		 $(".tab_checked").each(function(){
			  var size = $(this).parent().next().find('.share').length;
			  if(size > 0){
				  bool = true;
				  return false;
			  }
		 });
		 if(bool && data.files.length + data.folders.length >= 2){
			 //layer.msg(differences_select_auth,{icon: 5,time:1500});
			 layer.confirm(differences_select_auth_remove, {icon: 3, title: i18n_global_prompt},function(index){
				 layer.closeAll();
				 shareMoreAuthRemove(data);
			 });
			 return;
		 }
		 
		 if(data.files.length + data.folders.length  >= 2){
			 shareMoreAuth(data);
		 } else {
			 var openType = 1;
			 //var openIds = new Array();
			 if(data.files.length > 0){
				 openType = 2;
				 openId = data.files[0].id;
			 } else {
				 openId = data.folders[0].id;
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
    	//右侧关联信息
    	selectCheckRightTable($(this));
     });
     
    
     $(".tabs tr td:nth-child(2)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
		 if(!isOper){
    		return;
    	 }
    	 selectCheckBox(obj);
    	 //右侧关联信息
    	 selectCheckRightTable(obj);
     });
     
     $(".tabs tr td:nth-child(3)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
    	 if(!isOper){
     		return;
     	 }
    	 selectCheckBox(obj);
    	 //右侧关联信息
    	 selectCheckRightTable(obj);
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
  	   	     	contentType:"application/json",
  	   	        async: false, 
  	   	        success: function(result){
  	   	            checkErrorCode(result);
  	   	        	if((data.folders.length + data.files.length > 1) || (data.folders != null && data.folders.length > 0)){
		  	   	   		$(".ts_box").hide();
		  	 		}
  	   		        if (result.code == 1000 && result.result == "SUCCESS") {
  	   		        	//window.location.href = result.codeInfo;
	  	   		       var frameSrc = result.codeInfo;  
	  			        $("#NoPermissioniframe").attr("src", frameSrc);  
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
 		var fileId = $(".tab_contain").find(".tab_checked").attr('value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg(please_select_a_file_edited_online,{icon: 5,time:1000});
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
 			var obj = $(".tab_contain").find(".tab_checked").parent().parent().find(".imageFile").attr("fileid");
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
 	 			var fileId = $(".tab_contain").find(".tab_checked").attr("value");
 	 	 		if(fileId == ""){
 	 	 			layer.msg(please_click_on_the_file_preview,{icon: 5,time:1000});
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
 	
 	
 	//解压
 	$(".ac_decpro").click(function(){
 		if(!checkPermission(this)){
    		return;
    	}
 		var fileId = $(".tab_contain").find(".tab_checked").attr('value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg(please_select_the_file_extracted_online,{icon: 5,time:1000});
 	   		return false;
 		}
 		//var fileName = $(".tab_contain").find(".tab_checked").parent().next().find(".input_filename").val();
 		$("#showMsg").html(wait_extracting_file);
  		$(".ts_box").show();
  		$("#taskSpan").show();
  		$.ajax({
  			  url: fileServiceUrl + "/fileManager/decompressZip?callback=jsonpCallback",
  			  data: "fileId="+fileId,
  	          dataType: "jsonp",
  	          success: function (data) {
  	          }
  	    });
 	});
 	
 	
 	//解压
 	$(".decompressMenu").click(function(){
 		var fileId = $(".tab_contain").find(".tab_checked").attr('value');
 		if(typeof(fileId) == "undefined" || fileId == ""){
 			layer.msg(please_select_the_file_extracted_online,{icon: 5,time:1000});
 	   		return false;
 		}
 		//var fileName = $(".tab_contain").find(".tab_checked").parent().next().find(".input_filename").val();
 		$("#showMsg").html(wait_extracting_file);
  		$(".ts_box").show();
  		$("#taskSpan").show();
  		$.ajax({
  			  url: fileServiceUrl + "/fileManager/decompressZip?callback=jsonpCallback",
  			  data: "fileId="+fileId,
  	          dataType: "jsonp",
  	          success: function (data) {
  	          }
  	    });
 	});
 	
	//取消解压
 	$("#cancelDecompress").click(function(){
  		var taskName = $("#taskSpan").find("input").val();
  		$.ajax({
  			  url: fileServiceUrl + "/fileManager/cancelDecompress?callback=jsonpBack",
  			  data: "taskName="+taskName,
  	          dataType: "jsonp",
  	          success: function (data) {
  	          }
  	    });
 	});
 	
 	$("#txtSearch").click(function(){
 		
 	});
});

//解压返回
function jsonpCallback(data){
	checkFileErrorCode(data);
	if(data.code == 1000 && data.result.progress == 2){
		$(".ts_box").hide();
		$("#taskSpan").hide();
		layer.msg(decompression_success,{icon: 1,time:1000});
		setTimeout(function(){
    		refresh();
    	}, 1500);
	} else if(data.code == 1000 && data.result.progress != 2){
		if(data.result.progress == 1){
			$("#showMsg").html(wait_extracting_file);
		}
		$("#taskSpan").find("input").val(data.result.taskName);
		setTimeout(function(){
			decResult(data.result.taskName, data.result.progress);
    	}, 1500);
	}
}

//取消
function jsonpBack(data){
	checkErrorCode(data);
	if(data.code == 1000 ){
		$(".ts_box").hide();
		$("#taskSpan").hide();
	} 
}

/****
 * 查询进度
 * @param taskName 进程名称
 * @param progress 进度
 */
function decResult(taskName, progress){
	$.ajax({
		  url: fileServiceUrl + "/fileManager/decompressInfo?callback=jsonpCallback",
		  data: "taskName="+taskName+"&progress="+progress,
          dataType: "jsonp",
          success: function (data) {
          }
    });
}


//上传的公共处理方法
function uploadFunc(dataUrl){
	var folderId = $.trim($("#parentId").val());
	if(folderId == ""){
		layer.msg(please_select_the_directory_upload,{icon: 5,time:1000});
   		return false;
	}
	var data_url = dataUrl + '&folderId=' + folderId;
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
		 };
	     fileArray.push(objFile);
	  }
    });
    //TODO 这里修改为全展示
    showOperBtn(folderArray, fileArray);
    //showOperMenu(folderArray, fileArray);
    //判断是否全选
    $("#fileCount").html(fileArray.length);
    $("#folderCount").html(folderArray.length);
    //看是否全部选中
    var bool = true;
    $(".tab_contain").find('.tab_check').each(function(){
		//var aa = $(this).attr('value');
		var check_class = $(this).attr('class');
		if(!(check_class.indexOf('tab_checked') != -1)){
			//var aa = $(this).attr('value');
			bool = false;
			return false;
  		}		
    });
    if(bool){
    	$("#allCheck").addClass('tab_checked');
    }
}

//右侧关联表单
function selectCheckRightTable(obj){
	if($(obj).attr("class") == undefined || $(obj).attr("id") == "allCheck"){
		return;
	}
	$(".attr-title").css("display","block");
	$(".attr-detail").css("display","block");
	//获取属性
	var openId = $(obj).find("input:first").val();
	var filename = $(obj).parent().parent().find(".file_name a").html();
	var createTime = $(obj).find(".createTime").val();
	var updateTime = $(obj).parent().parent().find("td:last").html();
	var openType = $(obj).parent().parent().attr("class");
	var fileTypeInt = 0;
	var fileVersion = $(obj).find(".fileVersion").val();
	var fileVersionNum = $(obj).find(".fileVersionNum").val();
	var signNames = "";
	var fileType = $(obj).find(".fileType").val();
	var fileTypeClass = "ico ico_folder ";
	if(openType == 'trfile'){
		$(".attr-title.sign").css("display","block");
		$(".attr-detail.sign").css("display","block");
		$(".attr-title.version").css("display","block");
		$(".attr-detail.version").css("display","block");
		fileTypeClass = fileTypeClass + "t" + fileType;
		fileTypeInt = 2;
		if(fileVersion == 0){
			$(".hideFlash").css("display","none");
			$(".hideFlash").attr("href","javascript:void(0);");
			fileVersion = "V1.0";
		}else{
			$(".hideFlash").css("display","block");
			$(".hideFlash").attr("href","javascript:showVersion("+openId+","+fileVersion+");");
			fileVersion = "V" + (fileVersion/10);
		};
	}else{
		$(".attr-title.sign").css("display","none");
		$(".attr-detail.sign").css("display","none");
		$(".attr-title.version").css("display","none");
		$(".attr-detail.version").css("display","none");
		fileTypeClass = fileTypeClass + "tfile";
		fileTypeInt = 1;
		fileVersion = i18n_nothing;
		fileVersionNum = 0;
	}
	$(obj).parent().parent().find("td:eq(2) i").each(function(){
		signNames += $(this).html() + "，";
	});
	if(signNames.length > 0){
		signNames = signNames.substring(0,signNames.length-1);
	}else{
		signNames = i18n_nothing;
	}
	//ajax获取分享属性
	var manager = "";
	var see = "";
	var read = "";
	
	//先隐藏分享栏
	$("#competence").hide();
	
	$.ajax({
		url: basepath+"/personal/auth?openId="+openId + "&openType="+fileTypeInt,
        dataType:"json",
        type: 'GET',
        async: true, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	var bindex = true;
	        	var shareObj = eval('(' + data.result + ')');
	        	//构造分享属性
	        	var shareManager = shareObj.manager;
	        	var shareSee = shareObj.see;
	        	var shareRead = shareObj.read;
	        	if(shareManager.all){
	        		bindex = false;
	        		$("#competence").find(".attr-title.manager").css("display","block");
	        		$("#competence").find(".attr-detail.manager").css("display","block");
	        		manager = i18n_everybody;
	        	}else{
	        		$("#competence").find(".attr-title.manager").css("display","none");
	        		$("#competence").find(".attr-detail.manager").css("display","none");
	        		if(JSON.stringify(shareManager.deptNames)!="[]"){
	        			bindex = false;
	        			manager += i18n_department+"："+shareManager.deptNames.join(",") + "<br>";
	        			$("#competence").find(".attr-title.manager").css("display","block");
		        		$("#competence").find(".attr-detail.manager").css("display","block");
	        		}
	        		if(JSON.stringify(shareManager.employeeNames)!="[]"){
	        			bindex = false;
	        			manager += i18n_employee+"："+shareManager.employeeNames.join(",") + "<br>";
	        			$("#competence").find(".attr-title.manager").css("display","block");
		        		$("#competence").find(".attr-detail.manager").css("display","block");
	        		}
	        		if(JSON.stringify(shareManager.workgroupNames)!="[]"){
	        			bindex = false;
	        			manager += i18n_workgroup+"："+shareManager.workgroupNames.join(",");
	        			$("#competence").find(".attr-title.manager").css("display","block");
		        		$("#competence").find(".attr-detail.manager").css("display","block");
	        		};
	        	}
	        	if(shareSee.all){
	        		bindex = false;
	        		$("#competence").find(".attr-title.see").css("display","block");
	        		$("#competence").find(".attr-detail.see").css("display","block");
	        		see = i18n_everybody;
	        	}else{
	        		$("#competence").find(".attr-title.see").css("display","none");
	        		$("#competence").find(".attr-detail.see").css("display","none");
	        		if(JSON.stringify(shareSee.deptNames)!="[]"){
	        			bindex = false;
	        			$("#competence").find(".attr-title.see").css("display","block");
		        		$("#competence").find(".attr-detail.see").css("display","block");
	        			see += i18n_department+"："+shareSee.deptNames.join(",") + "<br>";
	        		}
	        		if(JSON.stringify(shareSee.employeeNames)!="[]"){
	        			bindex = false;
	        			$("#competence").find(".attr-title.see").css("display","block");
		        		$("#competence").find(".attr-detail.see").css("display","block");
	        			see += i18n_employee+"："+shareSee.employeeNames.join(",") + "<br>";
	        		}
	        		if(JSON.stringify(shareSee.workgroupNames)!="[]"){
	        			bindex = false;
	        			see += i18n_workgroup+"："+shareSee.workgroupNames.join(",");
	        			$("#competence").find(".attr-title.see").css("display","block");
		        		$("#competence").find(".attr-detail.see").css("display","block");
	        		};
	        	}
	        	if(shareRead.all){
	        		bindex = false;
	        		$("#competence").find(".attr-title.read").css("display","block");
	        		$("#competence").find(".attr-detail.read").css("display","block");
	        		read = i18n_everybody;
	        	}else{
	        		$("#competence").find(".attr-title.read").css("display","none");
	        		$("#competence").find(".attr-detail.read").css("display","none");
	        		if(JSON.stringify(shareRead.deptNames)!="[]"){
	        			bindex = false;
	        			$("#competence").find(".attr-title.read").css("display","block");
		        		$("#competence").find(".attr-detail.read").css("display","block");
	        			read += i18n_department+"："+shareRead.deptNames.join(",") + "<br>";
	        		}
	        		if(JSON.stringify(shareRead.employeeNames)!="[]"){
	        			bindex = false;
	        			$("#competence").find(".attr-title.read").css("display","block");
		        		$("#competence").find(".attr-detail.read").css("display","block");
	        			read += i18n_employee+"："+shareRead.employeeNames.join(",") + "<br>";
	        		}
	        		if(JSON.stringify(shareRead.workgroupNames)!="[]"){
	        			bindex = false;
	        			read += i18n_workgroup+"："+shareRead.workgroupNames.join(",");
	        			$("#competence").find(".attr-title.read").css("display","block");
		        		$("#competence").find(".attr-detail.read").css("display","block");
	        		};
	        	}
	        	if(bindex){
	        		$("#share1").css("display","block");
	        	}else{
	        		$("#share1").css("display","none");
	        	};
	        };
	        $("#competence1").html(manager);
			$("#competence2").html(see);
			$("#competence3").html(read);
			
			//展示分享栏
			$("#competence").show();
        }
	 });
		
	
	//右侧改变
	$(".right-attr .title span").attr("class",fileTypeClass);
	$("#filename1").html(filename);
	$("#filename1").attr("title",filename);
	$("#fileaddtime1").html(createTime);
	$("#fileedittime1").html(updateTime);
	$("#fvcurrent").html(fileVersion);
	$("#fvnum").html(fileVersionNum);
	$("#Tags1").html(signNames);
	
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

function openFolderWindow(){
	var data = getCheckSelected();
	if(data.folders.length + data.files.length == 0){
		 layer.msg(please_check_the_file_or_folder,{icon: 5,time:1500});
		 return false;
	}
	var title = getTitle();
	var openHtml = getOperTypeHtml(data);
	$("#operTypeHtml").html(openHtml);
    layer.open({
        type: 2,
        title :title,
        area: ['800px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/personal/folderTree'
        ,btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确定】的回调
      	    //var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var jsonFolder = iframeWin.callSelectFolderBack(); //得到iframe页的body内容
			//计算选中记录条数
		    var data = getCheckSelected(jsonFolder.folderId);
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
	   		};
	   		folderArray.push(folderJson);
	   	 }
    });
    $("i[name='fileCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var id= $(this).find("input[type='checkbox']").val();
	   		var fileJson = {
	   			"id" : id
	   		};
	   		fileArray.push(fileJson);
	   	 }
    });
    var data = {
		"folders" : folderArray,
		"files" : fileArray,
		"id" : folderId
	};
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
	};
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
	$("#showMsg").html(wait_copying_files);
	$(".ts_box").show();
	 //在进行管理
	 $.ajax({
		url: basepath+"/personal/copyManageFolder",
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(initData),
        contentType:"application/json",
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
}

//移动文件
function moveFolderFile(initData){
	layer.closeAll();
	$("#showMsg").html(wait_moving_files);
	$(".ts_box").show();
	 //在进行管理
	 $.ajax({
		url: basepath+"/personal/moveManageFolder",
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(initData),
        contentType:"application/json",
        async: false, 
        success: function(data){
        	$(".ts_box").hide();
        	checkErrorCode(data);
	        if (data.code == 1000) { 
	        	layer.msg(mobile_success,{icon: 1,time:1500});
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
        contentType:"application/json",
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
	$("#copyFile").html(a_folder_that_is_being_copied+":");
	$("#exitsFile").html(existing_folder+":");
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
		$("#moreSameName").show();
		$("#sySame").html(syNumber - 1);
	} else {
		$("#moreSameName").hide();
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
        			};
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
       			};
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
	$("#copyFile").html(a_folder_that_is_being_copied+":");
	$("#exitsFile").html(existing_folder+":");
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
	if(syNumber > 1){
		$("#moreSameName").show();
		$("#sySame").html(syNumber - 1);
	} else {
		$("#moreSameName").hide();
	}
	//复制到
	//var operType = $("#operType").val();
	var title = getTitle();
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
        			};
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
       			};
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

//获取操作名称
function getOperTypeHtml(data){
	//var operType = $("#operType").val();
	var operTypeHtml = same_name_file_select_your_operation+":";
	if(data.files.length == 0){
		dataName = same_name_folder_select_your_operation+":";
	} 
	return operTypeHtml;
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
	};
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
              contentType:"application/json",
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
	if($("#requestTour").val()==1){
		dataUrl = dataUrl + "&tour=1";
	}
    layer.open({
        type: 2,
        title :set_share_range,
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		// body = layer.getChildFrame('body', index);
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
	if($("#requestTour").val()==1){
		dataUrl = dataUrl + "&tour=1";
	}
    layer.open({
        type: 2,
        title :set_share_range,
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		//var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var data = iframeWin.getSelectedData(); //得到iframe页的body内容
		/*	var folderData = decodeURI(data.data);
			data.folder = folderData;*/
			ajaxShareAuth(data);
      	}
    });
}

//分享界面，先移除选中文件的分享权限
function shareMoreAuthRemove(data){
	var dataUrl = basepath + '/personal/authPage?removeFlag=true';
	var dataString  = JSON.stringify(data);
	dataUrl = dataUrl + "&data=" + encodeURI(encodeURI(dataString));
	if($("#requestTour").val()==1){
		dataUrl = dataUrl + "&tour=1";
	}
    layer.open({
        type: 2,
        title :set_share_range,
        area: ['600px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: dataUrl,
        btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确认】的回调
       		//var body = layer.getChildFrame('body', index);
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
         contentType:"application/json",
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
			showAction($(".ac_rename"));
			showAction($(".openMenu"));
		} 
		//显示下载、复制、移动、移除
		showAction($(".ac_down"));
		showAction($(".ac_copy"));
		showAction($(".ac_move"));
		showAction($(".ac_del"));
		showAction($(".ac_share"));
	} else {
		if(fileArray.length > 0 && fileArray.length != 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAction($(".ac_down"));
			showAction($(".ac_copy"));
			showAction($(".ac_move"));
			showAction($(".ac_del"));
			showAction($(".ac_share"));
			showAction($(".ac_take"));
		} else if(fileArray.length == 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAllBtn();
			hideAction($(".decompressMenu"));
			hideAction($(".ac_decpro"));
			hideAction($(".ac_edit"));
			//显示编辑按钮
			var index = fileArray[0].name.lastIndexOf(".");
			if(index > 0){
				var lastFileName = fileArray[0].name.substring(index);
				//判断是否在可以解压的文档格式内
				for(var i in decompressType){
					if(decompressType[i] == lastFileName){
						showAction($(".decompressMenu"));
						showAction($(".ac_decpro"));
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
	hideAction($(".ac_decpro"));
	hideAction($(".ac_copy"));
	hideAction($(".ac_move"));
	hideAction($(".ac_del"));
	hideAction($(".ac_share"));
	hideAction($(".ac_rename"));
	hideAction($(".ac_sign"));
	hideAction($(".ac_take"));
	hideAction($(".ac_share"));
	hideAction($(".ac_edit"));
	$(".right_menu").find("li").hide();
}

//显示所有BTN
function showAllBtn(){
	showAction($(".ac_down"));
	showAction($(".ac_decpro"));
	showAction($(".ac_copy"));
	showAction($(".ac_move"));
	showAction($(".ac_del"));
	showAction($(".ac_share"));
	showAction($(".ac_rename"));
	showAction($(".ac_sign"));
	showAction($(".ac_take"));
	showAction($(".ac_share"));
	showAction($(".ac_edit"));
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

/*$(function() {
bootstrap-tour部分js代码
//Instance the tour
	var tour = new Tour({
		  steps: [
		  {
		    element: ".logo",
		    title: "操作指引",
		    content: "这里是操作指引，如需了解更多，请点击下一步；点击结束后将不再出现此内容"
		  },
		  {
		    element: "#queryName",
		    title: "操作指引",
		    content: "Content of my step"
		  }
		]});

// Initialize the tour
tour.init();

// Start the tour
tour.start();
});*/