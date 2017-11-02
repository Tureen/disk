<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="webname" value="企业文件管理平台-授权系统" />
<c:set var="staticpath" value="${ctx}/static" />
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<LINK rel="Bookmark" href="${ctx}/favicon.ico" >
<LINK rel="Shortcut Icon" href="${ctx}/favicon.ico" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<link href="${plugins}/static/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${plugins}/static/h-ui/css/H-ui.login.css" rel="stylesheet" type="text/css" />
<link href="${plugins}/static/h-ui/css/style.css" rel="stylesheet" type="text/css" />
<link href="${plugins}/lib/Hui-iconfont/1.0.7/iconfont.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.error_yz{width:100px;height: 28px;line-height: 28px;border-radius: 20px;padding: 0 25px;background: #303030 url(../images/error_yz.png) no-repeat 10px center;position: absolute;left: -160px;top:8px;z-index: 100;color:#fff;}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>${webname }</title>
</head>
<body>
<input type="hidden" id="TenantId" name="TenantId" value="" />
<div class="header"></div>
<div class="loginWraper">
  <div id="loginform" class="loginBox">
    <form class="form form-horizontal" id="loginForm" method="post">
      <div class="row cl">
        <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60d;</i></label>
        <div class="formControls col-xs-8">
          <input id="mobile" name="mobile" type="text" placeholder="账号" class="input-text size-L" class="{required:true,minlength:5,messages:{required:'请输入内容'}}" >
        </div>
      </div>
      <div class="row cl">
        <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
        <div class="formControls col-xs-8">
          <input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <div class="formControls col-xs-8 col-xs-offset-3">
          <input class="input-text size-L" type="text" placeholder="验证码" id="validateCode" name="validateCode" value="" style="width:150px;">
          <img id="imgss" onclick="imgchange()" src="${ctx}/check/image" title="点击换一张" class="regimg"/></a></div>
      </div>
      <div class="row cl">
        <div class="formControls col-xs-8 col-xs-offset-3">
          <input name="" type="submit" class="btn btn-success radius size-L" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
          <input name="" type="reset" class="btn btn-default radius size-L" value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
        </div>
      </div>
    </form>
  </div>
</div>
<div class="footer">Copyright 桃源企业文件管理平台-授权系统</div>
<script type="text/javascript" src="${plugins}/lib/layer/2.1/layer.js"></script> 
<script type="text/javascript" src="${plugins}/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${plugins}/static/h-ui/js/H-ui.js"></script> 
<script type="text/javascript" src="${staticpath }/js/login/login.js"></script>
<script type="text/javascript" src="${plugins}/lib/jquery.validation/1.14.0/jquery.validate.min.js"></script> 
<script type="text/javascript" src="${plugins}/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="${plugins}/lib/jquery.validation/1.14.0/messages_zh.min.js"></script> 
<script type="text/javascript">
$(function(){
	 $("#loginForm").validate({
		rules:{
			mobile:{
				required:true,
				minlength:4,
				maxlength:16
			},
			password:{
				required:true,
			},
			validateCode:{
				required:true,
			},
		},
		messages:{
			mobile:"请输入账号",
			password:"请输入密码",
			validateCode:"请输入验证码",
		},
		onkeyup:false,
		success:"valid",
		submitHandler:function(form){
			$.ajax({ url: basepath+"/login/checkLogin",
		        data:$("form").serialize(),
		        dataType:"json",
		        type: 'POST',
		        async: false, 
		        success: function(data){
			        if (data.code == 1000) {
			            window.location.href=basepath + "/home/index";
			        }else if(data.code == 2003){
			        	$("#password").parent().append("<label id=\"password-error\" class=\"error\" for=\"password\">"+data.codeInfo+"</label>");
			        	$("#password").addClass("error");
			            imgchange();//重新加载验证码
			            return false;
			        }else if(data.code == 2004){
			        	$("#mobile").parent().append("<label id=\"mobile-error\" class=\"error\" for=\"mobile\">"+data.codeInfo+"</label>");
			        	$("#mobile").addClass("error");
			            imgchange();//重新加载验证码
			            return false;
			        }else if(data.code == 2002){
			        	$("#validateCode").parent().append("<label id=\"validateCode-error\" class=\"error\" for=\"validateCode\">验证码不正确</label>");
			        	$("#validateCode").addClass("error");
			            imgchange();//重新加载验证码
			            return false;
			        }
		      }});
		}
	});
});	 
</script>
</body>
</html>