/**跳转登录页面***/
function gotoLogin(){
	 top.location.href= basepath + "/login/index";
}

/**退出登录***/
function outLogin(){
	 top.location.href= basepath + "/login/outlogin";
}

/***坚持返回码**/
function checkErrorCode(code){
	if(code == 2000){
		//服务器异常
		layer.msg('服务器数据异常',{icon: 5,time:1000});
	}else if(code == 2005){
		//登录失效
		gotoLogin();
		return false;
	} else if(code == 2006){
	    //无该权限
		layer.msg('权限不足',{icon: 5,time:1000});
		return false;
	} 
	return true;
}

/**跳转登录页面***/
function gotoLoginFail(){
	 window.location.href= basepath + "/common/sessionout";
}