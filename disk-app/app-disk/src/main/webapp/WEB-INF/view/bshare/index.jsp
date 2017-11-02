<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/bshare/index.js?v=20170103"></script>
<script type="text/javascript">
	var fileServiceUrl = '${fileServiceUrl}';
	var identity = '${identity}';
	var token = '${token}';
	var spaceType = '${spaceType}';
</script>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<!-- 右键菜单 -->
<div class="right_menu">
    <ul class="first">
        <li class="openMenu rightmouse"><a href="javascript:;"><fmt:message key="open" bundle="${i18n}"/></a></li>
        <li class="ac_down rightmouse"><a href="javascript:;"><fmt:message key="download" bundle="${i18n}"/></a></li>
    </ul>
    <ul>
        <li class="ac_copy rightmouse"><a href="javascript:;"><fmt:message key="copy_to" bundle="${i18n}"/></a></li> 
    </ul>
    <ul>
        <li class="ac_rename rightmouse"><a href="javascript:;"><fmt:message key="rename" bundle="${i18n}"/></a></li>
        <li class="ac_edit rightmouse"><a href="javascript:;"><fmt:message key="edit" bundle="${i18n}"/></a></li> 
    </ul>
</div>
<!-- 提示框 begin -->
<div class="ts_box" style="display: none">
    <i class="ts_tips_loading"></i><span id="showMsg"><fmt:message key="copy_prompt" bundle="${i18n}"/></span>
