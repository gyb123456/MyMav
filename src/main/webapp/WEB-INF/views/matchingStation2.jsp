<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title> </title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="plugins/bootstrap3/css/bootstrap.min.css" rel="stylesheet" type="text/css">
 	<link href="plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" type="text/css">
 	<link href="plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css">
 
  </head>
  
  <body>
  			匹配所有站点ID的页面
  		  <select id="stationId"></select>
 		<div id="toolbar" style="margin-left: 60px">
	        <button   class="btn btn-primary"  onclick="saveRel()">
	             匹配选中项<!-- <i class="glyphicon glyphicon-ok"></i> -->
	        </button>
		</div>
  		 <table 
  			id="table"
  		 	data-toggle="table"
	 		data-toolbar="#toolbar"
  		 	data-toolbar-align="left"
  		 	data-show-refresh="true"
			data-buttons-align="right"
  		 	data-url="user/findAllStationAndRoute"
  		 	data-method="get"
  		 	data-query-params="queryParam"
  		 	data-pagination="true"
  		 	data-page-size="50"
  		 	data-search-text=""
  		 	data-checkbox-header="false"
  		 	data-maintain-selected="true"
  		 ><!-- data-side-pagination="server" 
  			 data-pagination="true"
  		 	data-page-size="1000"
  		 	data-page-list=[1000]
  		 	
  		 	data-toolbar="#toolbar"
  		 	data-toolbar-align="right"
  		 	
  		 	data-response-handler="responseHandler"
  		 	-->
	  		 	<thead>
	  		 		<tr>
	  		 			<th data-formatter="numFormatter">序号</th>
	  		 			<th data-formatter="stateFormatter" data-checkbox="true" >选择</th>
	  		 			
	  		 			<th data-field="vo.stationId" >stationId</th>
	  		 			<th data-field="vo.stationName" >站点名</th>
	  		 			<th data-field="vo.routeId">routeId</th>
	  		 			<th data-field="vo.routeName">线路名</th>
	  		 			<th data-field="vo.dir" data-formatter="dirFormatter">方向</th>
	  		 			<th data-formatter="matchFormatter">已匹配</th>
	  		 			<th data-formatter="relFormatter">相关站点</th>
	  		 		</tr>
	  		 	</thead>
	  		 	
  		 </table>
  		 
  		 <!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog"  style="width: 800px;height: 500px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">
					相关站点
				</h4>
			</div>
			<div class="modal-body">
				
				<div>
					线路名：
					<select id="route" class="selectpicker" data-live-search="true">
					</select>
					方向：
					<select id="dir" class="selectpicker">
					</select>
				</div>
				<div> 
					站点：
					<select id="station" class="selectpicker" data-live-search="true">
					</select>
					<button  class="btn btn-primary" onclick="updateRel()" >添加</button>
				</div>
			
			
				<table 
		  			id="tableModel"
		  		 	data-toggle="table"
		  		 	data-url="user/findRoute"
		  		 	data-method="get"
		  		 	data-pagination="true"
		  		 	data-page-size="20"
		  		 >
		  		 <!--  
		  		 data-show-refresh="true"
	  		 	 data-buttons-align="left"
		  		 data-query-params="queryParamModel" 
		  		 -->
			  		 	<thead>
			  		 		<tr>
			  		 			<th data-field="stationName" data-valign="middle" data-align="center">站点名</th>
			  		 			<th data-field="routeName">线路名</th>
			  		 			<th data-field="dirStr">开往方向</th>
			  		 			<th data-field="firstStation" data-formatter="firstStationFormatter">首末站</th>
			  		 			<th data-field="routeId">routeId</th>
			  		 			<th data-field="stationId"  data-valign="middle" data-align="center">stationId</th>
			  		 			<th data-field="dir"  data-formatter="dirFormatter">方向</th>
			  		 			<th data-field="stationId" data-formatter="delFormatter">移除</th>
			  		 		</tr>
			  		 	</thead>
		  		 </table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary">
					提交更改
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal fade -->

  </body>
  
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap-table/bootstrap-table.min.js "></script>
<script type="text/javascript" src="plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="plugins/jquery-ui/jquery-ui.min.js">  </script><!-- 设置modal可拖拽的js -->
<script type="text/javascript" src="plugins/bootstrap-select/js/bootstrap-select.min.js">  </script>
<script type="text/javascript" src="plugins/bootstrap-select/js/language/defaults-zh_CN.min.js">  </script>
 
<script type="text/javascript" src="js/route/mactchingStation2.js">  </script>
</html>
