<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%
       //加载i18n资源文件，request.getLocale()获取访问用户所在的国家地区
       java.util.ResourceBundle myResourcesBundle = java.util.ResourceBundle.getBundle("i18n.myproperties",request.getLocale());
%>
<script type="text/javascript" src="${staticpath}/js/auth/index.js?v=2016"></script>
<fmt:setBundle basename="i18n.myproperties" var="i18n"/> 
<div class="outlayer con_management">
    <div class="tab_management">
        <ul>
            <c:forEach items="${authorityTypes }" var="authorityType">
              <li <c:if test="${authorityType.code == 1}">class="on"</c:if>><input type="hidden" value="${authorityType.code}"/>${authorityType.desc }</li>
            </c:forEach>
        </ul>
    </div>
    <input type="hidden" id="openType" value="${authHelper.openType }"/>
    <input type="hidden" id="openId" value="${authHelper.openId}"/>
    <input type="hidden" id="data" value="${authHelper.data}"/>    
    <div class="tab_management_con">
        <div class="managebox " style="display:block">
            <div class="m_lbox">
            <h2>${manageAuthorityType.desc }</h2>
            <p class="color_7d">
            <c:forEach items="${manageAuthorityType.authorities}" var="authority"  varStatus="auth">
                <c:if test="${auth.index == 0}">
	               ${authority.desc}
	              </c:if>
	            <c:if test="${auth.index > 0}">
	                           、${authority.desc}
	            </c:if>
            </c:forEach>  
            <input type="hidden" id="authType" value="${manageAuthHelper.authType}"/>    
            </p>
            <div class="area"><label class="color_7d" for=""><fmt:message key="scope" bundle="${i18n}"/></label><textarea name="" class="shareName" cols="30" rows="10" placeholder=""></textarea></div>
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
                    <li><label class="tab_check" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
			        <c:forEach items="${departments}" var="department">
				      <li><label class="tab_check
				      <c:forEach items="${manageAuthHelper.deptIds}" var="deptId">
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
	                            <c:forEach items="${manageAuthHelper.employees}" var="emp">
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
        <div class="managebox " style="display:none">
            <div class="m_lbox">
            <h2>${seeAuthorityType.desc }</h2>
            <p class="color_7d">
            <c:forEach items="${seeAuthorityType.authorities}" var="authority"  varStatus="auth">
                <c:if test="${auth.index == 0}">
	               ${authority.desc}
	              </c:if>
	            <c:if test="${auth.index > 0}">
	                           、${authority.desc}
	            </c:if>
            </c:forEach>  
            <input type="hidden" id="authType" value="${seeAuthHelper.authType}"/>    
            </p>
            <div class="area"><label class="color_7d" for=""><fmt:message key="scope" bundle="${i18n}"/></label><textarea name="" class="shareName" cols="30" rows="10" placeholder=""></textarea></div>
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
                    <li><label class="tab_check <c:if test="${seeAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
			       <c:forEach items="${departments}" var="department">
				      <li><label class="tab_check
				      <c:forEach items="${seeAuthHelper.deptIds}" var="deptId">
					       <c:if test="${deptId == department.id }">
					         tab_checked
					       </c:if>
			          </c:forEach>" for=""  name="deptCheck" kvalue="${department.id }"></label><span>${department.deptName }</span></li>
				     </c:forEach>
                </ul>
            </div>
            <div class="tab_2_con pad0">
                <div class="layer_searchbox"><a href="javascript:;"><fmt:message key="search" bundle="${i18n}"/></a><input type="text" class="layer_search" placeholder="搜索"/></div>
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
	                           " name="empCheck" for="" kvalue="${employee.id }"></label><input type="hidden" value="${employee.deptId}"/><span>${employee.employeeName }</span></li>
	                        </c:forEach>
	                    </ul>
	                </c:forEach>
                </div>
            </div>
         </div>
        </div>
        <div class="managebox" style="display:none">
            <div class="m_lbox">
            <h2>${readAuthorityType.desc }</h2>
            <p class="color_7d">
            <c:forEach items="${readAuthorityType.authorities}" var="authority"  varStatus="auth">
                <c:if test="${auth.index == 0}">
	               ${authority.desc}
	              </c:if>
	            <c:if test="${auth.index > 0}">
	                           、${authority.desc}
	            </c:if>
            </c:forEach>  
            <input type="hidden" id="authType" value="${readAuthHelper.authType}"/>    
            </p>
            <div class="area"><label class="color_7d" for=""><fmt:message key="scope" bundle="${i18n}"/></label><textarea name="" class="shareName" cols="30" rows="10" placeholder=""></textarea></div>
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
                    <li><label class="tab_check <c:if test="${readAuthHelper.all}">tab_checked</c:if>" id="allCheck" for=""></label><span><fmt:message key="all" bundle="${i18n}"/></span></li>
			       <c:forEach items="${departments}" var="department">
				      <li><label class="tab_check
				      <c:forEach items="${readAuthHelper.deptIds}" var="deptId">
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
	                            <c:forEach items="${readAuthHelper.employees}" var="emp">
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
    </div>
</div>
