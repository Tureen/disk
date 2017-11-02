<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<link href="${staticpath}/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/style-metro.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/style.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/font-awesome.min.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="${plugins}/common/jquery/jquery-migrate-1.2.1.min.js"></script>
<script src="${plugins}/common/jquery/jquery-ui-1.10.1.custom.min.js"
	type="text/javascript"></script>
<script src="${plugins}/common/jquery/jquery.slimscroll.min.js"
	type="text/javascript"></script>
<script src="${plugins}/common/jquery/jquery.blockui.min.js"
	type="text/javascript"></script>
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<div class="right_part">
		<div class="main_con main_con_an">
			<div class="crumbs">
				<ul>
					<li><i class="tip_r"></i><a href=""><fmt:message key="full_text_retrieval" bundle="${i18n}"/></a></li>
				</ul>
			</div>
			<div class="zlbg zlbg_an" >
			  <iframe src="${sessionScope.fileServiceUrl }/lucene/index" style="width:100%;height:100%;border: 0px;"></iframe>
			</div>
		</div>
	</div>
</div>
</body>
</html>