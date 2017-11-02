<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<title><fmt:message key="i18n_global_preview_file_conversion" bundle="${i18n}" /></title>
</head>
<body class="bg_color_gray">
<div class="bg_error">
    <div class="errorbox">
        <p class="f24" align="center"><img class="pr10" src="${staticpath }/images/busy.gif" alt=""><fmt:message key="i18n_global_preview_conversion_queue_busy" bundle="${i18n}" /></p>
    </div>   
</div>
<script type="text/javascript">
	window.location.href = "${url}";
</script>
</html>