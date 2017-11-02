<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/customer/save.js?v=2111"></script>
<article class="page-container">

	<form action="${ctx}/customer/save" method="post" class="form form-horizontal" id="queryForm">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户端生成编码：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="clientCode" name="clientCode">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>授权时间（天）：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="licenseHour" name="licenseHour" maxlength="5" onkeyup='this.value=this.value.replace(/\D/gi,"")'>
			<span style="color:	#DAA520;font-size: 2px">(授权天数填入“99999”则为不限时间)</span></div>
			<!-- <div class="formControls col-xs-8 col-sm-7">
				<select class="input-text" id="licenseHour" name="licenseHour">
					<option value="720" selected="selected">一个月</option>
					<option value="2160">三个月</option>
					<option value="4320">半年</option>
					<option value="8640">一年</option>
					<option value="17280">两年</option>
					<option value="864000">永久</option>
				</select>
			</div> -->
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>允许注册员工数：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="registerNum" name="registerNum" maxlength="4" onkeyup='this.value=this.value.replace(/\D/gi,"")'>
				<span style="color:	#DAA520;font-size: 2px">(注册员工数填入“9999”则为不限人数)</span></div>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户名称：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="customerName" name="customerName" datatype="*4-16" nullmsg="客户名称不能为空">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>联系人：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="contacts" name="contacts">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>联系人电话：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="contactsMobile" name="contactsMobile" onkeyup='this.value=this.value.replace(/\D/gi,"")'>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">联系人地址：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="customerAddress" name="customerAddress">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">联系人邮箱：</label>
			<div class="formControls col-xs-8 col-sm-7">
				<input type="text" class="input-text" value="" placeholder="" id="contactsEmail" name="contactsEmail">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-7 col-xs-offset-4 col-sm-offset-3">
				<button type="submit" class="btn btn-success radius" id="admin-admin-save" name="admin-admin-save"><i class="icon-ok"></i>确定</button>
			</div>
		</div>
	</form>
</article>
</body>
</html>
