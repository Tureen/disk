<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
    <TITLE> ZTREE DEMO - Simple Data</TITLE>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/demo.css?v=2016" type="text/css">
    <link rel="stylesheet" href="${plugins}/lib/zTree/v3/css/metroStyle/metroStyle.css?v=2015" type="text/css">
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="${plugins}/lib/zTree/v3/js/jquery.ztree.exedit-3.5.js"></script>
    <SCRIPT type="text/javascript">
    
    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    	demoIframe = $("#testIframe");
        
        $("#addDept").click(function(){
        	// 添加弹出层代码
     		la = layer.open({
     			type:2,
     			title:"添加部门",
     			area: ['500px', '250px'],
     			content:basepath + "/dept/addoredit",
     			end:function(index, layero){
     			}
     		});
        });
    });
    
      <!--
      var setting = {
          view: {
              addHoverDom: addHoverDom,
              removeHoverDom: removeHoverDom,
              selectedMulti: false,
              showIcon: false,
          	  showLine: false
          },
          data: {
              simpleData: {
                  enable: true,
              	idKey: "id",
  				pIdKey: "pId",
  				rootPId: ""
              }
          },
          edit: {
              enable: false,
          },
          callback: {
  			beforeClick: beforeClick
  		}
      };

      var zNodes = [];
      <c:forEach items="${depts}" var="dept">
      var data = 
        { id: "<c:out value="${dept.id}"/>", pId: "<c:out value="${dept.parentId}"/>", name: "<c:out value="${dept.deptName}"/>", open: true};
      zNodes.push(data);
      </c:forEach>

      function showIconForTree(treeId, treeNode) {
      	return treeNode.level != 2;
      };
      
      var newCount = 1;
      function addHoverDom(treeId, treeNode) {
          var sObj = $("#" + treeNode.tId + "_span");
          if(treeNode.id == '00'){
        	  if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
              var addStr = "<span class='button add' id='addBtn_" + treeNode.tId+ "' title='添加部门' onfocus='this.blur();'></span>"
                  + "<span title='编辑部门' id='editBtn_" + treeNode.tId+ "' class='button edit' onfocus='this.blur();'></span>";
              sObj.after(addStr); 
          } else {
        	  if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
              var addStr = "<span class='button add' id='addBtn_" + treeNode.tId+ "' title='添加部门' onfocus='this.blur();'></span>"
                  + "<span title='编辑部门' id='editBtn_" + treeNode.tId+ "' class='button edit' onfocus='this.blur();'></span>"
                  + "<span title='移除部门' id='removeBtn_" + treeNode.tId+ "' class='button remove' onfocus='this.blur();'></span>";
              sObj.after(addStr); 
          }
          //添加
          var btn = $("#addBtn_"+treeNode.tId);
          if (btn) btn.bind("click", function(){
         		// 添加弹出层代码
         		la = layer.open({
         			type:2,
         			title:"添加部门",
         			area: ['500px', '250px'],
         			content:basepath + "/dept/addoredit?parentId="+treeNode.id,
         			end:function(index, layero){
         			}
         		
         		
         		});
              return false;
          });
          
          //修改
          var btn = $("#editBtn_"+treeNode.tId);
          if (btn) btn.bind("click", function(){
              var zTree = $.fn.zTree.getZTreeObj("treeDemo");
              var data = ['500px', '250px'];
              if(treeNode.id == '00'){
            	  data = ['500px', '200px'];
              }
	           // 添加弹出层代码
	       	   layer.open({
	       			type:2,
	       			fix: false, //不固定
	       			shade:0.4,
	       			title:"编辑部门",
	       			area: data,
	       			content:basepath + "/dept/addoredit?parentId="+treeNode.pId + "&id="+treeNode.id,
	       			end:function(index, layero){
	       			}
	       	  });
              return false;
          });
          
          //删除部门
          var btn = $("#removeBtn_"+treeNode.tId);
          if (btn) btn.bind("click", function(){
              var zTree = $.fn.zTree.getZTreeObj("treeDemo");
              layer.confirm("确认移除部门 '" + treeNode.name + "'?", {
            	  btn: ['确定','取消'] 
            	}, function(){
            		$.ajax({
          				type:"POST",
          				url:basepath + "/dept/delDept/"+treeNode.id,
          				dataType:"json", 
          			    data: data,
          			    async: false,
          				success:function(data){
          					if(data.code=="1000"){
          						layer.msg("已移除该部门信息",{icon: 1,time:1000});
          						zTree.removeNode(treeNode);
          					} else {
          						layer.msg(data.codeInfo,{icon: 2,time:1000});
          					}
          				}
          		     }); 
            	}, function(){
            	});
              return false;
          });
      };
      
      function removeHoverDom(treeId, treeNode) {
          $("#addBtn_"+treeNode.tId).unbind().remove();
          $("#editBtn_"+treeNode.tId).unbind().remove();
          $("#removeBtn_"+treeNode.tId).unbind().remove();
      };
      
      function beforeClick(treeId, treeNode) {
    		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    		zTree.expandNode(treeNode,true);
    		var zTree = $.fn.zTree.getZTreeObj("tree");
    		demoIframe.attr("src", basepath + "/dept/treeright/" + treeNode.id);
    		return true;
      }	
        //-->
    </SCRIPT>
</HEAD>
<BODY>
<table class="table">
	<tr>
		<td width="251px" class="va-t" style="padding:0;">
			<div class="content_wrap">
			   <div class="zTreeDemoBackground left">
			       <div class="zzjg_top">组织架构<span id="addDept"><i></i>添加部门</span></div>
			       <ul id="treeDemo" class="ztree"></ul>
			   </div>
			</div>
		</td>
		<td class="va-t" style="height: 500px"><IFRAME ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO width=100%  height=800px SRC="${ctx}/dept/treeright/00"></IFRAME></td>
	</tr>
</table>
</BODY>
</HTML>