<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<%@ include file="/WEB-INF/view/include/header.jsp"%>
<link href="${staticpath}/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/style-metro.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/style.css" rel="stylesheet"
	type="text/css" />
<link href="${staticpath}/css/font-awesome.min.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="${plugins}/common/jquery/jquery-migrate-1.2.1.min.js"></script>
<script src="${plugins}/common/jquery/jquery-ui-1.10.1.custom.min.js"
	type="text/javascript"></script>
<script src="${plugins}/common/jquery/jquery.slimscroll.min.js"
	type="text/javascript"></script>
<script src="${plugins}/common/jquery/jquery.blockui.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${plugins}/common/jquery/jsapi.js"></script>
<script type="text/javascript" src="${plugins}/common/jquery/corechart.js"></script>		
<script type="text/javascript" src="${plugins}/common/jquery/jquery.gvChart-1.0.1.min.js"></script>
<script type="text/javascript" src="${plugins}/common/jquery/jquery.ba-resize.min.js"></script>
<body class="min_body_width">
<div class="box_main">
	<%@ include file="/WEB-INF/view/include/left.jsp"%>
	<div class="right_part">
		<div class="main_con main_con_an p-manage-outline">
			<div class="crumbs">
				<ul>
					<li><i class="tip_b"></i><a href=""><fmt:message
								key="overview" bundle="${i18n}" /></a></li>
				</ul>
			</div>

			<div class="scroll ui-scrollbar scroll-content"
				style="margin-bottom: 0px; margin-right: 0px; height: 100%;">
				<div class="for-charts-display">
					<div class="data fn-clear">
						<div class="employee-data-wrap">
							<div class="employee-data">
								<h2>我的空间文件统计</h2>
								<ul class="fn-clear">
									<li>
										<p>我的目录数量</p>
										<p class="data-num">${disk.folderNumber }</p> <a
										href="${ctx}/personal/index">查看</a>
									</li>
									<li>
										<p>我的文件数量</p>
										<p class="data-num">${disk.fileNumer }</p> <a
										href="${ctx}/personal/index">查看</a>
									</li>
									<li>
										<p>我的标签数量</p>
										<p class="data-num">${disk.signNumber }</p> <a
										href="${ctx}/sign/index">查看</a>
									</li>
									<li>
										<p>已设置的提取码数</p>
										<p class="data-num">${disk.takeFileNumber }</p> <a href="${ctx}/takecode/index"
										class="login-detail-today">查看</a> <!--<a href="#accountsecurity/loginlogs/=/type-today">查看</a>-->
									</li>
								</ul>
							</div>
						</div>

						<div class="safe-data fn-clear">
							<h2>他人共享文件统计</h2>
							<ul class="fn-clear">
								<li class="authdevice">
									<p>收到的目录数</p>
									<p class="data-num">${disk.receiveFolderNumber }</p> <a
									href="${ctx}/bshare/index">查看</a>
								</li>
								<li>
									<p>收到的文件数</p>
									<p class="data-num">${disk.receiveFileNumber }</p> <a
									href="${ctx}/bshare/index">查看</a>
								</li>
								
							</ul>
						</div>

					</div>

					<div class="account-use">
						<h2>空间用量</h2>
						<ul class="fn-clear">
							<li>
								<div class="detail">
									<em>分配情况</em> <i class="deli"></i> <span>个人空间</span>&nbsp;${personalSizeNum }，
									<span>协作空间${teamworkSizeNum }，</span> <span>总量：</span>
									<p>
										<span class="account-num detail-num">${fn:split(spaceSizeNum, " ")[0]} </span><span
											class="num-unit">${fn:split(spaceSizeNum, " ")[1]}</span>
									</p>

								</div>
								<div style="width: 300px; margin: 0 auto 0 112px;">

									<table id='myTable5'>
										<caption>空间分配情况</caption>
										<thead>
											<tr>
												<th></th>
												<th>个人空间</th>
												<th>协作空间</th>

											</tr>
										</thead>
										<tbody>
											<tr>
												<th>${spaceSizeB }</th>
												<td>${personalSizeB/spaceSizeB*100 <= 0.15 ? 0 :  personalSizeB}</td>
												<td>${teamworkSizeB/spaceSizeB*100 <= 0.15 ? 0 :  teamworkSizeB}</td>
											</tr>
										</tbody>
									</table>


								</div>
							</li>
							<li>
								<div class="detail">
									<em>个人空间统计</em> <i class="deli"></i> <span>已分配&nbsp;${personalSizeNum }，
									</span> <span>已用&nbsp;${personalUseSizeNum }，</span> <span>剩余：</span>
									<p>
										<span class="detail-num space-num">${fn:split(personalUsableSizeNum, " ")[0]}</span><span
											class="num-unit">${fn:split(personalUsableSizeNum, " ")[1]}</span>
									</p>
								</div>
								<div style="width: 300px; margin: 0 auto 0 112px;">

									<table id='myTable2'>
										<caption>个人空间统计</caption>
										<thead>
											<tr>
												<th></th>
												<th>已用</th>
												<th>剩余</th>

											</tr>
										</thead>
										<tbody>
											<tr>
												<th>${personalSizeB }</th>
												<td>${personalUseSize/personalSizeB*100 <= 0.15 ? 0 :  personalUseSize}</td>
												<td>${personalUsableSize/personalSizeB*100 <= 0.15 ? 0 :  personalUsableSize}</td>
											</tr>
										</tbody>
									</table>


								</div>
							</li>
							<%-- <li>
								<div class="detail">
									<em>协作空间统计</em> <i class="deli"></i> <span>购买&nbsp;10.00
										GB，</span> <span>已用&nbsp;9.00 MB，</span> <span>剩余可用：</span>
									<p>
										<span class="detail-num space-num">9.99</span><span
											class="num-unit">GB</span>
									</p>
								</div>
								<div style="width: 300px; margin: 0 10px 0 auto;">

									<table id='myTable1'>
										<caption>会员地区分布</caption>
										<thead>
											<tr>
												<th></th>
												<th>河北</th>
												<th>河南</th>
												<th>湖北</th>
												<th>湖南</th>
												<th>山东</th>
												<th>山西</th>

											</tr>
										</thead>
										<tbody>
											<tr>
												<th>1200</th>
												<td>540</td>
												<td>300</td>
												<td>150</td>
												<td>180</td>
												<td>120</td>
												<td>180</td>
											</tr>
										</tbody>
									</table>


								</div>
							</li> --%>
							<li>
								<div class="detail">
									<em>协作空间统计</em> <i class="deli"></i> <span>已分配&nbsp;${teamworkSizeNum }，
									</span> <span>已用&nbsp;${teamworkUseSizeNum }，</span> <span>剩余：</span>
									<p>
										<span class="detail-num work-num">${fn:split(teamworkUsableSizeNum, " ")[0]}</span><span
											class="num-unit">${fn:split(teamworkUsableSizeNum, " ")[1]}</span>
									</p>
								</div>
								<div style="width: 300px; padding-left:100px;">

									<table id='myTable1'>
										<caption>协作空间统计</caption>
										<thead>
											<tr>
												<th></th>
												<th>已用</th>
												<th>剩余</th>

											</tr>
										</thead>
										<tbody>
											<tr>
												<th>${teamworkSizeB }</th>
												<td>${teamworkUseSize/teamworkSizeB*100 <= 0.15 ? 0 :  teamworkUseSize}</td>
												<td>${teamworkUsableSize/teamworkSizeB*100 <= 0.15 ? 0 :  teamworkUsableSize}</td>
											</tr>
										</tbody>
									</table>


								</div>
							</li>
						</ul>
					</div>

				</div>
				<div class="others">
					<h2>其他</h2>
					<a class="message-btn" style=""
						href="javascript:;" onclick="javascript:window.location.href = '${ctx }/message/index';">${messageNum }
						条未读消息</a> 
					<a class="apply-btn" style=""
						href="javascript:;" onclick="javascript:window.location.href = '${ctx }/wgapply/index';">${workgroupApplyNum }
						个未审核工作组申请</a> 
					<a class="contact-btn" style=""
						href="javascript:;" onclick="javascript:window.location.href = '${ctx }/contact/index';">${contactNum }
						个联系人</a> 
					<a class="group-btn" style=""
						href="javascript:;" onclick="javascript:window.location.href = '${ctx }/workgroup/index';">${workgroupNum }
						个已加入的工作组</a> 
				</div>
			</div>


			<%-- <div class="zlbg zlbg_an">
                <div class="zlbox">
                	<div class="item">
                        <div class="part_top">
                            <div class="zl_tip_l01"></div>
                            <div class="zl_top_con">
                                <p class="text01"><fmt:message key="used_space" bundle="${i18n}" /></p>
                                <div class="jdtbox"><i style="width:<fmt:formatNumber type="number" value="${disk.diskSize / (personalSpaceSize*1024*1024) *100 }" pattern="0.00" maxFractionDigits="2"/>%"></i></div>
                                <p class="text02"><span class="fr"><fmt:formatNumber type="number" value="${disk.diskSize / (personalSpaceSize*1024*1024) *100}" pattern="0.00" maxFractionDigits="2"/>%
							</span><span>${disk.showDiskSize }/${personalSpaceNumber }</span></p>
                            </div>
                        </div>
                    </div>
                	 <div class="item item_margin">
                        <div class="part_top">
                            <div class="zl_tip_l02"></div>
                            <div class="zl_top_con">
                                <p class="text01"><fmt:message key="space_size" bundle="${i18n}" /></p>
                                <p class="text02 mt30"><span>${personalSpaceNumber }</span></p>
                            </div>
                        </div>
                    </div>
                    
                   
                </div>
            </div>
			
			
			
			
			 <div class="homecontent homecontent_an">
				<div class="row-fluid">

					<div class="span3 responsive" data-tablet="span6 "
						data-desktop="span3">

						<div class="dashboard-stat blue">

							<div class="visual visual_bg1">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.folderNumber }</div>

								<div class="desc">

									<fmt:message key="folder_num" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/personal/index"><fmt:message key="to_view" bundle="${i18n}" /><i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>



					<div class="span3 responsive" data-tablet="span6   "
						data-desktop="span3">

						<div class="dashboard-stat purple">

							<div class="visual visual_bg2">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.fileNumer }</div>

								<div class="desc">

									<fmt:message key="file_num" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/personal/index"><fmt:message key="to_view" bundle="${i18n}" /> <i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>

					<div class="span3 responsive" data-tablet="span6 fix-offset"
						data-desktop="span3">

						<div class="dashboard-stat yellow">

							<div class="visual visual_bg3">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.signNumber }</div>

								<div class="desc">

									<fmt:message key="tag_num" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/sign/index"><fmt:message key="to_view" bundle="${i18n}" /><i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>
				</div>

				<!--  -->
				<div class="row-fluid">
					<div class="span3 responsive" data-tablet="span6 fix-offset"
						data-desktop="span3">

						<div class="dashboard-stat green">

							<div class="visual visual_bg4">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.receiveFolderNumber }</div>

								<div class="desc">

									<fmt:message key="receive_folder" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/bshare/index"><fmt:message key="to_view" bundle="${i18n}" /><i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>
					
					<div class="span3 responsive" data-tablet="span6"
						data-desktop="span3">

						<div class="dashboard-stat reds">

							<div class="visual visual_bg5">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.receiveFileNumber }</div>

								<div class="desc">

									<fmt:message key="receive_file" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/bshare/index"><fmt:message key="to_view" bundle="${i18n}" /><i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>
					
					<div class="span3 responsive" data-tablet="span6 fix-offset"
						data-desktop="span3">

						<div class="dashboard-stat gray">

							<div class="visual visual_bg6">

								<i class="icon-comments"></i>

							</div>

							<div class="details">

								<div class="number">${disk.takeFileNumber }</div>

								<div class="desc">

									<fmt:message key="extract_file" bundle="${i18n}" />

								</div>

							</div>

							<a class="more" href="${ctx}/takecode/index"><fmt:message key="to_view" bundle="${i18n}" /><i
								class="m-icon-swapright m-icon-white"></i>

							</a>

						</div>

					</div>
				</div>
			</div>  --%>
			<%-- <div class="content">
                <div class="overview_box">
                    <ul>
                        <li class="bg_view01">
                            <i class="overview_01"></i>
                            <div class="txt color_v1"><p><span class="bold f40">${personalSpaceNumber }</span><!-- <span class="f20">G</span> --></p><p class="f14"><fmt:message key="space_size" bundle="${i18n}"/></p></div>
                        </li>
                        <li class="bg_view02">
                            <i class="overview_02"></i>
                            <div class="txt color_v2"><p><span class="bold f40">${disk.showDiskSize }</span><!-- <span class="f20">M</span> --></p><p class="f14"><fmt:message key="used_space" bundle="${i18n}"/></p></div>
                        </li>
                        <li class="bg_view03">
                            <i class="overview_03"></i>
                            <div class="txt color_v3"><p><span class="bold f40"><fmt:formatDate value="${disk.createTime}" pattern="yyyy-MM-dd" /></span></p><p class="f14"><fmt:message key="expiration" bundle="${i18n}"/></p></div>
                        </li>
                       	<li class="bg_view04">
                       	        <div class="overview_shadow"><a href="${ctx}/personal/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
	                            <i class="overview_04"></i>
	                            <div class="txt color_v4"><p><span class="bold f40">${disk.folderNumber }</span></p><p class="f14"><fmt:message key="folder_num" bundle="${i18n}"/></p></div>
	                    </li>
                        <li class="bg_view05">
                            <div class="overview_shadow"><a href="${ctx}/bshare/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
                            <i class="overview_05"></i>
                            <div class="txt color_v5"><p><span class="bold f40">${disk.receiveFolderNumber }</span></p><p class="f14"><fmt:message key="receive_folder" bundle="${i18n}"/></p></div>
                        </li>
                        <li class="bg_view06">
                            <div class="overview_shadow"><a href="${ctx}/sign/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
                            <i class="overview_06"></i>
                            <div class="txt color_v6"><p><span class="bold f40">${disk.signNumber }</span></p><p class="f14"><fmt:message key="tag_num" bundle="${i18n}"/></p></div>
                        </li>
	                    <li class="bg_view07">
                            <div class="overview_shadow"><a href="${ctx}/personal/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
                            <i class="overview_07"></i>
                            <div class="txt color_v7"><p><span class="bold f40">${disk.fileNumer }</span></p><p class="f14"><fmt:message key="file_num" bundle="${i18n}"/></p></div>
	                    </li>
                        <li class="bg_view08">
                            <div class="overview_shadow"><a href="${ctx}/takecode/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
                            <i class="overview_08"></i>
                            <div class="txt color_v8"><p><span class="bold f40">${disk.takeFileNumber }</span></p><p class="f14"><fmt:message key="extract_file" bundle="${i18n}"/></p></div>
                        </li>
                        <li class="bg_view09">
                            <div class="overview_shadow"><a href="${ctx}/bshare/index"><fmt:message key="to_view" bundle="${i18n}"/></a></div>
                            <i class="overview_09"></i>
                            <div class="txt color_v9"><p><span class="bold f40">${disk.receiveFileNumber }</span></p><p class="f14"><fmt:message key="receive_file" bundle="${i18n}"/></p></div>
                        </li>
                        <li class="bg_view10">
                            <i class="overview_10">
                                <div class="percentBox">
                                    <div id="bg1"></div> 
                                </div>
                            </i>
                            <div class="txt color_v10"><p><span class="bold f40" id="txt1"> <!-- 承载进度文字 --></span></p><p class="f14"><fmt:message key="space_usage" bundle="${i18n}"/></p></div>
                        </li>
                    </ul>
                </div>
            </div> --%>
		</div>

	</div>
