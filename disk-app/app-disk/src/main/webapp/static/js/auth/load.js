$(function(){
	$(".layer_search").keyup(function(){
		$('.tab_searchbox').find('p').show();
		$('.tab_searchbox').find('li').show();
		 var searchName = $(this).val();
		 if(searchName == ''){
			 return ;
		 }
		 $('.tab_searchbox').find('ul').each(function(){
			 var bool = true;
		     $(this).find('span').each(function(){
				 var empName = $(this).html();
				 if(empName.indexOf(searchName) == -1){
					// 不包含
					$(this).parent().hide();
				 } else {
					 bool = false; 
				 }
		     });
		     //检测下当前ul下的所有节点是否全部为display为none
		    if(bool){
		    	$(this).prev().hide();
		    }
		 });
	 });
	 
	$(".tab_check").click(function(){
    	$(this).toggleClass("tab_checked");
    	//获取选中的对象
    	getSelectName();
	});
	
	//获取选中名称
	getSelectName();
})

function getSelectedData(){
	var checkClass = $("#allCheck").attr('class');
	var data = {all:false,employees:null,deptIds:null};
	if(checkClass.indexOf('tab_checked') != -1){
		data.all = true;
	} else {
		//
	   var deptArray = new Array();
 	   var empArray = new Array();
 	   $("label[name='deptCheck']").each(function(){
 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
 		   		var id= $(this).attr("kvalue");
 		     	deptArray.push(id);
 		   	 }
 	    });
 	    data.deptIds = deptArray;
 	    $("label[name='empCheck']").each(function(){
 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
 		   		var id= $(this).attr("kvalue");
 		   	    var deptId = $(this).next().val();
 		     	var json = {
		   			id : id,
		   			deptId : deptId
		   		}
 		    	empArray.push(json);
 		   	 }
 	    });
 	   data.employees = empArray;
	}
	data.openType = $("#openType").val();
	data.openId = $("#openId").val();
	data.authType = $("#authType").val();
	return data;
}


function getMoreSelectedData(){
	var checkClass = $("#allCheck").attr('class');
	var data = {all:false,employees:null,deptIds:null};
	if(checkClass.indexOf('tab_checked') != -1){
		data.all = true;
	} else {
		//
	   var deptArray = new Array();
 	   var empArray = new Array();
 	   $("label[name='deptCheck']").each(function(){
 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
 		   		var id= $(this).attr("kvalue");
 		     	deptArray.push(id);
 		   	 }
 	    });
 	    data.deptIds = deptArray;
 	    $("label[name='empCheck']").each(function(){
 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
 		   		var id= $(this).attr("kvalue");
 		   	    var deptId = $(this).next().val();
 		     	var json = {
		   			id : id,
		   			deptId : deptId
		   		}
 		    	empArray.push(json);
 		   	 }
 	    });
 	   data.employees = empArray;
	}
	data.data = $("#data").val();
	data.authType = $("#authType").val();
	return data;
}

function getSelectName(){
	var nameArray = new Array();
	var checkClass = $("#allCheck").attr('class');
	if(checkClass.indexOf('tab_checked') != -1){
		nameArray.push($("#allCheck").next().html());
	}
	$("label[name='deptCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var deptName = $(this).next().html();
	   		nameArray.push(deptName);
	   	 }
    });
	$("label[name='empCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   	    var empName = $(this).next().next().html();
	    	nameArray.push(empName);
	   	 }
    });
	var name = nameArray.join("、");
	$("#shareName").html(name);
	
}
