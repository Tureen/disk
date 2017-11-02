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
<link rel="stylesheet" href="${plugins}/ztree/zTreeStyle/zTreeStyle.css?v=20170207" type="text/css">
<script type="text/javascript" src="${plugins}/ztree/jquery.ztree.core.js"></script> 
<script type="text/javascript" src="${staticpath}/js/comcontact/index.js?v=20170110"></script>
<script type="text/javascript">
var zNodes = [];
<c:forEach items="${departmentList}" var="dept">
 var data = { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true };
 zNodes.push(data);
</c:forEach>
</script>
<style type="text/css">
.layui-layer-btn{
	border-top:0px !important;
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
                        <div class="crumbs"><ul><li><i class="tip_t2"></i><a href="javascript:void(0);"><fmt:message key="contact_common" bundle="${i18n}" /></a></li></ul></div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_contact"><i class="ac_t01"></i><fmt:message key="c_add_contact" bundle="${i18n}" /></li>
                            </ul>
                        </div>
                        <div class="r_search_contain">
                         <form action="${ctx}/comcontact/index" method="post" id="searchForm">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="pr_employee" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.employeeName }"/>
							<a id="searchFile" class="magnifier" href="javascript:void(0);"></a></div> 
                         </form>
                      </div>
                        <div class="r_search_contain">
                            <div class="label_search">
                            	<input id="deptName" style="cursor: pointer;" type="text" value="${query.deptName }" placeholder="<fmt:message key="c_employee_dept" bundle="${i18n}"/>" readonly="readonly" onclick="showMenu(); return false;" <%-- id="queryName" name="queryName" value="${query.employeeName }" --%>/>
                            	<input type="hidden" name="deptId" value="${query.deptId }" id="deptId">
                            	<div id="menuContent" class="menuContent contact-dept">
									<ul id="treeDemo" class="ztree" style="margin-top:0; "></ul>
					         	</div>
							</div> 
                      </div>
                </div>  
            </div>
            <form action="${ctx }/comcontact/index" id = "queryForm" method="post">
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
	                            <th width="20%"><fmt:message key="c_employee_name" bundle="${i18n}" /></th>
	                            <th width="20%"><fmt:message key="c_employee_mobile" bundle="${i18n}" /></th>
	                            <th width="30%"><fmt:message key="c_employee_email" bundle="${i18n}" /></th>
	                            <th width="20%"><fmt:message key="c_employee_dept" bundle="${i18n}" /></th>
		                    </tr>
		                </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                    	<%-- <input id="employeeId" type="hidden" value="${employeeId }" />
	                    	<input id="contactIds" type="hidden" value="${contactIds }" /> --%>
	                    	<c:forEach items="${query.list}" var="employee">
	                    		<tr class="stabs">
	                    			<td width="10%">
		                            	<i class="tab_check" name="msgItem" value="${employee.id}"></i>
		                            </td>
		                            <td width="20%">
		                            	${employee.employeeName }
	                            	</td>
		                            <td width="20%">
		                            	${employee.employeeMobile }
	                            	</td>
		                            <td width="30%">
		                            	${employee.employeeEmail }
	                            	</td>
		                            <td width="20%">
		                            	${employee.deptName }
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