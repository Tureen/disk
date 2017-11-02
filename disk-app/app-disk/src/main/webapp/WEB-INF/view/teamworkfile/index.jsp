<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/teamworkfile/index.js?v=211111"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/websocket-js/swfobject.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/websocket-js/web_socket.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/websocket-js/jquery.WebSocket.js"></script>


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
	
	var isConnectWs = false;//判断是否连接
	
	var WEB_SOCKET_SWF_LOCATION = '${pageContext.request.contextPath}/plugins/websocket-js/WebSocketMain.swf';
	var WEB_SOCKET_DEBUG = true;
	
	var ws;//当前用户websocket对象
	//websocket链接
	function connection(){
		   ws = $.websocket({  
		        domain:document.domain, 
		        protocol:"disk/myecho/" + $("#teamworkId").val() + "/" + $("#sessionEmployeeId").val(), 
		        port:window.location.port,
		        onOpen:function(event){  
		        	isConnectWs = true;
		        },  
		        onError:function(event){
		        	isConnectWs = false;
		        },  
		        onMessage:function(result){
		        	downloadFlag=true;
		        	var messageArray = eval('('+result+')');
		        	if(messageArray != null && messageArray[0]){
		        		var map = messageArray[0];
		        		var mapKey = messageArray[1];
		        		var portraitPath = messageArray[2];
			        	teamworkMessageSend(map, mapKey, portraitPath, 2);
			        	scrollDown('chat_wrapper');
		        	}
		        },
		        onClose:function(event){
		        	ws = null;
			    }
		    });  
	}
	
	window.onbeforeunload = function(){
		if(ws != null){
		   ws.close();
		}
   };
	
	//上传完成，点击退出时，子iframe调用：websocket发布上传信息
	function fMain(teamworkMessageId){ 
		 if(teamworkMessageId != null){
			 //上传完成，点击退出时，
			 sendWebsocket(teamworkMessageId);
		 }
	 }  
	
