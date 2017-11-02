<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/personal/index.js?v=2016081903"></script>
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
        <li class="openMenu"><a href="javascript:;">打开</a></li>
        <li class="ac_down"><a href="javascript:;">下载</a></li>
        <li class="ac_share"><a href="javascript:;">分享</a></li>  
    </ul>
    <ul>
        <li class="ac_move"><a href="javascript:;">移动到</a></li>
        <li class="ac_copy"><a href="javascript:;">复制到</a></li> 
    </ul>
    <ul>
        <li class="ac_rename"><a href="javascript:;">重命名</a></li>
        <li class="ac_edit"><a href="javascript:;">编辑</a></li> 
        <li class="decompressMenu"><a href="javascript:;">解压</a></li>  
    </ul>
    <ul>
        <li class="ac_sign"><a href="javascript:;">标签</a></li>
        <li class="ac_take"><a href="javascript:;">提取码</a></li>
        <li class="ac_del"><a href="javascript:;">删除</a></li>  
    </ul>
</div>
<!-- 提示框 begin -->
<div class="ts_box" style="display: none">
    <i class="ts_tips_loading"></i><span id="showMsg">正在复制文件，请稍后...</span>
    <span id="taskSpan" style="display: none"><input type="hidden" value=""/><a href="#" id="cancelDecompress">取消</a></span>
</div>
<!-- 提示框 over -->
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<input type="hidden" id="sessionEmployeeId" value="${sessionScope.employee.id}" />
	<input type="hidden" id="folderId" value="${query.folderId}" />
    <div class="right_part">
        <div class="main_con"> 
            <div class="r_topcon">
                <div class="actionbox cf">
                        <div class="crumbs">
                        <ul>
			                <li><i class="tip_a"></i></li>
				            <li><a  href="${ctx}/personal/index">我的空间</a></li>
				            <c:forEach items="${folders}" var="folder">
				            	<li><a  class="cutword" title="${folder.folderName }" href="${ctx}/personal/index?folderId=${folder.id}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				             <!-- 排序 -->
                            <span class="array_item"><a class="array_v active" href="javascript:void(0);"></a><a class="array_h" href="javascript:void(0);"></a></span>
				            <c:if test="${not empty query.queryName}">
				                             <li><a  class="cutword" href="#">&nbsp;&gt;&nbsp;搜索：${query.queryName}</a></li>
				            </c:if>
				            
				            </ul>
			            </div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_folder" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t01"></i>新建文件夹</li>
                                <li class="ac_upload" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t02"></i>上传<i class="ac_down_box"></i>
                                  <div class="ac_downbox">
                                        <a href="#" id="uploadFile">上传文件</a>
                                        <a href="#" id="uploadFolder">上传文件夹</a>
                                    </div>
                                </li>
                                
                                <li class="ac_down" style="display:none"><i class="ac_t04"></i>下载</li>
                                <li class="ac_move" style="display:none"><i class="ac_t05"></i>移动</li>
                                <li class="ac_copy" style="display:none"><i class="ac_t06"></i>复制</li>
                                <li class="ac_del" style="display:none"><i class="ac_t08"></i>删除</li>
                                <li class="ac_rename" style="display:none"><i class="ac_t11"></i>重命名</li>
                                <li class="ac_take" style="display:none"><i class="ac_t09"></i>提取码</li>
                                <li class="ac_sign" style="display:none"><i class="ac_t07"></i>标签</li>
                                <li class="ac_share" style="display:none"><i class="ac_t12"></i>分享</li>
                                <li class="ac_edit" style="display:none"><i class="ac_t13"></i>编辑</li>
                                <li class="ac_decpro" style="display:none"><i class="ac_t15"></i>解压</li>
                            </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/personal/index" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="文件名" id="queryName" name="queryName" value="${query.queryName}"/>
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            <div class="content">
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
	            <div id="mainContent" <c:if test="${fn:length(folder.folders) + fn:length(folder.files) > 0}">style="display: block"</c:if>
	            		<c:if test="${fn:length(folder.folders) + fn:length(folder.files) == 0}">style="display: none"</c:if>> <!-- 表格行 -->
	                <table class="tabs_th">
	                    <tr>
	                        <th width="5%"><i class="tab_check" id="allCheck" for=""><input class="none" type="checkbox"></i></th>
	                        <th width="50%">文件名称</th>
                            <th width="10%">&nbsp;</th>
                            <th width="10%">大小</th>
                            <th width="10%">版本</th>
                            <th width="15%">更新时间<span  id="sort" class="date_tip_up"></span></th>
	                    </tr>
	                </table>
	                <div class="tab_contain">
	                    <table id="new_tr" style="display:none">
	                        <tr>
	                            <td></td>
	                            <input type="hidden" id="addIndex" value="0" />
	                            <input type="hidden" id="parentId" value="${folder.id}" />
	                            <td colspan="6"><p class="filebox"><i class="tipbg tips_bg_files"></i><span class="edit_name" style="display:inline"><input class="input_filename" type="text" placeholder="新建文件夹" value="新建文件夹" maxlength="228"/><a class="yes"  href="javascript:void(0)"
											onclick="createFolder(this);" title="确定" >确定</a><a class="cancel no" href="javascript:;" title="取消">取消</a></span></td>
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
		                            </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
			                            	<i class="tipbg tips_bg_files"><c:if test="${folder.shareStatus == 1}"><span class="share"></span></c:if></i>
			                            	<span class="file_name">
			                            		<a class="remove_check" href="${pageContext.request.contextPath }/personal/index?folderId=${folder.id}">${folder.folderName}</a>
		                            		</span>
			                            	<span class="edit_name">
			                            		<input class="input_filename" type="text" placeholder="重命名"  maxlength="228"  value = "${folder.folderName}">
			                                  	<a class="yes" href="javascript:;" onclick="operRename(this,'${folder.id}','1')">确定</a>
			                                  	<a class="cancel no edit folder" href="javascript:;">取消</a>
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
		                            </i></td>
		                            <td width="30%">
		                            	<p class="filebox">
		                            		<i class="tipbg  tips_bg_${file.fileType }"><c:if test="${file.shareStatus == 1}"><span class="share"></span></c:if></i>
		                            		<span class="file_name <c:if test="${file.fileType == 1 }">imageFile</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>" fileId="${file.id}"><a class="remove_check" href="javascript:void(0);">${file.fileName}</a></span>
		                            		<span class="edit_name">
		                            			<input class="input_filename" type="text" placeholder="重命名" maxlength="228" value = "${file.fileName}">
		                            			<a class="yes" href="javascript:;" onclick="operRename(this,'${file.id}','2')">确定</a>
		                            			<a class="cancel no edit" href="javascript:;">取消</a>
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