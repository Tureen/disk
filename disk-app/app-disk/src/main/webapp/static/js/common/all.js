$(function() {
	// 左侧导航折叠
	$("#nav .tips_drop").click(
			function() {
				$(this).toggleClass("current").parent().next("div.down_box")
						.slideToggle(300)
			});
	// 下拉
	$(".more_action").hover(function() {
		$(this).find(".down_list").stop().slideToggle(300);
	})
	$(".top_right li").on("hover", function() {
		$(this).toggleClass("hover");
		$(this).find(".drop_a").stop().slideToggle(300);
	})
	// tabs hover
	// $(".tabs").find("tr").each(function(){
	// if($(this).index() != 0){
	// $(this).live('hover',function(){
	// $(this).toggleClass("bgc_hover");
	// $(this).find(".edit").toggle();
	// })
	// }
	// });
	// 字母搜索
	$(".search_md a").on("click", function() {
		$(this).addClass("focus").siblings().removeClass("focus");
	});
	// 时间升序降序排练
	$(".date_tip_up").on("click", function() {
		if(!isOper){
		    return;
		}
		$(this).toggleClass("date_tip_down");
	});

	//tab选项卡-个人设置
    $(".info_tab li").live("click",function(){
        var infoli=$(this).index();
    	$(this).addClass("on").siblings().removeClass("on");
        $(".info_tab_con").eq(infoli).show().siblings(".info_tab_con").hide();
        
    });
    //tab选项卡-input栏-个人设置
    $(".info_tab_con li").live("click",function(){
    	$(this).addClass("on").siblings().removeClass("on");
    });
    
	// 展开收缩
	var labels = $(".labelku span:gt(10)");
	//labels.hide();
	$(".control_open").click(function() {
		labels.slideToggle();
		$(this).toggleClass("control_up")
	});
	// 上传弹出层
	/*$('#upload').on('click', function() {
		layer.open({
			type : 1,
			title : '上传文件',
			area : [ '800px', '460px' ],
			shadeClose : true, // 点击遮罩关闭
			content : $('#con_upload'),
			btn : [ '上传', '取消' ]
		});
	});*/
	//标签管理-标签下拉
    $(".labels_list li").live("click",function(){
    	$(this).children(".alltext").hide();
        $(this).children(".drop_label").fadeToggle();
        $(".labels_list li").focus();
        $(this).siblings().children(".drop_label").hide();
    });
    
    
    $(".labels_list li").hover(function(){
        $(this).children(".alltext").stop().toggle();
        $(this).siblings().children(".alltext").stop().hide();
    });
    
    $(".ac_upload").hover(function(){
    	if($(this).hasClass("invalid")){
    		return;
    	}
        $(this).find(".ac_downbox").stop().toggle()
    })
    
});
