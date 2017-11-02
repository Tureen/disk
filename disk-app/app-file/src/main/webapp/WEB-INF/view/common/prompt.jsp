<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body class="bg_color_gray">
<div class="bg_error">
    <div class="errorbox_loading">
        <div class="error_warn"></div>
        <div class="error_text">
            <p class="color_7d f52"><fmt:message key="i18n_global_reminder" bundle="${i18n}" /></p>
            <p class="color_7d f24" style="width: 400px;">${msg }</p>
        </div>
    </div>   
</div>
</body>
</html>