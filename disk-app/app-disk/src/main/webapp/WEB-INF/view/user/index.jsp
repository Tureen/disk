<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<script type="text/javascript" src="${plugins}/des/des.js"></script>
<link rel="stylesheet" href="${plugins}/Jcrop/css/jquery.Jcrop.css" type="text/css" />
<script type="text/javascript" src="${plugins}/Jcrop/js/jquery.Jcrop.js"></script>
<style>
.cropper-bg {
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAAA3NCSVQICAjb4U/gAAAABlBMVEXMzMz////TjRV2AAAACXBIWXMAAArrAAAK6wGCiw1aAAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M26LyyjAAAABFJREFUCJlj+M/AgBVhF/0PAH6/D/HkDxOGAAAAAElFTkSuQmCC");
}

.jcrop-holder{
    width: 300px;
    height: 300px;
}
</style>
<script type="text/javascript">
$(function(){
	
	//修改提交
	$("#subBtn").on('click', function(){
		$.ajax({
			url: basepath+"/user/update",
	        type: 'POST',
	        data: $('#subForm').serialize(),
	        success: function(data){
		        if (data.code == 1000) {
		        	layer.msg(i18n_global_modify_success,{icon: 1,time:2000});
		        	setTimeout(function(){
		        		window.location.reload();
		        	}, 2000);
		        }
	        }
		 });
	});
	//修改密码
	$("#subBtnPass").on('click', function(){
		var oldpass = $("#oldpass").val();
    	var newpass = $("#newpass").val();
    	var confirmpass = $("#confirmpass").val();
    	if(oldpass==null||oldpass==""){
    		layer.msg(i18n_fill_before_pwd,{icon: 5,time:2000});
    		return;
    	}
    	if(newpass==null||newpass==""){
    		layer.msg(i18n_fill_new_pwd,{icon: 5,time:2000});
    		return;
    	}
    	if(confirmpass==null||confirmpass==""){
    		layer.msg(i18n_fill_new_pwd_again,{icon: 5,time:2000});
    		return;
    	}
    	if(newpass!=confirmpass){
    		layer.msg(i18n_new_pwd_not_consistent,{icon: 5,time:2000});
    		return;
    	}
    	if(confirmpass.length < 6){
       		layer.msg(i18n_new_pwd_short,{icon: 5,time:2000});
       		return;
    	}
    	var data = {
    			"password" : oldpass,
    			"newPassword" : newpass,
    			"confirmPassword" : confirmpass,
    	}
    	//检验旧密码是否一致
    	$.ajax({
			url: basepath+"/user/check",
			dataType : "JSON",
			data : data,
			type: 'POST',
			async : false,
			cache:false,
	        success: function(data){
		        if (data.code == 1000) {
		        	layer.msg(i18n_jump_login,{icon: 1,time:2000});
		        	setTimeout(function(){
		        		window.location.href = basepath + "/home/index";
		        	}, 2000);
		        }else if (data.code == 2009){
		        	layer.msg(i18n_before_pwd_error,{icon: 5,time:2000});
		        }else if (data.code == 2010){
		        	layer.msg(i18n_before_pwd_empty,{icon: 5,time:2000});
		        }else if (data.code == 2011){
		        	layer.msg(i18n_new_pwd_empty,{icon: 5,time:2000});
		        }else if (data.code == 2012){
		        	layer.msg(i18n_new_pwd_again_empty,{icon: 5,time:2000});
		        }else if (data.code == 2013){
		        	layer.msg(i18n_new_pwd_not_consistent,{icon: 5,time:2000});
		        }else if (data.code == 2006){//临时提示，用于试用帐号不允许修改密码
		        	layer.msg("试用帐号无法修改密码，请联系平台开通正式账号!",{icon: 5,time:2000});
		        }
	        }
		 });
	});
	
	//修改空间分配提交
	$("#subBtnSpace").on('click', function(){
		$.ajax({
			url: basepath+"/user/updateSpace",
	        type: 'POST',
	        data: $('#subFormSpace').serialize(),
	        success: function(data){
		        if (data.code == 1000) {
		        	layer.msg(i18n_global_modify_success,{icon: 1,time:2000});
		        	setTimeout(function(){
		        		window.location.reload();
		        	}, 2000);
		        }
	        }
		 });
	});
	
	//单选    
    $(".chose_control").each(function(){
        $(this).find("i").click(function(){
        	//赋值sex隐藏域
        	var r = $(this).find("input")[0];
        	var sex = $(r).val();
        	$("#sex").val(sex);
            $(this).addClass("chosed");
            $(this).parent(".chose_control").siblings().find("i").removeClass("chosed");
        });
    });
	
  //iuput边框变色
    $(".infos li input,textarea").focus(function() {
       $(this).addClass("settings-input-on");
       $(this).parent().find(".settings-input-prompt").show();
     }).blur(function() {
    	 $(this).removeClass("settings-input-on");
    	 $(this).parent().find(".settings-input-prompt").hide();
     })
     
     $("#headerBtn").click(function(){
    	 var fileData = $("#file").val();
    	 if(fileData.length == 0){
    		 layer.msg(i18n_select_picture, {
    				icon : 5,
    				time : 1500
    			});
    		 return ;
    	 }
    	 $("#crop_form").submit();
     });
  
});
</script>

