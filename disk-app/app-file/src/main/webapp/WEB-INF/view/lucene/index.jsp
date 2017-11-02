<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="staticpath" value="${ctx}/static" />
<script type="text/javascript">var basepath ='${ctx}';</script>
<c:set var="lucenepath" value="${ctx}/static/js/lucene" />
<c:set var="localvar" value="<%=LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<fmt:setLocale value="${localvar }"/>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<c:if test="${localvar=='zh_CN' }">
	<script type="text/javascript" src="${staticpath}/js/i18n/i18n_zh.js?v=20161118"></script>
</c:if>
<c:if test="${localvar=='en_US' }">
	<script type="text/javascript" src="${staticpath}/js/i18n/i18n_en.js?v=20161118"></script>
</c:if>
<title><fmt:message key="i18n_global_full_text_retrieval" bundle="${i18n}" /></title>
<!--Basic Styles-->
<link rel="stylesheet" href="${lucenepath }/css/bootstrap.min.css">
<link rel="stylesheet" href="${lucenepath }/css/font-awesome.min.css">
<link rel="stylesheet" href="${lucenepath }/css/weather-icons.min.css">
<link rel="stylesheet"
	href="${lucenepath }/css/materialdesignicons.min.css">

<!--Beyond styles-->
<link rel="stylesheet" href="${lucenepath }/css/beyond.min.css">
<link rel="stylesheet" href="${lucenepath }/css/demo.min.css">
<link rel="stylesheet" href="${lucenepath }/css/typicons.min.css">
<link rel="stylesheet" href="${lucenepath }/css/animate.min.css">
<link rel="stylesheet"
	href="${lucenepath }/css/dataTables.bootstrap.css">

<link href="${lucenepath }/css/member_center.css" type="text/css"
	rel="stylesheet">
<link href="${lucenepath }/css/docm_style.css" type="text/css"
	rel="stylesheet">
<link href="${lucenepath }/css/m_con.min.css" type="text/css"
	rel="stylesheet">
<!-- Custom styles for this template -->
<link rel="stylesheet" href="${lucenepath }/css/style.css">
<link rel="stylesheet" href="${lucenepath }/css/style.css">

<style>
* {
	font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial,
		"Microsoft YaHei", FreeSans, Arimo, "Droid Sans",
		"wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3",
		Arial, sans-serif;
}

.form-control {
	display: block;
	width: 90%;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.428571429;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, box-shadow
		ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
}

.btn-group .btn-flat {
	padding: 0 12px;
}

[type=checkbox]+label,[type=radio]+label {
	margin-right: 0px;
}

.pagination li {
	width: auto;
}

[type=checkbox]+label {
	margin-top: 0px;
}
.lucenes{
    width: 60px;
    text-align: center;
    border-color: #4898d5;
    background-color: #2e8ded;
    color: #fff;
    height: 34px;
    line-height: 33px;
    margin: 0 6px;
    padding: 0 15px;
    border: 1px solid #dedede;
    background-color: #f1f1f1;
    color: #333;
    border-radius: 2px;
    font-weight: 400;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    float:left;
}
</style>
</head>

