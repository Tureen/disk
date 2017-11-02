<%@page import="com.yunip.config.LocalLanguageHelper"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link href="${plugins}/bootstrap/bootstrap-tour-standalone.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${plugins}/bootstrap/bootstrap-tour-standalone.min.js"></script>
<c:if test="${not empty sessionScope.employee}">
	<c:if test="${empty isIfreame}">
		<iframe style="display: none;" src="${sessionScope.fileServiceUrl}/authorize/login?identity=${sessionScope.identity }&token=${sessionScope.token}&language=<%=LocalLanguageHelper.getLocalLanguage(request) %>"></iframe>
	</c:if>
	<script type="text/javascript">
	$(function(){
		$.post("${ctx}/message/getEmployeeUnreadMessageNum", function(data){
			if(data!=0){
				$("#msgNum").css("background","#ff9900");
			}
			$("#msgNum").html(data);
		});
		
		$("[data-toggle='popover']").popover();
		
		$("#show_tour").click(function(){
			var tour = new Tour({
				  storage: false, //相关可选项目值：window.localStorage(缺省), window.sessionStorage ，false　或者自定义obj
				  template:"<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><div class='popover-navigation'><button class='btn btn-default' data-role='prev'>« "+i18n_previous+"</button><span data-role='separator'></span><button class='btn btn-default' data-role='next'>"+i18n_next+" »</button><button id='endtour' class='btn btn-default' data-role='end'>"+i18n_end+"</button></div></div>",
				  steps:[
				    {element:'#show_tour',placement: "bottom",title:i18n_boot_entry,
				    	content:i18n_boot_entry_one
			    	},
				    {element:'#show_tour',placement: "bottom",title:i18n_boot_entry,
				    	content:''
			    	}
				  ],
				  onStart: function (tour) {
					  if(tour._current==null){
						  $("#tour_0").focus();
					  }
				  },
				  onNext: function (tour) {
					  if(tour._current==0){
						  shareTour();
						  return;
					  }
					  $("#tour_"+(tour._current+1)).focus();
				  },
				  onPrev: function (tour) {
					  $("#tour_"+(tour._current-1)).focus();
				  },
				  onHide: function (tour) {
					  $("#tour_"+(tour._current)).blur();
				  }
					  
				});
				// Initialize the tour
				tour.init();

				// Start the tour
				tour.start();
		});
		
		$("#share_tour").live("click",function(){
			shareTour();
		});
		
		if($("#requestTour").val()==2){
			shareEndTour();
		}
		
	});
	
	function shareTour(){
		var shareTour = new Tour({
			  storage: false, //相关可选项目值：window.localStorage(缺省), window.sessionStorage ，false　或者自定义obj
			  smartPlacement:true,
			  template:"<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><div class='popover-navigation'><button class='btn btn-default' data-role='prev'>« "+i18n_previous+"</button><span data-role='separator'></span><button class='btn btn-default' data-role='next'>"+i18n_next+" »</button><button id='tour_end_second' class='btn btn-default' data-role='end'>"+i18n_end+"</button></div></div>",
			  steps:[
				{element:'.tab_check:eq(1)',placement: "bottom",title:i18n_shared_document_guidelines,
					content:i18n_boot_entry_two,path:basepath+"/personal/index?tour=1&type=0"
				},
			    {element:'#ac_share_tour',placement: "bottom",title:i18n_share_function,
			    	content:i18n_boot_entry_three,backdrop:true
		    	},
			    {element:'',placement: "top",title:'按钮一',
			    	content:''
		    	}
			  ],
			  onStart: function (tour) {
				  //window.location.href=window.location.href+'#heihei';
			  },
			  onShown: function(tour) {
				    $(".tour-backdrop").appendTo(".pContainer");
				    $(".tour-step-background").appendTo(".pContainer");
				    $(".tour-step-background").css("left", "0px");
				},
			  onNext: function (tour) {
				  if($(".tabs .tab_check").length==0){
						alert(i18n_boot_entry_alert);
						window.location.href=basepath+"/personal/index";
				  	}
				  if(tour._current==0){
					  if($(".tab_check:eq(1)").attr("class").indexOf("tab_checked")==-1){
					  	$(".tab_check").eq(1).click();
					  }
				  }
				  if(tour._current==1){
					  $("#ac_share_tour").click();
				  }
			  },
			  onPrev: function (tour) {
				  if(tour._current==1){
					  $(".tab_check").eq(1).click();
				  }
			  },
			  onHide: function (tour) {
			  },
			  onEnd: function (tour) {
				  	//$("#requestTour").val("");
				  	window.location.href=basepath+"/personal/index"
				  }
			});
			// Initialize the tour
			shareTour.init();
			shareTour.start();
	}
	
	function shareEndTour(){
		var shareEndTour = new Tour({
			storage: false,
			smartPlacement:true,
			template:"<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><div class='popover-navigation'><button class='btn btn-default' data-role='prev'>« "+i18n_previous+"</button><span data-role='separator'></span><button class='btn btn-default' data-role='next'>"+i18n_next+" »</button><button id='tour_end_second' class='btn btn-default' data-role='end'>"+i18n_end+"</button></div></div>",
			steps:[
					{element:'#share_left',placement: "right",title:i18n_global_prompt,
						content:i18n_boot_entry_four,backdrop:true
					}
				  ],
				  onShown: function() {
					  /* $("#share_left a").css("border-top-style","solid");
					  $("#share_left a").css("border-bottom-style","solid");
					  $("#share_left a").css("border-width","1px"); */
					},
				  onEnd: function (tour) {
					  	//$("#requestTour").val("");
					  	window.location.href=basepath+"/personal/index"
					  }
			
		});
		shareEndTour.init();
		shareEndTour.start();
	}
	</script>
