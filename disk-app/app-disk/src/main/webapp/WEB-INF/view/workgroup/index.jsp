<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="msgTemplate_1">
	<fmt:message key="i18n_share_message_template_content" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_2">
	<fmt:message key="i18n_file" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_3">
	<fmt:message key="i18n_files" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_4">
	<fmt:message key="i18n_folder" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_5">
	<fmt:message key="i18n_folders" bundle="${i18n}" />
</c:set>
<script type="text/javascript" src="${staticpath}/js/workgroup/index.js?v=20170418"></script>

<style type="text/css">
.layui-layer-btn{
	border-top:0px !important;
}
.tabs td span {
    padding-left: 0px;
}
.view_user{
	cursor: pointer;
	color: #2791ca;
}
.view_user:hover {
	color: #00B2EE;
}
.select_search{
	border: none;
    width: 156px;
    height: 26px;
}
</style>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<div class="box_main">
    <%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
                        <div class="crumbs"><ul><li><i class="tip_t1"></i><a href="javascript:void(0);"><fmt:message key="contact_my" bundle="${i18n}" /></a></li></ul></div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_add"><i class="ac_t18"></i><fmt:message key="w_add_workgroup" bundle="${i18n}" /></li>
                                <li class="ac_apply"><i class="ac_t19"></i><fmt:message key="w_apply_join" bundle="${i18n}" /></li>
                                <li class="ac_quit"><i class="ac_t10"></i><fmt:message key="w_quit_workgroup" bundle="${i18n}" /></li>
                                <li class="ac_edit"><i class="ac_t11"></i><fmt:message key="w_edit_workgroup" bundle="${i18n}" /></li>
                                <li class="ac_del"><i class="ac_t08"></i><fmt:message key="w_delete_workgroup" bundle="${i18n}" /></li>
                                <li class="ac_adduser"><i class="ac_t20"></i><fmt:message key="w_add_workgroup_user" bundle="${i18n}" /></li>
                                <li class="ac_transfer"><i class="ac_t03"></i><fmt:message key="w_transfer_workgroup" bundle="${i18n}" /></li>
                                <li class="ac_my_apply"><i class="ac_t06"></i><fmt:message key="w_my_application_record" bundle="${i18n}" /></li>
                            </ul>
                        </div>
                        <form action="${ctx}/workgroup/index" method="post" id="searchForm">
	                        <div class="r_search_contain">
	                            <div class="label_search"><input type="text" placeholder="<fmt:message key="w_workgroup_name" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.workgroupName }"/>
								<a id="searchFile" class="magnifier" href="javascript:void(0);"></a></div> 
	                        </div>
	                        <div class="r_search_contain">
		                    	<div class="label_search">
		                    		<select class="select_search" id="queryEmployeeId" name="queryEmployeeId">
		                   				<option value="-1"><fmt:message key="w_all_workgroup" bundle="${i18n}" /></option>
	                    				<option value="${employeeId }" <c:if test="${query.employeeId==employeeId }">selected="selected"</c:if>><fmt:message key="w_my_workgroup" bundle="${i18n}" /></option>
		                    		</select>
								</div> 
		              		</div>
                        </form>
                </div>  
            </div>
            <form action="${ctx }/workgroup/index" id = "queryForm" method="post">
            <input id="joinIds" type="hidden" value="${joinIds }">
            <input id="hasIds" type="hidden" value="${hasIds }">
            <div class="content content_an">
            	<c:if test="${empty query.list}">
            		<div id="noneContent" class="tab_contain noline mt10">
			       		<p class="nofile"><img src="${staticpath}/images/nocontact.png" /><br><span><i></i><fmt:message key="c_no_contact" bundle="${i18n}" /></span></p>
	               </div>
            	</c:if>
	            <div id="mainContent">
	                <div class="tab_contain" style="height: 100%">
	                	<table class="tabs_th">
		                    <tr>
		                        <th width="10%"><i class="tab_check" id="allCheck"><input class="none" type="checkbox" /></i></th>
	                            <th width="20%"><fmt:message key="w_workgroup_name" bundle="${i18n}" /></th>
	                            <th width="30%"><fmt:message key="w_describe" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_group_administrator" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_group_user" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_join_state" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_valid_status" bundle="${i18n}" /></th>
		                    </tr>
		                </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                    	<c:forEach items="${query.list}" var="workgroup">
	                    		<tr class="stabs">
	                    			<td width="10%">
		                            	<i id="${workgroup.id}" class="tab_check" name="msgItem" value="${workgroup.id}"></i>
		                            </td>
		                            <td width="20%">
		                            	${workgroup.workgroupName }
	                            	</td>
	                            	<td width="30%"  title="${workgroup.remark }" style="cursor: default;">
	                            		<c:if test="${fn:length(workgroup.remark)>16}">${fn:substring(workgroup.remark, 0, 16)}...</c:if><c:if test="${fn:length(workgroup.remark)<=16}">${workgroup.remark }</c:if>
	                            	</td>
		                            <td width="10%">
		                            	${workgroup.createAdmin }
	                            	</td>
	                            	<td width="10%" class="has_status">
	                            		<fmt:message key="w_no_view_permissions" bundle="${i18n}" />
	                            	</td>
		                            <td width="10%" class="join_status">
		                            	<fmt:message key="w_no_join" bundle="${i18n}" />
	                            	</td>
		                            <td width="10%">
		                            	<c:if test="${workgroup.validStatus == 1}"><span style="color: #008000"><fmt:message key="w_valid_status_true" bundle="${i18n}" /></span></c:if>
		                            	<c:if test="${workgroup.validStatus == 0}"><span style="color: gray"><fmt:message key="w_valid_status_false" bundle="${i18n}" /></span></c:if>
	                            	</td>
	                    		</tr>
	                    	</c:forEach>
	                    </table> 
	                    <div class="bottom_page bottom_txt"><%@ include file="/WEB-INF/view/include/myPaging.jsp"%></div> 
	                </div>    
	            </div>
	        </div>
	        </form>
        </div>
    </div>
</div>
</body>
</html>