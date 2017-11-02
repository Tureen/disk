<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<c:if test="${isPcLoginOut }">
<script type="text/javascript">
	console.log("loginOut");
	var ipcRenderer = require('electron').ipcRenderer;
	ipcRenderer.on('ping', function() {
		ipcRenderer.sendToHost("用户退出登录！");
	});
</script>
</c:if>
<script src="${staticpath}/js/pclogin/index.js?v=20161114"></script>
<link href="${staticpath}/css/material.min.css" rel="stylesheet" type="text/css" />
<style type="text/css">
html {
    overflow: hidden;
     font-family: Microsoft Yahei;
}

#loginform {
    margin-top: 2%;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    flex: 0;
    align-items: center;
    justify-content: center;
    align-content: center;
}

#text {
    margin-top: 3%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.Grid-cell {
    flex: 1;
}

.Grid-cell.u-full {
    flex: 0 0 100%;
}
</style>
<body>
    <div id='main'>
        <div id='loginform' >
            <img src='${staticpath}/images/pclogin-icon.png' />
            <div id='text'>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input clear_tishi" type="text" id="login_user" />
                    <label class="mdl-textfield__label" for="login_user"><fmt:message key="i18n_pclogin_login_name" bundle="${i18n}"/></label>
                    <span class="mdl-textfield__error"></span>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input clear_tishi" type="password" id="login_password" />
                    <label class="mdl-textfield__label" for="login_password"><fmt:message key="i18n_pclogin_password" bundle="${i18n}"/></label>
                    <span class="mdl-textfield__error"></span>
                </div>
                <div id="showCode" class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style='<c:if test="${empty sessionScope.attemptlogin }">display: none;</c:if>'>
                    <input class="mdl-textfield__input clear_tishi" type="text" id="login_yzm" style="width: 58%;display:inline;" />
                    <label class="mdl-textfield__label" for="login_yzm"><fmt:message key="i18n_pclogin_validate_code" bundle="${i18n}"/></label>
                    <img id="imgss" onclick="imgchange()" src="${ctx}/check/image" title="<fmt:message key="verification_code_prompt_second" bundle="${i18n}"/>" class="regimg"/>
                    <span class="mdl-textfield__error"></span>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <label class="mdl-switch mdl-js-switch mdl-js-ripple-effect" for="keepPassword">
					  <input type="checkbox" id="keepPassword" class="mdl-switch__input" />
					  <span class="mdl-switch__label"><fmt:message key="i18n_pclogin_remember_password" bundle="${i18n}"/></span>
					</label>
                </div>
            </div>
            <div>
            	<span class="mdl-textfield__error" id="login_result"></span>
                <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent Grid-cell u-full" id='loginbtn' style='width: 300px;margin-top:8%'><fmt:message key="login" bundle="${i18n}"/></button>
            </div>
        </div>
    </div>
    <div id="demo-toast-example" class="mdl-js-snackbar mdl-snackbar">
        <div class="mdl-snackbar__text"></div>
        <button class="mdl-snackbar__action" type="button"></button>
    </div>
</body>
<script src="${staticpath}/js/pclogin/material.min.js"></script>
</html>