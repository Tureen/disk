<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/personal/list.js?v=20170103001"></script>
<script type="text/javascript">
	var fileServiceUrl = '${fileServiceUrl}';
	var identity = '${identity}';
	var token = '${token}';
	var spaceType = '${spaceType}';
</script>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<body>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<input type="hidden" value="${permissionStr }" id="permissionStr">
<!-- 右键菜单 -->
<div class="right_menu">
    <ul class="first">
        <li class="openMenu rightmouse"><a href="javascript:;"><fmt:message key="open" bundle="${i18n}"/></a></li>
        <li class="ac_down rightmouse"><a href="javascript:;"><fmt:message key="download" bundle="${i18n}"/></a></li>
        <li class="ac_share rightmouse"><a href="javascript:;"><fmt:message key="share" bundle="${i18n}"/></a></li>  
    </ul>
    <ul>
        <li class="ac_move rightmouse"><a href="javascript:;"><fmt:message key="move_to" bundle="${i18n}"/></a></li>
        <li class="ac_copy rightmouse"><a href="javascript:;"><fmt:message key="copy_to" bundle="${i18n}"/></a></li> 
    </ul>
    <ul>
        <li class="ac_rename rightmouse"><a href="javascript:;"><fmt:message key="rename" bundle="${i18n}"/></a></li>
        <li class="ac_edit rightmouse"><a href="javascript:;"><fmt:message key="edit" bundle="${i18n}"/></a></li> 
        <li class="ac_decpro rightmouse"><a href="javascript:;"><fmt:message key="extract" bundle="${i18n}"/></a></li>  
    </ul>
    <ul>
        <li class="ac_sign rightmouse"><a href="javascript:;"><fmt:message key="sign" bundle="${i18n}"/></a></li>
        <li class="ac_take rightmouse"><a href="javascript:;"><fmt:message key="extracted_code" bundle="${i18n}"/></a></li>
        <li class="ac_del rightmouse"><a href="javascript:;"><fmt:message key="delete" bundle="${i18n}"/></a></li>  
    </ul>
</div>
<!-- 提示框 begin -->
<div class="ts_box" style="display: none">
    <i class="ts_tips_loading"></i><span id="showMsg"><fmt:message key="copy_prompt" bundle="${i18n}"/></span>
    <span id="taskSpan" style="display: none"><input type="hidden" value=""/><a href="#" id="cancelDecompress"><fmt:message key="cancel" bundle="${i18n}"/></a></span>
