var isOper = true;

function searchFile(){
	var data_url = basepath + "/teamworkfile/index";
	var queryName = $("#queryName").val();
	data_url = data_url +"?queryName=" + encodeURI(encodeURI(queryName)) + "&teamworkId=" + $("#teamworkId").val();
	window.location.href = data_url;
}

//禁止右键菜单功能
document.oncontextmenu = function(){
    return false;   
};

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
	//打开websocket链接
	//connection();
	
	//右侧菜单
	rightSelect();
	scrollDown('chat_wrapper');
	
	showAction($(".ac_folder"));
	showAction($(".ac_upload"));
	
	$(".attr-title").css("display","none");
	$(".attr-detail").css("display","none");
	
	$("#addIndex").val(0);
	
	//点击小图片，显示表情
	$("#bq").click(function(e){
		var topTmp = $(this).offset().top + $(this).outerHeight() - 142 - 25;
		var leftTmp =$(this).offset().left - 16;
		$(".face").css("top", topTmp);
		$(".face").css("left", leftTmp);
		$(".face").toggle();//展开收缩
		e.stopPropagation();//阻止冒泡事件
	});
	
	//在桌面任意地方点击，他是关闭
	$(document).click(function(e){
		var target = $(e.target);
		if($(target).attr("id") != 'content'){
			$(".face").css("display", "none");
		}
	});
	
	//点击小图标时，添加功能
	$(".face ul li").click(function(){
		$("#content").focus();
		var simg=$(this).find("img").parent().html();
		var sel, range;
		if (window.getSelection) { // IE9 或 非IE浏览器  
			sel = window.getSelection();
			range = sel.getRangeAt(0);
			range.deleteContents();
			var el = document.createElement('div');
            el.innerHTML = simg;
            var frag = document.createDocumentFragment(), node, lastNode;
            lastNode = null;
            while ((node = el.firstChild)) {
                lastNode = frag.appendChild(node);
            }
            //var firstNode = frag.firstChild;
            range.insertNode(frag);
            if (lastNode) {
                range = range.cloneRange();
                range.setStartAfter(lastNode);
                range.collapse(true);
                sel.removeAllRanges();
                sel.addRange(range);
            }
		}else if (document.selection && document.selection.type != "Control") {// IE < 9  
			var textLenth = 0, imageNum = 0;
			range = document.selection.createRange();
			textLenth = $("#content").text().length;
			imageNum = $("#content").find("img").size();
			range.moveStart("character", textLenth + imageNum);
			range.pasteHTML(simg);
	    } 
	});
	
	//输入框回车提交
	$('#content').keydown(function(e){
		if(e.shiftKey && e.which == 13 || e.which == 10){
			//shift + enter 换行
			return true;
		} else if (e.ctrlKey && e.which == 13) { 
			//ctrl + enter
		} else if (e.ctrlKey && e.which == 65 ) { 
			//alt + a 全选
		} else if (e.which == 8 ) { 
			//backspace 退格
		}else if(e.keyCode==13){
			//完整的内容
			var firstHtml = $(this).html();
			//过滤
			var reg1 = /&nbsp;/ig;//清除&nbsp;
			var reg2 = /(^\s*)|(\s*$)/g;//清除两边空格;
			var reg3 = /\s+/g;//合并中间多个空格为一个;
			var reg4 = /(^(<br>)*)|((<br>)*$)/g;//清除两边<br>;
			var reg5 = /(<br>)+/g;//合并中间多个<br>为一个;
			firstHtml = firstHtml.replace(reg1, '').replace(reg2, '').replace(reg3, ' ').replace(reg4, '').replace(reg5, '<br>');
			if(firstHtml.length == 0){
				return false;
			}
			//正则后内容
			var regHtml = firstHtml;
			//匹配图片（g表示匹配所有结果i表示区分大小写）
			var imgReg = /<img.*?(?:>|\/>)/gi;
			//匹配src属性
			//var titleReg = /title=[\'\"]?([^\'\"]*)[\'\"]?/i;
			//所有已成功匹配图片的数组
			var arr = regHtml.match(imgReg);
			if(arr != null && arr.length > 0){
				for (var i = 0; i < arr.length; i++) {
					var currentTitle = $(arr[i]).attr("title");
					if(currentTitle != ''){
						regHtml = regHtml.replace(arr[i], currentTitle);
					}
					/* var title = arr[i].match(titleReg);
					//获取图片地址
					if(title[1]){
						regHtml = regHtml.replace(arr[i], title[1]);
					} */
				}
			}
			//过滤剩余html标签(不用， div中<>会被转义)
			/* var reg6 = /<[^<>]+>/g;//去除HTML tag
			firstHtml = firstHtml.replace(reg6, '');
			regHtml = regHtml.replace(reg6, '');
			console.log(firstHtml); */
			//enter 发布
			if(!isConnectWs){
				var contentHtml = "<div class=\"chat_item c_f_after chat_item_text has_content\">";
				contentHtml += "<div><div class=\"avatar_wrapper\">";
				contentHtml += "<img src=\""+ basepath +"/user/portrait\" class=\"avatar avatar-24 ng-isolate-scope\" onerror=\"this.src='"+ basepath +"/static/images/info_photo.jpg'\"></div>";
				contentHtml += "<div class=\"chat_main\" style=\"padding-right: 10px;\">";
				contentHtml += "<div class=\"chat_member\">";
				contentHtml += "<span class=\"ng-binding\">"+ $("#employeeName").val() +"</span>";
				contentHtml += "<span class=\"ng-binding ng-binding-time\">"+ (new Date()).format("yyyy-MM-dd hh:mm:ss") +"</span></div>";
				contentHtml += "<div class=\"chat_text\">";
				contentHtml += "<div><div class=\"ng-scope\">";
				contentHtml += "<div><div class=\"chat_text_inner\">";
				contentHtml += "<span class=\"chat_text_span\">";
				contentHtml += "<span class=\"text\">";
				contentHtml += "<span class=\"ng-scope analysis_text\">"+ firstHtml +"</span></span></span>";
				contentHtml += "</div></div></div></div></div></div></div>";
				$("#chat_wrapper").append(contentHtml);
				scrollDown('chat_wrapper');
			}
			//写入数据库
			ajaxRemark(regHtml);
			return false;
		}  else {
			//完整的内容
			var firstHtml = $(this).html();
			//过滤
			var reg1 = /&nbsp;/ig;//清除&nbsp;
			var reg2 = /(^\s*)|(\s*$)/g;//清除两边空格;
			var reg3 = /\s+/g;//合并中间多个空格为一个;
			var reg4 = /(^(<br>)*)|((<br>)*$)/g;//清除两边<br>;
			var reg5 = /(<br>)+/g;//合并中间多个<br>为一个;
			firstHtml = firstHtml.replace(reg1, '').replace(reg2, '').replace(reg3, ' ').replace(reg4, '').replace(reg5, '<br>');
			if(firstHtml.length != 0){
				//正则后内容
				var regHtml = firstHtml;
				//匹配图片（g表示匹配所有结果i表示区分大小写）
				var imgReg = /<img.*?(?:>|\/>)/gi;
				//所有已成功匹配图片的数组
				var arr = regHtml.match(imgReg);
				if(arr != null && arr.length > 0){
					for (var i = 0; i < arr.length; i++) {
						regHtml = regHtml.replace(arr[i], "  ");//一个表情占两个字节
					}
				}
				if(regHtml.length >= 200){
					return false;
				}
			}
		}
	});
	
	//更多历史消息浏览
	$(".addmessage").live('click', function(){
		var $obj = $(this);
		$obj.html("<div class=\"ts_box\" style=\"background: #ffffff;position: static;\"><i class=\"teamwork_tips_loading\"></i></div>");
		var objTr = $(this).parent().parent().next();
		setTimeout(function(){
			rightSelect();
			var scrollheight = $(objTr).position().top;
			$obj.parent().parent().remove();
			scrollHeight("chat_wrapper",scrollheight - 33);//减去上一步删除的元素的高度
  		}, 1500);
	});
	
	//框架内图像加载后再次执行，下拉框拖至最后
	$("#chat_wrapper img").load(function(){
		scrollDown('chat_wrapper');
	});
	
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
    	if(data.teamworkFiles.length + data.teamworkFolders.length!=1){
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
	 
	 //导出
	 $('.ac_export').on('click', function(){
		 if(!checkPermission(this)){
    		return;
    	 }
		 if(!isOper){
    		return;
         }
		 openExportFolderWindow();
	 });
	
	 //预览
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
                content: fileServiceUrl + "/fileManager/onlinePreviewTxtTeamwork?fileId="+ fileId
                ,btn: [close]
            });
 		 } else {
			//word 等相关文档
			$.ajax({
				url: basepath+"/teamworkfile/preview",
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
	 
    //取消重命名
    $(".cancel").live("click",function(){
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
    	 if(!checkPermission(this)){
     		return;
     	}
    	var data = getCheckSelected();
		if(data.teamworkFolders.length + data.teamworkFiles.length == 0){
			layer.msg(please_select_a_file_or_directory_download,{icon: 5,time:1000});
			return false;
		}
		if((data.teamworkFolders.length + data.teamworkFiles.length > 1) || (data.teamworkFolders != null && data.teamworkFolders.length > 0)){
			$("#showMsg").html(wait_packing_files);
	  		$(".ts_box").show();
		}
  		setTimeout(function(){
  			$.ajax({
  	   			url: basepath+"/teamworkfile/download",
  	   	        dataType:"json",
  	   	        type: 'POST',
  	   	     	data: JSON.stringify(data),
  	   	     	contentType:"application/json",
  	   	        async: false, 
  	   	        success: function(result){
  	   	        	checkErrorCode(result);
	   	        	if((data.teamworkFolders.length + data.teamworkFiles.length > 1) || (data.teamworkFolders != null && data.teamworkFolders.length > 0)){
		  	   	   		$(".ts_box").hide();
		  	 		}
	   		        if (result.code == 1000) {
	   		        	sendWebsocket(result.result);//websocket发布
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
   			url: basepath+"/teamworkfile/read",
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
 		var data_url = fileServiceUrl + '/authorize/toTeamworkUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
 	});
 	
    /**
     * 上传文件夹
     */
	$('#uploadFolder').on('click', function(){
		if(!checkPermission(this)){
    		return;
    	}
		var data_url = fileServiceUrl + '/authorize/toTeamworkFolderUploadPage?spaceType=' + spaceType;
        uploadFunc(data_url);
	});
	
 	/*********************************************************************右键菜单*****************************************************************************/
 	//打开
 	$(".openMenu").live('click',function(){
 		$(".right_menu").hide();
 		var data = getCheckSelected();
 		if(data.teamworkFolders.length + data.teamworkFiles.length != 1){
 			layer.msg(unable_open_multiple_files_the_sametime,{icon: 5,time:1000});
 	   		return false;
 		}
 		//判断是文件夹还是文件
 		if(data.teamworkFolders.length == 1){
 			//文件夹直接跳转
 			window.location.href = basepath + "/teamworkfile/index?folderId="+ data.teamworkFolders[0].id + "&teamworkId=" + $("#teamworkId").val();
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
 	 		   		url: basepath+"/teamworkfile/read",
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
 	 				url: basepath+"/teamworkfile/preview",
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
 	 				                title :document_preview+'-'+fileName,
 	 				                area: ['800px', '600px'],
 	 				                shadeClose: false, //点击遮罩关闭
 	 				                maxmin:true,
 	 				                content: fileServiceUrl + "/fileManager/onlinePreviewTxtTeamwork?fileId="+ fileId
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
	var teamworkId = $("#teamworkId").val();
	var data_url = dataUrl + '&folderId=' + folderId + '&teamworkId=' + teamworkId + '&domain=' + window.location.host + "&webname=" + basepath;
    layer.open({
         type: 2,
         title :upload_files,
         area: ['800px', '470px'],
         shadeClose: false, //点击遮罩关闭
         maxmin: false,
         move:false,
         closeBtn: 0,
         title:false,
         content: data_url
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
			//aa = $(this).attr('value');
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
        content: basepath + '/teamworkfile/version?fileId='+id+"&fileVersion="+version
        ,btn: [close]
    	/*,
        yes: function(index, layero){
      	    //按钮【新建文件夹】的回调
       	var body = layer.getChildFrame('body', index);
        }*/
    });
}

//选中操作后选中的文件和文件夹数量
function getCheckSelected(folderId){
   var folderArray = new Array();
   var fileArray = new Array();
   var teamworkId = $("#teamworkId").val();
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
		"teamworkFolders" : folderArray,
		"teamworkFiles" : fileArray,
		"id" : folderId,
		"teamworkId" : teamworkId
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
	var teamworkId = $("#teamworkId").val();
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
		"folderName" : folderName,
		"teamworkId" : teamworkId
	};
	$.ajax({
		url: basepath+"/teamworkfile/createTeamworkFolder",
        dataType:"json",
        type: 'POST',
        data:data,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	sendWebsocket(data.result);//websocket发布
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

/**
 * 移动窗口
 */
function openFolderWindow(){
	var data = getCheckSelected();
	if(data.teamworkFolders.length + data.teamworkFiles.length == 0){
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
        content: basepath + '/teamworkfile/folderTree?teamworkId=' + $("#teamworkId").val()
        ,btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确定】的回调
      	   // var body = layer.getChildFrame('body', index);
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

/**
 * 移动时，查询同名
 * @param initData
 */
function operSameName(initData){
	$.ajax({
		url: basepath+"/teamworkfile/getSameName",
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
        }
	}); 
}

//最终操作方法
function operFolderFile(initData){
	var operType = $("#operType").val();
	if(operType == 1){
		moveFolderFile(initData);
	}else if(operType == 2) {
		//复制
		//copyFolderFile(initData);
	}
}

//移动文件
function moveFolderFile(initData){
	layer.closeAll();
	$("#showMsg").html(wait_moving_files);
	$(".ts_box").show();
	 //在进行管理
	 $.ajax({
		url: basepath+"/teamworkfile/moveManageFolder",
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

//操作同名文件夹
function operSameFolder(initData, data){
	  var lastJsonFolders = new Array();
	  //先添加不重名的文件夹
	  for(var i = 0 ; i < initData.teamworkFolders.length; i++){
		  var operFolder = initData.teamworkFolders[i];
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
            		initData.teamworkFolders = lastJsonFolders;
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
        		initData.teamworkFolders = lastJsonFolders;
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
            		initData.teamworkFolders = lastJsonFolders;
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
	  for(var i = 0 ; i < initData.teamworkFiles.length; i++){
		  var operFile = initData.teamworkFiles[i];
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
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index == -1){
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFiles.length > indexWin){
            		openSameFileWindow(indexWin, initData, data, lastJsonFiles);
            	} else {
            		//调用 文件操作接口
            		initData.teamworkFiles = lastJsonFiles;
            		operFolderFile(initData);
            	}
        	}
        },btn2:function(){
        	//保持两个文件夹
        	layer.closeAll();
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
        		initData.teamworkFiles = lastJsonFiles;
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
            		initData.teamworkFiles = lastJsonFiles;
            		operFolderFile(initData);
            	}
        	}
        }
    });
}

/**
 * 导出窗口
 */
function openExportFolderWindow(){
	var data = getCheckSelected();
	if(data.teamworkFolders.length + data.teamworkFiles.length == 0){
		 layer.msg(please_check_the_file_or_folder,{icon: 5,time:1500});
		 return false;
	}
	var title = i18n_teamwork_export_to_personal;
	var openHtml = getOperTypeHtml(data);
	$("#operTypeHtml").html(openHtml);
    layer.open({
        type: 2,
        title :title,
        area: ['800px', '450px'],
        shadeClose: false, //点击遮罩关闭
        content: basepath + '/teamworkfile/exportFolderTree'
        ,btn: [determine, close],
        yes: function(index, layero){
      	    //按钮【确定】的回调
      	    //var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var jsonFolder = iframeWin.callSelectFolderBack(); //得到iframe页的body内容
			//计算选中记录条数
		    var data = getCheckSelected(jsonFolder.folderId);
			layer.closeAll();
			operExportSameName(data);
      	  }
    });
}

/**
 * 导出时，查询同名
 * @param initData
 */
function operExportSameName(initData){
	$.ajax({
		url: basepath+"/teamworkfile/getExportSameName",
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
	            	operExportSameFolder(initData, data);
	            } else if (data.result.oldFiles.length > 0){
	            	//操作文件
	            	operExportSameFile(initData, data);
	            } else {
	            	//直接调用操作
	            	exportFolderFile(initData);
	            }
	        }
        }
	}); 
}

//导出文件
function exportFolderFile(initData){
	layer.closeAll();
	$("#showMsg").html(wait_moving_files);
	$(".ts_box").show();
	 //在进行管理
	 $.ajax({
		url: basepath+"/teamworkfile/exportManageFolder",
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(initData),
        contentType:"application/json",
        async: false, 
        success: function(data){
        	$(".ts_box").hide();
        	checkErrorCode(data);
	        if (data.code == 1000) { 
	        	sendWebsocket(data.result);//websocket发布
	        	layer.msg(export_success,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		refresh();
	        	}, 1500);
	        }
     }}); 
}

//操作同名文件
function operExportSameFile(initData, data){
	  var lastJsonFiles = new Array();
	  //先添加不重名的文件
	  for(var i = 0 ; i < initData.teamworkFiles.length; i++){
		  var operFile = initData.teamworkFiles[i];
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
		  openExportSameFileWindow(indexWin, initData, data, lastJsonFiles);
	  } else {
		  //直接执行操作
		  exportFolderFile(initData);
	  }
}

//开启文件同名窗口
//@param indexWin 开启第几个窗口  @data 所有同名文件对象  @lastJsonFiles 最终操作的集合
function openExportSameFileWindow(indexWin, initData, data, lastJsonFiles){
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
        	var index = $("#sameNameCheck").attr('class').indexOf('tab_checked');
        	if(index == -1){
        		//获取页面上的选择
        		indexWin = indexWin + 1;
            	if(sameFiles.length > indexWin){
            		openExportSameFileWindow(indexWin, initData, data, lastJsonFiles);
            	} else {
            		//调用 文件操作接口
            		initData.teamworkFiles = lastJsonFiles;
            		exportFolderFile(initData);
            	}
        	}
        },btn2:function(){
        	//保持两个文件夹
        	layer.closeAll();
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
        		initData.teamworkFiles = lastJsonFiles;
        		exportFolderFile(initData);
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
            		openExportSameFileWindow(indexWin, initData, data, lastJsonFiles);
            	} else {
            		//调用 文件操作接口
            		initData.teamworkFiles = lastJsonFiles;
            		exportFolderFile(initData);
            	}
        	}
        }
    });
}

//操作同名文件夹
function operExportSameFolder(initData, data){
	  var lastJsonFolders = new Array();
	  //先添加不重名的文件夹
	  for(var i = 0 ; i < initData.teamworkFolders.length; i++){
		  var operFolder = initData.teamworkFolders[i];
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
		  openExportSameFolderWindow(indexWin, initData, data, lastJsonFolders);
	  }
}

//开启文件夹同名窗口
//@param indexWin 开启第几个窗口 @initData操作的数据DATA  @data 所有同名文件夹对象  @lastJsonFolders 最终操作的集合
function openExportSameFolderWindow(indexWin, initData, data, lastJsonFolders){
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
            		openExportSameFolderWindow(indexWin, initData, data, lastJsonFolders);
            	} else {
            		//调用文件同名弹窗  代表同名文件夹结束
            		initData.teamworkFolders = lastJsonFolders;
            		operExportSameFile(initData, data);
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
        		initData.teamworkFolders = lastJsonFolders;
        		operExportSameFile(initData, data);
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
            		openExportSameFolderWindow(indexWin, initData, data, lastJsonFolders);
            	} else {
            		//调用文件同名弹窗 代表同名文件夹结束
            		initData.teamworkFolders = lastJsonFolders;
            		operExportSameFile(initData, data);
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
	if(data.teamworkFiles.length == 0){
		dataName = same_name_folder_select_your_operation+":";
	} 
	return operTypeHtml;
}


//重命名操作（同名判断）
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
		url: basepath+"/teamworkfile/checkSameName",
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

//重命名
function reNameFunc(data){
	$.ajax({
		url: basepath+"/teamworkfile/rename",
        dataType:"json",
        type: 'POST',
        data: data,
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	sendWebsocket(data.result);//websocket发布
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
	if(data.teamworkFolders.length + data.teamworkFiles.length == 0){
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
       		  url: basepath+"/teamworkfile/delManageFolder",
              dataType:"json",
              type: 'POST',
              data: JSON.stringify(data),
              contentType:"application/json",
              async: false, 
              success: function(data){
            	$(".ts_box").hide();
            	checkErrorCode(data);
       	        if (data.code == 1000) {
       	        	sendWebsocket(data.result);//websocket发布
	       	        layer.msg(delete_success,{icon: 1,time:1500});
		        	setTimeout(function(){
		        		refresh();
		        	}, 1500);
       	        }
           }});  
      	}
    });
}

var operationDate = null;

//右侧菜单
function rightSelect(){
	var data = {
		"teamworkId" : $("#teamworkId").val(),
		"operationDate" : operationDate
	};
	$.ajax({
		url: basepath + "/teamworkfile/rightMenu",
        dataType:"json",
        data: JSON.stringify(data),
        type: 'POST',
        async: false, 
        contentType:"application/json",
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	//成员对象集
	        	var employeeAddArray = data.result.employeeAddObjs;
	        	if(employeeAddArray!=null && employeeAddArray.length > 0){
	        		$("#teamworkMember").text(i18n_teamworkfile_member + "：" + employeeAddArray.length + i18n_teamworkfile_people);
	        	}else{
	        		$("#teamworkMember").text(i18n_teamworkfile_member + "：" + 0 + i18n_teamworkfile_people);
	        	}
	        	//记录信息集
	        	var map = data.result.map;
	        	var mapKey = data.result.mapKey;
	        	var portraitPath = data.result.portraitPath;
	        	//协作信息发送
	        	teamworkMessageSend(map, mapKey, portraitPath, 1);
	        	//查看更多消息
	        	var teamworkMessageQuery =  data.result.teamworkMessageQuery;
	        	if(teamworkMessageQuery != null){
	        		if(teamworkMessageQuery.recordCount > teamworkMessageQuery.pageSize){
	        			var addmessageText = "";
	        			addmessageText = "<div class=\"record-list\"><dl class=\"record-list-info\" style=\"color:gray;text-align: center;border: none;\"><span class=\"addmessage\" style=\"cursor: pointer;\">" + i18n_teamworkfile_check_more_details + "</span></dl></div>";
        				$(".chat_wrapper").prepend(addmessageText);
	        		}
	        	}
	        	operationDate = data.result.teamworkMessageQuery.operationDate;
	        }
        }
	});
}

