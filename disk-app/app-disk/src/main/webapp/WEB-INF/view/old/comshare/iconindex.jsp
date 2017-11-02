<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/comshare/icon.js?v=20160811"></script>
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
    </ul>
    <ul>
        <li class="ac_copy"><a href="javascript:;"><fmt:message key="copy_to" bundle="${i18n}"/></a></li> 
    </ul>
    <ul>
        <li class="ac_rename"><a href="javascript:;"><fmt:message key="rename" bundle="${i18n}"/></a></li>
        <li class="ac_edit"><a href="javascript:;"><fmt:message key="edit" bundle="${i18n}"/></a></li> 
    </ul>
</div>
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<input type="hidden" id="sessionEmployeeId" value="${sessionScope.employee.id}" />
    <div class="right_part">
        <div class="main_con"> 
            <div class="r_topcon">
                <div class="actionbox cf">
                        <div class="crumbs">
                        	<ul>
			                <li><i class="tip_c"></i></li>
			                <li><a href="">公共空间</a></li>
			                <span class="array_item"><a class="array_v" href="javascript:void(0);"></a><a class="array_h active" href="javascript:void(0);"></a></span>
			                </ul>
			            </div>
                        <div class="action_btn">
                            <ul>
	                            <li class="ac_refresh"><i class="ac_t03"></i><fmt:message key="refresh" bundle="${i18n}"/></li>
	                            <li class="ac_down" style="display:none"><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
	                            <li class="ac_copy" style="display:none"><i class="ac_t06"></i><fmt:message key="copy_to" bundle="${i18n}"/></li>
	                            <li class="ac_rename" style="display:none"><i class="ac_t11"></i><fmt:message key="rename" bundle="${i18n}"/></li>
	                            <li class="ac_edit" style="display:none"><i class="ac_t13"></i><fmt:message key="edit" bundle="${i18n}"/></li>
	                        </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/comshare/indexkey" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="文件名" id="queryName" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            <div class="all_chosebox"><label class="tab_check" id="allCheck" for=""><input class="none" type="checkbox"></label>全选</div>
            <div class="content" style="top:152px">
                <c:if test="${fn:length(folder.folders) + fn:length(folder.files) == 0}">
	                <div id="noneContent" class="tab_contain noline mt10">
	                    <c:if test="${empty query.queryName}">
			               <p class="nofile"><img src="${staticpath}/images/nofile.png" alt=""><br><span><i></i>当前目录还没有文件！</span></p>
			            </c:if>
			            <c:if test="${not empty query.queryName}">
			               <p class="nofile"><br><span><i><img src="${staticpath}/images/noselect.png" alt=""></i>没有查询到相关文件夹或者文件！</span></p>
			            </c:if>
	               </div>
                </c:if>
                
                  <div class="tab_contain">
                   <!-- 排序列表 -->
                   <input type="hidden" id="addIndex" value="0" />
                   <input type="hidden" id="parentId" value="0" />
                   <div class="array_list">
                       <ul>
                           <li class = "addFolder" style="display: none">
                               <div class="tips_img tfile"></div>
                               <p class="txt_name" style="display:none;"></p>
                               <div class="editName"><input class="input_filename" type="text"  placeholder="新建文件夹" value="新建文件夹" maxlength="228"/><a class="true" href="javascript:createFolder();"></a><a class="false" href="#"></a></div>
                           </li>
                            
                           <c:forEach items="${folder.folders}" var="fo">
                           <li>
                               <div class="tips_img tfile" key-value="${fo.id }">
                                     <input class="none" type="checkbox" value="${fo.id }">
									<input type="hidden" name="code" value="${fo.folderCode}"/>
									<input type="hidden" name="auth" value="${fo.authorityShare.operAuth }"/>
		                            <input type="hidden" name="pFolderId" value="${fo.parentId}" />
                               </div>
                               <i name = "folderCheck"  code="authorityCheck"></i>
                               <c:if test="${fo.shareStatus == 1}">
                                 <div class="sharetip"></div>
                               </c:if>
                               <p class="txt_name"><a href="${ctx }/comshare/indexinto/${fo.id}/${fo.id }/${fo.employeeId}" id="tofo${fo.id }">${fo.folderName }</a></p>
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="新建文件夹" value="${fo.folderName }" maxlength="228"/><a class="true"  onclick="operRename(this,'${fo.id}',1);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                            <c:forEach items="${folder.files}" var="fi">
	                        <li>
                               <div class="tips_img <c:if test="${fi.fileType != 1 }">t${fi.fileType}</c:if> <c:if test="${fi.fileType == 1 }">imageFile</c:if><c:if test="${fi.fileType > 1 }">previewFile</c:if>"  key-value ="${fi.id }" style="display: table-cell;vertical-align: middle;text-align: center;">
                                    <c:if test="${fi.fileType == 1 }">
                                    <img style="max-width:100px;_width:expression(this.width > 100 ? '100px' : this.width);max-height:100px;_height:expression(this.height > 100 ? '100px' : this.height);;vertical-align: middle;" src="${sessionScope.fileServiceUrl}/api/readerthumbImage?params=${fi.filePath}"></c:if>
                                    <input class="none" type="checkbox" value="${fi.id }">
									<input type="hidden" name="folderId" value="${fi.folderId}"/>
									<input type="hidden" name="employeeId" value="${fi.employeeId}" />
									<input type="hidden" name="auth" value="${fi.authorityShare.operAuth }"/>
                               </div>
                               <i name = "fileCheck"  code="authorityCheck"></i>
                               <c:if test="${fi.shareStatus == 1}">
                                 <div class="sharetip"></div>
                               </c:if>
                               <div class="txt_name"><a href="#" class="<c:if test="${fi.fileType == 1 }">imageFile</c:if><c:if test="${fi.fileType > 1 }">previewFile</c:if>"  key-value ="${fi.id }" id="filename${fi.id }">${fi.fileName}</a></div>
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="新建文件夹" value="${fi.fileName }" maxlength="228"/><a class="true" onclick="operRename(this,'${fi.id}',2);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                       </ul>
                   </div>
              	</div>
	            <div class="bottom_txt"> 您已选了<i id="fileCount">0</i>个文件，<i id="folderCount">0</i>个目录</div>
	        </div>
	    </div>
    </div>
</div>
<!-- 复制到-->
<div class="outlayer con_copy" style="display: none">
    <p class="bold"><span id="operTypeHtml"></p>
    <div class="copy_action">
        <div class="copy_file">
            <p id="exitsFile"></p>
            <p id="nowFolder"><i class="tipbg tips_bg_files"></i><span id="nowFolderName"></span></p>
            <p id="nowFile" style="display: none"><i class="tipbg" id="nowFileImg"></i><span id="nowFileName"></span></p>
            <p class="edit_date">修改日期：<span id="nowUpdateTime"></span></p>
        </div>
        <div class="copy_file">
            <p class="pt20" id="copyFile"></p>
            <p id="oldFolder"><i class="tipbg tips_bg_files"></i><span id="oldFolderName"></span></p>
            <p id="oldFile" style="display: none"><i class="tipbg" id="oldFileImg"></i><span id="oldFileName"></span></p>
            <p class="edit_date">修改日期：<span id="oldUpdateTime"></span></p>
        </div>
    </div>
    <div class="copy_action" id="moreSameName" style="display: none">
       <p><label class="tab_check" id="sameNameCheck" for=""><input class="none" type="checkbox"></label><span class="pl5">为其余的<i class="red" id="sySame">0</i>个重名文件都执行此操作</span></p>
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