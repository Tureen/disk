var createCodeFlag = 1;

$(document).ready(function(){
	
	//文本框获取/失去焦点注册事件
	$("#login").focus(function(){
	 	$(".iconaccount").css("background","url("+ basepath +"/static/images/login_account_chose.png) no-repeat");
	 	$(".iconaccount").css("background-position","0px 19px");
	});
	$("#login").blur(function(){
		$(".iconaccount").css("background","url("+ basepath +"/static/images/login_account.png) no-repeat");
	 	$(".iconaccount").css("background-position","0px 19px");
	});
	$("#password").focus(function(){
		$(".iconpassword").css("background","url("+ basepath +"/static/images/login_pwd_chose.png) no-repeat");
	 	$(".iconpassword").css("background-position","0px 19px");
	});
	$("#password").blur(function(){
		$(".iconpassword").css("background","url("+ basepath +"/static/images/login_pwd.png) no-repeat");
	 	$(".iconpassword").css("background-position","0px 19px");
	});
	$("#captcha").focus(function(){
		$(".iconcaptcha").css("background","url("+ basepath +"/static/images/login_captcha_chose.png) no-repeat");
	 	$(".iconcaptcha").css("background-position","0px 19px");
	});
	$("#captcha").blur(function(){
		$(".iconcaptcha").css("background","url("+ basepath +"/static/images/login_captcha.png) no-repeat");
	 	$(".iconcaptcha").css("background-position","0px 19px");
	});
	
	//语言切换注册事件
	$(".current-language").hover(function(){
		$(".language-switch-dropdown").css("display","block");
		//left-16.5  top-7 根据language-switch-dropdown样式中左上边距定义
		$(".language-switch-dropdown").css("left",$(".language-switch-dropup").position().left);
		$(".language-switch-dropdown").css("top",$(".language-switch-dropup").position().top);
		
	});
	
	$(".language-switch-dropdown").hover(function(){},function(){
		$(".language-switch-dropdown").css("display","none");
	});
	
	//记住我注册事件
	$(".icon-checkbox").click(function(){
		if($("#remember_login").val() == -1){
			$(".icon-checkbox").addClass("checked");
			$("#remember_login").val(0);
			return;
		}
		if($("#remember_login").val() == 0) {
			$(".icon-checkbox").removeClass("checked");
			$("#remember_login").val(-1);
			return;
		}
	});

	$(".fx-tab").click(function(){
		var id = $(this).attr("id");
		if(id == "qrcodelogin" && !$(this).hasClass("active")){
			$("#passlogin").removeClass("active");
			$("#qrcodelogin").addClass("active");
			$("#passform").css("display","none");
			$("#qrcodeform").css("display","block");
			
			if(createCodeFlag == 1){
				createCodeFlag = 2;
				setTimeout(function(){
					refresh();
				}, 3000);
			}
			
		}
		if(id == "passlogin" && !$(this).hasClass("active")){
			$("#qrcodelogin").removeClass("active");
			$("#passlogin").addClass("active");
			$("#qrcodeform").css("display","none");
			$("#passform").css("display","block");
			
			createCodeFlag = 1;
			$(".tip-outdate:eq(0)").css("display","none");
			$(".tip-outdate:eq(1)").css("display","none");
			$("#scan-code").html("<img class=\"wait-gif\" src=\""+$("#scan-code").next().val()+"\">");
		}
		
	});
	
	//刷新
	$(".btn-refresh-qrcode").click(function(){
		refresh();
	});
});

function refresh(){
	//登录方式判断
	if(createCodeFlag == 1){
		return;
	}
	$(".tip-outdate:eq(0)").css("display","none");
	$(".tip-outdate:eq(1)").css("display","none");
	beginLongPolling();
}

