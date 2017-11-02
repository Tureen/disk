var flag = true;
function addWorkgroupUser(){
	if(!flag){
		layer.msg(i18n_workgroup_please_do_not_submit,{icon: 5,time:1500});
		return;
	}
	var employeeId = 0;
	$(".add_employee").each(function(){
		employeeId = $(this).attr("code");
	});
	if(employeeId == 0){
		layer.msg(i18n_workgroup_please_select_transfer_staff,{icon: 5,time:1500});
		return;
	}
	var workgroupId = $("#workgroupId").val();
	$.ajax({
 		 url: basepath+"/workgroup/transfer?workgroupId="+workgroupId+"&employeeId="+employeeId,
        type: 'GET',
        contentType: "application/json",
        async: false, 
        success: function(data){
	       	checkErrorCode(data);
	       	checkErrorCode(data);
	        if (data.code == 1000) {
	        	flag = false;
	        	layer.msg(i18n_global_operation_success,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		parent.refresh();
	        		//parent.layer.closeAll();
	        	}, 1600);
	        }
  		}
	});  
}

function beforeAjax(type){
	$.ajax({
		url: basepath+"/workgroup/loaduserajax",
        dataType:"json",
        type: 'POST',
        data:$("#SubForm").serialize(),
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	//清空可选择用户
	        	$("#Setuserlist").html("");
	        	//填充
	        	var allObj = data.result;
	        	var employeeArray = allObj.list;
	        	if(employeeArray!=null && employeeArray.length > 0){
	        		for (var i=0;i<employeeArray.length;i++){
	        			if(employeeArray[i].employeeEmail==null){
	        				employeeArray[i].employeeEmail = "";
	        			}
	        			$("#Setuserlist").append("<li code=\""+employeeArray[i].id+"\" class=\"show_employee\"><div class=\"column column_id\">"+employeeArray[i].id+"&nbsp;</div><div class=\"column column_name\">"+employeeArray[i].employeeName+"&nbsp;</div><div class=\"column column_department\" title=\""+employeeArray[i].deptName+"\">"+employeeArray[i].deptName+"&nbsp;</div><div class=\"column column_email\">"+employeeArray[i].employeeEmail+"&nbsp;</div></li>");
	        		}
	        	}
	        	var recordCount = allObj.recordCount;
	        	var pageSize = allObj.pageSize;
	        	var pageIndex = allObj.pageIndex;
	        	//总记录数
	        	$("#new_totalCount").html("&nbsp;"+recordCount+"&nbsp;");
	        	//记录数（从x到y）
	        	var total1 = (pageIndex-1) * pageSize + 1;
	        	var total2 = (pageIndex) * pageSize;
	        	if(total2 > recordCount){
	        		total2 = recordCount;
	        	}
	        	$("#total_1").html("&nbsp;"+total1+"&nbsp;");
	        	$("#total_2").html("&nbsp;"+total2+"&nbsp;");
	        	//总页数
	        	var pageCount = Math.ceil(recordCount/pageSize);
	        	$("#new_totalpage").html(pageCount);
	        	
	        	//按钮判断
	        	if(recordCount != 0){
		        	if($("#new_page").val() == 1){
		        		if(!$("#page_frist").hasClass("disabled")){
		        			$("#page_frist").addClass("disabled");
		        		}
		        		if(!$("#page_prev").hasClass("disabled")){
		        			$("#page_prev").addClass("disabled");
		        		}
		        	}else{
		        		if($("#page_frist").hasClass("disabled")){
		        			$("#page_frist").removeClass("disabled");
		        		}
		        		if($("#page_prev").hasClass("disabled")){
		        			$("#page_prev").removeClass("disabled");
		        		}
		        	}
		        	
		        	if($("#new_page").val() == $("#new_totalpage").html()){
		        		if(!$("#page_last").hasClass("disabled")){
		        			$("#page_last").addClass("disabled");
		        		}
		        		if(!$("#page_next").hasClass("disabled")){
		        			$("#page_next").addClass("disabled");
		        		}
		        	}else{
		        		if($("#page_last").hasClass("disabled")){
		        			$("#page_last").removeClass("disabled");
		        		}
		        		if($("#page_next").hasClass("disabled")){
		        			$("#page_next").removeClass("disabled");
		        		}
		        	}
	        	}else{
	        		$("#page_frist").addClass("disabled");
	        		$("#page_prev").addClass("disabled");
	        		$("#page_next").addClass("disabled");
	        		$("#page_last").addClass("disabled");
	        	}
	        }
        }
	 });
}

