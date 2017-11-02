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
<script type="text/javascript" src="${staticpath}/js/workgroupapply/index.js?v=20170110111"></script>
<style type="text/css">
.layui-layer-btn{
	border-top:0px !important;
}
.tabs td span {
    padding-left: 0px;
}
.view_user{
	cursor: pointer;
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
                    <div class="crumbs"><ul><li><i class="tip_t4"></i><a href="javascript:void(0);"><fmt:message key="workgroup_apply_for" bundle="${i18n}"/></a></li></ul></div>
                       <div class="action_btn">
                           <ul>
                               <li class="ac_examination_true"><i class="ac_t16"></i><fmt:message key="w_audit_through" bundle="${i18n}" /></li>
                               <li class="ac_examination_fail"><i class="ac_t17"></i><fmt:message key="w_refuse_to_pass" bundle="${i18n}" /></li>
                           </ul>
                       </div>
					<form action="${ctx }/wgapply/index" method="post" id="searchForm">
						<div class="r_search_contain">
	   						<div class="label_search"><input type="text" placeholder="<fmt:message key="w_workgroup_name" bundle="${i18n}"/>" id="queryName" name="" value="${query.workgroupName }"/>
								<a id="searchFile" class="magnifier" href="javascript:void(0);"></a></div>
	            		</div>
	                	<div class="r_search_contain">
	                    	<div class="label_search">
	                    		<select class="select_search" id="applyStatus" name="applyStatus">
	                   				<option value="-1"><fmt:message key="w_all" bundle="${i18n}"/></option>
	                    			<c:forEach items="${enum }" var="workgroupStatus">
	                    				<option value="${workgroupStatus.status }" <c:if test="${workgroupStatus.status==query.applyStatus }">selected="selected"</c:if>><fmt:message key="${workgroupStatus.desc}" bundle="${i18n}" /></option>
	                    			</c:forEach>
	                    		</select>
							</div> 
	              		</div>
               		</form>
                </div>  
            </div>
            <form action="${ctx }/wgapply/index" id = "queryForm" method="post">
            <div class="content content_an">
            	<c:if test="${empty query.list}">
            		<div id="noneContent" class="tab_contain noline mt10">
			       		<p class="nofile"><img src="${staticpath}/images/nocontact.png" /><br><span><i></i><fmt:message key="w_no_workgroup" bundle="${i18n}" /></span></p>
	               </div>
            	</c:if>
	            <div id="mainContent">
	                <div class="tab_contain" style="height: 100%">
	                	<table class="tabs_th">
		                    <tr>
		                        <th width="10%"><i class="tab_check" id="allCheck"><input class="none" type="checkbox" /></i></th>
	                            <th width="20%"><fmt:message key="w_workgroup_name" bundle="${i18n}" /></th>
	                            <th width="20%"></th>
	                            <th width="10%"><fmt:message key="w_group_administrator" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_applicant" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_application_time" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_approval_time" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="w_valid_status" bundle="${i18n}" /></th>
		                    </tr>
		                </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                    	<%-- <input id="employeeId" type="hidden" value="${employeeId }" />
	                    	<input id="contactIds" type="hidden" value="${contactIds }" /> --%>
	                    	<c:forEach items="${query.list}" var="workgroupApply">
	                    		<tr class="stabs">
	                    			<td width="10%">
		                            	<i class="tab_check <c:if test="${workgroupApply.id==workgroupApplyId}">tab_checked</c:if>" name="msgItem" value="${workgroupApply.id}"></i>
		                            </td>
		                            <td width="20%">
		                            	${workgroupApply.workgroupName }
	                            	</td>
	                            	<td width="20%">
	                            	</td>
		                            <td width="10%">
		                            	${employee.employeeName }
	                            	</td>
		                            <td width="10%">
		                            	${workgroupApply.applyEmployeeName }
	                            	</td>
		                            <td width="10%">
		                            	<fmt:formatDate value="${workgroupApply.createTime }" pattern="yyyy-MM-dd HH:mm" />
	                            	</td>
		                            <td width="10%">
		                            	<fmt:formatDate value="${workgroupApply.updateTime }" pattern="yyyy-MM-dd HH:mm" />
	                            	</td>
		                            <td width="10%" class="workgroupStatus">
		                            	<c:forEach items="${enum }" var="workgroupStatus">
									    	<c:if test="${workgroupStatus.status==workgroupApply.applyStatus}"><span style="color: ${workgroupStatus.colorStyle}" code="${workgroupApply.applyStatus }"><fmt:message key="${workgroupStatus.desc}" bundle="${i18n}" /></span></c:if>
									    </c:forEach>
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