function beginLongPolling(){
	$("#scan-code").html("");
	var qrcode = inputQrcode();
	$("#scan-code").qrcode({ 
	    render: "table", //table方式 
	    width: 200, //宽度 
	    height:200, //高度 
	    text: "http://m.vcloud/"+qrcode //任意内容 
	}); 
	//长轮询
	(function longPolling(status) {
		//登录方式判断
		if(createCodeFlag == 1){
			//将此轮询中断，将该二维码通知后台失效
			var stopData = {
					"qrcode" : qrcode
			}
			$.ajax({
				url: $("#apiurl").val()+"/qrcode/stopScanning",
				data: stopData,
				type:"POST",
				dataType: "jsonp"
			});
			return;
		}
		var data = {
				"qrcode" : qrcode,
				"nowstatus" : status
				}
		$.ajax({
			  url: $("#apiurl").val()+"/qrcode/scanning",
			  data: data,
			  type:"POST",
	          dataType: "jsonp",
	          jsonpCallback: 'jsonpCallback',
			  timeout: 5000,
			  error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (textStatus == "timeout") { // 请求超时
                    longPolling(); // 递归调用
                // 其他错误，如网络错误等
                } else { 
                	longPolling();
                }
              },
			  success: function(data,textStatus) {
                if (textStatus == "success") { // 请求成功
                	if(data.code==2025){
                		//二维码失效
                		$(".tip-outdate:eq(1)").css("display","none");
                		$(".tip-outdate:eq(0)").css("display","block");
                		layer.msg(i18n_ip_unlogin,{icon: 5,time:3000});
                		return;
                	}
                	if(data.result.effective=='1'){
                		if(data.result.status=='3'){
	                		//取消登录,前端页面变更为二维码失效，提示刷新
                			$(".tip-outdate:eq(0)").css("display","block");
                			$(".tip-outdate:eq(1)").css("display","none");
	                		//longPolling(3);
	                		return;
	                	}
                		//二维码失效
                		$(".tip-outdate:eq(1)").css("display","none");
                		$(".tip-outdate:eq(0)").css("display","block");
                	}else{
	                	if(data.result.status=='0'){
	                		//未登录
	                		longPolling(0);
	                	}else if(data.result.status=='1'){
	                		//正在登录,前端页面变更为手机已扫描
	                		$(".tip-outdate:eq(1)").css("display","block");
	                		longPolling(1);
	                	}else if(data.result.status=='2'){
	                		//已登录,跳转
	                		 $.ajax({ url: basepath+"/login/checkLoginQrcode?token="+data.result.token,
	                		       dataType:"json",
	                		       type: 'GET',
	                		       async: false, 
	                		       success: function(data){
	                		    	   	if (data.code != 1000){
	                		    	   		layer.msg(i18n_qrcode_invalid,{icon: 5,time:2000})
	                		    	   		setTimeout(function(){
	                							refresh();
	                						}, 3000);
	                		    	   	}
	                		    	   	else if (data.code == 1000) {
	                			        	window.location.href=basepath + "/home/index";
	                			        }
	                			}});
	                		//window.location.href=basepath+"/login/index?token="+data.result.token;
	                	}
                	}
                }
		     }
		});
    })();
}

function randomString(len) {
　　len = len || 32;
　　var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
　　var maxPos = $chars.length;
　　var pwd = '';
　　for (i = 0; i < len; i++) {
　　　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
　　}
　　return pwd;
}

function inputQrcode(){
	var qrcode = randomString(8);
	$("#qrcode").val(qrcode);
	return qrcode;
}

//账户实体
var user = new Object();
function createUser(account, password, captcha){
	this.user.account = account;
	this.user.password = password;
	this.user.captcha = captcha;
}