$(function(){
	var t = $("#treeDemo");
	if(t.length!=0){
		t = $.fn.zTree.init(t, setting, zNodes);
	}
	
	//初始化可选择员工
	beforeAjax();
	
	//添加选择
	$(".show_employee").live("click",function(){
		//判断是否有其他已选择员工
		if($(this).parent().find(".is_add").length > 0){
			$(this).parent().find(".is_add").each(function(){
				$(this).removeClass("is_add");
				$(".add_div").parent().find("li[code='"+$(this).attr("code")+"']").remove();;
			});
		}
		var addObj = $(this);
		if($(addObj).hasClass("is_add")){
			return;
		}
		$(addObj).addClass("is_add");
		var addText = "<li class=\"add_employee\" code=\""+$(addObj).attr("code")+"\"><div class=\"column column_id\">"+$(addObj).attr("code")+"&nbsp;</div><div class=\"column column_name\">"+$(addObj).find(".column_name").html()+"&nbsp;</div><div class=\"column column_department\" title=\""+$(addObj).find(".column_department").html()+"\">"+$(addObj).find(".column_department").html()+"&nbsp;</div><div class=\"column column_email\">"+$(addObj).find(".column_email").html()+"&nbsp;</div></li>"
		$(".add_div").append(addText);
	});
	
	//取消选择
	$(".add_employee").live("click",function(){
		var delObj = $(this);
		$(".show_div").find("li[code='"+$(delObj).attr("code")+"']").removeClass("is_add");
		$(delObj).remove();
	});
	
	//首页
	$("#page_frist").live("click",function(){
		if($(this).hasClass("disabled")){
			return;
		}
		//改变页数
		$("#new_page").val("1");
		beforeAjax(1);
	});
	
	//上一页
	$("#page_prev").live("click",function(){
		if($(this).hasClass("disabled")){
			return;
		}
		//改变页数
		$("#new_page").val($("#new_page").val()*1 - 1);
		beforeAjax(1);
	});
	
	//下一页
	$("#page_next").live("click",function(){
		if($(this).hasClass("disabled")){
			return;
		}
		//改变页数
		$("#new_page").val($("#new_page").val()*1 + 1);
		beforeAjax(1);
	});
	
	//末页
	$("#page_last").live("click",function(){
		if($(this).hasClass("disabled")){
			return;
		}
		//改变页数
		$("#new_page").val($("#new_totalpage").html());
		beforeAjax(1);
	});
	
	//刷新
	$("#page_refresh").live("click",function(){
		$("#userKey1").val("");
		$("#new_page").val("1");
		beforeAjax(1);
	});
	
	//搜索
	$("#search").live("click",function(){
		beforeAjax(1);
	});
	
	//展示条数切换
	$("#new_pageSize").live("change",function(){
		beforeAjax(1);
	});
	
	//页数选择
	$('#new_page').live('keydown',function(e){
    	if(e.keyCode==13){
    		//判断是否在可选页数内
    		var num = $(this).val();
    		var totalNum = $("#new_totalpage").html();
    		if(num == "" || num == null || num == "undefined"){
    			$(this).val(1);
    		}else if(num*1 == 0){
    			$(this).val(1);
    		}else{
    			$(this).val(totalNum*1);
    		}
   			beforeAjax(1);
    		//失去焦点
   			$(this).blur();
    	}
	});
	
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

//点击部门事件
function onClick(e, treeId,treeNode){
	$("#deptId").val(treeNode.id);
	//员工名清空，页数选择回到第一页
	$("#userKey1").val("");
	$("#new_page").val(1);
	beforeAjax(1);
}