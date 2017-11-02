<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript" src="${staticpath}/js/manageauth/index.js?v=20170213"></script>
<!-- 设置分享范围 -->
<div id="con_settingshare" class="outlayer">
    <input type="hidden" id="openType" value="${authHelper.openType }"/>
    <input type="hidden" id="openId" value="${authHelper.openId}"/>
    <input type="hidden" id="data" value="${authHelper.data}"/>    
    <div class="mb10"><span class="power_explain"><fmt:message key="authorization_description" bundle="${i18n}"/>
        <div class="power_box" style="display:none">
            <table>
                <tr>
                    <th><fmt:message key="authority_content" bundle="${i18n}"/></th>
                    <th><fmt:message key="manage_authority" bundle="${i18n}"/></th>
                    <th><fmt:message key="view_authority" bundle="${i18n}"/></th>
                    <th><fmt:message key="preview_authority" bundle="${i18n}"/></th>
                </tr>
                <tr>
                    <td><fmt:message key="预览" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td><span class="yes"></span></td>
                    <td><span class="yes"></span></td>
                </tr>
                <tr>
                    <td><fmt:message key="download" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td><span class="yes"></span></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="copy" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td><span class="yes"></span></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="upload" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="modify" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><fmt:message key="create" bundle="${i18n}"/></td>
                    <td><span class="yes"></span></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
    </span><span><fmt:message key="shared_membership_permissions" bundle="${i18n}"/></span></div>
    <!--  当都不存在的情况下默认添加一个管理权限 -->
    <c:if test="${empty manageAuthHelper && empty seeAuthHelper &&empty readAuthHelper }">
    <!-- 判断是否存在 -->
    <div class="share_con">
        <div class="select_down fl">
            <p><fmt:message key="${authorityTypes[0].desc}权限" bundle="${i18n}"/></p>
            <div>
	            <c:forEach items="${authorityTypes }" var="authorityType">
	              <p date-value='<fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/>' key-value="${authorityType.code}" <c:if test="${authorityType.code == 1}">class="selected"</c:if>><fmt:message key="${authorityType.desc }权限" bundle="${i18n}"/></p>     
	            </c:forEach>                    
            </div>
        </div>
        <div class="share_box_r"><span class="share_area"><fmt:message key="selected_range" bundle="${i18n}"/>：<b id="deptNumber">0</b><fmt:message key="num_dept" bundle="${i18n}"/>，<b id="empNumber">0</b><fmt:message key="num_colleague" bundle="${i18n}"/>，<b id="workgroupNumber">0</b><fmt:message key="num_workgroup" bundle="${i18n}"/></span><i class="user_share_tip"></i>
            <div class="dropbox">
            <!-- tab选项 -->
                <div class="m_rbox">
                    <div class="tab_3" id="tabT2">
                        <ul>
                               <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="employee" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="w_workgroup" bundle="${i18n}"/></li>
                        </ul>
                    </div>
                    <div id="tabB2">
                        <div class="tab_3_con on">
                            <ul class="department">
                            <li><label class="tab_check" <c:if test="${manageAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
						       <c:forEach items="${departments}" var="department">
						          <li><label class="tab_check" name="deptCheck" for="${department.id }"></label><span>${department.deptName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                        <div class="tab_3_con pad0">
                            <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder="<fmt:message key="search" bundle="${i18n}"/>"/></div>
                               <div class="tab_searchbox">
					                <c:forEach items="${employees}" var="employeeMap">
					                   <p>${employeeMap.key}</p>
					                   <ul>
					                        <c:forEach items="${employeeMap.value}" var="employee">
					                           <li><label class="tab_check" name="empCheck" for="${employee.id }" key-value="${employee.deptId}"></label><span>${employee.employeeName }</span></li>
					                        </c:forEach>
					                    </ul>
					                </c:forEach>
				               </div>
				            </div>
				        <div class="tab_3_con">
                            <ul class="workgroup">
						       <c:forEach items="${workgroups}" var="workgroup">
						          <li><label class="tab_check
						          <c:forEach items="${manageAuthHelper.workgroupIds}" var="workgroupId">
								       <c:if test="${workgroupId == workgroup.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="workgroupCheck" for="${workgroup.id }"></label><span>${workgroup.workgroupName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="tab_label_r">
                    <ul>
                    </ul>
                </div>
             </div>
        </div>
    </div>
    </c:if>
    <!-- 判断管理权限是否存在 -->
    <c:if test="${not empty manageAuthHelper }">
    <!-- 判断是否存在 -->
    <div class="share_con">
        <div class="select_down fl">
            <p><fmt:message key="${authorityTypes[0].desc}权限" bundle="${i18n}"/></p>
            <div>
                 <c:forEach items="${authorityTypes }" var="authorityType">
	                <p date-value='<fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/>' key-value="${authorityType.code}" <c:if test="${authorityType.code == 1}">class="selected"</c:if>><fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/></p>     
	             </c:forEach>                                   
            </div>
        </div>
        <div class="share_box_r"><span class="share_area"><fmt:message key="selected_range" bundle="${i18n}"/>：<b id="deptNumber">0</b><fmt:message key="num_dept" bundle="${i18n}"/>，<b id="empNumber">0</b><fmt:message key="num_colleague" bundle="${i18n}"/>，<b id="workgroupNumber">0</b><fmt:message key="num_workgroup" bundle="${i18n}"/></span><i class="user_share_tip"></i>
            <div class="dropbox">
            <!-- tab选项 -->
                <div class="m_rbox">
                    <div class="tab_3" id="tabT2">
                        <ul>
                               <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="employee" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="w_workgroup" bundle="${i18n}"/></li>
                        </ul>
                    </div>
                    <div id="tabB2">
                        <div class="tab_3_con on">
                            <ul class="department">
                            <li><label class="tab_check <c:if test="${manageAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
						       <c:forEach items="${departments}" var="department">
						          <li><label class="tab_check
						          <c:forEach items="${manageAuthHelper.deptIds}" var="deptId">
								       <c:if test="${deptId == department.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="deptCheck" for="${department.id }"></label><span>${department.deptName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                        <div class="tab_3_con pad0">
                            <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder="<fmt:message key="search" bundle="${i18n}"/>"/></div>
                               <div class="tab_searchbox">
					                <c:forEach items="${employees}" var="employeeMap">
					                   <p>${employeeMap.key}</p>
					                   <ul>
					                        <c:forEach items="${employeeMap.value}" var="employee">
					                           <li><label class="tab_check
					                            <c:forEach items="${manageAuthHelper.employees}" var="emp">
					                               <c:if test = "${emp.id == employee.id}">
					                                 tab_checked
					                               </c:if>
					                            </c:forEach>
					                           " name="empCheck" for="${employee.id }" key-value="${employee.deptId}"></label><span>${employee.employeeName }</span></li>
					                        </c:forEach>
					                    </ul>
					                </c:forEach>
				               </div>
				            </div>
				         <div class="tab_3_con">
                            <ul class="workgroup">
						       <c:forEach items="${workgroups}" var="workgroup">
						          <li><label class="tab_check
						          <c:forEach items="${manageAuthHelper.workgroupIds}" var="workgroupId">
								       <c:if test="${workgroupId == workgroup.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="workgroupCheck" for="${workgroup.id }"></label><span>${workgroup.workgroupName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="tab_label_r">
                    <ul>
                    </ul>
                </div>
             </div>
        </div>
    </div>
    </c:if>
    <!-- 判断查看权限是否存在 -->
    <c:if test="${not empty seeAuthHelper }">
    <!-- 判断是否存在 -->
    <div class="share_con">
        <div class="select_down fl">
            <p><fmt:message key="${authorityTypes[1].desc}权限" bundle="${i18n}"/></p>
            <div>
                 <c:forEach items="${authorityTypes }" var="authorityType">
	                <p date-value="<fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/>" key-value="${authorityType.code}" <c:if test="${authorityType.code == 2}">class="selected"</c:if>><fmt:message key="${authorityType.desc }权限" bundle="${i18n}"/></p>     
	             </c:forEach>                                   
            </div>
        </div>
        <div class="share_box_r"><span class="share_area"><fmt:message key="selected_range" bundle="${i18n}"/>：<b id="deptNumber">0</b><fmt:message key="num_dept" bundle="${i18n}"/>，<b id="empNumber">0</b><fmt:message key="num_colleague" bundle="${i18n}"/>，<b id="workgroupNumber">0</b><fmt:message key="num_workgroup" bundle="${i18n}"/></span><i class="user_share_tip"></i>
            <div class="dropbox">
            <!-- tab选项 -->
                <div class="m_rbox">
                    <div class="tab_3" id="tabT2">
                        <ul>
                               <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="employee" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="w_workgroup" bundle="${i18n}"/></li>
                        </ul>
                    </div>
                    <div id="tabB2">
                        <div class="tab_3_con on">
                            <ul class="department">
                            <li><label class="tab_check <c:if test="${seeAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
						       <c:forEach items="${departments}" var="department">
						          <li><label class="tab_check
						          <c:forEach items="${seeAuthHelper.deptIds}" var="deptId">
								       <c:if test="${deptId == department.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="deptCheck" for="${department.id }"></label><span>${department.deptName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                        <div class="tab_3_con pad0">
                            <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder='<fmt:message key="search" bundle="${i18n}"/>'/></div>
                               <div class="tab_searchbox">
					                <c:forEach items="${employees}" var="employeeMap">
					                   <p>${employeeMap.key}</p>
					                   <ul>
					                        <c:forEach items="${employeeMap.value}" var="employee">
					                           <li><label class="tab_check
					                            <c:forEach items="${seeAuthHelper.employees}" var="emp">
					                               <c:if test = "${emp.id == employee.id}">
					                                 tab_checked
					                               </c:if>
					                            </c:forEach>
					                           " name="empCheck" for="${employee.id }" key-value="${employee.deptId}"></label><span>${employee.employeeName }</span></li>
					                        </c:forEach>
					                    </ul>
					                </c:forEach>
				               </div>
				            </div>
				         <div class="tab_3_con">
                            <ul class="workgroup">
						       <c:forEach items="${workgroups}" var="workgroup">
						          <li><label class="tab_check
						          <c:forEach items="${seeAuthHelper.workgroupIds}" var="workgroupId">
								       <c:if test="${workgroupId == workgroup.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="workgroupCheck" for="${workgroup.id }"></label><span>${workgroup.workgroupName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="tab_label_r">
                    <ul>
                    </ul>
                </div>
             </div>
        </div>
    </div>
    </c:if>
    <!-- 判断预览权限是否存在 -->
    <c:if test="${not empty readAuthHelper }">
    <!-- 判断是否存在 -->
    <div class="share_con">
        <div class="select_down fl">
            <p><fmt:message key="${authorityTypes[2].desc}权限" bundle="${i18n}"/></p>
            <div>
                 <c:forEach items="${authorityTypes }" var="authorityType">
	                <p date-value="<fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/>" key-value="${authorityType.code}" <c:if test="${authorityType.code == 3}">class="selected"</c:if>><fmt:message key="${authorityType.desc }权限" bundle="${i18n}"/></p>     
	             </c:forEach>                                   
            </div>
        </div>
        <div class="share_box_r"><span class="share_area"><fmt:message key="selected_range" bundle="${i18n}"/>：<b id="deptNumber">0</b><fmt:message key="num_dept" bundle="${i18n}"/>，<b id="empNumber">0</b><fmt:message key="num_colleague" bundle="${i18n}"/>，<b id="workgroupNumber">0</b><fmt:message key="num_workgroup" bundle="${i18n}"/></span><i class="user_share_tip"></i>
            <div class="dropbox">
            <!-- tab选项 -->
                <div class="m_rbox">
                    <div class="tab_3" id="tabT2">
                        <ul>
                               <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="employee" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="w_workgroup" bundle="${i18n}"/></li>
                        </ul>
                    </div>
                    <div id="tabB2">
                        <div class="tab_3_con on">
                            <ul class="department">
                            <li><label class="tab_check <c:if test="${readAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
						       <c:forEach items="${departments}" var="department">
						          <li><label class="tab_check
						          <c:forEach items="${readAuthHelper.deptIds}" var="deptId">
								       <c:if test="${deptId == department.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="deptCheck" for="${department.id }"></label><span>${department.deptName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                        <div class="tab_3_con pad0">
                            <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder='<fmt:message key="search" bundle="${i18n}"/>'/></div>
                               <div class="tab_searchbox">
					                <c:forEach items="${employees}" var="employeeMap">
					                   <p>${employeeMap.key}</p>
					                   <ul>
					                        <c:forEach items="${employeeMap.value}" var="employee">
					                           <li><label class="tab_check
					                            <c:forEach items="${readAuthHelper.employees}" var="emp">
					                               <c:if test = "${emp.id == employee.id}">
					                                 tab_checked
					                               </c:if>
					                            </c:forEach>
					                           " name="empCheck" for="${employee.id }" key-value="${employee.deptId}"></label><span>${employee.employeeName }</span></li>
					                        </c:forEach>
					                    </ul>
					                </c:forEach>
				               </div>
				            </div>
				         <div class="tab_3_con">
                            <ul class="workgroup">
						       <c:forEach items="${workgroups}" var="workgroup">
						          <li><label class="tab_check
						          <c:forEach items="${readAuthHelper.workgroupIds}" var="workgroupId">
								       <c:if test="${workgroupId == workgroup.id }">
								         tab_checked
								       </c:if>
						          </c:forEach>" name="workgroupCheck" for="${workgroup.id }"></label><span>${workgroup.workgroupName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="tab_label_r">
                    <ul>
                    </ul>
                </div>
             </div>
        </div>
    </div>
    </c:if>
    
    <div class="share_box2"><i class="plus_member"></i><fmt:message key="add_other_rights_members" bundle="${i18n}"/></div>
 </div>
 <div id="hideDiv" style="display: none">
    <div class="share_con">
        <div class="select_down fl">
            <p><fmt:message key="${authorityTypes[0].desc}权限" bundle="${i18n}"/></p>
            <div>
                <c:forEach items="${authorityTypes }" var="authorityType">
	              <p date-value='<fmt:message key="${authorityType.desc}权限" bundle="${i18n}"/>'  key-value="${authorityType.code}" <c:if test="${authorityType.code == 1}">class="selected"</c:if>><fmt:message key="${authorityType.desc }权限" bundle="${i18n}"/></p>     
	            </c:forEach>                               
            </div>
        </div>
        <div class="share_box_r"><span class="share_area"><fmt:message key="selected_range" bundle="${i18n}"/>：<b id="deptNumber">0</b><fmt:message key="num_dept" bundle="${i18n}"/>，<b id="empNumber">0</b><fmt:message key="num_colleague" bundle="${i18n}"/>，<b id="workgroupNumber">0</b><fmt:message key="num_workgroup" bundle="${i18n}"/></span><i class="user_share_tip"></i>
            <div class="dropbox">
            <!-- tab选项 -->
                <div class="m_rbox">
                    <div class="tab_3" id="tabT2">
                        <ul>
                               <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="employee" bundle="${i18n}"/></li>
                    		   <li><fmt:message key="w_workgroup" bundle="${i18n}"/></li>
                        </ul>
                    </div>
                    <div id="tabB2">
                        <div class="tab_3_con on">
                            <ul class="department">
                            <li><label class="tab_check" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
						       <c:forEach items="${departments}" var="department">
						          <li><label class="tab_check" name="deptCheck" for="${department.id }"></label><span>${department.deptName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                        <div class="tab_3_con pad0">
                            <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder='<fmt:message key="search" bundle="${i18n}"/>'/></div>
                               <div class="tab_searchbox">
					                <c:forEach items="${employees}" var="employeeMap">
					                   <p>${employeeMap.key}</p>
					                   <ul>
					                        <c:forEach items="${employeeMap.value}" var="employee">
					                           <li><label class="tab_check" name="empCheck" for="${employee.id }" key-value="${employee.deptId}"></label><span>${employee.employeeName }</span></li>
					                        </c:forEach>
					                    </ul>
					                </c:forEach>
				               </div>
				            </div>
				         <div class="tab_3_con on">
                            <ul class="department">
						       <c:forEach items="${workgroups}" var="workgroup">
						          <li><label class="tab_check" name="workgroupCheck" for="${workgroup.id }"></label><span>${workgroup.workgroupName }</span></li>
							     </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="tab_label_r">
                    <ul>
                    </ul>
                </div>
             </div>
        </div>
    </div>
</div>
