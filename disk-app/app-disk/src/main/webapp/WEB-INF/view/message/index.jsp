<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="msgTemplate_1">
	<fmt:message key="i18n_share_message_template_content" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_2">
	<fmt:message key="i18n_file" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_3">
	<fmt:message key="i18n_files" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_4">
	<fmt:message key="i18n_folder" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_5">
	<fmt:message key="i18n_folders" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_6">
	<fmt:message key="w_apply_join_your_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_7">
	<fmt:message key="w_workgroup_application_reviewed" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_8">
	<fmt:message key="w_application_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_9">
	<fmt:message key="w_finding_of_audit" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_10">
	<fmt:message key="w_quit_your_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_11">
	<fmt:message key="w_workgroup_name_lower" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_12">
	<fmt:message key="w_workgroup_your_join_dissolution" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_13">
	<fmt:message key="w_workgroup_name" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_14">
	<fmt:message key="w_operator" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_15">
	<fmt:message key="w_transfer_you_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_16">
	<fmt:message key="w_put_you_join_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="msgTemplate_17">
	<fmt:message key="w_got_you_out_workgroup" bundle="${i18n}" />
</c:set>
<c:set var="applyStatus_1">
	<fmt:message key="待审核" bundle="${i18n}" />
</c:set>
<c:set var="applyStatus_2">
	<fmt:message key="审核通过" bundle="${i18n}" />
</c:set>
<c:set var="applyStatus_3">
	<fmt:message key="审核不通过" bundle="${i18n}" />
</c:set>
<script type="text/javascript" src="${staticpath}/js/message/index.js?v=20170110"></script>
<script type="text/javascript">
	$(function(){
		$("#jump").live("click",function(){
			var fileIds = $(this).parent().find("input:eq(0)").val();
			var folderIds = $(this).parent().find("input:eq(1)").val();
			$("#messageJump").find("input:eq(0)").val(fileIds);
			$("#messageJump").find("input:eq(1)").val(folderIds);
			$("#messageJump").submit();
		});
		$("#jumpApply").live("click",function(){
			var workgroupApplyId = $(this).parent().find("input:eq(0)").val();
			$("#messageJumpApply").find("input:eq(0)").val(workgroupApplyId);
			$("#messageJumpApply").submit();
		});
	});
