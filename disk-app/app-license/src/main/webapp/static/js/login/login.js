
/*
*定义鼠标滚动事件
**/
(function(exports){
	exports.addEvent = (function(window, undefined) {        
        var _eventCompat = function(event) {
            var type = event.type;
            if (type == 'DOMMouseScroll' || type == 'mousewheel') {
                event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
            }
            //alert(event.delta);
            if (event.srcElement && !event.target) {
                event.target = event.srcElement;    
            }
            if (!event.preventDefault && event.returnValue !== undefined) {
                event.preventDefault = function() {
                    event.returnValue = false;
                };
            }
            /* 
               ......其他一些兼容性处理 */
            return event;
        };
        if (window.addEventListener) {
            return function(el, type, fn, capture) {
                if (type === "mousewheel" && document.mozHidden !== undefined) {
                    type = "DOMMouseScroll";
                }
                el.addEventListener(type, function(event) {
                    fn.call(this, _eventCompat(event));
                }, capture || false);
            }
        } else if (window.attachEvent) {
            return function(el, type, fn, capture) {
                el.attachEvent("on" + type, function(event) {
                    event = event || window.event;
                    fn.call(el, _eventCompat(event));    
                });
            }
        }
        return function() {};    
    })(exports); 
})(window);

/**
*页面滚动动画效果
*/
var LoginAnimate =  function(){
	var sum = $(".loginBgContainer .loginBg").size();
	var tag = $(".loginScroll");
	var scrollTag = $(".loginBgContainer").data("index",0);
	for(var i =0 ; i<sum ;i++){
		var a = $('<a class="scrollBtn'+(i==0? " act" : "")+'"></a>');
		a.click(function(){
			var index = $(this).index();
			$(this).addClass("act").siblings().removeClass("act");
			scrollTag.animate({top:-(index*100)+"%"},500,"swing",function(){
				$(this).data("index",index);
			});
		})
		tag.append(a);
	}
	addEvent(document, "mousewheel", function(event) {
		var index = scrollTag.data("index");
	    var nextIndex = event.delta > 0 ? index == 0 ? 0 : index-1 : index+1 >= sum ? index : index+1 ;
		if(nextIndex !=index){
			scrollTag.clearQueue().animate({top:-(nextIndex*100)+"%"},500,"swing",function(){
				$(this).data("index",nextIndex);
				tag.find(".scrollBtn").removeClass("act").eq(nextIndex).addClass("act");
			});
		}
	});
	/*
	-webkit-transition: 500ms ease;
  transition: 500ms ease;
  -webkit-transform: translate3d(0px, -100%, 0px);
  transform: translate3d(0px, -100%, 0px);*/
}

function imgchange(){
    $("#imgss").attr("src",basepath+"/check/image?a="+Math.random()+100);
}
