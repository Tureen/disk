<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript" src="${staticpath}/js/login/index.js?v=20170221"></script>
<script type="text/javascript" src="${plugins}/common/jquery/jquery.qrcode.min.js"></script>
<style>
body {
	background: #f9fafc;
	font-size: 12px;
	line-height: 1.4;
	font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft YaHei", 微软雅黑,
		微软雅黑, "WenQuanYi Micro Hei", 宋体, sans-serif;
	display: block;
}

body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td
	{
	margin: 0;
	padding: 0;
}

.center-logo {
	min-width: 1024px;
	padding: 60px 0 15px;
	text-align: center;
}

a {
	color: #079cda;
	text-decoration: none;
	outline: none;
}

img {
	vertical-align: middle;
}

.external-language-switch {
	width: 1024px;
	margin: 0 auto;
}

.language-switch.dropdown>a {
	line-height: 70px;
}

.language-switch.dropdown a {
	color: #404040;
}

.language-switch .language-switch-dropdown {
	text-align: center;
	border: 1px solid #e7e7e7;
	width: 100px;
	background-color: #FFFFFF;
}

.language-switch .language-switch-dropup {
	text-align: center;
	border: 1px solid #f9fafc;
	width: 100px;
	background-color: #f9fafc;
	float: right;
}

.language-switch .language-switch-dropup li {
	padding: 5px 0;
	background:url(${staticpath}/images/ac_down.png) no-repeat 80px 6px;
}

.language-switch .language-switch-dropdown .first_up{
	background:url(${staticpath}/images/ac_down.png) no-repeat 80px 6px;
}

ol,ul {
	list-style: none;
}

.external-language-switch .language-switch {
	width: 1200px;
	float: right;
	margin-top: -40px;
	font-size: 14px;
	text-align: right;
}

.language-switch .language-switch-dropdown li {
	padding: 5px 0;
}

li {
	list-style: none;
}

.language-switch .iconfont { /* font-size: 11px; */
	margin-left: 5px;
}

.login-box {
	width: 1024px;
	margin: 0 auto;
	padding: 10px 0 50px;
	background-color: #fff;
	position: relative;
}

.login-box .form {
	width: 400px;
	margin: 0 auto;
	font-size: 14px;
}

.login-box {
	font-size: 14px;
	margin-bottom: 0;
}

.form .form-control-group {
	margin-bottom: 15px;
}

.form-control-group .input-group {
	position: relative;
	display: table;
}

.form-control-group .input-group .input-group-addon {
	padding-right: 14px;
}

.form-control-group .input-group .input-group-addon {
	width: 1%;
	display: table-cell;
	white-space: nowrap;
	vertical-align: middle;
	box-sizing: border-box;
}

.input-group .iconaccount {
	background: url(${staticpath}/images/login_account.png) no-repeat;
	background-position: 0px 19px;
}

.input-group .iconpassword {
	background: url(${staticpath}/images/login_pwd.png) no-repeat;
	background-position: 0px 19px;
}

.input-group .iconcaptcha {
	background: url(${staticpath}/images/login_captcha.png) no-repeat;
	background-position: 0px 19px;
}

.current-language .iconfont {
	background: url(${staticpath}/images/drop-down.png) no-repeat;
	width: 100px;
}

.login-box .login-group .input {
	-webkit-transition: all 400ms;
	transition: all 400ms;
}

.form .form-control-group .input {
	height: 54px;
	padding-top: 16px;
	padding-bottom: 16px;
	line-height: 22px;
}

.form-control-group .input-group .input,.form-control-group .input-group .select
	{
	position: relative;
	width: 100%;
	display: table-cell;
	float: left;
	box-sizing: border-box;
}

.form .form-control-group .input,.form .form-control-group .select {
	border: 1px solid #d0d0d0;
	display: inline-block;
	vertical-align: middle;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	background: #fff;
}

.form .form-control-group input {
	line-height: 1.4;
}

.form input {
	color: #000;
}

input,select,textarea {
	border: none;
}

