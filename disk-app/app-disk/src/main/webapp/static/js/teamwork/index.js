$(function(){
	//员工加入协作
	var joinIds = $("#joinIds").val();
	var joinIdArray = eval("("+joinIds+")");
	//员工拥有协作
	var hasIds = $("#hasIds").val();
	var hasIdArray = eval("("+hasIds+")");
	
	$(".stabs").live("click",function(){
		if($(this).find("td:first i").hasClass("tab_check")){
			$(this).find("td:first i").toggleClass("tab_checked");
			getCheckSelected();
		}
	});
	
	//开启添加群组权限
	showAction(".ac_add");
	
	/**
	 * 全选/反选
	 */
	$("#allCheck").live("click",function(){
		$(this).toggleClass("tab_checked");
		var _class = $(this).attr('class');
		if(_class.indexOf('tab_checked') == -1){
			$(".tab_check").not($("#allCheck")).removeClass("tab_checked");
		}else{
			$(".tab_check").not($("#allCheck")).addClass("tab_checked");
		}
		getCheckSelected();
	});
	
	/**
	 * 创建协作
	 */
	$(".ac_add").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		layer.open({
	        type: 2,
	        title :i18n_create_teamwork,
	        area: ['600px', '510px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/teamwork/tosave'
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addTeamwork() //得到iframe页的body内容
	      	  }
	    });
	});
	
	/**
	 * 编辑
	 */
	$(".ac_edit").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		var idsArray = getCheckSelected();
		if(idsArray.length != 1){
			layer.msg(i18n_workgroup_select_edit_operation,{icon: 5,time:1500});
			return;
		}
		layer.open({
	        type: 2,
	        title :i18n_edit_teamwork,
	        area: ['600px', '510px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/teamwork/toedit?teamworkId='+idsArray[0]
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addTeamwork() //得到iframe页的body内容
	      	  }
	    });
	});
	
	/**
	 * 删除
	 */
	$(".ac_del").live("click", function(){
		if(!checkPermission(this)){
    		return;
    	}
		var data = getCheckSelected();
		if(data.length == 0){
			layer.msg(i18n_teamwork_select_delete_contact,{icon: 5,time:1500});
			return;
		}
		layer.confirm(i18n_teamwork_confirm_delete_contact, {icon: 3, title: i18n_global_prompt},function(index){
			layer.closeAll();
			$.ajax({
	     		url: basepath+"/teamwork/delete",
	            dataType:"json",
	            type: 'POST',
	            data: JSON.stringify(data),
	            contentType: "application/json",
	            async: false, 
	            success: function(data){
	     	        if (data.code == 1000) { 
		       	        layer.msg(i18n_global_operation_success,{icon: 1,time:1500});
			        	setTimeout(function(){refresh();}, 1500);
	     	        }else{
	     	        	layer.msg(i18n_global_operation_fail,{icon: 2,time:1500});
	     	        }
	            }
			});
		})
	});
	
	/**
	 * 退出协作
	 */
	$(".ac_quit").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		layer.confirm(i18n_teamwork_confirm_quit, {icon: 3, title: i18n_global_prompt},function(index){
			var data = getCheckSelected();
			$.ajax({
	     		url: basepath+"/teamwork/quitteamwork",
	            dataType:"json",
	            data: JSON.stringify(data),
	            type: 'POST',
	            contentType: "application/json",
	            async: false, 
	            success: function(data){
	     	        if (data.code == 1000) { 
		       	        layer.msg(i18n_global_exit_success,{icon: 1,time:1500});
			        	setTimeout(function(){refresh();}, 1500);
	     	        }else if(data.code == 2041){
	     	        	layer.msg(i18n_teamwork_createadmin_sames_select,{icon: 5,time:2000});
	     	        }else{
	     	        	layer.msg(i18n_global_exit_fail,{icon: 5,time:2000});
	     	        }
	            }
			});
		});
	});
	
	/**
	 * 管理协作用户
	 */
	$(".ac_adduser").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		var idsArray = getCheckSelected();
		if(idsArray.length != 1){
			layer.msg(i18n_teamwork_select_user_group_to_add,{icon: 5,time:1500});
			return;
		}
		layer.open({
	        type: 2,
	        title :i18n_teamwork_add_workgroup_user,
	        area: ['732px', '510px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/teamwork/loaduser?teamworkId='+idsArray[0]
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addTeamworkUser() //得到iframe页的body内容
	      	  }
	    });
	});
	
	/**
	 * 查看用户
	 */
	$(".view_user").live("click",function(){
		var teamworkId = $(this).parent().parent().parent().parent().find("i[name='msgItem']").attr("value");
		layer.open({
	        type: 2,
	        title :i18n_teamwork_user,
	        area: ['481px', '460px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/teamwork/seeuser?teamworkId='+teamworkId
	        ,btn: [close]
	    });
	});
	
	/**
	 * 开始协作
	 */
	$(".begin_teamwork").live("click",function(){
		var teamworkId = $(this).parent().parent().parent().parent().find("i[name='msgItem']").attr("value");
		$.ajax({
     		url: basepath + "/teamworkfile/checkIndex?teamworkId=" + teamworkId,
            type: 'GET',
            contentType: "application/json",
            async: false, 
            success: function(data){
     	        if (data.code == 1000) { 
     	        	window.location.href = basepath + "/teamworkfile/index?teamworkId=" + teamworkId;
     	        }else{
     	        	layer.msg(i18n_global_no_operation_permission,{icon: 5,time:2000});
     	        }
            }
		});
	});
	
	$("#searchFile").live("click", function(){
		var data_url = $("#searchForm").attr("action");
		var queryName = $("#queryName").val();
		var teamworkSearchType = $("#teamworkSearchType").val();
		data_url = data_url +"?teamworkSearchType=" + teamworkSearchType*1 +"&teamworkName=" + encodeURI(encodeURI(queryName));
		window.location.href = data_url;
	});
	
	/**
	 * 获取选中的消息
	 */
	function getCheckSelected(){
		if($("#allCheck").hasClass("tab_checked") && $(".stabs .tab_checked").length != $(".stabs .tab_check").length){
			$("#allCheck").toggleClass("tab_checked");
		}
		if(!$("#allCheck").hasClass("tab_checked") && $(".stabs .tab_check").length > 0 && $(".stabs .tab_checked").length == $(".stabs .tab_check").length){
			$("#allCheck").toggleClass("tab_checked");
		}
		$(".stabs").attr("style","");
		$(".stabs").find(".tab_check").each(function(){
			if($(this).hasClass("tab_checked")){
				$(this).parent().parent().attr("style","background: #f3f3f3;");
			}
		});
	   var msgArray = new Array();
	   $(".tab_checked[name='msgItem']").each(function(){
		   msgArray.push($(this).attr("value"));
		});
	   showOperBtn(msgArray);
	   permissionHideOperBtn(msgArray);
	    return msgArray;
	}
	
	//根据勾选数量显示不同操作按钮
	function showOperBtn(fileArray){
		if(fileArray.length > 0 ){
			if(fileArray.length == 1){
				showAction($(".ac_edit"));
				showAction($(".ac_adduser"));
			}else{
				hideAction($(".ac_edit"));
				hideAction($(".ac_adduser"));
			}
			showAction($(".ac_del"));
			showAction($(".ac_quit"));
		} else{
			hideAction($(".ac_del"));
			hideAction($(".ac_edit"));
			hideAction($(".ac_quit"));
			hideAction($(".ac_adduser"));
		}
	}
	
	//根据勾选id判断权限并隐藏功能
	function permissionHideOperBtn(msgArray){
		if(msgArray.length > 0){
			if(msgArray.length == 1){
				var teamworkId = msgArray[0];
				if($.inArray(teamworkId * 1, hasIdArray) == -1){
					hideAction($(".ac_edit"));
					hideAction($(".ac_adduser"));
				}
			}
			for (var i=0;i<msgArray.length;i++){
				if($.inArray(msgArray[i] * 1, hasIdArray) == -1){
					hideAction($(".ac_del"));
				}
				if($.inArray(msgArray[i] * 1, joinIdArray) != -1){
					
				}else{
					hideAction($(".ac_quit"));
				}
			}
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
	
	//清空选中项
	function clearCheck(){
		$(".tab_checked").removeClass("tab_checked");
	}
});
	
function showMenu() {
	var cityObj = $("#deptName");
	var cityOffset = $("#deptName").position();
	
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent")) {
		if((event.target.className).indexOf('switch')==-1){
			hideMenu();
		}
	}
}