function hrefSearchFile(id){
	window.location.href = basepath+"/sign/index?signId="+id;
}

//checkbox模拟
$(function(){
	//加载文件与标签相关列表数据
    loadTable(0);
    
	//移除标签(批量)（自有）
    $(".remove_qx").live("click",function(){
    	if(!checkPermission(this)){
    		return;
    	}
    	var data = getCheckSelectedSign();
    	if(data.fileSigns.length == 0){
    		 layer.msg(i18n_select_record,{icon: 5,time:1000});
	   		 return false;
	   	}
    	//构建移除展示
    	var nameArray = new Array();
    	for(var i=0;i<data.fileSigns.length;i++){
    		var fileId = data.fileSigns[i].fileId;
    		var signId = data.fileSigns[i].signId;
    		var fileType = data.fileSigns[i].fileType;
    		var r = "#fileName"+fileId+"-"+signId;
        	var fileName = $(r).html();
        	nameArray.push(fileName);
    	}
    	if(nameArray.length == 1){
    		$("#move").html(i18n_delete_sign_confirm1+nameArray+i18n_delete_sign_confirm2);
    	} else {
    		$("#move").html(i18n_delete_signs_confirm1+nameArray.length+i18n_delete_signs_confirm2);
    	}
        layer.open({
            type: 1,
            title :i18n_delete_sign,
            area: ['600px', '183px'],
            shadeClose: true, //点击遮罩关闭
            content: $('.con_remove_qx')
            ,btn: [determine, close],
            yes: function(index, layero){
            	$.ajax({
            		url: basepath+"/sign/delfilesign",
                    dataType:"json",
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    async: false, 
                    success: function(data){
                    	if (data.code == 1000) {
                    		layer.closeAll();
                    		layer.msg(delete_success,{icon: 1,time:2000});
            	        	setTimeout(function(){
            	        		window.location.reload();
            	        	}, 1500);
            	        }
            	  }}); 
          	  }
        });
    });
	
     $(".tab_check").live("click",function(){
    	$(this).toggleClass("tab_checked");
    	selectCheckBox($(this));
     });
     
    
     $(".tabs tr td:nth-child(2)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
    	 selectCheckBox(obj);
     });
     
     $(".tabs tr td:nth-child(3)").live("click",function(){
    	 $(this).parent().find('.tab_check').toggleClass("tab_checked");
    	 var obj = $(this).parent().find('.tab_check');
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
     
    //点击标签搜索文件与标签相关
    $(".labels_list").find('li div .sign_search').on('click',function(){
    	var signId = $(this).parent().find('input :first').val();
    	loadTable(signId);
    });
    
  	//搜索文件名
	$("#searchFile").click(function(){
    	loadTable();
	});
  	
  	//搜索标签
	$(".searchSign").click(function(){
		$("#querySign").submit();
	});
	
  	//标签管理
    $(".label_list_btn").live("click",function(){
        $(".labels_list").find("label").toggle();
    });
  
    /**
     * 下载
     */
     $(".btn_download_enable").live("click",function(){
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
   		}, 500);
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
			        	/*var myWindow = window.open(fileServiceUrl + "/fileManager/preview?params=" + result.codeInfo);
			        	myWindow.document.title = "###";
			        	window.location.href = fileServiceUrl + "/fileManager/preview?params=" + result.codeInfo;*/
			        }else{
			        	layer.msg(result.codeInfo,{icon: 5,time:1000});
			        }
		        }
	 		});
		}
 	});
    
    //新建标签
    $(".sign_new").click(function(){
    	$("#sign_new_val").val("");
    	layer.open({
            type: 1,
            title :i18n_create_sign,
            area: ['400px', '210px'],
            shadeClose: false, //点击遮罩关闭
            content: $(".con_sign_new")
            ,btn: [determine, close],
            yes: function(index, layero){
            	var signName = $("#sign_new_val").val();
            	//检测标签名
        		signName = signName.replace(/^\s+|\s+$/g,"");
        		if(signName == null || signName == ''){
        			$("#sign_new_val").val("")
        			return;
        		}
        		if(signName=='null'){
        			layer.msg(i18n_sign_confirm1,{icon: 5,time:1500}); 
        		    return;
        		}
        		var index = $.inArray(signName, json);
        		if(index != -1){
        			layer.msg(i18n_sign_confirm2,{icon: 5,time:1500}); 
        		    return;
        		}
        		var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
        		if (patrn.test(signName)) {
        			layer.msg(i18n_sign_confirm3,{icon: 5,time:1500}); 
        		    return;
        		}
        		//ajax
            	var data = {
            			"signName" : signName
           		}
           		$.ajax({
           			url: basepath+"/sign/save",
           	        dataType:"json",
           	        type: 'POST',
           	        data:data,
           	        async: false, 
           	        success: function(data){
           		        if (data.code == 1000) {
           		        	layer.closeAll();
           		        	layer.msg(create_success,{icon: 1,time:1500});
           		        	setTimeout(function(){
           		        		refresh();
           		        	}, 1500);
           		        }
           	        }
           		 });
        	}
        });
    	
    });
    
    //编辑标签
    $(".sign_edit").click(function(){
    	var name = $(this).parent().parent().find("a:first").html();
    	$(".con_sign_edit").find("p input:first").val(name);
    	var signId = $(this).parent().find("input:first").val();
    	layer.open({
            type: 1,
            title :i18n_edit_sign,
            area: ['400px', '210px'],
            shadeClose: false, //点击遮罩关闭
            content: $(".con_sign_edit")
            ,btn: [determine, close],
            yes: function(index, layero){
            	var signName = $("#sign_edit_val").val();
            	//检测标签名
        		signName = signName.replace(/^\s+|\s+$/g,"");
        		if(signName == null || signName == ''){
        			$("#sign_edit_val").val("");
        			return;
        		}
        		if(signName=='null'){
        			layer.msg(i18n_sign_confirm1,{icon: 5,time:1500}); 
        		    return;
        		}
        		var index = $.inArray(signName, json);
        		if(index != -1 && signName != name){
        			layer.msg(i18n_sign_confirm2,{icon: 5,time:1500}); 
        		    return;
        		}
        		var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
        		if (patrn.test(signName)) {
        			layer.msg(i18n_sign_confirm3,{icon: 5,time:1500}); 
        		    return;
        		}
        		//ajax
            	var data = {
            			"id"   : signId,
            			"signName" : signName
           		}
           		$.ajax({
           			url: basepath+"/sign/edit",
           	        dataType:"json",
           	        type: 'POST',
           	        data:data,
           	        async: false, 
           	        success: function(data){
           		        if (data.code == 1000) {
           		        	layer.closeAll();
           		        	layer.msg(successful_operation,{icon: 1,time:1500});
           		        	setTimeout(function(){
           		        		refresh();
           		        	}, 1500);
           		        }
           	        }
           		 });
       	  	}
        });
    	
    });
    
    //删除标签
    $(".sign_remove").click(function(){
    	var name = $(this).parent().parent().find("a:first").html();
    	$("#move").html(i18n_delete_sign1+name+i18n_delete_sign2);
    	var signId = $(this).parent().find("input:first").val();
    	layer.open({
            type: 1,
            title :i18n_delete_sign,
            area: ['600px', '183px'],
            shadeClose: false, //点击遮罩关闭
            content: $(".con_remove_qx")
            ,btn: [determine, close],
            yes: function(index, layero){
            	var data = {
            			"signId"   : signId
           		}
           		$.ajax({
           			url: basepath+"/sign/delete",
           	        dataType:"json",
           	        type: 'POST',
           	        data:data,
           	        async: false, 
           	        success: function(data){
           		        if (data.code == 1000) {
           		        	layer.closeAll();
           		        	layer.msg(delete_success,{icon: 1,time:1500});
           		        	setTimeout(function(){
           		        		refresh();
           		        	}, 1500);
           		        }
           	        }
           		 });
       	  	}
        });
    	
    });
});

