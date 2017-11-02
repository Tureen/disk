<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>

<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an">
            <div class="content content_an">
                <div class="tab_contain noline mt10">
                    <div class="loginfail">
                        <i class="img_loginfail"></i>
                        <div>
                            <p class="text1"><fmt:message key="login_timeout" bundle="${i18n}"/></p>
                            <p class="text2"><fmt:message key="sorry_timeout1" bundle="${i18n}"/></p>
                            <p><fmt:message key="sorry_timeout2" bundle="${i18n}"/><a class="color_blue" href="${ctx}/login/index"><fmt:message key="sorry_timeout3" bundle="${i18n}"/></a>><fmt:message key="sorry_timeout4" bundle="${i18n}"/></p>
                        </div>
                    </div>

                </div>
    
            </div>
        </div>
    </div>
</div>
</body>
</html>