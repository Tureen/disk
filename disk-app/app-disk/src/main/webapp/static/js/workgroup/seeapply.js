var flag = true;
function addWorkgroupUser(){
	if(!flag){
		layer.msg(i18n_workgroup_please_do_not_submit,{icon: 5,time:1500});
		return;
	}
	var idsArray = new Array();
	$(".add_employee").each(function(){
		idsArray.push($(this).attr("code"));
	});
	var workgroupId = $("#workgroupId").val();
	$.ajax({
 		 url: basepath+"/workgroup/saveloaduser?workgroupId="+workgroupId,
        dataType:"json",
        type: 'POST',
        data: JSON.stringify(idsArray),
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
	        	//区分，点击查询,翻页等功能时，不改变已操作的选择用户
	        	if(type!=1){
	        		//清空选择用户
	        		$(".add_div").html("");
		        	//已加入的工作组员工
		        	var employeeAddObjs = $("#employeeAddObjs").val();
		        	var employeeAddArray = eval("("+employeeAddObjs+")");
		        	if(employeeAddArray!=null && employeeAddArray.length > 0){
		        		for (var i=0;i<employeeAddArray.length;i++){
		        			var employeeAddObj = employeeAddArray[i];
		        			var addText = "<li class=\"add_employee\" code=\""+employeeAddObj.employeeId+"\"><div class=\"column column_id\">"+employeeAddObj.employeeId+"&nbsp;</div><div class=\"column column_name\">"+employeeAddObj.employeeName+"&nbsp;</div><div class=\"column column_department\" title=\""+employeeAddObj.deptName+"\">"+employeeAddObj.deptName+"&nbsp;</div><div class=\"column column_email\">"+employeeAddObj.employeeEmail+"&nbsp;</div></li>"
		        			$(".add_div").append(addText);
		        			//上框，选中标注
		        			var workgroupObj = $(".show_div").find("li[code='"+employeeAddObj.employeeId+"']");
		        			if($(workgroupObj).attr("code")!=undefined){
			        			$(workgroupObj).addClass("is_add");
		        			}
		        		}
		        	}
	        	}else{
	        		$(".add_div").find(".add_employee").each(function(){
	        			var workgroupObj = $(".show_div").find("li[code='"+$(this).attr("code")+"']");
		        		if($(workgroupObj).attr("code")!=undefined){
		        			$(workgroupObj).addClass("is_add");
	        			}
	        		});
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
	
	//序号排序
	var i = 1;
    $(".add_div .column_id").each(function(){
    	$(this).text(i++);
    });
});

