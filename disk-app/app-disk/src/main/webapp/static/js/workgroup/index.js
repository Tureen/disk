$(function(){
	//员工加入状态
	var joinIds = $("#joinIds").val();
	var joinIdArray = eval("("+joinIds+")");
	if(joinIdArray!=null && joinIdArray.length > 0){
		for (var i=0;i<joinIdArray.length;i++){
			//工作组加入状态修改
			$("#"+joinIdArray[i]).parent().parent().find(".join_status").html("<span style=\"color: #008000\">" + i18n_workgroup_is_join + "</span>");
			//查看工作组权限修改
			$("#"+joinIdArray[i]).parent().parent().find(".has_status").html("<span class=\"view_user\">" + i18n_workgroup_view_user + "</span>");
		}
	}
	//员工拥有组
	var hasIds = $("#hasIds").val();
	var hasIdArray = eval("("+hasIds+")");
	if(hasIdArray!=null && hasIdArray.length > 0){
		for (var i=0;i<hasIdArray.length;i++){
			//查看工作组权限修改
			$("#"+hasIdArray[i]).parent().parent().find(".has_status").html("<span class=\"view_user\">" + i18n_workgroup_view_user + "</span>");
		}
	}
	//开启添加群组权限
	showAction(".ac_add");
	showAction(".ac_my_apply");
	
	$("#jump").live("click",function(){
		var fileIds = $(this).parent().find("input:eq(0)").val();
		var folderIds = $(this).parent().find("input:eq(1)").val();
		$("#messageJump").find("input:eq(0)").val(fileIds);
		$("#messageJump").find("input:eq(1)").val(folderIds);
		$("#messageJump").submit();
	});
	
	
	/**
	 * 复选选中/取消
	 */
	$(".stabs").live("click",function(){
		if($(this).find("td:first i").hasClass("tab_check")){
			$(this).find("td:first i").toggleClass("tab_checked");
			getCheckSelected();
		}
	});
	
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
	 * 添加群组
	 */
	$(".ac_add").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		layer.open({
	        type: 2,
	        title :i18n_w_add_workgroup,
	        area: ['600px', '500px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/workgroup/tosave'
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addWorkgroup() //得到iframe页的body内容
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
	        title :i18n_w_edit_workgroup,
	        area: ['600px', '500px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/workgroup/toedit?workgroupId='+idsArray[0]
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addWorkgroup() //得到iframe页的body内容
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
			layer.msg(i18n_workgroup_select_delete_contact,{icon: 5,time:1500});
			return;
		}
		layer.confirm(i18n_workgroup_confirm_delete_contact, {icon: 3, title: i18n_global_prompt},function(index){
			layer.closeAll();
			$.ajax({
	     		url: basepath+"/workgroup/delete",
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
	 * 我的申请
	 */
	$(".ac_my_apply").live("click",function(){
		var workgroupId = $(this).parent().parent().find("i[name='msgItem']").attr("value");
		layer.open({
	        type: 2,
	        title :i18n_w_my_application_record,
	        area: ['681px', '460px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/workgroup/myworkgroupapply'
	        ,btn: [close]
	    });
	});
	
	/**
	 * 申请加入
	 */
	$(".ac_apply").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		var data = getCheckSelected();
		$.ajax({
     		url: basepath+"/workgroup/saveapply",
            dataType:"json",
            data: JSON.stringify(data),
            type: 'POST',
            contentType: "application/json",
            async: false, 
            success: function(data){
     	        if (data.code == 1000) { 
	       	        layer.msg(i18n_workgroup_apply_for,{icon: 1,time:1500});
		        	setTimeout(function(){refresh();}, 1500);
     	        }else{
     	        	layer.msg(i18n_workgroup_apply_for_fail,{icon: 5,time:2000});
     	        }
            }
		});
	});
	
	/**
	 * 退出群组
	 */
	$(".ac_quit").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		layer.confirm(i18n_workgroup_confirm_quit, {icon: 3, title: i18n_global_prompt},function(index){
			var data = getCheckSelected();
			$.ajax({
	     		url: basepath+"/workgroup/quitworkgroup",
	            dataType:"json",
	            data: JSON.stringify(data),
	            type: 'POST',
	            contentType: "application/json",
	            async: false, 
	            success: function(data){
	     	        if (data.code == 1000) { 
		       	        layer.msg(i18n_global_exit_success,{icon: 1,time:1500});
			        	setTimeout(function(){refresh();}, 1500);
	     	        }else if(data.code == 2040){
	     	        	layer.msg(i18n_w_createadmin_sames_select,{icon: 5,time:2000});
	     	        }else{
	     	        	layer.msg(i18n_global_exit_fail,{icon: 5,time:2000});
	     	        }
	            }
			});
		});
	});
	
	/**
	 * 管理群组用户
	 */
	$(".ac_adduser").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		var idsArray = getCheckSelected();
		if(idsArray.length != 1){
			layer.msg(i18n_workgroup_select_user_group_to_add,{icon: 5,time:1500});
			return;
		}
		layer.open({
	        type: 2,
	        title :i18n_w_add_workgroup_user,
	        area: ['732px', '510px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/workgroup/loaduser?workgroupId='+idsArray[0]
	        ,btn: [determine, close],
	        yes: function(index, layero){
	      	    //按钮【确定】的回调
           		var body = layer.getChildFrame('body', index);
    			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
    			iframeWin.addWorkgroupUser() //得到iframe页的body内容
	      	  }
	    });
	});
	
	/**
	 * 查看用户
	 */
	$(".view_user").live("click",function(){
		var workgroupId = $(this).parent().parent().find("i[name='msgItem']").attr("value");
		layer.open({
	        type: 2,
	        title :i18n_workgroup,
	        area: ['481px', '460px'],
	        shadeClose: false, //点击遮罩关闭
	        content: basepath + '/workgroup/seeuser?workgroupId='+workgroupId
	        ,btn: [close]
	    });
	});
	
	/**
	 * 转让群组
	 */
	$(".ac_transfer").live("click",function(){
		if(!checkPermission(this)){
    		return;
    	}
		var idsArray = getCheckSelected();
		if(idsArray.length != 1){
			layer.msg(i18n_workgroup_select_transfer_group,{icon: 5,time:1500});
			return;
		}
		layer.confirm(i18n_workgroup_sure_to_transfer_the_group, {icon: 3, title: i18n_global_prompt},function(index){
			layer.closeAll();
			layer.open({
		        type: 2,
		        title :i18n_w_transfer_workgroup,
		        area: ['632px', '510px'],
		        shadeClose: false, //点击遮罩关闭
		        content: basepath + '/workgroup/seetransfer?workgroupId='+idsArray[0]
				,btn: [determine, close],
		        yes: function(index, layero){
		      	    //按钮【确定】的回调
		       		var body = layer.getChildFrame('body', index);
					var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					iframeWin.addWorkgroupUser() //得到iframe页的body内容
		      	  }
		    });
		});
		
	});
		
		/*var dialog = jDialog.confirm(i18n_workgroup_sure_to_transfer_the_group,{
			handler : function(button,dialog) {
				dialog.close();
				layer.open({
			        type: 2,
			        title :i18n_workgroup,
			        area: ['632px', '510px'],
			        shadeClose: false, //点击遮罩关闭
			        content: basepath + '/workgroup/seetransfer?workgroupId='+idsArray[0]
					,btn: [determine, close],
			        yes: function(index, layero){
			      	    //按钮【确定】的回调
			       		var body = layer.getChildFrame('body', index);
						var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.addWorkgroupUser() //得到iframe页的body内容
			      	  }
			    });
				
			}
		},{
			handler : function(button,dialog) {
				dialog.close();
			}
		});	*/
	
		
	/**
	 * 点击查询
	 */
	$("#searchFile").live("click", function(){
		var data_url = $("#searchForm").attr("action");
		var queryName = $("#queryName").val();
		var queryEmployeeId = $("#queryEmployeeId").val();
		data_url = data_url +"?employeeId="+queryEmployeeId+"&workgroupName=" + encodeURI(encodeURI(queryName));
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
	function showOperBtn(msgArray){
		if(msgArray.length > 0){
			if(msgArray.length == 1){
				showAction($(".ac_edit"));
				showAction($(".ac_adduser"));
				showAction($(".ac_transfer"));
			}else{
				hideAction($(".ac_edit"));
				hideAction($(".ac_adduser"));
				hideAction($(".ac_transfer"));
			}
			showAction($(".ac_del"));
			showAction($(".ac_apply"));
			showAction($(".ac_quit"));
		}else{
			hideAction($(".ac_edit"));
			hideAction($(".ac_apply"));
			hideAction($(".ac_quit"));
			hideAction($(".ac_adduser"));
			hideAction($(".ac_transfer"));
			hideAction($(".ac_del"));
		}
	}
	
	//根据勾选id判断权限并隐藏功能
	function permissionHideOperBtn(msgArray){
		if(msgArray.length > 0){
			if(msgArray.length == 1){
				var workgroupId = msgArray[0];
				if($.inArray(workgroupId * 1, hasIdArray) == -1){
					hideAction($(".ac_edit"));
					hideAction($(".ac_transfer"));
					hideAction($(".ac_adduser"));
				}
			}
			for (var i=0;i<msgArray.length;i++){
				if($.inArray(msgArray[i] * 1, hasIdArray) == -1){
					hideAction($(".ac_del"));
				}
				if($.inArray(msgArray[i] * 1, joinIdArray) != -1){
					hideAction($(".ac_apply"));
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
});

	
function onClick(e, treeId,treeNode){
	$("#deptName").val(treeNode.name);
	$("#deptId").val(treeNode.id);
}

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