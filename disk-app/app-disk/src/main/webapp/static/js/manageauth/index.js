$(function(){
	
	//权限说明
	$(".power_explain").live("hover",function(){
	    $(this).find(".power_box").stop().fadeToggle(300)
	});
	$(".user_share_tip").live("click",function(){
	    $(this).next(".dropbox").stop().fadeToggle(300);
	    $(".tab_3 li").removeClass("on");
		$(this).parent().find(".tab_3 li:first").addClass("on");
        $(this).next().find(".tab_3_con").attr("style","display:none");
	    $(this).next().find(".tab_3_con :first").attr("style","display:inline");
	});
	
	$(".select_down p,.topmenu_down p").live("click",function(e){
       $(".select_down,.topmenu_down").removeClass("active");
       $(this).parent().addClass("active");
       $(this).next("div").slideDown();
       e.stopPropagation();//阻止冒泡程序
    });
	
    $(".select_down div p,.topmenu_down div p").live("click",function(e){
        var _this = $(this);
        //$(".select_down > p").text(_this.attr("date-value"));
        _this.parent('div').siblings('p').text(_this.attr("date-value"));
        $(".select_down p,.topmenu_down p").next("div").slideUp();
        _this.addClass("selected").siblings().removeClass("selected");
        $(".select_down,.topmenu_down").removeClass("active");
        e.stopPropagation();//阻止冒泡程序
    });
    
    $(document).live("click",function(){
        $(".select_down,.topmenu_down").removeClass("active");
        $(".select_down > div,.topmenu_down > div").slideUp();
    });
	
    $(".layer_search").live('keyup',function(){
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
	 
	$(".tab_check").live('click',function(){
    	$(this).toggleClass("tab_checked");
    	//获取选中的对象
    	//判断是点击的部门还是员工
    	var data ;
    	if($(this).attr("name") == 'empCheck'){
    		data = $(this).parent().parent().parent().parent().parent().parent().parent();
    	} else {
      		data = $(this).parent().parent().parent().parent().parent().parent();
    	}
    	getSelectName(data);
	});
	
	$(".share_box2").click(function(){
		var count = $("#con_settingshare").find(".share_con").length;
		if(count < 3){
			//先获取已经存在的选项
			$("#con_settingshare").find(".share_con :last").after($("#hideDiv").html());
		} else {
			layer.msg(add_up_to_three_options,{icon: 5,time:2000});
		}
	});
	
	$(".tab_3  li").live('click',function(){
		$(".tab_3 li").removeClass("on");
		$(this).addClass("on");
		var index = $(".tab_3 li").index(this);
		$(".tab_3_con").attr("style","display:none");
		$(".tab_3_con").each(function(_index){
			 if(_index == index){
				 $(this).attr("style","display:inline");
			 }
		});
	});
	
	$(".remove").live('click',function(){
		var id = $(this).attr('for');
		var data = $(this).parent().parent().parent().prev();
		if(id.length == 0){
			//所有人
			 data.find("label[id='allCheck']").removeClass("tab_checked");
		} else {
			var kvalue = $(this).attr('kvalue');
			data.find("label[for='"+id+"']").removeClass("tab_checked");
		}
		$(this).parent().remove();
		getSelectName(data.parent());
	});
})

$(document).ready(function(){ 
		　　getInitSelectName();
});

function getSelectedData(){
	var datas = new Array();
	$("#con_settingshare").find(".share_con").each(function(){
		var checkClass = $(this).find("#allCheck").attr('class');
		var data = {all:false,employees:null,deptIds:null,workgroupIds:null};
		if(checkClass.indexOf('tab_checked') != -1){
			data.all = true;
		} else {
			//
		  var deptArray = new Array();
	 	  var empArray = new Array();
	 	  var workgroupArray = new Array();
	 	  $(this).find("label[name='deptCheck']").each(function(){
	 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	 		   		var id= $(this).attr("for");
	 		     	deptArray.push(id);
	 		   	 }
	 	    });
	 	    data.deptIds = deptArray;
	 	    $(this).find("label[name='empCheck']").each(function(){
	 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	 		   		var id= $(this).attr("for");
	 		   	    var deptId = $(this).attr("key-value");
	 		     	var json = {
			   			id : id,
			   			deptId : deptId
			   		}
	 		    	empArray.push(json);
	 		   	 }
	 	    });
	 	   data.employees = empArray;
	 	  $(this).find("label[name='workgroupCheck']").each(function(){
	 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	 		   		var id= $(this).attr("for");
	 		   		workgroupArray.push(id);
	 		   	 }
	 	    });
	 	    data.workgroupIds = workgroupArray;
		}
		data.openType = $("#openType").val();
		data.openId = $("#openId").val();
		data.data = $("#data").val();
		data.authType = $(this).find(".selected").attr('key-value');
		datas.push(data);
	});
	var json = {
	    "authHelpers": datas	
	}
	return json;
}


