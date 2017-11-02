<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="webname" value="<fmt:message key="project_name" bundle="${i18n}"/>" />
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<script type="text/javascript" src="${staticpath}/js/auth/load.js?v=20160530"></script>
<!-- 管理 -->
 <div class="managebox">
        <div class="m_lbox">
            <h2>${authorityType.desc }</h2>
            <p class="color_7d">
            <c:forEach items="${authorityType.authorities}" var="authority"  varStatus="auth">
                <c:if test="${auth.index == 0}">
	               ${authority.desc}
	              </c:if>
	            <c:if test="${auth.index > 0}">
	                           、${authority.desc}
	            </c:if>
            </c:forEach>  
            <input type="hidden" id="authType" value="${authHelper.authType}"/>    
            </p>
            <div class="area"><label class="color_7d" for=""><fmt:message key="scope" bundle="${i18n}"/></label><textarea name="" id="shareName" cols="30" rows="10" placeholder=""></textarea></div>
        </div>
        <div class="m_rbox">
            <div class="tab_2">
                <ul>
                    <li class="on"><fmt:message key="dept" bundle="${i18n}"/></li>
                    <li><fmt:message key="employee" bundle="${i18n}"/></li>
                </ul>
            </div>
            <div class="tab_2_con" style="display:block">
                <ul class="department">
                    <li><label class="tab_check <c:if test="${authHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
			       <c:forEach items="${departments}" var="department">
				      <li><label class="tab_check
				      <c:forEach items="${authHelper.deptIds}" var="deptId">
					       <c:if test="${deptId == department.id }">
					         tab_checked
					       </c:if>
			          </c:forEach>" for=""  name="deptCheck" kvalue="${department.id }"></label><span>${department.deptName }</span></li>
				     </c:forEach>
                </ul>
            </div>
            <div class="tab_2_con pad0">
                <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder="<fmt:message key="search" bundle="${i18n}"/>"/></div>
                <div class="tab_searchbox">
	                <c:forEach items="${employees}" var="employeeMap">
	                   <p>${employeeMap.key}</p>
	                    <ul>
	                        <c:forEach items="${employeeMap.value}" var="employee">
	                           <li><label class="tab_check
	                            <c:forEach items="${authHelper.employees}" var="emp">
	                               <c:if test = "${emp.id == employee.id}">
	                                 tab_checked
	                               </c:if>
	                            </c:forEach>
	                           " name="empCheck" for="" kvalue="${employee.id }"></label><input type="hidden" value="${employee.deptId}"/><span>${employee.employeeName }</span></li>
	                        </c:forEach>
	                    </ul>
	                </c:forEach>
                </div>
            </div>
         </div>
    </div>