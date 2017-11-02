<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/view/include/global.jsp"%>
<link rel="stylesheet" type="text/css" href="${plugins}/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${plugins}/jquery-easyui-1.5.1/themes/icon.css">
<script type="text/javascript" src="${plugins}/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="${plugins}/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<table id="tt" class="easyui-datagrid" style="width:600px;height:250px"
		url="${ctx }/workgroup/test"
		title="Load Data" iconCls="icon-save"
		rownumbers="true" pagination="true">
	<thead>
		<!-- 
		<tr>
			<th field="itemid" width="80">Item ID</th>
			<th field="productid" width="80">Product ID</th>
			<th field="listprice" width="80" align="right">List Price</th>
			<th field="unitcost" width="80" align="right">Unit Cost</th>
			<th field="attr1" width="150">Attribute</th>
			<th field="status" width="60" align="center">Stauts</th>
		</tr>
		 
		<tr>
				<th data-options="field:'id',width:80">ID</th>
				<th data-options="field:'productid',width:100">姓名</th>
				<th data-options="field:'listprice',width:80,align:'right'">部门</th>
				<th data-options="field:'unitcost',width:80,align:'right'">邮箱</th>
				<th data-options="field:'attr1',width:240">Attribute</th>
				<th data-options="field:'status',width:60,align:'center'">Status</th>
			</tr>-->
			
		<tr>
			<th data-options="field:'id',width:80">ID</th>
			<th data-options="field:'employeeName',width:100">姓名</th>
			<th data-options="field:'deptName',width:80,align:'right'">部门</th>
			<th data-options="field:'employeeEmail',width:80,align:'right'">邮箱</th>
		</tr>
	</thead>
</table>
<script type="text/javascript">
$(function(){
// 	var p = $('#tt').datagrid('getPager');
//     $(p).pagination({
//         pageSize: 2,//每页显示的记录条数，默认为10
//         pageList: [2, 4, 6],//可以设置每页记录条数的列表
//         beforePageText: '第',//页数文本框前显示的汉字
//         afterPageText: '页    共 {pages} 页',
//         displayMsg: '当前显示 {from}-{to} 条记录,共 {total} 条记录'
//     });
	
});
</script>

<!-- 
<table id="dg" title="Client Side Pagination" style="width:700px;height:300px" data-options="rownumbers:true,singleSelect:true,autoRowHeight:false,pagination:true,pageSize:10">
	<thead>
		<tr>
			<th field="inv" width="80">Inv No</th>
			<th field="date" width="100">Date</th>
			<th field="name" width="80">Name</th>
			<th field="amount" width="80" align="right">Amount</th>
			<th field="price" width="80" align="right">Price</th>
			<th field="cost" width="100" align="right">Cost</th>
			<th field="note" width="110">Note</th>
		</tr>
	</thead>
</table>
<script>
		(function($){
			function pagerFilter(data){
				if ($.isArray(data)){	// is array
					data = {
						total: data.length,
						rows: data
					}
				}
				var target = this;
				var dg = $(target);
				var state = dg.data('datagrid');
				var opts = dg.datagrid('options');
				if (!state.allRows){
					state.allRows = (data.rows);
				}
				if (!opts.remoteSort && opts.sortName){
					var names = opts.sortName.split(',');
					var orders = opts.sortOrder.split(',');
					state.allRows.sort(function(r1,r2){
						var r = 0;
						for(var i=0; i<names.length; i++){
							var sn = names[i];
							var so = orders[i];
							var col = $(target).datagrid('getColumnOption', sn);
							var sortFunc = col.sorter || function(a,b){
								return a==b ? 0 : (a>b?1:-1);
							};
							r = sortFunc(r1[sn], r2[sn]) * (so=='asc'?1:-1);
							if (r != 0){
								return r;
							}
						}
						return r;
					});
				}
				var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
				var end = start + parseInt(opts.pageSize);
				data.rows = state.allRows.slice(start, end);
				return data;
			}

			var loadDataMethod = $.fn.datagrid.methods.loadData;
			var deleteRowMethod = $.fn.datagrid.methods.deleteRow;
			$.extend($.fn.datagrid.methods, {
				clientPaging: function(jq){
					return jq.each(function(){
						var dg = $(this);
                        var state = dg.data('datagrid');
                        var opts = state.options;
                        opts.loadFilter = pagerFilter;
                        var onBeforeLoad = opts.onBeforeLoad;
                        opts.onBeforeLoad = function(param){
                            state.allRows = null;
                            return onBeforeLoad.call(this, param);
                        }
                        var pager = dg.datagrid('getPager');
						pager.pagination({
							onSelectPage:function(pageNum, pageSize){
								opts.pageNumber = pageNum;
								opts.pageSize = pageSize;
								pager.pagination('refresh',{
									pageNumber:pageNum,
									pageSize:pageSize
								});
								dg.datagrid('loadData',state.allRows);
							}
						});
                        $(this).datagrid('loadData', state.data);
                        if (opts.url){
                        	$(this).datagrid('reload');
                        }
					});
				},
                loadData: function(jq, data){
                    jq.each(function(){
                        $(this).data('datagrid').allRows = null;
                    });
                    return loadDataMethod.call($.fn.datagrid.methods, jq, data);
                },
                deleteRow: function(jq, index){
                	return jq.each(function(){
                		var row = $(this).datagrid('getRows')[index];
                		deleteRowMethod.call($.fn.datagrid.methods, $(this), index);
                		var state = $(this).data('datagrid');
                		if (state.options.loadFilter == pagerFilter){
                			for(var i=0; i<state.allRows.length; i++){
                				if (state.allRows[i] == row){
                					state.allRows.splice(i,1);
                					break;
                				}
                			}
                			$(this).datagrid('loadData', state.allRows);
                		}
                	});
                },
                getAllRows: function(jq){
                	return jq.data('datagrid').allRows;
                }
			})
		})(jQuery);

		function getData(){
			var rows = [];
			for(var i=1; i<=800; i++){
				var amount = Math.floor(Math.random()*1000);
				var price = Math.floor(Math.random()*1000);
				rows.push({
					inv: 'Inv No '+i,
					date: $.fn.datebox.defaults.formatter(new Date()),
					name: 'Name '+i,
					amount: amount,
					price: price,
					cost: amount*price,
					note: 'Note '+i
				});
			}
			return rows;
		}
		
		$(function(){
			$('#dg').datagrid({data:getData()}).datagrid('clientPaging');
		});
	</script>
 -->