</div>
</div>
</div><%-- 
<script type="text/javascript" src="${plugins}/raphael/raphael-min.js"></script>
<!-- 圆形进度条js -->
<script type="text/javascript">
	/*圆形百分比*/
	var paper = null;

	function init(b, n, t, c) {
		//初始化Raphael画布 
		this.paper = Raphael(b, 80, 80);

		//把底图先画上去 
		// this.paper.image("images/progressBg.jpg", 0, 0, 80, 80); 

		//进度比例，0到1，在本例中我们画65% 
		//需要注意，下面的算法不支持画100%，要按99.99%来画 
		var percent = n, drawPercent = percent >= 1 ? 0.9999 : percent;

		//开始计算各点的位置，见后图 
		//r1是内圆半径，r2是外圆半径 
		var r1 = 32, r2 = 40, PI = Math.PI, p1 = {
			x : 40,
			y : 80
		}, p4 = {
			x : p1.x,
			y : p1.y - r2 + r1
		}, p2 = {
			x : p1.x + r2 * Math.sin(2 * PI * (1 - drawPercent)),
			y : p1.y - r2 + r2 * Math.cos(2 * PI * (1 - drawPercent))
		}, p3 = {
			x : p4.x + r1 * Math.sin(2 * PI * (1 - drawPercent)),
			y : p4.y - r1 + r1 * Math.cos(2 * PI * (1 - drawPercent))
		}, path = [ 'M', p1.x, ' ', p1.y, 'A', r2, ' ', r2, ' 0 ',
				percent > 0.5 ? 1 : 0, ' 1 ', p2.x, ' ', p2.y, 'L', p3.x, ' ',
				p3.y, 'A', r1, ' ', r1, ' 0 ', percent > 0.5 ? 1 : 0, ' 0 ',
				p4.x, ' ', p4.y, 'Z' ].join('');

		//用path方法画图形，由两段圆弧和两条直线组成，画弧线的算法见后 
		this.paper.path(path)
		//填充渐变色，从#3f0b3f到#ff66ff 
		.attr({
			"stroke-width" : 0.5,
			"stroke" : "#d2d4d8",
			"fill" : "90-" + c
		});

		//显示进度文字 
		$(t).text(Math.round(percent * 100) + "%");
	}

	var percentage = parseInt('${disk.diskSize}')
			/ (parseInt('${personalSpaceSize }') * 1024 * 1024);
	percentage = percentage.toFixed(2);
	init('bg1', percentage, '#txt1', '#49829b');
</script> --%>


<script type="text/javascript" src="${staticpath}/js/home/home.js"></script>
<script>
	jQuery(document).ready(function() {
		App.init(); // initlayout and core plugins

	});
	gvChartInit();
	$(document).ready(function(){
		$('#myTable5').gvChart({
			chartType: 'PieChart',
			gvSettings: {
				vAxis: {title: 'No of players'},
				hAxis: {title: 'Month'},
				width: 300,
				height: 175
			}
		});
		$('#myTable1').gvChart({
			chartType: 'PieChart',
			gvSettings: {
				vAxis: {title: 'No of players'},
				hAxis: {title: 'Month'},
				width: 300,
				height: 175
			}
		});
		$('#myTable2').gvChart({
			chartType: 'PieChart',
			gvSettings: {
				vAxis: {title: 'No of players'},
				hAxis: {title: 'Month'},
				width: 300,
				height: 175
			}
		});
	});
</script>
</body>
</html>