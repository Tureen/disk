<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/personal/index.js?v=20170210"></script>
<script type="text/javascript">
	var fileServiceUrl = '${fileServiceUrl}';
	var identity = '${identity}';
	var token = '${token}';
	var spaceType = '${spaceType}';
	function changeSize() {
	    var width = parseInt($(".tab_contain").val());
	    var height = parseInt($(".tab_contain").val());

	    $(".tab_contain").width(width).height(height);

	    // update scrollbars
	    $('.tab_contain').perfectScrollbar('update');

	    // or even with vanilla JS!
	    Ps.update(document.getElementById('tab_contain'));
	}
	$(function(){
		
		//$('#tab_contain').perfectScrollbar();
	    // with vanilla JS!
	    //Ps.initialize(document.getElementById('tab_contain'));
		
		
		if($("#requestTour").val()==1){
			// Start the tour
			shareTour();
		}
	});
</script>
<style>
.layui-layer-btn{
	padding-top: 10px;
}
</style>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<input type="hidden" value="${permissionStr }" id="permissionStr">
<div id="btn1" style=" width : 10px;
    height : 10px;
    position : absolute;
    left : 50%;
    top : 50%;
    margin-left : -5px; /*一半的高度*/
    margin-top : -5px;  /*一半的宽度*/"></div>
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
        <li class="decompressMenu rightmouse"><a href="javascript:;"><fmt:message key="extract" bundle="${i18n}"/></a></li>
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
	<input type="hidden" id="requestTour" value="${requestScope.tour}">
	<input type="hidden" id="sessionEmployeeId" value="${sessionScope.employee.id}" />
	<input type="hidden" id="folderId" value="${query.folderId}" />
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
                        <div class="crumbs">
                        <ul>
			                <li><i class="tip_a"></i></li>
				            <li><a  href="${ctx}/personal/index"><fmt:message key="personal_space" bundle="${i18n}"/></a></li>
				            <c:forEach items="${folders}" var="folder">
				            	<li><a  class="cutword" title="${folder.folderName }" href="${ctx}/personal/index?folderId=${folder.id}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				             <!-- 排序 -->
                            <li class="array_item"><a class="array_v active" href="javascript:void(0);"></a><a class="array_h" href="javascript:void(0);"></a></li>
				            <c:if test="${not empty query.queryName}">
				                             <li><a  class="cutword" href="#">&nbsp;&gt;&nbsp;<fmt:message key="search" bundle="${i18n}"/>：${query.queryName}</a></li>
				            </c:if>
				            
				            </ul>
			            </div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_folder cancheck btn btn-default"  <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t01"></i><fmt:message key="new_folder" bundle="${i18n}"/></li>
                                <li id="ac_upload_tour" class="ac_upload cancheck" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t02"></i><fmt:message key="upload" bundle="${i18n}"/><i class="ac_down_box"></i>
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
                                <li id="ac_share_tour" class="ac_share"><i class="ac_t12"></i><fmt:message key="share" bundle="${i18n}"/></li>
                                <li class="ac_edit" ><i class="ac_t13"></i><fmt:message key="edit" bundle="${i18n}"/></li>
                                <li class="ac_decpro"><i class="ac_t15"></i><fmt:message key="extract" bundle="${i18n}"/></li>
                            </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/personal/index" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="file_name" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            <div class="content content_an" style="margin-right: 242px;">
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
	                        <th width="50%"><fmt:message key="file_name" bundle="${i18n}"/></th>
                            <th width="10%">&nbsp;</th>
                            <th width="10%"><fmt:message key="size" bundle="${i18n}"/></th>
                            <th width="10%"><fmt:message key="version" bundle="${i18n}"/></th>
                            <th width="15%"><fmt:message key="update_time" bundle="${i18n}"/><span  id="sort" class="date_tip_up"></span></th>
	                    </tr>
	                </table>
	                <div class="tab_contain" id="tab_contain">
	                    <table id="new_tr" style="display:none">
	                        <tr>
	                            <td></td>
	                            <input type="hidden" id="addIndex" value="0" />
	                            <input type="hidden" id="parentId" value="${folder.id}" />
	                            <td colspan="6"><p class="filebox"><i class="tipbg tips_bg_files"></i><span class="edit_name" style="display:inline"><input class="input_filename" type="text" placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="<fmt:message key="new_folder" bundle="${i18n}"/>" maxlength="228"/><a class="yes"  href="javascript:void(0)"
											onclick="createFolder(this);" title="<fmt:message key="ok" bundle="${i18n}"/>" ><fmt:message key="ok" bundle="${i18n}"/></a><a class="cancel no" href="javascript:;" title="<fmt:message key="cancel" bundle="${i18n}"/>"><fmt:message key="cancel" bundle="${i18n}"/></a></span></td>
	                        </tr>
	                    </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                        <c:forEach items="${folder.folders}" var="folder">
		                        <tr class="trfolder">
		                            <td width="5%"><i class="tab_check" name="folderCheck" for="">
		                            <input class="none" type="checkbox" value="${folder.id}"/>
		                            <input type="hidden" name="code" value="${folder.folderCode}"/>
		                            <input type="hidden" name="employeeId" value="${folder.employeeId}" />
		                            <input type="hidden" name="pFolderId" value="${folder.parentId}" /> 
		                            <input type="hidden" class="createTime" value="<fmt:formatDate value="${folder.createTime}" pattern="yyyy-MM-dd HH:mm" />">
		                            </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
			                            	<i class="tipbg tips_bg_files"><c:if test="${folder.shareStatus == 1}"><span class="share"></span></c:if></i>
			                            	<span class="file_name">
			                            		<a class="remove_check" href="${pageContext.request.contextPath }/personal/index?folderId=${folder.id}">${folder.folderName}</a>
		                            		</span>
			                            	<span class="edit_name">
			                            		<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>"  maxlength="228"  value = "${folder.folderName}">
			                                  	<a class="yes" href="javascript:;" onclick="operRename(this,'${folder.id}','1')"><fmt:message key="ok" bundle="${i18n}"/></a>
			                                  	<a class="cancel no edit folder" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
		                                  	</span>
	                                  	</p>
                                  	</td>
			                        <td width="30%"></td>
	                                <td width="10%">-</td>
	                                <td width="10%"><span>-</span></td>
	                                <td width="15%"><fmt:formatDate value="${folder.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
		                        </tr>
	                        </c:forEach>
	                        <c:forEach items="${folder.files}" var="file">
		                        <tr class="trfile">
		                            <td width="5%"><i class="tab_check" name="fileCheck" value="${file.id}" for="">
		                            <input class="none" type="checkbox" value="${file.id}"/>
		                            <input type="hidden" name="folderId" value="${file.folderId}"/>
		                             <input type="hidden" name="employeeId" value="${file.employeeId}" />
		                             <input type="hidden" class="createTime" value="<fmt:formatDate value="${file.createTime}" pattern="yyyy-MM-dd HH:mm" />">
		                             <input type="hidden" class="fileVersion" value="${file.fileVersion }">
		                             <input type="hidden" class="fileVersionNum" value="${file.fileVersionNum }">
		                             <input type="hidden" class="fileType" value="${file.fileType }">
		                            </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
		                            		<i class="tipbg  tips_bg_${file.fileType }"><c:if test="${file.shareStatus == 1}"><span class="share"></span></c:if></i>
		                            		<span class="file_name <c:if test="${file.fileType == 1 }">imageFile</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>" fileId="${file.id}"><a class="remove_check" href="javascript:void(0);">${file.fileName}</a></span>
		                            		<span class="edit_name">
		                            			<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>" maxlength="228" value = "${file.fileName}">
		                            			<a class="yes" href="javascript:;" onclick="operRename(this,'${file.id}','2')"><fmt:message key="ok" bundle="${i18n}"/></a>
		                            			<a class="cancel no edit" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
	                            			</span>
                            			</p>
                           			</td>
		                            <td width="30%">
		                            	<span class="labels">
				                            <c:forEach items="${file.signs }" var="sign">
				                            	<i title="${sign.signName }">${sign.signName }</i>
				                            </c:forEach>
		                            	</span>
	                            	</td>
		                            <td width="10%">${file.showFileSize}</td>
		                            <td width="10%">
	                                    <c:if test="${file.fileVersion >= 10 }">
											<span class="color_blue"><a href="javascript:showVersion('${file.id }','${file.fileVersion }');">V${file.fileVersion/10}</a></span>
										</c:if> 
										<c:if test="${empty file.fileVersion || file.fileVersion == 0 }">
								            <span>V1.0</span>
								        </c:if>
							        </td>
		                            <td width="15%"><fmt:formatDate value="${file.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
		                        </tr>
	                        </c:forEach>
	                    </table> 
	                </div>    
	            </div>
					
					<div class="bottom_txt bottom_txt_an" id="btn2"> <fmt:message key="selected" bundle="${i18n}"/> <i id="fileCount">0</i> <fmt:message key="files" bundle="${i18n}"/>,<i id="folderCount">0 </i><fmt:message key="folders" bundle="${i18n}"/></div>
	        </div>
	        
	        
	        
	        <div class="attr_content">
				<div class="tab">
					<a hidefocus="true" href="javascript:void(0);"
						class="tab1 active"><fmt:message key="pr_attribute" bundle="${i18n}"/></a> <input type="hidden" id="filetype2"
						filetype="myfile">
				</div>
				<!-- 属性 -->
				<div class="right-attr">
					<!-- 关联 -->
					<div class="tabwrap" id="tabnotfile">
						<div class="titeam prop">
							<div id="tabProp" style="display: block">
								<h2 class="title">
									<span class="ico ico_folder" id="filetype1"></span>
									<p>
										<a href="javascript:void(0);" id="filename1"
											onclick="$('#pid').val(7836);$('#showdiv').val('N');$('#sFrm').submit();return false;"><fmt:message key="pr_no_select" bundle="${i18n}"/></a>
									</p>
								</h2>
								<!-- <h3 class="attr-title">
									<i><em class="square"></em></i><span>储存状态</span>
								</h3>
								<div class="attr-detail">
									<span id="storeStatus1">在线储存</span>
								</div> -->
								<h3 class="attr-title">
									<i><em class="square"></em></i><span><fmt:message key="pr_time" bundle="${i18n}"/></span>
								</h3>
								<div class="attr-detail">
									<p>
										<fmt:message key="pr_create_time" bundle="${i18n}"/>：<span id="fileaddtime1"></span>
									</p>
									<p>
										<fmt:message key="pr_update_time" bundle="${i18n}"/>：<span id="fileedittime1"></span>
									</p>
								</div>
								<h3 class="attr-title sign">
									<i><em class="square"></em></i><span><fmt:message key="pr_sign" bundle="${i18n}"/></span>
								</h3>
								<div class="attr-detail sign">
									<span id="Tags1"><fmt:message key="pr_nothing" bundle="${i18n}"/></span>
								</div>
								<h3 class="attr-title version">
									<i><em class="square"></em></i><span><fmt:message key="pr_version_information" bundle="${i18n}"/></span>
								</h3>
								<div class="attr-detail version">
									<p>
										<fmt:message key="pr_current_version" bundle="${i18n}"/>：<span id="fvcurrent"></span>
									</p>
									<p>
										<fmt:message key="pr_version_number" bundle="${i18n}"/>：<span id="fvnum"></span>
									</p>
									<a class="hideFlash" style="color: #0050d7" id="fvoption" href="javascript:void(0);"><fmt:message key="pr_version_operation" bundle="${i18n}"/></a>
									<!-- <span id="Tags1"></span> -->
								</div>
								<h3 class="attr-title share">
									<i><em class="square"></em></i><span><fmt:message key="pr_share" bundle="${i18n}"/></span>
								</h3>
								<div id="competence" class="attr-detail share" >
									<span id="share1"><fmt:message key="pr_nothing" bundle="${i18n}"/></span>
									<h3 class="attr-title manager">
										<span><fmt:message key="pr_manage_authority" bundle="${i18n}"/></span>
									</h3>
									<div class="attr-detail manager" >
										<span id="competence1"><fmt:message key="pr_nothing" bundle="${i18n}"/></span>
									</div>
									<h3 class="attr-title see">
										<span><fmt:message key="pr_view_authority" bundle="${i18n}"/></span>
									</h3>
									<div class="attr-detail see" >
										<span id="competence2"><fmt:message key="pr_nothing" bundle="${i18n}"/></span>
									</div>
									<h3 class="attr-title read">
										<span><fmt:message key="pr_preview_authority" bundle="${i18n}"/></span>
									</h3>
									<div class="attr-detail read" >
										<span id="competence3"><fmt:message key="pr_nothing" bundle="${i18n}"/></span>
									</div>
								</div>
								<!-- <div id="securityLevel" class="none">
									<h3 class="attr-title">
										<i><em class="square"></em></i><span>密级</span>
									</h3>
									<div class="attr-detail" id="securityLevel1"></div>
								</div> -->
							</div>
							<div class="status-loading" style="display: none;">
								<span class="loader-icon"><span class="cue-msg"><fmt:message key="pr_loading" bundle="${i18n}"/></span></span>
							</div>
						</div>
					</div>
				</div>
				<div id="right_no-result"></div>
			</div>
					
					
	    </div>
    </div>
</div>
<!-- 复制到-->
<div class="outlayer con_copy" style="display: none">
    <p class="bold"><span id="operTypeHtml"></span></p>
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
<%@ include file="/WEB-INF/view/include/createcode.jsp"%>
</body>
<!-- 下载隐藏iframe -->
<div class="modal-body" style="display: none">  
    <iframe id="NoPermissioniframe" width="100%" height="50%" frameborder="0"></iframe>  
</div>
</html>