<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/manageshare/index.js?v=20160901"></script>
<script type="text/javascript">
	var fileServiceUrl = '${fileServiceUrl}';
	var identity = '${identity}';
	var token = '${token}';
	var spaceType = '${spaceType}';
</script>
<body>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<!-- 右键菜单 -->
<div class="right_menu">
    <ul class="first">
        <li class="openMenu"><a href="javascript:;"><fmt:message key="open" bundle="${i18n}"/></a></li>
        <li class="ac_down"><a href="javascript:;"><fmt:message key="download" bundle="${i18n}"/></a></li>
        <li class="ac_share"><a href="javascript:;"><fmt:message key="share" bundle="${i18n}"/></a></li>  
    </ul>
    <ul>
        <li class="ac_move"><a href="javascript:;"><fmt:message key="move_to" bundle="${i18n}"/></a></li>
        <li class="ac_copy"><a href="javascript:;"><fmt:message key="copy_to" bundle="${i18n}"/></a></li> 
    </ul>
    <ul>
        <li class="ac_rename"><a href="javascript:;"><fmt:message key="rename" bundle="${i18n}"/></a></li>
        <li class="ac_edit"><a href="javascript:;"><fmt:message key="edit" bundle="${i18n}"/></a></li> 
	<li class="decompressMenu"><a href="javascript:;">解压</a></li>
    </ul>
    <ul>
        <li class="ac_take"><a href="javascript:;"><fmt:message key="extracted_code" bundle="${i18n}"/></a></li>
        <li class="ac_del"><a href="javascript:;"><fmt:message key="delete" bundle="${i18n}"/></a></li>  
    </ul>
</div>
<!-- 提示框 begin -->
<div class="ts_box" style="display: none">
    <i class="ts_tips_loading"></i><span id="showMsg"><fmt:message key="copy_prompt" bundle="${i18n}"/></span>
</div>
<!-- 提示框 over -->
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<input type="hidden" id="sessionEmployeeId" value="${robotEmployee.id}" />
    <div class="right_part">
        <div class="main_con"> 
            <div class="r_topcon">
                <div class="actionbox cf">
                        <div class="crumbs">
                        <ul>
			                <li><i class="tip_n"></i></li>
				            <li><a href="${ctx}/manageshare/index"><fmt:message key="management_public_space" bundle="${i18n}"/></a></li>
				            <c:forEach items="${folders}" var="folder">
				            	<li><a class="cutword" title="${folder.folderName }" href="${ctx}/manageshare/index?folderId=${folder.id}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				            <c:if test="${not empty query.queryName}">
				                             <li><a href="#" class="cutword" title="${query.queryName}">&nbsp;&gt;&nbsp;搜索：${query.queryName}</a></li>
				            </c:if>
				            <span class="array_item"><a class="array_v active" href="javascript:void(0);"></a><a class="array_h " href="javascript:void(0);"></a></span>
				       	</ul>
			            </div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_folder"><i class="ac_t01"></i><fmt:message key="new_folder" bundle="${i18n}"/></li>
                                <li class="ac_refresh"><i class="ac_t03"></i><fmt:message key="refresh" bundle="${i18n}"/></li>
                                <li class="ac_upload"><i class="ac_t02"></i><fmt:message key="upload" bundle="${i18n}"/><i class="ac_down_box"></i>
                                	<div class="ac_downbox">
                                        <a href="#" id="uploadFile">上传文件</a>
                                        <a href="#" id="uploadFolder">上传文件夹</a>
                                    </div>
                                </li>
                                <li class="ac_down" style="display:none"><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
                                <li class="ac_move" style="display:none"><i class="ac_t05"></i><fmt:message key="move_to" bundle="${i18n}"/></li>
                                <li class="ac_copy" style="display:none"><i class="ac_t06"></i><fmt:message key="copy_to" bundle="${i18n}"/></li>
                                <li class="ac_del" style="display:none"><i class="ac_t08"></i><fmt:message key="delete" bundle="${i18n}"/></li>
                                <li class="ac_rename" style="display:none"><i class="ac_t11"></i><fmt:message key="rename" bundle="${i18n}"/></li>
                                <li class="ac_take" style="display:none"><i class="ac_t09"></i><fmt:message key="extracted_code" bundle="${i18n}"/></li>
                                <li class="ac_share" style="display:none"><i class="ac_t12"></i><fmt:message key="share" bundle="${i18n}"/></li>
                                <li class="ac_edit" style="display:none"><i class="ac_t13"></i><fmt:message key="edit" bundle="${i18n}"/></li>
                            </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/manageshare/index" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="file_name" bundle="${i18n}"/>"  id="queryName" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            <div class="content">
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
	                <div class="tab_contain">
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
	                        <c:forEach items="${folder.folders}" var="fo">
		                        <tr class="trfolder">
		                            <td width="5%"><i class="tab_check" name="folderCheck" for="">
					    <input class="none" type="checkbox" value="${fo.id}"/>
					    <input type="hidden" name="code" value="${fo.folderCode}"/>
					    <input type="hidden" name="employeeId" value="${fo.employeeId}" />
		                            <input type="hidden" name="pFolderId" value="${fo.parentId}" />
					    </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
			                            	<i class="tipbg tips_bg_files"><c:if test="${fo.shareStatus == 1}"><span class="share"></span></c:if></i>
			                            	<span class="file_name">
			                            		<a class="remove_check" href="${pageContext.request.contextPath }/manageshare/index?folderId=${fo.id}">${fo.folderName}</a>
		                            		</span>
			                            	<span class="edit_name">
			                            		<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>"   maxlength="228"  value = "${fo.folderName}">
			                                  	<a class="yes" href="javascript:;" onclick="operRename(this,'${fo.id}','1')"><fmt:message key="ok" bundle="${i18n}"/></a>
			                                  	<a class="cancel no edit folder" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
		                                  	</span>
	                                  	</p>
                                  	</td>
			                        <td width="30%"></td>
	                                <td width="10%">-</td>
	                                <td width="10%"><span>-</span></td>
	                                <td width="15%"><fmt:formatDate value="${fo.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
		                        </tr>
	                        </c:forEach>
	                        <c:forEach items="${folder.files}" var="fi">
		                        <tr class="trfile">
		                            <td width="5%"><i class="tab_check" name="fileCheck" value="${fi.id}" for="">
					    <input class="none" type="checkbox" value="${fi.id}"/>
					    <input type="hidden" name="folderId" value="${fi.folderId}"/>
					    <input type="hidden" name="employeeId" value="${fi.employeeId}" />
					    </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
		                            		<i class="tipbg  tips_bg_${fi.fileType }"><c:if test="${fi.shareStatus == 1}"><span class="share"></span></c:if></i>
		                            		<span class="file_name <c:if test="${fi.fileType == 1 }">imageFile</c:if><c:if test="${fi.fileType > 1 }">previewFile</c:if>" fileId="${fi.id}"><a class="remove_check" href="javascript:void(0);">${fi.fileName}</a></span>
		                            		<span class="edit_name">
		                            			<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>"  maxlength="228" value = "${fi.fileName}">
		                            			<a class="yes" href="javascript:;" onclick="operRename(this,'${fi.id}','2')"><fmt:message key="ok" bundle="${i18n}"/></a>
		                            			<a class="cancel no edit" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
	                            			</span>
                            			</p>
                           			</td>
		                            <td width="30%">
		                            	<span class="labels">
				                            <c:forEach items="${fi.signs }" var="sign">
				                            	<i title="${sign.signName }">${sign.signName }</i>
				                            </c:forEach>
		                            	</span>
	                            	</td>
		                            <td width="10%">${fi.showFileSize}</td>
		                            <td width="10%">
	                                    <c:if test="${fi.fileVersion >= 10 }">
											<span class="color_blue"><a href="javascript:showVersion('${fi.id }','${fi.fileVersion }');">V${fi.fileVersion/10}</a></span>
										</c:if> 
										<c:if test="${empty fi.fileVersion || fi.fileVersion == 0 }">
								            <span>V1.0</span>
								        </c:if>
							        </td>
		                            <td width="15%"><fmt:formatDate value="${fi.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
		                        </tr>
	                        </c:forEach>
	                    </table> 
	                </div>    
	            </div>
	            <div class="bottom_txt"> <fmt:message key="selected" bundle="${i18n}"/> <i id="fileCount">0</i> <fmt:message key="files" bundle="${i18n}"/>,<i id="folderCount">0 </i><fmt:message key="folders" bundle="${i18n}"/></div>
	        </div>
	    </div>
    </div>
