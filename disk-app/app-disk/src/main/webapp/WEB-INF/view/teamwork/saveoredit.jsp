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
function addTeamwork(){
	if(!flag){
		layer.msg(i18n_workgroup_please_do_not_submit,{icon: 5,time:1500});
		return;
	}
	var teamworkName = $("#teamworkName").val();
	if(teamworkName == "" || teamworkName == null){
		layer.msg(i18n_workgroup_fill_in_name,{icon: 5,time:1500});
		return;
	}
	$.ajax({
		url: basepath+"/teamwork/save",
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

$(function(){
	$(".team_logo_img").click(function(){
		var iconType = $(this).attr("iconType");
		var src = $(this).attr("src");
		$("#imgIcon").attr("src", src);
		$("#icon").val(iconType);
	});
});
</script>
<div class="outlayer con_tiqu" id="con_settqm">
	<form method="post" id="SubForm">
		<input type="hidden" name="id" value="${teamwork.id }">
		<ul class="info_list detail scrollbar">
		  <li class="h123">
		  	<div class="team_logo page">
		        <h2 class="head"><strong><fmt:message key="tw_check_teamwork_icon" bundle="${i18n}"/></strong></h2>
		
		        <div class="team_body">
		            <div class="team_logo_list">
		                <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_a.png" iconType="1">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_b.png" iconType="2">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_c.png" iconType="3">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_d.png" iconType="4">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_e.png" iconType="5">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_f.png" iconType="6">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_g.png" iconType="7">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_h.png" iconType="8">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_i.png" iconType="9">
		                    </a>
		                    <a href="javascript:;">
		                        <img class="team_logo_img" src="${staticpath}/images/lib_j.png" iconType="10">
		                    </a>
		                                </div>
		            <div class="team_logo_choose">
	            		<img id="imgIcon" src="<c:if test="${teamwork.icon != null }">${staticpath}/images/lib_${teamwork.iconStr}.png</c:if><c:if test="${teamwork.icon == null }">${staticpath}/images/lib_a.png</c:if>" class="team_logo_choose">
	            		<input id="icon" type="hidden" name="icon" value="<c:if test="${teamwork.icon != null }">${teamwork.icon}</c:if><c:if test="${teamwork.icon == null }">1</c:if>">
		            </div>
		        </div>
		        
		    </div>
		  </li>
	      <li class="row"> <span class="w160"><fmt:message key="tw_teamwork_name" bundle="${i18n}"/>：</span>
	        <input id="teamworkName" name="teamworkName" class="text_wg w290" type="text" msg="名称长度为1~50个字符" maxlength="50" value="${teamwork.teamworkName }">
	      </li>
	      <li class="row h122"> <span class="w160"><fmt:message key="w_describe" bundle="${i18n}" />：</span>
	        <textarea id="remark" name="remark" class="w290 h110 text_wg" msg="描述长度为0~200个字符" maxlength="200">${teamwork.remark }</textarea>
	      </li>
	      <input type="hidden" value="1" name="validStatus">
	      <%--  <li class="row"> <span class="w160"><fmt:message key="w_valid_status" bundle="${i18n}" />：</span> <span class="jurisdiction">
				   		<label class="label_wg">
				   		       <input id="display" class="radio" type="radio" value="1" name="validStatus" <c:if test="${teamwork.validStatus==null }">checked="checked"</c:if><c:if test="${teamwork.validStatus==1 }">checked="checked"</c:if> ><fmt:message key="w_valid_status_true" bundle="${i18n}" /></label>
	                    <label class="label_wg">
	                           <input id="hidden" class="radio" type="radio" value="0" name="validStatus" <c:if test="${teamwork.validStatus==0 }">checked="checked"</c:if>><fmt:message key="w_valid_status_false" bundle="${i18n}" />
	                    </label>
	        </span> </li>  --%>
	    </ul>
	</form>
</div>