</div>
<!-- 提示框 over -->
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<input type="hidden" id="sessionEmployeeId" value="${sessionScope.employee.id}" />
    <div class="right_part">
        <div class="main_con main_con_an">
        	<div class="r_topcon r_topcon_an">
        		<div class="actionbox cf">
        			<div class="crumbs">
        			<ul>
        				<li><i class="tip_e"></i></li>
        				<li><a href=""><fmt:message key="shared_space" bundle="${i18n}"/></a></li>
        				<span class="array_item"><a class="array_v active" href="javascript:void(0);"></a><a class="array_h" href="javascript:void(0);"></a></span>
        			</ul>
        			</div>
        			<div class="action_btn">
        				<ul>
                            <li class="ac_down" ><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
                            <li class="ac_copy" ><i class="ac_t06"></i><fmt:message key="copy_to" bundle="${i18n}"/></li>
                            <li class="ac_rename" ><i class="ac_t11"></i><fmt:message key="rename" bundle="${i18n}"/></li>
                            <li class="ac_edit" ><i class="ac_t13"></i><fmt:message key="edit" bundle="${i18n}"/></li>
                        </ul>
        			</div>
        			<input type="hidden" id="operType" value="0" />
        			<div class="r_search_contain">
                       <form action="${ctx}/bshare/listkey" method="post" id="queryForm">
                          	<div class="label_search"><input id="queryName" type="text" placeholder="<fmt:message key="file_name" bundle="${i18n}"/>" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                       </form>
                    </div>
        		</div>
        	</div>
            <div class="content content_an">
                <c:if test="${fn:length(folder.folders) + fn:length(folder.files) == 0}">
	                <div id="noneContent" class="tab_contain noline mt10">
	                    <c:if test="${empty query.queryName}">
			               <p class="nofile"><img src="${staticpath}/images/nofile.png" alt=""><br><span><i></i><fmt:message key="no_files" bundle="${i18n}"/></span></p>
			            </c:if>
			            <c:if test="${not empty query.queryName}">
			               <p class="nofile"><br><span><i><img src="${staticpath}/images/noselect.png" alt=""></i><fmt:message key="query_no_files" bundle="${i18n}"/></span></p>
			            </c:if>
	               </div>
                </c:if>
	            <div id="mainContent" <c:if test="${fn:length(folder.folders) + fn:length(folder.files) > 0}">style="display: block"</c:if>
	            			<c:if test="${fn:length(folder.folders) + fn:length(folder.files) == 0}">style="display: none"</c:if>> <!-- 表格行 -->
	                <table class="tabs_th">
	                    <tr>
	                        <th width="5%"><i class="tab_check" id="allCheck" for=""><input class="none" type="checkbox"></i></th>
	                        <th width="40%"><fmt:message key="file_name" bundle="${i18n}"/></th>
                            <th width="10%">&nbsp;</th>
                            <th width="10%"><fmt:message key="sharer" bundle="${i18n}"/></th>
                            <th width="10%"><fmt:message key="size" bundle="${i18n}"/></th>
                            <th width="10%"><fmt:message key="curr_auth" bundle="${i18n}"/></th>
                            <th width="15%"><fmt:message key="update_time" bundle="${i18n}"/><span  id="sort" class="date_tip_up"></span></th>
	                    </tr>
	                </table>
	                <div class="tab_contain">
						<table id="new_tr" style="display:none">
	                        <tr>
	                            <td></td>
	                            <input type="hidden" id="addIndex" value="0" />
	                            <input type="hidden" id="parentId" value="0" />
	                            <td colspan="6"><p class="filebox"><i class="tipbg tips_bg_files"></i><span class="edit_name" style="display:inline"><input class="input_filename" type="text" placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="<fmt:message key="new_folder" bundle="${i18n}"/>"  maxlength="228"/><a class="yes"  href="javascript:void(0)"
											onclick="createFolder(this);" title="<fmt:message key="ok" bundle="${i18n}"/>" ><fmt:message key="ok" bundle="${i18n}"/></a><a class="cancel no" href="javascript:;" title="<fmt:message key="cancel" bundle="${i18n}"/>"><fmt:message key="cancel" bundle="${i18n}"/></a></span></td>
	                        </tr>
		                </table>
			    		<!-- 表格 -->
		                <table class="tabs" id="tabs">
		                	<c:forEach items="${folder.folders}" var="fo">
			                    <tr class="trfolder">
			                        <td width="5%"><i class="tab_check <c:forEach items="${folderArr}" var="foid"><c:if test="${foid==fo.id}">tab_checked</c:if></c:forEach>" name="folderCheck"  for="">
										<input class="none" type="checkbox" value="${fo.id }">
										<input type="hidden" name="code" value="${fo.folderCode}"/>
										<input type="hidden" name="auth" value="${fo.authorityShare.operAuth }"/>
		                            	<input type="hidden" name="pFolderId" value="${fo.parentId}" />
									</i></td>
			                        <td width="30%">
			                        	<p class="filebox">
			                        		<i class="tipbg tips_bg_files"></i>
			                        		<span class="file_name">
			                        			<a class="remove_check" id="tofo${fo.id }" href="${ctx }/bshare/indexinto/${fo.id}/${fo.id }/${fo.employeeId}">${fo.folderName }</a>
			                        		</span>
			                            	<span class="edit_name">
			                            		<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>" value = "${fo.folderName}"  maxlength="228">
			                                  	<a class="yes" href="javascript:;" onclick="operRename(this,'${fo.id}','1')"><fmt:message key="ok" bundle="${i18n}"/></a>
			                                  	<a class="cancel no edit folder" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
			                                </span>
			                             </p>
			                        </td>
			                        <td width="20%"></td>
			                        <td width="10%">${fo.createAdmin }</td>
			                        <td width="10%">-</td>
									<td width="10%">
										<c:forEach items="${enum }" var="type">
										   	<c:if test="${type.code==fo.authorityShare.operAuth}">
										   		<input type="hidden" value="${type.code}"/><fmt:message key="${type.desc }" bundle="${i18n}"/>
										   	</c:if>
										</c:forEach>
									</td>
			                        <td width="15%"><fmt:formatDate value="${fo.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
			                    </tr>
			                </c:forEach>
			        		<c:forEach items="${folder.files}" var="fi" >
			                    <tr class="trfile">
			                        <td width="5%"><i class="tab_check <c:forEach items="${fileArr}" var="fiid"><c:if test="${fiid==fi.id}">tab_checked</c:if></c:forEach>" name="fileCheck" value="${fi.id}" for="">
										<input class="none" type="checkbox" value="${fi.id }">
										<input type="hidden" name="folderId" value="${fi.folderId}"/>
										<input type="hidden" name="employeeId" value="${fi.employeeId}" />
										<input type="hidden" name="auth" value="${fi.authorityShare.operAuth }"/>
									</i></td>
			                        <td width="30%">
			                        	<p class="filebox">
				                        	<i class="tipbg  tips_bg_${fi.fileType }"></i>
				                        	<span class="file_name <c:if test="${fi.fileType == 1 }">imageFile</c:if><c:if test="${fi.fileType > 1 }">previewFile</c:if>" fileId="${fi.id}"><a class="remove_check" href="javascript:void(0);">${fi.fileName}</a></span>
				                        	<span class="edit_name">
				                        		<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>" maxlength="228" value = "${fi.fileName}">
				                        		<a class="yes" href="javascript:;" onclick="operRename(this,'${fi.id}','2')"><fmt:message key="ok" bundle="${i18n}"/></a>
				                        		<a class="cancel no edit" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
				                        	</span>                   	
				                        </p>
			                        </td>
			                        <td width="20%">
										<span class="labels">
				                            <c:forEach items="${fi.signs }" var="sign">
				                            	<i title="${sign.signName }">${sign.signName }</i>
				                            </c:forEach>
	                            		</span>
			                        </td>
			                        <td width="10%">${fi.createAdmin }</td>
			                        <td width="10%">${fi.showFileSize}</td>
		                            <td width="10%">
	                                    <c:forEach items="${enum }" var="type">
										   	<c:if test="${type.code==fi.authorityShare.operAuth}">
										   		<input type="hidden" value="${type.code}"/><fmt:message key="${type.desc }" bundle="${i18n}"/>
										   	</c:if>
										</c:forEach>
							        </td>			                    
		                            <td width="15%"><fmt:formatDate value="${fi.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
						        </tr>
		                    </c:forEach>
		                </table>
	                </div>
	            </div>
	            <div class="bottom_txt bottom_txt_an"><fmt:message key="selected" bundle="${i18n}"/><i id="fileCount">0</i> <fmt:message key="files" bundle="${i18n}"/>,<i id="folderCount">0</i> <fmt:message key="folders" bundle="${i18n}"/></div>
	    	</div>
        </div>
    </div>
