$(function(){
	/**
	 * 文件上传按钮
	 */
	$("#addFileUploadBtn").click(function(){
		layer_show("文件上传", "http://192.168.11.246:8080/file/authorize/toUploadPage?identity=" + identity + "&token=" + token, null, 500);
	});
});