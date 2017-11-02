<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
</head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<link href="${staticpath}/css/common.css?v=20161206" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${staticpath}/js/common/all.js"></script>
<script type="text/javascript" src="${plugins}/layer/layer.js"></script><!-- 弹出层 -->
<script type="text/javascript" src="${plugins}/common/side/context-menu.js"></script>
<script type="text/javascript" src="${staticpath}/js/sign/index.js?v=201608291"></script>
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_zh.js?v=20161122"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/common/i18n_en.js?v=20161122"></script>
</c:if>
<body>

<div class="outlayer" id="con_label">
    <div class="labelbox">
        <p class="p1"><fmt:message key="operate_target" bundle="${i18n}"/><span><i class="tipbg  tips_bg_${file.fileType }"></i>${file.fileName }</span></p>
        <p class="p2"><span class="label_title"><fmt:message key="sign_create" bundle="${i18n}"/></span><%-- <a href="javascript:showNew();"><i><img src="${staticpath }/images/newlabel.png" alt=""></i>新建标签</a> --%></p>
        <div class="newlabel_list" >
        	<input type="hidden" name="fileId" value="${file.id }">
        	<c:forEach items="${signList }" var="fs">
	        	<span><input type="hidden" value="${fs.id }"><i>${fs.signName }</i><i class="label_close"></i></span>
	        </c:forEach>
        </div>
       	<div class="newlabel_newcreate"><input class="input_style02" name="signName" id="signName" type="text" placeholder="<fmt:message key="enter_sign" bundle="${i18n}"/>" maxlength="16"/><a class="btn_all" href="javascript:addSign();"><fmt:message key="ok" bundle="${i18n}"/></a><!-- <a class="btn_all quit" href="javascript:hideNew();">取消</a> --></div>
        <p class="p2"><span class="label_title"><fmt:message key="sign_library" bundle="${i18n}"/></span><b></b></p>
        <div class="labelku"> 
        	<c:forEach items="${allList }" var="sign">
        		<span><input type="hidden" value="${sign.id }"><i class="label_chose">${sign.signName }</i></span>
        	</c:forEach>
        </div>
    </div>
</div>
<script type="text/javascript">
//构建标签库重名判断数组
var strArr = '${strArr}';
var json=eval(strArr); 
var jsonAdd = new Array();
var jsonChoseNum = parseInt('${fn:length(signList)}');

$(function(){
	$(".label_close").live('click', function(){
		var signname = $(this).parent().find("i:first").html();
		var index = $.inArray(signname, jsonAdd);
		if(index != -1){
			jsonAdd.splice(index,1);
		}
		var indexInit = $.inArray(signname, json);
		if(indexInit != -1){
			jsonChoseNum = jsonChoseNum - 1;
		}
		$(this).parent().remove();
	});
	$(".label_chose").live('click', function(){
		if(!checkLength()){
			layer.msg(i18n_sign_addup,{icon: 5,time:1500});
			return;
		}
		var tmpSignId = $(this).parent().find('input').val();
		var tmpSignName = $(this).parent().find('i').html();
		//验证是否已有重复标签
		var index = true;
		$(".newlabel_list").find('span').each(function(){
			var signId = $(this).find('input').val();
			if(signId == tmpSignId){
				layer.msg(i18n_sign_repeat,{icon: 5,time:1500});
				index = false;
			}
		});
		if(index){
			//加入
			$(".newlabel_list").append("<span><input type=\"hidden\" value=\""+tmpSignId+"\"><i>"+tmpSignName+"</i><i class=\"label_close\"></i></span>")
			//添加进临时数
			jsonChoseNum =jsonChoseNum+ 1; 
		}
	});
})
function checkLength(){
	var length = jsonAdd.length+jsonChoseNum;
	if(length>=3){
		return false;
	}
	return true;
}

function showNew(){
	$(".newlabel_newcreate").show();
}
function hideNew(){
	$(".newlabel_newcreate").hide();
}

function addSign(){
	if(!checkLength()){
		layer.msg(i18n_sign_addup,{icon: 5,time:1500});
		return;
	}
	var tmpSignName = $("#signName").val();
	tmpSignName = tmpSignName.replace(/^\s+|\s+$/g,"");
	if(tmpSignName == null || tmpSignName == ''){
		$("#signName").val("");
		return;
	}
	if(tmpSignName == "null"){
		layer.msg(i18n_sign_confirm1,{icon: 5,time:1500}); 
		return;
	}
	var index = $.inArray(tmpSignName, json);
	if(index != -1){
		layer.msg(i18n_sign_confirm2,{icon: 5,time:1500}); 
	    return;
	}
	var indexTmp = $.inArray(tmpSignName, jsonAdd);
	if(indexTmp != -1){
		layer.msg(i18n_sign_confirm2,{icon: 5,time:1500}); 
	    return;
	}
	var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if (patrn.test(tmpSignName)) {
		layer.msg(i18n_sign_confirm3,{icon: 5,time:1500}); 
	    return;
	}
	$(".newlabel_list").append("<span><input type=\"hidden\" value=\"0\"><i>"+$("#signName").val()+"</i><i class=\"label_close\"></i></span>")
	$("#signName").val("");
	//添加进临时数组
	jsonAdd.push(tmpSignName);
}

function choseSign(){
	$(".labelku").find('')	
}

function addFileSign(){
	var jsonArray = new Array();
	$(".newlabel_list").find('span').each(function(){
		var signObj = $(this).find('i')[0];
		var signId = $(this).find('input').val();
		if(signId == undefined || signId == ''){
			signId = 0;
		}
		var jsonSign = {
			id : signId,
			signName : $(signObj).html()
		}
		jsonArray.push(jsonSign);
	});
	var jsonData ={
	    "fileId": ${file.id},
	    "signs": jsonArray
	};
	$.ajax({
   		url: basepath+"/sign/savefile",
           dataType:"json",
           type: 'POST',
           data: JSON.stringify(jsonData),
           contentType: "application/json",
           async: false, 
           success: function(data){
           	if (data.code == 1000) {
   	        }
   	  }}); 
}
</script>
</body>
</html>