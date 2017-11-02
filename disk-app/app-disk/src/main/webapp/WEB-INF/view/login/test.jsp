<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="webname" value="<%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.TITLE.getKey()) %>" />
<c:set var="staticpath" value="${ctx}/static" />
<html lang="en">
<head>
<meta charset="UTF-8">
<title>${webname }</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script type="text/javascript">var basepath ='${ctx}';</script>
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${plugins}/common/jquery/jquery.qrcode.min.js"></script>
<script type="text/javascript">
var refreshIndex = 1;//刷新标识

$(function(){
	setTimeout(function(){
		refresh();
	}, 3000);
});

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

function refresh(){
	if(refreshIndex == 1){
		$("#onescaning").css("display","none");
		$("#noqrcode").css("display","none");
		$("#refresh").css("display","none");
		beginLongPolling();
	}
}

function beginLongPolling(){
	refreshIndex = 0;
	$("#code").html("");
	var qrcode = inputQrcode();
	$("#code").qrcode({ 
	    render: "table", //table方式 
	    width: 140, //宽度 
	    height:140, //高度 
	    text: "http://m.vcloud/"+qrcode //任意内容 
	}); 
	//长轮询
	(function longPolling(status) {
		var data = {
				"timed"   : new Date().getTime(),
				"qrcode" : qrcode,
				"nowstatus" : status
				}
		$.ajax({
			  url: "http://192.168.11.71:8088/api/qrcode/scanning",
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
                	if(data.result.effective=='1'){
                		if(data.result.status=='3'){
	                		//取消登录,前端页面变更为二维码失效，提示刷新
	                		$("#onescaning").css("display","none");
                			$("#noqrcode").css("display","block");
	                		$("#refresh").css("display","block");
	                		//longPolling(3);
	                		refreshIndex = 1;
	                		return;
	                	}
                		//二维码失效
                		$("#noqrcode").css("display","block");
                		$("#refresh").css("display","block");
                		refreshIndex = 1;
                	}else{
	                	if(data.result.status=='0'){
	                		//未登录
	                		longPolling(0);
	                	}else if(data.result.status=='1'){
	                		//正在登录,前端页面变更为手机已扫描
	                		$("#onescaning").css("display","block");
	                		longPolling(1);
	                	}else if(data.result.status=='2'){
	                		//已登录,跳转
	                		window.location.href=basepath+"/login/index?token="+data.result.token;
	                	}
                	}
                }
		     }
		});
    })();
}

function inputQrcode(){
	var qrcode = randomString(8);
	$("#qrcode").val(qrcode);
	return qrcode;
}
</script>
</head>
<body>
<input id="qrcode" type="hidden" value="">
<div id="code"><img src="${staticpath}/images/buffer.gif"></div> 
<div id="onescaning" style="display: none">手机已扫描，等待登录</div>
<div id="noqrcode" style="display: none">二维码失效</div>
<div id="refresh" style="display: none"><input type="button" onclick="refresh()" value="刷新" style="position:absolute;top:50%;left:50%;margin-left:-21px;margin-top:-10.5px;opacity:1"></div>
</body>
</html>