function getMoreSelectedData(){
	var datas = new Array();
	$(".managebox").each(function(){
		var checkClass = $(this).find("#allCheck").attr('class');
		var data = {all:false,employees:null,deptIds:null,workgroupIds:null};
		if(checkClass.indexOf('tab_checked') != -1){
			data.all = true;
		} else {
			//
		   var deptArray = new Array();
	 	   var empArray = new Array();
	 	   var workgroupArray = new Array();
	 	  $(this).find("label[name='deptCheck']").each(function(){
	 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	 		   		var id= $(this).attr("kvalue");
	 		     	deptArray.push(id);
	 		   	 }
	 	    });
	 	    data.deptIds = deptArray;
	 	   $(this).find("label[name='empCheck']").each(function(){
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
	 	  $(this).find("label[name='workgroupCheck']").each(function(){
	 		   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	 		   		var id= $(this).attr("for");
	 		   		workgroupArray.push(id);
	 		   	 }
	 	    });
	 	    data.workgroupIds = workgroupArray;
		}
		data.data = $("#data").val();
		data.authType = $(this).find("#authType").val();
		datas.push(data);
	});
	var json = {
		"authHelpers": datas	
	}
	return json;
}

function getSelectName(data){
	var deptNameArray = new Array();
	var deptIdArray = new Array();
	var empNameArray = new Array();
	var empIdArray = new Array();
	var workgroupNameArray = new Array();
	var workgroupIdArray = new Array();
	data.find("label[name='deptCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var deptName = $(this).next().html();
	   		deptNameArray.push(deptName);
	   		deptIdArray.push($(this).attr("for"));
	   	 }
    });
	data.find("label[name='empCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   	    var empName = $(this).next().html();
	    	empNameArray.push(empName);
	   	    empIdArray.push($(this).attr("for"));
	   	 }
    });
	data.find("label[name='workgroupCheck']").each(function(){
	   	 if($(this).attr('class').indexOf('tab_checked') != -1){
	   		var workgroupName = $(this).next().html();
	   		workgroupNameArray.push(workgroupName);
	   		workgroupIdArray.push($(this).attr("for"));
	   	 }
	});
	data.find(".tab_label_r").find("ul").html("");
	//全部人是否选中
	var allCheckObj = data.find("label[id='allCheck']");
	var allCheckHtml = allCheckObj.next().html();
	var deptNumber = 0;
	if(allCheckObj.attr('class').indexOf('tab_checked') != -1){
		data.find(".tab_label_r").find("ul").append("<li>"+allCheckHtml+"<i class='remove' for=''></i></li>");
		deptNumber += 1;
	}
	for(var index in deptNameArray){
		data.find(".tab_label_r").find("ul").append("<li>"+deptNameArray[index]+"<i class='remove' for="+deptIdArray[index]+"></i></li>");
	}
	for(var index in empNameArray){
		data.find(".tab_label_r").find("ul").append("<li>"+empNameArray[index]+"<i class='remove' for="+empIdArray[index]+"></i></li>");
	}
	for(var index in workgroupNameArray){
		data.find(".tab_label_r").find("ul").append("<li>"+workgroupNameArray[index]+"<i class='remove' for="+workgroupIdArray[index]+"></i></li>");
	}
	data.parent().find("#deptNumber").html(deptNumber + deptNameArray.length);
	data.parent().find("#empNumber").html(empNameArray.length);
	data.parent().find("#workgroupNumber").html(workgroupNameArray.length);
}

//获取默认勾选的部门和员工在右侧展示
function getInitSelectName(obj){
	$(".dropbox").each(function(){
		getSelectName($(this));
	});
}
