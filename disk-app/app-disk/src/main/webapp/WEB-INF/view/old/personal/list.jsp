<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<script type="text/javascript" src="${staticpath}/js/personal/list.js?v=20160901"></script>
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
        <li class="ac_decpro"><a href="javascript:;">解压</a></li>  
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
				            <li><a  href="${ctx}/personal/index?type=1">个人空间</a></li>
				            <c:forEach items="${folders}" var="folder">
				            	<li><a  class="cutword" title="${folder.folderName }" href="${ctx}/personal/index?type=1&folderId=${folder.id}">&nbsp;&gt;&nbsp;${folder.folderName }</a></li>
				            </c:forEach>
				             <!-- 排序 -->
                            <span class="array_item"><a class="array_v" href="javascript:;"></a><a class="array_h active" href="javascript:;"></a></span>
				            <c:if test="${not empty query.queryName}">
				                  <li><a class="cutword" href="#">&nbsp;&gt;&nbsp;搜索：${query.queryName}</a></li>
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
                         <form action="${ctx}/personal/index?type=1" method="post" id="queryForm">
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
	               <input type="hidden" id="parentId" value="${folder.id}" />
                   <div class="array_list">
                       <ul>
                           <li class = "addFolder" style="display: none">
                               <div class="tips_img tfile"></div>
                               <p class="txt_name" style="display:none;"></p>
                               <div class="editName"><input class="input_filename" type="text"  placeholder="新建文件夹" value="新建文件夹" maxlength="228"/><a class="true" href="javascript:createFolder();"></a><a class="false" href="#"></a></div>
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
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="新建文件夹" value="${folder.folderName }" maxlength="228"/><a class="true"  onclick="operRename(this,'${folder.id}',1);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                            <c:forEach items="${folder.files}" var="file">
	                        <li>
                               <div class="tips_img <c:if test="${file.fileType != 1 }">t${file.fileType}</c:if> <c:if test="${file.fileType == 1 }">imageFile</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>"  key-value ="${file.id }" style="display: table-cell;vertical-align: middle;text-align: center;">
                                    <c:if test="${file.fileType == 1 }">
                                 <!--    <img src="http://192.168.11.71:8083/file/diskApi/readerImage?params=A222CDAC57231A2FD95FEC422FB6D2A089E76EEF6F4300494200C3B8AC1F1541DDC6F4AF14E90425FD6FC29B48B49C10C7E86F71619293BF3F952C8A9354E8E1705B5065771B0A0E961D8C225D7F13BF120AD9CE90F11A3A6F53448801CDB276DD02275EBD453D21"/> -->
                                    
                                    
                                    <img style="max-width:100px;_width:expression(this.width > 100 ? '100px' : this.width);max-height:100px;_height:expression(this.height > 100 ? '100px' : this.height);;vertical-align: middle;" src="${sessionScope.fileServiceUrl}/api/readerthumbImage?params=${file.filePath}"></c:if>
                                    <input type="hidden" name="folderId" value="${file.folderId}"/>
		                            <input type="hidden" name="employeeId" value="${file.employeeId}" />
                               </div>
                               <i name = "fileCheck"></i>
                               <c:if test="${file.shareStatus == 1}">
                                 <div class="sharetip"></div>
                               </c:if>
                               <div class="txt_name"><a href="#" class="<c:if test="${file.fileType == 1 }">imageFileA</c:if><c:if test="${file.fileType > 1 }">previewFile</c:if>"  key-value ="${file.id }">${file.fileName}</a></div>
                               <div class="editName" style="display: none"><input class="input_filename" type="text"  placeholder="新建文件夹" value="${file.fileName }" maxlength="228"/><a class="true" onclick="operRename(this,'${file.id}',2);"></a><a class="false" href="#"></a></div>
                           </li>
                           </c:forEach>
                       </ul>
                   </div>
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