//协作信息发送
function teamworkMessageSend(map, mapKey, portraitPath, type){
	portraitPath = portraitPath.replace(/\\/g, "/");
	for(var key in mapKey) {
		var msgType = map[mapKey[key]][0].msgType;
		var msgHtml = null;
		var chatWrapperHtml = "";
		if(msgType == 1){
			msgHtml = i18n_teamworkfile_upload + map[mapKey[key]].length + i18n_teamworkfile_items;
		} else if (msgType == 2){
			msgHtml = i18n_teamworkfile_delete + map[mapKey[key]].length + i18n_teamworkfile_items;
		} else if (msgType == 3){
			msgHtml = i18n_teamworkfile_download + map[mapKey[key]].length + i18n_teamworkfile_items;
    	} else if (msgType == 4){
    		msgHtml = i18n_teamworkfile_create + map[mapKey[key]].length + i18n_teamworkfile_items;
        } else if (msgType == 5){
        	msgHtml = i18n_teamworkfile_export + map[mapKey[key]].length + i18n_teamworkfile_items;
        } else if (msgType == 6){
        	msgHtml = i18n_teamworkfile_remark;
        } else if (msgType == 7){
        	msgHtml = i18n_teamworkfile_rename + map[mapKey[key]].length + i18n_teamworkfile_items;
        } else if (msgType == 8){
        	msgHtml = map[mapKey[key]][0].createAdmin + "<br>" + i18n_teamworkfile_create_this_teamwork;
        } else if (msgType == 9){
        	msgHtml = map[mapKey[key]][0].createAdmin + "<br>" + i18n_teamworkfile_invited + map[mapKey[key]].length + i18n_teamworkfile_new_members;
        } else if (msgType == 10){
        	msgHtml = map[mapKey[key]][0].createAdmin + "<br>" + i18n_teamworkfile_quit_this_teamwork;
        } else if (msgType == 11){
        	msgHtml = map[mapKey[key]][0].createAdmin + "<br>" + i18n_teamworkfile_edit_teamwork_information;
        } else if (msgType == 12){
        	msgHtml = map[mapKey[key]][0].createAdmin + "<br>" + i18n_teamworkfile_kick_out + map[mapKey[key]].length + i18n_teamworkfile_members;
        }
		var createTime = null;
		if(type == 2){
			//websocket传值时利用json强转，将createTime对象化，需把createTime对象的time时间戳提取出再格式化
			createTime = (new Date((map[mapKey[key]][0].createTime.time)*1)).format("yyyy-MM-dd hh:mm:ss");
		}else if(type == 1){
			createTime = (new Date(map[mapKey[key]][0].createTime)).format("yyyy-MM-dd hh:mm:ss");
		}
		if(msgType == 6){
			chatWrapperHtml += "<div class=\"chat_item c_f_after chat_item_text has_content\">";
			chatWrapperHtml += "<div><div class=\"avatar_wrapper\">";
			chatWrapperHtml += "<img src=\""+ basepath + "/load/portrait?Path=" + portraitPath + map[mapKey[key]][0].employeePortrait + "\" class=\"avatar avatar-24 ng-isolate-scope\" onerror=\"this.src='"+ basepath +"/static/images/info_photo.jpg'\"></div>";
			chatWrapperHtml += "<div class=\"chat_main\" style=\"padding-right: 10px;\">";
			chatWrapperHtml += "<div class=\"chat_member\">";
			chatWrapperHtml += "<span class=\"ng-binding\">"+ map[mapKey[key]][0].employeeName +"</span>";
			chatWrapperHtml += "<span class=\"ng-binding ng-binding-time\">"+ createTime +"</span></div>";
			chatWrapperHtml += "<div class=\"chat_text\">";
			chatWrapperHtml += "<div><div class=\"ng-scope\">";
			chatWrapperHtml += "<div><div class=\"chat_text_inner\">";
			chatWrapperHtml += "<span class=\"chat_text_span\">";
			chatWrapperHtml += "<span class=\"text\">";
			chatWrapperHtml += "<span class=\"ng-scope analysis_text\">"+ map[mapKey[key]][0].content +"</span></span></span>";
			chatWrapperHtml += "</div></div></div></div></div></div></div>";
		} else {
			if(msgType < 8){
        		chatWrapperHtml += "<div class=\"record-list\">";
        		chatWrapperHtml += "<dl class=\"record-list-info\">";
        		chatWrapperHtml += "<dt class=\"record-info-desc\">";
        		chatWrapperHtml += "<span class=\"record-user\">"+ map[mapKey[key]][0].employeeName +"</span>";
        		chatWrapperHtml += "<span class=\"record-desc\">"+ msgHtml +"</span>";
        		chatWrapperHtml += "<span class=\"record-time\">"+ createTime +"</span></dt>";
    			for(var i = 0;i < map[mapKey[key]].length; i++) {
    				chatWrapperHtml += "<dd class=\"record-info-doc\">";
	        		chatWrapperHtml += "<i class=\"fileicon-small-pic\"></i>";
	        		var fileTypeTmp = map[mapKey[key]][i].fileType;
	        		if((map[mapKey[key]][i].fileId == null || map[mapKey[key]][i].fileId == "") && map[mapKey[key]][i].folderId != null && map[mapKey[key]][i].folderId != ""){
	        			fileTypeTmp = "files";
	        		}
	        		chatWrapperHtml += "<span node-type=\"record-doc\" class=\"record-doc\">" + "<i class=\"tipbg  tips_bg_" + fileTypeTmp + "\"></i>" + map[mapKey[key]][i].content + "</span></dd>";
    			}
    		} else{
    			chatWrapperHtml += "<div class=\"record-list\">";
        		chatWrapperHtml += "<dl class=\"record-list-info\">";
        		chatWrapperHtml += "<dt class=\"record-info-desc\">";
        		chatWrapperHtml += "<span class=\"record-desc\">"+ msgHtml +"</span>";
        		chatWrapperHtml += "<span class=\"record-time\">"+ createTime +"</span></dt>";
    			if(msgType == 9 || msgType == 12){
        			for(var i = 0;i < map[mapKey[key]].length; i++) {
        				chatWrapperHtml += "<dd class=\"record-info-doc\">";
    	        		chatWrapperHtml += "<span node-type=\"record-doc\" class=\"record-doc\">"
    	        			+ "<img src=\""+ basepath + "/load/portrait?Path=" + portraitPath + map[mapKey[key]][i].employeePortrait + "\" class=\"avatar avatar-24 ng-isolate-scope\" onerror=\"this.src='"+ basepath +"/static/images/info_photo.jpg'\">"
    	        			+ "&nbsp;" + map[mapKey[key]][i].employeeName
    	        			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + map[mapKey[key]][i].deptName + "</span></dd>";
        			}
    			}
    		}
		}
		if(type == 1){
			$(".chat_wrapper").prepend(chatWrapperHtml);
		}else if(type == 2){
			$(".chat_wrapper").append(chatWrapperHtml);
		}
	}
	changeImage();
}

