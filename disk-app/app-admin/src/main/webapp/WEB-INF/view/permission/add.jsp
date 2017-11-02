<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
</head>
<body>
<script type="text/javascript" src="${staticpath }/js/permission/add.js"></script>
<div class="page-container">
  <form action="${ctx }/permission/save" method="post" class="form form-horizontal" id="permissionForm">
  	<input type="hidden" name="permissionFid" value="${permissionParent.id }">
  	<div class="row cl">
      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>权限类型：</label>
      <div class="formControls col-xs-6 col-sm-9">
        <input type="text" class="input-text"
        <c:forEach items="${enum}" var="permissionEnum">
	      	<c:if test="${(permissionParent.permissionType+1)==permissionEnum.type }">value="${permissionEnum.desc}"</c:if>
	    </c:forEach>
          placeholder="" id="permissionType" readonly="readonly">
         <input type="hidden" name="permissionType" value="${permissionParent.permissionType+1 }">
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>上级目录：</label>
      <div class="formControls col-xs-6 col-sm-9">
        <input type="text" class="input-text" value="${permissionParent.permissionName }" placeholder="" id="permissionFName" readonly="readonly" >
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>分类名称：</label>
      <div class="formControls col-xs-6 col-sm-9">
        <input type="text" class="input-text" value="" placeholder="" id="permissionName" name="permissionName">
      </div>
      <div class="col-5"> </div>
    </div>
    <c:if test="${permissionParent.permissionType>=1 }">
	    <div class="row cl">
	      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>权限编码：</label>
	      <div class="formControls col-xs-6 col-sm-9">
	        <input type="text" class="input-text" value="" placeholder="" id="permissionCode" name="permissionCode">
	      </div>
	      <div class="col-5"> </div>
	    </div>
	 </c:if>
	 <c:if test="${permissionParent.permissionType==1 }">
	    <div class="row cl">
	      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>导航URL：</label>
	      <div class="formControls col-xs-6 col-sm-9">
	        <input type="text" class="input-text" value="" placeholder="" id="permissionUrl" name="permissionUrl">
	      </div>
	      <div class="col-5"> </div>
	    </div>
    </c:if>
    <c:if test="${permissionParent.permissionType==0 }">
	    <div class="row cl">
	      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>权限图标：</label>
	      <div class="formControls col-xs-6 col-sm-9">
	        <input type="text" class="input-text" value="" placeholder="" id="permissionIcon" name="permissionIcon">
	      </div>
	      <div class="col-5"> </div>
	    </div>
    </c:if>
    <div class="row cl">
      <label class="form-label col-xs-3 col-sm-3"><span class="c-red">*</span>是否启用：</label>
      <div class="formControls col-xs-6 col-sm-9">
        <input type="radio" class="input-radio" value="1" placeholder="" name="validStatus" checked="checked">是
        <input type="radio" class="input-radio" value="0" placeholder="" name="validStatus">否
      </div>
      <div class="col-5"> </div>
    </div>
    <div class="row cl">
      <div class="col-xs-6 col-sm-9 col-xs-offset-3 col-sm-offset-3">
        <input class="btn btn-primary radius" id="btnAdd" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>
</body>
</html>