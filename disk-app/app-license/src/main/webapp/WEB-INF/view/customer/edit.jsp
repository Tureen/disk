<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/customer/save.js?v=211"></script>
<script type="text/javascript">
$(function(){
	$("#licenseHourOpen").click(function(){
		if($("#licenseHourType").val() == 0){
			$("#licenseHourType").val(1);
			$("#licenseHour").prop("name","licenseHour");
			$("#licenseHour").removeAttr("readonly");
			$("#licenseHourOpen").find("i").html("&#xe605;");
			layer.msg("解锁成功，修改后会重新生成授权码",{icon: 7,time:1000});
		}else if($("#licenseHourType").val() == 1){
			$("#licenseHourType").val(0);
			$("#licenseHour").val($("#licenseHour").parent().find("input:eq(0)").val());
			$("#licenseHour").removeAttr("name");
			$("#licenseHour").prop("readonly",'readonly');
			$("#licenseHourOpen").find("i").html("&#xe60e;");
			layer.msg("加锁成功,数值还原",{icon: 4,time:1000});
		}
	});
	$("#registerNumOpen").click(function(){
		if($("#registerKeyType").val() == 0){
			$("#registerKeyType").val(1);
			$("#registerNum").prop("name","registerNum");
			$("#registerNum").removeAttr("readonly");
			$("#registerNumOpen").find("i").html("&#xe605;");
			layer.msg("解锁成功，修改后会重新生成授权码",{icon: 7,time:1000});
		}else if($("#registerKeyType").val() == 1){
			$("#registerKeyType").val(0);
			$("#registerNum").val($("#registerNum").parent().find("input:eq(0)").val());
			$("#registerNum").removeAttr("name");
			$("#registerNum").prop("readonly",'readonly');
			$("#registerNumOpen").find("i").html("&#xe60e;");
			layer.msg("加锁成功,数值还原",{icon: 4,time:1000});
		}
	});
});
</script>
<article class="page-container">

	<form action="${ctx}/customer/edit" method="post" class="form form-horizontal" id="queryForm">
		<input type="hidden" name="id" value="${customer.id }">
		<%-- <div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>授权码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.licenseCode }" placeholder="" readonly="readonly">
			</div>
		</div> --%>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户端生成编码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input name="clientCode" type="text" class="input-text" value="${customer.clientCode }" placeholder="" readonly="readonly">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>授权时间(天)：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="hidden" value="<fmt:parseNumber integerOnly="true" value="${customer.licenseHour/24}" />">
				<input id="licenseHour" type="text" class="input-text" value="<fmt:parseNumber integerOnly="true" value="${customer.licenseHour/24}" />" placeholder="" readonly="readonly" maxlength="5" onkeyup='this.value=this.value.replace(/\D/gi,"")'><span id="licenseHourOpen" style="margin-left: -20px;cursor: pointer;"><i class="Hui-iconfont">&#xe60e;</i></span>
				<input id="licenseHourType" type="hidden" name="licenseHourType" value="0">
				<span style="color:	#DAA520;font-size: 2px">(授权天数填入“99999”则为不限时间)</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>允许注册员工数：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="hidden" value="${customer.registerNum }">
				<input id="registerNum" type="text" class="input-text" value="${customer.registerNum }" placeholder="" readonly="readonly"  maxlength="4" onkeyup='this.value=this.value.replace(/\D/gi,"")'><span id="registerNumOpen" style="margin-left: -20px;cursor: pointer;"><i class="Hui-iconfont">&#xe60e;</i></span>
				<input id="registerKeyType" type="hidden" name="registerKeyType" value="0">
				<span style="color:	#DAA520;font-size: 2px">(注册员工数填入“9999”则为不限人数)</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户名称：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.customerName }" placeholder="" id="customerName" name="customerName" datatype="*4-16" nullmsg="客户名称不能为空">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>联系人：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.contacts }" placeholder="" id="contacts" name="contacts">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>联系人电话：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.contactsMobile }" placeholder="" id="contactsMobile" name="contactsMobile" onkeyup='this.value=this.value.replace(/\D/gi,"")'>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">联系人地址：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.customerAddress }" placeholder="" id="customerAddress" name="customerAddress">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">联系人邮箱：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="${customer.contactsEmail }" placeholder="" id="contactsEmail" name="contactsEmail">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="submit" class="btn btn-success radius" id="admin-admin-save" name="admin-admin-save"><i class="icon-ok"></i>修改</button>
			</div>
		</div>
	</form>
</article>
</body>
</html>
