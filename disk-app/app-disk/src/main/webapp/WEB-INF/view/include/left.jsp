<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
     <div class="left_part left_part_an" style="display: none;">
        <div class="navbox">
        	
            <%-- <div class="left_top">
            	<a href="${ctx }/user/index">
            	     <c:if test="${!empty sessionScope.employee.employeePortrait}">
				        <i class="nav_top_img"><img src="${ctx }/user/portrait"/></i>
				      </c:if>
				      <c:if test="${empty sessionScope.employee.employeePortrait}">
				       <i class="nav_top_img"><img src="${ctx }/static/images/info_photo.jpg"></i>
				      </c:if>
	                <div><p class="p1"> ${sessionScope.employee.employeeName}</p><p class="p2"> ${sessionScope.employee.deptName}</p></div>
                </a>
            </div> --%>
            <div class="sidebar-fold sidebar-fold_an"></div>
            <div class="nav" id="nav">
            	<input type="hidden" id="menu_status" value="normal"/>
            	<p>
            		<a class="tips_drop" href="javascript:;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="file_store" bundle="${i18n}"/></a>
            		<input value="<fmt:message key="file_store" bundle="${i18n}"/>" type="hidden"/>
            	</p>
            	<div class="down_box">
            	<p>
            		<a href="${ctx }/home/index?nojump=true"><i class="tip_a"></i><fmt:message key="overviews" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="overviews" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <p>
                	<a href="${ctx }/personal/index"><i class="tip_b"></i><fmt:message key="personal_space" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="personal_space" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <c:if test="${sessionScope.employee.commonShareStatus}">
                	<p>
                		<a href="${ctx }/manageshare/index"><i class="tip_m"></i><fmt:message key="management_public_space" bundle="${i18n}"/></a>
                		<input value="<fmt:message key="management_public_space" bundle="${i18n}"/>" type="hidden"/>
                	</p>
                </c:if>
                <p>
                	<a href="${ctx }/comshare/index"><i class="tip_c"></i><fmt:message key="public_space" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="public_space" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <p id="share_left">
                	<a href="${ctx }/share/index" id="share_on"><i class="tip_d"></i><fmt:message key="share_space" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="share_space" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <p>
                	<a href="${ctx }/bshare/index"><i class="tip_e"></i><fmt:message key="shared_space" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="shared_space" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <p>
           			<a href="${ctx }/teamwork/index"><i class="tip_t5"></i><fmt:message key="tw_teamwork" bundle="${i18n}"/></a>
           			<input value="<fmt:message key="tw_teamwork" bundle="${i18n}"/>" type="hidden"/>
          		</p>
                </div>
                <p>
                	<a class="tips_drop" href="javascript:;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="auxiliary_tool" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="auxiliary_tool" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <div class="down_box">
                	<p>
                		<a href="${ctx }/sign/index"><i class="tip_f"></i><fmt:message key="sign_space" bundle="${i18n}"/></a>
                		<input value="<fmt:message key="sign_space" bundle="${i18n}"/>" type="hidden"/>
                	</p>
                	<p>
                		<a href="${ctx }/takecode/index"><i class="tip_g"></i><fmt:message key="takecode_space" bundle="${i18n}"/></a>
                		<input value="<fmt:message key="takecode_space" bundle="${i18n}"/>" type="hidden"/>
                	</p>
                	<p>
                		<a href="${ctx }/lucene/index"><i class="tip_r"></i><fmt:message key="full_text_retrieval" bundle="${i18n}"/></a>
                		<input value="<fmt:message key="full_text_retrieval" bundle="${i18n}"/>" type="hidden"/>
                	</p>
                </div>
                <p>
                	<a class="tips_drop" href="javascript:;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="c_contacts" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="c_contacts" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <div class="down_box">
                	<p>
                		<a href="${ctx }/contact/index"><i class="tip_t3"></i><fmt:message key="contact_my" bundle="${i18n}"/></a>
                		<input value="<fmt:message key="contact_my" bundle="${i18n}"/>" type="hidden"/>
                	</p>
            		<p>
            			<a href="${ctx }/comcontact/index"><i class="tip_t2"></i><fmt:message key="contact_common" bundle="${i18n}"/></a>
            			<input value="<fmt:message key="contact_common" bundle="${i18n}"/>" type="hidden"/>
            		</p>
            		<p>
            			<a href="${ctx }/workgroup/index"><i class="tip_t1"></i><fmt:message key="workgroup" bundle="${i18n}"/></a>
            			<input value="<fmt:message key="workgroup" bundle="${i18n}"/>" type="hidden"/>
           			</p>
            		<p>
            			<a href="${ctx }/wgapply/index"><i class="tip_t4"></i><fmt:message key="workgroup_apply_for" bundle="${i18n}"/></a>
            			<input value="<fmt:message key="workgroup_apply_for" bundle="${i18n}"/>" type="hidden"/>
           			</p>
                </div>
                <p>
                	<a class="tips_drop" href="javascript:;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="user_center" bundle="${i18n}"/></a>
                	<input value="<fmt:message key="user_center" bundle="${i18n}"/>" type="hidden"/>
                </p>
                <div class="down_box">
            		<p>
            			<a href="${ctx }/user/index"><i class="tip_o"></i><fmt:message key="personal_information" bundle="${i18n}"/></a>
            			<input value="<fmt:message key="personal_information" bundle="${i18n}"/>" type="hidden"/>
            		</p>
                	<p>
	                	<a href="${ctx }/message/index"><i class="tip_msg"></i><fmt:message key="message_center" bundle="${i18n}"/></a>
	                	<input value="<fmt:message key="message_center" bundle="${i18n}"/>" type="hidden"/>
                	</p>
					<p>
						<a href="${ctx }/login/loginOut"><i class="tip_s"></i><fmt:message key="login_out" bundle="${i18n}"/></a>
						<input value="<fmt:message key="login_out" bundle="${i18n}"/>" type="hidden"/>
					</p>
				<!-- ${sessionScope.fileServiceUrl } -->
            	</div>
            </div>
            
            
        </div>
    </div>
    <div class="aliyun-console-sidebar-tooltip right" ><div class="tooltip-arrow"></div><div class="tooltip-inner ng-binding" ></div></div>
<script type="text/javascript">
$(function(){
	$(".nav").find('a').removeClass('active');
	$(".nav").find('a').each(function(){
		var h_href =$(this).attr('href');
		if(h_href.indexOf('${namespace}') != -1){
			$(this).addClass('active');
		}
	});
})
</script>