input,textarea,select {
	font-family: inherit;
	font-size: inherit;
	font-weight: inherit;
	resize: none;
	outline: none;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

.login-box .form .login-other {
	margin: 15px 0 30px;
	overflow: hidden;
}

.login-box .form .login-other .remember-login {
	float: left;
}

.icon-checkbox {
	background-position: 0 -450px;
}

.icon {
	background-image: url(${staticpath}/images/icons_all.png);
	width: 25px;
	height: 25px;
}

.icon,.icon-type-s,.icon-type-m,.icon-type-l {
	display: inline-block;
	text-align: center;
	vertical-align: middle;
	background-repeat: no-repeat;
}

.login-box .form .login-other .forgot-pswd {
	float: right;
	font-size: 14px;
	margin-top: 2px;
	color: #787878;
}

.login-box .form .pure-button {
	width: 100%;
	height: 38px;
	line-height: 38px;
	font-size: 16px;
}

.button-primary,.button-selected,.locked-button-primary,a.button-primary,a.button-selected
	{
	background-color: #079cda;
	color: #fff;
	border-color: #0589c0;
}

.pure-button {
	display: inline-block;
	white-space: nowrap;
	vertical-align: baseline;
	text-align: center;
	cursor: pointer;
	font-family: inherit;
	padding: 0 20px;
	border: 1px solid #d9d9d9;
	border-radius: 2px;
	-webkit-user-drag: none;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	outline: none;
}

.footer {
	min-width: 1024px;
	text-align: center;
	margin: 25px 0 50px;
	color: #999;
}

.footer .copyright,.footer .certificate {
	display: inline-block;
}

.icon-checkbox.checked {
	background-position: -25px -450px;
}

.form .form-control-group .input-border-bottom .input,.form .form-control-group .select-border-bottom .select
	{
	border: 0;
}

.form .form-control-group .input-border-bottom,.form .form-control-group .select-border-bottom
	{
	border-width: 0 0 1px;
	border-style: solid;
	border-color: #e7e7e7;
}

.login-box {
	font-size: 14px;
	margin-bottom: 0;
}

.error-msg-account {
	font-size: 14px;
	margin-bottom: 0;
	color: #f00;
	margin-top: 6px;
	display: none;
}

.error-msg-password {
	font-size: 14px;
	margin-bottom: 0;
	color: #f00;
	margin-top: 6px;
	display: none;
}

.error-msg-captcha {
	font-size: 14px;
	margin-bottom: 0;
	color: #f00;
	margin-top: 6px;
	display: none;
}

.language-switch-dropdown {
	display: none;
}

.login_searchbox {
	width: 200px;
	height: 34px;
	line-height: 34px;
	border-bottom: 1px solid #c9ced0;
	border-left:0px;
	border-right:0px;
	border-top:0px;
	background: url(${staticpath}/images/login_searchtip.png) no-repeat
		170px center;
	margin-top: 20px;
	float:right;
}

.search_text {
	width: 165px;
	background: transparent;
	height: 34px;
	line-height: 34px;
	color: #272727;
	margin-left: 3px;
	float: left;
	display: block;
}

.login_searchtip {
	display: inline-block;
	width: 30px;
	cursor: pointer;
	height: 34px;
}
.code_search{
	width:1024px;
	margin: 0 auto;
}

.title_font{
	font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft YaHei", 微软雅黑, 微软雅黑, "WenQuanYi Micro Hei", 宋体, sans-serif;
	font-size: 16px;
    line-height: 24px;
	color: #999;
}
.fx-tab{
	cursor: pointer;
}
.btn-refresh-qrcode{
	cursor: pointer;
}
.j-phone{
	width: 45px;
	opacity: 1;
	margin-top: 5px;
}
.j-qrcode-status-text{
	font-weight: bolder;
}
.wait-gif{
	position: absolute;
	top: 50%;
	left: 50%;
	margin-left: -50px;
	margin-top: -50px;
}
</style>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="applicable-device" content="pc,mobile">
<title>登录 - vcloud</title>
</head>
<body class="i18n-zh-CN">
	<input id="apiurl" type="hidden" value="<%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.SYSTEMCODE, com.yunip.constant.SystemContant.SYSTEM_API_DOMAIN)%>">
	<div class="code_search"> 
		<form action="${ctx}/takecode/pick" method="post" id="queryForm">
	        	<div class="login_searchbox"><input name="takeCode" class="search_text" type="text" value="" placeholder="<fmt:message key="take_code_prompt" bundle="${i18n}"/>"/><a class="login_searchtip" href="javascript:void(0);" onclick="pickTakeCode()"></a></div>
	    </form>
    </div>
	<div class="center-logo">
		<a href="" title="vcloud"> <img src="${staticpath}/images/logo_login.png" alt="vcloud">
		</a>
		<br/>
		<span class="title_font"></span>
	</div>
	<div class="external-language-switch">
	<c:if test="${localvar=='zh_CN' }">
		<div class="language-switch dropdown ">
			<ul class="language-switch-dropup">
				<li>
				<a href="#" data-sort-by="zh-CN" class="current-language" >简体中文<i
				class="iconfont icon-arrow2"></i></a>
				</li>
			</ul>
			<ul class="language-switch-dropdown"
				style="position: absolute; z-index: 99;  ">
				<li class="first_up"><a href="#" data-by="zh-CN" onclick="setAjaxCookie('zh_CN')">简体中文<i
						class="iconfont icon-single-arrow-up"></i></a></li>
				<li><a href="#" data-by="en" onclick="setAjaxCookie('en_US')">English<i
						class="iconfont icon-single-arrow-up"></i></a></li>
			</ul>
		</div>
	</c:if>
	<c:if test="${localvar=='en_US' }">
		<div class="language-switch dropdown ">
			<ul class="language-switch-dropup">
			<li>
			<a href="#" data-sort-by="zh-CN" class="current-language" >English<i
				class="iconfont icon-arrow2"></i></a>
			</li>
			</ul>
			<ul class="language-switch-dropdown "
				style="position: absolute; z-index: 99;">
				<li class="first_up"><a href="#" data-by="en" onclick="setAjaxCookie('en_US')">English<i
						class="iconfont icon-single-arrow-up"></i></a></li>
				<li><a href="#" data-by="zh-CN" onclick="setAjaxCookie('zh_CN')">简体中文<i
						class="iconfont icon-single-arrow-up"></i></a></li>
			</ul>
		</div>
	</c:if>
		
	</div>
	<div class="login-tabs-box">
		<div class="fx-tabs login-tabs">
			<div id="passlogin" class="fx-tab active"><fmt:message key="i18n_qrcode_account_password_login" bundle="${i18n}"/></div>
			<div id="qrcodelogin" class="fx-tab "><fmt:message key="i18n_qrcode_quick_logon" bundle="${i18n}"/></div>
		</div>
	</div>
	<div class="login-box">
		<form id="passform" action="javascript:login();" method="post"
			accept-charset="utf-8" class="form" >
			<div style="display: none">
				<input type="hidden" name="requesttoken"
					value="a18b9912a5b37578afa1523179d9bd9a" />
			</div>
			<input type="hidden" value="" id="api_key"> <input
				type="hidden" value="" id="real_lang">
			<ul class="input-box">
				<li class="login-group country-phone form-control-group">
					<div class="input-group">
						<i class="iconaccount input-group-addon input-border-bottom">&nbsp;&nbsp;&nbsp;</i>
						<input type="text" id="login" name="identifier"
							class="input input-border-bottom" data-role="phone" value=""
							placeholder="<fmt:message key="username_prompt" bundle="${i18n}"/>" aria-label="username_prompt">
					</div> <span class="error-msg-account" id="account-error"><fmt:message key="username_prompt" bundle="${i18n}"/></span>
				</li>
				<li class="form-control-group">
					<div class="input-group input-border-bottom">
						<i class="iconpassword input-group-addon">&nbsp;&nbsp;&nbsp;</i> <input
							type="password" id="password" name="password" class="input"
							placeholder="<fmt:message key="password_prompt" bundle="${i18n}"/>" aria-label="<fmt:message key="password_prompt" bundle="${i18n}"/>">
					</div> <span class="error-msg-password" id="password-error"><fmt:message key="password_prompt" bundle="${i18n}"/></span>
				</li>
				<li class="form-control-group pic-captcha" style="<c:if test="${empty sessionScope.attemptlogin }">display: none;</c:if>"><div
						class="input-group input-border-bottom">
						<i class="iconcaptcha input-group-addon"
							data-iconfont="identifying code1">&nbsp;&nbsp;&nbsp;</i><input
							name="pic_captcha_code" id="captcha" class="input" type="text"
							maxlength="4" data-role="captcha" placeholder="<fmt:message key="verification_code_prompt_second" bundle="${i18n}"/>"><i
							class="input-group-addon" data-role="get_captcha_box">
							<img id="imgss" onclick="imgchange()" src="${ctx}/check/image" title="<fmt:message key="verification_code_prompt_second" bundle="${i18n}"/>" />
							</i>
					</div> <span class="error-msg-captcha" id="captcha-error"><fmt:message key="verification_code_prompt_second" bundle="${i18n}"/></span>
				</li>
			</ul>
			<div class="login-other">
				<div class="remember-login">
					<i class="icon icon-checkbox"></i><label><fmt:message key="remember_login" bundle="${i18n}"/></label> <input
						type="hidden" name="remember_login" id="remember_login" value="-1"
						autocomplete="off">
				</div>
			</div>
			<button class="pure-button button-primary" id="login_btn"
				aria-label="<fmt:message key="login_button" bundle="${i18n}"/>"><fmt:message key="login_button" bundle="${i18n}"/></button>
		</form>
		
		<form id="qrcodeform" style="display: none">
			<div class="j-login-fast-wrap login-wrap login-fast-wrap active">
				<div class="qrcode-wrap">
					<div class="j-qrcode-img-box qrcode-img-box">

						<div class="fx-loading j-loading hide"></div>
						<div id="scan-code"><img class="wait-gif" src="${staticpath}/images/buffer.gif"></div>
						<input type="hidden" value="${staticpath}/images/buffer.gif">
						<%-- <img class="qrcode-img" alt="二维码"
							src="${staticpath}/images/scan_code.png"> --%>
						<div class="tip-outdate" style="display: none;">
							<div class="tip-outdate-mask"></div>
							<div class="tip-outdate-operate">
								<p class="j-qrcode-status-text"><fmt:message key="i18n_qrcode_expire" bundle="${i18n}"/></p>
								<a class="btn-refresh-qrcode"><fmt:message key="i18n_qrcode_refresh" bundle="${i18n}"/></a>
							</div>
						</div>
						<div class="tip-outdate" style="display: none;">
							<div class="tip-outdate-mask"></div>
							<div class="tip-outdate-operate">
								<p class="j-qrcode-status-text"><fmt:message key="i18n_qrcode_logining" bundle="${i18n}"/></p>
								<img class="j-phone" alt="<fmt:message key="i18n_qrcode" bundle="${i18n}"/>" src="${staticpath}/images/phone.jpg">
							</div>
						</div>
					</div>
					<div class="tip-qrcode-importance">
						<fmt:message key="i18n_qrcode_sweep" bundle="${i18n}"/>
						<div class="line line-l"></div>
						<div class="line line-r"></div>
					</div>
					<div class="tip-qrcode-scan">
						<fmt:message key="i18n_qrcode_use" bundle="${i18n}"/><span class="light btn-dl-app"><fmt:message key="i18n_qrcode_phone" bundle="${i18n}"/></span><fmt:message key="i18n_qrcode_scan" bundle="${i18n}"/>&nbsp;&nbsp;<span
							class="icon-scan icon-q"></span>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="footer">
		<div class="copyright">
			<c:if test="${localvar=='zh_CN'}"><%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.WEB_RECORD.getKey()) %></c:if>
			<c:if test="${localvar=='en_US'}"><%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.WEB_RECORD_ENGLISH.getKey()) %></c:if>
		</div>
	</div>
	
</body>
</html>
