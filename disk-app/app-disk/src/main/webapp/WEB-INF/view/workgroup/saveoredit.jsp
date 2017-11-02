<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link href="${staticpath}/css/workgroup.css?v=20170114" rel="stylesheet" type="text/css" />
<style>
.file_title, .popup_row, .ldocName, .popup_list .column_bottom, .select_bar, .preview_mode, .detail .row, .add_contacts .row2 .w160, .cgray, .rename span, .full-text-search span, .new-plan-list li .title {
    height: none;
    line-height: 28px;
}
.select {
    width: 114px;
    background: none;
    border: none;
    padding: 5px 10px;
    cursor: auto;
}
</style>
<script type="text/javascript">
var flag = true;
function addWorkgroup(){
	if(!flag){
		layer.msg(i18n_workgroup_please_do_not_submit,{icon: 5,time:1500});
		return;
	}
	var workgroupName = $("#workgroupName").val();
	if(workgroupName == "" || workgroupName == null){
		layer.msg(i18n_workgroup_fill_in_name,{icon: 5,time:1500});
		return;
	}
	$.ajax({
		url: basepath+"/workgroup/save",
        dataType:"json",
        type: 'POST',
        data:$("#SubForm").serialize(),
        async: false, 
        success: function(data){
        	checkErrorCode(data);
	        if (data.code == 1000) {
	        	flag = false;
	        	layer.msg(i18n_global_operation_success,{icon: 1,time:1500});
	        	setTimeout(function(){
	        		parent.refresh();
	        		//parent.layer.closeAll();
	        	}, 1600);
	        }
        }
	 });
}
</script>
<div class="outlayer con_tiqu" id="con_settqm">
	<form method="post" id="SubForm">
		<input type="hidden" name="id" value="${workgroup.id }">
		<ul class="info_list detail scrollbar" style="height: 380px;">
	      <li class="row"> <span class="w160"><fmt:message key="w_group_type" bundle="${i18n}" />：</span>
	        <div class="dropdown">
	          <input id="aaa" class="text_wg select" type="text" readonly="readonly" value="<fmt:message key="w_workgroup" bundle="${i18n}" />">
	        </div>
	      </li>
	      <li class="row"> <span class="w160"><fmt:message key="w_workgroup_name" bundle="${i18n}" />：</span>
	        <input id="workgroupName" name="workgroupName" class="text_wg w290" type="text" msg="名称长度为1~50个字符" maxlength="50" value="${workgroup.workgroupName }">
	      </li>
	      <li class="row h122"> <span class="w160"><fmt:message key="w_describe" bundle="${i18n}" />：</span>
	        <textarea id="remark" name="remark" class="w290 h110 text_wg" msg="描述长度为0~200个字符" maxlength="200">${workgroup.remark }</textarea>
	      </li>
	      <li class="row"> <span class="w160"><fmt:message key="w_valid_status" bundle="${i18n}" />：</span> <span class="jurisdiction">
				   		<label class="label_wg">
				   		       <input id="display" class="radio" type="radio" value="1" name="validStatus" <c:if test="${workgroup.validStatus==null }">checked="checked"</c:if><c:if test="${workgroup.validStatus==1 }">checked="checked"</c:if> ><fmt:message key="w_valid_status_true" bundle="${i18n}" /></label>
	                    <label class="label_wg">
	                           <input id="hidden" class="radio" type="radio" value="0" name="validStatus" <c:if test="${workgroup.validStatus==0 }">checked="checked"</c:if>><fmt:message key="w_valid_status_false" bundle="${i18n}" />
	                    </label>
	        </span> </li>
	    </ul>
	</form>
</div>