</script>
<style type="text/css">
.layui-layer-btn{
	border-top:0px !important;
}
</style>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<div class="box_main">
    <%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an">
                <div class="actionbox cf">
                        <div class="crumbs"><ul><li><i class="tip_msg"></i><a href="javascript:void(0);"><fmt:message key="message_center" bundle="${i18n}" /></a></li></ul></div>
                        <div class="action_btn">
                            <ul>
                                <li class="ac_del"><i class="ac_t08"></i><fmt:message key="delete" bundle="${i18n}" /></li>
                            </ul>
                        </div>
                </div>  
            </div>
            <form action="${ctx }/message/index" id = "queryForm" method="post">
            <div class="content content_an">
            	<c:if test="${empty query.list}">
            		<div id="noneContent" class="tab_contain noline mt10">
			       		<p class="nofile"><img src="${staticpath}/images/nomessage.png" /><br><span><i></i><fmt:message key="no_message_cue" bundle="${i18n}" /></span></p>
	               </div>
            	</c:if>
	            <div id="mainContent">
	                <div class="tab_contain" style="height: 100%">
	                	<table class="tabs_th">
		                    <tr>
		                        <th width="5%"><i class="tab_check" id="allCheck"><input class="none" type="checkbox" /></i></th>
	                            <th width="5%">&nbsp;</th>
	                            <th width="65%"><fmt:message key="message_title" bundle="${i18n}" /></th>
	                            <th width="10%"><fmt:message key="message_type" bundle="${i18n}" /></th>
	                            <th width="15%"><fmt:message key="message_time" bundle="${i18n}" /></th>
		                    </tr>
		                </table>
	                    <!-- 表格 -->
	                    <table class="tabs" id="tabs">
	                        <c:forEach items="${query.list}" var="item">
		                        <tr class="trfile stabs">
		                            <td width="5%">
		                            	<i class="tab_check" name="msgItem" value="${item.id}"></i>
		                            </td>
		                            <td width="5%">
		                            	<img width="20px;" height="15px;" src="${staticpath}/images/message_${item.status }.png" />
		                            	<c:if test="${item.status eq 0 }"><input type="hidden" name="unReadMsg" value="${item.id}" /></c:if>
                           			</td>
		                            <td width="65%">
		                            	<c:if test="${item.msgType ==  1}">
			                            	<a href="javascript:;" id="jump" title="${fn:replace(fn:replace(fn:replace(fn:replace(fn:replace(item.content, '向您分享了', msgTemplate_1), '个文件', msgTemplate_3), '个目录', msgTemplate_5), '文件', msgTemplate_2), '目录', msgTemplate_4) }">${fn:replace(fn:replace(fn:replace(item.title, '向您分享了', msgTemplate_1), '个文件', msgTemplate_3), '个目录', msgTemplate_5) }</a>
			                            	<input type="hidden" value="${item.fileIds }">
		        							<input type="hidden" value="${item.folderIds }">
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  3}">
			                            	<a href="javascript:;" id="jumpApply" title="${fn:replace(item.content, '申请加入您的工作组', msgTemplate_6)}">${fn:replace(item.title, '申请加入您的工作组', msgTemplate_6)}</a>
			                            	<input type="hidden" value="${item.commonId }">
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  4}">
			                            	<a href="javascript:;" title="${fn:replace(fn:replace(fn:replace(fn:replace(fn:replace(item.content, '申请的工作组', msgTemplate_8),'审核结果',msgTemplate_9),'待审核',applyStatus_1),'审核通过',applyStatus_2),'审核不通过',applyStatus_3)}">${fn:replace(item.title, '您的工作组申请已审核', msgTemplate_7)}</a>
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  5}">
			                            	<a href="javascript:;" title="${fn:replace(fn:replace(item.content, '退出了您的工作组', msgTemplate_10),'工作组名',msgTemplate_11)}">${fn:replace(item.title, '退出了您的工作组', msgTemplate_10)}</a>
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  6}">
			                            	<a href="javascript:;" title="${fn:replace(fn:replace(fn:replace(item.content, '您所在的工作组被解散', msgTemplate_12),'工作组名',msgTemplate_13),'操作人',msgTemplate_14)}">${fn:replace(item.title, '您所在的工作组被解散', msgTemplate_12)}</a>
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  7}">
			                            	<a href="javascript:;" title="${fn:replace(fn:replace(item.content, '将工作组转让给您', msgTemplate_15),'工作组名',msgTemplate_13)}">${fn:replace(item.title, '将工作组转让给您', msgTemplate_15)}</a>
		                            	</c:if>
		                            	<c:if test="${item.msgType ==  8}">
			                            	<a href="javascript:;" title="${fn:replace(fn:replace(fn:replace(fn:replace(item.content, '将您加入工作组', msgTemplate_16),'将您移除出工作组',msgTemplate_17),'工作组名',msgTemplate_13),'操作人',msgTemplate_14)}">${fn:replace(fn:replace(item.title, '将您加入工作组', msgTemplate_16),'将您移除出工作组',msgTemplate_17)}</a>
		                            	</c:if>
	                            	</td>
	                            	<td width="10%">
	                            		<c:forEach items="${typeList}" var="typeItem"><c:if test="${item.msgType eq typeItem.code }"><fmt:message key="${typeItem.text}" bundle="${i18n}" /></c:if></c:forEach>
	                            	</td>
		                            <td width="15%"><fmt:formatDate value="${item.sendTime}" pattern="yyyy-MM-dd HH:mm" /></td>
		                        </tr>
	                        </c:forEach>
	                    </table> 
	                    <div class="bottom_page bottom_txt"><%@ include file="/WEB-INF/view/include/myPaging.jsp"%></div> 
	                </div>    
	            </div>
	        </div>
	        </form>
	        <form action="${ctx }/bshare/index" id="messageJump" method="post">
	        	<input type="hidden" name="fileIds">
	        	<input type="hidden" name="folderIds">
	        </form>
	        <form action="${ctx }/wgapply/index" id="messageJumpApply" method="post">
	        	<input type="hidden" name="workgroupApplyId">
	        </form>
        </div>
    </div>
</div>
</body>
</html>