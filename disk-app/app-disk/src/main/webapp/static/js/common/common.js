if( document.all ){//IE下禁止选择
	 document.onselectstart=function(){return false;}
}
//JS操作cookies方法!
//写cookies
function setAjaxCookie(name){
	$.ajax({
			url: basepath+"/login/languagecookie?cookievar="+name,
	        type: 'GET',
	     	contentType:"application/json",
	        async: false, 
	        success: function(result){
	        	window.location.reload();
	        }
		 });
}

//元素改变监听程序
//(function($,h,c){var a=$([]),e=$.resize=$.extend($.resize,{}),i,k="setTimeout",j="resize",d=j+"-special-event",b="delay",f="throttleWindow";e[b]=250;e[f]=true;$.event.special[j]={setup:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.add(l);$.data(this,d,{w:l.width(),h:l.height()});if(a.length===1){g()}},teardown:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.not(l);l.removeData(d);if(!a.length){clearTimeout(i)}},add:function(l){if(!e[f]&&this[k]){return false}var n;function m(s,o,p){var q=$(this),r=$.data(this,d);r.w=o!==c?o:q.width();r.h=p!==c?p:q.height();n.apply(this,arguments)}if($.isFunction(l)){n=l;return m}else{n=l.handler;l.handler=m}}};function g(){i=h[k](function(){a.each(function(){var n=$(this),m=n.width(),l=n.height(),o=$.data(this,d);if(m!==o.w||l!==o.h){n.trigger(j,[o.w=m,o.h=l])}});g()},e[b])}})(jQuery,this); 

/*var r_width1 = 1050;//按钮区正常值
var r_width2 = 174;//查询区域宽度
var r_width3 = 31;//空余的宽度
var r_top1 = 122;//表格及属性区域正常top值
var r_top2 = 162;//按钮区换行后表格top值
var divWidth;

function DivWidth(){
	//按钮区域宽度
	this.btn_width = r_width1;
	//表格top值
	this.table_top = r_top1;
	//属性区域top值
	this.attr_top = r_top1;
	this.change = function(){
		if($(".action_btn li:first")) {
			var btn_top_f = $(".action_btn li:first").offsetParent().css("top");
			var btn_top_l = $(".action_btn li:last").offsetParent().css("top");
			if(btn_top_f == btn_top_l) return;
		}
		var crumbs_width = $(".crumbs").width();
		var temp_width = crumbs_width - r_width2 - r_width3;
		if(temp_width < r_width1){
				// 按钮区确定换行
				this.table_top = r_top2;
				this.attr_top = r_top2;
				this.btn_width = crumbs_width - r_width2 - r_width3;
		} else {
				// 否则回归正常值
				this.btn_width = r_width1;
				this.table_top = r_top1;
				this.attr_top = r_top1;
		} 
		$(".action_btn ul").css("width",this.btn_width);
		$(".content").css("top",this.table_top);
		$(".attr_content").css("top",this.attr_top);
		
	}
}*/

function getCookie(name)
{
var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
if(arr=document.cookie.match(reg))
return unescape(arr[2]);
else
return null;
}

//JS操作cookies方法!
//写cookies
function setCookie(name,value)
{
var Days = 30;
var exp = new Date();
exp.setTime(exp.getTime() + Days*24*60*60*1000);
document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+"; path=/";
}