</div>
<!-- 提示框 over -->
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<input type="hidden" id="sessionEmployeeId" value="${sessionScope.employee.id}" />
	<input type="hidden" id="folderId" value="${query.folderId}" />
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
                        <div class="crumbs">
                        <ul>
			                <li><i class="tip_a"></i></li>
				            <li><a  href="${ctx}/personal/index?type=1"><fmt:message key="personal_space" bundle="${i18n}"/></a></li>
				            <c:forEach items="${folders}" var="folder">
				            	<li><a  class="cutword" title="${folder.folderName }" href="${ctx}/personal/index?type=1&folderId=${folder.id}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				             <!-- 排序 -->
                            <span class="array_item"><a class="array_v" href="javascript:;"></a><a class="array_h active" href="javascript:;"></a></span>
				            <c:if test="${not empty query.queryName}">
				                  <li><a class="cutword" href="#">&nbsp;&gt;&nbsp;<fmt:message key="search" bundle="${i18n}"/>：${query.queryName}</a></li>
				            </c:if>
				            </ul>
			            </div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_folder cancheck" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t01"></i><fmt:message key="new_folder" bundle="${i18n}"/></li>
                                <li class="ac_upload cancheck" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t02"></i><fmt:message key="upload" bundle="${i18n}"/><i class="ac_down_box"></i>
                                  <div class="ac_downbox">
                                        <a href="#" id="uploadFile"><fmt:message key="upload_file" bundle="${i18n}"/></a>
                                        <a href="#" id="uploadFolder"><fmt:message key="upload_folder" bundle="${i18n}"/></a>
                                    </div>
                                </li>
                                <li class="ac_down"><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
                                <li class="ac_move"><i class="ac_t05"></i><fmt:message key="move_to" bundle="${i18n}"/></li>
                                <li class="ac_copy"><i class="ac_t06"></i><fmt:message key="copy_to" bundle="${i18n}"/></li>
                                <li class="ac_del"><i class="ac_t08"></i><fmt:message key="delete" bundle="${i18n}"/></li>
                                <li class="ac_rename"><i class="ac_t11"></i><fmt:message key="rename" bundle="${i18n}"/></li>
                                <li class="ac_take"><i class="ac_t09"></i><fmt:message key="extracted_code" bundle="${i18n}"/></li>
                                <li class="ac_sign"><i class="ac_t07"></i><fmt:message key="sign" bundle="${i18n}"/></li>
                                <li class="ac_share"><i class="ac_t12"></i><fmt:message key="share" bundle="${i18n}"/></li>
                                <li class="ac_edit" ><i class="ac_t13"></i><fmt:message key="edit" bundle="${i18n}"/></li>
                                <li class="ac_decpro"><i class="ac_t15"></i><fmt:message key="extract" bundle="${i18n}"/></li>
                            </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/personal/index?type=1" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="file_name" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            
            <div class="content content_an" >
            <div class="all_chosebox all_chosebox_an"><label class="tab_check" id="allCheck" for=""><input class="none" type="checkbox"></label><fmt:message key="select_all" bundle="${i18n}"/></div>
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
                
                  <div class="tab_contain">
                   <!-- 排序列表 -->
                   <input type="hidden" id="addIndex" value="0" />
	               <input type="hidden" id="parentId" value="${folder.id}" />
                   <div class="array_list">
                       <ul>
                           <li class = "addFolder" style="display: none">
                               <div class="tips_img tfile"></div>
                               <p class="txt_name" style="display:none;"></p>
                               <div class="editName"><input class="input_filename" type="text"  placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="<fmt:message key="new_folder" bundle="${i18n}"/>" maxlength="228"/><a class="true" href="javascript:void(0)" onclick="createFolder(this);"></a><a class="false" href="#"></a></div>
                           </li>
                            
                           <c:forEach items="${folder.folders}" var="folder">
                           <li>
                               <div class="tips_img tfile" key-value="${folder.id }" >
                                      <input type="hidden" name="code" value="${folder.folderCode}"/>
		                            <input type="hidden" name="employeeId" value="${folder.employeeId}" />
		                            <input type="hidden" name="pFolderId" value="${folder.parentId}" /> 
                               </div>
                               <i name = "folderCheck"></i>
                               <c:if test="${folder.shareStatus == 1}">
                                 <div class="sharetip"></div>
                               </c:if>
                               <p class="txt_name"><a href="${ctx }/personal/index?folderId=${folder.id }">${folder.folderName }</a></p>
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="${folder.folderName }" maxlength="228"/><a class="true"  onclick="operRename(this,'${folder.id}',1);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                            <c:forEach items="${folder.files}" var="file">
	                        <li>
                               <div class="tips_img <c:if test="${file.fileType != 1 }">t${file.fileType}</c:if> <c:if test="${file.fileType == 1 }">imageFile</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>"  key-value ="${file.id }" style="display: table-cell;vertical-align: middle;text-align: center;">
                                    <c:if test="${file.fileType == 1 }">
                                    <img style="max-width:100px;_width:expression(this.width > 100 ? '100px' : this.width);max-height:100px;_height:expression(this.height > 100 ? '100px' : this.height);;vertical-align: middle;" src="${file.serverUrl}/fileManager/readerthumbImage?params=${file.id}"></c:if>
                                    <input type="hidden" name="folderId" value="${file.folderId}"/>
		                            <input type="hidden" name="employeeId" value="${file.employeeId}" />
                               </div>
                               <i name = "fileCheck"></i>
                               <c:if test="${file.shareStatus == 1}">
                                 <div class="sharetip"></div>
                               </c:if>
                               <div class="txt_name"><a href="#" class="<c:if test="${file.fileType == 1 }">imageFileA</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>"  key-value ="${file.id }">${file.fileName}</a></div>
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="${file.fileName }" maxlength="228"/><a class="true" onclick="operRename(this,'${file.id}',2);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                       </ul>
                   </div>
				   </div>
              </div>
	            <div class="bottom_txt bottom_txt_an"> <fmt:message key="selected" bundle="${i18n}"/> <i id="fileCount">0</i> <fmt:message key="files" bundle="${i18n}"/>,<i id="folderCount">0 </i><fmt:message key="folders" bundle="${i18n}"/></div>
	        </div>
	    </div>
    </div>
</div>
<!-- 复制到-->
<div class="outlayer con_copy" style="display: none">
    <p class="bold"><span id="operTypeHtml"></span></p>
    <div class="copy_action">
        <div class="copy_file">
            <p id="exitsFile"></p>
            <p id="nowFolder"><i class="tipbg tips_bg_files"></i><span id="nowFolderName"></span></p>
            <p id="nowFile" style="display: none"><i class="tipbg" id="nowFileImg"></i><span id="nowFileName"></span></p>
            <p class="edit_date"><fmt:message key="update_times" bundle="${i18n}"/>：<span id="nowUpdateTime"></span></p>
        </div>
        <div class="copy_file">
            <p class="pt20" id="copyFile"></p>
            <p id="oldFolder"><i class="tipbg tips_bg_files"></i><span id="oldFolderName"></span></p>
            <p id="oldFile" style="display: none"><i class="tipbg" id="oldFileImg"></i><span id="oldFileName"></span></p>
            <p class="edit_date"><fmt:message key="update_times" bundle="${i18n}"/>：<span id="oldUpdateTime"></span></p>
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
<%@ include file="/WEB-INF/view/include/createcode.jsp"%>
</body>
</html>