<script type="text/javascript">
	  jQuery(function($){
		    var jcrop_api,
		        boundx,
		        boundy,

		        $preview = $('#preview-pane'),
		        $pcnt = $('#preview-pane .preview-container'),
		        $pimg = $('#preview-pane .preview-container img'),
		        xsize = $pcnt.width(),
		        ysize = $pcnt.height();
		    
		       function aaa(){
		    	   alert("aa")
		       }
		    
			    function initJcrop(){
		    	   $('#target').Jcrop({
				      onChange: updatePreview,
				      onSelect: updatePreview,
				      allowSelect:true,
				      allowSelect:true,
				      boxWidth:300,
			          boxHeight:300,
				      minSize:[200,200],
				      maxSize:[xsize,ysize],
				      aspectRatio: 1
				    }, function(){  
				      // Use the API to get the real image size
				       var bounds = this.getBounds();
					   boundx = bounds[0];
					   boundy = bounds[1];
				       // Store the API in the jcrop_api variable
				       jcrop_api = this;
				       checkJcrop();
				       // Move the preview into the jcrop container for css positioning
			       });
			    }
			    
			    function checkJcrop(){
			   	 var fileData = $("#file").val();
			    	 if(fileData.length == 0){
			    		 jcrop_api.disable();
			    	 }
			    }
		    
		        $("#file").change(function () {
			        var $file = $(this);
			        var fileObj = $file[0];
			        var windowURL = window.URL || window.webkitURL;
			        var dataURL;
			        var $img = $("#target");
			
			        if (fileObj && fileObj.files && fileObj.files[0]) {
			            dataURL = windowURL.createObjectURL(fileObj.files[0]);
			            initJcrop();
			            $img.attr('src', dataURL);
			            $pimg.attr('src',$img.attr('src'));
				        jcrop_api.setImage($img.attr('src'));
				        jcrop_api.animateTo([100,100,400,300]);
				        jcrop_api.enable();
			        } else {
			            dataURL = $file.val();
			            var imgObj = document.getElementById("target");
			            var imgFile = document.getElementById("file");
			            imgFile.select();
			            var imgSrc = dataURL;
			            var localImagId = document.getElementById("localImag");
			            $img.attr('src','');
			            //必须设置初始大小
			            localImagId.style.width = "300px";
			            localImagId.style.height = "300px";
			            //图片异常的捕捉，防止用户修改后缀来伪造图片
			            layer.msg('i18n_select_browser', {
		    				icon : 4,
		    				time : 2500
		    			});
			            try{
				            localImagId.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				            localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
			            } catch(e) {
			               layer.msg(i18n_picture_correct, {
			    				icon : 5,
			    				time : 1500
			    			});
			               return false;
			            }
			        }
			        return false;
		    });

		    function updatePreview(c){
		      if (parseInt(c.w) > 0){
		        var rx = xsize / c.w;
		        var ry = ysize / c.h;
		    	$("#x").val(parseInt(c.x));
				$("#y").val(parseInt(c.y));
				$("#w").val(parseInt(c.w));
				$("#h").val(parseInt(c.h));
		        $pimg.css({
		          width: Math.round(rx * boundx) + 'px',
		          height: Math.round(ry * boundy) + 'px',
		          marginLeft: '-' + Math.round(rx * c.x) + 'px',
		          marginTop: '-' + Math.round(ry * c.y) + 'px'
		        });
		      }
		    };
		  });
