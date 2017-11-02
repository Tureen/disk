$(function(){
	$("#jump").live("click",function(){
		var fileIds = $(this).parent().find("input:eq(0)").val();
		var folderIds = $(this).parent().find("input:eq(1)").val();
		$("#messageJump").find("input:eq(0)").val(fileIds);
		$("#messageJump").find("input:eq(1)").val(folderIds);
		$("#messageJump").submit();
	});
	
	var t = $("#treeDemo");
	if(t.length!=0){
		t = $.fn.zTree.init(t, setting, zNodes);
	}
	
	/**
	 * 复选选中/取消
	 */
	/*$(".tab_check").live("click",function(){
		$(this).toggleClass("tab_checked");
		getCheckSelected();
	});*/
	
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
	 * 删除关联
	 */
	$(".ac_del").live("click", function(){
		if(!checkPermission(this)){
    		return;
    	}
		var data = getCheckSelected();
		if(data.length == 0){
			layer.msg(i18n_select_delete_contact,{icon: 5,time:1500});
			return;
		}
		layer.confirm(i18n_confirm_delete_contact, {icon: 3, title: i18n_global_prompt},function(index){
			$.ajax({
	     		url: basepath+"/contact/delete",
	            dataType:"json",
	            type: 'POST',
	            data: JSON.stringify(data),
	            contentType: "application/json",
	            async: false, 
	            success: function(data){
	     	        if (data.code == 1000) { 
		       	        layer.msg(i18n_global_delete_success,{icon: 1,time:1500});
			        	setTimeout(function(){window.location.href = window.location.href;}, 1500);
	     	        }else{
	     	        	layer.msg(i18n_global_delete_fail,{icon: 2,time:1500});
	     	        }
	            }
			});
		})
	});
	
	$("#searchFile").live("click", function(){
		var data_url = $("#searchForm").attr("action");
		var queryName = $("#queryName").val();
		var deptId = $("#deptId").val();
		var deptName = $("#deptName").val();
		data_url = data_url +"?employeeName=" + encodeURI(encodeURI(queryName)) + "&deptId=" +deptId + "&deptName=" + encodeURI(encodeURI(deptName)) ;
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
	    return msgArray;
	}
	
	/**
	 * 标记已读
	 */
	function markRead(){
		var data = getPageUnread();
		if(data.length > 0){
			$.ajax({
	     		url: basepath+"/message/markRead",
	            dataType:"json",
	            type: 'POST',
	            data: JSON.stringify(data),
	            contentType: "application/json",
	            async: false, 
	            success: function(data){
	            	
	            }
			});
		}
	}
	
	/**
	 * 获取当前页面未读消息
	 */
	function getPageUnread(){
		var msgArray = new Array();
	    $(":input[name='unReadMsg']").each(function(){
		   msgArray.push($(this).val());
		});
	    return msgArray;
	}
	
	//调用标记当前页面的消息状态为已读
	markRead();
	
	//根据勾选数量显示不同操作按钮
	function showOperBtn(fileArray){
		if(fileArray.length > 0 ){
			showAction($(".ac_del"));
		} else{
			hideAction($(".ac_del"));
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

var setting = {
		view: {
			dblClickExpand: false,
			showLine: false,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: ""
			}
		},
		callback: {
			onClick:onClick
		}
	};


	
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