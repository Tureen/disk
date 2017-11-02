<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<aside class="Hui-aside">
	<input runat="server" id="divScrollValue" type="hidden" value="" />
	<div class="menu_dropdown bk_2">
	    <c:forEach items="${sessionScope.AdminInfo.adminAuthority.permissions}" var="permission">
		    <dl id="menu-admin">
				<dt><i class="Hui-iconfont">${permission.permissionIcon}</i> <span>${permission.permissionName}</span><i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
				<dd>
					<ul>
					     <c:forEach items="${permission.permissions}" var="per">
					        <li><a _href="${ctx}${per.permissionUrl}" data-title="${per.permissionName}" href="javascript:void(0)">${per.permissionName}</a></li>
					     </c:forEach>
					</ul>
				</dd>
			</dl>
	    </c:forEach>
	</div>
</aside>