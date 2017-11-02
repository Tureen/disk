<%@ page language="java" import="com.yunip.utils.page.PageQuery" pageEncoding="UTF-8"%>
<div class="pages">
<%
	PageQuery query=(PageQuery)request.getAttribute("query"); 
%>
<input type="hidden" id="pageIndex" name="pageIndex" value="1" />
<input type="hidden" id="currentPage" name="currentPage" value="<c:out value="${query.pageIndex}" />" />
<div class="dataTables_info">
		<span class="text">共<%=query.getPageCount() %>页</span>
		<span class="text">共<%=query.getRecordCount() %>条记录</span>
	</div>
<div class="dataTables_paginate paging_simple_numbers">
	<a class="plink paginate_button previous disabled" href="#" onclick="lastPage()"><上一页</a> 
<% if(query.getPageCount()<8){ 
	for(int i=1;i<=query.getPageCount();i++){    
		if(query.getPageIndex()==i){
%>
	<a href="#" class="current" onclick="selected(<%=i %>)"><%=i %></a> 
		<%}else{%>
	<a href="#" class="plink" onclick="selected(<%=i %>)"><%=i %></a>   
<%
		}
	}   
}else{  
	if(query.getPageIndex()<5){
		for(int i=1;i<=8;i++){    	  
			if(query.getPageIndex()==i){
%>
	<a href="#" class="current" onclick="selected(<%=i %>)"><%=i %></a> 
			<%}else{%>
	<a href="#" class="plink" onclick="selected(<%=i %>)"><%=i %></a>
<%
			}
		}
	}else{
	    if(query.getPageCount()-query.getPageIndex()<8){
			for(int i=query.getPageCount()-7;i<=query.getPageCount();i++){
				if(query.getPageIndex()==i){
%>
	<a href="#" class="current" onclick="selected(<%=i %>)"><%=i %></a> 
				<%}else{%>
	<a href="#" class="plink" onclick="selected(<%=i %>)"><%=i %></a>
<%  
				}
			}
		}else{    		  
      		for(int i=query.getPageIndex()-3;i<=query.getPageIndex()+4;i++){      		
      			if(query.getPageIndex()==i){
    		  	 %>
    		   		<a href="#" class="current" onclick="selected(<%=i %>)"><%=i %></a> 
    			<%}else{ %>
      				<a href="#" class="plink" onclick="selected(<%=i %>)"><%=i %></a>
      			<%
				}
			}
		}
	}
}
%>
		<a class="plink paginate_button previous disabled" id="nextPage" href="#" onclick="nextPages()">下一页></a>
		<span class="text" style="font-size:14px; color:#666">
			转到第 <input type="text" class="pinput" id="pagNo" style="width: 30px"> 页
		</span>
		<input type="button" class="ptn paginate_button" onclick="tiaozhuan()" value="确定">
	</div>
	
</div>
            
 <script language="javascript">
 	var pag=<c:out value="${query.pageCount}"/>;
 	var index=<c:out value="${query.pageIndex}"/>; 	
 	/**
 	* 页面输入跳转页面 
 	*/
	function tiaozhuan(){	 		
		var indexpage=$("#pagNo").val();		
	if(indexpage>pag){
		$("#pagNo").focus();
		alert("输入页数大于总页数");
		$("#pagNo").val('');
	}else{		
		 if(indexpage.search("^[0-9]*[1-9][0-9]*$")!=0){
			 	$("#pagNo").focus();
			 	$("#pagNo").val('');
		        alert("请输入数字");       
		 }else{
			$("#pageIndex").val(indexpage);		
			$("#beanForm").submit();
		 }
	}
	}
	/**
 	* 下一页
 	*/
	function nextPages(){		
		if(pag==0 || pag==index){
			alert("已经是最后一页");
		}else{
			$("#pageIndex").val(index+1);
			$("#beanForm").submit();
		}
	}
	/**
 	* 上一页
 	*/
	function lastPage(){		
		if(index==1){
			alert("已经是第一页");
		}else{
			$("#pageIndex").val(index-1);
			$("#beanForm").submit();
		}
	}
	/**
 	* 选择页面
 	*/
	function selected(page){
		$("#pageIndex").val(page);
		$("#beanForm").submit();
	}
</script>