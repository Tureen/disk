$(function(){
	/**
	 * 登录按钮
	 */
	$("#loginbtn").live("click", function(){
		checkLogin();
	});
	 
   /**
    * 输入用户名/密码
    */
   $("#login_user, #login_password, #login_yzm").keydown(function(e){
	   if(e.keyCode == 13){
		   checkLogin();
	   }
	});
   
   /**
    * 清除输入框提示
    */
   $('.clear_tishi').live("focus",function(){
	   $(this).parent().removeClass("is-invalid");
	   $("#login_result").text("").css("visibility", "hidden");
   })
   
   
   initKeepPasswordUserInfo();
})

/**
 * 获得浏览器cookie
 * @param name cookie key的名字
 */
function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr = document.cookie.match(reg)){
		return unescape(arr[2]);
	}else{
		return null;
	}
}

/**
 * 获取写入cookie中当前用户的信息
 * @returns
 */
function getUserJsonInfo(){
	var tempText = getCookie("adminPCClient");
	if(tempText != null && tempText != ""){
		tempText = tempText.replace(/\\/g,"");
		return JSON.parse(tempText.substring(1, tempText.length - 1));
	}else{
		return null;
	}
}

/**
 * 初始化登录记住密码的帐号
 */
function initKeepPasswordUserInfo(){
   var currentUser = getUserJsonInfo();
   if(currentUser != null && currentUser != ""){
	   	$("#login_user").val(currentUser.mobile);
	   	$("#login_password").val("********");
	   	$("#keepPassword").attr("checked", true)
   }
}

/**
 * 重新加载验证码
 */
function imgchange(){
    $("#imgss").attr("src", basepath + "/check/image?rd=" + Math.random() + 100);
}

/**
 * 登录请求验证
 */
function checkLogin(){
	//var bool = true;//默认记住密码
	var bool = $("#keepPassword").is(':checked');//默认记住密码
	var mobile = $("#login_user").val();
	var password = $("#login_password").val();
	var code = $("#login_yzm").val();
	if(mobile == ''){
		$("#login_user").parent().addClass("is-invalid is-dirty").find(".mdl-textfield__error").text(i18n_pclogin_input_user_name);
		imgchange();//重新加载验证码
		return;
	}
	if(password == ''){
		$("#login_password").parent().addClass("is-invalid is-dirty").find(".mdl-textfield__error").text(i18n_pclogin_input_password);
		imgchange();//重新加载验证码
		return;
	}
	var data = {mobile:mobile,validateCode:code,password:password,bool:bool}
	if(password == "********"){
		$.ajax({ 
			url: basepath + "/login/keepPasswordLogin",
			data:data,
			dataType:"json",
			type: 'POST',
			async: true, 
			success: function(data){
			if (data.code == 1000) {
				window.location.href=basepath + "/home/index";
			}else{
				if(data.code == 2003){
					$("#login_password").val("");
				}
				$("#login_result").text(data.codeInfo).css("visibility", "visible");
				$("#login_result").show();
				$("#showCode").show();
				imgchange();//重新加载验证码
				return false;
			}
		}});
	}else{
		$.ajax({ 
			url: basepath + "/login/checkLogin",
			data:data,
			dataType:"json",
			type: 'POST',
			async: true, 
			success: function(data){
			if (data.code == 1000) {
				window.location.href=basepath + "/home/index?isPcLogin=true";
			}else{
				if(data.code == 2003){
					$("#login_password").val("");
				}
				$("#login_result").text(data.codeInfo).css("visibility", "visible");
				$("#login_result").show();
				$("#showCode").show();
				imgchange();//重新加载验证码
				return false;
			}
		}});
	}
}