<body>

	<div style="padding-top: 0px;" id="cl-wrapper" class="sb-collapsed">

		<div style="background: #fff; width: auto; padding-right: 0px;"
			class="container-fluid">
			<div class="main-app">
				<div class="head">
					<h2 style="padding-bottom: 10px;"><fmt:message key="i18n_global_full_text_retrieval" bundle="${i18n}" /></h2>
					<form action="${ctx }/lucene/index" id="queryForm" method="post">
					<a class="lucenes" id="lucenes" style="margin-right: 10px;width:75px;"><fmt:message key="i18n_global_search" bundle="${i18n}" /></a>
						<input type="text" name="queryValue"
							placeholder="<fmt:message key="i18n_global_search_describe" bundle="${i18n}" />" value="${page.queryValue }"
							id="keyword" name="keyword" class="form-control"> <input
							type="hidden" id="currentPage" name="currentPage"
							value="${page.currentPage}" />
							
					</form>
					<div class="options ">
						<h5 style="padding: 0px; margin: 0px;"
							class="btn-group pull-right  ">
							<button onclick="down()" disabled="disabled"
								class="btn-flat waves-effect waves-dark disabled" id="btn_down"
								type="button">
								<i class="mdi mdi-download"></i><fmt:message key="i18n_global_download" bundle="${i18n}" />
							</button>
						</h5>
						<div class="form-group"><fmt:message key="i18n_global_search_result1" bundle="${i18n}" />${page.totalRecord}<fmt:message key="i18n_global_search_result2" bundle="${i18n}" />${page.queryTime }<fmt:message key="i18n_global_search_result3" bundle="${i18n}" /></div>
					</div>
				</div>
				<div class="filters">

					<div style="display: inline;">
						<input type="checkbox" class="filled-in" name="checkall"
							onclick="checkall(this)" id="check-all"> <label
							style="vertical-align: -webkit-baseline-middle;" for="check-all"></label>
						<span><fmt:message key="i18n_global_check_all" bundle="${i18n}" /></span>
					</div>


					<div class="clear"></div>
				</div>

				<div class="items products">
					<c:forEach items="${page.items }" var="file">
						<div class="item">
							<div class="checkbox ">
								<input type="checkbox" value="${file.id}" onclick="opshow()"
									class="filled-in" name="checkbox" id="${file.id}"> <label
									for="${file.id}"></label>

							</div>
							<div onclick="selitem(this)">
								<a href="#" <c:if test="${not empty file.filePath}">class="previewFile"</c:if>> <span
									style="vertical-align: text-bottom;"
									class="fa formatspan ${file.fileSuffix}">${file.fileSuffix}</span>
									<h3 class="from">
										${file.fileName}
										<c:if test="${empty file.filePath}">(<fmt:message key="i18n_global_already_delete" bundle="${i18n}" />)</c:if>
										<p class="file-address">
											<c:if test="${!empty file.filePath}">
												<a href="javascript:void(0);" onclick="javascript:window.top.location.href = '${diskServer}/personal/index'"><fmt:message key="i18n_global_personal_space" bundle="${i18n}" /></a>&gt;
												 <c:forEach items="${file.parentFolders }" var="folder">
													<a href="javascript:void(0);" onclick="javascript:window.top.location.href = '${diskServer}/personal/index?folderId=${folder.id}'">${folder.folderName}</a>&gt;
												 </c:forEach>
											</c:if>
										<c:if test="${empty file.filePath}">
										  <fmt:message key="i18n_global_already_delete" bundle="${i18n}" />
										</c:if>
										</p>
									</h3>
								</a>
								<p class="msg">${file.content}</p>
							</div>
						</div>
					</c:forEach>

				</div>
				<div class="pull-right" style="margin-right: 25px;">
					<div>
						<ul class="pagination pagination-lg" id="pagination"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>


	<!--Basic Scripts-->
	<script src="${lucenepath }/js/jquery.min.js"></script>
	<script type="text/javascript" src="${staticpath}/js/layer/layer.js"></script>
	<!-- 弹出层 -->
	<script type="text/javascript"
		src="${staticpath }/js/common.js?v=20160630"></script>

	<script src="${lucenepath }/js/bootstrap.min.js"></script>
	<script src="${lucenepath }/js/jquery.slimscroll.min.js"></script>


	<!--Jquery Select2-->
	<script src="${lucenepath }/js/select2.js"></script>
	<script src="${lucenepath }/js/bootbox.js"></script>

	<!--Bootstrap Date Picker-->
	<script src="${lucenepath }/js/moment.js"></script>
	<script src="${lucenepath }/js/daterangepicker.js"></script>
	<!-- 文档读取类型 -->
	<script src="${lucenepath }/js/fileType.js"></script>

	<script>
		function checkall(e) {
			$("input[name='checkbox']").each(function() {
				this.checked = e.checked;
			});
			opshow();
		}
		function opshow() {
			var uids = [];
			var ucatids = [];
			$("input[name='checkbox']").each(function() {
				if (this.checked) {
					if (this.value != '') {
						var _tmpstr = this.value.split('_')
						uids.push(_tmpstr[1]);
						ucatids.push(_tmpstr[0]);
					}
				}
			});
			if (uids.length > 0) {
				var conids = uids.join(',');
				var catids = ucatids.join(',');
				$('.btn-flat').addClass('disabled');
				$('.btn-flat').attr('disabled', 'disabled');

				$('#btn_down').removeClass('disabled');
				$('#btn_down').removeAttr('disabled');
			} else {
				$('.btn-flat').addClass('disabled');
				$('.btn-flat').attr('disabled', 'disabled');
			}
		}
		
		function selitem(e) {
			$(e).parent().find('input[type="checkbox"]').each(function() {
				this.checked = !this.checked;
			});
			opshow();
		}
	
		function down() {
			var uids = [];
			$("input[name='checkbox']").each(function() {
				if (this.checked) {
					if (this.value != '') {
						uids.push(this.value);
					}
				}
			});
			if (uids.length == 0) {
				window.top.errorshow(i18n_lucene_prompt_1);
				return false;
			}
			layer.msg(i18n_lucene_wait_packing_files, {icon: 16});
			$.ajax({
        		url: basepath+"/fileManager/reqDownloadFile",
                dataType:"json",
                type: 'POST',
                data: JSON.stringify(uids),
                contentType: "application/json",
                async: false, 
                success: function(data){
                	if (data.code == 1000) {
                		// 加载层
                		//layer.closeAll();
                		window.location.href = data.result;
        	        } else {
        	        	layer.msg(data.codeInfo,{icon: 5,time:1000});
        	        }
        	  }}); 
		}
		
	$(function(){
		/**
	 	 * 预览
	 	 */
	 	$(".previewFile").on("click", function(){
	 		var fileId = $(this).parent().parent().prev().find('input').val();
	 		if(fileId == ""){
	 			layer.msg(i18n_lucene_prompt_2,{icon: 5,time:1000});
	 	   		return false;
	 		}
	 		var fileName = $(this).html();
	 		//判断是否为文档文档编辑(true)还是word相关文档编辑(false)
	 		var index = fileName.indexOf(".");
	    	var bool = false;
			if(index > 0){
				var lastFileName = getFileSuffixName(fileName).trim();
				 for(var i in fileType){
	            	if(fileType[i]==lastFileName){
	            		bool = true;
	            		break;
	            	}
	            }
			}
			if(bool){
				layer.open({
	                type: 2,
	                title :i18n_global_file_preview + '-' + fileName,
	                area: ['800px', '600px'],
	                shadeClose: false, //点击遮罩关闭
	                maxmin:true,
	                content: basepath + "/fileManager/onlineEditTxt?params="+ fileId + "&read=0"
	                ,btn: [i18n_global_close]
	            });
			} else {
				window.location.href = basepath + "/fileManager/previewConvert?params=" + fileId;
			}
	 	})
	});
		
	</script>
	<script language="javascript" type="text/javascript">
		document.onkeydown = function(event) {
			var e = event || window.event
					|| arguments.callee.caller.arguments[0];
			if (e && e.keyCode == 13) {
				if ($('#keyword').val() == '') {
					alert(i18n_lucene_input_search_keyword);
					return false;
				}
				$("#queryForm").submit();
			}
		};
		$('#lucenes').click(function(event){
				if ($('#keyword').val() == '') {
					alert(i18n_lucene_input_search_keyword);
					return false;
				}
				$("#queryForm").submit();
		});
	</script>

	<script src="${lucenepath }/js/materialize.min.js"></script>
	<script src="${lucenepath }/js/jqPaginator.js"></script>

	<div class="hiddendiv common"></div>
	<script type="text/javascript">
var totalPage  = '${page.totalPage}';
if(totalPage != 0){
	$.jqPaginator('#pagination', {
	    totalPages: ${page.totalPage},
	    visiblePages: 10,
	    currentPage: ${page.currentPage},
	    <c:if test="${localvar=='zh_CN' }">
	    first: '<li class="first"><a href="javascript:;">首页</a></li>',
	    last: '<li class="last"><a href="javascript:;">尾页</a></li>',
	    prev: '<li class="prev"><a href="javascript:;">上一页</a></li>',
	    next: '<li class="next"><a href="javascript:;">下一页</a></li>',
	    page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
	    </c:if>
	    onPageChange: function (num, type) {
	    	var index = $("#currentPage").val();
	        if(index != num){
	        	$("#currentPage").val(num);
	        	$("#queryForm").submit();
	    	}
	    }
	});
}
</script>
</body>
</html>