</c:if>
<fmt:setBundle basename="i18n.myproperties" var="i18n" scope="session"/> 
<div class="header">
    <div class="logo" ></div>
    <div class="top_left">
    <%-- <ul>
    	<li><a>
    		<c:if test="${localvar=='zh_CN'}"><%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.WEB_NAVIGATION.getKey()) %></c:if>
    		<c:if test="${localvar=='en_US'}"><%=com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.BASICSCODE, com.yunip.enums.basics.BasicsInfoCode.WEB_NAVIGATION_ENGLISH.getKey()) %></c:if>
    		</a></li> 
    </ul> --%>
    </div>
    <div class="top_right">
    	
        <ul>
            <!--      <div class="drop_a">
                    <a href="">OA流程</a>
                    <a href="">合同管理</a>
                    <a href="">CRM办公</a>
                </div>  -->
            <c:if test="${not empty sessionScope.employee}">
            	<li onclick="javascript:window.location.href = '${ctx }/message/index';" style="width:80px; "><div class="message_tip"><i id="msgNum" >0</i></div></li>
            	<li onclick="setAjaxCookie( <c:if test="${localvar=='en_US'}">
            								'zh_CN'
											</c:if>
											<c:if test="${localvar=='zh_CN'}">
            									'en_US'
											</c:if>        	
											)">
            	<a  href="javascript:;">
					<c:if test="${localvar=='en_US'}">
						<i class="languagepic_en"></i>
            			English
					</c:if>
					<c:if test="${localvar=='zh_CN'}">
						<i class="languagepic_zh"></i>
            			简体中文
					</c:if>        	
				</a></li>
            	<li><a id="show_tour" href="javascript:;"><i class="helpcenter"></i><fmt:message key="operational_guidelines" bundle="${i18n}"/></a></li>
            	<li><a  href="${ctx }/lucene/index"><i class="fullsearchimg"></i><fmt:message key="retrieval" bundle="${i18n}"/></a></li> 
            	<li><a  href="${ctx }/takecode/pick"><i class="takecode_header"></i><fmt:message key="takecode_header" bundle="${i18n}"/></a></li>
	            <li class="last"><a href="">
			        <img class=top_portrait src="${ctx }/user/portrait" onerror="this.src='${ctx }/static/images/info_photo.jpg'"/>
	            	<i class=""></i>&nbsp;&nbsp;${sessionScope.employee.employeeMobile}<i class="down_tips"></i></a>
	                <div class="drop_a">
	                    <a href="${ctx }/login/loginOut"><fmt:message key="login_out" bundle="${i18n}"/></a>
	                    <input type="hidden" id="fileServer" value="${sessionScope.fileServiceUrl}"/>
	                </div>  
	            </li>
            </c:if>
            <c:if test="${empty sessionScope.employee}">
	            <li class="last"><a href=""><i class="userimg"></i><i><fmt:message key="hello" bundle="${i18n}"/>，<fmt:message key="visitor" bundle="${i18n}"/></i><i class="down_tips"></i></a>
	                <div class="drop_a">
	                    <a href="${ctx }/login/loginOut"  style="text-align: center;text-indent:0px"><fmt:message key="login_button" bundle="${i18n}"/></a>
	                </div>  
	            </li>
            </c:if>
        </ul>
    </div>
</div>