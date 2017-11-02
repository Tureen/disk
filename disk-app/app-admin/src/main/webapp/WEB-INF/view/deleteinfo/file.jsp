<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${plugins}/lib/My97DatePicker/WdatePicker.js" />
<script type="text/javascript" src="${plugins}/lib/My97DatePicker/WdatePicker.js"></script> 
<link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 
<%-- <script type="text/javascript" src="${staticpath }/js/file/look.js"></script> --%>
<style type="text/css">
.menuContent{
    height:100px;
    overflow-x:hidden;
    overflow-y:scroll;
    background-color:#F0F0F0;
    border:1px solid #f0f0f0;
    z-index: 999;
}
</style>
<script type="text/javascript">
	$(function(){
		$("#checkall").click(function(){
			//判断是全选or取消
			var index = 0;
			$("[name='checkbox']").each(function(){
				if(!$(this).prop('checked') && !$(this).prop('disabled')){
					index = 1;
					return false;
				} 
			});
			//全选
			if(index == 1){
				$("[name='checkbox']").each(function(){
					if(!$(this).prop('disabled')){
						$(this).prop("checked",'true');
					}
				});
			}else{
				$("[name='checkbox']").removeAttr("checked");//取消全选
			}
		});
		
		$(".checkbox").click(function(){
			var index = 0;
			$("[name='checkbox']").each(function(){
				if(!$(this).prop('checked') && !$(this).prop('disabled')){
					index = 1;
					return false;
				} 
			});
			if(index == 1){
				$("#checkall").removeAttr("checked");
			}else{
				$("#checkall").prop("checked",'true');
			}
		});
		
		//删除物理地址
		$("#delete").click(function(){
			var idStr = getSelect();
			if(idStr.length==0){
				layer.msg('请勾选要删除的文件!',{icon: 5,time:1500});
				return;
			}
			if(window.confirm('确定要删除文件物理地址吗？（文件将无法找回）')){
				$.ajax({
					url : basepath + "/deleteinfo/deletefile?idStr="+idStr,
					dataType : "JSON",
					type: 'GET',
					async : false,
					cache:false,
					success : function (data){
						if(checkErrorCode(data.code)){
							if(data.code=="1000"){
								layer.msg('操作成功!',{icon: 1,time:1500});
								setTimeout(function(){
									window.location.href=basepath+"/deleteinfo/file?pageIndex=${query.pageIndex}";
				            	}, 1500);
							} else {
								layer.msg(data.codeInfo,{icon: 2,time:1500});
							}
					    }
					}
				});
			}
		});
		
		//还原到原目录
		$("#restore").click(function(){
			var idStr = getSelect();
			if(idStr.length==0){
				layer.msg('请勾选要还原的文件!',{icon: 5,time:1500});
				return;
			}
			if(window.confirm('确定要还原文件到原有目录吗？')){
				$.ajax({
					url : basepath + "/deleteinfo/restorefile?idStr="+idStr,
					dataType : "JSON",
					type: 'GET',
					async : false,
					cache:false,
					success : function (data){
						if(checkErrorCode(data.code)){
							if(data.code=="1000"){
								layer.msg('操作成功!',{icon: 1,time:1500});
								setTimeout(function(){
									window.location.href=basepath+"/deleteinfo/file?pageIndex=${query.pageIndex}";
				            	}, 1500);
							} else {
								layer.msg(data.codeInfo,{icon: 2,time:1500});
							}
					    }
					}
				});
			}
		});
		
		/**
	     * 下载
	     */
	     $(".download").click(function(){
	    	 var folderArray = new Array();
   	   		 var fileArray = new Array();
   	   		 var id = $(this).next().val();
   	   		 var fileJson = {
   			 	"id" : id
	   		 }
	   		fileArray.push(fileJson);
	    	 var data = {
   				"folders" : folderArray,
   				"files" : fileArray,
   			}
	    	 $.ajax({
  	   			url: basepath+"/deleteinfo/download",
  	   	        dataType:"json",
  	   	        type: 'POST',
  	   	     	data: JSON.stringify(data),
  	   	     	contentType: "application/json",
  	   	        async: false, 
  	   	        success: function(result){
  	   		        if (result.code == 1000) {
  	   		        	window.location.href = result.result;
  	   		        }else{
  	   		        	layer.msg(result.codeInfo,{icon: 5,time:1500});
  	   		        }
  	   	        }
  	   		 });
	     });
	});
	
	function getSelect(){
		var idStr = "";
		//获取对象
		$("[name='checkbox']:checkbox:checked").each(function(){   
			idStr += $(this).next().val() + ",";
	    }); 
		if(idStr.length > 0){
			idStr = idStr.substring(0,idStr.length-1);
		}
		return idStr;
	}
	
	function jumpFile(){
		window.location.href = basepath + "/deleteinfo/file";
	}
	function jumpFolder(){
		window.location.href = basepath + "/deleteinfo/folder";
	}