$(document)
		.ready(
				function() {
					//左侧缩进菜单cookie取值
					if(getCookie("indent")=="reduced"){
						reducedMenu();
					}else{
						normalMenu();
					}
					/*divWidth = new DivWidth();
					divWidth.change();
					
					$(".content").resize(function(){
						divWidth.change();
					});*/
					
					// �����ɫ
					$(".tableOdd").each(function() {
						$(this).find("tr:odd").addClass("tdColor");
					});
					$(".tableOdd").find("tr").each(function() {
						if ($(this).index() != 0) {
							$(this).hover(function() {
								$(this).toggleClass("mouseon")
							})
						}
					});
					$(".tableEven").each(function() {
						$(this).find("tr:even").addClass("tdColor");
					});

					// ��Ŀ������������߶�
					$("#details .item").each(function() {
						var detailsItem = $(this).height();
						$(this).find(".detailsLbor1").height(detailsItem);
					});
					// ��ѡ
					$("#payment .label").each(function() {
						$(this).find("span").click(function() {
							$(this).siblings().removeClass("checked");
							$(this).addClass("checked");
						});
					});
					// ��ѡ
					$(".label,.label2,.label3").each(function() {
						$(this).find("span").click(function() {
							$(this).siblings().removeClass("checked");
							$(this).addClass("checked");
						});
					});

					// 时间排序
					$("#sort")
							.live(
									'click',
									function() {
										if (!isOper) {
											return;
										}
										var val = $(this).attr("class");
										var index = val
												.indexOf("date_tip_down");
										var folderList = new Array();
										var fileList = new Array();
										var newList = new Array();

										$("#tabs")
												.find(".trfolder")
												.each(
														function() {
															folderList
																	.push(new sortTime(
																			$(
																					this)
																					.clone(),
																			$(
																					this)
																					.find(
																							"td :last")
																					.html()));
														});
										$("#tabs").find(".trfile").each(
												function() {
													fileList.push(new sortTime(
															$(this).clone(), $(
																	this).find(
																	"td :last")
																	.html()));
												});
										// 升序
										if (index != -1) {
											folderList
													.sort(function(a, b) {
														if (a.updateTime > b.updateTime) {
															return 1;
														} else {
															return -1;
														}
													});
											fileList
													.sort(function(a, b) {
														if (a.updateTime > b.updateTime) {
															return 1;
														} else {
															return -1;
														}
													});
										}
										// 降序
										else {
											folderList
													.sort(function(a, b) {
														if (a.updateTime < b.updateTime) {
															return 1;
														} else {
															return -1;
														}
													});
											fileList
													.sort(function(a, b) {
														if (a.updateTime < b.updateTime) {
															return 1;
														} else {
															return -1;
														}
													});
										}
										for ( var j = 0; j < folderList.length; j++) {
											newList[j] = folderList[j].tr;
										}
										for ( var j = folderList.length; j < (fileList.length + folderList.length); j++) {
											newList[j] = fileList[j
													- folderList.length].tr;
										}
										var i = 0;
										$("#tabs").find("tr").each(function() {
											$(this).replaceWith($(newList[i]));
											i++;
										});
									})
						$('.sidebar-fold').live('click',function(){
							if($('#menu_status').val() == "reduced")
								normalMenu();
							else if($('#menu_status').val() == "normal")
								reducedMenu();
						});
						
						$(".nav").find("p").each(function(){
							
							
							$(this).hover(
							function(){
								var top = $(this).offset().top;
								var left = $(this).offset().left;
								if($(".sidebar-fold").hasClass("sidebar-fold_an")) return;
								//var scrollTop = $(".navbox").scrollTop();
								var html_ = $(this).find("input:last-child").val();
								$(".tooltip-inner").html(html_);
								$(".aliyun-console-sidebar-tooltip").css("top",top);
								$(".aliyun-console-sidebar-tooltip").show(50);
							},
							function(){
								$(".aliyun-console-sidebar-tooltip").hide();
							}
							);
						});
				});

// 排序对象（更新时间）
function sortTime(tr, updateTime) {
	this.tr = tr;
	this.updateTime = updateTime;
}

//选项卡
function setTab(m,n){
    var menu=document.getElementById("tabT"+m).getElementsByTagName("li");
    var div=document.getElementById("tabB"+m).getElementsByTagName("div");
    var showdiv=[];
    for (i=0; j=div[i]; i++){
        if ((" "+div[i].className+" ").indexOf(" tab_3_con ")!=-1){  
            showdiv.push(div[i]);
        }
    }
    for(i=0;i<menu.length;i++){
        menu[i].className=i==n?"on":"";
        showdiv[i].style.display=i==n?"Block":"none";
    }
}

// �ֲ�ͼ
$(document).ready(function() {
	var on = $(".point li");
	var img = $(".img li");
	// �Զ�����
	var i = 0;
	var len = img.length;
	var timer = setInterval(autoPlay, 5000);
	function autoPlay() {
		if (i == len - 1) {
			i = 0;
		} else {
			i++;
		}
		play(i);
	}
	function play(i) {
		on.removeClass("current");
		img.fadeOut(1000);
		on.eq(i).addClass("current");
		img.eq(i).fadeIn(3000);
	}

	// ����Զ�����
	$(".slide").mouseover(function() {
		clearInterval(timer);
	});
	$(".slide").mouseout(function() {
		timer = setInterval(autoPlay, 5000);
	});

	// ����仯
	on.each(function(index) {
		$(this).click(function() {
			on.removeClass("current");
			img.fadeOut(1000);
			on.eq(index).addClass("current");
			img.eq(index).fadeIn(3000);
		});
	});
});

// �ص�����
$(function() {
	$('.return_top_ico').click(function() {
		// $(window).scrollTop(0);
		var goTop = setInterval(function() {
			$(window).scrollTop($(window).scrollTop() / 1.1)
			if ($(window).scrollTop() < 1)
				clearInterval(goTop);
		}, 8);
		return false;
	})
	$(window).scroll(function() {
		if ($(window).scrollTop() > 400) {
			$('.return_top_ico').fadeIn();
		} else {
			$('.return_top_ico').fadeOut();
		}
	})
})
/** 缩减菜单** */
function reducedMenu(){
	$('.left_part').addClass('left_part_un');
	$('.left_part').removeClass('left_part_an');
	$('.sidebar-fold').addClass('sidebar-fold_un');
	$('.sidebar-fold').removeClass('sidebar-fold_an');
	$('.right_part .main_con').addClass('main_con_un');
	$('.right_part .main_con').removeClass('main_con_an');
	$('.right_part .r_topcon').addClass('r_topcon_un');
	$('.right_part .r_topcon').removeClass('r_topcon_an');
	$('.bottom_txt').addClass('bottom_txt_un');
	$('.bottom_txt').removeClass('bottom_txt_an');
	$('.right_part .content').addClass('content_un');
	$('.right_part .content').removeClass('content_an');
	$('.all_chosebox').addClass('all_chosebox_un');
	$('.all_chosebox').removeClass('all_chosebox_an');
	
	
	$('.right_part .homecontent').addClass('homecontent_un');
	$('.right_part .homecontent').removeClass('homecontent_an');
	$('.zlbg').addClass('zlbg_un');
	$('.zlbg').removeClass('zlbg_an');
	$('.right_part .content_label').addClass('content_label_un');
	$('.right_part .content_label').removeClass('content_label_an');
	
	
	$('#menu_status').val('reduced');
	
	$('.left_part').css('display','block');
	setCookie("indent","reduced");
}

