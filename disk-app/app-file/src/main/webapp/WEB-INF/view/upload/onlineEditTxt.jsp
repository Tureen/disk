<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<meta charset="utf-8"/>
<link rel="stylesheet" href="${staticpath }/js/codemirror/lib/codemirror.css?v=1">
<link rel="stylesheet" href="${staticpath }/js/codemirror/addon/dialog/dialog.css">
<link rel="stylesheet" href="${staticpath }/js/codemirror/addon/search/matchesonscrollbar.css">
<script src="${staticpath }/js/codemirror/lib/codemirror.js"></script>
<script src="${staticpath }/js/codemirror/mode/xml/xml.js"></script>
<script src="${staticpath }/js/codemirror/addon/dialog/dialog.js"></script>
<script src="${staticpath }/js/codemirror/addon/search/searchcursor.js"></script>
<script src="${staticpath }/js/codemirror/addon/search/search.js"></script>
<script src="${staticpath }/js/codemirror/addon/scroll/annotatescrollbar.js"></script>
<script src="${staticpath }/js/codemirror/addon/search/matchesonscrollbar.js"></script>
<script src="${staticpath }/js/codemirror/addon/search/jump-to-line.js"></script>
<script src="${staticpath }/js/messageEvent.min.js"></script>
<style type="text/css">
 dt {font-family: monospace; color: #666;}
</style>
<%-- <form>
	
</form> --%>
<div id="con_upload" class="outlayer"  style="height: 100%;padding: 0">
    <ul class="tabs_upload" style="height: 100%">
        <textarea id="code" name="code" readonly="readonly">${content }</textarea>
    </ul>
</div>
<script>
var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
  mode: "text/html",
  lineNumbers: true,
  extraKeys: {"Alt-F": "findPersistent"}
  <c:if test="${read == 0}">,readOnly: true</c:if>
});

function getContent(){
	return editor.getValue();
}
</script>

<script type="text/javascript">
 messageEvent.add(function (event) {
	 var content = getContent();
	 $.ajax({
		url: "${url }",
        dataType:"json",
        type: "POST",
     	data: {content:content},
        async: false, 
        success: function(data){
        	if(data.code == 1000){
        		layer.msg(i18n_global_edit_success, {icon: 1,time:1500});
        		setTimeout(function(){
        			messageEvent.postMessage(window.parent, "close", '*');
            	}, 1500);
        	}else{
        		layer.msg(data.codeInfo,{icon: 5,time:1500});
        	}
        }
	 }); 
});
</script>
