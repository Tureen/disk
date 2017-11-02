<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<title><fmt:message key="i18n_global_online_edit" bundle="${i18n}" /></title>
<style type="text/css">
.saveFile{
	width:80px;
	height:35px;
	line-height:35px;
	text-align:center;
	background:#4898d5;
	color:#fff;
	border-radius:3px;
	display:inline-block;
	margin: 5px 0px 5px 0px;
}
</style>
</head>
<body>

</body>
<script type="text/javascript" src="${staticpath }/js/ntkooffice/ntkoofficecontrol.js?v=20160622"></script>
<script type="text/javascript">
var OFFICE_CONTROL_OBJ;//控件对象
var fileId = '${fileId}';
var fileName = '${fileName}';

/**
 * 打开文件
 */
function openFile(fileUrl){
	OFFICE_CONTROL_OBJ.OpenFromURL(fileUrl);
}

/**
 * 保存文件
 */
function saveFileToUrl(){
	var result = OFFICE_CONTROL_OBJ.SaveToURL("${ctx}/upload/onlineEditUpLoad", "NTKOFILE", "fileId=" + fileId, fileName, "MyFile");
	if($.trim(result) != ""){//针对IE浏览器提示
		OFFICE_CONTROL_OBJ.ShowTipMessage(i18n_global_prompt_title, result);
	}
}

/**
 * 保存文件完成后触发的事件(只有谷歌和火狐可以回调)
 * code: 0表示成功, 1没有找到保存的URL
 */
function OnSaveToURL(type, code, html){
	if(code == 0){//表示成功
		OFFICE_CONTROL_OBJ.ShowTipMessage(i18n_global_prompt_title, html);
	}else{
		OFFICE_CONTROL_OBJ.ShowTipMessage(i18n_global_prompt_title, i18n_onlineEdit_operate_timeout);
	}
}

$(function(){
	OFFICE_CONTROL_OBJ = document.all("TANGER_OCX");
	openFile("${url}");
	
	$("#saveFile").on('click', function(){
		saveFileToUrl();
	});
});
</script>
</html>