//替换文本为表情
function changeImage(){
	$(".analysis_text").each(function(){
		var content = $(this).html();
		var conReg = /\[.*?(?:\]|\/>)/gi;
		var arr = content.match(conReg);
		if(arr != null){
			for (var i = 0; i < arr.length; i++) {
				var imgHtml = $(".face").find("ul li img[title='"+arr[i]+"']").parent().html();
				if(imgHtml != undefined){
					//防止title内容被替换，直接删除
					imgHtml = imgHtml.replace(arr[i], "&nbsp;");
					content = content.replace(arr[i], imgHtml);
				}
			}
		}
		$(this).html(content);
});
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
		showAction($(".ac_export"));
		showAction($(".ac_del"));
		showAction($(".ac_share"));
	} else {
		if(fileArray.length > 0 && fileArray.length != 1){
			//显示下载、复制、移动、移除、分享、重命名
			showAction($(".ac_down"));
			showAction($(".ac_copy"));
			showAction($(".ac_move"));
			showAction($(".ac_export"));
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
	hideAction($(".ac_export"));
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
	showAction($(".ac_export"));
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

//留言提交
function ajaxRemark(content){
	var data = {
		"content" : content,
		"teamworkId" : $("#teamworkId").val()
	};
	//在进行管理
	 $.ajax({
		url: basepath+"/teamworkfile/sendContent",
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(data),
        contentType:"application/json",
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) { 
	        	$("#content").html("");
	        	sendWebsocket(data.result);//websocket发布
	        }
     }}); 
}

function sendWebsocket(data){
	if(isConnectWs){
		ws.send(data);
	}
}