</div>
<!-- 复制到-->
<div class="outlayer con_copy" style="display: none">
    <p class="bold"><span id="operTypeHtml">复制</span>的位置已经包含了同名的文件</span class="folderNameClass">夹</span>，请选择你的操作：</p>
    <div class="copy_action">
        <div class="copy_file">
            <p>已有的文件<i class="folderNameClass">夹</i>：</p>
            <p id="nowFolder"><i class="tipbg tips_bg_files"></i><span id="nowFolderName"></span></p>
            <p id="nowFile" style="display: none"><i class="tipbg" id="nowFileImg"></i><span id="nowFileName"></span></p>
            <p class="edit_date">修改日期：<span id="nowUpdateTime"></span></p>
        </div>
        <div class="copy_file">
            <p class="pt20">正在复制的文件<i class="folderNameClass">夹</i>：</p>
            <p id="oldFolder"><i class="tipbg tips_bg_files"></i><span id="oldFolderName"></span></p>
            <p id="oldFile" style="display: none"><i class="tipbg" id="oldFileImg"></i><span id="oldFileName"></span></p>
            <p class="edit_date">修改日期：<span id="oldUpdateTime"></span></p>
        </div>
    </div>
    <div class="copy_action" id="moreSameName" style="display: none">
       <p><label class="tab_check" id="sameNameCheck" for=""><input class="none" type="checkbox"></label><span class="pl5">为其余的<i class="red" id="sySame">0</i>个重名文件<span class="folderNameClass">夹</span>都执行此操作</span></p>
    </div>
</div>
<!-- 删除 -->
<div class="outlayer con_remove_qx" style="display: none">
    <p class="color_7d mt20"><span class="color_31"  id="move"></span></p>
</div>
<!-- 设置提取码2016-6-14 -->
<div class="outlayer con_tiqu" id="con_settqm">
    <p class="color_7d">生成下载链接及提取码发送给其他同事好友</p>
    <div class="set_tqmbg">
       <p class="p1"><a class="btn set_tiqu" href="javascript:void(0);">创建链接</a><input id="effectivetime" onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-\#{%d+1}',isShowToday:false})" class="set_input" type="text" placeholder="有效截止时间"></p>
       <p>1、设置有效时间，限制提取期限。<br/>2、不设置有效时间，无限期提取。</p>
    </div>
</div>
</body>
</html>