<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<c:set var="localvar" value="<%=com.yunip.config.LocalLanguageHelper.getLocalLanguage(request) %>"></c:set>
<!--引入JS-->
<script type="text/javascript" src="${staticpath }/js/flexPaper/flexpaper.js"></script>
<script type="text/javascript" src="${staticpath }/js/flexPaper/flexpaper_handlers.js"></script>
<title>${fileName }</title>
</head>
<body>
<div id="documentViewer" class="flexpaper_viewer" style="width:100%;height:100%"></div>

<script type="text/javascript">
    $('#documentViewer').FlexPaperViewer(
            { config : {
            	
            	jsDirectory : '${ctx}/static/js/flexPaper',//设置引入flash插件目录
            	SWFFile : escape('${ctx}${url}' + '&rd=' + Math.random()),
                Scale : 0.6,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : true,
                FitWidthOnLoad : true,
                FullScreenAsMaxWindow : false,
                ProgressiveLoading : false,
                MinZoomSize : 0.2,
                MaxZoomSize : 5,
                SearchMatchAll : false,
                InitViewMode : 'Portrait',
                RenderingOrder : 'flash',
                StartAtPage : '',
                PrintEnabled: false,//是否支持打印

                ViewModeToolsVisible : true,
                ZoomToolsVisible : true,
                NavToolsVisible : true,
                CursorToolsVisible : true,
                SearchToolsVisible : true,
                //WMode : 'window',
                <c:if test="${localvar=='zh_CN' }">
                	localeChain: 'zh_CN'
		    	</c:if>
		    	<c:if test="${localvar=='en_US' }">
		    		localeChain: 'en_US'
		    	</c:if>
            }}
    );
</script>
</body>
</html>