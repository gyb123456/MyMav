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
 
  </head>
  
  <body>
  			匹配同名不同站点ID的页面
  		  <select id="stationId"></select>
 		<div id="toolbar" style="margin-left: 60px">
	        <button   class="btn btn-primary"  onclick="match()">
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
  		 	data-url="user/findSameStationAndRoute"
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
	  		 		</tr>
	  		 	</thead>
	  		 	
  		 </table>
  </body>
  
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap-table/bootstrap-table.min.js "></script>
<script type="text/javascript" src="plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="js/route/mactchingStation3.js">  </script>
	
	
</html>
