<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script>
</script>
 <!-- 表格行 -->
<table class="tabs_th">
        <tr>
        <th width="5%"><i id="allCheck" class="tab_check" for=""><input class="none" type="checkbox"></i></th>
        <th width="45%"><fmt:message key="file_name" bundle="${i18n}"/></th>
        <th width="25%"><fmt:message key="size" bundle="${i18n}"/></th>
        <th widt"25%"><fmt:message key="update_time" bundle="${i18n}"/><span class="date_tip_up"></span></th> 
    </tr>
</table>

<div class="tab_contain">
    <!-- 表格 -->
<table class="tabs" id="tabs">
	<c:forEach items="${fileSignQuery.list }" var="fileSign">
     <tr>
         <td width="5%"><i class="tab_check" name="fileSignCheck" value="${fileSign.fileId}" for=""><input class="none" name="fileId" value="${fileSign.fileId }"><input class="none" name="signId" value="${fileSign.signId }"><input class="none" name="fileType" value="${fileSign.fileType }"></i></td>
         <td width="45%">
             <p class="filebox">
             	<i class="tipbg  tips_bg_${fileSign.fileType }"></i>
             	<span class="file_name <c:if test="${fileSign.fileType == 1 }">imageFile</c:if><c:if test="${fileSign.fileType > 1 }">previewFile</c:if>" fileId="${fileSign.fileId}"><a class="remove_check" id="fileName${fileSign.fileId }-${fileSign.signId }" href="javascript:void(0);">${fileSign.fileName }</a></span>
           		</p>
         </td>
         <td width="25%">${fileSign.showFileSize }</td>
         <td width="25%"><fmt:formatDate value="${fileSign.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
     </tr> 
    </c:forEach>
    </table> 
</div>   
