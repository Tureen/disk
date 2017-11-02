/**
 * 文件类型图标
 */
var fileIconList = [
		{
			"type" : "图片",
			"code" : 1,
			"suffix" : [ ".jpg", ".jpeg", ".png", ".bmp" ]
		},
		{
			"type" : "压缩包",
			"code" : 2,
			"suffix" : [ ".zip", ".z", ".tgz", ".tar", ".gtar", ".gz", ".rar" ]
		},
		{
			"type" : "文本",
			"code" : 3,
			"suffix" : [ ".txt", ".c", ".cpp", ".conf", ".h", ".log", ".prop",
					".rc", ".sh", ".xml" ]
		},
		{
			"type" : "office word",
			"code" : 4,
			"suffix" : [ ".doc", ".docx" ]
		},
		{
			"type" : "office excel",
			"code" : 5,
			"suffix" : [ ".xls", ".xlsx" ]
		},
		{
			"type" : "office excel",
			"code" : 6,
			"suffix" : [ ".ppt", ".pps", ".pptx", ".ppsx" ]
		},
		{
			"type" : "wps文字",
			"code" : 7,
			"suffix" : [ ".wps", ".wpt", ".doc", ".dot", ".rtf" ]
		},
		{
			"type" : "wps演示",
			"code" : 8,
			"suffix" : [ ".dps", ".dpt", ".ppt", ".pot", ".pps" ]
		},
		{
			"type" : "wps表格",
			"code" : 9,
			"suffix" : [ ".et", ".ett", ".xls", ".xlt" ]
		},
		{
			"type" : "邮件",
			"code" : 10,
			"suffix" : [ ".msg", ".oft" ]
		},
		{
			"type" : "android安装文件",
			"code" : 11,
			"suffix" : [ ".apk" ]
		},
		{
			"type" : "ios安装文件",
			"code" : 12,
			"suffix" : [ ".ipa", ".deb", ".pxl" ]
		},
		{
			"type" : "动态图",
			"code" : 13,
			"suffix" : [ ".gif" ]
		},
		{
			"type" : "视频文件",
			"code" : 14,
			"suffix" : [ ".avi", ".rmvb", ".rm", ".asf", ".divx", ".mpg",
					".mpeg", ".mpe", ".wmv", ".mp4", ".mkv", ".vob" ]
		}, {
			"type" : "未知类型",
			"code" : 15,
			"suffix" : [ "" ]
		}, {
			"type" : "PDF",
			"code" : 16,
			"suffix" : [ ".pdf" ]
		} ]

/**
 * 计算文件的单位转换
 */
function bytesToSize(bytes) {
	var size = parseFloat(bytes);
	var unitArr = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ];
	var rank = 0;
	while (size > 1000) {
		size = size / 1024;
		rank++;
	}
	var result = size + "";
	if (result.indexOf(".") != -1) {
		if (size > 10) {// 超过10的单位大小文件只显示小数点后一位
			return result.substring(0, result.indexOf(".") + 2) + " "
					+ unitArr[rank];
		} else {
			return result.substring(0, result.indexOf(".") + 3) + " "
					+ unitArr[rank];
		}
	} else {
		return result + " " + unitArr[rank];
	}

}

/**
 * 获得文件后缀名（包含.）
 * 
 * @param fileName
 *            文件名
 */
function getFileSuffixName(fileName) {
	if (fileName == "") {
		return "";
	}
	var start = fileName.lastIndexOf(".");
	var end = fileName.length;
	return fileName.substring(start, end);
}

/**
 * 获得文件后缀名（不包含.）
 * 
 * @param fileName
 *            文件名
 */
function getFileSuffixExtName(fileName) {
	if (fileName == "") {
		return "";
	}
	var start = fileName.lastIndexOf(".");
	var end = fileName.length;
	return fileName.substring(start + 1, end);
}

/**
 * 获得文件图标
 * 
 * @param fileName
 *            文件名
 */
function getFileIcon(fileName) {
	var suffix = getFileSuffixName(fileName);
	for ( var i = 0; i < fileIconList.length; i++) {
		var suffixArr = fileIconList[i].suffix;
		for ( var j = 0; j < suffixArr.length; j++) {
			if (suffix.toLowerCase() == suffixArr[j].toLowerCase()) {
				return fileIconList[i].code;
			}
		}
	}
	return fileIconList[14].code;
}

/**
 * 验证文件类型是否可以上传
 * 
 * @param fileName
 *            文件名
 * @param validateModel
 *            验证模式(0:不进行验证|1:允许上传类型|2.禁止上传类型)
 * @param validateRule(数组)
 *            验证规则
 * @return 返回boolean值(true：验证通过 false:验证不通过)
 */
function validateFileTypeIsUpload(fileName, validateModel, validateRule) {
	var flag = false;
	if (fileName == "") {
		return flag;
	}
	if (validateModel == 0) {// 不验证直接通过
		flag = true;
	} else if (validateModel == 1) {// 允许上传类型
		for ( var i = 0; i < validateRule.length; i++) {
			if (getFileSuffixExtName(fileName).toLowerCase() == validateRule[i]
					.toLowerCase()) {
				flag = true;
				return flag;
			}
		}
	} else if (validateModel == 2) {// 禁止上传类型
		for ( var i = 0; i < validateRule.length; i++) {
			if (getFileSuffixExtName(fileName).toLowerCase() == validateRule[i]
					.toLowerCase()) {
				return flag;
			}
		}
		flag = true;
	}
	return flag;
}

/**
 * 验证文件大小是否可上传
 * 
 * @param fileSize
 *            文件大小
 * @param maxSize
 *            文件允许上传的最大大小(单位MB)
 * @return 返回boolean值(true：验证通过 false:验证不通过)
 */
function validateAllowUploadMaxSize(fileSize, maxSize) {
	if (parseFloat(fileSize) <= parseFloat(maxSize * 1024 * 1024)) {
		return true;
	}
	return false;
}

/**
 * 检测特殊字符
 * 
 * @param str
 *            需要验证的字符
 * @return 返回boolean值(true：验证通过 false:验证不通过)
 */
function illegalChar(str) {
	var pattern = /^[^\\\"\:\?\<\>\|\*]+$/;
	if (str.trim().length == 0) {
		return false;
	} else if (!pattern.test(str)) {
		return false;
	}
	return true;
}

/*******************************************************************************
 * 去除空格
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}