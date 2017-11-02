/**
 * 浏览器引入插件
 */

/**
 * 提示内容
 */
var tipMsg = i18n_onlineEdit_prompt_1;

var formHtml = '<center>' + 
			   '<form id="MyFile" action="upLoadOfficeFile.jsp" enctype="multipart/form-data">' + 
			   '<input type="button" class="saveFile" id="saveFile" value="' + i18n_global_save_file + '" />' + 
			   '</form>' + 
			   '</center>';

var classid = '01DFB4B4-0E07-4e3f-8B7A-98FD6BFF153F';
var officeControlVersion = '5,0,2,4';

//插件控件
var ntkoPlugPath = basepath + "/static/js/ntkooffice/ofctnewclsid.cab#version=" + officeControlVersion;
// 请勿修改，否则可能出错
var userAgent = navigator.userAgent, 
	rMsie = /(msie\s|trident.*rv:)([\w.]+)/, 
	rFirefox = /(firefox)\/([\w.]+)/, 
	rOpera = /(opera).+version\/([\w.]+)/, 
	rChrome = /(chrome)\/([\w.]+)/, 
	rSafari = /version\/([\w.]+).*(safari)/;
var browser;
var version;
var ua = userAgent.toLowerCase();
function uaMatch(ua) {
	var match = rMsie.exec(ua);
	if (match != null) {
		return { browser : "IE", version : match[2] || "0" };
	}
	var match = rFirefox.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rOpera.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rChrome.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rSafari.exec(ua);
	if (match != null) {
		return { browser : match[2] || "", version : match[1] || "0" };
	}
	if (match != null) {
		return { browser : "", version : "0" };
	}
}
var browserMatch = uaMatch(userAgent.toLowerCase());
if (browserMatch.browser) {
	browser = browserMatch.browser;
	version = browserMatch.version;
}

//document.write(browser);
//document.write(version);


if (browser == "IE" && window.navigator.platform == "Win32"){//IE浏览器
	document.write('<!-- 用来产生编辑状态的ActiveX控件的JS脚本-->   ');
	document.write('<!-- 因为微软的ActiveX新机制，需要一个外部引入的js-->   ');
	document.write(formHtml);
	document.write('<object id="TANGER_OCX" classid="clsid:' + classid + '"');
	document.write('codebase="' + ntkoPlugPath + '" width="100%" height="100%">   ');
	document.write('<param name="IsUseUTF8URL" value="-1">   ');
	document.write('<param name="IsUseUTF8Data" value="-1">   ');
	document.write('<param name="IsShowNetErrorMsg" value="false">   ');
	document.write('<param name="BorderStyle" value="1">   ');
	document.write('<param name="BorderColor" value="14402205">   ');
	document.write('<param name="TitlebarColor" value="15658734">   ');
	//document.write('<param name="isoptforopenspeed" value="0">   ');
	document.write('<param name="TitlebarTextColor" value="0">   ');
	document.write('<param name="MenubarColor" value="14402205">   ');
	document.write('<param name="MenuButtonColor" VALUE="16180947">   ');
	document.write('<param name="MenuBarStyle" value="3">   ');
	document.write('<param name="MenuButtonStyle" value="7">   ');
	document.write('<param name="WebUserName" value="NTKO">   ');
	document.write('<param name="MakerCaption" value="深圳市盛世桃源网络科技有限公司">   ');
	document.write('<param name="MakerKey" value="0AC3C0394590DC9443231D690CBE2400E5B3B81C">   ');
	document.write('<param name="ProductCaption" value="深圳市盛世桃源网络科技有限公司">   ');
	document.write('<param name="ProductKey" value="75432D385B6FB8E2269077FC3ADAB598399F397A">   ');
	document.write('<param name="Caption" value="http://www.mytaoyuan.com">   ');
	document.write('<SPAN STYLE="color:red">' + i18n_onlineEdit_prompt_2 + '</SPAN>   ');
	document.write('</object>');
}else{
	document.write(tipMsg);
}