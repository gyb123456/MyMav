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
  			 展示页面
  		 <table 
  			id="table"
  		 	data-toggle="table"
  		 	data-show-refresh="true"
			data-buttons-align="right"
  		 	data-url="user/showAllStation"
  		 	data-method="get"
  		 	data-pagination="true"
  		 	data-page-size="50"
  		 	data-search-text=""
  		 >
	  		 	<thead>
	  		 		<tr>
	  		 			<th data-formatter="numFormatter">序号</th>
	  		 			<th data-field="groupId" >组</th>
	  		 			<th data-field="stationId" >stationId</th>
	  		 			<th data-field="stationName" >站点名</th>
	  		 			<th data-field="firstStation" data-formatter="firstStationFormatter">首末站</th>
	  		 			<th data-field="routeId">routeId</th>
	  		 			<th data-field="routeName">线路名</th>
	  		 			<th data-field="dirStr">方向</th>
	  		 		</tr>
	  		 	</thead>
	  		 	
  		 </table>
  </body>
  
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plugins/bootstrap-table/bootstrap-table.min.js "></script>
<script type="text/javascript" src="plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="js/route/mactchingStation1.js">  </script>
	
	
</html>
