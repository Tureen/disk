<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/fancy.jsp"%>
<script type="text/javascript" src="${plugins}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var fileServiceUrl = '${fileServiceUrl}';

//构建标签库重名判断数组
var strArr = '${strArr}';
var json=eval(strArr); 
</script>
<script type="text/javascript" src="${staticpath}/js/sign/index.js?v=20170104"></script>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<div class="box_main">
   	<%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an">
            <div class="crumbs"><ul><li><i class="tip_f"></i><a href=""><fmt:message key="sign_manager" bundle="${i18n}"/></a></li></ul></div>
            <div class="labels_leftpart">
            	<form action="${ctx}/sign/index" method="post" id="querySign">
	            	<div class="datechose"><input name="startDate" id="startDate" onFocus="WdatePicker({lang:'zh-cn',maxDate:'#F{$dp.$D(\'endDate\',{d:-1});}'})" value="${signQuery.startDate }" class="dates" type="text" readonly="readonly"><span class="label_gang" style="padding: 1px">-</span><input name="endDate" id="endDate" class="dates fr" onFocus="WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'startDate\',{d:1});}'})" type="text" value="${signQuery.endDate }" readonly="readonly"></div>
	                <div class="label_search"><input name="signName" type="text" value="${signQuery.signName }" placeholder="<fmt:message key="sign_name" bundle="${i18n}"/>"><input type="submit" style="cursor: pointer;" class="magnifier searchSign" value=""></div>
            	</form>
                <div class="label_left_list">
                    <a class="sign_new" href="javascript:void(0);"><i class="newfile_label"></i><fmt:message key="sign_create" bundle="${i18n}"/></a>
                </div>
                <div class="labels_list">
                    <ul>
                    	<c:forEach items="${signQuery.list }" var="sign">
                        	<li>
                        		<label class="tab_check" for=""></label>
                        		<c:if test="${fn:length(sign.signName)>20}"><span>${sign.signName }</span></c:if>
                        		<c:if test="${fn:length(sign.signName)<=20 }"><a href="javascript:void(0);">${sign.signName }</a></c:if>
                        		<div class="drop_label">
                        			<input type="hidden" value="${sign.id }"/>
	                                <a class="sign_search" href="javascript:void(0);"><i class="relation_label"></i><fmt:message key="assoc_file" bundle="${i18n}"/></a>
	                                <a class="sign_edit" href="javascript:void(0);"><i class="edit_label"></i><fmt:message key="edit" bundle="${i18n}"/></a>
	                                <a class="sign_remove" href="javascript:void(0);"><i class="del_label"></i><fmt:message key="delete" bundle="${i18n}"/></a>
                            	</div>   
                       		</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="content_label_top">  
                <div class="actionbox cf" style="height:48px;">
                    <div class="action_btn">
                        <ul>    
                            <li class="btn_download_enable"><i class="ac_t04"></i><fmt:message key="download" bundle="${i18n}"/></li>
                            <li class="remove_qx"><i class="ac_t10"></i><fmt:message key="remove_assoc" bundle="${i18n}"/></li>                     
                        </ul>
                    </div>
                    <div class="r_search_contain">
                        	<%-- <div class="label_search"><input name="fileName" type="text" id="fileName" placeholder="文件名" value="${fileSignQuery.fileName }"/><a id="searchFile" class="magnifier" href="javascript:void(0);" ></a></div>  --%>
                       		<!-- <table class="tabs">
                       			<td>
			                       	<span class="labels">
			                           	<i title="1">1</i>
			                       	</span>
		                       	</td>
	                       	</table> -->
                    </div>
                </div>
           </div>
           <div class="content_label content_label_an">  
                
            </div>
            <div class="bottom_txt bottom_txt_an b_txt"><fmt:message key="selected" bundle="${i18n}"/><i id="fileSignCount">0</i><fmt:message key="files" bundle="${i18n}"/></div>
        </div>
    </div>
</div>
<!-- 移除关联 -->
<div class="outlayer con_remove_qx" style="display: none">
    <p class="color_7d mt20"><span class="color_31"  id="move"></span></p>
</div>
<!-- 新建标签 -->
<div class="outlayer con_sign_new">
	<p class="mt10"><input id="sign_new_val" placeholder="<fmt:message key="sign_name" bundle="${i18n}"/>"  type="text" class="input_style01" style="width: 350px;height: 27px" maxlength="16"></p>
	<p class="color_7d mt10"><fmt:message key="sign_lenth" bundle="${i18n}"/></p>
</div>
<!-- 编辑标签 -->
<div class="outlayer con_sign_edit">
	<p class="mt10"><input id="sign_edit_val" placeholder="<fmt:message key="sign_name" bundle="${i18n}"/>" type="text" class="input_style01" style="width: 350px;height: 27px" maxlength="16"></p>
	<p class="color_7d mt10"><fmt:message key="sign_lenth" bundle="${i18n}"/></p>
</div>
</body>
</html>