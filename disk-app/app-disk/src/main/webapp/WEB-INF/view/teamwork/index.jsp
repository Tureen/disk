<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript" src="${staticpath}/js/teamwork/index.js?v=20170418"></script>
<style type="text/css">
.layui-layer-btn{
	border-top:0px !important;
}
.tabs td span {
    padding-left: 0px;
}
.mouse_click{
	cursor: pointer;
	color: #2791ca;
}
.mouse_click:hover {
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
                    <div class="crumbs"><ul><li><i class="tip_t5"></i><a href="javascript:void(0);"><fmt:message key="tw_teamwork" bundle="${i18n}"/></a></li></ul></div>
                       <div class="action_btn">
                           <ul>
                               <li class="ac_add"><i class="ac_t18"></i><fmt:message key="tw_create_teamwork" bundle="${i18n}"/></li>
                               <li class="ac_edit"><i class="ac_t11"></i><fmt:message key="tw_edit_teamwork" bundle="${i18n}"/></li>
                               <li class="ac_del"><i class="ac_t08"></i><fmt:message key="tw_delete_teamwork" bundle="${i18n}"/></li>
                               <li class="ac_quit"><i class="ac_t10"></i><fmt:message key="tw_quit_teamwork" bundle="${i18n}"/></li>
                               <li class="ac_adduser"><i class="ac_t20"></i><fmt:message key="tw_manage_teamwork_user" bundle="${i18n}"/></li>
                           </ul>
                       </div>
					<form action="${ctx }/teamwork/index" method="post" id="searchForm">
						<div class="r_search_contain">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="tw_teamwork_name" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.teamworkName }"/>
							<a id="searchFile" class="magnifier" href="javascript:void(0);"></a></div> 
                        </div>
                        <div class="r_search_contain">
	                    	<div class="label_search">
	                    		<select class="select_search" id="teamworkSearchType" name="teamworkSearchType">
	                   				<c:forEach items="${enums }" var="enumm">
	                    				<option value="${enumm.type }" <c:if test="${enumm.type==query.teamworkSearchType }">selected="selected"</c:if>><fmt:message key="${enumm.desc}" bundle="${i18n}"/></option>
	                    			</c:forEach>
	                    		</select>
							</div> 
	              		</div>
               		</form>
                </div>  
            </div>
            <form action="${ctx }/teamwork/index" id = "queryForm" method="post">
            <input id="joinIds" type="hidden" value="${joinIds }">
            <input id="hasIds" type="hidden" value="${hasIds }">
            <div class="content content_an">
            	<c:if test="${empty query.list}">
            		<div id="noneContent" class="tab_contain noline mt10">
			       		<p class="nofile"><img src="${staticpath}/images/nocontact.png" /><br><span><i></i><fmt:message key="tw_you_have_not_joined" bundle="${i18n}" /></span></p>
	               </div>
            	</c:if>
	            <div id="mainContent">
	                <div class="tab_contain" style="height: 100%">
	                	<table class="tabs_th">
		                    <tr>
		                        <th width="5%"><i class="tab_check" id="allCheck"><input class="none" type="checkbox" /></i></th>
	                            <%-- <th width="20%"><fmt:message key="tw_teamwork_name" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_administrators" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_used_space" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_member_number" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_create_time" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_remark" bundle="${i18n}"/></th>
	                            <th width="10%"><fmt:message key="tw_operation" bundle="${i18n}"/></th> --%>
		                    </tr>
		                </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                    	<c:forEach items="${query.list}" var="teamwork">
	                    		<tr class="stabs">
	                    			<td width="5%">
		                            	<i class="tab_check <%-- <c:if test="${workgroupApply.id==workgroupApplyId}">tab_checked</c:if> --%>" name="msgItem" value="${teamwork.id}"></i>
		                            </td>
		                            <td>
										<div class="app-item">
										
											<a class="j-app-query-status" href="javascript:;">
											
												<img class="app-icon" src="${staticpath}/images/lib_${teamwork.iconStr}.png">
											
											</a>
											<div class="app-desc-block">
												<span class="app-title">
												
													<a class="j-app-query-status " href="javascript:;">${teamwork.teamworkName }</a>
												
												</span>
												
												<span class="app-source app-source-native-red"><fmt:message key="tw_administrators" bundle="${i18n}"/>：${teamwork.employeeName }</span>
												<span class="app-source app-source-native-blue"><fmt:message key="tw_member_number" bundle="${i18n}"/>：${teamwork.employeeNumber }</span>
												<span class="app-source app-source-native-yellow"><fmt:message key="tw_used_space" bundle="${i18n}"/>：${teamwork.showFileSize }</span>
												<span class="app-source app-source-native-green"><fmt:formatDate value="${teamwork.createTime }" pattern="yyyy-MM-dd HH:mm" /></span>
												<p class="app-desc">${teamwork.remark }</p>
											</div>
											<div class="app-control-block fn-clear">
												<a class="j-app-query-status j-app-config fn-right begin_teamwork mouse_click" href="javascript:;"><fmt:message key="tw_teamwork_begin" bundle="${i18n}"/></a>
												<a class="j-app-query-status j-app-config fn-right view_user mouse_click" href="javascript:;"><fmt:message key="tw_view_user" bundle="${i18n}"/></a>
										</div>
										</div>
									</td>
	                    			<%-- <td width="5%">
		                            	<i class="tab_check <c:if test="${workgroupApply.id==workgroupApplyId}">tab_checked</c:if>" name="msgItem" value="${teamwork.id}"></i>
		                            </td>
		                            <td width="20%">
		                            	<p class="filebox">
			                            	<i class="tipbg_teamwork tips_bg_teamwork"></i>
			                            	<span class="file_name">
			                            		${teamwork.teamworkName }
		                            		</span>
	                                  	</p>
	                            	</td>
	                            	<td width="10%">
	                            		${teamwork.employeeName }
	                            	</td>
		                            <td width="10%">
		                            	${teamwork.showFileSize }
	                            	</td>
		                            <td width="10%">
		                            	${teamwork.employeeNumber }
	                            	</td>
		                            <td width="10%">
		                            	<fmt:formatDate value="${teamwork.createTime }" pattern="yyyy-MM-dd HH:mm" />
	                            	</td>
		                            <td width="10%">
		                            	${teamwork.remark }
	                            	</td>
		                            <td width="10%">
		                            	<span class="view_user mouse_click"><fmt:message key="tw_view_user" bundle="${i18n}"/></span> |
		                            	<span class="begin_teamwork mouse_click"><fmt:message key="tw_teamwork_begin" bundle="${i18n}"/></span>
	                            	</td> --%>
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