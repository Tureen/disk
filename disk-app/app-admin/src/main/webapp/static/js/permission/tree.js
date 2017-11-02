function beforeClick(treeId, treeNode){
	if(treeNode.type==3){
		return false;
	}
	var zTree = $.fn.zTree.getZTreeObj("tree");
	demoIframe.attr("src", basepath + "/permission/toinsertpermission/" + treeNode.id);
	return true;
}

$(document).ready(function(){
	var t = $("#treeDemo");
	t = $.fn.zTree.init(t, setting, zNodes);
	demoIframe = $("#testIframe");
	demoIframe.bind("load", loadReady);
	
	var parentName = $('#_selected', parent.document).parent().parent().parent().parent().find('span').html();
	var titleName = $('#_selected', parent.document).html();
	$("#parentName").html(parentName);
	$("#titleName").html(titleName);
})