</script>
<style>
.layui-layer-btn{
	padding-top: 10px;
}
.face{ width:277px; height:120px; overflow-y:auto; background:#fff; padding:10px;border:1px solid #ddd; box-shadow:2px 2px 3px #666;position:absolute;/*绝对定位*/ display:none;/*隐藏*/z-index:99;}

.face ul li{width:22px;height:22px;list-style-type:none;/*去掉圆点*/ float:left;margin:2px; cursor:pointer;}
</style>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
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
        <li class="ac_move rightmouse"><a href="javascript:;"><fmt:message key="move_to" bundle="${i18n}"/></a></li>
        <li class="ac_del rightmouse"><a href="javascript:;"><fmt:message key="delete" bundle="${i18n}"/></a></li>  
        <li class="ac_rename rightmouse"><a href="javascript:;"><fmt:message key="rename" bundle="${i18n}"/></a></li>
        <li class="ac_export rightmouse"><a href="javascript:;"><fmt:message key="tw_export" bundle="${i18n}"/></a></li>
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
	<input type="hidden" id="teamworkId" value="${query.teamworkId}" />
	<input id="employeeAddObjs" type="hidden" value="${employeeAddObjs }">
	<input id="employeeName" type="hidden" value="${employee.employeeName }">
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
                        <div class="crumbs">
                        <ul>
			                <li><i class="tip_t5"></i><a href="${ctx}/teamwork/index"><fmt:message key="tw_teamwork" bundle="${i18n}"/></a></li>
				            <li><a  href="${ctx}/teamworkfile/index?teamworkId=${query.teamworkId}">&nbsp;&gt;&nbsp;${teamworkFolder.teamworkName }</a></li>
				            <c:forEach items="${teamworkFolders}" var="folder">
				            	<li><a  class="cutword" title="${folder.folderName }" href="${ctx}/teamworkfile/index?folderId=${folder.id}&teamworkId=${query.teamworkId}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				             <!-- 排序 -->
				            <c:if test="${not empty query.queryName}">
				                             <li><a  class="cutword" href="#">&nbsp;&gt;&nbsp;<fmt:message key="search" bundle="${i18n}"/>：${query.queryName}</a></li>
				            </c:if>
				            
				            </ul>
			            </div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_folder cancheck btn btn-default"  <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t01"></i><fmt:message key="new_folder" bundle="${i18n}"/></li>
                                <li id="ac_upload_tour" class="ac_upload cancheck" <c:if test="${not empty query.queryName}"> style="display:none"</c:if>><i class="ac_t02"></i><fmt:message key="upload" bundle="${i18n}"/><i class="ac_down_box" style="background-position: right top;"></i>
                                  <div class="ac_downbox" >
                                        <a href="#" id="uploadFile"><fmt:message key="upload_file" bundle="${i18n}"/></a>
                                        <a href="#" id="uploadFolder"><fmt:message key="upload_folder" bundle="${i18n}"/></a>
                                    </div>
                                </li>
                                <li class="ac_down"><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
                                <li class="ac_move"><i class="ac_t05"></i><fmt:message key="move_to" bundle="${i18n}"/></li>
                                <li class="ac_del"><i class="ac_t08"></i><fmt:message key="delete" bundle="${i18n}"/></li>
                                <li class="ac_rename"><i class="ac_t11"></i><fmt:message key="rename" bundle="${i18n}"/></li>
                                <li class="ac_export"><i class="ac_t21"></i><fmt:message key="tw_export" bundle="${i18n}"/></li>
                                <%-- <li class="ac_decpro"><i class="ac_t15"></i><fmt:message key="extract" bundle="${i18n}"/></li> --%>
                            </ul>
                        </div>
                       <input type="hidden" id="operType" value="0" />
                       <div class="r_search_contain">
                         <form action="${ctx}/teamworkfile/index" method="post" id="queryForm">
                            <div class="label_search"><input type="text" placeholder="<fmt:message key="file_name" bundle="${i18n}"/>" id="queryName" name="queryName" value="${query.queryName}"/>
                            <input type="hidden" name="teamworkId" value="${query.teamworkId}" />
							<a class="magnifier" href="javascript:searchFile();"></a></div> 
                         </form>
                      </div>
                </div>  
            </div>
            <div class="content content_an" style="margin-right: 312px;">
                <c:if test="${fn:length(teamworkFolder.teamworkFolders) + fn:length(teamworkFolder.teamworkFiles) == 0}">
	                <div id="noneContent" class="tab_contain noline mt10">
	                    <c:if test="${empty query.queryName}">
			               <p class="nofile"><img src="${staticpath}/images/nofile.png" alt=""><br><span><i></i><fmt:message key="no_files" bundle="${i18n}"/></span></p>
			            </c:if>
			            <c:if test="${not empty query.queryName}">
			               <p class="nofile"><br><span><i><img src="${staticpath}/images/noselect.png" alt=""></i><fmt:message key="query_no_files" bundle="${i18n}"/></span></p>
			            </c:if>
	               </div>
                </c:if>
	            <div id="mainContent" <c:if test="${fn:length(teamworkFolder.teamworkFolders) + fn:length(teamworkFolder.teamworkFiles) > 0}">style="display: block"</c:if>
	            		<c:if test="${fn:length(teamworkFolder.teamworkFolders) + fn:length(teamworkFolder.teamworkFiles) == 0}">style="display: none"</c:if>> <!-- 表格行 -->
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
	                            <input type="hidden" id="parentId" value="${teamworkFolder.id}" />
	                            <td colspan="6"><p class="filebox"><i class="tipbg tips_bg_files"></i><span class="edit_name" style="display:inline"><input class="input_filename" type="text" placeholder="<fmt:message key="new_folder" bundle="${i18n}"/>" value="<fmt:message key="new_folder" bundle="${i18n}"/>" maxlength="228"/><a class="yes"  href="javascript:void(0)"
											onclick="createFolder(this);" title="<fmt:message key="ok" bundle="${i18n}"/>" ><fmt:message key="ok" bundle="${i18n}"/></a><a class="cancel no" href="javascript:;" title="<fmt:message key="cancel" bundle="${i18n}"/>"><fmt:message key="cancel" bundle="${i18n}"/></a></span></td>
	                        </tr>
	                    </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                        <c:forEach items="${teamworkFolder.teamworkFolders}" var="folder">
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
			                            	<i class="tipbg tips_bg_files"></i>
			                            	<span class="file_name">
			                            		<a class="remove_check" href="${pageContext.request.contextPath }/teamworkfile/index?folderId=${folder.id}&teamworkId=${query.teamworkId}">${folder.folderName}</a>
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
	                        <c:forEach items="${teamworkFolder.teamworkFiles}" var="file">
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
		                            		<i class="tipbg  tips_bg_${file.fileType }"></i>
		                            		<span class="file_name <c:if test="${file.fileType == 1 }">imageFile</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>" fileId="${file.id}"><a class="remove_check" href="javascript:void(0);">${file.fileName}</a></span>
		                            		<span class="edit_name">
		                            			<input class="input_filename" type="text" placeholder="<fmt:message key="rename" bundle="${i18n}"/>" maxlength="228" value = "${file.fileName}">
		                            			<a class="yes" href="javascript:;" onclick="operRename(this,'${file.id}','2')"><fmt:message key="ok" bundle="${i18n}"/></a>
		                            			<a class="cancel no edit" href="javascript:;"><fmt:message key="cancel" bundle="${i18n}"/></a>
	                            			</span>
                            			</p>
                           			</td>
		                            <td width="30%">
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
	        
	        <!-- 文件协作信息start -->
			<div class="attr_content" style="overflow-y: hidden; width: 300px; border: 1px solid rgba(0, 0, 0, 0.15);">
				<div class="scrollbar right_sidebar ng-scope" style="width: 300px;">
					<!-- 头部信息 -->
					<div class="head_file_wrapper ng-scope">
						<div class="icon_wrapper">
							<i class="icon-64  icon-folder" size="64" dir="1" filename="bbb" style="background: url(${staticpath}/images/lib_${teamworkFolder.iconStr}.png) no-repeat center;background-size: 65px 65px;">
							</i>
						</div>
						<div class="info">
							<h4 class="title ng-binding dir">${teamworkFolder.teamworkName }</h4>
							<div class="attr">
								<span id="teamworkMember" class="ng-binding ng-scope"></span>
							</div>
						</div>
					</div>
					<!-- 日志信息 -->
					<div id="chat_wrapper" class="chat_wrapper chat-top">
						<!-- 包含文件or文件夹的信息 -->
						<!-- <div class="record-list">
							<dl class="record-list-info">
								<dt class="record-info-desc">
									<span class="record-user">李</span>
									<span class="record-desc">上传了1项内容</span>
									<span class="record-time">10:42</span>
								</dt>
								<dd class="record-info-doc">
									<i class="fileicon-small-pic"></i>
									<span node-type="record-doc" class="record-doc">QQ图片20170223104501.png</span>
								</dd>
							</dl>
						</div> -->
						<!-- <div class="record-date">2017-02-23</div> -->
						<!-- 留言 -->
						<%-- <div class="chat_item c_f_after chat_item_text has_content">
							<div>
								<div class="avatar_wrapper">
									<img src="${ctx }/user/portrait" class="avatar avatar-24 ng-isolate-scope">
								</div>
								<div class="chat_main" style="padding-right: 10px;">
									<div class="chat_member">
										<span class="ng-binding">li</span>
										<span class="ng-binding ng-binding-time">19:00</span>
									</div>
									<div class="chat_text">
										<div>
											<div class="ng-scope">
												<div>
													<div class="chat_text_inner">
														<span class="chat_text_span"> 
															<span class="text">
																<span class="ng-scope">1</span>
															</span>
														</span>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div> --%>
					</div>
				</div>
				<div class="send_msg_control">
					<div class="toolbar_container">
						<ul class="toolbar">
							<li class="ng-scope">
								<span class="tool_btn"> 
									<i id="bq" tooltip="表情" class="icon16x16 icon_emoji"></i>
								</span>
							</li>
							
							<!-- <li class="ng-line">
								<span class="tool_line"> 
									<i id="zhy" tooltip="最后页" class="icon16x16 line_7"></i>
								</span>
							</li>
							<li class="ng-line">
								<span class="tool_line"> 
									<i id="xyy" tooltip="下一页" class="icon16x16 line_1"></i>
								</span>
							</li>
							<li class="ng-line">
								<span class="tool_line"> 
									<i id="syy" tooltip="上一页" class="icon16x16 line_3"></i>
								</span>
							</li>
							<li class="ng-line">
								<span class="tool_line"> 
									<i id="dyy" tooltip="第一页" class="icon16x16 line_5"></i>
								</span>
							</li> -->
						</ul>
					</div>
					<!-- <textarea id="content" ng-style="textareaStyle" cursor-pos="" ng-trim="false"
						insert-pos="" insert-to="insertStr" ng-model="postText"
						focus-me="focusTextarea" input-tip="@"
						input-tip-list="remindMembers" input-tip-placement="top right"
						placeholder="按Enter键发送，Shift+Enter换行，不超过800字"
						class="post_text ng-pristine ng-untouched ng-valid"
						style="text-indent: 0px;"></textarea> -->
					<!-- <div class="message" contentEditable='true'></div> -->
					<div onfocus="if($(this).html()=='<fmt:message key="tw_remark_prompt" bundle="${i18n}"/>'){$(this).html('');this.style.color='#333';}" onblur="if($(this).html()==''){$(this).html('<fmt:message key="tw_remark_prompt" bundle="${i18n}"/>');this.style.color='#999';}" id="content" style="height: 80px;width: 300px;overflow-y:auto;word-wrap: break-word;color: #999" contentEditable='true'><fmt:message key="tw_remark_prompt" bundle="${i18n}"/></div>
				</div>
			</div>
	        <!-- 文件协作信息end -->
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
<%@ include file="/WEB-INF/view/include/emoji.jsp"%>
</body>
<!-- 下载隐藏iframe -->
<div class="modal-body" style="display: none">  
    <iframe id="NoPermissioniframe" width="100%" height="50%" frameborder="0"></iframe>  
</div>
<!-- <iframesrciframesrc="http://192.168.11.71:8088/file/authorize/toTeamworkUploadPage?spaceType=755c8e48738e18d254da8d38d7509c07&folderId=0&teamworkId=6"frameborder="0" name="myframe"></iframe> -->  

</html>