/** 正常菜单** */
function normalMenu(){
	$('.left_part').removeClass('left_part_un');
	$('.left_part').addClass('left_part_an');
	$('.sidebar-fold').removeClass('sidebar-fold_un');
	$('.sidebar-fold').addClass('sidebar-fold_an');
	$('.right_part .main_con').removeClass('main_con_un');
	$('.right_part .main_con').addClass('main_con_an');
	$('.right_part .r_topcon').removeClass('r_topcon_un');
	$('.right_part .r_topcon').addClass('r_topcon_an');
	$('.bottom_txt').removeClass('bottom_txt_un');
	$('.bottom_txt').addClass('bottom_txt_an');
	$('.right_part .content').removeClass('content_un');
	$('.right_part .content').addClass('content_an');
	$('.all_chosebox').removeClass('all_chosebox_un');
	$('.all_chosebox').addClass('all_chosebox_an');
	
	$('.right_part .homecontent').removeClass('homecontent_un');
	$('.right_part .homecontent').addClass('homecontent_an');
	$('.zlbg').removeClass('zlbg_un');
	$('.zlbg').addClass('zlbg_an');
	$('.right_part .content_label').removeClass('content_label_un');
	$('.right_part .content_label').addClass('content_label_an');
	
	$('#menu_status').val('normal');
	
	$('.left_part').css('display','block');
	setCookie("indent","normal");
}

/** 跳转登录页面** */
function gotoLogin() {
	top.location.href = basepath + "/login/index";
}

/** 退出登录** */
function outLogin() {
	top.location.href = basepath + "/login/outlogin";
}

/** *坚持返回码* */
function checkErrorCode(data) {
	if (data.code == 2000) {
		// 服务器异常
		layer.msg(data.codeInfo, {
			icon : 5,
			time : 2000
		});
	} else if (data.code > 2000) {
		$(".ts_box").hide();
		layer.msg(data.codeInfo, {
			icon : 5,
			time : 3000
		});
		return false;
	}
	return true;
}

/** *坚持返回码* */
function checkFileErrorCode(data) {
	if (data.code == 2000) {
		// 服务器异常
		layer.msg(data.codeInfo, {
			icon : 5,
			time : 2000
		});
	} else if (data.code > 2000) {
		//用户取消解压不需要提示
		$(".ts_box").hide();
		if(data.code != 2013){
			layer.msg(data.codeInfo, {
				icon : 5,
				time : 2000
			});
		}
		return false;
	}
	return true;
}


// trim 方法
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, '');
}

/** 跳转登录页面** */
function gotoLoginFail() {
	window.location.href = basepath + "/common/sessionout";
}

/** 禁用操作* */
function disOper() {
	if (isOper) {
		isOper = false;
	}
}

/** 打开操作* */
function openOper() {
	if (!isOper) {
		isOper = true;
	}
}

/**
 * 检测特殊字符
 */
function illegalChar(str) {
	if (str == "null") {
		layer.msg(i18n_name_not_null, {
			icon : 5,
			time : 1500
		});
		return false;
	}
	var pattern = /^[^\/\\\"\:\?\<\>\|\*]+$/;
	if (!pattern.test(str)) {
		layer.msg(i18n_special_characters_cannot_be_included+'<br>&nbsp;\\ \/ : * ? \" <> |', {
			icon : 5,
			time : 1500
		});
		return false;
	} else {
		return checkEmptyChar(str);
	}
}

// 刷新界面
function refresh() {
	window.location.href = window.location.href;
	window.location.reload();
}

// 检验是否为空字符串
function checkEmptyChar(emptyChar) {
	if (emptyChar.trim().length == 0) {
		layer.msg(i18n_space_character, {
			icon : 5,
			time : 1500
		});
		return false;
	}
	return true;
}

messageEvent.add(function (event) {
   if(event.data == 'close'){
	   window.location.reload();
   }
});

//下拉框拖至底部
function scrollDown(id){
	document.getElementById(id).scrollTop = document.getElementById(id).scrollHeight;
}

//下拉框拖至指定位置
function scrollHeight(id, scrollHeight){
	document.getElementById(id).scrollTop = scrollHeight;
}

var GB2312UnicodeConverter = { 
	    ToUnicode: function (str) {
	      return escape(str).toLocaleLowerCase().replace(/%u/gi, '\\u');
	      } 
	    , ToGB2312: function (str) {
	        return unescape(str.replace(/\\u/gi, '%u'));
	      } 
	  };