</script>
<body class="min_body_width">
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<div class="box_main">
    <%@ include file="/WEB-INF/view/include/left.jsp"%>
    <div class="right_part">
        <div class="main_con main_con_an"> 
            <div class="r_topcon r_topcon_an"> 
               <div class="crumbs"><ul><li><i class="tip_o"></i><a href=""><fmt:message key="u_userinfo" bundle="${i18n}"/></a></li></ul></div>     
            </div>
            <div class="content content_an" style="top:90px;">
               <ul class="info_tab">
                   <li class="on"><span><fmt:message key="u_baseinfo" bundle="${i18n}"/></span></li>
                   <li><span><fmt:message key="u_otherinfo" bundle="${i18n}"/></span></li>
                   <li><span><fmt:message key="u_modify_pwd" bundle="${i18n}"/></span></li>
                   <li><span><fmt:message key="u_avatar_settings" bundle="${i18n}"/></span></li>
                   <li><span><fmt:message key="u_space_settings" bundle="${i18n}"/></span></li>
               </ul>
               <div class="info_tab_con" style="display:block">
                   <ul class="infos">
                       <li><label for=""><fmt:message key="u_username" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><span><input type="text"  class="settings-input state-disabled" value="${employee.employeeName }" disabled="disabled"></span></li>
                       <li><label for=""><fmt:message key="u_number" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><span><input type="text"  class="settings-input state-disabled" value="${employee.employeeCode }" disabled="disabled"></span></li>
                       <li><label for=""><fmt:message key="u_phone" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><span><input type="text"  class="settings-input state-disabled" value="${employee.employeeMobile }" disabled="disabled"></span></li>
                       <li><label for=""><fmt:message key="u_sex" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><span><input type="text"  class="settings-input state-disabled" value="<c:if test="${employee.employeeSex==0 }">男</c:if><c:if test="${employee.employeeSex==1 }">女</c:if>" disabled="disabled"></span></li>
                       <li><label for=""><fmt:message key="u_dept" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><span><input type="text"  class="settings-input state-disabled" value="${employee.deptName }" disabled="disabled"></span></li>
                   </ul>
                   
               </div>
               <div class="info_tab_con">
               	   <form id="subForm">
               	   	   <input type="hidden" name="id" value="${employee.id }">
	                   <ul class="infos">
	                   		<li>
	                   			<label for=""><fmt:message key="u_wechat" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	                   			<input type="text"  class="settings-input" maxlength="50" value="${employee.employeeWechat }" name="employeeWechat">
	                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_wechar_fill" bundle="${i18n}"/></span>
	                   		</li>
	                   		<li>
	                   			<label for=""><fmt:message key="u_qq" bundle="${i18n}"/>　&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	                   			<input type="text"  class="settings-input" maxlength="50" value="${employee.employeeQq }" name="employeeQq">
	                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_qq_fill" bundle="${i18n}"/></span>
	                   		</li>
	                   		<li>
	                   			<label for=""><fmt:message key="u_email" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	                   			<input type="text"  class="settings-input" maxlength="50" value="${employee.employeeEmail }" name="employeeEmail">
	                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_email_fill" bundle="${i18n}"/></span>
	                   		</li>
	                   		<li>
	                   			<label for=""><fmt:message key="u_profile" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	                   			<textarea   class="settings-textarea" name="introduction">${employee.introduction }</textarea>
	                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_profile_fill" bundle="${i18n}"/></span>
	                   		</li>
	                      <%--  <li><label for="">微信</label><input name="employeeWechat" type="text" value="${employee.employeeWechat }" maxlength="20"></li>
	                       <li><label for="">QQ</label><input name="employeeQq" type="text" value="${employee.employeeQq }" maxlength="20"></li>
	                       <li><label for="">邮箱</label><input name="employeeEmail" type="text" value="${employee.employeeEmail }" maxlength="30"></li>
	                       <li><label for="">介绍</label><textarea name="introduction" id="">${employee.introduction }</textarea></li> --%>
	                   </ul>
	                   <a id="subBtn" href="javascript:void(0);" class="infobtn"><fmt:message key="u_save" bundle="${i18n}"/></a>
                   </form>
               </div>
               <div class="info_tab_con">
                   <ul class="infos">
                   		<li>
                   			<label for=""><fmt:message key="u_current_pwd" bundle="${i18n}"/></label>
                   			<input id="oldpass" type="password" maxlength="20" class="settings-input">
                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_current_pwd_fill" bundle="${i18n}"/></span>
	                   	</li>
	                   	<li>
                   			<label for=""><fmt:message key="u_new_pwd" bundle="${i18n}"/>&nbsp;&nbsp;&nbsp;</label>
                   			<input id="newpass" type="password" maxlength="20" class="settings-input">
                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_new_pwd_fill" bundle="${i18n}"/></span>
	                   	</li>
	                   	<li>
                   			<label for=""><fmt:message key="u_new_pwd_confirm" bundle="${i18n}"/></label>
                   			<input id="confirmpass" type="password" maxlength="20" class="settings-input">
                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="u_new_pwd_fill_again" bundle="${i18n}"/></span>
	                   	</li>
                       <!-- <li><label for="">原密码</label><input id="oldpass" type="password" maxlength="20" placeholder="请输入原始密码"></li>
                       <li><label for="">新密码</label><input id="newpass" type="password" maxlength="20" placeholder="请输入新密码"></li>
                       <li><label for="">确认新密码</label><input id="confirmpass" type="password" maxlength="20" placeholder="请再次输入新密码"></li> -->
                   </ul>
                   <a id="subBtnPass" href="javascript:void(0);" class="infobtn"><fmt:message key="u_save" bundle="${i18n}"/></a>
               </div>
               <div class="info_tab_con">
                    <form action="${ctx }/user/upload" method="post" id="crop_form" enctype ="multipart/form-data">
	                   <div class="set_photo_l">
	                       <label class="infofile"><input type="file" id="file" name="file" accept="image/png,image/jpeg"  style="filter:alpha(opacity=0);-moz-opacity:0; opacity:0;"><fmt:message key="i18n_user_select_picture" bundle="${i18n}"/></label>
	                       <p class="color_7d"><fmt:message key="u_avatar_support" bundle="${i18n}"/></p>
	                       <a href="javascript:void(0)" id="headerBtn" class="infobtn"><fmt:message key="u_avatar_save" bundle="${i18n}"/></a>
	                   </div>
	                   <div class="set_photo_r">
	                       <p class="color_7d"><fmt:message key="u_avatar_preview" bundle="${i18n}"/></p>
	                       <ul class="pic_box">
	                           <li>
	                           <div class = "cropper-bg" id="localImag" style="width: 300px;height: 300px;">
							       	<img src="${plugins}/Jcrop/css/back.png" id="target" width="300px" height="300px"/>
							   </div>
		                     <input type="hidden" id="x" name="x" value="0"/>
		                     <input type="hidden" id="y" name="y" value="0"/>
		                     <input type="hidden" id="w" name="w" value="0"/>
		                     <input type="hidden" id="h" name="h" value="0"/>
				          
				         </div>
		              </form>
               </div>
               <div class="info_tab_con">
               	<form id="subFormSpace">
            	   <input type="hidden" name="id" value="${employee.id }">
                   <ul class="infos">
                   		<li>
                   			<label for=""><fmt:message key="personal_space_b" bundle="${i18n}"/>（MB）</label>
                   			<input id="oldpass" maxlength="20" type="text" class="settings-input" disabled="disabled" value="${personalSize }">
                   			<span class="settings-input-prompt" style="display: none;" ></span>
	                   	</li>
	                   	<li>
	                   			<label for=""></label>
								<div class="remaining-quota">

									<div class="remaining-progress">
										<span id="p3" class="remaining-progress-span"
											style="width: ${personalUseRatio}%"></span>
									</div>
									<p class="remaining-space">
										<span id="p2">${personalUseSizeNum }</span>/<span id="p1">${personalSizeNum }</span>
									</p>
								</div>
							</li>
	                   	<li>
                   			<label for=""><fmt:message key="work_space" bundle="${i18n}"/></label>
                   			<input id="confirmpass" name="teamworkSpaceSize" type="number"  onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"  onkeyup="if(this.value > ${teamworkMaxSize}){this.value=${teamworkMaxSize }} if(this.value < ${teamworkMinSize}){this.value=${teamworkMinSize }}" min="${teamworkMinSize}" max="${teamworkMaxSize }" value="${teamworkSize }" maxlength="20" class="settings-input">
                   			<span class="settings-input-prompt" style="display: none;"><fmt:message key="tw_remark_prompt_b" bundle="${i18n}"/></span>
	                   	</li>
	                   	<li>
	                   			<label for=""></label>
								<div class="remaining-quota">

									<div class="remaining-progress">
										<span id="p3" class="remaining-progress-span"
											style="width: ${teamworkUseRatio}%"></span>
									</div>
									<p class="remaining-space">
										<span id="p2">${teamworkUseSizeNum }</span>/<span id="p1">${teamworkSizeNum }</span>
									</p>
								</div>
							</li>
                   </ul>
                   <a id="subBtnSpace" href="javascript:void(0);" class="infobtn"><fmt:message key="u_save" bundle="${i18n}"/></a>
					<div style="height: 22px; text-align: left; padding: 20px 0 0 0;">
							<font color="#7d7d7d"><fmt:message key="tw_remark_prompt_c" bundle="${i18n}"/></font>
						</div>
				</form>
			</div>
           </div>
            
        </div>
    </div>
</div>
</body>
</html>