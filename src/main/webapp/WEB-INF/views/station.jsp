<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'station.jsp' starting page</title>
    
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
 
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="plugins/bootstrap3/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="plugins/bootstrap-table/bootstrap-table.min.js "></script>
	<script type="text/javascript" src="plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
	 
  </head>
  
  <body>
  		  <input id="stationId" type="text" placeholder="输入站点Id" value="74810003"/>
  		 <table 
  			id="table"
  		 	data-toggle="table"
  		 	data-url="user/findRoute"
  		 	data-method="get"
  		 	data-query-params="queryParam"
  		 	data-show-refresh="true"
  		 	data-buttons-align="left"
  		 	data-pagination="true"
  		 	data-page-size="500"
  		 ><!-- data-side-pagination="server" 
  			 data-pagination="true"
  		 	data-page-size="1000"
  		 	data-page-list=[1000]
  		 	-->
	  		 	<thead>
	  		 		<tr>
	  		 			<th data-field="stationName" data-valign="middle" data-align="center">站点名</th>
	  		 			<th data-field="routeName">线路名</th>
	  		 			<th data-field="dirStr">开往方向</th>
	  		 			<th data-field="firstStation" data-formatter="firstStationFormatter">首末站</th>
	  		 			<th data-field="routeId">routeId</th>
	  		 			<th data-field="stationId"  data-valign="middle" data-align="center">stationId</th>
	  		 			<th data-field="dir">方向</th>
	  		 		</tr>
	  		 	</thead>
	  		 	
  		 </table>
  		 <script type="text/javascript">
			queryParam	= function(params) {
				params.stationId=$('#stationId').val();
				return params;
			}
			function firstStationFormatter(value){
				if(!value){
					return "";
				}
				return "是";
			}
  		 </script>
  </body>
</html>