//登录
function login(){
	var accountVal = $('#login').val();
	var passwordVal = $('#password').val();
	var captchaVal = $('#captcha').val();
	this.createUser(accountVal, passwordVal, captchaVal);
	emptyValidate(this.user);
	

	 //获取是否选中的状态
	 var bool = false;
	 if($("#remember_login").val() != -1){
		 bool = true;
	 }
	 var account = $("#login").val();
	 var password = $("#password").val();
	 var code = $("#captcha").val();
	 
	 
	//创建实体
	this.createUser(account, password, code);
	//验证
	emptyValidate(this.user);
	 
	 var data = {mobile:account,validateCode:code,password:password,bool:bool};
	 $.ajax({ url: basepath+"/login/checkLogin",
       data:data,
       dataType:"json",
       type: 'POST',
       async: false, 
       success: function(data){
    	   	if (data.code != 1000){
	    	   	closePrompt();
	    	   	openCaptcha();
    	   	}
	        if (data.code == 1000) {
	        	//loginFileServer(data.result.identity, data.result.token ,bool);
	        	window.location.href=basepath + "/home/index";
	        } else if(data.code == 2014){
	        	//账户含有空格
	        	errorPromptAccount();
	            imgchange();//重新加载验证码
	            return false;
	        }else if(data.code == 2015){
	        	//密码有空格
	        	errorPromptAccount();
	            imgchange();//重新加载验证码
	            return false;
	        }
	        else if(data.code == 2002){
	        	//验证码后台对比错误
	        	errorPromptCaptcha();
	            imgchange();//重新加载验证码
	            return false;
	        }
	        else if(data.code == 2003){
	        	//密码错误
	        	errorPromptPassword();
	            imgchange();//重新加载验证码
	            return false;
	        } else if(data.code == 2004){
	        	frozenPromptAccount();
	            imgchange();//重新加载验证码
	            return false;
	        } else if(data.code == 2020){
	        	ipPromptBanned();
	            imgchange();//重新加载验证码
	            return false;
	        }
	}});
}

//验证码更新
function imgchange(){
    $("#imgss").attr("src",basepath+"/check/image?a="+Math.random()+100);
}

//非空验证
function emptyValidate(user){
	closePrompt();
	if(user.account == ''){
		emptyPromptAccount();
		return;
	}
	if(user.password == ''){
		emptyPromptPassword();
		return;
	}
	if(user.captcha == ''){
		emptyPromptCaptcha();
		return;
	} 
	$('.error-msg-account').css('display','none');
	$('.error-msg-password').css('display','none');
	$('.error-msg-captcha').css('display','none');
}

//隐藏所有提示
function closePrompt(){
	$('.error-msg-account').css('display','none');
	$('.error-msg-password').css('display','none');
	$('.error-msg-captcha').css('display','none');
}

//显示验证码
function openCaptcha(){
	$(".pic-captcha").css("display","block");
}

//空用户名提示
function emptyPromptAccount(){
	$('.error-msg-account').html(username_error_fourth);
	$('.error-msg-account').css('display','inline-block');
}

//空密码提示
function emptyPromptPassword(){
	$('.error-msg-password').html(password_error_third);
	$('.error-msg-password').css('display','inline-block');
}

//空验证码提示
function emptyPromptCaptcha(){
	$("#captcha").focus();
	$('.error-msg-captcha').html(verification_code_error);
	$('.error-msg-captcha').css('display','inline-block');
}


//错误用户名提示
function errorPromptAccount(){
	$('.error-msg-password').html(password_error);
	$('.error-msg-password').css('display','inline-block');
}

//错误密码提示
function errorPromptPassword(){
	$('.error-msg-password').html(password_error);
	$('.error-msg-password').css('display','inline-block');
}

//错误验证码提示
function errorPromptCaptcha(){
	$("#captcha").focus();
	$('.error-msg-captcha').html(verification_code_error);
	$('.error-msg-captcha').css('display','inline-block');
}

//账户被冻结
function frozenPromptAccount(){
	$('.error-msg-account').html(username_error);
	$('.error-msg-account').css('display','inline-block');
}

//ip禁止登陆
function ipPromptBanned(){
	$('.error-msg-account').html(username_error_second);
	$('.error-msg-account').css('display','inline-block');
}

//提取码搜索
function pickTakeCode(){
	$("#queryForm").submit();
}
