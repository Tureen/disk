<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="plugins" value="${pageContext.request.contextPath}/plugins"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; ">
<title>Insert title here</title>
<link href="${plugins}/bootstrap/bootstrap-tour-standalone.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${plugins}/common/jquery/jquery.min-1.8.3.js"></script><!--JQ库文件1.8.3以上版本支持live-->
<script type="text/javascript" src="${plugins}/bootstrap/bootstrap-tour-standalone.min.js"></script>
</head>
<script type="text/javascript">
$(function(){
	$("[data-toggle='popover']").popover();
	/* // Instance the tour
	var tour = new Tour({
	  steps: [
	  {
	    element: "#my-element",
	    title: "Title of my step",
	    content: "Content of my step"
	  },
	  {
	    element: "#my-other-element",
	    title: "Title of my step",
	    content: "Content of my step"
	  }
	]}); */

	var storage;
    try {
      storage = false;
    } catch (_error) {
      storage = window.localStorage;
    }
	var tour = new Tour({
	  name:"tour1",
	  container: "body",
	  smartPlacement:true,
	  keyboard:false,
	  debug:false, 
	  /* backdrop:true,
	  backdropContainer:"body",
	  redirect:function(){
		  document.location.href="www.baidu.com"
	  }, */
	  orphan:false,
	  duration:false,
	  delay:false,
	  template:"<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><div class='popover-navigation'><button class='btn btn-default' data-role='prev'>« 上一条</button><span data-role='separator'></span><button class='btn btn-default' data-role='next'>下一条 »</button><button class='btn btn-default' data-role='end'>结束引导</button></div></div>",
	  /*template:'<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-title"></h3> <div class="popover-content"></div> <div class="popover-navigation"> <div class="btn-group"> <button class="btn btn-sm btn-default" data-role="prev">&laquo; Prev</button> <button class="btn btn-sm btn-default" data-role="next">Next &raquo;</button> <button class="btn btn-sm btn-default" data-role="pause-resume" data-pause-text="Pause" data-resume-text="Resume">Pause</button> </div> <button class="btn btn-sm btn-default" data-role="end">End tour</button> </div> </div>',*/
	  storage: storage, //相关可选项目值：window.localStorage(缺省), window.sessionStorage ，false　或者自定义obj
	  steps:[
	   /*  {element:'#btn1',title:'按钮一',content:'网格模式',orphan: true,path: "/disk/home/index",}, */
	    {element:'#btn1',title:'按钮1',content:'列表模式',orphan: true},
	    {element:'#btn1',title:'按钮2',content:'列表模式',orphan: true},
	    {element:'#btn1',title:'按钮3',content:'列表模式',orphan: true},
	    {element:'#btn1',title:'按钮4',content:'列表模式',orphan: true},
	    {element:'#btn1',title:'按钮5',content:'列表模式',orphan: true}
	  ],
	  onShow: function (tour) {
		  var i = tour.getCurrentStep;
		  console.log(i);
		  },
		  /* onNext: function (tour) {console.log(tour._current);$("#on").click()},
		  onPrev: function (tour) {console.log(tour._current);}, */
	  /* afterGetState: function (key, value) {console.log("key:"+key);console.log("value:"+value)},
	  afterSetState: function (key, value) {console.log("afterSetState")},
	  afterRemoveState: function (key, value) {console.log("afterRemoveState")},
	  onStart: function (tour) {console.log("onStart")},
	  onEnd: function (tour) {console.log("onEnd")},
	  
	  onShown: function (tour) {console.log("onShown")},
	  onHide: function (tour) {console.log("onHide")},
	  onHidden: function (tour) {console.log("onHidden")},
	  
	 
	  onPause: function (tour, duration) {console.log("onPause")},
	  onResume: function (tour, duration) {console.log("onResume")},
	  onRedirectError: function (tour) {console.log("onRedirectError")} */
	});
	// Initialize the tour
	tour.init();

	// Start the tour
	tour.start();
	
});

$(document).on("click", "[data-toggle='popover']", function(e) {
	//alert(1)
});

</script>
<body>
 <button class="btn btn-primary btn-lg" >
    <i class="glyphicon glyphicon-th-large"></i>
  </button>
  
  
  <div class="container" style="padding: 100px 50px 10px;" >
    <button  type="button" class="btn btn-default" title="Popover title"
            data-container="body" data-toggle="popover" data-placement="left"
            data-content="左侧的 Popover 中的一些内容"  data-placement="bottom">
        左侧的 Popover
    </button>
    <button type="button" class="btn btn-primary" title="Popover title"
            data-container="body" data-toggle="popover" data-placement="top"
            data-content="顶部的 Popover 中的一些内容">
        顶部的 Popover
    </button>
    <button type="button" class="btn btn-success" title="Popover title"
            data-container="body" data-toggle="popover" data-placement="bottom"
            data-content="底部的 Popover 中的一些内容">
        底部的 Popover
    </button>
   	<button id="on" type="button" class="btn btn-warning" title="Popover title"
            data-container="body" data-toggle="popover" data-placement="right"
            data-content="右侧的 Popover 中的一些内容">
        右侧的 Popover
    </button>
    <!-- <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br> -->
    <button class="btn btn-primary btn-lg "  id="btn2" >
    <i class="glyphicon glyphicon-th-list"></i>
  </button>
    <button class="btn btn-primary btn-lg "  id="btn3" >
    <i class="glyphicon glyphicon-th-list"></i>
  </button>
    <button class="btn btn-primary btn-lg "  id="btn4" >
    <i class="glyphicon glyphicon-th-list"></i>
  </button>
    <button class="btn btn-primary btn-lg "  id="btn5" >
    <i class="glyphicon glyphicon-th-list"></i>
  </button>
    <button class="btn btn-primary btn-lg "  id="btn6" >
    <i class="glyphicon glyphicon-th-list"></i>
  </button>
  <div id="btn1" style="width : 10px;
    height : 10px;
    position : absolute;
    left : 50%;
    top : 50%;
    margin-left : -5px; /*一半的高度*/
    margin-top : -5px;  /*一半的宽度*/">123</div>
</div>
</html>