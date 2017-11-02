<%@ page language="java" import="com.yunip.utils.page.PageQuery" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="totalPages" value="${query.pageCount == 0 ? 1 : query.pageCount}"/>
<script type="text/javascript" src="${ctx}/plugins/page/jqPaginator.js"></script>
<link href="${ctx}/plugins/page/bootstrap.min.css" rel="stylesheet" type="text/css" />

<div>
<ul class="pagination" id="pagination" style="margin: 0px 0;"></ul>
<ul style="margin: 0px 0;" id="pagination" class="pagination">
	<li class="page" jp-role="page" jp-data="1"><a href="javascript:;"><fmt:message key="i18n_global_page_total_of" bundle="${i18n}" />${query.pageCount}<fmt:message key="i18n_global_total_page" bundle="${i18n}" /></a></li>
	<li class="page" jp-role="page" jp-data="2"><a href="javascript:;"><fmt:message key="i18n_global_page_total_of" bundle="${i18n}" />${query.recordCount}<fmt:message key="i18n_global_total_record" bundle="${i18n}" /></a></li>
</ul>
</div>
<input type="hidden" id="pageIndex" name = "pageIndex" value="${query.pageIndex}"/>
<script type="text/javascript">
$.jqPaginator('#pagination', {
    totalPages: ${totalPages},
    visiblePages: 7,
    currentPage: ${query.pageIndex > totalPages ? totalPages : query.pageIndex},
    <c:if test="${localvar=='zh_CN' }">
    first: '<li class="first"><a href="javascript:;">首页</a></li>',
    last: '<li class="last"><a href="javascript:;">尾页</a></li>',
    prev: '<li class="prev"><a href="javascript:;">上一页</a></li>',
    next: '<li class="next"><a href="javascript:;">下一页</a></li>',
    page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
    </c:if>
    onPageChange: function (num, type) {
    	var index = $("#pageIndex").val();
        if(index != num){
        	$("#pageIndex").val(num);
        	$("#queryForm").submit();
    	}
    }
});
</script>