</div>
<!-- 复制到-->
<div class="outlayer con_copy" style="display: none">
    <p class="bold"><span id="operTypeHtml"></span><%-- <fmt:message key="same_files" bundle="${i18n}"/><fmt:message key="select_oper" bundle="${i18n}"/> --%></p>
    <div class="copy_action">
        <div class="copy_file">
            <p><fmt:message key="exist_files" bundle="${i18n}"/>：</p>
            <p id="nowFolder"><i class="tipbg tips_bg_files"></i><span id="nowFolderName"></span></p>
            <p id="nowFile" style="display: none"><i class="tipbg" id="nowFileImg"></i><span id="nowFileName"></span></p>
            <p class="edit_date"><fmt:message key="update_times" bundle="${i18n}"/><span id="nowUpdateTime"></span></p>
        </div>
        <div class="copy_file">
            <p class="pt20"><fmt:message key="copying_files" bundle="${i18n}"/>：</p>
            <p id="oldFolder"><i class="tipbg tips_bg_files"></i><span id="oldFolderName"></span></p>
            <p id="oldFile" style="display: none"><i class="tipbg" id="oldFileImg"></i><span id="oldFileName"></span></p>
            <p class="edit_date"><fmt:message key="update_times" bundle="${i18n}"/><span id="oldUpdateTime"></span></p>
        </div>
    </div>
    <div class="copy_action" id="moreSameName" style="display: none">
       <p><label class="tab_check" id="sameNameCheck" for=""><input class="none" type="checkbox"></label><span class="pl5"><fmt:message key="for_rest" bundle="${i18n}"/><i class="red" id="sySame">0</i><fmt:message key="rename_files" bundle="${i18n}"/><fmt:message key="perform_oper" bundle="${i18n}"/></span></p>
    </div>
</div>
<!-- 删除 -->
<div class="outlayer con_remove_qx" style="display: none">
    <p class="color_7d mt20"><span class="color_31"  id="move"></span></p>
</div>
</body>
</html>