function selectCheckBox(obj){
	var _class = obj.attr('class');
	if(_class == undefined){
		return ;
	}
	if(_class.indexOf('tab_checked') == -1){
		$("#allCheck").removeClass("tab_checked");
	}
    //计算选中记录条数
    var fileSignArray = new Array();
    var id = obj.attr('id');
    $(".tabs tr").attr("style","");
   	$("i[name='fileSignCheck']").each(function(){
        if(id == 'allCheck'){
         	$(this).attr('class',_class);
        }
	     	 if($(this).attr('class').indexOf('tab_checked') != -1){
	     		 $(this).parent().parent().attr("style","background: #f3f3f3;");
	     		fileSignArray.push($(this).find("input[name='fileId']").val());
	     	 }
    });
    //判断是否全选
    $("#fileSignCount").html(fileSignArray.length);
    showOperBtn(fileSignArray)
}

function loadTable(signId){
	var fileName = $("#fileName").val();
    var data = {
    	signId : signId,
    	fileName : fileName
    }
	//table查询
    $(".content_label").load(basepath+'/sign/loadTable',data); 
}

//选中操作后选中的文件和文件夹数量
 function getCheckSelected(folderId){
	 var folderArray = new Array();
	 var fileArray = new Array();
     $("i[name='fileSignCheck']").each(function(){
 	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
 	   		var id= $(this).find("input[name='fileId']").val();
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

//选中操作后选中的标签数量（自有）
function getCheckSelectedSign(){
   var fileSignArray = new Array();
   $("i[name='fileSignCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var fileId= $(this).find("input[name='fileId']").val();
	   		var signId= $(this).find("input[name='signId']").val();
	   		var fileType= $(this).find("input[name='fileType']").val();
	   		var fileSignJson = {
	   			"fileId" : fileId,
	   			"signId" : signId,
	   			"fileType" : fileType
	   		}
	   		fileSignArray.push(fileSignJson);
	   	 }
    });
    var data = {
		"fileSigns" : fileSignArray
	}
    return data;
}

//根据勾选数量显示不同操作按钮
function showOperBtn(fileArray){
	if(fileArray.length > 0 ){
		showAction($(".btn_download_enable"));
		showAction($(".remove_qx"));
	} else{
		hideAction($(".btn_download_enable"));
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
