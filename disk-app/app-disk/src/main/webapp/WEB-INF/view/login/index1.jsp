<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript" src="${staticpath}/js/login/index.js"></script>

<!-- <script type="text/javascript">
    $(function () {
        (function longPolling() {
            $.ajax({
                url: "${pageContext.request.contextPath}/login/ajax",
                data: {"timed": new Date().getTime()},
                dataType: "text",
                timeout: 5000,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#state").append("[state: " + textStatus + ", error: " + errorThrown + " ]<br/>");
                    if (textStatus == "timeout") { // 请求超时
                            longPolling(); // 递归调用
                        
                        // 其他错误，如网络错误等
                        } else { 
                            longPolling();
                        }
                    },
                success: function (data, textStatus) {
                    $("#state").append("[state: " + textStatus + ", data: { " + data + "} ]<br/>");
                    
                    if (textStatus == "success") { // 请求成功
                        longPolling();
                    }
                }
            });
        })();
        
    });
</script> -->

<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<body class="login_bg">
<div class="login_top">
    <div class="box1200">
         <input type="hidden" id="fileServer" value="${sessionScope.fileServiceUrl}"/>
        <div class="logo"></div>
        <form action="${ctx}/takecode/pick" method="post" id="queryForm">
        	<div class="login_searchbox"><input name="takeCode" class="search_text" type="text" value="" placeholder="<fmt:message key="take_code_prompt" bundle="${i18n}"/>"/><a class="login_searchtip" href="javascript:void(0);" onclick="pickTakeCode()"></a></div>
        </form>
    </div>
</div>

<div class="box1200">
    <div class="login_line"><fmt:message key="login" bundle="${i18n}"/> </div>
    <div class="login_con">
        <div class="login_box"></div>
        <ul>
            <li><input class="login_user clear_tishi" type="text" value="" placeholder="<fmt:message key="username_prompt" bundle="${i18n}"/>"></li>
            <li><input class="login_password clear_tishi" type="password" value="" placeholder="<fmt:message key="password_prompt" bundle="${i18n}"/>"></li>
            <li id="showCode" style='<c:if test="${empty sessionScope.attemptlogin }">display: none;</c:if>'><input class="login_yzm clear_tishi" type="text" value=""  placeholder="<fmt:message key="verification_code_prompt" bundle="${i18n}"/>" >
            <span class="yzm_box"><img id="imgss" onclick="imgchange()" src="${ctx}/check/image" title="<fmt:message key="verification_code_prompt_second" bundle="${i18n}"/>" class="regimg"/></span></li>
            <li><i class="rem_check rem_checked" for=""><input class="none" type="checkbox" id="rememberStatus" /></i><label for="rememberStatus"><span class="pl5"><fmt:message key="remember_login" bundle="${i18n}"/></span></label></li>
            <li class="btnbox"><input type="submit" name="button" id="button" value="<fmt:message key="login_button" bundle="${i18n}"/>" class="login_out" onmousedown="javascript:this.className='login_down'" onmouseout="javascript:this.className='login_out'" /></li>
            <%-- <li ><i class="reminder">为了能让您有更好的体验，我们建议您使用Google Chrome浏览器.</i> <a href="${ctx }/plugins/tools/Chrome.zip" style="text-decoration:underline;">点击下载</a></li> --%>
        </ul> 
    </div>
</div>
</body>
</html>