</script>
<div class="page-container dataTables_wrapper">
    <form action="${ctx }/deleteinfo/file" method="post" id="beanForm">
		<div >
			<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" placeholder="起始日期 "  value="${query.startDate }" name = "startDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			-
			<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" placeholder="终止日期" value="${query.endDate }" name = "endDate" class="input-text Wdate" style="width:120px;" readonly="readonly">
			<input type="text" class="input-text" style="width:150px" placeholder="文件名" id="" name="fileName" value="${query.fileName }">
			<input type="text" class="input-text" style="width:150px" placeholder="员工姓名/域用户名" id="" name="employeeName" value="${query.employeeName }">
			<input type="text" class="input-text" style="width:150px" placeholder="员工IP/域用户IP" id="" name="actionEmployeeIp" value="${query.actionEmployeeIp }">
			<span class="select-box" style="width:120px">
			<select class="select"  name="status">
				<option value="" selected disabled="disabled">文件状态</option>
				<option  value="">全部</option>
				<option <c:if test="${query.status==blackType.status }">selected="selected"</c:if> value="0">回收站</option>
				<option <c:if test="${query.status==grayType.status }">selected="selected"</c:if> value="1">已删除</option>
				<option <c:if test="${query.status==greenType.status }">selected="selected"</c:if> value="2">已还原</option>
			</select>
			</span>
			<button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
		</div>
	<div>
	<div class="btn-group" style="text-align:right;">
		<span class="btn btn-primary radius" >文&nbsp;&nbsp;件</span>
		<span class="btn btn-default radius"  onclick="jumpFolder()">文件夹</span>
	</div>
	<div class="cl pd-5 bg-1 bk-gray"> 
	<span class="l"> 
	<a class="btn btn-primary radius" id="delete" href="javascript:;"><i class="Hui-iconfont">&#xe609;</i> 删除文件</a> 
	<a class="btn btn-primary radius" id="restore" href="javascript:;"><i class="Hui-iconfont">&#xe66b;</i> 还原文件</a> 
	</span></div>
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<td width="5%"><input id="checkall" type="checkbox" name="checkall" style="width: 16px;height: 16px;cursor: pointer;"><!-- <input id="checkall" type="button" class="tab_627_btn" value="全选" style="width: 60%;background-color: #2cc9d0;cursor: pointer;"/> --></td>
				<th width="15%">文件名</th>
				<th width="20%">路径</th>
				<th width="5%">文件类型</th>
				<th width="5%">大小</th>
				<th width="5%">所属员工</th>
				<th width="5%">删除人</th>
				<th width="10%">删除人IP</th>
				<th width="10%">删除时间</th>
				<th width="5%">操作管理员</th>
				<th width="5%">文件状态</th>
				<th width="5%">操作</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach items="${query.list }" var="file">
		   <tr class="text-c">
				<td><input type="checkbox" name="checkbox" class="checkbox" style="width: 16px;height: 16px;cursor: pointer;" <c:if test="${file.status!=blackType.status}">disabled="disabled"</c:if>><input type="hidden" value="${file.id }"></td>
				<td>${file.fileName}</td>
				<td><a href="javascript:;" title="/${file.absolutePath}">/${fn:length(file.absolutePath) > 30 ? fn:substring(file.absolutePath, 0, 30):(file.absolutePath)}${fn:length(file.absolutePath) > 30 ? "……" : ""}</a>
				</td>
				<td><c:if test="${file.employeeId==1}">公共文件</c:if><c:if test="${file.employeeId!=1}">个人文件</c:if></td>
				<td>${file.showFileSize}</td>
				<td>${file.employeeName}</td>
				<td>${file.updateAdmin}</td>
				<td>${file.actionEmployeeIp }</td>
				<td><fmt:formatDate value="${file.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				<td><c:if test="${empty file.adminName}">-</c:if><c:if test="${!empty file.adminName}">${file.adminName }</c:if></td>
				<td><c:if test="${file.status==blackType.status}">
				   		<span style="color: black">${blackType.desc }</span>
				   	</c:if>
				   	<c:if test="${file.status==grayType.status}">
				   		<span style="color: gray">${grayType.desc }</span>
				   	</c:if>
				   	<c:if test="${file.status==greenType.status}">
				   		<span style="color: green">${greenType.desc }</span>
				   	</c:if>
				</td>
				<td><c:if test="${file.status==blackType.status }"><a href="javascript:;" class="download" style="cursor: pointer;color: blue">下载</a><input type="hidden" value="${file.id }"></c:if>
				<c:if test="${file.status!=blackType.status }">-</c:if>
				</td>
			</tr>
		   </c:forEach>
		</tbody>
	</table>
	<%@ include file="/WEB-INF/view/include/paging.jsp"%>
	</div>
	</form>
</body>
</html>